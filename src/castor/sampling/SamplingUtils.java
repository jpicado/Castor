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
import castor.utils.Triple;

public class SamplingUtils {
	
	private static final String SELECT_WHERE_SQL_STATEMENT = "SELECT * FROM %s WHERE %s = %s";
	private static final String SELECT_SQL_STATEMENT = "SELECT * FROM %s";
	private static final String SELECT_GROUPBY_SQL_STATEMENT = "SELECT %s FROM %s GROUP BY %s";

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
			if (!usedModes.contains(mode.toStringOnlyInputAccessModes())) {
				usedModes.add(mode.toStringOnlyInputAccessModes());
				
				for (Argument argument : mode.getArguments()) {
					if (argument.getIdentifierType().equals(IdentifierType.INPUT)) {
						if (!modesGroupedByType.containsKey(argument.getType())) {
							modesGroupedByType.put(argument.getType(), new ArrayList<Mode>());
						}
						modesGroupedByType.get(argument.getType()).add(mode);
					}
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
			if (modesGroupedByType.containsKey(type)) {
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
	}
	
	/*
	 * Finds the join tree at schema level (using modes). If an attribute can be constant, consider each distinct value as a separate join path.
	 */
	public static JoinNode findStratifiedJoinTree(GenericDAO genericDAO, Schema schema, DataModel dataModel, Parameters parameters) {
		JoinNode headNode = new JoinNode(dataModel.getModeH().getPredicateName());
		
		// Group modes by type
		Map<String,List<Mode>> modesGroupedByType = new HashMap<String,List<Mode>>();
		Set<String> usedModes = new HashSet<String>();
		for (Mode mode : dataModel.getModesB()) {
			if (!usedModes.contains(mode.toStringOnlyInputOrConstantAccessModes())) {
				usedModes.add(mode.toStringOnlyInputOrConstantAccessModes());
				
				boolean containsConstantAttribute = containsConstantAttribute(mode);
				
				for (Argument argument : mode.getArguments()) {
					if (argument.getIdentifierType().equals(IdentifierType.INPUT)) {
					
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
										if (previousMode.toStringOnlyInputOrConstantAccessModes().equals(mode.toStringOnlyInputOrConstantAccessModes())) {
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
		}
		
		// Call recursive function
		findStratifiedJoinTreeAux(genericDAO, schema, headNode, dataModel.getModeH(), modesGroupedByType, 0, parameters.getIterations());
		
		return headNode;
	}
	
	/*
	 * Recursive auxiliary function for findStratifiedJoinTree
	 */
	private static void findStratifiedJoinTreeAux(GenericDAO genericDAO, Schema schema, JoinNode node, Mode modeForNode, Map<String, List<Mode>> modesGroupedByType, int currentIteration, int maxIterations) {
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
			if (modesGroupedByType.containsKey(type)) {
				for (Mode mode : modesGroupedByType.get(type)) {
					String relationName = mode.getPredicateName();
					
					List<JoinNodeRelation> nodeRelations = new ArrayList<JoinNodeRelation>();
					
					// Find regions
					List<Integer> constantAttributesPositions = new ArrayList<Integer>();
					List<String> constantAttributesNames = new ArrayList<String>();
					for (int attrPos = 0; attrPos < mode.getArguments().size(); attrPos++) {
						if (mode.getArguments().get(attrPos).getIdentifierType() == IdentifierType.CONSTANT) {
							constantAttributesPositions.add(attrPos);
							String attributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames().get(attrPos);
							constantAttributesNames.add(attributeName);
						}
					}
					
					if (constantAttributesPositions.isEmpty()) {
						nodeRelations.add(new JoinNodeRelation(mode.getPredicateName()));
					} else {
						// Add a nodeRelation for each region
						// Get regions
						String constantAttributesString = String.join(",", constantAttributesNames);
						String getRegionsQuery = String.format(SELECT_GROUPBY_SQL_STATEMENT, constantAttributesString,
								relationName, constantAttributesString);
	
						GenericTableObject getRegionsResult = genericDAO.executeQuery(getRegionsQuery);
						if (getRegionsResult != null) {
							// Each tuple represents a region
							for (Tuple tuple : getRegionsResult.getTable()) {
								nodeRelations.add(new JoinNodeRelation(relationName, constantAttributesNames, tuple.getStringValues()));
							}
						}
					}
					
					for (int j = 0; j < mode.getArguments().size(); j++) {
						// Skip cases where joining same relation over same attribute
						if (modeForNode.getPredicateName().equals(mode.getPredicateName()) && inputAttribute == j)
							continue;
						
						// If same type, relations can join
						if (type.equals(mode.getArguments().get(j).getType())) {
							// Temporal node, created to find 
							JoinNode tempJoinNode = new JoinNode(mode.getPredicateName());
							
							// Recursive call
							findStratifiedJoinTreeAux(genericDAO, schema, tempJoinNode, mode, modesGroupedByType, currentIteration+1, maxIterations);
							
							for (JoinNodeRelation joinNodeRelation : nodeRelations) {
								JoinNode newJoinNode = new JoinNode(joinNodeRelation, tempJoinNode.getEdges());
								node.getEdges().add(new JoinEdge(newJoinNode, i, j));
							}
						}
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
	
	public static List<List<JoinPathNode>> getAllJoinPathsByListFromTree(JoinNode node) {
		List<List<JoinPathNode>> joinPaths = new ArrayList<List<JoinPathNode>>();
		for (JoinEdge edge : node.getEdges()) {
			List<JoinPathNode> newPath = new ArrayList<JoinPathNode>();
			getAllJoinPathsByListFromTreeAux(node.getNodeRelation(), edge.getLeftJoinAttribute(), edge.getJoinNode(), edge.getRightJoinAttribute(), newPath, joinPaths);
		}
		return joinPaths;
	}
	
	private static void getAllJoinPathsByListFromTreeAux(JoinNodeRelation sourceJoinRelation, int sourceJoinAttribute, JoinNode targetJoinNode, int targetJoinAttribute, List<JoinPathNode> currentPath, List<List<JoinPathNode>> allPaths) {
		currentPath.add(new JoinPathNode(sourceJoinRelation, sourceJoinAttribute, targetJoinNode.getNodeRelation(), targetJoinAttribute));
		
		if (targetJoinNode.getEdges().isEmpty()) {
			allPaths.add(currentPath);
		}
		
		for (JoinEdge edge : targetJoinNode.getEdges()) {
			List<JoinPathNode> newPath = new ArrayList<JoinPathNode>(currentPath);
			getAllJoinPathsByListFromTreeAux(targetJoinNode.getNodeRelation(), edge.getLeftJoinAttribute(), edge.getJoinNode(), edge.getRightJoinAttribute(), newPath, allPaths);
		}
	}
	
	public static List<List<String>> getAllJoinPathsByListStringsFromTree(JoinNode node) {
		List<List<String>> joinPaths = new ArrayList<List<String>>();
		List<String> newPath = new ArrayList<String>();
		getAllJoinPathsByListStringsFromTreeAux(node, 0, 0, newPath, joinPaths);
		return joinPaths;
	}
	
	private static void getAllJoinPathsByListStringsFromTreeAux(JoinNode node, int sourceJoinAttribute, int targetJoinAttribute, List<String> currentPath, List<List<String>> allPaths) {
		currentPath.add(//"["+sourceJoinAttribute+"] " +
				node.getNodeRelation().getRelation());// + " [" + targetJoinAttribute + "]");
		
		if (node.getEdges().isEmpty()) {
			allPaths.add(currentPath);
		}
		
		for (JoinEdge edge : node.getEdges()) {
			List<String> newPath = new ArrayList<String>(currentPath);
			getAllJoinPathsByListStringsFromTreeAux(edge.getJoinNode(), edge.getLeftJoinAttribute(), edge.getRightJoinAttribute(), newPath, allPaths);
		}
	}
	
	public static List<JoinNode> getAllJoinPathsFromTree(JoinNode node) {
		List<JoinNode> joinPaths = new ArrayList<JoinNode>();
		
		List<List<JoinPathNode>> joinPathsList = getAllJoinPathsByListFromTree(node);
		for (List<JoinPathNode> joinPath : joinPathsList) {
			JoinNode currentNode = new JoinNode(joinPath.get(0).getLeftJoinRelation());
			joinPaths.add(currentNode);
			for (JoinPathNode pathNode : joinPath) {
				JoinNode newNode = new JoinNode(pathNode.getRightJoinRelation());
				JoinEdge newEdge = new JoinEdge(newNode, pathNode.getLeftJoinAttribute(), pathNode.getRightJoinAttribute());
				currentNode.getEdges().add(newEdge);
				currentNode = newNode;
			}
		}
		
		return joinPaths;
	}
	
	/*
	 * Dynamic programming algorithm to compute size of join paths starting from tuple, for all tuples.
	 * This corresponds to statistics needed by Stream Sampling.
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
	 * Count number of join paths starting from tuple using a query
	 */
	public static long computeJoinPathSizeFromTupleWithQueries(GenericDAO genericDAO, Schema schema, Tuple tuple, JoinNode node) {
		List<List<JoinPathNode>> joinPaths = SamplingUtils.getAllJoinPathsByListFromTree(node);
		
		long size = 1;
		if (joinPaths.size() > 0) {
			// sum sizes of join paths
			for (List<JoinPathNode> joinPath : joinPaths) {
				String countQuery = generateCountQueryForJoinPathStartingFromTuple(schema, joinPath, tuple);
//				System.out.println(countQuery);
				Long result = genericDAO.executeScalarQuery(countQuery);
				if (result > 0) {
					size *= result;
				}
			}
		}
		return size;
	}
	
	/*
	 * Count number of join paths starting from tuple using memorization
	 */
	public static long computeJoinPathSizeFromTuple(GenericDAO genericDAO, Schema schema, Tuple tuple, JoinNode node, int depth, Map<Triple<String,Integer,Tuple>,Long> joinPathSizes, int queryLimit) {
		long size = 1;
		
		Triple<String,Integer,Tuple> key = new Triple<String,Integer,Tuple>(node.getNodeRelation().getRelation(),depth,tuple);
		if (joinPathSizes.containsKey(key)) {
			size = joinPathSizes.get(key);
		} else {
			if (node.getEdges().size() > 0) {
				for (JoinEdge joinEdge : node.getEdges()) {
					if (tuple.getValues().get(joinEdge.getLeftJoinAttribute()) == null) {
						continue;
					}

					// tuple semijoin child
					String leftAttributeValue = tuple.getValues().get(joinEdge.getLeftJoinAttribute()).toString();
					String rightAttributeName = schema.getRelations().get(joinEdge.getJoinNode().getNodeRelation().getRelation().toUpperCase()).getAttributeNames().get(joinEdge.getRightJoinAttribute());
					String query = String.format(SELECT_WHERE_SQL_STATEMENT, joinEdge.getJoinNode().getNodeRelation().getRelation(), rightAttributeName, "'"+leftAttributeValue+"'");
					
					for (int attrPos = 0; attrPos < joinEdge.getJoinNode().getNodeRelation().getConstantAttributeNames().size(); attrPos++) {
						query += " AND ";
						String selectAttributeName = joinEdge.getJoinNode().getNodeRelation().getConstantAttributeNames().get(attrPos);
						String selectAttributeValue = joinEdge.getJoinNode().getNodeRelation().getConstantAttributeValues().get(attrPos);
						query += selectAttributeName + " = '" + selectAttributeValue + "'";
					}
					query += " LIMIT " + queryLimit;
					GenericTableObject result = genericDAO.executeQuery(query);
					
					long sum = 0;
					if (result != null) {
						for (Tuple tupleChild : result.getTable()) {
							sum += computeJoinPathSizeFromTuple(genericDAO, schema, tupleChild, joinEdge.getJoinNode(), depth+1, joinPathSizes, queryLimit);
						}
					}
					if (sum > 0) {
						size *= sum;
					}
				}
			}
			joinPathSizes.put(key, size);
		}
		
		return size;
	}
	
	/*
	 * Count number of join paths starting from tuple joining with relation
	 */
	public static long computeJoinPathSizeFromTupleAndRelation(GenericDAO genericDAO, Schema schema, Tuple tuple, JoinNode node, JoinEdge joinEdge, int depth, Map<Triple<String,Integer,Tuple>,Long> joinPathSizes, int queryLimit) {
		// tuple semijoin child
		String leftAttributeValue = tuple.getValues().get(joinEdge.getLeftJoinAttribute()).toString();
		String rightAttributeName = schema.getRelations().get(joinEdge.getJoinNode().getNodeRelation().getRelation().toUpperCase()).getAttributeNames().get(joinEdge.getRightJoinAttribute());
		String query = String.format(SELECT_WHERE_SQL_STATEMENT, joinEdge.getJoinNode().getNodeRelation().getRelation(), rightAttributeName, "'"+leftAttributeValue+"'");
		
		for (int attrPos = 0; attrPos < joinEdge.getJoinNode().getNodeRelation().getConstantAttributeNames().size(); attrPos++) {
			query += " AND ";
			String selectAttributeName = joinEdge.getJoinNode().getNodeRelation().getConstantAttributeNames().get(attrPos);
			String selectAttributeValue = joinEdge.getJoinNode().getNodeRelation().getConstantAttributeValues().get(attrPos);
			query += selectAttributeName + " = '" + selectAttributeValue + "'";
		}
		GenericTableObject result = genericDAO.executeQuery(query);
		
		long sum = 0;
		if (result != null) {
			for (Tuple tupleChild : result.getTable()) {
				sum += computeJoinPathSizeFromTuple(genericDAO, schema, tupleChild, joinEdge.getJoinNode(), depth+1, joinPathSizes, queryLimit);
			}
		}
		
		return sum;
	}
	
	public static long computeOlkenWeightFromJoinNode(GenericDAO genericDAO, Schema schema, JoinNode node, StatisticsOlkenSampling statistics) {
		long weight = 1;
		
		for (JoinEdge joinEdge : node.getEdges()) {
			String relationName = joinEdge.getJoinNode().getNodeRelation().getRelation();
			String attributeName = schema.getRelations().get(joinEdge.getJoinNode().getNodeRelation().getRelation().toUpperCase()).getAttributeNames().get(joinEdge.getRightJoinAttribute());
			Pair<String,String> key = new Pair<String,String>(relationName, attributeName);
			
			// Multiply by maximum frequency of current relation
			weight *= statistics.getMaximumFrequencyOnAttribute().get(key);
			
			// Multiply by maximum frequencies of children
			weight *= computeOlkenWeightFromJoinNode(genericDAO, schema, joinEdge.getJoinNode(), statistics);
		}
		
		return weight;
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
			String leftAttributeName = schema.getRelations().get(joinPathNode.getLeftJoinRelation().getRelation().toUpperCase()).getAttributeNames().get(joinPathNode.getLeftJoinAttribute());
			String rightAttributeName = schema.getRelations().get(joinPathNode.getRightJoinRelation().getRelation().toUpperCase()).getAttributeNames().get(joinPathNode.getRightJoinAttribute());
			
			String rightRelationAlias = tableNamePrefix + tableCounter++;
			sb.append("SELECT " + rightRelationAlias + ".* FROM " + joinPathNode.getRightJoinRelation().getRelation() + " " + rightRelationAlias + " JOIN (");
			
			if (i == 0) {
				sb.append("SELECT * FROM " + joinPathNode.getLeftJoinRelation().getRelation() + " WHERE ");
				boolean first = true;
				for (int j=0; j<tuple.getValues().size(); j++) {
					if (tuple.getValues().get(j) == null) {
						continue;
					}
					
					if (!first) {
						sb.append(" AND ");
					}
					String attributeName = schema.getRelations().get(joinPathNode.getLeftJoinRelation().getRelation().toUpperCase()).getAttributeNames().get(j);
					sb.append(attributeName + " = '" + tuple.getValues().get(j).toString() + "'" );
					first = false;
				}
				
			}
			
			sb.append(") " + joinPathNode.getLeftJoinRelation().getRelation() + " ON " + rightRelationAlias + "." + rightAttributeName + " = " + joinPathNode.getLeftJoinRelation().getRelation() + "." + leftAttributeName);
		}
		
		sb.append(") " + tableNamePrefix + tableCounter++ + " ");
		
		return sb.toString();
	}
}
