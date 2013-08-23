package edu.mit.sips.core.electricity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import edu.mit.sips.core.City;
import edu.mit.sips.core.DefaultInfrastructureSoS;
import edu.mit.sips.core.OptimizationOptions;
import edu.mit.sips.core.Society;

/**
 * The Class DefaultElectricitySoS.
 */
public class DefaultElectricitySoS extends DefaultInfrastructureSoS implements ElectricitySoS {
	
	/**
	 * The Class Local.
	 */
	public static class Local extends DefaultInfrastructureSoS.Local implements ElectricitySoS.Local {
		
		/**
		 * Instantiates a new default electricity so s.
		 */
		public Local() {
			super("Electricity");
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.electricity.ElectricitySystem.Local#addElement(edu.mit.sips.core.electricity.ElectricityElement)
		 */
		@Override
		public boolean addElement(ElectricityElement element) {
			for(ElectricitySystem.Local system : getNestedSystems()) {
				if(system.getSociety().getName().equals(element.getOrigin())) {
					return system.addElement(element);
				}
			}
			return false;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getElectricityFromBurningPetroleum()
		 */
		@Override
		public double getElectricityFromBurningPetroleum() {
			double value = 0;
			for(ElectricitySystem.Local system : getNestedSystems()) {
				value += system.getElectricityFromBurningPetroleum();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getElectricityInDistribution()
		 */
		@Override
		public double getElectricityInDistribution() {
			double value = 0;
			for(ElectricityElement e : getInternalElements()) {
				if(!getSociety().getCities().contains(
						getSociety().getCountry().getCity(e.getDestination()))) {
					value += e.getElectricityInput();
				}
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getElectricityOutDistribution()
		 */
		@Override
		public double getElectricityOutDistribution() {
			double value = 0;
			for(ElectricityElement e : getInternalElements()) {
				if(!getSociety().getCities().contains(
						getSociety().getCountry().getCity(e.getDestination()))) {
					value += e.getElectricityInput();
				}
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getElectricityOutDistributionLosses()
		 */
		@Override
		public double getElectricityOutDistributionLosses() {
			double value = 0;
			for(ElectricityElement e : getInternalElements()) {
				value += e.getElectricityInput() - e.getElectricityOutput();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getElectricityProduction()
		 */
		@Override
		public double getElectricityProduction() {
			double value = 0;
			for(ElectricitySystem.Local system : getNestedSystems()) {
				value += system.getElectricityProduction();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getElectricityWasted()
		 */
		@Override
		public double getElectricityWasted() {
			double value = 0;
			for(ElectricitySystem.Local system : getNestedSystems()) {
				value += system.getElectricityWasted();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getElements()
		 */
		@Override
		public List<? extends ElectricityElement> getElements() {
			List<ElectricityElement> elements = new ArrayList<ElectricityElement>();
			elements.addAll(getInternalElements());
			elements.addAll(getExternalElements());
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getExternalElements()
		 */
		@Override
		public List<? extends ElectricityElement> getExternalElements() {
			List<ElectricityElement> elements = new ArrayList<ElectricityElement>();
			if(!getSociety().equals(getSociety().getCountry())) {
				for(ElectricitySystem.Local system : getNestedSystems()) {
					elements.addAll(system.getExternalElements());
				}
				elements.removeAll(getInternalElements());
			}
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getInternalElements()
		 */
		@Override
		public List<? extends ElectricityElement> getInternalElements() {
			List<ElectricityElement> elements = new ArrayList<ElectricityElement>();
			for(ElectricitySystem.Local system : getNestedSystems()) {
				elements.addAll(system.getInternalElements());
			}
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getLocalElectricityFraction()
		 */
		@Override
		public double getLocalElectricityFraction() {
			if(getSociety().getTotalElectricityDemand() > 0) {
				return Math.min(1, getElectricityProduction()
						/ getSociety().getTotalElectricityDemand());
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.DefaultInfrastructureSoS.Local#getNestedSystems()
		 */
		@Override
		public List<ElectricitySystem.Local> getNestedSystems() {
			List<ElectricitySystem.Local> systems = new ArrayList<ElectricitySystem.Local>();
			for(Society society : getSociety().getNestedSocieties()) {
				if(society.getElectricitySystem() instanceof ElectricitySystem.Local){ 
					systems.add((ElectricitySystem.Local)society.getElectricitySystem());
				}
			}
			return Collections.unmodifiableList(systems);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getPetroleumBurned()
		 */
		@Override
		public double getPetroleumBurned() {
			double value = 0;
			for(ElectricitySystem.Local system : getNestedSystems()) {
				value += system.getPetroleumBurned();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getPetroleumConsumption()
		 */
		@Override
		public double getPetroleumConsumption() {
			double value = 0;
			for(ElectricitySystem system : getNestedSystems()) {
				value += system.getPetroleumConsumption();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getRenewableElectricityFraction()
		 */
		@Override
		public double getRenewableElectricityFraction() {
			if(getSociety().getTotalElectricityDemand() > 0) {
				return getRenewableElectricityProduction() / 
						getSociety().getTotalElectricityDemand();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getRenewableElectricityProduction()
		 */
		@Override
		public double getRenewableElectricityProduction() {
			double value = 0;
			for(ElectricitySystem.Local system : getNestedSystems()) {
				value += system.getRenewableElectricityProduction();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getTotalElectricitySupply()
		 */
		@Override
		public double getTotalElectricitySupply() {
			double value = 0;
			for(ElectricitySystem.Local system : getNestedSystems()) {
				value += system.getTotalElectricitySupply();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getUnitProductionCost()
		 */
		@Override
		public double getUnitProductionCost() {
			if(getElectricityProduction() > 0) {
				return (getLifecycleExpense() + getConsumptionExpense()) 
						/ getElectricityProduction();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getUnitSupplyProfit()
		 */
		@Override
		public double getUnitSupplyProfit() {
			if(getTotalElectricitySupply() > 0) {
				return getCashFlow() / getTotalElectricitySupply();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getWaterConsumption()
		 */
		@Override
		public double getWaterConsumption() {
			double value = 0;
			for(ElectricitySystem system : getNestedSystems()) {
				value += system.getWaterConsumption();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.SimEntity#initialize(long)
		 */
		@Override
		public void initialize(long time) { }

		/**
		 * Optimize electricity distribution.
		 */
		@Override
		public void optimizeElectricityDistribution() {
			// Make a list of cities and infrastructure elements. The vector
			// of decision variables includes the throughput of each distribution
			// element and the petroleum burning amounts in each city.
			List<City> cities = getSociety().getCities();
			List<? extends ElectricityElement> elements = getInternalElements();
			
			// Count number of variables.
			int numVariables = elements.size() + cities.size();
			
			// Create a list to hold the linear constraints.
			List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

			double[] costCoefficients = new double[numVariables];
			double[] initialValues = new double[numVariables];

			for(ElectricityElement element : elements) {
				// Add constraints for distribution throughput, i.e. the
				// throughput for each distribution element cannot exceed the maximum.
				double[] distributionConstraint = new double[numVariables];
				distributionConstraint[elements.indexOf(element)] = 1;
				constraints.add(new LinearConstraint(distributionConstraint, 
						Relationship.LEQ, element.getMaxElectricityInput()));
				
				// Set distribution cost.
				costCoefficients[elements.indexOf(element)] 
						= element.getVariableOperationsCostOfElectricityDistribution();
				initialValues[elements.indexOf(element)] 
						= element.getElectricityInput();
			}
			
			for(City city : cities) {
				if(!(city.getElectricitySystem() instanceof ElectricitySystem.Local)) {
					continue;
				}

				ElectricitySystem.Local electricitySystem = 
						(ElectricitySystem.Local) city.getElectricitySystem();
				
				// Add constraints for city supply/demand, i.e. the in-flow less
				// out-flow (corrected for efficiency of distribution) must equal
				// the total demand less any local production.
				double[] flowCoefficients = new double[numVariables];
				for(ElectricityElement element : elements) {
					if(city.getName().equals(element.getOrigin())) {
						// Set coefficient for in-flow to the distribution element.
						// Order origin first to never distribute in self loop.
						flowCoefficients[elements.indexOf(element)] = -1;
					} else if(city.getName().equals(element.getDestination())) {
						// Set coefficient for out-flow from the distribution element.
						flowCoefficients[elements.indexOf(element)] 
								= element.getDistributionEfficiency();
					}
				}
				// Allow petroleum burning in this city.
				flowCoefficients[elements.size() + cities.indexOf(city)] 
						= city.getGlobals().getElectricalIntensityOfBurningPetroleum();
				// Constrain in-flow to meet net demand.
				constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
						city.getTotalElectricityDemand() 
						- electricitySystem.getElectricityProduction()));

				// Set petroleum burn cost in each city.
				costCoefficients[elements.size() + cities.indexOf(city)] = 
						city.getGlobals().getPetroleumDomesticPrice();
				initialValues[elements.size() + cities.indexOf(city)] = 
						Math.max(0,electricitySystem.getPetroleumBurned());
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
				
				// For each flow variable, set the input in the
				// corresponding distribution element.
				for(int i = 0; i < elements.size(); i++) {
					// Add Math.min checks in case error exceeds bounds.
					elements.get(i).setElectricityInput(Math.min(
							output.getPoint()[i], 
							elements.get(i).getMaxElectricityInput()));
				}
			} catch(TooManyIterationsException ignore) { 
				// Don't overwrite existing values.
				ignore.printStackTrace();
			} catch(NoFeasibleSolutionException ignore) {
				// Don't overwrite existing values.
				ignore.printStackTrace();
			}
		}
		
		/**
		 * Optimize electricity production and distribution.
		 *
		 * @param optimizationOptions the optimization options
		 */
		@Override
		public void optimizeElectricityProductionAndDistribution(OptimizationOptions optimizationOptions) {
			List<City> cities = getSociety().getCities();
			List<? extends ElectricityElement> elements = getInternalElements();
			
			// Count number of variables.
			int numVariables = 2*elements.size() + cities.size();

			List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
			
			double[] costCoefficients = new double[numVariables];
			double[] initialValues = new double[numVariables];
			
			for(ElectricityElement element : elements) {
				// Constrain maximum production in each fixed element.
				double[] productionConstraint = new double[numVariables];
				productionConstraint[elements.indexOf(element)] = 1;
				constraints.add(new LinearConstraint(productionConstraint, 
						Relationship.LEQ, element.getMaxElectricityProduction()));
				
				// Constrain maximum throughput in each distribution element.
				double[] throughputConstraint = new double[numVariables];
				throughputConstraint[elements.size() + elements.indexOf(element)] = 1;
				constraints.add(new LinearConstraint(throughputConstraint, 
						Relationship.LEQ, element.getMaxElectricityInput()));

				// Minimize costs - most obvious cost is importing, though also 
				// minimize transportation even if free.
				costCoefficients[elements.indexOf(element)] 
						= element.getVariableOperationsCostOfElectricityProduction()
		                		 + element.getWaterIntensityOfElectricityProduction()
		                		 * (getSociety().getGlobals().getWaterDomesticPrice()
		                				 + optimizationOptions.getDeltaDomesticWaterPrice())
		                		 + element.getPetroleumIntensityOfElectricityProduction()
		                		 * (getSociety().getGlobals().getPetroleumDomesticPrice()
		                				 + optimizationOptions.getDeltaDomesticOilPrice());
				initialValues[elements.indexOf(element)] 
						= element.getElectricityProduction();

				// Set distribution cost.
				costCoefficients[elements.size() + elements.indexOf(element)] 
						= element.getVariableOperationsCostOfElectricityDistribution();
				initialValues[elements.size() + elements.indexOf(element)] 
						= element.getElectricityInput();
			}
			
			for(City city : cities) {
				if(!(city.getElectricitySystem() instanceof ElectricitySystem.Local)) {
					continue;
				}

				ElectricitySystem.Local electricitySystem = 
						(ElectricitySystem.Local) city.getElectricitySystem();
				
				// Constrain supply = demand in each city.
				double[] flowCoefficients = new double[numVariables];
				for(ElectricityElement element : elements) {
					if(city.getName().equals(element.getOrigin())) {
						flowCoefficients[elements.indexOf(element)] = 1;
					}
					
					if(city.getName().equals(element.getOrigin())) {
						// Set coefficient for in-flow to the distribution element.
						// Order origin first to never distribute in self loop.
						flowCoefficients[elements.size() + elements.indexOf(element)] 
								= -1;
					} else if(city.getName().equals(element.getDestination())) {
						// Set coefficient for out-flow from the distribution element.
						flowCoefficients[elements.size() + elements.indexOf(element)] 
								= element.getDistributionEfficiency();
					}
				}
				// Allow burning of petroleum in this city.
				flowCoefficients[2*elements.size() + cities.indexOf(city)] 
						= city.getGlobals().getElectricalIntensityOfBurningPetroleum();
				
				// Constrain in-flow and production to meet demand.
				constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
						city.getTotalElectricityDemand()));

				// Set petroleum burn cost in each city.
				costCoefficients[2*elements.size() + cities.indexOf(city)] = 
						city.getGlobals().getPetroleumDomesticPrice()
						+ optimizationOptions.getDeltaDomesticOilPrice();
				initialValues[2*elements.size() + cities.indexOf(city)] = 
						Math.max(0,electricitySystem.getPetroleumBurned());
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
		
				for(int i = 0; i < elements.size(); i++) {
					// Add Math.min checks in case error exceeds bounds.
					elements.get(i).setElectricityProduction(Math.min(
							output.getPoint()[i],
							elements.get(i).getMaxElectricityProduction()));
					elements.get(i).setElectricityInput(Math.min(
							output.getPoint()[elements.size() + i],
							elements.get(i).getMaxElectricityInput()));
				}
			} catch(TooManyIterationsException ignore) { 
				// Don't overwrite existing values.
				ignore.printStackTrace();
			} catch(NoFeasibleSolutionException ignore) {
				// Don't overwrite existing values.
				ignore.printStackTrace();
			}
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.electricity.ElectricitySystem.Local#removeElement(edu.mit.sips.core.electricity.ElectricityElement)
		 */
		@Override
		public boolean removeElement(ElectricityElement element) {
			for(ElectricitySystem.Local system : getNestedSystems()) {
				if(system.getSociety().getName().equals(element.getOrigin())) {
					return system.removeElement(element);
				}
			}
			return false;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.SimEntity#tick()
		 */
		@Override
		public void tick() { }

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.SimEntity#tock()
		 */
		@Override
		public void tock() { }
	}

	/**
	 * Instantiates a new default electricity so s.
	 */
	public DefaultElectricitySoS() {
		super("Electricity");
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSoS#getNestedSystems()
	 */
	@Override
	public List<ElectricitySystem> getNestedSystems() {
		List<ElectricitySystem> systems = new ArrayList<ElectricitySystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			systems.add(society.getElectricitySystem());
		}
		return Collections.unmodifiableList(systems);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getPetroleumConsumption()
	 */
	@Override
	public double getPetroleumConsumption() {
		double value = 0;
		for(ElectricitySystem system : getNestedSystems()) {
			value += system.getPetroleumConsumption();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getWaterConsumption()
	 */
	@Override
	public double getWaterConsumption() {
		double value = 0;
		for(ElectricitySystem system : getNestedSystems()) {
			value += system.getWaterConsumption();
		}
		return value;
	}
}
