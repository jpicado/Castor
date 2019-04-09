package castor.algorithms.bottomclause;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import castor.db.DBCommons;
import castor.language.IdentifierType;
import castor.language.InclusionDependency;
import castor.language.Mode;
import castor.language.Schema;
import castor.utils.Commons;

public class StoredProcedureGeneratorSaturationInsideSP {

	private static final String VOLTDB_HOME_VARIABLE = "VOLTDB_HOME";

	private static final String TEMP_FOLDER = "spsTemp";
	private static final String CATALOG = TEMP_FOLDER + "/autocatalog.jar";
	private static final String AUTO_PACKAGE = "auto";
	private static final String SP_GENERATION_LOCATION = TEMP_FOLDER + "/src/" + AUTO_PACKAGE;
	private static final String SP_COMPILED_LOCATION = TEMP_FOLDER + "/bin";

	// Template names and argument names
	private static final String TEMPLATE_LOCATION = "res/spTemplate.stg";
	private static final String SP_BOTTOMCLAUSE_SQLSTATEMENT_TEMPLATE = "spBottomClauseSqlStatementTemplate";
	private static final String SP_BOTTOMCLAUSE_MODE_TEMPLATE = "spBottomClauseModeTemplate";
	private static final String SP_BOTTOMCLAUSE_INCLUSIONDEPENDENCY_TEMPLATE = "spBottomClauseInclusionDependencyTemplate";
	private static final String SP_BOTTOMCLAUSE_PROCEDURE_TEMPLATE = "spBottomClauseProcedureTemplate";
	private static final String SP_BOTTOMCLAUSE_GROUPEDMODES_TEMPLATE = "spNewListeralsForGroupedModesClear";
	private static final String RELATION_ARG_NAME = "relation";
	private static final String ATTRIBUTE_ARG_NAME = "attribute";
	private static final String ATTRIBUTENUMBER_ARG_NAME = "attributeNumber";
	private static final String QUERY_LIMIT = "queryLimit";
	private static final String MODEBSTRING_ARG_NAME = "modeBString";
	private static final String PACKAGE_ARG_NAME = "package";
	private static final String NAME_ARG_NAME = "name";
	private static final String VARIABLE_PREFIX_ARG_NAME = "variablePrefix";
	private static final String SQLSTATEMENTS_ARG_NAME = "sqlStatements";
	private static final String MODEHSTRING_ARG_NAME = "modeHString";
	private static final String MODEBOPERATIONS_ARG_NAME = "modesBOperations";
	private static final String LEFTINDPREDICATE_ARG_NAME = "leftIndPredicate";
	private static final String LEFTINDATTNUMBER_ARG_NAME = "leftIndAttNumber";
	private static final String RIGHTINDPREDICATE_ARG_NAME = "rightIndPredicate";
	private static final String RIGHTINDATTNUMBER_ARG_NAME = "rightIndAttNumber";
	private static final String MODESFORRIGHTINDPREDICATE_ARG_NAME = "modesForRightIndPredicate";

	private List<String> procedures = new ArrayList<String>();

	public boolean generateAndCompileStoredProcedures(String dbURL, String port, String dataset, String spName,
			int iterations, Schema schema, Mode modeH, List<Mode> modesB, boolean applyInds, int queryLimit) throws Exception {

		// Generate stored procedures
		// Bottom clause
		generateStoredProcedure(dataset, spName, iterations, schema, modeH, modesB, applyInds, false, queryLimit);
		// Ground bottom clause
		generateStoredProcedure(dataset, spName, iterations, schema, modeH, modesB, applyInds, true, queryLimit);

		// Compile stored procedures and generate catalog
		boolean success = compileStoredProcedures(dataset);

		// Load stored procedures to database
		if (success) {
			loadProceduresToDB(dbURL, port);
			
			// Delete temp folder only if there was success
			removeTempFolder();
		}

		return success;
	}

