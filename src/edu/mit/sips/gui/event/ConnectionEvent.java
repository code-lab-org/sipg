package edu.mit.sips.gui.event;

import java.util.EventObject;

/**
 * The Class ConnectionEvent.
 * 
 * @author Paul T Grogan, ptgrogan@mit.edu
 */
public class ConnectionEvent extends EventObject {
	private static final long serialVersionUID = -389874206436130676L;
	
	private final boolean connected;
	
	/**
	 * Instantiates a new connection event.
	 *
	 * @param source the source
	 * @param connected the connected
	 */
	public ConnectionEvent(Object source, boolean connected) {
		super(source);
		this.connected = connected;
	}
	
	/**
	 * Checks if is connected.
	 *
	 * @return true, if is connected
	 */
	public boolean isConnected() {
		return connected;
	}
}
