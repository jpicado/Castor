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
import aima.core.util.datastructure.Pair;
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
import castor.utils.Commons;
import castor.utils.RandomSet;

public abstract class BottomClauseGeneratorNaiveSamplingWithSimilarity implements BottomClauseGenerator {

	protected static final String SELECTIN_SQL_STATEMENT = "SELECT * FROM %s WHERE %s IN %s";

	protected int varCounter;
	private boolean sample;
	private Random randomGenerator;
	private Map<Pair<String,Integer>, List<MatchingDependency>> mds;

	public BottomClauseGeneratorNaiveSamplingWithSimilarity(boolean sample, int seed, Map<Pair<String,Integer>, List<MatchingDependency>> mds) {
		varCounter = 0;
		this.sample = sample;
		this.randomGenerator = new Random(seed);
		this.mds = mds;
		
		//TODO create indexes for relation.attributes in right sides of mds
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
				hashVariableToConstant, exampleTuple, schema, dataModel.getModeH(), dataModel.getModesB(), parameters.getIterations(), parameters.getRecall(), parameters.isUseInds(), parameters.getMaxterms(), false, parameters.isRandomizeRecall());
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
					hashVariableToConstant, example, schema, dataModel.getModeH(), dataModel.getModesB(), parameters.getIterations(), parameters.getRecall(), parameters.isUseInds(), parameters.getMaxterms(), false, parameters.isRandomizeRecall()));
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
				hashVariableToConstant, exampleTuple, schema, dataModel.getModeH(), dataModel.getModesB(), parameters.getIterations(), parameters.getGroundRecall(), parameters.isUseInds(), parameters.getMaxterms(), true, parameters.isRandomizeRecall());
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
			Schema schema, Mode modeH, List<Mode> modesB, int iterations, int recall, boolean applyInds, int maxTerms, boolean ground, boolean shuffleTuples) {

		// Check that arities of example and modeH match
		if (modeH.getArguments().size() != exampleTuple.getValues().size()) {
			throw new IllegalArgumentException("Example arity does not match modeH arity.");
		}
		
		MyClause clause = new MyClause();
		
		// Known terms for a data type
		Map<String, Set<String>> inTerms = new HashMap<String, Set<String>>();
		Map<Pair<String,Integer>, Set<String>> inTermsForMDs = new HashMap<Pair<String,Integer>, Set<String>>();

		// Create head literal
		varCounter = 0;
		if (ground) {
			modeH = modeH.toGroundMode();
		}
		Predicate headLiteral = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant, exampleTuple,
				modeH, true, inTerms, inTermsForMDs, mds);
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
			Map<Pair<String,Integer>, Set<String>> newInTermsForMDs = new HashMap<Pair<String,Integer>, Set<String>>();

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
						hashConstantToVariable, hashVariableToConstant, inTerms, newInTerms, 
						inTermsForMDs, newInTermsForMDs, mds,
						relationName, attributeName, inputVarPosition,
						relationAttributeModes, groupedModes, recall, ground, shuffleTuples);

				for (Predicate literal : newLiterals) {
					clause.addNegativeLiteral(literal);
				}
			}
			
			// Add new terms to inTerms
			inTerms.clear();
			inTerms.putAll(newInTerms);
		}

		return clause;
	}
	
	/*
	 * Performs mode operation for set of modes with same relation name and input attribute.
	 * Returns a list of new literals to be added to clause.
	 */
	public List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> inTerms, Map<String, Set<String>> newInTerms,
			Map<Pair<String,Integer>, Set<String>> inTermsForMDs, Map<Pair<String,Integer>, Set<String>> newInTermsForMDs, 
			Map<Pair<String,Integer>, List<MatchingDependency>> mds,
			String relationName, String attributeName, int inputAttributePosition,
			List<Mode> relationAttributeModes, Map<Pair<String, Integer>, List<Mode>> groupedModes,
			int recall, boolean ground, boolean randomizeRecall) {
		List<Predicate> newLiterals = new LinkedList<Predicate>();
		
		// If sampling is turned off, set recall to max value
		if (!this.sample) {
			recall = Integer.MAX_VALUE;
		}
		
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
			String knownTerms = toListString(knownTermsSet);
	
			// Create query and run
			String query = String.format(SELECTIN_SQL_STATEMENT, relationName, attributeName, knownTerms);
			GenericTableObject result = genericDAO.executeQuery(query);
			
			if (result != null) {
				if (!randomizeRecall || result.getTable().size() <= recall) {
					// Get first tuples from result
					int solutionsCounter = 0;
					for (Tuple tuple : result.getTable()) {
						if (solutionsCounter >= recall)
							break;
						
						modeOperationsForTuple(tuple, newLiterals, clause, hashConstantToVariable, hashVariableToConstant, newInTerms, newInTermsForMDs, mds, 
								relationAttributeModes, ground);
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
							
							modeOperationsForTuple(tuple, newLiterals, clause, hashConstantToVariable, hashVariableToConstant, newInTerms, newInTermsForMDs, mds, 
									relationAttributeModes, ground);
							solutionsCounter++;
						}
					}
				}
			}
		}
		
		Pair<String,Integer> key = new Pair<String,Integer>(relationName,inputAttributePosition);
		if (inTermsForMDs.containsKey(key)) {
			//TODO
			// for each known value
			// 		R = similarity search on relationName.attributeName, looking for values in inTermsForMDs[<md.getRightRelation,md.getRightAttribute>]
			//		for each tuple in R
			// 			createLiteralFromTupleWithSimilarity(tuple,value,inputAttributePosition)
			//			//createLiteralFromTupleWithSimilarity should create literal for tuple and literal for similarity sim(value,tuple[inputAttributePosition])
			//			//should also save values as in createLiteralFromTuple
			//		
			

			// On createLiteral and createLiteralSim when saving values
			// If matchingDependencies.containsLeft(<relationName,attributeName>)
			// 		inTermsForMDs.put(<md.getRightRelation,md.getRightAttribute>, new values)
			////
		}
		
		

		
		
		return newLiterals;
	}
	
	private void modeOperationsForTuple(Tuple tuple, List<Predicate> newLiterals, MyClause clause, 
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> newInTerms,
			Map<Pair<String,Integer>, Set<String>> newInTermsForMDs,
			Map<Pair<String,Integer>, List<MatchingDependency>> mds,
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
					mode, false, newInTerms, newInTermsForMDs, mds);
			
			// Do not add literal if it's exactly the same as head literal
			if (!literal.equals(clause.getPositiveLiterals().get(0).getAtomicSentence())) {
				addNotRepeated(newLiterals, literal);
			}
		}
	}

	/*
	 * Creates a literal from a tuple and a mode.
	 */
	protected Predicate createLiteralFromTuple(Map<String, String> hashConstantToVariable,
			Map<String, String> hashVariableToConstant, Tuple tuple, Mode mode, boolean headMode, 
			Map<String, Set<String>> inTerms, Map<Pair<String,Integer>, Set<String>> inTermsForMDs,
			Map<Pair<String,Integer>, List<MatchingDependency>> mds) {
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
			if (headMode ||
					mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.OUTPUT) ||
					mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
				String variableType = mode.getArguments().get(i).getType();
				if (!inTerms.containsKey(variableType)) {
					inTerms.put(variableType, new HashSet<String>());
				}
				inTerms.get(variableType).add(value);
				
				// Add constants that can be used in similarity search
				Pair<String,Integer> leftKey = new Pair<String,Integer>(mode.getPredicateName(),i);
				if (mds.containsKey(leftKey)) {
					for (MatchingDependency md : mds.get(leftKey)) {
						Pair<String,Integer> rightKey = new Pair<String,Integer>(md.getRightPredicateName(),md.getRightAttributeNumber());
						if (!inTermsForMDs.containsKey(rightKey)) {
							inTermsForMDs.put(rightKey, new HashSet<String>());
						}
						inTermsForMDs.get(rightKey).add(value);
					}
				}
			}
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
}
