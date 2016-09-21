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
import castor.algorithms.coverageengines.CoverageByDBJoiningAllSingleExample;
import castor.algorithms.coverageengines.CoverageBySubsumptionParallel;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.db.DBCommons;
import castor.db.StoredProcedureGeneratorSaturationInsideSP;
import castor.db.dataaccess.BottomClauseConstructionDAO;
import castor.db.dataaccess.DAOFactory;
import castor.db.dataaccess.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.inputdecoders.CastorDecoder;
import castor.inputdecoders.InputDecoder;
import castor.language.Mode;
import castor.language.Relation;
import castor.language.Schema;
import castor.utils.TimeKeeper;
import castor.utils.TimeWatch;

public class CastorCmd {
	
	// Options
	@Option(name="-sp",usage="Create stored procedure")
    private boolean createStoredProcedure = true;
	
	@Option(name="-settings",usage="Settings file",required=true)
    private String settingsFile;
	
	@Option(name="-schema",usage="Schema file",required=true)
    private String schemaFile;
	
	@Option(name="-target",usage="Target relation",required=true)
    private String target;
	
	@Option(name="-minprec",usage="Minimum precision")
    private double minPrecision = 0.5;
	
	@Option(name="-minrec",usage="Minimum recall")
    private double minRecall = 0;
	
	@Option(name="-sample",usage="Sample size")
    private int sample = 1;
	
	@Option(name="-beam",usage="Beam size")
    private int beam = 1;
	
	@Option(name="-threads",usage="Numbers of threads")
    private int threads = 1;
	
	@Option(name="-minimize",usage="Minimize bottom clause.")
    private boolean minimizeBottomClause = false;
	
	@Option(name="-sat",usage="Only build bottom clause for example given in option e.")
    private boolean saturation = false;
	
	@Option(name="-groundsat",usage="Only ground build bottom clause for example given in option e.")
    private boolean groundSaturation = false;
	
	@Option(name="-e",usage="Example to build bottom clause for (only when using sat or groundsat options)")
    private int exampleForSaturation = 0;
	
	@Option(name="-reduction",usage="Reduction method to use. Options: precision (default), consistency, none.")
    private String reductionMethod = CastorLearner.NEGATIVE_REDUCTION_PRECISION;
	
	@Option(name="-useInds",usage="Use inclusion dependencies in bottom clause construction, if available.")
    private boolean applyInds = true;
	
	@Argument
    private List<String> arguments = new ArrayList<String>();

	// Logger
	private static Logger logger = Logger.getLogger(CastorCmd.class);
	
	// Settings
	private String dbURL;
	
	// Language restriction
	private int iterations;
	private int recall;
	private int maxterms;
	private String spName;
	private Mode modeH;
	private List<Mode> modesB;
	
	private Schema schema;

	public static void main(String[] args) {
		CastorCmd program = new CastorCmd();
		program.run(args);
	}
	
	public void run(String[] args) {
		TimeWatch tw = TimeWatch.start();
		
		// Parse the arguments
        try {
        	CmdLineParser parser = new CmdLineParser(this);
			parser.parseArgument(args);
            if (!this.reductionMethod.equals(CastorLearner.NEGATIVE_REDUCTION_NONE) &&
            		!this.reductionMethod.equals(CastorLearner.NEGATIVE_REDUCTION_CONSISTENCY) &&
            		!this.reductionMethod.equals(CastorLearner.NEGATIVE_REDUCTION_PRECISION)) {
            	throw new IllegalArgumentException("Incorrect reduction method. Options: precision (default), consistency, none.");
            }
        } catch (CmdLineException e) {
//			e.printStackTrace();
			logger.error(e.getMessage());
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return;
		}
        
		String postrainTableName = target+DBCommons.TRAIN_POS_SUFFIX;
		String negtrainTableName = target+DBCommons.TRAIN_NEG_SUFFIX;
		String postestTableName = target+DBCommons.TEST_POS_SUFFIX;
		String negtestTableName = target+DBCommons.TEST_NEG_SUFFIX;
		
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.VOLTDB);
        try {
        	boolean success;
        	
        	// Read settings and language restriction
        	success = this.encodeInput();
        	if (!success) {
        		return;
        	}
        	
        	// Create data access objects and set URL of data
        	try {
        		daoFactory.initConnection(this.dbURL);
        	}
        	catch (RuntimeException e) {
        		logger.error("Unable to connect to server with URL: " + this.dbURL);
        		return;
        	}
        	daoFactory.initConnection(this.dbURL);
    		GenericDAO genericDAO = daoFactory.getGenericDAO();
            BottomClauseConstructionDAO bottomClauseConstructionDAO = daoFactory.getBottomClauseConstructionDAO();
        	
        	// Generate and compile stored procedures
        	if (this.createStoredProcedure) {
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
            CoverageEngine coverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, posTrain, negTrain, this.spName, this.iterations, this.recall, this.maxterms, this.threads, createFullCoverageEngine);
//            CoverageEngine coverageEngine = new CoverageByDBJoiningAllSingleExample(genericDAO, posTrain, negTrain);
            TimeKeeper.creatingCoverageTime = tw.time();
            
            CastorLearner castor = new CastorLearner(genericDAO, bottomClauseConstructionDAO, coverageEngine, this.minPrecision, this.minRecall, this.minimizeBottomClause);
            
            if (saturation) {
            	// BOTTOM CLAUSE
            	BottomClauseUtil.generateBottomClauseForExample(BottomClauseUtil.ALGORITHMS.INSIDE_STORED_PROCEDURE, genericDAO, bottomClauseConstructionDAO, coverageEngine.getAllPosExamples().get(this.exampleForSaturation), this.schema, this.modeH, this.modesB, this.iterations, this.spName, this.recall, this.maxterms);
            } else if (groundSaturation) {
            	// GROUND BOTTOM CLAUSE
            	BottomClauseUtil.generateGroundBottomClauseForExample(BottomClauseUtil.ALGORITHMS.INSIDE_STORED_PROCEDURE, genericDAO, bottomClauseConstructionDAO, coverageEngine.getAllPosExamples().get(this.exampleForSaturation), this.schema, this.modeH, this.modesB, this.iterations, this.spName, this.recall, this.maxterms);
            } else {
	            // LEARN
	            logger.info("Learning...");
	            List<ClauseInfo> definition = castor.learn(this.schema, this.modeH, this.modesB, posTrain, negTrain, this.spName, this.iterations, this.recall, this.maxterms, this.sample, this.beam, this.reductionMethod);
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
	            CoverageEngine testCoverageEngine = new CoverageBySubsumptionParallel(genericDAO, bottomClauseConstructionDAO, posTest, negTest, this.spName, this.iterations, this.recall, this.maxterms, this.threads, true);
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
	
	private boolean encodeInput() {
		boolean success = true;
		
		logger.info("Reading settings and language restriction from file...");
		
		InputDecoder encoder = new CastorDecoder();
		
		if (encoder.readSchemaFileFromSql(schemaFile) &&
				encoder.readUserInputFile(settingsFile)) {
			this.schema = encoder.getSchema();
			this.dbURL = encoder.getServerName();
			this.iterations = encoder.getIterations();
			this.recall = encoder.getRecall();
			this.maxterms = encoder.getMaxTerms();
			this.spName = encoder.getSPNameTemplate();
			this.modeH = encoder.getModeH();
			this.modesB = encoder.getModesB();
		} else {
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
		boolean success = spGenerator.generateAndCompileStoredProcedures("sp", this.spName, this.iterations, this.schema, this.modeH, this.modesB, this.applyInds);
		return success;
	}
}
