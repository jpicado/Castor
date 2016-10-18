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

	/*
	 * Find all literals in the same inclusion chain as the given literal. Chain is followed until there are no changes.
	 */
	public static List<Literal> findLiteralsInInclusionChain(Schema schema, MyClause clause, Literal literal) {
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
	public static List<List<Literal>> findAllInclusionChainsOLD(Schema schema, MyClause clause) {
		List<List<Literal>> allChains = new LinkedList<List<Literal>>();
		
		for (Literal literal : clause.getNegativeLiterals()) {
			// Create chain containing literal
			List<Literal> currentChain = new LinkedList<Literal>();
			currentChain.add(literal);
			// Add literal to used predicates (a chain cannot have another literal with same predicate name)
			Set<String> usedPredicates = new HashSet<String>();
			usedPredicates.add(literal.getAtomicSentence().getSymbolicName());
			
			DataDependenciesUtils.findInclusionChainsAuxOLD(schema, clause, allChains, currentChain, usedPredicates, literal);
		}
		
		return allChains;
	}
	// This implementation is faster
	public static List<List<Literal>> findAllInclusionChains(Schema schema, MyClause clause) {
		List<List<Literal>> allChains = new LinkedList<List<Literal>>();
		
		for (Literal literal : clause.getNegativeLiterals()) {
			// Create chain containing literal
			List<Literal> currentChain = new LinkedList<Literal>();
			currentChain.add(literal);
			// Add literal to used predicates (a chain cannot have another literal with same predicate name)
			Set<String> usedPredicates = new HashSet<String>();
			usedPredicates.add(literal.getAtomicSentence().getSymbolicName());
//			System.out.println("Calling aux for:"+literal.getAtomicSentence().getSymbolicName());
			DataDependenciesUtils.findInclusionChainsAux(schema, clause, allChains, currentChain, usedPredicates);
		}
		
		return allChains;
	}
	
	/*
	 * Find all inclusion class instances in a clause given a literal
	 */
	public static List<List<Literal>> findInclusionChains(Schema schema, MyClause clause, Literal literal) {
		List<List<Literal>> chains = new LinkedList<List<Literal>>();
		// Create chain containing literal
		List<Literal> currentChain = new LinkedList<Literal>();
		currentChain.add(literal);
		// Add literal to used predicates (a chain cannot have another literal with same predicate name)
		Set<String> usedPredicates = new HashSet<String>();
		usedPredicates.add(literal.getAtomicSentence().getSymbolicName());
		
		DataDependenciesUtils.findInclusionChainsAuxOLD(schema, clause, chains, currentChain, usedPredicates, literal);
		return chains;
	}
	
	/*
	 * Auxiliary predicate to find all inclusion class instances
	 */
	private static void findInclusionChainsAuxOLD(Schema schema, MyClause clause, List<List<Literal>> chains, List<Literal> currentChain, Set<String> usedPredicates, Literal currentLiteral) {
		boolean chainExtended = false;
		String currentPredicate = currentLiteral.getAtomicSentence().getSymbolicName();
		if (schema.getInclusionDependencies().containsKey(currentPredicate)) {
			for (InclusionDependency ind : schema.getInclusionDependencies().get(currentPredicate)) {
				// If right predicate in IND has not been added, process IND
				if (!usedPredicates.contains(ind.getRightPredicateName())) {
					Term leftIndTerm = currentLiteral.getAtomicSentence().getArgs().get(ind.getLeftAttributeNumber());
					
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
								
								DataDependenciesUtils.findInclusionChainsAuxOLD(schema, clause, chains, newChain, newUsedPredicates, otherLiteral);
								chainExtended = true;
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
	// This implementation is faster
	private static void findInclusionChainsAux(Schema schema, MyClause clause, List<List<Literal>> chains, List<Literal> currentChain, Set<String> usedPredicates) {
		boolean chainExtended = false;
//		System.out.println("recursive call");
//		System.out.println(usedPredicates.toString());
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
									
									DataDependenciesUtils.findInclusionChainsAux(schema, clause, chains, newChain, newUsedPredicates);
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
	
	public static boolean sameChain(List<Literal> chain1, List<Literal> chain2) {
		return chain1.containsAll(chain2) && chain2.containsAll(chain1);
	}
}
