package castor.sampling;

public class JoinPathNode {

	private JoinNodeRelation leftJoinRelation;
	private int leftJoinAttribute;
	private JoinNodeRelation rightJoinRelation;
	private int rightJoinAttribute;
	
	public JoinPathNode(JoinNodeRelation leftJoinRelation, int leftJoinAttribute, JoinNodeRelation rightJoinRelation,
			int rightJoinAttribute) {
		super();
		this.leftJoinRelation = leftJoinRelation;
		this.leftJoinAttribute = leftJoinAttribute;
		this.rightJoinRelation = rightJoinRelation;
		this.rightJoinAttribute = rightJoinAttribute;
	}
	public JoinNodeRelation getLeftJoinRelation() {
		return leftJoinRelation;
	}
	public int getLeftJoinAttribute() {
		return leftJoinAttribute;
	}
	public JoinNodeRelation getRightJoinRelation() {
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
