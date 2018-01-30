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
import castor.utils.RandomSet;

public class BottomClauseGeneratorNaiveSampling extends BottomClauseGeneratorOriginalAlgorithm {

	private boolean sample;
	private Random randomGenerator;
	
	public BottomClauseGeneratorNaiveSampling(boolean sample, int seed) {
		super();
		this.sample = sample;
		this.randomGenerator = new Random(seed);
	}
	
	@Override
	public List<Predicate> operationForGroupedModes(GenericDAO genericDAO, Schema schema, MyClause clause,
			Map<String, String> hashConstantToVariable, Map<String, String> hashVariableToConstant,
			Map<String, Set<String>> newInTerms, Set<String> distinctTerms, String relationName, String attributeName,
			List<Mode> relationAttributeModes, Map<Pair<String, Integer>, List<Mode>> groupedModes, RandomSet<String> knownTermsSet,
			int recall, boolean ground, boolean randomizeRecall) {
		List<Predicate> newLiterals = new LinkedList<Predicate>();
		
		// If sampling is turned off, set recall to max value
		if (!this.sample) {
			recall = Integer.MAX_VALUE;
		}
		
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
					
					modeOperationsForTuple(tuple, newLiterals, clause, hashConstantToVariable, hashVariableToConstant, newInTerms, distinctTerms, relationAttributeModes, ground);
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
						
						modeOperationsForTuple(tuple, newLiterals, clause, hashConstantToVariable, hashVariableToConstant, newInTerms, distinctTerms, relationAttributeModes, ground);
						solutionsCounter++;
					}
				}
			}
		}
		
//		if (result != null) {
//			Set<String> usedModes = new HashSet<String>();
//			for (Mode mode : relationAttributeModes) {
//				if (ground) {
//					if (usedModes.contains(mode.toGroundModeString())) {
//						continue;
//					}
//					else {
//						mode = mode.toGroundMode();
//						usedModes.add(mode.toGroundModeString());
//					}
//				}
//				int solutionsCounter = 0;
//				for (Tuple tuple : result.getTable()) {
//					if (solutionsCounter >= recall)
//						break;
//
//					Predicate literal = createLiteralFromTuple(hashConstantToVariable, hashVariableToConstant, tuple,
//							mode, false, newInTerms, distinctTerms);
//					
//					// Do not add literal if it's exactly the same as head literal
//					if (!literal.equals(clause.getPositiveLiterals().get(0).getAtomicSentence())) {
//						addNotRepeated(newLiterals, literal);
//						solutionsCounter++;
//					}
//				}
//			}
//		}

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
