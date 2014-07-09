package edu.mit.isos2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Scenario {
	private final String name;
	private final List<Location> locations = new ArrayList<Location>();
	private final List<Element> elements = new ArrayList<Element>();
	private final long initialTime;
	
	protected Scenario() {
		name = "";
		initialTime = 0;
	}
	
	public Scenario(String name, long initialTime, 
			Collection<Location> locations, 
			Collection<? extends Element> elements) {
		this.name = name;
		this.initialTime = initialTime;
		this.locations.addAll(locations);
		this.elements.addAll(elements);
	}
	
	public String getName() {
		return name;
	}
	
	public Collection<Element> getElements() {
		return new ArrayList<Element>(elements);
	}
	
	public Collection<Location> getLocations() {
		return new ArrayList<Location>(locations);
	}
	
	public long getInitialTime() {
		return initialTime;
	}
	
	public String toString() {
		return new StringBuilder(getName()).append(" {initialTime: ")
			.append(initialTime).append("}").toString();
	}
}
