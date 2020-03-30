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
package edu.mit.sips.core.petroleum;

import edu.mit.sips.core.base.DefaultInfrastructureElement;
import edu.mit.sips.core.lifecycle.LifecycleModel;
import edu.mit.sips.units.ElectricityUnits;
import edu.mit.sips.units.OilUnits;
import edu.mit.sips.units.TimeUnits;

/**
 * The default implementation of the petroleum element interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultPetroleumElement extends DefaultInfrastructureElement
		implements PetroleumElement {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	
	/**
	 * Builder function to create a new distribution element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxPetroleumInput the max petroleum input
	 * @param initialPetroleumInput the initial petroleum input
	 * @param electricalIntensityOfPetroleumDistribution the electrical intensity of petroleum distribution
	 * @param variableOperationsCostOfPetroleumDistribution the variable operations cost of petroleum distribution
	 * @return the petroleum element
	 */
	public static PetroleumElement createDistributionElement(
			String templateName, String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double distributionEfficiency,
			double maxPetroleumInput, double initialPetroleumInput, 
			double electricalIntensityOfPetroleumDistribution,
			double variableOperationsCostOfPetroleumDistribution) {
		return new DefaultPetroleumElement(templateName, name, origin, 
				destination, lifecycleModel, 0, 0, 0, 0, 
				distributionEfficiency, maxPetroleumInput, initialPetroleumInput,
				electricalIntensityOfPetroleumDistribution,
				variableOperationsCostOfPetroleumDistribution);
	}
	
	/**
	 * Builder function to create a new production element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param reservoirIntensityOfPetroleumProduction the reservoir intensity of petroleum production
	 * @param maxPetroleumProduction the max petroleum production
	 * @param initialPetroleumProduction the initial petroleum production
	 * @param variableOperationsCostOfPetroleumProduction the variable operations cost of petroleum production
	 * @return the petroleum element
	 */
	public static PetroleumElement createProductionElement(
			String templateName, String name, 
			String origin, String destination, 
			LifecycleModel lifecycleModel, double reservoirIntensityOfPetroleumProduction,
			double maxPetroleumProduction, double initialPetroleumProduction,
			double variableOperationsCostOfPetroleumProduction) {
		return new DefaultPetroleumElement(templateName, name, origin, 
				destination, lifecycleModel, reservoirIntensityOfPetroleumProduction,
				maxPetroleumProduction, initialPetroleumProduction,
				variableOperationsCostOfPetroleumProduction, 0, 0, 0, 0, 0);
	}
	
	private final double reservoirIntensityOfPetroleumProduction;
	private final double maxPetroleumProduction;
	private final double initialPetroleumProduction;
	private final double distributionEfficiency;
	private final double maxPetroleumInput;
	private final double variableOperationsCostOfPetroleumProduction;
	private final double initialPetroleumInput;
	private final double electricalIntensityOfPetroleumDistribution;
	private final double variableOperationsCostOfPetroleumDistribution;
	
	private double petroleumProduction;
	private double petroleumInput;
	
	/**
	 * Instantiates a new default petroleum element.
	 */
	protected DefaultPetroleumElement() {
		reservoirIntensityOfPetroleumProduction = 0;
		maxPetroleumProduction = 0;
		initialPetroleumProduction = 0;
		distributionEfficiency = 0;
		maxPetroleumInput = 0;
		variableOperationsCostOfPetroleumProduction = 0;
		initialPetroleumInput = 0;
		electricalIntensityOfPetroleumDistribution = 0;
		variableOperationsCostOfPetroleumDistribution = 0;
	}
	
	/**
	 * Instantiates a new default petroleum element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param reservoirIntensityOfPetroleumProduction the reservoir intensity of petroleum production
	 * @param maxPetroleumProduction the max petroleum production
	 * @param initialPetroleumProduction the initial petroleum production
	 * @param variableOperationsCostOfPetroleumProduction the variable operations cost of petroleum production
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxPetroleumInput the max petroleum input
	 * @param initialPetroleumInput the initial petroleum input
	 * @param electricalIntensityOfPetroleumDistribution the electrical intensity of petroleum distribution
	 * @param variableOperationsCostOfPetroleumDistribution the variable operations cost of petroleum distribution
	 */
	protected DefaultPetroleumElement(String templateName, String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double reservoirIntensityOfPetroleumProduction,
			double maxPetroleumProduction, double initialPetroleumProduction, 
			double variableOperationsCostOfPetroleumProduction,
			double distributionEfficiency, double maxPetroleumInput, 
			double initialPetroleumInput, 
			double electricalIntensityOfPetroleumDistribution,
			double variableOperationsCostOfPetroleumDistribution) {
		super(templateName, name, origin, destination, lifecycleModel);
		
		if(reservoirIntensityOfPetroleumProduction < 0) {
			throw new IllegalArgumentException(
					"Reservoir intensity cannot be negative.");
		}
		this.reservoirIntensityOfPetroleumProduction = reservoirIntensityOfPetroleumProduction;
		
		if(maxPetroleumProduction < 0) {
			throw new IllegalArgumentException(
					"Maximum petroleum production cannot be negative.");
		}
		this.maxPetroleumProduction = maxPetroleumProduction;
		
		if(initialPetroleumProduction > maxPetroleumProduction) {
			throw new IllegalArgumentException(
					"Initial petroleum production cannot exceed maximum.");
		} else if(initialPetroleumProduction < 0) {
			throw new IllegalArgumentException(
					"Initial petroleum production cannot be negative.");
		}
		this.initialPetroleumProduction = initialPetroleumProduction;

		if(variableOperationsCostOfPetroleumProduction < 0) {
			throw new IllegalArgumentException(
					"Variable cost of production cannot be negative.");
		}
		this.variableOperationsCostOfPetroleumProduction = variableOperationsCostOfPetroleumProduction;
		
		if(distributionEfficiency < 0 || distributionEfficiency > 1) {
			throw new IllegalArgumentException(
					"Distribution efficiency must be between 0 and 1.");
		}
		this.distributionEfficiency = distributionEfficiency;
		
		if(maxPetroleumInput < 0) {
			throw new IllegalArgumentException(
					"Maximum petroleum input cannot be negative.");
		}
		this.maxPetroleumInput = maxPetroleumInput;
		
		if(initialPetroleumInput > maxPetroleumInput) {
			throw new IllegalArgumentException(
					"Initial petroleum input cannot exceed maximum.");
		} else if(initialPetroleumInput < 0) {
			throw new IllegalArgumentException(
					"Initial petroleum input cannot be negative.");
		}
		this.initialPetroleumInput = initialPetroleumInput;

		if(electricalIntensityOfPetroleumDistribution < 0) {
			throw new IllegalArgumentException(
					"Electrical intensity cannot be negative.");
		}
		this.electricalIntensityOfPetroleumDistribution = electricalIntensityOfPetroleumDistribution;
		
		if(variableOperationsCostOfPetroleumDistribution < 0) {
			throw new IllegalArgumentException(
					"Variable cost of distribution cannot be negative.");
		}
		this.variableOperationsCostOfPetroleumDistribution = variableOperationsCostOfPetroleumDistribution;
	}
	
	@Override
	public double getDistributionEfficiency() {
		if(isOperational()) {
			return distributionEfficiency;
		} else {
			return 0;
		}
	}
	
	@Override
	public double getElectricalIntensityOfPetroleumDistribution() {
		return electricalIntensityOfPetroleumDistribution;
	}
	
	@Override
	public double getElectricityConsumption() {
		if(isOperational()) {
			return petroleumInput * electricalIntensityOfPetroleumDistribution;
		} else {
			return 0;
		}
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
	public double getMaxPetroleumInput() {
		if(isOperational()) {
			return maxPetroleumInput;
		} else {
			return 0;
		}
	}

	@Override
	public double getMaxPetroleumProduction() {
		if(isOperational()) {
			return maxPetroleumProduction;
		} else {
			return 0;
		}
	}

	@Override
	public EditablePetroleumElement getMutableElement() {
		EditablePetroleumElement element = 
				new EditablePetroleumElement();
		setMutableFields(element);
		element.setReservoirIntensityOfPetroleumProduction(
				reservoirIntensityOfPetroleumProduction);
		element.setMaxPetroleumProduction(
				maxPetroleumProduction);
		element.setInitialPetroleumProduction(initialPetroleumProduction);
		element.setDistributionEfficiency(distributionEfficiency);
		element.setMaxPetroleumInput(maxPetroleumInput);
		element.setVariableOperationsCostOfPetroleumProduction(
				variableOperationsCostOfPetroleumProduction);
		element.setInitialPetroleumInput(initialPetroleumInput);
		element.setElectricalIntensityOfPetroleumDistribution(
				electricalIntensityOfPetroleumDistribution);
		element.setVariableOperationsCostOfPetroleumDistribution(
				variableOperationsCostOfPetroleumDistribution);
		return element;
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
	public double getPetroleumInput() {
		if(isOperational()) {
			return petroleumInput;
		} else {
			return 0;
		}
	}
	
	@Override
	public double getPetroleumOutput() {
		if(isOperational()) {
			return petroleumInput * distributionEfficiency;
		} else {
			return 0;
		}
	}
	
	@Override
	public double getPetroleumProduction() {
		if(isOperational()) {
			return petroleumProduction;
		} else {
			return 0;
		}
	}

	@Override
	public double getReservoirIntensityOfPetroleumProduction() {
		if(isOperational()) {
			return reservoirIntensityOfPetroleumProduction;
		} else {
			return 0;
		}
	}

	@Override
	public double getReservoirWithdrawals() {
		if(isOperational()) {
			return getPetroleumProduction() * reservoirIntensityOfPetroleumProduction;
		} else {
			return 0;
		}
	}

	@Override
	public double getTotalOperationsExpense() {
		return getFixedOperationsExpense() 
				+ variableOperationsCostOfPetroleumProduction * petroleumProduction 
				+ variableOperationsCostOfPetroleumDistribution * petroleumInput;
	}

	@Override
	public double getVariableOperationsCostOfPetroleumDistribution() {
		return variableOperationsCostOfPetroleumDistribution;
	}

	@Override
	public double getVariableOperationsCostOfPetroleumProduction() {
		return variableOperationsCostOfPetroleumProduction;
	}

	@Override 
	public void initialize(long time) {
		super.initialize(time);
		setPetroleumProduction(initialPetroleumProduction);
		setPetroleumInput(initialPetroleumInput);
	}

	@Override
	public void setPetroleumInput(double petroleumInput) {
		if(petroleumInput < 0) {
			throw new IllegalArgumentException(
					"Petroleum input cannot be negative.");
		} else if(petroleumInput > maxPetroleumInput) {
			throw new IllegalArgumentException(
					"Petroleum input cannot exceed maximum.");
		}
		this.petroleumInput = petroleumInput;
		fireElementChangeEvent();
	}

	@Override
	public void setPetroleumProduction(double petroleumProduction) {
		if(petroleumProduction > maxPetroleumProduction) {
			throw new IllegalArgumentException(
					"Petroleum production cannot exceed maximum.");
		} else if(petroleumProduction < 0) {
			throw new IllegalArgumentException(
					"Petroleum production cannot be negative.");
		}
		this.petroleumProduction = petroleumProduction;
		fireElementChangeEvent();
	}
}
