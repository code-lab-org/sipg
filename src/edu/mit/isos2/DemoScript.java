package edu.mit.isos2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.log4j.BasicConfigurator;

import edu.mit.isos2.element.DefaultElement;
import edu.mit.isos2.element.Element;
import edu.mit.isos2.event.SimulationTimeEvent;
import edu.mit.isos2.event.SimulationTimeListener;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceMatrix;
import edu.mit.isos2.resource.ResourceType;
import edu.mit.isos2.state.DefaultState;
import edu.mit.isos2.state.ExchangingState;
import edu.mit.isos2.state.NullState;

public class DemoScript {
	public static void main(String[] args) {
		BasicConfigurator.configure();

		final int stepsPerYear = 1000;
		final double timeStepDuration = 0.5;
		final double simulationDuration = 20.0;
		final Simulator sim = new Simulator(buildScenario(stepsPerYear));
		sim.addSimulationTimeListener(new SimulationTimeListener() {
			@Override
			public void timeAdvanced(SimulationTimeEvent event) {
				//System.out.println("time is now " + event.getTime());
				if(event.getTime()==simulationDuration*stepsPerYear) {
					System.out.println("e_w1 contents " + sim.getScenario().getElement("e_w1").getContents());
				}
			}
		});
		for(int i = 0; i < 100; i++) {
			System.out.println("Simulation completed in " 
					+ sim.execute((int) (simulationDuration*stepsPerYear), 
							(int) (timeStepDuration*stepsPerYear), 20)
					+ " ms");
		}
	}

	public static Scenario buildScenario(double stepsPerYear) {
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

		Element e_s1 = createSocialElement("e_s1", l_aa, 0.065/stepsPerYear, 4.0/stepsPerYear, 1.0/stepsPerYear, 3.0, 0.07/stepsPerYear);
		Element e_s2 = createSocialElement("e_s2", l_bb, 0.050/stepsPerYear, 3.0/stepsPerYear, 1.2/stepsPerYear, 1.0, 0.05/stepsPerYear);
		Element e_s3 = createSocialElement("e_s3", l_cc, 0.060/stepsPerYear, 3.5/stepsPerYear, 1.0/stepsPerYear, 6.0, 0.06/stepsPerYear);

		Element e_o1 = createOilElement("e_o1", l_aa, 1.5, 1.0, 500);
		Element e_o2 = createOilElement("e_o2", l_bb, 2.0, 1.0, 100);
		Element e_o3 = createOilElement("e_o3", l_cc, 1.6, 1.0, 400);

		Element e_e1 = createElectricityElement("e_e1", l_aa, 1.0/stepsPerYear, 0.25);
		Element e_e2 = createElectricityElement("e_e2", l_bb, 0.5/stepsPerYear, 0.30);
		Element e_e3 = createElectricityElement("e_e3", l_cc, 0.8/stepsPerYear, 0.25);

		Element e_w1 = createWaterElement("e_w1", l_aa, 1.0, 0.9, 200);
		Element e_w2 = createWaterElement("e_w2", l_bb, 1.0, 0.9, 150);
		Element e_w3 = createWaterElement("e_w3", l_cc, 1.0, 0.9, 250);
		Element e_w4 = createPlantElement("e_w4", l_aa, 0.5/stepsPerYear, 4.5);
		Element e_w5 = createPlantElement("e_w5", l_cc, 0.4/stepsPerYear, 4.5);
		Element e_w6 = createPipelineElement("e_w6", l_ab, 0.02/stepsPerYear, 0.9, 2.5);
		Element e_w7 = createPipelineElement("e_w7", l_cb, 0.02/stepsPerYear, 0.9, 2.0);
		Element e_w8 = createWaterController("e_w8", l_aa, 
				Arrays.asList(e_w1, e_w2, e_w3, e_w4, e_w5, e_w6, e_w7));

		// federation agreement
		setUpAgreement(e_s1, e_e1, e_o1, e_w1);
		setUpAgreement(e_s2, e_e2, e_o2, e_w2);
		setUpAgreement(e_s3, e_e3, e_o3, e_w3);

		return new Scenario("Demo", 0, 
				Arrays.asList(l_aa, l_bb, l_cc, l_ab, l_ba, l_bc, l_cb), 
				Arrays.asList(e_s1, e_s2, e_s3, e_o1, e_o2, e_o3, 
						e_e1, e_e2, e_e3, e_w1, e_w2, e_w3, e_w4, e_w5, e_w6, e_w7, e_w8));
	}

