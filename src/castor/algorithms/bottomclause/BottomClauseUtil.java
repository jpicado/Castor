package castor.algorithms.bottomclause;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import castor.db.dataaccess.BottomClauseConstructionDAO;
import castor.db.dataaccess.GenericDAO;
import castor.hypotheses.MyClause;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.utils.Formatter;
import castor.utils.TimeWatch;

public class BottomClauseUtil {

	public enum ALGORITHMS {
		ORIGINAL,
		INSIDE_STORED_PROCEDURE
	}
	
	// Logger
	private static Logger logger = Logger.getLogger(BottomClauseUtil.class);
	
	/*
	 * Generate bottom clause for a specific example
	 */
	public static MyClause generateBottomClauseForExample(BottomClauseUtil.ALGORITHMS bottomClauseAlgorithm, GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema, Mode modeH, List<Mode> modesB, int iterations, String spName, int recall, int maxterms, boolean applyInds) {
		MyClause clause = null;
		TimeWatch watch;
		
		logger.info("Generating bottom clause for example <" + String.join(",", exampleTuple.getValues()) + ">...");
		watch = TimeWatch.start();
		
		if (bottomClauseAlgorithm == BottomClauseUtil.ALGORITHMS.ORIGINAL) {
			BottomClauseGeneratorOriginalAlgorithm saturator = new BottomClauseGeneratorOriginalAlgorithm();
			clause = saturator.generateBottomClause(genericDAO, exampleTuple, schema, modeH, modesB, iterations, recall, applyInds);
		} else if (bottomClauseAlgorithm == BottomClauseUtil.ALGORITHMS.INSIDE_STORED_PROCEDURE) {
			BottomClauseGeneratorInsideSP saturator = new BottomClauseGeneratorInsideSP();
			clause = saturator.generateBottomClause(bottomClauseConstructionDAO, exampleTuple, spName, iterations, recall, maxterms);
		} else {
			throw new IllegalArgumentException("Unsupported algorithm.");
		}
		
		logger.info("Bottom clause: \n"+ Formatter.prettyPrint(clause));
		logger.info("Literals: " + clause.getNumberLiterals());
		logger.info("Saturation time: " + watch.time(TimeUnit.MILLISECONDS) + " milliseconds.");
			
		return clause;
	}
	
	/*
	 * Generate ground bottom clause for a specific example
	 */
	public static String generateGroundBottomClauseForExample(BottomClauseUtil.ALGORITHMS bottomClauseAlgorithm, GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema, Mode modeH, List<Mode> modesB, int iterations, String spName, int recall, int maxterms) {
		String clause = "";
		TimeWatch watch;
		
		// Generate bottom clause
		logger.info("Generating ground bottom clause for example <" + String.join(",", exampleTuple.getValues()) + ">...");
		watch = TimeWatch.start();
		
		if (bottomClauseAlgorithm == BottomClauseUtil.ALGORITHMS.INSIDE_STORED_PROCEDURE) {
			BottomClauseGeneratorInsideSP saturator = new BottomClauseGeneratorInsideSP();
			clause = saturator.generateGroundBottomClauseString(bottomClauseConstructionDAO, exampleTuple, spName, iterations, recall, maxterms);
		} else {
			//TODO implement ground bottom clause generation for original algorithm
			throw new IllegalArgumentException("Unsupported algorithm.");
		}
		
		logger.info("Ground bottom clause: \n"+ clause);
		logger.info("Saturation time: " + watch.time(TimeUnit.MILLISECONDS) + " milliseconds.");
		
		return clause;
	}
}
