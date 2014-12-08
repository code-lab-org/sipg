package edu.mit.isos.app.elect;

import edu.mit.isos.app.ElectElement;
import edu.mit.isos.app.PetrolElement;
import edu.mit.isos.app.SocialElement;
import edu.mit.isos.app.WaterElement;
import edu.mit.isos.core.context.Location;
import edu.mit.isos.core.element.DefaultElement;

public class LocalElectElement extends DefaultElement implements ElectElement {
	public LocalElectElement(String name, Location location, 
			double solarCap, double thermalOil) {
		super(name, location, new ElectState(solarCap, thermalOil));
	}
	
	public void setPetrolSupplier(PetrolElement element) {
		if(getInitialState() instanceof ElectState) {
			ElectState state = (ElectState) getInitialState();
			state.setPetrolSupplier(element);
		}
	}
	
	public void setCustomer(PetrolElement element) {
		if(getInitialState() instanceof ElectState) {
			ElectState state = (ElectState) getInitialState();
			state.setCustomer(element);
		}
	}
	
	public void setCustomer(SocialElement element) {
		if(getInitialState() instanceof ElectState) {
			ElectState state = (ElectState) getInitialState();
			state.setCustomer(element);
		}
	}
	
	public void setCustomer(WaterElement element) {
		if(getInitialState() instanceof ElectState) {
			ElectState state = (ElectState) getInitialState();
			state.setCustomer(element);
		}
	}
	
	@Override
	public double getPetrolReceived() {
		if(getState() instanceof ElectState) {
			return ((ElectState)getState()).getPetrolReceived();
		}
		return 0;
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
}