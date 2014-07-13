package edu.mit.isos2;

import java.util.Date;

import org.apache.log4j.Logger;

import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;

public class Simulator {
	private static Logger logger = Logger.getLogger("edu.mit.isos");
	
	private final Scenario scenario;
	private final StateHistory history = new StateHistory();
	
	private int iterationsPerTimestep = 2;
	private boolean verifyFlow = true, verifyExchange = true, outputs = true;

	public Simulator(Scenario scenario) {
		this.scenario = scenario;
	}
	
	public void execute(long duration, long timeStep) {
		long startTime = new Date().getTime();
		
		long time = scenario.getInitialTime();

		for(SimEntity entity : scenario.getSimEntities()) {
			entity.initialize(scenario.getInitialTime());
		}
		
		if(outputs) {
			history.clear();
		}

		logger.info("Executing scenario " + scenario 
				+ " for duration " + duration 
				+ " with a timestep of " + timeStep 
				+ " and options {" 
				+ "iterationsPerTimestep: " + iterationsPerTimestep
				+ ", verifyFlow: " + verifyFlow 
				+ ", verifyExchange: " + verifyExchange
				+ ", outputs: " + outputs + "}.");
		
		while(time <= scenario.getInitialTime() + duration) {
			for(int i = 0; i < iterationsPerTimestep; i++) {
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
		}
		if(outputs) {
			logger.info("Simulation completed in "
					+ (new Date().getTime() - startTime) + " ms");

			history.displayOutputs(verifyFlow);
		}
	}
}