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
package edu.mit.sips.core.agriculture;

import edu.mit.sips.core.DefaultInfrastructureElement;
import edu.mit.sips.core.lifecycle.LifecycleModel;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The default implementation of the agriculture element interface.
 * 
 * @author Paul T. Grogan
 */
public final class DefaultAgricultureElement extends DefaultInfrastructureElement implements AgricultureElement {
	private final static TimeUnits foodTimeUnits = TimeUnits.year;
	private final static FoodUnits foodUnits = FoodUnits.GJ;
	private final static TimeUnits waterTimeUnits = TimeUnits.year;
	private final static WaterUnits waterUnits = WaterUnits.m3;
	
	/**
	 * Builder function to create a new distribution element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param efficiency the efficiency
	 * @param maxFoodInput the max food input
	 * @param initialFoodInput the initial food input
	 * @param variableOperationsCostOfFoodDistribution the variable operations cost of food distribution
	 * @return the default agriculture element
	 */
	public static DefaultAgricultureElement createDistributionElement(
			String templateName, String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double efficiency,
			double maxFoodInput, double initialFoodInput, 
			double variableOperationsCostOfFoodDistribution) {
		return new DefaultAgricultureElement(templateName, name, origin, destination, lifecycleModel, 0, 0,
				0, 0, 0, 0, efficiency, maxFoodInput, initialFoodInput, 
				variableOperationsCostOfFoodDistribution);
	}
	
	/**
	 * Builder function to create a new production element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param maxLandArea the max land area
	 * @param initialLandArea the initial land area
	 * @param foodIntensityOfLandUsed the food intensity of land used
	 * @param costIntensityOfLandUsed the cost intensity of land used
	 * @param waterIntensityOfLandUsed the water intensity of land used
	 * @param laborIntensityOfLandUsed the labor intensity of land used
	 * @return the default agriculture element
	 */
	public static DefaultAgricultureElement createProductionElement(
			String templateName, String name, 
			String origin, String destination, 
			LifecycleModel lifecycleModel, double maxLandArea, 
			double initialLandArea, double foodIntensityOfLandUsed, 
			double costIntensityOfLandUsed, double waterIntensityOfLandUsed,
			double laborIntensityOfLandUsed) {
		return new DefaultAgricultureElement(templateName, name, origin, destination, 
				lifecycleModel, maxLandArea, 
				initialLandArea, foodIntensityOfLandUsed, costIntensityOfLandUsed, 
				waterIntensityOfLandUsed, laborIntensityOfLandUsed, 0, 0, 0, 0);
	}
	
	private final double maxLandArea;
	private final double initialLandArea;
	private final double foodIntensityOfLandUsed;
	private final double costIntensityOfLandUsed;
	private final double waterIntensityOfLandUsed;
	private final double laborIntensityOfLandUsed;

	private final double maxFoodInput;
	private final double initialFoodInput;
	private final double distributionEfficiency;
	private final double variableOperationsCostOfFoodDistribution;
	
	private double landArea;
	private double foodInput;
	
	/**
	 * Instantiates a new default agriculture element.
	 */
	protected DefaultAgricultureElement() {
		super();
		
		this.maxLandArea = 0;
		this.initialLandArea = 0;
		this.foodIntensityOfLandUsed = 0;
		this.costIntensityOfLandUsed = 0;
		this.waterIntensityOfLandUsed = 0;
		this.laborIntensityOfLandUsed = 0;
		
		this.distributionEfficiency = 0;
		this.variableOperationsCostOfFoodDistribution = 0;
		
		this.maxFoodInput = 0;
		this.initialFoodInput = 0;
	}
	
	/**
	 * Instantiates a new default agriculture element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param maxLandArea the max land area
	 * @param initialLandArea the initial land area
	 * @param foodIntensityOfLandUsed the food intensity of land used
	 * @param costIntensityOfLandUsed the cost intensity of land used
	 * @param waterIntensityOfLandUsed the water intensity of land used
	 * @param laborIntensityOfLandUsed the labor intensity of land used
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxFoodInput the max food input
	 * @param initialFoodInput the initial food input
	 * @param variableOperationsCostOfFoodDistribution the variable operations cost of food distribution
	 */
	protected DefaultAgricultureElement(String templateName, String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double maxLandArea, 
			double initialLandArea, double foodIntensityOfLandUsed, 
			double costIntensityOfLandUsed, double waterIntensityOfLandUsed,
			double laborIntensityOfLandUsed, double distributionEfficiency, 
			double maxFoodInput, double initialFoodInput, 
			double variableOperationsCostOfFoodDistribution) {
		super(templateName, name, origin, destination, lifecycleModel);
		
		if(maxLandArea < 0) {
			throw new IllegalArgumentException(
					"Maximum land area cannot be negative.");
		}
		this.maxLandArea = maxLandArea;
		
		if(initialLandArea > maxLandArea) {
			throw new IllegalArgumentException(
					"Initial land area cannot exceed maximum.");
		} else if(initialLandArea < 0) {
			throw new IllegalArgumentException(
					"Initial land area cannot be negative.");
		}
		this.initialLandArea = initialLandArea;

		if(foodIntensityOfLandUsed < 0) {
			throw new IllegalArgumentException(
					"Food intensity cannot be negative.");
		}
		this.foodIntensityOfLandUsed = foodIntensityOfLandUsed;

		if(costIntensityOfLandUsed < 0) {
			throw new IllegalArgumentException(
					"Cost intensity cannot be negative.");
		}
		this.costIntensityOfLandUsed = costIntensityOfLandUsed;

		if(waterIntensityOfLandUsed < 0) {
			throw new IllegalArgumentException(
					"Water intensity cannot be negative.");
		}
		this.waterIntensityOfLandUsed = waterIntensityOfLandUsed;

		if(laborIntensityOfLandUsed < 0) {
			throw new IllegalArgumentException(
					"Labor intensity cannot be negative.");
		}
		this.laborIntensityOfLandUsed = laborIntensityOfLandUsed;
		
		if(distributionEfficiency < 0 || distributionEfficiency > 1) {
			throw new IllegalArgumentException(
					"Efficiency must be between 0 and 1.");
		}
		this.distributionEfficiency = distributionEfficiency;
		
		if(maxFoodInput < 0) {
			throw new IllegalArgumentException(
					"Maximum food input cannot be negative.");
		}
		this.maxFoodInput = maxFoodInput;
		
		if(initialFoodInput > maxFoodInput) {
			throw new IllegalArgumentException(
					"Initial food input cannot exceed maximum.");
		} else if(initialFoodInput < 0) {
			throw new IllegalArgumentException(
					"Initial food input cannot be negative.");
		}
		this.initialFoodInput = initialFoodInput;

		if(variableOperationsCostOfFoodDistribution < 0) {
			throw new IllegalArgumentException(
					"Variable cost of distribution cannot be negative.");
		}
		this.variableOperationsCostOfFoodDistribution = variableOperationsCostOfFoodDistribution;
	}

