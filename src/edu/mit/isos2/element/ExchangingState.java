package edu.mit.isos2.element;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceType;

public class ExchangingState extends DefaultState implements ResourceExchanging {
	private Map<ResourceType, Element> suppliers = new HashMap<ResourceType, Element>();
	private Set<Element> customers = new HashSet<Element>();
	private Map<Element, Resource> demand = new HashMap<Element, Resource>();
	private Map<Element, Resource> nextDemand = new HashMap<Element, Resource>();
	private Map<Element, Resource> supply = new HashMap<Element, Resource>();
	private Map<Element, Resource> nextSupply = new HashMap<Element, Resource>();
	
	protected ExchangingState() {
		super();
	}
	
	public ExchangingState(String name) {
		super(name);
	}
	
	public void setSupplier(ResourceType type, Element supplier) {
		suppliers.put(type, supplier);
	}
	
	public void addCustomer(Element customer) {
		customers.add(customer);
	}

	@Override
	public final Resource getSentTo(Element element, long duration) {
		Resource sent = ResourceFactory.createResource();
		if(customers.contains(element)) {
			if(demand.get(element) != null) {
				sent = sent.add(demand.get(element));
			}
		}
		return sent;
	}
	
	@Override
	public final Resource getSent(long duration) {
		Resource sent = ResourceFactory.createResource();
		for(Element customer : customers) {
			sent = sent.add(getSentTo(customer, duration));
		}
		return sent;
	}
	
	@Override
	public final Resource getReceivedFrom(Element element, long duration) {
		Resource received = ResourceFactory.createResource();
		if(suppliers.containsValue(element)) {
			for(ResourceType t : ResourceType.values()) {
				if(suppliers.get(t) != null && suppliers.get(t).equals(element)) {
					received = received.add(getReceived(duration)).get(t);
				}
			}
		}
		return received;
	}
	
	@Override
	public Resource getReceived(long duration) {
		return ResourceFactory.createResource();
	}
	
	@Override
	public final void iterateTick(Element element, long duration) {
		nextDemand.clear();
		for(Element customer : customers) {
			if(customer.getState() instanceof ResourceExchanging) {
				ResourceExchanging rex = (ResourceExchanging) customer.getState();
				nextDemand.put(customer, rex.getReceivedFrom(element, duration));
			}
		}
		nextSupply.clear();
		for(Element supplier : suppliers.values()) {
			if(supplier.getState() instanceof ResourceExchanging) {
				ResourceExchanging rex = (ResourceExchanging) supplier.getState();
				nextSupply.put(supplier, rex.getSentTo(element, duration));
			}
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