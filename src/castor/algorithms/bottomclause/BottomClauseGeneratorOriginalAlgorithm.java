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
import aima.core.util.datastructure.Pair;
import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.hypotheses.MyClause;
import castor.language.IdentifierType;
import castor.language.InclusionDependency;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.mappings.MyClauseToIDAClause;
import castor.settings.DataModel;
import castor.settings.Parameters;
import castor.utils.Commons;
import castor.utils.RandomSet;

public abstract class BottomClauseGeneratorOriginalAlgorithm implements BottomClauseGenerator {

	protected static final String SELECTIN_SQL_STATEMENT = "SELECT * FROM %s WHERE %s IN %s;";

	protected int varCounter;

	public BottomClauseGeneratorOriginalAlgorithm() {
		varCounter = 0;
	}

	/*
	 * Generate bottom clause for one example
	 */
	@Override
	public MyClause generateBottomClause(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, 
			Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters) {
		Map<String, String> hashConstantToVariable = new HashMap<String, String>();
		Map<String, String> hashVariableToConstant = new HashMap<String, String>();
		return this.generateBottomClauseOneQueryPerRelation(genericDAO, hashConstantToVariable,
				hashVariableToConstant, exampleTuple, schema, dataModel.getModeH(), dataModel.getModesB(), parameters.getIterations(), parameters.getRecall(), parameters.isUseInds(), parameters.getMaxterms(), false);
	}
	
	/*
	 * Generate bottom clause for each input example in examples list Reuses hash
	 * function to keep consistency between variable associations
	 */
	public List<MyClause> generateBottomClauses(GenericDAO genericDAO, List<Tuple> examples, Schema schema, DataModel dataModel, Parameters parameters) {
		List<MyClause> bottomClauses = new LinkedList<MyClause>();
		Map<String, String> hashConstantToVariable = new HashMap<String, String>();
		Map<String, String> hashVariableToConstant = new HashMap<String, String>();
		for (Tuple example : examples) {
			bottomClauses.add(this.generateBottomClauseOneQueryPerRelationAttribute(genericDAO, hashConstantToVariable,
					hashVariableToConstant, example, schema, dataModel.getModeH(), dataModel.getModesB(), parameters.getIterations(), parameters.getRecall(), parameters.isUseInds(), parameters.getMaxterms(), false));
		}
		return bottomClauses;
	}
	
	/*
	 * Generate ground bottom clause for one example
	 */
	@Override
	public MyClause generateGroundBottomClause(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, 
			Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters) {
		Map<String, String> hashConstantToVariable = new HashMap<String, String>();
		Map<String, String> hashVariableToConstant = new HashMap<String, String>();
		return this.generateBottomClauseOneQueryPerRelationAttribute(genericDAO, hashConstantToVariable,
				hashVariableToConstant, exampleTuple, schema, dataModel.getModeH(), dataModel.getModesB(), parameters.getIterations(), parameters.getGroundRecall(), parameters.isUseInds(), parameters.getMaxterms(), true);
	}
	
