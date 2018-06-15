package edu.mit.isos.core.state;

import edu.mit.isos.core.context.Resource;
import edu.mit.isos.core.element.LocalElement;

public interface ResourceTransporting {
	public Resource getInput(LocalElement element, long duration);
	public Resource getOutput(LocalElement element, long duration);
	public void transport(LocalElement element, Resource input, Resource output);
}
