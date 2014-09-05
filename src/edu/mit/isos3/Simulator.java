package edu.mit.isos3;

import hla.rti1516e.exceptions.RTIexception;

import java.util.Date;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.event.SimulationTimeEvent;
import edu.mit.isos3.event.SimulationTimeListener;
import edu.mit.isos3.hla.ISOSambassador;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;

public class Simulator {
	private static Logger logger = Logger.getLogger("edu.mit.isos");
	
	private final Scenario scenario;
	private final StateHistory history = new StateHistory();
	private EventListenerList listeners = new EventListenerList();
	
	private boolean verifyFlow = false, verifyExchange = false, outputs = false;

	public Simulator(Scenario scenario) {
		this.scenario = scenario;
	}
	
	public void setVerifyFlow(boolean verifyFlow) {
		this.verifyFlow = verifyFlow;
	}
	
	public void setVerifyExchange(boolean verifyExchange) {
		this.verifyExchange = verifyExchange;
	}
	
	public void setOutputs(boolean outputs) {
		this.outputs = outputs;
	}
	
	public Scenario getScenario() {
		return scenario;
	}
	
	public void addSimulationTimeListener(SimulationTimeListener listener) {
		listeners.add(SimulationTimeListener.class, listener);
	}
	
	public void removeSimulationTimeListener(SimulationTimeListener listener) {
		listeners.remove(SimulationTimeListener.class, listener);
	} 
	
	private void fireTimeAdvanced(long time, long duration) {
		SimulationTimeListener[] listeners = this.listeners.getListeners(
				SimulationTimeListener.class);
		for(int i = 0; i < listeners.length; i++) {
			listeners[i].timeAdvanced(new SimulationTimeEvent(this, time, duration));
		}
	}
	
	public long initialize(ISOSambassador amb, String federateName, 
			long timeStep, int iterations) throws RTIexception {
		long startTime = new Date().getTime();
		for(SimEntity entity : scenario.getElements()) {
			entity.initialize(scenario.getInitialTime());
		}
		amb.initialize(scenario, iterations, timeStep);
		return startTime;
	}

	public long execute(ISOSambassador amb, String federateName, 
			long duration, long timeStep, int iterations) throws RTIexception {
		long startTime = new Date().getTime();
		long time = scenario.getInitialTime();
		
		if(outputs) {
			history.clear();
		}

		logger.info("Executing scenario " + scenario 
				+ " for duration " + duration 
				+ " with a timestep of " + timeStep 
				+ " and " + iterations + " iterations" 
				+ " and options {" 
				+ "verifyFlow: " + verifyFlow 
				+ ", verifyExchange: " + verifyExchange
				+ ", outputs: " + outputs + "}.");
		
		while(time <= scenario.getInitialTime() + duration) {
			amb.advance();
			
			if(outputs) {
				history.log(time);
			}
			
			if(verifyFlow) {
				for(Location location : scenario.getLocations()) {
					Resource flowRate = ResourceFactory.create();
					for(LocalElement element : scenario.getElements()) {
						flowRate = flowRate.add(element.getNetFlow(location, timeStep));
					}
					if(outputs) {
						history.logFlow(time, location, flowRate);
					}
					if(!flowRate.isZero()) {
						logger.warn(location + " @ t = " + time + 
								": Non-zero flow rate at " + location + ": " 
								+ flowRate);
					}
				}
			}

			if(verifyExchange) {
				for(LocalElement e1 : scenario.getElements()) {
					for(LocalElement e2 : scenario.getElements()) {
						Resource e12 = e1.getNetExchange(e2, timeStep);
						Resource e21 = e2.getNetExchange(e1, timeStep);

						if(!e12.equals(e21.negate())) {
							/*
							logger.warn("@ t = " + time + ": Unbalanced resource exchange: " + 
									e1.getName() + "->" + e12 + "->" + e2.getName() + ", " + 
									e2.getName() + "->" + e21 + "->" + e1.getName());
							*/
							logger.warn("@ t = " + time + ": Unbalanced resource exchange: " + 
									e1.getName() + "<->"  + e2.getName() + ", delta=" +
									e12.add(e21) + ", error=" + (e12.add(e21)).safeDivide(e12));
						}
					}
				}
			}
			
			if(outputs) {
				for(LocalElement element : scenario.getElements()) {
					history.logElement(time, timeStep, element);
				}
			}
			
			logger.trace("Simulation time is " + time + ".");
			
			for(SimEntity entity : scenario.getElements()) {
				entity.tick(timeStep);
			}
			fireTimeAdvanced(time, timeStep);
			for(SimEntity entity : scenario.getElements()) {
				entity.tock();
			}
			time = time + timeStep;
		}
		return startTime;
	}
}