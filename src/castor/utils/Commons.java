package castor.utils;

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
}
