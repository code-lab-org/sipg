package edu.mit.isos;

public class WaterSystemElement extends Element {
	private transient Resource socialSupply, nextSocialSupply;
	private transient Resource electricitySupply, nextElectricitySupply;
	private Element socialSystem;
	private Element electricitySystem;
	
	public WaterSystemElement(String name, Location initialLocation, 
			Resource initialStock, State initialState) {
		super(name, initialLocation, initialStock, initialState);
	}
	
	@Override
	public Resource getStoreOutputs() {
		return getTransformInputs().get(Resource.AQUIFER);
	}
	
	@Override
	public Resource getTransformInputs() {
		return getWaterProduction().get(Resource.WATER).swap(Resource.WATER, Resource.AQUIFER)
				.add(getWaterProduction().get(Resource.WATER).swap(Resource.WATER, Resource.ELECTRICITY).multiply(1));
	}
	
	public Resource getWaterProduction() {
		return socialSupply.add(electricitySupply);
	}
	
	@Override
	public Resource getTransformOutputs() {
		return getWaterProduction()
				.add(getTransformInputs().get(Resource.ELECTRICITY).swap(Resource.ELECTRICITY, Resource.CURRENCY).multiply(-1))
				.add(getWaterProduction().get(Resource.WATER).swap(Resource.WATER, Resource.CURRENCY).multiply(1.5));
	}
	
	@Override
	public void initialize(long initialTime) {
		super.initialize(initialTime);
		socialSupply = new Resource();
		electricitySupply = new Resource();
	}
	
	@Override
	public void miniTick() {
		nextSocialSupply = socialSystem==null?new Resource():socialSystem.getExchangeFrom(this).get(Resource.WATER);
		nextElectricitySupply = electricitySystem==null?new Resource():electricitySystem.getExchangeFrom(this).get(Resource.WATER);
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
		if(element == null) {
			return new Resource();
		} else if(element == electricitySystem) {
			return getTransformInputs().get(Resource.ELECTRICITY);
		} else {
			return new Resource();
		}
	}

	@Override
	public Resource getExchangeInputs() {
		return getTransformOutputs();
	}

	@Override
	public Resource getExchangeOutputs() {
		return getTransformInputs().get(Resource.ELECTRICITY);
	}
}