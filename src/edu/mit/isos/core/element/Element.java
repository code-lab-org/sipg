package edu.mit.isos.core.element;

import edu.mit.isos.core.context.Location;
import edu.mit.isos.core.context.Resource;

public interface Element {
	public String getName();
	public Location getLocation();
	public Resource getNetExchange(Element element, long duration);
}
