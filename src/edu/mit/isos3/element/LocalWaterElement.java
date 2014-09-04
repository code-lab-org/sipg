package edu.mit.isos3.element;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.state.ExchangingState;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceMatrix;
import edu.mit.isos3.resource.ResourceType;

public class LocalWaterElement extends DefaultElement implements WaterElement {
	private double electReceived, nextElectReceived;
	private double waterSentToSocial, nextWaterSentToSocial;
	
	public LocalWaterElement(String name, Location location,
			double liftAquifer, double liftElect, double initialAquifer) {
		super(name, location, new WaterState(liftAquifer, liftElect));
		initialContents(ResourceFactory.create(ResourceType.AQUIFER, initialAquifer));
	}
	
	public WaterState getOperatingState() {
		return (WaterState) getInitialState();
	}
	
	public void setElectSupplier(Element element) {
		if(getInitialState() instanceof WaterState) {
			WaterState state = (WaterState) getInitialState();
			state.setSupplier(ResourceType.ELECTRICITY, element);
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
		return electReceived;
	}
	
	@Override
	public double getWaterSentToSocial() {
		return waterSentToSocial;
	}
	
	public void iterateTick(long duration) {
		super.iterateTick(duration);
		if(getState() instanceof WaterState) {
			WaterState state = (WaterState) getState();
			nextElectReceived = state.getReceived(this, duration)
					.getQuantity(ResourceType.ELECTRICITY);
			nextWaterSentToSocial = state.getSent(this, duration)
					.getQuantity(ResourceType.WATER);
		}
	}
	
	public void iterateTock() {
		super.iterateTock();
		electReceived = nextElectReceived;
		waterSentToSocial = nextWaterSentToSocial;
	}
	
	public static class WaterState extends ExchangingState {
		private ResourceMatrix liftMatrix = new ResourceMatrix();
		Resource produced = ResourceFactory.create();
		Resource received = ResourceFactory.create();
		private final Resource initialProduced = ResourceFactory.create();
		private final Resource initialReceived = ResourceFactory.create();
		private SocialElement socialCustomer = null;

		public WaterState(double liftAquifer, double liftElect) {
			super("Ops");
			liftMatrix = new ResourceMatrix(
					ResourceType.WATER, ResourceFactory.create(
							new ResourceType[]{ResourceType.AQUIFER, ResourceType.ELECTRICITY}, 
							new double[]{liftAquifer, liftElect}));
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
		public Resource getReceived(LocalElement element, long duration) {
			return received;
		}
		@Override
		public void initialize(LocalElement element, long initialTime) {
			super.initialize(element, initialTime);
			produced = initialProduced;
			received = initialReceived;
		}
		@Override
		public void iterateTick(LocalElement element, long duration) {
			super.iterateTick(element, duration);
			nextDemand.clear();
			if(socialCustomer != null) {
				nextDemand.put(socialCustomer, ResourceFactory.create(
						ResourceType.WATER, socialCustomer.getWaterReceived()));
			}
		}
	}
}