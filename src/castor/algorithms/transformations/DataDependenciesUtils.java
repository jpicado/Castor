package castor.algorithms.transformations;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Term;
import castor.hypotheses.MyClause;
import castor.language.InclusionDependency;
import castor.language.Schema;

public class DataDependenciesUtils {

	public static List<Literal> findLiteralsInInclusionChain(Schema schema, MyClause clause, Literal literal) {
		return DataDependenciesUtils.findLiteralsInInclusionChain(schema, clause.getNegativeLiterals(), literal, new HashSet<String>());
	}
	
	/*
	 * Find all literals in the same inclusion chain as the given literal. Recursive.
	 */
	private static List<Literal> findLiteralsInInclusionChain(Schema schema, List<Literal> clauseLiterals, Literal currentLiteral, Set<String> seenPredicates) {
		List<Literal> outputLiterals = new LinkedList<Literal>();
		outputLiterals.add(currentLiteral);
		
		if (!seenPredicates.contains(currentLiteral.getAtomicSentence().getSymbolicName())
				&& schema.getInclusionDependencies() != null
				&& schema.getInclusionDependencies().containsKey(currentLiteral.getAtomicSentence().getSymbolicName())) {
			
			for (InclusionDependency ind : schema.getInclusionDependencies().get(currentLiteral.getAtomicSentence().getSymbolicName())) {
				Term leftIndTerm = currentLiteral.getAtomicSentence().getArgs().get(ind.getLeftAttributeNumber());
				
				if (!seenPredicates.contains(ind.getRightPredicateName())) {
					// Follow IND
					for (int j = 0; j < clauseLiterals.size(); j++) {
						Literal otherLiteral = clauseLiterals.get(j);
						String otherLiteralPredicateName = otherLiteral.getAtomicSentence().getSymbolicName();
						
						if (otherLiteralPredicateName.equals(ind.getRightPredicateName())) {
							Term rightIndTerm = otherLiteral.getAtomicSentence().getArgs().get(ind.getRightAttributeNumber());
	
							if (leftIndTerm.equals(rightIndTerm)) {
								// Add current predicate to seen list
								seenPredicates.add(currentLiteral.getAtomicSentence().getSymbolicName());
								
								// Follow chain
								outputLiterals.addAll(findLiteralsInInclusionChain(schema, clauseLiterals, otherLiteral, seenPredicates));
							}
						}
					}
				}
			}
		}

		return outputLiterals;
	}
	
	/*
	 * Find all literals in the same inclusion chain as the given literal. Chain is followed until there are no changes.
	 */
	public static List<Literal> findLiteralsInInclusionChainOLD(Schema schema, MyClause clause, Literal literal) {
		List<Literal> literals = new LinkedList<Literal>();
		literals.add(literal);
		return DataDependenciesUtils.findLiteralsInInclusionChain(schema, clause, literals);
	}
	
	/*
	 * Find all literals in the same inclusion chain as any literal in the given literals. Chain is followed until there are no changes.
	 * Does not allow cyclic chains.
	 */
	public static List<Literal> findLiteralsInInclusionChain(Schema schema, MyClause clause, List<Literal> literals) {
		// Keep track of predicates names added to chain
		Set<String> addedPredicateNames = new HashSet<String>();
		for (Literal literal : literals) {
			addedPredicateNames.add(literal.getAtomicSentence().getSymbolicName());
		}
		
		// Loop until list of literals is not modified
		int previousLength = 0;
		while (previousLength < literals.size()) {
			previousLength = literals.size();
			
			Set<String> iterationAddedPredicateNames = new HashSet<String>();
			List<Literal> iterationAddedLiterals = new LinkedList<Literal>();
			
			for (int i = 0; i < literals.size(); i++) {
				Literal leftLiteral = literals.get(i);
				String leftLiteralPredicateName = leftLiteral.getAtomicSentence().getSymbolicName();
				
				if (schema.getInclusionDependencies().containsKey(leftLiteralPredicateName)) {				
					for (InclusionDependency ind : schema.getInclusionDependencies().get(leftLiteralPredicateName)) {
						// If right predicate in IND has not been added, process IND
						if (!addedPredicateNames.contains(ind.getRightPredicateName())) {
							Term leftIndTerm = leftLiteral.getAtomicSentence().getArgs().get(ind.getLeftAttributeNumber());
							
							// If IND holds with other literal, add that literal to literals in inclusion chain 
							for (int j = 0; j < clause.getNegativeLiterals().size(); j++) {
								Literal otherLiteral = clause.getNegativeLiterals().get(j);
								String otherLiteralPredicateName = otherLiteral.getAtomicSentence().getSymbolicName();
			
								if (otherLiteralPredicateName.equals(ind.getRightPredicateName())) {
									Term rightIndTerm = otherLiteral.getAtomicSentence().getArgs().get(ind.getRightAttributeNumber());
			
									if (leftIndTerm.equals(rightIndTerm) &&
											!literals.contains(otherLiteral) &&
											!iterationAddedLiterals.contains(otherLiteral)) {
										iterationAddedLiterals.add(otherLiteral);
										iterationAddedPredicateNames.add(otherLiteral.getAtomicSentence().getSymbolicName());
									}
								}
							}
						}
					}
				}
			}
			
			addedPredicateNames.addAll(iterationAddedPredicateNames);
			literals.addAll(iterationAddedLiterals);
		}
		return literals;
	}
	
