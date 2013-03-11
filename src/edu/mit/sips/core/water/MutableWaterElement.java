package edu.mit.sips.core.water;

import edu.mit.sips.core.DefaultMutableInfrastructureElement;

/**
 * The Class MutableWaterElement.
 */
public final class MutableWaterElement extends DefaultMutableInfrastructureElement {
	private double maxWaterProduction;
	private double initialWaterProduction;
	private double reservoirIntensityOfWaterProduction;
	private double electricalIntensityOfWaterProduction;
	private double variableOperationsCostOfWaterProduction;

	private double maxWaterInput;
	private double initialWaterInput;
	private double distributionEfficiency;
	private double electricalIntensityOfWaterDistribution;
	private double variableOperationsCostOfWaterDistribution;
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableInfrastructureElement#createElement()
	 */
	@Override
	public WaterElement createElement() {
		return new DefaultWaterElement(getName(), getOrigin(), 
				getDestination(), getLifecycleModel().createLifecycleModel(), 
				getReservoirIntensityOfWaterProduction(),
				getMaxWaterProduction(), getInitialWaterProduction(), 
				getElectricalIntensityOfWaterProduction(),
				getVariableOperationsCostOfWaterProduction(),
				getDistributionEfficiency(), getMaxWaterInput(), 
				getInitialWaterInput(), 
				getElectricalIntensityOfWaterDistribution(),
				getVariableOperationsCostOfWaterDistribution());
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
	 * Gets the electrical intensity of water distribution.
	 *
	 * @return the electrical intensity of water distribution
	 */
	public double getElectricalIntensityOfWaterDistribution() {
		return electricalIntensityOfWaterDistribution;
	}
	
	/**
	 * Gets the electrical intensity of water production.
	 *
	 * @return the electrical intensity of water production
	 */
	public double getElectricalIntensityOfWaterProduction() {
		return electricalIntensityOfWaterProduction;
	}
	
	/**
	 * Gets the initial water input.
	 *
	 * @return the initial water input
	 */
	public double getInitialWaterInput() {
		return initialWaterInput;
	}
	
	/**
	 * Gets the initial water production.
	 *
	 * @return the initial water production
	 */
	public double getInitialWaterProduction() {
		return initialWaterProduction;
	}
	
	/**
	 * Gets the max water input.
	 *
	 * @return the max water input
	 */
	public double getMaxWaterInput() {
		return maxWaterInput;
	}
	
	/**
	 * Gets the max water production.
	 *
	 * @return the max water production
	 */
	public double getMaxWaterProduction() {
		return maxWaterProduction;
	}
	
	/**
	 * Gets the reservoir intensity of water production.
	 *
	 * @return the reservoir intensity of water production
	 */
	public double getReservoirIntensityOfWaterProduction() {
		return reservoirIntensityOfWaterProduction;
	}
	
	/**
	 * Gets the variable operations cost of water distribution.
	 *
	 * @return the variable operations cost of water distribution
	 */
	public double getVariableOperationsCostOfWaterDistribution() {
		return variableOperationsCostOfWaterDistribution;
	}
	
	/**
	 * Gets the variable operations cost of water production.
	 *
	 * @return the variable operations cost of water production
	 */
	public double getVariableOperationsCostOfWaterProduction() {
		return variableOperationsCostOfWaterProduction;
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
	 * Sets the electrical intensity of water distribution.
	 *
	 * @param electricalIntensityOfWaterDistribution the new electrical intensity of water distribution
	 */
	public void setElectricalIntensityOfWaterDistribution(
			double electricalIntensityOfWaterDistribution) {
		this.electricalIntensityOfWaterDistribution = electricalIntensityOfWaterDistribution;
	}
	
	/**
	 * Sets the electrical intensity of water production.
	 *
	 * @param electricalIntensityOfWaterProduction the new electrical intensity of water production
	 */
	public void setElectricalIntensityOfWaterProduction(
			double electricalIntensityOfWaterProduction) {
		this.electricalIntensityOfWaterProduction = electricalIntensityOfWaterProduction;
	}
	
	/**
	 * Sets the initial water input.
	 *
	 * @param initialWaterInput the new initial water input
	 */
	public void setInitialWaterInput(double initialWaterInput) {
		this.initialWaterInput = initialWaterInput;
	}
	
	/**
	 * Sets the initial water production.
	 *
	 * @param initialWaterProduction the new initial water production
	 */
	public void setInitialWaterProduction(double initialWaterProduction) {
		this.initialWaterProduction = initialWaterProduction;
	}
	
	/**
	 * Sets the max water input.
	 *
	 * @param maxWaterInput the new max water input
	 */
	public void setMaxWaterInput(double maxWaterInput) {
		this.maxWaterInput = maxWaterInput;
	}
	
	/**
	 * Sets the max water production.
	 *
	 * @param maxWaterProduction the new max water production
	 */
	public void setMaxWaterProduction(double maxWaterProduction) {
		this.maxWaterProduction = maxWaterProduction;
	}
	
	/**
	 * Sets the reservoir intensity of water production.
	 *
	 * @param reservoirIntensityOfWaterProduction the new reservoir intensity of water production
	 */
	public void setReservoirIntensityOfWaterProduction(
			double reservoirIntensityOfWaterProduction) {
		this.reservoirIntensityOfWaterProduction = reservoirIntensityOfWaterProduction;
	}
	
	/**
	 * Sets the variable operations cost of water distribution.
	 *
	 * @param variableOperationsCostOfWaterDistribution the new variable operations cost of water distribution
	 */
	public void setVariableOperationsCostOfWaterDistribution(
			double variableOperationsCostOfWaterDistribution) {
		this.variableOperationsCostOfWaterDistribution = variableOperationsCostOfWaterDistribution;
	}

	/**
	 * Sets the variable operations cost of water production.
	 *
	 * @param variableOperationsCostOfWaterProduction the new variable operations cost of water production
	 */
	public void setVariableOperationsCostOfWaterProduction(
			double variableOperationsCostOfWaterProduction) {
		this.variableOperationsCostOfWaterProduction = variableOperationsCostOfWaterProduction;
	}
}
