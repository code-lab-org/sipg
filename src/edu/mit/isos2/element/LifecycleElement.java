package edu.mit.isos2.element;

import java.util.Arrays;

import edu.mit.isos2.Location;
import edu.mit.isos2.state.EmptyState;
import edu.mit.isos2.state.NullState;
import edu.mit.isos2.state.OperatingState;
import edu.mit.isos2.state.TransitioningState;

public class LifecycleElement extends DefaultElement {	
	private EmptyState emptyState;
	private TransitioningState commissioningState;
	private OperatingState operatingState;
	private TransitioningState decommissioningState;
	private NullState nullState;
	
	protected LifecycleElement() {
		super();
	}
	
	protected LifecycleElement(String name, Location initialLocation,
			EmptyState emptyState, TransitioningState commissioningState,
			OperatingState operatingState, 
			TransitioningState decommissioningState, NullState nullState) {
		super(name, initialLocation);
		this.emptyState = emptyState;
		this.commissioningState = commissioningState;
		this.operatingState = operatingState;
		this.decommissioningState = decommissioningState;
		this.nullState = nullState;

		states(Arrays.asList(emptyState, commissioningState, 
				operatingState, decommissioningState, nullState));
		initialState(emptyState);
	}
	
	@Override
	public void initialize(long initialTime) {
		if(initialTime < emptyState.getStateChangeTime()) {
			initialState(emptyState);
		} else if(initialTime < emptyState.getStateChangeTime() 
				+ commissioningState.getTimeInState()) {
			initialState(commissioningState);
		} else if(initialTime < emptyState.getStateChangeTime()
				+ commissioningState.getTimeInState()
				+ operatingState.getTimeInState()) {
			initialState(operatingState);
		} else if(initialTime < emptyState.getStateChangeTime()
				+ commissioningState.getTimeInState()
				+ operatingState.getTimeInState()
				+ decommissioningState.getTimeInState()) {
			initialState(decommissioningState);
		} else {
			initialState(nullState);
		}
		super.initialize(initialTime);
	}
}
