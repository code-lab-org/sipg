package edu.mit.sips.core.agriculture;

import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.FoodUnitsOutput;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Interface AgricultureElement.
 */
public interface AgricultureElement extends InfrastructureElement, FoodUnitsOutput, CurrencyUnitsOutput, WaterUnitsOutput {
	
	/**
	 * Gets the cost intensity of land used.
	 *
	 * @return the cost intensity of land used
	 */
	public double getCostIntensityOfLandUsed();
	
	/**
	 * Gets the distribution efficiency.
	 *
	 * @return the distribution efficiency
	 */
	public double getDistributionEfficiency();

	/**
	 * Gets the food input.
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
	 * Gets the food output.
	 *
	 * @return the food output
	 */
	public double getFoodOutput();
	
	/**
	 * Gets the food production.
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
	 * Gets the land area.
	 *
	 * @return the land area
	 */
	public double getLandArea();
	
	/**
	 * Gets the max food input.
	 *
	 * @return the max food input
	 */
	public double getMaxFoodInput();
	
	/**
	 * Gets the max land area.
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
	 * Gets the water consumption.
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
	 * Sets the food input.
	 *
	 * @param foodInput the new food input
	 */
	public void setFoodInput(double foodInput);
	
	/**
	 * Sets the land area.
	 *
	 * @param landArea the new land area
	 */
	public void setLandArea(double landArea);
}
