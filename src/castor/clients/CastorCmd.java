package castor.clients;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import castor.algorithms.CastorLearner;
import castor.algorithms.bottomclause.BottomClauseUtil;
import castor.algorithms.coverageengines.CoverageBySubsumptionParallel;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.db.DBCommons;
import castor.db.StoredProcedureGeneratorSaturationInsideSP;
import castor.db.dataaccess.BottomClauseConstructionDAO;
import castor.db.dataaccess.DAOFactory;
import castor.db.dataaccess.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.language.Relation;
import castor.language.Schema;
import castor.settings.DataModel;
import castor.settings.JsonSettingsReader;
import castor.settings.Parameters;
import castor.utils.FileUtils;
import castor.utils.TimeKeeper;
import castor.utils.TimeWatch;

import com.google.gson.JsonObject;

public class CastorCmd {
	
	// Options
	@Option(name="-parameters",usage="Parameters file",required=true)
    private String parametersFile;
	
	@Option(name="-schema",usage="Schema file",required=true)
    private String schemaFile;
	
	@Option(name="-dataModel",usage="Data model file",required=true)
    private String dataModelFile;
	
	@Option(name="-sat",usage="Only build bottom clause for example given in option e.")
    private boolean saturation = false;
	
	@Option(name="-groundsat",usage="Only ground build bottom clause for example given in option e.")
    private boolean groundSaturation = false;
	
	@Option(name="-e",usage="Example to build bottom clause for (only when using sat or groundsat options)")
    private int exampleForSaturation = 0;
	
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
        
        // Read JSON objects
        JsonObject parametersJson = FileUtils.convertFileToJSON(parametersFile);
        JsonObject schemaJson = FileUtils.convertFileToJSON(schemaFile);
        JsonObject dataModelJson = FileUtils.convertFileToJSON(dataModelFile);
        
        // Get parameters from JSON object
 		success = this.readParametersFromJson(parametersJson);
     	if (!success) {
     		return;
     	}
 		
 		// Get schema from JSON object
 		success = this.readSchemaFromJson(schemaJson);
     	if (!success) {
     		return;
     	}
     	
 		// Get data model from JSON object
     	success = this.readDataModelFromJson(dataModelJson);
     	if (!success) {
     		return;
     	}
        
     	String postrainTableName = this.dataModel.getTarget()+DBCommons.TRAIN_POS_SUFFIX;
		String negtrainTableName = this.dataModel.getTarget()+DBCommons.TRAIN_NEG_SUFFIX;
		String postestTableName = this.dataModel.getTarget()+DBCommons.TEST_POS_SUFFIX;
		String negtestTableName = this.dataModel.getTarget()+DBCommons.TEST_NEG_SUFFIX;
		
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
        	daoFactory.initConnection(this.parameters.getDbURL());
    		GenericDAO genericDAO = daoFactory.getGenericDAO();
            BottomClauseConstructionDAO bottomClauseConstructionDAO = daoFactory.getBottomClauseConstructionDAO();
        	
        	// Generate and compile stored procedures
        	if (this.parameters.isCreateStoredProcedure()) {
	        	success = this.compileStoredProcedures();
	        	if (!success) {
	        		return;
	        	}
        	}

	        // General
	        Relation posTrain = this.schema.getRelations().get(postrainTableName);
	      	Relation negTrain = this.schema.getRelations().get(negtrainTableName);
	      	Relation posTest = this.schema.getRelations().get(postestTableName);
	        Relation negTest = this.schema.getRelations().get(negtestTableName);
	        
            // Create CoverageEngine and ProGolem object
            tw.reset();
            logger.info("Creating coverage engine...");
            
            boolean createFullCoverageEngine = !saturation && !groundSaturation;
            CoverageEngine coverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, posTrain, negTrain, this.dataModel.getSpName(), this.parameters.getIterations(), this.parameters.getRecall(), this.parameters.getMaxterms(), this.parameters.getThreads(), createFullCoverageEngine);
            TimeKeeper.creatingCoverageTime = tw.time();
            
            if (saturation) {
            	// BOTTOM CLAUSE
            	BottomClauseUtil.generateBottomClauseForExample(BottomClauseUtil.ALGORITHMS.INSIDE_STORED_PROCEDURE, genericDAO, bottomClauseConstructionDAO, coverageEngine.getAllPosExamples().get(this.exampleForSaturation), this.schema, this.dataModel.getModeH(), this.dataModel.getModesB(), this.parameters.getIterations(), this.dataModel.getSpName(), this.parameters.getRecall(), this.parameters.getMaxterms());
            } else if (groundSaturation) {
            	// GROUND BOTTOM CLAUSE
            	BottomClauseUtil.generateGroundBottomClauseForExample(BottomClauseUtil.ALGORITHMS.INSIDE_STORED_PROCEDURE, genericDAO, bottomClauseConstructionDAO, coverageEngine.getAllPosExamples().get(this.exampleForSaturation), this.schema, this.dataModel.getModeH(), this.dataModel.getModesB(), this.parameters.getIterations(), this.dataModel.getSpName(), this.parameters.getRecall(), this.parameters.getMaxterms());
            } else {
	            // LEARN
            	logger.info("Learning...");
            	CastorLearner castor = new CastorLearner(genericDAO, bottomClauseConstructionDAO, coverageEngine, parameters);
	            List<ClauseInfo> definition = castor.learn(this.schema, this.dataModel.getModeH(), this.dataModel.getModesB(), posTrain, negTrain, this.dataModel.getSpName());
	            TimeKeeper.totalTime += tw.time();
	            
	            logger.info("Total time: " + TimeKeeper.totalTime);
	            logger.info("Creating coverage engine time: " + TimeKeeper.creatingCoverageTime);
	            logger.info("Learning time: " + TimeKeeper.learningTime);
	            logger.info("Coverage time: " + TimeKeeper.coverageTime);
	            logger.info("Coverage calls: " + TimeKeeper.coverageCalls);
	            logger.info("Scoring time: " + TimeKeeper.scoringTime);
	            logger.info("Entailment time: " + TimeKeeper.entailmentTime);
	            logger.info("Transformation time: " + TimeKeeper.transformationTime);
	            logger.info("Reduction time: " + TimeKeeper.reducerTime);
	            
	            // EVALUATE DEFINITION
	            logger.info("Evaluating on testing data...");
	            CoverageEngine testCoverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, posTest, negTest, this.dataModel.getSpName(), this.parameters.getIterations(), this.parameters.getRecall(), this.parameters.getMaxterms(), this.parameters.getThreads(), true);
	            castor.evaluate(testCoverageEngine, this.schema, definition, posTest, negTest);
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
	 * Read data model from JSON object
	 */
	private boolean readParametersFromJson(JsonObject parametersJson) {
		boolean success;
		try {
			logger.info("Reading parameters...");
			parameters = JsonSettingsReader.readParameters(parametersJson);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	/*
	 * Read data model from JSON object
	 */
	private boolean readDataModelFromJson(JsonObject dataModelJson) {
		boolean success;
		try {
			logger.info("Reading data model...");
			dataModel = JsonSettingsReader.readDataModel(dataModelJson);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	/*
	 * Read schema from JSON object
	 */
	private boolean readSchemaFromJson(JsonObject schemaJson) {
		boolean success;
		try {
			logger.info("Reading schema...");
			schema = JsonSettingsReader.readSchema(schemaJson);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
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
