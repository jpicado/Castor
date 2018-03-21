package castor.clients;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import castor.algorithms.CastorLearner;
import castor.algorithms.Learner;
import castor.algorithms.bottomclause.BottomClauseGenerator;
import castor.algorithms.bottomclause.BottomClauseGeneratorInsideSP;
import castor.algorithms.bottomclause.BottomClauseGeneratorNaiveSampling;
import castor.algorithms.bottomclause.BottomClauseGeneratorNaiveSamplingWithSimilarity;
import castor.algorithms.bottomclause.BottomClauseGeneratorStratifiedSampling;
import castor.algorithms.bottomclause.BottomClauseGeneratorStratifiedSamplingWithSimilarity;
import castor.algorithms.bottomclause.BottomClauseGeneratorStreamSampling;
import castor.algorithms.bottomclause.BottomClauseGeneratorWithGroupedModesOlkenSampling;
import castor.algorithms.bottomclause.StoredProcedureGeneratorSaturationInsideSP;
import castor.algorithms.coverageengines.CoverageBySubsumptionParallel;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.DAOFactory;
import castor.dataaccess.db.GenericDAO;
import castor.db.DBCommons;
import castor.db.QueryGenerator;
import castor.hypotheses.ClauseInfo;
import castor.language.InclusionDependency;
import castor.language.Mode;
import castor.language.Relation;
import castor.language.Schema;
import castor.sampling.StatisticsExtractor;
import castor.sampling.StatisticsOlkenSampling;
import castor.sampling.StatisticsStreamSampling;
import castor.settings.DataModel;
import castor.settings.JsonSettingsReader;
import castor.settings.Parameters;
import castor.settings.SamplingMethods;
import castor.utils.FileUtils;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;
import castor.wrappers.LearningResult;

public class CastorClient2 {
	
	private Parameters parameters;
	private Schema schema;
	private DataModel dataModel;

	public static void main(String args[]) {		
		CastorClient2 cc = new CastorClient2();
		cc.learn("uwcse", false);
	}
	
	public LearningResult learn(String dataset, boolean createSPs) {
		CastorCmd program = new CastorCmd();
		
		String parameters;
		String inds;
		String dataModel;
		String trainPosSuffix;
		String trainNegSuffix;
		String testPosSuffix;
		String testNegSuffix;
		
		if (dataset.equals("uwcse")) {
			if (createSPs)
				parameters = "/Users/jose/Box Sync/Castor/comparisons/Castor/Castor-ICDE-demo/uwcse/castor-input/parameters-naive-createsps.json";
			else
				parameters = "/Users/jose/Box Sync/Castor/comparisons/Castor/Castor-ICDE-demo/uwcse/castor-input/parameters-naive-sps.json";
			inds = "/Users/jose/Box Sync/Castor/comparisons/Castor/Castor-ICDE-demo/uwcse/castor-input/inds-schema1.json";
			dataModel = "/Users/jose/Box Sync/Castor/comparisons/Castor/Castor-ICDE-demo/uwcse/castor-input/datamodel-schema1.json";
			trainPosSuffix = "_FOLD1_TRAIN_POS";
			trainNegSuffix = "_FOLD1_TRAIN_NEG";
			testPosSuffix = "_FOLD1_TEST_POS";
			testNegSuffix = "_FOLD1_TEST_NEG";
		} else {
			throw new IllegalArgumentException("Dataset not supported.");
		}
		
		String[] uwcseArgs = {
				"-parameters", parameters,
				"-inds", inds,
				"-dataModel", dataModel,
				"-trainPosSuffix", trainPosSuffix,
				"-trainNegSuffix", trainNegSuffix,
				"-testPosSuffix", testPosSuffix,
				"-testNegSuffix", testNegSuffix,
				"-test",
				};
		
		LearningResult learningResult = program.run(uwcseArgs);
		return learningResult;
	}
	
