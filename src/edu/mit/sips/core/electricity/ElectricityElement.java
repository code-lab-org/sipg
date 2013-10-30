package edu.mit.sips.core.electricity;

import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Interface ElectricityElement.
 */
public interface ElectricityElement extends InfrastructureElement, 
		WaterUnitsOutput, ElectricityUnitsOutput, OilUnitsOutput {
	
	/**
	 * Gets the distribution efficiency.
	 *
	 * @return the distribution efficiency
	 */
	public double getDistributionEfficiency();
	
	/**
	 * Gets the electricity input.
	 *
	 * @return the electricity input
	 */
	public double getElectricityInput();

	/**
	 * Gets the electricity output.
	 *
	 * @return the electricity output
	 */
	public double getElectricityOutput();

	/**
	 * Gets the electricity production.
	 *
	 * @return the electricity production
	 */
	public double getElectricityProduction();
	
	/**
	 * Gets the max electricity input.
	 *
	 * @return the max electricity input
	 */
	public double getMaxElectricityInput();
	
	/**
	 * Gets the max electricity production.
	 *
	 * @return the max electricity production
	 */
	public double getMaxElectricityProduction();
	
	/**
	 * Gets the petroleum consumption.
	 *
	 * @return the petroleum consumption
	 */
	public double getPetroleumConsumption();

	/**
	 * Gets the petroleum intensity of electricity production.
	 *
	 * @return the petroleum intensity of electricity production
	 */
	public double getPetroleumIntensityOfElectricityProduction();
	
	/**
	 * Gets the variable operations cost of electricity distribution.
	 *
	 * @return the variable operations cost of electricity distribution
	 */
	public double getVariableOperationsCostOfElectricityDistribution();

	/**
	 * Gets the variable operations cost of electricity production.
	 *
	 * @return the variable operations cost of electricity production
	 */
	public double getVariableOperationsCostOfElectricityProduction();

	/**
	 * Gets the water consumption.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
	
	/**
	 * Gets the water intensity of electricity production.
	 *
	 * @return the water intensity of electricity production
	 */
	public double getWaterIntensityOfElectricityProduction();
	
	/**
	 * Checks if is renewable electricity.
	 *
	 * @return true, if is renewable electricity
	 */
	public boolean isRenewableElectricity();

	/**
	 * Sets the electricity input.
	 *
	 * @param electricityInput the new electricity input
	 */
	public void setElectricityInput(double electricityInput);

	/**
	 * Sets the electricity production.
	 *
	 * @param electricityProduction the new electricity production
	 */
	public void setElectricityProduction(double electricityProduction);
}
