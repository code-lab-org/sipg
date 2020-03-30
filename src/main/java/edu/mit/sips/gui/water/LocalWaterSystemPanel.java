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
package edu.mit.sips.gui.water;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.water.LocalWaterSoS;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.gui.PlottingUtils;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.gui.base.SpatialStatePanel;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * A water system panel suitable for locally-controlled systems.
 * 
 * @author Paul T. Grogan
 */
public class LocalWaterSystemPanel extends WaterSystemPanel
implements CurrencyUnitsOutput, WaterUnitsOutput, ElectricityUnitsOutput {	
	private static final long serialVersionUID = -3665986046863585665L;
	private static final CurrencyUnits currencyUnits = CurrencyUnits.Bsim;
	private static final TimeUnits currencyTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.TWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.km3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;

	private final WaterSystem.Local waterSystem;
	private final List<LocalWaterSystemPanel> nestedPanels = 
			new ArrayList<LocalWaterSystemPanel>();
	private final SpatialStatePanel waterStatePanel;

	DefaultTableXYDataset waterSourceAggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterSourceDisaggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterUseAggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterUseDisaggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityUseAggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityUseDisaggregatedData = new DefaultTableXYDataset();
	
	DefaultTableXYDataset waterAquiferDataset = new DefaultTableXYDataset();
	
	DefaultTableXYDataset cashFlow = new DefaultTableXYDataset();
	DefaultTableXYDataset netCashFlow = new DefaultTableXYDataset();
	DefaultTableXYDataset cumulativeBalance = new DefaultTableXYDataset();
	
	/**
	 * Instantiates a new local water system panel.
	 *
	 * @param waterSystem the water system
	 */
	public LocalWaterSystemPanel(WaterSystem.Local waterSystem) {
		super(waterSystem);
		this.waterSystem = waterSystem;
				
		List<String> revenueNames;
		if(!(getSociety() instanceof Country)) {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", "Distribution Expense", 
					"Import Expense", "Distribution Revenue", 
					"Domestic Revenue");
		} else {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", "Import Expense", "Domestic Revenue");
		}
		for(String name : revenueNames) {
			cashFlow.addSeries(new XYSeries(name, true, false));
		}
		
		final JTabbedPane nationalPane;
		if(waterSystem instanceof LocalWaterSoS) {
			nationalPane = new JTabbedPane();
			addTab(getSociety().getName(), Icons.COUNTRY, nationalPane);
		} else {
			nationalPane = this;
		}
		
		if(waterSystem instanceof LocalWaterSoS) {
			nationalPane.addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
					waterSystem.getName() + " Net Revenue",
					"Annual Net Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")", cashFlow, 
							PlottingUtils.getCashFlowColors(revenueNames), netCashFlow,
							"Cumulative Net Revenue (" + getCurrencyUnits() + ")",
							cumulativeBalance));
		} else {
			nationalPane.addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
					waterSystem.getName() + " Net Revenue",
					"Annual Net Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")", cashFlow, 
							PlottingUtils.getCashFlowColors(revenueNames), netCashFlow));
		}
		
		List<String> waterSourceAggregatedNames = new ArrayList<String>();
		List<String> waterSourceDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				waterSourceDisaggregatedNames.add(society.getName());
			}
		}
		waterSourceAggregatedNames.add(getSociety().getName() + " Production");
		waterSourceAggregatedNames.add("Private Operations");
		if(!(getSociety() instanceof Country)) {
			waterSourceAggregatedNames.add("In-Distribution");
		}
		waterSourceAggregatedNames.add("Import");
		waterSourceDisaggregatedNames.add("Import");
		for(String name : waterSourceAggregatedNames) {
			waterSourceAggregatedData.addSeries(new XYSeries(name, true, false));
		}
		for(String name : waterSourceDisaggregatedNames) {
			waterSourceDisaggregatedData.addSeries(new XYSeries(name, true, false));
		}
		if(waterSystem instanceof LocalWaterSoS) {
			nationalPane.addTab("Source", Icons.WATER_SOURCE, createToggleableStackedAreaChart(
					waterSystem.getName() + " Source",
					"Water Source (" + waterUnits + "/" + waterTimeUnits + ")", 
					waterSourceAggregatedData, PlottingUtils.getResourceColors(waterSourceAggregatedNames),
					waterSourceDisaggregatedData, PlottingUtils.getResourceColors(waterSourceDisaggregatedNames)));
		} else {
			nationalPane.addTab("Source", Icons.WATER_SOURCE, createStackedAreaChart(
					waterSystem.getName() + " Source",
					"Water Source (" + waterUnits + "/" + waterTimeUnits + ")", 
					waterSourceAggregatedData, PlottingUtils.getResourceColors(waterSourceAggregatedNames)));
		}
		
		List<String> waterUseAggregatedNames = new ArrayList<String>();
		List<String> waterUseDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				waterUseDisaggregatedNames.add(society.getName());
			}
		}
		waterUseAggregatedNames.add(getSociety().getName() + " Society");
		waterUseAggregatedNames.add("Agriculture Operations");
		if(!(waterSystem instanceof LocalWaterSoS)) {
			waterUseAggregatedNames.add("Out-Distribution");
		}
		for(String name : waterUseAggregatedNames) {
			waterUseAggregatedData.addSeries(new XYSeries(name, true, false));
		}
		for(String name : waterUseDisaggregatedNames) {
			waterUseDisaggregatedData.addSeries(new XYSeries(name, true, false));
		}
		if(waterSystem instanceof LocalWaterSoS) {
			nationalPane.addTab("Use", Icons.WATER_USE, createToggleableStackedAreaChart(
					waterSystem.getName() + " Use",
					"Water Use (" + waterUnits + "/" + waterTimeUnits + ")", 
					waterUseAggregatedData, PlottingUtils.getResourceColors(waterUseAggregatedNames), 
					waterUseDisaggregatedData, PlottingUtils.getResourceColors(waterUseDisaggregatedNames)));
		} else {
			nationalPane.addTab("Use", Icons.WATER_USE, createStackedAreaChart(
					waterSystem.getName() + " Use",
					"Water Use (" + waterUnits + "/" + waterTimeUnits + ")", 
					waterUseAggregatedData, PlottingUtils.getResourceColors(waterUseAggregatedNames)));
		}
		
		List<String> electricityUseAggregatedNames = new ArrayList<String>();
		List<String> electricityUseDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				electricityUseDisaggregatedNames.add(society.getName() + " Operations");
			}
		}
		electricityUseAggregatedNames.add(getSociety().getName() + " Operations");
		electricityUseAggregatedNames.add("Private Operations");
		if(waterSystem instanceof LocalWaterSoS) {
			nationalPane.addTab("Use", Icons.ELECTRICITY_USE, createToggleableStackedAreaChart(
					waterSystem.getName() + " Electricity Use",
					"Electricity Use (" + electricityUnits + "/" + electricityTimeUnits + ")",
					electricityUseAggregatedData, PlottingUtils.getResourceColors(electricityUseAggregatedNames),
					electricityUseDisaggregatedData, PlottingUtils.getResourceColors(electricityUseDisaggregatedNames)));
		} else {
			nationalPane.addTab("Use", Icons.ELECTRICITY_USE, createStackedAreaChart(
					waterSystem.getName() + " Electricity Use",
					"Electricity Use (" + electricityUnits + "/" + electricityTimeUnits + ")",
					electricityUseAggregatedData, PlottingUtils.getResourceColors(electricityUseAggregatedNames)));
		}

		List<Color> societyColors = new ArrayList<Color>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				societyColors.add(PlottingUtils.getSocietyColor(society));
			}
		} else {
			societyColors.add(PlottingUtils.getSocietyColor(getSociety()));
		}
		nationalPane.addTab("Aquifer", Icons.WATER_RESERVOIR, createStackedAreaChart(
				waterSystem.getName() + " Aquifer",
				"Water Aquifer Volume (" + waterUnits + ")", 
				waterAquiferDataset, societyColors.toArray(new Color[0])));
		
		waterStatePanel = new SpatialStatePanel(
				waterSystem.getSociety(), new WaterStateProvider());
		nationalPane.addTab("Network", Icons.NETWORK, waterStatePanel);
		
		ChangeListener tabSynchronizer = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(e.getSource() instanceof JTabbedPane) {
					JTabbedPane pane = ((JTabbedPane)e.getSource());
					if(e.getSource() == nationalPane) {
						for(LocalWaterSystemPanel panel : nestedPanels) {
							panel.setSelectedIndex(pane.getSelectedIndex());
						}
					} else {
						nationalPane.setSelectedIndex(pane.getSelectedIndex());
					}
				}
			}
		};
		nationalPane.addChangeListener(tabSynchronizer);
		
		if(waterSystem instanceof LocalWaterSoS) {
			JTabbedPane regionalData = this; // new JTabbedPane();
			for(WaterSystem.Local nestedSystem : 
				((LocalWaterSoS) waterSystem).getNestedSystems()) {
				LocalWaterSystemPanel nestedPanel = 
						new LocalWaterSystemPanel(nestedSystem);
				nestedPanel.addChangeListener(tabSynchronizer);
				nestedPanels.add(nestedPanel);
				regionalData.addTab(nestedSystem.getSociety().getName(), 
						Icons.CITY, nestedPanel);
			}
		}
	}
	
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}

	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
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
	 * Gets the nested water systems.
	 *
	 * @return the nested water systems
	 */
	private List<WaterSystem.Local> getNestedWaterSystems() {
		List<WaterSystem.Local> systems = new ArrayList<WaterSystem.Local>();
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			if(nestedSociety.getWaterSystem() instanceof WaterSystem.Local) {
				systems.add((WaterSystem.Local)nestedSociety.getWaterSystem());
			}
		}
		return systems;
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
	 * Initialize.
	 */
	private void initialize() {
		waterAquiferDataset.removeAllSeries();
		waterUseAggregatedData.removeAllSeries();
		waterUseDisaggregatedData.removeAllSeries();
		waterSourceAggregatedData.removeAllSeries();
		waterSourceDisaggregatedData.removeAllSeries();
		electricityUseAggregatedData.removeAllSeries();
		electricityUseDisaggregatedData.removeAllSeries();
		cashFlow.removeAllSeries();
		netCashFlow.removeAllSeries();
		cumulativeBalance.removeAllSeries();
	}

	@Override
	public void simulationCompleted(UpdateEvent event) {
		for(LocalWaterSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationCompleted(event);
		}
	}

	@Override
	public void simulationInitialized(UpdateEvent event) {
		initialize();
		for(LocalWaterSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationInitialized(event);
		}
		waterStatePanel.repaint();
	}

	@Override
	public void simulationUpdated(UpdateEvent event) {
		update((int)event.getTime());
		for(LocalWaterSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationUpdated(event);
		}
		waterStatePanel.repaint();
	}

	/**
	 * Update.
	 *
	 * @param year the year
	 */
	private void update(int year) {
		updateSeries(cashFlow, "Capital Expense", year, 
				CurrencyUnits.convertFlow(
						-waterSystem.getCapitalExpense(), 
						waterSystem, this));
		updateSeries(cashFlow, "Operations Expense", year, 
				CurrencyUnits.convertFlow(
						-waterSystem.getOperationsExpense()
						-waterSystem.getConsumptionExpense(), 
						waterSystem, this));
		updateSeries(cashFlow, "Decommission Expense", year, 
				CurrencyUnits.convertFlow(
						-waterSystem.getDecommissionExpense(), 
						waterSystem, this));
		if(!(waterSystem.getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Expense", year, 
					CurrencyUnits.convertFlow(
							-waterSystem.getDistributionExpense(), 
							waterSystem, this));
		}
		updateSeries(cashFlow, "Import Expense", year, 
				CurrencyUnits.convertFlow(
						-waterSystem.getImportExpense(), 
						waterSystem, this));
		if(!(waterSystem.getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Revenue", year, 
					CurrencyUnits.convertFlow(
							-waterSystem.getDistributionExpense(), 
							waterSystem, this));
		}
		updateSeries(cashFlow, "Domestic Revenue", year, 
				CurrencyUnits.convertFlow(
						waterSystem.getSalesRevenue(), 
						waterSystem, this));
		updateSeries(netCashFlow, "Net Revenue", year, 
				CurrencyUnits.convertFlow(waterSystem.getCashFlow(),
						waterSystem, this));
		updateSeries(cumulativeBalance, "Cumulative Net Revenue", year, 
				CurrencyUnits.convertFlow(waterSystem.getCumulativeCashFlow(),
						waterSystem, this));
		
		updateSeries(waterSourceAggregatedData, "Production", year, 
				WaterUnits.convertFlow(waterSystem.getWaterProduction(), 
						waterSystem, this));
		updateSeries(waterSourceAggregatedData, "Private Operations", year, 
				WaterUnits.convertFlow(waterSystem.getWaterFromPrivateProduction(), 
						waterSystem, this));
		updateSeries(waterUseAggregatedData, "Society", year, 
				WaterUnits.convertFlow(getSociety().getSocialSystem().getWaterConsumption(),
						getSociety().getSocialSystem(), this));
		updateSeries(waterUseAggregatedData, "Agriculture Operations", year, 
				WaterUnits.convertFlow(getSociety().getAgricultureSystem().getWaterConsumption(), 
						getSociety().getAgricultureSystem(), this));
		if(waterSystem instanceof LocalWaterSoS) {
			for(WaterSystem.Local nestedSystem : getNestedWaterSystems()) {
				updateSeries(waterAquiferDataset, nestedSystem.getSociety().getName(), year, 
						WaterUnits.convertFlow(nestedSystem.getWaterReservoirVolume(), 
								waterSystem, this));
			}
		} else {
			updateSeries(waterSourceAggregatedData, "Distribution", year, 
					WaterUnits.convertFlow(waterSystem.getWaterInDistribution(), 
							waterSystem, this));
			updateSeries(waterUseAggregatedData, "Distribution", year,
					WaterUnits.convertFlow(waterSystem.getWaterOutDistribution(), 
							waterSystem, this));
			updateSeries(waterAquiferDataset, "Aquifer", year, 
					WaterUnits.convertStock(waterSystem.getWaterReservoirVolume(), 
							waterSystem, this));
		}
		updateSeries(electricityUseAggregatedData, "Operations", year, 
				ElectricityUnits.convertFlow(waterSystem.getElectricityConsumptionFromPublicProduction(),
						waterSystem, this));
		updateSeries(electricityUseAggregatedData, "Private Operations", year, 
				ElectricityUnits.convertFlow(waterSystem.getElectricityConsumptionFromPrivateProduction(),
						waterSystem, this));
		for(WaterSystem.Local nestedSystem : getNestedWaterSystems()) {
				updateSeries(waterSourceDisaggregatedData, nestedSystem.getSociety().getName(), year,
						WaterUnits.convertFlow(nestedSystem.getTotalWaterSupply()
								+ nestedSystem.getWaterOutDistribution()
								- nestedSystem.getWaterInDistribution()
								+ nestedSystem.getWaterFromPrivateProduction()
								- nestedSystem.getWaterImport(), nestedSystem, this));
				updateSeries(waterUseDisaggregatedData, nestedSystem.getSociety().getName(), year,
						WaterUnits.convertFlow(nestedSystem.getSociety().getTotalWaterDemand(), 
								nestedSystem.getSociety(), this));
				updateSeries(electricityUseDisaggregatedData, nestedSystem.getSociety().getName(), year, 
						ElectricityUnits.convertFlow(nestedSystem.getElectricityConsumption(),
								nestedSystem, this));
		}
		updateSeries(waterSourceAggregatedData, "Import", year, 
				WaterUnits.convertFlow(waterSystem.getWaterImport(), 
						waterSystem, this));
		updateSeries(waterSourceDisaggregatedData, "Import", year, 
				WaterUnits.convertFlow(waterSystem.getWaterImport(), 
						waterSystem, this));
	}
}
