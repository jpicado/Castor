package castor.algorithms.transformations;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import aima.core.logic.fol.SubstVisitor;
import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import castor.hypotheses.MyClause;
import castor.utils.NumbersKeeper;
import castor.utils.TimeWatch;

public class ClauseTransformations {

	private static Unifier unifier = new Unifier();
	private static SubstVisitor substVisitor = new SubstVisitor();
	
	/*
	 * Minimize clause using theta-subsumption (same as homomorphism)
	 */
	public static MyClause minimize(MyClause clause) {
		TimeWatch tw = TimeWatch.start();
		for (Literal literalToRemove : clause.getNegativeLiterals()) {
			// Create new clause with head
			MyClause tempClause = new MyClause(clause.getPositiveLiterals());
			// Add all literals except literalToRemove
			for (Literal otherLiteral : clause.getNegativeLiterals()) {
				if (!literalToRemove.equals(otherLiteral)) {
					tempClause.addLiteral(otherLiteral);
				}
			}
			// If new clause subsumes original clause, start again with new clause
			if (subsumes(clause, tempClause, literalToRemove)) {
				NumbersKeeper.minimizationTime += tw.time();
				return minimize(tempClause);
			}
		}
		NumbersKeeper.minimizationTime += tw.time();
		return clause;
	}
	
	/*
	 * Check if subsumerClause subsumes subsumedClause, where subsumerClause = subsumedClause U {literalRemoved}
	 */
	private static boolean subsumes(MyClause subsumerClause, MyClause subsumedClause, Literal literalRemoved) {
		boolean subsumes  = false;
		
		// Only accept definite clauses
		if (!subsumerClause.isDefiniteClause() || !subsumedClause.isDefiniteClause()) {
			throw new IllegalArgumentException(
					"Only definite clauses (exactly one positive literal) are supported.");
		}
		
		Literal headLiteral = subsumerClause.getPositiveLiterals().get(0);
		
		// If heads are not equal, return false
		if (!headLiteral.equals(subsumedClause.getPositiveLiterals().get(0))) {
			//subsumes = false;
		} else {
			for (Literal literalOfSubsumed : subsumedClause.getNegativeLiterals()) {
				// Compute most general unifier of literal1 w.r.t. literal2
				Map<Variable, Term> theta = mostGeneralUnifierOfLiteralWrtLiteral(literalRemoved.getAtomicSentence(), literalOfSubsumed.getAtomicSentence());
				
				if (theta != null) {
				
					// It is not a valid substitution if it is substituting a head variable 
					boolean substitutingHeadVariable = false;
					for (Variable var : theta.keySet()) {
						if (headLiteral.getAtomicSentence().getArgs().contains(var)) {
							substitutingHeadVariable = true;
							break;
						}
					}
				
					if (!substitutingHeadVariable) {
						// Apply substitution to all literals in c1 and check if they are in c2
						boolean subset = true;
						for (Literal literalOfSubsumer : subsumerClause.getNegativeLiterals()) {
							Literal literalOfSubsumerTheta = substVisitor.subst(theta, literalOfSubsumer);
							if (!subsumedClause.getNegativeLiterals().contains(literalOfSubsumerTheta)) {
								subset = false;
								break;
							}
						}
						// If c1\theta subset c2, then c1 subsumes c2
						if (subset) {
							subsumes = true;
							break;
						}
					}
				}
			}
		}
		return subsumes;
	}
	
	/*
	 * Unifies two atoms and returns valid substitution only if substituted variables are in atom1 (cannot substitute from atom2)
	 * If return value != null, then atom1 theta-subsumes atom2
	 */
	public static Map<Variable, Term> mostGeneralUnifierOfLiteralWrtLiteral(AtomicSentence atom1, AtomicSentence atom2) {
		Map<Variable, Term> theta = unifier.unify(atom1, atom2);
		
		if (theta != null) {
			boolean isValid = true;
			for (Map.Entry<Variable, Term> entry : theta.entrySet()) {
			    if (!atom1.getArgs().contains(entry.getKey())) {
			    	isValid = false;
			    	break;
			    }
			}
			if (!isValid) {
				return null;
			}
		}
		return theta;
	}
	
	/*
	 * Reorder clause by patterns in descending order of number of shared variables in the pattern
	 * A pattern is a set of literals that share local variables
	 */
	public static MyClause reorder(MyClause clause) {
		// Get head variables
		List<Term> headVariables = clause.getPositiveLiterals().get(0).getAtomicSentence().getArgs();
		
		// Organize patterns in a map so that a pattern is in an entry where the key is the maximum number of shared variables in the pattern
		Map<Integer, List<Literal>> orderedPatternsMap = new TreeMap<Integer, List<Literal>>(Collections.reverseOrder());
		boolean[] addedToPattern = new boolean[clause.getNumberNegativeLiterals()];
		for (int i = 0; i < clause.getNumberNegativeLiterals(); i++) {
			Literal literal = clause.getNegativeLiterals().get(i);
			Set<Term> patternTerms = new HashSet<Term>();
			// Get other literals that share local variables with current literal, that have not been assigned
			List<Literal> sharingLiterals = new LinkedList<Literal>();
			sharingLiterals.add(literal);
			patternTerms.addAll(literal.getAtomicSentence().getArgs());
			int maxSharedVars = 0;
			for (int j = i + 1; j < clause.getNumberNegativeLiterals(); j++) {
				Literal otherLiteral = clause.getNegativeLiterals().get(j);
				if (!addedToPattern[j]) {
					int sharedVars = getNumberLocalVariablesInSet(otherLiteral, patternTerms, headVariables);
					if (sharedVars > 0) {
						sharingLiterals.add(otherLiteral);
						patternTerms.addAll(otherLiteral.getAtomicSentence().getArgs());
						maxSharedVars = Math.max(maxSharedVars, sharedVars);
						addedToPattern[j] = true;
					}
				}
			}
			// Add to map with key being the number of maximum shared variables
			if (!orderedPatternsMap.containsKey(maxSharedVars)) {
				orderedPatternsMap.put(maxSharedVars, new LinkedList<Literal>());
			}
			orderedPatternsMap.get(maxSharedVars).addAll(sharingLiterals);
		}
		
		// Compile all body literals into single list, patterns ordered by number of shared variables
		List<Literal> newClauseLiterals = new LinkedList<Literal>();
		for(Map.Entry<Integer, List<Literal>> entry : orderedPatternsMap.entrySet()) {
			newClauseLiterals.addAll(entry.getValue());
		}
		
		// Create new clause with head and ordered body literals
		MyClause newClause = new MyClause(newClauseLiterals);
		newClause.addLiteral(clause.getPositiveLiterals().get(0));
		return newClause;
	}
	
	private static int getNumberLocalVariablesInSet(Literal literal, Set<Term> terms, List<Term> headVariables) {
		int count = 0;
		for (Term term : literal.getAtomicSentence().getArgs()) {
			if (terms.contains(term) && !headVariables.contains(term)) {
				count++;
			}
		}
		return count;
	}
}
