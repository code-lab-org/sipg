package edu.mit.isos2;

import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class SupplierElement extends DefaultElement {
	private Element customer;
	private Resource demand;
	private transient Resource nextDemand;
	
	protected SupplierElement() {
		super();
	}
	
	public SupplierElement(String name, Location initialLocation) {
		super(name, initialLocation);
	}
	
	public void setCustomer(Element customer) {
		this.customer = customer;
	}
	
	@Override
	public Resource getSendingRate() {
		return getSendingRateTo(customer);
	}
	
	@Override
	public Resource getSendingRateTo(Element element) {
		if(element.equals(customer)) {
			return demand;
		} else {
			return super.getSendingRateTo(element);
		}
	}
	
	@Override
	public void initialize(long initialTime) {
		super.initialize(initialTime);
		demand = nextDemand = ResourceFactory.createResource();
	}
	
	@Override
	public void iterateTick() {
		nextDemand = customer.getReceivingRateFrom(this);
	}
	
	@Override
	public void iterateTock() {
		demand = nextDemand;
	}
}