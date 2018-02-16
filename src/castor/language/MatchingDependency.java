package castor.language;


public class MatchingDependency{

	private String leftPredicateName;
	private int leftAttributeNumber;
	private String rightPredicateName;
	private int rightAttributeNumber;
	private int maxDistance;
	
	public MatchingDependency(String leftPredicateName, int leftAttributeNumber, String rightPredicateName, int rightAttributeNumber, int maxDistance){
		super();
		this.leftPredicateName = leftPredicateName;
		this.leftAttributeNumber = leftAttributeNumber;
		this.rightPredicateName = rightPredicateName;
		this.rightAttributeNumber = rightAttributeNumber;
		this.maxDistance = maxDistance;
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
	
	public int getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(int maxDistance) {
		this.maxDistance = maxDistance;
	}
}