	private void generateStoredProcedure(String dataset, String spNameTemplate, int iterations, Schema schema,
			Mode modeH, List<Mode> modesB, boolean applyInds, boolean ground, int queryLimit) throws Exception {

		// Create folder where stored procedures will be written
		new File(SP_GENERATION_LOCATION + File.separator + dataset).mkdirs();

		// Load file that contains template for stored procedures
		final STGroup stGroup = new STGroupFile(TEMPLATE_LOCATION, '$', '$');

		// Group modes by predicate name
		Map<String, String> groupedModesStrings = new HashMap<String, String>();
		Map<String, List<Mode>> groupedModes = new LinkedHashMap<String, List<Mode>>();
		for (Mode mode : modesB) {
			String modeString;
			if (ground) {
				modeString = mode.toGroundModeString();
			} else {
				modeString = mode.toString();
			}

			// Add to grouped modes
			if (!groupedModesStrings.containsKey(mode.getPredicateName())) {
				groupedModesStrings.put(mode.getPredicateName(), modeString);
			} else if (!groupedModesStrings.get(mode.getPredicateName()).contains(modeString)) {
				groupedModesStrings.put(mode.getPredicateName(),
						groupedModesStrings.get(mode.getPredicateName()) + ";" + modeString);
			}

			if (!groupedModes.containsKey(mode.getPredicateName())) {
				groupedModes.put(mode.getPredicateName(), new LinkedList<Mode>());
			}
			groupedModes.get(mode.getPredicateName()).add(mode);
		}

		// Create SQL statements and mode operations
		StringBuilder sqlStatementsBuilder = new StringBuilder();
		StringBuilder modesOperationsBuilder = new StringBuilder();
		Set<String> seenPredicatesAttributes = new HashSet<String>();

		Iterator<Map.Entry<String, List<Mode>>> it = groupedModes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<Mode>> pair = it.next();
			String modePredicate = pair.getKey();

			final ST groupedModesLiteralListTemplate = stGroup.getInstanceOf(SP_BOTTOMCLAUSE_GROUPEDMODES_TEMPLATE);
			modesOperationsBuilder.append(groupedModesLiteralListTemplate.render() + "\n");

			for (Mode mode : pair.getValue()) {
				String modeString;
				if (ground) {
					modeString = mode.toGroundModeString();
				} else {
					modeString = mode.toString();
				}

				// Create SQL statements

				// Find input attribute
				// TODO currently handling one single input
				int attributeNumber;
				for (attributeNumber = 0; attributeNumber < mode.getArguments().size(); attributeNumber++) {
					if (mode.getArguments().get(attributeNumber).getIdentifierType().equals(IdentifierType.INPUT)) {
						break;
					}
				}

				// Check if already saw another mode with the same predicate name and attribute
				// number
				// This is done to avoid creating sqlStmt with same names
				String modeAttributeId = mode.getPredicateName() + "_" + attributeNumber;
				if (!seenPredicatesAttributes.contains(modeAttributeId)) {
					seenPredicatesAttributes.add(modeAttributeId);

					// Get attribute name
					String attribute = schema.getRelations().get(mode.getPredicateName().toUpperCase())
							.getAttributeNames().get(attributeNumber);

					// Create SQL statement from template
					final ST sqlStatementTemplate = stGroup.getInstanceOf(SP_BOTTOMCLAUSE_SQLSTATEMENT_TEMPLATE);
					sqlStatementTemplate.add(RELATION_ARG_NAME, mode.getPredicateName());
					sqlStatementTemplate.add(ATTRIBUTE_ARG_NAME, attribute);
					sqlStatementTemplate.add(ATTRIBUTENUMBER_ARG_NAME, attributeNumber);
					sqlStatementTemplate.add(QUERY_LIMIT, queryLimit);

					sqlStatementsBuilder.append(sqlStatementTemplate.render() + "\n");
				}

				// Create operations for mode from template
				final ST modeTemplate = stGroup.getInstanceOf(SP_BOTTOMCLAUSE_MODE_TEMPLATE);
				modeTemplate.add(RELATION_ARG_NAME, mode.getPredicateName());
				modeTemplate.add(MODEBSTRING_ARG_NAME, modeString);
				modeTemplate.add(ATTRIBUTENUMBER_ARG_NAME, attributeNumber);

				modesOperationsBuilder.append(modeTemplate.render() + "\n");
			}

			// Apply INDs
			if (applyInds) {
				modesOperationsBuilder.append(
						followIndChain(schema, modePredicate, new HashSet<String>(), groupedModesStrings, stGroup));
			}
		}

