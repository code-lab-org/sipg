package edu.mit.isos3.element.state;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.Element;
import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.resource.Resource;

public interface State {	
	public String getName();
	
	public void initialize(LocalElement element, long initialTime);
	public void iterateTick(LocalElement element, long duration);
	public void iterateTock();
	public void tick(LocalElement element, long duration);
	public void tock();
	
	public Resource getNetFlow(LocalElement element, Location location, long duration);
	public Resource getNetExchange(LocalElement element1, Element element2, long duration);
}
