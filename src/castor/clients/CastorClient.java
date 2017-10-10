package castor.clients;

import java.util.List;

import org.apache.log4j.Logger;

import castor.algorithms.CastorLearner;
import castor.algorithms.bottomclause.StoredProcedureGeneratorSaturationInsideSP;
import castor.algorithms.coverageengines.CoverageBySubsumptionParallel;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.DAOFactory;
import castor.dataaccess.db.GenericDAO;
import castor.db.DBCommons;
import castor.hypotheses.ClauseInfo;
import castor.language.Relation;
import castor.language.Schema;
import castor.mappings.MyClauseToClauseAsString;
import castor.settings.DataModel;
import castor.settings.JsonSettingsReader;
import castor.settings.Parameters;
import castor.utils.FileUtils;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;
import castor.wrappers.EvaluationResult;
import castor.wrappers.LearningResult;

import com.google.gson.JsonObject;

public class CastorClient {

//	private Parameters parameters;
//	private Schema schema;
//	private DataModel dataModel;
//	
//	// Logger
//	private static Logger logger = Logger.getLogger(CastorClient.class);
//	
//	public static void main(String[] args) {
//		/*String dataModelFile = "/Users/jose/Documents/workspaceOSU/ProGolem-In-DB/ProGolem-In-DB/inputfiles/uwcse/set1/datamodel-uwcse-set1-schema1.json";
//		String schemaFile = "/Users/jose/Documents/workspaceOSU/ProGolem-In-DB/ProGolem-In-DB/inputfiles/uwcse/set1/schema-uwcse-set1-schema1.json";
//		String parametersFile = "/Users/jose/Documents/workspaceOSU/ProGolem-In-DB/ProGolem-In-DB/inputfiles/uwcse/set1/parameters.json";
//		CastorClient cs = new CastorClient();
//
//		JsonObject schemaJson = convertFileToJSON(schemaFile);
//		JsonObject dataModelJson = convertFileToJSON(dataModelFile);
//		JsonObject parametersJson = convertFileToJSON(parametersFile);
//		
//		cs.learn("localhost", 21212, "", schemaJson, dataModelJson, parametersJson);*/
//		
//		CastorClient cs = new CastorClient();
//		cs.learn("hiv", "11111111111111111111111111111111111111111111111111", "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
////		LearningResult res = cs.test();
////		System.out.println(res.getSchema());
//	}
//	
//	public LearningResult test() {
//		LearningResult learningResult = new LearningResult();
//		learningResult.setSuccess(false);
//		learningResult.setAlgorithm("Castor");
//		
//		JsonObject jsonObject = null;
//        try {
//        	jsonObject = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream("/resources/uwcse/schema-uwcse-set1-schema1.json"));
//            learningResult.setSchema(jsonObject.get("name").getAsString());
//        } catch (Exception e) {
//        	e.printStackTrace();
//        	learningResult.setErrorMessage(e.getMessage());
//        }
//		
//		return learningResult;
//	}
//	
//	
//	
//	public LearningResult learn(String dataset) {
//		if (dataset.equals("uwcse")) {
//			return learnUWCSE("", "");
//		} else if (dataset.equals("hiv")) {
//			return learnHIV("", "");
//		}
//		return new LearningResult();
//	}
//	
//	public LearningResult learn(String dataset, String posExamplesFlags, String negExamplesFlags) {
//		if (dataset.equals("uwcse")) {
//			return learnUWCSE(posExamplesFlags, negExamplesFlags);
//		} else if (dataset.equals("hiv")) {
//			return learnHIV(posExamplesFlags, negExamplesFlags);
//		}
//		return new LearningResult();
//	}
//	
//	public LearningResult learnUWCSE(String posExamplesFlags, String negExamplesFlags) {
//		String schemaResource = "/resources/uwcse/schema-uwcse-set1-schema1.json";
//		String dataModelResource = "/resources/uwcse/datamodel-uwcse-set1-schema1.json";
//		String parametersResource = "/resources/uwcse/parameters.json";
//		
//		JsonObject schemaJson = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream(schemaResource));
//		JsonObject dataModelJson = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream(dataModelResource));
//		JsonObject parametersJson = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream(parametersResource));
//		
//		return learn(schemaJson, dataModelJson, parametersJson, posExamplesFlags, negExamplesFlags);
//	}
//	
//	public LearningResult learnHIV(String posExamplesFlags, String negExamplesFlags) {
//		String schemaResource = "/resources/hiv/schema-hiv-set2-schema1.json";
//		String dataModelResource = "/resources/hiv/datamodel-hiv-set2-schema1.json";
//		String parametersResource = "/resources/hiv/parameters.json";
//		
//		JsonObject schemaJson = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream(schemaResource));
//		JsonObject dataModelJson = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream(dataModelResource));
//		JsonObject parametersJson = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream(parametersResource));
//	
//		return learn(schemaJson, dataModelJson, parametersJson, posExamplesFlags, negExamplesFlags);
//	}
//	
//	// default client port: 21212 
//	public LearningResult learn(JsonObject schemaJson, JsonObject dataModelJson, JsonObject parametersJson, String posExamplesFlags, String negExamplesFlags) {
//		TimeWatch tw = TimeWatch.start();
//		boolean success;
//		LearningResult learningResult = new LearningResult();
//		learningResult.setAlgorithm("Castor");
//		
//		// Get parameters from JSON object
//		success = this.readParametersFromJson(parametersJson);
//    	if (!success) {
//    		learningResult.setSuccess(false);
//    		learningResult.setErrorMessage("Error reading parameters");
//    		return learningResult;
//    	}
//		
//		// Get schema from JSON object
//		success = this.readSchemaFromJson(schemaJson);
//    	if (!success) {
//    		learningResult.setSuccess(false);
//    		learningResult.setErrorMessage("Error reading schema");
//    		return learningResult;
//    	}
//    	learningResult.setSchema(schema.getName());
//    	
//		// Get data model from JSON object
//    	success = this.readDataModelFromJson(dataModelJson);
//    	if (!success) {
//    		learningResult.setSuccess(false);
//    		learningResult.setErrorMessage("Error reading data model");
//    		return learningResult;
//    	}
//		
//		String postrainTableName = this.dataModel.getTarget()+DBCommons.TRAIN_POS_SUFFIX;
//		String negtrainTableName = this.dataModel.getTarget()+DBCommons.TRAIN_NEG_SUFFIX;
//		String postestTableName = this.dataModel.getTarget()+DBCommons.TEST_POS_SUFFIX;
//		String negtestTableName = this.dataModel.getTarget()+DBCommons.TEST_NEG_SUFFIX;
//		
//		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.VOLTDB);
//        try {
//        	// Create data access objects and set URL of data
//        	try {
//        		daoFactory.initConnection(this.parameters.getDbURL());
//        	} catch(RuntimeException e) {
//        		learningResult.setSuccess(false);
//        		learningResult.setErrorMessage("Unable to connect to server with URL: " + this.parameters.getDbURL() + "\n" + e.getMessage());
//        		logger.error(learningResult.getErrorMessage());
//        		return learningResult;
//        	}
//    		GenericDAO genericDAO = daoFactory.getGenericDAO();
//            BottomClauseConstructionDAO bottomClauseConstructionDAO = daoFactory.getBottomClauseConstructionDAO();
//            
//        	// Generate and compile stored procedures
//        	if (this.parameters.isCreateStoredProcedure()) {
//	        	success = this.compileStoredProcedures();
//	        	if (!success) {
//	        		learningResult.setSuccess(false);
//	        		learningResult.setErrorMessage("Error creating stored procedures");
//	        		return learningResult;
//	        	}
//        	}
//            
//	        // Get examples relations
//	        Relation posTrain = this.schema.getRelations().get(postrainTableName);
//	      	Relation negTrain = this.schema.getRelations().get(negtrainTableName);
//	      	Relation posTest = this.schema.getRelations().get(postestTableName);
//	        Relation negTest = this.schema.getRelations().get(negtestTableName);
//	        
//            tw.reset();
//            logger.info("Creating coverage engine...");
//            CoverageEngine coverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, posTrain, negTrain, this.dataModel.getSpName(), this.parameters.getIterations(), this.parameters.getRecall(), this.parameters.getMaxterms(), this.parameters.getThreads(), true);
//         // Use following line for demo of Castor using HIV-Small
////          CoverageEngine coverageEngine = new CoverageBySubsumptionOptimizedParallel1_HIVSmallHardcoded(genericDAO, bottomClauseConstructionDAO, posTrain, negTrain, this.dataModel.getSpName(), this.parameters.getIterations(), this.parameters.getRecall(), this.parameters.getMaxterms(), this.parameters.getThreads(), true, posExamplesFlags, negExamplesFlags);
//            NumbersKeeper.creatingCoverageTime = tw.time();
//            
//            CastorLearner learner = new CastorLearner(genericDAO, bottomClauseConstructionDAO, coverageEngine, this.parameters);
//            
//            // Learn
//            logger.info("Learning...");
//            List<ClauseInfo> definition = learner.learn(this.schema, this.dataModel.getModeH(), this.dataModel.getModesB(), posTrain, negTrain, this.dataModel.getSpName());
//            NumbersKeeper.totalTime += tw.time();
//            
//            logger.info("Total time: " + NumbersKeeper.totalTime);
//            logger.info("Creating coverage engine time: " + NumbersKeeper.creatingCoverageTime);
//            logger.info("Learning time: " + NumbersKeeper.learningTime);
//            logger.info("Coverage time: " + NumbersKeeper.coverageTime);
//            logger.info("Coverage calls: " + NumbersKeeper.coverageCalls);
//            logger.info("Scoring time: " + NumbersKeeper.scoringTime);
//            logger.info("Entailment time: " + NumbersKeeper.entailmentTime);
//            logger.info("Minimization time: " + NumbersKeeper.minimizationTime);
//            logger.info("Reduction time: " + NumbersKeeper.reducerTime);
//            
//            // Evaluate
//            logger.info("Evaluating on testing data...");
//            CoverageEngine testCoverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, posTest, negTest, this.dataModel.getSpName(), this.parameters.getIterations(), this.parameters.getRecall(), this.parameters.getMaxterms(), this.parameters.getThreads(), true);
//            // Use following line for demo of Castor using HIV-Small
////            CoverageEngine testCoverageEngine = new CoverageBySubsumptionOptimizedParallel1_HIVSmallHardcoded(genericDAO, bottomClauseConstructionDAO, posTrain, negTrain, this.dataModel.getSpName(), this.iterations, this.recall, this.maxterms, this.threads, true, posExamplesFlags, negExamplesFlags);
//            EvaluationResult testEvaluationResult = learner.evaluate(testCoverageEngine, this.schema, definition, posTest, negTest);
//            
//            // Set learning result
//            learningResult.setSuccess(true);
//            double accuracy = testEvaluationResult.getAccuracy();
//            if (Double.isNaN(accuracy))
//            	accuracy = 0;
//            double precision = testEvaluationResult.getPrecision();
//            if (Double.isNaN(precision))
//            	precision = 0;
//            double recall = testEvaluationResult.getRecall();
//            if (Double.isNaN(recall))
//            	recall = 0;
//            double f1 = testEvaluationResult.getF1();
//            if (Double.isNaN(f1))
//            	f1 = 0;
//            learningResult.setAccuracy(accuracy);
//            learningResult.setPrecision(precision);
//            learningResult.setRecall(recall);
//            learningResult.setF1(f1);
//            learningResult.setTime(NumbersKeeper.totalTime/1000);//transforming to seconds
//            learningResult.setDefinition(MyClauseToClauseAsString.parseDefinition(definition));
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            learningResult.setSuccess(false);
//            learningResult.setErrorMessage(e.getMessage());
//        }
//        finally {
//        	// Close connection to DBMS
//        	daoFactory.closeConnection();
//        }
//        
//        return learningResult;
//	}
//	
//	/*
//	 * Read data model from JSON object
//	 */
//	private boolean readParametersFromJson(JsonObject parametersJson) {
//		boolean success;
//		try {
//			logger.info("Reading parameters...");
//			parameters = JsonSettingsReader.readParameters(parametersJson);
//			success = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			success = false;
//		}
//		return success;
//	}
//	
//	/*
//	 * Read data model from JSON object
//	 */
//	private boolean readDataModelFromJson(JsonObject dataModelJson) {
//		boolean success;
//		try {
//			logger.info("Reading data model...");
//			dataModel = JsonSettingsReader.readDataModel(dataModelJson);
//			success = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			success = false;
//		}
//		return success;
//	}
//	
//	/*
//	 * Read schema from JSON object
//	 */
//	private boolean readSchemaFromJson(JsonObject schemaJson) {
//		boolean success;
//		try {
//			logger.info("Reading schema...");
//			schema = JsonSettingsReader.readSchema(schemaJson);
//			success = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			success = false;
//		}
//		return success;
//	}
//	
//	/*
//	 * Call method to generate, compile, and create stored procedures
//	 */
//	private boolean compileStoredProcedures() {
//		boolean success;
//		try {
//			StoredProcedureGeneratorSaturationInsideSP spGenerator = new StoredProcedureGeneratorSaturationInsideSP();
//			success = spGenerator.generateAndCompileStoredProcedures("sp", this.dataModel.getSpName(), this.parameters.getIterations(), this.schema, this.dataModel.getModeH(), this.dataModel.getModesB(), this.parameters.isUseInds());
//		} catch (Exception e) {
//			e.printStackTrace();
//			success = false;
//		}
//		return success;
//	}
}
