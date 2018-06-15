package edu.mit.sips.gui.event;

import java.util.EventListener;

/**
 * The listener interface for receiving connection events.
 * The class that is interested in processing a connection
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addConnectionListener<code> method. When
 * the connection event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ConnectionEvent
 * @author Paul T Grogan, ptgrogan@mit.edu
 */
public interface ConnectionListener extends EventListener {
	
	/**
	 * Connection event occurred.
	 *
	 * @param e the event
	 */
	public void connectionEventOccurred(ConnectionEvent e);
}
