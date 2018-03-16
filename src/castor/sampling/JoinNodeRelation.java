package castor.sampling;

import java.util.List;
import java.util.Objects;

public class JoinNodeRelation {

	private String relation;
	private List<String> constantAttributeNames;
	private List<String> constantAttributeValues;
	
	public JoinNodeRelation(String relation) {
		super();
		this.relation = relation;
	}

	public JoinNodeRelation(String relation, List<String> constantAttributeNames,
			List<String> constantAttributeValues) {
		super();
		this.relation = relation;
		this.constantAttributeNames = constantAttributeNames;
		this.constantAttributeValues = constantAttributeValues;
	}

	public String getRelation() {
		return relation;
	}

	public List<String> getConstantAttributePositions() {
		return constantAttributeNames;
	}

	public List<String> getConstantAttributeValues() {
		return constantAttributeValues;
	}
	
	@Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof JoinNodeRelation)) {
            return false;
        }
        JoinNodeRelation joinNodeRelation = (JoinNodeRelation) o;
        return Objects.equals(relation, joinNodeRelation.relation) &&
                Objects.equals(constantAttributeNames, joinNodeRelation.constantAttributeNames) &&
                Objects.equals(constantAttributeValues, joinNodeRelation.constantAttributeValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relation, constantAttributeNames, constantAttributeValues);
    }
}
