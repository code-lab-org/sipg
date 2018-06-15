package edu.mit.sips.hla;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.event.EventListenerList;

import edu.mit.sips.gui.event.ConnectionEvent;
import edu.mit.sips.gui.event.ConnectionListener;

/**
 * The Class FederationConnection.
 */
public class FederationConnection implements Serializable {
	private static final long serialVersionUID = -4649447975331252471L;
	private String federationName, fomPath, federateName, federateType;
	private transient volatile AtomicBoolean connected = new AtomicBoolean(false);
	
	private final transient EventListenerList listenerList = 
			new EventListenerList();
	
	/**
	 * Instantiates a new federation connection.
	 */
	public FederationConnection() {
		federationName = "";
		fomPath = "";
		federateName = "";
		federateType = "";
	}
	
	/**
	 * Instantiates a new federation connection.
	 *
	 * @param federationName the federation name
	 * @param fomPath the fom path
	 * @param federateName the federate name
	 * @param federateType the federate type
	 */
	public FederationConnection(String federationName, 
			String fomPath, String federateName, String federateType) {
		this.federationName = federationName;
		this.fomPath = fomPath;
		this.federateName = federateName;
		this.federateType = federateType;
	}
	
	/**
	 * Adds the connection listener.
	 *
	 * @param listener the listener
	 */
	public void addConnectionListener(ConnectionListener listener) {
		listenerList.add(ConnectionListener.class, listener);
	}
	
	/**
	 * Fires a connection event.
	 *
	 * @param event the event
	 */
	private void fireConnectionEvent(ConnectionEvent event) {
		ConnectionListener[] listeners = listenerList.getListeners(ConnectionListener.class);
		for(int i = 0; i < listeners.length; i++) {
			listeners[i].connectionEventOccurred(event);
		}
	}

	/**
	 * Gets the federate name.
	 *
	 * @return the federate name
	 */
	public String getFederateName() {
		return federateName;
	}

	/**
	 * Gets the federate type.
	 *
	 * @return the federate type
	 */
	public String getFederateType() {
		return federateType;
	}

	/**
	 * Gets the federation name.
	 *
	 * @return the federation name
	 */
	public String getFederationName() {
		return federationName;
	}

	/**
	 * Gets the fom path.
	 *
	 * @return the fom path
	 */
	public String getFomPath() {
		return fomPath;
	}

	/**
	 * Checks if is connected.
	 *
	 * @return true, if is connected
	 */
	public boolean isConnected() {
		synchronized(connected) {
			return connected.get();
		}
	}
	
	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return federationName.isEmpty() 
				&& fomPath.isEmpty() 
				&& federateName.isEmpty() 
				&& federateType.isEmpty();
	}
	
	/**
	 * Removes the connection listener.
	 *
	 * @param listener the listener
	 */
	public void removeConnectionListener(ConnectionListener listener) {
		listenerList.remove(ConnectionListener.class, listener);
	}

	/**
	 * Sets the connected.
	 *
	 * @param connected the new connected
	 */
	public void setConnected(boolean connected) {
		synchronized(this.connected) {
			this.connected.set(connected);
			fireConnectionEvent(new ConnectionEvent(this, this));
		}
	}
	
	/**
	 * Sets the federate name.
	 *
	 * @param federateName the new federate name
	 */
	public void setFederateName(String federateName) {
		if(federateName == null) {
			throw new IllegalArgumentException("Federate name cannot be null");
		}
		this.federateName = federateName;
	}
	
	/**
	 * Sets the federate type.
	 *
	 * @param federateType the new federate type
	 */
	public void setFederateType(String federateType) {
		if(federateType == null) {
			throw new IllegalArgumentException("Federate type cannot be null");
		}
		this.federateType = federateType;
	}
	
	/**
	 * Sets the federation name.
	 *
	 * @param federationName the new federation name
	 */
	public void setFederationName(String federationName) {
		if(federationName == null) {
			throw new IllegalArgumentException("Federation name cannot be null");
		}
		this.federationName = federationName;
	}
	
	/**
	 * Sets the fom path.
	 *
	 * @param fomPath the new fom path
	 */
	public void setFomPath(String fomPath) {
		if(fomPath == null) {
			throw new IllegalArgumentException("FOM path cannot be null");
		}
		this.fomPath = fomPath;
	}
}
