package edu.mit.isos2.resource;


public abstract class ResourceFactory {
	public static Resource create() {
		return new DoubleArrayResource();
	}
	
	public static Resource create(String[] amounts) {
		return new DoubleArrayResource(amounts);
	}
	
	public static Resource create(ResourceType type, String amount) {
		return new DoubleArrayResource(type, amount);
	}
	
	public static Resource create(double[] amounts) {
		String[] strAmounts = new String[amounts.length];
		for(int i = 0; i < amounts.length; i++) {
			strAmounts[i] = String.valueOf(amounts[i]);
		}
		return create(strAmounts);
	}
	
	public static Resource create(ResourceType type, double amount) {
		return create(type, String.valueOf(amount));
	}
}