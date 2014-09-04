package edu.mit.isos3.element;

import java.util.Collection;

import edu.mit.isos3.Location;
import edu.mit.isos3.SimEntity;
import edu.mit.isos3.element.Element;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.element.state.State;

public interface LocalElement extends Element, SimEntity {
	public Collection<? extends State> getStates();
	
	public Resource getInitialContents();
	public Location getInitialLocation();
	public Element getInitialParent();
	public State getInitialState();
	
	public Resource getContents();
	public Element getParent();
	public State getState();
	
	public void addContents(Resource resource);
	public void removeContents(Resource resource);
	public void setState(State state);
	public void setParent(Element parent);
	public void setLocation(Location location);
	
	public Resource getNetFlow(Location location, long duration);
	public Resource getNetExchange(Element element, long duration);
}
