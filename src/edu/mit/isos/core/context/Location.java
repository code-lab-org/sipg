package edu.mit.isos.core.context;

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
	
	public boolean isNodal() {
		return origin != null && origin.equals(destination);
	}
	
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(!(o instanceof Location)) {
			return false;
		}
		Location l = (Location)o;
		return l.origin.equals(origin) 
				&& l.destination.equals(destination);
	}
	
	public int hashCode() {
		return 17 + 31*origin.hashCode() + 31*destination.hashCode();
	}
}
