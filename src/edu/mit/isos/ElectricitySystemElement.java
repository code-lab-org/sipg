package edu.mit.isos;

public class ElectricitySystemElement extends Element {
	private transient Resource socialSupply, nextSocialSupply; 
	private transient Resource waterSupply, nextWaterSupply; 
	private Element socialSystem, waterSystem, oilSystem;
	
	public ElectricitySystemElement(String name, Location initialLocation, 
			State initialState) {
		super(name, initialLocation, new Resource(), initialState);
	}
	
	@Override
	public Resource getTransformInputs() {
		return getTransformOutputs().get(Resource.ELECTRICITY).swap(Resource.ELECTRICITY, Resource.OIL).multiply(0.5)
				.add(getTransformOutputs().get(Resource.ELECTRICITY).swap(Resource.ELECTRICITY, Resource.WATER).multiply(0.1));
	}
	
	@Override
	public Resource getTransformOutputs() {
		return socialSupply.add(waterSupply);
	}
	
	@Override
	public void initialize(long initialTime) {
		super.initialize(initialTime);
		socialSupply = new Resource();
		waterSupply = new Resource();
	}

	@Override
	public void miniTick() {
		nextSocialSupply = socialSystem==null?new Resource():socialSystem.getExchangeFrom(this);
		nextWaterSupply = waterSystem==null?new Resource():waterSystem.getExchangeFrom(this);
	}
	
	@Override
	public void miniTock() {
		socialSupply = nextSocialSupply;
		waterSupply = nextWaterSupply;
	}

	@Override
	public Resource getExchangeTo(Element element) {
		if(element == null) {
			return new Resource();
		} else if(element == socialSystem) {
			return socialSupply;
		} else if(element == waterSystem) {
			return waterSupply;
		} else {
			return new Resource();
		}
	}

	@Override
	public Resource getExchangeFrom(Element element) {
		if(element == null) {
			return new Resource();
		} else if(element == waterSystem) {
			return getTransformInputs().get(Resource.WATER);
		} else if(element == oilSystem) {
			return getTransformInputs().get(Resource.OIL);
		} else {
			return new Resource();
		}
	}
	
	public void setSocialSystem(Element socialSystem) {
		this.socialSystem = socialSystem;
	}
	
	public void setWaterSystem(Element waterSystem) {
		this.waterSystem = waterSystem;
	}
	
	public void setOilSystem(Element oilSystem) {
		this.oilSystem = oilSystem;
	}

	@Override
	public Resource getExchangeInputs() {
		return getTransformOutputs();
	}

	@Override
	public Resource getExchangeOutputs() {
		return getTransformInputs();
	}
}
