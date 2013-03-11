package edu.mit.sips.gui;

import java.util.EventListener;

/**
 * The listener interface for receiving update events.
 * The class that is interested in processing a update
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addUpdateListener<code> method. When
 * the update event occurs, that object's appropriate
 * method is invoked.
 *
 * @see UpdateEvent
 */
public interface UpdateListener extends EventListener {
	
	/**
	 * Simulation initialized.
	 *
	 * @param event the event
	 */
	public void simulationInitialized(UpdateEvent event);
	
	/**
	 * Invoked when simulation update occurs.
	 *
	 * @param event the event
	 */
	public void simulationUpdated(UpdateEvent event);
}
