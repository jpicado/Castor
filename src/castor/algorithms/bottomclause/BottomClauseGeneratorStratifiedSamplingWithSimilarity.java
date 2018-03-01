package castor.algorithms.bottomclause;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import castor.utils.Commons;
import castor.utils.Pair;
import castor.utils.RandomSet;

public class BottomClauseGeneratorStratifiedSamplingWithSimilarity implements BottomClauseGenerator {

	private static final String NULL_PREFIX = "null";
	
	private static final String PROJET_SELECT_SQL_STATEMENT = "SELECT DISTINCT(%s) FROM %s";
	private static final String SELECTIN_SQL_STATEMENT = "SELECT * FROM %s WHERE %s IN %s";
	private static final String SELECT_SQL_STATEMENT = "SELECT * FROM %s WHERE %s = %s";
	private static final String SELECTIN_TWOATTRIBUTES_SQL_STATEMENT = "SELECT * FROM %s WHERE %s IN %s AND %s IN %s";
	private static final String PROJET_SELECTIN_SQL_STATEMENT = "SELECT DISTINCT(%s) FROM %s WHERE %s IN %s";
	private static final String SELECT_GROUPBY_SQL_STATEMENT = "SELECT %s FROM %s WHERE %s IN %s GROUP BY %s";

	private int varCounter;
	private int nullCounter;
	private int seed;
	private Map<Pair<String, Integer>, List<MatchingDependency>> mds;
	private Map<Pair<String, Integer>, HSTree> hsTrees;

	public BottomClauseGeneratorStratifiedSamplingWithSimilarity(GenericDAO genericDAO, Schema schema, int seed) {
		this.varCounter = 0;
		this.seed = seed;
		this.mds = schema.getMatchingDependencies();
		this.hsTrees = new HashMap<Pair<String, Integer>, HSTree>();
		createIndexes(genericDAO, schema);
	}

