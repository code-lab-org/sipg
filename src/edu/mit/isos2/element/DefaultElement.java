package edu.mit.isos2.element;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.mit.isos2.Location;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.state.DefaultState;
import edu.mit.isos2.state.State;

public class DefaultElement implements Element {

	protected DefaultElement() {
		this("", null);
	}
	
	public DefaultElement(String name, Location initialLocation) {
		this(name, initialLocation, new DefaultState("Default"));
	}
	
	public DefaultElement(String name, Location initialLocation, State initialState) {
		this.name = name;
		initialParent = this;
		this.initialLocation = initialLocation;
		initialContents = ResourceFactory.create();
		this.initialState = initialState;
		states.add(initialState);
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
		if(!states.contains(initialState)) {
			throw new IllegalArgumentException(
					"States does not include " + initialState);
		}
		this.initialState = initialState;
		return this;
	}
	
	public DefaultElement states(Collection<? extends State> states) {
		this.states = new HashSet<State>(states);
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
		for(State state : states) {
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
		for(State state : states) {
			state.tick(this, duration);
		}
	}
	
	public void addContents(Resource resource) {
		nextContents = nextContents.add(resource);
	}
	
	public void removeContents(Resource resource) {
		nextContents = nextContents.subtract(resource);
	}
	
	@Override
	public void setState(State state) {
		if(!states.contains(initialState)) {
			throw new IllegalArgumentException(
					"States does not include " + state);
		}
		nextState = state;
	}

	@Override
	public void setLocation(Location location) {
		if(!equals(parent)) {
			throw new IllegalStateException(
					"Cannot change location for nested element " + this);
		}
		nextLocation = location;
	}

	@Override
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
		for(State state : states) {
			state.tock();
		}
	}
	
	public String toString() {
		return name + " " + " (" + state + " @ " + location + ", " + contents + ") ";
	}

	@Override
	public Resource getNetFlow(Location location, long duration) {
		if(state != null) {
			return state.getNetFlow(this, location, duration);
		} else {
			return ResourceFactory.create();
		}
	}

	@Override
	public Resource getNetExchange(Element element, long duration) {
		if(state != null) {
			return state.getNetExchange(this, element, duration);
		} else {
			return ResourceFactory.create();
		}
	}

	@Override
	public Collection<? extends Element> getElements() {
		return Arrays.asList(this);
	}

	@Override
	public Resource getInitialContents() {
		return initialContents;
	}

	@Override
	public Location getInitialLocation() {
		return initialLocation;
	}

	@Override
	public Element getInitialParent() {
		return initialParent;
	}

	@Override
	public State getInitialState() {
		return initialState;
	}
}
