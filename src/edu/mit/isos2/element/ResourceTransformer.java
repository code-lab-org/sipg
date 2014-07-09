package edu.mit.isos2.element;

import edu.mit.isos2.resource.Resource;

public interface ResourceTransformer extends Element {
	public Resource getProductionRate();
	public Resource getConsumptionRate();
}
