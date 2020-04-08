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
package edu.mit.sipg.sim;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.event.EventListenerList;

import edu.mit.sipg.gui.event.ConnectionEvent;
import edu.mit.sipg.gui.event.ConnectionListener;

/**
 * A default implementation of the connection interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultConnection implements Connection {
	private static final long serialVersionUID = -4649447975331252471L;
	private String federationName, federateName;
	private transient volatile AtomicBoolean connected = new AtomicBoolean(false);
	private final transient EventListenerList listenerList = 
			new EventListenerList();
	
	/**
	 * Instantiates a new default connection.
	 */
	public DefaultConnection() {
		federationName = "";
		federateName = "";
	}
	
	/**
	 * Instantiates a new default connection.
	 *
	 * @param federationName the federation name
	 * @param federateName the federate name
	 */
	public DefaultConnection(String federationName, String federateName) {
		this.federationName = federationName;
		this.federateName = federateName;
	}
	
	@Override
	public void addConnectionListener(ConnectionListener listener) {
		listenerList.add(ConnectionListener.class, listener);
	}

	/**
	 * Fire connection event.
	 *
	 * @param event the event
	 */
	private void fireConnectionEvent(ConnectionEvent event) {
		ConnectionListener[] listeners = listenerList.getListeners(ConnectionListener.class);
		for(int i = 0; i < listeners.length; i++) {
			listeners[i].connectionEventOccurred(event);
		}
	}

	@Override
	public String getFederateName() {
		return federateName;
	}

	@Override
	public String getFederationName() {
		return federationName;
	}

	@Override
	public boolean isConnected() {
		synchronized(connected) {
			return connected.get();
		}
	}
	
	@Override
	public void removeConnectionListener(ConnectionListener listener) {
		listenerList.remove(ConnectionListener.class, listener);
	}

	@Override
	public void setConnected(boolean connected) {
		synchronized(this.connected) {
			this.connected.set(connected);
			fireConnectionEvent(new ConnectionEvent(this, this));
		}
	}
	
	@Override
	public void setFederateName(String federateName) {
		if(federateName == null) {
			throw new IllegalArgumentException("Federate name cannot be null");
		}
		this.federateName = federateName;
	}
	
	@Override
	public void setFederationName(String federationName) {
		if(federationName == null) {
			throw new IllegalArgumentException("Federation name cannot be null");
		}
		this.federationName = federationName;
	}
}
