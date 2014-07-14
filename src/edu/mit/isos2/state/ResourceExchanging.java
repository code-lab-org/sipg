package edu.mit.isos2.state;

import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;

public interface ResourceExchanging {
	public Resource getSent(Element element, long duration);
	public Resource getSentTo(Element element1, Element element2, long duration);
	public Resource getReceived(Element element, long duration);
	public Resource getReceivedFrom(Element element1, Element element2, long duration);
	public void exchange(Element element1, Element element2, Resource sent, Resource received);
}
