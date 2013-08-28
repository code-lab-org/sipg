package edu.mit.sips.core.agriculture;

import edu.mit.sips.core.DefaultMutableInfrastructureElement;

/**
 * The Class MutableAgricultureElement.
 */
public final class MutableAgricultureElement extends DefaultMutableInfrastructureElement {
	private double maxLandArea;
	private double initialLandArea;
	private AgricultureProduct product = AgricultureProduct.NONE;

	private double maxFoodInput;
	private double initialFoodInput;
	private double distributionEfficiency;
	private double variableOperationsCostOfFoodDistribution;
	
	/**
	 * Creates the element.
	 *
	 * @param element the element
	 * @return the agriculture element
	 */
	public AgricultureElement createElement() {
		return new DefaultAgricultureElement(getTemplateName(), getName(), getOrigin(), 
				getDestination(), getLifecycleModel().createLifecycleModel(), 
				getMaxLandArea(),  getInitialLandArea(), 
				getProduct(), getDistributionEfficiency(), 
				getMaxFoodInput(), getInitialFoodInput(), 
				getVariableOperationsCostOfFoodDistribution());
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
	 * Gets the max food input.
	 *
	 * @return the max food input
	 */
	public double getMaxFoodInput() {
		return maxFoodInput;
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
	 * Gets the product.
	 *
	 * @return the product
	 */
	public AgricultureProduct getProduct() {
		return product;
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
	 * Sets the distribution efficiency.
	 *
	 * @param distributionEfficiency the new distribution efficiency
	 */
	public void setDistributionEfficiency(double distributionEfficiency) {
		this.distributionEfficiency = distributionEfficiency;
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
	 * Sets the product.
	 *
	 * @param product the new product
	 */
	public void setProduct(AgricultureProduct product) {
		this.product = product;
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
}
