package castor.sampling;

import java.util.HashMap;
import java.util.Map;

import aima.core.util.datastructure.Pair;
import castor.dataaccess.db.GenericDAO;
import castor.language.Relation;
import castor.language.Schema;

public class StatisticsExtractor {

	private static final String QUERY_COUNT_TUPLES = "SELECT COUNT(*) FROM %s;";
	private static final String QUERY_COUNT_DISTINCT_TUPLES = "SELECT MAX(value) from (SELECT COUNT(DISTINCT *) AS value FROM %s GROUP BY %s) t;";
	
	public static StatisticsOlkenSampling extractStatisticsForOlkenSampling(GenericDAO genericDAO, Schema schema) {
		Map<String, Long> relationSize = new HashMap<String, Long>();
		Map<Pair<String,String>, Long> maximumFrequencyOnAttribute = new HashMap<Pair<String,String>, Long>();
		
		for (Relation relation : schema.getRelations().values()) {
			// Get relation size
			String queryCount = String.format(QUERY_COUNT_TUPLES, relation.getName());
			long countTuples = genericDAO.executeScalarQuery(queryCount);
			relationSize.put(relation.getName(), countTuples);
			
			// For each attribute, get maximum number of repeated values in attribute
			for (String attribute: relation.getAttributeNames()) {
				String query = String.format(QUERY_COUNT_DISTINCT_TUPLES, relation.getName(), attribute);
				long count = genericDAO.executeScalarQuery(query);
				maximumFrequencyOnAttribute.put(new Pair<String,String>(relation.getName(), attribute), count);
			}
		}
		
		return new StatisticsOlkenSampling(relationSize, maximumFrequencyOnAttribute);
	}
}
