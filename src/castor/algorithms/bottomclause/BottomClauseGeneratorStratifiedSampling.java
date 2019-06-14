package castor.algorithms.bottomclause;

import java.util.ArrayList;
import java.util.Collection;
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

	private static final String NULL_PREFIX = "null";
	
	private static final String SELECTIN_SQL_STATEMENT = "SELECT * FROM %s WHERE %s IN %s";
	private static final String SELECTIN_TWOATTRIBUTES_SQL_STATEMENT = "SELECT * FROM %s WHERE %s IN %s AND %s IN %s";
	private static final String PROJET_SELECTIN_SQL_STATEMENT = "SELECT DISTINCT(%s) FROM %s WHERE %s IN %s";
	private static final String SELECT_GROUPBY_SQL_STATEMENT = "SELECT %s FROM %s WHERE %s IN %s GROUP BY %s";
	
	private static boolean SAMPLE_WITH_REPLACEMENT = false;

	private int varCounter;
	private int nullCounter;
	private int seed;

	public BottomClauseGeneratorStratifiedSampling(int seed) {
		this.varCounter = 0;
		this.nullCounter = 0;
		this.seed = seed;
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
		MyClause clause = new MyClause();
		Map<String, String> hashConstantToVariable = new HashMap<String, String>();

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

			// For each relation that have attributes with same type, call stratifiedSamplingRecursive
			if (groupedRelationsByAttributeType.containsKey(attributeType)) {
				for (Pair<String, Integer> relationInputAttributePair : groupedRelationsByAttributeType.get(attributeType)) {
					String relationName = relationInputAttributePair.getFirst();
					int inputAttributePosition = relationInputAttributePair.getSecond();
	
					stratifiedSamplingRecursive(genericDAO, schema, groupedModes, groupedRelationsByAttributeType,
							hashConstantToVariable, relationName, inputAttributePosition, attributeType, values,
							parameters.getIterations(), 1, sampleSize, ground, parameters.getQueryLimit(), clause, randomGenerator);
				}
			}
		}

		return clause;
	}

	/*
	 * Recursive function that performs stratified sampling
	 */
	private List<Tuple> stratifiedSamplingRecursive(GenericDAO genericDAO, Schema schema,
			Map<Pair<String, Integer>, List<Mode>> groupedModes,
			Map<String, List<Pair<String, Integer>>> groupedRelationsByAttributeType,
			Map<String, String> hashConstantToVariable, String relationName, int inputAttributePosition,
			String inputAttributeType, List<String> inputAttributeValues, int iterations, int currentIteration,
			int sampleSize, boolean ground, int queryLimit, MyClause clause, Random randomGenerator) {
		List<Tuple> sample = new LinkedList<Tuple>();

		// If do not have known values for input attribute, return empty set of tuples
		if (inputAttributeValues.isEmpty()) {
			return sample;
		}

		List<Mode> relationAttributeModes = groupedModes
				.get(new Pair<String, Integer>(relationName, inputAttributePosition));
		String inputAttributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames()
				.get(inputAttributePosition);
		String inputAttributeKnownTerms = collectionToString(inputAttributeValues);

		//TODO: USE OPTION1
		List<List<Tuple>> strata = computeStrata(genericDAO, schema, relationName, inputAttributeName, inputAttributeKnownTerms, relationAttributeModes, Integer.MAX_VALUE, queryLimit);
		//TODO: USE OPTION2 
//		List<List<Tuple>> strata = computeSampledStrata(genericDAO, schema, relationName, inputAttributeName, inputAttributeKnownTerms, relationAttributeModes, sampleSize, queryLimit, randomGenerator);
		
		// Check whether last iteration
		if (iterations == currentIteration) {
			// Base case: last iteration

			// Random sample
			//TODO: OPTION1
//			for (List<Tuple> stratum : strata) {
//				sample.addAll(randomSampleFromList(stratum, sampleSize, SAMPLE_WITH_REPLACEMENT, randomGenerator));
//			}
			//TODO: OPTION2
			for (List<Tuple> stratum : strata) {
				sample.addAll(stratum);
			}

			// Sample by order
			// sample.addAll(sampleFromStrata(genericDAO, schema, relationName,
			// inputAttributeName, inputAttributeKnownTerms, relationAttributeModes,
			// sampleSize, queryLimit));
		} else {
			// Recursive call
			int relationArity = relationAttributeModes.get(0).getArguments().size();
			for (int i = 0; i < relationArity; i++) {
				// Get values for attribute in position i of relation
				String joinAttributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames()
						.get(i);
				List<String> values = projectSelectIn(genericDAO, relationName, joinAttributeName, inputAttributeName, inputAttributeKnownTerms, queryLimit);

				// Get all types in modes for this attribute
				Set<String> attributeTypes = new HashSet<String>();
				for (Mode mode : relationAttributeModes) {
					attributeTypes.add(mode.getArguments().get(i).getType());
				}

				for (String type : attributeTypes) {
					
					if (!groupedRelationsByAttributeType.containsKey(type))
						continue;
					
					for (Pair<String, Integer> relationInputAttributePair : groupedRelationsByAttributeType.get(type)) {
						List<String> localValues = new ArrayList<String>(values);

						String joinRelationName = relationInputAttributePair.getFirst();
						int joinAttributePosition = relationInputAttributePair.getSecond();

						if (relationName.equals(joinRelationName) && inputAttributePosition == joinAttributePosition) {
							localValues.removeAll(inputAttributeValues);
						}

						Set<String> seenJoinModesInputAttribute = new HashSet<String>();
						for (Mode mode : groupedModes
								.get(new Pair<String, Integer>(joinRelationName, joinAttributePosition))) {
							// Get type for attribute in mode
							String joinAttributeType = mode.getArguments().get(joinAttributePosition).getType();

							// Skip mode if another mode with same relation and input attribute has already
							// been seen
							String key = joinRelationName + "_" + joinAttributePosition + "_" + joinAttributeType;
							if (seenJoinModesInputAttribute.contains(key))
								continue;
							seenJoinModesInputAttribute.add(key);

							// Recursive call
							List<Tuple> returnedTuples = stratifiedSamplingRecursive(genericDAO, schema, groupedModes,
									groupedRelationsByAttributeType, hashConstantToVariable, joinRelationName,
									joinAttributePosition, joinAttributeType, localValues, iterations,
									currentIteration + 1, sampleSize, ground, queryLimit, clause, randomGenerator);

							// Get tuples in relation that join with returnTuples
							Set<String> joinAttributeValues = projectFromTuples(returnedTuples, joinAttributePosition);
							if (joinAttributeValues.size() > 0) {
								String joinAttributeKnownTerms = collectionToString(joinAttributeValues);
								String joinQuery = String.format(SELECTIN_TWOATTRIBUTES_SQL_STATEMENT, relationName,
										inputAttributeName, inputAttributeKnownTerms, joinAttributeName,
										joinAttributeKnownTerms);
								joinQuery += " LIMIT " + queryLimit;
								GenericTableObject result = genericDAO.executeQuery(joinQuery);
								if (result != null) {
									sample.addAll(result.getTable());
								}
							}
						}
					}
				}
			}

			// Check if sample includes tuples in each stratum. If not, add tuples from
			// stratum to sample.
			for (List<Tuple> stratum : strata) {
				if (!isIntersect(sample, stratum)) {
					//TODO: OPTION1
//					sample.addAll(randomSampleFromList(stratum, sampleSize, SAMPLE_WITH_REPLACEMENT, randomGenerator));
					//TODO: OPTION2
					sample.addAll(stratum);
				}
			}
		}

		// Create literals for tuples in sample and add to clause
		for (Tuple tuple : sample) {
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

				Predicate newLiteral = createLiteralFromTuple(hashConstantToVariable, tuple, mode, false);
				clause.addNegativeLiteral(newLiteral);
			}
		}
		return sample;
	}

	/*
	 * Check if there is intersection between two lists
	 */
	private boolean isIntersect(List<Tuple> list1, List<Tuple> list2) {
		boolean intersect = false;
		for (Tuple tuple : list1) {
			if (list2.contains(tuple)) {
				intersect = true;
				break;
			}
		}
		return intersect;
	}

	/*
	 * Get a random sample from list.
	 */
	private List<Tuple> randomSampleFromList(List<Tuple> list, int sampleSize, boolean withReplacement,
			Random randomGenerator) {
		List<Tuple> sample = new LinkedList<Tuple>();

		if (list.size() <= sampleSize) {
			sample.addAll(list);
		} else {
			Set<Integer> usedIndexes = new HashSet<Integer>();
			int resultsCounter = 0;
			while (resultsCounter < sampleSize && usedIndexes.size() < list.size()) {
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
	private List<String> projectSelectIn(GenericDAO genericDAO, String relationName, String projectAttributeName,
			String inputAttributeName, String inputAttributeKnownTerms, int queryLimit) {
		List<String> values = new ArrayList<String>();
		String query = String.format(PROJET_SELECTIN_SQL_STATEMENT, projectAttributeName, relationName, inputAttributeName, inputAttributeKnownTerms, queryLimit);
		query += " LIMIT " + queryLimit;
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
	private Set<String> projectFromTuples(List<Tuple> tuples, int projectPosition) {
		Set<String> values = new HashSet<String>();
		for (Tuple tuple : tuples) {
			values.add(tuple.getStringValues().get(projectPosition));
		}
		return values;
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
				terms.add(new Constant("\"" + Commons.escapeMetaCharacters(value) + "\""));
			} else {
				// INPUT or OUTPUT type
				String valueWithSuffix = value + "_" + mode.getArguments().get(i).getType();
				if (!hashConstantToVariable.containsKey(valueWithSuffix)) {
					String var = Commons.newVariable(varCounter);
					varCounter++;

					hashConstantToVariable.put(valueWithSuffix, var);
				}
				terms.add(new Variable(hashConstantToVariable.get(valueWithSuffix)));
			}
		}

		Predicate literal = new Predicate(mode.getPredicateName(), terms);
		return literal;
	}

	/*
	 * Compute strata, and for each strata, sample the first sampleSize values.
	 */
	private List<Tuple> computeSampledStrataWithLimit(GenericDAO genericDAO, Schema schema, String relationName,
			String inputAttributeName, String inputAttributeKnownTerms, List<Mode> relationModes, int sampleSize, int queryLimit) {
		List<Tuple> sample = new ArrayList<Tuple>();

		// Get attributes that can be constant
		RandomSet<String> constantAttributesNames = new RandomSet<String>();
		RandomSet<Integer> constantAttributesPositions = new RandomSet<Integer>();
		for (Mode mode : relationModes) {
			for (int i = 0; i < mode.getArguments().size(); i++) {
				if (mode.getArguments().get(i).getIdentifierType() == IdentifierType.CONSTANT) {
					String attributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames()
							.get(i);
					constantAttributesNames.add(attributeName);
					constantAttributesPositions.add(i);
				}
			}
		}

		if (constantAttributesNames.isEmpty()) {
			// Add whole table as stratum
			String stratumQuery = String.format(SELECTIN_SQL_STATEMENT + " LIMIT " + sampleSize, relationName,
					inputAttributeName, inputAttributeKnownTerms);
			GenericTableObject stratumResult = genericDAO.executeQuery(stratumQuery);
			if (stratumResult != null) {
				sample.addAll(stratumResult.getTable());
			}
		} else {
			// Get regions
			String constantAttributesString = String.join(",", constantAttributesNames);
			String getRegionsQuery = String.format(SELECT_GROUPBY_SQL_STATEMENT, constantAttributesString,
					relationName, inputAttributeName, inputAttributeKnownTerms, constantAttributesString);
			getRegionsQuery += " LIMIT " + queryLimit;
			GenericTableObject getRegionsResult = genericDAO.executeQuery(getRegionsQuery);
			if (getRegionsResult != null) {
				String query = String.format(SELECTIN_SQL_STATEMENT, relationName, inputAttributeName, inputAttributeKnownTerms);
				query += " LIMIT " + queryLimit;
				GenericTableObject allTuplesResult = genericDAO.executeQuery(query);
				
				// Each tuple represents a region
				for (Tuple tuple : getRegionsResult.getTable()) {
					List<Pair<Integer, Object>> selectConditions = new LinkedList<Pair<Integer, Object>>();
					for (int i = 0; i < constantAttributesNames.size(); i++) {
						selectConditions.add(new Pair<Integer, Object>(constantAttributesPositions.get(i),
								tuple.getValues().get(i)));
					}
					
					List<Tuple> stratum = selectLimit(allTuplesResult.getTable(), selectConditions, sampleSize);
					sample.addAll(stratum);
				}
			}
		}

		return sample;
	}

	/*
	 * Computes strata defined by regions (distinct values in inputAttributeName).
	 */
	private List<List<Tuple>> computeStrata(GenericDAO genericDAO, Schema schema, String relationName,
			String inputAttributeName, String inputAttributeKnownTerms, List<Mode> relationModes, int sampleSize, int queryLimit) {
		List<List<Tuple>> strata = new ArrayList<List<Tuple>>();

		// Get attributes that can be constant
		RandomSet<String> constantAttributes = new RandomSet<String>();
		RandomSet<Integer> constantAttributesPositions = new RandomSet<Integer>();
		for (Mode mode : relationModes) {
			for (int i = 0; i < mode.getArguments().size(); i++) {
				if (mode.getArguments().get(i).getIdentifierType() == IdentifierType.CONSTANT) {
					String attributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames()
							.get(i);
					constantAttributes.add(attributeName);
					constantAttributesPositions.add(i);
				}
			}
		}
		
		String query = String.format(SELECTIN_SQL_STATEMENT, relationName, inputAttributeName, inputAttributeKnownTerms);
		query += " LIMIT " + queryLimit;
		GenericTableObject allTuplesResult = genericDAO.executeQuery(query);

		if (constantAttributes.isEmpty()) {
			// Add whole table as stratum
			
			if (allTuplesResult != null) {
				strata.add(allTuplesResult.getTable());
			}
		} else {
			// Get regions
			String constantAttributesString = String.join(",", constantAttributes);
			String getRegionsQuery = String.format(SELECT_GROUPBY_SQL_STATEMENT, constantAttributesString,
					relationName, inputAttributeName, inputAttributeKnownTerms, constantAttributesString);
			getRegionsQuery += " LIMIT " + queryLimit;
			GenericTableObject getRegionsResult = genericDAO.executeQuery(getRegionsQuery);
			if (getRegionsResult != null) {
				// Each tuple represents a region
				for (Tuple tuple : getRegionsResult.getTable()) {
					List<Pair<Integer, Object>> selectConditions = new LinkedList<Pair<Integer, Object>>();
					for (int i = 0; i < constantAttributes.size(); i++) {
						if (tuple.getValues().get(i) != null) {
							selectConditions.add(new Pair<Integer, Object>(constantAttributesPositions.get(i),
									tuple.getValues().get(i)));
						}
					}
					
					List<Tuple> stratum = selectLimit(allTuplesResult.getTable(), selectConditions, sampleSize);
					strata.add(stratum);
				}
			}
		}

		return strata;
	}
	
	/*
	 * Computes strata defined by regions (distinct values in inputAttributeName).
	 * Number of samples in strata depends on number of regions and sample size.
	 */
	private List<List<Tuple>> computeSampledStrata(GenericDAO genericDAO, Schema schema, String relationName,
			String inputAttributeName, String inputAttributeKnownTerms, List<Mode> relationModes, int sampleSize, int queryLimit, Random randomGenerator) {
		List<List<Tuple>> strata = new ArrayList<List<Tuple>>();

		// Get attributes that can be constant
		RandomSet<String> constantAttributes = new RandomSet<String>();
		RandomSet<Integer> constantAttributesPositions = new RandomSet<Integer>();
		for (Mode mode : relationModes) {
			for (int i = 0; i < mode.getArguments().size(); i++) {
				if (mode.getArguments().get(i).getIdentifierType() == IdentifierType.CONSTANT) {
					String attributeName = schema.getRelations().get(relationName.toUpperCase()).getAttributeNames()
							.get(i);
					constantAttributes.add(attributeName);
					constantAttributesPositions.add(i);
				}
			}
		}
		
		String query = String.format(SELECTIN_SQL_STATEMENT, relationName, inputAttributeName, inputAttributeKnownTerms);
		query += " LIMIT " + queryLimit;
		GenericTableObject allTuplesResult = genericDAO.executeQuery(query);

		if (constantAttributes.isEmpty()) {
			// Add sampled table as stratum
			if (allTuplesResult != null) {
				strata.add(randomSampleFromList(allTuplesResult.getTable(), sampleSize, SAMPLE_WITH_REPLACEMENT, randomGenerator));
			}
		} else {
			// Get regions
			String constantAttributesString = String.join(",", constantAttributes);
			String getRegionsQuery = String.format(SELECT_GROUPBY_SQL_STATEMENT, constantAttributesString,
					relationName, inputAttributeName, inputAttributeKnownTerms, constantAttributesString);
			getRegionsQuery += " LIMIT " + queryLimit;
			GenericTableObject getRegionsResult = genericDAO.executeQuery(getRegionsQuery);
			if (getRegionsResult != null) {
				int nRegions = getRegionsResult.getTable().size();
				if (nRegions > 0) {
					// if #regions > sampleSize, want 1 per region
					// else, want ceiling(sampleSize / #regions) per region
					int samplePerRegion;
					if (getRegionsResult.getTable().size() > sampleSize) {
						samplePerRegion = 1;
					} else {
						samplePerRegion = (int)Math.ceil(sampleSize / getRegionsResult.getTable().size());
					}
					
					// Each tuple represents a region
					for (Tuple tuple : getRegionsResult.getTable()) {
						List<Pair<Integer, Object>> selectConditions = new LinkedList<Pair<Integer, Object>>();
						for (int i = 0; i < constantAttributes.size(); i++) {
							selectConditions.add(new Pair<Integer, Object>(constantAttributesPositions.get(i),
									tuple.getValues().get(i)));
						}

						// Get all tuples that satisfy region
						List<Tuple> stratum = selectLimit(allTuplesResult.getTable(), selectConditions, Integer.MAX_VALUE);
						// Sample from these tuples
						strata.add(randomSampleFromList(stratum, samplePerRegion, SAMPLE_WITH_REPLACEMENT, randomGenerator));
					}
				}
			}
		}

		return strata;
	}
	
	private List<Tuple> selectLimit(List<Tuple> tuples, List<Pair<Integer, Object>> selectConditions, int limit) {
		List<Tuple> selectedTuples = new ArrayList<Tuple>();
		
		// Put results in Table object
		int resultsCounter = 0;
		for (Tuple tuple : tuples) {			
			boolean keep = true;
			for (Pair<Integer,Object> selectCondition : selectConditions) {
				int attributePosition = selectCondition.getFirst();
				
				Object value;
				if (tuple.getValues().get(attributePosition) != null) {
					value = tuple.getValues().get(attributePosition);

					Object desiredValue = selectCondition.getSecond();
					if (!value.equals(desiredValue)) {
						keep = false;
						break;
					}
				}
			}
			
			if (keep) {
				selectedTuples.add(tuple);
				resultsCounter++;
				
				if (resultsCounter == limit)
					break;
			}
		}
		
		return selectedTuples;
	}

	private void runComputeValuesImportance(GenericDAO genericDAO, List<Tuple> examples, boolean isPositive,
			Schema schema, DataModel dataModel, Parameters parameters,
			Map<String, Map<String, Map<String, Pair<Integer, Integer>>>> valuesImportance) {
		for (Tuple example : examples) {
			this.computeValuesImportance(valuesImportance, genericDAO, example, isPositive, schema,
					dataModel.getModeH(), dataModel.getModesB(), parameters.getIterations(), parameters.isUseInds(),
					parameters.getMaxterms());
		}
	}

	private void computeValuesImportance(Map<String, Map<String, Map<String, Pair<Integer, Integer>>>> valuesImportance,
			GenericDAO genericDAO, Tuple exampleTuple, boolean isPositive, Schema schema, Mode modeH, List<Mode> modesB,
			int iterations, boolean applyInds, int maxTerms) {

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

			if (!headMode && mode.getArguments().get(i).getIdentifierType() == IdentifierType.CONSTANT) {
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
			String knownTerms = collectionToString(inTerms.get(attributeType));
			// USING OR
			// expression = attributeName + " IN " + knownTerms;

			// USING UNION
			expression = String.format(SELECTIN_SQL_STATEMENT, mode.getPredicateName(), attributeName, knownTerms);
		}
		return expression;
	}

	/*
	 * Convert set to string "('item1','item2',...)"
	 */
	private String collectionToString(Collection<String> terms) {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
				
		int counter = 0;
		for (String term : terms) {
			if (term != null) {
				if (counter > 0) {
					builder.append(",");
				}
				
				// Escape single quotes
				term = term.replace("'", "''");
				builder.append("'" + term + "'");
				counter++;
			}
		}
		builder.append(")");
		return builder.toString();
	}
}
