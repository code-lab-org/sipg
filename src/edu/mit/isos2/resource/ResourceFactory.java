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
	
	public static Resource createResource(double[] amounts) {
		String[] strAmounts = new String[amounts.length];
		for(int i = 0; i < amounts.length; i++) {
			strAmounts[i] = String.valueOf(amounts[i]);
		}
		return createResource(strAmounts);
	}
	
	public static Resource createResource(ResourceType type, double amount) {
		return createResource(type, String.valueOf(amount));
	}
}