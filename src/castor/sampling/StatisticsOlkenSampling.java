package castor.sampling;

import java.util.Map;

import aima.core.util.datastructure.Pair;

public class StatisticsOlkenSampling {

	private Map<String, Long> relationSize;
	private Map<Pair<String,String>, Long> maximumFrequencyOnAttribute;
	
	public StatisticsOlkenSampling(Map<String, Long> relationSize, Map<Pair<String, String>, Long> maximumFrequencyOnAttribute) {
		super();
		this.relationSize = relationSize;
		this.maximumFrequencyOnAttribute = maximumFrequencyOnAttribute;
	}

	public Map<String, Long> getRelationSize() {
		return relationSize;
	}

	public void setRelationSize(Map<String, Long> relationSize) {
		this.relationSize = relationSize;
	}

	public Map<Pair<String, String>, Long> getMaximumFrequencyOnAttribute() {
		return maximumFrequencyOnAttribute;
	}

	public void setMaximumFrequencyOnAttribute(Map<Pair<String, String>, Long> maximumFrequencyOnAttribute) {
		this.maximumFrequencyOnAttribute = maximumFrequencyOnAttribute;
	}
}