	/*
	 * Generate ground bottom clause for one example in string format
	 */
	@Override
	public String generateGroundBottomClauseString(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, 
			Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters) {
		MyClause clause = generateGroundBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema, dataModel, parameters);
		return clause.toString2(MyClauseToIDAClause.POSITIVE_SYMBOL, MyClauseToIDAClause.NEGATE_SYMBOL);
	}

	/*
	 * Bottom clause generation as described in original algorithm.
	 * Queries database only once per relation-input_attribute.
	 */
	private MyClause generateBottomClauseOneQueryPerRelationAttribute(GenericDAO genericDAO,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant, Tuple exampleTuple,
			Schema schema, Mode modeH, List<Mode> modesB, int iterations, int recall, boolean applyInds, int maxTerms, boolean ground) {

		// Check that arities of example and modeH match
		if (modeH.getArguments().size() != exampleTuple.getValues().size()) {
			throw new IllegalArgumentException("Example arity does not match modeH arity.");
		}
		
		MyClause clause = new MyClause();
		
		// Known terms for a data type
		Map<String, Set<String>> inTerms = new HashMap<String, Set<String>>();
		
		// Keep track of all used variables and constants in clause
		Set<String> distinctTerms = new HashSet<String>();

		// Create head literal
		varCounter = 0;
		if (ground) {
			modeH = modeH.toGroundMode();
		}
		Predicate headLiteral = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant, exampleTuple,
				modeH, inTerms, distinctTerms);
		clause.addPositiveLiteral(headLiteral);

		// Group modes by relation name - input attribute position
		Map<Pair<String, Integer>, List<Mode>> groupedModes = new LinkedHashMap<Pair<String, Integer>, List<Mode>>();
		for (Mode mode : modesB) {
			// Get name of input attribute
			int inputVarPosition = 0;
			for (int i = 0; i < mode.getArguments().size(); i++) {
				if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.INPUT)) {
					inputVarPosition = i;
					break;
				}
			}

			Pair<String, Integer> key = new Pair<String, Integer>(mode.getPredicateName(), inputVarPosition);
			if (!groupedModes.containsKey(key)) {
				groupedModes.put(key, new LinkedList<Mode>());
			}
			groupedModes.get(key).add(mode);
		}
		
		// Create body literals
		for (int j = 0; j < iterations; j++) {
			// Create new inTerms to hold terms for this iteration
			Map<String, Set<String>> newInTerms = new HashMap<String, Set<String>>();

			Iterator<Entry<Pair<String, Integer>, List<Mode>>> groupedModesIterator = groupedModes.entrySet()
					.iterator();
			while (groupedModesIterator.hasNext()) {
				Entry<Pair<String, Integer>, List<Mode>> entry = groupedModesIterator.next();
				String relationName = entry.getKey().getFirst();
				int inputVarPosition = entry.getKey().getSecond();
				List<Mode> relationAttributeModes = entry.getValue();

				// Get input attribute name
				String attributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames()
						.get(inputVarPosition);

				// Get all attribute types for input attribute
				Set<String> attributeTypes = new HashSet<String>();
				for (Mode mode : relationAttributeModes) {
					attributeTypes.add(mode.getArguments().get(inputVarPosition).getType());
				}

				// Get known terms for all attribute types
				boolean seenType = false;
				RandomSet<String> knownTermsSet = new RandomSet<String>();
				for (String type : attributeTypes) {
					if (inTerms.containsKey(type)) {
						seenType = true;
						knownTermsSet.addAll(inTerms.get(type));
					}
				}
				// If there is no list of known terms for attributeType, skip mode
				if (!seenType) {
					continue;
				}

				// Generate new literals for grouped modes
				List<Predicate> newLiterals = operationForGroupedModes(genericDAO, schema, clause,
						hashConstantToVariable, hashVariableToConstant, newInTerms, distinctTerms, relationName, attributeName,
						relationAttributeModes, groupedModes, knownTermsSet, recall, ground);

				// Apply INDs
				if (applyInds) {
					followIndChain(genericDAO, schema, clause, newLiterals, hashConstantToVariable,
							hashVariableToConstant, newInTerms, distinctTerms, groupedModes, recall, relationName,
							new HashSet<String>(), ground);
				}
				for (Predicate literal : newLiterals) {
					clause.addNegativeLiteral(literal);
				}
			}
			
			// Add new terms to inTerms
//			Iterator<Map.Entry<String, Set<String>>> newInTermsIterator = newInTerms.entrySet().iterator();
//			while (newInTermsIterator.hasNext()) {
//				Map.Entry<String, Set<String>> pair = (Map.Entry<String, Set<String>>) newInTermsIterator.next();
//				String variableType = pair.getKey();
//				if (!inTerms.containsKey(variableType)) {
//					inTerms.put(variableType, new HashSet<String>());
//				}
//				inTerms.get(variableType).addAll(newInTerms.get(variableType));
//			}
			inTerms.clear();
			inTerms.putAll(newInTerms);
			
			// Stopping condition: check number of distinct terms
		    if (distinctTerms.size() >= maxTerms) {
		    		break;
		    }
		}

		return clause;
	}
	
	/*
	 * Performs mode operation for set of modes with same relation name and input attribute.
	 * Returns a list of new literals to be added to clause.
	 */
	abstract protected List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> newInTerms, Set<String> distinctTerms, String relationName, String attributeName,
			List<Mode> relationAttributeModes, Map<Pair<String, Integer>, List<Mode>> groupedModes, RandomSet<String> knownTermsSet,
			int recall, boolean ground);
	

	/*private List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> newInTerms, Set<String> distinctTerms, String relationName, String attributeName,
			List<Mode> relationAttributeModes, Map<Pair<String, Integer>, List<Mode>> groupedModes, String knownTerms,
			int recall, boolean ground) {
		List<Predicate> newLiterals = new LinkedList<Predicate>();

		// Create query and run
		String query = String.format(SELECTIN_SQL_STATEMENT, relationName, attributeName, knownTerms);
		GenericTableObject result = genericDAO.executeQuery(query);

		if (result != null) {
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
				int solutionsCounter = 0;
				for (Tuple tuple : result.getTable()) {
					if (solutionsCounter >= recall)
						break;

					Predicate literal = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant, tuple,
							mode, newInTerms, distinctTerms);
					
					// Do not add literal if it's exactly the same as head literal
					if (!literal.equals(clause.getPositiveLiterals().get(0).getAtomicSentence())) {
						addNotRepeated(newLiterals, literal);
						solutionsCounter++;
					}
				}
			}

		}

		return newLiterals;
	}*/

	/*
	 * Creates a literal from a tuple and a mode.
	 */
	protected Predicate createLiteralFromTuple(Map<String, String> hashConstantToVariable,
			Map<String, String> hashVariableToConstant, Tuple tuple, Mode mode, Map<String, Set<String>> inTerms, Set<String> distinctTerms) {
		List<Term> terms = new ArrayList<Term>();
		for (int i = 0; i < mode.getArguments().size(); i++) {
			String value = tuple.getValues().get(i);

			if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
				terms.add(new Constant("\"" + value + "\""));
				distinctTerms.add(value);
			} else {
				// INPUT or OUTPUT type
				if (!hashConstantToVariable.containsKey(value)) {
					String var = Commons.newVariable(varCounter);
					varCounter++;

					hashConstantToVariable.put(value, var);
					hashVariableToConstant.put(var, value);
					
					distinctTerms.add(var);
				}
				terms.add(new Variable(hashConstantToVariable.get(value)));
			}
			// Add constants to inTerms
//			if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.INPUT) ||
//					mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
				String variableType = mode.getArguments().get(i).getType();
				if (!inTerms.containsKey(variableType)) {
					inTerms.put(variableType, new HashSet<String>());
				}
				inTerms.get(variableType).add(value);
//			}
		}
		
		Predicate literal = new Predicate(mode.getPredicateName(), terms);
		return literal;
	}

	/*
	 * Recursively follow inclusion dependencies chain
	 */
	private void followIndChain(GenericDAO genericDAO, Schema schema, MyClause clause,
			List<Predicate> newLiteralsForGroupedModes, Map<String, String> hashConstantToVariable,
			Map<String, String> hashVariableToConstant, Map<String, Set<String>> newInTerms, Set<String> distinctTerms,
			Map<Pair<String, Integer>, List<Mode>> groupedModes, int recall, String currentPredicate,
			Set<String> seenPredicates, boolean ground) {

		if (!seenPredicates.contains(currentPredicate)
				&& schema.getInclusionDependencies().containsKey(currentPredicate)) {
			for (InclusionDependency ind : schema.getInclusionDependencies().get(currentPredicate)) {

				if (!seenPredicates.contains(ind.getRightPredicateName())) {
					// Apply IND
					List<Predicate> literalsFromInd = applyInclusionDependency(genericDAO, schema, clause,
							newLiteralsForGroupedModes, hashConstantToVariable, hashVariableToConstant, newInTerms, distinctTerms, ind,
							groupedModes, recall, ground);
					addNotRepeated(newLiteralsForGroupedModes, literalsFromInd);

					// Add current predicate to seen list
					seenPredicates.add(currentPredicate);

					// Follow chain
					followIndChain(genericDAO, schema, clause, newLiteralsForGroupedModes, hashConstantToVariable,
							hashVariableToConstant, newInTerms, distinctTerms, groupedModes, recall, ind.getRightPredicateName(),
							seenPredicates, ground);
				}
			}
		}
	}

	/*
	 * Apply inclusion dependency to clause. Return list of new literals to be added to clause.
	 */
	private List<Predicate> applyInclusionDependency(GenericDAO genericDAO, Schema schema, MyClause clause,
			List<Predicate> newLiteralsForGroupedModes, Map<String, String> hashConstantToVariable,
			Map<String, String> hashVariableToConstant, Map<String, Set<String>> newInTerms, Set<String> distinctTerms, InclusionDependency ind,
			Map<Pair<String, Integer>, List<Mode>> groupedModes, int recall, boolean ground) {

		List<Predicate> newLiterals = new LinkedList<Predicate>();

		// For each literal with predicate equal to leftIndPredicate, check if IND holds
		for (int i = 0; i < newLiteralsForGroupedModes.size(); i++) {
			Predicate literal = newLiteralsForGroupedModes.get(i);
			String predicate = literal.getPredicateName();

			if (predicate.equals(ind.getLeftPredicateName())) {
				Term leftIndTerm = literal.getArgs().get(ind.getLeftAttributeNumber());

				// Check if IND is satisfied
				boolean indSatisfied = false;
				for (int j = 0; j < clause.getNegativeLiterals().size(); j++) {
					Predicate otherLiteral = (Predicate) clause.getNegativeLiterals().get(j).getAtomicSentence();
					String otherPredicate = otherLiteral.getPredicateName();

					if (otherPredicate.equals(ind.getRightPredicateName())) {
						Term rightIndTerm = otherLiteral.getArgs().get(ind.getRightAttributeNumber());

						if (leftIndTerm.equals(rightIndTerm)) {
							// IND satisfied
							indSatisfied = true;
							break;
						}
					}
				}

				// If IND is not satisfied, add literals that make IND satisfied
				if (!indSatisfied) {
					// Get constant value
					String value;
					if (Commons.isVariable(leftIndTerm)) {
						value = hashVariableToConstant.get(leftIndTerm.getSymbolicName());
					} else {
						// value = leftIndTerm.substring(1, leftIndTerm.length()-1);
						value = leftIndTerm.getSymbolicName();
					}
					RandomSet<String> termsSet = new RandomSet<String>();
					termsSet.add(value.replaceAll("^\"|\"$", ""));

					// Get modes for relation-attribute
					String relationName = ind.getRightPredicateName();
					String attributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames()
							.get(ind.getRightAttributeNumber());
					Pair<String, Integer> key = new Pair<String, Integer>(ind.getRightPredicateName(),
							ind.getRightAttributeNumber());
					List<Mode> relationAttributeModes = groupedModes.get(key);

					// Generate new literals
					List<Predicate> modeBLiterals = operationForGroupedModes(genericDAO, schema, clause,
							hashConstantToVariable, hashVariableToConstant, newInTerms, distinctTerms, relationName, attributeName,
							relationAttributeModes, groupedModes, termsSet, recall, ground);
					addNotRepeated(newLiterals, modeBLiterals);
				}
			}
		}
		return newLiterals;
	}

	/*
	 * Add non-repeated newLiterals to literals
	 */
	protected void addNotRepeated(List<Predicate> literals, List<Predicate> newLiterals) {
		for (Predicate newLiteral : newLiterals) {
			if (!literals.contains(newLiteral)) {
				literals.add(newLiteral);
			}
		}
	}

	/*
	 * Add non-repeated newLiteral to literals
	 */
	protected void addNotRepeated(List<Predicate> literals, Predicate newLiteral) {
		if (!literals.contains(newLiteral)) {
			literals.add(newLiteral);
		}
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
	
	/*
	 * DOES NOT HANDLE INDS (CANNOT BE USED FOR SCHEMA INDEPENDENCE)
	 */
	//TODO INCOMPLETE
	private MyClause generateBottomClauseOneQueryPerRelation(GenericDAO genericDAO,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant, Tuple exampleTuple,
			Schema schema, Mode modeH, List<Mode> modesB, int iterations, int recall, boolean applyInds, int maxTerms, boolean ground) {

		// Check that arities of example and modeH match
		if (modeH.getArguments().size() != exampleTuple.getValues().size()) {
			throw new IllegalArgumentException("Example arity does not match modeH arity.");
		}
		
		MyClause clause = new MyClause();
		
		// Known terms for a data type
		Map<String, Set<String>> inTerms = new HashMap<String, Set<String>>();
		
		// Keep track of all used variables and constants in clause
		Set<String> distinctTerms = new HashSet<String>();

		// Create head literal
		varCounter = 0;
		if (ground) {
			modeH = modeH.toGroundMode();
		}
		Predicate headLiteral = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant, exampleTuple,
				modeH, inTerms, distinctTerms);
		clause.addPositiveLiteral(headLiteral);

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
			// Group the WHERE expressions of each mode into relations
			Map<String, List<String>> relationsWhereExpressions = new HashMap<String, List<String>>();
			Set<Pair<String,Integer>> seenModesInputAttribute = new HashSet<Pair<String,Integer>>();
			
			for (Mode mode : modesB) {
				int inputVarPosition = 0;
				for (int i = 0; i < mode.getArguments().size(); i++) {
					if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.INPUT)) {
						inputVarPosition = i;
						break;
					}
				}
				
				Pair<String,Integer> relationInputAttribute = new Pair<String,Integer>(mode.getPredicateName(), inputVarPosition);
				if (!seenModesInputAttribute.contains(relationInputAttribute)) {
					String expression = computeWhereExpression(mode, schema, inTerms);
					if (!expression.isEmpty()) {
						if (!relationsWhereExpressions.containsKey(mode.getPredicateName())) {
							relationsWhereExpressions.put(mode.getPredicateName(), new LinkedList<String>());
						}
						relationsWhereExpressions.get(mode.getPredicateName()).add(expression);
					}
					
					seenModesInputAttribute.add(relationInputAttribute);
				}
			}
			
			// Create new inTerms to hold terms for this iteration
			Map<String, Set<String>> newInTerms = new HashMap<String, Set<String>>();
			
			Iterator<Entry<String, List<String>>> expressionsIterator = relationsWhereExpressions.entrySet().iterator();
			while (expressionsIterator.hasNext()) {
				Map.Entry<String, List<String>> pair = (Map.Entry<String, List<String>>) expressionsIterator.next();
				String relation = pair.getKey();
				List<String> expressions = pair.getValue();

				// Create query
				String query = "SELECT * FROM " + relation + " WHERE ";
				for (int i = 0; i < expressions.size(); i++) {
					query += expressions.get(i);
					if (i < expressions.size() - 1) {
						query += " OR ";
					}
				}
				query += ";";

				// Run query
				GenericTableObject result = genericDAO.executeQuery(query);

				if (result != null) {
					List<Predicate> newLiterals = new LinkedList<Predicate>();
					
					Set<String> usedModes = new HashSet<String>();
					for (Mode mode : groupedModes.get(relation)) {
						if (ground) {
							if (usedModes.contains(mode.toGroundModeString())) {
								continue;
							}
							else {
								mode = mode.toGroundMode();
								usedModes.add(mode.toGroundModeString());
							}
						}
						int solutionsCounter = 0;
						for (Tuple tuple : result.getTable()) {
							if (solutionsCounter >= recall)
								break;

							Predicate literal = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant, tuple,
									mode, newInTerms, distinctTerms);
							
							// Do not add literal if it's exactly the same as head literal
							if (!literal.equals(clause.getPositiveLiterals().get(0).getAtomicSentence())) {
								addNotRepeated(newLiterals, literal);
								solutionsCounter++;
							}
						}
					}
					
					// Apply INDs
//					if (applyInds) {
//						followIndChain(genericDAO, schema, clause, newLiterals, hashConstantToVariable,
//								hashVariableToConstant, newInTerms, distinctTerms, groupedModes, recall, relation,
//								new HashSet<String>(), ground);
//					}
					for (Predicate literal : newLiterals) {
						clause.addNegativeLiteral(literal);
					}
				}
			}
			
			inTerms.clear();
			inTerms.putAll(newInTerms);
			
			// Stopping condition: check number of distinct terms
		    if (distinctTerms.size() >= maxTerms) {
		    		break;
		    }
		}

		return clause;
	}
	
	/*
	 * Compute the WHERE expression to be used in an SQL query
	 */
	private String computeWhereExpression(Mode mode, Schema schema, Map<String, Set<String>> inTerms) {
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
			expression += attributeName + " IN " + knownTerms;
		}
		return expression;
	}

	/*
	 * Bottom clause generation as described in original algorith.
	 * Assumes that exampleTuple is for relation with same name as modeH.
	 * Makes a DB call for each mode, instead of grouping them
	 */
	private MyClause generateBottomClause(GenericDAO genericDAO, Map<String, String> hashConstantToVariable,
			Map<String, String> hashVariableToConstant, Tuple exampleTuple, Schema schema, Mode modeH,
			List<Mode> modesB, int iterations, int recall) {

		// Check that arities of example and modeH match
		if (modeH.getArguments().size() != exampleTuple.getValues().size()) {
			throw new IllegalArgumentException("Example arity does not match modeH arity.");
		}
		
		MyClause clause = new MyClause();
		
		// Known terms for a data type
		Map<String, Set<String>> inTerms = new HashMap<String, Set<String>>();
		
		// Keep track of all used variables and constants in clause
		Set<String> distinctTerms = new HashSet<String>();

		// Create head literal
		varCounter = 0;
		Predicate headLiteral = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant, exampleTuple,
				modeH, inTerms, distinctTerms);
		clause.addPositiveLiteral(headLiteral);

		// Create body literals
		for (int j = 0; j < iterations; j++) {

			// Create new inTerms to hold terms for this iteration
			Map<String, Set<String>> newInTerms = new HashMap<String, Set<String>>();

			for (Mode mode : modesB) {
				// Find position of input variable
				int inputVarPosition = 0;
				for (int i = 0; i < mode.getArguments().size(); i++) {
					if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.INPUT)) {
						inputVarPosition = i;
						break;
					}
				}

				String attributeName = schema.getRelations().get(mode.getPredicateName().toUpperCase())
						.getAttributeNames().get(inputVarPosition);
				String attributeType = mode.getArguments().get(inputVarPosition).getType();

				// If there is no list of known terms for attributeType, skip mode
				if (!inTerms.containsKey(attributeType)) {
					continue;
				}
				String knownTerms = toListString(inTerms.get(attributeType));

				// Create query and run
				String query = String.format(SELECTIN_SQL_STATEMENT, mode.getPredicateName(), attributeName,
						knownTerms);
				GenericTableObject result = genericDAO.executeQuery(query);

				if (result != null) {
					int solutionsCounter = 0;
					for (Tuple tuple : result.getTable()) {

						if (solutionsCounter >= recall)
							break;

						Predicate literal = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant,
								tuple, mode, newInTerms, distinctTerms);
						clause.addNegativeLiteral(literal);
						solutionsCounter++;
					}
				}
			}

			// Add new terms to inTerms
			Iterator<Map.Entry<String, Set<String>>> newInTermsIterator = newInTerms.entrySet().iterator();
			while (newInTermsIterator.hasNext()) {
				Map.Entry<String, Set<String>> pair = (Map.Entry<String, Set<String>>) newInTermsIterator.next();
				String variableType = pair.getKey();
				if (!inTerms.containsKey(variableType)) {
					inTerms.put(variableType, new HashSet<String>());
				}
				inTerms.get(variableType).addAll(newInTerms.get(variableType));
			}
		}

		return clause;
	}
}
