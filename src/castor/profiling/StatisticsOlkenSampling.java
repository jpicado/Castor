package castor.profiling;

import java.util.Map;

import aima.core.util.datastructure.Pair;

public class StatisticsOlkenSampling {

	private Map<String, Long> relationSize;
	private Map<Pair<String,String>, Long> maxNumberOfDistinctTuplesWithAttribute;
	
	public StatisticsOlkenSampling(Map<String, Long> relationSize, Map<Pair<String, String>, Long> maxNumberOfDistinctTuplesWithAttribute) {
		super();
		this.relationSize = relationSize;
		this.maxNumberOfDistinctTuplesWithAttribute = maxNumberOfDistinctTuplesWithAttribute;
	}

	public Map<String, Long> getRelationSize() {
		return relationSize;
	}

	public void setRelationSize(Map<String, Long> relationSize) {
		this.relationSize = relationSize;
	}

	public Map<Pair<String, String>, Long> getMaxNumberOfDistinctTuplesWithAttribute() {
		return maxNumberOfDistinctTuplesWithAttribute;
	}

	public void setMaxNumberOfDistinctTuplesWithAttribute(
			Map<Pair<String, String>, Long> numberOfDistinctTuplesWithAttribute) {
		this.maxNumberOfDistinctTuplesWithAttribute = numberOfDistinctTuplesWithAttribute;
	}
}
