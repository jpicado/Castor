package castor.similarity;

import java.util.Objects;

public class SimilarValue {

	private String value;
	private int distance;
	
	public SimilarValue(String value, int distance) {
		super();
		this.value = value;
		this.distance = distance;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	@Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof SimilarValue)) {
            return false;
        }
        SimilarValue similarValue = (SimilarValue) o;
        return distance == similarValue.distance &&
                Objects.equals(value, similarValue.value);
    }
	
	@Override
    public int hashCode() {
        return Objects.hash(value, distance);
    }
}
