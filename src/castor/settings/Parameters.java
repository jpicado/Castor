package castor.settings;

import castor.algorithms.transformations.ReductionMethods;

public class Parameters {

	// Default values for parameters
	private boolean createStoredProcedure = true;
	private boolean useStoredProcedure = true;
	private double minPrecision = 0.5;
	private double minRecall = 0;
	private int minPos = 2;
	private double maxNoise = 1.0;
	private int sample = 1;
	private int beam = 1;
	private int threads = 1;
	private boolean minimizeBottomClause = false;
	private String reductionMethod = ReductionMethods.NEGATIVE_REDUCTION_CONSISTENCY;
	private int iterations = 2;
	private int recall = 10;
	private int groundRecall = Integer.MAX_VALUE;
	private int maxterms = Integer.MAX_VALUE;
	private boolean useInds = true;
	private String dbURL = "localhost";
	private String port = "21212";
	private int randomSeed = 1;
	private String samplingMethod = SamplingMethods.NAIVE;
	private boolean sampleInTesting = false;
	private boolean sampleGroundBottomClauses = false;
	private boolean sampleInCoveringApproach = false;
	private boolean shuffleExamples = false;
	private boolean randomizeRecall = false;
	private boolean allowSimilarity = false;
	
	public boolean isCreateStoredProcedure() {
		return createStoredProcedure;
	}
	public void setCreateStoredProcedure(boolean createStoredProcedure) {
		this.createStoredProcedure = createStoredProcedure;
	}
	public boolean isUseStoredProcedure() {
		return useStoredProcedure;
	}
	public void setUseStoredProcedure(boolean useStoredProcedure) {
		this.useStoredProcedure = useStoredProcedure;
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
	public int getMinPos() {
		return minPos;
	}
	public void setMinPos(int minPos) {
		this.minPos = minPos;
	}
	public double getMaxNoise() {
		return maxNoise;
	}
	public void setMaxNoise(double maxNoise) {
		this.maxNoise = maxNoise;
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
			this.reductionMethod = ReductionMethods.NEGATIVE_REDUCTION_CONSISTENCY;
		} else if (reductionMethod.equals("precision")) {
			this.reductionMethod = ReductionMethods.NEGATIVE_REDUCTION_PRECISION;
		} else if (reductionMethod.equals("none")) {
			this.reductionMethod = ReductionMethods.NEGATIVE_REDUCTION_NONE;
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
	public int getGroundRecall() {
		return groundRecall;
	}
	public void setGroundRecall(int groundRecall) {
		this.groundRecall = groundRecall;
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
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public int getRandomSeed() {
		return randomSeed;
	}
	public void setRandomSeed(int randomSeed) {
		this.randomSeed = randomSeed;
	}
	public String getSamplingMethod() {
		return samplingMethod;
	}
	public void setSamplingMethod(String samplingMethod) {
		if (samplingMethod.equals("naive")) {
			this.samplingMethod = SamplingMethods.NAIVE;
		} else if (samplingMethod.equals("olken")) {
			this.samplingMethod = SamplingMethods.OLKEN;
		} else if (samplingMethod.equals("stream")) {
			this.samplingMethod = SamplingMethods.STREAM;
		} else if (samplingMethod.equals("stratified")) {
			this.samplingMethod = SamplingMethods.STRATIFIED;
		} else if (samplingMethod.equals("semistratified")) {
			this.samplingMethod = SamplingMethods.SEMISTRATIFIED;
		} else {
			throw new IllegalArgumentException("Unknown sampling method.");
		}
	}
	public boolean isSampleInTesting() {
		return sampleInTesting;
	}
	public void setSampleInTesting(boolean sampleInTesting) {
		this.sampleInTesting = sampleInTesting;
	}
	public boolean isSampleGroundBottomClauses() {
		return sampleGroundBottomClauses;
	}
	public void setSampleGroundBottomClauses(boolean sampleGroundBottomClauses) {
		this.sampleGroundBottomClauses = sampleGroundBottomClauses;
	}
	public boolean isSampleInCoveringApproach() {
		return sampleInCoveringApproach;
	}
	public void setSampleInCoveringApproach(boolean sampleInCoveringApproach) {
		this.sampleInCoveringApproach = sampleInCoveringApproach;
	}
	public boolean isShuffleExamples() {
		return shuffleExamples;
	}
	public void setShuffleExamples(boolean shuffleExamples) {
		this.shuffleExamples = shuffleExamples;
	}
	public boolean isRandomizeRecall() {
		return randomizeRecall;
	}
	public void setRandomizeRecall(boolean randomizeRecall) {
		this.randomizeRecall = randomizeRecall;
	}
	public boolean isAllowSimilarity() {
		return allowSimilarity;
	}
	public void setAllowSimilarity(boolean allowSimilarity) {
		this.allowSimilarity = allowSimilarity;
	}
	@Override
	public String toString() {
		return "Parameters [createStoredProcedure=" + createStoredProcedure + ", useStoredProcedure="
				+ useStoredProcedure + ", minPrecision=" + minPrecision + ", minRecall=" + minRecall + ", minPos="
				+ minPos + ", maxNoise=" + maxNoise + ", sample=" + sample + ", beam=" + beam + ", threads=" + threads
				+ ", minimizeBottomClause=" + minimizeBottomClause + ", reductionMethod=" + reductionMethod
				+ ", iterations=" + iterations + ", recall=" + recall + ", groundRecall=" + groundRecall + ", maxterms="
				+ maxterms + ", useInds=" + useInds + ", dbURL=" + dbURL + ", port=" + port + ", randomSeed="
				+ randomSeed + ", samplingMethod=" + samplingMethod + ", sampleInTesting=" + sampleInTesting
				+ ", sampleGroundBottomClauses=" + sampleGroundBottomClauses + ", sampleInCoveringApproach="
				+ sampleInCoveringApproach + ", shuffleExamples=" + shuffleExamples + ", randomizeRecall="
				+ randomizeRecall + ", allowSimilarity=" + allowSimilarity + "]";
	}
}