		// Create stored procedure from template
		String packageName = AUTO_PACKAGE + "." + dataset;
		String spName = spNameTemplate;
		if (ground) {
			spName += DBCommons.GROUND_BOTTONCLAUSE_PROCEDURE_SUFFIX;
		}

		String modeHString;
		if (ground) {
			modeHString = modeH.toGroundModeString();
		} else {
			modeHString = modeH.toString();
		}

		final ST storedProcedureTemplate = stGroup.getInstanceOf(SP_BOTTOMCLAUSE_PROCEDURE_TEMPLATE);
		storedProcedureTemplate.add(PACKAGE_ARG_NAME, packageName);
		storedProcedureTemplate.add(NAME_ARG_NAME, spName);
		storedProcedureTemplate.add(VARIABLE_PREFIX_ARG_NAME, Commons.VARIABLE_PREFIX);
		storedProcedureTemplate.add(SQLSTATEMENTS_ARG_NAME, sqlStatementsBuilder.toString());
		storedProcedureTemplate.add(MODEHSTRING_ARG_NAME, modeHString);
		storedProcedureTemplate.add(MODEBOPERATIONS_ARG_NAME, modesOperationsBuilder.toString());

		// Render to files
		String spFile = SP_GENERATION_LOCATION + File.separator + dataset + File.separator + spName + ".java";
		PrintWriter spWriter = new PrintWriter(spFile, "UTF-8");
		spWriter.println(storedProcedureTemplate.render());
		spWriter.close();

