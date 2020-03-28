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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.mit.sips.core.City;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.LocalAgricultureSystem;
import edu.mit.sips.core.agriculture.RecordedAgricultureSystem;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.electricity.LocalElectricitySystem;
import edu.mit.sips.core.electricity.RecordedElectricitySystem;
import edu.mit.sips.core.petroleum.LocalPetroleumSystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.petroleum.RecordedPetroleumSystem;
import edu.mit.sips.core.water.LocalWaterSystem;
import edu.mit.sips.core.water.RecordedWaterSystem;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.io.Icons;
import edu.mit.sips.io.Serialization;
import edu.mit.sips.sim.Simulator;
import edu.mit.sips.sim.hla.HlaSimulator;

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
	private ElementsPane elementsPane;
	private SocietyPane societyPane;
	private final JFileChooser fileChooser, dataChooser;

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
	private final Action exportAgriculture = new AbstractAction("Write Data") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
			exportAgricultureData();
		}
	};
	private final Action importAgriculture = new AbstractAction("Read Data") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
			importAgricultureData();
		}
	};
	private final Action clearAgriculture = new AbstractAction("Clear Data") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
			clearAgricultureData();
		}
	};
	private final Action exportWater = new AbstractAction("Write Data") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
			exportWaterData();
		}
	};
	private final Action importWater = new AbstractAction("Read Data") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
			importWaterData();
		}
	};
	private final Action clearWater = new AbstractAction("Clear Data") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
			clearWaterData();
		}
	};
	private final Action exportEnergy = new AbstractAction("Write Data") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
			exportEnergyData();
		}
	};
	private final Action importEnergy = new AbstractAction("Read Data") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
			importEnergyData();
		}
	};
	private final Action clearEnergy = new AbstractAction("Clear Data") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
			clearEnergyData();
		}
	};

	private final Action openScenario = new AbstractAction("Open") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
			close();

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
					// close readers
					br.close();
					fr.close();
					HlaSimulator sim = new HlaSimulator(Serialization.deserialize(jsonBuilder.toString()));
					initialize(sim);
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
			if(JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(null)) {
				save(fileChooser.getSelectedFile());
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

		File userOutputDir = new File(System.getProperty("user.home"), "sips-g");
		if(!userOutputDir.exists()) {
			userOutputDir.mkdir();
		}
		fileChooser = new JFileChooser(userOutputDir);
		fileChooser.setFileFilter(
				new FileNameExtensionFilter("JSON files","json"));

		dataChooser = new JFileChooser(System.getProperty("user.home"));
		dataChooser.setFileFilter(new FileNameExtensionFilter("Data files", "dat"));

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
		JMenu agricultureDataMenu = new JMenu("Agriculture");
		agricultureDataMenu.add(new JMenuItem(exportAgriculture));
		agricultureDataMenu.add(new JMenuItem(importAgriculture));
		agricultureDataMenu.add(new JMenuItem(clearAgriculture));
		menuBar.add(agricultureDataMenu);
		JMenu waterDataMenu = new JMenu("Water");
		waterDataMenu.add(new JMenuItem(exportWater));
		waterDataMenu.add(new JMenuItem(importWater));
		waterDataMenu.add(new JMenuItem(clearWater));
		menuBar.add(waterDataMenu);
		JMenu energyDataMenu = new JMenu("Energy");
		energyDataMenu.add(new JMenuItem(exportEnergy));
		energyDataMenu.add(new JMenuItem(importEnergy));
		energyDataMenu.add(new JMenuItem(clearEnergy));
		menuBar.add(energyDataMenu);
		setJMenuBar(menuBar);

		connectionToolbar = new ConnectionToolbar();
		connectionPanel = new ConnectionPanel();
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.gray);
		setContentPane(contentPane);
		setPreferredSize(new Dimension(1280,720));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		initialize(null);
	}

	protected void autoSave() {
		File file = new File("autosave.json");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("auto-saving: " + file);
		save(file);
	}
	private void clearAgricultureData() {
		for(City city : simulator.getScenario().getCountry().getCities()) {
			if(city.getAgricultureSystem() instanceof RecordedAgricultureSystem) {
				city.setAgricultureSystem(new RecordedAgricultureSystem());
			}
		}
		JOptionPane.showMessageDialog(this, 
				"Cleared agriculture data.", null, 
				JOptionPane.INFORMATION_MESSAGE);
	}
	private void clearEnergyData() {
		for(City city : simulator.getScenario().getCountry().getCities()) {
			if(city.getPetroleumSystem() instanceof RecordedPetroleumSystem) {
				city.setPetroleumSystem(new RecordedPetroleumSystem());
			}
			if(city.getElectricitySystem() instanceof RecordedElectricitySystem) {
				city.setElectricitySystem(new RecordedElectricitySystem());
			}
		}
		JOptionPane.showMessageDialog(this, 
				"Cleared energy data.", null, 
				JOptionPane.INFORMATION_MESSAGE);
	}
	private void clearWaterData() {
		for(City city : simulator.getScenario().getCountry().getCities()) {
			if(city.getWaterSystem() instanceof RecordedWaterSystem) {
				city.setWaterSystem(new RecordedWaterSystem());
			}
		}
		JOptionPane.showMessageDialog(this, 
				"Cleared water data.", null, 
				JOptionPane.INFORMATION_MESSAGE);
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

	private void exportAgricultureData() {
		Map<String, RecordedAgricultureSystem> data = 
				new HashMap<String, RecordedAgricultureSystem>();
		for(City city : simulator.getScenario().getCountry().getCities()) {
			if(city.getAgricultureSystem() instanceof LocalAgricultureSystem) {
				LocalAgricultureSystem system = (LocalAgricultureSystem) city.getAgricultureSystem();
				RecordedAgricultureSystem placeholder = new RecordedAgricultureSystem();
				placeholder.setRecordedWaterConsumption(system.getWaterConsumptionLog());
				placeholder.setRecordedCapitalExpense(system.getCapitalExpenseLog());
				placeholder.setRecordedCashFlow(system.getCashFlowLog());
				placeholder.setRecordedFoodProduction(system.getFoodProductionLog());
				placeholder.setRecordedTotalFoodSupply(system.getTotalFoodSupplyLog());
				placeholder.setRecordedFoodDomesticPrice(system.getFoodDomesticPriceLog());
				placeholder.setRecordedFoodImportPrice(system.getFoodImportPriceLog());
				placeholder.setRecordedFoodExportPrice(system.getFoodExportPriceLog());
				data.put(city.getName(), placeholder);
			}
		}
		if(exportData(data)) {
			JOptionPane.showMessageDialog(this, 
					"Saved agriculture data.", null, 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private boolean exportData(Object dataPack) {
		if(JFileChooser.APPROVE_OPTION == dataChooser.showSaveDialog(this)) {
			File f = dataChooser.getSelectedFile();
			String filePath = f.getPath();
			if(!filePath.toLowerCase().endsWith(".dat")) {
				f = new File(filePath + ".dat");
			}
			if(f.exists() && JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(
					this, "File " + f.getName() + " exists. Overwrite?", 
					null, JOptionPane.OK_CANCEL_OPTION)) {

				return false;
			}

			try {
				FileOutputStream fileOut = new FileOutputStream(f);

				ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
				outStream.writeObject(dataPack);
				outStream.close();
				fileOut.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, 
						"Error while writing data.", null, 
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return false;
			}

			File userOutputDir = new File("logs");
			if(!userOutputDir.exists()) {
				userOutputDir.mkdir();
			}
			File userState = new File(userOutputDir, 
					System.getProperty("user.name") + "-export-" + 
							new Date().getTime() + ".json");
			try {
				userState.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			save(userState);
			
			return true;
		}
		return false;
	}

	private void exportEnergyData() {
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		for(City city : simulator.getScenario().getCountry().getCities()) {
			data.put(city.getName(), new Object[2]);
			if(city.getPetroleumSystem() instanceof LocalPetroleumSystem) {
				LocalPetroleumSystem system = (LocalPetroleumSystem) city.getPetroleumSystem();
				RecordedPetroleumSystem placeholder = new RecordedPetroleumSystem();
				placeholder.setRecordedElectricityConsumption(system.getElectricityConsumptionMap());
				placeholder.setRecordedCapitalExpense(system.getCapitalExpenseLog());
				placeholder.setRecordedCashFlow(system.getCashFlowLog());
				placeholder.setRecordedPetroleumReservoirVolume(system.getPetroleumReservoirVolumeLog());
				placeholder.setRecordedPetroleumWithdrawals(system.getPetroleumWithdrawalsLog());
				placeholder.setRecordedPetroleumDomesticPrice(system.getPetroleumDomesticPriceLog());
				placeholder.setRecordedPetroleumImportPrice(system.getPetroleumImportPriceLog());
				placeholder.setRecordedPetroleumExportPrice(system.getPetroleumExportPriceLog());
				data.get(city.getName())[0] = placeholder;
			}
			if(city.getElectricitySystem() instanceof LocalElectricitySystem) {
				LocalElectricitySystem system = (LocalElectricitySystem) city.getElectricitySystem();
				RecordedElectricitySystem placeholder = new RecordedElectricitySystem();
				placeholder.setRecordedPetroleumConsumption(system.getPetroleumConsumptionLog());
				placeholder.setRecordedWaterConsumption(system.getWaterConsumptionLog());
				placeholder.setRecordedCapitalExpense(system.getCapitalExpenseLog());
				placeholder.setRecordedCashFlow(system.getCashFlowLog());
				placeholder.setRecordedElectricityDomesticPrice(system.getElectricityDomesticPriceLog());
				data.get(city.getName())[1] = placeholder;
			}
		}
		if(exportData(data)) {
			JOptionPane.showMessageDialog(this, 
					"Saved energy data.", null, 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void exportWaterData() {
		Map<String, RecordedWaterSystem> data = 
				new HashMap<String, RecordedWaterSystem>();
		for(City city : simulator.getScenario().getCountry().getCities()) {
			if(city.getWaterSystem() instanceof LocalWaterSystem) {
				LocalWaterSystem system = (LocalWaterSystem) city.getWaterSystem();
				RecordedWaterSystem placeholder = new RecordedWaterSystem();
				placeholder.setRecordedElectricityConsumption(system.getElectricityConsumptionLog());
				placeholder.setRecordedCapitalExpense(system.getCapitalExpenseLog());
				placeholder.setRecordedCashFlow(system.getCashFlowLog());
				placeholder.setRecordedWaterReservoirVolume(system.getWaterReservoirVolumeMap());
				placeholder.setRecordedReservoirWithdrawals(system.getReservoirWithdrawalsLog());
				placeholder.setRecordedWaterDomesticPrice(system.getWaterDomesticPriceLog());
				placeholder.setRecordedWaterImportPrice(system.getWaterImportPriceLog());
				data.put(city.getName(), placeholder);
			}
		}
		if(exportData(data)) {
			JOptionPane.showMessageDialog(this, 
					"Saved water data.", null, 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private void importAgricultureData() {
		Map<?,?> map = importData();
		if(map != null) {
			for(City city : simulator.getScenario().getCountry().getCities()) {
				if(map.containsKey(city.getName()) 
						&& city.getAgricultureSystem() instanceof RecordedAgricultureSystem
						&& map.get(city.getName()) instanceof RecordedAgricultureSystem) {
					city.setAgricultureSystem(
							(RecordedAgricultureSystem) map.get(city.getName()));
				} else {
					JOptionPane.showMessageDialog(this, 
							"File is not agriculture data.", null, 
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			JOptionPane.showMessageDialog(this, 
					"Loaded agriculture data.", null, 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private Map<?,?> importData() {
		if(JFileChooser.APPROVE_OPTION == dataChooser.showOpenDialog(this)) {
			Object o = null;
			try {
				FileInputStream fileIn = new FileInputStream(dataChooser.getSelectedFile());
				ObjectInputStream inStream = new ObjectInputStream(fileIn);
				o = inStream.readObject();
				inStream.close();
				fileIn.close();
			} catch (IOException | ClassNotFoundException e) {
				JOptionPane.showMessageDialog(this, 
						"Error while reading data.", null, 
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return null;
			}

			if(o instanceof Map<?, ?>) {
				return (Map<?, ?>) o;
			} else {
				JOptionPane.showMessageDialog(this, 
						"File is not compatible data.", null, 
						JOptionPane.ERROR_MESSAGE);
				return null;
			}
		} else {
			return null;
		}
	}

	private void importEnergyData() {
		Map<?,?> map = importData();
		if(map != null) {
			for(City city : simulator.getScenario().getCountry().getCities()) {
				if(map.containsKey(city.getName()) 
						&& map.get(city.getName()) instanceof Object[]
								&& ((Object[])map.get(city.getName())).length == 2) {
					Object[] objects = (Object[])map.get(city.getName());
					if(city.getPetroleumSystem() instanceof RecordedPetroleumSystem
							&& objects[0] instanceof RecordedPetroleumSystem) {
						city.setPetroleumSystem(
								(RecordedPetroleumSystem) objects[0]);
					} else {
						JOptionPane.showMessageDialog(this, 
								"File is not petroleum data.", null, 
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(city.getElectricitySystem() instanceof RecordedElectricitySystem
							&& objects[1] instanceof RecordedElectricitySystem) {
						city.setElectricitySystem(
								(RecordedElectricitySystem) objects[1]);
					} else {
						JOptionPane.showMessageDialog(this, 
								"File is not electricity data.", null, 
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				} else {
					JOptionPane.showMessageDialog(this, 
							"File is not energy data.", null, 
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			JOptionPane.showMessageDialog(this, 
					"Loaded energy data.", null, 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void importWaterData() {
		Map<?,?> map = importData();
		if(map != null) {
			for(City city : simulator.getScenario().getCountry().getCities()) {
				if(map.containsKey(city.getName()) 
						&& city.getWaterSystem() instanceof RecordedWaterSystem
						&& map.get(city.getName()) instanceof RecordedWaterSystem) {
					city.setWaterSystem(
							(RecordedWaterSystem) map.get(city.getName()));
				} else {
					JOptionPane.showMessageDialog(this, 
							"File is not water data.", null, 
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			JOptionPane.showMessageDialog(this, 
					"Loaded water data.", null, 
					JOptionPane.INFORMATION_MESSAGE);
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
				if(this.simulator.getConnection().isConnected()) {
					this.simulator.disconnect();
				}
				this.simulator.getConnection().removeConnectionListener(connectionPanel);
				this.simulator.getConnection().removeConnectionListener(connectionToolbar);
				this.simulator.removeUpdateListener(this);
				this.simulator.removeUpdateListener(simulationPane);
				this.simulator.removeUpdateListener(societyPane);
				this.simulator = null;
			}
			if(contentPane.getComponentCount() > 0) {
				contentPane.removeAll();
			}
			societyPane = null;
			elementsPane = null;
			nationalPane = null;
			simulationPane = null;
			validate();
			repaint();
			setTitle("Data Viewer");
		} else {
			this.simulator = simulator;
			this.simulator.addUpdateListener(this);
			connectionPanel.initialize(simulator);
			simulator.getConnection().addConnectionListener(connectionPanel);
			simulator.getConnection().addConnectionListener(connectionToolbar);

			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			societyPane = new SocietyPane(this.simulator.getScenario());
			this.simulator.addUpdateListener(societyPane);
			elementsPane = new ElementsPane(simulator);
			elementsPane.initialize();
			this.simulationPane = new SimulationControlPane(this, this.simulator);
			this.simulator.addUpdateListener(simulationPane);
			nationalPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(new BorderLayout());
			leftPanel.add(simulationPane, BorderLayout.NORTH);
			leftPanel.add(elementsPane, BorderLayout.CENTER);
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
		editConnection.setEnabled(simulator != null);// && false); // TODO

		exportAgriculture.setEnabled(simulator != null 
				&& simulator.getScenario().getCountry().getAgricultureSystem() 
				instanceof AgricultureSystem.Local);	
		importAgriculture.setEnabled(simulator != null 
				&& ! (simulator.getScenario().getCountry().getAgricultureSystem() 
						instanceof AgricultureSystem.Local));
		clearAgriculture.setEnabled(simulator != null 
				&& ! (simulator.getScenario().getCountry().getAgricultureSystem() 
						instanceof AgricultureSystem.Local));	

		exportWater.setEnabled(simulator != null 
				&& simulator.getScenario().getCountry().getWaterSystem() 
				instanceof WaterSystem.Local);	
		importWater.setEnabled(simulator != null 
				&& ! (simulator.getScenario().getCountry().getWaterSystem() 
						instanceof WaterSystem.Local));
		clearWater.setEnabled(simulator != null 
				&& ! (simulator.getScenario().getCountry().getWaterSystem() 
						instanceof WaterSystem.Local));	

		exportEnergy.setEnabled(simulator != null 
				&& simulator.getScenario().getCountry().getPetroleumSystem() 
				instanceof PetroleumSystem.Local
				&& simulator.getScenario().getCountry().getElectricitySystem() 
				instanceof ElectricitySystem.Local);	
		importEnergy.setEnabled(simulator != null 
				&& ! (simulator.getScenario().getCountry().getPetroleumSystem() 
						instanceof PetroleumSystem.Local
						&& simulator.getScenario().getCountry().getElectricitySystem() 
						instanceof ElectricitySystem.Local));
		clearEnergy.setEnabled(simulator != null 
				&& ! (simulator.getScenario().getCountry().getPetroleumSystem() 
						instanceof PetroleumSystem.Local
						&& simulator.getScenario().getCountry().getElectricitySystem() 
						instanceof ElectricitySystem.Local));	
	}

	private void save(File file) {
		try {
			// create a file writer and buffered writer
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			// write the JSON-ified experiment to file
			bw.write(Serialization.serialize(simulator.getScenario()));
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

	/**
	 * Show connection dialog.
	 */
	private void showConnectionDialog() {
		connectionPanel.updateFields();
		JOptionPane.showMessageDialog(this, connectionPanel,
				"Edit Connection", JOptionPane.PLAIN_MESSAGE);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationCompleted(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationCompleted(UpdateEvent event) {
		File logsDir = new File("logs");
		if(!logsDir.exists()) {
			logsDir.mkdir();
		}
		File finalState = new File(logsDir, System.getProperty("user.name") 
				+ "-" + new Date().getTime() + ".json");
		try {
			finalState.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		save(finalState);

		File userOutputDir = new File(System.getProperty("user.home"), "sips-g");
		if(!userOutputDir.exists()) {
			userOutputDir.mkdir();
		}
		File userFinalState = new File(userOutputDir, 
				new Date().getTime() + "-scenario.json");
		try {
			userFinalState.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		save(userFinalState);
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
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
