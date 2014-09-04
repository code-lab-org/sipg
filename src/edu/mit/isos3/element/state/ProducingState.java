package edu.mit.isos3.element.state;

import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceMatrix;

public class ProducingState extends OperatingState {
	private ResourceMatrix consumptionMatrix = new ResourceMatrix();
	
	private Resource productionRate = ResourceFactory.create();
	private Resource initialProductionRate = ResourceFactory.create();
	private Resource nextProductionRate = ResourceFactory.create();
	
	protected ProducingState() {
		super();
	}
	
	public ProducingState(String name, long timeInState, State nextState) {
		super(name, timeInState, nextState);
	}
	
	public ProducingState initialProductionRate(Resource initialProductionRate) {
		this.initialProductionRate = initialProductionRate;
		return this;
	}
	
	public ProducingState consumptionMatrix(ResourceMatrix consumptionMatrix) {
		this.consumptionMatrix = consumptionMatrix;
		return this;
	}
	
	@Override
	public Resource getProduced(LocalElement element, long duration) {
		return productionRate.multiply(duration);
	}
	
	public void setProductionRate(Resource productionRate) {
		nextProductionRate = productionRate;
	}
	
	public Resource getProductionRate() {
		return productionRate;
	}

	@Override
	public Resource getInput(LocalElement element, long duration) {
		return getConsumed(element, duration);
	}
	
	@Override
	public Resource getOutput(LocalElement element, long duration) {
		return getProduced(element, duration);
	}

	@Override
	public Resource getConsumed(LocalElement element, long duration) {
		return super.getConsumed(element, duration).add(consumptionMatrix.multiply(getProduced(element, duration)));
	}
	
	public void initialize(LocalElement element, long initialTime) {
		super.initialize(element, initialTime);
		productionRate = nextProductionRate = initialProductionRate;
	}

	@Override
	public void iterateTock() { 
		super.iterateTock();
		productionRate = nextProductionRate;
	}
}
