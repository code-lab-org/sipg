package edu.mit.isos2.state;

import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;

public interface ResourceTransforming {
	public Resource getProduced(long duration);
	public Resource getConsumed(long duration);
	public void transform(Element element, Resource consumed, Resource produced);
}
