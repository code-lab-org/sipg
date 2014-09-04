package edu.mit.isos3.element.state;

import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;


public class OperatingState extends DefaultState {
	private Resource fixedExpense = ResourceFactory.create();
	
	private long timeInState;
	private State nextState;
	
	private long initialDuration, duration;
	private transient long nextDuration;
	
	protected OperatingState() {
		this("Operating", 0, null);
	}
	
	public OperatingState(String name, long timeInState, State nextState) {
		super(name);
		this.timeInState = timeInState;
		this.nextState = nextState;
	}
	
	public OperatingState fixedExpense(Resource fixedExpense) {
		this.fixedExpense = fixedExpense;
		return this;
	}
	
	public Resource getExpense(long duration) {
		return fixedExpense;
	}
	
	public long getTimeInState() {
		return timeInState;
	}
	
	public State getNextState() {
		return nextState;
	}
	
	public void initialize(LocalElement element, long initialTime) {
		super.initialize(element, initialTime);
		if(!element.getStates().contains(nextState)) {
			throw new IllegalStateException(
					"Element does not contain next state " + nextState);
		}
		duration = nextDuration = initialDuration;
	}
	
	public void tick(LocalElement element, long duration) {
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
	
	public void tock() {
		duration = nextDuration;
	}
	
	@Override
	public Resource getInput(LocalElement element, long duration) {
		return getExpense(duration);
	}
	
	@Override
	public Resource getConsumed(LocalElement element, long duration) {
		return getExpense(duration);
	}
}
