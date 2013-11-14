package edu.mit.isos;

public class SocialSystemElement extends Element {
	private Element transportIn, transportOut;
	private Element waterSystem, electricitySystem, oilSystem;
	
	public void setWaterSystem(Element waterSystem) {
		this.waterSystem = waterSystem;
	}

	public void setElectricitySystem(Element electricitySystem) {
		this.electricitySystem = electricitySystem;
	}

	public void setOilSystem(Element oilSystem) {
		this.oilSystem = oilSystem;
	}

	public SocialSystemElement(String name, Location initialLocation, 
			Resource initialStock, State initialState) {
		super(name, initialLocation, initialStock, initialState);
	}
	
	@Override
	public Resource getStoreInputs() {
		return getTransformOutputs().add(transportIn==null?new Resource():transportIn.getTransportOutputs());
	}
	
	@Override
	public Resource getStoreOutputs() {
		return (transportOut==null?new Resource():transportOut.getTransportInputs());
	}
	
	@Override
	public Resource getTransformInputs() {
		return getStock().get(Resource.PEOPLE).swap(Resource.PEOPLE, Resource.WATER).multiply(1)
				.add(getStock().get(Resource.PEOPLE).swap(Resource.PEOPLE, Resource.ELECTRICITY).multiply(1))
				.add(getStock().get(Resource.PEOPLE).swap(Resource.PEOPLE, Resource.OIL).multiply(1));
	}
	
	@Override
	public void initialize(long initialTime) {
		super.initialize(initialTime);
	}
	
	@Override
	public void miniTock() { }
	
	public void setTransportIn(Element transportIn) {
		this.transportIn = transportIn;
	}
	
	public void setTransportOut(Element transportLink) {
		this.transportOut = transportLink;
	}

	@Override
	public Resource getExchangeTo(Element element) {
		return new Resource();
	}

	@Override
	public Resource getExchangeFrom(Element element) {
		if(element == null) {
			return new Resource();
		} else if(element == waterSystem) {
			return getTransformInputs().get(Resource.WATER);
		} else if(element == electricitySystem) {
			return getTransformInputs().get(Resource.ELECTRICITY);
		}  else if(element == oilSystem) {
			return getTransformInputs().get(Resource.OIL);
		}  else {
			return new Resource();
		}
	}
	
	@Override
	public Resource getExchangeInputs() {
		return new Resource();
	}

	@Override
	public Resource getExchangeOutputs() {
		return getTransformInputs();
	}
}
