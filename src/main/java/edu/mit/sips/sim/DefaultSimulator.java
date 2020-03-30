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
package edu.mit.sips.sim;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import edu.mit.sips.core.agriculture.AgricultureSoS;
import edu.mit.sips.core.electricity.ElectricitySoS;
import edu.mit.sips.core.petroleum.PetroleumSoS;
import edu.mit.sips.core.water.WaterSoS;
import edu.mit.sips.gui.event.SimulationControlEvent;
import edu.mit.sips.gui.event.UpdateEvent;
import edu.mit.sips.gui.event.UpdateListener;
import edu.mit.sips.gui.event.SimulationControlEvent.AdvanceToEnd;
import edu.mit.sips.gui.event.SimulationControlEvent.Execute;
import edu.mit.sips.gui.event.SimulationControlEvent.Reset;
import edu.mit.sips.scenario.Scenario;

/**
 * The default implementation of the simulator interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultSimulator implements Simulator {	
	private static Logger logger = Logger.getLogger(DefaultSimulator.class);
	
	protected Scenario scenario;
	protected boolean autoOptimizeDistribution = true;
	protected boolean autoOptimizeProductionAndDistribution = true;
	protected final AtomicBoolean initialized = new AtomicBoolean(false);
	protected final AtomicBoolean completed = new AtomicBoolean(false);
	protected long startTime, endTime;
	protected long time;
	protected final int numberIterations = 4;
	protected final Connection connection;
	protected transient EventListenerList listenerList = new EventListenerList();
	
	/**
	 * Instantiates a new default simulator.
	 *
	 * @param scenario the scenario
	 * @param connection the connection
	 */
	public DefaultSimulator(Scenario scenario, Connection connection) {
		this.scenario = scenario;
		this.connection = connection;
	}

	/**
	 * Adds the update listener.
	 *
	 * @param listener the listener
	 */
	@Override
	public void addUpdateListener(UpdateListener listener) {
		listenerList.add(UpdateListener.class, listener);
	}
	
	/**
	 * Advance.
	 *
	 * @param duration the duration
	 */
	protected void advance(long duration) {
		if(scenario == null) {
			return;
		}
		
		logger.debug("Advancing simulation with duration " + duration + ".");
		
		if(time + duration > endTime) {
			throw new IllegalArgumentException("Duration cannot exceed end time.");
		}
		if(!initialized.get()) {
			throw new IllegalStateException("Simulation must be initialized.");
		}
		
		long stopTime = Math.min(endTime, time + duration);
		
		while(time <= stopTime) {
			for(int i = 0; i < numberIterations; i++) {
				runAutoOptimization();
			}
			
			logger.trace("Tick/tocking the country (time = " + time + ").");
			scenario.getCountry().tick();
			fireUpdateEvent(time);
			if(time >= endTime) {
				logger.trace("Simulation is completed.");
				completed.set(true);
				fireCompleteEvent(time);
				return;
			}
			scenario.getCountry().tock();
			time = time + 1;
			logger.trace("The time is now " + time + ".");
		}
	}
	
	@Override
	public synchronized void advanceSimulation(SimulationControlEvent.Advance event) {
		advance(event.getDuration());
	}
	
	@Override
	public synchronized void advanceSimulationToEnd(AdvanceToEnd event) {
		advance(endTime - time);
	}
	
	@Override
	public synchronized void executeSimulation(Execute event) {
		initialize(event.getStartTime(), event.getEndTime());
		advance(endTime - time);
	}
	
	/**
	 * Fire complete event.
	 *
	 * @param time the time
	 */
	protected void fireCompleteEvent(long time) {
		logger.info("Firing complete event with time " + time);
		for(UpdateListener listener 
				: listenerList.getListeners(UpdateListener.class)) {
			listener.simulationCompleted(new UpdateEvent(this, time, scenario.getCountry()));
		}
	}
	
	/**
	 * Fire initialize event.
	 */
	protected void fireInitializeEvent() {
		for(UpdateListener listener 
				: listenerList.getListeners(UpdateListener.class)) {
			listener.simulationInitialized(new UpdateEvent(this, time, scenario.getCountry()));
		}
	}
	
	/**
	 * Fire update event.
	 *
	 * @param time the time
	 */
	protected void fireUpdateEvent(long time) {
		logger.info("Firing update event with time " + time);
		for(UpdateListener listener 
				: listenerList.getListeners(UpdateListener.class)) {
			listener.simulationUpdated(new UpdateEvent(this, time, scenario.getCountry()));
		}
	}
	
	@Override
	public Connection getConnection() {
		return connection;
	}

	@Override
	public Scenario getScenario() {
		return scenario;
	}
	
	@Override
	public long getEndTime() {
		return endTime;
	}
	
	@Override
	public long getStartTime() {
		return startTime;
	}
	
	@Override
	public long getTime() {
		return time;
	}
	
	/**
	 * Initialize.
	 *
	 * @param startTime the start time
	 * @param endTime the end time
	 */
	protected void initialize(long startTime, long endTime) {
		// No need to validate start time.
		this.startTime = startTime;
		
		// Validate end time.
		if(endTime < startTime) {
			throw new IllegalArgumentException(
					"End year cannot precede start year.");
		}
		this.endTime = endTime;
		
		time = startTime;

		scenario.getCountry().initialize(startTime);

		for(int i = 0; i < numberIterations; i++) {
			runAutoOptimization();
		}
		
		fireInitializeEvent();

		initialized.set(true);
		completed.set(false);
	}

	@Override
	public synchronized void initializeSimulation(SimulationControlEvent.Initialize event) {
		initialize(event.getStartTime(), event.getEndTime());
	}

	@Override
	public boolean isAutoOptimizeDistribution() {
		return autoOptimizeDistribution;
	}

	@Override
	public boolean isAutoOptimizeProductionAndDistribution() {
		return autoOptimizeProductionAndDistribution;
	}

	@Override
	public boolean isCompleted() {
		return completed.get();
	}

	@Override
	public boolean isInitialized() {
		return initialized.get();
	}

	@Override
	public void removeUpdateListener(UpdateListener listener) {
		listenerList.remove(UpdateListener.class, listener);
	}

	@Override
	public synchronized void resetSimulation(Reset event) {
		initialize(startTime, endTime);
	}

	@Override
	public void runAutoOptimization() {
		if(autoOptimizeProductionAndDistribution) {
			if(scenario.getCountry().getAgricultureSystem() instanceof AgricultureSoS.Local) {
				((AgricultureSoS.Local)scenario.getCountry().getAgricultureSystem())
				.optimizeFoodProductionAndDistribution();
			}
			
			if(scenario.getCountry().getWaterSystem() instanceof WaterSoS.Local) {
				((WaterSoS.Local)scenario.getCountry().getWaterSystem())
				.optimizeWaterProductionAndDistribution();
			}
			
			if(scenario.getCountry().getElectricitySystem() instanceof ElectricitySoS.Local) {
				((ElectricitySoS.Local)scenario.getCountry().getElectricitySystem())
				.optimizeElectricityProductionAndDistribution();
			}
			
			if(scenario.getCountry().getPetroleumSystem() instanceof PetroleumSoS.Local) {
				((PetroleumSoS.Local)scenario.getCountry().getPetroleumSystem())
				.optimizePetroleumProductionAndDistribution();
			}
		} else if(autoOptimizeDistribution) {
			if(scenario.getCountry().getAgricultureSystem() instanceof AgricultureSoS.Local) {
				((AgricultureSoS.Local)scenario.getCountry().getAgricultureSystem())
				.optimizeFoodDistribution();
			}
			
			if(scenario.getCountry().getWaterSystem() instanceof WaterSoS.Local) {
				((WaterSoS.Local)scenario.getCountry().getWaterSystem())
				.optimizeWaterDistribution();
			}
			
			if(scenario.getCountry().getElectricitySystem() instanceof ElectricitySoS.Local) {
				((ElectricitySoS.Local)scenario.getCountry().getElectricitySystem())
				.optimizeElectricityDistribution();
			}
			
			if(scenario.getCountry().getPetroleumSystem() instanceof PetroleumSoS.Local) {
				((PetroleumSoS.Local)scenario.getCountry().getPetroleumSystem())
				.optimizePetroleumDistribution();
			}
		}
	}

	@Override
	public void runOptimization() {
		runAutoOptimization();
		fireUpdateEvent(time);
	}

	@Override
	public void setAutoOptimizeDistribution(boolean autoOptimizeDistribution) {
		this.autoOptimizeDistribution = autoOptimizeDistribution;
	}
	
	@Override
	public void setAutoOptimizeProductionAndDistribution(
			boolean autoOptimizeProductionAndDistribution) {
		this.autoOptimizeProductionAndDistribution = autoOptimizeProductionAndDistribution;
	}

	@Override
	public void connect() {
		connection.setConnected(true);
	}

	@Override
	public void disconnect() {
		connection.setConnected(false);
	}

	@Override
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
		initialized.set(false);
	}
}
