package edu.mit.isos2.element;

import edu.mit.isos2.Location;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;


public class NullState implements State {
	
	public NullState() { }
	
	public String getName() {
		return "Null";
	}
	
	public String toString() {
		return getName();
	}

	@Override
	public void initialize(Element element, long initialTime) { }

	@Override
	public void tick(Element element, long duration) { }

	@Override
	public void tock() { }

	@Override
	public void iterateTick(Element element, long duration) { }

	@Override
	public void iterateTock() { }

	@Override
	public Resource getNetFlow(Element element, Location location, long duration) {
		return ResourceFactory.create();
	}

	@Override
	public Resource getNetExchange(Element element1, Element element2,
			long duration) {
		return ResourceFactory.create();
	}
}
