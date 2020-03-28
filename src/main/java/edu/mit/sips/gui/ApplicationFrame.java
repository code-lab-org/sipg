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
package edu.mit.sips.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
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

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import edu.mit.sips.core.City;
import edu.mit.sips.core.agriculture.LocalAgricultureSystem;
import edu.mit.sips.core.agriculture.RecordedAgricultureSystem;
import edu.mit.sips.core.electricity.LocalElectricitySystem;
import edu.mit.sips.core.electricity.RecordedElectricitySystem;
import edu.mit.sips.core.petroleum.LocalPetroleumSystem;
import edu.mit.sips.core.petroleum.RecordedPetroleumSystem;
import edu.mit.sips.core.water.LocalWaterSystem;
import edu.mit.sips.core.water.RecordedWaterSystem;
import edu.mit.sips.gui.base.ElementsPane;
import edu.mit.sips.io.Icons;
import edu.mit.sips.io.Serialization;
import edu.mit.sips.log.ScoreFileLogger;
import edu.mit.sips.scenario.DefaultScenario;
import edu.mit.sips.scenario.Scenario;
import edu.mit.sips.sim.Simulator;

/**
 * The main frame containing the application.
 * 
 * @author Paul T. Grogan
 */
public class ApplicationFrame extends JFrame implements UpdateListener {
	private static final long serialVersionUID = 809065861110839895L;
	private static Logger logger = Logger.getLogger(ApplicationFrame.class);

	private File userOutputDir;
	private File logOutputDir;
	private final JPanel contentPane;
	private final ConnectionPanel connectionPanel;
	private final ConnectionToolbar connectionToolbar;
	private Simulator simulator;
	private JSplitPane nationalPane;
	private SimulationControlPanel simulationPane;
	private ElementsPane elementsPane;
	private SocietyPane societyPane;
	private ScoreFileLogger scoreLogger;
	private final JFileChooser scenarioFileChooser;
	private final JFileChooser recordedDataChooser;
	
