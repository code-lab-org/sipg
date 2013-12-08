package edu.mit.isos;

import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Simulator {
	private static Logger logger = Logger.getLogger("edu.mit.isos");
	
	private final Scenario scenario;
	private final StateHistory history = new StateHistory();
	
	private int iterationsPerTimestep = 6;
	private boolean verifyFlow = false, verifyExchange = false, outputs = false;
	
	public static void main(String[] args) {
		BasicConfigurator.configure();

		Node west = new Node("N1");
		Node east = new Node("N2");
		Location w = new Location(west);
		Location we = new Location(west, east);
		Location e = new Location(east);

		HighwayElement highway = new HighwayElement(
				"Highway", we, new State("Default"));

		SocialSystemElement socialWest = new SocialSystemElement(
				"City West", w, new Resource(Resource.PEOPLE, "10"), new State("Default")) {
			@Override
			public Resource getTransformOutputs() {
				return new Resource(Resource.PEOPLE, "2");
			}
		};

		SocialSystemElement socialEast = new SocialSystemElement(
				"City East", e, new Resource(), new State("Default"));
		
		WaterSystemElement waterWest = new WaterSystemElement(
				"Water West", w, new Resource(Resource.AQUIFER, "3000"), new State("Default"));
		
		WaterSystemElement waterEast = new WaterSystemElement(
				"Water East", e, new Resource(Resource.AQUIFER, "2000"), new State("Default"));
		
		ElectricitySystemElement electWest = new ElectricitySystemElement(
				"Elect West", w, new State("Default"));
		
		ElectricitySystemElement electEast = new ElectricitySystemElement(
				"Elect East", e, new State("Default"));
		
		OilSystemElement oilWest = new OilSystemElement(
				"Oil West", w, new Resource(Resource.RESERVES, "5000"), new State("Default"));
		
		OilSystemElement oilEast = new OilSystemElement(
				"Oil East", e, new Resource(Resource.RESERVES, "6000"), new State("Default"));


		// federation agreement
		highway.setSocialSystem(socialWest);
		socialWest.setTransportOut(highway);
		socialEast.setTransportIn(highway);

		socialWest.setWaterSystem(waterWest);
		socialWest.setElectricitySystem(electWest);
		socialWest.setOilSystem(oilWest);
		waterWest.setSocialSystem(socialWest);
		waterWest.setElectricitySystem(electWest);
		oilWest.setSocialSystem(socialWest);
		oilWest.setElectricitySystem(electWest);
		electWest.setSocialSystem(socialWest);
		electWest.setWaterSystem(waterWest);
		electWest.setOilSystem(oilWest);
		
		socialEast.setWaterSystem(waterEast);
		socialEast.setOilSystem(oilEast);
		socialEast.setElectricitySystem(electEast);
		waterEast.setSocialSystem(socialEast);
		waterEast.setElectricitySystem(electEast);
		oilEast.setSocialSystem(socialEast);
		oilEast.setElectricitySystem(electEast);
		electEast.setSocialSystem(socialEast);
		electEast.setWaterSystem(waterEast);
		electEast.setOilSystem(oilEast);
		
		Scenario scenario = new Scenario("Baseline", 0, Arrays.asList(w, we, e), 
				Arrays.asList(socialWest, highway, socialEast, waterWest, 
						waterEast, electWest, electEast, oilWest, oilEast));
		
		Simulator sim = new Simulator(scenario);
		long startTime = new Date().getTime();
		sim.execute(20, 1);
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
					element.miniTick();
				}
				for(Element element : scenario.getElements()) {
					element.miniTock();
				}
			}
			if(outputs) {
				history.log(time);
			}
			
			if(verifyFlow) {
				for(Location location : scenario.getLocations()) {
					Resource outflow = new Resource();
					Resource inflow = new Resource();
					for(Element element : scenario.getElements()) {
						if(element.getLocation().equals(location)) {
							inflow = inflow.add(element.getTransformOutputs())
									.add(element.getStoreOutputs());
						}
						if(element.getLocation().equals(location)) {
							outflow = outflow.add(element.getTransformInputs())
									.add(element.getStoreInputs());
						}
						if(location.isNodal() && element.getLocation().getDestination().equals(location.getOrigin())) {
							inflow = inflow.add(element.getTransportOutputs())
									.add(element.getExchangeOutputs());
						} 
						if(location.isNodal() && element.getLocation().getOrigin().equals(location.getOrigin())) {
							outflow = outflow.add(element.getTransportInputs())
									.add(element.getExchangeInputs());
						}
					}
					if(outputs) {
						history.logInflow(time, location, inflow);
						history.logOutflow(time, location, outflow);
					}
					if(!outflow.equals(inflow)) {
						logger.warn(location + " @ t = " + time + 
								": Inflow does not equal outflow: " + 
								inflow + "->" + location + "->" + outflow);
					}
				}
			}
			
			if(verifyExchange) {
				for(Element e1 : scenario.getElements()) {
					for(Element e2 : scenario.getElements()) {
						if(!e1.getExchangeTo(e2).equals(e2.getExchangeFrom(e1))) {
							logger.warn("@ t = " + time + ": Exchange is not equal: " + 
									e1.getName() + "->" + e1.getExchangeTo(e2) + "->" + e2.getName() + ", " + 
									e2.getName() + "<-" + e2.getExchangeFrom(e1) + "<-" + e1.getName());
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