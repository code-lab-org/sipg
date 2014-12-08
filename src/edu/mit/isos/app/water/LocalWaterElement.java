package edu.mit.isos.app.water;

import edu.mit.isos.app.ElectElement;
import edu.mit.isos.app.SocialElement;
import edu.mit.isos.app.WaterElement;
import edu.mit.isos.core.context.Location;
import edu.mit.isos.core.context.ResourceFactory;
import edu.mit.isos.core.context.ResourceType;
import edu.mit.isos.core.element.DefaultElement;

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
			state.setElectSupplier(element);
		}
	}
	
	public void setCustomer(SocialElement element) {
		if(getInitialState() instanceof WaterState) {
			WaterState state = (WaterState) getInitialState();
			state.setCustomer(element);
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
}