	@Override
	public double getCostIntensityOfLandUsed() {
		return costIntensityOfLandUsed;
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
	public double getFoodInput() {
		if(isOperational()) {
			return foodInput;
		} else {
			return 0;
		}
	}

	@Override
	public double getFoodIntensityOfLandUsed() {
		return foodIntensityOfLandUsed;
	}

	@Override
	public double getFoodOutput() {
		if(isOperational()) {
			return foodInput * distributionEfficiency;
		} else {
			return 0;
		}
	}

	@Override
	public double getFoodProduction() {
		if(isOperational()) {
			return foodIntensityOfLandUsed * landArea;
		} else {
			return 0;
		}
	}

	@Override
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}

	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
	}
	
	@Override
	public double getLaborIntensityOfLandUsed() {
		return laborIntensityOfLandUsed;
	}

	@Override
	public double getLandArea() {
		if(isOperational()) {
			return landArea;
		} else {
			return 0;
		}
	}
	
	@Override
	public double getMaxFoodInput() {
		if(isOperational()) {
			return maxFoodInput;
		} else {
			return 0;
		}
	}

	@Override
	public double getMaxFoodProduction() {
		if(isOperational()) {
			return getMaxLandArea() * getFoodIntensityOfLandUsed();
		} else {
			return 0;
		}
	}
	
	@Override
	public double getMaxLandArea() {
		if(isOperational()) {
			return maxLandArea;
		} else {
			return 0;
		}
	}

	@Override
	public EditableAgricultureElement getMutableElement() {
		EditableAgricultureElement element = new EditableAgricultureElement();
		setMutableFields(element);
		element.setMaxLandArea(maxLandArea);
		element.setInitialLandArea(initialLandArea);
		element.setCostIntensityOfLandUsed(CurrencyUnits.convertFlow(
				costIntensityOfLandUsed, this, element));
		element.setFoodIntensityOfLandUsed(FoodUnits.convertFlow(
				foodIntensityOfLandUsed, this, element));
		element.setWaterIntensityOfLandUsed(WaterUnits.convertFlow(
				waterIntensityOfLandUsed, this, element));
		element.setLaborIntensityOfLandUsed(laborIntensityOfLandUsed);
		element.setMaxFoodInput(FoodUnits.convertFlow(
				maxFoodInput, this, element));
		element.setInitialFoodInput(FoodUnits.convertFlow(
				initialFoodInput, this, element));
		element.setDistributionEfficiency(distributionEfficiency);
		element.setVariableOperationsCostOfFoodDistribution(DefaultUnits.convert(
				variableOperationsCostOfFoodDistribution, 
				getCurrencyUnits(), getFoodUnits(), 
				element.getCurrencyUnits(), element.getFoodUnits()));
		return element;
	}

	@Override
	public double getTotalOperationsExpense() {
		return getFixedOperationsExpense() 
				+ costIntensityOfLandUsed * landArea 
				+ variableOperationsCostOfFoodDistribution * foodInput;
	}

	@Override
	public double getVariableOperationsCostOfFoodDistribution() {
		return variableOperationsCostOfFoodDistribution;
	}

	@Override
	public double getWaterConsumption() {
		if(isOperational()) {
			return waterIntensityOfLandUsed * landArea;
		} else {
			return 0;
		}
	}

	@Override
	public double getWaterIntensityOfLandUsed() {
		return waterIntensityOfLandUsed;
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
		setFoodInput(initialFoodInput);
		setLandArea(initialLandArea);
	}

	@Override
	public void setFoodInput(double foodInput) {
		if(foodInput < 0) {
			throw new IllegalArgumentException(
					"Food input cannot be negative.");
		} else if(foodInput > maxFoodInput) {
			throw new IllegalArgumentException(
					"Food input cannot exceed maximum.");
		}
		this.foodInput = foodInput;
		fireElementChangeEvent();
	}

	@Override
	public void setLandArea(double landArea) {
		if(landArea > maxLandArea) {
			throw new IllegalArgumentException(
					"Land area cannot exceed maximum (" 
							+ landArea + ">" + maxLandArea + ").");
		} else if(landArea < 0) {
			throw new IllegalArgumentException(
					"Land area cannot be negative.");
		}
		this.landArea = landArea;
		fireElementChangeEvent();
	}
}