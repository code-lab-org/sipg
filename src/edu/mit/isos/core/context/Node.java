package edu.mit.isos.core.context;

public class Node {
	private String name;
	
	public Node() {
		name = "";
	}
	
	public Node(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(!(o instanceof Node)) {
			return false;
		}
		Node n = (Node)o;
		return n.name.equals(name);
	}
	
	public int hashCode() {
		return name.hashCode();
	}
}
