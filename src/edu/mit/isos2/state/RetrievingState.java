package edu.mit.isos2.state;

import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceMatrix;

public class RetrievingState extends OperatingState {
	private ResourceMatrix consumptionMatrix = new ResourceMatrix();
	
	private Resource retrievalRate = ResourceFactory.create();
	private Resource initialRetrievalRate = ResourceFactory.create();
	private Resource nextRetrievalRate = ResourceFactory.create();
	
	protected RetrievingState() {
		super();
	}
	
	public RetrievingState(String name, long timeInState, State nextState) {
		super(name, timeInState, nextState);
	}
	
	public RetrievingState initialRetrievalRate(Resource initialRetrievalRate) {
		this.initialRetrievalRate = initialRetrievalRate;
		return this;
	}
	
	public RetrievingState consumptionMatrix(ResourceMatrix consumptionMatrix) {
		this.consumptionMatrix = consumptionMatrix;
		return this;
	}
	
	@Override
	public Resource getRetrieved(long duration) {
		return retrievalRate.multiply(duration);
	}
	
	public void setRetrievalRate(Resource retrievalRate) {
		nextRetrievalRate = retrievalRate;
	}
	
	public Resource getRetrievalRate() {
		return retrievalRate;
	}

	@Override
	public Resource getConsumed(long duration) {
		return super.getConsumed(duration).add(consumptionMatrix.multiply(getRetrieved(duration)));
	}
	
	public void initialize(Element element, long initialTime) {
		super.initialize(element, initialTime);
		retrievalRate = nextRetrievalRate = initialRetrievalRate;
	}

	@Override
	public void iterateTock() { 
		super.iterateTock();
		retrievalRate = nextRetrievalRate;
	}
}
