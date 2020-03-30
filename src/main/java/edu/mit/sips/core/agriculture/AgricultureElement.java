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

import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.units.FoodUnitsOutput;
import edu.mit.sips.units.WaterUnitsOutput;

/**
 * An interface to agriculture sector infrastructure elements.
 * 
 * @author Paul T. Grogan
 */
public interface AgricultureElement extends InfrastructureElement, 
		FoodUnitsOutput, WaterUnitsOutput {
	
	/**
	 * Gets the cost intensity of land used.
	 *
	 * @return the cost intensity of land used
	 */
	public double getCostIntensityOfLandUsed();
	
	/**
	 * Gets the food distribution efficiency.
	 *
	 * @return the distribution efficiency
	 */
	public double getDistributionEfficiency();

	/**
	 * Gets the quantity of food input to distribution.
	 *
	 * @return the food input
	 */
	public double getFoodInput();

	/**
	 * Gets the food intensity of land used.
	 *
	 * @return the food intensity of land used
	 */
	public double getFoodIntensityOfLandUsed();
	
	/**
	 * Gets the quantity of food output from distribution.
	 *
	 * @return the food output
	 */
	public double getFoodOutput();
	
	/**
	 * Gets the quantity of food produced by this element.
	 *
	 * @return the food production
	 */
	public double getFoodProduction();
	
	/**
	 * Gets the labor intensity of land used.
	 *
	 * @return the labor intensity of land used
	 */
	public double getLaborIntensityOfLandUsed();
	
	/**
	 * Gets the land area used by this agriculture element.
	 *
	 * @return the land area
	 */
	public double getLandArea();
	
	/**
	 * Gets the maximum quantity of food input to this element.
	 *
	 * @return the max food input
	 */
	public double getMaxFoodInput();
	
	/**
	 * Gets the maximum quantity of food produced by this element.
	 *
	 * @return the max food production
	 */
	public double getMaxFoodProduction();
	
	/**
	 * Gets the maximum amount of land area used by this element.
	 *
	 * @return the max land area
	 */
	public double getMaxLandArea();

	/**
	 * Gets the variable operations cost of food distribution.
	 *
	 * @return the variable operations cost of food distribution
	 */
	public double getVariableOperationsCostOfFoodDistribution();
	
	/**
	 * Gets the quantity of water consumed by this element.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
	
	/**
	 * Gets the water intensity of land used.
	 *
	 * @return the water intensity of land used
	 */
	public double getWaterIntensityOfLandUsed();
	
	/**
	 * Sets the quantity of food input to distribution by this element.
	 *
	 * @param foodInput the new food input
	 */
	public void setFoodInput(double foodInput);
	
	/**
	 * Sets the quantity of land area used by this element.
	 *
	 * @param landArea the new land area
	 */
	public void setLandArea(double landArea);
}
