package edu.mit.isos3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.mit.isos3.element.Element;
import edu.mit.isos3.element.LocalElement;

public class Scenario {
	private final String name;
	private final List<Location> locations = new ArrayList<Location>();
	private final List<LocalElement> elements = new ArrayList<LocalElement>();
	private final long initialTime;
	
	protected Scenario() {
		name = "";
		initialTime = 0;
	}
	
	public Scenario(String name, long initialTime,
			Collection<Location> locations, 
			Collection<? extends LocalElement> elements) {
		this.name = name;
		this.initialTime = initialTime;
		this.locations.addAll(locations);
		this.elements.addAll(elements);
	}
	
	public String getName() {
		return name;
	}
	
	public Element getElement(String name) {
		for(Element element : elements) {
			if(element.getName().equals(name)) {
				return element;
			}
		}
		return null;
	}
	
	public List<LocalElement> getElements() {
		return new ArrayList<LocalElement>(elements);
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
