package edu.mit.isos;

public class Element {
	protected String name;
	protected Resource stock = new Resource();
	protected Resource transformInputs = new Resource();
	protected Resource transformOutputs = new Resource();
	protected Resource transportInputs = new Resource();
	protected Resource transportOutputs = new Resource();
	protected Resource storeInputs = new Resource();
	protected Resource storeOutputs = new Resource();
	protected Resource exchangeInputs = new Resource();
	protected Resource exchangeOutputs = new Resource();
	protected Node origin, destination;
	protected State state;

	public Element() {
		name = "";
		origin = null;
		destination = null;
	}

	public Element(String name, Node origin, Node destination) {
		this.name = name;
		this.origin = origin;
		this.destination = destination;
	}

	public State getState() {
		return state;
	}
	
	public String getName() {
		return name;
	}
	
	public Node getDestination() {
		return destination;
	}

	public Resource getExchangeInputs() {
		return exchangeInputs;
	}

	public Resource getExchangeOutputs() {
		return exchangeOutputs;
	}

	public Node getOrigin() {
		return origin;
	}
	public Resource getStock() {
		return stock;
	}
	public Resource getStoreInputs() {
		return storeInputs;
	}
	public Resource getStoreOutputs() {
		return storeOutputs;
	}
	public Resource getTransformInputs() {
		return transformInputs;
	}
	
	public Resource getTransformOutputs() {
		return transformOutputs;
	}
	
	public Resource getTransportInputs() {
		return transportInputs;
	}
	
	public Resource getTransportOutputs() {
		return transportOutputs;
	}
	
	public void tick() {		
		stock = stock.add(storeInputs).subtract(storeOutputs);
	}
	
	public String toString() {
		return name + " " + stock.toString();
	}
}
