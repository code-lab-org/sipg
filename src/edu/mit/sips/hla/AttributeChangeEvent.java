package edu.mit.sips.hla;

import java.util.EventObject;

/**
 * The Class StateChangeEvent.
 */
public class AttributeChangeEvent extends EventObject {
	private static final long serialVersionUID = -8507407666558946152L;
	
	private final String attributeName;
	
	/**
	 * Instantiates a new state change event.
	 *
	 * @param source the source
	 * @param attributeName the attribute name
	 */
	public AttributeChangeEvent(Object source, String attributeName) {
		super(source);
		this.attributeName = attributeName;
	}
	
	/**
	 * Gets the attribute name.
	 *
	 * @return the attribute name
	 */
	public String getAttributeName() {
		return attributeName;
	}
}
