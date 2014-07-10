package edu.mit.isos2.element;

import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceMatrix;

public class LinearState extends DefaultState {
	public Resource inputRate = ResourceFactory.create();
	public Resource outputRate = ResourceFactory.create();
	public Resource storageRate = ResourceFactory.create();
	public Resource retrievalRate = ResourceFactory.create();
	public Resource productionRate = ResourceFactory.create();
	public Resource consumptionRate = ResourceFactory.create();

	public ResourceMatrix inputForOutput = new ResourceMatrix();
	public ResourceMatrix consumeForProduction = new ResourceMatrix();
	
	public boolean storeAllConsumed = false;
	public boolean retrieveAllProduced = false;
	public boolean outputAllProduced = false;
	
	protected LinearState() {
		super();
	}

	public LinearState(String name) {
		super(name);
	}

	public Resource getConsumed(long duration) {
		return consumptionRate.multiply(duration)
				.add(consumeForProduction.multiply(productionRate).multiply(duration));
	}

	public Resource getInput(long duration) {
		return inputRate.multiply(duration)
				.add(inputForOutput.multiply(outputRate).multiply(duration));
	}
	public Resource getOutput(long duration) {
		if(outputAllProduced) {
			return getProduced(duration).add(retrievalRate.multiply(duration));
		}
		return outputRate.multiply(duration);
	}

	public Resource getProduced(long duration) {
		return productionRate.multiply(duration);
	}

	public Resource getRetrieved(long duration) {
		if(retrieveAllProduced) {
			return getProduced(duration).add(retrievalRate.multiply(duration));
		}
		return retrievalRate.multiply(duration);
	}
	
	public Resource getStored(long duration) {
		if(storeAllConsumed) {
			return getConsumed(duration).add(storageRate.multiply(duration));
		}
		return storageRate.multiply(duration);
	}
}