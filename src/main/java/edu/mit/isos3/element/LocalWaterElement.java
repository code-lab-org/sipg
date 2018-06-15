package edu.mit.isos3.element;

import java.util.Arrays;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.state.DefaultState;
import edu.mit.isos3.element.state.ResourceExchanging;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceMatrix;
import edu.mit.isos3.resource.ResourceType;

public class LocalWaterElement extends DefaultElement implements WaterElement {	
	public LocalWaterElement(String name, Location location,
			double liftAquifer, double liftElect, double initialAquifer) {
		super(name, location, new WaterState(liftAquifer, liftElect));
		initialContents(ResourceFactory.create(ResourceType.AQUIFER, initialAquifer));
	}
	
	public WaterState getOperatingState() {
		return (WaterState) getInitialState();
	}
	
	public void setElectSupplier(ElectElement element) {
		if(getInitialState() instanceof WaterState) {
			WaterState state = (WaterState) getInitialState();
			state.electSupplier = element;
		}
	}
	
	public void setCustomer(SocialElement element) {
		if(getInitialState() instanceof WaterState) {
			WaterState state = (WaterState) getInitialState();
			state.socialCustomer = element;
		}
	}
	
	@Override
	public double getElectReceived() {
		if(getState() instanceof WaterState) {
			return ((WaterState)getState()).getElectReceived();
		}
		return 0;
	}
	
	@Override
	public double getWaterSentToSocial() {
		if(getState() instanceof WaterState) {
			return ((WaterState)getState()).getWaterSentToSocial();
		}
		return 0;
	}
	
	public static class WaterState extends DefaultState implements ResourceExchanging {
		private ResourceMatrix liftMatrix = new ResourceMatrix();
		Resource produced = ResourceFactory.create();
		Resource received = ResourceFactory.create();
		
		private SocialElement socialCustomer = null;
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
			produced = ResourceFactory.create();
			received = ResourceFactory.create();
			socialCustomer = null;
			electSupplier = null;
			electReceived = nextElectReceived = 0;
		}

		public WaterState(double liftAquifer, double liftElect) {
			super("Ops");
			liftMatrix = new ResourceMatrix(
					ResourceType.WATER, ResourceFactory.create(
							new ResourceType[]{ResourceType.AQUIFER, ResourceType.ELECTRICITY}, 
							new double[]{liftAquifer, liftElect}));
		}
		
		private double getWaterSentToSocial() {
			if(socialCustomer != null) {
				return socialCustomer.getWaterReceived();
			} else {
				return 0;
			}
		}

		@Override 
		public Resource getProduced(LocalElement element, long duration) {
			return produced;
		}

		@Override
		public Resource getRetrieved(LocalElement element, long duration) {
			return getConsumed(element, duration).get(ResourceType.AQUIFER);
		}

		@Override
		public Resource getConsumed(LocalElement element, long duration) {
			return liftMatrix.multiply(produced);
		}
		public void setProduced(Resource produced, long duration) {
			this.produced = produced;
		}
		public void setReceived(Resource received, long duration) {
			this.received = received;
		}
		
		@Override
		public Resource getSentTo(LocalElement element1, Element element2, long duration) {
			Resource sent = ResourceFactory.create();
			if(element2 != null && element2.equals(socialCustomer)) {
				sent = sent.add(ResourceFactory.create(ResourceType.WATER, 
						socialCustomer.getWaterReceived()));
			}
			return sent;
		}
		
		@Override
		public Resource getSent(LocalElement element, long duration) {
			Resource sent = ResourceFactory.create();
			for(Element customer : Arrays.asList(socialCustomer)) {
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
			return received;
		}
		
		@Override
		public void tick(LocalElement element, long duration) {
			super.tick(element, duration);
			for(Element customer : Arrays.asList(socialCustomer)) {
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