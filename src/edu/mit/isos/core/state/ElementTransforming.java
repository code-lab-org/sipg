package edu.mit.isos.core.state;

import edu.mit.isos.core.element.LocalElement;

public interface ElementTransforming {
	public void transform(LocalElement element, State nextState);
}
