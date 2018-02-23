package castor.algorithms.transformations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google_voltpatches.common.collect.Sets;

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

public class CastorReducer {
	
	public static enum MEASURE {
		CONSISTENCY,
		PRECISION
	}
	
	//================================================================================
    // NEGATIVE REDUCTION IN TERMS OF INCLUSION CHAINS (INSTANCES OF INCLUSSION CLASSES)
    //================================================================================
	
	/*
	 * Negative reduction algorithm
	 * Similar to what is described in Golem's paper, with some changes to make it schema independent
	 * Works with inclusion chains (instances of inclusion classes)
	 */
	public static MyClause negativeReduce(GenericDAO genericDAO, CoverageEngine coverageEngine, MyClause clause, Schema schema, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation, CastorReducer.MEASURE measure, ClauseEvaluator evaluator) {
		TimeWatch tw = TimeWatch.start();
		
		// Reorder so that it is head-connected from left to right. This way it won't remove literals that are needed to make other literals head-connected. 
		clause = ClauseTransformations.reorderClauseForHeadConnected(clause);
		
		List<Term> headVariables = clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs();
		
		List<List<Literal>> allChains = DataDependenciesUtils.findAllInclusionChains2(schema, clause);
		allChains = reorderChains(allChains, headVariables);
		
		// Get negative examples covered by clause (only used if measure is consistency)
		boolean[] originallyCovered = null;
		if (measure.equals(CastorReducer.MEASURE.CONSISTENCY)) {
			ClauseInfo clauseInfo = new ClauseInfo(clause, coverageEngine.getAllPosExamples().size(), coverageEngine.getAllNegExamples().size());
			originallyCovered = coverageEngine.coveredExamplesFromRelation(genericDAO, schema, clauseInfo, negExamplesRelation, false);
		}
		
		// Try to reduce only if clause is not empty
		if (clause.getNumberNegativeLiterals() > 0) {
			int previousLength = 0;
			while(true) {
				int bestChainPosition;
				if (measure.equals(CastorReducer.MEASURE.CONSISTENCY)) {
					// Find first literal such that the negative coverage of the clause is equal to negative coverage of original clause
					bestChainPosition = CastorReducer.findFirstChainWithSameRelationCoverage(genericDAO, coverageEngine, schema, clause, negExamplesRelation, originallyCovered, allChains);
				} else if (measure.equals(CastorReducer.MEASURE.PRECISION)) {
					// Search chain that provides best score
					bestChainPosition = CastorReducer.findChainWithBestScore(genericDAO, coverageEngine, schema, clause, remainingPosExamples, posExamplesRelation, negExamplesRelation, allChains, evaluator, EvaluationFunctions.FUNCTION.PRECISION);
				} else {
					throw new IllegalArgumentException("Unknown reduction method.");
				}
				
				// Get head connecting chains for literal in best chain
				List<Literal> bestChain = allChains.get(bestChainPosition);
				List<List<Literal>> headConnectingChains = CastorReducer.findHeadConnectingChains(clause, allChains, bestChain, bestChainPosition);
				
				// Compute best variation of bestChain
				//TODO not used in HIV, but used in other datasets
//				bestChain = CastorReducer.computeBestVariationOfChain(genericDAO, coverageEngine, schema, clause, posExamplesRelation, negExamplesRelation, allChains, bestChain, bestChainPosition);
				
				// Put head connecting chains and best chain together
				List<List<Literal>> chainsToKeep = new LinkedList<List<Literal>>();
				chainsToKeep.addAll(headConnectingChains);
				chainsToKeep.add(bestChain);
				
				// Get remaining chains until firstChainPosition
				List<List<Literal>> remainingChains = new LinkedList<List<Literal>>();
				for (int i = 0; i < bestChainPosition; i++) {
					boolean inChainsToKeep = false;
					for (List<Literal> chain : chainsToKeep) {
						if (DataDependenciesUtils.sameChain(allChains.get(i), chain)) {
							inChainsToKeep = true;
							break;
						}
					}
					if (!inChainsToKeep) {
						remainingChains.add(allChains.get(i));
					}
				}
				
				////
				// Check clause safety
				
				// Find head variables that do not appear in body
				List<Term> variablesNotInBody = new LinkedList<Term>();
				for (Term term : headVariables) {
					if (!termAppearsInChains(term, chainsToKeep) && !termAppearsInChains(term, remainingChains)) {
						variablesNotInBody.add(term);
					}
				}
				
				// Find all chains containing terms in variablesNotInBody
				List<List<Literal>> chainsWithHeadVariables = new LinkedList<List<Literal>>();
				for (int i = bestChainPosition; i < allChains.size(); i++) {
					
					boolean addChain = false;
					Iterator<Term> iterator = variablesNotInBody.iterator();
					while(iterator.hasNext()) {
						Term term = iterator.next();
						if (termAppearsInChain(term, allChains.get(i))) {
							addChain = true;
							iterator.remove();
						}
					}
					
					if (addChain) {
						boolean inChainsToKeep = false;
						for (List<Literal> chain : chainsToKeep) {
							if (DataDependenciesUtils.sameChain(allChains.get(i), chain)) {
								inChainsToKeep = true;
								break;
							}
						}
						if (!inChainsToKeep) {
							chainsWithHeadVariables.add(allChains.get(i));
						}
					}
					
					if (variablesNotInBody.isEmpty()) break;
				}
				////
				
				// Update allChains
				allChains.clear();
				allChains.addAll(chainsToKeep);
				allChains.addAll(chainsWithHeadVariables);
				allChains.addAll(remainingChains);
				
				// Terminate if allChains length remained the same within a cycle
				if (allChains.size() == previousLength) {
					break;
				}
				previousLength = allChains.size();
			}
		}
		
		// Create new clause with head literal and literals in allChains
		Set<Literal> allLiterals = new HashSet<Literal>();
		allLiterals.add(clause.getPositiveLiterals().get(0));
		for (List<Literal> chain : allChains) {
			allLiterals.addAll(chain);
		}
		MyClause newClause = new MyClause(allLiterals);
		
		NumbersKeeper.reducerTime += tw.time();
		return newClause;
	}

