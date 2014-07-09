package edu.mit.isos2.element;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.mit.isos2.Location;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceType;

public class ExchangingElement extends DefaultElement implements ResourceExchanger {
	private Map<ResourceType, ResourceExchanger> suppliers = 
			new HashMap<ResourceType, ResourceExchanger>();
	private Set<ResourceExchanger> customers = 
			new HashSet<ResourceExchanger>();
	private Map<ResourceExchanger, Resource> demand = 
			new HashMap<ResourceExchanger, Resource>();
	private Map<ResourceExchanger, Resource> nextDemand = 
			new HashMap<ResourceExchanger, Resource>();
	private Map<ResourceExchanger, Resource> supply = 
			new HashMap<ResourceExchanger, Resource>();
	private Map<ResourceExchanger, Resource> nextSupply = 
			new HashMap<ResourceExchanger, Resource>();
	
	protected ExchangingElement() {
		super();
	}
	
	public ExchangingElement(String name, Location initialLocation) {
		super(name, initialLocation);
	}
	
	public void setSupplier(ResourceType type, ResourceExchanger supplier) {
		suppliers.put(type, supplier);
	}
	
	public void addCustomer(ResourceExchanger customer) {
		customers.add(customer);
	}

	@Override
	public final Resource getSendingRateTo(ResourceExchanger element) {
		Resource sendingRate = ResourceFactory.createResource();
		if(customers.contains(element)) {
			sendingRate = sendingRate.add(demand.get(element));
		}
		return sendingRate;
	}
	
	@Override
	public final Resource getSendingRate() {
		Resource sendingRate = ResourceFactory.createResource();
		for(ResourceExchanger customer : customers) {
			sendingRate = sendingRate.add(getSendingRateTo(customer));
		}
		return sendingRate;
	}
	
	@Override
	public final Resource getReceivingRateFrom(ResourceExchanger element) {
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
		for(ResourceExchanger customer : customers) {
			demand.put(customer, ResourceFactory.createResource());
			nextDemand.put(customer, ResourceFactory.createResource());
		}
		supply.clear();
		nextSupply.clear();
		for(ResourceExchanger supplier : suppliers.values()) {
			supply.put(supplier, ResourceFactory.createResource());
			nextSupply.put(supplier, ResourceFactory.createResource());
		}
	}
	
	public void tick(long timeStep) {
		super.tick(timeStep);
		exchangeResources(getSendingRate().multiply(timeStep),
				getReceivingRate().multiply(timeStep));
	}
	
	@Override
	public final void iterateTick() {
		nextDemand.clear();
		for(ResourceExchanger customer : customers) {
			nextDemand.put(customer, customer.getReceivingRateFrom(this));
		}
		nextSupply.clear();
		for(ResourceExchanger supplier : suppliers.values()) {
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