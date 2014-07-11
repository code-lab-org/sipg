package edu.mit.isos2.state;

import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceMatrix;

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
	public Resource getProduced(long duration) {
		return productionRate.multiply(duration);
	}
	
	public void setProductionRate(Resource productionRate) {
		nextProductionRate = productionRate;
	}
	
	public Resource getProductionRate() {
		return productionRate;
	}

	@Override
	public Resource getConsumed(long duration) {
		return super.getConsumed(duration).add(consumptionMatrix.multiply(getProduced(duration)));
	}
	
	public void initialize(Element element, long initialTime) {
		super.initialize(element, initialTime);
		productionRate = nextProductionRate = initialProductionRate;
	}

	@Override
	public void iterateTock() { 
		super.iterateTock();
		productionRate = nextProductionRate;
	}
}