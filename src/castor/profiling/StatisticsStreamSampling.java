package castor.profiling;

import java.util.Map;

import aima.core.util.datastructure.Pair;

public class StatisticsStreamSampling {

	private Map<String, Long> relationSize;
	private Map<Pair<String,String>, Map<Object, Long>> numberOfTuplesForValue;

	public StatisticsStreamSampling(Map<String, Long> relationSize, Map<Pair<String, String>, Map<Object, Long>> numberOfTuplesForValue) {
		super();
		this.relationSize = relationSize;
		this.numberOfTuplesForValue = numberOfTuplesForValue;
	}
	
	public Map<String, Long> getRelationSize() {
		return relationSize;
	}

	public void setRelationSize(Map<String, Long> relationSize) {
		this.relationSize = relationSize;
	}

	public Map<Pair<String, String>, Map<Object, Long>> getNumberOfTuplesForValue() {
		return numberOfTuplesForValue;
	}

	public void setNumberOfTuplesForValue(Map<Pair<String, String>, Map<Object, Long>> numberOfTuplesForValue) {
		this.numberOfTuplesForValue = numberOfTuplesForValue;
	}
}