	/*
	 * Find all inclusion chains (instances of inclusion classes) in a clause
	 */
	// This implementation had a bug, would run forever
	public static List<List<Literal>> findAllInclusionChains(Schema schema, MyClause clause) {
		List<List<Literal>> allChains = new LinkedList<List<Literal>>();
		
		for (Literal literal : clause.getNegativeLiterals()) {
			// Create chain containing literal
			List<Literal> currentChain = new LinkedList<Literal>();
			currentChain.add(literal);
			// Add literal to used predicates (a chain cannot have another literal with same predicate name)
			Set<String> usedPredicates = new HashSet<String>();
			usedPredicates.add(literal.getAtomicSentence().getSymbolicName());
			DataDependenciesUtils.findInclusionChainsAux(schema, clause, allChains, currentChain, usedPredicates, 0);
		}
		
		return allChains;
	}
	public static List<List<Literal>> findAllInclusionChains2(Schema schema, MyClause clause) {
		List<List<Literal>> allChains = new LinkedList<List<Literal>>();
		
		for (Literal literal : clause.getNegativeLiterals()) {
			// Add literal to used predicates (a chain cannot have another literal with same predicate name)
			Set<String> usedPredicates = new HashSet<String>();
			usedPredicates.add(literal.getAtomicSentence().getSymbolicName());
			List<List<Literal>> newChains = DataDependenciesUtils.findInclusionChainsAux2(schema, clause, literal, usedPredicates);
			
			for (List<Literal> newChain : newChains) {
				boolean repeatedChain = false;
				for(List<Literal> chain : allChains) {
					if (sameChain(newChain, chain)) {
						repeatedChain = true;
						break;
					}
	            }
				if (!repeatedChain) {
					allChains.add(newChain);
				}
			}
		}
		
		return allChains;
	}
	
	/*
	 * Auxiliary predicate to find all inclusion class instances
	 */
	// This implementation had a bug, would run forever
	private static void findInclusionChainsAux(Schema schema, MyClause clause, List<List<Literal>> chains, List<Literal> currentChain, Set<String> usedPredicates, int depth) {
		boolean chainExtended = false;

		for (Literal literal : currentChain) {
			String predicate = literal.getAtomicSentence().getSymbolicName();
			
			if (schema.getInclusionDependencies().containsKey(predicate)) {
				for (InclusionDependency ind : schema.getInclusionDependencies().get(predicate)) {
					
					if (!usedPredicates.contains(ind.getRightPredicateName())) {
						Term leftIndTerm = literal.getAtomicSentence().getArgs().get(ind.getLeftAttributeNumber());
						
						// If IND holds with other literal, add that literal to literals in inclusion chain
						for (int j = 0; j < clause.getNegativeLiterals().size(); j++) {
							Literal otherLiteral = clause.getNegativeLiterals().get(j);
							String otherLiteralPredicateName = otherLiteral.getAtomicSentence().getSymbolicName();
		
							if (otherLiteralPredicateName.equals(ind.getRightPredicateName())) {
								Term rightIndTerm = otherLiteral.getAtomicSentence().getArgs().get(ind.getRightAttributeNumber());
		
								if (leftIndTerm.equals(rightIndTerm)) {
									List<Literal> newChain = new LinkedList<Literal>(currentChain);
									Set<String> newUsedPredicates = new HashSet<String>(usedPredicates);
									
									newChain.add(otherLiteral);
									newUsedPredicates.add(otherLiteralPredicateName);
									
									DataDependenciesUtils.findInclusionChainsAux(schema, clause, chains, newChain, newUsedPredicates, depth+1);
									chainExtended = true;
								}
							}
						}
					}
				}
			}
		}
		
		if (!chainExtended) {
			// Cannot make chain longer
			// Add to list of chains if there is no other chain with exactly the same literals (order does not matter)
			boolean repeatedChain = false;
			for(List<Literal> chain : chains) {
				if (sameChain(currentChain, chain)) {
					repeatedChain = true;
					break;
				}
            }
			if (!repeatedChain) {
				chains.add(currentChain);
			}
		}
	}
	
