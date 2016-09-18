package castor.algorithms.transformations;

import java.util.LinkedList;
import java.util.List;

import aima.core.logic.fol.kb.data.Literal;

public class TreeNode {
	private TreeNode parent;
	private List<TreeNode> children;
	private Literal literal;
	
	public TreeNode(Literal literal) {
		super();
		this.parent = null;
		this.children = new LinkedList<TreeNode>();
		this.literal = literal;
	}
	
	public TreeNode(TreeNode parent, Literal literal) {
		super();
		this.parent = parent;
		this.children = new LinkedList<TreeNode>();
		this.literal = literal;
	}
	
	public TreeNode(TreeNode parent, List<TreeNode> children, Literal literal) {
		super();
		this.parent = parent;
		this.children = children;
		this.literal = literal;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public Literal getLiteral() {
		return literal;
	}

	public void setLiteral(Literal literal) {
		this.literal = literal;
	}
	
	public void addChild(TreeNode child) {
		this.children.add(child);
	}
}
