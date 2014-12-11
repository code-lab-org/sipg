package edu.mit.isos.core.context;

public class DoubleArrayResource extends DefaultResource implements Resource {
	private static final double epsilon = 1e-12;
	private final double[] amount = new double[ResourceType.values().length];
	
	protected DoubleArrayResource() {
		for(int i = 0; i < amount.length; i++) {
			amount[i] = 0;
		}
	}
	
	protected DoubleArrayResource(String[] amounts) {
		this();
		if(amounts.length != amount.length) {
			throw new IllegalArgumentException("Not enough amounts. Expected " 
					+ amount.length + ", received " + amounts.length + ".");
		}
		for(ResourceType t : ResourceType.values()) {
			amount[t.ordinal()] = Double.parseDouble(amounts[t.ordinal()]);
		}
	}
	
	protected DoubleArrayResource(ResourceType type, String amount) {
		this(type, Double.parseDouble(amount));
	}
	
	private DoubleArrayResource(ResourceType type, double amount) {
		this();
		this.amount[type.ordinal()] = amount;
	}
	
	public DoubleArrayResource add(Resource resource) {
		DoubleArrayResource newResource = new DoubleArrayResource();
		for(ResourceType t : ResourceType.values()) {
			newResource.amount[t.ordinal()] = this.amount[t.ordinal()] 
					+ resource.getQuantity(t);
		}
		return newResource;
	}
	
	public DoubleArrayResource negate() {
		DoubleArrayResource newResource = new DoubleArrayResource();
		for(ResourceType type : ResourceType.values()) {
			newResource.amount[type.ordinal()] = -this.amount[type.ordinal()];
		}
		return newResource;
	}
	
	public DoubleArrayResource multiply(double scalar) {
		DoubleArrayResource newResource = new DoubleArrayResource();
		for(ResourceType t : ResourceType.values()) {
			newResource.amount[t.ordinal()] = this.amount[t.ordinal()] * scalar;
		}
		return newResource;
	}
	
	public DoubleArrayResource multiply(Resource resource) {
		DoubleArrayResource newResource = copy();
		for(ResourceType t : ResourceType.values()) {
			newResource.amount[t.ordinal()] = this.amount[t.ordinal()]
					* resource.getQuantity(t);
		}
		return newResource;
	}
	
	public DoubleArrayResource copy() {
		DoubleArrayResource newResource = new DoubleArrayResource();
		for(ResourceType t : ResourceType.values()) {
			newResource.amount[t.ordinal()] = this.amount[t.ordinal()];
		}
		return newResource;
	}
	
	public DoubleArrayResource swap(ResourceType oldType, ResourceType newType) {
		DoubleArrayResource newResource = copy();
		double value = newResource.amount[oldType.ordinal()];
		newResource.amount[oldType.ordinal()] = newResource.amount[newType.ordinal()];
		newResource.amount[newType.ordinal()] = value;
		return newResource;
	}
	
	public DoubleArrayResource get(ResourceType type) {
		return new DoubleArrayResource(type, amount[type.ordinal()]);
	}

	@Override
	public double getQuantity(ResourceType type) {
		return amount[type.ordinal()];
	}

	@Override
	public boolean isZero() {
		for(ResourceType t : ResourceType.values()) {
			if(Math.abs(amount[t.ordinal()]) > epsilon) {
				return false;
			}
		}
		return true;
	}

	public final Resource safeDivide(Resource resource) {
		DoubleArrayResource newResource = new DoubleArrayResource();
		for(ResourceType t : ResourceType.values()) {
			if(Math.abs(resource.getQuantity(t)) > epsilon) {
				newResource.amount[t.ordinal()] = amount[t.ordinal()] / resource.getQuantity(t);
			}
		}
		return newResource;
	}
	
	public final Resource absoluteValue() {
		DoubleArrayResource newResource = new DoubleArrayResource();
		for(ResourceType t : ResourceType.values()) {
			newResource.amount[t.ordinal()] = Math.abs(amount[t.ordinal()]);
		}
		return newResource;
	}
}
