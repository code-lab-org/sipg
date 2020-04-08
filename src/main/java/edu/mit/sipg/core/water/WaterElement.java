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
package edu.mit.sipg.core.water;

import edu.mit.sipg.core.base.InfrastructureElement;
import edu.mit.sipg.units.ElectricityUnitsOutput;
import edu.mit.sipg.units.WaterUnitsOutput;

/**
 * An interface to water sector infrastructure elements.
 * 
 * @author Paul T. Grogan
 */
public interface WaterElement extends InfrastructureElement, 
		WaterUnitsOutput, ElectricityUnitsOutput {

	/**
	 * Gets the water distribution efficiency.
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
	 * Gets the quantity of electricity consumed by this element.
	 *
	 * @return the electricity consumption
	 */
	public double getElectricityConsumption();
	
	/**
	 * Gets the maximum water input to this element.
	 *
	 * @return the max water input
	 */
	public double getMaxWaterInput();
	
	/**
	 * Gets the maximum water produced by this element.
	 *
	 * @return the max water production
	 */
	public double getMaxWaterProduction();
	
	/**
	 * Gets the aquifer intensity of water production.
	 *
	 * @return the aquifer intensity of water production
	 */
	public double getAquiferIntensityOfWaterProduction();
	
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
	 * Gets the quantity of water input to this element.
	 *
	 * @return the water input
	 */
	public double getWaterInput();
	
	/**
	 * Gets the quantity of water output from this element.
	 *
	 * @return the water output
	 */
	public double getWaterOutput();
	
	/**
	 * Gets the quantity of water produced by this element.
	 *
	 * @return the water production
	 */
	public double getWaterProduction();
	
	/**
	 * Gets the quantity of aquifer withdrawals from this element.
	 *
	 * @return the aquifer withdrawals
	 */
	public double getAquiferWithdrawals();

	/**
	 * Checks if coastal access is required for this element.
	 *
	 * @return true, if is coastal access required
	 */
	public boolean isCoastalAccessRequired();

	/**
	 * Sets the quantity of water input to this element.
	 *
	 * @param waterInput the new water input
	 */
	public void setWaterInput(double waterInput);

	/**
	 * Sets the quantity of water produced by this element.
	 *
	 * @param waterProduction the new water production
	 */
	public void setWaterProduction(double waterProduction);
}