	/*
	 * Recursive function that finds all instances of inclusion chains starting at literal, without using predicates in usedPredicates
	 */
	private static List<List<Literal>> findInclusionChainsAux2(Schema schema, MyClause clause, Literal literal, Set<String> usedPredicates) {
		List<List<Literal>> chains = new LinkedList<List<Literal>>();
		String predicate = literal.getAtomicSentence().getSymbolicName();
		boolean chainExtended = false;
		
		if (schema.getInclusionDependencies() != null
				&& schema.getInclusionDependencies().containsKey(predicate)) {
			
			// List of chains generated by each IND
			List<List<List<Literal>>> chainsByInds = new LinkedList<List<List<Literal>>>();
			for (InclusionDependency ind : schema.getInclusionDependencies().get(predicate)) {
				
				// List of chains generated by the current IND
				List<List<Literal>> chainsByCurrentInd = new LinkedList<List<Literal>>();
				
				if (!usedPredicates.contains(ind.getRightPredicateName())) {
					Term leftIndTerm = literal.getAtomicSentence().getArgs().get(ind.getLeftAttributeNumber());
					
					// If IND holds with other literal, add that literal to literals in inclusion chain
					for (int j = 0; j < clause.getNegativeLiterals().size(); j++) {
						Literal otherLiteral = clause.getNegativeLiterals().get(j);
						String otherLiteralPredicateName = otherLiteral.getAtomicSentence().getSymbolicName();
	
						if (otherLiteralPredicateName.equals(ind.getRightPredicateName())) {
							Term rightIndTerm = otherLiteral.getAtomicSentence().getArgs().get(ind.getRightAttributeNumber());
	
							if (leftIndTerm.equals(rightIndTerm)) {
								Set<String> newUsedPredicates = new HashSet<String>(usedPredicates);
								newUsedPredicates.add(otherLiteralPredicateName);
								
								List<List<Literal>> newChains = DataDependenciesUtils.findInclusionChainsAux2(schema, clause, otherLiteral, newUsedPredicates);
								chainsByCurrentInd.addAll(newChains);
							}
						}
					}
				}
				
				if (chainsByCurrentInd.size() > 0) {
					chainsByInds.add(chainsByCurrentInd);
				}
			}
			
			// Compute all combinations of chains in chainsByInds
			if (chainsByInds.size() > 0) {
				List<List<Literal>> combinedChains = findAllCombinations(chainsByInds, 0);
				if (combinedChains.size() > 0) {
					chainExtended = true;
					for (List<Literal> chain : combinedChains) {
						chain.add(literal);
						chains.add(chain);
					}
				}
			}
		}
		
		// If there were no changes, create a chain from current literal
		if (!chainExtended) {
			List<Literal> newChain = new LinkedList<Literal>();
			newChain.add(literal);
			chains.add(newChain);
		}
		
		return chains;
	}
	
	/*
	 * Recursive function that creates all combinations of chains
	 */
	private static List<List<Literal>> findAllCombinations(List<List<List<Literal>>> chainsByInds, int current) {
		List<List<Literal>> chains = new LinkedList<List<Literal>>();
		
		if (current == chainsByInds.size()-1) {
			chains.addAll(chainsByInds.get(current));
		} else {
			for (List<Literal> chainsByInd : chainsByInds.get(current)) {
				List<List<Literal>> moreChains = findAllCombinations(chainsByInds, current+1);
				for (List<Literal> chain : moreChains) {
					List<Literal> newChain = new LinkedList<Literal>(chainsByInd);
					newChain.addAll(chain);
					chains.add(newChain);
				}
			}
		}
		
		return chains;
	}

	public static boolean sameChain(List<Literal> chain1, List<Literal> chain2) {
		return chain1.containsAll(chain2) && chain2.containsAll(chain1);
	}
}
