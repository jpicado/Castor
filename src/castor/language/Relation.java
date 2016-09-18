package castor.language;

import java.util.List;

public class Relation {

	private String name;
	private List<String> attributeNames;
	
	public Relation(String name, List<String> attributeNames) {
		super();
		this.name = name;
		this.attributeNames = attributeNames;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getAttributeNames() {
		return attributeNames;
	}
}