	public void run(String parametersFile, String indsFile, String dataModelFile, boolean outputSQL, boolean testLearnedDefinition) {
		TimeWatch tw = TimeWatch.start();
		boolean success;
		
		// Get parameters from file
		JsonObject parametersJson = FileUtils.convertFileToJSON(parametersFile);
		parameters = this.readParametersFromJson(parametersJson);

		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.VOLTDB);
		try {
			// Create data access objects and set URL of data
			try {
				String url = this.parameters.getDbURL() + ":" + this.parameters.getPort();
				daoFactory.initConnection(url);
			} catch (RuntimeException e) {
				System.err.println("Unable to connect to server with URL: " + this.parameters.getDbURL());
				return;
			}
			GenericDAO genericDAO = daoFactory.getGenericDAO();
			BottomClauseConstructionDAO bottomClauseConstructionDAO = daoFactory.getBottomClauseConstructionDAO();

			// Read from DB
			schema = genericDAO.getSchema();
			
			// If INDs were specified either on IND file or DDL file, read them
			JsonObject indsJson = FileUtils.convertFileToJSON(indsFile);
			this.readINDsFromJson(indsJson);

			// Get data model from file
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
			
			// Get examples from DB
			examplesSource = CoverageBySubsumptionParallel.EXAMPLES_SOURCE.DB;
			
			String posTrainTableName = (this.dataModel.getModeH().getPredicateName() + DBCommons.TRAIN_POS_SUFFIX).toUpperCase();
			String negTrainTableName = (this.dataModel.getModeH().getPredicateName() + DBCommons.TRAIN_NEG_SUFFIX).toUpperCase();
			
			posTrain = this.schema.getRelations().get(posTrainTableName);
			negTrain = this.schema.getRelations().get(negTrainTableName);

			// Check that tables containing examples exist in schema
			if (posTrain == null || negTrain == null) {
				throw new IllegalArgumentException(
						"One or more tables containing training examples do not exist in the schema: "
								+ posTrainTableName + ", " + negTrainTableName +
								"\nMake sure that tables exist in the database or specify path of files contaning examples.");
			}
			
			this.validateExamplesRelations(posTrain, negTrain);

			// Generate and compile stored procedures
			if (this.parameters.isCreateStoredProcedure()) {
				success = this.compileStoredProcedures();
				if (!success) {
					return;
				}
			}
			
			// Create saturator
			BottomClauseGenerator saturator;
			BottomClauseGenerator coverageEngineSaturator;
			System.out.println("Preprocessing...");
			tw.reset();
			if (parameters.isUseStoredProcedure()) {
				if (parameters.isAllowSimilarity() ||
						parameters.getSamplingMethod().equals(SamplingMethods.OLKEN) ||
						parameters.getSamplingMethod().equals(SamplingMethods.STREAM) ||
						parameters.getSamplingMethod().equals(SamplingMethods.STRATIFIED)) {
					throw new UnsupportedOperationException("Sampling method or simliarity not supported inside stored procedure.");
				} else {
					saturator = new BottomClauseGeneratorInsideSP();
					coverageEngineSaturator = new BottomClauseGeneratorInsideSP();
				}
			} else {
				if (parameters.isAllowSimilarity()) {
					//TODO implement other sampling methods
					if (parameters.getSamplingMethod().equals(SamplingMethods.OLKEN) ||
							parameters.getSamplingMethod().equals(SamplingMethods.STREAM))  {
						throw new UnsupportedOperationException("Sampling method not supported when allowing similarity.");
					} else if (parameters.getSamplingMethod().equals(SamplingMethods.STRATIFIED)) {
						saturator = new BottomClauseGeneratorStratifiedSamplingWithSimilarity(genericDAO, schema, parameters.getRandomSeed());
						coverageEngineSaturator = new BottomClauseGeneratorStratifiedSamplingWithSimilarity(genericDAO, schema, parameters.getRandomSeed());
					} else {
						saturator = new BottomClauseGeneratorNaiveSamplingWithSimilarity(genericDAO, schema, true, parameters.getRandomSeed());
						coverageEngineSaturator = new BottomClauseGeneratorNaiveSamplingWithSimilarity(genericDAO, schema, parameters.isSampleGroundBottomClauses(), parameters.getRandomSeed());
					}
				} else {
					saturator = getNewCoverageEngine(genericDAO);
					
					if (parameters.isSampleGroundBottomClauses()) {
						coverageEngineSaturator = getNewCoverageEngine(genericDAO);
					} else {
						coverageEngineSaturator = new BottomClauseGeneratorNaiveSampling(false, parameters.getRandomSeed());
					}
				}
			}
			NumbersKeeper.preprocessingTime = tw.time();
			
			// Create CoverageEngine
			tw.reset();
			System.out.println("Creating coverage engine...");
			System.out.println("Creating coverage engine...");
			boolean createFullCoverageEngine = true;
			CoverageEngine coverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, coverageEngineSaturator,
					posTrain, negTrain, this.schema, this.dataModel, this.parameters, createFullCoverageEngine,
					examplesSource, posTrainExamplesFile, negTrainExamplesFile);
//					CoverageEngine coverageEngine = new CoverageByDBJoiningAllSingleExample(genericDAO, posTrain, negTrain, parameters);
			
