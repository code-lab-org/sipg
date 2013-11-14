package edu.mit.isos;

public class HighwayElement extends Element {
	private Element socialSystem;
	
	public HighwayElement(String name, Location initialLocation, State initialState) {
		super(name, initialLocation, new Resource(), initialState);
	}
	
	public void setSocialSystem(Element socialSystem) {
		this.socialSystem = socialSystem;
	}
	
	@Override
	public Resource getTransportInputs() {
		return socialSystem==null?new Resource():socialSystem.getStock().get(Resource.PEOPLE).multiply(0.01);
	}
	
	@Override
	public Resource getTransportOutputs() {
		return getTransportInputs().subtract(getTransformInputs()); // receive people
	}

	@Override
	public Resource getExchangeTo(Element element) {
		return new Resource();
	}

	@Override
	public Resource getExchangeFrom(Element element) {
		return new Resource();
	}

	@Override
	public Resource getExchangeInputs() {
		return new Resource();
	}

	@Override
	public Resource getExchangeOutputs() {
		return new Resource();
	}
}
