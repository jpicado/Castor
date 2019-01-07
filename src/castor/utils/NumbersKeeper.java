package castor.utils;

public class NumbersKeeper {

	public static long totalTime = 0;
	public static long learningTime = 0;
	
	public static long creatingCoverageTime = 0;
	public static long preprocessingTime = 0;
	public static long bottomClauseConstructionTime = 0;
	
	public static long coverageTime = 0;
	public static int coverageCalls = 0;
	public static long clauseLengthSum = 0;
	
	public static long entailmentTime = 0;
	public static long scoringTime = 0;
	
	public static long minimizationTime = 0;
	public static long reducerTime = 0;
	public static long lggTime = 0;
	
	public static long learnClauseTime = 0;
	
	public static long computeJoinSizesTime = 0;
	
	public static long similaritySearchTime = 0;
	
	public static void reset() {
		totalTime = 0;
		learningTime = 0;
		
		creatingCoverageTime = 0;
		preprocessingTime = 0;
		bottomClauseConstructionTime = 0;
		
		coverageTime = 0;
		coverageCalls = 0;
		clauseLengthSum = 0;
		
		entailmentTime = 0;
		scoringTime = 0;
		
		minimizationTime = 0;
		reducerTime = 0;
		lggTime = 0;
		
		learnClauseTime = 0;
		
		computeJoinSizesTime = 0;
	}
}
