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
package edu.mit.sips.core.electricity;

import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * An interface to electricity sector infrastructure elements.
 * 
 * @author Paul T. Grogan
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
	 * Gets the electricity input to this element.
	 *
	 * @return the electricity input
	 */
	public double getElectricityInput();

	/**
	 * Gets the electricity output from this element.
	 *
	 * @return the electricity output
	 */
	public double getElectricityOutput();

	/**
	 * Gets the electricity produced by this element.
	 *
	 * @return the electricity production
	 */
	public double getElectricityProduction();
	
	/**
	 * Gets the maximum electricity input to this element.
	 *
	 * @return the max electricity input
	 */
	public double getMaxElectricityInput();
	
	/**
	 * Gets the maximum electricity produced by this element.
	 *
	 * @return the max electricity production
	 */
	public double getMaxElectricityProduction();
	
	/**
	 * Gets the petroleum consumed by this element.
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
	 * Gets the water consumed by this element.
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
	 * Checks if this element generates renewable electricity.
	 *
	 * @return true, if generates renewable electricity
	 */
	public boolean isRenewableElectricity();

	/**
	 * Sets the electricity input for distribution by this element.
	 *
	 * @param electricityInput the new electricity input
	 */
	public void setElectricityInput(double electricityInput);

	/**
	 * Sets the electricity production for this element.
	 *
	 * @param electricityProduction the new electricity production
	 */
	public void setElectricityProduction(double electricityProduction);
}
