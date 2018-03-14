package castor.algorithms.bottomclause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import aima.core.logic.fol.parsing.ast.Predicate;
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
import castor.sampling.StatisticsStreamSampling;
import castor.settings.DataModel;
import castor.settings.Parameters;
import castor.utils.RandomSet;

public class BottomClauseGeneratorStreamSamplingNEW extends BottomClauseGeneratorOriginalAlgorithm {

	private static final String SELECT_WHERE_SQL_STATEMENT = "SELECT * FROM %s WHERE %s = %s;";
	private static final String SELECT_SQL_STATEMENT = "SELECT * FROM %s;";
	
	private StatisticsStreamSampling statistics;
//	private Map<Tuple,Long> weightUpperBounds;
	private Map<Pair<Tuple,String>,Integer> weightWithChildUpperBounds;

	public BottomClauseGeneratorStreamSamplingNEW(int seed, GenericDAO genericDAO, Schema schema, DataModel dataModel, Parameters parameters) {
		super(seed);
		this.varCounter = 0;
		
		computeUpperBounds(genericDAO, schema, dataModel, parameters);
	}
	
	private void computeUpperBounds(GenericDAO genericDAO, Schema schema, DataModel dataModel, Parameters parameters) {
		// weightWithChildUpperBounds: <t,R> -> W
		weightWithChildUpperBounds = new HashMap<Pair<Tuple,String>,Integer>();
		
		JoinNode rootNode = SamplingUtils.findJoinTree(dataModel, parameters);
		
		// Group nodes by depth
		Map<Integer,List<JoinNode>> nodesAtDepth = new HashMap<Integer,List<JoinNode>>();
		groupJoinNodesByDepth(rootNode, nodesAtDepth, 0);
		
		// Dynamic programming algorithm
		int maxDepth = Collections.max(nodesAtDepth.keySet());
		for (int i = maxDepth; i >= 1; i--) {
			System.out.println("Depth:"+i);
			for (JoinNode node : nodesAtDepth.get(i)) {
				String query = String.format(SELECT_SQL_STATEMENT, node.getRelation());
				System.out.println("A:"+query);
				GenericTableObject result = genericDAO.executeQuery(query);
				if (result != null) {
					if (i == maxDepth) {
						// Leaf
						for (Tuple tuple : result.getTable()) {
							weightWithChildUpperBounds.put(new Pair<Tuple,String>(tuple, ""), 1);
							System.out.println(tuple.toString() + "-" + "none" + ":" + 1);
						}
					} else {
						// Non-leaf
						for (Tuple tuple : result.getTable()) {
							for (JoinEdge joinEdge : node.getEdges()) {
								String leftAttributeValue = tuple.getValues().get(joinEdge.getLeftJoinAttribute()).toString();
								JoinNode rightNode = joinEdge.getJoinNode();
								String rightRelation = rightNode.getRelation();
								String rightAttributeName = schema.getRelations().get(rightRelation.toUpperCase()).getAttributeNames().get(joinEdge.getRightJoinAttribute());
								
								// tuple semijoin rightRelation
								String queryChild = String.format(SELECT_WHERE_SQL_STATEMENT, rightRelation, rightAttributeName, "'"+leftAttributeValue+"'");
//								System.out.println("B:"+queryChild);
								GenericTableObject resultChild = genericDAO.executeQuery(queryChild);
								
								// W(tuple semijoin rightRelation) = sum of W(t) for each t in (tuple semijoin rightRelation)
								int weight = 0;
								if (resultChild != null) {
									for (Tuple tupleChild : resultChild.getTable()) {
										if (rightNode.getEdges().size() == 0) {
											// Child is leaf
											weight += weightWithChildUpperBounds.get(new Pair<Tuple,String>(tupleChild,""));
										} else {
											// Child is non-leaf
											
											// W(t) = product W(t,R)
											int product = 1;
											for (JoinEdge childJoinEdge : rightNode.getEdges()) {
												product *= weightWithChildUpperBounds.get(new Pair<Tuple,String>(tupleChild,childJoinEdge.getJoinNode().getRelation()));
											}
											weight += product;
										}
									}
								}
								
								weightWithChildUpperBounds.put(new Pair<Tuple,String>(tuple, rightRelation), weight);
								System.out.println(tuple.toString() + "-" + rightRelation + ":" + weight);
							}
						}
					}
				}
			}
		}
		
		System.out.println(weightWithChildUpperBounds.toString());
	}
	
