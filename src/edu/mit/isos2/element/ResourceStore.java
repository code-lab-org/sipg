package edu.mit.isos2.element;

import edu.mit.isos2.resource.Resource;

public interface ResourceStore extends Element {
	public Resource getStorageRate();
	public Resource getRetrievalRate();
}
