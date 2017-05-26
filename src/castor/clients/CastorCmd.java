package castor.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import castor.algorithms.CastorLearner;
import castor.algorithms.Golem;
import castor.algorithms.Learner;
import castor.algorithms.ProGolem;
import castor.algorithms.bottomclause.BottomClauseUtil;
import castor.algorithms.coverageengines.CoverageBySubsumptionParallel;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.db.DBCommons;
import castor.db.StoredProcedureGeneratorSaturationInsideSP;
import castor.db.dataaccess.BottomClauseConstructionDAO;
import castor.db.dataaccess.DAOFactory;
import castor.db.dataaccess.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.language.InclusionDependency;
import castor.language.Mode;
import castor.language.Relation;
import castor.language.Schema;
import castor.settings.DataModel;
import castor.settings.JsonSettingsReader;
import castor.settings.Parameters;
import castor.utils.FileUtils;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;

import com.google.gson.JsonObject;

public class CastorCmd {
	
	public static final String ALGORITHM_CASTOR = "Castor";
	public static final String ALGORITHM_PROGOLEM = "ProGolem";
	public static final String ALGORITHM_GOLEM = "Golem";
	
	// Options
	@Option(name="-parameters",usage="Parameters file",required=true)
    private String parametersFile;
	
	@Option(name="-schema",usage="Schema file",required=false)
    private String schemaFile = null;
	
	@Option(name="-inds",usage="INDs file",required=false)
    private String indsFile = null;
	
	@Option(name="-dataModel",usage="Data model file",required=true)
    private String dataModelFile;
	
	@Option(name="-sat",usage="Only build bottom clause for example given in option e.")
    private boolean saturation = false;
	
	@Option(name="-groundsat",usage="Only ground build bottom clause for example given in option e.")
    private boolean groundSaturation = false;
	
	@Option(name="-e",usage="Example to build bottom clause for (only when using sat or groundsat options)")
    private int exampleForSaturation = 0;
	
	@Option(name="-algorithm",usage="Algorithm to run (Castor, Golem)",required=false)
    private String algorithm = ALGORITHM_CASTOR;
	
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
	
	public void run(String[] args) {
		TimeWatch tw = TimeWatch.start();
		boolean success;
		
		// Parse the arguments
        try {
        	CmdLineParser parser = new CmdLineParser(this);
			parser.parseArgument(args);
        } catch (CmdLineException e) {
			logger.error(e.getMessage());
			return;
		}
        
        // Get parameters from file
        JsonObject parametersJson = FileUtils.convertFileToJSON(parametersFile);
 		parameters = this.readParametersFromJson(parametersJson);
		
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.VOLTDB);
        try {
        	// Create data access objects and set URL of data
        	try {
        		daoFactory.initConnection(this.parameters.getDbURL());
        	}
        	catch (RuntimeException e) {
        		logger.error("Unable to connect to server with URL: " + this.parameters.getDbURL());
        		return;
        	}
    		GenericDAO genericDAO = daoFactory.getGenericDAO();
            BottomClauseConstructionDAO bottomClauseConstructionDAO = daoFactory.getBottomClauseConstructionDAO();
            
     		// Get schema from file or from DB
         	if (schemaFile != null) {
            	JsonObject schemaJson = FileUtils.convertFileToJSON(schemaFile);
            	schema = this.readSchemaFromJson(schemaJson);
            } else {
            	// Read from DB
            	schema = genericDAO.getSchema();
            }
         	
         	// Get INDs from file, if given
         	if (indsFile != null) {
            	JsonObject indsJson = FileUtils.convertFileToJSON(indsFile);
            	this.readINDsFromJson(indsJson);
            }
         	
         	// Get data model from file
         	JsonObject dataModelJson = FileUtils.convertFileToJSON(dataModelFile);
         	dataModel = this.readDataModelFromJson(dataModelJson);
         	
         	// Validate data model
         	this.validateDataModel();
        	
        	// Generate and compile stored procedures
        	if (this.parameters.isCreateStoredProcedure()) {
	        	success = this.compileStoredProcedures();
	        	if (!success) {
	        		return;
	        	}
        	}
        	
	        // Obtain train and test relations
        	String postrainTableName = this.dataModel.getTarget()+DBCommons.TRAIN_POS_SUFFIX;
    		String negtrainTableName = this.dataModel.getTarget()+DBCommons.TRAIN_NEG_SUFFIX;
    		String postestTableName = this.dataModel.getTarget()+DBCommons.TEST_POS_SUFFIX;
    		String negtestTableName = this.dataModel.getTarget()+DBCommons.TEST_NEG_SUFFIX;
    		
	        Relation posTrain = this.schema.getRelations().get(postrainTableName.toUpperCase());
	      	Relation negTrain = this.schema.getRelations().get(negtrainTableName.toUpperCase());
	      	Relation posTest = this.schema.getRelations().get(postestTableName.toUpperCase());
	        Relation negTest = this.schema.getRelations().get(negtestTableName.toUpperCase());
	        
            // Create CoverageEngine
            tw.reset();
            logger.info("Creating coverage engine...");
            boolean createFullCoverageEngine = !saturation && !groundSaturation;
            CoverageEngine coverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, posTrain, negTrain, this.dataModel.getSpName(), this.parameters.getIterations(), this.parameters.getRecall(), this.parameters.getMaxterms(), this.parameters.getThreads(), createFullCoverageEngine);
            NumbersKeeper.creatingCoverageTime = tw.time();
            
