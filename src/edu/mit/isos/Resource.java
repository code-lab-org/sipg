package edu.mit.isos;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Resource {
	private List<BigDecimal> amount = new ArrayList<BigDecimal>(5);
	
	public Resource() {
		for(int i = 0; i < 5; i++) {
			amount.add(i, new BigDecimal(0));
		}
	}
	
	public Resource(double amount0, double amount1, double amount2, 
			double amount3, double amount4) {
		amount.add(0, new BigDecimal(amount0));
		amount.add(1, new BigDecimal(amount1));
		amount.add(2, new BigDecimal(amount2));
		amount.add(3, new BigDecimal(amount3));
		amount.add(4, new BigDecimal(amount4));
	}
	
	public Resource add(Resource resource) {
		Resource newResource = new Resource();
		for(int i = 0; i < amount.size(); i++) {
			newResource.amount.set(i, this.amount.get(i).add(resource.amount.get(i)));
		}
		return newResource;
	}
	
	public Resource subtract(Resource resource) {
		Resource newResource = new Resource();
		for(int i = 0; i < amount.size(); i++) {
			newResource.amount.set(i, this.amount.get(i).subtract(resource.amount.get(i)));
		}
		return newResource;
	}
	
	public Resource multiply(double scalar) {
		Resource newResource = new Resource();
		for(int i = 0; i < amount.size(); i++) {
			newResource.amount.set(i, this.amount.get(i).multiply(new BigDecimal(scalar)));
		}
		return newResource;
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
