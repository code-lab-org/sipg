package edu.mit.isos2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateHistory {
	private final List<Long> timeHistory = new ArrayList<Long>();
	private final Map<Long,Map<Location, Resource>> flowHistory = 
			new HashMap<Long, Map<Location, Resource>>();
	private final Map<Long,Map<String, State>> stateHistory = 
			new HashMap<Long, Map<String, State>>();
	private final Map<Long,Map<String, String>> parentHistory = 
			new HashMap<Long, Map<String, String>>();
	private final Map<Long,Map<String, Location>> locationHistory = 
			new HashMap<Long, Map<String, Location>>();
	private final Map<Long,Map<String,Resource>> contentsHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> consumedHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> producedHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> storedHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> retrievedHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> inputHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> outputHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> sentHistory = 
			new HashMap<Long, Map<String, Resource>>();
	private final Map<Long,Map<String,Resource>> receivedHistory = 
			new HashMap<Long, Map<String, Resource>>();


	public void clear() {
		timeHistory.clear();
		flowHistory.clear();
		parentHistory.clear();
		locationHistory.clear();
		stateHistory.clear();
		contentsHistory.clear();
		consumedHistory.clear();
		producedHistory.clear();
		storedHistory.clear();
		retrievedHistory.clear();
		inputHistory.clear();
		outputHistory.clear();
		sentHistory.clear();
		receivedHistory.clear();
	}

	public void log(long time) {
		flowHistory.put(time, new HashMap<Location,Resource>());

		timeHistory.add(time);
		parentHistory.put(time, new HashMap<String,String>());
		locationHistory.put(time, new HashMap<String,Location>());
		stateHistory.put(time, new HashMap<String,State>());
		contentsHistory.put(time, new HashMap<String,Resource>());
		consumedHistory.put(time, new HashMap<String,Resource>());
		producedHistory.put(time, new HashMap<String,Resource>());
		storedHistory.put(time, new HashMap<String,Resource>());
		retrievedHistory.put(time, new HashMap<String,Resource>());
		inputHistory.put(time, new HashMap<String,Resource>());
		outputHistory.put(time, new HashMap<String,Resource>());
		sentHistory.put(time, new HashMap<String,Resource>());
		receivedHistory.put(time, new HashMap<String,Resource>());
	}

	public void logFlow(long time, Location location, Resource flow) {
		flowHistory.get(time).put(location, flow);
	}
	
	public void logElement(long time, Element element) {
		locationHistory.get(time).put(element.getName(), element.getLocation());
		stateHistory.get(time).put(element.getName(), element.getState());
		contentsHistory.get(time).put(element.getName(), element.getContents());
		parentHistory.get(time).put(element.getName(), element.getParent().getName());
		consumedHistory.get(time).put(element.getName(), element.getConsumptionRate());
		producedHistory.get(time).put(element.getName(), element.getProductionRate());
		storedHistory.get(time).put(element.getName(), element.getStorageRate());
		retrievedHistory.get(time).put(element.getName(), element.getRetrievalRate());
		inputHistory.get(time).put(element.getName(), element.getInputRate());
		outputHistory.get(time).put(element.getName(), element.getOutputRate());
		sentHistory.get(time).put(element.getName(), element.getSendingRate());
		receivedHistory.get(time).put(element.getName(), element.getReceivingRate());
	}

	public void displayOutputs(boolean flowOutputs) {
		for(long time : timeHistory) {
			System.out.println("Time = " + time);
			System.out.println(String.format("%-19s %-9s %-9s %-19s %-59s %-59s %-59s %-59s %-59s", 
					"Element", "State", "Location", "Parent", "Contents", "Sending Rate", "Receiving Rate", "Consumption Rate", "Production Rate"));
			for(String element : contentsHistory.get(time).keySet()) {

				System.out.println(String.format("%-19s %-9s %-9s %-19s %-59s %-59s %-59s %-59s %-59s", 
						element, 
						stateHistory.get(time).get(element),
						locationHistory.get(time).get(element), 
						parentHistory.get(time).get(element),
						contentsHistory.get(time).get(element), 
						sentHistory.get(time).get(element), 
						receivedHistory.get(time).get(element), 
						consumedHistory.get(time).get(element), 
						producedHistory.get(time).get(element)));
			}
			System.out.println();

			if(flowOutputs) {
				System.out.println(String.format("%-19s %-59s", 
						"Location", "Flow Rate"));
				for(Location location : flowHistory.get(time).keySet()) {
					System.out.println(String.format("%-19s %-59s", 
							location, 
							flowHistory.get(time).get(location)));
				}
				System.out.println();
			}
		}
	}
}
