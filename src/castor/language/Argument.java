package castor.language;

public class Argument {

	private String type;//type of the att
	private IdentifierType identifierType;//input, output, cte
	
	public Argument(String type, IdentifierType identifierType) {
		super();
		this.type = type;
		this.identifierType = identifierType;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public IdentifierType getIdentifierType() {
		return identifierType;
	}
	
	public void printArg(){
		System.out.println("\t" + type + "-->" + "\t" + identifierType);
	}
}
