package castor.clients;

import castor.ddlindextract.DDLIndMain;
import castor.modetransform.ModeGenerator;
import castor.modetransform.TransformMain;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

/**
 * PreprocessInput can be used for two tasks: 1) DDLIndExtraction : Extracts IND
 * from DDL and also outputs invalid pks parameters required: ddl 2)
 * ModeTransformation: For given input modes and transformation schema ->
 * generate modes for transformed schema parameters required: dataModelFile,
 * transformSchema
 */
public class PreprocessClient {
	// DDLIndExtraction
	@Option(name = "-ddl", usage = "Schema file.", required = false, handler = StringArrayOptionHandler.class)
	private String[] ddlPath = null;

	@Option(name = "-outputInds", usage = "Output file containing extracted inclusion dependencies in JSON format.", required = false, handler = StringArrayOptionHandler.class)
	private String[] outputIndFilePath = null;

	// ModeTransformation using transformation
	@Option(name = "-dataModel", usage = "Data model file.", required = false, handler = StringArrayOptionHandler.class)
	private String[] dataModelFilePath = null;

	@Option(name = "-transformation", usage = "Transformation file.", required = false, handler = StringArrayOptionHandler.class)
	private String[] transformSchemaPath = null;

	@Option(name = "-outputModes", usage = "Output file containing generated modes.", required = false, handler = StringArrayOptionHandler.class)
	private String[] outputModesFilePath = null;

	// ModeGeneration using metadata
	// Also uses -ddl and -outputModes
	@Option(name = "-metadata", usage = "Metadata file.", required = false, handler = StringArrayOptionHandler.class)
	private String[] metadataFilePath = null;
	
	@Option(name = "-target", usage = "Name of target relation.", required = false, handler=StringArrayOptionHandler.class)
	private String target = null;
	
	@Option(name = "-examplesFile", usage = "File containing examples, used to read the header only (attribute names).", required = false, handler=StringArrayOptionHandler.class)
	private String examplesFilePath[] = null;

	final static Logger logger = Logger.getLogger(PreprocessClient.class);

	public static void main(String[] args) {
		PreprocessClient client = new PreprocessClient();
		client.process(args);
	}

	public void process(String[] args) {
		// Parse the arguments
		CmdLineParser parser = new CmdLineParser(this);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			logger.error(e.getMessage());
			parser.printUsage(System.out);
			return;
		}

		// DDLIndExtraction
		if (ddlPath != null && outputIndFilePath != null) {
			logger.info("Extracting INDs from DDL file...");
			String ddl = getOption(ddlPath);
			String outputIndFile = getOption(outputIndFilePath);
			boolean indResult = DDLIndMain.extractIndFromDDL(ddl, outputIndFile);
			if (indResult == false) {
				logger.error("Error while extracting INDs from DDL.");
			}
		}

		// ModeTransformation using transformation
		if (dataModelFilePath != null && transformSchemaPath != null && outputModesFilePath != null) {
			logger.info("Transforming modes using transformation...");
			String dataModelFile = getOption(dataModelFilePath);
			String transformSchema = getOption(transformSchemaPath);
			String outputModesFile = getOption(outputModesFilePath);
			boolean modeResult = TransformMain.generateModesUsingTransformation(dataModelFile, transformSchema, outputModesFile);
			if (modeResult == false) {
				logger.error("Error while transformating modes using transformation.");
			}
		}
		
		// ModeGeneration using metadata
		if (ddlPath != null && metadataFilePath != null && target != null && examplesFilePath != null && outputModesFilePath != null) {
			logger.info("Generating modes using metadata...");
			String ddlFile = getOption(ddlPath);
			String examplesFile = getOption(examplesFilePath);
			String metadataFile = getOption(metadataFilePath);
			String outputModesFile = getOption(outputModesFilePath);
			
			ModeGenerator.generateModesFromMetadata(ddlFile, target, examplesFile, metadataFile, "CastorProcedure", outputModesFile);
		}
	}

	/*
	 * Get an option string from multiple strings
	 */
	private String getOption(String[] option) {
		StringBuilder cmd = new StringBuilder();
		for (int i = 0; i < option.length; i++) {
			cmd.append(option[i]);
			cmd.append(" ");
		}
		return cmd.toString().trim().replaceAll("^\"|\"$", "");
	}
}
