package edu.mit.isos2;

import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public abstract class Element {
	private String name;
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

	protected Element() {
		name = "";
		initialParent = this;
		initialLocation = null;
		initialContents = ResourceFactory.createResource();
		initialState = new State();
	}
	
	public Element initialContents(Resource initialContents) {
		this.initialContents = initialContents;
		return this;
	}
	
	public Element initialState(State initialState) {
		this.initialState = initialState;
		return this;
	}
	
	public Element initialParent(Element initialParent) {
		this.initialParent = initialParent;
		return this;
	}
	
	public Element(String name, Location initialLocation) {
		this.name = name;
		initialParent = this;
		this.initialLocation = initialLocation;
		initialContents = ResourceFactory.createResource();
		initialState = new State("Default");
	}

	
	public Resource getContents() {
		return contents;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Element getParent() {
		return parent;
	}
	
	public String getName() {
		return name;
	}

	public abstract Resource getInputRate();
	public abstract Resource getOutputRate();
	public abstract Resource getProductionRate();
	public abstract Resource getConsumptionRate();
	public abstract Resource getStorageRate();
	public abstract Resource getRetrievalRate();
	public abstract Resource getSendingRate();
	public abstract Resource getSendingRateTo(Element element);
	public abstract Resource getReceivingRate();
	public abstract Resource getReceivingRateFrom(Element element);
	
	public State getState() {
		return state;
	}
	
	public void initialize(long initialTime) {
		contents = nextContents = initialContents;
		state = nextState = initialState;
		parent = nextParent = initialParent;
		location = nextLocation = initialLocation;
	}
	
	public void iterateTick() {
		
	}
	
	public void iterateTock() {
		
	}
	
	public void tick(long duration) {
		nextContents = contents.copy();
		storeResources(getStorageRate().multiply(duration), 
				getRetrievalRate().multiply(duration));
		transformResources(getConsumptionRate().multiply(duration),
				getProductionRate().multiply(duration));
		transportResources(getInputRate().multiply(duration), 
				getOutputRate().multiply(duration));
		exchangeResources(getSendingRate().multiply(duration),
				getReceivingRate().multiply(duration));
	}
	
	private void storeResources(Resource stored, Resource retrieved) {
		nextContents = nextContents.add(stored).subtract(retrieved);
	}
	
	private void transformResources(Resource consumed, Resource produced) {
		nextContents = nextContents.subtract(consumed).add(produced);
	}
	
	private void transportResources(Resource input, Resource output) {
		nextContents = nextContents.add(input).subtract(output);
	}
	
	private void exchangeResources(Resource sent, Resource received) {
		nextContents = nextContents.subtract(sent).add(received);
	}
	
	public void transformElement(State state) {
		nextState = state;
	}
	
	public void transportElement(Location location) {
		nextLocation = location;
	}
	
	public void storeElement(Element parent) {
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
	}
	
	public String toString() {
		return name + " " + " (" + state + " @ " + location + ", " + contents + ") ";
	}
}
