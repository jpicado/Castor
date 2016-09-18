package castor.algorithms.transformations;

import java.util.LinkedList;
import java.util.List;

import aima.core.logic.fol.kb.data.Literal;

public class TreeNodeChains {
	private TreeNodeChains parent;
	private List<TreeNodeChains> children;
	private List<Literal> object;
	
	public TreeNodeChains(List<Literal> object) {
		super();
		this.parent = null;
		this.children = new LinkedList<TreeNodeChains>();
		this.object = object;
	}
	
	public TreeNodeChains(TreeNodeChains parent, List<Literal> object) {
		super();
		this.parent = parent;
		this.children = new LinkedList<TreeNodeChains>();
		this.object = object;
	}
	
	public TreeNodeChains(TreeNodeChains parent, List<TreeNodeChains> children, List<Literal> object) {
		super();
		this.parent = parent;
		this.children = children;
		this.object = object;
	}

	public TreeNodeChains getParent() {
		return parent;
	}

	public void setParent(TreeNodeChains parent) {
		this.parent = parent;
	}

	public List<TreeNodeChains> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNodeChains> children) {
		this.children = children;
	}

	public List<Literal> getObject() {
		return object;
	}

	public void setObject(List<Literal> object) {
		this.object = object;
	}
	
	public void addChild(TreeNodeChains child) {
		this.children.add(child);
	}
}
