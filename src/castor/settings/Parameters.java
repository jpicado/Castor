package castor.settings;

import castor.algorithms.CastorLearner;

public class Parameters {

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
	private boolean useInds = true;
	private String dbURL = "localhost";
	
	public boolean isCreateStoredProcedure() {
		return createStoredProcedure;
	}
	public void setCreateStoredProcedure(boolean createStoredProcedure) {
		this.createStoredProcedure = createStoredProcedure;
	}
	public double getMinPrecision() {
		return minPrecision;
	}
	public void setMinPrecision(double minPrecision) {
		this.minPrecision = minPrecision;
	}
	public double getMinRecall() {
		return minRecall;
	}
	public void setMinRecall(double minRecall) {
		this.minRecall = minRecall;
	}
	public int getSample() {
		return sample;
	}
	public void setSample(int sample) {
		this.sample = sample;
	}
	public int getBeam() {
		return beam;
	}
	public void setBeam(int beam) {
		this.beam = beam;
	}
	public int getThreads() {
		return threads;
	}
	public void setThreads(int threads) {
		this.threads = threads;
	}
	public boolean isMinimizeBottomClause() {
		return minimizeBottomClause;
	}
	public void setMinimizeBottomClause(boolean minimizeBottomClause) {
		this.minimizeBottomClause = minimizeBottomClause;
	}
	public String getReductionMethod() {
		return reductionMethod;
	}
	public void setReductionMethod(String reductionMethod) {
		if (reductionMethod.equals("consistency")) {
			this.reductionMethod = CastorLearner.NEGATIVE_REDUCTION_PRECISION;
		} else if (reductionMethod.equals("precision")) {
			this.reductionMethod = CastorLearner.NEGATIVE_REDUCTION_PRECISION;
		} else if (reductionMethod.equals("none")) {
			this.reductionMethod = CastorLearner.NEGATIVE_REDUCTION_NONE;
		} else {
			throw new IllegalArgumentException("Unknown reduction method.");
		}
	}
	public int getIterations() {
		return iterations;
	}
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	public int getRecall() {
		return recall;
	}
	public void setRecall(int recall) {
		this.recall = recall;
	}
	public int getMaxterms() {
		return maxterms;
	}
	public void setMaxterms(int maxterms) {
		this.maxterms = maxterms;
	}
	public boolean isUseInds() {
		return useInds;
	}
	public void setUseInds(boolean useInds) {
		this.useInds = useInds;
	}
	public String getDbURL() {
		return dbURL;
	}
	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}
}
