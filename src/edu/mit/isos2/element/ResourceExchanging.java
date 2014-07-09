package edu.mit.isos2.element;

import edu.mit.isos2.resource.Resource;

public interface ResourceExchanging {
	public Resource getSent(long duration);
	public Resource getSentTo(Element element, long duration);
	public Resource getReceived(long duration);
	public Resource getReceivedFrom(Element element, long duration);
}
