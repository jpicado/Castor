package castor.algorithms.bottomclause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import aima.core.logic.fol.parsing.ast.Predicate;
import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.hypotheses.MyClause;
import castor.language.IdentifierType;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.settings.DataModel;
import castor.settings.Parameters;
import castor.utils.Pair;

public class BottomClauseGeneratorWithGroupedModesImportanceSampling extends BottomClauseGeneratorWithGroupedModes {

	private boolean sample;

	public BottomClauseGeneratorWithGroupedModesImportanceSampling(boolean sample) {
		super();
		this.sample = sample;
	}
	
	public void run(GenericDAO genericDAO, List<Tuple> examples, boolean isPositive, Schema schema, DataModel dataModel, Parameters parameters, Map<String, Map<String, Map<String, Pair<Integer, Integer>>>> valuesImportance) {
		for (Tuple example : examples) {
			this.computeValuesImportance(valuesImportance, genericDAO, example, isPositive, schema, dataModel.getModeH(), dataModel.getModesB(), parameters.getIterations(), parameters.isUseInds(), parameters.getMaxterms());
		}
	}

	private void computeValuesImportance(Map<String, Map<String, Map<String, Pair<Integer, Integer>>>> valuesImportance, GenericDAO genericDAO,
			Tuple exampleTuple, boolean isPositive, Schema schema, Mode modeH, List<Mode> modesB, int iterations,
			boolean applyInds, int maxTerms) {

		// Check that arities of example and modeH match
		if (modeH.getArguments().size() != exampleTuple.getValues().size()) {
			throw new IllegalArgumentException("Example arity does not match modeH arity.");
		}

		// Known terms for a data type
		Map<String, Set<String>> inTerms = new HashMap<String, Set<String>>();
		Map<String, Set<String>> previousIterationsInTerms = new HashMap<String, Set<String>>();

		// Keep track of all used variables and constants in clause
		Set<String> distinctTerms = new HashSet<String>();

		// Process head
		updateMapsFromTuple(exampleTuple, isPositive, modeH, true, inTerms, previousIterationsInTerms, valuesImportance,
				schema);

		// Group modes by relation name
		Map<String, List<Mode>> groupedModes = new LinkedHashMap<String, List<Mode>>();
		for (Mode mode : modesB) {
			if (!groupedModes.containsKey(mode.getPredicateName())) {
				groupedModes.put(mode.getPredicateName(), new LinkedList<Mode>());
			}
			groupedModes.get(mode.getPredicateName()).add(mode);
		}

		// Create body literals
		for (int j = 0; j < iterations; j++) {
			// Create new inTerms to hold terms for this iteration
			Map<String, Set<String>> newInTerms = new HashMap<String, Set<String>>();

			Iterator<Entry<String, List<Mode>>> groupedModesIterator = groupedModes.entrySet().iterator();
			while (groupedModesIterator.hasNext()) {
				Map.Entry<String, List<Mode>> pair = (Map.Entry<String, List<Mode>>) groupedModesIterator.next();
				List<Mode> relationModes = pair.getValue();

				this.computeValuesImportanceForGroupedModes(genericDAO, schema, isPositive, inTerms, newInTerms,
						previousIterationsInTerms, valuesImportance, relationModes);

				// Apply INDs
				// if (applyInds) {
				// followIndChain(genericDAO, schema, clause, newLiterals,
				// hashConstantToVariable,
				// hashVariableToConstant, newInTerms, distinctTerms, groupedModes, recall,
				// relation,
				// new HashSet<String>(), ground);
				// }
			}

			// Update previousIterationsInTerms
			for (String key : inTerms.keySet()) {
				if (!previousIterationsInTerms.containsKey(key)) {
					previousIterationsInTerms.put(key, new HashSet<String>());
				}
				previousIterationsInTerms.get(key).addAll(inTerms.get(key));
			}

			// Update inTerms
			inTerms.clear();
			inTerms.putAll(newInTerms);

			// Stopping condition: check number of distinct terms
			if (distinctTerms.size() >= maxTerms) {
				break;
			}
		}
	}

