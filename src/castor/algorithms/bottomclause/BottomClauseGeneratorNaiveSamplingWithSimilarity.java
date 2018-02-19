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
import java.util.Random;
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
import castor.language.MatchingDependency;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.mappings.MyClauseToIDAClause;
import castor.settings.DataModel;
import castor.settings.Parameters;
import castor.similarity.HSTree;
import castor.similarity.HSTreeCreator;
import castor.similarity.SimilarityUtils;
import castor.utils.Commons;
import castor.utils.Pair;
import castor.utils.RandomSet;

public class BottomClauseGeneratorNaiveSamplingWithSimilarity implements BottomClauseGenerator {

	private static final String SELECTIN_SQL_STATEMENT = "SELECT * FROM %s WHERE %s IN %s";
	private static final String PROJET_SELECT_SQL_STATEMENT = "SELECT DISTINCT(%s) FROM %s";

	private int varCounter;
	private boolean sample;
	private Random randomGenerator;
	private Map<Pair<String, Integer>, List<MatchingDependency>> mds;
	private Map<Pair<String, Integer>, HSTree> hsTrees;

	public BottomClauseGeneratorNaiveSamplingWithSimilarity(GenericDAO genericDAO, Schema schema, boolean sample, int seed) {
		this.varCounter = 0;
		this.sample = sample;
		this.randomGenerator = new Random(seed);
		this.mds = schema.getMatchingDependencies();
		this.hsTrees = new HashMap<Pair<String, Integer>, HSTree>();
		createIndexes(genericDAO, schema);
	}

	/*
	 * Create an HSTree for each relation-attribute pair in right side of all
	 * matching dependencies
	 */
	private void createIndexes(GenericDAO genericDAO, Schema schema) {
		for (List<MatchingDependency> searchAttributeMDs : schema.getMatchingDependencies().values()) {
			for (MatchingDependency md : searchAttributeMDs) {
				String relation = md.getRightPredicateName();
				String attribute = schema.getRelations().get(relation.toUpperCase()).getAttributeNames()
						.get(md.getRightAttributeNumber());
				int attributePosition = md.getRightAttributeNumber();

				Pair<String, Integer> key = new Pair<String, Integer>(relation, attributePosition);
				if (!hsTrees.containsKey(key)) {
					List<String> values = projectSelectIn(genericDAO, relation, attribute);
					HSTree hsTree = HSTreeCreator.buildHSTree(values);
					hsTrees.put(key, hsTree);
				}
			}
		}
	}

	/*
	 * Generate bottom clause for one example
	 */
	@Override
	public MyClause generateBottomClause(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO,
			Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters) {
		Map<String, String> hashConstantToVariable = new HashMap<String, String>();
		Map<String, String> hashVariableToConstant = new HashMap<String, String>();
		return this.generateBottomClauseOneQueryPerRelationAttribute(genericDAO, hashConstantToVariable,
				hashVariableToConstant, exampleTuple, schema, dataModel.getModeH(), dataModel.getModesB(),
				parameters.getIterations(), parameters.getRecall(), parameters.isUseInds(), parameters.getMaxterms(),
				false, parameters.isRandomizeRecall());
	}

	/*
	 * Generate bottom clause for each input example in examples list Reuses hash
	 * function to keep consistency between variable associations
	 */
	public List<MyClause> generateBottomClauses(GenericDAO genericDAO, List<Tuple> examples, Schema schema,
			DataModel dataModel, Parameters parameters) {
		List<MyClause> bottomClauses = new LinkedList<MyClause>();
		Map<String, String> hashConstantToVariable = new HashMap<String, String>();
		Map<String, String> hashVariableToConstant = new HashMap<String, String>();
		for (Tuple example : examples) {
			bottomClauses.add(this.generateBottomClauseOneQueryPerRelationAttribute(genericDAO, hashConstantToVariable,
					hashVariableToConstant, example, schema, dataModel.getModeH(), dataModel.getModesB(),
					parameters.getIterations(), parameters.getRecall(), parameters.isUseInds(),
					parameters.getMaxterms(), false, parameters.isRandomizeRecall()));
		}
		return bottomClauses;
	}

