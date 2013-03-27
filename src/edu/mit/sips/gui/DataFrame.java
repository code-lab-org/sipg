package edu.mit.sips.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.mit.sips.core.City;
import edu.mit.sips.hla.FederationConnection;
import edu.mit.sips.io.Icons;
import edu.mit.sips.io.Serialization;
import edu.mit.sips.sim.Simulator;

/**
 * The Class DataFrame.
 */
public class DataFrame extends JFrame implements UpdateListener {
	private static final long serialVersionUID = 809065861110839895L;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					DataFrame frame = new DataFrame();
					frame.pack();
					frame.setVisible(true);
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private final JPanel contentPane;
	private final ConnectionPanel connectionPanel;
	private final ConnectionToolbar connectionToolbar;
	private Simulator simulator;
	private JSplitPane nationalPane;
	private SimulationControlPane simulationPane;
	private JTabbedPane elementsListPane;
	private SocietyPane societyPane;
	
	private final Action newScenario = new AbstractAction("New") {
		private static final long serialVersionUID = 7259597700641022096L;

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override 
		public void actionPerformed(ActionEvent e) {
			// TODO
		}
	};
	
	private final Action openScenario = new AbstractAction("Open") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
			close();
			
			// create file chooser to browse for json file
			JFileChooser fileChooser = new JFileChooser(".");
			fileChooser.setFileFilter(
					new FileNameExtensionFilter("JSON files","json"));
			if(JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
				// load experiment from file
				try {
					// create a string-builder to efficiently read in JSON data
					StringBuilder jsonBuilder = new StringBuilder();
					// create file reader and buffered reader
					FileReader fr = new FileReader(fileChooser.getSelectedFile());
					BufferedReader br = new BufferedReader(fr);
					String line;
					// do while the next line is not null (not reached end of file)
					while((line = br.readLine()) != null) {
						// append line to string builder
						jsonBuilder.append(line);
					}
					// closer readers
					br.close();
					fr.close();
					
					initialize(new Simulator(
							Serialization.deserialize(jsonBuilder.toString())));
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(contentPane.getTopLevelAncestor(), 
							ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		}
	};
	
	private final Action saveScenario = new AbstractAction("Save as...") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
			// create file chooser to browse for json file
			JFileChooser fileChooser = new JFileChooser(".");
			fileChooser.setFileFilter(
					new FileNameExtensionFilter("JSON files","json"));
			if(JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(null)) {
				try {
					// create a file writer and buffered writer
					FileWriter fw = new FileWriter(fileChooser.getSelectedFile());
					BufferedWriter bw = new BufferedWriter(fw);
					// write the JSON-ified experiment to file
					bw.write(Serialization.serialize(simulator.getCountry()));
					// flush and close writers
					bw.flush();
					bw.close();
					fw.close();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(contentPane.getTopLevelAncestor(), 
							ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		}
	};
	
	private final Action closeScenario = new AbstractAction("Close") {
		private static final long serialVersionUID = 4589751151727368209L;

		@Override
		public void actionPerformed(ActionEvent e) {
			close();
		}
	};
	
	private final Action exitAction = new AbstractAction("Exit") {
		private static final long serialVersionUID = 4589751151727368209L;

		@Override
		public void actionPerformed(ActionEvent e) {
			exit();
		}
	};
	
	private final Action editConnection = new AbstractAction("Edit Connection") {
		private static final long serialVersionUID = 4589751151727368209L;

		@Override
		public void actionPerformed(ActionEvent e) {
			showConnectionDialog();
		}
	};
	
	/**
	 * Instantiates a new data frame.
	 */
	public DataFrame() {
		super("Data Viewer");
		setIconImage(Icons.SYSTEM_MONITOR);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem newItem = new JMenuItem(newScenario);
		newItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		newItem.setMnemonic(KeyEvent.VK_N);
		fileMenu.add(newItem);
		JMenuItem openItem = new JMenuItem(openScenario);
		openItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		openItem.setMnemonic(KeyEvent.VK_O);
		fileMenu.add(openItem);
		fileMenu.add(new JSeparator());
		JMenuItem closeItem = new JMenuItem(closeScenario);
		closeItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
		closeItem.setMnemonic(KeyEvent.VK_C);
		fileMenu.add(closeItem);
		fileMenu.add(new JSeparator());
		JMenuItem saveItem = new JMenuItem(saveScenario);
		saveItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		saveItem.setMnemonic(KeyEvent.VK_S);
		fileMenu.add(saveItem);
		fileMenu.add(new JSeparator());
		JMenuItem exitItem = new JMenuItem(exitAction);
		exitItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
		exitItem.setMnemonic(KeyEvent.VK_X);
		fileMenu.add(exitItem);
		JMenu editMenu = new JMenu("Edit");
		JMenuItem connectionItem = new JMenuItem(editConnection);
		editMenu.add(connectionItem);
		menuBar.add(editMenu);
		setJMenuBar(menuBar);

		connectionToolbar = new ConnectionToolbar();
		connectionPanel = new ConnectionPanel();
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.gray);
		setContentPane(contentPane);
		setPreferredSize(new Dimension(1000,600));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		
		initialize(null);
	}
	
	/**
	 * Close.
	 */
	private void close() {
		if(simulator != null && JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
				getContentPane(), "Close scenario?", "Confirm", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)) {
			initialize(null);
		}
	}
	