            if (saturation) {
            	// BOTTOM CLAUSE
            	BottomClauseUtil.generateBottomClauseForExample(BottomClauseUtil.ALGORITHMS.INSIDE_STORED_PROCEDURE, genericDAO, bottomClauseConstructionDAO, coverageEngine.getAllPosExamples().get(this.exampleForSaturation), this.schema, this.dataModel.getModeH(), this.dataModel.getModesB(), this.parameters.getIterations(), this.dataModel.getSpName(), this.parameters.getRecall(), this.parameters.getMaxterms());
            } else if (groundSaturation) {
            	// GROUND BOTTOM CLAUSE
            	BottomClauseUtil.generateGroundBottomClauseForExample(BottomClauseUtil.ALGORITHMS.INSIDE_STORED_PROCEDURE, genericDAO, bottomClauseConstructionDAO, coverageEngine.getAllPosExamples().get(this.exampleForSaturation), this.schema, this.dataModel.getModeH(), this.dataModel.getModesB(), this.parameters.getIterations(), this.dataModel.getSpName(), this.parameters.getRecall(), this.parameters.getMaxterms());
            } else {
	            // LEARN
            	logger.info("Learning...");
            	Learner learner;
            	if (this.algorithm.equals(ALGORITHM_CASTOR)) {
            		learner = new CastorLearner(genericDAO, bottomClauseConstructionDAO, coverageEngine, parameters);
            	} else if (this.algorithm.equals(ALGORITHM_GOLEM)) {
            		learner = new Golem(genericDAO, bottomClauseConstructionDAO, coverageEngine, dataModel, parameters);
            	} else if (this.algorithm.equals(ALGORITHM_PROGOLEM)) {
            		learner = new ProGolem(genericDAO, coverageEngine, parameters);
            	} else {
            		throw new IllegalArgumentException("Learning algorithm " + this.algorithm + " not implemented.");
            	}
            	List<ClauseInfo> definition = learner.learn(this.schema, this.dataModel.getModeH(), this.dataModel.getModesB(), posTrain, negTrain, this.dataModel.getSpName());
	            
	            NumbersKeeper.totalTime += tw.time();
	            
	            logger.info("Total time: " + NumbersKeeper.totalTime);
	            logger.info("Creating coverage engine time: " + NumbersKeeper.creatingCoverageTime);
	            logger.info("Learning time: " + NumbersKeeper.learningTime);
	            logger.info("Coverage time: " + NumbersKeeper.coverageTime);
	            logger.info("Coverage calls: " + NumbersKeeper.coverageCalls);
	            logger.info("Scoring time: " + NumbersKeeper.scoringTime);
	            logger.info("Entailment time: " + NumbersKeeper.entailmentTime);
	            logger.info("Minimization time: " + NumbersKeeper.minimizationTime);
	            logger.info("Reduction time: " + NumbersKeeper.reducerTime);
	            logger.info("LGG time: " + NumbersKeeper.lggTime);
	            logger.info("LearnClause time: " + NumbersKeeper.learnClauseTime);
	            
	            logger.info("Avg clause length in coverage: " + (NumbersKeeper.clauseLengthSum / NumbersKeeper.coverageCalls));
	            
	            // EVALUATE DEFINITION
	            logger.info("Evaluating on testing data...");
	            CoverageEngine testCoverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, posTest, negTest, this.dataModel.getSpName(), this.parameters.getIterations(), this.parameters.getRecall(), this.parameters.getMaxterms(), this.parameters.getThreads(), true);
	            learner.evaluate(testCoverageEngine, this.schema, definition, posTest, negTest);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        	// Close connection to DBMS
        	daoFactory.closeConnection();
        }
	}
	
	/*
	 * Check that all relations in data model exist in schema
	 */
	private void validateDataModel() {
		// Check head mode
		//validateModeRelation(dataModel.getModeH().getPredicateName(), dataModel.getModeH().getArguments().size());
		
		// Check body modes
		for (Mode mode : dataModel.getModesB()) {
			validateModeRelation(mode.getPredicateName().toUpperCase(), mode.getArguments().size());
		}
	}
	
	/*
	 * Check that relation exist in schema and with same number of attributes
	 */
	private void validateModeRelation(String relationName, int relationArity) {
		if (!schema.getRelations().containsKey(relationName) || 
				schema.getRelations().get(relationName).getAttributeNames().size() != relationArity) {
			throw new IllegalArgumentException("Schema does not contain relation " + relationName + " or number of attributes in mode does not match with number of attributes in relation in schema.");
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
	private void readINDsFromJson(JsonObject indsJson) {
		try {
			logger.info("Reading inclusion dependencies...");
			Map<String, List<InclusionDependency>> inds = JsonSettingsReader.readINDs(indsJson);
			schema.setInclusionDependencies(inds);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * Call method to generate, compile, and create stored procedures
	 */
	private boolean compileStoredProcedures() throws Exception
	{
		// Generate stored procedures
		StoredProcedureGeneratorSaturationInsideSP spGenerator = new StoredProcedureGeneratorSaturationInsideSP();
		boolean success = spGenerator.generateAndCompileStoredProcedures("sp", this.dataModel.getSpName(), this.parameters.getIterations(), this.schema, this.dataModel.getModeH(), this.dataModel.getModesB(), this.parameters.isUseInds());
		return success;
	}
}
