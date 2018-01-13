package castor.algorithms.bottomclause;

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
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.profiling.StatisticsStreamSampling;
import castor.utils.RandomSet;

public class BottomClauseGeneratorStreamSampling extends BottomClauseGeneratorOriginalAlgorithm {

	private static final String SELECT_SQL_STATEMENT = "SELECT * FROM %s WHERE %s = %s;";

	private Random randomGenerator;
	private StatisticsStreamSampling statistics;
//	private Map<String, Pair<String,String>> typetoRelationAttribute;

	public BottomClauseGeneratorStreamSampling(int seed, StatisticsStreamSampling statistics) {//, Map<String, Pair<String,String>> typetoRelationAttribute) {
		this.varCounter = 0;
		this.randomGenerator = new Random(seed);
		this.statistics = statistics;
//		this.typetoRelationAttribute = typetoRelationAttribute;
	}

	/*
	 * Implements Stream-Sampling algorithm from paper "On Random Sampling Over Joins"
	 */
	public List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> newInTerms, Set<String> distinctTerms, String relationName, String attributeName,
			List<Mode> relationAttributeModes, Map<Pair<String, Integer>, List<Mode>> groupedModes, RandomSet<String> knownTermsSet,
			int recall, boolean ground) {
		List<Predicate> newLiterals = new LinkedList<Predicate>();
		
		long sampleSize = Math.min(recall, statistics.getRelationSize().get(relationName.toUpperCase()));
		
		// Stream-Sample algorithm
		
		// Step 1
		// Create sample containing dummy values (we use first value in knownTerms)
		List<String> sample = new LinkedList<String>();
		for (int i = 0; i < sampleSize; i++) {
			sample.add(knownTermsSet.get(0));
		}
		// If there are more values, user Black-Box WR2 (reservoir sampling) to get sample from knownTermsSet
		if (knownTermsSet.size() > 1) {
			long cumulativeWeight = 0;
			for(String value : knownTermsSet) {
				Pair<String,String> key = new Pair<String,String>(relationName.toUpperCase(), attributeName.toUpperCase());
				if (!statistics.getNumberOfTuplesForValue().get(key).containsKey(value)) {
					continue;
				}
				
				//TODO check for multiple joins
				long weight = statistics.getNumberOfTuplesForValue().get(key).get(value);
				cumulativeWeight += weight;
				
				double p = (double) weight / (double) cumulativeWeight;
				for (int i = 0; i < sample.size(); i++) {
					if (randomGenerator.nextDouble() < p) {
						sample.set(i, value);
					}
				}
			}
		}
		
		// Step 2
		for (String value : sample) {
			// Create query and run
			String query = String.format(SELECT_SQL_STATEMENT, relationName, attributeName, "'"+value+"'");
			GenericTableObject result = genericDAO.executeQuery(query);
			
			if (result != null && result.getTable().size() > 0) {
				// Sample a tuple from result
				//TODO can we get a single sample directly from the query to the DB?
				Tuple tuple = result.getTable().get(randomGenerator.nextInt(result.getTable().size()));
				
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
							mode, false, newInTerms, distinctTerms);
					
					// Do not add literal if it's exactly the same as head literal
					if (!literal.equals(clause.getPositiveLiterals().get(0).getAtomicSentence())) {
						addNotRepeated(newLiterals, literal);
					}
				}
			}
		}

		return newLiterals;
	}
}
