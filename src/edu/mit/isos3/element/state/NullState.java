package edu.mit.isos3.element.state;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.Element;
import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;


public class NullState implements State {
	
	public NullState() { }
	
	public String getName() {
		return "";
	}
	
	public String toString() {
		return getName();
	}

	@Override
	public void initialize(LocalElement element, long initialTime) { }

	@Override
	public void tick(LocalElement element, long duration) { }

	@Override
	public void tock() { }

	@Override
	public void iterateTick(LocalElement element, long duration) { }

	@Override
	public void iterateTock() { }

	@Override
	public Resource getNetFlow(LocalElement element, Location location, long duration) {
		return ResourceFactory.create();
	}

	@Override
	public Resource getNetExchange(LocalElement element1, Element element2,
			long duration) {
		return ResourceFactory.create();
	}
}
