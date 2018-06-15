package edu.mit.isos.core.state;

import edu.mit.isos.core.element.Element;
import edu.mit.isos.core.element.LocalElement;

public interface ElementStoring {
	public void store(LocalElement element, Element nextParent);
}
