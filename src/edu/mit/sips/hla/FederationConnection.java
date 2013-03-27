package edu.mit.sips.hla;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.event.EventListenerList;

import edu.mit.sips.gui.event.ConnectionEvent;
import edu.mit.sips.gui.event.ConnectionListener;

/**
 * The Class FederationConnection.
 */
public class FederationConnection implements Serializable {
	private static final long serialVersionUID = -4649447975331252471L;
	private static final String CONNECTION_DATA = "connection.data";
	private String host, federationName, fomPath, federateName, federateType;
	private int port;
	private transient volatile AtomicBoolean connected = new AtomicBoolean(false);
	
	private final transient EventListenerList listenerList = 
			new EventListenerList();
	
	/**
	 * Instantiates a new federation connection.
	 */
	public FederationConnection() {
		host = "";
		port = 0;
		federationName = "";
		fomPath = "";
		federateName = "";
		federateType = "";
		loadData();
	}
	
	/**
	 * Instantiates a new federation connection.
	 *
	 * @param host the host
	 * @param port the port
	 * @param federationName the federation name
	 * @param fomPath the fom path
	 * @param federateName the federate name
	 * @param federateType the federate type
	 */
	public FederationConnection(String host, int port, String federationName, 
			String fomPath, String federateName, String federateType) {
		this.host = host;
		this.port = port;
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
	 * Clears the saved connection data file.
	 */
	public void clearData() {
		new File(CONNECTION_DATA).delete();
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
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets the local settings designator.
	 *
	 * @return the local settings designator
	 */
	public String getLocalSettingsDesignator() {
		// format for pitch prti
		return "crcHost=" + host + ":" + port;
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
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
	 * Checks if is data saved.
	 *
	 * @return true, if is data saved
	 */
	public boolean isDataSaved() {
		InputStream input;
		try {
			input = new FileInputStream(new File(CONNECTION_DATA));
			input.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return host.isEmpty() 
				&& port == 0 
				&& federationName.isEmpty() 
				&& fomPath.isEmpty() 
				&& federateName.isEmpty() 
				&& federateType.isEmpty();
	}
	
	/**
	 * Loads connection data from file.
	 */
	private void loadData() {
		InputStream input;
		Properties properties = new Properties();
		try {
			input = new FileInputStream(new File(CONNECTION_DATA));
			properties.loadFromXML(input);
			input.close();
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		federateName = properties.getProperty("name");
		federateType = properties.getProperty("type");
		federationName = properties.getProperty("federation");
		fomPath = properties.getProperty("fom");
		host = properties.getProperty("host");
		port = Integer.parseInt(properties.getProperty("port"));
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
	 * Saves the connection data to file.
	 */
	public void saveData() {
		OutputStream output;
		Properties properties = new Properties();
		properties.setProperty("name", federateName);
		properties.setProperty("type", federateType);
		properties.setProperty("federation", federationName);
		properties.setProperty("fom", fomPath);
		properties.setProperty("host", host);
		properties.setProperty("port", new Integer(port).toString());
		try {
			output = new FileOutputStream(new File(CONNECTION_DATA));
			properties.storeToXML(output, null);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	/**
	 * Sets the host.
	 *
	 * @param host the new host
	 */
	public void setHost(String host) {
		if(host == null) {
			throw new IllegalArgumentException("Host cannot be null");
		}
		this.host = host;
	}
	
	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(int port) {
		if(port < 0) {
			throw new IllegalArgumentException("Port cannot be negative");
		}
		this.port = port;
	}
}
