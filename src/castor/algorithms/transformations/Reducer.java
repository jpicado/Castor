package castor.algorithms.transformations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import castor.algorithms.clauseevaluation.ClauseEvaluator;
import castor.algorithms.clauseevaluation.EvaluationFunctions;
import castor.algorithms.coverageengines.CoverageEngine;
import castor.dataaccess.db.GenericDAO;
import castor.hypotheses.ClauseInfo;
import castor.hypotheses.MyClause;
import castor.language.Relation;
import castor.language.Schema;
import castor.language.Tuple;
import castor.utils.Commons;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;

public class Reducer {
	
	public enum MEASURE {
		CONSISTENCY,
		PRECISION
	}
	
	//================================================================================
    // NEGATIVE REDUCTION IN TERMS OF LITERALS
    //================================================================================
	
	/*
	 * Negative reduction algorithm
	 * Similar to what is described in Golem's paper, with some changes to make it schema independent
	 */
	public static MyClause negativeReduce(GenericDAO genericDAO, CoverageEngine coverageEngine, MyClause clause, Schema schema, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation, Reducer.MEASURE measure, ClauseEvaluator evaluator) {
		TimeWatch tw = TimeWatch.start();
		MyClause reducedClause = clause;
		
		if (reducedClause.getNumberNegativeLiterals() > 0) {
			// Get maximum value of variable and; add 1 to name new variable
			int varCounter = Reducer.getMaxVarValue(reducedClause) + 1;
			
			// Get negative examples covered by clause (only used if measure is consistency)
			boolean[] originallyCovered = null;
			if (measure.equals(Reducer.MEASURE.CONSISTENCY)) {
				ClauseInfo clauseInfo = new ClauseInfo(clause, coverageEngine.getAllPosExamples().size(), coverageEngine.getAllNegExamples().size());
				originallyCovered = coverageEngine.coveredExamplesFromRelation(genericDAO, schema, clauseInfo, negExamplesRelation, false);
			}
			
			int previousLength = 0;
			int previousPreviousLength = 0;
			while(true) {
				int firstLiteralPosition;
				if (measure.equals(Reducer.MEASURE.CONSISTENCY)) {
					// Find first literal such that the negative coverage of the clause is equal to negative coverage of original clause
					firstLiteralPosition = Reducer.findFirstLiteralWithSameRelationCoverage(genericDAO, coverageEngine, schema, reducedClause, negExamplesRelation, originallyCovered);
				} else if (measure.equals(CastorReducer.MEASURE.PRECISION)) {
					// Search literal that provides best precision
					firstLiteralPosition = Reducer.findLiteralWithBestScore(genericDAO, coverageEngine, schema, reducedClause, remainingPosExamples, posExamplesRelation, negExamplesRelation, evaluator, EvaluationFunctions.FUNCTION.PRECISION);
				} else {
					throw new IllegalArgumentException("Unknown reduction method.");
				}
				Literal firstLiteral = reducedClause.getNegativeLiterals().get(firstLiteralPosition);

				// Get head connecting literals
				List<Literal> headConnectingLiterals = Reducer.findHeadConnectingLiterals(reducedClause, firstLiteral, firstLiteralPosition);
				headConnectingLiterals.add(firstLiteral);
				
				// Find all literals in inclusion chain
				List<Literal> literalsInInclusionChain = DataDependenciesUtils.findLiteralsInInclusionChain(schema, reducedClause, headConnectingLiterals);
				
				// Get list of remaining literals until firstLiteralPosition
				List<Literal> remainingLiterals = new LinkedList<Literal>();
				for (int i = 0; i < firstLiteralPosition; i++) {
					if (!literalsInInclusionChain.contains(reducedClause.getNegativeLiterals().get(i))) {
						remainingLiterals.add(reducedClause.getNegativeLiterals().get(i));
					}
				}
				
				// For literals in inclusion chain that are in a position after firstLiteral, replace all their constants with variables
				for (int i = 0; i < literalsInInclusionChain.size(); i++) {
					Literal literal = literalsInInclusionChain.get(i);
					if (reducedClause.getNegativeLiterals().indexOf(literal) > firstLiteralPosition) {
						List<Term> newTerms = new LinkedList<Term>();
						for (Term term : literal.getAtomicSentence().getArgs()) {
							if (Commons.isVariable(term.getSymbolicName())) {
								newTerms.add(term);
							} else {
								newTerms.add(new Variable(Commons.newVariable(varCounter)));
								varCounter++;
							}
						}
						// Create new negative literal
						Literal newLiteral = new Literal(new Predicate(literal.getAtomicSentence().getSymbolicName(), newTerms), true);
						literalsInInclusionChain.set(i, newLiteral);
					}
				}
				
				// Create new clause with order of body literals: firstLiteral, literalsInInclusionChain, remainingLiterals
				// Note that order matters
				List<Literal> newClauseLiterals = new LinkedList<Literal>();
				newClauseLiterals.add(clause.getPositiveLiterals().get(0));
				newClauseLiterals.addAll(literalsInInclusionChain);
				newClauseLiterals.addAll(remainingLiterals);
				reducedClause = new MyClause(newClauseLiterals);
				
				// Terminate if clause length remained the same within a cycle
				if (reducedClause.getNumberNegativeLiterals() == previousPreviousLength) {
					break;
				}
				previousPreviousLength = previousLength;
				previousLength = reducedClause.getNumberNegativeLiterals();
			}
		}
		
		NumbersKeeper.reducerTime += tw.time();
		return reducedClause;
	}
	
