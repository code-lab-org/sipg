package edu.mit.sips.gui.event;

import java.util.EventObject;

import edu.mit.sips.hla.FederationConnection;

/**
 * The Class ConnectionEvent.
 * 
 * @author Paul T Grogan, ptgrogan@mit.edu
 */
public class ConnectionEvent extends EventObject {
	private static final long serialVersionUID = -389874206436130676L;
	
	private final FederationConnection connection;
	
	/**
	 * Instantiates a new connection event.
	 *
	 * @param source the source
	 * @param connected the connected
	 * @param host the host
	 * @param federationName the federation name
	 */
	public ConnectionEvent(Object source, FederationConnection connection) {
		super(source);
		this.connection = connection;
	}
	
	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public FederationConnection getConnection() {
		return connection;
	}
}
