package castor.language;


public class MatchingDependency{

	private String leftPredicateName;
	private int leftAttributeNumber;
	private String rightPredicateName;
	private int rightAttributeNumber;
	// If value < 1, use as relative value (actual distance is maxDistance * string length); otherwise, use as absolute value.
	private double maxDistance;
	
	public MatchingDependency(String leftPredicateName, int leftAttributeNumber, String rightPredicateName, int rightAttributeNumber, double maxDistance){
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
	
	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}
}
