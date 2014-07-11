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

	public Resource getInput(long duration) {
		return ResourceFactory.create();
	}

	public Resource getOutput(long duration) {
		return ResourceFactory.create();
	}

	public Resource getProduced(long duration) {
		return ResourceFactory.create();
	}

	public Resource getConsumed(long duration) {
		return ResourceFactory.create();
	}

	public Resource getStored(long duration) {
		return ResourceFactory.create();
	}

	public Resource getRetrieved(long duration) {
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
			store(element, getStored(duration), getRetrieved(duration));
			transport(element, getInput(duration), getOutput(duration));
			transform(element, getConsumed(duration), getProduced(duration));
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
			netFlow = netFlow.subtract(getStored(duration)).add(getRetrieved(duration))
					.add(getProduced(duration)).subtract(getConsumed(duration))
					.add(getInput(duration)).subtract(getOutput(duration));
		}
		if(location.isNodal() && location.getOrigin().equals(element.getLocation().getOrigin())) {
			netFlow = netFlow.subtract(getInput(duration));
		}
		if(location.isNodal() && location.getOrigin().equals(element.getLocation().getDestination())) {
			netFlow = netFlow.add(getOutput(duration));
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
