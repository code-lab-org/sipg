package edu.mit.isos.core.state;

import edu.mit.isos.core.context.Resource;
import edu.mit.isos.core.element.LocalElement;

public interface ResourceStoring {
	public Resource getStored(LocalElement element, long duration);
	public Resource getRetrieved(LocalElement element, long duration);
	public void store(LocalElement element, Resource stored, Resource retrieved);
}
