package edu.mit.isos3.element;

import java.util.Arrays;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.state.DefaultState;
import edu.mit.isos3.element.state.ResourceExchanging;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceMatrix;
import edu.mit.isos3.resource.ResourceType;

public class LocalElectElement extends DefaultElement implements ElectElement {
	private double petrolReceived, nextPetrolReceived;
	
	public LocalElectElement(String name, Location location, 
			double solarCap, double thermalOil) {
		super(name, location, new ElectState(solarCap, thermalOil));
	}
	
	public void setPetrolSupplier(PetrolElement element) {
		if(getInitialState() instanceof ElectState) {
			ElectState state = (ElectState) getInitialState();
			state.petrolSupplier = element;
		}
	}
	
	public void setCustomer(PetrolElement element) {
		if(getInitialState() instanceof ElectState) {
			ElectState state = (ElectState) getInitialState();
			state.petrolCustomer = element;
		}
	}
	
	public void setCustomer(SocialElement element) {
		if(getInitialState() instanceof ElectState) {
			ElectState state = (ElectState) getInitialState();
			state.socialCustomer = element;
		}
	}
	
	public void setCustomer(WaterElement element) {
		if(getInitialState() instanceof ElectState) {
			ElectState state = (ElectState) getInitialState();
			state.waterCustomer = element;
		}
	}
	
	@Override
	public double getPetrolReceived() {
		return petrolReceived;
	}
	
	@Override
	public double getElectSentToSocial() {
		if(getState() instanceof ElectState) {
			return ((ElectState)getState()).getElectSentToSocial();
		}
		return 0;
	}
	
	@Override
	public double getElectSentToPetrol() {
		if(getState() instanceof ElectState) {
			return ((ElectState)getState()).getElectSentToPetrol();
		}
		return 0;
	}
	
	@Override
	public double getElectSentToWater() {
		if(getState() instanceof ElectState) {
			return ((ElectState)getState()).getElectSentToWater();
		}
		return 0;
	}
	
	@Override
	public void initialize(long initialTime) {
		super.initialize(initialTime);
		petrolReceived = nextPetrolReceived = 0;
	}
	
	public void iterateTick(long duration) {
		super.iterateTick(duration);
		if(getState() instanceof ElectState) {
			ElectState state = (ElectState) getState();
			nextPetrolReceived = state.getReceived(this, duration)
					.getQuantity(ResourceType.OIL);
		}
	}
	
	public void iterateTock() {
		super.iterateTock();
		petrolReceived = nextPetrolReceived;
	}
	
	public static class ElectState extends DefaultState implements ResourceExchanging {
		private ResourceMatrix tfMatrix = new ResourceMatrix();
		private Resource solarCapacity = ResourceFactory.create();
		private PetrolElement petrolSupplier = null;
		private PetrolElement petrolCustomer = null;
		private SocialElement socialCustomer = null;
		private WaterElement waterCustomer = null;
		
		@Override
		public void initialize(LocalElement element, long initialTime) {
			super.initialize(element, initialTime);
			petrolSupplier = null;
			petrolCustomer = null;
			socialCustomer = null;
			waterCustomer = null;
		}

		public ElectState(double solarCap, double thermalOil) {
			super("Ops");
			tfMatrix = new ResourceMatrix(
					ResourceType.ELECTRICITY, 
					ResourceFactory.create(ResourceType.OIL, thermalOil));

			solarCapacity = ResourceFactory.create(
					ResourceType.ELECTRICITY, solarCap);
		}
		
		private double getElectSentToSocial() {
			if(socialCustomer != null) {
				return socialCustomer.getElectReceived();
			} else {
				return 0;
			}
		}
		
		private double getElectSentToPetrol() {
			if(petrolCustomer != null) {
				return petrolCustomer.getElectReceived();
			} else {
				return 0;
			}
		}
		
		private double getElectSentToWater() {
			if(waterCustomer != null) {
				return waterCustomer.getElectReceived();
			} else {
				return 0;
			}
		}

		@Override 
		public Resource getProduced(LocalElement element, long duration) {
			return getSent(element, duration);
		}

		@Override
		public Resource getConsumed(LocalElement element, long duration) {
			return tfMatrix.multiply(getProduced(element, duration)
					.subtract(solarCapacity.multiply(duration))
					.truncatePositive());
		}
		
		@Override
		public Resource getSentTo(LocalElement element1, Element element2, long duration) {
			Resource sent = ResourceFactory.create();
			if(element2 != null && element2.equals(socialCustomer)) {
				sent = sent.add(ResourceFactory.create(ResourceType.ELECTRICITY, 
						socialCustomer.getElectReceived()));
			}
			if(element2 != null && element2.equals(petrolCustomer)) {
				sent = sent.add(ResourceFactory.create(ResourceType.ELECTRICITY, 
						petrolCustomer.getElectReceived()));
			}
			if(element2 != null && element2.equals(waterCustomer)) {
				sent = sent.add(ResourceFactory.create(ResourceType.ELECTRICITY, 
						waterCustomer.getElectReceived()));
			}
			return sent;
		}
		
		@Override
		public Resource getSent(LocalElement element, long duration) {
			Resource sent = ResourceFactory.create();
			for(Element customer : Arrays.asList(socialCustomer, petrolCustomer, waterCustomer)) {
				sent = sent.add(getSentTo(element, customer, duration));
			}
			return sent;
		}
		
		@Override
		public Resource getReceivedFrom(LocalElement element1, Element element2, long duration) {
			Resource received = ResourceFactory.create();
			if(element2 != null && element2.equals(petrolSupplier)) {
				received = received.add(getReceived(element1, duration).get(ResourceType.OIL));
			}
			return received;
		}
		
		@Override
		public Resource getReceived(LocalElement element, long duration) {
			return getConsumed(element, duration).get(ResourceType.OIL);
		}
		
		@Override
		public void tick(LocalElement element, long duration) {
			super.tick(element, duration);
			for(Element customer : Arrays.asList(socialCustomer, petrolCustomer, waterCustomer)) {
				exchange(element, customer, getSentTo(element, customer, duration), 
						ResourceFactory.create());
			}
			for(Element supplier : Arrays.asList(petrolSupplier)) {
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