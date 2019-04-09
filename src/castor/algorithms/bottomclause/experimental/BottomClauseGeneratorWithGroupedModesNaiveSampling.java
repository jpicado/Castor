/*
 * Bottom clause generation as described in original algorithm, except that it only does one query to the DB per relation (even for different input attributes).
 * Advantages or disadvantages compared to BottomClauseGeneratorNaiveSampling?
 */
package castor.algorithms.bottomclause.experimental;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import aima.core.logic.fol.parsing.ast.Predicate;
import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.hypotheses.MyClause;
import castor.language.IdentifierType;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;

public class BottomClauseGeneratorWithGroupedModesNaiveSampling extends BottomClauseGeneratorWithGroupedModes {

	private boolean sample;
	
	public BottomClauseGeneratorWithGroupedModesNaiveSampling(boolean sample, int seed) {
		super(seed);
		this.sample = sample;
	}
	
	@Override
	public List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> inTerms, Map<String, Set<String>> newInTerms, Map<String, Set<String>> previousIterationsInTerms,
			Set<String> distinctTerms,
			String relationName, List<Mode> relationModes, int recall, boolean ground, boolean randomizeRecall, int queryLimit, Random randomGenerator) {
		List<Predicate> newLiterals = new LinkedList<Predicate>();
		
		// If sampling is turned off, set recall to max value
		if (!this.sample) {
			recall = Integer.MAX_VALUE;
		}
		
		List<String> expressions = new LinkedList<String>();
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
			
			// Get type for attribute in mode
			String inputAttributeType = mode.getArguments().get(inputVarPosition).getType();
			
			// Skip mode if another mode with same relation and input attribute has already been seen
			String key = mode.getPredicateName() + "_" + inputVarPosition + "_" + inputAttributeType;
			if (!seenModesInputAttribute.contains(key)) {
				String expression = computeExpression(mode, schema, inTerms);
				if (!expression.isEmpty()) {
					expressions.add(expression);
				}
				
				seenModesInputAttribute.add(key);
			}
		}
		
		if (!expressions.isEmpty()) {
	
			// Create query
			// USING OR
	//						StringBuilder queryBuilder = new StringBuilder();
	//						queryBuilder.append("SELECT * FROM " + relation + " WHERE ");
	//						for (int i = 0; i < expressions.size(); i++) {
	//							queryBuilder.append(expressions.get(i));
	//							if (i < expressions.size() - 1) {
	//								queryBuilder.append(" OR ");
	//							}
	//						}
	//						queryBuilder.append(";");
	//						String query = queryBuilder.toString();
			// USING UNION
			StringBuilder queryBuilder = new StringBuilder();
			for (int i = 0; i < expressions.size(); i++) {
				queryBuilder.append(expressions.get(i));
				if (i < expressions.size() - 1) {
					queryBuilder.append(" UNION ");
				}
			}
			String query = queryBuilder.toString();
			query += " LIMIT " + queryLimit;
			
			// Run query
			GenericTableObject result = genericDAO.executeQuery(query);
	
			if (result != null) {				
				if (!randomizeRecall || result.getTable().size() <= recall) {
					// Get first tuples from result
					int solutionsCounter = 0;
					for (Tuple tuple : result.getTable()) {
						if (solutionsCounter >= recall)
							break;
						
						modeOperationsForTuple(tuple, newLiterals, clause, hashConstantToVariable, hashVariableToConstant, newInTerms, previousIterationsInTerms, distinctTerms, relationModes, ground);
						solutionsCounter++;
					}
				} else {
					// Get random tuples from result (without replacement)
					Set<Integer> usedIndexes = new HashSet<Integer>();
					int solutionsCounter = 0;
					while (solutionsCounter < recall) {
						int randomIndex = randomGenerator.nextInt(result.getTable().size());
						if (!usedIndexes.contains(randomIndex)) {
							Tuple tuple = result.getTable().get(randomIndex);
							usedIndexes.add(randomIndex);
							
							modeOperationsForTuple(tuple, newLiterals, clause, hashConstantToVariable, hashVariableToConstant, newInTerms, previousIterationsInTerms, distinctTerms, relationModes, ground);
							solutionsCounter++;
						}
					}
				}
			
				// Apply INDs
	//							if (applyInds) {
	//								followIndChain(genericDAO, schema, clause, newLiterals, hashConstantToVariable,
	//										hashVariableToConstant, newInTerms, distinctTerms, groupedModes, recall, relation,
	//										new HashSet<String>(), ground);
	//							}
				for (Predicate literal : newLiterals) {
					clause.addNegativeLiteral(literal);
				}
			}
		}

		return newLiterals;
	}
	
	private void modeOperationsForTuple(Tuple tuple, List<Predicate> newLiterals, MyClause clause, 
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> newInTerms, Map<String, Set<String>> previousIterationsInTerms, Set<String> distinctTerms,
			List<Mode> relationAttributeModes, boolean ground) {
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
					mode, false, newInTerms, previousIterationsInTerms, distinctTerms);
			
			// Do not add literal if it's exactly the same as head literal
			if (!literal.equals(clause.getPositiveLiterals().get(0).getAtomicSentence())) {
				addNotRepeated(newLiterals, literal);
			}
		}
	}
}
