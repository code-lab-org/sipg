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
package edu.mit.sipg.io;

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
import edu.mit.sipg.scenario.GameScenario;
import edu.mit.sipg.scenario.Scenario;
import edu.mit.sipg.scenario.Sector;
import edu.mit.sipg.sim.hla.HlaSimulator;

/**
 * An application to combine scenarios from three player roles into one.
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
		String basePath = "C:\\Users\\Paul\\Dropbox\\research\\cces\\sirs\\sips-g\\";
		String agricultureBasePath = "green\\";
		String waterBasePath = "blue\\";
		String energyBasePath = "red\\";
		
		String sessionPath = "session13\\";
		String[] agriculturePaths = new String[]{};
		String[] waterPaths = new String[]{};
		String[] energyPaths = new String[]{};

		for(int i=0; i < agriculturePaths.length; i++) {
			runSimulation(getScenario(
					basePath+sessionPath+agricultureBasePath+agriculturePaths[i]));
		}

		for(int i=0; i < waterPaths.length; i++) {
			runSimulation(getScenario(
					basePath+sessionPath+waterBasePath+waterPaths[i]));
		}

		for(int i=0; i < energyPaths.length; i++) {
			runSimulation(getScenario(
					basePath+sessionPath+energyBasePath+energyPaths[i]));
		}
		
		for(int i = 0; i < agriculturePaths.length; i++) {
			Scenario agricultureScenario = getScenario(
					basePath+sessionPath+agricultureBasePath+agriculturePaths[i]);
			Scenario waterScenario = getScenario(
					basePath+sessionPath+waterBasePath+waterPaths[i]);
			Scenario energyScenario = getScenario(
					basePath+sessionPath+energyBasePath+energyPaths[i]);

			Scenario masterScenario = new GameScenario(
					Arrays.asList(GameScenario.INDUSTRIAL, 
							GameScenario.URBAN, 
							GameScenario.RURAL),
							Arrays.asList(Sector.AGRICULTURE,
									Sector.WATER,
									Sector.ELECTRICITY,
									Sector.PETROLEUM), true);
			LocalAgricultureSoS masterAgricultureSoS = (LocalAgricultureSoS) masterScenario.getCountry().getAgricultureSystem();
			for(AgricultureElement e : masterAgricultureSoS.getElements()) {
				masterAgricultureSoS.removeElement(e);
			}
			LocalAgricultureSoS baseAgricultureSoS = (LocalAgricultureSoS) agricultureScenario.getCountry().getAgricultureSystem();
			for(AgricultureElement e : baseAgricultureSoS.getElements()) {
				masterAgricultureSoS.addElement(e);
			}

			LocalWaterSoS masterWaterSoS = (LocalWaterSoS) masterScenario.getCountry().getWaterSystem();
			for(WaterElement e : masterWaterSoS.getElements()) {
				masterWaterSoS.removeElement(e);
			}
			LocalWaterSoS baseWaterSoS = (LocalWaterSoS) waterScenario.getCountry().getWaterSystem();
			for(WaterElement e : baseWaterSoS.getElements()) {
				masterWaterSoS.addElement(e);
			}

			LocalPetroleumSoS masterPetroleumSoS = (LocalPetroleumSoS) masterScenario.getCountry().getPetroleumSystem();
			for(PetroleumElement e : masterPetroleumSoS.getElements()) {
				masterPetroleumSoS.removeElement(e);
			}
			LocalPetroleumSoS basePetroleumSoS = (LocalPetroleumSoS) energyScenario.getCountry().getPetroleumSystem();
			for(PetroleumElement e : basePetroleumSoS.getElements()) {
				masterPetroleumSoS.addElement(e);
			}

			LocalElectricitySoS masterElectricitySoS = (LocalElectricitySoS) masterScenario.getCountry().getElectricitySystem();
			for(ElectricityElement e : masterElectricitySoS.getElements()) {
				masterElectricitySoS.removeElement(e);
			}
			LocalElectricitySoS baseElectricitySoS = (LocalElectricitySoS) energyScenario.getCountry().getElectricitySystem();
			for(ElectricityElement e : baseElectricitySoS.getElements()) {
				masterElectricitySoS.addElement(e);
			}

			try {
				FileWriter fw = new FileWriter(basePath+sessionPath+"master-"+(i+1)+".json");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(Serialization.serialize(masterScenario));
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			runSimulation(masterScenario);
		}
	}
	
	/**
	 * Run simulation.
	 *
	 * @param scenario the scenario
	 */
	private static void runSimulation(Scenario scenario) {
		final HlaSimulator simulator = new HlaSimulator(scenario);
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
