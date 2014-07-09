package edu.mit.isos2.element;

import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class DefaultState implements State, ResourceStoring, ResourceTransforming, ResourceTransporting {
	private final String name;
	
	public DefaultState() {
		this.name = "";
	}
	
	public DefaultState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}

	public Resource getInputRate() {
		return ResourceFactory.createResource();
	}

	public Resource getOutputRate() {
		return ResourceFactory.createResource();
	}

	public Resource getProductionRate() {
		return ResourceFactory.createResource();
	}

	public Resource getConsumptionRate() {
		return ResourceFactory.createResource();
	}

	public Resource getStorageRate() {
		return ResourceFactory.createResource();
	}

	public Resource getRetrievalRate() {
		return ResourceFactory.createResource();
	}
	
	public void iterateTick(Element element) { }
	public void iterateTock() { }
}
