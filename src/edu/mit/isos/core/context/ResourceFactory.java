package edu.mit.isos.core.context;

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
	
	public static Resource create(ResourceType[] types, String amounts[]) {
		if(types.length != amounts.length) {
			throw new IllegalArgumentException("Unbalanced arguments. " 
					+ types.length + "types, " + amounts.length + " amounts.");
		} else {
			Resource resource = create();
			for(int i = 0; i < types.length; i++) {
				resource = resource.add(create(types[i], amounts[i]));
			}
			return resource;
		}
	}
	
	public static Resource create(ResourceType[] types, double amounts[]) {
		if(types.length != amounts.length) {
			throw new IllegalArgumentException("Unbalanced arguments. " 
					+ types.length + "types, " + amounts.length + " amounts.");
		} else {
			Resource resource = create();
			for(int i = 0; i < types.length; i++) {
				resource = resource.add(create(types[i], amounts[i]));
			}
			return resource;
		}
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