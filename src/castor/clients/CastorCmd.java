package castor.clients;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import com.google.gson.JsonObject;

import castor.algorithms.CastorLearner;
import castor.algorithms.Golem;
import castor.algorithms.Learner;
import castor.algorithms.ProGolem;
import castor.algorithms.bottomclause.BottomClauseGenerator;
import castor.algorithms.bottomclause.BottomClauseGeneratorInsideSP;
import castor.algorithms.bottomclause.BottomClauseGeneratorNaiveSampling;
import castor.algorithms.bottomclause.BottomClauseUtil;
import castor.algorithms.bottomclause.StoredProcedureGeneratorSaturationInsideSP;
import castor.algorithms.coverageengines.CoverageBySubsumptionParallel;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.DAOFactory;
import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.file.CSVFileReader;
import castor.db.DBCommons;
import castor.db.QueryGenerator;
import castor.ddlindextract.DDLIndMain;
import castor.hypotheses.ClauseInfo;
import castor.language.InclusionDependency;
import castor.language.MatchingDependency;
import castor.language.Mode;
import castor.language.Relation;
import castor.language.Schema;
import castor.mappings.MyClauseToClauseAsString;
import castor.settings.DataModel;
import castor.settings.JsonSettingsReader;
import castor.settings.Parameters;
import castor.settings.SamplingMethods;
import castor.utils.FileUtils;
import castor.utils.Formatter;
import castor.utils.NumbersKeeper;
import castor.utils.Pair;
import castor.utils.TimeWatch;
import castor.wrappers.EvaluationResult;
import castor.wrappers.LearningResult;

public class CastorCmd {

	public static final String ALGORITHM_CASTOR = "Castor";
	public static final String ALGORITHM_PROGOLEM = "ProGolem";
	public static final String ALGORITHM_GOLEM = "Golem";

	// Options
	@Option(name = "-h", aliases = { "--help" })
	private boolean help = false;

	@Option(name = "-parameters", usage = "Parameters file.", required = true, handler=StringArrayOptionHandler.class)
	private String[] parametersFilePath;

	@Option(name = "-schema", usage = "Schema file (if not provided, schema is extracted from DB).", required = false, handler=StringArrayOptionHandler.class)
	private String[] schemaFilePath = null;

	@Option(name = "-inds", usage = "INDs file.", required = false, handler=StringArrayOptionHandler.class)
	private String[] indsFilePath = null;

	@Option(name = "-dataModel", usage = "Data model file.", required = true, handler=StringArrayOptionHandler.class)
	private String[] dataModelFilePath;
	
	@Option(name = "-ddl", usage = "DDL file, used to extract INDs.", required = false, handler=StringArrayOptionHandler.class)
	private String[] ddlFilePath = null;

	@Option(name = "-sat", usage = "Only build bottom clause for example given in parameter e.")
	private boolean saturation = false;

	@Option(name = "-groundsat", usage = "Only build ground bottom clause for example given in parameter e.")
	private boolean groundSaturation = false;

	@Option(name = "-e", usage = "Example to build bottom clause for (position of tuple in table; only when using sat or groundsat parameters).")
	private int exampleForSaturation = 0;

	@Option(name = "-algorithm", usage = "Algorithm to run (Castor, Golem, ProGolem).", required = false)
	private String algorithm = ALGORITHM_CASTOR;

	@Option(name = "-trainPosSuffix", usage = "Suffix for table containing training positive examples.", required = false)
	private String trainPosSuffix = DBCommons.TRAIN_POS_SUFFIX;

	@Option(name = "-trainNegSuffix", usage = "Suffix for table containing training negative examples.", required = false)
	private String trainNegSuffix = DBCommons.TRAIN_NEG_SUFFIX;

	@Option(name = "-testPosSuffix", usage = "Suffix for table containing testing positive examples.", required = false)
	private String testPosSuffix = DBCommons.TEST_POS_SUFFIX;

	@Option(name = "-testNegSuffix", usage = "Suffix for table containing testing negative examples.", required = false)
	private String testNegSuffix = DBCommons.TEST_NEG_SUFFIX;

	@Option(name = "-test", usage = "Evaluate learned definition on testing data.")
	private boolean testLearnedDefinition = false;

