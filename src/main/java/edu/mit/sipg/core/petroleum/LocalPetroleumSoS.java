/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sipg.core.petroleum;

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

import edu.mit.sipg.core.City;
import edu.mit.sipg.core.Society;
import edu.mit.sipg.core.base.LocalInfrastructureSoS;
import edu.mit.sipg.core.electricity.ElectricitySoS;
import edu.mit.sipg.units.DefaultUnits;
import edu.mit.sipg.units.ElectricityUnits;
import edu.mit.sipg.units.OilUnits;
import edu.mit.sipg.units.TimeUnits;

/**
 * The locally-controlled implementation of the petroleum system-of-systems interface.
 * 
 * @author Paul T. Grogan
 */
public class LocalPetroleumSoS extends LocalInfrastructureSoS implements PetroleumSoS.Local {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private List<Double> reservoirSecurityHistory = new ArrayList<Double>();

	/**
	 * Instantiates a new local petroleum system-of-systems.
	 */
	public LocalPetroleumSoS() {
		super("Petroleum");
	}

	@Override
	public boolean addElement(PetroleumElement element) {
		for(PetroleumSystem.Local system : getNestedSystems()) {
			if(system.getSociety().getName().equals(element.getOrigin())) {
				return system.addElement(element);
			}
		}
		return false;
	}

	/**
	 * Compute reservoir security score.
	 *
	 * @return the double
	 */
	private double computeReservoirSecurityScore() {
		double minLifetime = 0;
		double maxLifetime = 200;
		if(getReservoirLifetime() < minLifetime) {
			return 0;
		} else if(getReservoirLifetime() > maxLifetime) {
			return 1000;
		} else {
			return 1000*(getReservoirLifetime() - minLifetime)/(maxLifetime - minLifetime);
		}
	}

	@Override
	public double getAggregateScore(long year, ElectricitySoS.Local electricitySystem) {
		return (getReservoirSecurityScore() + getFinancialSecurityScore(year, electricitySystem) + getPoliticalPowerScore(year, electricitySystem))/3d;
	}

