package edu.mit.isos2.resource;


public interface Resource {
	public Resource add(Resource resource);
	
	public Resource negate();

	public Resource subtract(Resource resource);
	
	public Resource multiply(double scalar);
	
	public Resource multiply(Resource resource);
	
	public Resource safeDivide(Resource resource);
	
	public Resource absoluteValue();
	
	public Resource copy();
	
	public Resource swap(ResourceType oldType, ResourceType newType);
	
	public Resource get(ResourceType type);
	
	public double getQuantity(ResourceType type);
	
	public boolean isZero();
	
	public Resource truncatePositive();
	
	public Resource truncateNegative();
}
