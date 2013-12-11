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
import edu.mit.sips.core.LocalInfrastructureSoS;
import edu.mit.sips.core.OptimizationOptions;
import edu.mit.sips.core.Society;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

public class LocalWaterSoS extends LocalInfrastructureSoS implements WaterSoS.Local {	
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;	

	/**
	 * Instantiates a new local.
	 */
	public LocalWaterSoS() {
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
			value += ElectricityUnits.convertFlow(
					system.getElectricityConsumption(), 
					system, this);
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getElectricityConsumptionFromPrivateProduction()
	 */
	@Override
	public double getElectricityConsumptionFromPrivateProduction() {
		double value = 0;
		for(WaterSystem.Local system : getNestedSystems()) {
			value += ElectricityUnits.convertFlow(
					system.getElectricityConsumptionFromPrivateProduction(), 
					system, this);
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getElectricityConsumptionFromPublicProduction()
	 */
	@Override
	public double getElectricityConsumptionFromPublicProduction() {
		double value = 0;
		for(WaterSystem.Local system : getNestedSystems()) {
			value += ElectricityUnits.convertFlow(
					system.getElectricityConsumptionFromPublicProduction(), 
					system, this);
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityTimeUnits()
	 */
	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnits()
	 */
	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
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
			return Math.min(1, (getWaterProduction() + getWaterFromPrivateProduction())
					/ WaterUnits.convertFlow(
							getSociety().getTotalWaterDemand(), 
							getSociety(), this));
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
			value += WaterUnits.convertStock(
					system.getMaxWaterReservoirVolume(), 
					system, this);
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
			return getRenewableWaterProduction() / WaterUnits.convertFlow(
					getSociety().getTotalWaterDemand(), 
					getSociety(), this);
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
			value += WaterUnits.convertFlow(
					system.getRenewableWaterProduction(), 
					system, this);
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getReservoirWithdrawals()
	 */
	@Override
	public double getReservoirWithdrawals() {
		double value = 0;
		for(WaterSystem.Local system : getNestedSystems()) {
			value += WaterUnits.convertFlow(
					system.getReservoirWithdrawals(),
					system, this);
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getReservoirWithdrawalsFromPrivateProduction()
	 */
	@Override
	public double getReservoirWithdrawalsFromPrivateProduction() {
		double value = 0;
		for(WaterSystem.Local system : getNestedSystems()) {
			value += WaterUnits.convertFlow(
					system.getReservoirWithdrawalsFromPrivateProduction(),
					system, this);
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getReservoirWithdrawalsFromPublicProduction()
	 */
	@Override
	public double getReservoirWithdrawalsFromPublicProduction() {
		double value = 0;
		for(WaterSystem.Local system : getNestedSystems()) {
			value += WaterUnits.convertFlow(
					system.getReservoirWithdrawalsFromPublicProduction(),
					system, this);
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
			value += WaterUnits.convertFlow(
					system.getTotalWaterSupply(),
					system, this);
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
				value += CurrencyUnits.convertFlow(
						system.getWaterDomesticPrice(), 
						system, this);
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterFromPrivateProduction()
	 */
	@Override
	public double getWaterFromPrivateProduction() {
		double value = 0;
		for(WaterSystem.Local system : getNestedSystems()) {
			value += WaterUnits.convertFlow(
					system.getWaterFromPrivateProduction(), 
					system, this);
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
			value += WaterUnits.convertFlow(
					system.getWaterImport(), system, this);
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
				value += CurrencyUnits.convertFlow(
						system.getWaterImportPrice(),
						system, this);
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
			value += WaterUnits.convertFlow(e.getWaterOutput(), e, this);
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
				value += WaterUnits.convertFlow(e.getWaterInput(), e, this);
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
			value += WaterUnits.convertFlow(e.getWaterInput() - e.getWaterOutput(), e, this);
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
			value += WaterUnits.convertFlow(system.getWaterProduction(), system, this);
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
			value += WaterUnits.convertFlow(system.getWaterReservoirRechargeRate(), system, this);
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
			value += WaterUnits.convertFlow(system.getWaterReservoirVolume(), system, this);
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
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterWasted()
	 */
	@Override
	public double getWaterWasted() {
		double value = 0;
		for(WaterSystem.Local system : getNestedSystems()) {
			value += WaterUnits.convertFlow(system.getWaterWasted(), system, this);
		}
		return value;
	}

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

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSoS.Local#optimizeWaterDistribution()
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
					* DefaultUnits.convert(
							getSociety().getElectricitySystem().getElectricityDomesticPrice(),
							getSociety().getElectricitySystem().getCurrencyUnits(), 
							getSociety().getElectricitySystem().getElectricityUnits(),
							getCurrencyUnits(), getElectricityUnits());
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
	 * @see edu.mit.sips.core.water.WaterSoS.Local#optimizeWaterProductionAndDistribution(edu.mit.sips.core.OptimizationOptions)
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
					* (DefaultUnits.convert(
							getSociety().getElectricitySystem().getElectricityDomesticPrice(),
							getSociety().getElectricitySystem().getCurrencyUnits(), 
							getSociety().getElectricitySystem().getElectricityUnits(),
							getCurrencyUnits(), getElectricityUnits())
							+ optimizationOptions.getDeltaDomesticElectricityPrice());
			initialValues[elements.indexOf(element)] 
					= element.getWaterProduction();

			// Set a distribution cost using variable operations expense.
			costCoefficients[elements.size() + elements.indexOf(element)] 
					= element.getVariableOperationsCostOfWaterDistribution()
					+ element.getReservoirIntensityOfWaterProduction()
					* optimizationOptions.getDeltaAquiferWaterPrice()
					+ element.getElectricalIntensityOfWaterDistribution()
					* (DefaultUnits.convert(
							getSociety().getElectricitySystem().getElectricityDomesticPrice(),
							getSociety().getElectricitySystem().getCurrencyUnits(), 
							getSociety().getElectricitySystem().getElectricityUnits(),
							getCurrencyUnits(), getElectricityUnits())
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

	@Override
	public double getWaterAgriculturalPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(WaterSystem system : getNestedSystems()) {
				value += CurrencyUnits.convertStock(system.getWaterAgriculturalPrice(), system, this);
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}
}