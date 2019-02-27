package castor.algorithms.coverageengines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Term;
import castor.algorithms.bottomclause.BottomClauseGenerator;
import castor.dataaccess.db.BottomClauseConstructionDAO;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.MyClause;
import castor.language.Relation;
import castor.language.Schema;
import castor.mappings.MyClauseToIDAClause;
import castor.settings.DataModel;
import castor.settings.Parameters;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;
import ida.ilp.logic.Clause;
import ida.ilp.logic.subsumption.Matching;

public class CoverageBySubsumptionParallelAlternativeCoverage extends CoverageBySubsumptionParallel {
	
	private Parameters parameters;

	public CoverageBySubsumptionParallelAlternativeCoverage(GenericDAO genericDAO,
			BottomClauseConstructionDAO bottomClauseConstructionDAO, BottomClauseGenerator saturator,
			Relation posExamplesRelation, Relation negExamplesRelation, Schema schema, DataModel dataModel,
			Parameters parameters, boolean withMatchings, EXAMPLES_SOURCE examplesSource, String posExamplesFile,
			String negExamplesFile) {
		super(genericDAO, bottomClauseConstructionDAO, saturator, posExamplesRelation, negExamplesRelation, schema, dataModel,
				parameters, withMatchings, examplesSource, posExamplesFile, negExamplesFile);
		
		this.parameters = parameters;
	}
	
	@Override
	protected int[] clauseCoverage(MyClause clause, boolean[] undecided, boolean isPositiveRelation) {
//		System.out.println(Formatter.prettyPrint(clause));
		// Check coverage of original clause. If original clause covers an example, any stable clause derived from original clause also covers the example.
		int[] subsumptionResult = clauseCoverageAux(clause, undecided, isPositiveRelation);
		
		// If checking coverage on positive examples, return subsumption result
		// The stable clauses derived from the clause may cover more examples than what is reported in subsumptionResult. However, those examples may not be covered by all stable clauses.
		if (isPositiveRelation)
			return subsumptionResult;
		
		// Use undecided to avoid checking coverage of an example if it's already covered
		undecided = updateUndecided(subsumptionResult, undecided);
		
		Stack<State> stack = new Stack<State>();
		Set<MyClause> evaluatedClauses = new HashSet<MyClause>();
		evaluatedClauses.add(clause);
		
		Set<State> exploredStates = new HashSet<State>();
		
		// Get the set of matching literals
		Set<Literal> matchingLiterals = new HashSet<Literal>();
		for (Literal literal : clause.getNegativeLiterals()) {
			if (literal.getAtomicSentence().getSymbolicName().startsWith(parameters.getMatchingLiteralPrefix())) {
				matchingLiterals.add(literal);
			}
		}
		
		// Keep only literals that will be conflicting with other literals
		//TODO is this right?
		matchingLiterals = getConflictingLiterals(matchingLiterals);
		
		// If there is only matching literal, there is only one stable clause
		if (matchingLiterals.size() <= 1)
			return subsumptionResult;
		
		// Add states to stack
		for (Literal literal : matchingLiterals) {
			Set<Literal> remainingLiterals = new HashSet<Literal>(matchingLiterals);
			remainingLiterals.remove(literal);
			State state = new State(clause, literal, remainingLiterals);
			stack.push(state);
		}
		
		while (!stack.isEmpty()) {
			if (allDecided(undecided)) 
				break;
			
			State state = stack.pop();
			exploredStates.add(state);
			
			Set<Literal> conflictingLiteralsWithLiteral = getConflictingLiteralsWithLiteral(state.remainingLiterals, state.literal);
			
			// Create new clause without conflicting literals
			MyClause newClause = new MyClause(state.clause.getPositiveLiterals());
			for (Literal literal : state.clause.getNegativeLiterals()) {
				if (!conflictingLiteralsWithLiteral.contains(literal)) {
					newClause.addLiteral(literal);
				}
			}
			
			// Remove non-head-connected literals
			// NOTE: if I use order-dependent method, I may remove literals that should stay in clause
//			newClause = removeNotHeadConnectedLiterals(newClause);
			newClause = removeNotHeadConnectedLiteralsOrderAgnostic(newClause);
			
			// Remove conflicting literals from remaining literals
			Set<Literal> newRemainingLiterals = new HashSet<Literal>(state.remainingLiterals);
			newRemainingLiterals.removeAll(conflictingLiteralsWithLiteral);
			
			// Remove literals in remaining literals that are not in newClause
			Iterator<Literal> iterator = newRemainingLiterals.iterator();
			while (iterator.hasNext()) {
				Literal literal = iterator.next();
				if (!newClause.getNegativeLiterals().contains(literal)) {
					iterator.remove();
				}
			}
			
			// Keep only literals that will be conflicting with other literals
			//TODO is this right?
			newRemainingLiterals = getConflictingLiterals(newRemainingLiterals);
			
			if (newRemainingLiterals.isEmpty()) {
				// If no remaining literals, evaluate clause
				if (!evaluatedClauses.contains(newClause)) {
					int[] subsumptionResultLocal = clauseCoverageAux(newClause, undecided, isPositiveRelation);
					subsumptionResult = updateSubsumptionResult(subsumptionResultLocal, subsumptionResult);
					undecided = updateUndecided(subsumptionResult, undecided);
					evaluatedClauses.add(newClause);
					
//					System.out.println("GC:"+Formatter.prettyPrint(newClause));
				}
			} else {
				// For each remaining literals, create a new state
				for (Literal literal : newRemainingLiterals) {
					Set<Literal> remainingLiterals = new HashSet<Literal>(newRemainingLiterals);
					remainingLiterals.remove(literal);
					State newState = new State(newClause, literal, remainingLiterals);
					
					if (!exploredStates.contains(newState)) 
						stack.push(newState);
				}
			}
		}
		
		return subsumptionResult;
	}

