package edu.mit.isos3.element.state;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.Element;
import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;

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
	
	public void initialize(LocalElement element, long initialTime) {
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
	
	public void tick(LocalElement element, long duration) {
		nextTime = time + duration;

		if(element.getState().equals(this)
				&& nextTime >= stateChangeTime) {
			transform(element, nextState);
		}
	}
	
	public void tock() {
		time = nextTime;
	}

	@Override
	public void iterateTick(LocalElement element, long duration) { }

	@Override
	public void iterateTock() { }

	@Override
	public void transform(LocalElement element, State nextState) {
		element.setState(nextState);
	}

	@Override
	public Resource getNetFlow(LocalElement element, Location location, long duration) {
		return ResourceFactory.create();
	}

	@Override
	public Resource getNetExchange(LocalElement element1, Element element2,
			long duration) {
		return ResourceFactory.create();
	}
}
