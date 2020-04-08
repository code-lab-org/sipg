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
package edu.mit.sipg.gui.electricity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

import edu.mit.sipg.core.Country;
import edu.mit.sipg.core.Society;
import edu.mit.sipg.core.electricity.ElectricitySystem;
import edu.mit.sipg.core.electricity.LocalElectricitySoS;
import edu.mit.sipg.gui.PlottingUtils;
import edu.mit.sipg.gui.base.SpatialStatePanel;
import edu.mit.sipg.gui.event.UpdateEvent;
import edu.mit.sipg.io.Icons;
import edu.mit.sipg.units.CurrencyUnits;
import edu.mit.sipg.units.CurrencyUnitsOutput;
import edu.mit.sipg.units.ElectricityUnits;
import edu.mit.sipg.units.ElectricityUnitsOutput;
import edu.mit.sipg.units.OilUnits;
import edu.mit.sipg.units.OilUnitsOutput;
import edu.mit.sipg.units.TimeUnits;
import edu.mit.sipg.units.WaterUnits;
import edu.mit.sipg.units.WaterUnitsOutput;

/**
 * An electricity system panel suitable for locally-controlled systems.
 * 
 * @author Paul T. Grogan
 */
public class LocalElectricitySystemPanel extends ElectricitySystemPanel 
		implements CurrencyUnitsOutput, ElectricityUnitsOutput, OilUnitsOutput, WaterUnitsOutput {
	private static final long serialVersionUID = 2218175276232419659L;
	private static final CurrencyUnits currencyUnits = CurrencyUnits.Bsim;
	private static final TimeUnits currencyTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.TWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.Mtoe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	
	private final ElectricitySystem.Local electricitySystem;
	private final List<LocalElectricitySystemPanel> nestedPanels = 
			new ArrayList<LocalElectricitySystemPanel>();
	private final SpatialStatePanel electricityStatePanel;
	
	DefaultTableXYDataset electricitySourceAggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset electricitySourceDisaggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityUseAggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityUseDisaggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumUseAggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumUseDisaggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterUseAggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterUseDisaggregatedData = new DefaultTableXYDataset();
	
	DefaultTableXYDataset cashFlow = new DefaultTableXYDataset();
	DefaultTableXYDataset netCashFlow = new DefaultTableXYDataset();
	DefaultTableXYDataset cumulativeBalance = new DefaultTableXYDataset();
	
	/**
	 * Instantiates a new local electricity system panel.
	 *
	 * @param electricitySystem the electricity system
	 */
	public LocalElectricitySystemPanel(ElectricitySystem.Local electricitySystem) {
		super(electricitySystem);
		this.electricitySystem = electricitySystem;

		List<String> revenueNames;
		if(!(getSociety() instanceof Country)) {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", "Distribution Expense", 
					"Distribution Revenue", "Domestic Revenue");
		} else {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", "Domestic Revenue");
		}
		for(String name : revenueNames) {
			cashFlow.addSeries(new XYSeries(name, true, false));
		}

		final JTabbedPane nationalPane;
		if(electricitySystem instanceof LocalElectricitySoS) {
			nationalPane = new JTabbedPane();
			addTab(getSociety().getName(), Icons.COUNTRY, nationalPane);
		} else {
			nationalPane = this;
		}
		
		if(electricitySystem instanceof LocalElectricitySoS) {
			nationalPane.addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
					electricitySystem.getName() + " Net Revenue",
					"Annual Net Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")", cashFlow, 
							PlottingUtils.getCashFlowColors(revenueNames), netCashFlow,
							"Cumulative Net Revenue (" + getCurrencyUnits() + ")",
							cumulativeBalance));
		} else {
			nationalPane.addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
					electricitySystem.getName() + " Net Revenue",
					"Annual Net Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")", cashFlow, 
							PlottingUtils.getCashFlowColors(revenueNames), netCashFlow));
		}
		
		List<String> electricitySourceAggregatedNames = new ArrayList<String>();
		List<String> electricitySourceDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				electricitySourceDisaggregatedNames.add(society.getName());
			}
		}
		electricitySourceAggregatedNames.add(getSociety().getName() + " Production");
		electricitySourceAggregatedNames.add("Private Operations");
		if(!(electricitySystem instanceof LocalElectricitySoS)) {
			electricitySourceAggregatedNames.add("In-Distribution");
		}
		for(String name : electricitySourceAggregatedNames) {
			electricitySourceAggregatedData.addSeries(new XYSeries(name, true, false));
		}
		for(String name : electricitySourceDisaggregatedNames) {
			electricitySourceDisaggregatedData.addSeries(new XYSeries(name, true, false));
		}
		if(electricitySystem instanceof LocalElectricitySoS) {
			nationalPane.addTab("Source", Icons.ELECTRICITY_SOURCE, createToggleableStackedAreaChart(
					electricitySystem.getName() + " Source",
					"Electricity Source (" + electricityUnits + "/" + electricityTimeUnits + ")", 
					electricitySourceAggregatedData, PlottingUtils.getResourceColors(electricitySourceAggregatedNames), 
					electricitySourceDisaggregatedData, PlottingUtils.getResourceColors(electricitySourceDisaggregatedNames)));
		} else {
			nationalPane.addTab("Source", Icons.ELECTRICITY_SOURCE, createStackedAreaChart(
					electricitySystem.getName() + " Source",
					"Electricity Source (" + electricityUnits + "/" + electricityTimeUnits + ")", 
					electricitySourceAggregatedData, PlottingUtils.getResourceColors(electricitySourceAggregatedNames)));
		}
		
		List<String> electricityUseAggregatedNames = new ArrayList<String>();
		List<String> electricityUseDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				electricityUseDisaggregatedNames.add(society.getName());
			}
		}
		electricityUseAggregatedNames.add(getSociety().getName()  + " Society");
		electricityUseAggregatedNames.add("Water Operations");
		electricityUseAggregatedNames.add("Petroleum Operations");
		if(!(electricitySystem instanceof LocalElectricitySoS)) {
			electricityUseAggregatedNames.add("Out-Distribution");
		}
		electricityUseAggregatedNames.add("Wasted");
		for(String name : electricityUseAggregatedNames) {
			electricityUseAggregatedData.addSeries(new XYSeries(name, true, false));
		}
		for(String name : electricityUseDisaggregatedNames) {
			electricityUseDisaggregatedData.addSeries(new XYSeries(name, true, false));
		}
		if(electricitySystem instanceof LocalElectricitySoS) {
			nationalPane.addTab("Use", Icons.ELECTRICITY_USE, createToggleableStackedAreaChart(
					electricitySystem.getName() + " Use",
					"Electricity Use (" + electricityUnits + "/" + electricityTimeUnits + ")", 
					electricityUseAggregatedData, PlottingUtils.getResourceColors(electricityUseAggregatedNames), 
					electricityUseDisaggregatedData, PlottingUtils.getResourceColors(electricityUseDisaggregatedNames)));
		} else {
			nationalPane.addTab("Use", Icons.ELECTRICITY_USE, createStackedAreaChart(
					electricitySystem.getName() + " Use",
					"Electricity Use (" + electricityUnits + "/" + electricityTimeUnits + ")", 
					electricityUseAggregatedData, PlottingUtils.getResourceColors(electricityUseAggregatedNames)));
		}
		List<String> oilUseAggregatedNames = new ArrayList<String>();
		List<String> oilUseDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				oilUseDisaggregatedNames.add(society.getName() + " Operations");
			}
		}
		oilUseAggregatedNames.add(getSociety().getName() + " Operations");
		oilUseAggregatedNames.add("Private Operations");
		
		electricityStatePanel = new SpatialStatePanel(
				getSociety(), new ElectricityStateProvider());
		nationalPane.addTab("Network", Icons.NETWORK, electricityStatePanel);
		
		ChangeListener tabSynchronizer = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(e.getSource() instanceof JTabbedPane) {
					JTabbedPane pane = ((JTabbedPane)e.getSource());
					if(e.getSource() == nationalPane) {
						for(LocalElectricitySystemPanel panel : nestedPanels) {
							panel.setSelectedIndex(pane.getSelectedIndex());
						}
					} else {
						nationalPane.setSelectedIndex(pane.getSelectedIndex());
					}
				}
			}
		};
		nationalPane.addChangeListener(tabSynchronizer);
		
		if(electricitySystem instanceof LocalElectricitySoS) {
			JTabbedPane regionalData = this; // new JTabbedPane();
			for(ElectricitySystem.Local nestedSystem : 
				((LocalElectricitySoS) electricitySystem).getNestedSystems()) {
				LocalElectricitySystemPanel nestedPanel = 
						new LocalElectricitySystemPanel(nestedSystem);
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
	 * Gets the nested electricity systems.
	 *
	 * @return the nested electricity systems
	 */
	private List<ElectricitySystem.Local> getNestedElectricitySystems() {
		List<ElectricitySystem.Local> systems = new ArrayList<ElectricitySystem.Local>();
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			if(nestedSociety.getElectricitySystem() instanceof ElectricitySystem.Local) {
				systems.add((ElectricitySystem.Local)nestedSociety.getElectricitySystem());
			}
		}
		return systems;
	}
	
	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}

	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
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
		cashFlow.removeAllSeries();
		netCashFlow.removeAllSeries();
		electricityUseAggregatedData.removeAllSeries();
		electricityUseDisaggregatedData.removeAllSeries();
		electricitySourceAggregatedData.removeAllSeries();
		electricitySourceDisaggregatedData.removeAllSeries();
		petroleumUseAggregatedData.removeAllSeries();
		petroleumUseDisaggregatedData.removeAllSeries();
		waterUseAggregatedData.removeAllSeries();
		waterUseDisaggregatedData.removeAllSeries();
		cashFlow.removeAllSeries();
		netCashFlow.removeAllSeries();
		cumulativeBalance.removeAllSeries();
	}

	@Override
	public void simulationCompleted(UpdateEvent event) {
		for(LocalElectricitySystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationCompleted(event);
		}
	}

	@Override
	public void simulationInitialized(UpdateEvent event) {
		initialize();
		for(LocalElectricitySystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationInitialized(event);
		}
		electricityStatePanel.repaint();
	}

	@Override
	public void simulationUpdated(UpdateEvent event) {
		update((int)event.getTime());
		for(LocalElectricitySystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationUpdated(event);
		}
		electricityStatePanel.repaint();
	}

	/**
	 * Update.
	 *
	 * @param year the year
	 */
	private void update(int year) {
		updateSeries(electricitySourceAggregatedData, "Production", year, 
				ElectricityUnits.convertFlow(
						electricitySystem.getElectricityProduction(), 
						electricitySystem, this));
		updateSeries(electricitySourceAggregatedData, "Private Operations", year, 
				ElectricityUnits.convertFlow(
						electricitySystem.getElectricityFromPrivateProduction(),
						electricitySystem, this));
		updateSeries(electricityUseAggregatedData, "Society", year, 
				ElectricityUnits.convertFlow(getSociety().getSocialSystem().getElectricityConsumption(),
						getSociety().getSocialSystem(), this));
		updateSeries(electricityUseAggregatedData, "Water Operations", year,  
				ElectricityUnits.convertFlow(
						getSociety().getWaterSystem().getElectricityConsumption(),
						getSociety().getWaterSystem(), this));
		updateSeries(electricityUseAggregatedData, "Petroleum Operations", year,  
				ElectricityUnits.convertFlow(
						getSociety().getPetroleumSystem().getElectricityConsumption(),
						getSociety().getPetroleumSystem(), this));
		if(electricitySystem instanceof LocalElectricitySoS) {
			for(ElectricitySystem.Local nestedSystem : getNestedElectricitySystems()) {
				updateSeries(electricitySourceDisaggregatedData, nestedSystem.getSociety().getName(), year,
						ElectricityUnits.convertFlow(nestedSystem.getTotalElectricitySupply()
								+ nestedSystem.getElectricityFromPrivateProduction()
								+ nestedSystem.getElectricityOutDistribution()
								- nestedSystem.getElectricityInDistribution()
								- nestedSystem.getElectricityWasted(), 
								nestedSystem, this));
				updateSeries(electricityUseDisaggregatedData, nestedSystem.getSociety().getName(), year,
						ElectricityUnits.convertFlow(
								nestedSystem.getSociety().getTotalElectricityDemand(), 
								nestedSystem.getSociety(), this));
				updateSeries(petroleumUseDisaggregatedData, nestedSystem.getSociety().getName(), year, 
						OilUnits.convertFlow(electricitySystem.getPetroleumConsumption(),
								electricitySystem, this));
			}
		} else {
			updateSeries(electricitySourceAggregatedData,  "Distribution", year, 
					ElectricityUnits.convertFlow(
							electricitySystem.getElectricityInDistribution(), 
							electricitySystem, this));
			updateSeries(electricityUseAggregatedData, "Distribution", year, 
					ElectricityUnits.convertFlow(
							electricitySystem.getElectricityOutDistribution(), 
							electricitySystem, this));
		}
		updateSeries(electricityUseAggregatedData, "Wasted", year,  
				ElectricityUnits.convertFlow(
						electricitySystem.getElectricityWasted(),
						electricitySystem, this));
		updateSeries(electricityUseDisaggregatedData, "Wasted", year,  
				ElectricityUnits.convertFlow(
						electricitySystem.getElectricityWasted(),
						electricitySystem, this));
		updateSeries(petroleumUseAggregatedData, "Operations", year, 
				OilUnits.convertFlow(electricitySystem.getPetroleumConsumptionFromPublicProduction(),
						electricitySystem, this));
		updateSeries(petroleumUseAggregatedData, "Private Operations", year, 
				OilUnits.convertFlow(electricitySystem.getPetroleumConsumptionFromPrivateProduction(),
						electricitySystem, this));
		
		updateSeries(cashFlow, "Capital Expense", year, 
				CurrencyUnits.convertFlow(
						-electricitySystem.getCapitalExpense(), 
						electricitySystem, this));
		updateSeries(cashFlow, "Operations Expense", year, 
				CurrencyUnits.convertFlow(
						-electricitySystem.getOperationsExpense()
						-electricitySystem.getConsumptionExpense(), 
						electricitySystem, this));
		updateSeries(cashFlow, "Decommission Expense", year, 
				CurrencyUnits.convertFlow(
						-electricitySystem.getDecommissionExpense(), 
						electricitySystem, this));
		if(!(electricitySystem.getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Expense", year, 
					CurrencyUnits.convertFlow(
							-electricitySystem.getDistributionExpense(), 
							electricitySystem, this));
			updateSeries(cashFlow, "Distribution Revenue", year, 
					CurrencyUnits.convertFlow(
							electricitySystem.getDistributionRevenue(), 
							electricitySystem, this));
		}
		updateSeries(cashFlow, "Domestic Revenue", year, 
				CurrencyUnits.convertFlow(
						electricitySystem.getSalesRevenue(), 
						electricitySystem, this));
		updateSeries(netCashFlow, "Net Revenue", year, 
				CurrencyUnits.convertFlow(
						electricitySystem.getCashFlow(), 
						electricitySystem, this));
		updateSeries(cumulativeBalance, "Cumulative Net Revenue", year, 
				CurrencyUnits.convertFlow(electricitySystem.getCumulativeCashFlow(),
						electricitySystem, this));
	}
}
