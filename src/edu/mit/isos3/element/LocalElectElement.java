package edu.mit.isos3.element;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.state.ExchangingState;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceMatrix;
import edu.mit.isos3.resource.ResourceType;

public class LocalElectElement extends DefaultElement implements ElectElement {
	private double petrolReceived, nextPetrolReceived;
	private double electSentToPetrol, nextElectSentToPetrol;
	private double electSentToSocial, nextElectSentToSocial;
	private double electSentToWater, nextElectSentToWater;
	
	public LocalElectElement(String name, Location location, 
			double solarCap, double thermalOil) {
		super(name, location, new ElectState(solarCap, thermalOil));
	}
	
	public void setPetrolSupplier(PetrolElement element) {
		if(getInitialState() instanceof ElectState) {
			ElectState state = (ElectState) getInitialState();
			state.setSupplier(ResourceType.OIL, element);
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
		return electSentToSocial;
	}
	
	@Override
	public double getElectSentToPetrol() {
		return electSentToPetrol;
	}
	
	@Override
	public double getElectSentToWater() {
		return electSentToWater;
	}
	
	public void iterateTick(long duration) {
		super.iterateTick(duration);
		if(getState() instanceof ElectState) {
			ElectState state = (ElectState) getState();
			nextPetrolReceived = state.getReceived(this, duration)
					.getQuantity(ResourceType.OIL);
			nextElectSentToPetrol = state.getSentTo(this, state.petrolCustomer, duration)
					.getQuantity(ResourceType.ELECTRICITY);
			nextElectSentToSocial = state.getSentTo(this, state.socialCustomer, duration)
					.getQuantity(ResourceType.ELECTRICITY);
			nextElectSentToWater = state.getSentTo(this, state.waterCustomer, duration)
					.getQuantity(ResourceType.ELECTRICITY);
		}
	}
	
	public void iterateTock() {
		super.iterateTock();
		petrolReceived = nextPetrolReceived;
		electSentToPetrol = nextElectSentToPetrol;
		electSentToSocial = nextElectSentToSocial;
		electSentToWater = nextElectSentToWater;
	}
	
	public static class ElectState extends ExchangingState {
		private ResourceMatrix tfMatrix = new ResourceMatrix();
		private Resource solarCapacity = ResourceFactory.create();
		private PetrolElement petrolCustomer = null;
		private SocialElement socialCustomer = null;
		private WaterElement waterCustomer = null;

		public ElectState(double solarCap, double thermalOil) {
			super("Ops");
			tfMatrix = new ResourceMatrix(
					ResourceType.ELECTRICITY, 
					ResourceFactory.create(ResourceType.OIL, thermalOil));

			solarCapacity = ResourceFactory.create(
					ResourceType.ELECTRICITY, solarCap);
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
		public Resource getReceived(LocalElement element, long duration) {
			return getConsumed(element, duration).get(ResourceType.OIL);
		}
		@Override
		public void iterateTick(LocalElement element, long duration) {
			super.iterateTick(element, duration);
			nextDemand.clear();
			if(socialCustomer != null) {
				nextDemand.put(socialCustomer, ResourceFactory.create(
						ResourceType.ELECTRICITY, socialCustomer.getElectReceived()));
			}
			if(petrolCustomer != null) {
				nextDemand.put(petrolCustomer, ResourceFactory.create(
						ResourceType.ELECTRICITY, petrolCustomer.getElectReceived()));
			}
			if(waterCustomer != null) {
				nextDemand.put(waterCustomer, ResourceFactory.create(
						ResourceType.ELECTRICITY, waterCustomer.getElectReceived()));
			}
		}
	}
}