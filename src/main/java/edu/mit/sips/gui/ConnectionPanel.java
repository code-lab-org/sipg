package edu.mit.sips.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.mit.sips.gui.event.ConnectionEvent;
import edu.mit.sips.gui.event.ConnectionListener;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.Simulator;
import edu.mit.sips.sim.hla.HlaConnection;

/**
 * The Class ConnectionPanel. A panel allowing connections to be specified.
 * 
 * @author Paul T Grogan, ptgrogan@mit.edu
 */
public final class ConnectionPanel extends JPanel 
implements ActionListener, ConnectionListener {
	private static final long serialVersionUID = 8697615119488025958L;

	private static final String CONNECTION_DATA = "connection.data";
	private static final String BROWSE_FOM = "browseFOM", 
			TOGGLE_REMEMBER = "toggleReminder",
			CONNECT = "connect";
	
	private final JTextField federateName, federateType, federationName, fomPath;
	private final JCheckBox rememberCheck;
	private final JButton connectButton, browseButton;
	private final JLabel statusLabel;

	private Simulator simulator;
	
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
			public void ancestorMoved(AncestorEvent event) { }

			@Override
			public void ancestorRemoved(AncestorEvent event) { }
		});
		federateName.setToolTipText("Your user name.");
		federateType = new JTextField(12);
		federateName.setToolTipText("Your organization name.");
		federationName = new JTextField(12);
		federationName.setToolTipText("The federation to join.");
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
		fomPath.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				isFomValid();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				isFomValid();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
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
	 * Clears the saved connection data file.
	 */
	private void clearData() {
		new File(CONNECTION_DATA).delete();
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
		rememberCheck.setEnabled(false);
		connectButton.setEnabled(false);
		if(!simulator.getConnection().isConnected()) {
			statusLabel.setText("Connecting to RTI...");
			statusLabel.setIcon(Icons.LOADING);

			simulator.getConnection().setFederationName(federationName.getText());
			if(simulator.getConnection() instanceof HlaConnection) {
				((HlaConnection)simulator.getConnection()).setFomPath(fomPath.getText());
				((HlaConnection)simulator.getConnection()).setFederateType(federateType.getText());
			}
			simulator.getConnection().setFederateName(federateName.getText());
			new SwingWorker<Void,Void>() {
				@Override
				protected Void doInBackground() {
					try {
						simulator.connect();
					} catch (Exception ex) {
						ex.printStackTrace();
						federateName.setEnabled(true);
						federateType.setEnabled(true);
						federationName.setEnabled(true);
						fomPath.setEnabled(true);
						browseButton.setEnabled(true);
						rememberCheck.setEnabled(true);
						connectButton.setEnabled(true);
						connectButton.setText("Connect");
						statusLabel.setIcon(null);
						simulator.disconnect();
						statusLabel.setText("Failed (" + ex.getMessage() + ")");
					}
					return null;
				}
			}.execute();
		} else {
			statusLabel.setText("Disconnecting...");
			statusLabel.setIcon(Icons.LOADING);
			try {
				new SwingWorker<Void,Void>() {
					@Override
					protected Void doInBackground() {
						simulator.disconnect();
						return null;
					}
				}.execute();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
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
		rememberCheck.setEnabled(!isConnected);
		connectButton.setEnabled(true);
		connectButton.setText(isConnected?"Disconnect":"Connect");
		statusLabel.setIcon(isConnected?Icons.LOADING_COMPLETE:null);
		
		if(isConnected) {
			statusLabel.setText("Connected to RTI.");
		} else {
			statusLabel.setText("");
		}
	}
	
	/**
	 * Sets the state.
	 *
	 * @param federateAmbassador the new state
	 */
	public void initialize(Simulator simulator) {
		this.simulator = simulator;
		
		loadData();
		updateFields();
		isDataValid();
	}
	
	/**
	 * Update fields.
	 */
	public void updateFields() {
		connectButton.setText(simulator.getConnection().isConnected()?"Disconnect":"Connect");
		federateName.setText(simulator.getConnection().getFederateName());
		federationName.setText(simulator.getConnection().getFederationName());
		if(simulator.getConnection() instanceof HlaConnection) {
			federateType.setText(((HlaConnection)simulator.getConnection()).getFederateType());
			fomPath.setText(((HlaConnection)simulator.getConnection()).getFomPath());
		}
		rememberCheck.setSelected(isDataSaved());
	}

	/**
	 * Checks if is data saved.
	 *
	 * @return true, if is data saved
	 */
	private boolean isDataSaved() {
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
	 * Checks if the data is valid.
	 *
	 * @return true, if the data is valid
	 */
	private boolean isDataValid() {
		return !federateName.getText().isEmpty() 
				&& !federationName.getText().isEmpty()
				&& isFomValid();
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
		simulator.getConnection().setFederateName(properties.getProperty("name"));
		simulator.getConnection().setFederationName(properties.getProperty("federation"));
		if(simulator.getConnection() instanceof HlaConnection) {
			((HlaConnection)simulator.getConnection()).setFederateType(properties.getProperty("type"));
			((HlaConnection)simulator.getConnection()).setFomPath(properties.getProperty("fom"));
		}
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
		try {
			output = new FileOutputStream(new File(CONNECTION_DATA));
			properties.storeToXML(output, null);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
