package edu.mit.isos2.element;

import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceMatrix;

public class DistributingState extends OperatingState implements ResourceStoring, ResourceTransporting, ResourceTransforming {
	private ResourceMatrix inputMatrix = new ResourceMatrix();
	
	private Resource outputRate = ResourceFactory.create();
	private Resource initialOutputRate = ResourceFactory.create();
	private Resource nextOutputRate = ResourceFactory.create();
	
	protected DistributingState() {
		super();
	}
	
	public DistributingState(String name, long timeInState, State nextState) {
		super(name, timeInState, nextState);
	}
	
	public DistributingState initialOutputRate(Resource initialOutputRate) {
		this.initialOutputRate = initialOutputRate;
		return this;
	}
	
	public DistributingState inputMatrix(ResourceMatrix inputMatrix) {
		this.inputMatrix = inputMatrix;
		return this;
	}
	
	@Override
	public Resource getOutput(long duration) {
		return outputRate.multiply(duration);
	}
	
	public void setOutputRate(Resource outputRate) {
		nextOutputRate = outputRate;
	}
	
	public Resource getOutputRate() {
		return outputRate;
	}
	
	@Override
	public Resource getConsumed(long duration) {
		return super.getConsumed(duration).add(getInput(duration).subtract(getOutput(duration)));
	}

	@Override
	public Resource getInput(long duration) {
		return inputMatrix.multiply(getOutput(duration));
	}
	
	public void initialize(Element element, long initialTime) {
		super.initialize(element, initialTime);
		outputRate = nextOutputRate = initialOutputRate;
	}

	@Override
	public void iterateTock() { 
		super.iterateTock();
		outputRate = nextOutputRate;
	}
}
