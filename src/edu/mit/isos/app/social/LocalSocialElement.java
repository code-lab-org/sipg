package edu.mit.isos.app.social;

import edu.mit.isos.app.ElectElement;
import edu.mit.isos.app.PetrolElement;
import edu.mit.isos.app.SocialElement;
import edu.mit.isos.app.WaterElement;
import edu.mit.isos.core.context.Location;
import edu.mit.isos.core.context.ResourceFactory;
import edu.mit.isos.core.context.ResourceType;
import edu.mit.isos.core.element.DefaultElement;

public class LocalSocialElement extends DefaultElement implements SocialElement {	
	public LocalSocialElement(String name, Location location, 
			double waterPC, double electPC, double oilPC, 
			double initialPopulation, double growthRate) {
		super(name, location, new SocialState(electPC, oilPC, waterPC, growthRate));
		initialContents(ResourceFactory.create(ResourceType.PEOPLE, initialPopulation));
	}
	
	public void setElectSupplier(ElectElement element) {
		if(getInitialState() instanceof SocialState) {
			SocialState state = (SocialState) getInitialState();
			state.setElectSupplier(element);
		}
	}
	
	public void setPetrolSupplier(PetrolElement element) {
		if(getInitialState() instanceof SocialState) {
			SocialState state = (SocialState) getInitialState();
			state.setPetrolSupplier(element);
		}
	}
	
	public void setWaterSupplier(WaterElement element) {
		if(getInitialState() instanceof SocialState) {
			SocialState state = (SocialState) getInitialState();
			state.setWaterSupplier(element);
		}
	}
	
	@Override
	public double getElectReceived() {
		if(getState() instanceof SocialState) {
			return ((SocialState)getState()).getElectReceived();
		}
		return 0;
	}
	
	@Override
	public double getPetrolReceived() {
		if(getState() instanceof SocialState) {
			return ((SocialState)getState()).getPetrolReceived();
		}
		return 0;
	}
	
	@Override
	public double getWaterReceived() {
		if(getState() instanceof SocialState) {
			return ((SocialState)getState()).getWaterReceived();
		}
		return 0;
	}
}