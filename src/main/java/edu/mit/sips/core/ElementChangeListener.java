package edu.mit.sips.core;

import java.util.EventListener;

/**
 * The listener interface for receiving elementChange events.
 * The class that is interested in processing a elementChange
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addElementChangeListener<code> method. When
 * the elementChange event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ElementChangeEvent
 */
public interface ElementChangeListener extends EventListener {
	
	/**
	 * Attribute changed.
	 *
	 * @param evt the evt
	 */
	public void elementChanged(ElementChangeEvent evt);
}
