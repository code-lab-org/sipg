package edu.mit.isos2.element;

import edu.mit.isos2.resource.Resource;

public interface ResourceTransporting {
	public Resource getInput(long duration);
	public Resource getOutput(long duration);
}
