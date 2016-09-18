package castor.algorithms.clauseevaluation;

public class EvaluationFunctions {

	public enum FUNCTION {
		ACCURACY,
		COVERAGE,
		PRECISION,
		RECALL,
		F1,
		F2,
		MCC
	}
	
	public static double score(EvaluationFunctions.FUNCTION function, int tp, int fp, int tn, int fn) {
		double score = 0;
		switch(function) {
		case ACCURACY:
			score = accuracy(tp, fp, tn, fn);
			break;
		case COVERAGE:
			score = coverage(tp, fp, tn, fn);
			break;
		case PRECISION:
			score = precision(tp, fp, tn, fn);
			break;
		case RECALL:
			score = recall(tp, fp, tn, fn);
			break;
		case F1:
			score = f1(tp, fp, tn, fn);
			break;
		case F2:
			score = f2(tp, fp, tn, fn);
			break;
		case MCC:
			score = mcc(tp, fp, tn, fn);
			break;
		}
		return score;
	}
	
	private static double accuracy(int tp, int fp, int tn, int fn) {
		double n = tp + fp + tn + fn;
		return ((double)(tp + tn))/n;
	}
	
	private static double coverage(int tp, int fp, int tn, int fn) {
		return (double)(tp - fp);
	}
	
	private static double precision(int tp, int fp, int tn, int fn) {
		return ((double)tp)/((double)(tp + fp));
	}
	
	private static double recall(int tp, int fp, int tn, int fn) {
		return ((double)tp)/((double)(tp + fn));
	}
	
	private static double f1(int tp, int fp, int tn, int fn) {
		double precision = precision(tp, fp, tn, fn);
		double recall = recall(tp, fp, tn, fn);
		return (2.0 * precision * recall) / (precision + recall);
	}
	
	private static double f2(int tp, int fp, int tn, int fn) {
		double precision = precision(tp, fp, tn, fn);
		double recall = recall(tp, fp, tn, fn);
		return (5.0 * precision * recall) / ((4.0 * precision) + recall);
	}
	
	private static double mcc(int tp, int fp, int tn, int fn) {
		double numerator = ((double)(tp * tn)) - ((double)(fp * fn));
		double denominatorAux = ((double)(tp+fp))*((double)(tp+fn))*((double)(tn+fp))*((double)(tn+fn));
		double denominator = Math.sqrt(denominatorAux);
		return numerator / denominator;
	}
}