	/**
	 * Exit.
	 */
	private void exit() {
		close();
		if(simulator == null) {
			dispose();
		}
	}

	/**
	 * Sets the simulator.
	 *
	 * @param simulator the new simulator
	 */
	public void initialize(final Simulator simulator) {
		if(simulator == null) {
			if(this.simulator != null) {
				if(this.simulator.getConnection() != null) {
					if(this.simulator.getConnection().isConnected()) {
						try {
							this.simulator.getAmbassador().disconnect();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					this.simulator.getConnection().removeConnectionListener(connectionPanel);
					this.simulator.getConnection().removeConnectionListener(connectionToolbar);
					this.simulator.setConnection(null);
				}
				this.simulator.removeUpdateListener(this);
				this.simulator.removeUpdateListener(simulationPane);
				this.simulator.removeUpdateListener(societyPane);
				this.simulator = null;
			}
			if(contentPane.getComponentCount() > 0) {
				contentPane.removeAll();
			}
			societyPane = null;
			elementsListPane = null;
			nationalPane = null;
			simulationPane = null;
			validate();
			repaint();
			setTitle("Data Viewer");
		} else {
			this.simulator = simulator;
			this.simulator.addUpdateListener(this);
			simulator.setConnection(new FederationConnection());
			connectionPanel.initialize(simulator.getConnection(), simulator.getAmbassador());
			simulator.getConnection().addConnectionListener(connectionPanel);
			simulator.getConnection().addConnectionListener(connectionToolbar);
			
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			societyPane = new SocietyPane(this.simulator.getCountry());
			societyPane.initialize();
			this.simulator.addUpdateListener(societyPane);
			elementsListPane = new JTabbedPane();
			for(City city : this.simulator.getCountry().getCities()) {
				ElementsPane elementsPane = new ElementsPane(city);
				elementsPane.initialize();
				elementsListPane.addTab(city.getName(), elementsPane);
			}
			elementsListPane.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if(elementsListPane.getSelectedComponent() instanceof ElementsPane) {
						((ElementsPane)elementsListPane.getSelectedComponent()).initialize();
					}
				}
			});
			simulationPane = new SimulationControlPane(this.simulator);
			this.simulator.addUpdateListener(simulationPane);
			nationalPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(new BorderLayout());
			leftPanel.add(simulationPane, BorderLayout.NORTH);
			leftPanel.add(elementsListPane, BorderLayout.CENTER);
			nationalPane.setLeftComponent(leftPanel);
			nationalPane.setRightComponent(societyPane);
			nationalPane.setResizeWeight(0);
			contentPane.add(nationalPane, BorderLayout.CENTER);
			contentPane.add(connectionToolbar, BorderLayout.SOUTH);

			validate();
			repaint();
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		newScenario.setEnabled(simulator == null);
		saveScenario.setEnabled(simulator != null);
		closeScenario.setEnabled(simulator != null);
		editConnection.setEnabled(simulator != null);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationInitialized(final UpdateEvent event) {
		// Note: must use SwingUtilities.invokeAndWait method here because
		// the UpdateEvent passes the "active" Country instance. If we don't
		// wait for the GUI to update, the simulation will race ahead, causing
		// it to display data for future time periods!
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					int year = (int) event.getTime();
					
					setTitle("Data as of " + year);

					societyPane.initialize();
					societyPane.updateDatasets(year);
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(final UpdateEvent event) {
		// Note: must use SwingUtilities.invokeAndWait method here because
		// the UpdateEvent passes the "active" Country instance. If we don't
		// wait for the GUI to update, the simulation will race ahead, causing
		// it to display data for future time periods!
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					int year = (int) event.getTime();
					
					setTitle("Data as of " + year);
					
					societyPane.updateDatasets(year);
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Show connection dialog.
	 */
	private void showConnectionDialog() {
		JOptionPane.showMessageDialog(this, connectionPanel,
				"Edit Connection", JOptionPane.PLAIN_MESSAGE);
	}
}
