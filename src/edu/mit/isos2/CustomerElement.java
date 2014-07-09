package edu.mit.isos2;

import edu.mit.isos2.resource.Resource;

public class CustomerElement extends DefaultElement {
	private Element supplier;
	
	protected CustomerElement() {
		super();
	}
	
	public CustomerElement(String name, Location initialLocation) {
		super(name, initialLocation);
	}
	
	public void setSupplier(Element supplier) {
		this.supplier = supplier;
	}
	
	@Override
	public Resource getSendingRateTo(Element element) {
		if(element.equals(supplier)) {
			return getSendingRate();
		} else {
			return super.getSendingRateTo(element);
		}
	}
}