package castor.language;

import java.util.List;

public class Tuple {

	private List<String> values;

	public Tuple(List<String> values) {
		super();
		this.values = values;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
}
