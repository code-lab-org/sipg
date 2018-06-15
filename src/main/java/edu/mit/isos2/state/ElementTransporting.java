package edu.mit.isos2.state;

import edu.mit.isos2.Location;
import edu.mit.isos2.element.Element;

public interface ElementTransporting {
	public void transport(Element element, Location nextLocation);
}
