package edu.mit.isos2.state;

import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;

public interface ResourceTransporting {
	public Resource getInput(Element element, long duration);
	public Resource getOutput(Element element, long duration);
	public void transport(Element element, Resource input, Resource output);
}
