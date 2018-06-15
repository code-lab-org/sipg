package edu.mit.isos2.state;

import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;

public interface ResourceStoring {
	public Resource getStored(Element element, long duration);
	public Resource getRetrieved(Element element, long duration);
	public void store(Element element, Resource stored, Resource retrieved);
}
