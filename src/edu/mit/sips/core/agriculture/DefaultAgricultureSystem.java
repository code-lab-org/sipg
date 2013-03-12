package edu.mit.sips.core.agriculture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.linear.ArrayRealVector;
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
public abstract class DefaultAgricultureSystem implements AgricultureSystem {
	
	/**
	 * The Class Local.
	 */
	public static abstract class Local extends DefaultInfrastructureSystem.Local implements AgricultureSystem.Local {
		
		/**
		 * Instantiates a new local.
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
			return getSociety().getGlobals().getWaterDomesticPrice()
					* getWaterConsumption();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getDistributionExpense()
		 */
		@Override
		public double getDistributionExpense() {
			return getSociety().getGlobals().getFoodDomesticPrice()
					* getFoodInDistribution();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getDistributionRevenue()
		 */
		@Override
		public double getDistributionRevenue() {
			return getSociety().getGlobals().getFoodDomesticPrice()
					* getFoodOutDistribution();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getEconomicProduction()
		 */
		@Override
		public double getDomesticProduction() {
			return getSociety().getGlobals().getFoodDomesticPrice() 
					* getFoodProduction()
					+ getExportRevenue() - getImportExpense();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getElements()
		 */
		@Override
		public List<AgricultureElement> getElements() {
			List<AgricultureElement> elements = new ArrayList<AgricultureElement>();
			elements.addAll(getInternalElements());
			elements.addAll(getExternalElements());
			return Collections.unmodifiableList(elements);
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getExportRevenue()
		 */
		@Override
		public double getExportRevenue() {
			return getSociety().getGlobals().getFoodExportPrice() * getFoodExport();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getExternalElements()
		 */
		@Override
		public List<AgricultureElement> getExternalElements() {
			List<AgricultureElement> elements = new ArrayList<AgricultureElement>();
			
			// TODO bad practice, assuming super-system is also local
			AgricultureSystem.Local agricultureSystem = 
					(AgricultureSystem.Local)getSociety().getSociety().getAgricultureSystem();
			for(AgricultureElement element : agricultureSystem.getElements()) {
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
		 * @see edu.mit.sips.AgricultureSystem#getFoodExport()
		 */
		@Override
		public double getFoodExport() {
			return Math.max(0, getFoodProduction() 
					+ getFoodInDistribution()
					- getFoodOutDistribution()
					- getSociety().getTotalFoodDemand());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getFoodImport()
		 */
		@Override
		public double getFoodImport() {
			return Math.max(0, getSociety().getTotalFoodDemand() 
					+ getFoodOutDistribution() 
					- getFoodInDistribution()
					- getFoodProduction());
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getFoodInDistribution()
		 */
		@Override
		public double getFoodInDistribution() {
			double distribution = 0;
			for(AgricultureElement e : getExternalElements()) {
				distribution += e.getFoodOutput();
			}
			return distribution;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getFoodOutDistribution()
		 */
		@Override
		public double getFoodOutDistribution() {
			double distribution = 0;
			for(AgricultureElement e : getInternalElements()) {
				if(e.getDestination() == e.getOrigin()) {
					// if a self-loop, only add distribution losses
					distribution += e.getFoodInput() - e.getFoodOutput();
				} else {
					distribution += e.getFoodInput();
				}
			}
			return distribution;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getFoodProduction()
		 */
		@Override
		public double getFoodProduction() {
			double foodProduction = 0;
			for(AgricultureElement e : getInternalElements()) {
				foodProduction += e.getFoodProduction();
			}
			return foodProduction;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getImportExpense()
		 */
		@Override
		public double getImportExpense() {
			return getSociety().getGlobals().getFoodImportPrice() * getFoodImport();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
		 */
		@Override
		public List<AgricultureElement> getInternalElements() {
			List<AgricultureElement> elements = new ArrayList<AgricultureElement>();
			// TODO bad practice, assuming super-system is also local
			AgricultureSystem.Local agricultureSystem = 
					(AgricultureSystem.Local)getSociety().getSociety().getAgricultureSystem();
			for(AgricultureElement element : agricultureSystem.getInternalElements()) {
				// add element if origin is inside this society
				if(getSociety().getCities().contains(
						getSociety().getCountry().getCity(element.getOrigin()))) {
					elements.add(element);
				}
			}
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getLandAreaUsed()
		 */
		@Override
		public double getLandAreaUsed() {
			double landAreaUsed = 0;
			for(AgricultureElement e : getInternalElements()) {
				landAreaUsed += e.getLandArea();
			}
			return landAreaUsed;
		}
		
		/**
		 * Gets the local food fraction.
		 *
		 * @return the local food fraction
		 */
		@Override
		public double getLocalFoodFraction() {
			if(getSociety().getTotalFoodDemand() > 0) {
				return Math.max(0, (getFoodProduction()
						- getFoodOutDistribution()
						- getFoodExport()))
						/ getSociety().getTotalFoodDemand();
			} 
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getLocalFoodSupply()
		 */
		@Override
		public double getLocalFoodSupply() {
			return getFoodProduction() 
					+ getFoodInDistribution() 
					- getFoodOutDistribution();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getProductionRevenue()
		 */
		@Override
		public double getSalesRevenue() {
			return getSociety().getGlobals().getFoodDomesticPrice() 
					* getSociety().getTotalFoodDemand();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getTotalFoodSupply()
		 */
		@Override
		public double getTotalFoodSupply() {
			return getLocalFoodSupply() + getFoodImport() - getFoodExport();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getWaterConsumption()
		 */
		@Override
		public double getWaterConsumption() {
			double waterConsumption = 0;
			for(AgricultureElement e : getInternalElements()) {
				waterConsumption += e.getWaterConsumption();
			}
			return waterConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#optimizeFoodDistribution()
		 */
		@Override
		public void optimizeFoodDistribution() {
			// Make a list of cities and infrastructure elements. The vector
			// of decision variables includes the throughput of each distribution
			// element and the import and export amounts in each city.
			List<City> cities = getSociety().getCities();
			List<AgricultureElement> elements = getInternalElements();
			
			// Count number of variables.
			int numVariables = elements.size() + 2*cities.size();
			
			// Create a list to hold the linear constraints.
			List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

			double[] costCoefficients = new double[numVariables];
			double[] initialValues = new double[numVariables];

			for(AgricultureElement element : elements) {
				// Add constraints for distribution throughput, i.e. the
				// throughput for each distribution element cannot exceed the maximum.
				double[] distributionConstraint = new double[numVariables];
				distributionConstraint[elements.indexOf(element)] = 1;
				constraints.add(new LinearConstraint(distributionConstraint, 
						Relationship.LEQ, element.getMaxFoodInput()));
				
				// Set distribution cost.
				costCoefficients[elements.indexOf(element)] 
						= element.getVariableOperationsCostOfFoodDistribution();
				initialValues[elements.indexOf(element)] 
						= element.getFoodInput();
			}
			
			for(City city : cities) {
				// TODO bad practice: assuming all nested cities have local systems
				AgricultureSystem.Local agricultureSystem = 
						(AgricultureSystem.Local) city.getAgricultureSystem();
				
				// Add constraints for city supply/demand, i.e. the in-flow less
				// out-flow (corrected for efficiency of distribution) must equal
				// the total demand less any local production.
				double[] flowCoefficients = new double[numVariables];
				for(AgricultureElement element : elements) {
					if(city.getName().equals(element.getOrigin())) {
						// Set coefficient for in-flow to the distribution element.
						// Order origin first to never distribute in self loop.
						flowCoefficients[elements.indexOf(element)] 
								= -1;
					} else if(city.getName().equals(element.getDestination())) {
						// Set coefficient for out-flow from the distribution element.
						flowCoefficients[elements.indexOf(element)] 
								= element.getDistributionEfficiency();
					}
				}
				// Allow import in this city.
				flowCoefficients[elements.size() + cities.indexOf(city)] = 1;
				// Allow export from this city.
				flowCoefficients[elements.size() + cities.size() + cities.indexOf(city)] = -1;
				// Constrain in-flow to meet net demand.
				constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
						city.getTotalFoodDemand() 
						- agricultureSystem.getFoodProduction()));

				// Set import cost in each city.
				costCoefficients[elements.size() + cities.indexOf(city)] = 
						city.getGlobals().getFoodImportPrice();
				initialValues[elements.size() + cities.indexOf(city)] = 
						agricultureSystem.getFoodImport();
				// Set export price in each city.
				costCoefficients[elements.size() + cities.size() + cities.indexOf(city)] = 
						-city.getGlobals().getFoodExportPrice();
				initialValues[elements.size() + cities.size() + cities.indexOf(city)] = 
						agricultureSystem.getFoodExport();
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
					elements.get(i).setFoodInput(Math.min(output.getPoint()[i],
							elements.get(i).getMaxFoodInput()));
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
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#optimizeFoodProductionAndDistribution(double)
		 */
		@Override
		public void optimizeFoodProductionAndDistribution(double deltaProductionCost) {
			List<City> cities = getSociety().getCities();
			List<AgricultureElement> elements = getInternalElements();
			
			// Count number of variables.
			int numVariables = 2*elements.size() + 2*cities.size();

			List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
			
			double[] costCoefficients = new double[numVariables];
			double[] initialValues = new double[numVariables];
			
			for(AgricultureElement element : elements) {
				// Constrain maximum land use in each fixed element.
				double[] productionConstraint = new double[numVariables];
				productionConstraint[elements.indexOf(element)] = 1;
				constraints.add(new LinearConstraint(productionConstraint, 
						Relationship.LEQ, element.getMaxLandArea()));

				// Constrain maximum throughput in each distribution element.
				double[] throughputConstraint = new double[numVariables];
				throughputConstraint[elements.size() + elements.indexOf(element)] = 1;
				constraints.add(new LinearConstraint(throughputConstraint, 
						Relationship.LEQ, element.getMaxFoodInput()));

				// Minimize costs - most obvious cost is importing, though also 
				// minimize transportation even if free.
				costCoefficients[elements.indexOf(element)] = 
						element.getProduct().getCostIntensityOfLandUsed() 
						+ element.getProduct().getWaterIntensityOfLandUsed()
						* getSociety().getGlobals().getWaterDomesticPrice()
						+ deltaProductionCost;
				initialValues[elements.indexOf(element)] = element.getLandArea();

				// Set the distribution cost.
				costCoefficients[elements.size() + elements.indexOf(element)] 
						= element.getVariableOperationsCostOfFoodDistribution();
				initialValues[elements.size() + elements.indexOf(element)] 
						= element.getFoodInput();
			}
			
			for(City city : cities) {
				// TODO bad practice: assuming all nested cities have local systems
				AgricultureSystem.Local agricultureSystem = 
						(AgricultureSystem.Local) city.getAgricultureSystem();
				
				// Constrain maximum arable land and labor in each city.
				double[] resourceConstraint = new double[numVariables];
				double[] laborConstraint = new double[numVariables];
				for(AgricultureElement element : elements) {
					if(city.getName().equals(element.getOrigin())) {
						resourceConstraint[elements.indexOf(element)] = 1.0;
						laborConstraint[elements.indexOf(element)] = 
								element.getProduct().getLaborIntensityOfLandUsed();
					}
				}
				constraints.add(new LinearConstraint(resourceConstraint, 
						Relationship.LEQ, agricultureSystem.getArableLandArea()));
				constraints.add(new LinearConstraint(laborConstraint, 
						Relationship.LEQ, city.getSocialSystem().getPopulation() 
						* city.getGlobals().getAgricultureLaborParticipationRate()));
				
				// Constrain supply = demand in each city.
				double[] flowCoefficients = new double[numVariables];
				for(AgricultureElement element : elements) {
					if(city.getName().equals(element.getOrigin())) {
						flowCoefficients[elements.indexOf(element)] 
								= element.getProduct().getFoodIntensityOfLandUsed();
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
				// Allow export from this city.
				flowCoefficients[2*elements.size() + cities.size() + cities.indexOf(city)] = -1;
				
				// Constrain in-flow and production to meet demand.
				constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
						city.getTotalFoodDemand()));

				// Set import cost in each city.
				costCoefficients[2*elements.size() + cities.indexOf(city)] 
						= city.getGlobals().getFoodImportPrice();
				initialValues[2*elements.size() + cities.indexOf(city)] 
						= agricultureSystem.getFoodImport();
				// Set export price in each city.
				costCoefficients[2*elements.size() + cities.size() + cities.indexOf(city)] 
						= -city.getGlobals().getFoodExportPrice();
				initialValues[2*elements.size() + cities.size() + cities.indexOf(city)] 
						= agricultureSystem.getFoodExport();
			}
			
			try {
				// Run optimization and get results.
				
				// NOTE: reduce epsilon from 1e-6 (default) to 1e-3 to cope with 
				// large magnitude differences between variables. Keep max ulps
				// set at the default (10).
				PointValuePair output = new SimplexSolver(1e-3, 10).optimize(
						GoalType.MINIMIZE,
						new MaxIter(1000),
						new NonNegativeConstraint(true), 
						new LinearConstraintSet(constraints), 
						new LinearObjectiveFunction(costCoefficients, 0d),
						new InitialGuess(initialValues));
		
				for(int i = 0; i < elements.size(); i++) {
					// Add Math.min checks in case error exceeds bounds.
					elements.get(i).setLandArea(Math.min(output.getPoint()[i],
							elements.get(i).getMaxLandArea()));
					elements.get(i).setFoodInput(Math.min(
							output.getPoint()[elements.size() + i],
							elements.get(i).getMaxFoodInput()));
				}
			} catch(TooManyIterationsException ignore) { 
				// Don't overwrite existing values.
				ignore.printStackTrace();
			} catch(NoFeasibleSolutionException ignore) {
				// Don't overwrite existing values.
				ignore.printStackTrace();
				System.out.print("A = [");
				for(LinearConstraint constraint : constraints) {
					if(constraint.getRelationship()==Relationship.LEQ) {
						System.out.print("[");
						for(double d : constraint.getCoefficients().toArray()) {
							System.out.print(d + " ");
						}
						System.out.println("];");
					}
				}
				System.out.println("];");
				
				System.out.print("b = [");
				for(LinearConstraint constraint : constraints) {
					if(constraint.getRelationship()==Relationship.LEQ) {
						System.out.print(constraint.getValue() + " ");
					}
				}
				System.out.println("]';");
				System.out.print("Aeq = [");
				for(LinearConstraint constraint : constraints) {
					if(constraint.getRelationship()==Relationship.EQ) {
						System.out.print("[");
						for(double d : constraint.getCoefficients().toArray()) {
							System.out.print(d + " ");
						}
						System.out.println("];");
					}
				}
				System.out.println("];");
				
				System.out.print("beq = [");
				for(LinearConstraint constraint : constraints) {
					if(constraint.getRelationship()==Relationship.EQ) {
						System.out.print(constraint.getValue() + " ");
					}
				}
				System.out.println("]';");
				
				System.out.print("f = [");
				for(double d : new ArrayRealVector(costCoefficients).toArray()) {
					System.out.print(d + " ");
				}
				System.out.println("]';");
				
				System.out.print("x0 = [");
				for(double d : new ArrayRealVector(initialValues).toArray()) {
					System.out.print(d + " ");
				}
				System.out.println("];");
			}
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getUnitProductionCost()
		 */
		@Override
		public double getUnitProductionCost() {
			if(getFoodProduction() > 0) {
				return (getLifecycleExpense() + getConsumptionExpense()) 
						/ getFoodProduction();
			}
			return 0;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#geUnitSupplyCost()
		 */
		@Override
		public double getUnitSupplyProfit() {
			if(getTotalFoodSupply() > 0) {
				return getCashFlow() / getTotalFoodSupply();
			}
			return 0;
		}
	}
	
	/**
	 * The Class Remote.
	 */
	public static class Remote extends DefaultInfrastructureSystem.Remote implements AgricultureSystem.Remote {
		private double waterConsumption;
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getWaterConsumption()
		 */
		@Override
		public double getWaterConsumption() {
			return waterConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Remote#setWaterConsumption(double)
		 */
		@Override
		public void setWaterConsumption(double waterConsumption) {
			this.waterConsumption = waterConsumption;
		}
	}
}
