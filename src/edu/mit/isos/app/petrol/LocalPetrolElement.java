package edu.mit.isos.app.petrol;

import edu.mit.isos.app.ElectElement;
import edu.mit.isos.app.PetrolElement;
import edu.mit.isos.app.SocialElement;
import edu.mit.isos.core.context.Location;
import edu.mit.isos.core.context.ResourceFactory;
import edu.mit.isos.core.context.ResourceType;
import edu.mit.isos.core.element.DefaultElement;

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
			state.setElectSupplier(element);
		}
	}
	
	public void setCustomer(SocialElement element) {
		if(getInitialState() instanceof PetrolState) {
			PetrolState state = (PetrolState) getInitialState();
			state.setCustomer(element);
		}
	}
	
	public void setCustomer(ElectElement element) {
		if(getInitialState() instanceof PetrolState) {
			PetrolState state = (PetrolState) getInitialState();
			state.setCustomer(element);
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
	

}