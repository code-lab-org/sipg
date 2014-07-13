package edu.mit.isos2.system;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.mit.isos2.Location;
import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceType;
import edu.mit.isos2.state.ResourceExchanging;
import edu.mit.isos2.state.State;

public class SystemState implements State, ResourceExchanging {
	private final String name;
	private Map<ResourceType, Element> suppliers = new HashMap<ResourceType, Element>();
	private Set<Element> customers = new HashSet<Element>();
	private Map<Element, Resource> demand = new HashMap<Element, Resource>();
	private Map<Element, Resource> nextDemand = new HashMap<Element, Resource>();
	private Map<Element, Resource> supply = new HashMap<Element, Resource>();
	private Map<Element, Resource> nextSupply = new HashMap<Element, Resource>();
	
	protected SystemState() {
		this("");
	}
	
	public SystemState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	public void setSupplier(ResourceType type, Element supplier) {
		suppliers.put(type, supplier);
	}
	
	public void addCustomer(Element customer) {
		customers.add(customer);
	}

	@Override
	public final Resource getSentTo(Element element, long duration) {
		Resource sent = ResourceFactory.create();
		if(customers.contains(element)) {
			if(demand.get(element) != null) {
				sent = sent.add(demand.get(element));
			}
		}
		return sent;
	}
	
	@Override
	public final Resource getSent(long duration) {
		Resource sent = ResourceFactory.create();
		for(Element customer : customers) {
			sent = sent.add(getSentTo(customer, duration));
		}
		return sent;
	}
	
	@Override
	public final Resource getReceivedFrom(Element element, long duration) {
		Resource received = ResourceFactory.create();
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
		return ResourceFactory.create();
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
	
	@Override
	public void tick(Element element, long duration) {
		for(Element customer : customers) {
			exchange(element, customer, getSentTo(customer, duration), 
					ResourceFactory.create());
		}
		for(Element supplier : suppliers.values()) {
			exchange(element, supplier, ResourceFactory.create(), 
					getReceivedFrom(supplier, duration));
		}
	}

	@Override
	public void exchange(Element element1, Element element2, Resource sent, Resource received) {
		if(!sent.isZero() && !element1.getLocation().getDestination().equals(
				element2.getLocation().getOrigin())) {
			throw new IllegalArgumentException("Incompatible resource exchange, " 
					+ element1.getName() + " destination " 
					+ element1.getLocation().getDestination() + " =/= " 
					+ element2.getName() + " origin " 
					+ element2.getLocation().getOrigin());
		}
		if(!received.isZero() && !element1.getLocation().getOrigin().equals(
				element2.getLocation().getDestination())) {
			throw new IllegalArgumentException("Incompatible resource exchange: " 
					+ element1.getName() + " origin " 
					+ element1.getLocation().getOrigin() + " =/= " + 
					element2.getName() + " destination " 
					+ element2.getLocation().getDestination());
		}
	}
	
	@Override
	public Resource getNetFlow(Element element, Location location, long duration) {
		Resource netFlow = ResourceFactory.create();
		if(location.isNodal() && location.getOrigin().equals(element.getLocation().getOrigin())) {
			netFlow = netFlow.subtract(getSent(duration))
					.add(getReceived(duration));
		}
		return netFlow;
	}

	@Override
	public Resource getNetExchange(Element element1, Element element2,
			long duration) {
		Resource netExchange = ResourceFactory.create();
		netExchange = netExchange.add(getSentTo(element2, duration))
				.subtract(getReceivedFrom(element2, duration));
		return netExchange;
	}

	@Override
	public void initialize(Element element, long initialTime) { }

	@Override
	public void tock() { }
}
