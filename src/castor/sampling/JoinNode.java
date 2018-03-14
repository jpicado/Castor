package castor.sampling;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class JoinNode {

	private String relation;
	private Set<JoinEdge> edges;
	
	public JoinNode(String relation, Set<JoinEdge> edges) {
		super();
		this.relation = relation;
		this.edges = edges;
	}
	public JoinNode(String relation) {
		super();
		this.relation = relation;
		edges = new HashSet<JoinEdge>();
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public Set<JoinEdge> getEdges() {
		return edges;
	}
	public void setEdges(Set<JoinEdge> edges) {
		this.edges = edges;
	}
	
	@Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof JoinNode)) {
            return false;
        }
        JoinNode joinNode = (JoinNode) o;
        return relation == joinNode.relation &&
                Objects.equals(edges, joinNode.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relation, edges);
    }
    
	@Override
	public String toString() {
		return "JoinNode [relation=" + relation + ", edges=" + edges.toString() + "]";
	}
}
