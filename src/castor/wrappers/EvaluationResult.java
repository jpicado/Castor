package castor.wrappers;


public class EvaluationResult {

	private double accuracy;
	private double precision;
	private double recall;
	private double f1;
	
	public EvaluationResult(double accuracy, double precision, double recall,
			double f1) {
		super();
		this.accuracy = accuracy;
		this.precision = precision;
		this.recall = recall;
		this.f1 = f1;
	}
	public double getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	public double getPrecision() {
		return precision;
	}
	public void setPrecision(double precision) {
		this.precision = precision;
	}
	public double getRecall() {
		return recall;
	}
	public void setRecall(double recall) {
		this.recall = recall;
	}
	public double getF1() {
		return f1;
	}
	public void setF1(double f1) {
		this.f1 = f1;
	}
}