	/*
	 * Gets the maximum value of any variable in the clause
	 */
	private static int getMaxVarValue(MyClause clause) {
		int max = Integer.MIN_VALUE;
		
		for (Literal literal : clause.getLiterals()) {
			for (Term term : literal.getAtomicSentence().getArgs()) {
				if (Commons.isVariable(term.getSymbolicName())) {
					int value = Integer.parseInt(term.getSymbolicName().replace(Commons.VARIABLE_PREFIX, ""));
					max = Math.max(max, value);
				}
			}
		}
		return max;
	}
	
	/*
	 * Find first literal in clause that yields a clause with the same coverage as the specified table
	 * Uses binary search
	 */
	private static int findFirstLiteralWithSameRelationCoverage(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, MyClause clause, Relation relation, boolean[] originallyCovered) {
		int lowerbound = 0;
		int upperbound = clause.getNumberNegativeLiterals() - 1;
		
		while(lowerbound != upperbound) {
			int n = (lowerbound + upperbound) / 2;
			
			// Create new clause with first n body literals from clause
			List<Literal> literals = new LinkedList<Literal>();
			literals.add(clause.getPositiveLiterals().get(0));
			for (int i = 0; i <= n; i++) {
				literals.add((clause.getNegativeLiterals().get(i)));
			}
			MyClause newClause = new MyClause(literals);
			ClauseInfo newClauseInfo = new ClauseInfo(newClause, coverageEngine.getAllPosExamples().size(), coverageEngine.getAllNegExamples().size());
			
			// Count covered examples of new clause and compare with originally covered examples
			boolean[] coveredExamples = coverageEngine.coveredExamplesFromRelation(genericDAO, schema, newClauseInfo, relation, false);
			if (Arrays.equals(originallyCovered, coveredExamples)) {
				upperbound = n;
			} else {
				lowerbound = n + 1;
			}
		}
		return lowerbound;
	}
	
	private static int findLiteralWithBestScore(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, MyClause clause, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation, ClauseEvaluator evaluator, EvaluationFunctions.FUNCTION function) {
		int bestPosition = 0;
		double bestScore = 0;
		
		for (int i = 0; i < clause.getNumberNegativeLiterals(); i++) {
			// Create new clause with first n body literals from clause
			List<Literal> literals = new LinkedList<Literal>();
			literals.add(clause.getPositiveLiterals().get(0));
			for (int j = 0; j <= i; j++) {
				literals.add((clause.getNegativeLiterals().get(j)));
			}
			MyClause newClause = new MyClause(literals);
			ClauseInfo newClauseInfo = new ClauseInfo(newClause, coverageEngine.getAllPosExamples().size(), coverageEngine.getAllNegExamples().size());
			
			// Compute score
			double score = evaluator.computeScore(genericDAO, coverageEngine, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, newClauseInfo, EvaluationFunctions.FUNCTION.PRECISION);
			if (score > bestScore) {
				bestPosition = i;
				bestScore = score;
			}
		}
		
		return bestPosition;
	}
	
