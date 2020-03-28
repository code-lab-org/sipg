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

import edu.mit.sips.core.DefaultMutableInfrastructureElement;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * An implementation of the mutable infrastructure element class for the electricity sector.
 * 
 * @author Paul T. Grogan
 */
public final class MutableElectricityElement extends DefaultMutableInfrastructureElement 
		implements WaterUnitsOutput, OilUnitsOutput, ElectricityUnitsOutput {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	
	private double maxElectricityProduction;
	private double initialElectricityProduction;
	private double petroleumIntensityOfElectricityProduction;
	private double waterIntensityOfElectricityProduction;
	private double variableOperationsCostOfElectricityProduction;
	
	private double maxElectricityInput;
	private double initialElectricityInput;
	private double distributionEfficiency;
	private double variableOperationsCostOfElectricityDistribution;

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

	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	/**
	 * Gets the initial electricity input to this element.
	 *
	 * @return the initial electricity input
	 */
	public double getInitialElectricityInput() {
		return initialElectricityInput;
	}

	/**
	 * Gets the initial electricity production from this element.
	 *
	 * @return the initial electricity production
	 */
	public double getInitialElectricityProduction() {
		return initialElectricityProduction;
	}

	/**
	 * Gets the max electricity input to this element.
	 *
	 * @return the max electricity input
	 */
	public double getMaxElectricityInput() {
		return maxElectricityInput;
	}

	/**
	 * Gets the max electricity production from this element.
	 *
	 * @return the max electricity production
	 */
	public double getMaxElectricityProduction() {
		return maxElectricityProduction;
	}

	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}

	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
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

	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
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
	 * Sets the initial electricity input to this element.
	 *
	 * @param initialElectricityInput the new initial electricity input
	 */
	public void setInitialElectricityInput(double initialElectricityInput) {
		this.initialElectricityInput = initialElectricityInput;
	}

	/**
	 * Sets the initial electricity production of this element.
	 *
	 * @param initialElectricityProduction the new initial electricity production
	 */
	public void setInitialElectricityProduction(double initialElectricityProduction) {
		this.initialElectricityProduction = initialElectricityProduction;
	}

	/**
	 * Sets the max electricity input to this element.
	 *
	 * @param maxElectricityInput the new max electricity input
	 */
	public void setMaxElectricityInput(double maxElectricityInput) {
		this.maxElectricityInput = maxElectricityInput;
	}

	/**
	 * Sets the max electricity production of this element.
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
