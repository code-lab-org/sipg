package edu.mit.isos2;

import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import edu.mit.isos2.Resource.ResourceType;

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

		final ExchangingElement e1 = new ExchangingElement("Desal. Plant", w) {
			@Override
			public Resource getStorageRate() {
				return getConsumptionRate().get(ResourceType.AQUIFER);
			}
			@Override
			public Resource getConsumptionRate() {
				return new BigDecimalArrayResource(ResourceType.AQUIFER, "0.1")
						.add(new BigDecimalArrayResource(ResourceType.ELECTRICITY, "0.5"));
			}
			@Override
			public Resource getProductionRate() {
				return new BigDecimalArrayResource(ResourceType.WATER, "0.1");
			}
			@Override
			public Resource getReceivingRate() {
				return getConsumptionRate().get(ResourceType.ELECTRICITY);
			}
		};
		Element e2 = new DefaultElement("Aquifer", w) {
			@Override
			public Resource getRetrievalRate() {
				return e1.getConsumptionRate().get(ResourceType.AQUIFER);
			}
		}.initialContents(new BigDecimalArrayResource(ResourceType.AQUIFER, "100"));
		
		ExchangingElement e3 = new ExchangingElement("Power Plant", w) {
			@Override
			public Resource getConsumptionRate() {
				return getProductionRate().get(ResourceType.ELECTRICITY)
						.swap(ResourceType.ELECTRICITY, ResourceType.OIL).multiply(0.75);
			}
			@Override
			public Resource getProductionRate() {
				return getSendingRate();
			}
			@Override
			public Resource getReceivingRate() {
				return getConsumptionRate().get(ResourceType.OIL);
			}
		};
		
		ExchangingElement e4 = (ExchangingElement) new ExchangingElement("Fuel Tank", w)
			.initialContents(new BigDecimalArrayResource(ResourceType.OIL, "1000"));
		
		// federation agreement		
		e1.setSupplier(ResourceType.ELECTRICITY, e3);
		e3.addCustomer(e1);
		
		e3.setSupplier(ResourceType.OIL, e4);
		e4.addCustomer(e3);
		
		Scenario scenario = new Scenario("Baseline", 0, Arrays.asList(w, we, e, ew), 
				Arrays.asList(e1, e2, e3, e4));
		
		Simulator sim = new Simulator(scenario);
		long startTime = new Date().getTime();
		sim.execute(20, 2);
		logger.info("Simulation completed in "
				+ (new Date().getTime() - startTime) + " ms");
		
		if(sim.outputs) {
			sim.history.displayOutputs(sim.verifyFlow);
		}
		
	}

	public Simulator(Scenario scenario) {
		this.scenario = scenario;
	}
	
	public void execute(long duration, long timeStep) {
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
					element.iterateTick();
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
					Resource flowRate = new BigDecimalArrayResource();
					for(Element element : scenario.getElements()) {
						if(element.getLocation().equals(location)) {
							flowRate = flowRate.subtract(element.getStorageRate())
									.add(element.getRetrievalRate());
						}
						if(location.isNodal() && element.getLocation().getDestination().equals(location.getOrigin())) {
							flowRate = flowRate.add(element.getOutputRate());
						} 
						if(location.isNodal() && element.getLocation().getOrigin().equals(location.getOrigin())) {
							flowRate = flowRate.subtract(element.getInputRate());
						}
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
						if(!e1.getSendingRateTo(e2).equals(
								e2.getReceivingRateFrom(e1))) {
							logger.warn("@ t = " + time + ": Unbalanced resource exchange: " + 
									e1.getName() + "->" + e1.getSendingRateTo(e2) + "->" + e2.getName() + ", " + 
									e2.getName() + "<-" + e2.getReceivingRateFrom(e1) + "<-" + e1.getName());
						}
						if(!e1.getSendingRateTo(e2).isZero()
								&& !e1.getLocation().getDestination().equals(
										e2.getLocation().getOrigin())) {
							logger.warn("@ t = " + time + ": Incompatible resource exchange: " + 
									e1.getName() + " destination " + e1.getLocation().getDestination() + " =/= " + 
									e2.getName() + " origin " + e2.getLocation().getOrigin());
						}
						if(!e1.getReceivingRateFrom(e2).isZero()
								&& !e1.getLocation().getOrigin().equals(
										e2.getLocation().getDestination())) {
							logger.warn("@ t = " + time + ": Incompatible resource exchange: " + 
									e1.getName() + " origin " + e1.getLocation().getOrigin() + " =/= " + 
									e2.getName() + " destination " + e2.getLocation().getDestination());
						}
					}
				}
			}
			
			if(outputs) {
				for(Element element : scenario.getElements()) {
					history.logElement(time, element);
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
	}
}