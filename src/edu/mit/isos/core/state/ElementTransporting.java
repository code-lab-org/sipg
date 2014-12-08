package edu.mit.isos.core.state;

import edu.mit.isos.core.context.Location;
import edu.mit.isos.core.element.LocalElement;

public interface ElementTransporting {
	public void transport(LocalElement element, Location nextLocation);
}
