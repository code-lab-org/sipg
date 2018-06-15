package edu.mit.isos3.element.state;

import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceMatrix;

public class RetrievingState extends OperatingState {
	private ResourceMatrix consumptionMatrix = new ResourceMatrix();
	
	private Resource retrieved = ResourceFactory.create();
	
	protected RetrievingState() {
		super();
	}
	
	public RetrievingState(String name, long timeInState, State nextState) {
		super(name, timeInState, nextState);
	}
	
	public RetrievingState consumptionMatrix(ResourceMatrix consumptionMatrix) {
		this.consumptionMatrix = consumptionMatrix;
		return this;
	}
	
	@Override
	public Resource getRetrieved(LocalElement element, long duration) {
		return retrieved;
	}
	
	public void setRetrieved(Resource retrieved) {
		this.retrieved = retrieved;
	}
	
	public Resource getRetrieved() {
		return retrieved;
	}

	@Override
	public Resource getConsumed(LocalElement element, long duration) {
		return super.getConsumed(element, duration).add(consumptionMatrix.multiply(getRetrieved(element, duration)));
	}
	
	public void initialize(LocalElement element, long initialTime) {
		super.initialize(element, initialTime);
		retrieved = ResourceFactory.create();
	}

	@Override
	public void tick(LocalElement element, long duration) { 
		super.tick(element, duration);
		retrieved = ResourceFactory.create();
	}
}
