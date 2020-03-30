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
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.LocalAgricultureSoS;
import edu.mit.sips.gui.PlottingUtils;
import edu.mit.sips.gui.base.SpatialStatePanel;
import edu.mit.sips.gui.event.UpdateEvent;
import edu.mit.sips.io.Icons;
import edu.mit.sips.units.CurrencyUnits;
import edu.mit.sips.units.CurrencyUnitsOutput;
import edu.mit.sips.units.FoodUnits;
import edu.mit.sips.units.FoodUnitsOutput;
import edu.mit.sips.units.TimeUnits;
import edu.mit.sips.units.WaterUnits;
import edu.mit.sips.units.WaterUnitsOutput;

/**
 * An agriculture system panel suitable for locally-controlled systems.
 * 
 * @author Paul T. Grogan
 */
public class LocalAgricultureSystemPanel extends AgricultureSystemPanel 
implements FoodUnitsOutput, CurrencyUnitsOutput, WaterUnitsOutput {
	private static final long serialVersionUID = 569560127649283731L;
	private static final FoodUnits foodUnits = FoodUnits.EJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.km3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final CurrencyUnits currencyUnits = CurrencyUnits.Bsim;
	private static final TimeUnits currencyTimeUnits = TimeUnits.year;

	private final AgricultureSystem.Local agricultureSystem;
	private final List<LocalAgricultureSystemPanel> nestedPanels = 
			new ArrayList<LocalAgricultureSystemPanel>();
	private final SpatialStatePanel agricultureStatePanel;
	
	DefaultTableXYDataset landAvailableAggregatedDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset landAvailableDisaggregatedDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset laborAvailableAggregatedDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset laborAvailableDisaggregatedDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset foodSourceAggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset foodSourceDisaggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset foodUseAggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset foodUseDisaggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterUseAggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterUseDisaggregatedData = new DefaultTableXYDataset();
	DefaultTableXYDataset cashFlow = new DefaultTableXYDataset();
	DefaultTableXYDataset netCashFlow = new DefaultTableXYDataset();
	DefaultTableXYDataset cumulativeBalance = new DefaultTableXYDataset();
		
	/**
	 * Instantiates a new local agriculture system panel.
	 *
	 * @param agricultureSystem the agriculture system
	 */
	public LocalAgricultureSystemPanel(AgricultureSystem.Local agricultureSystem) {
		super(agricultureSystem);
		this.agricultureSystem = agricultureSystem;
		
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
		if(agricultureSystem instanceof LocalAgricultureSoS) {
			nationalPane = new JTabbedPane();
			addTab(getSociety().getName(), Icons.COUNTRY, nationalPane);
		} else {
			nationalPane = this;
		}

		if(agricultureSystem instanceof LocalAgricultureSoS) {
			nationalPane.addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
					agricultureSystem.getName() + " Net Revenue",
					"Annual Net Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")", cashFlow, 
							PlottingUtils.getCashFlowColors(revenueNames), netCashFlow,
							"Cumulative Net Revenue (" + getCurrencyUnits() + ")",
							cumulativeBalance));
		} else {
			nationalPane.addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
					agricultureSystem.getName() + " Net Revenue",
					"Annual Net Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")", cashFlow, 
							PlottingUtils.getCashFlowColors(revenueNames), netCashFlow));
		}
		
		List<String> foodSourceAggregatedNames = new ArrayList<String>();
		List<String> foodSourceDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				foodSourceDisaggregatedNames.add(society.getName());
			}
		}
		foodSourceAggregatedNames.add(getSociety().getName() + " Production");
		if(!(agricultureSystem instanceof LocalAgricultureSoS)) {
			foodSourceAggregatedNames.add("In-Distribution");
		}
		foodSourceAggregatedNames.add("Import");
		foodSourceDisaggregatedNames.add("Import");
		for(String name : foodSourceAggregatedNames) {
			foodSourceAggregatedData.addSeries(new XYSeries(name, true, false));
		}
		for(String name : foodSourceDisaggregatedNames) {
			foodSourceDisaggregatedData.addSeries(new XYSeries(name, true, false));
		}
		if(agricultureSystem instanceof LocalAgricultureSoS) {
			nationalPane.addTab("Source", Icons.AGRICULTURE_SOURCE, createToggleableStackedAreaChart(
					agricultureSystem.getName() + " Food Source",
					"Food Source (" + foodUnits + "/" + foodTimeUnits + ")", 
					foodSourceAggregatedData, PlottingUtils.getResourceColors(foodSourceAggregatedNames), 
					foodSourceDisaggregatedData, PlottingUtils.getResourceColors(foodSourceDisaggregatedNames)));
		} else {
			nationalPane.addTab("Source", Icons.AGRICULTURE_SOURCE, createStackedAreaChart(
					agricultureSystem.getName() + " Food Source",
					"Food Source (" + foodUnits + "/" + foodTimeUnits + ")", 
					foodSourceAggregatedData, PlottingUtils.getResourceColors(foodSourceAggregatedNames)));
		}
		
		List<String> foodUseAggregatedNames = new ArrayList<String>();
		List<String> foodUseDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				foodUseDisaggregatedNames.add(society.getName());
			}
		}
		foodUseAggregatedNames.add(getSociety().getName()  + " Society");
		if(!(agricultureSystem instanceof LocalAgricultureSoS)) {
			foodUseAggregatedNames.add("Out-Distribution");
		}
		foodUseAggregatedNames.add("Export");
		foodUseDisaggregatedNames.add("Export");
		for(String name : foodUseAggregatedNames) {
			foodUseAggregatedData.addSeries(new XYSeries(name, true, false));
		}
		for(String name : foodUseDisaggregatedNames) {
			foodUseDisaggregatedData.addSeries(new XYSeries(name, true, false));
		}
		if(agricultureSystem instanceof LocalAgricultureSoS) {
			nationalPane.addTab("Use", Icons.AGRICULTURE_USE, createToggleableStackedAreaChart(
					agricultureSystem.getName() + " Food Use",
					"Food Use (" + foodUnits + "/" + foodTimeUnits + ")", 
					foodUseAggregatedData, PlottingUtils.getResourceColors(foodUseAggregatedNames), 
					foodUseDisaggregatedData, PlottingUtils.getResourceColors(foodUseDisaggregatedNames)));
		} else {
			nationalPane.addTab("Use", Icons.AGRICULTURE_USE, createStackedAreaChart(
					agricultureSystem.getName() + " Food Use",
					"Food Use (" + foodUnits + "/" + foodTimeUnits + ")", 
					foodUseAggregatedData, PlottingUtils.getResourceColors(foodUseAggregatedNames)));
		}
		
		List<String> waterUseAggregatedNames = new ArrayList<String>();
		List<String> waterUseDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				waterUseDisaggregatedNames.add(society.getName() + " Operations");
			}
		}
		waterUseDisaggregatedNames.add(getSociety().getName() + " Operations");
		if(agricultureSystem instanceof LocalAgricultureSoS) {
			nationalPane.addTab("Use", Icons.WATER_USE, createToggleableStackedAreaChart(
					agricultureSystem.getName() + " Water Use",
					"Water Use (" + waterUnits + "/" + waterTimeUnits + ")",
					waterUseAggregatedData, PlottingUtils.getResourceColors(waterUseAggregatedNames),
					waterUseDisaggregatedData, PlottingUtils.getResourceColors(waterUseDisaggregatedNames)));
		} else {
			nationalPane.addTab("Use", Icons.WATER_USE, createStackedAreaChart(
					agricultureSystem.getName() + " Water Use",
					"Water Use (" + waterUnits + "/" + waterTimeUnits + ")",
					waterUseAggregatedData, PlottingUtils.getResourceColors(waterUseAggregatedNames)));
		}
				
		List<Color> landColorsAggregated = new ArrayList<Color>();
		List<Color> landColorsDisaggregated = new ArrayList<Color>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				landColorsDisaggregated.add(PlottingUtils.getSocietySecondaryColor(society));
				landColorsDisaggregated.add(PlottingUtils.getSocietyColor(society));
			}
		}
		landColorsAggregated.add(PlottingUtils.getSocietySecondaryColor(getSociety()));
		landColorsAggregated.add(PlottingUtils.getSocietyColor(getSociety()));
		
		if(agricultureSystem instanceof LocalAgricultureSoS) {
			nationalPane.addTab("Land", Icons.ARABLE_LAND, createToggleableStackedAreaChart(
					agricultureSystem.getName() + " Land", "Arable Land (km^2)", 
					landAvailableAggregatedDataset, landColorsAggregated.toArray(new Color[0]),
					landAvailableDisaggregatedDataset, landColorsDisaggregated.toArray(new Color[0])));
			nationalPane.addTab("Labor", Icons.LABOR, createToggleableStackedAreaChart(
					agricultureSystem.getName() + " Labor", "Available Labor (people)", 
					laborAvailableAggregatedDataset, landColorsAggregated.toArray(new Color[0]),
					laborAvailableDisaggregatedDataset, landColorsDisaggregated.toArray(new Color[0])));
		} else {
			nationalPane.addTab("Land", Icons.ARABLE_LAND, createStackedAreaChart(
					agricultureSystem.getName() + " Land", "Arable Land (km^2)", 
					landAvailableAggregatedDataset, landColorsAggregated.toArray(new Color[0])));
			nationalPane.addTab("Labor", Icons.LABOR, createStackedAreaChart(
					agricultureSystem.getName() + " Labor", "Available Labor (people)", 
					laborAvailableAggregatedDataset, landColorsAggregated.toArray(new Color[0])));
		}

		agricultureStatePanel = new SpatialStatePanel(
				agricultureSystem.getSociety(), new AgricultureStateProvider());
		nationalPane.addTab("Network", Icons.NETWORK, agricultureStatePanel);
		
		ChangeListener tabSynchronizer = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(e.getSource() instanceof JTabbedPane) {
					JTabbedPane pane = ((JTabbedPane)e.getSource());
					if(e.getSource() == nationalPane) {
						for(LocalAgricultureSystemPanel panel : nestedPanels) {
							panel.setSelectedIndex(pane.getSelectedIndex());
						}
					} else {
						nationalPane.setSelectedIndex(pane.getSelectedIndex());
					}
				}
			}
		};
		nationalPane.addChangeListener(tabSynchronizer);
		
		if(agricultureSystem instanceof LocalAgricultureSoS) {
			JTabbedPane regionalData = this;
			for(AgricultureSystem.Local nestedSystem : 
				((LocalAgricultureSoS) agricultureSystem).getNestedSystems()) {
				LocalAgricultureSystemPanel nestedPanel = 
						new LocalAgricultureSystemPanel(nestedSystem);
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
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}
	
	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
	}

	/**
	 * Gets the nested agriculture systems.
	 *
	 * @return the nested agriculture systems
	 */
	private List<AgricultureSystem.Local> getNestedAgricultureSystems() {
		List<AgricultureSystem.Local> systems = new ArrayList<AgricultureSystem.Local>();
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			if(nestedSociety.getAgricultureSystem() instanceof AgricultureSystem.Local) {
				systems.add((AgricultureSystem.Local)nestedSociety.getAgricultureSystem());
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
		landAvailableAggregatedDataset.removeAllSeries();
		landAvailableAggregatedDataset.removeAllSeries();
		laborAvailableAggregatedDataset.removeAllSeries();
		laborAvailableDisaggregatedDataset.removeAllSeries();
		foodUseAggregatedData.removeAllSeries();
		foodUseDisaggregatedData.removeAllSeries();
		waterUseAggregatedData.removeAllSeries();
		waterUseDisaggregatedData.removeAllSeries();
		foodSourceAggregatedData.removeAllSeries();
		foodSourceDisaggregatedData.removeAllSeries();
		cashFlow.removeAllSeries();
		netCashFlow.removeAllSeries();
		cumulativeBalance.removeAllSeries();
	}

	@Override
	public void simulationCompleted(UpdateEvent event) {
		for(LocalAgricultureSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationCompleted(event);
		}
	}

	@Override
	public void simulationInitialized(UpdateEvent event) {
		initialize();
		for(LocalAgricultureSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationInitialized(event);
		}
		agricultureStatePanel.repaint();
	}

	@Override
	public void simulationUpdated(UpdateEvent event) {
		update((int)event.getTime());
		for(LocalAgricultureSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationUpdated(event);
		}
		agricultureStatePanel.repaint();
	}

	/**
	 * Update.
	 *
	 * @param year the year
	 */
	private void update(int year) {		
		if(agricultureSystem instanceof LocalAgricultureSoS) {
			for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
				updateSeries(landAvailableDisaggregatedDataset, nestedSystem.getSociety().getName() + " Land Available", 
						year, nestedSystem.getArableLandArea() - nestedSystem.getLandAreaUsed());
				updateSeries(landAvailableDisaggregatedDataset, nestedSystem.getSociety().getName() + " Land Used", 
						year, nestedSystem.getLandAreaUsed());
			}
			for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
				updateSeries(laborAvailableDisaggregatedDataset, nestedSystem.getSociety().getName() + " Labor Available", 
						year, nestedSystem.getLaborParticipationRate() 
						* nestedSystem.getSociety().getSocialSystem().getPopulation()
						- nestedSystem.getLaborUsed());
				updateSeries(laborAvailableDisaggregatedDataset, nestedSystem.getSociety().getName() + " Labor Used", 
						year, nestedSystem.getLaborUsed());
			}
		}
		updateSeries(landAvailableAggregatedDataset, "Available", year, 
				agricultureSystem.getArableLandArea() - agricultureSystem.getLandAreaUsed());
		updateSeries(landAvailableAggregatedDataset, "Used", year, 
				agricultureSystem.getLandAreaUsed());
		
		updateSeries(laborAvailableAggregatedDataset, "Available", year, 
				agricultureSystem.getLaborParticipationRate() 
				* getSociety().getSocialSystem().getPopulation()
				- agricultureSystem.getLaborUsed());
		updateSeries(laborAvailableAggregatedDataset, "Used", year, 
				agricultureSystem.getLaborUsed());
		
		updateSeries(cashFlow, "Capital Expense", year, 
				CurrencyUnits.convertFlow(-agricultureSystem.getCapitalExpense(),
						agricultureSystem, this));
		updateSeries(cashFlow, "Operations Expense", year, 
				CurrencyUnits.convertFlow(-agricultureSystem.getOperationsExpense()
						-agricultureSystem.getConsumptionExpense(),
						agricultureSystem, this));
		updateSeries(cashFlow, "Decommission Expense", year, 
				CurrencyUnits.convertFlow(-agricultureSystem.getDecommissionExpense(),
						agricultureSystem, this));
		if(!(getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Expense", year, 
					CurrencyUnits.convertFlow(-agricultureSystem.getDistributionExpense(),
							agricultureSystem, this));
		}
		updateSeries(cashFlow, "Import Expense", year, 
				CurrencyUnits.convertFlow(-agricultureSystem.getImportExpense(),
						agricultureSystem, this));
		if(!(getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Revenue", year, 
					CurrencyUnits.convertFlow(agricultureSystem.getDistributionRevenue(),
							agricultureSystem, this));
		}
		updateSeries(cashFlow, "Export Revenue", year, 
				CurrencyUnits.convertFlow(agricultureSystem.getExportRevenue(),
						agricultureSystem, this));
		updateSeries(cashFlow, "Domestic Revenue", year, 
				CurrencyUnits.convertFlow(agricultureSystem.getSalesRevenue(),
						agricultureSystem, this));
		updateSeries(netCashFlow, "Net Revenue", year, 
				CurrencyUnits.convertFlow(agricultureSystem.getCashFlow(),
						agricultureSystem, this));
		updateSeries(cumulativeBalance, "Cumulative Net Revenue", year, 
				CurrencyUnits.convertFlow(agricultureSystem.getCumulativeCashFlow(),
						agricultureSystem, this));

		updateSeries(foodSourceAggregatedData, "Production", year, 
				FoodUnits.convertFlow(agricultureSystem.getFoodProduction(),
						agricultureSystem, this));
		updateSeries(foodUseAggregatedData, "Society", year, 
				FoodUnits.convertFlow(getSociety().getSocialSystem().getFoodConsumption(),
						getSociety().getSocialSystem(), this));
		if(agricultureSystem instanceof LocalAgricultureSoS) {
			for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
				updateSeries(foodSourceDisaggregatedData, nestedSystem.getSociety().getName(), year,
						FoodUnits.convertFlow(nestedSystem.getTotalFoodSupply()
								+ nestedSystem.getFoodOutDistribution()
								- nestedSystem.getFoodInDistribution()
								+ nestedSystem.getFoodExport()
								- nestedSystem.getFoodImport(), nestedSystem, this));
				updateSeries(foodUseDisaggregatedData, nestedSystem.getSociety().getName(), year,
						FoodUnits.convertFlow(nestedSystem.getSociety().getTotalFoodDemand(), 
								nestedSystem.getSociety(), this));
				updateSeries(waterUseDisaggregatedData, nestedSystem.getSociety().getName(), year, 
						WaterUnits.convertFlow(nestedSystem.getWaterConsumption(),
								nestedSystem, this));
			}
		} else {
			updateSeries(foodSourceAggregatedData, "Distribution", year, 
					FoodUnits.convertFlow(agricultureSystem.getFoodInDistribution(),
							agricultureSystem, this));
			updateSeries(foodUseAggregatedData, "Distribution", year, 
					FoodUnits.convertFlow(agricultureSystem.getFoodOutDistribution(), 
							agricultureSystem, this));
		}
		updateSeries(waterUseAggregatedData, "Operations", year, 
				WaterUnits.convertFlow(agricultureSystem.getWaterConsumption(),
						agricultureSystem, this));
		updateSeries(foodSourceAggregatedData, "Import", year, 
				FoodUnits.convertFlow(agricultureSystem.getFoodImport(),
						agricultureSystem, this));
		updateSeries(foodSourceDisaggregatedData, "Import", year, 
				FoodUnits.convertFlow(agricultureSystem.getFoodImport(),
						agricultureSystem, this));
		updateSeries(foodUseAggregatedData, "Export", year, 
				FoodUnits.convertFlow(agricultureSystem.getFoodExport(),
						agricultureSystem, this));
		updateSeries(foodUseDisaggregatedData, "Export", year, 
				FoodUnits.convertFlow(agricultureSystem.getFoodExport(),
						agricultureSystem, this));
	}
}
