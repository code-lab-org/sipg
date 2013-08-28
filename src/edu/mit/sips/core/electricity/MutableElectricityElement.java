package edu.mit.sips.core.electricity;

import edu.mit.sips.core.DefaultMutableInfrastructureElement;

/**
 * The Class MutableElectricityElement.
 */
public final class MutableElectricityElement extends
		DefaultMutableInfrastructureElement {
	private double maxElectricityProduction;
	private double initialElectricityProduction;
	private double petroleumIntensityOfElectricityProduction;
	private double waterIntensityOfElectricityProduction;
	private double variableOperationsCostOfElectricityProduction;
	
	private double maxElectricityInput;
	private double initialElectricityInput;
	private double distributionEfficiency;
	private double variableOperationsCostOfElectricityDistribution;

	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableInfrastructureElement#createElement()
	 */
	@Override
	public ElectricityElement createElement() {
		return new DefaultElectricityElement(getTemplateName(), getName(), 
				getOrigin(), getDestination(),
				getLifecycleModel().createLifecycleModel(), getMaxElectricityProduction(), 
				getInitialElectricityProduction(), 
				getPetroleumIntensityOfElectricityProduction(),
				getWaterIntensityOfElectricityProduction(), 
				getVariableOperationsCostOfElectricityProduction(),
				getDistributionEfficiency(), getMaxElectricityInput(), 
				getInitialElectricityInput(),
				getVariableOperationsCostOfElectricityDistribution());
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
	 * Gets the initial electricity input.
	 *
	 * @return the initial electricity input
	 */
	public double getInitialElectricityInput() {
		return initialElectricityInput;
	}

	/**
	 * Gets the initial electricity production.
	 *
	 * @return the initial electricity production
	 */
	public double getInitialElectricityProduction() {
		return initialElectricityProduction;
	}

	/**
	 * Gets the max electricity input.
	 *
	 * @return the max electricity input
	 */
	public double getMaxElectricityInput() {
		return maxElectricityInput;
	}

	/**
	 * Gets the max electricity production.
	 *
	 * @return the max electricity production
	 */
	public double getMaxElectricityProduction() {
		return maxElectricityProduction;
	}

	/**
	 * Gets the petroleum intensity of electricity production.
	 *
	 * @return the petroleum intensity of electricity production
	 */
	public double getPetroleumIntensityOfElectricityProduction() {
		return petroleumIntensityOfElectricityProduction;
	}

	/**
	 * Gets the variable operations cost of electricity distribution.
	 *
	 * @return the variable operations cost of electricity distribution
	 */
	public double getVariableOperationsCostOfElectricityDistribution() {
		return variableOperationsCostOfElectricityDistribution;
	}

	/**
	 * Gets the variable operations cost of electricity production.
	 *
	 * @return the variable operations cost of electricity production
	 */
	public double getVariableOperationsCostOfElectricityProduction() {
		return variableOperationsCostOfElectricityProduction;
	}

	/**
	 * Gets the water intensity of electricity production.
	 *
	 * @return the water intensity of electricity production
	 */
	public double getWaterIntensityOfElectricityProduction() {
		return waterIntensityOfElectricityProduction;
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
	 * Sets the initial electricity input.
	 *
	 * @param initialElectricityInput the new initial electricity input
	 */
	public void setInitialElectricityInput(double initialElectricityInput) {
		this.initialElectricityInput = initialElectricityInput;
	}

	/**
	 * Sets the initial electricity production.
	 *
	 * @param initialElectricityProduction the new initial electricity production
	 */
	public void setInitialElectricityProduction(double initialElectricityProduction) {
		this.initialElectricityProduction = initialElectricityProduction;
	}

	/**
	 * Sets the max electricity input.
	 *
	 * @param maxElectricityInput the new max electricity input
	 */
	public void setMaxElectricityInput(double maxElectricityInput) {
		this.maxElectricityInput = maxElectricityInput;
	}

	/**
	 * Sets the max electricity production.
	 *
	 * @param maxElectricityProduction the new max electricity production
	 */
	public void setMaxElectricityProduction(double maxElectricityProduction) {
		this.maxElectricityProduction = maxElectricityProduction;
	}

	/**
	 * Sets the petroleum intensity of electricity production.
	 *
	 * @param petroleumIntensityOfElectricityProduction the new petroleum intensity of electricity production
	 */
	public void setPetroleumIntensityOfElectricityProduction(
			double petroleumIntensityOfElectricityProduction) {
		this.petroleumIntensityOfElectricityProduction = petroleumIntensityOfElectricityProduction;
	}

	/**
	 * Sets the variable operations cost of electricity distribution.
	 *
	 * @param variableOperationsCostOfElectricityDistribution the new variable operations cost of electricity distribution
	 */
	public void setVariableOperationsCostOfElectricityDistribution(
			double variableOperationsCostOfElectricityDistribution) {
		this.variableOperationsCostOfElectricityDistribution = variableOperationsCostOfElectricityDistribution;
	}

	/**
	 * Sets the variable operations cost of electricity production.
	 *
	 * @param variableOperationsCostOfElectricityProduction the new variable operations cost of electricity production
	 */
	public void setVariableOperationsCostOfElectricityProduction(
			double variableOperationsCostOfElectricityProduction) {
		this.variableOperationsCostOfElectricityProduction = variableOperationsCostOfElectricityProduction;
	}

	/**
	 * Sets the water intensity of electricity production.
	 *
	 * @param waterIntensityOfElectricityProduction the new water intensity of electricity production
	 */
	public void setWaterIntensityOfElectricityProduction(
			double waterIntensityOfElectricityProduction) {
		this.waterIntensityOfElectricityProduction = waterIntensityOfElectricityProduction;
	}

}
