package edu.mit.isos2.element;

import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class TransitioningState extends DefaultState {
	private long timeInState;
	private State nextState;
	
	private long initialDuration, duration;
	private transient long nextDuration;
	
	private Resource totalExpense = ResourceFactory.create();
	
	protected TransitioningState() { 
		this("Transitioning", ResourceFactory.create(), 0, null);
	}
	
	public TransitioningState(String name, Resource totalExpense, long timeInState, State nextState) {
		super(name);
		this.totalExpense = totalExpense;
		this.timeInState = timeInState;
		this.nextState = nextState;
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
	
	private Resource getExpense(long duration) {
		if(timeInState > 0) {
			long remaining = duration;
			if(this.duration + duration > timeInState) {
				remaining = timeInState-this.duration;
			}
			return totalExpense.multiply(remaining/(double)timeInState);
		}
		return ResourceFactory.create();
	}
	
	@Override
	public Resource getConsumed(long duration) {
		return getExpense(duration);
	}

	@Override
	public void initialize(Element element, long initialTime) {
		super.initialize(element, initialTime);
		if(!element.getStates().contains(nextState)) {
			throw new IllegalStateException(
					"Element does not contain next state " + nextState);
		}
		duration = nextDuration = initialDuration;
	}

	@Override
	public void tick(Element element, long duration) {
		super.tick(element, duration);
		if(equals(element.getState())) {
			nextDuration = this.duration + duration;
			if(nextDuration >= timeInState) {
				transform(element, nextState);
			}
		} else {
			nextDuration = 0;
		}
	}

	@Override
	public void tock() {
		super.tock();
		duration = nextDuration;
	}
}
