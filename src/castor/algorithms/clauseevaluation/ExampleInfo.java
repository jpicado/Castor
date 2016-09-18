package castor.algorithms.clauseevaluation;

public class ExampleInfo {
	private boolean evaluated;
	private boolean covered;
	
	public ExampleInfo() {
		super();
		this.evaluated = false;
		this.covered = false;
	}

	public ExampleInfo(boolean evaluated, boolean covered) {
		super();
		this.evaluated = evaluated;
		this.covered = covered;
	}

	public boolean isEvaluated() {
		return evaluated;
	}

	public void setEvaluated(boolean evaluated) {
		this.evaluated = evaluated;
	}

	public boolean isCovered() {
		return covered;
	}

	public void setCovered(boolean covered) {
		this.covered = covered;
	}
}
