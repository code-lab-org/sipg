package edu.mit.sips.core.energy;

/**
 * The Interface PetroleumElement.
 */
public interface PetroleumElement extends EnergyElement {

	/**
	 * Gets the distribution efficiency.
	 *
	 * @return the distribution efficiency
	 */
	public double getDistributionEfficiency();
	
	/**
	 * Gets the electrical intensity of petroleum distribution.
	 *
	 * @return the electrical intensity of petroleum distribution
	 */
	public double getElectricalIntensityOfPetroleumDistribution();
	
	/**
	 * Gets the electricity consumption.
	 *
	 * @return the electricity consumption
	 */
	public double getElectricityConsumption();

	/**
	 * Gets the max petroleum input.
	 *
	 * @return the max petroleum input
	 */
	public double getMaxPetroleumInput();
	
	/**
	 * Gets the max petroleum production.
	 *
	 * @return the max petroleum production
	 */
	public double getMaxPetroleumProduction();

	/**
	 * Gets the petroleum input.
	 *
	 * @return the petroleum input
	 */
	public double getPetroleumInput() ;

	/**
	 * Gets the petroleum output.
	 *
	 * @return the petroleum output
	 */
	public double getPetroleumOutput();

	/**
	 * Gets the petroleum production.
	 *
	 * @return the petroleum production
	 */
	public double getPetroleumProduction();

	/**
	 * Gets the petroleum withdrawals.
	 *
	 * @return the petroleum withdrawals
	 */
	public double getPetroleumWithdrawals();
	
	/**
	 * Gets the reservoir intensity of petroleum production.
	 *
	 * @return the reservoir intensity of petroleum production
	 */
	public double getReservoirIntensityOfPetroleumProduction();
	
	/**
	 * Gets the variable operations cost of petroleum distribution.
	 *
	 * @return the variable operations cost of petroleum distribution
	 */
	public double getVariableOperationsCostOfPetroleumDistribution();

	/**
	 * Gets the variable operations cost of petroleum production.
	 *
	 * @return the variable operations cost of petroleum production
	 */
	public double getVariableOperationsCostOfPetroleumProduction();

	/**
	 * Sets the petroleum input.
	 *
	 * @param petroleumInput the new petroleum input
	 */
	public void setPetroleumInput(double petroleumInput);

	/**
	 * Sets the petroleum production.
	 *
	 * @param petroleumProduction the new petroleum production
	 */
	public void setPetroleumProduction(double petroleumProduction);
}
