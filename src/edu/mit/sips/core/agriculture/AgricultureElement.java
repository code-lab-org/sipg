package edu.mit.sips.core.agriculture;

import edu.mit.sips.core.InfrastructureElement;

/**
 * The Interface AgricultureElement.
 */
public interface AgricultureElement extends InfrastructureElement {
	
	/**
	 * Gets the variable operations cost of food distribution.
	 *
	 * @return the variable operations cost of food distribution
	 */
	public double getVariableOperationsCostOfFoodDistribution();
	
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
	 * Gets the product.
	 *
	 * @return the product
	 */
	public AgricultureProduct getProduct();
	
	/**
	 * Gets the water consumption.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
	
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
