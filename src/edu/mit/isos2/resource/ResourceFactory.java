package edu.mit.isos2.resource;


public abstract class ResourceFactory {
	public static Resource createResource() {
		return new DoubleArrayResource();
	}
	
	public static Resource createResource(String[] amounts) {
		return new DoubleArrayResource(amounts);
	}
	
	public static Resource createResource(ResourceType type, String amount) {
		return new DoubleArrayResource(type, amount);
	}
}
