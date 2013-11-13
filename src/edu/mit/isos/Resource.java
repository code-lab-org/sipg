package edu.mit.isos;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Resource {
	private static MathContext context = MathContext.UNLIMITED;//new MathContext(6, RoundingMode.HALF_UP);
	protected List<BigDecimal> amount = new ArrayList<BigDecimal>(5);
	
	public Resource() {
		for(int i = 0; i < 5; i++) {
			amount.add(i, new BigDecimal(0, context));
		}
	}
	
	public Resource(String amount0, String amount1, String amount2, 
			String amount3, String amount4) {
		this();
		amount.set(0, new BigDecimal(amount0, context));
		amount.set(1, new BigDecimal(amount1, context));
		amount.set(2, new BigDecimal(amount2, context));
		amount.set(3, new BigDecimal(amount3, context));
		amount.set(4, new BigDecimal(amount4, context));
	}
	
	public Resource(int index, String amount) {
		this(index, new BigDecimal(amount, context));
	}
	
	protected Resource(int index, BigDecimal amount) {
		this();
		this.amount.set(index, amount);
	}
	
	public Resource add(Resource resource) {
		Resource newResource = new Resource();
		for(int i = 0; i < amount.size(); i++) {
			newResource.amount.set(i, this.amount.get(i).add(resource.amount.get(i), context));
		}
		return newResource;
	}
	
	public Resource subtract(Resource resource) {
		Resource newResource = new Resource();
		for(int i = 0; i < amount.size(); i++) {
			newResource.amount.set(i, this.amount.get(i).subtract(resource.amount.get(i), context));
		}
		return newResource;
	}
	
	public Resource multiply(double scalar) {
		Resource newResource = new Resource();
		for(int i = 0; i < amount.size(); i++) {
			newResource.amount.set(i, this.amount.get(i).multiply(new BigDecimal(scalar, context), context));
		}
		return newResource;
	}
	
	public Resource multiply(Resource resource) {
		Resource newResource = copy();
		for(int i = 0; i < amount.size(); i++) {
			newResource.amount.set(i, this.amount.get(i).multiply(resource.amount.get(i), context));
		}
		return newResource;
	}
	
	private Resource copy() {
		Resource newResource = new Resource();
		for(int i = 0; i < amount.size(); i++) {
			newResource.amount.set(i, this.amount.get(i));
		}
		return newResource;
	}
	
	public Resource swap(int oldIndex, int newIndex) {
		Resource newResource = copy();
		BigDecimal value = newResource.amount.get(oldIndex);
		newResource.amount.set(oldIndex, newResource.amount.get(newIndex));
		newResource.amount.set(newIndex, value);
		return newResource;
	}
	
	public Resource get(int index) {
		return new Resource(index, amount.get(index));
	}
	
	public boolean equals(Object object) {
		if(object instanceof Resource) {
			Resource resource = (Resource) object;
			for(int i = 0; i < amount.size(); i++) {
				if(amount.get(i).compareTo(resource.amount.get(i))!=0) {
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
		for(int i = 0; i < amount.size(); i++) {
			b.append(format.format(amount.get(i).doubleValue()));
			if(i < amount.size() - 1) {
				b.append(", ");
			}
		}
		return b.append("]").toString();
	}
}
