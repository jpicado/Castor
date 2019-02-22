package castor.algorithms.coverageengines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
		// Check coverage of original clause. If original clause covers an example, any stable clause derived from original clause also covers the example.
		int[] subsumptionResult = clauseCoverageAux(clause, undecided, isPositiveRelation);
		
		// Use undecided to avoid checking coverage of an example if it's already covered
		undecided = updateUndecided(subsumptionResult, undecided);
		
		Stack<State> stack = new Stack<State>();
		Set<MyClause> evaluatedClauses = new HashSet<MyClause>();
		
		// Get the set of matching literals
		Set<Literal> matchingLiterals = new HashSet<Literal>();
		for (Literal literal : clause.getNegativeLiterals()) {
			if (literal.getAtomicSentence().getSymbolicName().startsWith(parameters.getMatchingLiteralPrefix())) {
				matchingLiterals.add(literal);
			}
		}
		
		// Add states to stack
		for (Literal literal : matchingLiterals) {
			Set<Literal> remainingLiterals = new HashSet<Literal>(matchingLiterals);
			remainingLiterals.remove(literal);
			State state = new State(clause, literal, remainingLiterals);
			stack.push(state);
		}
		
		while (!stack.isEmpty()) {
			State state = stack.pop();
			
			Set<Literal> conflictingLiterals = getConflictingLiterals(state.remainingLiterals, state.literal);
			
			// Create new clause without conflicting literals
			MyClause newClause = new MyClause(state.clause.getPositiveLiterals());
			for (Literal literal : state.clause.getNegativeLiterals()) {
				if (!conflictingLiterals.contains(literal)) {
					newClause.addLiteral(literal);
				}
			}
			
			// Remove non-head-connected literals
			newClause = removeNotHeadConnectedLiterals(newClause);
			
			// Remove conflicting literals from remaining literals
			Set<Literal> newRemainingLiterals = new HashSet<Literal>(state.remainingLiterals);
			newRemainingLiterals.removeAll(conflictingLiterals);
			
			// Remove literals in remaining literals that are not in newClause
			Iterator<Literal> iterator = newRemainingLiterals.iterator();
			while (iterator.hasNext()) {
				Literal literal = iterator.next();
				if (!newClause.getNegativeLiterals().contains(literal)) {
					iterator.remove();
				}
			}
			
			if (newRemainingLiterals.isEmpty()) {
				// If no remaining literals, evaluate clause
				if (!evaluatedClauses.contains(newClause)) {
					int[] subsumptionResultLocal = clauseCoverageAux(newClause, undecided, isPositiveRelation);
					subsumptionResult = updateSubsumptionResult(subsumptionResultLocal, subsumptionResult);
					undecided = updateUndecided(subsumptionResult, undecided);
					evaluatedClauses.add(newClause);
				}
			} else {
				// For each remaining literals, create a new state
				for (Literal literal : newRemainingLiterals) {
					Set<Literal> remainingLiterals = new HashSet<Literal>(newRemainingLiterals);
					remainingLiterals.remove(literal);
					State newState = new State(newClause, literal, remainingLiterals);
					stack.push(newState);
				}
			}
		}
		
		return subsumptionResult;
	}

	/*
	 * Remove literals that are not head-connected from clause
	 * NOTE: considers two literals connected if they share a variable (not a constant)
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
	 * NOTE: considers two literals connected if they share a variable (not a constant)
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
//		List<Term> variables = new ArrayList<Term>();
//		
//		for (Term term : terms) {
//			if (Commons.isVariable(term))
//				variables.add(term);
//		}
//		
//		return variables;
		
		return terms;
	}

	private Set<Literal> getConflictingLiterals(Set<Literal> remainingLiterals, Literal literal) {
		Set<Literal> conflictingLiterals = new HashSet<Literal>();
		for (Literal potentialLiteral : remainingLiterals) {
			if (!Collections.disjoint(literal.getAtomicSentence().getArgs(), potentialLiteral.getAtomicSentence().getArgs())) {
				conflictingLiterals.add(potentialLiteral);
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
	}
}
