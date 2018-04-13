package castor.sampling;

import java.util.Objects;

public class JoinEdge {

	private JoinNode joinNode;
	private int leftJoinAttribute;
	private int rightJoinAttribute;
	
	public JoinEdge(JoinNode rightJoinNode, int leftJoinAttribute, int rightJoinAttribute) {
		super();
		this.joinNode = rightJoinNode;
		this.leftJoinAttribute = leftJoinAttribute;
		this.rightJoinAttribute = rightJoinAttribute;
	}
	public JoinNode getJoinNode() {
		return joinNode;
	}
	public void setJoinNode(JoinNode joinNode) {
		this.joinNode = joinNode;
	}
	public int getLeftJoinAttribute() {
		return leftJoinAttribute;
	}
	public void setLeftJoinAttribute(int leftJoinAttribute) {
		this.leftJoinAttribute = leftJoinAttribute;
	}
	public int getRightJoinAttribute() {
		return rightJoinAttribute;
	}
	public void setRightJoinAttribute(int rightJoinAttribute) {
		this.rightJoinAttribute = rightJoinAttribute;
	}
	
	@Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof JoinEdge)) {
            return false;
        }
        JoinEdge joinEdge = (JoinEdge) o;
        return leftJoinAttribute == joinEdge.leftJoinAttribute &&
        		rightJoinAttribute == joinEdge.rightJoinAttribute &&
                Objects.equals(joinNode, joinEdge.joinNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(joinNode, leftJoinAttribute, rightJoinAttribute);
    }
    
	@Override
	public String toString() {
		return "JoinEdge [joinNode=" + joinNode.getNodeRelation().toString() + ", leftJoinAttribute=" + leftJoinAttribute + ", rightJoinAttribute="
				+ rightJoinAttribute + "]";
	}
}
