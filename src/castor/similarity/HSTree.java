package castor.similarity;

import java.util.List;
import java.util.Map;

public class HSTree {

	private List<String> strings;
	private Map<Integer,Map<Integer,Map<Integer,Map<String,List<Integer>>>>> invertedIndex;

	public HSTree(List<String> strings, Map<Integer,Map<Integer,Map<Integer,Map<String,List<Integer>>>>> invertedIndex) {
		super();
		this.strings = strings;
		this.invertedIndex = invertedIndex;
	}

	public List<String> getStrings() {
		return strings;
	}
	
	public void setStrings(List<String> strings) {
		this.strings = strings;
	}
	
	public Map<Integer,Map<Integer,Map<Integer,Map<String,List<Integer>>>>> getInvertedIndex() {
		return invertedIndex;
	}

	public void setInvertedIndex(Map<Integer,Map<Integer,Map<Integer,Map<String,List<Integer>>>>> invertedIndex) {
		this.invertedIndex = invertedIndex;
	}	
}
