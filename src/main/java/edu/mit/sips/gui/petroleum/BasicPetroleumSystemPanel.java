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
package edu.mit.sips.gui.petroleum;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.gui.event.UpdateEvent;
import edu.mit.sips.io.Icons;

/**
 * A basic petroleum system panel suitable for remotely-controlled systems.
 * 
 * @author Paul T. Grogan
 */
public class BasicPetroleumSystemPanel extends PetroleumSystemPanel {
	private static final long serialVersionUID = 569560127649283731L;

	DefaultTableXYDataset petroleumRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityConsumption = new DefaultTableXYDataset();

	/**
	 * Instantiates a new basic petroleum system panel.
	 *
	 * @param petroleumSystem the petroleum system
	 */
	public BasicPetroleumSystemPanel(PetroleumSystem petroleumSystem) {
		super(petroleumSystem);
		
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				getPetroleumSystem().getName() + " Cash Flow",
				"Petroleum Revenue (SAR/year)", petroleumRevenue));
		addTab("Electricity Consumption", Icons.ELECTRICITY_USE, createStackedAreaChart(
				getPetroleumSystem().getName() + " Electricity Use",
				"Electricity Consumed (toe/year)", electricityConsumption));
	}
	
	/**
	 * Gets the petroleum system.
	 *
	 * @return the petroleum system
	 */
	public PetroleumSystem getPetroleumSystem() {
		return (PetroleumSystem) getInfrastructureSystem();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {
		petroleumRevenue.removeAllSeries();
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
		updateSeries(petroleumRevenue, "Revenue", year, 
				getPetroleumSystem().getCashFlow());
		updateSeries(electricityConsumption, "Consumption", year, 
				getPetroleumSystem().getElectricityConsumption());
	}
}
