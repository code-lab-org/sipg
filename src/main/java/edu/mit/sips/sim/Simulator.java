/*
 * 
 */
package edu.mit.sips.sim;

import edu.mit.sips.core.OptimizationOptions;
import edu.mit.sips.gui.SimulationControlListener;
import edu.mit.sips.gui.UpdateListener;
import edu.mit.sips.scenario.Scenario;

public interface Simulator extends SimulationControlListener {

	/**
	 * Adds the update listener.
	 *
	 * @param listener the listener
	 */
	void addUpdateListener(UpdateListener listener);
	
	/**
	 * Connects this simulator.
	 */
	void connect();
	
	/**
	 * Disconnects this simulator.
	 */
	void disconnect();

	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	Connection getConnection();
	
	/**
	 * Gets the scenario.
	 *
	 * @return the scenario
	 */
	Scenario getScenario();

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	long getEndTime();

	/**
	 * Gets the optimization options.
	 *
	 * @return the optimization options
	 */
	OptimizationOptions getOptimizationOptions();

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	long getStartTime();

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	long getTime();

	/**
	 * Checks if is auto optimize distribution.
	 *
	 * @return true, if is auto optimize distribution
	 */
	boolean isAutoOptimizeDistribution();

	/**
	 * Checks if is auto optimize production and distribution.
	 *
	 * @return true, if is auto optimize production and distribution
	 */
	boolean isAutoOptimizeProductionAndDistribution();

	/**
	 * Checks if is completed.
	 *
	 * @return true, if is completed
	 */
	boolean isCompleted();

	/**
	 * Checks if is initialized.
	 *
	 * @return true, if is initialized
	 */
	boolean isInitialized();

	/**
	 * Removes the update listener.
	 *
	 * @param listener the listener
	 */
	void removeUpdateListener(UpdateListener listener);

	/**
	 * Run auto optimization.
	 */
	void runAutoOptimization();

	/**
	 * Run optimization.
	 */
	void runOptimization();

	/**
	 * Sets the auto optimize distribution.
	 *
	 * @param autoOptimizeDistribution the new auto optimize distribution
	 */
	void setAutoOptimizeDistribution(boolean autoOptimizeDistribution);

	/**
	 * Sets the auto optimize production and distribution.
	 *
	 * @param autoOptimizeProductionAndDistribution the new auto optimize production and distribution
	 */
	void setAutoOptimizeProductionAndDistribution(boolean autoOptimizeProductionAndDistribution);

}