	@Override
	public double getElectricityConsumption() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getElectricityConsumption();
		}
		return value;
	}

	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	@Override
	public List<? extends PetroleumElement> getElements() {
		List<PetroleumElement> elements = new ArrayList<PetroleumElement>();
		elements.addAll(getInternalElements());
		elements.addAll(getExternalElements());
		return Collections.unmodifiableList(elements);
	}

	@Override
	public List<? extends PetroleumElement> getExternalElements() {
		List<PetroleumElement> elements = new ArrayList<PetroleumElement>();
		if(!getSociety().equals(getSociety().getCountry())) {
			for(PetroleumSystem.Local system : getNestedSystems()) {
				elements.addAll(system.getExternalElements());
			}
			elements.removeAll(getInternalElements());
		}
		return Collections.unmodifiableList(elements);
	}

	@Override
	public double getFinancialSecurityScore(long year, ElectricitySoS.Local electricitySystem) {
		double dystopiaTotal = 0;
		double utopiaTotal = 500e9;
		double growthRate = 0.04;
		
		double minValue = dystopiaTotal * (Math.pow(1+growthRate, year-1940) - 1)
				/ (Math.pow(1+growthRate, 2010-1940) - 1);
		double maxValue = utopiaTotal * (Math.pow(1+growthRate, year-1940) - 1)
				/ (Math.pow(1+growthRate, 2010-1940) - 1);
		
		double cumulativeCashFlow = getCumulativeCashFlow() + electricitySystem.getCumulativeCashFlow();
		if(cumulativeCashFlow < minValue) {
			return 0;
		} else if(cumulativeCashFlow > maxValue) {
			return 1000;
		} else {
			return 1000*(cumulativeCashFlow - minValue)/(maxValue - minValue);
		}
	}

	@Override
	public List<? extends PetroleumElement> getInternalElements() {
		List<PetroleumElement> elements = new ArrayList<PetroleumElement>();
		for(PetroleumSystem.Local system : getNestedSystems()) {
			elements.addAll(system.getInternalElements());
		}
		return Collections.unmodifiableList(elements);
	}

	@Override
	public double getLocalPetroleumFraction() {
		if(getSociety().getTotalPetroleumDemand() > 0) {
			return Math.min(1, getPetroleumProduction()
					/ getSociety().getTotalPetroleumDemand());
		}
		return 0;
	}

	@Override
	public double getMaxPetroleumReservoirVolume() {
		double value = 0;
		for(PetroleumSystem.Local system : getNestedSystems()) {
			value += system.getMaxPetroleumReservoirVolume();
		}
		return value;
	}

	@Override
	public List<PetroleumSystem.Local> getNestedSystems() {
		List<PetroleumSystem.Local> systems = new ArrayList<PetroleumSystem.Local>();
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getPetroleumSystem() instanceof PetroleumSystem.Local){ 
				systems.add((PetroleumSystem.Local) society.getPetroleumSystem());
			}
		}
		return Collections.unmodifiableList(systems);
	}

	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}

	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
	}

	@Override
	public double getPetroleumDomesticPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(PetroleumSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getPetroleumDomesticPrice(), 
						system.getCurrencyUnits(), system.getOilUnits(),
						getCurrencyUnits(), getOilUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	@Override
	public double getPetroleumExport() {
		double value = 0;
		for(PetroleumSystem.Local system : getNestedSystems()) {
			value += system.getPetroleumExport();
		}
		return value;
	}

	@Override
	public double getPetroleumExportPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(PetroleumSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getPetroleumExportPrice(), 
						system.getCurrencyUnits(), system.getOilUnits(),
						getCurrencyUnits(), getOilUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	@Override
	public double getPetroleumImport() {
		double value = 0;
		for(PetroleumSystem.Local system : getNestedSystems()) {
			value += system.getPetroleumImport();
		}
		return value;
	}

	@Override
	public double getPetroleumImportPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(PetroleumSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getPetroleumImportPrice(), 
						system.getCurrencyUnits(), system.getOilUnits(),
						getCurrencyUnits(), getOilUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	@Override
	public double getPetroleumInDistribution() {
		double value = 0;
		for(PetroleumElement e : getExternalElements()) {
			value += e.getPetroleumOutput();
		}
		return value;
	}

	@Override
	public double getPetroleumOutDistribution() {
		double value = 0;
		for(PetroleumElement e : getInternalElements()) {
			if(!getSociety().getCities().contains(
					getSociety().getCountry().getCity(e.getDestination()))) {
				value += e.getPetroleumInput();
			}
		}
		return value;
	}

	@Override
	public double getPetroleumOutDistributionLosses() {
		double value = 0;
		for(PetroleumElement e : getInternalElements()) {
			value += e.getPetroleumInput() - e.getPetroleumOutput();
		}
		return value;
	}

	@Override
	public double getPetroleumProduction() {
		double value = 0;
		for(PetroleumSystem.Local system : getNestedSystems()) {
			value += system.getPetroleumProduction();
		}
		return value;
	}

	@Override
	public double getPoliticalPowerScore(long year, ElectricitySoS.Local electricitySystem) {
		double dystopiaTotal = 0;
		double utopiaTotal = 50e9;
		double growthRate = 0.03;
		
		double minValue = dystopiaTotal * (Math.pow(1+growthRate, year-1940) - 1)
				/ (Math.pow(1+growthRate, 2010-1940) - 1);
		double maxValue = utopiaTotal * (Math.pow(1+growthRate, year-1940) - 1)
				/ (Math.pow(1+growthRate, 2010-1940) - 1);
		
		double cumulativeCapitalExpense = getCumulativeCapitalExpense() + electricitySystem.getCumulativeCapitalExpense();
		if(cumulativeCapitalExpense < minValue) {
			return 0;
		} else if(cumulativeCapitalExpense > maxValue) {
			return 1000;
		} else {
			return 1000*(cumulativeCapitalExpense - minValue)/(maxValue - minValue);
		}
	}

	/**
	 * Gets the reservoir lifetime.
	 *
	 * @return the reservoir lifetime
	 */
	public double getReservoirLifetime() {
		return getReservoirWithdrawals() == 0 ? Double.MAX_VALUE 
				: (getReservoirVolume() / getReservoirWithdrawals());
	}

	@Override
	public double getReservoirSecurityScore() {
		double value = 0;
		for(double item : reservoirSecurityHistory) {
			value += item;
		}
		return value / reservoirSecurityHistory.size();
	}

	@Override
	public double getReservoirVolume() {
		double value = 0;
		for(PetroleumSystem.Local system : getNestedSystems()) {
			value += system.getReservoirVolume();
		}
		return value;
	}

	@Override
	public double getReservoirWithdrawals() {
		double value = 0;
		for(PetroleumSystem.Local system : getNestedSystems()) {
			value += system.getReservoirWithdrawals();
		}
		return value;
	}

	@Override
	public double getTotalPetroleumSupply() {
		double value = 0;
		for(PetroleumSystem.Local system : getNestedSystems()) {
			value += system.getTotalPetroleumSupply();
		}
		return value;
	}

	@Override
	public double getUnitProductionCost() {
		if(getPetroleumProduction() > 0) {
			return (getLifecycleExpense() + getConsumptionExpense()) 
					/ getPetroleumProduction();
		}
		return 0;
	}
	
	@Override
	public double getUnitSupplyProfit() {
		if(getTotalPetroleumSupply() > 0) {
			return getCashFlow() / getTotalPetroleumSupply();
		}
		return 0;
	}
	
	@Override
	public void initialize(long time) {
		super.initialize(time);
		reservoirSecurityHistory.clear();
	}
	
	@Override
	public void optimizePetroleumDistribution() {
		// Make a list of cities and infrastructure elements. The vector
		// of decision variables includes the throughput of each distribution
		// element and the import and export amounts in each city.
		List<City> cities = getSociety().getCities();
		List<? extends PetroleumElement> elements = getInternalElements();

		// Count number of variables.
		int numVariables = elements.size() + 2*cities.size();

		// Create a list to hold the linear constraints.
		List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

		double[] costCoefficients = new double[numVariables];
		double[] initialValues = new double[numVariables];

		for(PetroleumElement element : elements) {
			// Add constraints for distribution throughput, i.e. the
			// throughput for each distribution element cannot exceed the maximum.
			double[] distributionConstraint = new double[numVariables];
			distributionConstraint[elements.indexOf(element)] = 1;
			constraints.add(new LinearConstraint(distributionConstraint, 
					Relationship.LEQ, element.getMaxPetroleumInput()));

			// Set distribution cost.
			costCoefficients[elements.indexOf(element)] 
					= element.getVariableOperationsCostOfPetroleumDistribution()
					+ element.getElectricalIntensityOfPetroleumDistribution() * 
					DefaultUnits.convert(
							getSociety().getElectricitySystem().getElectricityDomesticPrice(),
							getSociety().getElectricitySystem().getCurrencyUnits(),
							getSociety().getElectricitySystem().getElectricityUnits(),
							getCurrencyUnits(), getElectricityUnits());
			initialValues[elements.indexOf(element)] 
					= element.getPetroleumInput();
		}

		for(City city : cities) {
			if(!(city.getPetroleumSystem() instanceof PetroleumSystem.Local)) {
				continue;
			}

			PetroleumSystem.Local energySystem = (PetroleumSystem.Local) city.getPetroleumSystem();

			// Add constraints for city supply/demand, i.e. the in-flow less
			// out-flow (corrected for efficiency of distribution) must equal
			// the total demand less any local production.
			double[] flowCoefficients = new double[numVariables];
			for(PetroleumElement element : elements) {
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
			// Allow export from this city.
			flowCoefficients[elements.size() + cities.size() + cities.indexOf(city)] = -1;
			// Constrain in-flow to meet net demand.
			constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
					energySystem.getSociety().getTotalPetroleumDemand() 
					- energySystem.getPetroleumProduction()));

			// Set import cost in each city.
			costCoefficients[elements.size() + cities.indexOf(city)] 
					= city.getPetroleumSystem().getPetroleumImportPrice();
			initialValues[elements.size() + cities.indexOf(city)] 
					= energySystem.getPetroleumImport();
			// Set export price in each city.
			costCoefficients[elements.size() + cities.size() + cities.indexOf(city)] 
					= -city.getPetroleumSystem().getPetroleumExportPrice();
			initialValues[elements.size() + cities.size() + cities.indexOf(city)] 
					= energySystem.getPetroleumExport();
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
				elements.get(i).setPetroleumInput(Math.min(output.getPoint()[i],
						elements.get(i).getMaxPetroleumInput()));
			}
		} catch(TooManyIterationsException ignore) { 
			// Don't overwrite existing values.
			ignore.printStackTrace();
		} catch(NoFeasibleSolutionException ignore) {
			// Don't overwrite existing values.
			ignore.printStackTrace();
		}
	}

	@Override
	public void optimizePetroleumProductionAndDistribution() {
		List<City> cities = getSociety().getCities();
		List<? extends PetroleumElement> elements = getInternalElements();

		// Count number of variables.
		int numVariables = 2*elements.size() + 2*cities.size();

		List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

		double[] costCoefficients = new double[numVariables];
		double[] initialValues = new double[numVariables];

		for(PetroleumElement element : elements) {
			// Constrain maximum production in each fixed element.
			double[] productionConstraint = new double[numVariables];
			productionConstraint[elements.indexOf(element)] = 1;
			constraints.add(new LinearConstraint(productionConstraint, 
					Relationship.LEQ, element.getMaxPetroleumProduction()));

			// Constrain maximum throughput in each distribution element.
			double[] throughputConstraint = new double[numVariables];
			throughputConstraint[elements.size() + elements.indexOf(element)] = 1;
			constraints.add(new LinearConstraint(throughputConstraint, 
					Relationship.LEQ, element.getMaxPetroleumInput()));

			// Minimize costs - most obvious cost is importing, though also 
			// minimize transportation even if free.
			costCoefficients[elements.indexOf(element)] 
					= element.getVariableOperationsCostOfPetroleumProduction();
			initialValues[elements.indexOf(element)] 
					= element.getPetroleumProduction();

			// Set distribution cost.
			costCoefficients[elements.size() + elements.indexOf(element)] 
					= element.getVariableOperationsCostOfPetroleumDistribution()
					+ element.getReservoirIntensityOfPetroleumProduction() 
					+ element.getElectricalIntensityOfPetroleumDistribution()
					* (DefaultUnits.convert(
							getSociety().getElectricitySystem().getElectricityDomesticPrice(),
							getSociety().getElectricitySystem().getCurrencyUnits(),
							getSociety().getElectricitySystem().getElectricityUnits(),
							getCurrencyUnits(), getElectricityUnits()));
			initialValues[elements.size() + elements.indexOf(element)] 
					= element.getPetroleumInput();
		}

		for(City city : cities) {
			if(!(city.getPetroleumSystem() instanceof PetroleumSystem.Local)) {
				continue;
			}

			PetroleumSystem.Local energySystem = (PetroleumSystem.Local) city.getPetroleumSystem();

			// Constrain maximum resource in each city.
			double[] resourceConstraint = new double[numVariables];
			for(PetroleumElement element : elements) {
				if(city.getName().equals(element.getOrigin())) {
					resourceConstraint[elements.indexOf(element)] 
							= element.getReservoirIntensityOfPetroleumProduction();
				}
			}
			constraints.add(new LinearConstraint(resourceConstraint, 
					Relationship.LEQ, energySystem.getReservoirVolume()));

			// Constrain supply = demand in each city.
			double[] flowCoefficients = new double[numVariables];
			for(PetroleumElement element : elements) {
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
			// Allow export from this city.
			flowCoefficients[2*elements.size() + cities.size() + cities.indexOf(city)] = -1;

			// Constrain in-flow and production to meet demand.
			constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
					city.getTotalPetroleumDemand()));

			// Set import cost in each city.
			costCoefficients[2*elements.size() + cities.indexOf(city)] 
					= city.getPetroleumSystem().getPetroleumImportPrice();
			initialValues[2*elements.size() + cities.indexOf(city)] 
					= energySystem.getPetroleumImport();
			// Set export price in each city.
			costCoefficients[2*elements.size() + cities.size() + cities.indexOf(city)] 
					= -city.getPetroleumSystem().getPetroleumExportPrice();
			initialValues[2*elements.size() + cities.size() + cities.indexOf(city)] 
					= energySystem.getPetroleumExport();
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
				elements.get(i).setPetroleumProduction(Math.min(
						output.getPoint()[i],
						elements.get(i).getMaxPetroleumProduction()));
				elements.get(i).setPetroleumInput(Math.min(
						output.getPoint()[elements.size() + i],
						elements.get(i).getMaxPetroleumInput()));
			}
		} catch(TooManyIterationsException ignore) { 
			// Don't overwrite existing values.
			ignore.printStackTrace();
		} catch(NoFeasibleSolutionException ignore) {
			// Don't overwrite existing values.
			ignore.printStackTrace();
		}
	}

	@Override
	public boolean removeElement(PetroleumElement element) {
		for(PetroleumSystem.Local system : getNestedSystems()) {
			if(system.getSociety().getName().equals(element.getOrigin())) {
				return system.removeElement(element);
			}
		}
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		this.reservoirSecurityHistory.add(computeReservoirSecurityScore());
	}
}