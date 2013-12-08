package edu.mit.isos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateHistory {
	private final List<Long> timeHistory = new ArrayList<Long>();
	private final Map<Long,Map<Location, Resource>> inflowHistory = 
			new HashMap<Long, Map<Location, Resource>>();
	private final Map<Long,Map<Location, Resource>> outflowHistory = 
			new HashMap<Long, Map<Location, Resource>>();
	private final Map<Long,Map<String, State>> stateHistory = 
			new HashMap<Long, Map<String, State>>();
	private final Map<Long,Map<String, Location>> locationHistory = 
			new HashMap<Long, Map<String, Location>>();
	private final Map<Long,Map<String,Resource>> stockHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> transformInHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> transformOutHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> storeInHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> storeOutHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> transportInHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> transportOutHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> exchangeInHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> exchangeOutHistory = 
			new HashMap<Long, Map<String, Resource>>();

	public void clear() {
		timeHistory.clear();
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

	public void log(long time) {
		inflowHistory.put(time, new HashMap<Location,Resource>());
		outflowHistory.put(time, new HashMap<Location,Resource>());

		timeHistory.add(time);
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
	}

	public void logInflow(long time, Location location, Resource inflow) {
		inflowHistory.get(time).put(location, inflow);
	}

	public void logOutflow(long time, Location location, Resource outflow) {
		outflowHistory.get(time).put(location, outflow);
	}

	public void logElement(long time, Element element) {
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

	public void displayOutputs(boolean flowOutputs) {
		for(long time : timeHistory) {
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

			if(flowOutputs) {
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
