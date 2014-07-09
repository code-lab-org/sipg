package edu.mit.isos2.element;

import edu.mit.isos2.resource.Resource;

public interface ResourceExchanger extends Element {
	public Resource getSendingRate();
	public Resource getSendingRateTo(ResourceExchanger element);
	public Resource getReceivingRate();
	public Resource getReceivingRateFrom(ResourceExchanger element);
}
