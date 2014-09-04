package edu.mit.isos3.element.state;

import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceMatrix;

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
	public Resource getOutput(LocalElement element, long duration) {
		return outputRate.multiply(duration);
	}
	
	public void setOutputRate(Resource outputRate) {
		nextOutputRate = outputRate;
	}
	
	public Resource getOutputRate() {
		return outputRate;
	}
	
	@Override
	public Resource getConsumed(LocalElement element, long duration) {
		return getInput(element, duration).subtract(getOutput(element, duration));
	}

	@Override
	public Resource getInput(LocalElement element, long duration) {
		return super.getInput(element, duration).add(inputMatrix.multiply(getOutput(element, duration))).add(getOutput(element, duration));
	}
	
	public void initialize(LocalElement element, long initialTime) {
		super.initialize(element, initialTime);
		outputRate = nextOutputRate = initialOutputRate;
	}

	@Override
	public void iterateTock() { 
		super.iterateTock();
		outputRate = nextOutputRate;
	}
}
