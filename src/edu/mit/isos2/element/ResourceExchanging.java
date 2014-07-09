package edu.mit.isos2.element;

import edu.mit.isos2.resource.Resource;

public interface ResourceExchanging {
	public Resource getSendingRate();
	public Resource getSendingRateTo(Element element);
	public Resource getReceivingRate();
	public Resource getReceivingRateFrom(Element element);
}
