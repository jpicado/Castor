package castor.algorithms.bottomclause;

import java.util.ArrayList;
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
import castor.utils.RandomSet;

public class BottomClauseGeneratorNaiveSamplingTupleByTuple extends BottomClauseGeneratorOriginalAlgorithm {

	protected static final String SELECTIN_SQL_STATEMENT = "SELECT * FROM %s WHERE %s IN %s";
	private static final String SELECT_SQL_STATEMENT = "SELECT * FROM %s WHERE %s = %s";
	
	private boolean sample;
	
	public BottomClauseGeneratorNaiveSamplingTupleByTuple(boolean sample, int seed) {
		super(seed);
		this.sample = sample;
	}
	
	@Override
	public List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> newInTerms, Set<String> distinctTerms, String relationName, String attributeName,
			List<Mode> relationAttributeModes, Map<Pair<String, Integer>, List<Mode>> groupedModes, RandomSet<String> knownTermsSet,
			int recall, boolean ground, boolean randomizeRecall, int queryLimit, Random randomGenerator) {
		List<Predicate> newLiterals = new LinkedList<Predicate>();
		
		// If sampling is turned off, set recall to max value
		if (!this.sample) {
			recall = Integer.MAX_VALUE;
		}
		
		if (randomizeRecall == false) {
			// Create query and run
			String knownTerms = toListString(knownTermsSet);
			String query = String.format(SELECTIN_SQL_STATEMENT, relationName, attributeName, knownTerms);
			query += " LIMIT " + queryLimit;
			GenericTableObject result = genericDAO.executeQuery(query);
			
			if (result != null) {
				// Get first tuples from result
				int solutionsCounter = 0;
				for (Tuple tuple : result.getTable()) {
					if (solutionsCounter >= recall)
						break;
					
					modeOperationsForTuple(tuple, newLiterals, clause, hashConstantToVariable, hashVariableToConstant, newInTerms, distinctTerms, relationAttributeModes, ground);
					solutionsCounter++;
				}
			}
		} else {
			List<Tuple> reservoir = new ArrayList<Tuple>();
			double totalWeight = 0;
			for (String knownTerm : knownTermsSet) {
				// Create query and run
				knownTerm = knownTerm.replace("'", "''");
				String query = String.format(SELECT_SQL_STATEMENT, relationName, attributeName, "'" + knownTerm + "'");
				query += " LIMIT " + queryLimit;
				GenericTableObject result = genericDAO.executeQuery(query);
				
				if (result != null) {
					for (Tuple tuple : result.getTable()) {
						// Only keep unique tuples
						if (!reservoir.contains(tuple)) {
							// Calculate tuple's weight and add to total weight
							double tupleWeight = 1.0 / (double)(knownTermsSet.size() + tuple.getValues().size());
							totalWeight += tupleWeight;
							
							if (reservoir.size() < recall) {
								// If there is space in reservoir, keep tuple
								reservoir.add(tuple);
							} else {
								// Otherwise, accept with some probability
								double probabilityAccept = tupleWeight / totalWeight;
								for (int i=0; i<reservoir.size(); i++) {
									if (randomGenerator.nextDouble() < probabilityAccept) {
										reservoir.set(i, tuple);
										// Only allow tuple to be added to reservoir once
										break;
									}
								}
							}
						}
					}
				}
			}
			
			// For each tuple in reservoir, create a new literal and add to newLiterals
			for (Tuple tuple : reservoir) {
				modeOperationsForTuple(tuple, newLiterals, clause, hashConstantToVariable, hashVariableToConstant, newInTerms, distinctTerms, relationAttributeModes, ground);
			}
		}

		return newLiterals;
	}
	
	private void modeOperationsForTuple(Tuple tuple, List<Predicate> newLiterals, MyClause clause, 
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> newInTerms, Set<String> distinctTerms,
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
					mode, false, newInTerms, distinctTerms);
			
			// Do not add literal if it's exactly the same as head literal
			if (!literal.equals(clause.getPositiveLiterals().get(0).getAtomicSentence())) {
				addNotRepeated(newLiterals, literal);
			}
		}
	}
}
