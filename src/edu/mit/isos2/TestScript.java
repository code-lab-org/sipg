package edu.mit.isos2;

import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;

import edu.mit.isos2.element.DefaultElement;
import edu.mit.isos2.element.Element;
import edu.mit.isos2.element.ElementFactory;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceMatrix;
import edu.mit.isos2.resource.ResourceType;
import edu.mit.isos2.state.DefaultState;
import edu.mit.isos2.state.ExchangingState;

public class TestScript {
	public static void main(String[] args) {
		BasicConfigurator.configure();
		Simulator sim = new Simulator(buildBaselineScenario());
		sim.execute(50, 2);
	}
	
	public static Scenario buildBaselineScenario() {
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
		

		DefaultElement e1 = new DefaultElement("Desal. Plant", w)
				.states(Arrays.asList(e1s)).initialState(e1s);
		DefaultElement e2 = new DefaultElement("Aquifer", w)
				.states(Arrays.asList(e2s)).initialState(e2s)
				.initialContents(ResourceFactory.create(ResourceType.AQUIFER, "100"));
		DefaultElement e3 = new DefaultElement("Power Plant", w)
				.states(Arrays.asList(e3s)).initialState(e3s);
		DefaultElement e4 = new DefaultElement("Fuel Tank", w)
				.states(Arrays.asList(e4s)).initialState(e4s)
				.initialContents(ResourceFactory.create(ResourceType.OIL, "1000"));
		
		/*
		
		SystemElement waterW = new SystemElement("W Water", w, 
				Arrays.asList(e1, e2));
		SystemElement waterE = new SystemElement("E Water", w, 
				new ArrayList<Element>());
		SuperSystem waterSystem = new SuperSystem("Water", 
				Arrays.asList(waterW, waterE));

		SystemElement electW = new SystemElement("W Elect", w,
				Arrays.asList(e3));
		SystemElement electE = new SystemElement("E Elect", e,
				new ArrayList<Element>());
		SuperSystem electSystem = new SuperSystem("Elect",
				Arrays.asList(electW, electE));
		
		SystemElement oilW = new SystemElement("W Oil", w, 
				Arrays.asList(e4));
		SystemElement oilE = new SystemElement("E Oil", e,
				new ArrayList<Element>());
		SuperSystem oilSystem = new SuperSystem("Oil",
				Arrays.asList(oilW, oilE));
		*/
		
		// federation agreement		
		e1s.setSupplier(ResourceType.ELECTRICITY, e3);
		e3s.addCustomer(e1);
		
		e3s.setSupplier(ResourceType.OIL, e4);
		e4s.addCustomer(e3);
		
		// waterW.getState().setSupplier(ResourceType.ELECTRICITY, electW);
		// electW.getState().addCustomer(waterW);
		
		// electW.getState().setSupplier(ResourceType.OIL, oilW);
		// oilW.getState().addCustomer(electW);
		
		
		// return new Scenario("Baseline", 0, Arrays.asList(w, we, e, ew), 
		//		Arrays.asList(waterSystem, electSystem, oilSystem));
		
		return new Scenario("Baseline", 0, Arrays.asList(w, we, e, ew), 
				Arrays.asList(e1, e2, e3, e4));
	}
	
	public static Scenario buildTestScenario() {
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		Location l11 = new Location(n1, n1);
		Location l22 = new Location(n2, n2);
		Location l12 = new Location(n1, n2);
		
		Element e1 = ElementFactory.createProductionElement(
				"Dummy 1", l11, 25, 3, 
				ResourceFactory.create(ResourceType.CURRENCY, 1e6), 9, 
				ResourceFactory.create(ResourceType.CURRENCY, 1e3), 
				new ResourceMatrix(ResourceType.WATER, 
						ResourceFactory.create(ResourceType.ELECTRICITY, 2.5)), 
				ResourceFactory.create(ResourceType.WATER, 1.2), 2, 
				ResourceFactory.create(ResourceType.CURRENCY, 500e3));
		
		Element e2 = ElementFactory.createDistributionElement(
				"Dummy 2", l12, 6, 2, 
				ResourceFactory.create(ResourceType.CURRENCY, 300e3), 10, 
				ResourceFactory.create(ResourceType.CURRENCY, 500), 
				new ResourceMatrix(ResourceType.WATER, 
						ResourceFactory.create(ResourceType.ELECTRICITY, 0.1)), 
				ResourceFactory.create(ResourceType.WATER, 2), 2, 
				ResourceFactory.create(ResourceType.CURRENCY, 200e3));
		
		Element e3 =  ElementFactory.createRetrievalElement("Budget", l11, 
				ResourceFactory.create(ResourceType.CURRENCY, 1e9), 
				ResourceFactory.create(), 
				new ResourceMatrix(), 
				ResourceFactory.create());
		
		return new Scenario("Test", 0, 
				Arrays.asList(l11, l22, l12), Arrays.asList(e1, e2, e3));
	}
}
