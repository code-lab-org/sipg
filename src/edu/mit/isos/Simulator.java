package edu.mit.isos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulator {
	private final static int NUM_SUB_STEPS = 6;
	private final static boolean verifyFlow = false, verifyExchange = false, log = true, output = false;
	
	public static void main(String[] args) {
		Simulator s = new Simulator();

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
		s.locations.addAll(Arrays.asList(w, we, e));

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
		
		s.elements.addAll(Arrays.asList(socialWest, highway, socialEast, waterWest, waterEast, electWest, electEast, oilWest, oilEast));
		
		long startTime = new Date().getTime();
		s.execute(0, 100);
		System.out.println("Simulation completed in " + (new Date().getTime() - startTime) +" ms");
		if(log) {
			s.postProcessOutput();
		}
		
	}
	private List<Location> locations = new ArrayList<Location>();

	private List<Element> elements = new ArrayList<Element>();
	private transient long initialTime, duration;
	private transient Map<Long,Map<Location, Resource>> inflowHistory = 
			new HashMap<Long, Map<Location, Resource>>();
	private transient Map<Long,Map<Location, Resource>> outflowHistory = 
			new HashMap<Long, Map<Location, Resource>>();
	private transient Map<Long,Map<String, State>> stateHistory = 
			new HashMap<Long, Map<String, State>>();
	private transient Map<Long,Map<String, Location>> locationHistory = 
			new HashMap<Long, Map<String, Location>>();
	private transient Map<Long,Map<String,Resource>> stockHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private transient Map<Long,Map<String,Resource>> transformInHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private transient Map<Long,Map<String,Resource>> transformOutHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private transient Map<Long,Map<String,Resource>> storeInHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private transient Map<Long,Map<String,Resource>> storeOutHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private transient Map<Long,Map<String,Resource>> transportInHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private transient Map<Long,Map<String,Resource>> transportOutHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private transient Map<Long,Map<String,Resource>> exchangeInHistory = 
			new HashMap<Long, Map<String, Resource>>();
	
	
	private transient Map<Long,Map<String,Resource>> exchangeOutHistory = 
			new HashMap<Long, Map<String, Resource>>();

	public Simulator() { }
	
	public void execute(long initialTime, long duration) {
		this.initialTime = initialTime;
		this.duration = duration;
		
		long time = initialTime;

		for(Element element : elements) {
			element.initialize(initialTime);
		}
		
		if(log) {
			inflowHistory.clear();
			outflowHistory.clear();
			locationHistory.clear();
			stateHistory.clear();
			stockHistory.clear();
			transformInHistory.clear();
			transformOutHistory.clear();
			storeInHistory.clear();
			storeOutHistory.clear();
			transportInHistory.clear();
			transportOutHistory.clear();
			exchangeInHistory.clear();
			exchangeOutHistory.clear();
		}
		
		if(output) {
			System.out.println("Time = " + time);
			for(Element element : elements) {
				System.out.println(element);
			}
		}
		
		while(time <= initialTime + duration) {
			for(int i = 0; i < NUM_SUB_STEPS; i++) {
				for(Element element : elements) {
					element.miniTick();
				}
				for(Element element : elements) {
					element.miniTock();
				}
			}
			
			if(verifyFlow) {
				if(log) {
					inflowHistory.put(time, new HashMap<Location,Resource>());
					outflowHistory.put(time, new HashMap<Location,Resource>());
				}
				for(Location location : locations) {
					Resource outflow = new Resource();
					Resource inflow = new Resource();
					for(Element element : elements) {
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
					if(log) {
						inflowHistory.get(time).put(location, inflow);
						outflowHistory.get(time).put(location, outflow);
					}
					if(!outflow.equals(inflow)) {
						System.err.println(location + " @ t = " + time + 
								": Inflow does not equal outflow: " + 
								inflow + "->" + location + "->" + outflow);
					}
				}
			}
			
			if(verifyExchange) {
				for(Element e1 : elements) {
					for(Element e2 : elements) {
						if(!e1.getExchangeTo(e2).equals(e2.getExchangeFrom(e1))) {
							System.err.println("@ t = " + time + ": Exchange is not equal: " + 
									e1.getName() + "->" + e1.getExchangeTo(e2) + "->" + e2.getName() + ", " + 
									e2.getName() + "<-" + e2.getExchangeFrom(e1) + "<-" + e1.getName());
						}
					}
				}
			}
			
			if(log) {
				log(time);
			}
			
			if(output) {
				System.out.println("Time = " + time);
				for(Element element : elements) {
					System.out.println(element);
				}
			}
			
			for(Element element : elements) {
				element.tick(1);
			}
			for(Element element : elements) {
				element.tock();
			}
			time = time + 1;
		}
	}

	private void log(long time) {
		locationHistory.put(time, new HashMap<String,Location>());
		stateHistory.put(time, new HashMap<String,State>());
		stockHistory.put(time, new HashMap<String,Resource>());
		transformInHistory.put(time, new HashMap<String,Resource>());
		transformOutHistory.put(time, new HashMap<String,Resource>());
		storeInHistory.put(time, new HashMap<String,Resource>());
		storeOutHistory.put(time, new HashMap<String,Resource>());
		transportInHistory.put(time, new HashMap<String,Resource>());
		transportOutHistory.put(time, new HashMap<String,Resource>());
		exchangeInHistory.put(time, new HashMap<String,Resource>());
		exchangeOutHistory.put(time, new HashMap<String,Resource>());
		
		for(Element element : elements) {
			locationHistory.get(time).put(element.getName(), element.getLocation());
			stateHistory.get(time).put(element.getName(), element.getState());
			stockHistory.get(time).put(element.getName(), element.getStock());
			transformInHistory.get(time).put(element.getName(), element.getTransformInputs());
			transformOutHistory.get(time).put(element.getName(), element.getTransformOutputs());
			storeInHistory.get(time).put(element.getName(), element.getStoreInputs());
			storeOutHistory.get(time).put(element.getName(), element.getStoreOutputs());
			transportInHistory.get(time).put(element.getName(), element.getTransportInputs());
			transportOutHistory.get(time).put(element.getName(), element.getTransportOutputs());
			exchangeInHistory.get(time).put(element.getName(), element.getExchangeInputs());
			exchangeOutHistory.get(time).put(element.getName(), element.getExchangeOutputs());
		}
	}
	
	private void postProcessOutput() {
		for(long time = initialTime; time <= initialTime + duration; time+=5) {
			System.out.println("Time = " + time);
			System.out.println(String.format("%-19s %-9s %-9s %-59s %-59s %-59s %-59s %-59s", 
					"Element", "State", "Location", "Stock", "Exchange-in", "Exchange-out", "Transform-in", "Transform-out"));
			for(String element : stockHistory.get(time).keySet()) {
				
				System.out.println(String.format("%-19s %-9s %-9s %-59s %-59s %-59s %-59s %-59s", 
						element, 
						stateHistory.get(time).get(element),
						locationHistory.get(time).get(element), 
						stockHistory.get(time).get(element), 
						exchangeInHistory.get(time).get(element), 
						exchangeOutHistory.get(time).get(element), 
						transformInHistory.get(time).get(element), 
						transformOutHistory.get(time).get(element)));
				
			}
			System.out.println();
			
			if(verifyFlow) {
				System.out.println(String.format("%-19s %-59s %-59s", 
						"Location", "In-flow", "Out-flow"));
				for(Location location : inflowHistory.get(time).keySet()) {
					System.out.println(String.format("%-19s %-59s %-59s", 
							location, 
							inflowHistory.get(time).get(location), 
							outflowHistory.get(time).get(location)));
				}
				System.out.println();
			}
		}
	}
}