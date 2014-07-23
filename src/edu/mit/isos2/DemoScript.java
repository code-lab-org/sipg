package edu.mit.isos2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;

import edu.mit.isos2.element.DefaultElement;
import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceMatrix;
import edu.mit.isos2.resource.ResourceType;
import edu.mit.isos2.state.DefaultState;
import edu.mit.isos2.state.ExchangingState;

public class DemoScript {
	public static void main(String[] args) {
		BasicConfigurator.configure();
		Simulator sim = new Simulator(buildScenario());
		sim.execute(10, 1, 20);
	}
	
	public static Scenario buildScenario() {
		Node n_a = new Node("A");
		Node n_b = new Node("B");
		Node n_c = new Node("C");
		Location l_aa = new Location(n_a, n_a);
		Location l_bb = new Location(n_b, n_b);
		Location l_cc = new Location(n_c, n_c);
		Location l_ab = new Location(n_a, n_b);
		Location l_ba = new Location(n_b, n_a);
		Location l_bc = new Location(n_b, n_c);
		Location l_cb = new Location(n_c, n_b);

		Element e_s1 = createSocialElement("e_s1", l_aa, 10.0, 3.0, 1.0, 1000, 0.03);
		Element e_s2 = createSocialElement("e_s2", l_bb, 10.0, 3.0, 1.0, 200, 0.03);
		Element e_s3 = createSocialElement("e_s3", l_cc, 10.0, 3.0, 1.0, 800, 0.04);
		
		Element e_e1 = createElectricityElement("e_e1", l_aa, 1000, 10);
		Element e_e2 = createElectricityElement("e_e2", l_bb, 1000, 10);
		Element e_e3 = createElectricityElement("e_e3", l_cc, 1000, 10);
		
		Element e_o1 = createOilElement("e_o1", l_aa, 0.01, 1, 1e6);
		Element e_o2 = createOilElement("e_o2", l_bb, 0.01, 1, 1e6);
		Element e_o3 = createOilElement("e_o3", l_cc, 0.01, 1, 1e6);

		Element e_w1 = createWaterElement("e_w1", l_aa, 12000, 0.05, 1, 0.05*0.001, 1e6);
		Element e_w2 = createWaterElement("e_w2", l_bb, 0, 0.05, 1, 0.05*0.001, 1e6);
		Element e_w3 = createWaterElement("e_w3", l_cc, 12000, 0.05, 1, 0.05*0.001, 1e6);
		Element e_w4 = createPipelineElement("e_w4", l_ab, 1000, 0.9, 0.001);
		setUpPipeline(e_w4, e_w1, e_w2);
		Element e_w5 = createPipelineElement("e_w5", l_cb, 1000, 0.9, 0.001);
		setUpPipeline(e_w5, e_w3, e_w2);
		
		// federation agreement
		setUpAgreement(e_s1, e_e1, e_o1, e_w1);
		setUpAgreement(e_s2, e_e2, e_o2, e_w2);
		setUpAgreement(e_s3, e_e3, e_o3, e_w3);
		
		return new Scenario("Demo", 0, 
				Arrays.asList(l_aa, l_bb, l_cc, l_ab, l_ba, l_bc, l_cb), 
				Arrays.asList(e_s1, e_s2, e_s3, e_o1, e_o2, e_o3, 
						e_e1, e_e2, e_e3, e_w1, e_w2, e_w3, e_w4, e_w5));
	}
	
	private static void setUpPipeline(Element pipeline, Element origin, Element destination) {
		((PipelineState)pipeline.getInitialState()).setOrigin(origin);
		((PipelineState)pipeline.getInitialState()).setDestination(destination);
		((WaterState)origin.getInitialState()).addPipeline(pipeline);
		((WaterState)destination.getInitialState()).addPipeline(pipeline);
	}
	
	private static void setUpAgreement(Element social, Element oil, Element electricity, Element water) {
		((ExchangingState)social.getInitialState()).setSupplier(ResourceType.ELECTRICITY, electricity);
		((ExchangingState)social.getInitialState()).setSupplier(ResourceType.OIL, oil);
		((ExchangingState)social.getInitialState()).setSupplier(ResourceType.WATER, water);
		
		((ExchangingState)oil.getInitialState()).addCustomer(electricity);
		((ExchangingState)oil.getInitialState()).addCustomer(social);
		((ExchangingState)oil.getInitialState()).addCustomer(water);
		((ExchangingState)oil.getInitialState()).setSupplier(ResourceType.ELECTRICITY, electricity);
		
		((ExchangingState)electricity.getInitialState()).addCustomer(water);
		((ExchangingState)electricity.getInitialState()).addCustomer(social);
		((ExchangingState)electricity.getInitialState()).addCustomer(oil);
		((ExchangingState)electricity.getInitialState()).setSupplier(ResourceType.OIL, oil);
		
		((ExchangingState)water.getInitialState()).addCustomer(social);
		((ExchangingState)water.getInitialState()).setSupplier(ResourceType.ELECTRICITY, electricity);
		((ExchangingState)water.getInitialState()).setSupplier(ResourceType.OIL, oil);
	}
	
