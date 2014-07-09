package edu.mit.isos2;

import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class DefaultElement extends Element {
	protected DefaultElement() {
		super();
	}
	
	public DefaultElement(String name, Location initialLocation) {
		super(name, initialLocation);
	}

	@Override
	public Resource getInputRate() {
		return ResourceFactory.createResource();
	}
	@Override
	public Resource getOutputRate() {
		return ResourceFactory.createResource();
	}
	@Override
	public Resource getProductionRate() {
		return ResourceFactory.createResource();
	}
	@Override
	public Resource getConsumptionRate() {
		return ResourceFactory.createResource();
	}
	@Override
	public Resource getStorageRate() {
		return ResourceFactory.createResource();
	}
	@Override
	public Resource getRetrievalRate() {
		return ResourceFactory.createResource();
	}
	@Override
	public Resource getSendingRate() {
		return ResourceFactory.createResource();
	}
	@Override
	public Resource getSendingRateTo(Element element) {
		return ResourceFactory.createResource();
	}
	@Override
	public Resource getReceivingRate() {
		return ResourceFactory.createResource();
	}
	@Override
	public Resource getReceivingRateFrom(Element element) {
		return ResourceFactory.createResource();
	}

}
