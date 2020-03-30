package edu.mit.sips.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.electricity.ElectricityElement;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.petroleum.PetroleumElement;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.gui.event.SimulationControlEvent;
import edu.mit.sips.scenario.GameElementTemplate;
import edu.mit.sips.scenario.Scenario;
import edu.mit.sips.sim.hla.HlaSimulator;

public class ScenarioAnalyzer {
	public static void main(String[] args) {
		String basePath = "C:\\Users\\Paul\\Dropbox\\research\\cces\\sirs\\sips-g\\";

		String sessionPath = "session16\\";
		String[] scenarioPaths = new String[]{
		};

		try {
			FileWriter fw = new FileWriter(basePath+sessionPath+"count.csv");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("path, large_wheat, large_transport, food_2009, " +
					"large_ro, huge_ro, water_2009, " +
					"large_well, large_pipeline, oil_2009, " +
					"large_thermal, large_solar, electricity_2009\n");

			for(int i = 0; i < scenarioPaths.length; i++) {
				Scenario scenario = getScenario(
						basePath+sessionPath+scenarioPaths[i]);
				
				HlaSimulator simulator = new HlaSimulator(scenario);
				// simulate to 2009 to avoid decommission-in-2010 effects
				simulator.initializeSimulation(new SimulationControlEvent
						.Initialize(simulator, 1950, 2009));
				simulator.executeSimulation(new SimulationControlEvent
						.Execute(simulator, 1950, 2009));

				int numWheat = 0, numTransport = 0, numLargeRO = 0, 
						numHugeRO = 0, numThermal = 0, numSolar = 0, 
						numWell = 0, numPipeline = 0;
				double foodProduction = 0, waterProduction = 0, 
						oilProduction = 0, electricityProduction = 0;

				if(scenario.getCountry().getAgricultureSystem() 
						instanceof AgricultureSystem.Local) {
					AgricultureSystem.Local system = (AgricultureSystem.Local) 
							scenario.getCountry().getAgricultureSystem();
					//numAgriculture += system.getInternalElements().size();
					for(AgricultureElement e : system.getInternalElements()) {
						if(e.getTemplateName().equals(GameElementTemplate.WHEAT_2.getName())) {
							numWheat++;
						}
						if(e.getTemplateName().equals(GameElementTemplate.FOOD_TRANSPORT_2.getName())) {
							numTransport++;
						}
						/*
						if(e.getMutableElement() instanceof MutableAgricultureElement) {
							foodProduction += ((MutableAgricultureElement)e.getMutableElement())
									.getMaxFoodProduction()/1e6d;
						}
						*/
						foodProduction += e.getMaxFoodProduction()/1e6d;
					}
				}

				if(scenario.getCountry().getWaterSystem() 
						instanceof WaterSystem.Local) {
					WaterSystem.Local system = (WaterSystem.Local) 
							scenario.getCountry().getWaterSystem();
					//numWater += system.getInternalElements().size();
					for(WaterElement e : system.getInternalElements()) {
						if(e.getTemplateName().equals(GameElementTemplate.RO_PLANT_2.getName())) {
							numLargeRO++;
						}
						if(e.getTemplateName().equals(GameElementTemplate.RO_PLANT_3.getName())) {
							numHugeRO++;
						}
						/*
						if(e.getMutableElement() instanceof MutableWaterElement) {
							waterProduction += ((MutableWaterElement)e.getMutableElement())
									.getMaxWaterProduction()/1e6d;
						}
						 */
						waterProduction += e.getMaxWaterProduction()/1e6d;
					}
				}

				if(scenario.getCountry().getPetroleumSystem() 
						instanceof PetroleumSystem.Local) {
					PetroleumSystem.Local system = (PetroleumSystem.Local) 
							scenario.getCountry().getPetroleumSystem();
					//numEnergy += system.getInternalElements().size();
					for(PetroleumElement e : system.getInternalElements()) {
						if(e.getTemplateName().equals(GameElementTemplate.OIL_WELL_2.getName())) {
							numWell++;
						}
						if(e.getTemplateName().equals(GameElementTemplate.OIL_PIPELINE_2.getName())) {
							numPipeline++;
						}
						/*
						if(e.getMutableElement() instanceof MutablePetroleumElement) {
							oilProduction += ((MutablePetroleumElement)e.getMutableElement())
									.getMaxPetroleumProduction()/1e6d;
						}
						*/
						oilProduction += e.getMaxPetroleumProduction()/1e6d;
					}
				}

				if(scenario.getCountry().getElectricitySystem() 
						instanceof ElectricitySystem.Local) {
					ElectricitySystem.Local system = (ElectricitySystem.Local) 
							scenario.getCountry().getElectricitySystem();
					//numEnergy += system.getInternalElements().size();
					for(ElectricityElement e : system.getInternalElements()) {
						if(e.getTemplateName().equals(GameElementTemplate.POWER_PLANT_2.getName())) {
							numThermal++;
						}
						if(e.getTemplateName().equals(GameElementTemplate.PV_PLANT_2.getName())) {
							numSolar++;
						}
						/*
						if(e.getMutableElement() instanceof MutableElectricityElement) {
							electricityProduction += ((MutableElectricityElement)e.getMutableElement())
									.getMaxElectricityProduction()/1e6d;
						}
						*/
						electricityProduction += e.getMaxElectricityProduction()/1e6d;
					}
				}
				bw.write(scenarioPaths[i] + ", " + 
						numWheat + ", " + numTransport + ", " + foodProduction + ", " +
						numLargeRO + ", " + numHugeRO + ", " + waterProduction + ", " +
						numWell + ", " + numPipeline + ", " + oilProduction + ", " + 
						numThermal + ", " + numSolar + ", " + electricityProduction + "\n");
			}
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
