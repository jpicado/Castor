package castor.algorithms.bottomclause;

import java.util.ArrayList;
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
import castor.sampling.SamplingUtils;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;
import castor.utils.Triple;

public abstract class BottomClauseGeneratorUsingJoinTreeStreamSampling extends BottomClauseGeneratorUsingJoinTree {

	public BottomClauseGeneratorUsingJoinTreeStreamSampling(int seed, JoinNode joinTree) {
		super(seed, joinTree);
	}
	
	/*
	 * Implements idea of Acyclic-Stream-Sample for bottom-clause construction.
	 * Gets only one sample from each relation.
	 */
	protected void generateBottomClauseAux(GenericDAO genericDAO, Schema schema, 
			Tuple tuple, JoinEdge joinEdge, 
			Map<String, List<Mode>> groupedModes, Map<String, String> hashConstantToVariable, 
			Random randomGenerator, MyClause clause, boolean ground,
			Map<Triple<String,Integer,Tuple>,Long> joinPathSizes, int depth, int queryLimit) {
		
		String relation = joinEdge.getJoinNode().getNodeRelation().getRelation();
		String attributeName = schema.getRelations().get(relation.toUpperCase()).getAttributeNames().get(joinEdge.getRightJoinAttribute());
		String query = String.format(SELECT_WHERE_SQL_STATEMENT, relation, attributeName, "'"+tuple.getValues().get(joinEdge.getLeftJoinAttribute()).toString()+"'");
		query += " LIMIT " + queryLimit;
		
		// Run query to get all tuples in join
		GenericTableObject result = genericDAO.executeQuery(query);
		
		// Get one tuple to add to sample
		Tuple joinTuple = null;
		if (result != null) {
			if (result.getTable().size() == 0) {
				// no tuples
				return;
			} else if (result.getTable().size() == 1) {
				// only one tuple
				joinTuple = result.getTable().get(0);
			} else {
				// sample a tuple using reservoir sampling (reservoir is joinTuple)
				long weightSummed = 0;
				while (joinTuple == null) {
					for (Tuple tupleInJoin : result.getTable()) {
						long size;
						Triple<String,Integer,Tuple> key = new Triple<String,Integer,Tuple>(relation,depth,tupleInJoin);
						if (joinPathSizes.containsKey(key))
							size = joinPathSizes.get(key);
						else {
							TimeWatch tw = TimeWatch.start();
							
//							size = SamplingUtils.computeJoinPathSizeFromTupleWithQueries(genericDAO, schema, tupleInJoin, joinEdge.getJoinNode());
							size = SamplingUtils.computeJoinPathSizeFromTuple(genericDAO, schema, tupleInJoin, joinEdge.getJoinNode(), depth, joinPathSizes, queryLimit);
							joinPathSizes.put(key, size);
							
							NumbersKeeper.computeJoinSizesTime += tw.time();
						}
						
						weightSummed += size;
						double p = (double)size / (double)weightSummed;
						if (randomGenerator.nextDouble() < p) {
							joinTuple = tupleInJoin;
						}
					}
				}
			}
		}
		
		// Apply modes and add literal to clause
		modeOperationsForTuple(joinTuple, clause, hashConstantToVariable, groupedModes.get(relation), ground);
		
		// Recursive call on node's children
		for (JoinEdge childJoinEdge : joinEdge.getJoinNode().getEdges()) {
			generateBottomClauseAux(genericDAO, schema, joinTuple, childJoinEdge, 
					groupedModes, hashConstantToVariable, randomGenerator, clause, ground, joinPathSizes, depth+1, queryLimit);
		}
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
				String selectQuery = String.format(SELECT_WHERE_SQL_STATEMENT, relation, attributeName, "'"+tuples.get(i).getValues().get(joinEdge.getLeftJoinAttribute()).toString()+"'");
				
				for (int attrPos = 0; attrPos < joinEdge.getJoinNode().getNodeRelation().getConstantAttributeNames().size(); attrPos++) {
					selectQuery += " AND ";
					String selectAttributeName = joinEdge.getJoinNode().getNodeRelation().getConstantAttributeNames().get(attrPos);
					String selectAttributeValue = joinEdge.getJoinNode().getNodeRelation().getConstantAttributeValues().get(attrPos);
					selectQuery += selectAttributeName + " = '" + selectAttributeValue + "'";
				}
				
				selectQueries.add(selectQuery);
			}
		}
		
		String query = String.join(" UNION ", selectQueries);
		query += " LIMIT " + queryLimit;
		
		// Run query to get all tuples in join
		GenericTableObject result = genericDAO.executeQuery(query);
		
		// Create reservoir
		List<Tuple> joinTuples = new ArrayList<Tuple>();
		
		if (result != null) {
			if (result.getTable().size() == 0) {
				// no tuples
				return;
			} if (result.getTable().size() <= sampleSize) {
				// add all tuples
				joinTuples.addAll(result.getTable());
			} else {
				// Sample using WR2 (reservoir sampling)
				for (int i = 0; i < sampleSize; i++) {
					joinTuples.add(null);
				}
				
				// sample tuples using reservoir sampling (reservoir is joinTuples)
				long weightSummed = 0;
				for (Tuple tupleInJoin : result.getTable()) {
					TimeWatch tw = TimeWatch.start();
//					long weight = SamplingUtils.computeJoinPathSizeFromTupleWithQueries(genericDAO, schema, tupleInJoin, joinEdge.getJoinNode());
					long weight = SamplingUtils.computeJoinPathSizeFromTuple(genericDAO, schema, tupleInJoin, joinEdge.getJoinNode(), depth, joinPathSizes, queryLimit);
					NumbersKeeper.computeJoinSizesTime += tw.time();
					
					weightSummed += weight;
					double p = (double)weight / (double)weightSummed;
					for (int i = 0; i < joinTuples.size(); i++) {
						if (randomGenerator.nextDouble() < p) {
							joinTuples.set(i, tupleInJoin);
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
}
