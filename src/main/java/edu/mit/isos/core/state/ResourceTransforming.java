package edu.mit.isos.core.state;

import edu.mit.isos.core.context.Resource;
import edu.mit.isos.core.element.LocalElement;

public interface ResourceTransforming {
	public Resource getProduced(LocalElement element, long duration);
	public Resource getConsumed(LocalElement element, long duration);
	public void transform(LocalElement element, Resource consumed, Resource produced);
}
