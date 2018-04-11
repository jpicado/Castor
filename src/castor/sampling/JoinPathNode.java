package castor.sampling;

public class JoinPathNode {

	private String leftJoinRelation;
	private int leftJoinAttribute;
	private String rightJoinRelation;
	private int rightJoinAttribute;
	
	public JoinPathNode(String leftJoinRelation, int leftJoinAttribute, String rightJoinRelation,
			int rightJoinAttribute) {
		super();
		this.leftJoinRelation = leftJoinRelation;
		this.leftJoinAttribute = leftJoinAttribute;
		this.rightJoinRelation = rightJoinRelation;
		this.rightJoinAttribute = rightJoinAttribute;
	}
	public String getLeftJoinRelation() {
		return leftJoinRelation;
	}
	public int getLeftJoinAttribute() {
		return leftJoinAttribute;
	}
	public String getRightJoinRelation() {
		return rightJoinRelation;
	}
	public int getRightJoinAttribute() {
		return rightJoinAttribute;
	}
	
	@Override
	public String toString() {
		return leftJoinRelation + "[" + leftJoinAttribute + "]-" + rightJoinRelation + "[" + rightJoinAttribute + "]";
	}
}
