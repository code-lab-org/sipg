package edu.mit.isos2.state;

import edu.mit.isos2.element.Element;


public interface ElementTransforming {
	public void transform(Element element, State nextState);
}
