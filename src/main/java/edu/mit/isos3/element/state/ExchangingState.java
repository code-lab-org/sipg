package edu.mit.isos3.element.state;

import java.util.HashMap;
import java.util.Map;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.Element;
import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceType;

public class ExchangingState extends DefaultState implements ResourceExchanging {
	private Map<ResourceType, Element> suppliers = new HashMap<ResourceType, Element>();
	private Map<Element, Resource> demand = new HashMap<Element, Resource>();
	protected Map<Element, Resource> nextDemand = new HashMap<Element, Resource>();
	
	protected ExchangingState() {
		super();
	}
	
	public ExchangingState(String name) {
		super(name);
	}
	
	public void setSupplier(ResourceType type, Element supplier) {
		suppliers.put(type, supplier);
	}

	@Override
	public Resource getSentTo(LocalElement element1, Element element2, long duration) {
		Resource sent = ResourceFactory.create();
		if(demand.keySet().contains(element2)) {
			sent = sent.add(demand.get(element2));
		}
		return sent;
	}
	
	@Override
	public Resource getSent(LocalElement element, long duration) {
		Resource sent = ResourceFactory.create();
		for(Element customer : demand.keySet()) {
			sent = sent.add(demand.get(customer));
		}
		return sent;
	}
	
	@Override
	public Resource getReceivedFrom(LocalElement element1, Element element2, long duration) {
		Resource received = ResourceFactory.create();
		for(ResourceType t : ResourceType.values()) {
			if(suppliers.get(t) != null && suppliers.get(t).equals(element2)) {
				received = received.add(getReceived(element1, duration).get(t));
			}
		}
		return received;
	}
	
	@Override
	public Resource getReceived(LocalElement element, long duration) {
		return ResourceFactory.create();
	}
	
	@Override
	public void initialize(LocalElement element, long initialTime) {
		super.initialize(element, initialTime);
		demand.clear();
		nextDemand.clear();
	}
	
	@Override
	public void iterateTick(LocalElement element, long duration) {
		super.iterateTick(element, duration);
		/* FIXME
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
		*/
	}
	
	@Override
	public void iterateTock() {
		super.iterateTock();
		demand.clear();
		demand.putAll(nextDemand);
	}
	
	@Override
	public void tick(LocalElement element, long duration) {
		super.tick(element, duration);
		for(Element customer : demand.keySet()) {
			exchange(element, customer, demand.get(customer), 
					ResourceFactory.create());
		}
		for(ResourceType type : suppliers.keySet()) {
			for(Element supplier : suppliers.values()) {
				exchange(element, supplier, ResourceFactory.create(), 
						getReceivedFrom(element, supplier, duration).get(type));
			}
		}
	}

	@Override
	public void exchange(LocalElement element1, Element element2, Resource sent, Resource received) {
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
	public Resource getNetFlow(LocalElement element, Location location, long duration) {
		Resource netFlow = super.getNetFlow(element, location, duration);
		if(location.isNodal() && location.getOrigin().equals(element.getLocation().getOrigin())) {
			netFlow = netFlow.subtract(getSent(element, duration))
					.add(getReceived(element, duration));
		}
		return netFlow;
	}

	@Override
	public Resource getNetExchange(LocalElement element1, Element element2,
			long duration) {
		Resource netExchange = super.getNetExchange(element1, element2, duration);
		netExchange = netExchange.add(getSentTo(element1, element2, duration))
				.subtract(getReceivedFrom(element1, element2, duration));
		return netExchange;
	}
}