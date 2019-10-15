package castor.algorithms.bottomclause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.hypotheses.MyClause;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.sampling.JoinEdge;
import castor.sampling.JoinNode;
import castor.utils.Triple;

public abstract class BottomClauseGeneratorUsingJoinTreeOlkenSampling extends BottomClauseGeneratorUsingJoinTree {
	
	public BottomClauseGeneratorUsingJoinTreeOlkenSampling(int seed, JoinNode joinTree) {
		super(seed, joinTree);
	}
	
	/*
	 * Implements idea of Acyclic-Stream-Sample for bottom-clause construction.
	 * Gets multiple samples from each relation.
	 */
	protected void generateBottomClauseAux(GenericDAO genericDAO, Schema schema, 
			List<Tuple> tuples, JoinEdge joinEdge, 
			Map<String, List<Mode>> groupedModes, Map<String, String> hashConstantToVariable, 
			Random randomGenerator, MyClause clause, boolean ground,
			Map<Triple<String,Integer,Tuple>,Long> joinPathSizes, int depth, int sampleSize, int queryLimit) {
		// Get tuples in relation in joinEdge that join with given tuples
		String relation = joinEdge.getJoinNode().getNodeRelation().getRelation();
		String attributeName = schema.getRelations().get(relation.toUpperCase()).getAttributeNames().get(joinEdge.getRightJoinAttribute());
		Set<String> selectQueries = new HashSet<String>();
		for (int i = 0; i < tuples.size(); i++) {
			if (tuples.get(i) != null && tuples.get(i).getValues().get(joinEdge.getLeftJoinAttribute()) != null) {
				String value = tuples.get(i).getValues().get(joinEdge.getLeftJoinAttribute()).toString();
				// Escape single quotes
				value = value.replace("'", "''");
				String selectQuery = String.format(SELECT_WHERE_SQL_STATEMENT, relation, attributeName, "'"+value+"'");
				
				for (int attrPos = 0; attrPos < joinEdge.getJoinNode().getNodeRelation().getConstantAttributeNames().size(); attrPos++) {
					selectQuery += " AND ";
					String selectAttributeName = joinEdge.getJoinNode().getNodeRelation().getConstantAttributeNames().get(attrPos);
					String selectAttributeValue = joinEdge.getJoinNode().getNodeRelation().getConstantAttributeValues().get(attrPos);
					// Escape single quotes
					selectAttributeValue = selectAttributeValue.replace("'", "''");
					selectQuery += selectAttributeName + " = '" + selectAttributeValue + "'";
				}
				
				selectQueries.add(selectQuery);
			}
		}
		
		String query = String.join(" UNION ", selectQueries);
		query += " LIMIT " + queryLimit;
		
		// Run query to get all tuples in join
		GenericTableObject result = genericDAO.executeQuery(query);
		
		// Sample
		List<Tuple> joinTuples = new ArrayList<Tuple>();
		
		if (result != null) {
			if (result.getTable().size() == 0) {
				// no tuples
				return;
			} else if (result.getTable().size() <= sampleSize) {
				// add all tuples
				joinTuples.addAll(result.getTable());
//			} else {
//				// Sample using WR1
//				long totalWeight = result.getTable().size();
//				long weightUsed = 0;
//				int samplesNeeded = sampleSize;
//				while (samplesNeeded > 0) {
//					// Get random tuple
//					Tuple randomTuple = result.getTable().get(randomGenerator.nextInt(result.getTable().size()));
//					long frequency = computeFrequencyOfValue(result.getTable(), joinEdge.getRightJoinAttribute(), randomTuple.getValues().get(joinEdge.getRightJoinAttribute()));
//					
//					// Compute number of copies of tuple to keep, equal to number of successes in binomial distribution
//					double successProbability = 1.0 / (double)(totalWeight - weightUsed);
//					BinomialDistribution dist = new BinomialDistribution(samplesNeeded, successProbability);
//					int copies = dist.sample();
//					
//					// Compute probability of keeping a sample
//					Pair<String,String> key = new Pair<String,String>(relation, attributeName);
//					long denominator = statistics.getMaximumFrequencyOnAttribute().get(key);
//					double acceptProbability = (double)frequency / (double)denominator;
//					
//					for (int copy=0; copy<copies; copy++) {
//						// Accept with probability p
//						if (randomGenerator.nextDouble() < acceptProbability) {
//							joinTuples.add(randomTuple);
//							samplesNeeded--;
//						}
//					}
//					weightUsed += 1;
//				}
			} else {
				// Sample using WR2 (reservoir sampling)
				for (int i = 0; i < sampleSize; i++) {
					joinTuples.add(null);
				}
				
				// sample tuples using reservoir sampling (reservoir is joinTuples)
				long weightSummed = 0;
				long denominator = computeMaxFrequency(result.getTable(), joinEdge.getRightJoinAttribute());
				for (Tuple tupleInJoin : result.getTable()) {
					// Compute probability of keeping a sample
					long frequency = computeFrequencyOfValue(result.getTable(), joinEdge.getRightJoinAttribute(), tupleInJoin.getValues().get(joinEdge.getRightJoinAttribute()));
					double acceptProbability = (double)frequency / (double)denominator;
					
					if (randomGenerator.nextDouble() < acceptProbability) {
						weightSummed += 1;
						double p = 1.0 / (double)weightSummed;
						for (int i = 0; i < joinTuples.size(); i++) {
							if (randomGenerator.nextDouble() < p) {
								joinTuples.set(i, tupleInJoin);
							}
						}
					}
				}
			}
		}
		
		for (Tuple joinTuple : joinTuples) {
			if (joinTuple != null) {
				// Apply modes and add literal to clause
				modeOperationsForTuple(joinTuple, clause, hashConstantToVariable, groupedModes.get(relation), ground);
			}
		}
		
		// Recursive call on node's children
		for (JoinEdge childJoinEdge : joinEdge.getJoinNode().getEdges()) {
			generateBottomClauseAux(genericDAO, schema, joinTuples, childJoinEdge, 
					groupedModes, hashConstantToVariable, randomGenerator, clause, ground, joinPathSizes, depth+1, sampleSize, queryLimit);
		}
	}
	
	private long computeMaxFrequency(List<Tuple> tuples, int attribute) {
		Map<Object,Long> frequencies = new HashMap<Object,Long>();
		for (Tuple tuple : tuples) {
			if (!frequencies.containsKey(tuple.getValues().get(attribute))) {
				frequencies.put(tuple.getValues().get(attribute), 1l);
			} else {
				frequencies.put(tuple.getValues().get(attribute), frequencies.get(tuple.getValues().get(attribute))+1);
			}
		}
		
		long maxValue = Integer.MIN_VALUE;
		for (Long value : frequencies.values()) {
			if (value > maxValue)
				maxValue = value;
		}
		return maxValue;
	}
	
	private long computeFrequencyOfValue(List<Tuple> tuples, int attribute, Object value) {
		long frequency = 0;
		for (Tuple tuple : tuples) {
			if (tuple.getValues().get(attribute).equals(value))
				frequency++;
		}
		return frequency;
	}
}
