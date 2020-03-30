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
import edu.mit.sips.core.petroleum.LocalPetroleumSoS;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.gui.PlottingUtils;
import edu.mit.sips.gui.base.SpatialStatePanel;
import edu.mit.sips.gui.event.UpdateEvent;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * A petroleum system panel suitable for locally-controlled systems.
 * 
 * @author Paul T. Grogan
 */
public class LocalPetroleumSystemPanel extends PetroleumSystemPanel 
		implements OilUnitsOutput, ElectricityUnitsOutput, CurrencyUnitsOutput {
	private static final long serialVersionUID = 2218175276232419659L;
	private static final CurrencyUnits currencyUnits = CurrencyUnits.Bsim;
	private static final TimeUnits currencyTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.Mtoe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	
	private final PetroleumSystem.Local petroleumSystem;
	private final List<LocalPetroleumSystemPanel> nestedPanels = 
			new ArrayList<LocalPetroleumSystemPanel>();
	private final SpatialStatePanel petroleumStatePanel;
	
	DefaultTableXYDataset petroleumReservoirDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumSourceAggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumSourceDisaggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumUseAggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumUseDisaggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityUseAggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityUseDisaggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset cashFlow = new DefaultTableXYDataset();
	DefaultTableXYDataset netCashFlow = new DefaultTableXYDataset();
	DefaultTableXYDataset cumulativeBalance = new DefaultTableXYDataset();
	
	/**
	 * Instantiates a new local petroleum system panel.
	 *
	 * @param petroleumSystem the petroleum system
	 */
	public LocalPetroleumSystemPanel(PetroleumSystem.Local petroleumSystem) {
		super(petroleumSystem);
		this.petroleumSystem = petroleumSystem;
		
		List<String> revenueNames;
		if(!(getSociety() instanceof Country)) {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", "Distribution Expense", 
					"Import Expense", "Distribution Revenue", "Export Revenue", 
					"Domestic Revenue");
		} else {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", "Import Expense", 
					"Export Revenue", "Domestic Revenue");
		}
		for(String name : revenueNames) {
			cashFlow.addSeries(new XYSeries(name, true, false));
		}
		
		final JTabbedPane nationalPane;
		if(petroleumSystem instanceof LocalPetroleumSoS) {
			nationalPane = new JTabbedPane();
			addTab(getSociety().getName(), Icons.COUNTRY, nationalPane);
		} else {
			nationalPane = this;
		}
		
		if(petroleumSystem instanceof LocalPetroleumSoS) {
			nationalPane.addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
					petroleumSystem.getName() + " Net Revenue",
					"Annual Net Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")", cashFlow, 
							PlottingUtils.getCashFlowColors(revenueNames), netCashFlow,
							"Cumulative Net Revenue (" + getCurrencyUnits() + ")",
							cumulativeBalance));
		} else {
			nationalPane.addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
					petroleumSystem.getName() + " Net Revenue",
					"Annual Net Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")", cashFlow, 
							PlottingUtils.getCashFlowColors(revenueNames), netCashFlow));
		}
		
		List<String> oilSourceAggregatedNames = new ArrayList<String>();
		List<String> oilSourceDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				oilSourceDisaggregatedNames.add(society.getName());
			}
		}
		oilSourceAggregatedNames.add(getSociety().getName() + " Production");
		if(!(getSociety() instanceof Country)) {
			oilSourceAggregatedNames.add("In-Distribution");
		}
		oilSourceAggregatedNames.add("Import");
		oilSourceDisaggregatedNames.add("Import");
		for(String name : oilSourceAggregatedNames) {
			petroleumSourceAggregatedData.addSeries(new XYSeries(name, true, false));
		}
		for(String name : oilSourceDisaggregatedNames) {
			petroleumSourceDisaggregatedData.addSeries(new XYSeries(name, true, false));
		}
		if(petroleumSystem instanceof LocalPetroleumSoS) {
			nationalPane.addTab("Source", Icons.PETROLEUM_SOURCE, createToggleableStackedAreaChart(
					petroleumSystem.getName() + " Oil Source",
					"Petroleum Source (" + oilUnits + "/" + oilTimeUnits + ")",
					petroleumSourceAggregatedData, PlottingUtils.getResourceColors(oilSourceAggregatedNames),
					petroleumSourceDisaggregatedData, PlottingUtils.getResourceColors(oilSourceDisaggregatedNames)));
		} else {
			nationalPane.addTab("Source", Icons.PETROLEUM_SOURCE, createStackedAreaChart(
					petroleumSystem.getName() + " Oil Source",
					"Petroleum Source (" + oilUnits + "/" + oilTimeUnits + ")",
					petroleumSourceAggregatedData, PlottingUtils.getResourceColors(oilSourceAggregatedNames)));
		}
		List<String> oilUseAggregatedNames = new ArrayList<String>();
		List<String> oilUseDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				oilUseDisaggregatedNames.add(society.getName());
			}
		}
		oilUseAggregatedNames.add(getSociety().getName()  + " Society");
		oilUseAggregatedNames.add("Electricity Operations");
		if(!(getSociety() instanceof Country)) {
			oilUseAggregatedNames.add("Out-Distribution");
		}
		oilUseAggregatedNames.add("Export");
		oilUseDisaggregatedNames.add("Export");
		for(String name : oilUseAggregatedNames) {
			petroleumUseAggregatedData.addSeries(new XYSeries(name, true, false));
		}
		for(String name : oilUseDisaggregatedNames) {
			petroleumUseDisaggregatedData.addSeries(new XYSeries(name, true, false));
		}
		if(petroleumSystem instanceof LocalPetroleumSoS) {
			nationalPane.addTab("Use", Icons.PETROLEUM_USE, createToggleableStackedAreaChart(
					petroleumSystem.getName() + " Oil Use",
					"Petroleum Use (" + oilUnits + "/" + oilTimeUnits + ")", 
					petroleumUseAggregatedData, PlottingUtils.getResourceColors(oilUseAggregatedNames), 
					petroleumUseDisaggregatedData, PlottingUtils.getResourceColors(oilUseDisaggregatedNames)));
		} else {
			nationalPane.addTab("Use", Icons.PETROLEUM_USE, createStackedAreaChart(
					petroleumSystem.getName() + " Oil Use",
					"Petroleum Use (" + oilUnits + "/" + oilTimeUnits + ")", 
					petroleumUseAggregatedData, PlottingUtils.getResourceColors(oilUseAggregatedNames)));
		}
		
		List<String> electricityUseAggregatedNames = new ArrayList<String>();
		List<String> electricityUseDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				electricityUseDisaggregatedNames.add(society.getName() + " Operations");
			}
		}
		electricityUseAggregatedNames.add(getSociety().getName() + " Operations");
		List<Color> societyColors = new ArrayList<Color>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				societyColors.add(PlottingUtils.getSocietyColor(society));
			}
		} else {
			societyColors.add(PlottingUtils.getSocietyColor(getSociety()));
		}
		nationalPane.addTab("Reservoir", Icons.PETROLEUM_RESERVOIR, createStackedAreaChart(
				petroleumSystem.getName() + " Oil Reservoir",
				"Oil Reservoir Volume (" + oilUnits + ")",
				petroleumReservoirDataset, societyColors.toArray(new Color[0])), "Reservoir");

		petroleumStatePanel = new SpatialStatePanel(getSociety(), 
				new PetroleumStateProvider());
		nationalPane.addTab("Network", Icons.NETWORK, petroleumStatePanel);
		
		ChangeListener tabSynchronizer = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(e.getSource() instanceof JTabbedPane) {
					JTabbedPane pane = ((JTabbedPane)e.getSource());
					if(e.getSource() == nationalPane) {
						for(LocalPetroleumSystemPanel panel : nestedPanels) {
							panel.setSelectedIndex(pane.getSelectedIndex());
						}
					} else {
						nationalPane.setSelectedIndex(pane.getSelectedIndex());
					}
				}
			}
		};
		nationalPane.addChangeListener(tabSynchronizer);
		
		if(petroleumSystem instanceof LocalPetroleumSoS) {
			JTabbedPane regionalData = this; // new JTabbedPane();
			for(PetroleumSystem.Local nestedSystem : 
				((LocalPetroleumSoS) petroleumSystem).getNestedSystems()) {
				LocalPetroleumSystemPanel nestedPanel = 
						new LocalPetroleumSystemPanel(nestedSystem);
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
	 * Gets the nested petroleum systems.
	 *
	 * @return the nested petroleum systems
	 */
	private List<PetroleumSystem.Local> getNestedPetroleumSystems() {
		List<PetroleumSystem.Local> systems = new ArrayList<PetroleumSystem.Local>();
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			if(nestedSociety.getPetroleumSystem() instanceof PetroleumSystem.Local) {
				systems.add((PetroleumSystem.Local)nestedSociety.getPetroleumSystem());
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
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		cashFlow.removeAllSeries();
		netCashFlow.removeAllSeries();
		petroleumReservoirDataset.removeAllSeries();
		petroleumUseAggregatedData.removeAllSeries();
		petroleumUseDisaggregatedData.removeAllSeries();
		petroleumSourceAggregatedData.removeAllSeries();
		petroleumSourceDisaggregatedData.removeAllSeries();
		electricityUseAggregatedData.removeAllSeries();
		electricityUseDisaggregatedData.removeAllSeries();
		cashFlow.removeAllSeries();
		netCashFlow.removeAllSeries();
		cumulativeBalance.removeAllSeries();
	}

	@Override
	public void simulationCompleted(UpdateEvent event) {
		for(LocalPetroleumSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationCompleted(event);
		}
	}

	@Override
	public void simulationInitialized(UpdateEvent event) {
		initialize();
		for(LocalPetroleumSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationInitialized(event);
		}
		petroleumStatePanel.repaint();
	}
	
	@Override
	public void simulationUpdated(UpdateEvent event) {
		update((int)event.getTime());
		for(LocalPetroleumSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationUpdated(event);
		}
		petroleumStatePanel.repaint();
	}

	/**
	 * Update.
	 *
	 * @param year the year
	 */
	private void update(int year) {
		updateSeries(cashFlow, "Capital Expense", year, 
				CurrencyUnits.convertFlow(
						-petroleumSystem.getCapitalExpense(),
						petroleumSystem, this));
		updateSeries(cashFlow, "Operations Expense", year,  
				CurrencyUnits.convertFlow(
						-petroleumSystem.getOperationsExpense()
						- petroleumSystem.getConsumptionExpense(),
						petroleumSystem, this));
		updateSeries(cashFlow, "Decommission Expense", year,  
				CurrencyUnits.convertFlow(
						-petroleumSystem.getDecommissionExpense(),
						petroleumSystem, this));
		if(!(petroleumSystem.getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Expense", year,  
					CurrencyUnits.convertFlow(
							-petroleumSystem.getDistributionExpense(),
							petroleumSystem, this));
		}
		updateSeries(cashFlow, "Import Expense", year,  
				CurrencyUnits.convertFlow(
						-petroleumSystem.getImportExpense(),
						petroleumSystem, this));
		if(!(petroleumSystem.getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Revenue", year, 
					CurrencyUnits.convertFlow( 
							petroleumSystem.getDistributionRevenue(),
							petroleumSystem, this));
		}
		updateSeries(cashFlow, "Export Revenue", year, 
				CurrencyUnits.convertFlow(
						petroleumSystem.getExportRevenue(),
						petroleumSystem, this));
		updateSeries(cashFlow, "Domestic Revenue", year,  
				CurrencyUnits.convertFlow(
						petroleumSystem.getSalesRevenue(),
						petroleumSystem, this));
		updateSeries(netCashFlow, "Net Revenue", year, 
				CurrencyUnits.convertFlow(petroleumSystem.getCashFlow(),
						petroleumSystem, this));
		updateSeries(cumulativeBalance, "Cumulative Net Revenue", year, 
				CurrencyUnits.convertFlow(petroleumSystem.getCumulativeCashFlow(),
						petroleumSystem, this));

		updateSeries(petroleumSourceAggregatedData, "Production", year, 
				OilUnits.convertFlow(petroleumSystem.getPetroleumProduction(), 
						petroleumSystem, this));
		updateSeries(petroleumUseAggregatedData, "Society", year, 
				OilUnits.convertFlow(getSociety().getSocialSystem().getPetroleumConsumption(),
						getSociety().getSocialSystem(), this));
		updateSeries(petroleumUseAggregatedData, "Electricity Operations", year, 
				OilUnits.convertFlow(
						getSociety().getElectricitySystem().getPetroleumConsumption(),
						getSociety().getElectricitySystem(), this));
		updateSeries(electricityUseAggregatedData, "Operations", year, 
				ElectricityUnits.convertFlow(petroleumSystem.getElectricityConsumption(),
						petroleumSystem, this));
		if(petroleumSystem instanceof LocalPetroleumSoS) {
			for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
				updateSeries(petroleumSourceDisaggregatedData, nestedSystem.getSociety().getName(), year,
						OilUnits.convertFlow(nestedSystem.getTotalPetroleumSupply()
								+ nestedSystem.getPetroleumOutDistribution()
								- nestedSystem.getPetroleumInDistribution()
								- nestedSystem.getPetroleumImport()
								+ nestedSystem.getPetroleumExport(), nestedSystem, this));
				updateSeries(petroleumUseDisaggregatedData, nestedSystem.getSociety().getName(), year,
						OilUnits.convertFlow(nestedSystem.getSociety().getTotalPetroleumDemand(), 
								nestedSystem.getSociety(), this));
				updateSeries(electricityUseDisaggregatedData, nestedSystem.getSociety().getName(), year, 
						ElectricityUnits.convertFlow(nestedSystem.getElectricityConsumption(),
								nestedSystem, this));
			}
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(petroleumReservoirDataset, nestedSociety.getName(), year, 
						OilUnits.convertStock(((PetroleumSystem)nestedSociety.getPetroleumSystem()).getReservoirVolume(),
								nestedSociety.getPetroleumSystem(), this));
			}
		} else {
			updateSeries(petroleumReservoirDataset, "Reservoir", year, 
					OilUnits.convertStock(
							petroleumSystem.getReservoirVolume(), 
							petroleumSystem, this));
			updateSeries(petroleumSourceAggregatedData, "Distribution", year, 
					OilUnits.convertFlow(petroleumSystem.getPetroleumInDistribution(),
							petroleumSystem, this));
			updateSeries(petroleumUseAggregatedData, "Distribution", year, 
					OilUnits.convertFlow(petroleumSystem.getPetroleumOutDistribution(),
							petroleumSystem, this));
		}
		
		updateSeries(petroleumSourceAggregatedData, "Import", year, 
				OilUnits.convertFlow(
						petroleumSystem.getPetroleumImport(),
						petroleumSystem, this));
		updateSeries(petroleumSourceDisaggregatedData, "Import", year, 
				OilUnits.convertFlow(
						petroleumSystem.getPetroleumImport(),
						petroleumSystem, this));
		updateSeries(petroleumUseAggregatedData, "Export", year, 
				OilUnits.convertFlow(
						petroleumSystem.getPetroleumExport(),
						petroleumSystem, this));
		updateSeries(petroleumUseDisaggregatedData, "Export", year, 
				OilUnits.convertFlow(
						petroleumSystem.getPetroleumExport(),
						petroleumSystem, this));
	}
}
