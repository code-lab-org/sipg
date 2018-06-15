package edu.mit.isos2.state;

import edu.mit.isos2.Location;
import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;

public interface State {	
	public String getName();
	
	public void initialize(Element element, long initialTime);
	public void iterateTick(Element element, long duration);
	public void iterateTock();
	public void tick(Element element, long duration);
	public void tock();
	
	public Resource getNetFlow(Element element, Location location, long duration);
	public Resource getNetExchange(Element element1, Element element2, long duration);
}
