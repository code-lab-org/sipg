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
package edu.mit.sips.gui.event;

import java.util.EventObject;

import edu.mit.sips.sim.Connection;

/**
 * Contains the details regarding a change in the connection status.
 * 
 * @author Paul T. Grogan
 */
public class ConnectionEvent extends EventObject {
	private static final long serialVersionUID = -389874206436130676L;
	
	private final Connection connection;
	
	/**
	 * Instantiates a new connection event.
	 *
	 * @param source the source
	 * @param connection the connection
	 */
	public ConnectionEvent(Object source, Connection connection) {
		super(source);
		this.connection = connection;
	}
	
	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}
}
