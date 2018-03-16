package castor.sampling;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class JoinNode {

	private JoinNodeRelation nodeRelation;
	private Set<JoinEdge> edges;
	
	public JoinNode(JoinNodeRelation nodeRelation, Set<JoinEdge> edges) {
		super();
		this.nodeRelation = nodeRelation;
		this.edges = edges;
	}
	public JoinNode(JoinNodeRelation nodeRelation) {
		super();
		this.nodeRelation = nodeRelation;
		edges = new HashSet<JoinEdge>();
	}
	public JoinNode(String relation) {
		super();
		this.nodeRelation = new JoinNodeRelation(relation);
		edges = new HashSet<JoinEdge>();
	}
	public JoinNodeRelation getNodeRelation() {
		return nodeRelation;
	}
	public Set<JoinEdge> getEdges() {
		return edges;
	}
	
	@Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof JoinNode)) {
            return false;
        }
        JoinNode joinNode = (JoinNode) o;
        return Objects.equals(nodeRelation, joinNode.nodeRelation) &&
                Objects.equals(edges, joinNode.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeRelation, edges);
    }
    
	@Override
	public String toString() {
		return "JoinNode [nodeRelation=" + nodeRelation + ", edges=" + edges.toString() + "]";
	}
}
