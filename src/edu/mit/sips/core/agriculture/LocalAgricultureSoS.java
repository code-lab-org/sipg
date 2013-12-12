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
import edu.mit.sips.core.LocalInfrastructureSoS;
import edu.mit.sips.core.OptimizationOptions;
import edu.mit.sips.core.Society;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

public class LocalAgricultureSoS extends LocalInfrastructureSoS implements AgricultureSoS.Local {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;
	
	public double getFoodSecurity() {
		return getTotalFoodSupply() == 0 ? 1 
				: (getFoodProduction() / getTotalFoodSupply());
	}

	/**
	 * Instantiates a new local.
	 */
	public LocalAgricultureSoS() {
		super("Agriculture");
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#addElement(edu.mit.sips.core.agriculture.AgricultureElement)
	 */
	@Override
	public boolean addElement(AgricultureElement element) {
		for(AgricultureSystem.Local system : getNestedSystems()) {
			if(system.getSociety().getName().equals(element.getOrigin())) {
				return system.addElement(element);
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getArableLandArea()
	 */
	@Override
	public double getArableLandArea() {
		double value = 0;
		for(AgricultureSystem.Local system : getNestedSystems()) {
			value += system.getArableLandArea();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getElements()
	 */
	@Override
	public List<? extends AgricultureElement> getElements() {
		List<AgricultureElement> elements = new ArrayList<AgricultureElement>();
		elements.addAll(getInternalElements());
		elements.addAll(getExternalElements());
		return Collections.unmodifiableList(elements);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getExternalElements()
	 */
	@Override
	public List<? extends AgricultureElement> getExternalElements() {
		List<AgricultureElement> elements = new ArrayList<AgricultureElement>();
		if(!getSociety().equals(getSociety().getCountry())) {
			for(AgricultureSystem.Local system : getNestedSystems()) {
				elements.addAll(system.getExternalElements());
			}
			elements.removeAll(getInternalElements());
		}
		return Collections.unmodifiableList(elements);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodDomesticPrice()
	 */
	@Override
	public double getFoodDomesticPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(AgricultureSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getFoodDomesticPrice(), 
						system.getCurrencyUnits(), system.getFoodUnits(),
						getCurrencyUnits(), getFoodUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getFoodExport()
	 */
	@Override
	public double getFoodExport() {
		double value = 0;
		for(AgricultureSystem.Local system : getNestedSystems()) {
			value += system.getFoodExport();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodExportPrice()
	 */
	@Override
	public double getFoodExportPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(AgricultureSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getFoodExportPrice(), 
						system.getCurrencyUnits(), system.getFoodUnits(),
						getCurrencyUnits(), getFoodUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getFoodImport()
	 */
	@Override
	public double getFoodImport() {
		double value = 0;
		for(AgricultureSystem.Local system : getNestedSystems()) {
			value += system.getFoodImport();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodImportPrice()
	 */
	@Override
	public double getFoodImportPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(AgricultureSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getFoodImportPrice(), 
						system.getCurrencyUnits(), system.getFoodUnits(),
						getCurrencyUnits(), getFoodUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getFoodInDistribution()
	 */
	@Override
	public double getFoodInDistribution() {
		double value = 0;
		for(AgricultureElement e : getExternalElements()) {
			value += e.getFoodOutput();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getFoodOutDistribution()
	 */
	@Override
	public double getFoodOutDistribution() {
		double value = 0;
		for(AgricultureElement e : getInternalElements()) {
			if(!getSociety().getCities().contains(
					getSociety().getCountry().getCity(e.getDestination()))) {
				value += e.getFoodInput();
			}
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getFoodOutDistributionLosses()
	 */
	@Override
	public double getFoodOutDistributionLosses() {
		double value = 0;
		for(AgricultureElement e : getInternalElements()) {
			value += e.getFoodInput() - e.getFoodOutput();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getFoodProduction()
	 */
	@Override
	public double getFoodProduction() {
		double value = 0;
		for(AgricultureSystem.Local system : getNestedSystems()) {
			value += system.getFoodProduction();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodTimeUnits()
	 */
	@Override
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodUnits()
	 */
	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getInternalElements()
	 */
	@Override
	public List<? extends AgricultureElement> getInternalElements() {
		List<AgricultureElement> elements = new ArrayList<AgricultureElement>();
		for(AgricultureSystem.Local system : getNestedSystems()) {
			elements.addAll(system.getInternalElements());
		}
		return Collections.unmodifiableList(elements);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getLaborParticipationRate()
	 */
	@Override
	public double getLaborParticipationRate() {
		if(getSociety().getSocialSystem().getPopulation() > 0) {
			double workers = 0;
			for(AgricultureSystem.Local system : getNestedSystems()) {
				workers += system.getLaborParticipationRate() * system.getSociety().getSocialSystem().getPopulation();
			}
			return workers / getSociety().getSocialSystem().getPopulation();
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getLaborUsed()
	 */
	@Override
	public long getLaborUsed() {
		long value = 0;
		for(AgricultureSystem.Local system : getNestedSystems()) {
			value += system.getLaborUsed();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getLandAreaUsed()
	 */
	@Override
	public double getLandAreaUsed() {
		double value = 0;
		for(AgricultureSystem.Local system : getNestedSystems()) {
			value += system.getLandAreaUsed();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getLocalFoodFraction()
	 */
	@Override
	public double getLocalFoodFraction() {
		if(getSociety().getTotalFoodDemand() > 0) {
			return Math.min(1, getFoodProduction() 
					/ getSociety().getTotalFoodDemand());
		} 
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getLocalFoodSupply()
	 */
	@Override
	public double getLocalFoodSupply() {
		double value = 0;
		for(AgricultureSystem.Local system : getNestedSystems()) {
			value += system.getLocalFoodSupply();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.DefaultInfrastructureSoS.Local#getNestedSystems()
	 */
	@Override
	public List<AgricultureSystem.Local> getNestedSystems() {
		List<AgricultureSystem.Local> systems = new ArrayList<AgricultureSystem.Local>();
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getAgricultureSystem() instanceof AgricultureSystem.Local){ 
				systems.add((AgricultureSystem.Local) society.getAgricultureSystem());
			}
		}
		return Collections.unmodifiableList(systems);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getTotalFoodSupply()
	 */
	@Override
	public double getTotalFoodSupply() {
		double value = 0;
		for(AgricultureSystem.Local system : getNestedSystems()) {
			value += system.getTotalFoodSupply();
		}
		return value;
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
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getUnitSupplyProfit()
	 */
	@Override
	public double getUnitSupplyProfit() {
		if(getTotalFoodSupply() > 0) {
			return getCashFlow() / getTotalFoodSupply();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getWaterConsumption()
	 */
	@Override
	public double getWaterConsumption() {
		double value = 0;
		for(AgricultureSystem system : getNestedSystems()) {
			value += system.getWaterConsumption();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterTimeUnits()
	 */
	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnits()
	 */
	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSoS.Local#optimizeFoodDistribution()
	 */
	@Override
	public void optimizeFoodDistribution() {
		// Make a list of cities and infrastructure elements. The vector
		// of decision variables includes the throughput of each distribution
		// element and the import and export amounts in each city.
		List<City> cities = getSociety().getCities();
		List<? extends AgricultureElement> elements = getInternalElements();

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
			if(!(city.getAgricultureSystem() instanceof AgricultureSystem.Local)) {
				continue;
			}

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
					city.getAgricultureSystem().getFoodImportPrice();
			initialValues[elements.size() + cities.indexOf(city)] = 
					agricultureSystem.getFoodImport();
			// Set export price in each city.
			costCoefficients[elements.size() + cities.size() + cities.indexOf(city)] = 
					-city.getAgricultureSystem().getFoodExportPrice();
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
	 * @see edu.mit.sips.core.agriculture.AgricultureSoS.Local#optimizeFoodProductionAndDistribution(double)
	 */
	@Override
	public void optimizeFoodProductionAndDistribution(OptimizationOptions optimizationOptions) {
		List<City> cities = getSociety().getCities();
		List<? extends AgricultureElement> elements = getInternalElements();

		// Count number of variables.
		int numVariables = 2*elements.size() + 2*cities.size();

		List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

		double[] costCoefficients = new double[numVariables];
		double[] initialValues = new double[numVariables];

		for(AgricultureElement element : elements) {
			// production constraint
			double[] productionConstraint = new double[numVariables];
			productionConstraint[elements.indexOf(element)] = 1;
			constraints.add(new LinearConstraint(productionConstraint, 
					Relationship.LEQ, element.getMaxLandArea()));

			// production cost
			costCoefficients[elements.indexOf(element)] = 
					element.getCostIntensityOfLandUsed() 
					+ element.getWaterIntensityOfLandUsed()
					* (DefaultUnits.convert(
							getSociety().getWaterSystem().getWaterDomesticPrice(),
							getSociety().getWaterSystem().getCurrencyUnits(), 
							getSociety().getWaterSystem().getWaterUnits(),
							getCurrencyUnits(), getWaterUnits())
							+ optimizationOptions.getDeltaDomesticWaterPrice());
			initialValues[elements.indexOf(element)] = element.getLandArea();

			// distribution constraint
			double[] distributionConstraint = new double[numVariables];
			distributionConstraint[elements.size() + elements.indexOf(element)] = 1;
			constraints.add(new LinearConstraint(distributionConstraint, 
					Relationship.LEQ, element.getMaxFoodInput()));

			// distribution cost
			costCoefficients[elements.size() + elements.indexOf(element)] 
					= element.getVariableOperationsCostOfFoodDistribution();
			initialValues[elements.size() + elements.indexOf(element)] 
					= element.getFoodInput();
		}

		for(City city : cities) {
			if(!(city.getAgricultureSystem() instanceof AgricultureSystem.Local)) {
				continue;
			}

			AgricultureSystem.Local agricultureSystem = 
					(AgricultureSystem.Local) city.getAgricultureSystem();

			// land constraint
			double[] landConstraint = new double[numVariables];
			for(AgricultureElement element : elements) {
				if(city.getName().equals(element.getOrigin())) {
					landConstraint[elements.indexOf(element)] = 1.0;
				}
			}
			constraints.add(new LinearConstraint(landConstraint, 
					Relationship.LEQ, agricultureSystem.getArableLandArea()));

			// labor constraint
			double[] laborConstraint = new double[numVariables];
			for(AgricultureElement element : elements) {
				if(city.getName().equals(element.getOrigin())) {
					laborConstraint[elements.indexOf(element)] = 
							element.getLaborIntensityOfLandUsed();
				}
			}
			constraints.add(new LinearConstraint(laborConstraint, 
					Relationship.LEQ, city.getSocialSystem().getPopulation() 
					* agricultureSystem.getLaborParticipationRate()));

			double[] flowCoefficients = new double[numVariables];
			for(AgricultureElement element : elements) {
				if(city.getName().equals(element.getOrigin())) {
					// production flow
					flowCoefficients[elements.indexOf(element)] 
							= element.getFoodIntensityOfLandUsed();
				}

				if(city.getName().equals(element.getOrigin())) {
					// distribution out-flow
					flowCoefficients[elements.size() + elements.indexOf(element)] = -1;
				} else if(city.getName().equals(element.getDestination())) {
					// distribution in-flow
					flowCoefficients[elements.size() + elements.indexOf(element)] 
							= element.getDistributionEfficiency();
				}
			}
			// import
			flowCoefficients[2*elements.size() + cities.indexOf(city)] = 1;
			costCoefficients[2*elements.size() + cities.indexOf(city)] 
					= city.getAgricultureSystem().getFoodImportPrice() 
					+ optimizationOptions.getDeltaImportFoodPrice();
			initialValues[2*elements.size() + cities.indexOf(city)] 
					= agricultureSystem.getFoodImport();

			// export
			flowCoefficients[2*elements.size() + cities.size() + cities.indexOf(city)] = -1;
			costCoefficients[2*elements.size() + cities.size() + cities.indexOf(city)] 
					= -city.getAgricultureSystem().getFoodExportPrice()
					- optimizationOptions.getDeltaExportFoodPrice();
			initialValues[2*elements.size() + cities.size() + cities.indexOf(city)] 
					= agricultureSystem.getFoodExport();

			// Constrain in-flow and production to meet demand.
			constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
					city.getTotalFoodDemand()));
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
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#removeElement(edu.mit.sips.core.agriculture.AgricultureElement)
	 */
	@Override
	public boolean removeElement(AgricultureElement element) {
		for(AgricultureSystem.Local system : getNestedSystems()) {
			if(system.getSociety().getName().equals(element.getOrigin())) {
				return system.removeElement(element);
			}
		}
		return false;
	}
}