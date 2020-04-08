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
package edu.mit.sipg.post;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.mit.sipg.core.agriculture.AgricultureElement;
import edu.mit.sipg.core.agriculture.LocalAgricultureSoS;
import edu.mit.sipg.core.electricity.ElectricityElement;
import edu.mit.sipg.core.electricity.LocalElectricitySoS;
import edu.mit.sipg.core.petroleum.LocalPetroleumSoS;
import edu.mit.sipg.core.petroleum.PetroleumElement;
import edu.mit.sipg.core.water.LocalWaterSoS;
import edu.mit.sipg.core.water.WaterElement;
import edu.mit.sipg.gui.ApplicationFrame;
import edu.mit.sipg.gui.event.SimulationControlEvent;
import edu.mit.sipg.io.Serialization;
import edu.mit.sipg.scenario.GameScenario;
import edu.mit.sipg.scenario.Scenario;
import edu.mit.sipg.scenario.Sector;
import edu.mit.sipg.sim.DefaultSimulator;

/**
 * An application to combine scenarios from three player roles into one.
 * 
 * This script assumes the data are organized as follows:
 * 
 * <BASE_PATH>/<SESSION_DIR>/<PLAYER_DIR>/<SCENARIO_FILE>
 * 
 * where <BASE_PATH> is the base path to the data directory, 
 * <SESSION_DIR> is a directory containing all scenario files for an experimental session, 
 * <PLAYER_DIR> defines the player-specific sub-directory, and 
 * <SCENARIO_FILE> is a saved JSON scenario assuming *equal numbers* of scenarios for each player.
 * 
 * @author Paul T. Grogan
 */
public class ScenarioCombinator {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		String basePath = ""; // define directory path to data directory
		String sessionDir = ""; // define session directory
		String agricultureDir = ""; // define sub-directory for agriculture player data
		String waterDir = ""; // define sub-directory for water player data
		String energyDir = ""; // define sub-directory for energy player data
		
		String[] agricultureFiles = new String[]{
				// list individual agriculture player scenarios here
		};
		String[] waterFiles = new String[]{
				// list individual water player scenarios here
		};
		String[] energyFiles = new String[]{
				// list individual energy player scenarios here
		};

		// verify that simulation runs for each player scenario
		for(int i=0; i < agricultureFiles.length; i++) {
			runSimulation(getScenario(
					basePath+sessionDir+agricultureDir+agricultureFiles[i]));
		}
		for(int i=0; i < waterFiles.length; i++) {
			runSimulation(getScenario(
					basePath+sessionDir+waterDir+waterFiles[i]));
		}
		for(int i=0; i < energyFiles.length; i++) {
			runSimulation(getScenario(
					basePath+sessionDir+energyDir+energyFiles[i]));
		}
		
		for(int i = 0; i < agricultureFiles.length; i++) {
			// get the scenarios for each individual player
			Scenario agricultureScenario = getScenario(
					basePath+sessionDir+agricultureDir+agricultureFiles[i]);
			Scenario waterScenario = getScenario(
					basePath+sessionDir+waterDir+waterFiles[i]);
			Scenario energyScenario = getScenario(
					basePath+sessionDir+energyDir+energyFiles[i]);
			
			// create an aggregated scenario
			Scenario masterScenario = new GameScenario(
					Arrays.asList(GameScenario.INDUSTRIAL, 
							GameScenario.URBAN, 
							GameScenario.RURAL),
							Arrays.asList(Sector.AGRICULTURE,
									Sector.WATER,
									Sector.ELECTRICITY,
									Sector.PETROLEUM), true);
			// copy over all agriculture elements
			LocalAgricultureSoS masterAgricultureSoS = (LocalAgricultureSoS) masterScenario.getCountry().getAgricultureSystem();
			for(AgricultureElement e : masterAgricultureSoS.getElements()) {
				masterAgricultureSoS.removeElement(e);
			}
			LocalAgricultureSoS baseAgricultureSoS = (LocalAgricultureSoS) agricultureScenario.getCountry().getAgricultureSystem();
			for(AgricultureElement e : baseAgricultureSoS.getElements()) {
				masterAgricultureSoS.addElement(e);
			}
			// copy over all water elements
			LocalWaterSoS masterWaterSoS = (LocalWaterSoS) masterScenario.getCountry().getWaterSystem();
			for(WaterElement e : masterWaterSoS.getElements()) {
				masterWaterSoS.removeElement(e);
			}
			LocalWaterSoS baseWaterSoS = (LocalWaterSoS) waterScenario.getCountry().getWaterSystem();
			for(WaterElement e : baseWaterSoS.getElements()) {
				masterWaterSoS.addElement(e);
			}
			// copy over all petroleum elements
			LocalPetroleumSoS masterPetroleumSoS = (LocalPetroleumSoS) masterScenario.getCountry().getPetroleumSystem();
			for(PetroleumElement e : masterPetroleumSoS.getElements()) {
				masterPetroleumSoS.removeElement(e);
			}
			LocalPetroleumSoS basePetroleumSoS = (LocalPetroleumSoS) energyScenario.getCountry().getPetroleumSystem();
			for(PetroleumElement e : basePetroleumSoS.getElements()) {
				masterPetroleumSoS.addElement(e);
			}
			// copy over all electricity elements
			LocalElectricitySoS masterElectricitySoS = (LocalElectricitySoS) masterScenario.getCountry().getElectricitySystem();
			for(ElectricityElement e : masterElectricitySoS.getElements()) {
				masterElectricitySoS.removeElement(e);
			}
			LocalElectricitySoS baseElectricitySoS = (LocalElectricitySoS) energyScenario.getCountry().getElectricitySystem();
			for(ElectricityElement e : baseElectricitySoS.getElements()) {
				masterElectricitySoS.addElement(e);
			}
			// write out master scenario file
			try {
				FileWriter fw = new FileWriter(basePath+sessionDir+"master-"+(i+1)+".json");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(Serialization.serialize(masterScenario));
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// verify scenario can be simulated
			runSimulation(masterScenario);
		}
	}
	
	/**
	 * Run simulation (and launch GUI to verify results).
	 *
	 * @param scenario the scenario
	 */
	private static void runSimulation(Scenario scenario) {
		final DefaultSimulator simulator = new DefaultSimulator(scenario);
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					ApplicationFrame frame = new ApplicationFrame(simulator);
					frame.pack();
					frame.setVisible(true);
				}
			});
		} catch (InterruptedException | InvocationTargetException e) {
			e.printStackTrace();
		}
		simulator.initializeSimulation(new SimulationControlEvent.Initialize(simulator, 1950, 2010));
		simulator.executeSimulation(new SimulationControlEvent.Execute(simulator, 1950, 2010));
	}

	/**
	 * Gets the scenario.
	 *
	 * @param filePath the file path
	 * @return the scenario
	 */
	private static Scenario getScenario(String filePath) {
		StringBuilder jsonBuilder = new StringBuilder();

		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null) {
				jsonBuilder.append(line);
			}
			br.close();
			fr.close();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return Serialization.deserialize(jsonBuilder.toString());
	}
}
