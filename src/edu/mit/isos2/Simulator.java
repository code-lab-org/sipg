package edu.mit.isos2;

import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import edu.mit.isos2.element.DefaultElement;
import edu.mit.isos2.element.DefaultState;
import edu.mit.isos2.element.Element;
import edu.mit.isos2.element.ExchangingState;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceType;

public class Simulator {
	private static Logger logger = Logger.getLogger("edu.mit.isos");
	
	private final Scenario scenario;
	private final StateHistory history = new StateHistory();
	
	private int iterationsPerTimestep = 2;
	private boolean verifyFlow = true, verifyExchange = true, outputs = true;
	
	public static void main(String[] args) {
		BasicConfigurator.configure();

		Node west = new Node("N1");
		Node east = new Node("N2");
		Location w = new Location(west);
		Location we = new Location(west, east);
		Location ew = new Location(east, west);
		Location e = new Location(east);
		
		final ExchangingState e1s = new ExchangingState("Default") {
			@Override
			public Resource getStored(long duration) {
				return getProduced(duration).get(ResourceType.WATER);
			}
			@Override
			public Resource getConsumed(long duration) {
				return ResourceFactory.create(ResourceType.AQUIFER, "0.1")
						.add(ResourceFactory.create(ResourceType.ELECTRICITY, "0.5")).multiply(duration);
			}
			@Override
			public Resource getProduced(long duration) {
				return ResourceFactory.create(ResourceType.WATER, "0.1").multiply(duration);
			}
			@Override
			public Resource getReceived(long duration) {
				return getConsumed(duration).get(ResourceType.ELECTRICITY);
			}
		};
		
		DefaultState e2s = new DefaultState("Default") {
			@Override
			public Resource getRetrieved(long duration) {
				return e1s.getConsumed(duration).get(ResourceType.AQUIFER);
			}
		};
		
		ExchangingState e3s = new ExchangingState("Default") {
			@Override
			public Resource getConsumed(long duration) {
				return getProduced(duration).get(ResourceType.ELECTRICITY)
						.swap(ResourceType.ELECTRICITY, ResourceType.OIL).multiply(0.75);
			}
			@Override
			public Resource getProduced(long duration) {
				return getSent(duration).get(ResourceType.ELECTRICITY);
			}
			@Override
			public Resource getReceived(long duration) {
				return getConsumed(duration).get(ResourceType.OIL);
			}
		};
		
		ExchangingState e4s = new ExchangingState("Default") {
			@Override
			public Resource getRetrieved(long duration) {
				return getSent(duration).get(ResourceType.OIL);
			}
		};
		

		Element e1 = new DefaultElement("Desal. Plant", w)
				.states(Arrays.asList(e1s)).initialState(e1s);
		Element e2 = new DefaultElement("Aquifer", w)
				.states(Arrays.asList(e2s)).initialState(e2s)
				.initialContents(ResourceFactory.create(ResourceType.AQUIFER, "100"));
		Element e3 = new DefaultElement("Power Plant", w)
				.states(Arrays.asList(e3s)).initialState(e3s);
		Element e4 = new DefaultElement("Fuel Tank", w)
				.states(Arrays.asList(e4s)).initialState(e4s)
				.initialContents(ResourceFactory.create(ResourceType.OIL, "1000"));
		
		// federation agreement		
		e1s.setSupplier(ResourceType.ELECTRICITY, e3);
		e3s.addCustomer(e1);
		
		e3s.setSupplier(ResourceType.OIL, e4);
		e4s.addCustomer(e3);
		
		Scenario scenario = new Scenario("Baseline", 0, Arrays.asList(w, we, e, ew), 
				Arrays.asList(e1, e2, e3, e4));
		
		Simulator sim = new Simulator(scenario);
		sim.execute(20, 2);
		
	}

	public Simulator(Scenario scenario) {
		this.scenario = scenario;
	}
	
	public void execute(long duration, long timeStep) {
		long startTime = new Date().getTime();
		
		long time = scenario.getInitialTime();

		for(Element element : scenario.getElements()) {
			element.initialize(scenario.getInitialTime());
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
				for(Element element : scenario.getElements()) {
					element.iterateTick(timeStep);
				}
				for(Element element : scenario.getElements()) {
					element.iterateTock();
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
			
			for(Element element : scenario.getElements()) {
				element.tick(timeStep);
			}
			for(Element element : scenario.getElements()) {
				element.tock();
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