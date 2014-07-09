package edu.mit.isos2.element;

import edu.mit.isos2.Location;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class DefaultElement implements Element {

	protected DefaultElement() {
		name = "";
		initialParent = this;
		initialLocation = null;
		initialContents = ResourceFactory.create();
		initialState = new DefaultState();
	}
	
	public DefaultElement(String name, Location initialLocation) {
		this.name = name;
		initialParent = this;
		this.initialLocation = initialLocation;
		initialContents = ResourceFactory.create();
		initialState = new DefaultState("Default");
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
	
	public void iterateTick(long duration) {
		state.iterateTick(this, duration);
	}
	
	public void iterateTock() {
		state.iterateTock();
	}
	
	public void tick(long duration) {
		nextContents = contents.copy();
		if(state instanceof ResourceStoring) {
			ResourceStoring res = (ResourceStoring) state;
			store(res.getStored(duration), 
					res.getRetrieved(duration));
		}
		if(state instanceof ResourceTransforming) {
			ResourceTransforming ref = (ResourceTransforming) state;
			transform(ref.getConsumed(duration),
					ref.getProduced(duration));
		}
		if(state instanceof ResourceTransporting) {
			ResourceTransporting rep = (ResourceTransporting) state;
			transport(rep.getInput(duration), 
					rep.getOutput(duration));
		}
		if(state instanceof ResourceExchanging) {
			ResourceExchanging rex = (ResourceExchanging) state;
			exchange(rex.getSent(duration),
					rex.getReceived(duration));
		}
	}

	@Override
	public void store(Resource stored, Resource retrieved) {
		nextContents = nextContents.add(stored).subtract(retrieved);
	}

	@Override
	public void transform(Resource consumed, Resource produced) {
		nextContents = nextContents.subtract(consumed).add(produced);
	}

	@Override
	public void transport(Resource input, Resource output) {
		nextContents = nextContents.add(input).subtract(output);
	}

	@Override
	public void exchange(Resource sent, Resource received) {
		nextContents = nextContents.subtract(sent).add(received);
	}
	
	@Override
	public void transform(State state) {
		nextState = state;
	}

	@Override
	public void transport(Location location) {
		nextLocation = location;
	}

	@Override
	public void store(Element parent) {
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
