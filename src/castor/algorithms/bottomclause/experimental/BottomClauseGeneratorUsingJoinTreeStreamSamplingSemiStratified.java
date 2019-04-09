package castor.algorithms.bottomclause.experimental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import castor.algorithms.bottomclause.BottomClauseGeneratorUsingJoinTreeStreamSampling;
import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.hypotheses.MyClause;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.sampling.JoinEdge;
import castor.sampling.JoinNode;
import castor.sampling.JoinNodeRelation;
import castor.sampling.SamplingUtils;
import castor.utils.NumbersKeeper;
import castor.utils.Pair;
import castor.utils.TimeWatch;
import castor.utils.Triple;

public class BottomClauseGeneratorUsingJoinTreeStreamSamplingSemiStratified extends BottomClauseGeneratorUsingJoinTreeStreamSampling {
	
	public BottomClauseGeneratorUsingJoinTreeStreamSamplingSemiStratified(int seed, JoinNode joinTree) {
		super(seed, joinTree);
	}

	@Override
	public void createBodyLiterals(GenericDAO genericDAO, Schema schema, 
			JoinNode joinTree, Tuple exampleTuple, 
			Map<String, List<Mode>> groupedModes, Map<String, String> hashConstantToVariable, 
			Random randomGenerator, MyClause clause, boolean ground,
			Map<Triple<String,Integer,Tuple>,Long> joinPathSizes, int sampleSize, int queryLimit) {
		
		List<Tuple> exampleTupleList = new ArrayList<Tuple>();
		exampleTupleList.add(exampleTuple);
		for (JoinEdge joinEdge : joinTree.getEdges()) {
			generateBottomClauseAux(genericDAO, schema, exampleTupleList, joinEdge, groupedModes, hashConstantToVariable, randomGenerator, clause, ground, joinPathSizes, 1, sampleSize, queryLimit);
		}
	}
	
	/*
	 * Implements modified version of Acyclic-Stream-Sample for bottom-clause construction.
	 * Modified in order to be stratified.
	 * Gets multiple samples from each relation.
	 */
	@Override
	protected void generateBottomClauseAux(GenericDAO genericDAO, Schema schema, 
			List<Tuple> tuples, JoinEdge joinEdge, 
			Map<String, List<Mode>> groupedModes, Map<String, String> hashConstantToVariable, 
			Random randomGenerator, MyClause clause, boolean ground,
			Map<Triple<String,Integer,Tuple>,Long> joinPathSizes, int depth, int sampleSize, int queryLimit) {
		
		String relation = joinEdge.getJoinNode().getNodeRelation().getRelation();
		String attributeName = schema.getRelations().get(relation.toUpperCase()).getAttributeNames().get(joinEdge.getRightJoinAttribute());
		Set<String> selectQueries = new HashSet<String>();
		for (int i = 0; i < tuples.size(); i++) {
			if (tuples.get(i) != null) {
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
			} else if (result.getTable().size() == 1) {
				// only one tuple
				joinTuples.add(result.getTable().get(0));
			} else if (result.getTable().size() <= sampleSize) {
				// add all tuples
				joinTuples.addAll(result.getTable());
			} else {
				// create dummy tuples
				for (int i = 0; i < sampleSize; i++) {
					joinTuples.add(null);
				}
				
				// Needed to compute weights later
				Map<JoinNodeRelation,Long> sizePerRelation = new HashMap<JoinNodeRelation,Long>();
				Map<Tuple,Integer> relationsJoiningWithTuple = new HashMap<Tuple,Integer>();
				Map<Pair<Tuple,JoinEdge>,Long> joinPathSizesFromTupleAndEdge = new HashMap<Pair<Tuple,JoinEdge>,Long>();
				for (Tuple tupleInJoin : result.getTable()) {
					for (JoinEdge childJoinEdge : joinEdge.getJoinNode().getEdges()) {
						// compute w(t,R)
						TimeWatch tw = TimeWatch.start();
						long size = SamplingUtils.computeJoinPathSizeFromTupleAndRelation(genericDAO, schema, tupleInJoin, joinEdge.getJoinNode(), childJoinEdge, depth, joinPathSizes, queryLimit);
						NumbersKeeper.computeJoinSizesTime += tw.time();
						joinPathSizesFromTupleAndEdge.put(new Pair<Tuple,JoinEdge>(tupleInJoin,childJoinEdge), size);
						
						if (size > 0) {
							// w(R)
							if (!sizePerRelation.containsKey(childJoinEdge.getJoinNode().getNodeRelation())) {
								sizePerRelation.put(childJoinEdge.getJoinNode().getNodeRelation(), size);
							} else {
								sizePerRelation.put(childJoinEdge.getJoinNode().getNodeRelation(), sizePerRelation.get(childJoinEdge.getJoinNode().getNodeRelation())+size);
							}
							
							// update relationsJoiningWithTuple
							if (!relationsJoiningWithTuple.containsKey(tupleInJoin)) {
								relationsJoiningWithTuple.put(tupleInJoin, 1);
							} else {
								relationsJoiningWithTuple.put(tupleInJoin, relationsJoiningWithTuple.get(tupleInJoin)+1);
							}
						}
					}
				}
				
				// sample a tuple using reservoir sampling (reservoir is joinTuples)
				double weightSummed = 0;
				for (Tuple tupleInJoin : result.getTable()) {
					double weight = 0;
					
					// compute w'(t) = ( \sum_R (w(t,R)/w(R))) / m
					if (joinEdge.getJoinNode().getEdges().size() > 0) {
						for (JoinEdge childJoinEdge : joinEdge.getJoinNode().getEdges()) {
							double w_t_R = (double)joinPathSizesFromTupleAndEdge.get(new Pair<Tuple,JoinEdge>(tupleInJoin,childJoinEdge));
							if (w_t_R > 0) {
								double w_R = (double)sizePerRelation.get(childJoinEdge.getJoinNode().getNodeRelation());
								weight += w_t_R / w_R;
							}
						}
						
						weight /= (double)relationsJoiningWithTuple.get(tupleInJoin);
					} else {
						weight = 1;
					}
					
					if (weight > 0) {
						weightSummed += weight;
						
						// keep in reservoir with some probability
						double p = (double)weight / (double)weightSummed;
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
}
