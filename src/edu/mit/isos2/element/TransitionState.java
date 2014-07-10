package edu.mit.isos2.element;

import edu.mit.isos2.Location;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class TransitionState implements State, ResourceTransforming, ElementTransforming {
	private String name;
	private long timeInState;
	private State nextState;
	
	private long initialDuration, duration;
	private transient long nextDuration;
	
	private Resource totalExpense = ResourceFactory.create();
	
	protected TransitionState() { 
		this("Transitioning", ResourceFactory.create(), 0, null);
	}
	
	public TransitionState(String name, Resource totalExpense, long timeInState, State nextState) {
		this.name = name;
		this.totalExpense = totalExpense;
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
	
	public Resource getTotalExpense() {
		return totalExpense;
	}
	
	@Override
	public Resource getConsumed(long duration) {
		if(timeInState > 0) {
			long remaining = duration;
			if(this.duration + duration > timeInState) {
				remaining = timeInState-this.duration;
			}
			return totalExpense.multiply(remaining/(double)timeInState);
		}
		return ResourceFactory.create();
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
			transform(element, getConsumed(duration), getProduced(duration));
		} else {
			nextDuration = 0;
		}
	}
	
	public void tock() {
		duration = nextDuration;
	}

	@Override
	public Resource getProduced(long duration) {
		return ResourceFactory.create();
	}

	@Override
	public void iterateTick(Element element, long duration) { }

	@Override
	public void iterateTock() { }

	@Override
	public void transform(Element element, Resource consumed, Resource produced) {
		// no longer modifies element contents
		// element.add(produced);
		// element.remove(consumed);
	}
	
	@Override
	public void transform(Element element, State nextState) {
		element.setState(nextState);
	}

	@Override
	public Resource getNetFlow(Element element, Location location, long duration) {
		Resource netFlow = ResourceFactory.create();
		if(element.getLocation().equals(location)) {
			netFlow = netFlow.add(getProduced(duration)).subtract(getConsumed(duration));
		}
		return netFlow;
	}

	@Override
	public Resource getNetExchange(Element element1, Element element2,
			long duration) {
		return ResourceFactory.create();
	}
}
