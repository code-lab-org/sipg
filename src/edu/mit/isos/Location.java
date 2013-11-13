package edu.mit.isos;

public class Location {
	private Node origin, destination;
	
	protected Location() {
		origin = null;
		destination = null;
	}
	
	public Location(Node origin, Node destination) {
		this.origin = origin;
		this.destination = destination;
	}
	
	public Location(Node node) {
		this(node, node);
	}
	
	public String toString() {
		if(isNodal()) {
			return origin.toString();
		} else {
			return origin + "-" + destination;
		}
	}
	
	public Node getOrigin() {
		return origin;
	}
	
	public Node getDestination() {
		return destination;
	}
	
	public boolean equals(Object object) {
		if(object instanceof Location) {
			return ((Location) object).origin.equals(origin) 
					&& ((Location) object).destination.equals(destination);
		} else {
			return false;
		}
	}
	
	public boolean isNodal() {
		return origin != null && origin.equals(destination);
	}
}
