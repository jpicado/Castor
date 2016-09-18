package castor.algorithms.clauseevaluation;

import java.util.HashMap;
import java.util.Map;

public class ExamplesInfo {

	private Map<String, ExampleInfo> posExamplesInfo;
	private Map<String, ExampleInfo> negExamplesInfo;
	
	public ExamplesInfo() {
		super();
		this.posExamplesInfo = new HashMap<String, ExampleInfo>();
		this.negExamplesInfo = new HashMap<String, ExampleInfo>();
	}

	public ExamplesInfo(Map<String, ExampleInfo> posExamplesInfo,
			Map<String, ExampleInfo> negExamplesInfo) {
		super();
		this.posExamplesInfo = posExamplesInfo;
		this.negExamplesInfo = negExamplesInfo;
	}

	public Map<String, ExampleInfo> getPosExamplesInfo() {
		return posExamplesInfo;
	}

	public Map<String, ExampleInfo> getNegExamplesInfo() {
		return negExamplesInfo;
	}
}
