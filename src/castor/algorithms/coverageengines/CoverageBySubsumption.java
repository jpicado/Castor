package castor.algorithms.coverageengines;

import ida.ilp.logic.Clause;
import ida.ilp.logic.subsumption.Matching;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.voltdb.client.Client;

import castor.algorithms.bottomclause.BottomClauseGeneratorInsideSP;
import castor.db.QueryGenerator;
import castor.db.dataaccess.BottomClauseConstructionDAO;
import castor.db.dataaccess.GenericDAO;
import castor.db.dataaccess.GenericTableObject;
import castor.hypotheses.ClauseInfo;
import castor.hypotheses.MyClause;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;
import castor.mappings.MyClauseToIDAClause;
import castor.utils.TimeKeeper;
import castor.utils.TimeWatch;

public class CoverageBySubsumption implements CoverageEngine {
	
	private List<Tuple> allPosExamples;
	private List<Tuple> allNegExamples;
	
	private Map<Integer, Integer> posExamplesIndexes;
	private Map<Integer, Integer> negExamplesIndexes;
	
	private Matching positiveMatching;
	private Matching negativeMatching;
	
	public CoverageBySubsumption(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, Relation posExamplesRelation, Relation negExamplesRelation, String spName, int iterations, int recall, int maxterms) {
		this.initialize(genericDAO, bottomClauseConstructionDAO, posExamplesRelation, negExamplesRelation, spName, iterations, recall, maxterms);
	}
	
	private void initialize(GenericDAO genericDAO, BottomClauseConstructionDAO bottomClauseConstructionDAO, Relation posExamplesRelation, Relation negExamplesRelation, String spName, int iterations, int recall, int maxterms) {
		// Get all positive and negative examples
		String posCoverageQuery = QueryGenerator.generateQuerySelectAllTuples(posExamplesRelation);
		GenericTableObject positiveResult = genericDAO.executeQuery(posCoverageQuery);
		List<Tuple> posExamplesTuples = positiveResult.getTable();
		this.allPosExamples = new LinkedList<Tuple>();
		this.posExamplesIndexes = new HashMap<Integer,Integer>();
		
		String negCoverageQuery = QueryGenerator.generateQuerySelectAllTuples(negExamplesRelation);
		GenericTableObject negativeResult = genericDAO.executeQuery(negCoverageQuery);
		List<Tuple> negExamplesTuples = negativeResult.getTable();
		this.allNegExamples = new LinkedList<Tuple>();
		this.negExamplesIndexes = new HashMap<Integer,Integer>();
		
		// Generate ground bottom clause for all examples, create clauses for each example, add to lists
		BottomClauseGeneratorInsideSP saturator = new BottomClauseGeneratorInsideSP();
		List<Clause> posExamples = new LinkedList<Clause>();
		List<Clause> negExamples = new LinkedList<Clause>();
		int counter = 0;
		for (Tuple exampleTuple : posExamplesTuples) {
			try {
				String groundClause = saturator.generateGroundBottomClauseString(bottomClauseConstructionDAO, exampleTuple, spName, iterations, Integer.MAX_VALUE, maxterms);
				
				// IDA Clause parses does not handle single quotes well. Remove them from example. Note they should also be removed when evaluating a hypothesis.
				groundClause = groundClause.replaceAll("'", "");
				if (!groundClause.isEmpty()) {
					posExamples.add(Clause.parse(groundClause));
					
					// Add to list of correctly parsed examples
					this.allPosExamples.add(exampleTuple);
					// Store index of example
					this.posExamplesIndexes.put(exampleTuple.hashCode(), counter);
					counter++;
				}
			} catch(Exception e) {
//					System.err.println("IDA library failed to parse clause:\n" + groundClause);
				System.err.println("Positive example " + exampleTuple.getValues().toString() + " ignored in subsumption.");
			}
		}
		counter = 0;
		for (Tuple exampleTuple : negExamplesTuples) {
			try {
				String groundClause = saturator.generateGroundBottomClauseString(bottomClauseConstructionDAO, exampleTuple, spName, iterations, Integer.MAX_VALUE, maxterms);
				
				// IDA Clause parses does not handle single quotes well. Remove them from example. Note they should also be removed when evaluating a hypothesis.
				groundClause = groundClause.replaceAll("'", "");
				if (!groundClause.isEmpty()) {
					negExamples.add(Clause.parse(groundClause));
					
					// Add to list of correctly parsed examples
					this.allNegExamples.add(exampleTuple);
					// Store index of example
					this.negExamplesIndexes.put(exampleTuple.hashCode(), counter);
					counter++;
				}
			} catch(Exception e) {
//					System.err.println("IDA library failed to parse clause:\n" + groundClause);
				System.err.println("Negative example " + exampleTuple.getValues().toString() + " ignored in subsumption.");
			}
		}
		
		// Initialize matching
		this.positiveMatching = new Matching(posExamples);
		this.negativeMatching = new Matching(negExamples);
	}
	
