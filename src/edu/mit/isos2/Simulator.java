package edu.mit.isos2;

import java.util.Date;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import edu.mit.isos2.element.Element;
import edu.mit.isos2.event.SimulationTimeEvent;
import edu.mit.isos2.event.SimulationTimeListener;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class Simulator {
	private static Logger logger = Logger.getLogger("edu.mit.isos");
	
	private final Scenario scenario;
	private final StateHistory history = new StateHistory();
	private EventListenerList listeners = new EventListenerList();
	
	private boolean verifyFlow = false, verifyExchange = false, outputs = false;

	public Simulator(Scenario scenario) {
		this.scenario = scenario;
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
	
	private void fireTimeAdvanced(long time) {
		SimulationTimeListener[] listeners = this.listeners.getListeners(
				SimulationTimeListener.class);
		for(int i = 0; i < listeners.length; i++) {
			listeners[i].timeAdvanced(new SimulationTimeEvent(this, time));
		}
	}
	
	public long execute(long duration, long timeStep, int iterations) {
		long startTime = new Date().getTime();
		
		long time = scenario.getInitialTime();

		for(SimEntity entity : scenario.getSimEntities()) {
			entity.initialize(scenario.getInitialTime());
		}
		
		fireTimeAdvanced(time);
		
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
		
		while(time < scenario.getInitialTime() + duration) {
			for(int i = 0; i < iterations; i++) {
				for(SimEntity entity : scenario.getSimEntities()) {
					entity.iterateTick(timeStep);
				}
				for(SimEntity entity : scenario.getSimEntities()) {
					entity.iterateTock();
				}
			}
			
			if(outputs) {
				history.log(time);
			}
			
			if(verifyFlow) {
				for(Location location : scenario.getLocations()) {
					Resource flowRate = ResourceFactory.create();
					for(Element element : scenario.getElements()) {
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
				for(Element e1 : scenario.getElements()) {
					for(Element e2 : scenario.getElements()) {
						Resource e12 = e1.getNetExchange(e2, timeStep);
						Resource e21 = e2.getNetExchange(e1, timeStep);

						if(!e12.equals(e21.negate())) {
							logger.warn("@ t = " + time + ": Unbalanced resource exchange: " + 
									e1.getName() + "->" + e12 + "->" + e2.getName() + ", " + 
									e2.getName() + "->" + e21 + "->" + e1.getName());
						}
					}
				}
			}
			
			if(outputs) {
				for(Element element : scenario.getElements()) {
					history.logElement(time, timeStep, element);
				}
			}
			
			logger.trace("Simulation time is " + time + ".");
			
			for(SimEntity entity : scenario.getSimEntities()) {
				entity.tick(timeStep);
			}
			for(SimEntity entity : scenario.getSimEntities()) {
				entity.tock();
			}
			time = time + timeStep;
			fireTimeAdvanced(time);
		}
		long executionTime = new Date().getTime() - startTime;
		if(outputs) {
			logger.info("Simulation completed in "
					+ executionTime + " ms");
			history.displayOutputs(verifyFlow);
		}
		return executionTime;
	}
}