	/*
	 * Find supporting literals of given literal
	 * Supporting literals of literal i is the set of literals j, 1<=j<i, that are needed to connect the head's variables to literal i's variables
	 */
	private static List<Literal> findHeadConnectingLiterals(MyClause clause, Literal literal, int literalPosition) {
		List<Literal> supportingLiterals = new LinkedList<Literal>();
		
		TreeNode root = new TreeNode(literal);
		Literal headLiteral = clause.getPositiveLiterals().get(0);
		
		// If literal already shares variables with head, nothing to do
		if (!shareVariables(headLiteral, literal)) {
			// For each literal before blocking literal, insert in tree and check if it connects to head literal
			for (int i = literalPosition - 1; i >= 0; i--) {
				Literal literalAtPos = clause.getNegativeLiterals().get(i);
				
				// Create new node with literal
				TreeNode literalAtPosNode = new TreeNode(literalAtPos);
				// Find potential parent using BFS
				TreeNode literalAtPosNodeParent = findParent(root, literalAtPosNode);
				
				if (literalAtPosNodeParent != null) {
					// Set parent and children
					literalAtPosNode.setParent(literalAtPosNodeParent);
					literalAtPosNodeParent.addChild(literalAtPosNode);
					
					// If literals share variables, found 
					if (shareVariables(headLiteral, literalAtPos)) {
						TreeNode currentNode = literalAtPosNode;
						while(!currentNode.getLiteral().equals(literal)) {
							supportingLiterals.add(currentNode.getLiteral());
							currentNode = currentNode.getParent();
						}
						break;
					}
				}
			}
		}
		
		return supportingLiterals;
	}
	
	/*
	 * Find parent of searchNode using breadth-first search
	 */
	private static TreeNode findParent(TreeNode root, TreeNode searchNode) {
		TreeNode parentNode = null;
		
		// BFS uses queue data structure
		Queue<TreeNode> queue = new LinkedList<TreeNode>();
		queue.add(root);
		while(!queue.isEmpty()) {
			TreeNode node = queue.remove();
			// If node's literal shares variables with searchNode's literal, node will be parent 
			if (shareVariables(node.getLiteral(), searchNode.getLiteral())) {
				parentNode = node;
				break;
			}
			// Else add node's children to queue
			for (TreeNode child : node.getChildren()) {
				queue.add(child);
			}
		}
		return parentNode;
	}

	/*
	 * Check if the two input literals share at least one variable
	 */
	private static boolean shareVariables(Literal literal1, Literal literal2) {
		boolean shareVariables = false;
		for (Term term : literal1.getAtomicSentence().getArgs()) {
			if (literal2.getAtomicSentence().getArgs().contains(term)) {
				shareVariables = true;
				break;
			}
		}
		return shareVariables;
	}
	
	
	//================================================================================
    // PROGOLEM'S ALGORITHM AND AUX FUNCTIONS
    //================================================================================
	
