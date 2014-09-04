package edu.mit.isos3.element.state;

import edu.mit.isos3.element.Element;
import edu.mit.isos3.element.LocalElement;


public interface ElementStoring {
	public void store(LocalElement element, Element nextParent);
}
