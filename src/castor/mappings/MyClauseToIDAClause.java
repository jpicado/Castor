package castor.mappings;

import castor.hypotheses.MyClause;
import ida.ilp.logic.Clause;

public class MyClauseToIDAClause {

	public static final String NEGATE_SYMBOL = "";
	
	public static Clause parseClause(MyClause clause) {
		return Clause.parse(clause.toString2(NEGATE_SYMBOL).replaceAll("'", ""));
	}
}
