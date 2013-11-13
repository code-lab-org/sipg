package edu.mit.isos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Simulator {
	private List<Node> nodes = new ArrayList<Node>();
	List<Element> elements = new ArrayList<Element>();
	
	public static void main(String[] args) {
		Simulator s = new Simulator();
		
		Node west = new Node("West");
		Node east = new Node("East");
		List<Node> nodes = new ArrayList<Node>();
		nodes.addAll(Arrays.asList(west, east));
		
		s.nodes.addAll(Arrays.asList(west, east));
		
		Element socialWest = new Element("Social West", west, west);
		socialWest.stock = new Resource(0,0,0,0,10);
		socialWest.transformOutputs = new Resource(0, 0, 0, 0, 2);
		socialWest.storeInputs = new Resource(0, 0, 0, 0, 1.5);
		
		Element socialWestEast = new Element("Social West->East", west, east);
		socialWestEast.transportInputs = new Resource(0, 0, 0, 0, 0.5);
		socialWestEast.transportOutputs = new Resource(0, 0, 0, 0, 0.5);
		
		Element socialEast = new Element("Social East", east, east);
		socialEast.storeInputs = new Resource(0, 0, 0, 0, 0.5);
		
		s.elements.addAll(Arrays.asList(socialWest, socialWestEast, socialEast));
		
		long startTime = new Date().getTime();
		s.execute();
		System.out.println("Simulation completed in " + (new Date().getTime() - startTime) +" ms");
	}
	
	public void execute() {
		System.out.println("Time = 0");
		for(Element element : elements) {
			System.out.println(element);
		}
		
		for(long t = 0; t < 100l; t++) {
			for(Node node : nodes) {
				Resource inflow = new Resource();
				Resource outflow = new Resource();
				for(Element element : elements) {
					if(element.origin.equals(node)) {
						inflow = inflow.add(element.getTransformInputs())
								.add(element.getTransportInputs())
								.add(element.getStoreInputs())
								.add(element.getExchangeInputs());
					}
					if(element.destination.equals(node)) {
						outflow = outflow.add(element.getTransformOutputs())
								.add(element.getTransportOutputs())
								.add(element.getStoreOutputs())
								.add(element.getExchangeOutputs());
					}
				}
				if(!inflow.equals(outflow)) {
					throw new RuntimeException("Inflow does not equal outflow: " + 
							inflow + "->" + node + "->" + outflow);
				}
			}
			System.out.println("Time = " + (t+1));
			for(Element element : elements) {
				element.tick();
				System.out.println(element);
			}
		}
	}
}
