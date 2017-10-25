package castor.algorithms.bottomclause;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.MyClause;
import castor.language.Schema;
import castor.language.Tuple;
import castor.settings.DataModel;
import castor.settings.Parameters;
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
	public static MyClause generateBottomClauseForExample(BottomClauseUtil.ALGORITHMS bottomClauseAlgorithm, GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters) {
		MyClause clause = null;
		TimeWatch watch;
		
		logger.info("Generating bottom clause for example <" + String.join(",", exampleTuple.getValues()) + ">...");
		watch = TimeWatch.start();
		
		BottomClauseGenerator saturator;
		if (bottomClauseAlgorithm == BottomClauseUtil.ALGORITHMS.ORIGINAL) {
			saturator = new BottomClauseGeneratorOriginalAlgorithm();
		} else if (bottomClauseAlgorithm == BottomClauseUtil.ALGORITHMS.INSIDE_STORED_PROCEDURE) {
			saturator = new BottomClauseGeneratorInsideSP();
		} else {
			throw new IllegalArgumentException("Unsupported algorithm.");
		}
		clause = saturator.generateBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema, dataModel, parameters);
		
		logger.info("Bottom clause: \n"+ Formatter.prettyPrint(clause));
		logger.info("Literals: " + clause.getNumberLiterals());
		logger.info("Saturation time: " + watch.time(TimeUnit.MILLISECONDS) + " milliseconds.");
			
		return clause;
	}
	
	/*
	 * Generate ground bottom clause for a specific example
	 */
	public static String generateGroundBottomClauseForExample(BottomClauseUtil.ALGORITHMS bottomClauseAlgorithm, GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters) {
		String clause = "";
		TimeWatch watch;
		
		// Generate bottom clause
		logger.info("Generating ground bottom clause for example <" + String.join(",", exampleTuple.getValues()) + ">...");
		watch = TimeWatch.start();
		
		BottomClauseGenerator saturator;
		if (bottomClauseAlgorithm == BottomClauseUtil.ALGORITHMS.ORIGINAL) {
			saturator = new BottomClauseGeneratorOriginalAlgorithm();
		} else if (bottomClauseAlgorithm == BottomClauseUtil.ALGORITHMS.INSIDE_STORED_PROCEDURE) {
			saturator = new BottomClauseGeneratorInsideSP();
		} else {
			throw new IllegalArgumentException("Unsupported algorithm.");
		}
		clause = saturator.generateGroundBottomClauseString(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema, dataModel, parameters);
		
		logger.info("Ground bottom clause: \n"+ clause);
		logger.info("Saturation time: " + watch.time(TimeUnit.MILLISECONDS) + " milliseconds.");
		
		return clause;
	}
}
