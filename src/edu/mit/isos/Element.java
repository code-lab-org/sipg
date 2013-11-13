package edu.mit.isos;

public abstract class Element {
	private String name;
	private final Resource initialStock;
	private final State initialState;
	private final Location initialLocation;
	private Resource stock;
	private State state;
	private Location location;
	private transient Location nextLocation;
	private transient Resource nextStock;
	private transient State nextState;

	public Element() {
		name = "";
		initialLocation = null;
		initialStock = new Resource();
		initialState = new State();
	}

	public Element(String name, Location initialLocation, Resource initialStock, State initialState) {
		this.name = name;
		this.initialLocation = initialLocation;
		this.initialStock = initialStock;
		this.initialState = initialState;
	}

	public State getState() {
		return state;
	}
	
	public String getName() {
		return name;
	}

	public Resource getExchangeInputs() {
		return new Resource();
	}

	public Resource getExchangeOutputs() {
		return new Resource();
	}

	public Location getLocation() {
		return location;
	}
	public Resource getStock() {
		return stock;
	}
	public Resource getStoreInputs() {
		return new Resource();
	}
	public Resource getStoreOutputs() {
		return new Resource();
	}
	public Resource getTransformInputs() {
		return new Resource();
	}
	
	public Resource getTransformOutputs() {
		return new Resource();
	}
	
	public Resource getTransportInputs() {
		return new Resource();
	}
	
	public Resource getTransportOutputs() {
		return new Resource();
	}
	
	public void initialize(long initialTime) {
		stock = initialStock;
		state = initialState;
		location = initialLocation;
	}
	
	public void tick(long duration) {		
		nextStock = stock.add(getStoreInputs().multiply(duration)).subtract(getStoreOutputs().multiply(duration));
		nextState = state;
		nextLocation = location;
	}
	
	public void tock() {
		stock = nextStock;
		state = nextState;
		location = nextLocation;
	}
	
	public String toString() {
		return name + " " + " (" + state + " @ " + location + ", " + stock + ") ";
	}
}
