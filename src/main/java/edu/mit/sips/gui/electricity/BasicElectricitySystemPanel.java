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
package edu.mit.sips.gui.electricity;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;

/**
 * A basic electricity system panel suitable for remotely-controlled systems.
 * 
 * @author Paul T. Grogan
 */
public class BasicElectricitySystemPanel extends ElectricitySystemPanel {
	private static final long serialVersionUID = 569560127649283731L;

	DefaultTableXYDataset electricityRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset waterConsumption = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumConsumption = new DefaultTableXYDataset();

	/**
	 * Instantiates a new basic electricity system panel.
	 *
	 * @param electricitySystem the electricity system
	 */
	public BasicElectricitySystemPanel(ElectricitySystem electricitySystem) {
		super(electricitySystem);
		
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				getElectricitySystem().getName() + " Cash Flow",
				"Electricity Revenue (SAR/year)", electricityRevenue));
		addTab("Water Consumption", Icons.WATER_USE, createStackedAreaChart(
				getElectricitySystem().getName() + " Water Use",
				"Water Consumed (m^3/year)", waterConsumption));
		addTab("Petroleum Consumption", Icons.PETROLEUM_USE, createStackedAreaChart(
				getElectricitySystem().getName() + " Oil Use",
				"Petroleum Consumed (toe/year)", petroleumConsumption));
	}
	
	/**
	 * Gets the electricity system.
	 *
	 * @return the electricity system
	 */
	public ElectricitySystem getElectricitySystem() {
		return (ElectricitySystem) getInfrastructureSystem();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {
		electricityRevenue.removeAllSeries();
		waterConsumption.removeAllSeries();
		petroleumConsumption.removeAllSeries();
	}

	@Override
	public void simulationCompleted(UpdateEvent event) { }
	
	@Override
	public void simulationInitialized(UpdateEvent event) {
		initialize();
	}

	@Override
	public void simulationUpdated(UpdateEvent event) {
		update((int)event.getTime());
	}

	/**
	 * Update.
	 *
	 * @param year the year
	 */
	private void update(int year) {
		updateSeries(electricityRevenue, "Revenue", year, 
				getElectricitySystem().getCashFlow());
		updateSeries(waterConsumption, "Consumption", year, 
				getElectricitySystem().getWaterConsumption());
		updateSeries(petroleumConsumption, "Consumption", year, 
				getElectricitySystem().getPetroleumConsumption());
	}
}
