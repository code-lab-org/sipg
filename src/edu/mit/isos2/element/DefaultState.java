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

	public Resource getInput(long duration) {
		return ResourceFactory.createResource();
	}

	public Resource getOutput(long duration) {
		return ResourceFactory.createResource();
	}

	public Resource getProduced(long duration) {
		return ResourceFactory.createResource();
	}

	public Resource getConsumed(long duration) {
		return ResourceFactory.createResource();
	}

	public Resource getStored(long duration) {
		return ResourceFactory.createResource();
	}

	public Resource getRetrieved(long duration) {
		return ResourceFactory.createResource();
	}
	
	public void iterateTick(Element element, long duration) { }
	public void iterateTock() { }
}
