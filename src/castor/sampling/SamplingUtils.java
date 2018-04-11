package castor.sampling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
			if (!usedModes.contains(mode.toStringWithoutAccessModes())) {
				usedModes.add(mode.toStringWithoutAccessModes());
				
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
	
	public static JoinNode findStratifiedJoinTree(DataModel dataModel, Parameters parameters) {
		JoinNode headNode = new JoinNode(dataModel.getModeH().getPredicateName());
		
		// Group modes by type
		Map<String,List<Mode>> modesGroupedByType = new HashMap<String,List<Mode>>();
		Set<String> usedModes = new HashSet<String>();
		for (Mode mode : dataModel.getModesB()) {
			if (!usedModes.contains(mode.toStringOnlyConstantAccessModes())) {
				usedModes.add(mode.toStringOnlyConstantAccessModes());
				
				boolean containsConstantAttribute = containsConstantAttribute(mode);
				
				for (Argument argument : mode.getArguments()) {
					if (!modesGroupedByType.containsKey(argument.getType())) {
						modesGroupedByType.put(argument.getType(), new ArrayList<Mode>());
					}
					
					// If mode contains constant attribute, remove previous modes that do not contain constant attributes or previous modes with same constant attribute
					if (containsConstantAttribute) {
						Iterator<Mode> it = modesGroupedByType.get(argument.getType()).iterator();
						while (it.hasNext()) {
							Mode previousMode = it.next();
							if (previousMode.toStringWithoutAccessModes().equals(mode.toStringWithoutAccessModes())) {
								if (containsConstantAttribute(previousMode)) {
									// Remove previous modes with same constant attribute
									if (previousMode.toStringOnlyConstantAccessModes().equals(mode.toStringOnlyConstantAccessModes())) {
										it.remove();
									}
								} else {
									// Remove modes that do not contain constant attributes
									it.remove();
								}
							}
						}
					}
					
					modesGroupedByType.get(argument.getType()).add(mode);
				}
			}
		}
		
		// Call recursive function
		findStratifiedJoinTreeAux(headNode, dataModel.getModeH(), modesGroupedByType, 0, parameters.getIterations());
		
		return headNode;
	}
	
	//TODO finish implementation
	private static void findStratifiedJoinTreeAux(JoinNode node, Mode modeForNode, Map<String, List<Mode>> modesGroupedByType, int currentIteration, int maxIterations) {
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
				List<JoinNodeRelation> nodeRelations = new ArrayList<JoinNodeRelation>();
				
				// Find regions
				List<Integer> constantAttributesPositions = new ArrayList<Integer>();
				for (int j = 0; i < mode.getArguments().size(); i++) {
					if (mode.getArguments().get(j).getIdentifierType() == IdentifierType.CONSTANT) {
						constantAttributesPositions.add(i);
					}
				}
				
				if (constantAttributesPositions.isEmpty()) {
					nodeRelations.add(new JoinNodeRelation(mode.getPredicateName()));
				} else {
					// Add a nodeRelation for each region
				}
				
				
				
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
	
	private static boolean containsConstantAttribute(Mode mode) {
		for (Argument argument : mode.getArguments()) {
			if (argument.getIdentifierType().equals(IdentifierType.CONSTANT)) 
				return true;
		}
		return false;
	}
	
	public static List<List<JoinPathNode>> getAllJoinPathsFromTree(JoinNode node) {
		List<List<JoinPathNode>> joinPaths = new ArrayList<List<JoinPathNode>>();
		for (JoinEdge edge : node.getEdges()) {
			List<JoinPathNode> newPath = new ArrayList<JoinPathNode>();
			getAllJoinPathsFromTreeAux(node.getNodeRelation().getRelation(), edge.getLeftJoinAttribute(), edge.getJoinNode(), edge.getRightJoinAttribute(), newPath, joinPaths);
		}
		return joinPaths;
	}
	
	private static void getAllJoinPathsFromTreeAux(String sourceJoinRelation, int sourceJoinAttribute, JoinNode targetJoinNode, int targetJoinAttribute, List<JoinPathNode> currentPath, List<List<JoinPathNode>> allPaths) {
		currentPath.add(new JoinPathNode(sourceJoinRelation, sourceJoinAttribute, targetJoinNode.getNodeRelation().getRelation(), targetJoinAttribute));
		
		if (targetJoinNode.getEdges().isEmpty()) {
			allPaths.add(currentPath);
		}
		
		for (JoinEdge edge : targetJoinNode.getEdges()) {
			List<JoinPathNode> newPath = new ArrayList<JoinPathNode>(currentPath);
			getAllJoinPathsFromTreeAux(targetJoinNode.getNodeRelation().getRelation(), edge.getLeftJoinAttribute(), edge.getJoinNode(), edge.getRightJoinAttribute(), newPath, allPaths);
		}
	}
	
	public static List<List<String>> getAllJoinPathsStringsFromTree(JoinNode node) {
		List<List<String>> joinPaths = new ArrayList<List<String>>();
		List<String> newPath = new ArrayList<String>();
		getAllJoinPathsStringsFromTreeAux(node, 0, 0, newPath, joinPaths);
		return joinPaths;
	}
	
	private static void getAllJoinPathsStringsFromTreeAux(JoinNode node, int sourceJoinAttribute, int targetJoinAttribute, List<String> currentPath, List<List<String>> allPaths) {
		currentPath.add(//"["+sourceJoinAttribute+"] " +
				node.getNodeRelation().getRelation());// + " [" + targetJoinAttribute + "]");
		
		if (node.getEdges().isEmpty()) {
			allPaths.add(currentPath);
		}
		
		for (JoinEdge edge : node.getEdges()) {
			List<String> newPath = new ArrayList<String>(currentPath);
			getAllJoinPathsStringsFromTreeAux(edge.getJoinNode(), edge.getLeftJoinAttribute(), edge.getRightJoinAttribute(), newPath, allPaths);
		}
	}
	
	/*
	 * Dynamic programming algorithm to compute size of join paths starting from tuple, for all tuples.
	 * This corresponds to statistics needed by Stream Sampling
	 */
	public static Map<Pair<Tuple,String>,Integer> computeJoinPathSizeAllTuples(GenericDAO genericDAO, Schema schema, DataModel dataModel, Parameters parameters) {
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
				String query = String.format(SELECT_SQL_STATEMENT, node.getNodeRelation().getRelation());
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
								String rightRelation = rightNode.getNodeRelation().getRelation();
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
												product *= weightWithChildUpperBounds.get(new Pair<Tuple,String>(tupleChild,childJoinEdge.getJoinNode().getNodeRelation().getRelation()));
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
	
	/*
	 * Count number of join paths starting from tuple
	 */
	public static long computeJoinPathSizeFromTuple(GenericDAO genericDAO, Schema schema, Tuple tuple, JoinNode node) {
		List<List<JoinPathNode>> joinPaths = SamplingUtils.getAllJoinPathsFromTree(node);
		
		long size = 0;
		if (joinPaths.size() == 0) {
			// current tuple is only joinPath
			size = 1;
		} else {
			// sum sizes of join paths
			for (List<JoinPathNode> joinPath : joinPaths) {
				String countQuery = generateCountQueryForJoinPathStartingFromTuple(schema, joinPath, tuple);
//				System.out.println(countQuery);
				Long result = genericDAO.executeScalarQuery(countQuery);
				size += result;
			}
		}
		return size;
	}

	/* 
	 * Generate query to count join paths starting from tuple
	 */
	private static String generateCountQueryForJoinPathStartingFromTuple(Schema schema, List<JoinPathNode> joinPath, Tuple tuple) {
		StringBuilder sb = new StringBuilder();
		int tableCounter = 0;
		String tableNamePrefix = "t";
		
		sb.append("SELECT COUNT(*) FROM (");
		
		for (int i=joinPath.size()-1; i >= 0; i--) {
			JoinPathNode joinPathNode = joinPath.get(i);
			String leftAttributeName = schema.getRelations().get(joinPathNode.getLeftJoinRelation().toUpperCase()).getAttributeNames().get(joinPathNode.getLeftJoinAttribute());
			String rightAttributeName = schema.getRelations().get(joinPathNode.getRightJoinRelation().toUpperCase()).getAttributeNames().get(joinPathNode.getRightJoinAttribute());
			
			String rightRelationAlias = tableNamePrefix + tableCounter++;
			sb.append("SELECT " + rightRelationAlias + ".* FROM " + joinPathNode.getRightJoinRelation() + " " + rightRelationAlias + " JOIN (");
			
			if (i == 0) {
				sb.append("SELECT * FROM " + joinPathNode.getLeftJoinRelation() + " WHERE ");
				for (int j=0; j<tuple.getValues().size(); j++) {
					if (j != 0) {
						sb.append(" AND ");
					}
					String attributeName = schema.getRelations().get(joinPathNode.getLeftJoinRelation().toUpperCase()).getAttributeNames().get(j);
					sb.append(attributeName + " = '" + tuple.getValues().get(j).toString() + "'" );
				}
				
			}
			
			sb.append(") " + joinPathNode.getLeftJoinRelation() + " ON " + rightRelationAlias + "." + rightAttributeName + " = " + joinPathNode.getLeftJoinRelation() + "." + leftAttributeName);
		}
		
		sb.append(") " + tableNamePrefix + tableCounter++ + " ");
		
		return sb.toString();
	}
}
