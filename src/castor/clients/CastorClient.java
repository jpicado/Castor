package castor.clients;

import java.util.List;

import org.apache.log4j.Logger;

import castor.algorithms.CastorLearner;
import castor.algorithms.coverageengines.CoverageBySubsumptionParallel;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.db.DBCommons;
import castor.db.StoredProcedureGeneratorSaturationInsideSP;
import castor.db.dataaccess.BottomClauseConstructionDAO;
import castor.db.dataaccess.DAOFactory;
import castor.db.dataaccess.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.inputdecoders.JsonDecoder;
import castor.language.DataModel;
import castor.language.Relation;
import castor.language.Schema;
import castor.mappings.MyClauseToClauseAsString;
import castor.utils.FileUtils;
import castor.utils.TimeKeeper;
import castor.utils.TimeWatch;
import castor.wrappers.EvaluationResult;
import castor.wrappers.LearningResult;

import com.google.gson.JsonObject;

public class CastorClient {

	private boolean createStoredProcedure = true;
	private double minPrecision = 0.5;
	private double minRecall = 0;
	private int sample = 1;
	private int beam = 1;
	private int threads = 1;
	private boolean minimizeBottomClause = false;
	private String reductionMethod = CastorLearner.NEGATIVE_REDUCTION_PRECISION;
	private int iterations = 2;
	private int recall = 10;
	private int maxterms = 1000;
	private boolean applyInds = true;
	private String dbURL = "localhost";
	
	private Schema schema;
	private DataModel dataModel;
	
	// Logger
	private static Logger logger = Logger.getLogger(CastorClient.class);
	
	public static void main(String[] args) {
		/*String dataModelFile = "/Users/jose/Documents/workspaceOSU/ProGolem-In-DB/ProGolem-In-DB/inputfiles/uwcse/set1/datamodel-uwcse-set1-schema1.json";
		String schemaFile = "/Users/jose/Documents/workspaceOSU/ProGolem-In-DB/ProGolem-In-DB/inputfiles/uwcse/set1/schema-uwcse-set1-schema1.json";
		String parametersFile = "/Users/jose/Documents/workspaceOSU/ProGolem-In-DB/ProGolem-In-DB/inputfiles/uwcse/set1/parameters.json";
		CastorClient cs = new CastorClient();

		JsonObject schemaJson = convertFileToJSON(schemaFile);
		JsonObject dataModelJson = convertFileToJSON(dataModelFile);
		JsonObject parametersJson = convertFileToJSON(parametersFile);
		
		cs.learn("localhost", 21212, "", schemaJson, dataModelJson, parametersJson);*/
		
		CastorClient cs = new CastorClient();
		cs.learn("hiv", "11111111111111111111111111111111111111111111111111", "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
//		LearningResult res = cs.test();
//		System.out.println(res.getSchema());
	}
	
	public LearningResult test() {
		LearningResult learningResult = new LearningResult();
		learningResult.setSuccess(false);
		learningResult.setAlgorithm("Castor");
		
		JsonObject jsonObject = null;
        try {
        	jsonObject = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream("/resources/uwcse/schema-uwcse-set1-schema1.json"));
            learningResult.setSchema(jsonObject.get("name").getAsString());
        } catch (Exception e) {
        	e.printStackTrace();
        	learningResult.setErrorMessage(e.getMessage());
        }
		
		return learningResult;
	}
	
	
	
	public LearningResult learn(String dataset) {
		if (dataset.equals("uwcse")) {
			return learnUWCSE("", "");
		} else if (dataset.equals("hiv")) {
			return learnHIV("", "");
		}
		return new LearningResult();
	}
	
	public LearningResult learn(String dataset, String posExamplesFlags, String negExamplesFlags) {
		if (dataset.equals("uwcse")) {
			return learnUWCSE(posExamplesFlags, negExamplesFlags);
		} else if (dataset.equals("hiv")) {
			return learnHIV(posExamplesFlags, negExamplesFlags);
		}
		return new LearningResult();
	}
	
