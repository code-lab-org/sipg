package edu.mit.sips.hla;

import java.util.EventListener;

/**
 * The listener interface for receiving attributeChange events.
 * The class that is interested in processing a attributeChange
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addAttributeChangeListener<code> method. When
 * the attributeChange event occurs, that object's appropriate
 * method is invoked.
 *
 * @see AttributeChangeEvent
 */
public interface AttributeChangeListener extends EventListener {
	
	/**
	 * Attribute changed.
	 *
	 * @param evt the evt
	 */
	public void attributeChanged(AttributeChangeEvent evt);
}
