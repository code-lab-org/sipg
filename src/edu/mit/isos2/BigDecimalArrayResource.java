package edu.mit.isos2;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BigDecimalArrayResource implements Resource {
	private static BigDecimal epsilon = new BigDecimal("1e-14");
	private static MathContext context = MathContext.DECIMAL32;
	private BigDecimal[] amount = new BigDecimal[Resource.ResourceType.values().length];
	
	public BigDecimalArrayResource() {
		for(int i = 0; i < amount.length; i++) {
			amount[i] = BigDecimal.ZERO;
		}
	}
	
	public BigDecimalArrayResource(String[] amounts) {
		this();
		if(amounts.length != amount.length) {
			throw new IllegalArgumentException("Not enough amounts. Expected " 
					+ amount.length + ", received " + amounts.length + ".");
		}
		for(ResourceType t : Resource.ResourceType.values()) {
			amount[t.ordinal()] = new BigDecimal(amounts[t.ordinal()], context);
		}
	}
	
	public BigDecimalArrayResource(ResourceType type, String amount) {
		this(type, new BigDecimal(amount, context));
	}
	
	protected BigDecimalArrayResource(ResourceType type, BigDecimal amount) {
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
	
	public BigDecimalArrayResource subtract(Resource resource) {
		BigDecimalArrayResource newResource = new BigDecimalArrayResource();
		for(ResourceType t : ResourceType.values()) {
			newResource.amount[t.ordinal()] = this.amount[t.ordinal()].subtract(
					new BigDecimal(resource.getQuantity(t)), context);
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
	
	public boolean equals(Object object) {
		if(object instanceof BigDecimalArrayResource) {
			BigDecimalArrayResource resource = (BigDecimalArrayResource) object;
			for(ResourceType t : ResourceType.values()) {
				if(amount[t.ordinal()].compareTo(resource.amount[t.ordinal()])!=0) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		NumberFormat format = new DecimalFormat("0.00");
		StringBuilder b = new StringBuilder().append("[");
		for(ResourceType t : ResourceType.values()) {
			b.append(format.format(amount[t.ordinal()].doubleValue()));
			if(t.ordinal() < amount.length - 1) {
				b.append(", ");
			}
		}
		return b.append("]").toString();
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
}
