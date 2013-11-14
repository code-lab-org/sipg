package edu.mit.isos;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Resource {
	public static final int AQUIFER = 0, WATER = 1, ELECTRICITY = 2, OIL = 3, RESERVES = 4, PEOPLE = 5, CURRENCY = 6;
	private static MathContext context = MathContext.DECIMAL32;
	private BigDecimal[] amount = new BigDecimal[7];
	
	public Resource() {
		for(int i = 0; i < amount.length; i++) {
			amount[i] = new BigDecimal(0, context);
		}
	}
	
	public Resource(String amount0, String amount1, String amount2, 
			String amount3, String amount4, String amount5) {
		this();
		amount[AQUIFER] = new BigDecimal(amount0, context);
		amount[WATER] = new BigDecimal(amount1, context);
		amount[ELECTRICITY] = new BigDecimal(amount2, context);
		amount[OIL] = new BigDecimal(amount3, context);
		amount[RESERVES] = new BigDecimal(amount4, context);
		amount[PEOPLE] = new BigDecimal(amount5, context);
	}
	
	public Resource(int index, String amount) {
		this(index, new BigDecimal(amount, context));
	}
	
	protected Resource(int index, BigDecimal amount) {
		this();
		this.amount[index] = amount;
	}
	
	public Resource add(Resource resource) {
		Resource newResource = new Resource();
		for(int i = 0; i < amount.length; i++) {
			newResource.amount[i] = this.amount[i].add(resource.amount[i], context);
		}
		return newResource;
	}
	
	public Resource subtract(Resource resource) {
		Resource newResource = new Resource();
		for(int i = 0; i < amount.length; i++) {
			newResource.amount[i] = this.amount[i].subtract(resource.amount[i], context);
		}
		return newResource;
	}
	
	public Resource multiply(double scalar) {
		Resource newResource = new Resource();
		for(int i = 0; i < amount.length; i++) {
			newResource.amount[i] = this.amount[i].multiply(new BigDecimal(scalar, context), context);
		}
		return newResource;
	}
	
	public Resource multiply(Resource resource) {
		Resource newResource = copy();
		for(int i = 0; i < amount.length; i++) {
			newResource.amount[i] = this.amount[i].multiply(resource.amount[i], context);
		}
		return newResource;
	}
	
	private Resource copy() {
		Resource newResource = new Resource();
		for(int i = 0; i < amount.length; i++) {
			newResource.amount[i] = this.amount[i];
		}
		return newResource;
	}
	
	public Resource swap(int oldIndex, int newIndex) {
		Resource newResource = copy();
		BigDecimal value = newResource.amount[oldIndex];
		newResource.amount[oldIndex] = newResource.amount[newIndex];
		newResource.amount[newIndex] = value;
		return newResource;
	}
	
	public Resource get(int index) {
		return new Resource(index, amount[index]);
	}
	
	public boolean equals(Object object) {
		if(object instanceof Resource) {
			Resource resource = (Resource) object;
			for(int i = 0; i < amount.length; i++) {
				if(amount[i].compareTo(resource.amount[i])!=0) {
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
		for(int i = 0; i < amount.length; i++) {
			b.append(format.format(amount[i].doubleValue()));
			if(i < amount.length - 1) {
				b.append(", ");
			}
		}
		return b.append("]").toString();
	}
}
