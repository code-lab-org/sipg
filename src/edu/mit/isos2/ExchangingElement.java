package edu.mit.isos2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceType;

public class ExchangingElement extends DefaultElement {
	private Map<ResourceType, Element> suppliers = new HashMap<ResourceType, Element>();
	private Set<Element> customers = new HashSet<Element>();
	private Map<Element, Resource> demand = new HashMap<Element, Resource>();
	private Map<Element, Resource> nextDemand = new HashMap<Element, Resource>();
	private Map<Element, Resource> supply = new HashMap<Element, Resource>();
	private Map<Element, Resource> nextSupply = new HashMap<Element, Resource>();
	
	protected ExchangingElement() {
		super();
	}
	
	public ExchangingElement(String name, Location initialLocation) {
		super(name, initialLocation);
	}
	
	public void setSupplier(ResourceType type, Element supplier) {
		suppliers.put(type, supplier);
	}
	
	public void addCustomer(Element customer) {
		customers.add(customer);
	}

	@Override
	public final Resource getSendingRateTo(Element element) {
		Resource sendingRate = ResourceFactory.createResource();
		if(customers.contains(element)) {
			sendingRate = sendingRate.add(demand.get(element));
		}
		return sendingRate;
	}
	
	@Override
	public final Resource getSendingRate() {
		Resource sendingRate = ResourceFactory.createResource();
		for(Element customer : customers) {
			sendingRate = sendingRate.add(getSendingRateTo(customer));
		}
		return sendingRate;
	}
	
	@Override
	public final Resource getReceivingRateFrom(Element element) {
		Resource receivingRate = ResourceFactory.createResource();
		if(suppliers.containsValue(element)) {
			for(ResourceType t : ResourceType.values()) {
				if(suppliers.get(t) != null && suppliers.get(t).equals(element)) {
					receivingRate = receivingRate.add(getReceivingRate()).get(t);
				}
			}
		}
		return receivingRate;
	}
	
	@Override
	public Resource getReceivingRate() {
		return ResourceFactory.createResource();
	}
	
	@Override
	public void initialize(long initialTime) {
		super.initialize(initialTime);
		demand.clear();
		nextDemand.clear();
		for(Element customer : customers) {
			demand.put(customer, ResourceFactory.createResource());
			nextDemand.put(customer, ResourceFactory.createResource());
		}
		supply.clear();
		nextSupply.clear();
		for(Element supplier : suppliers.values()) {
			supply.put(supplier, ResourceFactory.createResource());
			nextSupply.put(supplier, ResourceFactory.createResource());
		}
	}
	
	@Override
	public final void iterateTick() {
		nextDemand.clear();
		for(Element customer : customers) {
			nextDemand.put(customer, customer.getReceivingRateFrom(this));
		}
		nextSupply.clear();
		for(Element supplier : suppliers.values()) {
			nextSupply.put(supplier, supplier.getSendingRateTo(this));
		}
	}
	
	@Override
	public void iterateTock() {
		demand.clear();
		demand.putAll(nextDemand);
		supply.clear();
		supply.putAll(nextSupply);
	}
}