package castor.wrappers;

import java.util.List;

public class LearningResult {

	private boolean success;
	private String errorMessage;
	private String algorithm;
	private String schema;
	private double accuracy;
	private double precision;
	private double recall;
	private double f1;
	private long time;
	private List<ClauseAsString> definition;
	private String outputSQL;
	
	public LearningResult() {
		super();
		this.success = false;
	}
	public LearningResult(boolean success, String algorithm, String schema, double accuracy,
			double precision, double recall, double f1, long time,
			List<ClauseAsString> definition) {
		super();
		this.success = success;
		this.algorithm = algorithm;
		this.schema = schema;
		this.accuracy = accuracy;
		this.precision = precision;
		this.recall = recall;
		this.f1 = f1;
		this.time = time;
		this.definition = definition;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
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
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public List<ClauseAsString> getDefinition() {
		return definition;
	}
	public void setDefinition(List<ClauseAsString> definition) {
		this.definition = definition;
	}
	public String getOutputSQL() {
		return outputSQL;
	}
	public void setOutputSQL(String outputSQL) {
		this.outputSQL = outputSQL;
	}
}
