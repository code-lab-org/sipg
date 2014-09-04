package edu.mit.isos3.element;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.state.DefaultState;
import edu.mit.isos3.element.state.State;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;

public class DefaultElement implements LocalElement {

	protected DefaultElement() {
		this("", null);
	}
	
	public DefaultElement(String name, Location initialLocation) {
		this(name, initialLocation, new DefaultState("Default"));
	}
	
	public DefaultElement(String name, Location initialLocation, State initialState) {
		this(name, initialLocation, Arrays.asList(initialState));
	}
	
	public DefaultElement(String name, Location initialLocation, List<State> states) {
		this.name = name;
		initialParent = this;
		this.initialLocation = initialLocation;
		initialContents = ResourceFactory.create();
		this.states.addAll(states);
		if(states.size() > 0) {
			initialState = states.get(0);
		}
	}

	private String name;
	private Set<State> states = new HashSet<State>();
	
	private Element initialParent;
	private Resource initialContents;
	private State initialState;
	private Location initialLocation;
	
	private Element parent;
	private Resource contents;
	private State state;
	private Location location;
	
	private transient Location nextLocation;
	private transient Element nextParent;
	private transient Resource nextContents;
	private transient State nextState;
	
	public DefaultElement initialContents(Resource initialContents) {
		this.initialContents = initialContents;
		return this;
	}
	
	public DefaultElement initialState(State initialState) {
		if(!getStates().contains(initialState)) {
			states.add(initialState);
		}
		this.initialState = initialState;
		return this;
	}
	
	public DefaultElement initialParent(Element initialParent) {
		this.initialParent = initialParent;
		return this;
	}

	
	public Resource getContents() {
		return contents;
	}
	
	public Location getLocation() {
		if(!equals(parent)) {
			return parent.getLocation();
		} else {
			return location;
		}
	}
	
	public Element getParent() {
		return parent;
	}
	
	public String getName() {
		return name;
	}
	
	public State getState() {
		return state;
	}
	
	public Set<State> getStates() {
		return new HashSet<State>(states);
	}
	
	public void initialize(long initialTime) {
		contents = nextContents = initialContents;
		state = nextState = initialState;
		parent = nextParent = initialParent;
		location = nextLocation = initialLocation;
		for(State state : getStates()) {
			state.initialize(this, initialTime);
		}
	}
	
	public void iterateTick(long duration) {
		if(state != null) {
			state.iterateTick(this, duration);
		}
	}
	
	public void iterateTock() {
		if(state != null) {
			state.iterateTock();
		}
	}
	
	public void tick(long duration) {
		nextContents = contents.copy();
		for(State state : getStates()) {
			state.tick(this, duration);
		}
	}
	
	public void addContents(Resource resource) {
		nextContents = nextContents.add(resource);
	}
	
	public void removeContents(Resource resource) {
		nextContents = nextContents.subtract(resource);
	}
	
	public void setState(State state) {
		if(!getStates().contains(initialState)) {
			throw new IllegalArgumentException(
					"States does not include " + state);
		}
		nextState = state;
	}

	public void setLocation(Location location) {
		if(!equals(parent)) {
			throw new IllegalStateException(
					"Cannot change location for nested element " + this);
		}
		nextLocation = location;
	}

	public void setParent(Element parent) {
		if(!parent.getLocation().equals(getLocation())) {
			throw new IllegalArgumentException(
					"Parent must have same location as child.");
		}
		nextParent = parent;
	}
	
	public void tock() {
		contents = nextContents.copy();
		state = nextState;
		parent = nextParent;
		location = nextLocation;
		for(State state : getStates()) {
			state.tock();
		}
	}
	
	public String toString() {
		return name + " " + " (" + state + " @ " + location + ", " + contents + ") ";
	}

	public Resource getNetFlow(Location location, long duration) {
		if(state != null) {
			return state.getNetFlow(this, location, duration);
		} else {
			return ResourceFactory.create();
		}
	}

	public Resource getNetExchange(Element element, long duration) {
		if(state != null) {
			return state.getNetExchange(this, element, duration);
		} else {
			return ResourceFactory.create();
		}
	}

	public Collection<? extends Element> getElements() {
		return Arrays.asList(this);
	}

	public Resource getInitialContents() {
		return initialContents;
	}

	public Location getInitialLocation() {
		return initialLocation;
	}

	public Element getInitialParent() {
		return initialParent;
	}

	public State getInitialState() {
		return initialState;
	}
}
