package edu.mit.isos2.element;

import edu.mit.isos2.Location;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class OperatingState implements State, ElementTransforming {
	private String name;
	
	private long timeInState;
	private State nextState;
	
	private long initialDuration, duration;
	private transient long nextDuration;
	
	protected OperatingState() {
		this("Operating", 0, null);
	}
	
	public OperatingState(String name, long timeInState, State nextState) {
		this.name = name;
		this.timeInState = timeInState;
		this.nextState = nextState;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	public long getTimeInState() {
		return timeInState;
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
		duration = nextDuration = initialDuration;
	}
	
	public void tick(Element element, long duration) {
		if(element.getState().equals(this)) {
			nextDuration = this.duration + duration;
			if(nextDuration >= timeInState) {
				transform(element, nextState);
			}
		} else {
			nextDuration = 0;
		}
	}
	
	public void tock() {
		duration = nextDuration;
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
