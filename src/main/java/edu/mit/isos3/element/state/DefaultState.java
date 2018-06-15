package edu.mit.isos3.element.state;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.Element;
import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;

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

	public Resource getInput(LocalElement element, long duration) {
		return ResourceFactory.create();
	}

	public Resource getOutput(LocalElement element, long duration) {
		return ResourceFactory.create();
	}

	public Resource getProduced(LocalElement element, long duration) {
		return ResourceFactory.create();
	}

	public Resource getConsumed(LocalElement element, long duration) {
		return ResourceFactory.create();
	}

	public Resource getStored(LocalElement element, long duration) {
		return ResourceFactory.create();
	}

	public Resource getRetrieved(LocalElement element, long duration) {
		return ResourceFactory.create();
	}

	@Override
	public void iterateTick(LocalElement element, long duration) { }
	
	@Override
	public void iterateTock() { }

	@Override
	public void initialize(LocalElement element, long initialTime) {
		if(!element.getStates().contains(this)) {
			throw new IllegalStateException(
					"Element does not contain state " + this);
		}
	}

	@Override
	public void tick(LocalElement element, long duration) {
		if(equals(element.getState())) {
			store(element, getStored(element, duration), getRetrieved(element, duration));
			transport(element, getInput(element, duration), getOutput(element, duration));
			transform(element, getConsumed(element, duration), getProduced(element, duration));
		}
	}

	@Override
	public void tock() { }

	@Override
	public void store(LocalElement element, Resource stored, Resource retrieved) {
		element.addContents(stored);
		element.removeContents(retrieved);
	}

	@Override
	public void transport(LocalElement element, Resource input, Resource output) {
		// no longer modifies element contents
		// element.addContents(input);
		// element.removeContents(output);
	}

	@Override
	public void transform(LocalElement element, Resource consumed, Resource produced) {
		// no longer modifies element contents
		// element.add(produced);
		// element.remove(consumed);
	}

	@Override
	public Resource getNetFlow(LocalElement element, Location location, long duration) {
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
	public Resource getNetExchange(LocalElement element1, Element element2,
			long duration) {
		return ResourceFactory.create();
	}
	
	@Override
	public void transform(LocalElement element, State nextState) {
		element.setState(nextState);
	}
}