	private static void setUpAgreement(Element social, Element electricity, Element oil, Element water) {
		((ExchangingState)social.getInitialState()).setSupplier(ResourceType.ELECTRICITY, electricity);
		((ExchangingState)social.getInitialState()).setSupplier(ResourceType.OIL, oil);
		((ExchangingState)social.getInitialState()).setSupplier(ResourceType.WATER, water);

		((ExchangingState)oil.getInitialState()).addCustomer(ResourceType.OIL, electricity);
		((ExchangingState)oil.getInitialState()).addCustomer(ResourceType.OIL, social);
		//((ExchangingState)oil.getInitialState()).addCustomer(ResourceType.OIL, water);
		((ExchangingState)oil.getInitialState()).setSupplier(ResourceType.ELECTRICITY, electricity);

		((ExchangingState)electricity.getInitialState()).addCustomer(ResourceType.ELECTRICITY, water);
		((ExchangingState)electricity.getInitialState()).addCustomer(ResourceType.ELECTRICITY, social);
		((ExchangingState)electricity.getInitialState()).addCustomer(ResourceType.ELECTRICITY, oil);
		((ExchangingState)electricity.getInitialState()).setSupplier(ResourceType.OIL, oil);

		((ExchangingState)water.getInitialState()).addCustomer(ResourceType.WATER, social);
		((ExchangingState)water.getInitialState()).setSupplier(ResourceType.ELECTRICITY, electricity);
		//((ExchangingState)water.getInitialState()).setSupplier(ResourceType.OIL, oil);
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
						.subtract(solarCapacity.multiply(duration))
						.truncatePositive());
			}

			@Override
			public Resource getReceived(Element element, long duration) {
				return getConsumed(element, duration).get(ResourceType.OIL);
			}
		});
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
			private ResourceMatrix tfMatrix = new ResourceMatrix(
					ResourceType.PEOPLE, ResourceFactory.create(
							new ResourceType[]{ResourceType.ELECTRICITY, 
									ResourceType.WATER, ResourceType.OIL},
									new double[]{electPC, waterPC, oilPC}));
			@Override
			public Resource getReceived(Element element, long duration) {
				return getConsumed(element, duration);
			}

			@Override
			public Resource getConsumed(Element element, long duration) {
				return tfMatrix.multiply(element.getContents().get(ResourceType.PEOPLE))
						.multiply(duration);
			}

			@Override
			public Resource getProduced(Element element, long duration) {
				return element.getContents().get(ResourceType.PEOPLE)
						.multiply(Math.exp(growthRate*duration)-1);
			}

			@Override
			public Resource getStored(Element element, long duration) {
				return getProduced(element, duration);
			}
		}
				).initialContents(ResourceFactory.create(ResourceType.PEOPLE, initialPopulation));
	}

	private static Element createWaterController(String name, Location location, 
			final List<Element> elements) {
		return new DefaultElement(name, location, new NullState() {
			@Override
			public void iterateTick(Element element, long duration) {
				Set<Element> plants = new HashSet<Element>();
				Set<Element> pipelines = new HashSet<Element>();
				Set<Element> systems  = new HashSet<Element>();
				for(Element e : elements) {
					if(e.getState() instanceof PlantState) {
						plants.add(e);
					}
					if(e.getState() instanceof PipelineState) {
						pipelines.add(e);
					}
					if(e.getState() instanceof WaterState) {
						systems.add(e);
					}
				}

				List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
				double[] costCoefficients = new double[elements.size()];
				double[] initialValues = new double[elements.size()];
				for(Element e : systems) {
					// cost of lifting aquifer is 1
					costCoefficients[elements.indexOf(e)] = 1;
					initialValues[elements.indexOf(e)] = ((WaterState)e.getState())
							.getProduced(e, duration).getQuantity(ResourceType.WATER);
				}
				for(Element e : plants) {
					initialValues[elements.indexOf(e)] = ((PlantState)e.getState())
							.getProduced(e, duration).getQuantity(ResourceType.WATER);
					double[] productionCoefficients = new double[elements.size()];
					productionCoefficients[elements.indexOf(e)] = 1;
					constraints.add(new LinearConstraint(productionCoefficients, Relationship.LEQ, 
							((PlantState)e.getState()).productionCapacity.multiply(duration).getQuantity(ResourceType.WATER)));
				}
				for(Element e : pipelines) {
					initialValues[elements.indexOf(e)] = ((PipelineState)e.getState())
							.getOutput(e, duration).getQuantity(ResourceType.WATER);
					double[] outputCoefficients = new double[elements.size()];
					outputCoefficients[elements.indexOf(e)] = 1;
					constraints.add(new LinearConstraint(outputCoefficients, Relationship.LEQ, 
							((PipelineState)e.getState()).outputCapacity.multiply(duration).getQuantity(ResourceType.WATER)));
				}

				for(Element e : systems) {
					double[] flowCoefficients = new double[elements.size()];
					flowCoefficients[elements.indexOf(e)] = 1; // system production
					for(Element plant : plants) {
						if(plant.getLocation().equals(e.getLocation())) {
							flowCoefficients[elements.indexOf(plant)] = 1; // plant production
						}
					}
					for(Element pipeline : pipelines) {
						if(pipeline.getLocation().getOrigin().equals(
								e.getLocation().getOrigin())) {
							flowCoefficients[elements.indexOf(pipeline)] 
									= -1/((PipelineState)pipeline.getState()).eta; // pipeline input
						} else if(pipeline.getLocation().getDestination().equals(
								e.getLocation().getOrigin())) {
							flowCoefficients[elements.indexOf(pipeline)] = 1; // pipeline output
						}
					}
					constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
							((WaterState)e.getState()).getSent(e, duration).getQuantity(ResourceType.WATER)));
				}

				try {
					// Run optimization and get results.
					PointValuePair output = new SimplexSolver().optimize(
							GoalType.MINIMIZE,
							new MaxIter(1000),
							new NonNegativeConstraint(true), 
							new LinearConstraintSet(constraints), 
							new LinearObjectiveFunction(costCoefficients, 0d),
							new InitialGuess(initialValues));
					for(Element e : systems) {
						((WaterState)e.getState()).setProduced(ResourceFactory.create(ResourceType.WATER,
								output.getPoint()[elements.indexOf(e)]), duration);
					}
					for(Element e : plants) {
						((PlantState)e.getState()).setProduced(ResourceFactory.create(ResourceType.WATER,
								output.getPoint()[elements.indexOf(e)]), duration);
					}
					for(Element e : pipelines) {
						((PipelineState)e.getState()).setOutput(ResourceFactory.create(ResourceType.WATER,
								output.getPoint()[elements.indexOf(e)]), duration);
					}
				} catch(TooManyIterationsException ignore) { 
					// Don't overwrite existing values.
					ignore.printStackTrace();
				} catch(NoFeasibleSolutionException ignore) {
					// Don't overwrite existing values.
					ignore.printStackTrace();
				}

				for(Element system : systems) {
					Resource received = ((WaterState)system.getState())
							.getConsumed(system, duration).get(ResourceType.ELECTRICITY);
					for(Element plant : plants) {
						if(plant.getLocation().equals(system.getLocation())){
							received = received.add(((PlantState)plant.getState())
									.getConsumed(plant, duration).get(ResourceType.ELECTRICITY));
						}
					}
					for(Element pipeline : pipelines) {
						if(pipeline.getLocation().getOrigin().equals(
								system.getLocation().getOrigin())) {
							received = received.add(((PipelineState)pipeline.getState())
									.getInput(pipeline, duration).get(ResourceType.ELECTRICITY));
						}
					}
					((WaterState)system.getState()).setReceived(received, duration);
				}
			}
		});
	}

	private static Element createWaterElement(String name, Location location,
			double liftAquifer, double liftElect, double initialAquifer) {
		return new DefaultElement(name, location,
				new WaterState(liftAquifer, liftElect))
		.initialContents(ResourceFactory.create(ResourceType.AQUIFER, initialAquifer));
	}

	public static Element createPipelineElement(String name, Location location, 
			double capacity, double efficiency, double pumpElect) {
		return new DefaultElement(name, location,
				new PipelineState(capacity, efficiency, pumpElect));
	}
	public static Element createPlantElement(String name, Location location, 
			double capacity, double desalElect) {
		return new DefaultElement(name, location,
				new PlantState(capacity, desalElect));
	}

	private static class PlantState extends DefaultState {
		protected Resource productionCapacity;
		protected ResourceMatrix tfMatrix = new ResourceMatrix();
		protected Resource produced = ResourceFactory.create();

		public PlantState(double capacity, double desalElect) {
			super("Ops");
			this.productionCapacity = ResourceFactory.create(ResourceType.WATER, capacity);
			tfMatrix = new ResourceMatrix(
					ResourceType.WATER,
					ResourceFactory.create(ResourceType.ELECTRICITY, desalElect));
		}
		@Override
		public Resource getConsumed(Element element, long duration) {
			return tfMatrix.multiply(getProduced(element, duration));
		}
		@Override
		public Resource getProduced(Element element, long duration) {
			return produced;
		}
		protected void setProduced(Resource produced, long duration) {
			if(produced.getQuantity(ResourceType.WATER) > 
			productionCapacity.multiply(duration).getQuantity(ResourceType.WATER)) {
				this.produced = productionCapacity.multiply(duration);
			} else {
				this.produced = produced.truncatePositive();
			}
		}
	}

	private static class PipelineState extends DefaultState {
		protected Resource outputCapacity;
		protected double eta = 1;
		protected ResourceMatrix tpMatrix = new ResourceMatrix();
		protected Resource output = ResourceFactory.create();

		public PipelineState(double capacity, double efficiency, double pumpElect) {
			super("Ops");
			this.outputCapacity = ResourceFactory.create(ResourceType.WATER, capacity);
			eta = efficiency;
			tpMatrix = new ResourceMatrix(
					ResourceType.WATER, 
					ResourceFactory.create(ResourceType.ELECTRICITY, pumpElect));
		}

		@Override
		public Resource getInput(Element element, long duration) {
			return getOutput(element, duration).multiply(1/eta)
					.add(tpMatrix.multiply(getOutput(element, duration)));
		}
		@Override
		public Resource getOutput(Element element, long duration) {
			return output;
		}
		@Override
		public Resource getConsumed(Element element, long duration) {
			return getInput(element, duration).subtract(getOutput(element, duration));
		}
		protected void setOutput(Resource output, long duration) {
			if(output.getQuantity(ResourceType.WATER) > 
			outputCapacity.multiply(duration).getQuantity(ResourceType.WATER)) {
				this.output = outputCapacity.multiply(duration);
			} else {
				this.output = output.truncatePositive();
			}
		}
	}

	private static class WaterState extends ExchangingState {
		private ResourceMatrix liftMatrix = new ResourceMatrix();
		Resource produced = ResourceFactory.create();
		Resource received = ResourceFactory.create();

		public WaterState(double liftAquifer, double liftElect) {
			super("Ops");
			liftMatrix = new ResourceMatrix(
					ResourceType.WATER, ResourceFactory.create(
							new ResourceType[]{ResourceType.AQUIFER, ResourceType.ELECTRICITY}, 
							new double[]{liftAquifer, liftElect}));
		}

		@Override 
		public Resource getProduced(Element element, long duration) {
			return produced;
		}

		@Override
		public Resource getRetrieved(Element element, long duration) {
			return getConsumed(element, duration).get(ResourceType.AQUIFER);
		}

		@Override
		public Resource getConsumed(Element element, long duration) {
			return liftMatrix.multiply(produced);
		}
		@Override
		public Resource getReceived(Element element, long duration) {
			return received;
		}
		protected void setProduced(Resource produced, long duration) {
			this.produced = produced;
		}
		protected void setReceived(Resource received, long duration) {
			this.received = received;
		}
	}
}
