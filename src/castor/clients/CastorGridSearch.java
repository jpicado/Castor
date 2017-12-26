package castor.clients;

import java.util.ArrayList;
import java.util.LinkedList;
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
import castor.algorithms.bottomclause.BottomClauseGenerator;
import castor.algorithms.bottomclause.BottomClauseGeneratorInsideSP;
import castor.algorithms.bottomclause.StoredProcedureGeneratorSaturationInsideSP;
import castor.algorithms.coverageengines.CoverageBySubsumptionParallel;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.DAOFactory;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.language.InclusionDependency;
import castor.language.Mode;
import castor.language.Relation;
import castor.language.Schema;
import castor.settings.DataModel;
import castor.settings.JsonSettingsReader;
import castor.settings.Parameters;
import castor.utils.FileUtils;
import castor.utils.Formatter;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;
import castor.wrappers.EvaluationResult;

import com.google.gson.JsonObject;

public class CastorGridSearch {
	
	public static final String ALGORITHM_CASTOR = "Castor";
	public static final String ALGORITHM_PROGOLEM = "ProGolem";
	public static final String ALGORITHM_GOLEM = "Golem";
	
	// Options
	@Option(name="-h",aliases = { "--help" })
    private boolean help = false;
	
	@Option(name="-parameters",usage="Parameters file",required=true)
    private String parametersFile;
	
	@Option(name="-gridSearch",usage="Grid search parameters file",required=true)
    private String gridSearchParametersFile;
	
	@Option(name="-schema",usage="Schema file (if not provided, schema is extracted from DB)",required=false)
    private String schemaFile = null;
	
	@Option(name="-inds",usage="INDs file",required=false)
    private String indsFile = null;
	
	@Option(name="-dataModel",usage="Data model file",required=true)
    private String dataModelFile;
	
	@Option(name="-algorithm",usage="Algorithm to run (Castor, Golem, ProGolem)",required=false)
    private String algorithm = ALGORITHM_CASTOR;
	
	@Option(name="-posTrain",usage="Name of table containing positive training examples",required=true)
    private String posTrainTableName;
	
	@Option(name="-posVal",usage="Name of table containing positive validation examples",required=true)
    private String posValTableName;
	
	@Option(name="-posTest",usage="Name of table containing positive testing examples",required=true)
    private String posTestTableName;
	
	@Option(name="-negTrain",usage="Name of table containing negative training examples",required=true)
    private String negTrainTableName;
	
	@Option(name="-negVal",usage="Name of table containing negative validation examples",required=true)
    private String negValTableName;
	
	@Option(name="-negTest",usage="Name of table containing negative testing examples",required=true)
    private String negTestTableName;
	
	@Argument
    private List<String> arguments = new ArrayList<String>();
	
	private Parameters parameters;
	private Schema schema;
	private DataModel dataModel;

	// Logger
	private static Logger logger = Logger.getLogger(CastorGridSearch.class);

	public static void main(String[] args) {
		CastorGridSearch program = new CastorGridSearch();
		program.run(args);
	}
	
	public void run(String[] args) {
		TimeWatch tw = TimeWatch.start();
		boolean success;
		
		// Parse the arguments
		CmdLineParser parser = new CmdLineParser(this);
        try {
			parser.parseArgument(args);
        } catch (CmdLineException e) {
			logger.error(e.getMessage());
			parser.printUsage(System.out);
			return;
		}
        
        if (help) {
        	parser.printUsage(System.out);
			return;
        }
        
        // Get parameters from file
        JsonObject parametersJson = FileUtils.convertFileToJSON(parametersFile);
 		parameters = this.readParametersFromJson(parametersJson);
 		
 		// Get grid search parameters from file
        JsonObject gridSearchParametersJson = FileUtils.convertFileToJSON(gridSearchParametersFile);
        Map<String,List<Double>> gridSearchParameters = JsonSettingsReader.readGridSearchParameters(gridSearchParametersJson);
		
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.VOLTDB);
        try {
        	// Create data access objects and set URL of data
        	try {
        		String url = this.parameters.getDbURL() + ":" + this.parameters.getPort();        		
        		daoFactory.initConnection(url);
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
        	
	        Relation posTrain = this.schema.getRelations().get(posTrainTableName.toUpperCase());
	      	Relation negTrain = this.schema.getRelations().get(negTrainTableName.toUpperCase());
	      	Relation posVal = this.schema.getRelations().get(posValTableName.toUpperCase());
	      	Relation negVal = this.schema.getRelations().get(negValTableName.toUpperCase());
	      	Relation posTest = this.schema.getRelations().get(posTestTableName.toUpperCase());
	        Relation negTest = this.schema.getRelations().get(negTestTableName.toUpperCase());
	        
	        // Check that tables containing examples exist in schema
	        if (posTrain == null || negTrain == null || posVal == null || negVal == null || posTest == null || negTest == null) {
	        	throw new IllegalArgumentException("One or more tables containing training or testing examples do not exist in the schema:\n"+posTrainTableName+"\n"+negTrainTableName+"\n"+posValTableName+"\n"+negValTableName+"\n"+posTestTableName+"\n"+negTestTableName);
	        }
	        
	        // Generate and compile stored procedures
        	if (this.parameters.isCreateStoredProcedure()) {
	        	success = this.compileStoredProcedures();
	        	if (!success) {
	        		return;
	        	}
        	}
	        
        	// Get list of minprec and minrec values
        	List<Double> minprecValues;
        	if (gridSearchParameters.containsKey("minprec")) { 
        		minprecValues = gridSearchParameters.get("minprec");
        	} else {
        		minprecValues = new LinkedList<Double>();
        		minprecValues.add(0.67);
        	}
        	List<Double> minrecValues;
        	if (gridSearchParameters.containsKey("minrec")) { 
        		minrecValues = gridSearchParameters.get("minrec");
        	} else {
        		minrecValues = new LinkedList<Double>();
        		minrecValues.add(0.0);
        	}
        	
        	// Create saturator
        	BottomClauseGenerator saturator = new BottomClauseGeneratorInsideSP();
        	
        	// Create CoverageEngines
        tw.reset();
        logger.info("Creating train coverage engine...");
        CoverageEngine coverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, saturator, posTrain, negTrain, this.schema, this.dataModel, this.parameters, true, CoverageBySubsumptionParallel.EXAMPLES_SOURCE.DB, "", "");
        long creatingCoverageTime = tw.time();
        
        logger.info("Creating validation coverage engine...");
        CoverageEngine valCoverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, saturator, posVal, negVal, this.schema, this.dataModel, this.parameters, true, CoverageBySubsumptionParallel.EXAMPLES_SOURCE.DB, "", "");
        
