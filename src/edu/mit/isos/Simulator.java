package edu.mit.isos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulator {
	private final boolean log = true, logOutput = false;
	private List<Location> locations = new ArrayList<Location>();
	private List<Element> elements = new ArrayList<Element>();

	private transient long initialTime, duration;
	private transient Map<Long,Map<String,Resource>> stockHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private transient Map<Long,Map<String, State>> stateHistory = 
			new HashMap<Long, Map<String, State>>();
	private transient Map<Long,Map<String, Location>> locationHistory = 
			new HashMap<Long, Map<String, Location>>();

	public static void main(String[] args) {
		Simulator s = new Simulator();

		Node west = new Node("N1");
		Node east = new Node("N2");
		Location w = new Location(west);
		Location we = new Location(west, east);
		Location e = new Location(east);

		s.locations.addAll(Arrays.asList(w, we, e));

		final Element socialWestEast = new Element("Highway", we, new Resource(), new State("Default")) {
			@Override
			public Resource getTransportInputs() {
				return new Resource(4, "0.5"); // send people
			}
			@Override
			public Resource getTransformInputs() {
				return new Resource(4, "0.05"); // consume people
			}
			@Override
			public Resource getTransportOutputs() {
				return getTransportInputs().subtract(getTransformInputs()); // receive people
			}
		};

		final Element socialWest = new Element("West Coast", w, new Resource(4, "10"), new State("Default")) {
			@Override
			public Resource getTransformOutputs() {
				return new Resource(4, "2"); // produce people
			}
			@Override
			public Resource getStoreInputs() {
				return getTransformOutputs().subtract(socialWestEast.getTransportInputs()); // store received people
			}
			@Override
			public Resource getTransformInputs() {
				return getStock().swap(4, 1).multiply(new Resource(1,"0.25")); // consume water
			}
			@Override
			public Resource getExchangeOutputs() {
				return getTransformInputs(); // demand water
			}
		};

		final Element socialEast = new Element("East Coast", e, new Resource(), new State("Default")) {
			@Override
			public Resource getStoreInputs() {
				return socialWestEast.getTransportOutputs(); // store received people
			}
			@Override
			public Resource getTransformInputs() {
				return getStock().swap(4, 1).multiply(new Resource(1,"0.25")); // consume water
			}
			@Override
			public Resource getExchangeOutputs() {
				return getTransformInputs(); // demand water
			}
		};
		
		final Element waterWest = new Element("West Water", e, new Resource(0, "3000"), new State("Default")) {
			@Override
			public Resource getStoreOutputs() {
				return getTransformInputs(); // retrieve aquifer
			}
			@Override
			public Resource getTransformInputs() {
				return getTransformOutputs().get(1).swap(1, 0); // consume aquifer
			}
			@Override
			public Resource getTransformOutputs() {
				return getExchangeInputs(); // produce water
			}
			@Override
			public Resource getExchangeInputs() {
				return socialWest.getTransformInputs().get(1); // satisfy water demand
			}
		};
		
		final Element waterEast = new Element("East Water", e, new Resource(0, "2000"), new State("Default")) {
			@Override
			public Resource getStoreOutputs() {
				return getTransformInputs(); // retrieve aquifer
			}
			@Override
			public Resource getTransformInputs() {
				return getTransformOutputs().get(1).swap(1, 0); // consume aquifer
			}
			@Override
			public Resource getTransformOutputs() {
				return getExchangeInputs(); // produce water
			}
			@Override
			public Resource getExchangeInputs() {
				return socialEast.getTransformInputs().get(1); // satisfy water demand
			}
		};
		
		s.elements.addAll(Arrays.asList(socialWest, socialWestEast, socialEast, waterWest, waterEast));

		long startTime = new Date().getTime();
		s.execute(0, 100);
		System.out.println("Simulation completed in " + (new Date().getTime() - startTime) +" ms");
		s.postProcessOutput();
	}

	public void execute(long initialTime, long duration) {
		this.initialTime = initialTime;
		this.duration = duration;
		
		long time = initialTime;

		for(Element element : elements) {
			element.initialize(initialTime);
		}
		
		if(log) {
			stockHistory.clear();
			locationHistory.clear();
			stateHistory.clear();
			stockHistory.put(time, new HashMap<String,Resource>());
			locationHistory.put(time, new HashMap<String,Location>());
			stateHistory.put(time, new HashMap<String,State>());
			for(Element element : elements) {
				stockHistory.get(time).put(element.getName(), element.getStock());
				locationHistory.get(time).put(element.getName(), element.getLocation());
				stateHistory.get(time).put(element.getName(), element.getState());
			}
		}
		if(logOutput) {
			System.out.println("Time = " + time);
			for(Element element : elements) {
				System.out.println(element);
			}
		}

		while(time < initialTime + duration) {
			for(Location location : locations) {
				Resource outflow = new Resource();
				Resource inflow = new Resource();
				for(Element element : elements) {
					if(element.getLocation().equals(location)) {
						inflow = inflow.add(element.getTransformOutputs())
								.add(element.getStoreOutputs());
					}
					if(location.isNodal() && element.getLocation().getDestination().equals(location.getOrigin())) {
						inflow = inflow.add(element.getTransportOutputs())
								.add(element.getExchangeOutputs());
					} else if(!location.isNodal() && element.getLocation().equals(location)) {
						inflow = inflow.add(element.getTransportInputs())
								.add(element.getExchangeInputs());
					}
					if(element.getLocation().equals(location)) {
						outflow = outflow.add(element.getTransformInputs())
								.add(element.getStoreInputs());
					}
					if(location.isNodal() && element.getLocation().getOrigin().equals(location.getOrigin())) {
						outflow = outflow.add(element.getTransportInputs())
								.add(element.getExchangeInputs());
					} else if(!location.isNodal() && element.getLocation().equals(location)) {
						outflow = outflow.add(element.getTransportOutputs())
								.add(element.getExchangeOutputs());
					}
				}
				if(!outflow.equals(inflow)) {
					System.out.println(inflow.amount + " " + outflow.amount);
					throw new RuntimeException("Inflow does not equal outflow: " + 
							inflow + "->" + location + "->" + outflow);
				}
			}
			for(Element element : elements) {
				element.tick(1);
			}
			for(Element element : elements) {
				element.tock();
			}
			time = time + 1;
			if(log) {
				stockHistory.put(time, new HashMap<String,Resource>());
				locationHistory.put(time, new HashMap<String,Location>());
				stateHistory.put(time, new HashMap<String,State>());
				for(Element element : elements) {
					stockHistory.get(time).put(element.getName(), element.getStock());
					locationHistory.get(time).put(element.getName(), element.getLocation());
					stateHistory.get(time).put(element.getName(), element.getState());
				}
			}
			if(logOutput) {
				System.out.println("Time = " + time);
				for(Element element : elements) {
					System.out.println(element);
				}
			}
		}
	}
	
	private void postProcessOutput() {
		for(long time = initialTime; time <= initialTime + duration; time++) {
			System.out.println("Time = " + time);
			System.out.println(String.format("%-19s %-9s %-9s %-34s", 
					"Element", "State", "Location", "Stock"));
			for(String element : stockHistory.get(time).keySet()) {
				
				System.out.println(String.format("%-19s %-9s %-9s %-34s", 
						element, 
						stateHistory.get(time).get(element),
						locationHistory.get(time).get(element), 
						stockHistory.get(time).get(element)));
				
			}
			System.out.println();
		}
	}
}