	private static Element createElectricityElement(String name, Location location,
			final double solarCap, final double thermalOil) {
		return new DefaultElement(name, location, 
				new ExchangingState("Ops") {
					private ResourceMatrix tfMatrix = new ResourceMatrix(
							ResourceType.ELECTRICITY, 
							ResourceFactory.create(ResourceType.OIL, thermalOil));
					
					private Resource solarCapacity = ResourceFactory.create(
							ResourceType.ELECTRICITY, solarCap);
					
					@Override 
					public Resource getProduced(Element element, long duration) {
						return getSent(element, duration);
					}
					
					@Override
					public Resource getConsumed(Element element, long duration) {
						return tfMatrix.multiply(getProduced(element, duration)
								.subtract(solarCapacity)
								.truncatePositive());
					}
					
					@Override
					public Resource getReceived(Element element, long duration) {
						return getConsumed(element, duration);
					}
				}
		);
	}
	
	private static Element createOilElement(String name, Location location, 
			final double extractElect, final double extractReserves, 
			final double initialReserves) {
		return new DefaultElement(name, location, 
				new ExchangingState("Ops") {
					private ResourceMatrix tfMatrix = new ResourceMatrix(
							ResourceType.OIL, ResourceFactory.create(
									new ResourceType[]{ResourceType.ELECTRICITY, ResourceType.RESERVES},
									new double[]{extractElect, extractReserves}));
					@Override 
					public Resource getProduced(Element element, long duration) {
						return getSent(element, duration);
					}
					
					@Override
					public Resource getRetrieved(Element element, long duration) {
						return getConsumed(element, duration).get(ResourceType.RESERVES);
					}
					
					@Override
					public Resource getConsumed(Element element, long duration) {
						return tfMatrix.multiply(getProduced(element, duration));
					}
					
					@Override
					public Resource getReceived(Element element, long duration) {
						return getConsumed(element, duration).get(ResourceType.ELECTRICITY);
					}
				}
		).initialContents(ResourceFactory.create(ResourceType.RESERVES, initialReserves));
	}
	
	private static Element createSocialElement(String name, Location location, 
			final double waterPC, final double electPC, final double oilPC, 
			final double initialPopulation, final double growthRate) {
		return new DefaultElement(name, location, 
				new ExchangingState("Ops") {
					@Override
					public Resource getReceived(Element element, long duration) {
						return getConsumed(element, duration);
					}
					
					@Override
					public Resource getConsumed(Element element, long duration) {
						return ResourceFactory.create(
								new ResourceType[]{ResourceType.ELECTRICITY, 
										ResourceType.WATER, ResourceType.OIL},
								new double[]{electPC, waterPC, oilPC})
								.multiply(element.getContents().getQuantity(ResourceType.PEOPLE))
								.multiply(duration);
					}
					
					@Override
					public Resource getProduced(Element element, long duration) {
						return element.getContents().get(ResourceType.PEOPLE)
								.multiply(Math.pow(growthRate+1,duration)-1);
					}
					
					@Override
					public Resource getStored(Element element, long duration) {
						return getProduced(element, duration);
					}
				}
		).initialContents(ResourceFactory.create(ResourceType.PEOPLE, initialPopulation));
	}
	
	private static Element createWaterElement(String name, Location location,
			double desalCap, double desalOil, double liftAquifer, 
			double liftElect, double initialAquifer) {
		return new DefaultElement(name, location,
				new WaterState(desalCap, desalOil, liftAquifer, liftElect))
				.initialContents(ResourceFactory.create(ResourceType.AQUIFER, initialAquifer));
	}

	public static Element createPipelineElement(String name, Location location, 
			double capacity, double efficiency, double pumpElect) {
		return new DefaultElement(name, location,
				new PipelineState(capacity, efficiency, pumpElect));
	}
	
	private static class PipelineState extends DefaultState {
		protected Resource outputCapacity;
		protected double eta = 1;
		protected ResourceMatrix tpMatrix = new ResourceMatrix();
		protected Resource output = ResourceFactory.create();
		protected Element origin = null, destination = null;
		
		public PipelineState(double capacity, double efficiency, double pumpElect) {
			super("Ops");
			this.outputCapacity = ResourceFactory.create(ResourceType.WATER, capacity);
			eta = efficiency;
			tpMatrix = new ResourceMatrix(
					ResourceType.WATER, 
					ResourceFactory.create(ResourceType.ELECTRICITY, pumpElect));
		}
		
		protected void setOrigin(Element origin) {
			this.origin = origin;
		}
		
		protected void setDestination(Element destination) {
			this.destination = destination;
		}
		
