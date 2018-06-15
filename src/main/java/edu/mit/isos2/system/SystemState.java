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
import edu.mit.isos2.state.ResourceStoring;
import edu.mit.isos2.state.ResourceTransforming;
import edu.mit.isos2.state.ResourceTransporting;
import edu.mit.isos2.state.RetrievingState;
import edu.mit.isos2.state.State;

public class SystemState implements State, ResourceExchanging, ResourceStoring, ResourceTransforming, ResourceTransporting {
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
	public final Resource getSentTo(Element element1, Element element2, long duration) {
		Resource sent = ResourceFactory.create();
		if(customers.contains(element2)) {
			if(demand.get(element2) != null) {
				sent = sent.add(demand.get(element2));
			}
		}
		return sent;
	}
	
	@Override
	public final Resource getSent(Element element, long duration) {
		Resource sent = ResourceFactory.create();
		for(Element customer : customers) {
			sent = sent.add(getSentTo(element, customer, duration));
		}
		return sent;
	}
	
	@Override
	public final Resource getReceivedFrom(Element element1, Element element2, long duration) {
		Resource received = ResourceFactory.create();
		if(suppliers.containsValue(element2)) {
			for(ResourceType t : ResourceType.values()) {
				if(suppliers.get(t) != null && suppliers.get(t).equals(element2)) {
					received = received.add(getReceived(element1, duration)).get(t);
				}
			}
		}
		return received;
	}
	
	@Override
	public Resource getReceived(Element element, long duration) {
		Resource received = ResourceFactory.create();
		Resource input = getInput(element, duration); // TODO input only
		for(ResourceType t : suppliers.keySet()) {
			received = received.add(input.get(t));
		}
		return received;
	}
	
	@Override
	public final void iterateTick(Element element, long duration) {
		Resource netFlow = element.getNetFlow(element.getLocation(), duration);
		for(Element e : element.getElements()) {
			for(ResourceType t : ResourceType.values()) {
				if(netFlow.getQuantity(t) < 0
						&& e.getContents().getQuantity(t) > 0
						&& e.getState() instanceof RetrievingState) {
					((RetrievingState)e.getState()).setRetrieved(netFlow.get(t).negate());
				}
				// TODO fix case for over-supply
			}
		}

		nextDemand.clear();
		for(Element customer : customers) {
			if(customer.getState() instanceof ResourceExchanging) {
				ResourceExchanging rex = (ResourceExchanging) customer.getState();
				nextDemand.put(customer, rex.getReceivedFrom(customer, element, duration));
			}
		}
		nextSupply.clear();
		for(Element supplier : suppliers.values()) {
			if(supplier.getState() instanceof ResourceExchanging) {
				ResourceExchanging rex = (ResourceExchanging) supplier.getState();
				nextSupply.put(supplier, rex.getSentTo(supplier, element, duration));
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
			exchange(element, customer, getSentTo(element, customer, duration), 
					ResourceFactory.create());
		}
		for(Element supplier : suppliers.values()) {
			exchange(element, supplier, ResourceFactory.create(), 
					getReceivedFrom(element, supplier, duration));
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
		if(location.equals(element.getLocation()) ||
				(location.isNodal() && location.getOrigin().equals(element.getLocation().getOrigin()))) {
			netFlow = netFlow.subtract(getSent(element, duration))
					.add(getReceived(element, duration));
		}
		return netFlow;
	}

	@Override
	public Resource getNetExchange(Element element1, Element element2,
			long duration) {
		Resource netExchange = ResourceFactory.create();
		netExchange = netExchange.add(getSentTo(element1, element2, duration))
				.subtract(getReceivedFrom(element1, element2, duration));
		return netExchange;
	}

	@Override
	public void initialize(Element element, long initialTime) { }

	@Override
	public void tock() { }

	@Override
	public Resource getInput(Element element, long duration) {
		Resource input = ResourceFactory.create();
		for(Element e : element.getElements()) {
			if(e.getState() instanceof ResourceTransporting) {
				input = input.add(((ResourceTransporting)e.getState()).getInput(e, duration));
			}
		}
		return input;
	}

	@Override
	public Resource getOutput(Element element, long duration) {
		Resource output = ResourceFactory.create();
		for(Element e : element.getElements()) {
			if(e.getState() instanceof ResourceTransporting) {
				output = output.add(((ResourceTransporting)e.getState()).getOutput(e, duration));
			}
		}
		return output;
	}

	@Override
	public void transport(Element element, Resource input, Resource output) {
		throw new IllegalStateException("Cannot call transport method for system element");
	}

	@Override
	public Resource getProduced(Element element, long duration) {
		Resource produced = ResourceFactory.create();
		for(Element e : element.getElements()) {
			if(e.getState() instanceof ResourceTransforming) {
				produced = produced.add(((ResourceTransforming)e.getState()).getProduced(e, duration));
			}
		}
		return produced;
	}

	@Override
	public Resource getConsumed(Element element, long duration) {
		Resource consumed = ResourceFactory.create();
		for(Element e : element.getElements()) {
			if(e.getState() instanceof ResourceTransforming) {
				consumed = consumed.add(((ResourceTransforming)e.getState()).getConsumed(e, duration));
			}
		}
		return consumed;
	}

	@Override
	public void transform(Element element, Resource consumed, Resource produced) {
		throw new IllegalStateException("Cannot call transform method for system element");
	}

	@Override
	public Resource getStored(Element element, long duration) {
		Resource stored = ResourceFactory.create();
		for(Element e : element.getElements()) {
			if(e.getState() instanceof ResourceStoring) {
				stored = stored.add(((ResourceStoring)e.getState()).getStored(e, duration));
			}
		}
		return stored;
	}

	@Override
	public Resource getRetrieved(Element element, long duration) {
		Resource retrieved = ResourceFactory.create();
		for(Element e : element.getElements()) {
			if(e.getState() instanceof ResourceStoring) {
				retrieved = retrieved.add(((ResourceStoring)e.getState()).getRetrieved(e, duration));
			}
		}
		return retrieved;
	}

	@Override
	public void store(Element element, Resource stored, Resource retrieved) {
		throw new IllegalStateException("Cannot call store method for system element");
	}
}
