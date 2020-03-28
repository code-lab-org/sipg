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
package edu.mit.sips.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;

import edu.mit.sips.core.agriculture.AgricultureSoS;
import edu.mit.sips.core.electricity.ElectricitySoS;
import edu.mit.sips.core.petroleum.PetroleumSoS;
import edu.mit.sips.core.water.WaterSoS;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.gui.UpdateListener;

/**
 * Logs the scores for all local infrastructure systems at the end of 
 * each simulation execution. Writes a comma-delimited text file to 
 * the SIPG_HOME logs directory (specified with environment variable, 
 * defaults to user.home/SIPG/logs).
 * 
 * @author Paul T. Grogan
 */
public class ScoreFileLogger implements UpdateListener {
	private static Logger logger = Logger.getLogger(ScoreFileLogger.class);
	
	private final File logFile;
	
	private int overBudgetYear = 0;
	private int roundNumber = 0;
	
	/**
	 * Instantiates a new score file logger.
	 */
	public ScoreFileLogger() {
		File userOutputDir;
		if(System.getenv().containsKey("SIPG_HOME")) {
			userOutputDir = new File(System.getenv("SIPG_HOME"));
		} else {
			userOutputDir = new File(System.getProperty("user.home"), "SIPG");
		}
		if(!userOutputDir.exists()) {
			userOutputDir.mkdir();
		}
		File logOutputDir = new File(userOutputDir, "logs");
		if(!logOutputDir.exists()) {
			logOutputDir.mkdir();
		}
		logFile = new File(logOutputDir, System.getProperty("user.name") 
				+ "_" + new Date().getTime() + ".log");
	}

	@Override
	public void simulationCompleted(UpdateEvent event) {
		int year = (int) event.getTime();
		
		try {
			FileWriter fw = new FileWriter(logFile, true);
			fw.write(new Date().getTime() + ", ");
			fw.write(roundNumber + ", ");
			
			double foodScore = event.getCountry().getAgricultureSystem().getFoodSecurityScore();
			double aquiferScore = event.getCountry().getWaterSystem().getAquiferSecurityScore();
			double reservoirScore = event.getCountry().getPetroleumSystem().getReservoirSecurityScore();
			
			fw.write(foodScore + ", ");
			fw.write(aquiferScore + ", ");
			fw.write(reservoirScore + ", ");
			
			if(event.getCountry().getAgricultureSystem() instanceof AgricultureSoS.Local) {	
				double politicalScore = ((AgricultureSoS.Local) event.getCountry().getAgricultureSystem()).getPoliticalPowerScore(year);
				double financialScore = ((AgricultureSoS.Local) event.getCountry().getAgricultureSystem()).getFinancialSecurityScore(year);
				double aggregateScore = ((AgricultureSoS.Local) event.getCountry().getAgricultureSystem()).getAggregateScore(year);

				fw.write(financialScore + ", ");
				fw.write(politicalScore + ", ");
				fw.write(aggregateScore + ", ");
			}
			if(event.getCountry().getWaterSystem() instanceof WaterSoS.Local) {
				double politicalScore = ((WaterSoS.Local) event.getCountry().getWaterSystem()).getPoliticalPowerScore(year);
				double financialScore = ((WaterSoS.Local) event.getCountry().getWaterSystem()).getFinancialSecurityScore(year);
				double aggregateScore = ((WaterSoS.Local) event.getCountry().getWaterSystem()).getAggregateScore(year);

				fw.write(financialScore + ", ");
				fw.write(politicalScore + ", ");
				fw.write(aggregateScore + ", ");
			}
			if(event.getCountry().getPetroleumSystem() instanceof PetroleumSoS.Local
					&& event.getCountry().getElectricitySystem() instanceof ElectricitySoS.Local) {
				double politicalScore = ((PetroleumSoS.Local) event.getCountry().getPetroleumSystem()).getPoliticalPowerScore(
						year, (ElectricitySoS.Local) event.getCountry().getElectricitySystem());
				double financialScore = ((PetroleumSoS.Local) event.getCountry().getPetroleumSystem()).getFinancialSecurityScore(
						year, (ElectricitySoS.Local) event.getCountry().getElectricitySystem());
				double aggregateScore = ((PetroleumSoS.Local) event.getCountry().getPetroleumSystem()).getAggregateScore(
						year, (ElectricitySoS.Local) event.getCountry().getElectricitySystem());

				fw.write(financialScore + ", ");
				fw.write(politicalScore + ", ");
				fw.write(aggregateScore + ", ");
			}

			fw.write(overBudgetYear + ", ");
			fw.write(event.getCountry().getFinancialSecurityScore(year) + ", ");
			fw.write(event.getCountry().getAggregatedScore(year) + "\n");
			fw.close();
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	@Override
	public void simulationInitialized(UpdateEvent event) {
		overBudgetYear = 0;
		roundNumber++;
		if(!logFile.exists()) {
			try {
				FileWriter fw = new FileWriter(logFile);
				fw.write("Time, ");
				fw.write("Round, ");
				fw.write("Food Security, ");
				fw.write("Aquifer Security, ");
				fw.write("Reservoir Security, ");

				if(event.getCountry().getAgricultureSystem().isLocal()) {
					fw.write("Agriculture Profit, ");
					fw.write("Agriculture Investment, ");
					fw.write("Agriculture Score, ");
				}
				if(event.getCountry().getWaterSystem().isLocal()) {
					fw.write("Water Profit, ");
					fw.write("Water Investment, ");
					fw.write("Water Score, ");
				}
				if(event.getCountry().getPetroleumSystem().isLocal() 
						&& event.getCountry().getElectricitySystem().isLocal()) {
					fw.write("Energy Profit, ");
					fw.write("Energy Investment, ");
					fw.write("Energy Score, ");
				}

				fw.write("Over Budget, ");
				fw.write("National Profit, ");
				fw.write("Team Score \n");
				fw.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	@Override
	public void simulationUpdated(UpdateEvent event) {
		int year = (int) event.getTime();
		if(event.getCountry().getTotalCapitalExpense() > event.getCountry().getCapitalBudgetLimit()) {
			overBudgetYear = year;
		}
	}
}
