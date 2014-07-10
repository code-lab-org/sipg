package edu.mit.isos2.element;

import edu.mit.isos2.Location;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class ProducingState extends OperatingState implements ResourceTransforming {

	@Override
	public Resource getProduced(long duration) {
		return ResourceFactory.create();
	}

	@Override
	public Resource getConsumed(long duration) {
		return ResourceFactory.create();
	}
	
	public void tick(Element element, long duration) {
		super.tick(element, duration);
		if(element.getState().equals(this)) {
			transform(element, getConsumed(duration), getProduced(duration));
		}
	}

	@Override
	public void transform(Element element, Resource consumed, Resource produced) {
		// no longer modifies element contents
		// element.add(produced);
		// element.remove(consumed);
	}

	@Override
	public Resource getNetFlow(Element element, Location location, long duration) {
		Resource netFlow = super.getNetFlow(element, location, duration);
		if(element.getLocation().equals(location)) {
			netFlow = netFlow.add(getProduced(duration)).subtract(getConsumed(duration));
		}
		return netFlow;
	}
}