			System.out.println("Creating coverage engine for covering approach...");
			CoverageEngine coverageEngineForCoveringApproach;
			if (parameters.isSampleInCoveringApproach() == parameters.isSampleGroundBottomClauses()) {
				coverageEngineForCoveringApproach = coverageEngine;
			} else {
				BottomClauseGenerator coverageEngineSaturatorForCoveringApproach;
				if (parameters.isAllowSimilarity()) {
					coverageEngineSaturatorForCoveringApproach = new BottomClauseGeneratorNaiveSamplingWithSimilarity(genericDAO, schema, parameters.isSampleInCoveringApproach(), parameters.getRandomSeed());
				} else {
					coverageEngineSaturatorForCoveringApproach = new BottomClauseGeneratorNaiveSampling(parameters.isSampleInCoveringApproach(), parameters.getRandomSeed());
				}
				coverageEngineForCoveringApproach = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, coverageEngineSaturatorForCoveringApproach,
						posTrain, negTrain, this.schema, this.dataModel, this.parameters, createFullCoverageEngine,
						examplesSource, posTrainExamplesFile, negTrainExamplesFile);
			}
			NumbersKeeper.creatingCoverageTime = tw.time();
			
			
			// LEARN
			System.out.println("Learning...");
			Learner learner;
			learner = new CastorLearner(genericDAO, bottomClauseConstructionDAO, saturator, coverageEngine, coverageEngineForCoveringApproach, parameters, schema);
			
			List<ClauseInfo> definition = learner.learn(this.schema, this.dataModel, posTrain, negTrain, this.dataModel.getSpName(), false);

			List<String> sqlLines = new LinkedList<String>();
			if (outputSQL) {
				StringBuilder sb = new StringBuilder();
				sb.append("SQL format:\n");
				for (ClauseInfo clauseInfo : definition) {
					String clause = QueryGenerator.generateQueryFromClause(schema, clauseInfo);
					sb.append(clause+"\n");
					sqlLines.add(clause);
				}
				System.out.println(sb.toString());
			}
			
			// Save total time
			NumbersKeeper.totalTime += tw.time();
			
			// EVALUATE DEFINITION ON TRAINING DATA
			System.out.println("Evaluating on training data...");
			learner.evaluate(coverageEngine, this.schema, definition, posTrain, negTrain);

			// EVALUATE DEFINITION ON TESTING DATA
			if (testLearnedDefinition) {
				// Get examples from file or from DB
				Relation posTest;
				Relation negTest;
				CoverageBySubsumptionParallel.EXAMPLES_SOURCE examplesSourceTest;
				
				// If file names for examples are given, assume examples are in files
				String posTestExamplesFile = null;
				String negTestExamplesFile = null;
				
				// Get examples from DB
				examplesSourceTest = CoverageBySubsumptionParallel.EXAMPLES_SOURCE.DB;
				
				String posTestTableName = (this.dataModel.getModeH().getPredicateName() + DBCommons.TEST_POS_SUFFIX).toUpperCase();
				String negTestTableName = (this.dataModel.getModeH().getPredicateName() + DBCommons.TEST_NEG_SUFFIX).toUpperCase();
				
				posTest = this.schema.getRelations().get(posTestTableName);
				negTest = this.schema.getRelations().get(negTestTableName);

				// Check that tables containing examples exist in schema
				if (posTest == null || negTest == null) {
					throw new IllegalArgumentException(
							"One or more tables containing testing examples do not exist in the schema: "
									+ posTestTableName + ", " + negTestTableName +
									"\nMake sure that tables exist in the database or specify path of files contaning examples.");
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
					if (parameters.isAllowSimilarity()) {
						testSaturator = new BottomClauseGeneratorNaiveSamplingWithSimilarity(genericDAO, schema, true, parameters.getRandomSeed());
					} else {
						testSaturator = new BottomClauseGeneratorNaiveSampling(this.parameters.isSampleInTesting(), parameters.getRandomSeed());
					}
				}
				
				System.out.println("Evaluating on testing data...");
				CoverageEngine testCoverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, testSaturator, 
						posTest, negTest, this.schema, this.dataModel, this.parameters, true,
						examplesSourceTest, posTestExamplesFile, negTestExamplesFile);
				learner.evaluate(testCoverageEngine, this.schema, definition, posTest, negTest);
			}
			
			System.out.println("Total time: " + NumbersKeeper.totalTime);
			System.out.println("Creating coverage engine time: " + NumbersKeeper.creatingCoverageTime);
			System.out.println("Learning time: " + NumbersKeeper.learningTime);
			System.out.println("Coverage time: " + NumbersKeeper.coverageTime);
			System.out.println("Coverage calls: " + NumbersKeeper.coverageCalls);
			System.out.println("Scoring time: " + NumbersKeeper.scoringTime);
			System.out.println("Entailment time: " + NumbersKeeper.entailmentTime);
			System.out.println("Minimization time: " + NumbersKeeper.minimizationTime);
			System.out.println("Reduction time: " + NumbersKeeper.reducerTime);
			System.out.println("LGG time: " + NumbersKeeper.lggTime);
			System.out.println("LearnClause time: " + NumbersKeeper.learnClauseTime);
			System.out.println("Preprocessing time (extracting statistics, creating indexes, etc) (not included in total time): " + NumbersKeeper.preprocessingTime);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close connection to DBMS
			daoFactory.closeConnection();
		}
	}
	
	/*
	 * Create a new coverage engine based on parameters
	 */
	private BottomClauseGenerator getNewCoverageEngine(GenericDAO genericDAO) {
		BottomClauseGenerator saturator;
		if (parameters.getSamplingMethod().equals(SamplingMethods.OLKEN))  {
			System.out.println("Use Olken sampling. Extracting statistics from database instance...");
			StatisticsOlkenSampling statistics = StatisticsExtractor.extractStatisticsForOlkenSampling(genericDAO, schema);
			saturator = new BottomClauseGeneratorWithGroupedModesOlkenSampling(parameters.getRandomSeed(), statistics);
		} else if (parameters.getSamplingMethod().equals(SamplingMethods.STREAM)) {
			System.out.println("Use Stream sampling. Extracting statistics from database instance...");
			StatisticsStreamSampling statistics = StatisticsExtractor.extractStatisticsForStreamSampling(genericDAO, schema);
			saturator = new BottomClauseGeneratorStreamSampling(parameters.getRandomSeed(), statistics);
		} else if (parameters.getSamplingMethod().equals(SamplingMethods.STRATIFIED)) {
			saturator = new BottomClauseGeneratorStratifiedSampling(parameters.getRandomSeed());
		} else {
			saturator = new BottomClauseGeneratorNaiveSampling(true, parameters.getRandomSeed());
//					saturator = new BottomClauseGeneratorWithGroupedModesNaiveSampling(true);
		}
		return saturator;
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
			System.out.println("Reading parameters...");
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
			System.out.println("Reading data model...");
			dataModel = JsonSettingsReader.readDataModel(dataModelJson);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return dataModel;
	}

	/*
	 * Read INDs from JSON object
	 */
	private void readINDsFromJson(JsonObject dependenciesJson) {
		try {
			System.out.println("Reading inclusion dependencies...");
			Map<String, List<InclusionDependency>> inds = JsonSettingsReader.readINDs(dependenciesJson);
			schema.setInclusionDependencies(inds);
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
