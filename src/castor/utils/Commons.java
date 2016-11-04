package castor.utils;

import castor.hypotheses.MyClause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Term;

public class Commons {

	public static final String ALIAS_PREFIX = "t";
	
	// Must match variable prefix used in bottom clause generator
	public static final String VARIABLE_PREFIX = "V";
	
	/*
	 * Checks if a term is a variable
	 * Must match variable prefix used in bottom clause generator
	 */
	public static boolean isVariable(String term) {
		if (term.startsWith(Commons.VARIABLE_PREFIX)) 
			return true;
		return false;
	}
	
	/*
	 * Check if term is a variable
	 */
	public static boolean isVariable(Term term) {
		if (term.getSymbolicName().startsWith(VARIABLE_PREFIX)) 
			return true;
		return false;
	}
	
	/*
	 * Creates a new variable name given the variable counter
	 */
	public static String newVariable(int varCounter) {
		return Commons.VARIABLE_PREFIX + varCounter;
	}
	
	/*
	 * Creates a new variable given the variable counter
	 */
	public static String newAlias(int tableCounter) {
		return ALIAS_PREFIX + tableCounter;
	}
	
	/*
	 * Gets the maximum value of any variable in the clause
	 */
	public static int getMaxVarValue(MyClause clause) {
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
}
