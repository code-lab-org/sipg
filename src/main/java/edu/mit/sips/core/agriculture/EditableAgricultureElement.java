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

import edu.mit.sips.core.base.DefaultMutableInfrastructureElement;
import edu.mit.sips.units.CurrencyUnits;
import edu.mit.sips.units.DefaultUnits;
import edu.mit.sips.units.FoodUnits;
import edu.mit.sips.units.FoodUnitsOutput;
import edu.mit.sips.units.TimeUnits;
import edu.mit.sips.units.WaterUnits;
import edu.mit.sips.units.WaterUnitsOutput;

/**
 * An implementation of the editable infrastructure element class for the agriculture sector.
 * 
 * @author Paul T. Grogan
 */
public final class EditableAgricultureElement extends DefaultMutableInfrastructureElement 
		implements FoodUnitsOutput, WaterUnitsOutput {
	private static final TimeUnits foodTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	
	private double maxLandArea;
	private double initialLandArea;
	private double foodIntensityOfLandUsed;
	private double costIntensityOfLandUsed;
	private double waterIntensityOfLandUsed;
	private double laborIntensityOfLandUsed;

	private double maxFoodInput;
	private double initialFoodInput;
	private double distributionEfficiency;
	private double variableOperationsCostOfFoodDistribution;
	
	/**
	 * Creates a new agriculture element from this editable element.
	 *
	 * @return the agriculture element
	 */
	public AgricultureElement createElement() {
		DefaultAgricultureElement e = new DefaultAgricultureElement(); // for units
		return new DefaultAgricultureElement(
				getTemplateName(), 
				getName(), 
				getOrigin(), 
				getDestination(), 
				getLifecycleModel().createLifecycleModel(), 
				getMaxLandArea(), 
				getInitialLandArea(), 
				FoodUnits.convertFlow(getFoodIntensityOfLandUsed(), this, e), 
				CurrencyUnits.convertFlow(getCostIntensityOfLandUsed(), this, e), 
				WaterUnits.convertFlow(getWaterIntensityOfLandUsed(), this, e), 
				getLaborIntensityOfLandUsed(), 
				getDistributionEfficiency(), 
				FoodUnits.convertFlow(getMaxFoodInput(), this, e), 
				FoodUnits.convertFlow(getInitialFoodInput(), this, e), 
				DefaultUnits.convert(
					getVariableOperationsCostOfFoodDistribution(),
					getCurrencyUnits(), getFoodUnits(), 
					e.getCurrencyUnits(), e.getFoodUnits()
				)
			);
	}
	
	/**
	 * Gets the cost intensity of land used.
	 *
	 * @return the cost intensity of land used
	 */
	public double getCostIntensityOfLandUsed() {
		return costIntensityOfLandUsed;
	}
	
	/**
	 * Gets the distribution efficiency.
	 *
	 * @return the distribution efficiency
	 */
	public double getDistributionEfficiency() {
		return distributionEfficiency;
	}
	
	/**
	 * Gets the food intensity of land used.
	 *
	 * @return the food intensity of land used
	 */
	public double getFoodIntensityOfLandUsed() {
		return foodIntensityOfLandUsed;
	}
	
	@Override
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}
	
	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
	}
	
	/**
	 * Gets the initial food input.
	 *
	 * @return the initial food input
	 */
	public double getInitialFoodInput() {
		return initialFoodInput;
	}
	
	/**
	 * Gets the initial land area.
	 *
	 * @return the initial land area
	 */
	public double getInitialLandArea() {
		return initialLandArea;
	}
	
	/**
	 * Gets the labor intensity of land used.
	 *
	 * @return the labor intensity of land used
	 */
	public double getLaborIntensityOfLandUsed() {
		return laborIntensityOfLandUsed;
	}
	
	/**
	 * Gets the max food input.
	 *
	 * @return the max food input
	 */
	public double getMaxFoodInput() {
		return maxFoodInput;
	}
	
	/**
	 * Gets the max food production.
	 *
	 * @return the max food production
	 */
	public double getMaxFoodProduction() {
		return maxLandArea * foodIntensityOfLandUsed;
	}
	
	/**
	 * Gets the max land area.
	 *
	 * @return the max land area
	 */
	public double getMaxLandArea() {
		return maxLandArea;
	}
	
	/**
	 * Gets the variable operations cost of food distribution.
	 *
	 * @return the variable operations cost of food distribution
	 */
	public double getVariableOperationsCostOfFoodDistribution() {
		return variableOperationsCostOfFoodDistribution;
	}
	
	/**
	 * Gets the water intensity of land used.
	 *
	 * @return the water intensity of land used
	 */
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

	/**
	 * Sets the cost intensity of land used.
	 *
	 * @param costIntensityOfLandUsed the new cost intensity of land used
	 */
	public void setCostIntensityOfLandUsed(double costIntensityOfLandUsed) {
		this.costIntensityOfLandUsed = costIntensityOfLandUsed;
	}

	/**
	 * Sets the distribution efficiency.
	 *
	 * @param distributionEfficiency the new distribution efficiency
	 */
	public void setDistributionEfficiency(double distributionEfficiency) {
		this.distributionEfficiency = distributionEfficiency;
	}

	/**
	 * Sets the food intensity of land used.
	 *
	 * @param foodIntensityOfLandUsed the new food intensity of land used
	 */
	public void setFoodIntensityOfLandUsed(double foodIntensityOfLandUsed) {
		this.foodIntensityOfLandUsed = foodIntensityOfLandUsed;
	}

	/**
	 * Sets the initial food input.
	 *
	 * @param initialFoodInput the new initial food input
	 */
	public void setInitialFoodInput(double initialFoodInput) {
		this.initialFoodInput = initialFoodInput;
	}

	/**
	 * Sets the initial land area.
	 *
	 * @param initialLandArea the new initial land area
	 */
	public void setInitialLandArea(double initialLandArea) {
		this.initialLandArea = initialLandArea;
	}

	/**
	 * Sets the labor intensity of land used.
	 *
	 * @param laborIntensityOfLandUsed the new labor intensity of land used
	 */
	public void setLaborIntensityOfLandUsed(double laborIntensityOfLandUsed) {
		this.laborIntensityOfLandUsed = laborIntensityOfLandUsed;
	}

	/**
	 * Sets the max food input.
	 *
	 * @param maxFoodInput the new max food input
	 */
	public void setMaxFoodInput(double maxFoodInput) {
		this.maxFoodInput = maxFoodInput;
	}

	/**
	 * Sets the max land area.
	 *
	 * @param maxLandArea the new max land area
	 */
	public void setMaxLandArea(double maxLandArea) {
		this.maxLandArea = maxLandArea;
	}

	/**
	 * Sets the variable operations cost of food distribution.
	 *
	 * @param variableOperationsCostOfFoodDistribution the new variable operations cost of food distribution
	 */
	public void setVariableOperationsCostOfFoodDistribution(
			double variableOperationsCostOfFoodDistribution) {
		this.variableOperationsCostOfFoodDistribution = variableOperationsCostOfFoodDistribution;
	}

	/**
	 * Sets the water intensity of land used.
	 *
	 * @param waterIntensityOfLandUsed the new water intensity of land used
	 */
	public void setWaterIntensityOfLandUsed(double waterIntensityOfLandUsed) {
		this.waterIntensityOfLandUsed = waterIntensityOfLandUsed;
	}
}
