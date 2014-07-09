package edu.mit.isos2;

public class SupplierElement extends DefaultElement {
	private Element customer;
	private Resource demand;
	private transient Resource nextDemand;
	
	protected SupplierElement() {
		super();
	}
	
	public SupplierElement(String name, Location initialLocation) {
		super(name, initialLocation);
	}
	
	public void setCustomer(Element customer) {
		this.customer = customer;
	}
	
	@Override
	public Resource getSendingRate() {
		return getSendingRateTo(customer);
	}
	
	@Override
	public Resource getSendingRateTo(Element element) {
		if(element.equals(customer)) {
			return demand;
		} else {
			return super.getSendingRateTo(element);
		}
	}
	
	@Override
	public void initialize(long initialTime) {
		super.initialize(initialTime);
		demand = nextDemand = new BigDecimalArrayResource();
	}
	
	@Override
	public void iterateTick() {
		nextDemand = customer.getReceivingRateFrom(this);
	}
	
	@Override
	public void iterateTock() {
		demand = nextDemand;
	}
}