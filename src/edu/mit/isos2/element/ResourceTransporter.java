package edu.mit.isos2.element;

import edu.mit.isos2.resource.Resource;

public interface ResourceTransporter extends Element {
	public Resource getInputRate();
	public Resource getOutputRate();
}
