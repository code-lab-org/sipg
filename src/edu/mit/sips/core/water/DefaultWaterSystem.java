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
import edu.mit.sips.core.DefaultInfrastructureSystem;

/**
 * The Class DefaultAgricultureSystem.
 */
public abstract class DefaultWaterSystem implements WaterSystem {

	/**
	 * The Class Local.
	 */
	public static abstract class Local extends DefaultInfrastructureSystem.Local implements WaterSystem.Local {

		/**
		 * Instantiates a new city water system.
		 *
		 * @param name the name
		 */
		public Local(String name) {
			super(name);
		}
		
		/**
		 * Instantiates a new local.
		 */
		protected Local() {
			
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getConsumptionExpense()
		 */
		@Override
		public double getConsumptionExpense() {
			return getSociety().getGlobals().getElectricityDomesticPrice()
					* getElectricityConsumption();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getDistributionExpense()
		 */
		@Override
		public double getDistributionExpense() {
			return getSociety().getGlobals().getWaterDomesticPrice()
					* getWaterInDistribution();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getDistributionRevenue()
		 */
		@Override
		public double getDistributionRevenue() {
			return getSociety().getGlobals().getWaterDomesticPrice()
					* getWaterOutDistribution();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getEconomicProduction()
		 */
		@Override
		public double getDomesticProduction() {
			return (getSociety().getGlobals().getWaterDomesticPrice()
					+ getSociety().getGlobals().getEconomicIntensityOfWaterProduction())
					* (getWaterProduction() + getWaterFromArtesianWell())
					+ getExportRevenue()  - getImportExpense();
		}


		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getEnergyConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			double energyConsumption = 0;
			for(WaterElement e : getInternalElements()) {
				energyConsumption += e.getElectricityConsumption();
			}
			return energyConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getElements()
		 */
		@Override
		public List<WaterElement> getElements() {
			List<WaterElement> elements = new ArrayList<WaterElement>();
			elements.addAll(getInternalElements());
			elements.addAll(getExternalElements());
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getExportRevenue()
		 */
		@Override
		public double getExportRevenue() {
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getExternalElements()
		 */
		@Override
		public List<WaterElement> getExternalElements() {
			List<WaterElement> elements = new ArrayList<WaterElement>();
			for(WaterElement element : getNationalWaterSystem().getElements()) {
				// add element if origin is outside this society but destination
				// is within this society
				if(!getSociety().getCities().contains(
						getSociety().getCountry().getCity(element.getOrigin()))
						&& getSociety().getCities().contains(
								getSociety().getCountry().getCity(
										element.getDestination()))) {
					elements.add(element);
				}
			}
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getImportExpense()
		 */
		@Override
		public double getImportExpense() {
			return getSociety().getGlobals().getWaterImportPrice() * getWaterImport();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
		 */
		@Override
		public List<WaterElement> getInternalElements() {
			List<WaterElement> elements = new ArrayList<WaterElement>();
			for(WaterElement element : getNationalWaterSystem().getElements()) {
				// add element if origin is inside this society
				if(getSociety().getCities().contains(
						getSociety().getCountry().getCity(element.getOrigin()))) {
					elements.add(element);
				}
			}
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getLocalWaterFraction()
		 */
		@Override
		public double getLocalWaterFraction() {
			if(getSociety().getTotalWaterDemand() > 0) {
				return Math.max(0, getWaterProduction()
						+ getWaterFromArtesianWell()
						- getWaterOutDistribution()
						- getWaterWasted())
						/ getSociety().getTotalWaterDemand();
			} 
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getLocalWaterSupply()
		 */
		@Override
		public double getLocalWaterSupply() {
			return getWaterProduction() 
					+ getWaterFromArtesianWell()
					+ getWaterInDistribution()
					- getWaterOutDistribution();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getNationalWaterSystem()
		 */
		@Override
		public WaterSystem.Local getNationalWaterSystem() {
			return (WaterSystem.Local) getSociety().getCountry().getWaterSystem();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getProductionCost()
		 */
		@Override
		public double getProductionCost() {
			if(getWaterProduction() > 0) {
				return getLifecycleExpense() / getWaterProduction();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getRenewableWaterFraction()
		 */
		@Override
		public double getRenewableWaterFraction() {
			if(getWaterProduction() + getWaterFromArtesianWell() > 0) {
				return getRenewableWaterProduction() 
						/ (getWaterProduction() + getWaterFromArtesianWell());
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getRenewableWaterProduction()
		 */
		@Override
		public double getRenewableWaterProduction() {
			double renewableProduction = 0;
			for(WaterElement e : getInternalElements()) {
				if(e.getReservoirIntensityOfWaterProduction() < 1) {
					renewableProduction += e.getWaterProduction() 
							* (1 - e.getReservoirIntensityOfWaterProduction());
				}
			}
			renewableProduction += Math.min(getWaterReservoirRechargeRate(), 
					getWaterProduction() + getWaterFromArtesianWell() 
					- renewableProduction);
			return renewableProduction;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getWaterWithdrawals()
		 */
		@Override
		public double getReservoirWaterWithdrawals() {
			double waterWithdrawals = 0;
			for(WaterElement e : getInternalElements()) {
				waterWithdrawals += e.getWaterWithdrawals();
			}
			return waterWithdrawals;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getProductionRevenue()
		 */
		@Override
		public double getSalesRevenue() {
			return getSociety().getGlobals().getWaterDomesticPrice()
					* (getSociety().getTotalWaterDemand() - getWaterFromArtesianWell());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getSupplyCost()
		 */
		@Override
		public double getSupplyCost() {
			if(getTotalWaterSupply() > 0) {
				return getTotalExpense() / getTotalWaterSupply();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getTotalWaterSupply()
		 */
		public double getTotalWaterSupply() {
			return getLocalWaterSupply() 
					+ getWaterImport();
		}


		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getWaterFromArtesianWell()
		 */
		@Override
		public double getWaterFromArtesianWell() {
			// Artesian water used to meet shortfall in reaching minimum demand.
			return Math.min(getWaterReservoirVolume() - getReservoirWaterWithdrawals(), 
					Math.max(0, getSociety().getTotalWaterDemand()
							+ getWaterOutDistribution()
							- getWaterInDistribution()
							- getWaterProduction()));
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getWaterImport()
		 */
		@Override
		public double getWaterImport() {
			// Water is imported to meet shortfall in reaching minimum demand.
			// Note that water cannot be exported, and is wasted if excess.
			return Math.max(0, getSociety().getTotalWaterDemand()
					+ getWaterOutDistribution()
					- getWaterInDistribution()
					- getWaterProduction()
					- getWaterFromArtesianWell());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getWaterInDistribution()
		 */
		@Override
		public double getWaterInDistribution() {
			double distribution = 0;
			for(WaterElement e : getExternalElements()) {
				distribution += e.getWaterOutput();
			}
			return distribution;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getWaterOutDistribution()
		 */
		@Override
		public double getWaterOutDistribution() {
			double distribution = 0;
			for(WaterElement e : getInternalElements()) {
				if(e.getDestination() == e.getOrigin()) {
					// if a self-loop, only add distribution losses
					distribution += e.getWaterInput() - e.getWaterOutput();
				} else {
					distribution += e.getWaterInput();
				}
			}
			return distribution;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getWaterProduction()
		 */
		@Override
		public double getWaterProduction() {
			double waterProduction = 0;
			for(WaterElement e : getInternalElements()) {
				waterProduction += e.getWaterProduction();
			}
			return waterProduction;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getWaterWasted()
		 */
		@Override
		public double getWaterWasted() {
			// Water is wasted if supply exceeds maximum demand.
			return Math.max(0, getWaterProduction() 
					+ getWaterFromArtesianWell()
					+ getWaterInDistribution()
					- getWaterOutDistribution()
					- getSociety().getTotalWaterDemand());
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#optimizeWaterDistribution()
		 */
		@Override
		public void optimizeWaterDistribution() {
			// Make a list of cities and infrastructure elements. The vector
			// of decision variables includes the throughput of each distribution
			// element and the import and export amounts in each city.
			List<City> cities = getSociety().getCities();
			List<WaterElement> elements = getInternalElements();

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
						* getSociety().getGlobals().getElectricityDomesticPrice();
				initialValues[elements.indexOf(element)] 
						= element.getWaterInput();
			}

			for(City city : cities) {
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
						city.getGlobals().getWaterImportPrice();
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
		public void optimizeWaterProductionAndDistribution(double deltaProductionCost) {
			List<City> cities = getSociety().getCities();
			List<WaterElement> elements = getInternalElements();

			// Count number of variables.
			int numVariables = 2*elements.size() + cities.size();

			List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

			double[] costCoefficients = new double[numVariables];
			double[] initialValues = new double[numVariables];

			for(WaterElement element : elements) {
				// Constrain maximum production in each fixed element.
				double[] productionConstraint = new double[numVariables];
				productionConstraint[elements.indexOf(element)] = 1;
				constraints.add(new LinearConstraint(productionConstraint, 
						Relationship.LEQ, element.getMaxWaterProduction()));

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
						* getSociety().getGlobals().getElectricityDomesticPrice()
						+ deltaProductionCost;
				initialValues[elements.indexOf(element)] 
						= element.getWaterProduction();

				// Set a distribution cost using variable operations expense.
				costCoefficients[elements.size() + elements.indexOf(element)] 
						= element.getVariableOperationsCostOfWaterDistribution()
						+ element.getElectricalIntensityOfWaterDistribution()
						* getSociety().getGlobals().getElectricityDomesticPrice();
				initialValues[elements.size() + elements.indexOf(element)] 
						= element.getWaterInput();
			}

			for(City city : cities) {
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

				// Constrain in-flow and production to meet demand.
				constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
						city.getTotalWaterDemand()));

				// Set import cost in each city.
				costCoefficients[2*elements.size() + cities.indexOf(city)] 
						= city.getGlobals().getWaterImportPrice();
				initialValues[2*elements.size() + cities.indexOf(city)] 
						= waterSystem.getWaterImport();
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
			} catch(TooManyIterationsException ignore) { 
				// Don't overwrite existing values.
				ignore.printStackTrace();
			} catch(NoFeasibleSolutionException ignore) {
				// Don't overwrite existing values.
				ignore.printStackTrace();
			}
		}
	}

	/**
	 * The Class Remote.
	 */
	public static class Remote extends DefaultInfrastructureSystem.Remote implements WaterSystem.Remote {
		private double electricityConsumption;
		private double waterSupplyPerCapita;

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem#getElectricityConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			return electricityConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem#getWaterSupplyPerCapita()
		 */
		@Override
		public double getWaterSupplyPerCapita() {
			return waterSupplyPerCapita;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Remote#setElectricityConsumption(double)
		 */
		@Override
		public void setElectricityConsumption(double electricityConsumption) {
			this.electricityConsumption = electricityConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Remote#setWaterSupplyPerCapita(double)
		 */
		@Override
		public void setWaterSupplyPerCapita(double waterSupplyPerCapita) {
			this.waterSupplyPerCapita = waterSupplyPerCapita;
		}

	}
}