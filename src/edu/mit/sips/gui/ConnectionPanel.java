package edu.mit.sips.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.mit.sips.gui.event.ConnectionEvent;
import edu.mit.sips.gui.event.ConnectionListener;
import edu.mit.sips.hla.FederationConnection;
import edu.mit.sips.hla.SimAmbassador;
import edu.mit.sips.io.Icons;

/**
 * The Class ConnectionPanel. A panel allowing connections to be specified.
 * 
 * @author Paul T Grogan, ptgrogan@mit.edu
 */
public final class ConnectionPanel extends JPanel 
implements ActionListener, ConnectionListener {
	private static final long serialVersionUID = 8697615119488025958L;
	
	private static final String BROWSE_FOM = "browseFOM", 
			TOGGLE_REMEMBER = "toggleReminder",
			CONNECT = "connect";
	private static final String CONNECTION_DATA = "connection.data";
	
	private final JTextField federateName, federateType, federationName, hostAddress, portNumber, fomPath;
	private final JCheckBox rememberCheck;
	private final JButton connectButton, browseButton;
	private final JLabel statusLabel;
	
	private FederationConnection connection;
	private SimAmbassador ambassador;
	
	/**
	 * Instantiates a new connection panel.
	 */
	public ConnectionPanel() {
		federateName = new JTextField(12);
		federateName.addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorAdded(AncestorEvent event) {
				event.getComponent().requestFocusInWindow();
			}

			@Override
			public void ancestorRemoved(AncestorEvent event) { }

			@Override
			public void ancestorMoved(AncestorEvent event) { }
		});
		federateName.setToolTipText("Your user name.");
		federateType = new JTextField(12);
		federateName.setToolTipText("Your organization name.");
		federationName = new JTextField(12);
		federationName.setToolTipText("The federation to join.");
		hostAddress = new JTextField(12);
		hostAddress.setToolTipText("Address of the Central RTI Component.");
		portNumber = new JTextField(6);
		portNumber.setToolTipText("Port number of the Central RTI Component.");
		fomPath = new JTextField(20);
		fomPath.setToolTipText("The Federation Object Model (FOM) file location.");
		rememberCheck = new JCheckBox("Remember connection information");
		rememberCheck.setActionCommand(TOGGLE_REMEMBER);
		rememberCheck.addActionListener(this);
		connectButton = new JButton();
		connectButton.setActionCommand(CONNECT);
		connectButton.addActionListener(this);
		browseButton = new JButton("Browse");
		browseButton.setActionCommand(BROWSE_FOM);
		browseButton.addActionListener(this);
		statusLabel = new JLabel();
		
		setMinimumSize(new Dimension(400,200));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		add(new JLabel("Name: "), c);
		c.gridx++;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(federateName, c);
		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Org: "), c);
		c.gridx++;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(federateType, c);
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Federation: "), c);
		c.gridx++;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(federationName, c);
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("FOM File: "), c);
		c.gridx++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		fomPath.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				isFomValid();
			}
		});
		add(fomPath, c);
		c.gridx += 2;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		add(browseButton, c);
		c.gridy++;
		c.gridx = 0;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Host: "), c);
		c.gridx++;
		c.fill = GridBagConstraints.HORIZONTAL;
		hostAddress.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				isCrcAddressValid();
			}
		});
		add(hostAddress, c);
		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Port: "), c);
		c.gridx++;
		c.fill = GridBagConstraints.HORIZONTAL;
		portNumber.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				isCrcPortValid();
			}
		});
		add(portNumber, c);
		c.gridy++;
		c.gridx = 1;
		c.gridwidth = 3;
		add(rememberCheck, c);
		c.gridy++;
		c.gridx = 1;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(connectButton, c);
		c.gridy++;
		c.fill = GridBagConstraints.HORIZONTAL;
		statusLabel.setPreferredSize(new Dimension(200,35));
		add(statusLabel, c);
	}
	
	/**
	 * Sets the state.
	 *
	 * @param federateAmbassador the new state
	 */
	public void initialize(FederationConnection connection, SimAmbassador ambassador) {
		this.connection = connection;
		this.ambassador = ambassador;
		
		connectButton.setText(connection.isConnected()?"Disconnect":"Connect");
		if(connection.isEmpty()) {
			loadData();
		} else {
			federateName.setText(connection.getFederateName());
			federateType.setText(connection.getFederateType());
			federationName.setText(connection.getFederationName());
			fomPath.setText(connection.getFomPath());
			hostAddress.setText(connection.getHost());
			portNumber.setText(new Integer(connection.getPort()).toString());
			rememberCheck.setSelected(true);
		}
		isDataValid();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(BROWSE_FOM)) {
			JFileChooser fileChooser = new JFileChooser(
					new File(fomPath.getText()).exists()?
							fomPath.getText():System.getProperty("user.dir"));
			fileChooser.setDialogTitle("Select FOM File");
			fileChooser.setFileFilter(new FileNameExtensionFilter("FOM Files","xml"));
			if(fileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
				fomPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		} else if(e.getActionCommand().equals(CONNECT)) {
			if(rememberCheck.isSelected()) {
				saveData();
			}
			connect();
		} else if(e.getActionCommand().equals(TOGGLE_REMEMBER)) {
			if(rememberCheck.isSelected()) {
				saveData();
			} else {
				clearData();
			}
		}
	}
		
	/**
	 * Connects to the RTI.
	 */
	private void connect() {
		federateName.setEnabled(false);
		federateType.setEnabled(false);
		federationName.setEnabled(false);
		fomPath.setEnabled(false);
		browseButton.setEnabled(false);
		hostAddress.setEnabled(false);
		portNumber.setEnabled(false);
		rememberCheck.setEnabled(false);
		connectButton.setEnabled(false);
		if(!connection.isConnected()) {
			statusLabel.setText("Connecting to " + hostAddress.getText() + "...");
			statusLabel.setIcon(Icons.LOADING);
			if(!isCrcAddressValid()) {
				statusLabel.setText("Host is not valid (expected domain name or an IP address).");
			} else if(!isCrcPortValid()) {
				statusLabel.setText("Port is not valid (expected an integer).");
			} else {
				connection.setHost(hostAddress.getText());
				connection.setPort(Integer.parseInt(portNumber.getText()));
				connection.setFederationName(federationName.getText());
				connection.setFomPath(fomPath.getText());
				connection.setFederateName(federateName.getText());
				connection.setFederateType(federateType.getText());
				new SwingWorker<Void,Void>() {
					@Override
					protected Void doInBackground() {
						try {
							ambassador.connect(connection);
							ambassador.joinFederation();
						} catch (Exception ex) {
							ex.printStackTrace();
							federateName.setEnabled(true);
							federateType.setEnabled(true);
							federationName.setEnabled(true);
							fomPath.setEnabled(true);
							browseButton.setEnabled(true);
							hostAddress.setEnabled(true);
							portNumber.setEnabled(true);
							rememberCheck.setEnabled(true);
							connectButton.setEnabled(true);
							connectButton.setText("Connect");
							statusLabel.setIcon(null);
							try {
								ambassador.resignFederation();
								ambassador.disconnect();
							} catch (Exception ignored) { }
							statusLabel.setText("Failed (" + ex.getMessage() + ")");
						}
						return null;
					}
				}.execute();
			}
		} else {
			statusLabel.setText("Disconnecting...");
			statusLabel.setIcon(Icons.LOADING);
			try {
				new SwingWorker<Void,Void>() {
					@Override
					protected Void doInBackground() {
						try {
							ambassador.resignFederation();
							ambassador.disconnect();
						} catch (Exception ex) {
							ex.printStackTrace();
							statusLabel.setText("Failed (" + ex.getMessage() + ")");
						}
						return null;
					}
				}.execute();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks if the CRC address is valid.
	 *
	 * @return true, if the CRC address is valid
	 */
	private boolean isCrcAddressValid() {
		boolean validIpAddress = hostAddress.getText().matches(
				"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." 
				+ "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." 
				+ "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
				+ "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
		boolean validHostname = hostAddress.getText().matches(
				"(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*"
				+ "([A-Za-z]|[A-Za-z][A-Za-z0-9\\-]*[A-Za-z0-9])");
		if(validIpAddress || validHostname) {
			hostAddress.setForeground(Color.green);
			return true;
		} else {
			hostAddress.setForeground(Color.red);
			return false;
		}
	}
	
	/**
	 * Checks if the CRC port is valid.
	 *
	 * @return true, if the CRC port is valid
	 */
	private boolean isCrcPortValid() {
		if(portNumber.getText().matches("[0-9]+")) {
			portNumber.setForeground(Color.green);
			return true;
		} else {
			portNumber.setForeground(Color.red);
			return false;
		}
	}
	
	/**
	 * Checks if the FOM is valid.
	 *
	 * @return true, if the FOM is valid
	 */
	private boolean isFomValid() {
		if(new File(fomPath.getText()).exists()) {
			fomPath.setForeground(Color.green);
			return true;
		} else {
			fomPath.setForeground(Color.red);
			return false;
		}
	}
	
	/**
	 * Checks if the data is valid.
	 *
	 * @return true, if the data is valid
	 */
	private boolean isDataValid() {
		return isCrcAddressValid() 
				&& isCrcPortValid() 
				&& !federateName.getText().isEmpty() 
				&& !federationName.getText().isEmpty()
				&& isFomValid();
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
			rememberCheck.setSelected(false);
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		federateName.setText(properties.getProperty("name"));
		federateType.setText(properties.getProperty("type"));
		federationName.setText(properties.getProperty("federation"));
		fomPath.setText(properties.getProperty("fom"));
		hostAddress.setText(properties.getProperty("host"));
		portNumber.setText(properties.getProperty("port"));
		rememberCheck.setSelected(true);
	}
	
	/**
	 * Saves the connection data to file.
	 */
	private void saveData() {
		OutputStream output;
		Properties properties = new Properties();
		properties.setProperty("name", federateName.getText());
		properties.setProperty("type", federateType.getText());
		properties.setProperty("federation", federationName.getText());
		properties.setProperty("fom", fomPath.getText());
		properties.setProperty("host", hostAddress.getText());
		properties.setProperty("port", portNumber.getText());
		try {
			output = new FileOutputStream(new File(CONNECTION_DATA));
			properties.storeToXML(output, null);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Clears the saved connection data file.
	 */
	private void clearData() {
		new File(CONNECTION_DATA).delete();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.event.ConnectionListener#connectionEventOccurred(edu.mit.sips.gui.event.ConnectionEvent)
	 */
	@Override
	public void connectionEventOccurred(ConnectionEvent e) {
		boolean isConnected = e.getConnection().isConnected();
		federateName.setEnabled(!isConnected);
		federateType.setEnabled(!isConnected);
		federationName.setEnabled(!isConnected);
		fomPath.setEnabled(!isConnected);
		browseButton.setEnabled(!isConnected);
		hostAddress.setEnabled(!isConnected);
		portNumber.setEnabled(!isConnected);
		rememberCheck.setEnabled(!isConnected);
		connectButton.setEnabled(true);
		connectButton.setText(isConnected?"Disconnect":"Connect");
		statusLabel.setIcon(isConnected?Icons.LOADING_COMPLETE:null);
		
		if(isConnected) {
			statusLabel.setText("Connected to " + e.getConnection().getHost());
		} else {
			statusLabel.setText("");
		}
	}
}
