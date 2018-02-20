package castor.language;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Tuple {

	private List<Object> values;

	public Tuple(List<Object> values) {
		super();
		this.values = values;
	}

	public List<Object> getValues() {
		return values;
	}
	
	public List<String> getStringValues() {
		List<String> stringValues = new LinkedList<String>();
		
		for (Object val : values) {
			if (val == null)
				stringValues.add(null);
			else
				stringValues.add(val.toString());
		}
		
		return stringValues;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof Tuple)) {
			return false;
		}

		Tuple t2 = (Tuple) o;

		return this.values.equals(t2.values);
	}
	
	@Override
    public int hashCode() {
        return Objects.hash(this.values);
    }
	
	@Override
	public String toString() {
		return this.values.toString();
	}

}
