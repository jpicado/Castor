package castor.mappings;

import java.util.LinkedList;
import java.util.List;

import aima.core.logic.fol.kb.data.Literal;
import castor.hypotheses.ClauseInfo;
import castor.hypotheses.MyClause;
import castor.wrappers.ClauseAsString;

public class MyClauseToClauseAsString {

	public static ClauseAsString parseClause(MyClause clause) {
		String head = clause.getPositiveLiterals().get(0).getAtomicSentence().toString();
		List<String> body = new LinkedList<String>();
		for (Literal literal : clause.getNegativeLiterals()) {
			String literalString = literal.getAtomicSentence().toString();
			literalString = literalString.replaceAll("\"x01\"", "1");
			literalString = literalString.replaceAll("\"x00\"", "0");
			body.add(literalString);
		}
		return new ClauseAsString(head, body);
	}
	
	public static List<ClauseAsString> parseDefinition(List<ClauseInfo> definition) {
		List<ClauseAsString> definitionAsString = new LinkedList<ClauseAsString>();
		for (ClauseInfo clauseInfo : definition) {
			definitionAsString.add(parseClause(clauseInfo.getClause()));
		}
		return definitionAsString;
	}
}
