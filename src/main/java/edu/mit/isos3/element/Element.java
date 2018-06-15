package edu.mit.isos3.element;

import edu.mit.isos3.Location;
import edu.mit.isos3.resource.Resource;

public interface Element {
	public String getName();
	public Location getLocation();
	public Resource getNetExchange(Element element, long duration);
}
