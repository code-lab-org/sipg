package edu.mit.isos2.state;

import edu.mit.isos2.Location;
import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class EmptyState implements State, ElementTransforming {
	private long stateChangeTime;
	private State nextState;
	
	private long time;
	private transient long nextTime;
	
	public EmptyState(long stateChangeTime, State nextState) { 
		this.stateChangeTime = stateChangeTime;
		this.nextState = nextState;
	}
	
	public String getName() {
		return "Empty";
	}
	
	public String toString() {
		return getName();
	}
	
	public long getStateChangeTime() {
		return stateChangeTime;
	}
	
	public State getNextState() {
		return nextState;
	}
	
	public void initialize(Element element, long initialTime) {
		if(!element.getStates().contains(this)) {
			throw new IllegalStateException(
					"Element does not contain state " + this);
		}
		if(!element.getStates().contains(nextState)) {
			throw new IllegalStateException(
					"Element does not contain next state " + nextState);
		}
		time = nextTime = initialTime;
	}
	
	public void tick(Element element, long duration) {
		nextTime = time + duration;

		if(element.getState().equals(this)
				&& nextTime > stateChangeTime) {
			transform(element, nextState);
		}
	}
	
	public void tock() {
		time = nextTime;
	}

	@Override
	public void iterateTick(Element element, long duration) { }

	@Override
	public void iterateTock() { }

	@Override
	public void transform(Element element, State nextState) {
		element.setState(nextState);
	}

	@Override
	public Resource getNetFlow(Element element, Location location, long duration) {
		return ResourceFactory.create();
	}

	@Override
	public Resource getNetExchange(Element element1, Element element2,
			long duration) {
		return ResourceFactory.create();
	}
}
