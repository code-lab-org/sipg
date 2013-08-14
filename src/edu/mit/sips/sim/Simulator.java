package edu.mit.sips.sim;

import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.OptimizationOptions;
import edu.mit.sips.core.agriculture.AgricultureSoS;
import edu.mit.sips.core.energy.EnergySoS;
import edu.mit.sips.core.water.WaterSoS;
import edu.mit.sips.gui.SimulationControlEvent;
import edu.mit.sips.gui.SimulationControlEvent.AdvanceToEnd;
import edu.mit.sips.gui.SimulationControlEvent.Execute;
import edu.mit.sips.gui.SimulationControlEvent.Reset;
import edu.mit.sips.gui.SimulationControlListener;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.gui.UpdateListener;
import edu.mit.sips.hla.FederationConnection;
import edu.mit.sips.hla.SimAmbassador;

/**
 * The Class Simulator.
 */
public class Simulator implements SimulationControlListener {	
	private final Country country;
	
	private boolean autoOptimizeDistribution = true;
	private boolean autoOptimizeProductionAndDistribution = true;
	private final OptimizationOptions optimizationOptions = new OptimizationOptions();

	private final AtomicBoolean initialized = new AtomicBoolean(false);
	private final AtomicBoolean completed = new AtomicBoolean(false);
	private long startTime, endTime;
	private long time;

	private final FederationConnection connection = new FederationConnection();

	private transient SimAmbassador simAmbassador;

	private transient EventListenerList listenerList = new EventListenerList(); // mutable

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
	 * @param federateName the federate name
	 * @param country the country
	 */
	public Simulator(Country country) {
		// Validate country.
		if(country == null) {
			throw new IllegalArgumentException("Country cannot be null.");
		}
		this.country = country;
		
		try {
			simAmbassador = new SimAmbassador(this);
		} catch (RTIinternalError ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, 
					ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Adds the update listener.
	 *
	 * @param listener the listener
	 */
	public void addUpdateListener(UpdateListener listener) {
		listenerList.add(UpdateListener.class, listener);
	}
	
	/**
	 * Advance.
	 *
	 * @param duration the duration
	 */
	private void advance(long duration) {
		if(time + duration > endTime) {
			throw new IllegalArgumentException("Duration cannot exceed end time.");
		}
		if(!initialized.get()) {
			throw new IllegalStateException("Simulation must be initialized.");
		}
		
		long stopTime = Math.min(endTime + 1, time + duration);
		
		while(time < stopTime) {
			runAutoOptimization();
			
			try {
				simAmbassador.advance();
			} catch (NotConnected ignored) {
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			country.tick();
			fireUpdateEvent(time); // final update of current year
			time = time + 1;
			country.tock();
			if(time <= endTime) {
				fireUpdateEvent(time); // first update of next year
			}
		}
		
		if(time >= endTime) {
			completed.set(true);
			fireCompleteEvent(time);
			try {
				// TODO simAmbassador.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	 * Fire complete event.
	 *
	 * @param time the time
	 */
	public void fireCompleteEvent(long time) {
		System.out.println("Firing complete event with time " + time);
		for(UpdateListener listener 
				: listenerList.getListeners(UpdateListener.class)) {
			listener.simulationCompleted(new UpdateEvent(this, time, country));
		}
	}
	
	/**
	 * Fire initialize event.
	 *
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
	 * @param time the time
	 */
	public void fireUpdateEvent(long time) {
		System.out.println("Firing update event with time " + time);
		for(UpdateListener listener 
				: listenerList.getListeners(UpdateListener.class)) {
			listener.simulationUpdated(new UpdateEvent(this, time, country));
		}
	}
	
	/**
	 * Gets the ambassador.
	 *
	 * @return the ambassador
	 */
	public SimAmbassador getAmbassador() {
		return simAmbassador;
	}
	
	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public FederationConnection getConnection() {
		return connection;
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
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public long getEndTime() {
		return endTime;
	}
	
	/**
	 * Gets the optimization options.
	 *
	 * @return the optimization options
	 */
	public OptimizationOptions getOptimizationOptions() {
		return optimizationOptions;
	}
	
	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public long getStartTime() {
		return startTime;
	}
	
	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public long getTime() {
		return time;
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

		/* TODO
		if(simAmbassador.isInitialized()) {
			try {
				simAmbassador.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			simAmbassador.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		time = startTime;

		country.initialize(startTime);

		if(simAmbassador.isInitialized()) {
			try {
				simAmbassador.restoreInitialConditions();
			} catch (NotConnected ignored) {
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				simAmbassador.initialize(startTime);
			} catch (NotConnected ignored) {
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		runAutoOptimization();
		
		fireInitializeEvent();

		initialized.set(true);
		completed.set(false);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SimulationControlListener#initializeSimulation(edu.mit.sips.gui.SimulationControlEvent)
	 */
	@Override
	public synchronized void initializeSimulation(SimulationControlEvent.Initialize event) {
		initialize(event.getStartTime(), event.getEndTime());
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
	 * Checks if is auto optimize production and distribution.
	 *
	 * @return true, if is auto optimize production and distribution
	 */
	public boolean isAutoOptimizeProductionAndDistribution() {
		return autoOptimizeProductionAndDistribution;
	}

	/**
	 * Checks if is completed.
	 *
	 * @return true, if is completed
	 */
	public boolean isCompleted() {
		return completed.get();
	}
	
	/**
	 * Checks if is initialized.
	 *
	 * @return true, if is initialized
	 */
	public boolean isInitialized() {
		return initialized.get();
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
	 * Run auto optimization.
	 */
	private void runAutoOptimization() {
		if(autoOptimizeProductionAndDistribution) {
			if(country.getAgricultureSystem() instanceof AgricultureSoS.Local) {
				((AgricultureSoS.Local)country.getAgricultureSystem())
				.optimizeFoodProductionAndDistribution(optimizationOptions);
			}
			
			if(country.getWaterSystem() instanceof WaterSoS.Local) {
				((WaterSoS.Local)country.getWaterSystem())
				.optimizeWaterProductionAndDistribution(optimizationOptions);
			}
			
			if(country.getEnergySystem() instanceof EnergySoS.Local) {
				((EnergySoS.Local)country.getEnergySystem())
				.optimizeEnergyProductionAndDistribution(optimizationOptions);
			}
		} else if(autoOptimizeDistribution) {
			if(country.getAgricultureSystem() instanceof AgricultureSoS.Local) {
				((AgricultureSoS.Local)country.getAgricultureSystem())
				.optimizeFoodDistribution();
			}
			
			if(country.getWaterSystem() instanceof WaterSoS.Local) {
				((WaterSoS.Local)country.getWaterSystem())
				.optimizeWaterDistribution();
			}
			
			if(country.getEnergySystem() instanceof EnergySoS.Local) {
				((EnergySoS.Local)country.getEnergySystem())
				.optimizeEnergyDistribution();
			}
		}
	}

	/**
	 * Run optimization.
	 */
	public void runOptimization() {
		runAutoOptimization();
		fireUpdateEvent(time);
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
	 * Sets the auto optimize production and distribution.
	 *
	 * @param autoOptimizeProductionAndDistribution the new auto optimize production and distribution
	 */
	public void setAutoOptimizeProductionAndDistribution(
			boolean autoOptimizeProductionAndDistribution) {
		this.autoOptimizeProductionAndDistribution = autoOptimizeProductionAndDistribution;
	}
}
