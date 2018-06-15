package edu.mit.isos2.state;

import edu.mit.isos2.Location;
import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class DefaultState implements State, ResourceStoring, ResourceTransforming, ResourceTransporting, ElementTransforming {
	private final String name;
	
	protected DefaultState() {
		this.name = "";
	}
	
	public DefaultState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}

	public Resource getInput(Element element, long duration) {
		return ResourceFactory.create();
	}

	public Resource getOutput(Element element, long duration) {
		return ResourceFactory.create();
	}

	public Resource getProduced(Element element, long duration) {
		return ResourceFactory.create();
	}

	public Resource getConsumed(Element element, long duration) {
		return ResourceFactory.create();
	}

	public Resource getStored(Element element, long duration) {
		return ResourceFactory.create();
	}

	public Resource getRetrieved(Element element, long duration) {
		return ResourceFactory.create();
	}

	@Override
	public void iterateTick(Element element, long duration) { }
	
	@Override
	public void iterateTock() { }

	@Override
	public void initialize(Element element, long initialTime) {
		if(!element.getStates().contains(this)) {
			throw new IllegalStateException(
					"Element does not contain state " + this);
		}
	}

	@Override
	public void tick(Element element, long duration) {
		if(equals(element.getState())) {
			store(element, getStored(element, duration), getRetrieved(element, duration));
			transport(element, getInput(element, duration), getOutput(element, duration));
			transform(element, getConsumed(element, duration), getProduced(element, duration));
		}
	}

	@Override
	public void tock() { }

	@Override
	public void store(Element element, Resource stored, Resource retrieved) {
		element.addContents(stored);
		element.removeContents(retrieved);
	}

	@Override
	public void transport(Element element, Resource input, Resource output) {
		// no longer modifies element contents
		// element.addContents(input);
		// element.removeContents(output);
	}

	@Override
	public void transform(Element element, Resource consumed, Resource produced) {
		// no longer modifies element contents
		// element.add(produced);
		// element.remove(consumed);
	}

	@Override
	public Resource getNetFlow(Element element, Location location, long duration) {
		Resource netFlow = ResourceFactory.create();
		if(element.getLocation().equals(location)) {
			netFlow = netFlow.add(getRetrieved(element, duration)).subtract(getStored(element, duration))
					.add(getProduced(element, duration)).subtract(getConsumed(element, duration))
					.add(getInput(element, duration)).subtract(getOutput(element, duration));
		}
		if(location.isNodal() && location.getOrigin().equals(element.getLocation().getOrigin())) {
			netFlow = netFlow.subtract(getInput(element, duration));
		}
		if(location.isNodal() && location.getOrigin().equals(element.getLocation().getDestination())) {
			netFlow = netFlow.add(getOutput(element, duration));
		}
		return netFlow;
	}

	@Override
	public Resource getNetExchange(Element element1, Element element2,
			long duration) {
		return ResourceFactory.create();
	}
	
	@Override
	public void transform(Element element, State nextState) {
		element.setState(nextState);
	}
}
