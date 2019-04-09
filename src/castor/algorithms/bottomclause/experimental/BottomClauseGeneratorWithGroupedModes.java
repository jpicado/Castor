/*
 * Bottom clause generation as described in original algorithm, except that it only does one query to the DB per relation (even for different input attributes).
 * DOES NOT HANDLE INDS (CANNOT BE USED FOR SCHEMA INDEPENDENCE).
 */
package castor.algorithms.bottomclause.experimental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Set;

import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import castor.algorithms.bottomclause.BottomClauseGenerator;
import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.MyClause;
import castor.language.IdentifierType;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.mappings.MyClauseToIDAClause;
import castor.settings.DataModel;
import castor.settings.Parameters;
import castor.utils.Commons;

public abstract class BottomClauseGeneratorWithGroupedModes implements BottomClauseGenerator {

	private static final String NULL_PREFIX = "null";
	
	protected static final String SELECTIN_SQL_STATEMENT = "SELECT * FROM %s WHERE %s IN %s";

	protected int varCounter;
	private int nullCounter;
	protected int seed;

	public BottomClauseGeneratorWithGroupedModes(int seed) {
		varCounter = 0;
		nullCounter = 0;
		this.seed = seed;
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
				hashVariableToConstant, exampleTuple, schema, dataModel.getModeH(), dataModel.getModesB(), parameters.getIterations(), parameters.getRecall(), parameters.isUseInds(), parameters.getMaxterms(), false, parameters.isRandomizeRecall(), parameters.getQueryLimit());
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
			bottomClauses.add(this.generateBottomClauseOneQueryPerRelation(genericDAO, hashConstantToVariable,
					hashVariableToConstant, example, schema, dataModel.getModeH(), dataModel.getModesB(), parameters.getIterations(), parameters.getRecall(), parameters.isUseInds(), parameters.getMaxterms(), false, parameters.isRandomizeRecall(), parameters.getQueryLimit()));
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
		return this.generateBottomClauseOneQueryPerRelation(genericDAO, hashConstantToVariable,
				hashVariableToConstant, exampleTuple, schema, dataModel.getModeH(), dataModel.getModesB(), parameters.getIterations(), parameters.getGroundRecall(), parameters.isUseInds(), parameters.getMaxterms(), true, parameters.isRandomizeRecall(), parameters.getQueryLimit());
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
	 * Bottom clause generation as described in original algorithm, except that it only does one query to the DB per relation (even for different input attributes).
	 * DOES NOT HANDLE INDS (CANNOT BE USED FOR SCHEMA INDEPENDENCE)
	 */
	private MyClause generateBottomClauseOneQueryPerRelation(GenericDAO genericDAO,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant, Tuple exampleTuple,
			Schema schema, Mode modeH, List<Mode> modesB, int iterations, int recall, boolean applyInds, int maxTerms, boolean ground, boolean shuffleTuples, int queryLimit) {

		// Check that arities of example and modeH match
		if (modeH.getArguments().size() != exampleTuple.getValues().size()) {
			throw new IllegalArgumentException("Example arity does not match modeH arity.");
		}
		
		MyClause clause = new MyClause();
		
		Random randomGenerator = new Random(seed);
		
		// Known terms for a data type
		Map<String, Set<String>> inTerms = new HashMap<String, Set<String>>();
		Map<String, Set<String>> previousIterationsInTerms = new HashMap<String, Set<String>>();
		
		// Keep track of all used variables and constants in clause
		Set<String> distinctTerms = new HashSet<String>();

		// Create head literal
		varCounter = 0;
		if (ground) {
			modeH = modeH.toGroundMode();
		}
		Predicate headLiteral = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant, exampleTuple,
				modeH, true, inTerms, previousIterationsInTerms, distinctTerms);
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
			// Create new inTerms to hold terms for this iteration
			Map<String, Set<String>> newInTerms = new HashMap<String, Set<String>>();
			
			Iterator<Entry<String, List<Mode>>> groupedModesIterator = groupedModes.entrySet().iterator();
			while(groupedModesIterator.hasNext()) {
				Map.Entry<String, List<Mode>> pair = (Map.Entry<String, List<Mode>>) groupedModesIterator.next();
				String relation = pair.getKey();
				List<Mode> relationModes = pair.getValue();
				List<Predicate> newLiterals = this.operationForGroupedModes(genericDAO, schema, clause, hashConstantToVariable, hashVariableToConstant, inTerms, newInTerms, previousIterationsInTerms, distinctTerms, relation, relationModes, recall, ground, shuffleTuples, queryLimit, randomGenerator);
				
				// Apply INDs
//				if (applyInds) {
//					followIndChain(genericDAO, schema, clause, newLiterals, hashConstantToVariable,
//							hashVariableToConstant, newInTerms, distinctTerms, groupedModes, recall, relation,
//							new HashSet<String>(), ground);
//				}
				for (Predicate literal : newLiterals) {
					clause.addNegativeLiteral(literal);
				}
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

		return clause;
	}
	
	/*
	 * Performs mode operation for set of modes with same relation name and input attribute.
	 * Returns a list of new literals to be added to clause.
	 */
	abstract protected List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> inTerms, Map<String, Set<String>> newInTerms, Map<String, Set<String>> previousIterationsInTerms,
			Set<String> distinctTerms,
			String relationName, List<Mode> relationModes, int recall, boolean ground, boolean shuffleTuples, int queryLimit, Random randomGenerator);

	/*
	 * Creates a literal from a tuple and a mode.
	 */
	protected Predicate createLiteralFromTuple(Map<String, String> hashConstantToVariable,
			Map<String, String> hashVariableToConstant, Tuple tuple, Mode mode, boolean headMode, 
			Map<String, Set<String>> inTerms, Map<String, Set<String>> previousIterationsInTerms, Set<String> distinctTerms) {
		List<Term> terms = new ArrayList<Term>();
		for (int i = 0; i < mode.getArguments().size(); i++) {
			//TODO default value for nulls? distinct value?
			String value;
			if (tuple.getValues().get(i) != null) {
				value = tuple.getValues().get(i).toString();
			}
			else {
				value = NULL_PREFIX+nullCounter;
				nullCounter++;
			}

			if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
				terms.add(new Constant("\"" + Commons.escapeMetaCharacters(value) + "\""));
				distinctTerms.add(value);
			} else {
				// INPUT or OUTPUT type
				String valueWithSuffix = value + "_" + mode.getArguments().get(i).getType();
				if (!hashConstantToVariable.containsKey(valueWithSuffix)) {
					String var = Commons.newVariable(varCounter);
					varCounter++;

					hashConstantToVariable.put(valueWithSuffix, var);
					hashVariableToConstant.put(var, value);
					
					distinctTerms.add(var);
				}
				terms.add(new Variable(hashConstantToVariable.get(valueWithSuffix)));
			}
			// Add constants to inTerms
//			if (headMode ||
//					mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.OUTPUT) ||
//					mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
				String variableType = mode.getArguments().get(i).getType();
				
				if (!previousIterationsInTerms.containsKey(variableType) ||
						(previousIterationsInTerms.containsKey(variableType) && !previousIterationsInTerms.get(variableType).contains(value))) {
					if (!inTerms.containsKey(variableType)) {
						inTerms.put(variableType, new HashSet<String>());
					}
					inTerms.get(variableType).add(value);
				}
//			}
		}
		
		Predicate literal = new Predicate(mode.getPredicateName(), terms);
		return literal;
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
	 * Compute expression to be used in an SQL query
	 */
	protected String computeExpression(Mode mode, Schema schema, Map<String, Set<String>> inTerms) {
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
}