	public LearningResult learnUWCSE(String posExamplesFlags, String negExamplesFlags) {
		String schemaResource = "/resources/uwcse/schema-uwcse-set1-schema1.json";
		String dataModelResource = "/resources/uwcse/datamodel-uwcse-set1-schema1.json";
		String parametersResource = "/resources/uwcse/parameters.json";
		
		JsonObject schemaJson = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream(schemaResource));
		JsonObject dataModelJson = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream(dataModelResource));
		JsonObject parametersJson = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream(parametersResource));
		
		return learn(schemaJson, dataModelJson, parametersJson, posExamplesFlags, negExamplesFlags);
	}
	
	public LearningResult learnHIV(String posExamplesFlags, String negExamplesFlags) {
		String schemaResource = "/resources/hiv/schema-hiv-set2-schema1.json";
		String dataModelResource = "/resources/hiv/datamodel-hiv-set2-schema1.json";
		String parametersResource = "/resources/hiv/parameters.json";
		
		JsonObject schemaJson = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream(schemaResource));
		JsonObject dataModelJson = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream(dataModelResource));
		JsonObject parametersJson = FileUtils.convertStreamToJSON(this.getClass().getResourceAsStream(parametersResource));
	
		return learn(schemaJson, dataModelJson, parametersJson, posExamplesFlags, negExamplesFlags);
	}
	
	// default client port: 21212 
	public LearningResult learn(JsonObject schemaJson, JsonObject dataModelJson, JsonObject parametersJson, String posExamplesFlags, String negExamplesFlags) {
		TimeWatch tw = TimeWatch.start();
		boolean success;
		LearningResult learningResult = new LearningResult();
		learningResult.setAlgorithm("Castor");
		
		// Get parameters from JSON object
		this.decodeParametersFromJson(parametersJson);
		
		// Get schema from JSON object
		success = this.decodeSchemaFromJson(schemaJson);
    	if (!success) {
    		learningResult.setSuccess(false);
    		learningResult.setErrorMessage("Error reading schema");
    		return learningResult;
    	}
    	learningResult.setSchema(schema.getName());
    	
		// Get data model from JSON object
    	success = this.decodeDataModelFromJson(dataModelJson);
    	if (!success) {
    		learningResult.setSuccess(false);
    		learningResult.setErrorMessage("Error reading data model");
    		return learningResult;
    	}
		
		String postrainTableName = this.dataModel.getTarget()+DBCommons.TRAIN_POS_SUFFIX;
		String negtrainTableName = this.dataModel.getTarget()+DBCommons.TRAIN_NEG_SUFFIX;
		String postestTableName = this.dataModel.getTarget()+DBCommons.TEST_POS_SUFFIX;
		String negtestTableName = this.dataModel.getTarget()+DBCommons.TEST_NEG_SUFFIX;
		
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.VOLTDB);
        try {
        	// Create data access objects and set URL of data
        	try {
        		daoFactory.initConnection(this.dbURL);
        	} catch(RuntimeException e) {
        		learningResult.setSuccess(false);
        		learningResult.setErrorMessage("Unable to connect to server with URL: " + this.dbURL + "\n" + e.getMessage());
        		logger.error(learningResult.getErrorMessage());
        		return learningResult;
        	}
    		GenericDAO genericDAO = daoFactory.getGenericDAO();
            BottomClauseConstructionDAO bottomClauseConstructionDAO = daoFactory.getBottomClauseConstructionDAO();
            
        	// Generate and compile stored procedures
        	if (this.createStoredProcedure) {
	        	success = this.compileStoredProcedures();
	        	if (!success) {
	        		learningResult.setSuccess(false);
	        		learningResult.setErrorMessage("Error creating stored procedures");
	        		return learningResult;
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
            CoverageEngine coverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, posTrain, negTrain, this.dataModel.getSpName(), this.iterations, this.recall, this.maxterms, this.threads, true);
         // Use following line for demo of Castor using HIV-Small
//          CoverageEngine coverageEngine = new CoverageBySubsumptionOptimizedParallel1_HIVSmallHardcoded(genericDAO, bottomClauseConstructionDAO, posTrain, negTrain, this.dataModel.getSpName(), this.iterations, this.recall, this.maxterms, this.threads, true, posExamplesFlags, negExamplesFlags);
            TimeKeeper.creatingCoverageTime = tw.time();
            
            CastorLearner learner = new CastorLearner(genericDAO, bottomClauseConstructionDAO, coverageEngine, this.minPrecision, this.minRecall, this.minimizeBottomClause);
//            ProGolem learner = new ProGolem(genericDAO, coverageEngine, this.minPrecision, this.minRecall, this.minimizeBottomClause);
            
            // Learn
            logger.info("Learning...");
            List<ClauseInfo> definition = learner.learn(this.schema, this.dataModel.getModeH(), this.dataModel.getModesB(), posTrain, negTrain, this.dataModel.getSpName(), this.iterations, this.recall, this.maxterms, this.sample, this.beam, this.reductionMethod);
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
            
            // Evaluate
            logger.info("Evaluating on testing data...");
            CoverageEngine testCoverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, posTest, negTest, this.dataModel.getSpName(), this.iterations, this.recall, this.maxterms, this.threads, true);
            // Use following line for demo of Castor using HIV-Small
//            CoverageEngine testCoverageEngine = new CoverageBySubsumptionOptimizedParallel1_HIVSmallHardcoded(genericDAO, bottomClauseConstructionDAO, posTrain, negTrain, this.dataModel.getSpName(), this.iterations, this.recall, this.maxterms, this.threads, true, posExamplesFlags, negExamplesFlags);
            EvaluationResult testEvaluationResult = learner.evaluate(testCoverageEngine, this.schema, definition, posTest, negTest);
            
            // Set learning result
            learningResult.setSuccess(true);
            double accuracy = testEvaluationResult.getAccuracy();
            if (Double.isNaN(accuracy))
            	accuracy = 0;
            double precision = testEvaluationResult.getPrecision();
            if (Double.isNaN(precision))
            	precision = 0;
            double recall = testEvaluationResult.getRecall();
            if (Double.isNaN(recall))
            	recall = 0;
            double f1 = testEvaluationResult.getF1();
            if (Double.isNaN(f1))
            	f1 = 0;
            learningResult.setAccuracy(accuracy);
            learningResult.setPrecision(precision);
            learningResult.setRecall(recall);
            learningResult.setF1(f1);
            learningResult.setTime(TimeKeeper.totalTime/1000);//transforming to seconds
            learningResult.setDefinition(MyClauseToClauseAsString.parseDefinition(definition));
        }
        catch (Exception e) {
            e.printStackTrace();
            learningResult.setSuccess(false);
            learningResult.setErrorMessage(e.getMessage());
        }
        finally {
        	// Close connection to DBMS
        	daoFactory.closeConnection();
        }
        
        return learningResult;
	}
	
	
	
	private void decodeParametersFromJson(JsonObject parametersJson) {
		logger.info("Reading parameters...");
		if (parametersJson.get("createStoredProcedure") != null) {
			createStoredProcedure = parametersJson.get("createStoredProcedure").getAsBoolean();
		}
		if (parametersJson.get("minprec") != null) {
			minPrecision = parametersJson.get("minprec").getAsDouble();
		}
		if (parametersJson.get("minrec") != null) {
			minRecall = parametersJson.get("minrec").getAsDouble();
		}
		if (parametersJson.get("sample") != null) {
			sample = parametersJson.get("sample").getAsInt();
		}
		if (parametersJson.get("beam") != null) {
			beam = parametersJson.get("beam").getAsInt();
		}
		if (parametersJson.get("threads") != null) {
			threads = parametersJson.get("threads").getAsInt();
		}
		if (parametersJson.get("minimizeBottomClause") != null) {
			minimizeBottomClause = parametersJson.get("minimizeBottomClause").getAsBoolean();
		}
		if (parametersJson.get("reductionMethod") != null) {
			reductionMethod = parametersJson.get("reductionMethod").getAsString();
		}
		if (parametersJson.get("iterations") != null) {
			iterations = parametersJson.get("iterations").getAsInt();
		}
		if (parametersJson.get("recall") != null) {
			recall = parametersJson.get("recall").getAsInt();
		}
		if (parametersJson.get("maxterms") != null) {
			maxterms = parametersJson.get("maxterms").getAsInt();
		}
		if (parametersJson.get("useInds") != null) {
			applyInds = parametersJson.get("useInds").getAsBoolean();
		}
		if (parametersJson.get("dbURL") != null) {
			dbURL = parametersJson.get("dbURL").getAsString();
		}
	}
	
	/*
	 * Read data model from JSON object
	 */
	private boolean decodeDataModelFromJson(JsonObject dataModelJson) {
		boolean success;
		try {
			logger.info("Reading data model...");
			dataModel = JsonDecoder.decodeDataModel(dataModelJson);
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
	private boolean decodeSchemaFromJson(JsonObject schemaJson) {
		boolean success;
		try {
			logger.info("Reading schema...");
			schema = JsonDecoder.decodeSchema(schemaJson);
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
	private boolean compileStoredProcedures() {
		boolean success;
		try {
			StoredProcedureGeneratorSaturationInsideSP spGenerator = new StoredProcedureGeneratorSaturationInsideSP();
			success = spGenerator.generateAndCompileStoredProcedures("sp", this.dataModel.getSpName(), this.iterations, this.schema, this.dataModel.getModeH(), this.dataModel.getModesB(), this.applyInds);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
}