	private final Action newScenario = new AbstractAction("New") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override 
		public void actionPerformed(ActionEvent e) {
			initialize(new DefaultScenario());
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

			if(JFileChooser.APPROVE_OPTION == scenarioFileChooser.showOpenDialog(null)) {
				try {
					initialize(Serialization.deserialize(
							FileUtils.readFileToString(
									scenarioFileChooser.getSelectedFile(), 
									StandardCharsets.UTF_8)));
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(contentPane.getTopLevelAncestor(), 
							ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					logger.error(ex);
					ex.printStackTrace();
				}
			}
		}
	};
	private final Action saveScenario = new AbstractAction("Save as...") {
		private static final long serialVersionUID = 7259597700641022096L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(JFileChooser.APPROVE_OPTION == scenarioFileChooser.showSaveDialog(null)) {
				save(scenarioFileChooser.getSelectedFile());
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
	 * Instantiates a new application frame.
	 *
	 * @param simulator the simulator
	 */
	public ApplicationFrame(Simulator simulator) {
		super("SIPG");
		this.simulator = simulator;
		setIconImage(Icons.SYSTEM_MONITOR);

		if(System.getenv().containsKey("SIPG_HOME")) {
			userOutputDir = new File(System.getenv("SIPG_HOME"));
		} else {
			userOutputDir = new File(System.getProperty("user.home"), "SIPG");
		}
		if(!userOutputDir.exists()) {
			userOutputDir.mkdir();
		}
		logOutputDir = new File(userOutputDir, "logs");
		if(!logOutputDir.exists()) {
			logOutputDir.mkdir();
		}
		
		scenarioFileChooser = new JFileChooser(userOutputDir);
		scenarioFileChooser.setFileFilter(
				new FileNameExtensionFilter("JSON files","json"));

		recordedDataChooser = new JFileChooser(System.getProperty("user.home"));
		recordedDataChooser.setFileFilter(new FileNameExtensionFilter("Data files", "dat"));

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

		initialize(simulator.getScenario());
	}

	/**
	 * Auto save.
	 */
	private void autoSave() {
		File file = new File(userOutputDir, "autosave.json");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("auto-saving: " + file);
		save(file);
	}
	
	/**
	 * Clear agriculture data.
	 */
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
	
	/**
	 * Clear energy data.
	 */
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
	
	/**
	 * Clear water data.
	 */
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
	 * Close this application.
	 */
	private void close() {
		if(simulator.getScenario() != null && JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
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
		if(simulator.getScenario() == null) {
			dispose();
		}
	}

	/**
	 * Export agriculture data.
	 */
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

	/**
	 * Export data.
	 *
	 * @param dataPack the data pack
	 * @return true, if successful
	 */
	private boolean exportData(Object dataPack) {
		if(JFileChooser.APPROVE_OPTION == recordedDataChooser.showSaveDialog(this)) {
			File f = recordedDataChooser.getSelectedFile();
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
				logger.error(e);
				return false;
			}

			File userState = new File(logOutputDir, 
					System.getProperty("user.name") + "-export-" + 
							new Date().getTime() + ".json");
			try {
				userState.createNewFile();
			} catch (IOException e) {
				logger.error(e);
				return false;
			}
			save(userState);
			
			return true;
		}
		return false;
	}

	/**
	 * Export energy data.
	 */
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

	/**
	 * Export water data.
	 */
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
	
	/**
	 * Import agriculture data.
	 */
	private void importAgricultureData() {
		Map<?,?> data = importData();
		if(data != null) {
			for(City city : simulator.getScenario().getCountry().getCities()) {
				if(data.containsKey(city.getName()) 
						&& city.getAgricultureSystem() instanceof RecordedAgricultureSystem
						&& data.get(city.getName()) instanceof RecordedAgricultureSystem) {
					city.setAgricultureSystem(
							(RecordedAgricultureSystem) data.get(city.getName()));
				} else {
					JOptionPane.showMessageDialog(this, 
							"File does not contain agriculture system data.", null, 
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			JOptionPane.showMessageDialog(this, 
					"Loaded agriculture data.", null, 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Import data.
	 *
	 * @return the map
	 */
	private Map<?,?> importData() {
		if(JFileChooser.APPROVE_OPTION == recordedDataChooser.showOpenDialog(this)) {
			Object obj = null;
			try {
				FileInputStream fileIn = new FileInputStream(recordedDataChooser.getSelectedFile());
				ObjectInputStream inStream = new ObjectInputStream(fileIn);
				obj = inStream.readObject();
				inStream.close();
				fileIn.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, 
						"Error while reading data.", null, 
						JOptionPane.ERROR_MESSAGE);
				logger.error(e);
				return null;
			} catch(ClassNotFoundException e) {
				JOptionPane.showMessageDialog(this, 
						"Error while reading data.", null, 
						JOptionPane.ERROR_MESSAGE);
				logger.error(e);
			}

			if(obj instanceof Map<?, ?>) {
				return (Map<?, ?>) obj;
			} else {
				JOptionPane.showMessageDialog(this, 
						"File does not contain compatible data.", null, 
						JOptionPane.ERROR_MESSAGE);
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Import energy data.
	 */
	private void importEnergyData() {
		Map<?,?> data = importData();
		if(data != null) {
			for(City city : simulator.getScenario().getCountry().getCities()) {
				if(data.containsKey(city.getName()) 
						&& data.get(city.getName()) instanceof Object[]
								&& ((Object[])data.get(city.getName())).length == 2) {
					Object[] objects = (Object[])data.get(city.getName());
					if(city.getPetroleumSystem() instanceof RecordedPetroleumSystem
							&& objects[0] instanceof RecordedPetroleumSystem) {
						city.setPetroleumSystem(
								(RecordedPetroleumSystem) objects[0]);
					} else {
						JOptionPane.showMessageDialog(this, 
								"File does not contain petroleum system data.", null, 
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(city.getElectricitySystem() instanceof RecordedElectricitySystem
							&& objects[1] instanceof RecordedElectricitySystem) {
						city.setElectricitySystem(
								(RecordedElectricitySystem) objects[1]);
					} else {
						JOptionPane.showMessageDialog(this, 
								"File does not contain electricity system data.", null, 
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				} else {
					JOptionPane.showMessageDialog(this, 
							"File does not contain energy system data.", null, 
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			JOptionPane.showMessageDialog(this, 
					"Loaded energy data.", null, 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Import water data.
	 */
	private void importWaterData() {
		Map<?,?> data = importData();
		if(data != null) {
			for(City city : simulator.getScenario().getCountry().getCities()) {
				if(data.containsKey(city.getName()) 
						&& city.getWaterSystem() instanceof RecordedWaterSystem
						&& data.get(city.getName()) instanceof RecordedWaterSystem) {
					city.setWaterSystem(
							(RecordedWaterSystem) data.get(city.getName()));
				} else {
					JOptionPane.showMessageDialog(this, 
							"File does not contain water system data.", null, 
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
	 * Initialize the application frame for a scenario.
	 *
	 * @param scenario the scenario
	 */
	public void initialize(Scenario scenario) {
		simulator.setScenario(scenario);
		if(scenario == null) {
			if(simulator.getConnection().isConnected()) {
				simulator.disconnect();
			}
			simulator.getConnection().removeConnectionListener(connectionPanel);
			simulator.getConnection().removeConnectionListener(connectionToolbar);
			simulator.removeUpdateListener(this);
			simulator.removeUpdateListener(simulationPane);
			simulator.removeUpdateListener(societyPane);
			simulator.removeUpdateListener(scoreLogger);
			if(contentPane.getComponentCount() > 0) {
				contentPane.removeAll();
			}
			societyPane = null;
			elementsPane = null;
			nationalPane = null;
			simulationPane = null;
			
			validate();
			repaint();
			setTitle("SIPG");
		} else {
			simulator.addUpdateListener(this);
			connectionPanel.initialize(simulator);
			simulator.getConnection().addConnectionListener(connectionPanel);
			simulator.getConnection().addConnectionListener(connectionToolbar);

			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			societyPane = new SocietyPane(simulator.getScenario());
			simulator.addUpdateListener(societyPane);
			scoreLogger = new ScoreFileLogger();
			simulator.addUpdateListener(scoreLogger);
			elementsPane = new ElementsPane(simulator);
			elementsPane.initialize();
			simulationPane = new SimulationControlPanel(simulator);
			simulator.addUpdateListener(simulationPane);
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

		newScenario.setEnabled(scenario == null);
		saveScenario.setEnabled(scenario != null);
		closeScenario.setEnabled(scenario != null);
		editConnection.setEnabled(scenario != null);

		exportAgriculture.setEnabled(scenario != null 
				&& scenario.getCountry().getAgricultureSystem().isLocal());
		importAgriculture.setEnabled(scenario != null 
				&& !scenario.getCountry().getAgricultureSystem().isLocal());
		clearAgriculture.setEnabled(scenario != null 
				&& !scenario.getCountry().getAgricultureSystem().isLocal());

		exportWater.setEnabled(scenario != null 
				&& scenario.getCountry().getWaterSystem().isLocal());	
		importWater.setEnabled(scenario != null 
				&& !scenario.getCountry().getWaterSystem().isLocal());
		clearWater.setEnabled(scenario != null
				&& !scenario.getCountry().getWaterSystem().isLocal());	

		exportEnergy.setEnabled(scenario != null 
				&& scenario.getCountry().getPetroleumSystem().isLocal()
				&& scenario.getCountry().getElectricitySystem().isLocal());
		importEnergy.setEnabled(scenario != null 
				&& !scenario.getCountry().getPetroleumSystem().isLocal()
				&& !scenario.getCountry().getElectricitySystem().isLocal());
		clearEnergy.setEnabled(scenario != null 
				&& !scenario.getCountry().getPetroleumSystem().isLocal()
				&& !scenario.getCountry().getElectricitySystem().isLocal());
	}

	/**
	 * Save the scenario to file.
	 *
	 * @param file the file
	 */
	private void save(File file) {
		try {
			FileUtils.writeStringToFile(file, 
					Serialization.serialize(simulator.getScenario()), 
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(contentPane.getTopLevelAncestor(), 
					e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			logger.error(e);
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

	@Override
	public void simulationCompleted(UpdateEvent event) {
		File finalState = new File(logOutputDir, System.getProperty("user.name") 
				+ "-" + new Date().getTime() + ".json");
		try {
			finalState.createNewFile();
		} catch (IOException e) {
			logger.error(e);
		}
		save(finalState);

		File userFinalState = new File(userOutputDir, 
				new Date().getTime() + "-scenario.json");
		try {
			userFinalState.createNewFile();
		} catch (IOException e) {
			logger.error(e);
		}
		save(userFinalState);
	}

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
					autoSave();
					setTitle("SIPG | " + event.getTime());
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

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
					setTitle("SIPG | " + event.getTime());
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
