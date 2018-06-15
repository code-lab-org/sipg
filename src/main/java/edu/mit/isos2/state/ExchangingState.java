package edu.mit.isos2.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.mit.isos2.Location;
import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceType;

public class ExchangingState extends DefaultState implements ResourceExchanging {
	private Map<ResourceType, Element> suppliers = new HashMap<ResourceType, Element>();
	private Map<ResourceType, Set<Element>> customers = new HashMap<ResourceType, Set<Element>>();
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
	
	public void addCustomer(ResourceType type, Element customer) {
		if(!customers.containsKey(type)) {
			customers.put(type, new HashSet<Element>());
		}
		customers.get(type).add(customer);
	}

	@Override
	public Resource getSentTo(Element element1, Element element2, long duration) {
		Resource sent = ResourceFactory.create();
		for(ResourceType type : customers.keySet()) {
			if(customers.get(type) != null 
					&& customers.get(type).contains(element2)
					&& demand.get(element2) != null) {
				sent = sent.add(demand.get(element2));
			}
		}
		return sent;
	}
	
	@Override
	public Resource getSent(Element element, long duration) {
		Resource sent = ResourceFactory.create();
		for(ResourceType type : customers.keySet()) {
			for(Element customer : customers.get(type)) {
				sent = sent.add(getSentTo(element, customer, duration));
			}
		}
		return sent;
	}
	
	@Override
	public Resource getReceivedFrom(Element element1, Element element2, long duration) {
		Resource received = ResourceFactory.create();
		for(ResourceType t : ResourceType.values()) {
			if(suppliers.get(t) != null && suppliers.get(t).equals(element2)) {
				received = received.add(getReceived(element1, duration).get(t));
			}
		}
		return received;
	}
	
	@Override
	public Resource getReceived(Element element, long duration) {
		return ResourceFactory.create();
	}
	
	@Override
	public void initialize(Element element, long initialTime) {
		super.initialize(element, initialTime);
		demand.clear();
		nextDemand.clear();
		supply.clear();
		nextSupply.clear();
	}
	
	@Override
	public void iterateTick(Element element, long duration) {
		super.iterateTick(element, duration);
		nextDemand.clear();
		for(ResourceType type : customers.keySet()) {
			for(Element customer : customers.get(type)) {
				if(customer.getState() instanceof ResourceExchanging) {
					ResourceExchanging rex = (ResourceExchanging) customer.getState();
					if(nextDemand.get(customer) == null) {
						nextDemand.put(customer, ResourceFactory.create());
					}
					nextDemand.put(customer, nextDemand.get(customer)
							.add(rex.getReceivedFrom(customer, element, duration).get(type)));
				}
			}
		}
		nextSupply.clear();
		for(ResourceType type : suppliers.keySet()) {
			if(suppliers.get(type).getState() instanceof ResourceExchanging) {
				ResourceExchanging rex = (ResourceExchanging) suppliers.get(type).getState();
				if(nextSupply.get(suppliers.get(type)) == null) {
					nextSupply.put(suppliers.get(type), ResourceFactory.create());
				}
				nextSupply.put(suppliers.get(type), nextSupply.get(suppliers.get(type))
						.add(rex.getSentTo(suppliers.get(type), element, duration).get(type)));
			}
		}
	}
	
	@Override
	public void iterateTock() {
		super.iterateTock();
		demand.clear();
		demand.putAll(nextDemand);
		supply.clear();
		supply.putAll(nextSupply);
	}
	
	@Override
	public void tick(Element element, long duration) {
		super.tick(element, duration);
		for(ResourceType type : customers.keySet()) {
			for(Element customer : customers.get(type)) {
				exchange(element, customer, getSentTo(element, customer, duration).get(type), 
						ResourceFactory.create());
			}
		}
		for(ResourceType type : suppliers.keySet()) {
			for(Element supplier : suppliers.values()) {
				exchange(element, supplier, ResourceFactory.create(), 
						getReceivedFrom(element, supplier, duration).get(type));
			}
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
		// no longer modifies contents
		// element.remove(sent);
		// element.add(received);
	}
	
	@Override
	public Resource getNetFlow(Element element, Location location, long duration) {
		Resource netFlow = super.getNetFlow(element, location, duration);
		if(location.isNodal() && location.getOrigin().equals(element.getLocation().getOrigin())) {
			netFlow = netFlow.subtract(getSent(element, duration))
					.add(getReceived(element, duration));
		}
		return netFlow;
	}

	@Override
	public Resource getNetExchange(Element element1, Element element2,
			long duration) {
		Resource netExchange = super.getNetExchange(element1, element2, duration);
		netExchange = netExchange.add(getSentTo(element1, element2, duration))
				.subtract(getReceivedFrom(element1, element2, duration));
		return netExchange;
	}
}