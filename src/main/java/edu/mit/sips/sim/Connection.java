/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sips.sim;

import java.io.Serializable;

import edu.mit.sips.gui.event.ConnectionListener;

/**
 * An interface to a simulation connection.
 * 
 * @author Paul T. Grogan
 */
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