	@Override
	public List<Tuple> getAllPosExamples() {
		return allPosExamples;
	}

	@Override
	public List<Tuple> getAllNegExamples() {
		return allNegExamples;
	}
	
//	@Override
	public boolean entails1(Client client, Schema schema, ClauseInfo clauseInfo, Tuple example, Relation examplesRelation, boolean isPositiveRelation) {
		boolean entails = false;
		Clause convertedClause = MyClauseToIDAClause.parseClause(clauseInfo.getClause());
		if (isPositiveRelation) {
			if (this.posExamplesIndexes.containsKey(example.hashCode())) {
				int index = this.posExamplesIndexes.get(example.hashCode());
				entails = this.positiveMatching.subsumption(convertedClause, index);
			}
		} else {
			if (this.negExamplesIndexes.containsKey(example.hashCode())) {
				int index = this.negExamplesIndexes.get(example.hashCode());
				entails = this.negativeMatching.subsumption(convertedClause, index);
			}
		}
		return entails;
	}
	
	@Override
	public boolean entails(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, Tuple example, Relation examplesRelation, boolean isPositiveRelation) {
		boolean entails = false;
		
		// Get index of example to evaluate
		int index;
		boolean[] coveredExamples;
		boolean[] evaluatedExamples;
		if (isPositiveRelation) {
			if (!this.posExamplesIndexes.containsKey(example.hashCode())) 
				return false;
			
			index = this.posExamplesIndexes.get(example.hashCode());
			coveredExamples = clauseInfo.getPosExamplesCovered();
			evaluatedExamples = clauseInfo.getPosExamplesEvaluated();
		} else {
			if (this.negExamplesIndexes.containsKey(example.hashCode()))
				return false;
			
			index = this.negExamplesIndexes.get(example.hashCode());
			coveredExamples = clauseInfo.getNegExamplesCovered();
			evaluatedExamples = clauseInfo.getNegExamplesEvaluated();
		}
		
		// Check if it has been evaluated
		if (evaluatedExamples[index]) {
			if (coveredExamples[index]) {
				entails = true;
			} else {
				entails = false;
			}
		} else {
			boolean[] undecided = new boolean[coveredExamples.length];
			undecided[index] = true;
			
			// Check subsumption and update covered and evaluated examples
			int[] subsumptionResult = this.clauseCoverage(clauseInfo.getClause(), undecided, isPositiveRelation);
			if (subsumptionResult[index] == Matching.YES) {
				coveredExamples[index] = true;
				entails = true;
			} else {
				entails = false;
			}
			evaluatedExamples[index] = true;
		}
		
		// Update undecided examples in clauseInfo
		if (isPositiveRelation) {
			clauseInfo.setPosExamplesCovered(coveredExamples);
			clauseInfo.setPosExamplesEvaluated(evaluatedExamples);
		} else {
			clauseInfo.setNegExamplesCovered(coveredExamples);
			clauseInfo.setNegExamplesEvaluated(evaluatedExamples);
		}
		
		return entails;
	}
	
