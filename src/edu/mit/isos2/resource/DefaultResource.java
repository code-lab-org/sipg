package edu.mit.isos2.resource;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class DefaultResource implements Resource {

	@Override
	public final boolean equals(Object object) {
		if(object instanceof Resource) {
			return subtract((Resource) object).isZero();
		} else {
			return false;
		}
	}

	@Override
	public final String toString() {
		NumberFormat format = new DecimalFormat("0.00");
		StringBuilder b = new StringBuilder().append("[");
		for(ResourceType t : ResourceType.values()) {
			b.append(format.format(getQuantity(t)));
			if(t.ordinal() < ResourceType.values().length - 1) {
				b.append(", ");
			}
		}
		return b.append("]").toString();
	}

	@Override
	public final Resource subtract(Resource resource) {
		return add(resource.negate());
	}
}
