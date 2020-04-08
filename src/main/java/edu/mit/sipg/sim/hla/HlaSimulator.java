package edu.mit.sipg.sim.hla;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import edu.mit.sipg.scenario.Scenario;
import edu.mit.sipg.sim.DefaultSimulator;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIexception;

/**
 * The Class HLA Simulator.
 */
public class HlaSimulator extends DefaultSimulator {	
	private static Logger logger = Logger.getLogger(HlaSimulator.class);

	private transient HlaFederateAmbassador simAmbassador;
	
	/**
	 * Instantiates a new simulator.
	 *
	 * @param scenario the scenario
	 */
	public HlaSimulator(Scenario scenario) {
		super(scenario, new HlaConnection());
		
		logger.trace("Creating federate ambassador.");
		try {
			simAmbassador = new HlaFederateAmbassador(this, numberIterations*250, numberIterations);
		} catch (RTIexception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void setScenario(Scenario scenario) {
		super.setScenario(scenario);
		try {
			simAmbassador.disconnect();
		} catch (RTIexception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		simAmbassador.reset();
	}

	@Override
	protected void advance(long duration) {
		logger.debug("Advancing simulation with duration " + duration + ".");
		
		if(time + duration > endTime) {
			throw new IllegalArgumentException("Duration cannot exceed end time.");
		}
		if(!initialized.get()) {
			throw new IllegalStateException("Simulation must be initialized.");
		}
		
		long stopTime = Math.min(endTime, time + duration);
		
		while(time <= stopTime) {
			try {
				simAmbassador.advance();
			} catch (NotConnected ignored) {
				logger.warn("Not connected: continuing in offline mode.");
			} catch (RTIexception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
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
		
		fireInitializeEvent();

		initialized.set(true);
		completed.set(false);
	}

	@Override
	public void connect() {
		try {
			simAmbassador.connect();
		} catch (MalformedURLException | RTIexception e) {
			logger.error(e);
		}
	}

	@Override
	public void disconnect() {
		try {
			simAmbassador.disconnect();
		} catch (RTIexception e) {
			logger.error(e);
		}
	}
}
