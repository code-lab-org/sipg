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

import edu.mit.sipg.core.base.DefaultInfrastructureElement;
import edu.mit.sipg.core.lifecycle.LifecycleModel;
import edu.mit.sipg.units.ElectricityUnits;
import edu.mit.sipg.units.OilUnits;
import edu.mit.sipg.units.TimeUnits;
import edu.mit.sipg.units.WaterUnits;

/**
 * The default implementation of the electricity element interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultElectricityElement extends DefaultInfrastructureElement
		implements ElectricityElement {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	
	/**
	 * Builder function to create a new distribution element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxElectricityInput the max electricity input
	 * @param initialElectricityInput the initial electricity input
	 * @param variableOperationsCostOfElectricityDistribution the variable operations cost of electricity distribution
	 * @return the electricity element
	 */
	public static ElectricityElement createDistributionElement(
			String templateName, String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double distributionEfficiency,
			double maxElectricityInput, double initialElectricityInput,
			double variableOperationsCostOfElectricityDistribution) {
		return new DefaultElectricityElement(templateName, name, origin, 
				destination, lifecycleModel, 0, 
				0, 0, 0, 0, distributionEfficiency, maxElectricityInput, 
				initialElectricityInput, variableOperationsCostOfElectricityDistribution);
	}
	
	/**
	 * Builder function to create a new production element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param maxElectricityProduction the max electricity production
	 * @param initialElectricityProduction the initial electricity production
	 * @param petroleumIntensityOfElectricityProduction the petroleum intensity of electricity production
	 * @param waterIntensityOfElectricityProduction the water intensity of electricity production
	 * @param variableOperationsCostOfElectricityProduction the variable operations cost of electricity production
	 * @return the electricity element
	 */
	public static ElectricityElement createProductionElement(
			String templateName, String name, 
			String origin, String destination, 
			LifecycleModel lifecycleModel, double maxElectricityProduction, 
			double initialElectricityProduction, 
			double petroleumIntensityOfElectricityProduction,
			double waterIntensityOfElectricityProduction,
			double variableOperationsCostOfElectricityProduction) {
		return new DefaultElectricityElement(templateName, name, origin, 
				destination, lifecycleModel, maxElectricityProduction, 
				initialElectricityProduction, petroleumIntensityOfElectricityProduction,
				waterIntensityOfElectricityProduction, 
				variableOperationsCostOfElectricityProduction, 0, 0, 0, 0);
	}
	
	private final double maxElectricityProduction;
	private final double initialElectricityProduction;
	private final double petroleumIntensityOfElectricityProduction;
	private final double waterIntensityOfElectricityProduction;
	private final double variableOperationsCostOfElectricityProduction;

	private final double maxElectricityInput;
	private final double initialElectricityInput;
	
	private final double distributionEfficiency;
	private final double variableOperationsCostOfElectricityDistribution;
	private double electricityInput;
	private double electricityProduction;
	
	/**
	 * Instantiates a new default electricity element.
	 */
	protected DefaultElectricityElement() {
		maxElectricityProduction = 0;
		initialElectricityProduction = 0;
		petroleumIntensityOfElectricityProduction = 0;
		waterIntensityOfElectricityProduction = 0;
		distributionEfficiency = 0;
		variableOperationsCostOfElectricityProduction = 0;
		
		maxElectricityInput = 0;
		initialElectricityInput = 0;
		variableOperationsCostOfElectricityDistribution = 0;
	}
	
	/**
	 * Instantiates a new default electricity element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param maxElectricityProduction the max electricity production
	 * @param initialElectricityProduction the initial electricity production
	 * @param petroleumIntensityOfElectricityProduction the petroleum intensity of electricity production
	 * @param waterIntensityOfElectricityProduction the water intensity of electricity production
	 * @param variableOperationsCostOfElectricityProduction the variable operations cost of electricity production
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxElectricityInput the max electricity input
	 * @param initialElectricityInput the initial electricity input
	 * @param variableOperationsCostOfElectricityDistribution the variable operations cost of electricity distribution
	 */
	protected DefaultElectricityElement(String templateName, String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double maxElectricityProduction, 
			double initialElectricityProduction, 
			double petroleumIntensityOfElectricityProduction,
			double waterIntensityOfElectricityProduction, 
			double variableOperationsCostOfElectricityProduction,
			double distributionEfficiency, double maxElectricityInput, 
			double initialElectricityInput,
			double variableOperationsCostOfElectricityDistribution) {
		super(templateName, name, origin, destination, lifecycleModel);
		
		if(maxElectricityProduction < 0) {
			throw new IllegalArgumentException(
					"Maximum electricity production cannot be negative.");
		}
		this.maxElectricityProduction = maxElectricityProduction;
		
		if(initialElectricityProduction > maxElectricityProduction) {
			throw new IllegalArgumentException(
					"Initial electricity production cannot exceed maximum.");
		} else if(initialElectricityProduction < 0) {
			throw new IllegalArgumentException(
					"Initial electricity production cannot be negative.");
		}
		this.initialElectricityProduction = initialElectricityProduction;
		
		if(petroleumIntensityOfElectricityProduction < 0) {
			throw new IllegalArgumentException(
					"Petroleum intensity cannot be negative.");
		}
		this.petroleumIntensityOfElectricityProduction = petroleumIntensityOfElectricityProduction;
		
		if(waterIntensityOfElectricityProduction < 0) {
			throw new IllegalArgumentException(
					"Water intensity cannot be negative.");
		}
		this.waterIntensityOfElectricityProduction = waterIntensityOfElectricityProduction;
		
		if(variableOperationsCostOfElectricityProduction < 0) {
			throw new IllegalArgumentException(
					"Variable cost of production cannot be negative.");
		}
		this.variableOperationsCostOfElectricityProduction = variableOperationsCostOfElectricityProduction;
		
		if(distributionEfficiency < 0 || distributionEfficiency > 1) {
			throw new IllegalArgumentException(
					"Distribution efficiency must be between 0 and 1.");
		}
		this.distributionEfficiency = distributionEfficiency;
		
		if(maxElectricityInput < 0) {
			throw new IllegalArgumentException(
					"Maximum electricity input cannot be negative.");
		}
		this.maxElectricityInput = maxElectricityInput;
		
		if(initialElectricityInput > maxElectricityInput) {
			throw new IllegalArgumentException(
					"Initial electricity input cannot exceed maximum.");
		} else if(initialElectricityInput < 0) {
			throw new IllegalArgumentException(
					"Initial electricity input cannot be negative.");
		}
		this.initialElectricityInput = initialElectricityInput;
		
		if(variableOperationsCostOfElectricityDistribution < 0) {
			throw new IllegalArgumentException(
					"Variable cost of distribution cannot be negative.");
		}
		this.variableOperationsCostOfElectricityDistribution = variableOperationsCostOfElectricityDistribution;
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
	public double getElectricityInput() {
		if(isOperational()) {
			return electricityInput;
		} else {
			return 0;
		}
	}

	@Override
	public double getElectricityOutput() {
		if(isOperational()) {
			return electricityInput * distributionEfficiency;
		} else {
			return 0;
		}
	}

	@Override
	public double getElectricityProduction() {
		if(isOperational()) {
			return electricityProduction;
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
	public double getMaxElectricityInput() {
		if(isOperational()) {
			return maxElectricityInput;
		} else {
			return 0;
		}
	}
	
	@Override
	public double getMaxElectricityProduction() {
		if(isOperational()) {
			return maxElectricityProduction;
		} else {
			return 0;
		}
	}

	@Override
	public EditableElectricityElement getMutableElement() {
		EditableElectricityElement element = new EditableElectricityElement();
		setMutableFields(element);
		element.setMaxElectricityProduction(maxElectricityProduction);
		element.setInitialElectricityProduction(initialElectricityProduction);
		element.setPetroleumIntensityOfElectricityProduction(
				petroleumIntensityOfElectricityProduction);
		element.setWaterIntensityOfElectricityProduction(
				waterIntensityOfElectricityProduction);
		element.setVariableOperationsCostOfElectricityProduction(
				variableOperationsCostOfElectricityProduction);
		element.setMaxElectricityInput(maxElectricityInput);
		element.setInitialElectricityInput(initialElectricityInput);
		element.setDistributionEfficiency(
				distributionEfficiency);
		element.setVariableOperationsCostOfElectricityDistribution(
				variableOperationsCostOfElectricityDistribution);
		
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
	public double getPetroleumConsumption() {
		if(isOperational()) {
			return electricityProduction * petroleumIntensityOfElectricityProduction;
		} else {
			return 0;
		}
	}

	@Override
	public double getPetroleumIntensityOfElectricityProduction() {
		return petroleumIntensityOfElectricityProduction;
	}
	
	@Override
	public double getTotalOperationsExpense() {
		return getFixedOperationsExpense() 
				+ variableOperationsCostOfElectricityProduction * electricityProduction 
				+ variableOperationsCostOfElectricityDistribution * electricityInput;
	}
	
	@Override
	public double getVariableOperationsCostOfElectricityDistribution() {
		return variableOperationsCostOfElectricityDistribution;
	}
	
	@Override
	public double getVariableOperationsCostOfElectricityProduction() {
		return variableOperationsCostOfElectricityProduction;
	}

	@Override
	public double getWaterConsumption() {
		if(isOperational()) {
			return electricityProduction * waterIntensityOfElectricityProduction;
		} else {
			return 0;
		}
	}

	@Override
	public double getWaterIntensityOfElectricityProduction() {
		return waterIntensityOfElectricityProduction;
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
	public void initialize(long time) {
		super.initialize(time);
		setElectricityInput(initialElectricityInput);
		setElectricityProduction(initialElectricityProduction);
	}

	@Override
	public boolean isRenewableElectricity() {
		return petroleumIntensityOfElectricityProduction == 0;
	}

	@Override
	public void setElectricityInput(double electricityInput) {
		if(electricityInput < 0) {
			throw new IllegalArgumentException(
					"Electricity input cannot be negative.");
		} else if(electricityInput > maxElectricityInput) {
			throw new IllegalArgumentException(
					"Electricity input cannot exceed maximum.");
		}
		this.electricityInput = electricityInput;
		fireElementChangeEvent();
	}

	@Override
	public void setElectricityProduction(double electricityProduction) {
		if(electricityProduction > maxElectricityProduction) {
			throw new IllegalArgumentException(
					"Electricity production cannot exceed maximum.");
		} else if(electricityProduction < 0) {
			throw new IllegalArgumentException(
					"Electricity production cannot be negative.");
		}
		this.electricityProduction = electricityProduction;
		fireElementChangeEvent();
	}
}
