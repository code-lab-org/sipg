package edu.mit.sips.core.water;

import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Interface WaterElement.
 */
public interface WaterElement extends InfrastructureElement, WaterUnitsOutput {

	/**
	 * Gets the distribution efficiency.
	 *
	 * @return the distribution efficiency
	 */
	public double getDistributionEfficiency();
	
	/**
	 * Gets the electrical intensity of water distribution.
	 *
	 * @return the electrical intensity of water distribution
	 */
	public double getElectricalIntensityOfWaterDistribution();
	
	/**
	 * Gets the electrical intensity of water production.
	 *
	 * @return the electrical intensity of water production
	 */
	public double getElectricalIntensityOfWaterProduction();
	
	/**
	 * Gets the electricity consumption.
	 *
	 * @return the electricity consumption
	 */
	public double getElectricityConsumption();
	
	/**
	 * Gets the max water input.
	 *
	 * @return the max water input
	 */
	public double getMaxWaterInput();
	
	/**
	 * Gets the max water production.
	 *
	 * @return the max water production
	 */
	public double getMaxWaterProduction();
	
	/**
	 * Gets the reservoir intensity of water production.
	 *
	 * @return the reservoir intensity of water production
	 */
	public double getReservoirIntensityOfWaterProduction();
	
	/**
	 * Gets the variable operations cost of water distribution.
	 *
	 * @return the variable operations cost of water distribution
	 */
	public double getVariableOperationsCostOfWaterDistribution();
	
	/**
	 * Gets the variable operations cost of water production.
	 *
	 * @return the variable operations cost of water production
	 */
	public double getVariableOperationsCostOfWaterProduction();

	/**
	 * Gets the water input.
	 *
	 * @return the water input
	 */
	public double getWaterInput();
	
	/**
	 * Gets the water output.
	 *
	 * @return the water output
	 */
	public double getWaterOutput();
	
	/**
	 * Gets the water production.
	 *
	 * @return the water production
	 */
	public double getWaterProduction();
	
	/**
	 * Gets the water withdrawals.
	 *
	 * @return the water withdrawals
	 */
	public double getWaterWithdrawals();

	/**
	 * Checks if is coastal access required.
	 *
	 * @return true, if is coastal access required
	 */
	public boolean isCoastalAccessRequired();

	/**
	 * Sets the water input.
	 *
	 * @param waterInput the new water input
	 */
	public void setWaterInput(double waterInput);

	/**
	 * Sets the water production.
	 *
	 * @param waterProduction the new water production
	 */
	public void setWaterProduction(double waterProduction);
}
