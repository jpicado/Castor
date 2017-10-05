package castor.utils;

import java.util.List;
import java.util.Map.Entry;

import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import castor.hypotheses.ClauseInfo;
import castor.hypotheses.MyClause;

public class Formatter {

	public static String prettyPrint(List<Clause> hypothesis) {
		StringBuilder sb = new StringBuilder();
		for(Clause clause : hypothesis) {
			sb.append(prettyPrint(clause) + "\n");
		}
		return sb.toString();
	}
	
	public static String prettyPrint(Clause clause) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < clause.getPositiveLiterals().size(); i++) {
			Literal literal = clause.getPositiveLiterals().get(i);
			sb.append(literal.getAtomicSentence());
			if (i < clause.getPositiveLiterals().size() - 1) {
				sb.append(", ");
			}
		}
		
		sb.append(" :-\n\t");
		
		for (int i = 0; i < clause.getNegativeLiterals().size(); i++) {
			Literal literal = clause.getNegativeLiterals().get(i);
			sb.append(literal.getAtomicSentence());
			if (i < clause.getNegativeLiterals().size() - 1) {
				sb.append(", ");
			}
		}
		
		sb.append(".");
		return sb.toString();
	}
	
	public static String prettyPrint(MyClause clause) {
		StringBuilder sb = new StringBuilder();
		sb.append(prettyPrintAux(clause));
		sb.append(".");
		return sb.toString();
	}
	
	public static String prettyPrintAux(MyClause clause) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < clause.getPositiveLiterals().size(); i++) {
			Literal literal = clause.getPositiveLiterals().get(i);
			sb.append(literal.getAtomicSentence());
			if (i < clause.getPositiveLiterals().size() - 1) {
				sb.append(", ");
			}
		}
		
		sb.append(" :-\n\t");
		
		for (int i = 0; i < clause.getNegativeLiterals().size(); i++) {
			Literal literal = clause.getNegativeLiterals().get(i);
			sb.append(literal.getAtomicSentence());
			if (i < clause.getNegativeLiterals().size() - 1) {
				sb.append(", ");
			}
		}
		
		return sb.toString();
	}
	
	public static String prettyPrint(ClauseInfo clauseInfo) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(prettyPrintAux(clauseInfo.getClause()));
		
		for (Entry<Variable,Term> entry : clauseInfo.getHeadSubstitutions().entrySet()) {
			sb.append(", ");
			sb.append(entry.getKey() + "=" + entry.getValue());
		}
		
		sb.append(".");
		return sb.toString();
	}
}
