package castor.algorithms.bottomclause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.math3.distribution.BinomialDistribution;

import aima.core.util.datastructure.Pair;
import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.hypotheses.MyClause;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.sampling.JoinEdge;
import castor.sampling.JoinNode;
import castor.sampling.SamplingUtils;
import castor.sampling.StatisticsOlkenSampling;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;
import castor.utils.Triple;

public abstract class BottomClauseGeneratorUsingJoinTreeOlkenSampling extends BottomClauseGeneratorUsingJoinTree {

	protected StatisticsOlkenSampling statistics;
	
	public BottomClauseGeneratorUsingJoinTreeOlkenSampling(int seed, JoinNode joinTree) {
		super(seed, joinTree);
	}
	
	protected void generateBottomClauseAux(GenericDAO genericDAO, Schema schema, 
			List<Tuple> tuples, JoinEdge joinEdge, 
			Map<String, List<Mode>> groupedModes, Map<String, String> hashConstantToVariable, 
			Random randomGenerator, MyClause clause, boolean ground,
			Map<Triple<String,Integer,Tuple>,Long> joinPathSizes, int depth, int sampleSize) {
		// Get tuples in relation in joinEdge that join with given tuples
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
		
		// Run query to get all tuples in join
		GenericTableObject result = genericDAO.executeQuery(query);
		
		// Sample
		List<Tuple> joinTuples = new ArrayList<Tuple>();
		
		if (result != null) {
			if (result.getTable().size() == 0) {
				// no tuples
				return;
			} else if (result.getTable().size() == 1) {
				// only one tuple
				joinTuples.add(result.getTable().get(0));
			} else {
				// Sample using WR1
				Map<Tuple,Long> tupleWeights = new HashMap<Tuple,Long>();
				TimeWatch tw = TimeWatch.start();
				long weight = SamplingUtils.computeOlkenWeightFromJoinNode(genericDAO, schema, joinEdge.getJoinNode(), statistics);
				NumbersKeeper.computeJoinSizesTime += tw.time();
				
				long weightSummed = 0;
				for (Tuple tupleInJoin : result.getTable()) {
					//TODO ALL TUPLES HAVE SAME WEIGHT??? IF YES, SIMPLY MULTIPLY AND NO NEED MAP TO STORE WEIGHT
					tupleWeights.put(tupleInJoin, weight);
					weightSummed += weight;
				}
				
				long weightUsed = 0;
				int samplesNeeded = sampleSize;
				while (samplesNeeded > 0) {
					// Get random tuple
					Tuple tupleInJoin = result.getTable().get(randomGenerator.nextInt(result.getTable().size()));
					long tupleWeight = tupleWeights.get(tupleInJoin);
					weightUsed += tupleWeight;

					// Compute number of copies of tuple to keep, equal to number of successes in binomial distribution
					double successProbability = (double)tupleWeight / (double)(weightSummed - weightUsed);
					BinomialDistribution dist = new BinomialDistribution(samplesNeeded, successProbability);
					int copies = dist.sample();
					
					//TODO Compute probability of keeping a sample
					Pair<String,String> key = new Pair<String,String>(relation, attributeName);
					//IS THIS CORRECT???
					long numerator = weightSummed;
					long denominator = statistics.getMaximumFrequencyOnAttribute().get(key) * weight;
					double acceptProbability = (double)numerator / (double)denominator;
					
					for (int copy=0; copy<copies; copy++) {
						// Accept with probability p
						if (randomGenerator.nextDouble() < acceptProbability) {
							joinTuples.add(tupleInJoin);
							samplesNeeded--;
						}
					}
				}
			}
			
		}
	}
}
