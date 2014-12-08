package edu.mit.isos.app.water;

import java.util.ArrayList;
import java.util.Collection;
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

import edu.mit.isos.core.context.Location;
import edu.mit.isos.core.context.Resource;
import edu.mit.isos.core.context.ResourceFactory;
import edu.mit.isos.core.context.ResourceType;
import edu.mit.isos.core.element.DefaultElement;
import edu.mit.isos.core.element.LocalElement;
import edu.mit.isos.core.state.NullState;

public class WaterController extends DefaultElement {
	public WaterController(String name, Location location, 
			Collection<? extends LocalElement> elements) {
		super(name, location, new ControllerState(elements));
	}
	
	public static class ControllerState extends NullState {
		private List<LocalElement> elements = new ArrayList<LocalElement>();
		public ControllerState(Collection<? extends LocalElement> elements) {
			this.elements.addAll(elements);
		}
		@Override
		public void iterateTick(LocalElement element, long duration) {
			Set<WaterPlant> plants = new HashSet<WaterPlant>();
			Set<WaterPipeline> pipelines = new HashSet<WaterPipeline>();
			Set<LocalWaterElement> systems  = new HashSet<LocalWaterElement>();
			for(LocalElement e : elements) {
				if(e instanceof WaterPlant && ((WaterPlant)e).isOperating()) {
					plants.add((WaterPlant)e);
				}
				if(e instanceof WaterPipeline) {
					pipelines.add((WaterPipeline)e);
				}
				if(e instanceof LocalWaterElement) {
					systems.add((LocalWaterElement)e);
				}
			}

			List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
			double[] costCoefficients = new double[elements.size()];
			double[] initialValues = new double[elements.size()];
			for(LocalWaterElement e : systems) {
				// cost of lifting aquifer is 1
				costCoefficients[elements.indexOf(e)] = 1;
				initialValues[elements.indexOf(e)] = ((WaterState)e.getState())
						.getProduced(e, duration).getQuantity(ResourceType.WATER);
			}
			for(WaterPlant e : plants) {
				initialValues[elements.indexOf(e)] = e.getOperatingState()
						.getProduced(e, duration).getQuantity(ResourceType.WATER);
				double[] productionCoefficients = new double[elements.size()];
				productionCoefficients[elements.indexOf(e)] = 1;
				constraints.add(new LinearConstraint(productionCoefficients, Relationship.LEQ, 
						e.getOperatingState().productionCapacity.multiply(duration).getQuantity(ResourceType.WATER)));
			}
			for(WaterPipeline e : pipelines) {
				initialValues[elements.indexOf(e)] = e.getOperatingState()
						.getOutput(e, duration).getQuantity(ResourceType.WATER);
				double[] outputCoefficients = new double[elements.size()];
				outputCoefficients[elements.indexOf(e)] = 1;
				constraints.add(new LinearConstraint(outputCoefficients, Relationship.LEQ, 
						e.getOperatingState().outputCapacity.multiply(duration).getQuantity(ResourceType.WATER)));
			}

			for(LocalWaterElement e : systems) {
				double[] flowCoefficients = new double[elements.size()];
				flowCoefficients[elements.indexOf(e)] = 1; // system production
				for(WaterPlant plant : plants) {
					if(plant.getLocation().equals(e.getLocation())) {
						flowCoefficients[elements.indexOf(plant)] = 1; // plant production
					}
				}
				for(WaterPipeline pipeline : pipelines) {
					if(pipeline.getLocation().getOrigin().equals(
							e.getLocation().getOrigin())) {
						flowCoefficients[elements.indexOf(pipeline)] 
								= -1/pipeline.getOperatingState().eta; // pipeline input
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
				for(LocalWaterElement e : systems) {
					e.getOperatingState().setProduced(ResourceFactory.create(ResourceType.WATER,
							output.getPoint()[elements.indexOf(e)]), duration);
				}
				for(WaterPlant e : plants) {
					e.getOperatingState().setProduced(ResourceFactory.create(ResourceType.WATER,
							output.getPoint()[elements.indexOf(e)]), duration);
				}
				for(WaterPipeline e : pipelines) {
					e.getOperatingState().setOutput(ResourceFactory.create(ResourceType.WATER,
							output.getPoint()[elements.indexOf(e)]), duration);
				}
			} catch(TooManyIterationsException ignore) { 
				// Don't overwrite existing values.
				ignore.printStackTrace();
			} catch(NoFeasibleSolutionException ignore) {
				// Don't overwrite existing values.
				ignore.printStackTrace();
			}

			for(LocalWaterElement system : systems) {
				Resource received = ((WaterState)system.getState())
						.getConsumed(system, duration).get(ResourceType.ELECTRICITY);
				for(WaterPlant plant : plants) {
					if(plant.getLocation().equals(system.getLocation())){
						received = received.add(plant.getOperatingState()
								.getConsumed(plant, duration).get(ResourceType.ELECTRICITY));
					}
				}
				for(WaterPipeline pipeline : pipelines) {
					if(pipeline.getLocation().getOrigin().equals(
							system.getLocation().getOrigin())) {
						received = received.add(pipeline.getOperatingState()
								.getInput(pipeline, duration).get(ResourceType.ELECTRICITY));
					}
				}
				system.getOperatingState().setReceived(received, duration);
			}
		}
	}
}