        logger.info("Creating test coverage engine...");
        CoverageEngine testCoverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, saturator, posTest, negTest, this.schema, this.dataModel, this.parameters, true, CoverageBySubsumptionParallel.EXAMPLES_SOURCE.DB, "", "");
            
            // Create learner
        	logger.info("Learning...");
        	Learner learner;
        	if (this.algorithm.equals(ALGORITHM_CASTOR)) {
        		learner = new CastorLearner(genericDAO, bottomClauseConstructionDAO, saturator, coverageEngine, parameters, schema);
        	} else if (this.algorithm.equals(ALGORITHM_GOLEM)) {
        		learner = new Golem(genericDAO, bottomClauseConstructionDAO, saturator, coverageEngine, parameters);
        	} else if (this.algorithm.equals(ALGORITHM_PROGOLEM)) {
        		learner = new ProGolem(genericDAO, bottomClauseConstructionDAO, saturator, coverageEngine, parameters);
        	} else {
        		throw new IllegalArgumentException("Learning algorithm " + this.algorithm + " not implemented.");
        	}
        	
        	// Learn with different grid search parameter values
        	double bestminprec = 0;
        	double bestminrec = 0;
        	double bestF1 = Double.MAX_VALUE*-1;
        	List<ClauseInfo> bestDefinition = new LinkedList<ClauseInfo>();
        	for (Double minprec : minprecValues) {
				for (Double minrec : minrecValues) {
					learner.getParameters().setMinPrecision(minprec);
					learner.getParameters().setMinRecall(minrec);
					
					logger.info("================================================================================================================================\n\n\n");
					logger.info("Learning with minprec="+minprec+" and minrec="+minrec);
					
					// Learn definition
					NumbersKeeper.reset();
					tw.reset();
					List<ClauseInfo> definition = learner.learn(this.schema, this.dataModel, posTrain, negTrain, this.dataModel.getSpName(), false);
					NumbersKeeper.totalTime += tw.time();
		            
		            logger.info("Total time: " + (NumbersKeeper.totalTime + creatingCoverageTime));
		            logger.info("Creating coverage engine time: " + creatingCoverageTime);
		            logger.info("Learning time: " + NumbersKeeper.learningTime);
		            logger.info("Coverage time: " + NumbersKeeper.coverageTime);
		            logger.info("Coverage calls: " + NumbersKeeper.coverageCalls);
		            logger.info("Scoring time: " + NumbersKeeper.scoringTime);
		            logger.info("Entailment time: " + NumbersKeeper.entailmentTime);
		            logger.info("Minimization time: " + NumbersKeeper.minimizationTime);
		            logger.info("Reduction time: " + NumbersKeeper.reducerTime);
		            logger.info("LGG time: " + NumbersKeeper.lggTime);
		            logger.info("LearnClause time: " + NumbersKeeper.learnClauseTime);
		            
		            // Evaluate definition
		            logger.info("Evaluating on validation data...");
		            EvaluationResult result = learner.evaluate(valCoverageEngine, this.schema, definition, posVal, negVal);
		            
		            if (result.getF1() >= bestF1) {
		            	bestF1 = result.getF1();
		            	bestminprec = minprec;
		            	bestminrec = minrec;
		            	bestDefinition.clear();
		            	bestDefinition.addAll(definition);
		            }
				}
			}
        	
        	// Evaluate definition on test data
        	logger.info("================================================================================================================================\n\n\n");
        	logger.info("Best minprec="+bestminprec+", best minrec="+bestminrec+", best F1="+bestF1);
        	
        	StringBuilder sb = new StringBuilder();
        	for (ClauseInfo clauseInfo : bestDefinition) {
        		sb.append(Formatter.prettyPrint(clauseInfo.getClause())+"\n");
        	}
        	logger.info("Best definition:\n"+sb.toString());
        	
            logger.info("Evaluating on testing data using best parameters...");
            learner.evaluate(testCoverageEngine, this.schema, bestDefinition, posTest, negTest);
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
		boolean success = spGenerator.generateAndCompileStoredProcedures(this.parameters.getDbURL(), this.parameters.getPort(), "sp", this.dataModel.getSpName(), this.parameters.getIterations(), this.schema, this.dataModel.getModeH(), this.dataModel.getModesB(), this.parameters.isUseInds());
		return success;
	}
}
