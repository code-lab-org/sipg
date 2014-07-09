package edu.mit.isos2.element;

public class State {
	private final String name;
	
	public State() {
		this.name = "";
	}
	
	public State(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
}
