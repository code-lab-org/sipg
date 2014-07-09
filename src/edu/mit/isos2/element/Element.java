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

	/*
	public Resource getInputRate();
	public Resource getOutputRate();
	public Resource getProductionRate();
	public Resource getConsumptionRate();
	public Resource getStorageRate();
	public Resource getRetrievalRate();
	public Resource getSendingRate();
	public Resource getSendingRateTo(Element element);
	public Resource getReceivingRate();
	public Resource getReceivingRateFrom(Element element);
	
	protected void storeResources(Resource stored, Resource retrieved);
	protected void transformResources(Resource consumed, Resource produced);
	protected void transportResources(Resource input, Resource output);
	protected void exchangeResources(Resource sent, Resource received);
	
	protected void transformElement(State state);
	protected void transportElement(Location location);
	protected void storeElement(Element parent);
	*/
	
	public void initialize(long initialTime);
	public void iterateTick();
	public void iterateTock();
	public void tick(long duration);
	public void tock();
}
