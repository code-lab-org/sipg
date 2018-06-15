package edu.mit.isos3.element.state;

import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.resource.Resource;

public interface ResourceStoring {
	public Resource getStored(LocalElement element, long duration);
	public Resource getRetrieved(LocalElement element, long duration);
	public void store(LocalElement element, Resource stored, Resource retrieved);
}
