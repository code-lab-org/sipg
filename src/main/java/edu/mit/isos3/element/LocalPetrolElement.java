package edu.mit.isos3.element;

import java.util.Arrays;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.state.DefaultState;
import edu.mit.isos3.element.state.ResourceExchanging;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceMatrix;
import edu.mit.isos3.resource.ResourceType;

public class LocalPetrolElement extends DefaultElement implements PetrolElement {
	public LocalPetrolElement(String name, Location location, 
			double extractElect, double extractReserves, 
			double initialReserves) {
		super(name, location, new PetrolState(extractElect, extractReserves));
		initialContents(ResourceFactory.create(ResourceType.RESERVES, initialReserves));
	}
	
	public void setElectSupplier(ElectElement element) {
		if(getInitialState() instanceof PetrolState) {
			PetrolState state = (PetrolState) getInitialState();
			state.electSupplier = element;
		}
	}
	
	public void setCustomer(SocialElement element) {
		if(getInitialState() instanceof PetrolState) {
			PetrolState state = (PetrolState) getInitialState();
			state.socialCustomer = element;
		}
	}
	
	public void setCustomer(ElectElement element) {
		if(getInitialState() instanceof PetrolState) {
			PetrolState state = (PetrolState) getInitialState();
			state.electCustomer = element;
		}
	}
	
	@Override
	public double getElectReceived() {
		if(getState() instanceof PetrolState) {
			return ((PetrolState)getState()).getElectReceived();
		}
		return 0;
	}
	
	@Override
	public double getPetrolSentToSocial() {
		if(getState() instanceof PetrolState) {
			return ((PetrolState)getState()).getPetrolSentToSocial();
		}
		return 0;
	}
	
	@Override
	public double getPetrolSentToElect() {
		if(getState() instanceof PetrolState) {
			return ((PetrolState)getState()).getPetrolSentToElect();
		}
		return 0;
	}
	
	public static class PetrolState extends DefaultState implements ResourceExchanging {
		private ResourceMatrix tfMatrix = new ResourceMatrix();
		
		private SocialElement socialCustomer = null;
		private ElectElement electCustomer = null;
		private ElectElement electSupplier = null;
		private double electReceived, nextElectReceived;
		
		public double getElectReceived() {
			return electReceived;
		}
		
		@Override
		public void iterateTick(LocalElement element, long duration) {
			super.iterateTick(element, duration);
			nextElectReceived = getReceived(element, duration)
					.getQuantity(ResourceType.ELECTRICITY);
		}
		
		@Override
		public void iterateTock() {
			super.iterateTock();
			electReceived = nextElectReceived;
		}
		
		@Override
		public void initialize(LocalElement element, long initialTime) {
			super.initialize(element, initialTime);
			electReceived = nextElectReceived = 0;
			electSupplier = null;
			electCustomer = null;
			socialCustomer = null;
		}

		public PetrolState(double extractElect, double extractReserves) {
			super("Ops");
			tfMatrix = new ResourceMatrix(
					ResourceType.OIL, ResourceFactory.create(
							new ResourceType[]{ResourceType.ELECTRICITY, ResourceType.RESERVES},
							new double[]{extractElect, extractReserves}));
		}
		
		private double getPetrolSentToSocial() {
			if(socialCustomer != null) {
				return socialCustomer.getPetrolReceived();
			} else {
				return 0;
			}
		}
		
		private double getPetrolSentToElect() {
			if(electCustomer != null) {
				return electCustomer.getPetrolReceived();
			} else {
				return 0;
			}
		}

		@Override 
		public Resource getProduced(LocalElement element, long duration) {
			return getSent(element, duration);
		}

		@Override
		public Resource getRetrieved(LocalElement element, long duration) {
			return getConsumed(element, duration).get(ResourceType.RESERVES);
		}

		@Override
		public Resource getConsumed(LocalElement element, long duration) {
			return tfMatrix.multiply(getProduced(element, duration));
		}
		
		@Override
		public Resource getSentTo(LocalElement element1, Element element2, long duration) {
			Resource sent = ResourceFactory.create();
			if(element2 != null && element2.equals(socialCustomer)) {
				sent = sent.add(ResourceFactory.create(ResourceType.OIL, 
						socialCustomer.getPetrolReceived()));
			}
			if(element2 != null && element2.equals(electCustomer)) {
				sent = sent.add(ResourceFactory.create(ResourceType.OIL, 
						electCustomer.getPetrolReceived()));
			}
			return sent;
		}
		
		@Override
		public Resource getSent(LocalElement element, long duration) {
			Resource sent = ResourceFactory.create();
			for(Element customer : Arrays.asList(socialCustomer, electCustomer)) {
				sent = sent.add(getSentTo(element, customer, duration));
			}
			return sent;
		}
		
		@Override
		public Resource getReceivedFrom(LocalElement element1, Element element2, long duration) {
			Resource received = ResourceFactory.create();
			if(element2 != null && element2.equals(electSupplier)) {
				received = received.add(getReceived(element1, duration).get(ResourceType.ELECTRICITY));
			}
			return received;
		}
		
		@Override
		public Resource getReceived(LocalElement element, long duration) {
			return getConsumed(element, duration).get(ResourceType.ELECTRICITY);
		}
		
		@Override
		public void tick(LocalElement element, long duration) {
			super.tick(element, duration);
			for(Element customer : Arrays.asList(socialCustomer, electCustomer)) {
				exchange(element, customer, getSentTo(element, customer, duration), 
						ResourceFactory.create());
			}
			for(Element supplier : Arrays.asList(electSupplier)) {
				exchange(element, supplier, ResourceFactory.create(), 
						getReceivedFrom(element, supplier, duration));
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
}