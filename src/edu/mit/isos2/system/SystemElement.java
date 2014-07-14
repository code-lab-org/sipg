package edu.mit.isos2.system;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.mit.isos2.Location;
import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.state.State;

public class SystemElement implements Element {
	private Set<Element> elements = new HashSet<Element>();
	private String name;
	private Location location;
	private SystemState state = new SystemState("Default");
	
	protected SystemElement() {
		name = "";
		location = null;
	}
	
	public SystemElement(String name, Location location, 
			Collection<? extends Element> elements) {
		this.name = name;
		this.location = location;
		this.elements.addAll(elements);
	}
	
	public Collection<? extends Element> getElements() {
		return new HashSet<Element>(elements);
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public Collection<? extends State> getStates() {
		return Arrays.asList(state);
	}

	@Override
	public Resource getContents() {
		Resource contents = ResourceFactory.create();
		for(Element e : elements) {
			contents = contents.add(e.getContents());
		}
		return contents;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public Element getParent() {
		return this;
	}

	@Override
	public SystemState getState() {
		return state;
	}

	@Override
	public void initialize(long initialTime) {
		state.initialize(this, initialTime);
		for(Element e : elements) {
			e.initialize(initialTime);
		}
	}

	@Override
	public void iterateTick(long duration) {
		state.iterateTick(this, duration);
		for(Element e : elements) {
			e.iterateTick(duration);
		}
	}

	@Override
	public void iterateTock() {
		state.iterateTock();
		for(Element e : elements) {
			e.iterateTock();
		}
	}

	@Override
	public void tick(long duration) {
		state.tick(this, duration);
		for(Element e : elements) {
			e.tick(duration);
		}
	}

	@Override
	public void tock() {
		state.tock();
		for(Element e : elements) {
			e.tock();
		}
	}

	@Override
	public void addContents(Resource resource) {
		throw new IllegalStateException(
				"Cannot add to system element contents");
	}

	@Override
	public void removeContents(Resource resource) {
		throw new IllegalStateException(
				"Cannot remove from system element contents");
	}

	@Override
	public void setState(State state) {
		throw new IllegalStateException(
				"Cannot change system element state");
	}

	@Override
	public void setParent(Element parent) {
		throw new IllegalStateException(
				"Cannot change system element parent");
	}

	@Override
	public void setLocation(Location location) {
		throw new IllegalStateException(
				"Cannot change system element location");
	}

	@Override
	public Resource getNetFlow(Location location, long duration) {
		Resource netFlow = state.getNetFlow(this, location, duration);
		for(Element e : elements) {
			netFlow = netFlow.add(e.getNetFlow(location, duration));
		}
		return netFlow;
	}

	@Override
	public Resource getNetExchange(Element element, long duration) {
		Resource netExchange = state.getNetExchange(this, element, duration);
		for(Element e : elements) {
			netExchange = netExchange.add(e.getNetExchange(element, duration));
		}
		return netExchange;
	}
}
