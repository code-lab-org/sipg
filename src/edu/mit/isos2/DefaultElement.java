package edu.mit.isos2;

public class DefaultElement extends Element {
	protected DefaultElement() {
		super();
	}
	
	public DefaultElement(String name, Location initialLocation) {
		super(name, initialLocation);
	}

	@Override
	public Resource getInputRate() {
		return new BigDecimalArrayResource();
	}
	@Override
	public Resource getOutputRate() {
		return new BigDecimalArrayResource();
	}
	@Override
	public Resource getProductionRate() {
		return new BigDecimalArrayResource();
	}
	@Override
	public Resource getConsumptionRate() {
		return new BigDecimalArrayResource();
	}
	@Override
	public Resource getStorageRate() {
		return new BigDecimalArrayResource();
	}
	@Override
	public Resource getRetrievalRate() {
		return new BigDecimalArrayResource();
	}
	@Override
	public Resource getSendingRate() {
		return new BigDecimalArrayResource();
	}
	@Override
	public Resource getSendingRateTo(Element element) {
		return new BigDecimalArrayResource();
	}
	@Override
	public Resource getReceivingRate() {
		return new BigDecimalArrayResource();
	}
	@Override
	public Resource getReceivingRateFrom(Element element) {
		return new BigDecimalArrayResource();
	}

}
