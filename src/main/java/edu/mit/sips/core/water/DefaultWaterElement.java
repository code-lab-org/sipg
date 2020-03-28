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
package edu.mit.sips.core.water;

import edu.mit.sips.core.base.DefaultInfrastructureElement;
import edu.mit.sips.core.lifecycle.LifecycleModel;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The default implementation of the water element interface.
 * 
 * @author Paul T. Grogan
 */
public final class DefaultWaterElement extends DefaultInfrastructureElement implements WaterElement {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	
	/**
	 * Builder function to create a new distribution water element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxWaterInput the max water input
	 * @param initialWaterInput the initial water input
	 * @param electricalIntensityOfWaterDistribution the electrical intensity of water distribution
	 * @param variableOperationsCostOfWaterDistribution the variable operations cost of water distribution
	 * @return the water element
	 */
	public static DefaultWaterElement createDistributionElement(
			String templateName, String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double distributionEfficiency,
			double maxWaterInput, double initialWaterInput, 
			double electricalIntensityOfWaterDistribution,
			double variableOperationsCostOfWaterDistribution) {
		return new DefaultWaterElement(templateName, name, origin, destination, 
				lifecycleModel, 0, 0, 0, 0, 0, false,
				distributionEfficiency, maxWaterInput, initialWaterInput,
				electricalIntensityOfWaterDistribution,
				variableOperationsCostOfWaterDistribution);
	}
	
	/**
	 * Builder function to create a new production water element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param reservoirIntensityOfWaterProduction the reservoir intensity of water production
	 * @param maxWaterProduction the max water production
	 * @param initialWaterProduction the initial water production
	 * @param electricalIntensityOfWaterProduction the electrical intensity of water production
	 * @param variableOperationsCostOfWaterProduction the variable operations cost of water production
	 * @param coastalAccessRequired the coastal access required
	 * @return the water element
	 */
	public static DefaultWaterElement createProductionElement(
			String templateName, String name, 
			String origin, String destination, 
			LifecycleModel lifecycleModel, double reservoirIntensityOfWaterProduction,
			double maxWaterProduction, double initialWaterProduction, 
			double electricalIntensityOfWaterProduction,
			double variableOperationsCostOfWaterProduction,
			boolean coastalAccessRequired) {
		return new DefaultWaterElement(templateName, name, origin, 
				destination, lifecycleModel, 
				reservoirIntensityOfWaterProduction, maxWaterProduction, initialWaterProduction, 
				electricalIntensityOfWaterProduction,
				variableOperationsCostOfWaterProduction, coastalAccessRequired,
				0, 0, 0, 0, 0);
	}

	private final double maxWaterProduction;
	private final double initialWaterProduction;
	private final double reservoirIntensityOfWaterProduction;
	private final double electricalIntensityOfWaterProduction;
	private final double variableOperationsCostOfWaterProduction;
	private final boolean coastalAccessRequired;

	private final double maxWaterInput;
	private final double initialWaterInput;
	private final double distributionEfficiency;
	private final double electricalIntensityOfWaterDistribution;
	private final double variableOperationsCostOfWaterDistribution;

	private double waterProduction;
	private double waterInput;
	
	/**
	 * Instantiates a new default water element.
	 */
	protected DefaultWaterElement() {
		super();
		
		reservoirIntensityOfWaterProduction = 0;
		maxWaterProduction = 0;
		electricalIntensityOfWaterProduction = 0;
		variableOperationsCostOfWaterProduction = 0;
		coastalAccessRequired = false;

		initialWaterProduction = 0;
		distributionEfficiency = 0;
		maxWaterInput = 0;
		initialWaterInput = 0;
		electricalIntensityOfWaterDistribution = 0;
		variableOperationsCostOfWaterDistribution = 0;
	}
	