	@Option(name = "-outputSQL", usage = "Output the learned definition in SQL format.")
	private boolean outputSQL = false;
	
	@Option(name = "-posTrainExamplesFile", usage = "File containing positive training examples.", required = false, handler=StringArrayOptionHandler.class)
	private String posTrainExamplesFilePath[] = null;
	
	@Option(name = "-negTrainExamplesFile", usage = "File containing negative training examples.", required = false, handler=StringArrayOptionHandler.class)
	private String negTrainExamplesFilePath[] = null;
	
	@Option(name = "-posTestExamplesFile", usage = "File containing positive testing examples.", required = false, handler=StringArrayOptionHandler.class)
	private String[] posTestExamplesFilePath = null;
	
	@Option(name = "-negTestExamplesFile", usage = "File containing negative testing examples.", required = false, handler=StringArrayOptionHandler.class)
	private String[] negTestExamplesFilePath = null;
	
	@Option(name = "-globalDefinition", usage = "Learn one clause for each positive example, output all clauses. If true, learning is much slower.", required = false)
	private boolean globalDefinition = false;
	
	@Option(name = "-outputDefinitionFile", usage = "File where learned definition is output.", required = false, handler=StringArrayOptionHandler.class)
	private String[] outputDefinitionFilePath = null;

	@Argument
	private List<String> arguments = new ArrayList<String>();

	private Parameters parameters;
	private Schema schema;
	private DataModel dataModel;

	// Logger
	private static Logger logger = Logger.getLogger(CastorCmd.class);

	public static void main(String[] args) {
		CastorCmd program = new CastorCmd();
		program.run(args);
	}

	public LearningResult run(String[] args) {
		TimeWatch tw = TimeWatch.start();
		NumbersKeeper.reset();
		boolean success;
		LearningResult learningResult = new LearningResult();

		// Parse the arguments
		CmdLineParser parser = new CmdLineParser(this);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			logger.error(e.getMessage());
			parser.printUsage(System.out);
			return learningResult;
		}

		if (help) {
			parser.printUsage(System.out);
			return learningResult;
		}

		// Get parameters from file
		String parametersFile = getOption(parametersFilePath);
		JsonObject parametersJson = FileUtils.convertFileToJSON(parametersFile);
		parameters = this.readParametersFromJson(parametersJson);
		
