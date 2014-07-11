package edu.mit.isos2.state;

import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;

public interface ResourceStoring {
	public Resource getStored(long duration);
	public Resource getRetrieved(long duration);
	public void store(Element element, Resource stored, Resource retrieved);
}
