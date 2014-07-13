package edu.mit.isos2.system;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.mit.isos2.Location;
import edu.mit.isos2.SimEntity;
import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class SuperSystem implements SimEntity {
	private String name;
	private Set<SystemElement> elements = new HashSet<SystemElement>();
	
	public SuperSystem(String name, 
			Collection<? extends SystemElement> elements) {
		this.name = name;
		this.elements.addAll(elements);
	}
	
	public Collection<? extends Element> getElements() {
		return new HashSet<Element>(elements);
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	public SystemElement getSystemElement(Location location) {
		for(SystemElement element : elements) {
			if(element.getLocation().equals(location)) {
				return element;
			}
		}
		return null;
	}

	public Resource getNetFlow(Location location, long duration) {
		Resource contents = ResourceFactory.create();
		for(SystemElement e : elements) {
			contents.add(e.getNetFlow(location, duration));
		}
		return contents;
	}

	public Resource getNetExchange(SystemElement element, long duration) {
		Resource contents = ResourceFactory.create();
		for(SystemElement e : elements) {
			contents.add(e.getNetExchange(element, duration));
		}
		return contents;
	}

	public void initialize(long initialTime) {
		for(SystemElement e : elements) {
			e.initialize(initialTime);
		}
	}

	public void iterateTick(long duration) {
		Map<Location, Resource> netFlow = new HashMap<Location, Resource>();
		for(SystemElement e : elements) {
			if(!netFlow.containsKey(e.getLocation())) {
				netFlow.put(e.getLocation(), ResourceFactory.create());
			}
		}
		for(Location location : netFlow.keySet()) {
			for(SystemElement e : elements) {
				netFlow.put(location, netFlow.get(location)
						.add(e.getNetFlow(location, duration)));
			}
		}
		
		// optimization program here
		
		for(SystemElement e : elements) {
			e.iterateTick(duration);
		}
	}

	public void iterateTock() {
		for(SystemElement e : elements) {
			e.iterateTock();
		}
	}

	public void tick(long duration) {
		for(SystemElement e : elements) {
			e.tick(duration);
		}
	}

	public void tock() {
		for(SystemElement e : elements) {
			e.tock();
		}
	}
}
