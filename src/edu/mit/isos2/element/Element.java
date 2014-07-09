package edu.mit.isos2.element;

import edu.mit.isos2.Location;
import edu.mit.isos2.resource.Resource;

public interface Element {
	public Element initialContents(Resource initialContents);
	public Element initialState(State initialState);
	public Element initialParent(Element initialParent);
	
	public Resource getContents();
	public Location getLocation();
	public Element getParent();
	public String getName();
	public State getState();

	public void initialize(long initialTime);
	public void iterateTick(long duration);
	public void iterateTock();
	public void tick(long duration);
	public void tock();
	
	public void store(Resource stored, Resource retrieved);
	public void transform(Resource consumed, Resource produced);
	public void transport(Resource input, Resource output);
	public void exchange(Resource sent, Resource received);
	
	public void transport(Location nextLocation);
	public void transform(State nextState);
	public void store(Element nextParent);
}
