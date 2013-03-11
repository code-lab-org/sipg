package edu.mit.sips.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.mit.sips.core.City;
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
	private Simulator simulator;
	private JSplitPane nationalPane;
	private SimulationControlPane simulationPane;
	private JTabbedPane elementsListPane;
	private SocietyPane societyPane;
	
	private final Action newFile = new AbstractAction() {
		private static final long serialVersionUID = 7259597700641022096L;

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override 
		public void actionPerformed(ActionEvent e) {
			// TODO
		}
	};
	
	private final Action openFile = new AbstractAction() {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
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
					
					setSimulator(new Simulator(Serialization.deserialize(jsonBuilder.toString())));
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(contentPane.getTopLevelAncestor(), 
							ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		}
	};
	
	private final Action saveFile = new AbstractAction() {
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
	
	private final Action closeSim = new AbstractAction() {
		private static final long serialVersionUID = 4589751151727368209L;

		@Override
		public void actionPerformed(ActionEvent e) {
			setSimulator(null);
		}
	};
	
	/**
	 * Instantiates a new data frame.
	 */
	public DataFrame() {
		super("Data Viewer");
		setIconImage(Icons.SYSTEM_MONITOR);
		
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.gray);
		contentPane.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), "new");
		contentPane.getActionMap().put("new", newFile);
		contentPane.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), "save");
		contentPane.getActionMap().put("save", saveFile);
		contentPane.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK), "open");
		contentPane.getActionMap().put("open", openFile);
		contentPane.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK), "closeSim");
		contentPane.getActionMap().put("closeSim", closeSim);
		
		setContentPane(contentPane);
		setPreferredSize(new Dimension(1000,600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		newFile.setEnabled(simulator == null);
		openFile.setEnabled(simulator == null);
		saveFile.setEnabled(simulator != null);
		closeSim.setEnabled(simulator != null);
	}


	/**
	 * Sets the simulator.
	 *
	 * @param simulator the new simulator
	 */
	public void setSimulator(final Simulator simulator) {
		if(simulator == null) {
			this.simulator.removeUpdateListener(this);
			this.simulator.removeUpdateListener(simulationPane);
			this.simulator.removeUpdateListener(societyPane);
			contentPane.remove(nationalPane);
			this.simulator = null;
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
			validate();
			repaint();
		}

		newFile.setEnabled(simulator == null);
		openFile.setEnabled(simulator == null);
		saveFile.setEnabled(simulator != null);
		closeSim.setEnabled(simulator != null);
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
}