		@Override
		public Resource getInput(Element element, long duration) {
			return getOutput(element, duration).multiply(1/eta)
					.add(tpMatrix.multiply(getOutput(element, duration).multiply(1/eta)));
		}
		@Override
		public Resource getOutput(Element element, long duration) {
			return output;
		}
		@Override
		public Resource getConsumed(Element element, long duration) {
			return getInput(element, duration).subtract(getOutput(element, duration));
		}
		@Override
		public void iterateTick(Element element, long duration) {
			super.iterateTick(element, duration);
			if(origin != null && destination != null 
					&& origin.getState() instanceof WaterState 
					&& destination.getState() instanceof WaterState) {
				WaterState originState = (WaterState) origin.getState();
				WaterState destinationState = (WaterState) destination.getState();
				if(originState.getNetSupply(origin, duration).getQuantity(ResourceType.WATER) 
						< destinationState.getNetSupply(destination, duration).negate().getQuantity(ResourceType.WATER)) {
					output = output.add(originState.getNetSupply(origin, duration));
				} else {
					output = output.add(destinationState.getNetSupply(origin, duration).negate());
				}
				output = output.truncatePositive();
				if(output.getQuantity(ResourceType.WATER) 
						> outputCapacity.multiply(duration).getQuantity(ResourceType.WATER)) {
					output = outputCapacity.multiply(duration);
				}
			} else {
				output = ResourceFactory.create();
			}
		}
	}
	
	private static class WaterState extends ExchangingState {
		private Resource desalCapacity = ResourceFactory.create();
		private ResourceMatrix desalMatrix = new ResourceMatrix();
		private ResourceMatrix liftMatrix = new ResourceMatrix();
		private Set<Element> pipelines = new HashSet<Element>();

		public WaterState(double desalCap, double desalOil,
				double liftAquifer, double liftElect) {
			super("Ops");
			desalCapacity = ResourceFactory.create(
					ResourceType.WATER, desalCap);
			desalMatrix = new ResourceMatrix(
					ResourceType.WATER, 
					ResourceFactory.create(ResourceType.OIL, desalOil));
			liftMatrix = new ResourceMatrix(
					ResourceType.WATER, ResourceFactory.create(
							new ResourceType[]{ResourceType.AQUIFER, ResourceType.ELECTRICITY}, 
							new double[]{liftAquifer, liftElect}));
		}
		
		protected void addPipeline(Element pipeline) {
			pipelines.add(pipeline);
		}
		
		protected Resource getNetSupply(Element element, long duration) {
			return desalCapacity.multiply(duration)
					.subtract(getNetDemand(element, duration));
		}
		
		protected Resource getDesalProduction(Element element, long duration) {
			if(getNetDemand(element, duration).getQuantity(ResourceType.WATER) 
					< desalCapacity.multiply(duration).getQuantity(ResourceType.WATER)) {
				return getNetDemand(element, duration).get(ResourceType.WATER);
			} else {
				return desalCapacity.multiply(duration);
			}
		}
		
		protected Resource getLiftProduction(Element element, long duration) {
			return getNetDemand(element, duration)
					.subtract(desalCapacity.multiply(duration))
					.truncatePositive();
		}
		
		protected Resource getPipedIn(Element element, long duration) {
			Resource pipedIn = ResourceFactory.create();
			for(Element pipeline : pipelines) {
				if(pipeline.getLocation().getDestination().equals(element.getLocation().getOrigin())
						&& pipeline.getState() instanceof PipelineState) {
					pipedIn = pipedIn.add(((PipelineState)pipeline.getState())
							.getOutput(pipeline, duration).get(ResourceType.WATER));
				}
			}
			return pipedIn;
		}
		
		protected Resource getPipedOut(Element element, long duration) {
			Resource pipedOut = ResourceFactory.create();
			for(Element pipeline : pipelines) {
				if(pipeline.getLocation().getOrigin().equals(element.getLocation().getDestination())
						&& pipeline.getState() instanceof PipelineState) {
					pipedOut = pipedOut.add(((PipelineState)pipeline.getState())
							.getInput(pipeline, duration).get(ResourceType.WATER));
				}
			}
			return pipedOut;
		}
		
		protected Resource getNetDemand(Element element, long duration) {
			return getSent(element, duration).get(ResourceType.WATER)
					.add(getPipedOut(element, duration))
					.subtract(getPipedIn(element, duration));
		}
		
		@Override 
		public Resource getProduced(Element element, long duration) {
			return getDesalProduction(element, duration)
					.add(getLiftProduction(element, duration));
		}
		
		@Override
		public Resource getRetrieved(Element element, long duration) {
			return getConsumed(element, duration).get(ResourceType.AQUIFER);
		}
		
		@Override
		public Resource getConsumed(Element element, long duration) {
			return desalMatrix.multiply(getDesalProduction(element, duration))
					.add(liftMatrix.multiply(getLiftProduction(element, duration)));
		}
		
		@Override
		public Resource getReceived(Element element, long duration) {
			Resource received = getConsumed(element, duration).get(ResourceType.ELECTRICITY)
					.add(getConsumed(element, duration).get(ResourceType.OIL));
			for(Element pipeline : pipelines) {
				if(pipeline.getLocation().getOrigin().equals(element.getLocation().getDestination())
						&& pipeline.getState() instanceof PipelineState) {
					received = received.add(((PipelineState)pipeline.getState())
							.getInput(pipeline, duration).get(ResourceType.ELECTRICITY));
				}
			}
			return received;
		}
		
	}
}