		this.procedures.add(packageName + "." + spName);
	}
	
	private String followIndChain(Schema schema, String currentPredicate, Set<String> seenPredicates,
			Map<String, String> groupedModesStrings, STGroup stGroup) {
		StringBuilder sb = new StringBuilder();

		if (!seenPredicates.contains(currentPredicate)
				&& schema.getInclusionDependencies() != null
				&& schema.getInclusionDependencies().containsKey(currentPredicate)) {
			for (InclusionDependency ind : schema.getInclusionDependencies().get(currentPredicate)) {

				if (!seenPredicates.contains(ind.getRightPredicateName())) {
					// Add application of IND
					final ST indTemplate = stGroup.getInstanceOf(SP_BOTTOMCLAUSE_INCLUSIONDEPENDENCY_TEMPLATE);
					indTemplate.add(LEFTINDPREDICATE_ARG_NAME, ind.getLeftPredicateName());
					indTemplate.add(LEFTINDATTNUMBER_ARG_NAME, ind.getLeftAttributeNumber());
					indTemplate.add(RIGHTINDPREDICATE_ARG_NAME, ind.getRightPredicateName());
					indTemplate.add(RIGHTINDATTNUMBER_ARG_NAME, ind.getRightAttributeNumber());
					indTemplate.add(MODESFORRIGHTINDPREDICATE_ARG_NAME,
							groupedModesStrings.get(ind.getRightPredicateName()));

					sb.append(indTemplate.render() + "\n");

					// Add current predicate to seen list
					seenPredicates.add(currentPredicate);

					// Follow chain
					sb.append(followIndChain(schema, ind.getRightPredicateName(), seenPredicates, groupedModesStrings,
							stGroup));
				}
			}
		}

		return sb.toString();
	}

	private boolean compileStoredProcedures(String dataset) {
		// Compilation requirements
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

		// Get VoltDB home location
		String voltDBHome = System.getenv(VOLTDB_HOME_VARIABLE);
		// String voltDBHome = VOLTDB_HOME;
		if (voltDBHome == null || voltDBHome.isEmpty()) {
			throw new RuntimeException(
					"VoltDB not found in location given by environment variable " + VOLTDB_HOME_VARIABLE + ".");
		}

		// Setup class path and output location
		List<String> optionList = new ArrayList<String>();
		optionList.add("-classpath");
		// optionList.add(System.getProperty("java.class.path") + ":./voltdb.jar" + ":"
		// + voltDBHome + "/lib/*:" + voltDBHome + "/voltdb/*");
		optionList.add(System.getProperty("java.class.path") + ":./voltdb.jar");

		File spCompiledLocation = new File(SP_COMPILED_LOCATION);
		spCompiledLocation.mkdirs();
		optionList.add("-d");
		optionList.add(SP_COMPILED_LOCATION);

		File spLocationFolder = new File(SP_GENERATION_LOCATION + File.separator + dataset);
		File[] listOfFiles = spLocationFolder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".java");
			}
		});

		Iterable<? extends JavaFileObject> compilationUnit = fileManager
				.getJavaFileObjectsFromFiles(Arrays.asList(listOfFiles));
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, optionList, null,
				compilationUnit);

		boolean success = true;

		// Compile
		if (task.call()) {
			// Create catalog jar
			// createJar(spCompiledLocation); //this is not working, currently calling
			// command jar
			// Assumes jar is installed in computer. Would be better to create jar
			// programmatically
			try {
				Runtime.getRuntime().exec("jar cvf " + CATALOG + " -C " + SP_COMPILED_LOCATION + "/ .");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
				System.out.format("Error on line %d in %s%n", diagnostic.getLineNumber(),
						diagnostic.getSource().toUri());

				System.out.println(diagnostic.getMessage(Locale.ENGLISH));
			}
			success = false;
		}

		return success;
	}

	private void loadProceduresToDB(String dbURL, String port) throws Exception {
		String line;

		// Get VoltDB home location
		String voltDBHome = System.getenv(VOLTDB_HOME_VARIABLE);
		// String voltDBHome = VOLTDB_HOME;
		if (voltDBHome.isEmpty()) {
			throw new Exception("VoltDB not found in location given by environment variable " + VOLTDB_HOME_VARIABLE);
		}

		// Launch sqlcmd
		Process process = Runtime.getRuntime()
				.exec(voltDBHome + "/bin/sqlcmd --stop-on-error=false --servers=" + dbURL + " --port=" + port);
		OutputStream stdin = process.getOutputStream();
		InputStream stderr = process.getErrorStream();
		InputStream stdout = process.getInputStream();

		// Load classes
		line = "load classes " + CATALOG + ";\n";
		stdin.write(line.getBytes());
		stdin.flush();

		// Create procedures from classes
		for (String procedure : this.procedures) {
			// Drop procedure
			line = "drop procedure " + procedure + " if exists;\n";
			stdin.write(line.getBytes());
			stdin.flush();

			// Create procedure
			line = "create procedure from class " + procedure + ";\n";
			stdin.write(line.getBytes());
			stdin.flush();
		}

		stdin.close();

		// Clean up if any output in stdout
		BufferedReader brCleanUp = new BufferedReader(new InputStreamReader(stdout));
		while ((line = brCleanUp.readLine()) != null) {
			System.out.println("[sqlcmd stdout] " + line);
		}
		brCleanUp.close();

		// Clean up if any output in stderr
		brCleanUp = new BufferedReader(new InputStreamReader(stderr));
		while ((line = brCleanUp.readLine()) != null) {
			System.out.println("[sqlcmd stderr] " + line);
		}
		brCleanUp.close();
	}
	
	private void removeTempFolder() {
		try {
			FileUtils.deleteDirectory(new File(TEMP_FOLDER));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