		logger.info(parameters.toString());

		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.VOLTDB);
		try {
			// Create data access objects and set URL of data
			try {
				String url = this.parameters.getDbURL() + ":" + this.parameters.getPort();
				daoFactory.initConnection(url);
			} catch (RuntimeException e) {
				logger.error("Unable to connect to server with URL: " + this.parameters.getDbURL());
				return learningResult;
			}
			GenericDAO genericDAO = daoFactory.getGenericDAO();
			BottomClauseConstructionDAO bottomClauseConstructionDAO = daoFactory.getBottomClauseConstructionDAO();

			// Get schema from file or from DB
			if (schemaFilePath != null) {
				String schemaFile = getOption(schemaFilePath);
				JsonObject schemaJson = FileUtils.convertFileToJSON(schemaFile);
				schema = this.readSchemaFromJson(schemaJson);
			} else {
				// Read from DB
				schema = genericDAO.getSchema();
			}

			// Get INDs from file or DDL
			//TODO hardcoded name, replace with name of ddl + INDs
			String castorInputFolder = "castor-input/";
			Files.createDirectories(Paths.get(castorInputFolder));
			String indsFile = castorInputFolder + "inds.json";
			if (indsFilePath != null) {
				indsFile = getOption(indsFilePath);
			} else if (ddlFilePath != null) {
				String ddlFile = getOption(ddlFilePath);
				success = DDLIndMain.extractIndFromDDL(ddlFile, indsFile);
				if (!success) {
					return learningResult;
				}
			}
			// If INDs were specified either on IND file or DDL file, read them
			if (indsFilePath != null || ddlFilePath != null) {
				JsonObject indsJson = FileUtils.convertFileToJSON(indsFile);
				this.readINDsFromJson(indsJson);
				this.readMDsFromJson(indsJson);
			}

			// Get data model from file
			String dataModelFile = getOption(dataModelFilePath);
			JsonObject dataModelJson = FileUtils.convertFileToJSON(dataModelFile);
			dataModel = this.readDataModelFromJson(dataModelJson);

			// Validate data model
			this.validateDataModel();

			// Get examples from file or from DB
			Relation posTrain;
			Relation negTrain;
			CoverageBySubsumptionParallel.EXAMPLES_SOURCE examplesSource;
			
			// If file names for examples are given, assume examples are in files
			String posTrainExamplesFile = null;
			String negTrainExamplesFile = null;
			if (posTrainExamplesFilePath != null && negTrainExamplesFilePath != null) {
				// Get examples from file
				examplesSource = CoverageBySubsumptionParallel.EXAMPLES_SOURCE.FILE;
				
				posTrainExamplesFile = getOption(posTrainExamplesFilePath);
				negTrainExamplesFile = getOption(negTrainExamplesFilePath);
				
				String posTrainFileName = FilenameUtils.getBaseName(posTrainExamplesFile);
				String negTrainFileName = FilenameUtils.getBaseName(negTrainExamplesFile);

				List<String> posTrainExamplesFileHeader = CSVFileReader.readCSVHeader(posTrainExamplesFile);
				List<String> negTrainExamplesFileHeader = CSVFileReader.readCSVHeader(negTrainExamplesFile);
				
				posTrain = new Relation(posTrainFileName, posTrainExamplesFileHeader);
				negTrain = new Relation(negTrainFileName, negTrainExamplesFileHeader);
			} else {
				// Get examples from DB
				examplesSource = CoverageBySubsumptionParallel.EXAMPLES_SOURCE.DB;
				
				String posTrainTableName = (this.dataModel.getModeH().getPredicateName() + trainPosSuffix).toUpperCase();
				String negTrainTableName = (this.dataModel.getModeH().getPredicateName() + trainNegSuffix).toUpperCase();
				
				posTrain = this.schema.getRelations().get(posTrainTableName);
				negTrain = this.schema.getRelations().get(negTrainTableName);

				// Check that tables containing examples exist in schema
				if (posTrain == null || negTrain == null) {
					throw new IllegalArgumentException(
							"One or more tables containing training examples do not exist in the schema: "
									+ posTrainTableName + ", " + negTrainTableName +
									"\nMake sure that tables exist in the database or specify path of files contaning examples.");
				}
			}
			
			this.validateExamplesRelations(posTrain, negTrain);

			// Generate and compile stored procedures
			if (this.parameters.isCreateStoredProcedure()) {
				success = this.compileStoredProcedures();
				if (!success) {
					return learningResult;
				}
			}
			
			// Create saturator
			BottomClauseGenerator saturator;
			BottomClauseGenerator coverageEngineSaturator;
			logger.info("Preprocessing...");
			tw.reset();
			if (parameters.isUseStoredProcedure()) {
				if (parameters.isAllowSimilarity() ||
						parameters.getSamplingMethod().equals(SamplingMethods.OLKEN) ||
						parameters.getSamplingMethod().equals(SamplingMethods.STREAM) ||
						parameters.getSamplingMethod().equals(SamplingMethods.STRATIFIED) ||
						parameters.getSamplingMethod().equals(SamplingMethods.SEMISTRATIFIED)) {
					throw new UnsupportedOperationException("Sampling method or similarity not supported inside stored procedure.");
				} else {
					saturator = new BottomClauseGeneratorInsideSP();
					coverageEngineSaturator = new BottomClauseGeneratorInsideSP();
				}
			} else {
				saturator = new BottomClauseGeneratorNaiveSampling(true, parameters.getRandomSeed());
				if (parameters.isSampleGroundBottomClauses()) {
					coverageEngineSaturator = new BottomClauseGeneratorNaiveSampling(true, parameters.getRandomSeed());
				} else {
					coverageEngineSaturator = new BottomClauseGeneratorNaiveSampling(false, parameters.getRandomSeed());
				}
			}
			NumbersKeeper.preprocessingTime = tw.time();
			
			// Create CoverageEngine
			tw.reset();
			logger.info("Creating coverage engine...");
			boolean createFullCoverageEngine = !saturation && !groundSaturation;
			CoverageEngine coverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, coverageEngineSaturator,
					posTrain, negTrain, this.schema, this.dataModel, this.parameters, createFullCoverageEngine,
					examplesSource, posTrainExamplesFile, negTrainExamplesFile);