	/*
	 * Reorder chains according to how many terms in input variables they contain (descending)
	 * Bucket sort
	 */
	private static List<List<Literal>> reorderChains(List<List<Literal>> chains, List<Term> variables) {
		Map<Integer, List<List<Literal>>> chainsByCount = new HashMap<Integer, List<List<Literal>>>(variables.size());
		
		// Count how many terms it contains and add to corresponding bucket
		for (List<Literal> chain : chains) {
			int count = 0;
			for (Term term : variables) {
				if(termAppearsInChain(term, chain))
					count++;
			}
			
			if (!chainsByCount.containsKey(count)) {
				chainsByCount.put(count, new LinkedList<List<Literal>>());
			}
			chainsByCount.get(count).add(chain);
		}
		
		// Put all chains in same list, in order
		List<List<Literal>> orderedChains = new LinkedList<List<Literal>>();
		for (int i = variables.size(); i >= 0; i--) {
			if (chainsByCount.containsKey(i)) {
				orderedChains.addAll(chainsByCount.get(i));
			}
		}
		
		return orderedChains;
	}

	/*
	 * Check whether term appears in a group of chains
	 */
	private static boolean termAppearsInChains(Term term, List<List<Literal>> chains) {
		for (List<Literal> chain : chains) {
			if (termAppearsInChain(term, chain)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Check whether term appears in chain
	 */
	private static boolean termAppearsInChain(Term term, List<Literal> chain) {
		for (Literal literal : chain) {
			if (literal.getAtomicSentence().getArgs().contains(term)) {
				return true;
			}
		}
		return false;
	}

	// NEEDED FOR SOME DATASETS
	//TODO compute best variation according to hypothesis language (modes)
	private static List<Literal> computeBestVariationOfChain(GenericDAO genericDAO,
			CoverageEngine coverageEngine, Schema schema, MyClause clause,
			Relation posExamplesRelation, Relation negExamplesRelation,
			List<List<Literal>> allChains, List<Literal> bestChain,
			int bestChainPosition) {
		
		// Get all constants in bestChain
		Set<String> constantsInChain = new HashSet<String>();
		for (Literal literal : bestChain) {
			for (Term term : literal.getAtomicSentence().getArgs()) {
				if (!Commons.isVariable(term)) {
					constantsInChain.add(term.getSymbolicName());
				}
			}
		}
		
		List<Literal> newBestChain = bestChain;
		
		// If there are no constants, there is nothing to do
		if (!constantsInChain.isEmpty()) {
			// Compute all combinations of constants
			Set<Set<String>> constantsCombinations = Sets.powerSet(constantsInChain);
			
			// Create new clause with chains before firstChainPosition
			Set<Literal> literals = new LinkedHashSet<Literal>();
			literals.add(clause.getPositiveLiterals().get(0));
			for (int i = 0; i < bestChainPosition; i++) {
				literals.addAll(allChains.get(i));
			}
			
			// Get maximum value of variable and; add 1 to name new variable
			// Will be used to create new variables
			int varCounter = Commons.getMaxVarValue(clause) + 1;
			// Keep hash map from constants to variable name
			Map<String, String> hash = new HashMap<String, String>();
			
			// Try all combinations of constants replaced by new variables
			double bestScore = Double.MIN_VALUE;
			for (Set<String> combination : constantsCombinations) {
				// Assign variables to all constants
				for (String contant : combination) {
					if (!hash.containsKey(contant)) {
						hash.put(contant, Commons.newVariable(varCounter));
						varCounter++;
					}
				}
				// Replace all constants in combination with corresponding variables and create new literals for new chain
				List<Literal> newChain = new LinkedList<Literal>();
				for (Literal literal : bestChain) {
					List<Term> newTerms = new LinkedList<Term>();
					for (Term term : literal.getAtomicSentence().getArgs()) {
						if (combination.contains(term.getSymbolicName())) {
							newTerms.add(new Variable(hash.get(term.getSymbolicName())));
						} else {
							newTerms.add(term);
						}
					}
					Literal newLiteral = new Literal(new Predicate(literal.getAtomicSentence().getSymbolicName(), newTerms), true);
					newChain.add(newLiteral);
				}
				// Add all previous literals
				List<Literal> allLiterals = new LinkedList<Literal>();
				allLiterals.addAll(literals);
				allLiterals.addAll(newChain);
				MyClause newClause = new MyClause(allLiterals);
				ClauseInfo newClauseInfo = new ClauseInfo(newClause, coverageEngine.getAllPosExamples().size(), coverageEngine.getAllNegExamples().size());
				
				// Calculate measure
				int posCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, newClauseInfo, posExamplesRelation, true);
				int negCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, newClauseInfo, negExamplesRelation, false);
				int truePositive = posCoveredCount;
				int falsePositive = negCoveredCount;
				double precision = ( (double)(truePositive) / (double)(truePositive+falsePositive) );
				
				if (precision > bestScore) {
					newBestChain = newChain;
					bestScore = precision;
				}
			}
			
			if (!DataDependenciesUtils.sameChain(bestChain, newBestChain)) {
				System.out.println("NEW CHAIN!!!");
				System.out.println(bestChain.toString());
				System.out.println(newBestChain.toString());
			}
		}
		
		return newBestChain;
	}
	
	private static int findFirstChainWithSameRelationCoverage(GenericDAO genericDAO,
			CoverageEngine coverageEngine, Schema schema, MyClause clause, 
			Relation relation, boolean[] originallyCovered,
			List<List<Literal>> allChains) {
		
		int lowerbound = 0;
		int upperbound = allChains.size() - 1;
		
		while(lowerbound != upperbound) {
			int n = (lowerbound + upperbound) / 2;
			
			// Create new clause with first n chains
			Set<Literal> literals = new LinkedHashSet<Literal>();
			literals.add(clause.getPositiveLiterals().get(0));
			for (int i = 0; i <= n; i++) {
				literals.addAll(allChains.get(i));
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

	private static int findChainWithBestScore(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, MyClause clause, List<Tuple> remainingPosExamples, Relation posExamplesRelation, Relation negExamplesRelation, List<List<Literal>> allChains, ClauseEvaluator evaluator, EvaluationFunctions.FUNCTION function) {
		// Create new clause with all chains
		Set<Literal> literals = new LinkedHashSet<Literal>();
		literals.add(clause.getPositiveLiterals().get(0));
		for (int j = 0; j < allChains.size(); j++) {
			literals.addAll(allChains.get(j));
		}
		MyClause newClause = new MyClause(literals);
		ClauseInfo newClauseInfo = new ClauseInfo(newClause, coverageEngine.getAllPosExamples().size(), coverageEngine.getAllNegExamples().size());
		
		// Compute score
		int bestPosition = allChains.size() - 1;
		double bestScore = evaluator.computeScore(genericDAO, coverageEngine, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, newClauseInfo, function);
		
		// Check if there's a shorter clause with better score
		// Starting from longer clause to reuse coverage tests information
		for (int i = allChains.size()-2; i >= 0; i--) {
			// Create new clause with first i chains
			literals.clear();
			literals.add(clause.getPositiveLiterals().get(0));
			for (int j = 0; j <= i; j++) {
				literals.addAll(allChains.get(j));
			}
			newClause = new MyClause(literals);
			newClauseInfo.setMoreGeneralClause(newClause);

			// Compute score
			double score = evaluator.computeScore(genericDAO, coverageEngine, schema, remainingPosExamples, posExamplesRelation, negExamplesRelation, newClauseInfo, EvaluationFunctions.FUNCTION.PRECISION);
			if (score >= bestScore) {
				bestPosition = i;
				bestScore = score;
			}
		}
		return bestPosition;
	}
	
	/*
	 * Given a clause with all chains before firstChain, find literal l in firstChain (all literals up to l are added to the clause) that provides best precision  
	 */
	private static int literalInChainWithBestPrecision(GenericDAO genericDAO, CoverageEngine coverageEngine, Schema schema, MyClause clause, Relation posExamplesRelation, Relation negExamplesRelation, List<List<Literal>> allChains, List<Literal> firstChain, int firstChainPosition) {
		// Create new clause with chains before firstChainPosition
		Set<Literal> literals = new LinkedHashSet<Literal>();
		literals.add(clause.getPositiveLiterals().get(0));
		for (int i = 0; i < firstChainPosition; i++) {
			literals.addAll(allChains.get(i));
		}
		
		// Find which literal in firstChain provides best precision
		int bestPosition = 0;
		double bestPrecision = 0;
		for (int j = 0; j < firstChain.size(); j++) {
			MyClause newClause = new MyClause(literals);
			for (int k = 0; k <= j; k++) {
				newClause.addLiteral(firstChain.get(k));
			}
			ClauseInfo newClauseInfo = new ClauseInfo(newClause, coverageEngine.getAllPosExamples().size(), coverageEngine.getAllNegExamples().size());
			
			// Calculate measure
			int posCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, newClauseInfo, posExamplesRelation, true);
			int negCoveredCount = coverageEngine.countCoveredExamplesFromRelation(genericDAO, schema, newClauseInfo, negExamplesRelation, false);
			int truePositive = posCoveredCount;
			int falsePositive = negCoveredCount;
			double precision = ( (double)(truePositive) / (double)(truePositive+falsePositive) );
			if (precision > bestPrecision) {
				bestPosition = j;
				bestPrecision = precision;
			}
		}
		
		return bestPosition;
	}
	
	/*
	 * Find supporting literals of given literal
	 * Supporting literals of literal i is the set of literals j, 1<=j<i, that are needed to connect the head's variables to literal i's variables
	 */
	private static List<List<Literal>> findHeadConnectingChains(MyClause clause, List<List<Literal>> allChains, List<Literal> chain, int chainPosition) {
		List<List<Literal>> supportingChains = new LinkedList<List<Literal>>();

		TreeNodeChains root = new TreeNodeChains(chain);
		Literal headLiteral = clause.getPositiveLiterals().get(0);
		
		// If literal already shares variables with head, nothing to do
		if (!shareVariables(headLiteral, chain)) {
			// For each chain before given chain, insert in tree and check if it connects to head literal
			for (int i = chainPosition - 1; i >= 0; i--) {
				List<Literal> chainAtPos = allChains.get(i);
				
				// Create new node with chain
				TreeNodeChains chainAtPosNode = new TreeNodeChains(chainAtPos);
				// Find potential parent using BFS
				TreeNodeChains chainAtPosNodeParent = findParent(root, chainAtPosNode);
				
				if (chainAtPosNodeParent != null) {
					// Set parent and children
					chainAtPosNode.setParent(chainAtPosNodeParent);
					chainAtPosNodeParent.addChild(chainAtPosNode);
					
					// If literals share variables, found 
					if (shareVariables(headLiteral, chainAtPos)) {
						TreeNodeChains currentNode = chainAtPosNode;
						
						while(!DataDependenciesUtils.sameChain(currentNode.getObject(), chain)) {
							supportingChains.add(currentNode.getObject());
							currentNode = currentNode.getParent();
						}
						break;
					}
				}
			}
		}
		
		return supportingChains;
	}
	
	/*
	 * Find parent of searchNode using breadth-first search
	 */
	private static TreeNodeChains findParent(TreeNodeChains root, TreeNodeChains searchNode) {
		TreeNodeChains parentNode = null;
		
		// BFS uses queue data structure
		Queue<TreeNodeChains> queue = new LinkedList<TreeNodeChains>();
		queue.add(root);
		while(!queue.isEmpty()) {
			TreeNodeChains node = queue.remove();
			// If node's literal shares variables with searchNode's literal, node will be parent 
			if (shareVariables(node.getObject(), searchNode.getObject())) {
				parentNode = node;
				break;
			}
			// Else add node's children to queue
			for (TreeNodeChains child : node.getChildren()) {
				queue.add(child);
			}
		}
		return parentNode;
	}

	/*
	 * Check if the two input literals share at least one variable
	 */
	private static boolean shareVariables(Literal literal, List<Literal> chain) {
		boolean shareVariables = false;
		for (Literal literal2 : chain) {
			for (Term term : literal.getAtomicSentence().getArgs()) {
				if (literal2.getAtomicSentence().getArgs().contains(term)) {
					shareVariables = true;
					break;
				}
			}
		}
		return shareVariables;
	}
	
	private static boolean shareVariables(List<Literal> chain1, List<Literal> chain2) {
		boolean shareVariables = false;
		for (Literal literal1 : chain1) {
			for (Literal literal2 : chain2) {
				for (Term term : literal1.getAtomicSentence().getArgs()) {
					if (literal2.getAtomicSentence().getArgs().contains(term)) {
						shareVariables = true;
						break;
					}
				}
				if (shareVariables) break;
			}
			if (shareVariables) break;
		}
		return shareVariables;
	}
}

