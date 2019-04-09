/*
 * Bottom-clause construction using Olken sampling.
 * Queries database only once per relation-input_attribute.
 * NOTE - INCORRECT ALGORITHM: A tuple may be considered more than once (for two different relation-input_attribute pairs). Therefore, this tuple would have higher inclusion probability, which is incorrect. 
 */
package castor.algorithms.bottomclause.experimental;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.util.datastructure.Pair;
import castor.algorithms.bottomclause.BottomClauseGeneratorOriginalAlgorithm;
import castor.dataaccess.db.GenericDAO;
import castor.dataaccess.db.GenericTableObject;
import castor.hypotheses.MyClause;
import castor.language.Mode;
import castor.language.Schema;
import castor.language.Tuple;
import castor.sampling.StatisticsOlkenSampling;
import castor.utils.RandomSet;

public class BottomClauseGeneratorOlkenSampling extends BottomClauseGeneratorOriginalAlgorithm {

	private static final String SELECT_SQL_STATEMENT = "SELECT * FROM %s WHERE %s = %s;";
	private static final String COUNT_DISTINCT_TUPLES_SQL_STATEMENT = "SELECT COUNT(DISTINCT *) FROM %s WHERE %s = %s;";
	
	private StatisticsOlkenSampling statistics;

	public BottomClauseGeneratorOlkenSampling(int seed, StatisticsOlkenSampling statistics) {
		super(seed);
		this.seed = seed;
		this.statistics = statistics;
	}

	/*
	 * Implements ideas from Olken's random sampling algorithm
	 */
	public List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> newInTerms, Set<String> distinctTerms, String relationName, String attributeName,
			List<Mode> relationAttributeModes, Map<Pair<String, Integer>, List<Mode>> groupedModes, RandomSet<String> knownTermsSet,
			int recall, boolean ground, boolean shuffleTuples, int queryLimit, Random randomGenerator) {
		List<Predicate> newLiterals = new LinkedList<Predicate>();
		
		// RAJOIN algorithm (from Olken's thesis)
		for(int solutionsCounter=0; solutionsCounter < recall; solutionsCounter++) {
			//TODO max number of times to run the following loop?
			boolean accept = false;
			while (accept == false) {
				//TODO which approach is better?
				
				// APPROACH 1
				// Sample a constant from known constants
//				String randomValue = knownTermsSet.get(randomGenerator.nextInt(knownTermsSet.size()));
//				
//				// Find distinct tuples in relation with attribute = randomValue
//				String queryCardinality = String.format(COUNT_DISTINCT_TUPLES_SQL_STATEMENT, relationName, attributeName, "'"+randomValue+"'");
//				long cardinality = genericDAO.executeScalarQuery(queryCardinality);
//				
//				// If randomValue does not appear in relation, break (to avoid infinite loops)
//				if (cardinality == 0) {
////					knownTermsSet.remove(randomValue);
//					accept = true;
//					break;
//				}
				
				// APPROACH 2
				String randomValue = "";
				long cardinality = 0;
				for (int i = 0; i < knownTermsSet.size(); i++) {
					// Sample a constant from known constants
					randomValue = knownTermsSet.get(randomGenerator.nextInt(knownTermsSet.size()));
					
					// Find distinct tuples in relation with attribute = randomValue
					String queryCardinality = String.format(COUNT_DISTINCT_TUPLES_SQL_STATEMENT, relationName, attributeName, "'"+randomValue+"'");
					cardinality = genericDAO.executeScalarQuery(queryCardinality);
					
					if (cardinality == 0) {
						knownTermsSet.remove(randomValue);
					} else if (cardinality > 0) {
						// found value
						break;
					}
				}
				
				if (cardinality == 0) {
					accept = true;
					break;
				}
				////
				
				// Compute probability to keep sample
				Pair<String,String> key = new Pair<String, String>(relationName.toUpperCase(), attributeName.toUpperCase());
				double p = (double)cardinality / (double)statistics.getMaximumFrequencyOnAttribute().get(key);
				
				// Generate random number and check if accept or reject
				if (randomGenerator.nextDouble() < p) {
					// Create query and run
					String query = String.format(SELECT_SQL_STATEMENT, relationName, attributeName, "'"+randomValue+"'");
					query += " LIMIT " + queryLimit;
					GenericTableObject result = genericDAO.executeQuery(query);
					
					if (result != null) {
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
						
						accept = true;
					}
				}
			}
		}

		return newLiterals;
	}
}
