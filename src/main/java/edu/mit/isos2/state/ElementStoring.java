package edu.mit.isos2.state;

import edu.mit.isos2.element.Element;


public interface ElementStoring {
	public void store(Element element, Element nextParent);
}
