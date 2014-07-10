package edu.mit.isos2.element;

import java.util.Collection;

import edu.mit.isos2.Location;
import edu.mit.isos2.resource.Resource;

public interface Element {
	public Element states(Collection<? extends State> states);
	public Element initialContents(Resource initialContents);
	public Element initialState(State initialState);
	public Element initialParent(Element initialParent);

	public String getName();
	public Collection<? extends State> getStates();
	
	public Resource getContents();
	public Location getLocation();
	public Element getParent();
	public State getState();

	public void initialize(long initialTime);
	public void iterateTick(long duration);
	public void iterateTock();
	public void tick(long duration);
	public void tock();
	
	public void addContents(Resource resource);
	public void removeContents(Resource resource);
	public void setState(State state);
	public void setParent(Element parent);
	public void setLocation(Location location);
	
	public Resource getNetFlow(Location location, long duration);
	public Resource getNetExchange(Element element, long duration);
}
