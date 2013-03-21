package edu.mit.sips.hla;

import java.util.EventObject;
import java.util.List;

/**
 * The Class StateChangeEvent.
 */
public class AttributeChangeEvent extends EventObject {
	private static final long serialVersionUID = -8507407666558946152L;
	
	private final List<String> attributeNames;
	
	/**
	 * Instantiates a new state change event.
	 *
	 * @param source the source
	 * @param attributeNames the attribute names
	 */
	public AttributeChangeEvent(Object source, List<String> attributeNames) {
		super(source);
		this.attributeNames = attributeNames;
	}
	
	/**
	 * Gets the attribute names.
	 *
	 * @return the attribute names
	 */
	public List<String> getAttributeNames() {
		return attributeNames;
	}
}
