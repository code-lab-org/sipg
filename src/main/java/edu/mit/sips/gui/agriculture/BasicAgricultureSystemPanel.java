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
package edu.mit.sips.gui.agriculture;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;

/**
 * A basic implementation of the agriculture system panel interface.
 * 
 * @author Paul T. Grogan
 */
public class BasicAgricultureSystemPanel extends AgricultureSystemPanel {
	private static final long serialVersionUID = 569560127649283731L;

	DefaultTableXYDataset agricultureRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset waterConsumption = new DefaultTableXYDataset();

	/**
	 * Instantiates a new basic agriculture system panel.
	 *
	 * @param agricultureSystem the agriculture system
	 */
	public BasicAgricultureSystemPanel(AgricultureSystem agricultureSystem) {
		super(agricultureSystem);

		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				getAgricultureSystem().getName() + " Cash Flow",
				"Agriculture Revenue (SAR/year)", agricultureRevenue));
		addTab("Water Consumption", Icons.WATER_USE, createStackedAreaChart(
				getAgricultureSystem().getName() + " Water Use",
				"Water Consumed (m^3/year)", waterConsumption));
	}

	/**
	 * Gets the agriculture system.
	 *
	 * @return the agriculture system
	 */
	public AgricultureSystem getAgricultureSystem() {
		return (AgricultureSystem) getInfrastructureSystem();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {
		agricultureRevenue.removeAllSeries();
		waterConsumption.removeAllSeries();
	}

	/**
	 * Update.
	 *
	 * @param year the year
	 */
	private void update(int year) {
		updateSeries(agricultureRevenue, "Revenue", year, 
				getAgricultureSystem().getCashFlow());
		updateSeries(waterConsumption, "Consumption", year, 
				getAgricultureSystem().getWaterConsumption());
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
}
