package edu.mit.isos2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.mit.isos2.element.Element;

public class Scenario {
	private final String name;
	private final List<Location> locations = new ArrayList<Location>();
	private final List<SimEntity> entities = new ArrayList<SimEntity>();
	private final long initialTime;
	
	protected Scenario() {
		name = "";
		initialTime = 0;
	}
	
	public Scenario(String name, long initialTime,
			Collection<Location> locations, 
			Collection<? extends SimEntity> entities) {
		this.name = name;
		this.initialTime = initialTime;
		this.locations.addAll(locations);
		this.entities.addAll(entities);
	}
	
	public String getName() {
		return name;
	}
	
	public Collection<SimEntity> getSimEntities() {
		return new ArrayList<SimEntity>(entities);
	}

	public Collection<Element> getElements() {
		List<Element> elements = new ArrayList<Element>();
		for(SimEntity entity : entities) {
			elements.addAll(entity.getElements());
		}
		return elements;
	}
	
	public Element getElement(String name) {
		for(Element element : getElements()) {
			if(element.getName().equals(name)) {
				return element;
			}
		}
		return null;
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
