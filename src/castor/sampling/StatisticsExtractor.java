package castor.sampling;

import java.util.HashMap;
import java.util.Map;

import aima.core.util.datastructure.Pair;
import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;

public class StatisticsExtractor {

	private static final String QUERY_COUNT_TUPLES = "SELECT COUNT(*) FROM %s;";
	private static final String QUERY_SELECT_DISTINCT_TUPLES = "SELECT DISTINCT(%s) FROM %s;";
	private static final String QUERY_COUNT_DISTINCT_TUPLES = "SELECT MAX(value) from (SELECT COUNT(DISTINCT *) AS value FROM %s GROUP BY %s) t;";
	private static final String COUNT_DISTINCT_TUPLES_SQL_STATEMENT = "SELECT COUNT(DISTINCT *) FROM %s WHERE %s = %s;";
	
	public static StatisticsOlkenSampling extractStatisticsForOlkenSampling(GenericDAO genericDAO, Schema schema) {
		Map<String, Long> relationSize = new HashMap<String, Long>();
		Map<Pair<String,String>, Long> maxNumberOfDistinctTuplesWithAttribute = new HashMap<Pair<String,String>, Long>();
		
		for (Relation relation : schema.getRelations().values()) {
			// Get relation size
			String queryCount = String.format(QUERY_COUNT_TUPLES, relation.getName());
			long countTuples = genericDAO.executeScalarQuery(queryCount);
			relationSize.put(relation.getName(), countTuples);
			
			// For each attribute, get maximum number of repeated values in attribute
			for (String attribute: relation.getAttributeNames()) {
				String query = String.format(QUERY_COUNT_DISTINCT_TUPLES, relation.getName(), attribute);
				long count = genericDAO.executeScalarQuery(query);
				maxNumberOfDistinctTuplesWithAttribute.put(new Pair<String,String>(relation.getName(), attribute), count);
			}
		}
		
		return new StatisticsOlkenSampling(relationSize, maxNumberOfDistinctTuplesWithAttribute);
	}
	
	public static StatisticsStreamSampling extractStatisticsForStreamSampling(GenericDAO genericDAO, Schema schema) {
		Map<String, Long> relationSize = new HashMap<String, Long>();
		Map<Pair<String,String>, Map<Object, Long>> numberOfTuplesForValue = new HashMap<Pair<String,String>, Map<Object, Long>>();
		
		for (Relation relation : schema.getRelations().values()) {
			// Get relation size
			String queryCount = String.format(QUERY_COUNT_TUPLES, relation.getName());
			long countTuples = genericDAO.executeScalarQuery(queryCount);
			relationSize.put(relation.getName(), countTuples);
						
			for (String attribute: relation.getAttributeNames()) {
				Pair<String,String> key = new Pair<String,String>(relation.getName(), attribute);
				
				// Get distinct values for attribute
				String query = String.format(QUERY_SELECT_DISTINCT_TUPLES, attribute, relation.getName());
				GenericTableObject result = genericDAO.executeQuery(query);
				if (result != null) {
					// For each value, compute number of tuples that contain it
					for(Tuple tuple : result.getTable()) {
						String value = tuple.getValues().get(0).toString();
						
						// Find number of distinct tuples in relation with attribute = value
						String queryCardinality = String.format(COUNT_DISTINCT_TUPLES_SQL_STATEMENT, relation.getName(), attribute, "'"+value+"'");
						long cardinality = genericDAO.executeScalarQuery(queryCardinality);
						
						if (!numberOfTuplesForValue.containsKey(key)) {
							numberOfTuplesForValue.put(key, new HashMap<Object, Long>());
						}
						
						numberOfTuplesForValue.get(key).put(value, cardinality);
					}
				}
			}
		}
		
		return new StatisticsStreamSampling(relationSize, numberOfTuplesForValue);
	}
}
