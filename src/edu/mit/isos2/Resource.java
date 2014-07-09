package edu.mit.isos2;


public interface Resource {
	public static enum ResourceType {
		AQUIFER, WATER, ELECTRICITY, OIL, RESERVES, PEOPLE, CURRENCY
	}
	
	public Resource add(Resource resource);
	
	public Resource negate();
	
	public Resource subtract(Resource resource);
	
	public Resource multiply(double scalar);
	
	public Resource multiply(Resource resource);
	
	public Resource copy();
	
	public Resource swap(ResourceType oldType, ResourceType newType);
	
	public Resource get(ResourceType type);
	
	public double getQuantity(ResourceType type);
	
	public boolean equals(Object object);
	
	public boolean isZero();
}