	/*
	 * Generate ground bottom clause for one example
	 */
	@Override
	public MyClause generateGroundBottomClause(GenericDAO genericDAO,
			BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema,
			DataModel dataModel, Parameters parameters) {
		Map<String, String> hashConstantToVariable = new HashMap<String, String>();
		Map<String, String> hashVariableToConstant = new HashMap<String, String>();
		return this.generateBottomClauseOneQueryPerRelationAttribute(genericDAO, hashConstantToVariable,
				hashVariableToConstant, exampleTuple, schema, dataModel.getModeH(), dataModel.getModesB(),
				parameters.getIterations(), parameters.getGroundRecall(), parameters.isUseInds(),
				parameters.getMaxterms(), true, parameters.isRandomizeRecall());
	}

	/*
	 * Generate ground bottom clause for one example in string format
	 */
	@Override
	public String generateGroundBottomClauseString(GenericDAO genericDAO,
			BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema,
			DataModel dataModel, Parameters parameters) {
		MyClause clause = generateGroundBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema,
				dataModel, parameters);
		return clause.toString2(MyClauseToIDAClause.POSITIVE_SYMBOL, MyClauseToIDAClause.NEGATE_SYMBOL);
	}

	/*
	 * Bottom clause generation as described in original algorithm. Queries database
	 * only once per relation-input_attribute.
	 */
	private MyClause generateBottomClauseOneQueryPerRelationAttribute(GenericDAO genericDAO,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant, Tuple exampleTuple,
			Schema schema, Mode modeH, List<Mode> modesB, int iterations, int recall, boolean applyInds, int maxTerms,
			boolean ground, boolean shuffleTuples) {

		// Check that arities of example and modeH match
		if (modeH.getArguments().size() != exampleTuple.getValues().size()) {
			throw new IllegalArgumentException("Example arity does not match modeH arity.");
		}

		MyClause clause = new MyClause();

		// Known terms for a data type
		Map<String, Set<String>> inTerms = new HashMap<String, Set<String>>();
		Map<Pair<String, Integer>, Set<String>> inTermsForMDs = new HashMap<Pair<String, Integer>, Set<String>>();
		Map<Pair<String, Integer>, Integer> maxDistanceForMDs = new HashMap<Pair<String, Integer>, Integer>();

		// Create head literal
		varCounter = 0;
		if (ground) {
			modeH = modeH.toGroundMode();
		}
		Predicate headLiteral = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant, exampleTuple,
				modeH, true, inTerms, inTermsForMDs, maxDistanceForMDs);
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
			Map<Pair<String, Integer>, Set<String>> newInTermsForMDs = new HashMap<Pair<String, Integer>, Set<String>>();

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

				// Generate new literals for grouped modes
				List<Predicate> newLiterals = operationForGroupedModes(genericDAO, schema, clause,
						hashConstantToVariable, hashVariableToConstant, inTerms, newInTerms, inTermsForMDs,
						newInTermsForMDs, maxDistanceForMDs, relationName, attributeName, inputVarPosition,
						relationAttributeModes, groupedModes, recall, ground, shuffleTuples);

				for (Predicate literal : newLiterals) {
					clause.addNegativeLiteral(literal);
				}
			}

			// Add new terms to inTerms
			inTerms.clear();
			inTerms.putAll(newInTerms);
			inTermsForMDs.clear();
			inTermsForMDs.putAll(newInTermsForMDs);
		}

		return clause;
	}

	/*
	 * Performs mode operation for set of modes with same relation name and input
	 * attribute. Returns a list of new literals to be added to clause.
	 */
	public List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> inTerms, Map<String, Set<String>> newInTerms,
			Map<Pair<String, Integer>, Set<String>> inTermsForMDs,
			Map<Pair<String, Integer>, Set<String>> newInTermsForMDs,
			Map<Pair<String, Integer>, Integer> maxDistanceForMDs, String relationName, String attributeName,
			int inputAttributePosition, List<Mode> relationAttributeModes,
			Map<Pair<String, Integer>, List<Mode>> groupedModes, int recall, boolean ground, boolean randomizeRecall) {
		List<Predicate> newLiterals = new LinkedList<Predicate>();

		// If sampling is turned off, set recall to max value
		if (!this.sample) {
			recall = Integer.MAX_VALUE;
		}

		// EXACT SEARCH

		// Get all attribute types for input attribute
		Set<String> attributeTypes = new HashSet<String>();
		for (Mode mode : relationAttributeModes) {
			attributeTypes.add(mode.getArguments().get(inputAttributePosition).getType());
		}

		// Get known terms for all attribute types
		RandomSet<String> knownTermsSet = new RandomSet<String>();
		for (String type : attributeTypes) {
			if (inTerms.containsKey(type)) {
				knownTermsSet.addAll(inTerms.get(type));
			}
		}
		// If there is no list of known terms for attributeType, skip mode
		if (!knownTermsSet.isEmpty()) {
			String knownTerms = setToString(knownTermsSet);

			// Create query and run
			String query = String.format(SELECTIN_SQL_STATEMENT, relationName, attributeName, knownTerms);
			GenericTableObject result = genericDAO.executeQuery(query);

			List<Pair<Tuple, String>> tuplesWithSourceValue = new ArrayList<Pair<Tuple, String>>();
			if (result != null) {
				for (Tuple tuple : result.getTable()) {
					tuplesWithSourceValue.add(new Pair<Tuple, String>(tuple, ""));
				}
			}
			applyModesForTuples(tuplesWithSourceValue, newLiterals, clause, hashConstantToVariable,
					hashVariableToConstant, newInTerms, newInTermsForMDs, maxDistanceForMDs, 
					relationAttributeModes, recall, ground, randomizeRecall, false, inputAttributePosition);
		}

		// SIMILARITY SEARCH
		Pair<String, Integer> key = new Pair<String, Integer>(relationName, inputAttributePosition);
		if (inTermsForMDs.containsKey(key) && hsTrees.containsKey(key)) {
			List<Pair<Tuple, String>> tuplesWithSourceValue = new ArrayList<Pair<Tuple, String>>();
			for (String value : inTermsForMDs.get(key)) {
				Set<String> similarValues = hsTrees.get(key).hsSearch(value, maxDistanceForMDs.get(key));
				
				if (similarValues.isEmpty())
					continue;
				
				String knownTerms = setToString(similarValues);

				// Create query and run
				String query = String.format(SELECTIN_SQL_STATEMENT, relationName, attributeName, knownTerms);
				GenericTableObject result = genericDAO.executeQuery(query);
				if (result != null) {
					for (Tuple tuple : result.getTable()) {
						tuplesWithSourceValue.add(new Pair<Tuple, String>(tuple, value));
					}
				}
			}
			applyModesForTuples(tuplesWithSourceValue, newLiterals, clause, hashConstantToVariable,
					hashVariableToConstant, newInTerms, newInTermsForMDs, maxDistanceForMDs, 
					relationAttributeModes, recall, ground, randomizeRecall, true, inputAttributePosition);
		}

		return newLiterals;
	}

	private void applyModesForTuples(List<Pair<Tuple, String>> tuplesWithSourceValue, List<Predicate> newLiterals,
			MyClause clause, Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> newInTerms, Map<Pair<String, Integer>, Set<String>> newInTermsForMDs,
			Map<Pair<String, Integer>, Integer> maxDistanceForMDs, List<Mode> relationAttributeModes, int recall,
			boolean ground, boolean randomizeRecall, boolean createSimilarityPredicate,
			int similarAttributeInTuplePosition) {
		// Create literals from candidateTuples
		if (!randomizeRecall || tuplesWithSourceValue.size() <= recall) {
			// Get first tuples from result
			int solutionsCounter = 0;
			for (Pair<Tuple, String> tupleValuePair : tuplesWithSourceValue) {
				if (solutionsCounter >= recall)
					break;

				Tuple tuple = tupleValuePair.getFirst();
				String sourceValue = tupleValuePair.getSecond();

				modeOperationsForTuple(tuple, newLiterals, clause, hashConstantToVariable, hashVariableToConstant,
						newInTerms, newInTermsForMDs, maxDistanceForMDs, relationAttributeModes, ground,
						createSimilarityPredicate, sourceValue, similarAttributeInTuplePosition);
				solutionsCounter++;
			}
		} else {
			// Get random tuples from result (without replacement)
			Set<Integer> usedIndexes = new HashSet<Integer>();
			int solutionsCounter = 0;
			while (solutionsCounter < recall) {
				int randomIndex = randomGenerator.nextInt(tuplesWithSourceValue.size());
				if (!usedIndexes.contains(randomIndex)) {
					Pair<Tuple, String> tupleValuePair = tuplesWithSourceValue.get(randomIndex);
					Tuple tuple = tupleValuePair.getFirst();
					String sourceValue = tupleValuePair.getSecond();
					usedIndexes.add(randomIndex);

					modeOperationsForTuple(tuple, newLiterals, clause, hashConstantToVariable, hashVariableToConstant,
							newInTerms, newInTermsForMDs, maxDistanceForMDs, relationAttributeModes, ground,
							createSimilarityPredicate, sourceValue, similarAttributeInTuplePosition);
					solutionsCounter++;
				}
			}
		}
	}

	private void modeOperationsForTuple(Tuple tuple, List<Predicate> newLiterals, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> newInTerms, Map<Pair<String, Integer>, Set<String>> newInTermsForMDs,
			Map<Pair<String, Integer>, Integer> maxDistanceForMDs,
			List<Mode> relationAttributeModes, boolean ground,
			boolean createSimilarityPredicate, String similarValue, int similarAttributeInTuplePosition) {
		Set<String> usedModes = new HashSet<String>();
		for (Mode mode : relationAttributeModes) {
			if (ground) {
				if (usedModes.contains(mode.toGroundModeString())) {
					continue;
				} else {
					mode = mode.toGroundMode();
					usedModes.add(mode.toGroundModeString());
				}
			}

			Predicate literal = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant, tuple, mode,
					false, newInTerms, newInTermsForMDs, maxDistanceForMDs);

			// Do not add literal if it's exactly the same as head literal
			if (!literal.equals(clause.getPositiveLiterals().get(0).getAtomicSentence())) {
				// Add new literal
				addNotRepeated(newLiterals, literal, hashConstantToVariable, hashVariableToConstant, mode);
				//addNotRepeated(newLiterals, literal);
				
				// Create similarity predicate
				if (createSimilarityPredicate) {
					String valueInTupleString = tuple.getValues().get(similarAttributeInTuplePosition).toString();
					Predicate similarityLiteral = createSimilarityPredicate(mode, hashConstantToVariable, similarValue, similarAttributeInTuplePosition, valueInTupleString);
					addNotRepeated(newLiterals, similarityLiteral, hashConstantToVariable, hashVariableToConstant, mode);
					//addNotRepeated(newLiterals, similarityLiteral);
				}
			}
		}
	}
	
	private Predicate createSimilarityPredicate(Mode mode, Map<String, String> hashConstantToVariable, 
			String similarValue, int similarAttributeInTuplePosition, String valueInTupleString) {
		Term similarValueTerm;
		Term valueInTupleTerm;
		
		// Create similarity predicate containing constants or variables
		if (mode.getArguments().get(similarAttributeInTuplePosition).getIdentifierType()
				.equals(IdentifierType.CONSTANT)) {
			similarValueTerm = new Constant("\"" + similarValue + "\"");
			valueInTupleTerm = new Constant("\"" + valueInTupleString + "\"");
		} else {
			similarValueTerm = new Variable(hashConstantToVariable.get(similarValue));
			valueInTupleTerm = new Variable(hashConstantToVariable.get(valueInTupleString));
		}

		// Order of terms depends on lexicographic order
		List<Term> terms = new ArrayList<Term>();
		if (similarValue.compareTo(valueInTupleString) <= 0) {
			terms.add(similarValueTerm);
			terms.add(valueInTupleTerm);
		} else {
			terms.add(valueInTupleTerm);
			terms.add(similarValueTerm);
		}
		Predicate similarityLiteral = new Predicate("sim", terms);
		return similarityLiteral;
	}

	/*
	 * Creates a literal from a tuple and a mode.
	 */
	private Predicate createLiteralFromTuple(Map<String, String> hashConstantToVariable,
			Map<String, String> hashVariableToConstant, Tuple tuple, Mode mode, boolean headMode,
			Map<String, Set<String>> inTerms, Map<Pair<String, Integer>, Set<String>> inTermsForMDs,
			Map<Pair<String, Integer>, Integer> maxDistanceForMDs) {
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
			if (headMode || mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.OUTPUT)
					|| mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
				String variableType = mode.getArguments().get(i).getType();
				if (!inTerms.containsKey(variableType)) {
					inTerms.put(variableType, new HashSet<String>());
				}
				inTerms.get(variableType).add(value);

				// Add constants that can be used in similarity search
				Pair<String, Integer> leftKey = new Pair<String, Integer>(mode.getPredicateName(), i);
				if (mds.containsKey(leftKey)) {
					for (MatchingDependency md : mds.get(leftKey)) {
						Pair<String, Integer> rightKey = new Pair<String, Integer>(md.getRightPredicateName(),
								md.getRightAttributeNumber());
						if (!inTermsForMDs.containsKey(rightKey)) {
							inTermsForMDs.put(rightKey, new HashSet<String>());
						}
						inTermsForMDs.get(rightKey).add(value);

						int maxDistance = md.getMaxDistance();
						if (maxDistanceForMDs.containsKey(rightKey)) {
							maxDistance = Math.max(maxDistanceForMDs.get(rightKey), maxDistance);
						}
						maxDistanceForMDs.put(rightKey, maxDistance);
					}
				}
			}
		}

		Predicate literal = new Predicate(mode.getPredicateName(), terms);
		return literal;
	}

	private List<String> projectSelectIn(GenericDAO genericDAO, String relationName, String projectAttributeName) {
		List<String> values = new ArrayList<String>();
		String query = String.format(PROJET_SELECT_SQL_STATEMENT, projectAttributeName, relationName);
		GenericTableObject result = genericDAO.executeQuery(query);
		if (result != null) {
			for (Tuple tuple : result.getTable()) {
				values.add(tuple.getStringValues().get(0));
			}
		}
		return values;
	}

	/*
	 * Add non-repeated newLiteral to literals
	 */
	private void addNotRepeated(List<Predicate> literals, Predicate newLiteral) {
		if (!literals.contains(newLiteral)) {
			literals.add(newLiteral);
		}
	}
	/*
	 * Add newLiteral to literals, as well as similarity literals 
	 * Join inside iteration
	 */
	private void addNotRepeated(List<Predicate> literals, Predicate newLiteral,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant, Mode mode) {
		List<Predicate> newLiterals = new ArrayList<Predicate>();
		
		// Check every attribute in newLiteral
		for (int attributePosition = 0 ; attributePosition < newLiteral.getArgs().size(); attributePosition++) {
			// Get string value of attribute in newLiteral
			String valueInNewLiteral;
			if (Commons.isVariable(newLiteral.getTerms().get(attributePosition))) {
				valueInNewLiteral = hashVariableToConstant.get(newLiteral.getTerms().get(attributePosition).getSymbolicName());
			} else {
				valueInNewLiteral = newLiteral.getTerms().get(attributePosition).getSymbolicName().replaceAll("\"(.+)\"", "$1");
			}
			
			Pair<String,Integer> key = new Pair<String,Integer>(mode.getPredicateName(),attributePosition);
			
			// Compare against all other existing literals
			for (Predicate literal : literals) {
				// For each MD where left relation is newLiteral, check if need to add similarity predicate for attributes in this literal and newLiteral
				if (mds.containsKey(key)) {
					for (MatchingDependency md : mds.get(key)) {
						if (literal.getPredicateName().equals(md.getRightPredicateName())) {
							// Get string value of attribute in literal
							String valueInLiteral;
							if (Commons.isVariable(literal.getTerms().get(md.getRightAttributeNumber()))) {
								valueInLiteral = hashVariableToConstant.get(literal.getTerms().get(md.getRightAttributeNumber()).getSymbolicName());
							} else {
								valueInLiteral = literal.getTerms().get(md.getRightAttributeNumber()).getSymbolicName().replaceAll("\"(.+)\"", "$1");;
							}
							
							// Add similarity predicate if distance is less than max distance in MD
							if (SimilarityUtils.isLessThanDistance(valueInNewLiteral, valueInLiteral, md.getMaxDistance())) {
								Predicate similarityLiteral = createSimilarityPredicate(mode, hashConstantToVariable, valueInLiteral, attributePosition, valueInNewLiteral);
								newLiterals.add(similarityLiteral);
							}
						}
					}
				}
			}
		}
		
		// Add newLiteral
		newLiterals.add(newLiteral);
		
		// Add all newLiterals to literals
		addNotRepeated(literals, newLiterals);
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
	 * Convert set to string "('item1','item2',...)"
	 */
	private String setToString(Set<String> terms) {
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