	// AS DESCRIBED IN PROGOLEM'S THESIS
	public static MyClause negativeReduceProGolem(GenericDAO genericDAO, CoverageEngine coverageEngine, MyClause clause, Schema schema, Relation negExamplesRelation) {
		TimeWatch tw = TimeWatch.start();
		MyClause reducedClause = clause;
		
		// Get negative examples covered by clause
		ClauseInfo clauseInfo = new ClauseInfo(clause, coverageEngine.getAllPosExamples().size(), coverageEngine.getAllNegExamples().size());
		boolean[] originallyCovered = coverageEngine.coveredExamplesFromRelation(genericDAO, schema, clauseInfo, negExamplesRelation, false);
		
		while(true) {
			// Find first literal such that the negative coverage of the clause is equal to negative coverage of original clause
			int firstLiteralPosition = Reducer.findFirstLiteralWithSameRelationCoverage(genericDAO, coverageEngine, schema, reducedClause, negExamplesRelation, originallyCovered);
			Literal firstLiteral = reducedClause.getNegativeLiterals().get(firstLiteralPosition);
			
			// Find supporting literals of firstLiteral
			List<Literal> supportingLiterals = findSupportingLiterals(reducedClause, firstLiteral, firstLiteralPosition);
			
			// Get non-supporting literals
			List<Literal> nonSupportingLiterals = new LinkedList<Literal>();
			for (int i = 0; i < firstLiteralPosition; i++) {
				if (!firstLiteral.equals(reducedClause.getNegativeLiterals().get(i)) && 
						!supportingLiterals.contains(reducedClause.getNegativeLiterals().get(i))) {
					nonSupportingLiterals.add(reducedClause.getNegativeLiterals().get(i));
				}
			}
			
			// Put all literals of new clause together and create new clause
			List<Literal> newClauseLiterals = new LinkedList<Literal>();
			newClauseLiterals.addAll(reducedClause.getPositiveLiterals());
			newClauseLiterals.addAll(supportingLiterals);
			newClauseLiterals.add(firstLiteral);
			newClauseLiterals.addAll(nonSupportingLiterals);
			MyClause newClause = new MyClause(newClauseLiterals);
			
			// Check if clause length was reduced; if not, then return clause
			if (reducedClause.getNumberNegativeLiterals() == newClause.getNumberNegativeLiterals()) {
				reducedClause = newClause;
				break;
			}
			reducedClause = newClause;
		}

		NumbersKeeper.reducerTime += tw.time();
		return reducedClause;
	}
	
	private static List<Literal> findSupportingLiterals(MyClause clause, Literal literal, int literalPosition) {
		List<Literal> supportingLiterals = new LinkedList<Literal>();
		List<Term> headVariables = clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs();
		
		// Keep a set of seen terms in body variables, without counting head variables 
		Map<Term, List<Term>> supportingVariablesForTargetTerm = new HashMap<Term, List<Term>>();
		Map<Term, Boolean> foundSupportingLiteralsForTargetTerm = new HashMap<Term, Boolean>();
		int termsToSupport = 0;
		
		// Add all terms of literal to seenTerms except head variables
		for (Term term : literal.getAtomicSentence().getArgs()) {
			if (!headVariables.contains(term)) {
				supportingVariablesForTargetTerm.put(term, new LinkedList<Term>());
				supportingVariablesForTargetTerm.get(term).add(term);
				foundSupportingLiteralsForTargetTerm.put(term, false);
				termsToSupport++;
			}
		}
		
		// Find supporting literals
		int supportedTerms = 0;
		for (int i = literalPosition - 1; i >= 0; i--) {
			Literal literalAtPos = clause.getNegativeLiterals().get(i);
			
			// Terms in literal at position
			boolean containsHeadVariables = false;
			Set<Term> literalAtPosTermsNotInHead = new HashSet<Term>();
			for (Term term : literalAtPos.getAtomicSentence().getArgs()) {
				if (!headVariables.contains(term)) {
					literalAtPosTermsNotInHead.add(term);
				} else {
					containsHeadVariables = true;
				}
			}
			
			// Literal is supporting literal only if it contains a term that is not a head variable, but is in seenTermsExceptHeadVariables
			boolean isSupportingLiteral = false;
			for (Term term : literalAtPosTermsNotInHead) {
				for (Term targetTerm : supportingVariablesForTargetTerm.keySet()) {
					if (!foundSupportingLiteralsForTargetTerm.get(targetTerm) && supportingVariablesForTargetTerm.get(targetTerm).contains(term)) {
						isSupportingLiteral = true;
						supportingVariablesForTargetTerm.get(targetTerm).addAll(literalAtPosTermsNotInHead);
						
						if (containsHeadVariables) {
							foundSupportingLiteralsForTargetTerm.put(targetTerm, true);
							supportedTerms++;
						}
					}
				}
			}
			
			if (isSupportingLiteral) {
				supportingLiterals.add(0, literalAtPos);
			}
			
			if (supportedTerms == termsToSupport) {
				break;
			}
		}
		return supportingLiterals;
	}
}

