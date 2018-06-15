package edu.mit.isos3.element.state;

import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.resource.Resource;

public interface ResourceTransporting {
	public Resource getInput(LocalElement element, long duration);
	public Resource getOutput(LocalElement element, long duration);
	public void transport(LocalElement element, Resource input, Resource output);
}