	private void groupJoinNodesByDepth(JoinNode joinNode, Map<Integer,List<JoinNode>> nodesAtDepth, int currentDepth) {
		if (!nodesAtDepth.containsKey(currentDepth)) {
			nodesAtDepth.put(currentDepth, new ArrayList<JoinNode>());
		}
		nodesAtDepth.get(currentDepth).add(joinNode);
		
		for (JoinEdge joinEdge : joinNode.getEdges()) {
			groupJoinNodesByDepth(joinEdge.getJoinNode(), nodesAtDepth, currentDepth+1);
		}
	}
	

	/*
	 * Implements Stream-Sampling algorithm from paper "On Random Sampling Over Joins"
	 */
	public List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> newInTerms, Set<String> distinctTerms, String relationName, String attributeName,
			List<Mode> relationAttributeModes, Map<Pair<String, Integer>, List<Mode>> groupedModes, RandomSet<String> knownTermsSet,
			int recall, boolean ground, boolean shuffleTuples, Random randomGenerator) {
		List<Predicate> newLiterals = new LinkedList<Predicate>();
		
		long sampleSize = Math.min(recall, statistics.getRelationSize().get(relationName.toUpperCase()));
		
		// Stream-Sample algorithm
		
		// Step 1
		// Create sample containing dummy values (we use first value in knownTerms)
		List<String> sample = new LinkedList<String>();
		for (int i = 0; i < sampleSize; i++) {
			sample.add(knownTermsSet.get(0));
		}
		// If there are more values, user Black-Box WR2 (reservoir sampling) to get sample from knownTermsSet
		if (knownTermsSet.size() > 1) {
			long cumulativeWeight = 0;
			for(String value : knownTermsSet) {
				Pair<String,String> key = new Pair<String,String>(relationName.toUpperCase(), attributeName.toUpperCase());
				if (!statistics.getNumberOfTuplesForValue().get(key).containsKey(value)) {
					continue;
				}
				
				//TODO check for multiple joins
				long weight = statistics.getNumberOfTuplesForValue().get(key).get(value);
				cumulativeWeight += weight;
				
				double p = (double) weight / (double) cumulativeWeight;
				for (int i = 0; i < sample.size(); i++) {
					if (randomGenerator.nextDouble() < p) {
						sample.set(i, value);
					}
				}
			}
		}
		
		// Step 2
		for (String value : sample) {
			// Create query and run
			String query = String.format(SELECT_WHERE_SQL_STATEMENT, relationName, attributeName, "'"+value+"'");
			GenericTableObject result = genericDAO.executeQuery(query);
			
			if (result != null && result.getTable().size() > 0) {
				// Sample a tuple from result
				//TODO can we get a single sample directly from the query to the DB?
				Tuple tuple = result.getTable().get(randomGenerator.nextInt(result.getTable().size()));
				
				Set<String> usedModes = new HashSet<String>();
				for (Mode mode : relationAttributeModes) {
					if (ground) {
						if (usedModes.contains(mode.toGroundModeString())) {
							continue;
						}
						else {
							mode = mode.toGroundMode();
							usedModes.add(mode.toGroundModeString());
						}
					}
					
					Predicate literal = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant, tuple,
							mode, false, newInTerms, distinctTerms);
					
					// Do not add literal if it's exactly the same as head literal
					if (!literal.equals(clause.getPositiveLiterals().get(0).getAtomicSentence())) {
						addNotRepeated(newLiterals, literal);
					}
				}
			}
		}

		return newLiterals;
	}
}
