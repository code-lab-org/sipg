package edu.mit.isos3.element.state;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.LocalElement;

public interface ElementTransporting {
	public void transport(LocalElement element, Location nextLocation);
}