	@Override
	public int countCoveredExamplesFromList(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, List<Tuple> examples, Relation examplesRelation, boolean isPositiveRelation) {
		// Get information from clause info
		List<Tuple> allExamples;
		boolean[] coveredExamples;
		boolean[] evaluatedExamples;
		if (isPositiveRelation) {
			allExamples = this.allPosExamples;
			coveredExamples = clauseInfo.getPosExamplesCovered();
			evaluatedExamples = clauseInfo.getPosExamplesEvaluated();
		} else {
			allExamples = this.allNegExamples;
			coveredExamples = clauseInfo.getNegExamplesCovered();
			evaluatedExamples = clauseInfo.getNegExamplesEvaluated();
		}
		
		// Create array of undecided examples
		// If example has been covered, it is not undecided
		// Otherwise; if it's in the input list of examples, it is undecided
		boolean[] undecided = new boolean[allExamples.size()];
		boolean[] examplesToEvaluate = new boolean[allExamples.size()];
		for (int i = 0; i < undecided.length; i++) {
			// Only evaluate examples specified in input list of examples
			boolean evaluateExample = false;
			Tuple example = allExamples.get(i);
			if (examples.contains(example)) {
				evaluateExample = true;
				examplesToEvaluate[i] = true;
			} else {
				examplesToEvaluate[i] = false;
			}
			
			// Evaluate only if example has not been covered and is in input list
			undecided[i] = false;
			if (!evaluatedExamples[i] && evaluateExample) {
				undecided[i] = true;
			}
		}
		
		// Evaluate coverage of clause
		int[] subsumptionResult = this.clauseCoverage(clauseInfo.getClause(), undecided, isPositiveRelation);
		int inputExamplesCovered = 0;
		for (int i = 0; i < subsumptionResult.length; i++) {
			// If it was covered before or subsumption result indicates that it is now covered, count
			if (coveredExamples[i] || subsumptionResult[i] == Matching.YES) {
				coveredExamples[i] = true;
				
				// If example was in input list, increment count
				if (examplesToEvaluate[i]) {
					inputExamplesCovered++;
				}
			}
			
			// Update evaluated examples
			if (undecided[i]) {
				evaluatedExamples[i] = true;
			}
		}
		// Update undecided examples in clauseInfo
		if (isPositiveRelation) {
			clauseInfo.setPosExamplesCovered(coveredExamples);
			clauseInfo.setPosExamplesEvaluated(evaluatedExamples);
		} else {
			clauseInfo.setNegExamplesCovered(coveredExamples);
			clauseInfo.setNegExamplesEvaluated(evaluatedExamples);
		}
		return inputExamplesCovered;
	}
	
	@Override
	public int countCoveredExamplesFromRelation(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, Relation examplesRelation, boolean isPositiveRelation) {
		int covered = 0;
		
		boolean[] examplesCovered = this.coveredExamplesFromRelation(genericDAO, schema, clauseInfo, examplesRelation, isPositiveRelation);
		for (int i = 0; i < examplesCovered.length; i++) {
			if (examplesCovered[i]) {
				covered++;
			} 
		}
		return covered;
	}

	@Override
	public int countCoveredExamplesFromRelation(GenericDAO genericDAO, Schema schema, List<ClauseInfo> definition, Relation examplesRelation, boolean isPositiveRelation) {
		int covered = 0;
		
		boolean[] examplesCovered = this.coveredExamplesFromRelation(genericDAO, schema, definition, examplesRelation, isPositiveRelation);
		for (int i = 0; i < examplesCovered.length; i++) {
			if (examplesCovered[i]) {
				covered++;
			} 
		}
		return covered;
	}
	
	@Override
	public boolean[] coveredExamplesFromRelation(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, Relation examplesRelation, boolean isPositiveRelation) {
		boolean[] coveredExamples;
		
		// Get information from clause info
		List<Tuple> allExamples;
		boolean[] evaluatedExamples;
		if (isPositiveRelation) {
			allExamples = this.allPosExamples;
			coveredExamples = clauseInfo.getPosExamplesCovered();
			evaluatedExamples = clauseInfo.getPosExamplesEvaluated();
		} else {
			allExamples = this.allNegExamples;
			coveredExamples = clauseInfo.getNegExamplesCovered();
			evaluatedExamples = clauseInfo.getNegExamplesEvaluated();
		}
		
		// Create array of undecided examples. 
		// If example has not been covered, it is undecided. Otherwise, it is no undecided (we already know that the clause covers it)
		boolean[] undecided = new boolean[allExamples.size()];
		for (int i = 0; i < undecided.length; i++) {
			if (evaluatedExamples[i]) 
				undecided[i] = false;
			else
				undecided[i] = true;
		}
		
		// Evaluate coverage of clause
		int[] subsumptionResult = this.clauseCoverage(clauseInfo.getClause(), undecided, isPositiveRelation);
		for (int i = 0; i < subsumptionResult.length; i++) {
			// If it was covered before or subsumption result indicates that it is now covered, count
			if (coveredExamples[i] || subsumptionResult[i] == Matching.YES) {
				coveredExamples[i] = true;
			}
			
			// Update evaluated examples
			if (undecided[i]) {
				evaluatedExamples[i] = true;
			}
		}
		// Update undecided examples in clauseInfo
		if (isPositiveRelation) {
			clauseInfo.setPosExamplesCovered(coveredExamples);
			clauseInfo.setPosExamplesEvaluated(evaluatedExamples);
		} else {
			clauseInfo.setNegExamplesCovered(coveredExamples);
			clauseInfo.setNegExamplesEvaluated(evaluatedExamples);
		}
		return coveredExamples;
	}
	
