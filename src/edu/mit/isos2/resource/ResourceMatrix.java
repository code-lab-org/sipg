package edu.mit.isos2.resource;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;


public final class ResourceMatrix {
	private Resource[] resources = new Resource[ResourceType.values().length];
	
	public ResourceMatrix() {
		for(int i = 0; i < resources.length; i++) {
			resources[i] = ResourceFactory.create();
		}
	}
	
	public ResourceMatrix(Resource[] resources) {
		if(resources.length != this.resources.length) {
			throw new IllegalArgumentException("Not enough amounts. Expected " 
					+ this.resources.length + ", received " + resources.length + ".");
		} else {
			this.resources = Arrays.copyOf(resources, resources.length);
		}
	}
	
	public ResourceMatrix(ResourceType[] types, Resource[] resources) {
		if(types.length != resources.length) {
			throw new IllegalArgumentException("Unbalanced arguments. " 
					+ types.length + "types, " + resources.length + " resources.");
		} else {
			for(int i = 0; i < types.length; i++) {
				this.resources[types[i].ordinal()] = resources[i].copy();
			}
			for(int i = 0; i < resources.length; i++) {
				if(resources[i] == null) {
					resources[i] = ResourceFactory.create();
				}
			}
		}
	}
	
	public ResourceMatrix(ResourceType type, Resource resource) {
		resources[type.ordinal()] = resource;
		for(int i = 0; i < resources.length; i++) {
			if(resources[i] == null) {
				resources[i] = ResourceFactory.create();
			}
		}
	}
	
	@Override
	public final boolean equals(Object object) {
		if(object instanceof ResourceMatrix) {
			return subtract((ResourceMatrix) object).isZero();
		} else {
			return false;
		}
	}

	@Override
	public final String toString() {
		NumberFormat format = new DecimalFormat("0.00");
		StringBuilder b = new StringBuilder().append("[");
		for(ResourceType r : ResourceType.values()) {
			b.append("[");
			for(ResourceType c : ResourceType.values()) {
				b.append(format.format(getResource(r).getQuantity(c)));
				if(c.ordinal() < ResourceType.values().length - 1) {
					b.append(", ");
				}
			}
			b.append("]");
			if(r.ordinal() < ResourceType.values().length - 1) {
				b.append(", ");
			}
		}
		return b.append("]").toString();
	}

	public ResourceMatrix subtract(ResourceMatrix matrix) {
		return add(matrix.negate());
	}
	
	public ResourceMatrix copy() {
		return new ResourceMatrix(Arrays.copyOf(resources, resources.length));
	}
	
	public ResourceMatrix add(ResourceMatrix matrix) {
		Resource[] newResources = new Resource[resources.length];
		for(ResourceType t : ResourceType.values()) {
			newResources[t.ordinal()] = resources[t.ordinal()].add(matrix.getResource(t));
		}
		return new ResourceMatrix(newResources);
	}
	
	public ResourceMatrix negate() {
		Resource[] newResources = new Resource[resources.length];
		for(ResourceType t : ResourceType.values()) {
			newResources[t.ordinal()] = resources[t.ordinal()].negate();
		}
		return new ResourceMatrix(newResources);
	}
	
	public ResourceMatrix multiply(double scalar) {
		Resource[] newResources = new Resource[resources.length];
		for(ResourceType t : ResourceType.values()) {
			newResources[t.ordinal()] = resources[t.ordinal()].multiply(scalar);
		}
		return new ResourceMatrix(newResources);
	}
	
	public Resource getResource(ResourceType type) {
		return resources[type.ordinal()].copy();
	}
	
	public Resource multiply(Resource resource) {
		Resource newResource = ResourceFactory.create();
		for(ResourceType t : ResourceType.values()) {
			newResource = newResource.add(getResource(t).multiply(resource.getQuantity(t)));
		}
		return newResource;
	}
	
	public boolean isZero() {
		for(ResourceType t : ResourceType.values()) {
			if(!resources[t.ordinal()].isZero()) {
				return false;
			}
		}
		return true;
	}
}
