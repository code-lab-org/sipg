package edu.mit.isos2.element;

import edu.mit.isos2.Location;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class DefaultElement implements ResourceStore, ResourceTransformer, ResourceTransporter {

	protected DefaultElement() {
		name = "";
		initialParent = this;
		initialLocation = null;
		initialContents = ResourceFactory.createResource();
		initialState = new State();
	}
	
	public DefaultElement(String name, Location initialLocation) {
		this.name = name;
		initialParent = this;
		this.initialLocation = initialLocation;
		initialContents = ResourceFactory.createResource();
		initialState = new State("Default");
	}

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
	
	public void tick(long timeStep) {
		nextContents = contents.copy();
		storeResources(getStorageRate().multiply(timeStep), 
				getRetrievalRate().multiply(timeStep));
		transformResources(getConsumptionRate().multiply(timeStep),
				getProductionRate().multiply(timeStep));
		transportResources(getInputRate().multiply(timeStep), 
				getOutputRate().multiply(timeStep));
	}
	
	protected void storeResources(Resource stored, Resource retrieved) {
		nextContents = nextContents.add(stored).subtract(retrieved);
	}
	
	protected void transformResources(Resource consumed, Resource produced) {
		nextContents = nextContents.subtract(consumed).add(produced);
	}
	
	protected void transportResources(Resource input, Resource output) {
		nextContents = nextContents.add(input).subtract(output);
	}
	
	protected void exchangeResources(Resource sent, Resource received) {
		nextContents = nextContents.subtract(sent).add(received);
	}
	
	protected void transformElement(State state) {
		nextState = state;
	}
	
	protected void transportElement(Location location) {
		nextLocation = location;
	}
	
	protected void storeElement(Element parent) {
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

	public Resource getInputRate() {
		return ResourceFactory.createResource();
	}

	public Resource getOutputRate() {
		return ResourceFactory.createResource();
	}

	public Resource getProductionRate() {
		return ResourceFactory.createResource();
	}

	public Resource getConsumptionRate() {
		return ResourceFactory.createResource();
	}

	public Resource getStorageRate() {
		return ResourceFactory.createResource();
	}

	public Resource getRetrievalRate() {
		return ResourceFactory.createResource();
	}
}
