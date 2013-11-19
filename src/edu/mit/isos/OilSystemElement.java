package edu.mit.isos;

public class OilSystemElement extends Element { 
	private transient Resource socialSupply, nextSocialSupply; 
	private transient Resource electricitySupply, nextElectricitySupply;
	private Element socialSystem;
	private Element electricitySystem;
	
	public OilSystemElement(String name, Location initialLocation, 
			Resource initialStock, State initialState) {
		super(name, initialLocation, initialStock, initialState);
	}
	
	@Override
	public Resource getStoreOutputs() {
		return getTransformInputs();
	}
	
	@Override
	public Resource getTransformInputs() {
		return getOilProduction().get(Resource.OIL).swap(Resource.OIL, Resource.RESERVES);
	}
	
	private Resource getOilProduction() {
		return socialSupply.add(electricitySupply);
	}
	
	@Override
	public Resource getTransformOutputs() {
		return getOilProduction()
				.add(getOilProduction().get(Resource.OIL).swap(Resource.OIL, Resource.CURRENCY).multiply(10));
	}
	
	@Override
	public void initialize(long initialTime) {
		super.initialize(initialTime);
		socialSupply = new Resource();
		electricitySupply = new Resource();
	}
	
	@Override
	public void miniTick() {
		nextSocialSupply = socialSystem==null?new Resource():socialSystem.getExchangeFrom(this).get(Resource.OIL);
		nextElectricitySupply = electricitySystem==null?new Resource():electricitySystem.getExchangeFrom(this).get(Resource.OIL);
	}
	
	@Override
	public void miniTock() {
		socialSupply = nextSocialSupply;
		electricitySupply = nextElectricitySupply;
	}
	
	public void setElectricitySystem(Element electricitySystem) {
		this.electricitySystem = electricitySystem;
	}
	
	public void setSocialSystem(Element socialSystem) {
		this.socialSystem = socialSystem;
	}

	@Override
	public Resource getExchangeTo(Element element) {
		if(element == null) {
			return new Resource();
		} else if(element == socialSystem) {
			return socialSupply;
		} else if(element == electricitySystem) {
			return electricitySupply;
		} else {
			return new Resource();
		}
	}

	@Override
	public Resource getExchangeFrom(Element element) {
		return new Resource();
	}

	@Override
	public Resource getExchangeInputs() {
		return getTransformOutputs();
	}

	@Override
	public Resource getExchangeOutputs() {
		return new Resource();
	}
}