//			CoverageEngine coverageEngine = new CoverageByDBJoiningAllSingleExample(genericDAO, posTrain, negTrain, parameters);
			
			logger.info("Creating coverage engine for covering approach...");
			CoverageEngine coverageEngineForCoveringApproach;
			if (parameters.isSampleInCoveringApproach() == parameters.isSampleGroundBottomClauses()) {
				coverageEngineForCoveringApproach = coverageEngine;
			} else {
				BottomClauseGenerator coverageEngineSaturatorForCoveringApproach;
				coverageEngineSaturatorForCoveringApproach = new BottomClauseGeneratorNaiveSampling(parameters.isSampleInCoveringApproach(), parameters.getRandomSeed());
				coverageEngineForCoveringApproach = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, coverageEngineSaturatorForCoveringApproach,
						posTrain, negTrain, this.schema, this.dataModel, this.parameters, createFullCoverageEngine,
						examplesSource, posTrainExamplesFile, negTrainExamplesFile);
			}
			NumbersKeeper.creatingCoverageTime = tw.time();
			
			if (saturation || groundSaturation) {
				if (this.exampleForSaturation >= coverageEngine.getAllPosExamples().size() || this.exampleForSaturation < 0) {
					throw new IllegalArgumentException("Illegal index of positive example to saturate (greater than maximum index of positive examples or less than 0).");
				}
				
				if (saturation) {
					// BOTTOM CLAUSE
					BottomClauseUtil.generateBottomClauseForExample(genericDAO, bottomClauseConstructionDAO, saturator,
							coverageEngine.getAllPosExamples().get(this.exampleForSaturation), this.schema,
							this.dataModel, this.parameters);
				} else if (groundSaturation) {
					// GROUND BOTTOM CLAUSE
					BottomClauseUtil.generateGroundBottomClauseForExample(genericDAO, bottomClauseConstructionDAO, saturator,
							coverageEngine.getAllPosExamples().get(this.exampleForSaturation), this.schema,
							this.dataModel, this.parameters);
				}
				logger.info("Computing join sizes time: " + NumbersKeeper.computeJoinSizesTime);
			} else {
				// LEARN
				logger.info("Learning...");
				Learner learner;
				if (this.algorithm.equals(ALGORITHM_CASTOR)) {
					learner = new CastorLearner(genericDAO, bottomClauseConstructionDAO, saturator, coverageEngine, coverageEngineForCoveringApproach, parameters, schema);
				} else if (this.algorithm.equals(ALGORITHM_GOLEM)) {
					learner = new Golem(genericDAO, bottomClauseConstructionDAO, saturator, coverageEngine, parameters);
				} else if (this.algorithm.equals(ALGORITHM_PROGOLEM)) {
					learner = new ProGolem(genericDAO, bottomClauseConstructionDAO, saturator, coverageEngine, parameters);
				} else {
					throw new IllegalArgumentException("Learning algorithm " + this.algorithm + " not implemented.");
				}
				List<ClauseInfo> definition = learner.learn(this.schema, this.dataModel, posTrain, negTrain, this.dataModel.getSpName(), globalDefinition);

				List<String> sqlLines = new LinkedList<String>();
				if (outputSQL) {
					StringBuilder sb = new StringBuilder();
					sb.append("SQL format:\n");
					for (ClauseInfo clauseInfo : definition) {
						String clause = QueryGenerator.generateQueryFromClause(schema, clauseInfo);
						sb.append(clause+"\n");
						sqlLines.add(clause);
					}
					logger.info(sb.toString());
				}
				
				if (outputDefinitionFilePath != null) {
					List<String> lines = new LinkedList<String>();
					lines.add("DATALOG FORMAT:\n");
					for (ClauseInfo clauseInfo : definition) {
						lines.add(Formatter.prettyPrint(clauseInfo));
					}
					lines.add("\n\nSQL FORMAT:\n");
					lines.addAll(sqlLines);
					
					String outputDefinitionFile = getOption(outputDefinitionFilePath);
					FileUtils.writeToFile(outputDefinitionFile, lines);
				}
				
				// Save total time
				NumbersKeeper.totalTime += tw.time();
				
				// EVALUATE DEFINITION ON TRAINING DATA
				logger.info("Evaluating on training data...");
				learner.evaluate(coverageEngine, this.schema, definition, posTrain, negTrain);

				// EVALUATE DEFINITION ON TESTING DATA
				EvaluationResult testEvaluationResult = null;
				if (testLearnedDefinition) {
					// Get examples from file or from DB
					Relation posTest;
					Relation negTest;
					CoverageBySubsumptionParallel.EXAMPLES_SOURCE examplesSourceTest;
					
					// If file names for examples are given, assume examples are in files
					String posTestExamplesFile = null;
					String negTestExamplesFile = null;
					if (posTestExamplesFilePath != null && negTestExamplesFilePath != null) {
						// Get examples from file
						examplesSourceTest = CoverageBySubsumptionParallel.EXAMPLES_SOURCE.FILE;
						
						posTestExamplesFile = getOption(posTestExamplesFilePath);
						negTestExamplesFile = getOption(negTestExamplesFilePath);
						
						String posTestFileName = FilenameUtils.getBaseName(posTestExamplesFile);
						String negTestFileName = FilenameUtils.getBaseName(negTestExamplesFile);

						List<String> posTestExamplesFileHeader = CSVFileReader.readCSVHeader(posTestExamplesFile);
						List<String> negTestExamplesFileHeader = CSVFileReader.readCSVHeader(negTestExamplesFile);
						
						posTest = new Relation(posTestFileName, posTestExamplesFileHeader);
						negTest = new Relation(negTestFileName, negTestExamplesFileHeader);
					} else {
						// Get examples from DB
						examplesSourceTest = CoverageBySubsumptionParallel.EXAMPLES_SOURCE.DB;
						
						String posTestTableName = (this.dataModel.getModeH().getPredicateName() + testPosSuffix).toUpperCase();
						String negTestTableName = (this.dataModel.getModeH().getPredicateName() + testNegSuffix).toUpperCase();
						
						posTest = this.schema.getRelations().get(posTestTableName);
						negTest = this.schema.getRelations().get(negTestTableName);

						// Check that tables containing examples exist in schema
						if (posTest == null || negTest == null) {
							throw new IllegalArgumentException(
									"One or more tables containing testing examples do not exist in the schema: "
											+ posTestTableName + ", " + negTestTableName +
											"\nMake sure that tables exist in the database or specify path of files contaning examples.");
						}
					}

					// For testing, use original bottom clause construction. Check parameters to determine whether to use sampling.
					BottomClauseGenerator testSaturator;
					if (parameters.isUseStoredProcedure()) {
						if (parameters.isAllowSimilarity()) {
							throw new UnsupportedOperationException("Sampling method or simliarity not supported inside stored procedure.");
						} else {
							testSaturator = new BottomClauseGeneratorInsideSP();
						}
					} else {
						testSaturator = new BottomClauseGeneratorNaiveSampling(this.parameters.isSampleInTesting(), parameters.getRandomSeed());
					}
					
					logger.info("Evaluating on testing data...");
					CoverageEngine testCoverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, testSaturator, 
							posTest, negTest, this.schema, this.dataModel, this.parameters, true,
							examplesSourceTest, posTestExamplesFile, negTestExamplesFile);
					testEvaluationResult = learner.evaluate(testCoverageEngine, this.schema, definition, posTest, negTest);
				}
				
				logger.info("Total time: " + NumbersKeeper.totalTime);
				logger.info("Creating coverage engine time: " + NumbersKeeper.creatingCoverageTime);
				logger.info("Learning time: " + NumbersKeeper.learningTime);
				logger.info("Bottom-clause construction time: " + NumbersKeeper.bottomClauseConstructionTime);
				logger.info("Coverage time: " + NumbersKeeper.coverageTime);
				logger.info("Coverage calls: " + NumbersKeeper.coverageCalls);
				logger.info("Scoring time: " + NumbersKeeper.scoringTime);
				logger.info("Entailment time: " + NumbersKeeper.entailmentTime);
				logger.info("Minimization time: " + NumbersKeeper.minimizationTime);
				logger.info("Reduction time: " + NumbersKeeper.reducerTime);
				logger.info("LGG time: " + NumbersKeeper.lggTime);
				logger.info("LearnClause time: " + NumbersKeeper.learnClauseTime);
				logger.info("Preprocessing time (extracting statistics, creating indexes, etc) (not included in total time): " + NumbersKeeper.preprocessingTime);
				logger.info("Computing join sizes time: " + NumbersKeeper.computeJoinSizesTime);
				
				// Set learning result
	            learningResult.setSuccess(true);
	            learningResult.setDefinition(MyClauseToClauseAsString.parseDefinition(definition));
	            if (testEvaluationResult != null) {
		            learningResult.setAccuracy(testEvaluationResult.getAccuracy());
		            learningResult.setPrecision(testEvaluationResult.getPrecision());
		            learningResult.setRecall(testEvaluationResult.getRecall());
		            learningResult.setF1(testEvaluationResult.getF1());
	            }
	            learningResult.setTime(NumbersKeeper.totalTime/1000);//transforming to seconds
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close connection to DBMS
			daoFactory.closeConnection();
		}
		
		return learningResult;
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

	/*
	 * Check that all relations in data model exist in schema
	 */
	private void validateDataModel() {
		// Check head mode
		// validateModeRelation(dataModel.getModeH().getPredicateName(),
		// dataModel.getModeH().getArguments().size());

		// Check body modes
		for (Mode mode : dataModel.getModesB()) {
			validateModeRelation(mode.getPredicateName().toUpperCase(), mode.getArguments().size());
		}
	}

	/*
	 * Check that relation exist in schema and with same number of attributes
	 */
	private void validateModeRelation(String relationName, int relationArity) {
		if (!schema.getRelations().containsKey(relationName)
				|| schema.getRelations().get(relationName).getAttributeNames().size() != relationArity) {
			throw new IllegalArgumentException("Schema does not contain relation " + relationName
					+ " or number of attributes in mode does not match with number of attributes in relation in schema.");
		}
	}
	
	private void validateExamplesRelations(Relation posTrain, Relation negTrain) {
		if (dataModel.getModeH().getArguments().size() != posTrain.getAttributeNames().size() ||
				dataModel.getModeH().getArguments().size() != negTrain.getAttributeNames().size()) {
			throw new IllegalArgumentException("Number of attributes in head mode does not match with number of attributes in examples.");
		}
	}

	/*
	 * Read data model from JSON object
	 */
	private Parameters readParametersFromJson(JsonObject parametersJson) {
		Parameters parameters;
		try {
			logger.info("Reading parameters...");
			parameters = JsonSettingsReader.readParameters(parametersJson);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return parameters;
	}

	/*
	 * Read data model from JSON object
	 */
	private DataModel readDataModelFromJson(JsonObject dataModelJson) {
		DataModel dataModel;
		try {
			logger.info("Reading data model...");
			dataModel = JsonSettingsReader.readDataModel(dataModelJson);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return dataModel;
	}

	/*
	 * Read schema from JSON object
	 */
	private Schema readSchemaFromJson(JsonObject schemaJson) {
		Schema schema;
		try {
			logger.info("Reading schema...");
			schema = JsonSettingsReader.readSchema(schemaJson);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return schema;
	}

	/*
	 * Read INDs from JSON object
	 */
	private void readINDsFromJson(JsonObject dependenciesJson) {
		try {
			logger.info("Reading inclusion dependencies...");
			Map<String, List<InclusionDependency>> inds = JsonSettingsReader.readINDs(dependenciesJson);
			schema.setInclusionDependencies(inds);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * Read MDs from JSON object
	 */
	private void readMDsFromJson(JsonObject dependenciesJson) {
		try {
			logger.info("Reading matching dependencies...");
			Map<Pair<String,Integer>, List<MatchingDependency>> mds = JsonSettingsReader.readMDs(dependenciesJson);
			schema.setMatchingDependencies(mds);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * Call method to generate, compile, and create stored procedures
	 */
	private boolean compileStoredProcedures() throws Exception {
		// Generate stored procedures
		StoredProcedureGeneratorSaturationInsideSP spGenerator = new StoredProcedureGeneratorSaturationInsideSP();
		boolean success = spGenerator.generateAndCompileStoredProcedures(this.parameters.getDbURL(),
				this.parameters.getPort(), "sp", this.dataModel.getSpName(), this.parameters.getIterations(),
				this.schema, this.dataModel.getModeH(), this.dataModel.getModesB(), this.parameters.isUseInds());
		return success;
	}
}