	/*
	 * Remove literals that are not head-connected from clause
	 * NOTE: is not order dependent
	 */
	private MyClause removeNotHeadConnectedLiteralsOrderAgnostic(MyClause clause) {
		Set<Term> seenTerms = new HashSet<Term>();

		// Add head variables to seen terms
		seenTerms.addAll(getVariableTerms(clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs()));
		

		// MyClause's list of literals is unmodifiable, so must create a new list
		List<Literal> newClauseLiterals = new LinkedList<Literal>();
		newClauseLiterals.addAll(clause.getNegativeLiterals());

		// Iterate through negative literals and remove not head-connected from list
		Iterator<Literal> iterator = newClauseLiterals.iterator();
		while (iterator.hasNext()) {
			Literal literal = iterator.next();

			boolean headConnected = false;

			Set<Term> literalConnectedTerms = new HashSet<Term>();
			literalConnectedTerms.addAll(getVariableTerms(literal.getAtomicSentence().getArgs()));
			for (Literal otherLiteral : clause.getNegativeLiterals()) {
				// If there is an intersection between literal and otherLiteral terms, add
				// otherLiteral terms to list
				if (!Collections.disjoint(literalConnectedTerms, otherLiteral.getAtomicSentence().getArgs())) {
					literalConnectedTerms.addAll(getVariableTerms(otherLiteral.getAtomicSentence().getArgs()));
				}

				// Check if there's intersection between current list and head terms
				if (!Collections.disjoint(seenTerms, literalConnectedTerms)) {
					headConnected = true;
					break;
				}
			}

			if (headConnected) {
				// Literal is head-connected, so add its variables to seen terms
				seenTerms.addAll(getVariableTerms(literal.getAtomicSentence().getArgs()));
			} else {
				// Remove literal because it is not head-connected
				iterator.remove();
			}
		}

		newClauseLiterals.addAll(clause.getPositiveLiterals());
		MyClause newClause = new MyClause(newClauseLiterals);

		return newClause;
	}
	
	/*
	 * Remove literals that are not head-connected from clause
	 * NOTE: considers the order-dependent definition of head-connected
	 */
	private MyClause removeNotHeadConnectedLiterals(MyClause clause) {
		Set<Term> seenTerms = new HashSet<Term>();

		// Add head variables to seen terms
		seenTerms.addAll(getVariableTerms(clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs()));
		
		// MyClause's list of literals is unmodifiable, so must create a new list
		List<Literal> newClauseLiterals = new LinkedList<Literal>();
		newClauseLiterals.addAll(clause.getNegativeLiterals());

		// Iterate through negative literals and remove not head-connected from list
		Iterator<Literal> iterator = newClauseLiterals.iterator();
		while (iterator.hasNext()) {
			Literal literal = iterator.next();
			
			if (!Collections.disjoint(seenTerms, literal.getAtomicSentence().getArgs())) {
				seenTerms.addAll(getVariableTerms(literal.getAtomicSentence().getArgs()));
			} else {
				iterator.remove();
			}
		}
		
		newClauseLiterals.addAll(clause.getPositiveLiterals());
		MyClause newClause = new MyClause(newClauseLiterals);

		return newClause;
	}
	
	//TODO: remove getVariablesTerms in previous functions???
	private List<Term> getVariableTerms(List<Term> terms) {
		return terms;
//		List<Term> variables = new ArrayList<Term>();
//		
//		for (Term term : terms) {
//			if (Commons.isVariable(term))
//				variables.add(term);
//		}
//		
//		return variables;
	}

	private Set<Literal> getConflictingLiteralsWithLiteral(Set<Literal> remainingLiterals, Literal literal) {
		Set<Literal> conflictingLiterals = new HashSet<Literal>();
		for (Literal potentialLiteral : remainingLiterals) {
			if (!Collections.disjoint(literal.getAtomicSentence().getArgs(), potentialLiteral.getAtomicSentence().getArgs())) {
				conflictingLiterals.add(potentialLiteral);
			}
		}
		return conflictingLiterals;
	}
	
