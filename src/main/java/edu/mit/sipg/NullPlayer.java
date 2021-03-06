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
package edu.mit.sipg;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import edu.mit.sipg.gui.ApplicationFrame;
import edu.mit.sipg.scenario.GameScenario;
import edu.mit.sipg.scenario.Scenario;
import edu.mit.sipg.scenario.Sector;
import edu.mit.sipg.sim.Simulator;
import edu.mit.sipg.sim.hla.HlaSimulator;

/**
 * The application for a null player that does not control any infrastructure sectors.
 * 
 * @author Paul T. Grogan
 */
public class NullPlayer {
	private static Logger logger = Logger.getLogger(NullPlayer.class);
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();

		logger.debug("Creating scenario.");
		Scenario scenario = new GameScenario(
				Arrays.asList(GameScenario.INDUSTRIAL, 
						GameScenario.URBAN, 
						GameScenario.RURAL),
				new ArrayList<Sector>(), true);

		logger.debug("Creating simulator.");
		final Simulator simulator = new HlaSimulator(scenario);
		//simulator.addUpdateListener(new ConsoleLogger());
		
		logger.debug("Launching graphical user interface.");
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					ApplicationFrame frame = new ApplicationFrame(simulator);
					frame.pack();
					frame.setVisible(true);
				}
			});
		} catch (InterruptedException | InvocationTargetException e) {
			logger.error(e);
		}

		simulator.getConnection().setFederateName("Null Player");
		simulator.connect();
	}
}
