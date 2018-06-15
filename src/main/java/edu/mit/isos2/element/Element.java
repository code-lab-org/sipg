package edu.mit.isos2.element;

import java.util.Collection;

import edu.mit.isos2.Location;
import edu.mit.isos2.SimEntity;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.state.State;

public interface Element extends SimEntity {
	public String getName();
	public Collection<? extends State> getStates();
	
	public Resource getInitialContents();
	public Location getInitialLocation();
	public Element getInitialParent();
	public State getInitialState();
	
	public Resource getContents();
	public Location getLocation();
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
