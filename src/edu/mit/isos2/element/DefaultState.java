package edu.mit.isos2.element;

public class DefaultState implements State {
	private final String name;
	
	public DefaultState() {
		this.name = "";
	}
	
	public DefaultState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
}
