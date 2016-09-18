package castor.language;


public class InclusionDependency{

	private String leftPredicateName;
	private int leftAttributeNumber;
	private String rightPredicateName;
	private int rightAttributeNumber;
	
	public InclusionDependency(String leftPredicateName, int leftAttributeNumber, String rightPredicateName, int rightAttributeNumber){
		super();
		this.leftPredicateName = leftPredicateName;
		this.leftAttributeNumber = leftAttributeNumber;
		this.rightPredicateName = rightPredicateName;
		this.rightAttributeNumber = rightAttributeNumber;
	}
	
	public String getLeftPredicateName(){
		return leftPredicateName;
	}
	
	public String getRightPredicateName(){
		return rightPredicateName;
	}
	
	public int getLeftAttributeNumber(){
		return leftAttributeNumber;
	}
	
	public int getRightAttributeNumber(){
		return rightAttributeNumber;
	}
	
	public void print(){
		System.out.println("\t" + leftPredicateName + "--" + leftAttributeNumber + "\t" + rightPredicateName + "--" + rightAttributeNumber);
	}	
}