	private void updateMapsFromTuple(Tuple tuple, boolean isPositive, Mode mode, boolean headMode,
			Map<String, Set<String>> inTerms, Map<String, Set<String>> previousIterationsInTerms,
			Map<String, Map<String, Map<String, Pair<Integer, Integer>>>> valuesImportance, Schema schema) {
		for (int i = 0; i < mode.getArguments().size(); i++) {
			String value = tuple.getValues().get(i).toString();
			// Add constants to inTerms
			if (headMode || mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.OUTPUT)
					|| mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
				String variableType = mode.getArguments().get(i).getType();

				if (!previousIterationsInTerms.containsKey(variableType)
						|| (previousIterationsInTerms.containsKey(variableType)
								&& !previousIterationsInTerms.get(variableType).contains(value))) {
					if (!inTerms.containsKey(variableType)) {
						inTerms.put(variableType, new HashSet<String>());
					}
					inTerms.get(variableType).add(value);
				}
			}

			if (!headMode && 
					mode.getArguments().get(i).getIdentifierType() == IdentifierType.CONSTANT) {
				// Update values importance
				String relation = mode.getPredicateName();
				String attribute = schema.getRelations().get(relation.toUpperCase()).getAttributeNames().get(i);
				if (!valuesImportance.containsKey(relation)) {
					// Add map for relation
					valuesImportance.put(relation, new HashMap<String, Map<String, Pair<Integer, Integer>>>());
				}
				if (!valuesImportance.get(relation).containsKey(attribute)) {
					// Add map for attribute
					valuesImportance.get(relation).put(attribute, new HashMap<String, Pair<Integer, Integer>>());
				}
				if (valuesImportance.get(relation).get(attribute).containsKey(value)) {
					// Update counts
					if (isPositive) {
						int count = valuesImportance.get(relation).get(attribute).get(value).getFirst();
						valuesImportance.get(relation).get(attribute).get(value).setFirst(count + 1);
					} else {
						int count = valuesImportance.get(relation).get(attribute).get(value).getSecond();
						valuesImportance.get(relation).get(attribute).get(value).setSecond(count + 1);
					}
				} else {
					// Initialize counts
					if (isPositive) {
						valuesImportance.get(relation).get(attribute).put(value, new Pair<Integer, Integer>(1, 0));
					} else {
						valuesImportance.get(relation).get(attribute).put(value, new Pair<Integer, Integer>(0, 1));
					}
				}
			}
		}
	}

	private void computeValuesImportanceForGroupedModes(GenericDAO genericDAO, Schema schema, boolean isPositive,
			Map<String, Set<String>> inTerms, Map<String, Set<String>> newInTerms,
			Map<String, Set<String>> previousIterationsInTerms,
			Map<String, Map<String, Map<String, Pair<Integer, Integer>>>> valuesImportance, List<Mode> relationModes) {

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

			// Skip mode if another mode with same relation and input attribute has already
			// been seen
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
			// USING UNION
			StringBuilder queryBuilder = new StringBuilder();
			for (int i = 0; i < expressions.size(); i++) {
				queryBuilder.append(expressions.get(i));
				if (i < expressions.size() - 1) {
					queryBuilder.append(" UNION ");
				}
			}
			queryBuilder.append(";");
			String query = queryBuilder.toString();

			// Run query
			GenericTableObject result = genericDAO.executeQuery(query);

			if (result != null) {
				for (Mode mode : relationModes) {
					for (Tuple tuple : result.getTable()) {
						updateMapsFromTuple(tuple, isPositive, mode, false, newInTerms, previousIterationsInTerms,
								valuesImportance, schema);
					}
				}

				// Apply INDs
				// if (applyInds) {
				// followIndChain(genericDAO, schema, clause, newLiterals,
				// hashConstantToVariable,
				// hashVariableToConstant, newInTerms, distinctTerms, groupedModes, recall,
				// relation,
				// new HashSet<String>(), ground);
				// }
			}
		}
	}
	
	private List<Tuple> sortByImportance(Map<String, Map<String, Map<String, Pair<Integer, Integer>>>> valuesImportance, List<Tuple> tuples, List<Mode> relationModes) {
		List<Tuple> sortedTuples = new ArrayList<Tuple>();
		
		
		
		return sortedTuples;
	}

	@Override
	public List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> inTerms, Map<String, Set<String>> newInTerms,
			Map<String, Set<String>> previousIterationsInTerms, Set<String> distinctTerms, String relationName,
			List<Mode> relationModes, int recall, boolean ground) {
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

			// Skip mode if another mode with same relation and input attribute has already
			// been seen
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
			// StringBuilder queryBuilder = new StringBuilder();
			// queryBuilder.append("SELECT * FROM " + relation + " WHERE ");
			// for (int i = 0; i < expressions.size(); i++) {
			// queryBuilder.append(expressions.get(i));
			// if (i < expressions.size() - 1) {
			// queryBuilder.append(" OR ");
			// }
			// }
			// queryBuilder.append(";");
			// String query = queryBuilder.toString();
			// USING UNION
			StringBuilder queryBuilder = new StringBuilder();
			for (int i = 0; i < expressions.size(); i++) {
				queryBuilder.append(expressions.get(i));
				if (i < expressions.size() - 1) {
					queryBuilder.append(" UNION ");
				}
			}
			queryBuilder.append(";");
			String query = queryBuilder.toString();

			// Run query
			GenericTableObject result = genericDAO.executeQuery(query);

			if (result != null) {
				Set<String> usedModes = new HashSet<String>();
				for (Mode mode : relationModes) {
					if (ground) {
						if (usedModes.contains(mode.toGroundModeString())) {
							continue;
						} else {
							mode = mode.toGroundMode();
							usedModes.add(mode.toGroundModeString());
						}
					}
					int solutionsCounter = 0;
					for (Tuple tuple : result.getTable()) {
						if (solutionsCounter >= recall)
							break;

						Predicate literal = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant,
								tuple, mode, false, newInTerms, previousIterationsInTerms, distinctTerms);

						// Do not add literal if it's exactly the same as head literal
						if (!literal.equals(clause.getPositiveLiterals().get(0).getAtomicSentence())) {
							addNotRepeated(newLiterals, literal);
							solutionsCounter++;
						}
					}
				}

				// Apply INDs
				// if (applyInds) {
				// followIndChain(genericDAO, schema, clause, newLiterals,
				// hashConstantToVariable,
				// hashVariableToConstant, newInTerms, distinctTerms, groupedModes, recall,
				// relation,
				// new HashSet<String>(), ground);
				// }
				for (Predicate literal : newLiterals) {
					clause.addNegativeLiteral(literal);
				}
			}
		}

		return newLiterals;
	}
}
