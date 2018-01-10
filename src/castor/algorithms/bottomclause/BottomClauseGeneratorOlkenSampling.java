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
import castor.profiling.StatisticsOlkenSampling;
import castor.utils.RandomSet;

public class BottomClauseGeneratorOlkenSampling extends BottomClauseGeneratorOriginalAlgorithm {

	private static final String SELECT_SQL_STATEMENT = "SELECT * FROM %s WHERE %s = %s;";
	private static final String COUNT_DISTINCT_TUPLES_SQL_STATEMENT = "SELECT COUNT(DISTINCT *) FROM %s WHERE %s = %s;";

	private Random randomGenerator;
	private StatisticsOlkenSampling statistics;

	public BottomClauseGeneratorOlkenSampling(int seed, StatisticsOlkenSampling statistics) {
		this.varCounter = 0;
		this.randomGenerator = new Random(seed);
		this.statistics = statistics;
	}

	/*
	 * Implements ideas from Olken's random sampling algorithm
	 */
	public List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> newInTerms, Set<String> distinctTerms, String relationName, String attributeName,
			List<Mode> relationAttributeModes, Map<Pair<String, Integer>, List<Mode>> groupedModes, RandomSet<String> knownTermsSet,
			int recall, boolean ground) {
		List<Predicate> newLiterals = new LinkedList<Predicate>();
		
		//TODO what if join is smaller than recall
		long sampleSize = Math.min(recall, statistics.getRelationSize().get(relationName.toUpperCase()));
		
		// RAJOIN algorithm (from Olken's thesis)
		for(int solutionsCounter=0; solutionsCounter < sampleSize; solutionsCounter++) {
			//TODO max number of times the run the following loop?
			boolean accept = false;
			while (accept == false) {
				// Sample a constant from known constants
				String randomValue = knownTermsSet.get(randomGenerator.nextInt(knownTermsSet.size()));
				
				// Find distinct tuples in relation with attribute = randomValue
				String queryCardinality = String.format(COUNT_DISTINCT_TUPLES_SQL_STATEMENT, relationName, attributeName, "'"+randomValue+"'");
				long cardinality = genericDAO.executeScalarQuery(queryCardinality);
				
				// If randomValue does not appear in relation, break (to avoid infinite loops)
				//TODO better way to avoid infinite loop?
				if (cardinality == 0) {
					accept = true;
					break;
				}
				
				// Compute probability to keep sample
				Pair<String,String> key = new Pair<String, String>(relationName.toUpperCase(), attributeName.toUpperCase());
				double p = (double)cardinality / (double)statistics.getMaxNumberOfDistinctTuplesWithAttribute().get(key);
				
				// Generate random number and check if accept or reject
				if (randomGenerator.nextDouble() < p) {
					// Create query and run
					String query = String.format(SELECT_SQL_STATEMENT, relationName, attributeName, "'"+randomValue+"'");
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
									mode, newInTerms, distinctTerms);
							
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