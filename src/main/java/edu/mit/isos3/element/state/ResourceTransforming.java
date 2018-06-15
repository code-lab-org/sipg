package edu.mit.isos3.element.state;

import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.resource.Resource;

public interface ResourceTransforming {
	public Resource getProduced(LocalElement element, long duration);
	public Resource getConsumed(LocalElement element, long duration);
	public void transform(LocalElement element, Resource consumed, Resource produced);
}
