package castor.clients;

import java.util.LinkedList;
import java.util.List;

import ida.ilp.logic.Clause;
import ida.ilp.logic.subsumption.Matching;

public class IDATest {

	public static void main(String[] args) {

		List<Clause> ex = new LinkedList<Clause>();
		Clause e = Clause.parse("!target(\"1\"), successor(\"1\", \"2\"), successor(\"0\", \"1\"), zero(\"0\"), successor(\"2\", \"3\"), target(\"2\"), target(\"0\"), successor(\"3\", \"4\")");
		ex.add(e);
		Matching matching = new Matching(ex);
		
		boolean[] undecided = new boolean[ex.size()];
		for (int i = 0; i < undecided.length; i++) {
			undecided[i] = true;
		}
		
		Clause h = Clause.parse("!target(V0), zero(V0), successor(V0, V1), successor(V1, V2), successor(V2, V3), target(V2)");
		int[] result = matching.evaluateOnExamples(h, undecided);
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}
		
		System.out.println(Matching.YES);
		System.out.println(Matching.NO);
		System.out.println(Matching.UNDECIDED);
	}
}