	@Override
	public MyClause generateBottomClause(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO,
			Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters) {
		return generateBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema, dataModel,
				parameters, false);
	}

	@Override
	public MyClause generateGroundBottomClause(GenericDAO genericDAO,
			BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema,
			DataModel dataModel, Parameters parameters) {
		return generateBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema, dataModel,
				parameters, true);
	}

	@Override
	public String generateGroundBottomClauseString(GenericDAO genericDAO,
			BottomClauseConstructionDAO bottomClauseConstructionDAO, Tuple exampleTuple, Schema schema,
			DataModel dataModel, Parameters parameters) {
		MyClause clause = generateGroundBottomClause(genericDAO, bottomClauseConstructionDAO, exampleTuple, schema,
				dataModel, parameters);
		return clause.toString2(MyClauseToIDAClause.POSITIVE_SYMBOL, MyClauseToIDAClause.NEGATE_SYMBOL);
	}

	public MyClause generateBottomClause(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO,
			Tuple exampleTuple, Schema schema, DataModel dataModel, Parameters parameters, boolean ground) {
		Random randomGenerator = new Random(seed);

		Map<String, List<Mode>> groupedModesByRelation = new LinkedHashMap<String, List<Mode>>();
		Map<Pair<String, Integer>, List<Mode>> groupedModesByRelationAttribute = new LinkedHashMap<Pair<String, Integer>, List<Mode>>();
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
			
			// Group modes by relation name
			if (!groupedModesByRelation.containsKey(mode.getPredicateName())) {
				groupedModesByRelation.put(mode.getPredicateName(), new LinkedList<Mode>());
			}
			groupedModesByRelation.get(mode.getPredicateName()).add(mode);

			// Group modes by relation name - input attribute position
			Pair<String, Integer> key = new Pair<String, Integer>(mode.getPredicateName(), inputAttributePosition);
			if (!groupedModesByRelationAttribute.containsKey(key)) {
				groupedModesByRelationAttribute.put(key, new LinkedList<Mode>());
			}
			groupedModesByRelationAttribute.get(key).add(mode);

			// Group modes by input attribute type
			String inputAttributeType = mode.getArguments().get(inputAttributePosition).getType();
			if (!groupedRelationsByAttributeType.containsKey(inputAttributeType)) {
				groupedRelationsByAttributeType.put(inputAttributeType, new LinkedList<Pair<String, Integer>>());
			}
			if (!groupedRelationsByAttributeType.get(inputAttributeType).contains(key)) {
				groupedRelationsByAttributeType.get(inputAttributeType).add(key);
			}
		}

		int sampleSize;
		if (ground)
			sampleSize = parameters.getGroundRecall();
		else
			sampleSize = parameters.getRecall();

		varCounter = 0;
		nullCounter = 0;
		MyClause clause = new MyClause();
		Map<String, String> hashConstantToVariable = new HashMap<String, String>();
		Map<Pair<String, Integer>, Double> maxDistanceForMDs = new HashMap<Pair<String, Integer>, Double>();

		// Create head literal
		Mode modeH = dataModel.getModeH();
		if (ground) {
			modeH = modeH.toGroundMode();
		}
		Predicate headLiteral = createLiteralFromTuple(hashConstantToVariable, exampleTuple, modeH, true);
		clause.addPositiveLiteral(headLiteral);

		// For each attribute, get relations that have attributes with same type, and
		// call stratifiedSamplingRecursive
		for (int i = 0; i < modeH.getArguments().size(); i++) {
			String attributeType = modeH.getArguments().get(i).getType();
			List<String> values = new ArrayList<String>();
			values.add(exampleTuple.getValues().get(i).toString());

			// For each relation that have attributes with same type, call
			// stratifiedSamplingRecursive
			for (Pair<String, Integer> relationInputAttributePair : groupedRelationsByAttributeType
					.get(attributeType)) {
				String relationName = relationInputAttributePair.getFirst();
				int inputAttributePosition = relationInputAttributePair.getSecond();

				stratifiedSamplingRecursive(genericDAO, schema, groupedModesByRelation, groupedModesByRelationAttribute, groupedRelationsByAttributeType,
						hashConstantToVariable, modeH.getPredicateName(), i, relationName, inputAttributePosition, values,
						parameters.getIterations(), 1, sampleSize, ground, clause, randomGenerator,
						maxDistanceForMDs);
			}
		}

		return clause;
	}

	/*
	 * Recursive function that performs stratified sampling
	 */
	private List<Pair<Tuple, String>> stratifiedSamplingRecursive(GenericDAO genericDAO, Schema schema,
			Map<String, List<Mode>> groupedModesByRelation,
			Map<Pair<String, Integer>, List<Mode>> groupedModesByRelationAttribute,
			Map<String, List<Pair<String, Integer>>> groupedRelationsByAttributeType,
			Map<String, String> hashConstantToVariable, String sourceRelationName, int sourceInputAttributePosition, 
			String relationName, int inputAttributePosition,  
			List<String> inputAttributeValues, int iterations, int currentIteration,
			int sampleSize, boolean ground, MyClause clause, Random randomGenerator,
			Map<Pair<String, Integer>, Double> maxDistanceForMDs) {
		List<Pair<Tuple, String>> sample = new ArrayList<Pair<Tuple, String>>();

		// If do not have known values for input attribute, return empty set of tuples
		if (inputAttributeValues.isEmpty()) {
			return sample;
		}

		List<Mode> relationAttributeModes = groupedModesByRelationAttribute
				.get(new Pair<String, Integer>(relationName, inputAttributePosition));
		String inputAttributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames()
				.get(inputAttributePosition);

		// List containing input values and values similar to input values
		List<String> inputAttributeValuesAll = new ArrayList<String>();
		
		// For each value, keep the source similar value
		Map<String,String> sourceForSimilarValue = new HashMap<String,String>();
		
		// EXACT SEARCH
		// Use inputAttributeValues as is
		inputAttributeValuesAll.addAll(inputAttributeValues);
		
		// SIMILARITY SEARCH
		List<Pair<String,String>> inputAttributeValuesFromSimilarity = new ArrayList<Pair<String,String>>();
		Pair<String, Integer> keyForMDsSource = new Pair<String, Integer>(sourceRelationName, sourceInputAttributePosition);
		if (mds.containsKey(keyForMDsSource)) {
			
			boolean applySimilaritySearch = false;
			for (MatchingDependency md : mds.get(keyForMDsSource)) {
				if (md.getRightPredicateName().equals(relationName) && md.getRightAttributeNumber() == inputAttributePosition) {
					applySimilaritySearch = true;
					
					double maxDistance = md.getMaxDistance();
					if (maxDistanceForMDs.containsKey(keyForMDsSource)) {
						maxDistance = Math.max(maxDistanceForMDs.get(keyForMDsSource), maxDistance);
					}
					maxDistanceForMDs.put(keyForMDsSource, maxDistance);
				}
			}
			
			if (applySimilaritySearch) {
				Pair<String, Integer> keyForTrees = new Pair<String, Integer>(relationName, inputAttributePosition);
				if (hsTrees.containsKey(keyForTrees)) {
					for (String value : inputAttributeValues) {
						// Find similar values in relation
						int distance;
						if (maxDistanceForMDs.get(keyForMDsSource) < 1.0) {
							distance = (int)Math.ceil(maxDistanceForMDs.get(keyForMDsSource) * value.length());
						} else {
							distance = maxDistanceForMDs.get(keyForMDsSource).intValue();
						}
						Set<String> similarValues = hsTrees.get(keyForTrees).hsSearch(value, distance);
						
						if (similarValues.isEmpty())
							continue;
						
						for (String similarValue : similarValues) {
							sourceForSimilarValue.put(similarValue, value);
						}
						
						String knownTerms = collectionToString(similarValues);
						
						// Get tuples from similar values in relation
						String query = String.format(SELECTIN_SQL_STATEMENT, relationName, inputAttributeName, knownTerms);
						GenericTableObject result = genericDAO.executeQuery(query);
						if (result != null) {
							for (Tuple tuple : result.getTable()) {
								inputAttributeValuesFromSimilarity.add(new Pair<String,String>(tuple.getValues().get(inputAttributePosition).toString(), value));
								inputAttributeValuesAll.add(tuple.getValues().get(inputAttributePosition).toString());
							}
						}
					}
				}
			}
		}
		
		// Convert list of values to string
		String inputAttributeKnownTermsAll = collectionToString(inputAttributeValuesAll);
		
		// Compute strata
		List<List<Pair<Tuple, String>>> strata = computeStrata(genericDAO, schema, relationName, inputAttributeName,
				inputAttributeValues, inputAttributeValuesFromSimilarity, inputAttributeKnownTermsAll, groupedModesByRelation.get(relationName), Integer.MAX_VALUE);
		
		// Check whether last iteration
		if (iterations == currentIteration) {
			// Base case: last iteration

			// Random sample
			for (List<Pair<Tuple, String>> stratum : strata) {
				sample.addAll(randomSampleFromList(stratum, sampleSize, false, randomGenerator));
			}
		} else {
			// Recursive call
			Set<Pair<String,Integer>> relationAttributesUsed = new HashSet<Pair<String,Integer>>();
			
			int relationArity = relationAttributeModes.get(0).getArguments().size();
			for (int i = 0; i < relationArity; i++) {
				// Get values for attribute in position i of relation
				String joinAttributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames().get(i);
				Set<String> values = projectSelectIn(genericDAO, relationName, joinAttributeName, inputAttributeName, inputAttributeKnownTermsAll);

				// Get all types in modes for this attribute
				Set<String> attributeTypes = new HashSet<String>();
				for (Mode mode : relationAttributeModes) {
					attributeTypes.add(mode.getArguments().get(i).getType());
				}

				// RECURSIVE CALL BASED ON MODES
				for (String type : attributeTypes) {
					
					if (!groupedRelationsByAttributeType.containsKey(type))
						continue;
					
					for (Pair<String, Integer> relationInputAttributePair : groupedRelationsByAttributeType.get(type)) {
						String joinRelationName = relationInputAttributePair.getFirst();
						int joinAttributePosition = relationInputAttributePair.getSecond();

						List<String> localValues = new ArrayList<String>(values);
						if (relationName.equals(joinRelationName) && inputAttributePosition == joinAttributePosition) {
							localValues.removeAll(inputAttributeValuesAll);
						}
						
						List<Pair<Tuple,String>> sampleFromRecursiveCall = stratifiedSamplingRecursiveAux(genericDAO, schema, groupedModesByRelation, groupedModesByRelationAttribute, groupedRelationsByAttributeType, hashConstantToVariable, relationName, i, joinRelationName, joinAttributePosition, localValues, iterations, currentIteration, sampleSize, ground, clause, randomGenerator, maxDistanceForMDs, sourceForSimilarValue, inputAttributeName, joinAttributeName, inputAttributeKnownTermsAll);
						sample.addAll(sampleFromRecursiveCall);
						
						relationAttributesUsed.add(new Pair<String,Integer>(joinAttributeName,joinAttributePosition));
					}
				}
				
				// RECURSIVE CALL BASED ON MATCHING DEPENDENCIES
				Pair<String, Integer> keyForMDs = new Pair<String, Integer>(relationName, i);
				if (mds.containsKey(keyForMDs)) {
					for (MatchingDependency md : mds.get(keyForMDs)) {
						String joinRelationName = md.getRightPredicateName();
						int joinAttributePosition = md.getRightAttributeNumber();
						
						Pair<String, Integer> keyUsedRelationAttributes = new Pair<String, Integer>(joinRelationName, joinAttributePosition);
						if (!relationAttributesUsed.contains(keyUsedRelationAttributes)) { 
						
							List<String> localValues = new ArrayList<String>(values);
							if (relationName.equals(joinRelationName) && inputAttributePosition == joinAttributePosition) {
								localValues.removeAll(inputAttributeValuesAll);
							}
							
							List<Pair<Tuple,String>> sampleFromRecursiveCall = stratifiedSamplingRecursiveAux(genericDAO, schema, groupedModesByRelation, groupedModesByRelationAttribute, groupedRelationsByAttributeType, hashConstantToVariable, relationName, i, joinRelationName, joinAttributePosition, localValues, iterations, currentIteration, sampleSize, ground, clause, randomGenerator, maxDistanceForMDs, sourceForSimilarValue, inputAttributeName, joinAttributeName, inputAttributeKnownTermsAll);
							sample.addAll(sampleFromRecursiveCall);
							
							relationAttributesUsed.add(keyUsedRelationAttributes);
						}
					}
				}
			}

			// Check if sample includes tuples in each stratum. If not, add tuples from
			// stratum to sample.
			for (List<Pair<Tuple,String>> stratum : strata) {
				if (!isIntersectWithSource(sample, stratum)) {
					sample.addAll(randomSampleFromList(stratum, sampleSize, false, randomGenerator));
				}
			}
		}

		// Create literals for tuples in sample and add to clause
		for (Pair<Tuple,String> pair : sample) {
			Tuple tuple = pair.getFirst();
			String sourceValue = pair.getSecond();
			Set<String> usedModes = new HashSet<String>();
			for (Mode mode : groupedModesByRelation.get(relationName)) {
				if (ground) {
					if (usedModes.contains(mode.toGroundModeString())) {
						continue;
					} else {
						mode = mode.toGroundMode();
						usedModes.add(mode.toGroundModeString());
					}
				}
				
				Predicate newLiteral = createLiteralFromTuple(hashConstantToVariable, tuple, mode, false);
				clause.addNegativeLiteral(newLiteral);
				
				// Create similarity literals
				if (!sourceValue.equals("") && !sourceValue.startsWith(NULL_PREFIX)) {
					Predicate similarityLiteral = createSimilarityLiteralForTuple(mode, hashConstantToVariable, sourceValue, inputAttributePosition, tuple.getValues().get(inputAttributePosition).toString());
					clause.addNegativeLiteral(similarityLiteral);
				}
			}
		}
		return sample;
	}
	
	private List<Pair<Tuple,String>> stratifiedSamplingRecursiveAux(GenericDAO genericDAO, Schema schema,
			Map<String, List<Mode>> groupedModesByRelation,
			Map<Pair<String, Integer>, List<Mode>> groupedModesByRelationAttribute,
			Map<String, List<Pair<String, Integer>>> groupedRelationsByAttributeType,
			Map<String, String> hashConstantToVariable, String relationName, int inputAttributePosition, 
			String joinRelationName, int joinAttributePosition,
			List<String> localValues, int iterations, int currentIteration,
			int sampleSize, boolean ground, MyClause clause, Random randomGenerator,
			Map<Pair<String, Integer>, Double> maxDistanceForMDs,
			Map<String,String> sourceForSimilarValue, String inputAttributeName, String joinAttributeName,
			String inputAttributeKnownTermsAll) {
		List<Pair<Tuple,String>> sample = new ArrayList<Pair<Tuple,String>>();
		
		List<Pair<Tuple,String>> returnedTuples = stratifiedSamplingRecursive(genericDAO, schema, groupedModesByRelation, groupedModesByRelationAttribute,
				groupedRelationsByAttributeType, hashConstantToVariable,
				relationName, inputAttributePosition, joinRelationName, joinAttributePosition, 
				localValues, iterations,
				currentIteration + 1, sampleSize, ground, clause, randomGenerator,
				maxDistanceForMDs);

		// Get tuples in relation that join with returnTuples
		List<Pair<String,String>> joinAttributeValuesWithSource = projectFromTuples(returnedTuples, joinAttributePosition);
		if (joinAttributeValuesWithSource.size() > 0) {
			List<String> joinAttributeValuesFromExact = new ArrayList<String>();

			for (Pair<String,String> pair : joinAttributeValuesWithSource) {
				String value = pair.getFirst();
				String source = pair.getSecond();
				
				if (source.equals("")) {
					joinAttributeValuesFromExact.add(value);
				} else {
					joinAttributeValuesFromExact.add(source);
				}
			}
			
			String joinAttributeKnownTerms = collectionToString(joinAttributeValuesFromExact);
			String joinQuery = String.format(SELECTIN_TWOATTRIBUTES_SQL_STATEMENT, relationName,
					inputAttributeName, inputAttributeKnownTermsAll, joinAttributeName,
					joinAttributeKnownTerms);
			GenericTableObject result = genericDAO.executeQuery(joinQuery);
			if (result != null) {
				for (Tuple tuple : result.getTable()) {
					String valueInTuple = tuple.getValues().get(inputAttributePosition).toString();
					String source = "";
					if (sourceForSimilarValue.containsKey(valueInTuple)) {
						source = sourceForSimilarValue.get(valueInTuple);
					}
					sample.add(new Pair<Tuple,String>(tuple,source));
				}
			}
		}
		
		return sample;
	}

	/*
	 * Check if there is intersection between two lists
	 */
	private boolean isIntersectWithSource(List<Pair<Tuple,String>> list1, List<Pair<Tuple,String>> list2) {
		boolean intersect = false;
		
		for (Pair<Tuple,String> tuple : list1) {
			if (list2.contains(tuple)) {
				intersect = true;
				break;
			}
		}
		return intersect;
	}

	/*
	 * Get a random sample from list, without replacement.
	 */
	private List<Pair<Tuple, String>> randomSampleFromList(List<Pair<Tuple, String>> list, int sampleSize, boolean withReplacement,
			Random randomGenerator) {
		List<Pair<Tuple, String>> sample = new ArrayList<Pair<Tuple, String>>();

		if (list.size() <= sampleSize) {
			sample.addAll(list);
		} else {
			Set<Integer> usedIndexes = new HashSet<Integer>();
			int resultsCounter = 0;
			while (resultsCounter < sampleSize) {
				int randomIndex = randomGenerator.nextInt(list.size());
				if (withReplacement || !usedIndexes.contains(randomIndex)) {
					sample.add(list.get(randomIndex));
					resultsCounter++;
					usedIndexes.add(randomIndex);
				}
			}
		}

		return sample;
	}

	/*
	 * Run project-select-in query. Return values in projected column (first
	 * column).
	 */
	private Set<String> projectSelectIn(GenericDAO genericDAO, String relationName, String projectAttributeName,
			String inputAttributeName, String inputAttributeKnownTerms) {
		Set<String> values = new HashSet<String>();
		String query = String.format(PROJET_SELECTIN_SQL_STATEMENT, projectAttributeName, relationName,
				inputAttributeName, inputAttributeKnownTerms);
		GenericTableObject result = genericDAO.executeQuery(query);
		if (result != null) {
			for (Tuple tuple : result.getTable()) {
				values.add(tuple.getStringValues().get(0));
			}
		}
		return values;
	}

	/*
	 * Project a column from list of tuples.
	 */
	private List<Pair<String,String>> projectFromTuples(List<Pair<Tuple,String>> tuplesWithSource, int projectPosition) {
		List<Pair<String,String>> valuesWithSource = new ArrayList<Pair<String,String>>();
		for (Pair<Tuple,String> pair : tuplesWithSource) {
			String value = pair.getFirst().getStringValues().get(projectPosition);
			String source = pair.getSecond();
			valuesWithSource.add(new Pair<String,String>(value,source));
		}
		return valuesWithSource;
	}

	/*
	 * Creates a literal from a tuple and a mode.
	 */
	protected Predicate createLiteralFromTuple(Map<String, String> hashConstantToVariable, Tuple tuple, Mode mode,
			boolean headMode) {
		List<Term> terms = new ArrayList<Term>();
		for (int i = 0; i < mode.getArguments().size(); i++) {
			String value;
			if (tuple.getValues().get(i) != null) {
				value = tuple.getValues().get(i).toString();
			}
			else {
				value = NULL_PREFIX+nullCounter;
				nullCounter++;
			}

			if (mode.getArguments().get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
				terms.add(new Constant("\"" + value + "\""));
			} else {
				// INPUT or OUTPUT type
				if (!hashConstantToVariable.containsKey(value)) {
					String var = Commons.newVariable(varCounter);
					varCounter++;

					hashConstantToVariable.put(value, var);
				}
				terms.add(new Variable(hashConstantToVariable.get(value)));
			}
		}

		Predicate literal = new Predicate(mode.getPredicateName(), terms);
		return literal;
	}
	
	private Predicate createSimilarityLiteralForTuple(Mode mode, Map<String, String> hashConstantToVariable, 
			String sourceValue, int similarAttributeInTuplePosition, String valueInTupleString) {
		Term similarValueTerm;
		Term valueInTupleTerm;
		
		// Create similarity predicate containing constants or variables
		if (mode.getArguments().get(similarAttributeInTuplePosition).getIdentifierType()
				.equals(IdentifierType.CONSTANT)) {
			similarValueTerm = new Constant("\"" + sourceValue + "\"");
			valueInTupleTerm = new Constant("\"" + valueInTupleString + "\"");
		} else {
			if (!hashConstantToVariable.containsKey(sourceValue)) {
				String var = Commons.newVariable(varCounter);
				varCounter++;
				hashConstantToVariable.put(sourceValue, var);
			}
			if (!hashConstantToVariable.containsKey(valueInTupleString)) {
				String var = Commons.newVariable(varCounter);
				varCounter++;
				hashConstantToVariable.put(valueInTupleString, var);
			}
			
			similarValueTerm = new Variable(hashConstantToVariable.get(sourceValue));
			valueInTupleTerm = new Variable(hashConstantToVariable.get(valueInTupleString));
		}

		// Order of terms depends on lexicographic order
		List<Term> terms = new ArrayList<Term>();
		if (sourceValue.compareTo(valueInTupleString) <= 0) {
			terms.add(similarValueTerm);
			terms.add(valueInTupleTerm);
		} else {
			terms.add(valueInTupleTerm);
			terms.add(similarValueTerm);
		}
		Predicate similarityLiteral = new Predicate("sim", terms);
		return similarityLiteral;
	}

	private List<List<Pair<Tuple,String>>> computeStrata(GenericDAO genericDAO, Schema schema, String relationName,
			String inputAttributeName, List<String> exactInputValues, List<Pair<String,String>> similarInputValues, 
			String inputAttributeKnownTermsAll, List<Mode> relationModes, int sampleSize) {
		List<List<Pair<Tuple,String>>> strata = new ArrayList<List<Pair<Tuple,String>>>();

		if (relationModes == null) {
			throw new IllegalArgumentException("Missing modes for relation " + relationName.toUpperCase() + ".");
		}
		
		// Get attributes that can be constant
		RandomSet<String> constantAttributes = new RandomSet<String>();
		RandomSet<Integer> constantAttributesPositions = new RandomSet<Integer>();
		for (Mode mode : relationModes) {
			for (int i = 0; i < mode.getArguments().size(); i++) {
				if (mode.getArguments().get(i).getIdentifierType() == IdentifierType.CONSTANT) {
					String attributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames().get(i);
					constantAttributes.add(attributeName);
					constantAttributesPositions.add(i);
				}
			}
		}
		
		List<Pair<Tuple,String>> allTuplesWithSource = new ArrayList<Pair<Tuple,String>>();
		
		String inputAttributeKnownTerms = collectionToString(exactInputValues);
		String query = String.format(SELECTIN_SQL_STATEMENT, relationName, inputAttributeName, inputAttributeKnownTerms);
		GenericTableObject allTuplesResult = genericDAO.executeQuery(query);
		if (allTuplesResult != null) {
			for (Tuple tuple : allTuplesResult.getTable()) {
				allTuplesWithSource.add(new Pair<Tuple,String>(tuple,""));
			}
		}
		
		for (Pair<String,String> pair : similarInputValues) {
			String inputValue = pair.getFirst();
			String sourceValue = pair.getSecond();
			
			inputValue = "'" + inputValue.replace("'", "''") + "'";
			
			String queryForSim = String.format(SELECT_SQL_STATEMENT, relationName, inputAttributeName, inputValue);
			GenericTableObject resultForSim = genericDAO.executeQuery(queryForSim);
			if (resultForSim != null) {
				for (Tuple tuple : resultForSim.getTable()) {
					allTuplesWithSource.add(new Pair<Tuple,String>(tuple,sourceValue));
				}
			}
		}

		if (constantAttributes.isEmpty()) {
			// Add whole table as stratum
			if (allTuplesResult != null) {
				strata.add(allTuplesWithSource);
			}
		} else {
			// Get regions
			String constantAttributesString = String.join(",", constantAttributes);
			String getRegionsQuery = String.format(SELECT_GROUPBY_SQL_STATEMENT, constantAttributesString,
					relationName, inputAttributeName, inputAttributeKnownTermsAll, constantAttributesString);

			GenericTableObject getRegionsResult = genericDAO.executeQuery(getRegionsQuery);
			if (getRegionsResult != null) {
				// Each tuple represents a region
				for (Tuple tuple : getRegionsResult.getTable()) {
					List<Pair<Integer, Object>> selectConditions = new LinkedList<Pair<Integer, Object>>();
					for (int i = 0; i < constantAttributes.size(); i++) {
						selectConditions.add(new Pair<Integer, Object>(constantAttributesPositions.get(i),
								tuple.getValues().get(i)));
					}
					
					List<Pair<Tuple,String>> stratum = selectLimitWithSource(allTuplesWithSource, selectConditions, sampleSize);
					strata.add(stratum);
				}
			}
		}

		return strata;
	}
	
	private List<Pair<Tuple,String>> selectLimitWithSource(List<Pair<Tuple,String>> tuplesWithSource, List<Pair<Integer, Object>> selectConditions, int limit) {
		List<Pair<Tuple,String>> selectedTuples = new ArrayList<Pair<Tuple,String>>();
		
		// Put results in Table object
		int resultsCounter = 0;
		for (Pair<Tuple,String> pair : tuplesWithSource) {
			Tuple tuple = pair.getFirst();
			boolean keep = true;
			for (Pair<Integer,Object> selectCondition : selectConditions) {
				int attributePosition = selectCondition.getFirst();
				Object desiredValue = selectCondition.getSecond();
				if (!tuple.getValues().get(attributePosition).equals(desiredValue)) {
					keep = false;
					break;
				}
			}
			
			if (keep) {
				selectedTuples.add(pair);
				resultsCounter++;
				
				if (resultsCounter == limit)
					break;
			}
		}
		
		return selectedTuples;
	}

	/*
	 * Convert set to string "('item1','item2',...)"
	 */
	private String collectionToString(Collection<String> terms) {
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
	
	private List<String> projectSelectIn(GenericDAO genericDAO, String relationName, String projectAttributeName) {
		List<String> values = new ArrayList<String>();
		String query = String.format(PROJET_SELECT_SQL_STATEMENT, projectAttributeName, relationName);
		GenericTableObject result = genericDAO.executeQuery(query);
		if (result != null) {
			for (Tuple tuple : result.getTable()) {
				if (tuple.getValues().get(0) != null) {
					values.add(tuple.getValues().get(0).toString());
				}
			}
		}
		return values;
	}
}