	private Set<Literal> getConflictingLiterals(Set<Literal> literals) {
		Set<Literal> conflictingLiterals = new HashSet<Literal>();
		
		Map<Term,Set<Literal>> termToLiterals = new HashMap<Term,Set<Literal>>();
		for (Literal literal : literals) {
			for (Term term : literal.getAtomicSentence().getArgs()) {
				if (!termToLiterals.containsKey(term)) {
					termToLiterals.put(term, new HashSet<Literal>());
				}
				termToLiterals.get(term).add(literal);
			}
		}
		
		for (Term term : termToLiterals.keySet()) {
			if (termToLiterals.get(term).size() > 1) {
				conflictingLiterals.addAll(termToLiterals.get(term));
			}
		}
		
		return conflictingLiterals;
	}

	private boolean[] updateUndecided(int[] subsumptionResult, boolean[] undecided) {
		for (int i = 0; i < undecided.length; i++) {
			if (subsumptionResult[i] == Matching.YES)
				undecided[i] = false;
		}
		return undecided;
	}
	
	private boolean allDecided(boolean[] undecided) {
		for (int i = 0; i < undecided.length; i++) {
			if (undecided[i])
				return false;
		}
		return true;
	}
	
	private int[] updateSubsumptionResult(int[] subsumptionResultLocal, int[] subsumptionResult) {
		for (int i = 0; i < subsumptionResultLocal.length; i++) {
			if (subsumptionResultLocal[i] == Matching.YES)
				subsumptionResult[i] = Matching.YES;
		}
		return subsumptionResult;
	}
	
	private int[] clauseCoverageAux(MyClause clause, boolean[] undecided, boolean isPositiveRelation) {
		TimeWatch tw = TimeWatch.start();

		int nExamples = undecided.length;
		int[] subsumptionResult = new int[nExamples];

		try {
			// Create list of tasks
			List<Callable<Result>> tasks = new ArrayList<Callable<Result>>();

			int examplesPerMatching;
			if (isPositiveRelation)
				examplesPerMatching = this.posExamplesPerMatching;
			else
				examplesPerMatching = this.negExamplesPerMatching;

			// For each thread, create a task where the matching computes subsumption of its
			// examples
			for (int i = 0; i < threads; i++) {
				Clause convertedClause = MyClauseToIDAClause.parseClause(clause);

				int start = i * examplesPerMatching;
				int end = start + examplesPerMatching;
				// If last matching, add all remaining examples
				if (i == threads - 1) {
					end = nExamples;
				}
				int localExamplesPerMatching = end - start;

				// Create undecided array
				boolean[] undecidedLocal = new boolean[localExamplesPerMatching];
				for (int j = start; j < end; j++) {
					undecidedLocal[j - start] = undecided[j];
				}
				// Get correct matching
				Matching matching;
				if (isPositiveRelation)
					matching = this.positiveMatchings[i];
				else
					matching = this.negativeMatchings[i];
				// Add task
				tasks.add(new Evaluator(matching, convertedClause, undecidedLocal, start, end));
			}

			// Perform all tasks in parallel
			ExecutorService executor = Executors.newFixedThreadPool(threads);
			// ExecutorService executor = Executors.newCachedThreadPool();
			List<Future<Result>> results;
			results = executor.invokeAll(tasks);
			executor.shutdown();

			// Process results
			for (Future<Result> result : results) {
				// Put results in subsumptionResult
				for (int i = result.get().start; i < result.get().end && i < subsumptionResult.length; i++) {
					subsumptionResult[i] = result.get().subsumptionResult[i - result.get().start];
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}

		NumbersKeeper.coverageTime += tw.time();
		NumbersKeeper.coverageCalls++;
		NumbersKeeper.clauseLengthSum += clause.getNumberLiterals();
		
		return subsumptionResult;
	}
	
	private static class State {
		private final MyClause clause;
		private final Literal literal;
		private final Set<Literal> remainingLiterals;

		public State(MyClause clause, Literal literal, Set<Literal> remainingLiterals) {
			this.clause = clause;
			this.literal = literal;
			this.remainingLiterals = remainingLiterals;
		}

		@Override
		public int hashCode() {
			return Objects.hash(clause, literal, remainingLiterals);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			State other = (State) obj;
			if (clause == null) {
				if (other.clause != null)
					return false;
			} else if (!clause.equals(other.clause))
				return false;
			if (literal == null) {
				if (other.literal != null)
					return false;
			} else if (!literal.equals(other.literal))
				return false;
			if (remainingLiterals == null) {
				if (other.remainingLiterals != null)
					return false;
			} else if (!remainingLiterals.equals(other.remainingLiterals))
				return false;
			return true;
		}
	}
}
