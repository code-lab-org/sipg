package edu.mit.sips.core;

import java.util.EventObject;

/**
 * The Class ElementChangeEvent.
 */
public class ElementChangeEvent extends EventObject {
	private static final long serialVersionUID = 9169398194283374249L;

	/**
	 * Instantiates a new element change event.
	 *
	 * @param source the source
	 */
	public ElementChangeEvent(Object source) {
		super(source);
	}
}
