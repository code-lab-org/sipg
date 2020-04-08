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
package edu.mit.sipg.sim;

import edu.mit.sipg.gui.event.SimulationControlListener;
import edu.mit.sipg.gui.event.UpdateListener;
import edu.mit.sipg.scenario.Scenario;

/**
 * An interface to a simulator which processes a scenario.
 * 
 * @author Paul T. Grogan
 */
public interface Simulator extends SimulationControlListener {

	/**
	 * Adds the update listener.
	 *
	 * @param listener the listener
	 */
	public void addUpdateListener(UpdateListener listener);
	
	/**
	 * Connects this simulator.
	 */
	public void connect();
	
	/**
	 * Disconnects this simulator.
	 */
	public void disconnect();

	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public Connection getConnection();
	
	/**
	 * Gets the scenario.
	 *
	 * @return the scenario
	 */
	public Scenario getScenario();
	
	/**
	 * Sets the scenario.
	 *
	 * @param scenario the new scenario
	 */
	public void setScenario(Scenario scenario);

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public long getEndTime();

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public long getStartTime();

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public long getTime();

	/**
	 * Checks if is auto optimize distribution.
	 *
	 * @return true, if is auto optimize distribution
	 */
	public boolean isAutoOptimizeDistribution();

	/**
	 * Checks if is auto optimize production and distribution.
	 *
	 * @return true, if is auto optimize production and distribution
	 */
	public boolean isAutoOptimizeProductionAndDistribution();

	/**
	 * Checks if is completed.
	 *
	 * @return true, if is completed
	 */
	public boolean isCompleted();

	/**
	 * Checks if is initialized.
	 *
	 * @return true, if is initialized
	 */
	public boolean isInitialized();

	/**
	 * Removes the update listener.
	 *
	 * @param listener the listener
	 */
	public void removeUpdateListener(UpdateListener listener);

	/**
	 * Run auto optimization.
	 */
	public void runAutoOptimization();

	/**
	 * Run optimization.
	 */
	public void runOptimization();

	/**
	 * Sets the auto optimize distribution.
	 *
	 * @param autoOptimizeDistribution the new auto optimize distribution
	 */
	public void setAutoOptimizeDistribution(boolean autoOptimizeDistribution);

	/**
	 * Sets the auto optimize production and distribution.
	 *
	 * @param autoOptimizeProductionAndDistribution the new auto optimize production and distribution
	 */
	public void setAutoOptimizeProductionAndDistribution(boolean autoOptimizeProductionAndDistribution);

}