	/**
	 * Instantiates a new water element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param reservoirIntensityOfWaterProduction the reservoir intensity of water production
	 * @param maxWaterProduction the max water production
	 * @param initialWaterProduction the initial water production
	 * @param electricalIntensityOfWaterProduction the electrical intensity of water production
	 * @param variableOperationsCostOfWaterProduction the variable operations cost of water production
	 * @param coastalAccessRequired the coastal access required
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxWaterInput the max water input
	 * @param initialWaterInput the initial water input
	 * @param electricalIntensityOfWaterDistribution the electrical intensity of water distribution
	 * @param variableOperationsCostOfWaterDistribution the variable operations cost of water distribution
	 */
	protected DefaultWaterElement(String templateName, String name, 
			String origin, String destination, 
			LifecycleModel lifecycleModel, double reservoirIntensityOfWaterProduction,
			double maxWaterProduction, double initialWaterProduction, 
			double electricalIntensityOfWaterProduction, 
			double variableOperationsCostOfWaterProduction,
			boolean coastalAccessRequired,
			double distributionEfficiency, double maxWaterInput, 
			double initialWaterInput, double electricalIntensityOfWaterDistribution,
			double variableOperationsCostOfWaterDistribution) {
		super(templateName, name, origin, destination, lifecycleModel);
		
		if(reservoirIntensityOfWaterProduction < 0) {
			throw new IllegalArgumentException(
					"Reservoir intensity cannot be negative.");
		}
		this.reservoirIntensityOfWaterProduction = reservoirIntensityOfWaterProduction;
		
		if(maxWaterProduction < 0) {
			throw new IllegalArgumentException(
					"Maximum water production cannot be negative.");
		}
		this.maxWaterProduction = maxWaterProduction;
		
		if(initialWaterProduction > maxWaterProduction) {
			throw new IllegalArgumentException(
					"Initial water production cannot exceed maximum.");
		} else if(initialWaterProduction < 0) {
			throw new IllegalArgumentException(
					"Initial water production cannot be negative.");
		}
		this.initialWaterProduction = initialWaterProduction;
				
		if(electricalIntensityOfWaterProduction < 0) {
			throw new IllegalArgumentException(
					"Electrical intensity cannot be negative.");
		}
		this.electricalIntensityOfWaterProduction = electricalIntensityOfWaterProduction;
		
		if(variableOperationsCostOfWaterProduction < 0) {
			throw new IllegalArgumentException(
					"Variable cost of production cannot be negative.");
		}
		this.variableOperationsCostOfWaterProduction = variableOperationsCostOfWaterProduction;
		
		this.coastalAccessRequired = coastalAccessRequired;
		
		if(distributionEfficiency < 0 || distributionEfficiency > 1) {
			throw new IllegalArgumentException(
					"Distribution efficiency must be between 0 and 1.");
		}
		this.distributionEfficiency = distributionEfficiency;
		
		if(maxWaterInput < 0) {
			throw new IllegalArgumentException(
					"Maximum food input cannot be negative.");
		}
		this.maxWaterInput = maxWaterInput;
		
		if(initialWaterInput > maxWaterInput) {
			throw new IllegalArgumentException(
					"Initial water input cannot exceed maximum.");
		} else if(initialWaterInput < 0) {
			throw new IllegalArgumentException(
					"Initial water input cannot be negative.");
		}
		this.initialWaterInput = initialWaterInput;

		if(electricalIntensityOfWaterDistribution < 0) {
			throw new IllegalArgumentException(
					"Electrical intensity cannot be negative.");
		}
		this.electricalIntensityOfWaterDistribution = electricalIntensityOfWaterDistribution;
		
		if(variableOperationsCostOfWaterDistribution < 0) {
			throw new IllegalArgumentException(
					"Variable cost of distribution cannot be negative.");
		}
		this.variableOperationsCostOfWaterDistribution = variableOperationsCostOfWaterDistribution;
	}

	@Override
	public double getAquiferIntensityOfWaterProduction() {
		if(isOperational()) {
			return reservoirIntensityOfWaterProduction;
		} else {
			return 0;
		}
	}
	
	@Override
	public double getAquiferWithdrawals() {
		if(isOperational()) {
			return getWaterProduction() * reservoirIntensityOfWaterProduction;
		} else {
			return 0;
		}
	}

	@Override
	public double getDistributionEfficiency() {
		if(isOperational()) {
			return distributionEfficiency;
		} else {
			return 0;
		}
	}

	@Override
	public double getElectricalIntensityOfWaterDistribution() {
		return electricalIntensityOfWaterDistribution;
	}

	@Override
	public double getElectricalIntensityOfWaterProduction() {
		return electricalIntensityOfWaterProduction;
	}

