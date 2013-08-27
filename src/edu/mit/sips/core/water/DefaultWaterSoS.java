package edu.mit.sips.core.water;

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
 * The Class DefaultWaterSoS.
 */
public class DefaultWaterSoS extends DefaultInfrastructureSoS implements WaterSoS {
	
	/**
	 * The Class Local.
	 */
	public static class Local extends DefaultInfrastructureSoS.Local implements WaterSoS.Local {
		
		/**
		 * Instantiates a new local.
		 */
		public Local() {
			super("Water");
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#addElement(edu.mit.sips.core.water.WaterElement)
		 */
		@Override
		public boolean addElement(WaterElement element) {
			for(WaterSystem.Local system : getNestedSystems()) {
				if(system.getSociety().getName().equals(element.getOrigin())) {
					return system.addElement(element);
				}
			}
			return false;
		}


		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem#getElectricityConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			double value = 0;
			for(WaterSystem system : getNestedSystems()) {
				value += system.getElectricityConsumption();
			}
			return value;
		}


		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getElements()
		 */
		@Override
		public List<? extends WaterElement> getElements() {
			List<WaterElement> elements = new ArrayList<WaterElement>();
			elements.addAll(getInternalElements());
			elements.addAll(getExternalElements());
			return Collections.unmodifiableList(elements);
		}


		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getExternalElements()
		 */
		@Override
		public List<? extends WaterElement> getExternalElements() {
			List<WaterElement> elements = new ArrayList<WaterElement>();
			if(!getSociety().equals(getSociety().getCountry())) {
				for(WaterSystem.Local system : getNestedSystems()) {
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
		public List<? extends WaterElement> getInternalElements() {
			List<WaterElement> elements = new ArrayList<WaterElement>();
			for(WaterSystem.Local system : getNestedSystems()) {
				elements.addAll(system.getInternalElements());
			}
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getLocalWaterFraction()
		 */
		@Override
		public double getLocalWaterFraction() {
			if(getSociety().getTotalWaterDemand() > 0) {
				return Math.min(1, (getWaterProduction() + getWaterFromArtesianWell())
						/ getSociety().getTotalWaterDemand());
			} 
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getMaxWaterReservoirVolume()
		 */
		@Override
		public double getMaxWaterReservoirVolume() {
			double value = 0;
			for(WaterSystem.Local system : getNestedSystems()) {
				value += system.getMaxWaterReservoirVolume();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.DefaultInfrastructureSoS.Local#getNestedSystems()
		 */
		@Override
		public List<WaterSystem.Local> getNestedSystems() {
			List<WaterSystem.Local> systems = new ArrayList<WaterSystem.Local>();
			for(Society society : getSociety().getNestedSocieties()) {
				if(society.getWaterSystem() instanceof WaterSystem.Local){ 
					systems.add((WaterSystem.Local) society.getWaterSystem());
				}
			}
			return Collections.unmodifiableList(systems);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getRenewableWaterFraction()
		 */
		@Override
		public double getRenewableWaterFraction() {
			if(getSociety().getTotalWaterDemand() > 0) {
				return getRenewableWaterProduction() 
						/ getSociety().getTotalWaterDemand();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getRenewableWaterProduction()
		 */
		@Override
		public double getRenewableWaterProduction() {
			double value = 0;
			for(WaterSystem.Local system : getNestedSystems()) {
				value += system.getRenewableWaterProduction();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getReservoirWaterWithdrawals()
		 */
		@Override
		public double getReservoirWaterWithdrawals() {
			double value = 0;
			for(WaterSystem.Local system : getNestedSystems()) {
				value += system.getReservoirWaterWithdrawals();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getTotalWaterSupply()
		 */
		@Override
		public double getTotalWaterSupply() {
			double value = 0;
			for(WaterSystem.Local system : getNestedSystems()) {
				value += system.getTotalWaterSupply();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getUnitProductionCost()
		 */
		@Override
		public double getUnitProductionCost() {
			if(getWaterProduction() > 0) {
				return (getLifecycleExpense() + getConsumptionExpense()) 
						/ getWaterProduction();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getUnitSupplyProfit()
		 */
		@Override
		public double getUnitSupplyProfit() {
			if(getTotalWaterSupply() > 0) {
				return getCashFlow() / getTotalWaterSupply();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem#getWaterDomesticPrice()
		 */
		@Override
		public double getWaterDomesticPrice() {
			if(!getNestedSystems().isEmpty()) {
				double value = 0;
				for(WaterSystem system : getNestedSystems()) {
					value += system.getWaterDomesticPrice();
				}
				return value / getNestedSystems().size();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterFromArtesianWell()
		 */
		@Override
		public double getWaterFromArtesianWell() {
			double value = 0;
			for(WaterSystem.Local system : getNestedSystems()) {
				value += system.getWaterFromArtesianWell();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterImport()
		 */
		@Override
		public double getWaterImport() {
			double value = 0;
			for(WaterSystem.Local system : getNestedSystems()) {
				value += system.getWaterImport();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem#getWaterImportPrice()
		 */
		@Override
		public double getWaterImportPrice() {
			if(!getNestedSystems().isEmpty()) {
				double value = 0;
				for(WaterSystem system : getNestedSystems()) {
					value += system.getWaterImportPrice();
				}
				return value / getNestedSystems().size();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterInDistribution()
		 */
		@Override
		public double getWaterInDistribution() {
			double value = 0;
			for(WaterElement e : getExternalElements()) {
				value += e.getWaterOutput();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterOutDistribution()
		 */
		@Override
		public double getWaterOutDistribution() {
			double value = 0;
			for(WaterElement e : getInternalElements()) {
				if(!getSociety().getCities().contains(
						getSociety().getCountry().getCity(e.getDestination()))) {
					value += e.getWaterInput();
				}
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterOutDistributionLosses()
		 */
		@Override
		public double getWaterOutDistributionLosses() {
			double value = 0;
			for(WaterElement e : getInternalElements()) {
				value += e.getWaterInput() - e.getWaterOutput();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterProduction()
		 */
		@Override
		public double getWaterProduction() {
			double value = 0;
			for(WaterSystem.Local system : getNestedSystems()) {
				value += system.getWaterProduction();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterReservoirRechargeRate()
		 */
		@Override
		public double getWaterReservoirRechargeRate() {
			double value = 0;
			for(WaterSystem.Local system : getNestedSystems()) {
				value += system.getWaterReservoirRechargeRate();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterReservoirVolume()
		 */
		@Override
		public double getWaterReservoirVolume() {
			double value = 0;
			for(WaterSystem.Local system : getNestedSystems()) {
				value += system.getWaterReservoirVolume();
			}
			return value;
		}


		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterWasted()
		 */
		@Override
		public double getWaterWasted() {
			double value = 0;
			for(WaterSystem.Local system : getNestedSystems()) {
				value += system.getWaterWasted();
			}
			return value;
		}


		/* (non-Javadoc)
		 * @see edu.mit.sips.core.SimEntity#initialize(long)
		 */
		@Override
		public void initialize(long time) { }

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#optimizeWaterDistribution()
		 */
		@Override
		public void optimizeWaterDistribution() {
			// Make a list of cities and infrastructure elements. The vector
			// of decision variables includes the throughput of each distribution
			// element and the import and export amounts in each city.
			List<City> cities = getSociety().getCities();
			List<? extends WaterElement> elements = getInternalElements();

			// Count number of variables.
			int numVariables = elements.size() + cities.size();

			// Create a list to hold the linear constraints.
			List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

			double[] costCoefficients = new double[numVariables];
			double[] initialValues = new double[numVariables];

			for(WaterElement element : elements) {
				// Add constraints for distribution throughput, i.e. the
				// throughput for each distribution element cannot exceed the maximum.
				double[] distributionConstraint = new double[numVariables];
				distributionConstraint[elements.indexOf(element)] = 1;
				constraints.add(new LinearConstraint(distributionConstraint, 
						Relationship.LEQ, element.getMaxWaterInput()));

				// Set the distribution cost.
				costCoefficients[elements.indexOf(element)] 
						= element.getVariableOperationsCostOfWaterDistribution()
						+ element.getElectricalIntensityOfWaterDistribution()
						* getSociety().getElectricitySystem().getElectricityDomesticPrice();
				initialValues[elements.indexOf(element)] 
						= element.getWaterInput();
			}

			for(City city : cities) {
				if(!(city.getWaterSystem() instanceof WaterSystem.Local)) {
					continue;
				}
				
				WaterSystem.Local waterSystem = 
						(WaterSystem.Local) city.getWaterSystem();
				
				// Add constraints for city supply/demand, i.e. the in-flow less
				// out-flow (corrected for efficiency of distribution) must equal
				// the total demand less any local production.
				double[] flowCoefficients = new double[numVariables];
				for(WaterElement element : elements) {
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
				// Allow import in this city.
				flowCoefficients[elements.size() + cities.indexOf(city)] = 1;
				// Constrain in-flow to meet net demand.
				constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
						city.getTotalWaterDemand() 
						- waterSystem.getWaterProduction()));

				// Set import cost in each city.
				costCoefficients[elements.size() + cities.indexOf(city)] = 
						city.getWaterSystem().getWaterImportPrice();
				initialValues[elements.size() + cities.indexOf(city)] = 
						Math.max(0, waterSystem.getWaterImport());
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

				// For each flow variable, set the food input in the
				// corresponding distribution element.
				for(int i = 0; i < elements.size(); i++) {
					// Add Math.min checks in case error exceeds bounds.
					elements.get(i).setWaterInput(Math.min(output.getPoint()[i],
							elements.get(i).getMaxWaterInput()));
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
		 * @see edu.mit.sips.core.water.WaterSystem.Local#optimizeWaterProductionAndDistribution(double)
		 */
		@Override
		public void optimizeWaterProductionAndDistribution(OptimizationOptions optimizationOptions) {
			List<City> cities = getSociety().getCities();
			List<? extends WaterElement> elements = getInternalElements();

			// Count number of variables.
			int numVariables = 2*elements.size() + cities.size();

			List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

			double[] costCoefficients = new double[numVariables];
			double[] initialValues = new double[numVariables];

			for(WaterElement element : elements) {
				// Constrain maximum production in each fixed element.
				double[] productionConstraint = new double[numVariables];
				productionConstraint[elements.indexOf(element)] = 1;
				if(element.isCoastalAccessRequired() 
						&& !((WaterSystem.Local)getSociety().getCountry().getCity(
								element.getOrigin()).getWaterSystem()).isCoastalAccess()) {
					constraints.add(new LinearConstraint(productionConstraint, 
							Relationship.LEQ, 0));
				} else {
					constraints.add(new LinearConstraint(productionConstraint, 
							Relationship.LEQ, element.getMaxWaterProduction()));
				}

				// Constrain maximum throughput in each distribution element.
				double[] throughputConstraint = new double[numVariables];
				throughputConstraint[elements.size() + elements.indexOf(element)] = 1;
				constraints.add(new LinearConstraint(throughputConstraint, 
						Relationship.LEQ, element.getMaxWaterInput()));

				// Minimize costs - most obvious cost is importing, though also 
				// minimize transportation even if free.
				costCoefficients[elements.indexOf(element)] 
						= element.getVariableOperationsCostOfWaterProduction() 
						+ element.getElectricalIntensityOfWaterProduction()
						* (getSociety().getElectricitySystem().getElectricityDomesticPrice()
								+ optimizationOptions.getDeltaDomesticElectricityPrice());
				initialValues[elements.indexOf(element)] 
						= element.getWaterProduction();

				// Set a distribution cost using variable operations expense.
				costCoefficients[elements.size() + elements.indexOf(element)] 
						= element.getVariableOperationsCostOfWaterDistribution()
						+ element.getReservoirIntensityOfWaterProduction()
						* optimizationOptions.getDeltaAquiferWaterPrice()
						+ element.getElectricalIntensityOfWaterDistribution()
						* (getSociety().getElectricitySystem().getElectricityDomesticPrice()
								+ optimizationOptions.getDeltaDomesticElectricityPrice());
				initialValues[elements.size() + elements.indexOf(element)] 
						= element.getWaterInput();
			}

			for(City city : cities) {
				if(!(city.getWaterSystem() instanceof WaterSystem.Local)) {
					continue;
				}
				
				WaterSystem.Local waterSystem = 
						(WaterSystem.Local) city.getWaterSystem();
				
				// Constrain maximum resource in each city.
				double[] resourceConstraint = new double[numVariables];
				for(WaterElement element : elements) {
					if(city.getName().equals(element.getOrigin())) {
						resourceConstraint[elements.indexOf(element)] 
								= element.getReservoirIntensityOfWaterProduction();
					}
				}
				//resourceConstraint[2*elements.size() + cities.size() + cities.indexOf(city)] = 1;
				constraints.add(new LinearConstraint(resourceConstraint, 
						Relationship.LEQ, waterSystem.getWaterReservoirVolume()));

				// Constrain supply = demand in each city.
				double[] flowCoefficients = new double[numVariables];
				for(WaterElement element : elements) {
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
				// Allow import in this city.
				flowCoefficients[2*elements.size() + cities.indexOf(city)] = 1;
				
				// Allow artesian well in this city.
				//flowCoefficients[2*elements.size() + cities.size() + cities.indexOf(city)] = 1;

				// Constrain in-flow and production to meet demand.
				constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
						city.getTotalWaterDemand()));

				// Set import cost in each city.
				costCoefficients[2*elements.size() + cities.indexOf(city)] 
						= city.getWaterSystem().getWaterImportPrice() 
						+ optimizationOptions.getDeltaImportWaterPrice();
				initialValues[2*elements.size() + cities.indexOf(city)] 
						= waterSystem.getWaterImport();
				
				// Set artesian well cost in each city.
				//costCoefficients[2*elements.size() + cities.size() + cities.indexOf(city)] 
				//		= optimizationOptions.getDeltaAquiferWaterPrice();
				//initialValues[2*elements.size() + cities.size() + cities.indexOf(city)] 
				//		= waterSystem.getWaterFromArtesianWell();
				
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
					elements.get(i).setWaterProduction(Math.min(output.getPoint()[i],
							elements.get(i).getMaxWaterProduction()));
					elements.get(i).setWaterInput(Math.min(
							output.getPoint()[elements.size() + i],
							elements.get(i).getMaxWaterInput()));
				}
				//for(int i = 0; i < cities.size(); i++) {
				//	if(cities.get(i).getWaterSystem() instanceof WaterSystem.Local) { 
				//		((WaterSystem.Local) cities.get(i).getWaterSystem()).setWaterFromArtesianWell(
				//				output.getPoint()[2*elements.size() + cities.size() + i]);
				//	}
				//}
			} catch(TooManyIterationsException ignore) { 
				// Don't overwrite existing values.
				ignore.printStackTrace();
			} catch(NoFeasibleSolutionException ignore) {
				// Don't overwrite existing values.
				ignore.printStackTrace();
			}
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#removeElement(edu.mit.sips.core.water.WaterElement)
		 */
		@Override
		public boolean removeElement(WaterElement element) {
			for(WaterSystem.Local system : getNestedSystems()) {
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

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#isCoastalAccess()
		 */
		@Override
		public boolean isCoastalAccess() {
			for(WaterSystem.Local system : getNestedSystems()) {
				if(system.isCoastalAccess()) {
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * Instantiates a new default water so s.
	 */
	public DefaultWaterSoS() {
		super("Water");
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		double value = 0;
		for(WaterSystem system : getNestedSystems()) {
			value += system.getElectricityConsumption();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSoS#getNestedSystems()
	 */
	@Override
	public List<WaterSystem> getNestedSystems() {
		List<WaterSystem> systems = new ArrayList<WaterSystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			systems.add(society.getWaterSystem());
		}
		return Collections.unmodifiableList(systems);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterDomesticPrice()
	 */
	@Override
	public double getWaterDomesticPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(WaterSystem system : getNestedSystems()) {
				value += system.getWaterDomesticPrice();
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterImportPrice()
	 */
	@Override
	public double getWaterImportPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(WaterSystem system : getNestedSystems()) {
				value += system.getWaterImportPrice();
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}
}