	@Override
	public boolean[] coveredExamplesFromRelation(GenericDAO genericDAO, Schema schema, List<ClauseInfo> definition, Relation examplesRelation, boolean isPositiveRelation) {
		// Keep array that contains true if example is covered by definition; false otherwise
		boolean[] coveredExamples;
		if (isPositiveRelation) {
			coveredExamples = new boolean[this.allPosExamples.size()];
		} else {
			coveredExamples = new boolean[this.allNegExamples.size()];
		}
		Arrays.fill(coveredExamples, false);
		
		// Compute coverage for each clause
		for (ClauseInfo clauseInfo : definition) {
			// Evaluate using theta-subsumption
			boolean[] clauseCoveredExamples = this.coveredExamplesFromRelation(genericDAO, schema, clauseInfo, examplesRelation, isPositiveRelation);

			// Update covered array
			for (int i = 0; i < clauseCoveredExamples.length; i++) {
				if (clauseCoveredExamples[i]) {
					coveredExamples[i] = true;
				} 
			}
		}
		return coveredExamples;
	}
	
	@Override
	public List<Tuple> coveredExamplesTuplesFromRelation(GenericDAO genericDAO, Schema schema, ClauseInfo clauseInfo, Relation examplesRelation, boolean isPositiveRelation) {
		List<Tuple> coveredExamples = new LinkedList<Tuple>();
		
		// Get information from clause info
		List<Tuple> allExamples;
		boolean[] evaluatedExamples;
		boolean[] covered;
		if (isPositiveRelation) {
			allExamples = this.allPosExamples;
			covered = clauseInfo.getPosExamplesCovered();
			evaluatedExamples = clauseInfo.getPosExamplesEvaluated();
		} else {
			allExamples = this.allNegExamples;
			covered = clauseInfo.getNegExamplesCovered();
			evaluatedExamples = clauseInfo.getNegExamplesEvaluated();
		}
		
		// Create array of undecided examples. 
		// If example has not been covered, it is undecided. Otherwise, it is no undecided (we already know that the clause covers it)
		boolean[] undecided = new boolean[allExamples.size()];
		for (int i = 0; i < undecided.length; i++) {
			if (evaluatedExamples[i]) 
				undecided[i] = false;
			else
				undecided[i] = true;
		}
		
		// Evaluate coverage of clause
		int[] subsumptionResult = this.clauseCoverage(clauseInfo.getClause(), undecided, isPositiveRelation);
		for (int i = 0; i < subsumptionResult.length; i++) {
			// If it was covered before or subsumption result indicates that it is now covered, add to list
			if (covered[i] || subsumptionResult[i] == Matching.YES) {
				coveredExamples.add(allExamples.get(i));
				covered[i] = true;
			}
			
			// Update evaluated examples
			if (undecided[i]) {
				evaluatedExamples[i] = true;
			}
		}
		// Update undecided examples in clauseInfo
		if (isPositiveRelation) {
			clauseInfo.setPosExamplesCovered(covered);
			clauseInfo.setPosExamplesEvaluated(evaluatedExamples);
		} else {
			clauseInfo.setNegExamplesCovered(covered);
			clauseInfo.setNegExamplesEvaluated(evaluatedExamples);
		}
		return coveredExamples;
	}
	
	/* 
	 * Compute theta-subsumption coverage by using matching
	 */
	private int[] clauseCoverage(MyClause clause, boolean[] undecided, boolean isPositiveRelation) {
		TimeWatch tw = TimeWatch.start();
		
		int[] subsumptionResult;
		// Process input for subsumption
//			Clause convertedClause = Clause.parse(clause.toString2().replaceAll("'", ""));
		Clause convertedClause = MyClauseToIDAClause.parseClause(clause);
		// Evaluate using theta-subsumption
		if (isPositiveRelation) {
			subsumptionResult = positiveMatching.evaluateOnExamples(convertedClause, undecided);
		} else {
			subsumptionResult = negativeMatching.evaluateOnExamples(convertedClause, undecided);
		}
		
		TimeKeeper.coverageTime += tw.time();
		TimeKeeper.coverageCalls++;
		
		return subsumptionResult;
	}
}
