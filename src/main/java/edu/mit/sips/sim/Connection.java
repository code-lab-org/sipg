package edu.mit.sips.sim;

import java.io.Serializable;

import edu.mit.sips.gui.event.ConnectionListener;

public interface Connection extends Serializable {

	/**
	 * Adds the connection listener.
	 *
	 * @param listener the listener
	 */
	void addConnectionListener(ConnectionListener listener);

	/**
	 * Gets the federate name.
	 *
	 * @return the federate name
	 */
	String getFederateName();

	/**
	 * Gets the federation name.
	 *
	 * @return the federation name
	 */
	String getFederationName();

	/**
	 * Checks if is connected.
	 *
	 * @return true, if is connected
	 */
	boolean isConnected();

	/**
	 * Removes the connection listener.
	 *
	 * @param listener the listener
	 */
	void removeConnectionListener(ConnectionListener listener);

	/**
	 * Sets the connected.
	 *
	 * @param connected the new connected
	 */
	void setConnected(boolean connected);

	/**
	 * Sets the federate name.
	 *
	 * @param federateName the new federate name
	 */
	void setFederateName(String federateName);

	/**
	 * Sets the federation name.
	 *
	 * @param federationName the new federation name
	 */
	void setFederationName(String federationName);

}