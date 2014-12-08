package edu.mit.isos.core.state;

import edu.mit.isos.core.context.Resource;
import edu.mit.isos.core.element.Element;
import edu.mit.isos.core.element.LocalElement;

public interface ResourceExchanging {
	public Resource getSent(LocalElement element, long duration);
	public Resource getSentTo(LocalElement element1, Element element2, long duration);
	public Resource getReceived(LocalElement element, long duration);
	public Resource getReceivedFrom(LocalElement element1, Element element2, long duration);
	public void exchange(LocalElement element1, Element element2, Resource sent, Resource received);
}
