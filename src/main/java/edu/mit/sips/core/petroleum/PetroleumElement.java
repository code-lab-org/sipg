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
package edu.mit.sips.core.petroleum;

import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.units.ElectricityUnitsOutput;
import edu.mit.sips.units.OilUnitsOutput;

/**
 * An interface to petroleum sector infrastructure elements.
 * 
 * @author Paul T. Grogan
 */
public interface PetroleumElement extends InfrastructureElement, 
		ElectricityUnitsOutput, OilUnitsOutput {

	/**
	 * Gets the petroleum distribution efficiency.
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
	 * Gets the quantity of electricity consumed by this element.
	 *
	 * @return the electricity consumption
	 */
	public double getElectricityConsumption();

	/**
	 * Gets the max petroleum input to this element.
	 *
	 * @return the max petroleum input
	 */
	public double getMaxPetroleumInput();
	
	/**
	 * Gets the max petroleum produced by this element.
	 *
	 * @return the max petroleum production
	 */
	public double getMaxPetroleumProduction();

	/**
	 * Gets the petroleum input to this element.
	 *
	 * @return the petroleum input
	 */
	public double getPetroleumInput() ;

	/**
	 * Gets the petroleum output from this element.
	 *
	 * @return the petroleum output
	 */
	public double getPetroleumOutput();

	/**
	 * Gets the petroleum produced by this element.
	 *
	 * @return the petroleum production
	 */
	public double getPetroleumProduction();

	/**
	 * Gets the reservoir withdrawals by this element.
	 *
	 * @return the reservoir withdrawals
	 */
	public double getReservoirWithdrawals();
	
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
	 * Sets the petroleum input to this element.
	 *
	 * @param petroleumInput the new petroleum input
	 */
	public void setPetroleumInput(double petroleumInput);

	/**
	 * Sets the petroleum produced by this element.
	 *
	 * @param petroleumProduction the new petroleum production
	 */
	public void setPetroleumProduction(double petroleumProduction);
}
