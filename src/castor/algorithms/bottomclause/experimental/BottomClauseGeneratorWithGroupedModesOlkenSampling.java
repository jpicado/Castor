/*
 * Bottom-clause construction using Olken sampling.
 * Queries database only once per relation.
 * Fixes problem in BottomClauseGeneratorOlkenSampling of giving higher inclusion probabilities to some tuples. 
 */
package castor.algorithms.bottomclause.experimental;

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
import castor.language.IdentifierType;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.sampling.StatisticsOlkenSampling;
import castor.utils.RandomSet;

public class BottomClauseGeneratorWithGroupedModesOlkenSampling extends BottomClauseGeneratorWithGroupedModes {

	private static final String SELECT_SQL_STATEMENT = "SELECT * FROM %s WHERE %s = %s";
	private static final String COUNT_DISTINCT_TUPLES_SQL_STATEMENT = "SELECT COUNT(DISTINCT *) FROM %s WHERE %s = %s;";

	private StatisticsOlkenSampling statistics;

	public BottomClauseGeneratorWithGroupedModesOlkenSampling(int seed, StatisticsOlkenSampling statistics) {
		super(seed);
		this.statistics = statistics;
	}

	/*
	 * Implements ideas from Olken's random sampling algorithm.
	 * All grouped modes are for same relation.
	 */
	public List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> inTerms, Map<String, Set<String>> newInTerms, Map<String, Set<String>> previousIterationsInTerms,
			Set<String> distinctTerms,
			String relationName, List<Mode> relationModes, int recall, boolean ground, boolean shuffleTuples, int queryLimit, Random randomGenerator) {
		List<Predicate> newLiterals = new LinkedList<Predicate>();
		
		// COUNT query: Count the number of potential tuples
		List<String> expressions = new LinkedList<String>();
		Set<String> seenModesInputAttributeForCountQuery = new HashSet<String>();
		for (Mode mode : relationModes) {
			// Find input attribute position
			int inputVarPosition = 0;
			for (int i = 0; i < mode.getArguments().size(); i++) {
				if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.INPUT)) {
					inputVarPosition = i;
					break;
				}
			}
			
			// Get type for attribute in mode
			String inputAttributeType = mode.getArguments().get(inputVarPosition).getType();
			
			// Skip mode if another mode with same relation and input attribute has already been seen
			String key = mode.getPredicateName() + "_" + inputVarPosition + "_" + inputAttributeType;
			if (!seenModesInputAttributeForCountQuery.contains(key)) {
				String expression = computeExpression(mode, schema, inTerms);
				if (!expression.isEmpty()) {
					expressions.add(expression);
				}
				
				seenModesInputAttributeForCountQuery.add(key);
			}
		}
		
		// If expressions is empty (haven't seen any constant for type, return)
		if (expressions.isEmpty()) {
			return newLiterals;
		}
		
		// Create query
		// USING UNION
		StringBuilder countQueryBuilder = new StringBuilder();
		for (int i = 0; i < expressions.size(); i++) {
			countQueryBuilder.append(expressions.get(i));
			if (i < expressions.size() - 1) {
				countQueryBuilder.append(" UNION ");
			}
		}
		String countQuery = "SELECT COUNT(*) FROM (" + countQueryBuilder.toString() + ") t;";
		long numberOfTuples = genericDAO.executeScalarQuery(countQuery);
		
		// If number of tuples is zero, return
		if (numberOfTuples == 0) {
			return newLiterals;
		}
		// COUNT query
		
		// EXCEPT query: Create queries to be used to exclude tuples (using EXCEPT)
		StringBuilder exceptQueryBuilder = new StringBuilder();
		for (int i = 0; i < relationModes.get(0).getArguments().size(); i++) {
			// Find all types for current attribute
			Set<String> attributeTypes = new HashSet<String>();
			for (Mode mode : relationModes) {
				attributeTypes.add(mode.getArguments().get(i).getType());
			}
			
			// Find values in previousTerms
			Set<String> previousTermsSet = new HashSet<String>();
			for (String type : attributeTypes) {
				if (previousIterationsInTerms.containsKey(type)) {
					previousTermsSet.addAll(previousIterationsInTerms.get(type));
				}
			}
			
			// Create and add query to list
			if (!previousTermsSet.isEmpty()) {
				String knownTerms = toListString(previousTermsSet);
				String attributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames().get(i);
				String query = String.format(SELECTIN_SQL_STATEMENT, relationName, attributeName, knownTerms);
				
				if (exceptQueryBuilder.length() > 0) {
					exceptQueryBuilder.append(" UNION ");
				}
				exceptQueryBuilder.append(query);
			}
		}
		String exceptQuery = exceptQueryBuilder.toString();
		// EXCEPT query
		
		// RAJOIN algorithm (from Olken's thesis)
		Set<Tuple> usedTuples = new HashSet<Tuple>();
		for(int solutionsCounter=0; solutionsCounter < recall; solutionsCounter++) {
			//TODO max number of times to run the following loop?
			boolean accept = false;
			while (accept == false) {
				// For each input attribute in relation modes, get a random value
				List<Pair<String,String>> attributeValues = new LinkedList<Pair<String,String>>();
				double probabilitySum = 0;
				
				// Keep track of modes-input_attribute pairs, so that we get only one random value per modes-input_attribute pair
				Set<String> seenModesInputAttribute = new HashSet<String>();
				for (Mode mode : relationModes) {
					// Find input attribute position
					int inputVarPosition = 0;
					for (int i = 0; i < mode.getArguments().size(); i++) {
						if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.INPUT)) {
							inputVarPosition = i;
							break;
						}
					}
					
					// Get attribute name and type in mode
					String inputAttributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames().get(inputVarPosition);
					String inputAttributeType = mode.getArguments().get(inputVarPosition).getType();
					
					// Skip mode if another mode with same relation and input attribute has already been seen
					String keyRelationAttribute = mode.getPredicateName() + "_" + inputVarPosition + "_" + inputAttributeType;
					if (!seenModesInputAttribute.contains(keyRelationAttribute)) {
						seenModesInputAttribute.add(keyRelationAttribute);
						
						// Get known terms for all attribute types
						RandomSet<String> knownTermsSet = new RandomSet<String>();
						if (!inTerms.containsKey(inputAttributeType)) {
							// If there is no list of known terms for attributeType, skip mode
							continue;
						}
						knownTermsSet.addAll(inTerms.get(inputAttributeType));
						
						// Remove terms from previous iterations
						if (previousIterationsInTerms.containsKey(inputAttributeType)) {
							knownTermsSet.removeAll(previousIterationsInTerms.get(inputAttributeType));
						}
						
						String randomValue = "";
						long cardinality = 0;
						for (int i = 0; i < knownTermsSet.size(); i++) {
							// Sample a constant from known constants
							randomValue = knownTermsSet.get(randomGenerator.nextInt(knownTermsSet.size()));
							
							// Find distinct tuples in relation with attribute = randomValue
							String queryCardinality = String.format(COUNT_DISTINCT_TUPLES_SQL_STATEMENT, relationName, inputAttributeName, "'"+randomValue+"'");
							cardinality = genericDAO.executeScalarQuery(queryCardinality);
							
							if (cardinality == 0) {
								knownTermsSet.remove(randomValue);
							} else if (cardinality > 0) {
								// found value
								break;
							}
						}
						
						if (cardinality > 0) {
							// Add to attribute-value pair to attributeValues
							attributeValues.add(new Pair<String,String>(inputAttributeName, randomValue));
							
							// Compute probability and add to sum
							Pair<String,String> key = new Pair<String, String>(relationName.toUpperCase(), inputAttributeName.toUpperCase());
							double p = (double)cardinality / (double)statistics.getMaximumFrequencyOnAttribute().get(key);
							probabilitySum += p;
							//TODO: I THINK THAT THIS WAY OF COMPUTING PROBABILITY IS WRONG
							//1) DENOMINATOR SHOULD BE BASED ON THE TUPLES THAT CAN BE JOINED, NOT ON ALL TUPLES IN RELATION
							//2) IS IT OK TO SIMPLY SUM UP PROBABILITIES FOR EACH ATTRIBUTE?
						}
					}
				}
				
				if (attributeValues.isEmpty()) {
					// Trick to avoid infinite loops
					accept = true;
					break;
				}
				
				// Generate random number and check if accept or reject
				if (randomGenerator.nextDouble() < probabilitySum) {
					// Create query
					StringBuilder queryBuilder = new StringBuilder();
					for (int i = 0; i < attributeValues.size(); i++) {
						Pair<String,String> attributeValue = attributeValues.get(i);
						String queryForAttribute = String.format(SELECT_SQL_STATEMENT, relationName, attributeValue.getFirst(), "'"+attributeValue.getSecond()+"'");
						queryBuilder.append(queryForAttribute);
						if (i < attributeValues.size() - 1) {
							queryBuilder.append(" UNION ");
						}
					}
					// Add EXCEPT
					if (exceptQuery.length() > 0) {
						queryBuilder.append(" EXCEPT ( ");
						queryBuilder.append(exceptQuery);
						queryBuilder.append(")");
					}
					queryBuilder.append(";");
					String query = queryBuilder.toString();
					
					// Run query
					GenericTableObject result = genericDAO.executeQuery(query);
					
					if (result == null || result.getTable().isEmpty()) {
						// If result is empty, join of current value is empty, so exit current loop
						break;
					} else if (usedTuples.size() == numberOfTuples) {
						solutionsCounter += recall;
						break;
					} else {
						// Sample a tuple from result
						//TODO can we get a single sample directly from the query to the DB?
						Tuple tuple = result.getTable().get(randomGenerator.nextInt(result.getTable().size()));
						
						// If I already used this tuple, skip and try to get another one
						if (usedTuples.contains(tuple)) {
							continue;
						}
						usedTuples.add(tuple);
						
						Set<String> usedModes = new HashSet<String>();
						for (Mode mode : relationModes) {
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
									mode, false, newInTerms, previousIterationsInTerms, distinctTerms);
							
							// Do not add literal if it's exactly the same as head literal
							if (!literal.equals(clause.getPositiveLiterals().get(0).getAtomicSentence())) {
								addNotRepeated(newLiterals, literal);
							}
						}
						
						accept = true;
					}
				}
			}
		}
		
		return newLiterals;
	}
}
