package edu.mit.isos3.element;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.state.ExchangingState;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceMatrix;
import edu.mit.isos3.resource.ResourceType;

public class LocalSocialElement extends DefaultElement implements SocialElement {
	private double electReceived, nextElectReceived;
	private double petrolReceived, nextPetrolReceived;
	private double waterReceived, nextWaterReceived;
	
	public LocalSocialElement(String name, Location location, 
			double waterPC, double electPC, double oilPC, 
			double initialPopulation, double growthRate) {
		super(name, location, new SocialState(electPC, oilPC, waterPC, growthRate));
		initialContents(ResourceFactory.create(ResourceType.PEOPLE, initialPopulation));
	}
	
	public void setElectSupplier(ElectElement element) {
		if(getInitialState() instanceof SocialState) {
			SocialState state = (SocialState) getInitialState();
			state.setSupplier(ResourceType.ELECTRICITY, element);
		}
	}
	
	public void setPetrolSupplier(PetrolElement element) {
		if(getInitialState() instanceof SocialState) {
			SocialState state = (SocialState) getInitialState();
			state.setSupplier(ResourceType.OIL, element);
		}
	}
	
	public void setWaterSupplier(WaterElement element) {
		if(getInitialState() instanceof SocialState) {
			SocialState state = (SocialState) getInitialState();
			state.setSupplier(ResourceType.WATER, element);
		}
	}
	
	@Override
	public double getElectReceived() {
		return electReceived;
	}
	
	@Override
	public double getPetrolReceived() {
		return petrolReceived;
	}
	
	@Override
	public double getWaterReceived() {
		return waterReceived;
	}
	
	public void iterateTick(long duration) {
		super.iterateTick(duration);
		if(getState() instanceof SocialState) {
			SocialState state = (SocialState) getState();
			nextElectReceived = state.getReceived(this, duration)
					.getQuantity(ResourceType.ELECTRICITY);
			nextPetrolReceived = state.getReceived(this, duration)
					.getQuantity(ResourceType.OIL);
			nextWaterReceived = state.getReceived(this, duration)
					.getQuantity(ResourceType.WATER);
		}
	}
	
	public void iterateTock() {
		super.iterateTock();
		electReceived = nextElectReceived;
		petrolReceived = nextPetrolReceived;
		waterReceived = nextWaterReceived;
	}
	
	public static class SocialState extends ExchangingState {
		private ResourceMatrix demandMatrix = new ResourceMatrix();
		private double growthRate;
		
		public SocialState(double electPC, double oilPC, double waterPC, double growthRate) {
			super("Ops");
			demandMatrix = new ResourceMatrix(
					ResourceType.PEOPLE, ResourceFactory.create(
							new ResourceType[]{ResourceType.ELECTRICITY, 
									ResourceType.WATER, ResourceType.OIL},
									new double[]{electPC, waterPC, oilPC}));
			this.growthRate = growthRate;
		}
		
		@Override
		public Resource getConsumed(LocalElement element, long duration) {
			return demandMatrix.multiply(element.getContents().get(ResourceType.PEOPLE))
					.multiply(duration);
		}

		@Override
		public Resource getProduced(LocalElement element, long duration) {
			return element.getContents().get(ResourceType.PEOPLE)
					.multiply(Math.exp(growthRate*duration)-1);
		}

		@Override
		public Resource getStored(LocalElement element, long duration) {
			return getProduced(element, duration);
		}
		
		@Override
		public Resource getReceived(LocalElement element, long duration) {
			return getConsumed(element, duration);
		}
	}
}