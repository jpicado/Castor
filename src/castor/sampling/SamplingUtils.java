package castor.sampling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.util.datastructure.Pair;
import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.language.Argument;
import castor.language.IdentifierType;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.settings.DataModel;
import castor.settings.Parameters;

public class SamplingUtils {
	
	private static final String SELECT_WHERE_SQL_STATEMENT = "SELECT * FROM %s WHERE %s = %s;";
	private static final String SELECT_SQL_STATEMENT = "SELECT * FROM %s;";

	/*
	 * Finds the join tree at schema level (using modes)
	 */
	public static JoinNode findJoinTree(DataModel dataModel, Parameters parameters) {
		JoinNode headNode = new JoinNode(dataModel.getModeH().getPredicateName());
		
		// Group modes by type
		Map<String,List<Mode>> modesGroupedByType = new HashMap<String,List<Mode>>();
		Set<String> usedModes = new HashSet<String>();
		for (Mode mode : dataModel.getModesB()) {
			// Skip modes if already seen same modes but with different access modes (+,-,#)
			if (!usedModes.contains(mode.toGroundModeString())) {
				usedModes.add(mode.toGroundModeString());
				
				for (Argument argument : mode.getArguments()) {
					if (!modesGroupedByType.containsKey(argument.getType())) {
						modesGroupedByType.put(argument.getType(), new ArrayList<Mode>());
					}
					modesGroupedByType.get(argument.getType()).add(mode);
				}
			}
		}
		
		// Call recursive function
		findJoinTreeAux(headNode, dataModel.getModeH(), modesGroupedByType, 0, parameters.getIterations());
		
		return headNode;
	}

	/*
	 * Recursive auxiliary function for findJoinTree
	 */
	private static void findJoinTreeAux(JoinNode node, Mode modeForNode, Map<String, List<Mode>> modesGroupedByType, int currentIteration, int maxIterations) {
		if (currentIteration == maxIterations)
			return;
		
		// Find input attribute in mode
		int inputAttribute;
		for (inputAttribute = 0; inputAttribute < modeForNode.getArguments().size(); inputAttribute++) {
			if (modeForNode.getArguments().get(inputAttribute).getIdentifierType().equals(IdentifierType.INPUT))
				break;
		}
		
		for (int i = 0; i < modeForNode.getArguments().size(); i++) {
			String type = modeForNode.getArguments().get(i).getType();
			
			// Find other relations that join with current relation
			for (Mode mode : modesGroupedByType.get(type)) {
				for (int j = 0; j < mode.getArguments().size(); j++) {
					// Skip cases where joining same relation over same attribute
					if (modeForNode.getPredicateName().equals(mode.getPredicateName()) && inputAttribute == j)
						continue;
					
					// If same type, relations can join
					if (type.equals(mode.getArguments().get(j).getType())) {
						JoinNode newJoinNode = new JoinNode(mode.getPredicateName());
						node.getEdges().add(new JoinEdge(newJoinNode, i, j));
						
						// Recursive call
						findJoinTreeAux(newJoinNode, mode, modesGroupedByType, currentIteration+1, maxIterations);
					}
				}
			}
		}
	}
	
	public static Map<Pair<Tuple,String>,Integer> computeUpperBoundsStreamSampling(GenericDAO genericDAO, Schema schema, DataModel dataModel, Parameters parameters) {
		// weightWithChildUpperBounds: <t,R> -> W
		Map<Pair<Tuple,String>,Integer> weightWithChildUpperBounds = new HashMap<Pair<Tuple,String>,Integer>();
		
		JoinNode rootNode = SamplingUtils.findJoinTree(dataModel, parameters);
		
		// Group nodes by depth
		Map<Integer,List<JoinNode>> nodesAtDepth = new HashMap<Integer,List<JoinNode>>();
		groupJoinNodesByDepth(rootNode, nodesAtDepth, 0);
		
		// Dynamic programming algorithm
		int maxDepth = Collections.max(nodesAtDepth.keySet());
		for (int i = maxDepth; i >= 1; i--) {
			for (JoinNode node : nodesAtDepth.get(i)) {
				String query = String.format(SELECT_SQL_STATEMENT, node.getRelation());
				GenericTableObject result = genericDAO.executeQuery(query);
				if (result != null) {
					if (i == maxDepth) {
						// Leaf
						for (Tuple tuple : result.getTable()) {
							weightWithChildUpperBounds.put(new Pair<Tuple,String>(tuple, ""), 1);
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
							}
						}
					}
				}
			}
		}
		
		return weightWithChildUpperBounds;
	}
	
	private static void groupJoinNodesByDepth(JoinNode joinNode, Map<Integer,List<JoinNode>> nodesAtDepth, int currentDepth) {
		if (!nodesAtDepth.containsKey(currentDepth)) {
			nodesAtDepth.put(currentDepth, new ArrayList<JoinNode>());
		}
		nodesAtDepth.get(currentDepth).add(joinNode);
		
		for (JoinEdge joinEdge : joinNode.getEdges()) {
			groupJoinNodesByDepth(joinEdge.getJoinNode(), nodesAtDepth, currentDepth+1);
		}
	}
}
