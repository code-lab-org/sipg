package edu.mit.isos3.element;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.state.ExchangingState;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceMatrix;
import edu.mit.isos3.resource.ResourceType;

public class LocalPetrolElement extends DefaultElement implements PetrolElement {
	private double electReceived, nextElectReceived;
	
	public LocalPetrolElement(String name, Location location, 
			double extractElect, double extractReserves, 
			double initialReserves) {
		super(name, location, new PetrolState(extractElect, extractReserves));
		initialContents(ResourceFactory.create(ResourceType.RESERVES, initialReserves));
	}
	
	public void setElectSupplier(ElectElement element) {
		if(getInitialState() instanceof PetrolState) {
			PetrolState state = (PetrolState) getInitialState();
			state.setSupplier(ResourceType.ELECTRICITY, element);
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
		return electReceived;
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
	
	public void iterateTick(long duration) {
		super.iterateTick(duration);
		if(getState() instanceof PetrolState) {
			PetrolState state = (PetrolState) getState();
			nextElectReceived = state.getReceived(this, duration)
					.getQuantity(ResourceType.ELECTRICITY);
		}
	}
	
	public void iterateTock() {
		super.iterateTock();
		electReceived = nextElectReceived;
	}
	
	public static class PetrolState extends ExchangingState {
		private ResourceMatrix tfMatrix = new ResourceMatrix();
		private SocialElement socialCustomer = null;
		private ElectElement electCustomer = null;

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
		public Resource getReceived(LocalElement element, long duration) {
			return getConsumed(element, duration).get(ResourceType.ELECTRICITY);
		}
		@Override
		public void iterateTick(LocalElement element, long duration) {
			super.iterateTick(element, duration);
			nextDemand.clear();
			if(socialCustomer != null) {
				nextDemand.put(socialCustomer, ResourceFactory.create(
						ResourceType.OIL, socialCustomer.getPetrolReceived()));
			}
			if(electCustomer != null) {
				nextDemand.put(electCustomer, ResourceFactory.create(
						ResourceType.OIL, electCustomer.getPetrolReceived()));
			}
		}
	}
}