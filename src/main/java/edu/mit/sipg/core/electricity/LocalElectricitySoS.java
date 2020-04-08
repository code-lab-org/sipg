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
package edu.mit.sipg.core.electricity;

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
import edu.mit.sipg.units.DefaultUnits;
import edu.mit.sipg.units.ElectricityUnits;
import edu.mit.sipg.units.OilUnits;
import edu.mit.sipg.units.TimeUnits;
import edu.mit.sipg.units.WaterUnits;

/**
 * The locally-controlled implementation of the electricity system-of-systems interface.
 * 
 * @author Paul T. Grogan
 */
public class LocalElectricitySoS  extends LocalInfrastructureSoS implements ElectricitySoS.Local {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;

	/**
	 * Instantiates a new local electricity system-of-systems.
	 */
	public LocalElectricitySoS() {
		super("Electricity");
	}

	@Override
	public boolean addElement(ElectricityElement element) {
		for(ElectricitySystem.Local system : getNestedSystems()) {
			if(system.getSociety().getName().equals(element.getOrigin())) {
				return system.addElement(element);
			}
		}
		return false;
	}

	@Override
	public double getElectricityDomesticPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(ElectricitySystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getElectricityDomesticPrice(), 
						system.getCurrencyUnits(), system.getElectricityUnits(),
						getCurrencyUnits(), getElectricityUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	@Override
	public double getElectricityFromPrivateProduction() {
		double value = 0;
		for(ElectricitySystem.Local system : getNestedSystems()) {
			value += system.getElectricityFromPrivateProduction();
		}
		return value;
	}

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

	@Override
	public double getElectricityOutDistributionLosses() {
		double value = 0;
		for(ElectricityElement e : getInternalElements()) {
			value += e.getElectricityInput() - e.getElectricityOutput();
		}
		return value;
	}

	@Override
	public double getElectricityProduction() {
		double value = 0;
		for(ElectricitySystem.Local system : getNestedSystems()) {
			value += system.getElectricityProduction();
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
	public double getElectricityWasted() {
		double value = 0;
		for(ElectricitySystem.Local system : getNestedSystems()) {
			value += system.getElectricityWasted();
		}
		return value;
	}

	@Override
	public List<? extends ElectricityElement> getElements() {
		List<ElectricityElement> elements = new ArrayList<ElectricityElement>();
		elements.addAll(getInternalElements());
		elements.addAll(getExternalElements());
		return Collections.unmodifiableList(elements);
	}

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

	@Override
	public List<? extends ElectricityElement> getInternalElements() {
		List<ElectricityElement> elements = new ArrayList<ElectricityElement>();
		for(ElectricitySystem.Local system : getNestedSystems()) {
			elements.addAll(system.getInternalElements());
		}
		return Collections.unmodifiableList(elements);
	}

	@Override
	public double getLocalElectricityFraction() {
		if(getSociety().getTotalElectricityDemand() > 0) {
			return Math.min(1, (getElectricityProduction()
					+ getElectricityFromPrivateProduction())
					/ getSociety().getTotalElectricityDemand());
		}
		return 0;
	}

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

	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}

	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
	}

	@Override
	public double getPetroleumConsumption() {
		double value = 0;
		for(ElectricitySystem system : getNestedSystems()) {
			value += system.getPetroleumConsumption();
		}
		return value;
	}

	@Override
	public double getPetroleumConsumptionFromPrivateProduction() {
		double value = 0;
		for(ElectricitySystem.Local system : getNestedSystems()) {
			value += system.getPetroleumConsumptionFromPrivateProduction();
		}
		return value;
	}

	@Override
	public double getPetroleumConsumptionFromPublicProduction() {
		double value = 0;
		for(ElectricitySystem.Local system : getNestedSystems()) {
			value += system.getPetroleumConsumptionFromPublicProduction();
		}
		return value;
	}
	
	@Override
	public double getPetroleumIntensityOfPrivateProduction() {
		if(getNestedSystems().size() > 0) {
			double value = 0;
			for(ElectricitySystem.Local system : getNestedSystems()) {
				value += system.getPetroleumIntensityOfPrivateProduction();
			}
			return value / getNestedSystems().size();
		} else {
			return 0;
		}
	}

	@Override
	public double getRenewableElectricityFraction() {
		if(getSociety().getTotalElectricityDemand() > 0) {
			return getRenewableElectricityProduction() / 
					getSociety().getTotalElectricityDemand();
		}
		return 0;
	}

	@Override
	public double getRenewableElectricityProduction() {
		double value = 0;
		for(ElectricitySystem.Local system : getNestedSystems()) {
			value += system.getRenewableElectricityProduction();
		}
		return value;
	}

	@Override
	public double getTotalElectricitySupply() {
		double value = 0;
		for(ElectricitySystem.Local system : getNestedSystems()) {
			value += system.getTotalElectricitySupply();
		}
		return value;
	}

	@Override
	public double getUnitProductionCost() {
		if(getElectricityProduction() > 0) {
			return (getLifecycleExpense() + getConsumptionExpense()) 
					/ getElectricityProduction();
		}
		return 0;
	}

	@Override
	public double getUnitSupplyProfit() {
		if(getTotalElectricitySupply() > 0) {
			return getCashFlow() / getTotalElectricitySupply();
		}
		return 0;
	}

	@Override
	public double getWaterConsumption() {
		double value = 0;
		for(ElectricitySystem system : getNestedSystems()) {
			value += system.getWaterConsumption();
		}
		return value;
	}

	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}

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
					= electricitySystem.getPetroleumIntensityOfPrivateProduction();
			// Constrain in-flow to meet net demand.
			constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
					city.getTotalElectricityDemand() 
					- electricitySystem.getElectricityProduction()));

			// Set petroleum burn cost in each city.
			costCoefficients[elements.size() + cities.indexOf(city)] = 
					DefaultUnits.convert(city.getPetroleumSystem().getPetroleumDomesticPrice(),
							city.getPetroleumSystem().getCurrencyUnits(),
							city.getPetroleumSystem().getOilUnits(),
							getCurrencyUnits(), getOilUnits());
			initialValues[elements.size() + cities.indexOf(city)] = 
					Math.max(0,electricitySystem.getPetroleumConsumptionFromPrivateProduction());
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

	@Override
	public void optimizeElectricityProductionAndDistribution() {
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
					* (DefaultUnits.convert(getSociety().getWaterSystem().getWaterDomesticPrice(),
							getSociety().getWaterSystem().getCurrencyUnits(),
							getSociety().getWaterSystem().getWaterUnits(),
							getCurrencyUnits(), getWaterUnits()))
							+ element.getPetroleumIntensityOfElectricityProduction()
							* (DefaultUnits.convert(getSociety().getPetroleumSystem().getPetroleumDomesticPrice(),
									getSociety().getPetroleumSystem().getCurrencyUnits(),
									getSociety().getPetroleumSystem().getOilUnits(),
									getCurrencyUnits(), getOilUnits()));
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
					= electricitySystem.getPetroleumIntensityOfPrivateProduction();

			// Constrain in-flow and production to meet demand.
			constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
					city.getTotalElectricityDemand()));

			// Set petroleum burn cost in each city.
			costCoefficients[2*elements.size() + cities.indexOf(city)] = 
					DefaultUnits.convert(city.getPetroleumSystem().getPetroleumDomesticPrice(),
							city.getPetroleumSystem().getCurrencyUnits(),
							city.getPetroleumSystem().getOilUnits(),
							getCurrencyUnits(), getOilUnits());
			initialValues[2*elements.size() + cities.indexOf(city)] = 
					Math.max(0,electricitySystem.getPetroleumConsumptionFromPrivateProduction());
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

	@Override
	public boolean removeElement(ElectricityElement element) {
		for(ElectricitySystem.Local system : getNestedSystems()) {
			if(system.getSociety().getName().equals(element.getOrigin())) {
				return system.removeElement(element);
			}
		}
		return false;
	}
}