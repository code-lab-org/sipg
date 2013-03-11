package edu.mit.sips.sim;

import javax.swing.event.EventListenerList;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.NationalAgricultureSystem;
import edu.mit.sips.core.energy.NationalEnergySystem;
import edu.mit.sips.core.water.NationalWaterSystem;
import edu.mit.sips.gui.SimulationControlEvent;
import edu.mit.sips.gui.SimulationControlEvent.AdvanceToEnd;
import edu.mit.sips.gui.SimulationControlEvent.Execute;
import edu.mit.sips.gui.SimulationControlEvent.Reset;
import edu.mit.sips.gui.SimulationControlListener;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.gui.UpdateListener;

/**
 * The Class Simulator.
 */
public class Simulator implements SimulationControlListener {	
	private final Country country;
	
	private boolean autoOptimizeDistribution = true;
	private boolean autoOptimizeProductionAndDistribution = true;
	private double deltaAgricultureCost = 0, deltaWaterCost = 0, 
			deltaPetroleumCost = 0, deltaElectricityCost = 0;

	private boolean initialized = false;
	private long startTime, endTime;
	
	private long time;

	private transient EventListenerList listenerList = new EventListenerList(); // mutableO
	
	/**
	 * Instantiates a new simulator.
	 */
	protected Simulator() {
		country = null;
		listenerList = new EventListenerList();
	}
	
	/**
	 * Instantiates a new simulator.
	 *
	 * @param country the country
	 */
	public Simulator(final Country country) {
		
		// Validate country.
		if(country == null) {
			throw new IllegalArgumentException("Country cannot be null.");
		}
		this.country = country;
	}
	
	/**
	 * Adds the update listener.
	 *
	 * @param listener the listener
	 */
	public void addUpdateListener(UpdateListener listener) {
		listenerList.add(UpdateListener.class, listener);
	}
	
	private void advance(long duration) {
		if(time + duration > endTime) {
			throw new IllegalArgumentException("Duration cannot exceed end time.");
		}
		if(!initialized) {
			throw new IllegalStateException("Simulation must be initialized.");
		}
		
		long stopTime = Math.min(endTime, time + duration);
		
		while(time < stopTime) {
			tickTock();
			runAutoOptimization();
			fireUpdateEvent();
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SimulationControlListener#advanceSimulation(edu.mit.sips.gui.SimulationControlEvent)
	 */
	@Override
	public synchronized void advanceSimulation(SimulationControlEvent.Advance event) {
		advance(event.getDuration());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SimulationControlListener#advanceSimulationToEnd(edu.mit.sips.gui.SimulationControlEvent.AdvanceToEnd)
	 */
	@Override
	public synchronized void advanceSimulationToEnd(AdvanceToEnd event) {
		advance(endTime - time);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SimulationControlListener#executeSimulation(edu.mit.sips.gui.SimulationControlEvent.Execute)
	 */
	@Override
	public synchronized void executeSimulation(Execute event) {
		initialize(event.getStartTime(), event.getEndTime());
		advance(endTime - time);
	}
	
	/**
	 * Fire initialize event.
	 *
	 * @param event the event
	 */
	private void fireInitializeEvent() {
		for(UpdateListener listener 
				: listenerList.getListeners(UpdateListener.class)) {
			listener.simulationInitialized(new UpdateEvent(this, time, country));
		}
	}
	
	/**
	 * Fire update event.
	 *
	 * @param event the event
	 */
	private void fireUpdateEvent() {
		for(UpdateListener listener 
				: listenerList.getListeners(UpdateListener.class)) {
			listener.simulationUpdated(new UpdateEvent(this, time, country));
		}
	}
	
	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}
	
	/**
	 * Initialize.
	 *
	 * @param startTime the start time
	 * @param endTime the end time
	 */
	private void initialize(long startTime, long endTime) {
		// No need to validate start time.
		this.startTime = startTime;
		
		// Validate end time.
		if(endTime < startTime) {
			throw new IllegalArgumentException(
					"End year cannot precede start year.");
		}
		this.endTime = endTime;
		
		time = startTime;
		country.initialize(startTime);
		runAutoOptimization();
		fireInitializeEvent();
		initialized = true;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SimulationControlListener#initializeSimulation(edu.mit.sips.gui.SimulationControlEvent)
	 */
	@Override
	public synchronized void initializeSimulation(SimulationControlEvent.Initialize event) {
		initialize(event.getStartTime(), event.getEndTime());
	}

	/**
	 * Removes the update listener.
	 *
	 * @param listener the listener
	 */
	public void removeUpdateListener(UpdateListener listener) {
		listenerList.remove(UpdateListener.class, listener);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SimulationControlListener#resetSimulation(edu.mit.sips.gui.SimulationControlEvent.Reset)
	 */
	@Override
	public synchronized void resetSimulation(Reset event) {
		initialize(startTime, endTime);
	}

	/**
	 * Tick tock.
	 *
	 * @return the long
	 */
	private long tickTock() {
		country.tick();
		country.tock();
		return ++time;
	}
	
	/**
	 * Checks if is auto optimize distribution.
	 *
	 * @return true, if is auto optimize distribution
	 */
	public boolean isAutoOptimizeDistribution() {
		return autoOptimizeDistribution;
	}

	/**
	 * Sets the auto optimize distribution.
	 *
	 * @param autoOptimizeDistribution the new auto optimize distribution
	 */
	public void setAutoOptimizeDistribution(boolean autoOptimizeDistribution) {
		this.autoOptimizeDistribution = autoOptimizeDistribution;
	}

	/**
	 * Checks if is auto optimize production and distribution.
	 *
	 * @return true, if is auto optimize production and distribution
	 */
	public boolean isAutoOptimizeProductionAndDistribution() {
		return autoOptimizeProductionAndDistribution;
	}

	/**
	 * Sets the auto optimize production and distribution.
	 *
	 * @param autoOptimizeProductionAndDistribution the new auto optimize production and distribution
	 */
	public void setAutoOptimizeProductionAndDistribution(
			boolean autoOptimizeProductionAndDistribution) {
		this.autoOptimizeProductionAndDistribution = autoOptimizeProductionAndDistribution;
	}
	
	/**
	 * Run auto optimization.
	 */
	private void runAutoOptimization() {
		if(autoOptimizeProductionAndDistribution) {
			((NationalAgricultureSystem)country.getAgricultureSystem())
			.optimizeFoodProductionAndDistribution(deltaAgricultureCost);
			
			((NationalWaterSystem)country.getWaterSystem())
			.optimizeWaterProductionAndDistribution(deltaWaterCost);
			
			((NationalEnergySystem)country.getEnergySystem())
			.optimizeEnergyProductionAndDistribution(deltaPetroleumCost, deltaElectricityCost);
		} else if(autoOptimizeDistribution) {
			((NationalAgricultureSystem)country.getAgricultureSystem())
			.optimizeFoodDistribution();
			
			((NationalWaterSystem)country.getWaterSystem())
			.optimizeWaterDistribution();
			
			((NationalEnergySystem)country.getEnergySystem())
			.optimizeEnergyDistribution();
		}
	}
	
	/**
	 * Run optimization.
	 */
	public void runOptimization() {
		runAutoOptimization();
		fireUpdateEvent();
	}
}