	@Override
	public double getElectricityConsumption() {
		if(isOperational()) {
			return waterProduction * electricalIntensityOfWaterProduction 
					+ waterInput * electricalIntensityOfWaterDistribution;
		} else {
			return 0;
		}
	}
	
	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	@Override
	public double getMaxWaterInput() {
		if(isOperational()) {
			return maxWaterInput;
		} else {
			return 0;
		}
	}

	@Override
	public double getMaxWaterProduction() {
		if(isOperational()) {
			return maxWaterProduction;
		} else {
			return 0;
		}
	}

	@Override
	public EditableWaterElement getMutableElement() {
		EditableWaterElement element = new EditableWaterElement();
		setMutableFields(element);
		element.setReservoirIntensityOfWaterProduction(reservoirIntensityOfWaterProduction);
		element.setMaxWaterProduction(WaterUnits.convertFlow(
				maxWaterProduction, this, element));
		element.setInitialWaterProduction(WaterUnits.convertFlow(
				initialWaterProduction, this, element));
		element.setElectricalIntensityOfWaterProduction(DefaultUnits.convert(
				electricalIntensityOfWaterProduction, 
				getElectricityUnits(), getWaterUnits(), 
				element.getElectricityUnits(), element.getWaterUnits()));
		element.setVariableOperationsCostOfWaterProduction(DefaultUnits.convert(
				variableOperationsCostOfWaterProduction, 
				getCurrencyUnits(), getWaterUnits(), 
				element.getCurrencyUnits(), element.getWaterUnits()));
		element.setCoastalAccessRequired(coastalAccessRequired);
		element.setDistributionEfficiency(distributionEfficiency);
		element.setMaxWaterInput(WaterUnits.convertFlow(
				maxWaterInput, this, element));
		element.setInitialWaterInput(WaterUnits.convertFlow(
				initialWaterInput, this, element));
		element.setElectricalIntensityOfWaterDistribution(DefaultUnits.convert(
				electricalIntensityOfWaterDistribution, 
				getElectricityUnits(), getWaterUnits(), 
				element.getElectricityUnits(), element.getWaterUnits()));
		element.setVariableOperationsCostOfWaterDistribution(DefaultUnits.convert(
				variableOperationsCostOfWaterDistribution, 
				getCurrencyUnits(), getWaterUnits(), 
				element.getCurrencyUnits(), element.getWaterUnits()));
		return element;
	}

	@Override
	public double getTotalOperationsExpense() {
		return getFixedOperationsExpense() 
				+ variableOperationsCostOfWaterProduction * waterProduction 
				+ variableOperationsCostOfWaterDistribution * waterInput;
	}

	@Override
	public double getVariableOperationsCostOfWaterDistribution() {
		return variableOperationsCostOfWaterDistribution;
	}

	@Override
	public double getVariableOperationsCostOfWaterProduction() {
		return variableOperationsCostOfWaterProduction;
	}

	@Override
	public double getWaterInput() {
		if(isOperational()) {
			return waterInput;
		} else {
			return 0;
		}
	}

	@Override
	public double getWaterOutput() {
		if(isOperational()) {
			return waterInput * distributionEfficiency;
		} else {
			return 0;
		}
	}

	@Override
	public double getWaterProduction() {
		if(isOperational()) {
			return waterProduction;
		} else {
			return 0;
		}
	}

	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}
	
	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}

	@Override 
	public void initialize(long time) {
		super.initialize(time);
		setWaterProduction(initialWaterProduction);
		setWaterInput(initialWaterInput);
	}

	@Override
	public boolean isCoastalAccessRequired() {
		return coastalAccessRequired;
	}

	@Override
	public void setWaterInput(double waterInput) {
		if(waterInput < 0) {
			throw new IllegalArgumentException(
					"Water input cannot be negative.");
		} else if(waterInput > maxWaterInput) {
			throw new IllegalArgumentException(
					"Water input cannot exceed maximum.");
		}
		this.waterInput = waterInput;
		fireElementChangeEvent();
	}

	@Override
	public void setWaterProduction(double waterProduction) {
		if(waterProduction > maxWaterProduction) {
			throw new IllegalArgumentException(
					"Water production cannot exceed maximum.");
		} else if(waterProduction < 0) {
			throw new IllegalArgumentException(
					"Water production cannot be negative.");
		}
		this.waterProduction = waterProduction;
		fireElementChangeEvent();
	}
}
