package edu.mit.isos2.resource;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigDecimalArrayResource extends DefaultResource implements Resource {
	private static final BigDecimal epsilon = new BigDecimal("1e-12");
	private static final MathContext context = MathContext.DECIMAL32;
	private final BigDecimal[] amount = new BigDecimal[ResourceType.values().length];
	
	protected BigDecimalArrayResource() {
		for(int i = 0; i < amount.length; i++) {
			amount[i] = BigDecimal.ZERO;
		}
	}
	
	protected BigDecimalArrayResource(String[] amounts) {
		this();
		if(amounts.length != amount.length) {
			throw new IllegalArgumentException("Not enough amounts. Expected " 
					+ amount.length + ", received " + amounts.length + ".");
		}
		for(ResourceType t : ResourceType.values()) {
			amount[t.ordinal()] = new BigDecimal(amounts[t.ordinal()], context);
		}
	}
	
	protected BigDecimalArrayResource(ResourceType type, String amount) {
		this(type, new BigDecimal(amount, context));
	}
	
	private BigDecimalArrayResource(ResourceType type, BigDecimal amount) {
		this();
		this.amount[type.ordinal()] = amount;
	}
	
	public BigDecimalArrayResource add(Resource resource) {
		BigDecimalArrayResource newResource = new BigDecimalArrayResource();
		for(ResourceType t : ResourceType.values()) {
			newResource.amount[t.ordinal()] = this.amount[t.ordinal()].add(
					new BigDecimal(resource.getQuantity(t)), context);
		}
		return newResource;
	}
	
	public BigDecimalArrayResource negate() {
		BigDecimalArrayResource newResource = new BigDecimalArrayResource();
		for(ResourceType type : ResourceType.values()) {
			newResource.amount[type.ordinal()] = this.amount[type.ordinal()].negate();
		}
		return newResource;
	}
	
	public BigDecimalArrayResource multiply(double scalar) {
		BigDecimalArrayResource newResource = new BigDecimalArrayResource();
		for(ResourceType t : ResourceType.values()) {
			newResource.amount[t.ordinal()] = this.amount[t.ordinal()].multiply(
					new BigDecimal(scalar, context), context);
		}
		return newResource;
	}
	
	public BigDecimalArrayResource multiply(Resource resource) {
		BigDecimalArrayResource newResource = copy();
		for(ResourceType t : ResourceType.values()) {
			newResource.amount[t.ordinal()] = this.amount[t.ordinal()].multiply(
					new BigDecimal(resource.getQuantity(t)), context);
		}
		return newResource;
	}
	
	public BigDecimalArrayResource copy() {
		BigDecimalArrayResource newResource = new BigDecimalArrayResource();
		for(ResourceType t : ResourceType.values()) {
			newResource.amount[t.ordinal()] = this.amount[t.ordinal()];
		}
		return newResource;
	}
	
	public BigDecimalArrayResource swap(ResourceType oldType, ResourceType newType) {
		BigDecimalArrayResource newResource = copy();
		BigDecimal value = newResource.amount[oldType.ordinal()];
		newResource.amount[oldType.ordinal()] = newResource.amount[newType.ordinal()];
		newResource.amount[newType.ordinal()] = value;
		return newResource;
	}
	
	public BigDecimalArrayResource get(ResourceType type) {
		return new BigDecimalArrayResource(type, amount[type.ordinal()]);
	}

	@Override
	public double getQuantity(ResourceType type) {
		return amount[type.ordinal()].doubleValue();
	}

	@Override
	public boolean isZero() {
		for(ResourceType t : ResourceType.values()) {
			if(amount[t.ordinal()].abs().compareTo(epsilon) > 0) {
				return false;
			}
		}
		return true;
	}
	
	public final Resource safeDivide(Resource resource) {
		BigDecimalArrayResource newResource = new BigDecimalArrayResource();
		for(ResourceType t : ResourceType.values()) {
			if(resource.getQuantity(t) != 0) {
				newResource.amount[t.ordinal()] = amount[t.ordinal()]
						.divide(new BigDecimal(resource.getQuantity(t), context));
			}
		}
		return newResource;
	}
}
