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
package edu.mit.sipg.gui.water;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sipg.core.water.WaterSystem;
import edu.mit.sipg.gui.event.UpdateEvent;
import edu.mit.sipg.io.Icons;

/**
 * A basic water system panel suitable for remotely-controlled systems.
 * 
 * @author Paul T. Grogan
 */
public class BasicWaterSystemPanel extends WaterSystemPanel {
	private static final long serialVersionUID = 569560127649283731L;

	DefaultTableXYDataset waterRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityConsumption = new DefaultTableXYDataset();

	/**
	 * Instantiates a new basic water system panel.
	 *
	 * @param waterSystem the water system
	 */
	public BasicWaterSystemPanel(WaterSystem waterSystem) {
		super(waterSystem);
		
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				getWaterSystem().getName() + " Cash Flow",
				"Water Revenue (SAR/year)", waterRevenue));
		addTab("Electricity Consumption", Icons.ELECTRICITY_USE, createStackedAreaChart(
				getWaterSystem().getName() + " Electricity Use",
				"Electricity Consumed (m^3/year)", electricityConsumption));
	}
	
	/**
	 * Gets the water system.
	 *
	 * @return the water system
	 */
	public WaterSystem getWaterSystem() {
		return (WaterSystem) getInfrastructureSystem();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {
		waterRevenue.removeAllSeries();
		electricityConsumption.removeAllSeries();
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
		updateSeries(waterRevenue, "Revenue", year, 
				getWaterSystem().getCashFlow());
		updateSeries(electricityConsumption, "Consumption", year, 
				getWaterSystem().getElectricityConsumption());
	}
}
