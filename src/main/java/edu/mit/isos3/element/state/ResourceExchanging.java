package edu.mit.isos3.element.state;

import edu.mit.isos3.element.Element;
import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.resource.Resource;

public interface ResourceExchanging {
	public Resource getSent(LocalElement element, long duration);
	public Resource getSentTo(LocalElement element1, Element element2, long duration);
	public Resource getReceived(LocalElement element, long duration);
	public Resource getReceivedFrom(LocalElement element1, Element element2, long duration);
	public void exchange(LocalElement element1, Element element2, Resource sent, Resource received);
}
