package edu.mit.isos3.element.state;

import edu.mit.isos3.element.LocalElement;


public interface ElementTransforming {
	public void transform(LocalElement element, State nextState);
}
