package edu.mit.isos2.element;

import edu.mit.isos2.resource.Resource;

public interface ResourceTransforming {
	public Resource getProduced(long duration);
	public Resource getConsumed(long duration);
}
