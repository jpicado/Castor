package castor.hypotheses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

public class ClauseInfo {

	private MyClause clause;
	private Double score;
	
	// Store which examples are covered by clause
	private boolean[] posExamplesCovered;
	private boolean[] negExamplesCovered;
	
	// Store which examples have been evaluated for this clause
	private boolean[] posExamplesEvaluated;
	private boolean[] negExamplesEvaluated;
	
	// Store head substitutions
	Map<Variable, Term> headSubstitutions;

	public ClauseInfo(MyClause clause, int nPosExamples, int nNegExamples) {
		super();
		this.clause = clause;
		this.score = null;
		
		this.posExamplesCovered = new boolean[nPosExamples];
		this.negExamplesCovered = new boolean[nNegExamples];
		
		this.posExamplesEvaluated = new boolean[nPosExamples];
		this.negExamplesEvaluated = new boolean[nNegExamples];
		
		this.headSubstitutions = new HashMap<Variable, Term>();
	}
	
	public ClauseInfo(MyClause clause, boolean[] posExamplesCovered, boolean[] negExamplesCovered, boolean[] posExamplesEvaluated, boolean[] negExamplesEvaluated) {
		super();
		this.clause = clause;
		this.score = null;
		
		this.posExamplesCovered = posExamplesCovered;
		this.negExamplesCovered = negExamplesCovered;
		
		this.posExamplesEvaluated = posExamplesEvaluated;
		this.negExamplesEvaluated = negExamplesEvaluated;
		
		this.headSubstitutions = new HashMap<Variable, Term>();
	}
	
	public MyClause getClause() {
		return clause;
	}
	
	public void setMoreGeneralClause(MyClause clause) {
		this.clause = clause;
		this.score = null;
		
		// Covered examples stay the same because a more general clause still covers the same examples
		
		// Update examples evaluated; a more general clause can cover new examples
		for (int i = 0; i < posExamplesCovered.length; i++) {
			if (posExamplesEvaluated[i] && !posExamplesCovered[i]) {
				posExamplesEvaluated[i] = false;
			}
		}
		for (int i = 0; i < negExamplesCovered.length; i++) {
			if (negExamplesEvaluated[i] && !negExamplesCovered[i]) {
				negExamplesEvaluated[i] = false;
			}
		}
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public boolean[] getPosExamplesCovered() {
		return posExamplesCovered;
	}

	public boolean[] getNegExamplesCovered() {
		return negExamplesCovered;
	}

	public void setPosExamplesCovered(boolean[] posExamplesCovered) {
		this.posExamplesCovered = posExamplesCovered;
	}
	
	public void setNegExamplesCovered(boolean[] negExamplesCovered) {
		this.negExamplesCovered = negExamplesCovered;
	}
	
	public boolean[] getPosExamplesEvaluated() {
		return posExamplesEvaluated;
	}

	public boolean[] getNegExamplesEvaluated() {
		return negExamplesEvaluated;
	}

	public void setPosExamplesEvaluated(boolean[] posExamplesEvaluated) {
		this.posExamplesEvaluated = posExamplesEvaluated;
	}

	public void setNegExamplesEvaluated(boolean[] negExamplesEvaluated) {
		this.negExamplesEvaluated = negExamplesEvaluated;
	}

	public void resetCoveredExamples(int nPosExamples, int nNegExamples) {
		this.score = null;
		
		this.posExamplesCovered = new boolean[nPosExamples];
		this.negExamplesCovered = new boolean[nNegExamples];
		Arrays.fill(this.posExamplesCovered, false);
		Arrays.fill(this.negExamplesCovered, false);
		
		this.posExamplesEvaluated = new boolean[nPosExamples];
		this.negExamplesEvaluated = new boolean[nNegExamples];
		Arrays.fill(this.posExamplesEvaluated, false);
		Arrays.fill(this.negExamplesEvaluated, false);
	}

	public Map<Variable, Term> getHeadSubstitutions() {
		return headSubstitutions;
	}
}
