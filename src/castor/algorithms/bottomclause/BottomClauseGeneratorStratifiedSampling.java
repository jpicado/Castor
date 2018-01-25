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

import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.hypotheses.MyClause;
import castor.language.IdentifierType;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.mappings.MyClauseToIDAClause;
import castor.settings.DataModel;
import castor.settings.Parameters;
import castor.utils.Commons;
import castor.utils.Pair;
import castor.utils.RandomSet;

public class BottomClauseGeneratorStratifiedSampling implements BottomClauseGenerator {

	protected static final String SELECTIN_SQL_STATEMENT = "SELECT * FROM %s WHERE %s IN %s";
	protected static final String SELECT_SQL_STATEMENT = "SELECT * FROM %s";
	protected static final String SELECTDISTINCT_SQL_STATEMENT = "SELECT DISTINCT(%s) FROM %s";
	protected static final String SELECTWHERE_SQL_STATEMENT = "SELECT * FROM %s WHERE %s";
	
	protected int varCounter;

	public BottomClauseGeneratorStratifiedSampling(boolean sample) {
		this.varCounter = 0;
	}
	
	@Override
	public MyClause generateBottomClause(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO,
			Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters) {
		return generateBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema, dataModel, parameters, false);
	}

	@Override
	public MyClause generateGroundBottomClause(GenericDAO genericDAO,
			BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema,
			DataModel dataModel, Parameters parameters) {
		return generateBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema, dataModel, parameters, true);
	}

	@Override
	public String generateGroundBottomClauseString(GenericDAO genericDAO,
			BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema,
			DataModel dataModel, Parameters parameters) {
		MyClause clause = generateBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema, dataModel, parameters);
		return clause.toString2(MyClauseToIDAClause.POSITIVE_SYMBOL, MyClauseToIDAClause.NEGATE_SYMBOL);
	}
	
	public MyClause generateBottomClause(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, 
			Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters, boolean ground) {
		
		
		Map<Pair<String, Integer>, List<Mode>> groupedModes = new LinkedHashMap<Pair<String, Integer>, List<Mode>>();
		Map<String, List<Pair<String, Integer>>> groupedRelationsByAttributeType = new LinkedHashMap<String, List<Pair<String, Integer>>>();
		for (Mode mode : dataModel.getModesB()) {
			// Get position of input attribute
			int inputAttributePosition = 0;
			for (int i = 0; i < mode.getArguments().size(); i++) {
				if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.INPUT)) {
					inputAttributePosition = i;
					break;
				}
			}

			// Group modes by relation name - input attribute position
			Pair<String, Integer> key = new Pair<String, Integer>(mode.getPredicateName(), inputAttributePosition);
			if (!groupedModes.containsKey(key)) {
				groupedModes.put(key, new LinkedList<Mode>());
			}
			groupedModes.get(key).add(mode);
			
			// Group modes by input attribute type
			String inputAttributeType = mode.getArguments().get(inputAttributePosition).getType();
			if (!groupedRelationsByAttributeType.containsKey(inputAttributeType)) {
				groupedRelationsByAttributeType.put(inputAttributeType, new LinkedList<Pair<String, Integer>>());
			}
			groupedRelationsByAttributeType.get(inputAttributeType).add(new Pair<String, Integer>(mode.getPredicateName(), inputAttributePosition));
		}
		
		MyClause clause = new MyClause();
		varCounter = 0;
		Map<String, String> hashConstantToVariable = new HashMap<String, String>();
		Map<String, String> hashVariableToConstant = new HashMap<String, String>();
		
		// Create head literal
		Mode modeH = dataModel.getModeH();
		if (ground) {
			modeH = modeH.toGroundMode();
		}
		Predicate headLiteral = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant, exampleTuple,
				dataModel.getModeH(), true);
		clause.addPositiveLiteral(headLiteral);
		
		for (int i = 0; i < exampleTuple.getValues().size(); i++) {
			String attributeType = modeH.getArguments().get(i).getType();
			Set<Object> values = new HashSet<Object>();
			values.add(exampleTuple.getValues().get(i));
			
			for (Pair<String, Integer> relationInputAttributePair : groupedRelationsByAttributeType.get(attributeType)) {
				String relationName = relationInputAttributePair.getFirst();
				int inputAttributePosition = relationInputAttributePair.getSecond();
				
				stratifiedSamplingRecursive(genericDAO, schema, groupedModes, hashConstantToVariable, hashVariableToConstant, relationName, inputAttributePosition, values, parameters.getIterations(), 1, clause);
			}
		}
		
		return clause;
	}
	
	private List<Tuple> stratifiedSamplingRecursive(GenericDAO genericDAO, Schema schema, 
			Map<Pair<String, Integer>, List<Mode>> groupedModes,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant, 
			String relationName, int inputAttributePosition, Set<Object> values, 
			int iterations, int currentIteration, MyClause clause) {
		
		String inputAttributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames().get(inputAttributePosition);
			
		return new LinkedList<Tuple>();
	}
	
	/*
	 * Creates a literal from a tuple and a mode.
	 */
	protected Predicate createLiteralFromTuple(Map<String, String> hashConstantToVariable,
			Map<String, String> hashVariableToConstant, Tuple tuple, Mode mode, boolean headMode) {
		List<Term> terms = new ArrayList<Term>();
		for (int i = 0; i < mode.getArguments().size(); i++) {
			String value = tuple.getValues().get(i).toString();

			if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
				terms.add(new Constant("\"" + value + "\""));
			} else {
				// INPUT or OUTPUT type
				if (!hashConstantToVariable.containsKey(value)) {
					String var = Commons.newVariable(varCounter);
					varCounter++;

					hashConstantToVariable.put(value, var);
					hashVariableToConstant.put(var, value);
				}
				terms.add(new Variable(hashConstantToVariable.get(value)));
			}
			// Add constants to inTerms
//			if (headMode ||
//					mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.OUTPUT) ||
//					mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
//				String variableType = mode.getArguments().get(i).getType();
//				if (!inTerms.containsKey(variableType)) {
//					inTerms.put(variableType, new HashSet<String>());
//				}
//				inTerms.get(variableType).add(value);
//			}
		}
		
		Predicate literal = new Predicate(mode.getPredicateName(), terms);
		return literal;
	}
	
	private List<List<Tuple>> computeStrata(GenericDAO genericDAO, Schema schema, String relationName, List<Mode> relationModes) {
		List<List<Tuple>> strata = new LinkedList<List<Tuple>>();
		
		// Get attributes that can be constant
		RandomSet<String> constantAttributes = new RandomSet<String>();
		for (Mode mode : relationModes) {
			for (int i=0; i < mode.getArguments().size(); i++) {
				if (mode.getArguments().get(i).getIdentifierType() == IdentifierType.CONSTANT) {
					String attributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames().get(i);
					constantAttributes.add(attributeName);
				}
			}
		}
		
		if (constantAttributes.isEmpty()) {
			// Add whole table as stratum
			String stratumQuery = String.format(SELECT_SQL_STATEMENT, relationName);
			GenericTableObject stratumResult = genericDAO.executeQuery(stratumQuery);
			if (stratumResult != null) {
				strata.add(stratumResult.getTable());
			}
		} else {
			// Get regions
			String constantAttributesString = String.join(",", constantAttributes);
			String getRegionsQuery = String.format(SELECTDISTINCT_SQL_STATEMENT, constantAttributesString, relationName);
			
			GenericTableObject getRegionsResult = genericDAO.executeQuery(getRegionsQuery);
			if (getRegionsResult != null) {
				// Each tuple represents a region
				for (Tuple tuple : getRegionsResult.getTable()) {
					StringBuilder whereExpression = new StringBuilder();
					for (int i = 0; i < constantAttributes.size(); i++) {
						if (i > 0)
							whereExpression.append(" AND ");
						
						String attribute = constantAttributes.get(i);
						String value = tuple.getValues().get(i).toString();
						
						whereExpression.append(attribute + "=" + value);
					}
					
					// Get stratum
					String stratumQuery = String.format(SELECTWHERE_SQL_STATEMENT, relationName, whereExpression.toString()); 
					GenericTableObject stratumResult = genericDAO.executeQuery(stratumQuery);
					if (stratumResult != null) {
						strata.add(stratumResult.getTable());
					}
				}
			}
		}
		
		return strata;
	}
	
	public void runComputeValuesImportance(GenericDAO genericDAO, List<Tuple> examples, boolean isPositive, Schema schema, DataModel dataModel, Parameters parameters, Map<String, Map<String, Map<String, Pair<Integer, Integer>>>> valuesImportance) {
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
	
	/*
	 * Compute expression to be used in an SQL query
	 */
	private String computeExpression(Mode mode, Schema schema, Map<String, Set<String>> inTerms) {
		String expression = "";

		int inputVarPosition = 0;
		for (int i = 0; i < mode.getArguments().size(); i++) {
			if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.INPUT)) {
				inputVarPosition = i;
				break;
			}
		}

		String attributeName = schema.getRelations().get(mode.getPredicateName().toUpperCase()).getAttributeNames()
				.get(inputVarPosition);
		String attributeType = mode.getArguments().get(inputVarPosition).getType();

		// If there is no list of known terms for attributeType, skip mode
		if (inTerms.containsKey(attributeType)) {
			String knownTerms = toListString(inTerms.get(attributeType));
			// USING OR
//			expression = attributeName + " IN " + knownTerms;
			
			// USING UNION
			expression = String.format(SELECTIN_SQL_STATEMENT, mode.getPredicateName(), attributeName, knownTerms);
		}
		return expression;
	}
	
	/*
	 * Convert set to string "('item1','item2',...)"
	 */
	protected String toListString(Set<String> terms) {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		int counter = 0;
		for (String term : terms) {
			// Escape single quotes
			term = term.replace("'", "''");
			builder.append("'" + term + "'");
			if (counter < terms.size() - 1) {
				builder.append(",");
			}
			counter++;
		}
		builder.append(")");
		return builder.toString();
	}
}
