package edu.mit.sips.gui.electricity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.electricity.LocalElectricitySoS;
import edu.mit.sips.gui.LinearIndicatorPanel;
import edu.mit.sips.gui.PlottingUtils;
import edu.mit.sips.gui.SpatialStatePanel;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Class ElectricitySystemPanel.
 */
public class LocalElectricitySystemPanel extends ElectricitySystemPanel 
		implements CurrencyUnitsOutput, ElectricityUnitsOutput, OilUnitsOutput, WaterUnitsOutput {
	private static final long serialVersionUID = 2218175276232419659L;
	
	private final LinearIndicatorPanel renewableElectricityIndicatorPanel, 
	localElectricityIndicatorPanel;
	private final List<LocalElectricitySystemPanel> nestedPanels = 
			new ArrayList<LocalElectricitySystemPanel>();
	
	private final SpatialStatePanel electricityStatePanel;
	
	TimeSeriesCollection localElectricityData = new TimeSeriesCollection();
	TimeSeriesCollection electricityProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection electricitySupplyProfitData = new TimeSeriesCollection();
	TimeSeriesCollection renewableElectricityData = new TimeSeriesCollection();
	TimeSeriesCollection electricityConsumptionPerCapita = new TimeSeriesCollection();

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
	
	DefaultTableXYDataset capitalExpense = new DefaultTableXYDataset();
	DefaultTableXYDataset capitalExpenseTotal = new DefaultTableXYDataset();
	DefaultTableXYDataset cumulativeCapitalExpense = new DefaultTableXYDataset();
	
	private final CurrencyUnits currencyUnits = CurrencyUnits.Bsim;
	private final TimeUnits currencyTimeUnits = TimeUnits.year;
	private final ElectricityUnits electricityUnits = ElectricityUnits.TWh;
	private final TimeUnits electricityTimeUnits = TimeUnits.year;
	private final OilUnits oilUnits = OilUnits.Mtoe;
	private final TimeUnits oilTimeUnits = TimeUnits.year;
	private final WaterUnits waterUnits = WaterUnits.m3;
	private final TimeUnits waterTimeUnits = TimeUnits.year;

	/**
	 * Instantiates a new local electricity system panel.
	 *
	 * @param electricitySystem the electricity system
	 */
	public LocalElectricitySystemPanel(ElectricitySystem.Local electricitySystem) {
		super(electricitySystem);

		JPanel indicatorsPanel = new JPanel();
		indicatorsPanel.setLayout(
				new BoxLayout(indicatorsPanel, BoxLayout.LINE_AXIS));
		renewableElectricityIndicatorPanel = new LinearIndicatorPanel(
				"Renewable Electricity", 0, 1);
		localElectricityIndicatorPanel = new LinearIndicatorPanel(
				"Electricity Independence", 0, 1);
		indicatorsPanel.add(renewableElectricityIndicatorPanel);
		indicatorsPanel.add(localElectricityIndicatorPanel);
		// addTab("Indicators", Icons.INDICATORS, indicatorsPanel);

		List<String> revenueNames;
		if(!(getSociety() instanceof Country)) {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", /*"Input Expense", */"Distribution Expense", 
					"Distribution Revenue", "Domestic Revenue");
		} else {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", /*"Input Expense", */"Domestic Revenue");
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
		
		if(getElectricitySystem() instanceof LocalElectricitySoS) {
			nationalPane.addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
					getElectricitySystem().getName() + " Net Revenue",
					"Annual Net Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")", cashFlow, 
							PlottingUtils.getCashFlowColors(revenueNames), netCashFlow,
							"Cumulative Net Revenue (" + getCurrencyUnits() + ")",
							cumulativeBalance));
			/* temporarily removed
			addTab("Investment", Icons.INVESTMENT, createStackedAreaChart(
					"Annual Investment (" + getCurrencyUnits() + ")", capitalExpense, 
					PlottingUtils.getSocietyColors(getSociety().getNestedSocieties()), 
					capitalExpenseTotal,
					"Cumulative Investment (" + getCurrencyUnits() + ")", 
					cumulativeCapitalExpense));
			*/
		} else {
			nationalPane.addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
					getElectricitySystem().getName() + " Net Revenue",
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
		if(!(getElectricitySystem() instanceof LocalElectricitySoS)) {
			electricitySourceAggregatedNames.add("In-Distribution");
		}
		for(String name : electricitySourceAggregatedNames) {
			electricitySourceAggregatedData.addSeries(new XYSeries(name, true, false));
		}
		for(String name : electricitySourceDisaggregatedNames) {
			electricitySourceDisaggregatedData.addSeries(new XYSeries(name, true, false));
		}
		if(getElectricitySystem() instanceof LocalElectricitySoS) {
			nationalPane.addTab("Source", Icons.ELECTRICITY_SOURCE, createToggleableStackedAreaChart(
					getElectricitySystem().getName() + " Source",
					"Electricity Source (" + electricityUnits + "/" + electricityTimeUnits + ")", 
					electricitySourceAggregatedData, PlottingUtils.getResourceColors(electricitySourceAggregatedNames), 
					electricitySourceDisaggregatedData, PlottingUtils.getResourceColors(electricitySourceDisaggregatedNames)));
		} else {
			nationalPane.addTab("Source", Icons.ELECTRICITY_SOURCE, createStackedAreaChart(
					getElectricitySystem().getName() + " Source",
					"Electricity Source (" + electricityUnits + "/" + electricityTimeUnits + ")", 
					electricitySourceAggregatedData, PlottingUtils.getResourceColors(electricitySourceAggregatedNames)));
		}
		
		List<String> electricityUseAggregatedNames = new ArrayList<String>();
		List<String> electricityUseDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				electricityUseDisaggregatedNames.add(society.getName());
			}
			// electricityUseNames.add("Losses");
		}
		electricityUseAggregatedNames.add(getSociety().getName()  + " Society");
		electricityUseAggregatedNames.add("Water Operations");
		electricityUseAggregatedNames.add("Petroleum Operations");
		if(!(getElectricitySystem() instanceof LocalElectricitySoS)) {
			electricityUseAggregatedNames.add("Out-Distribution");
		}
		electricityUseAggregatedNames.add("Wasted");
		for(String name : electricityUseAggregatedNames) {
			electricityUseAggregatedData.addSeries(new XYSeries(name, true, false));
		}
		for(String name : electricityUseDisaggregatedNames) {
			electricityUseDisaggregatedData.addSeries(new XYSeries(name, true, false));
		}
		if(getElectricitySystem() instanceof LocalElectricitySoS) {
			nationalPane.addTab("Use", Icons.ELECTRICITY_USE, createToggleableStackedAreaChart(
					getElectricitySystem().getName() + " Use",
					"Electricity Use (" + electricityUnits + "/" + electricityTimeUnits + ")", 
					electricityUseAggregatedData, PlottingUtils.getResourceColors(electricityUseAggregatedNames), 
					electricityUseDisaggregatedData, PlottingUtils.getResourceColors(electricityUseDisaggregatedNames)));
		} else {
			nationalPane.addTab("Use", Icons.ELECTRICITY_USE, createStackedAreaChart(
					getElectricitySystem().getName() + " Use",
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
		/*if(getElectricitySystem() instanceof LocalElectricitySoS) {
			addTab("Use", Icons.PETROLEUM_USE, createToggleableStackedAreaChart(
					getElectricitySystem().getName() + " Petroleum Use",
					"Petroleum Use (" + oilUnits + "/" + oilTimeUnits + ")", 
					petroleumUseAggregatedData, PlottingUtils.getResourceColors(oilUseAggregatedNames), 
					petroleumUseDisaggregatedData, PlottingUtils.getResourceColors(oilUseDisaggregatedNames)));
		} else {
			addTab("Use", Icons.PETROLEUM_USE, createStackedAreaChart(
					getElectricitySystem().getName() + " Petroleum Use",
					"Petroleum Use (" + oilUnits + "/" + oilTimeUnits + ")", 
					petroleumUseAggregatedData, PlottingUtils.getResourceColors(oilUseAggregatedNames)));
		}
		
		addTab("Use", Icons.WATER_USE, createStackedAreaChart(
				"Water Use (" + waterUnits + "/" + waterTimeUnits + ")", waterUseData));
		addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Electricity Use Fraction (-)", 
				localElectricityData));
		addTab("Renewable", Icons.RENEWABLE, createTimeSeriesChart(
				"Renewable Electricity Fraction (-)", 
				renewableElectricityData));
		addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Electricity Consumption per Capita (" + 
						ElectricityUnits.kWh + "/" + TimeUnits.day + ")", 
				electricityConsumptionPerCapita), "Consumption");*/
		/* TODO
		addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
				"Unit Production Cost (SAR/toe)", 
				electricityProductCostData));
		addTab("Supply Profit", Icons.COST_SUPPLY, createTimeSeriesChart(
				"Unit Supply Profit (SAR/toe)", 
				electricitySupplyProfitData));
		*/
		
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
			// addTab("Regions", Icons.INFRASTRUCTURE, regionalData);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyTimeUnits()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnits()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	/**
	 * Gets the electricity system.
	 *
	 * @return the electricity system
	 */
	public ElectricitySystem.Local getElectricitySystem() {
		return (ElectricitySystem.Local) getInfrastructureSystem();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityTimeUnits()
	 */
	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnits()
	 */
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
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilTimeUnits()
	 */
	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilUnits()
	 */
	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
	}

	private void initialize() {
		renewableElectricityIndicatorPanel.initialize();
		localElectricityIndicatorPanel.initialize();
		cashFlow.removeAllSeries();
		netCashFlow.removeAllSeries();
		localElectricityData.removeAllSeries();
		renewableElectricityData.removeAllSeries();
		electricityProductCostData.removeAllSeries();
		electricitySupplyProfitData.removeAllSeries();
		electricityConsumptionPerCapita.removeAllSeries();
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
		capitalExpense.removeAllSeries();
		capitalExpenseTotal.removeAllSeries();
		cumulativeCapitalExpense.removeAllSeries();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationCompleted(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationCompleted(UpdateEvent event) {
		// nothing to do here
		for(LocalElectricitySystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationCompleted(event);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationInitialized(UpdateEvent event) {
		initialize();
		for(LocalElectricitySystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationInitialized(event);
		}
		electricityStatePanel.repaint();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		update((int)event.getTime());
		for(LocalElectricitySystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationUpdated(event);
		}
		electricityStatePanel.repaint();
	}

	private void update(int year) {
		/* temporarily removed
		updateSeriesCollection(localElectricityData, getSociety().getName(), 
				year, getElectricitySystem().getLocalElectricityFraction());
		localElectricityIndicatorPanel.setValue(
				getElectricitySystem().getLocalElectricityFraction());
		for(ElectricitySystem.Local nestedSystem : getNestedElectricitySystems()) {
			updateSeriesCollection(localElectricityData, 
					nestedSystem.getSociety().getName(), year, 
					nestedSystem.getLocalElectricityFraction());
		}
		
		updateSeriesCollection(renewableElectricityData, 
				getSociety().getName(), year, 
				getElectricitySystem().getRenewableElectricityFraction());
		renewableElectricityIndicatorPanel.setValue(
				getElectricitySystem().getRenewableElectricityFraction());
		for(ElectricitySystem.Local nestedSystem : getNestedElectricitySystems()) {
			updateSeriesCollection(renewableElectricityData, 
					nestedSystem.getSociety().getName(), year, 
					nestedSystem.getRenewableElectricityFraction());
		}
		updateSeriesCollection(electricityProductCostData, 
				getSociety().getName(), year, 
				getElectricitySystem().getUnitProductionCost());
		for(ElectricitySystem.Local nestedSystem : getNestedElectricitySystems()) {
			updateSeriesCollection(electricityProductCostData, 
					nestedSystem.getSociety().getName(), year, 
					nestedSystem.getUnitProductionCost());
		}

		updateSeriesCollection(electricitySupplyProfitData, 
				getSociety().getName(), year, 
				getElectricitySystem().getUnitSupplyProfit());
		for(ElectricitySystem.Local nestedSystem : getNestedElectricitySystems()) {
			updateSeriesCollection(electricitySupplyProfitData, 
					nestedSystem.getSociety().getName(), year, 
					nestedSystem.getUnitSupplyProfit());
		}
		
		if(getSociety().getSocialSystem().getPopulation() > 0) {
			updateSeriesCollection(electricityConsumptionPerCapita, 
					getSociety().getName(), year, 
					DefaultUnits.convertFlow(getSociety().getSocialSystem().getElectricityConsumption(),
							getSociety().getSocialSystem().getElectricityUnits(),
							getSociety().getSocialSystem().getElectricityTimeUnits(),
							ElectricityUnits.kWh, TimeUnits.day)
							/ getSociety().getSocialSystem().getPopulation());
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				if(nestedSociety.getSocialSystem().getPopulation() > 0) {
					updateSeriesCollection(electricityConsumptionPerCapita, 
							nestedSociety.getName(), year, 
							DefaultUnits.convertFlow(nestedSociety.getSocialSystem().getElectricityConsumption() ,
									nestedSociety.getSocialSystem().getElectricityUnits(),
									nestedSociety.getSocialSystem().getElectricityTimeUnits(),
									ElectricityUnits.kWh, TimeUnits.day)
									/ nestedSociety.getSocialSystem().getPopulation());
				}
			}
		}
		*/

		updateSeries(electricitySourceAggregatedData, "Production", year, 
				ElectricityUnits.convertFlow(
						getElectricitySystem().getElectricityProduction(), 
						getElectricitySystem(), this));
		updateSeries(electricitySourceAggregatedData, "Private Operations", year, 
				ElectricityUnits.convertFlow(
						getElectricitySystem().getElectricityFromPrivateProduction(),
						getElectricitySystem(), this));
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
		if(getElectricitySystem() instanceof LocalElectricitySoS) {
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
						OilUnits.convertFlow(getElectricitySystem().getPetroleumConsumption(),
								getElectricitySystem(), this));
				/* temporarily removed
				if(nestedSystem.getPetroleumConsumption() > 0) {
					updateSeries(petroleumUseData, nestedSystem.getName(), year, 
							OilUnits.convertFlow(nestedSystem.getPetroleumConsumptionFromPublicProduction(),
									nestedSystem, this));
				}
				if(nestedSystem.getWaterConsumption() > 0) {
					updateSeries(waterUseData, nestedSystem.getName(), year, 
							WaterUnits.convertFlow(nestedSystem.getWaterConsumption(),
									nestedSystem, this));
				}
				*/
			}
			/*updateSeries(electricitySourceData, "Production", year, 
					ElectricityUnits.convertFlow(
							getElectricitySystem().getElectricityProduction(),
							getElectricitySystem(), this));*/
			/*
			if(!getElectricitySystem().getExternalElements().isEmpty()) {
				updateSeries(electricitySourceData, "Distribution", year, 
						ElectricityUnits.convertFlow(
								getElectricitySystem().getElectricityInDistribution(),
								getElectricitySystem(), this));
				updateSeries(electricityUseData, "Distribution", year, 
						ElectricityUnits.convertFlow(
								getElectricitySystem().getElectricityOutDistribution(),
								getElectricitySystem(), this));
			}
			*/
			/*updateSeries(electricityUseData, "Distribution Losses", year, 
					ElectricityUnits.convertFlow(
							getElectricitySystem().getElectricityOutDistributionLosses(),
							getElectricitySystem(), this));*/
		} else {
			updateSeries(electricitySourceAggregatedData,  "Distribution", year, 
					ElectricityUnits.convertFlow(
							getElectricitySystem().getElectricityInDistribution(), 
							getElectricitySystem(), this));
			updateSeries(electricityUseAggregatedData, "Distribution", year, 
					ElectricityUnits.convertFlow(
							getElectricitySystem().getElectricityOutDistribution(), 
							getElectricitySystem(), this));
		}
		updateSeries(electricityUseAggregatedData, "Wasted", year,  
				ElectricityUnits.convertFlow(
						getElectricitySystem().getElectricityWasted(),
						getElectricitySystem(), this));
		updateSeries(electricityUseDisaggregatedData, "Wasted", year,  
				ElectricityUnits.convertFlow(
						getElectricitySystem().getElectricityWasted(),
						getElectricitySystem(), this));
		updateSeries(petroleumUseAggregatedData, "Operations", year, 
				OilUnits.convertFlow(getElectricitySystem().getPetroleumConsumptionFromPublicProduction(),
						getElectricitySystem(), this));
		updateSeries(petroleumUseAggregatedData, "Private Operations", year, 
				OilUnits.convertFlow(getElectricitySystem().getPetroleumConsumptionFromPrivateProduction(),
						getElectricitySystem(), this));
		/* temporarily removed
		updateSeries(waterUseData, "Operations", year, 
				WaterUnits.convertFlow(getElectricitySystem().getWaterConsumption(),
						getElectricitySystem(), this));
		*/
		
		updateSeries(cashFlow, "Capital Expense", year, 
				CurrencyUnits.convertFlow(
						-getElectricitySystem().getCapitalExpense(), 
						getElectricitySystem(), this));
		updateSeries(cashFlow, "Operations Expense", year, 
				CurrencyUnits.convertFlow(
						-getElectricitySystem().getOperationsExpense()
						-getElectricitySystem().getConsumptionExpense(), 
						getElectricitySystem(), this));
		updateSeries(cashFlow, "Decommission Expense", year, 
				CurrencyUnits.convertFlow(
						-getElectricitySystem().getDecommissionExpense(), 
						getElectricitySystem(), this));
		/*updateSeries(electricityRevenue, "Consumption Expense", year, 
				CurrencyUnits.convertFlow(
						-getElectricitySystem().getConsumptionExpense(), 
						getElectricitySystem(), this));*/
		if(!(getElectricitySystem().getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Expense", year, 
					CurrencyUnits.convertFlow(
							-getElectricitySystem().getDistributionExpense(), 
							getElectricitySystem(), this));
			updateSeries(cashFlow, "Distribution Revenue", year, 
					CurrencyUnits.convertFlow(
							getElectricitySystem().getDistributionRevenue(), 
							getElectricitySystem(), this));
		}
		updateSeries(cashFlow, "Domestic Revenue", year, 
				CurrencyUnits.convertFlow(
						getElectricitySystem().getSalesRevenue(), 
						getElectricitySystem(), this));
		updateSeries(netCashFlow, "Net Revenue", year, 
				CurrencyUnits.convertFlow(
						getElectricitySystem().getCashFlow(), 
						getElectricitySystem(), this));
		updateSeries(cumulativeBalance, "Cumulative Net Revenue", year, 
				CurrencyUnits.convertFlow(getElectricitySystem().getCumulativeCashFlow(),
						getElectricitySystem(), this));
		
		if(getElectricitySystem() instanceof LocalElectricitySoS) {
			for(InfrastructureSystem nestedSystem : getNestedElectricitySystems()) {
				updateSeries(capitalExpense, nestedSystem.getName(), year, 
						CurrencyUnits.convertFlow(nestedSystem.getCapitalExpense(), 
								nestedSystem, this));
			}
			updateSeries(capitalExpenseTotal, "Total Investment", year, 
					CurrencyUnits.convertFlow(
							((LocalElectricitySoS)getElectricitySystem())
							.getCapitalExpense(), getSociety(), this));
			updateSeries(cumulativeCapitalExpense, "Cumulative Investment", year, 
					CurrencyUnits.convertFlow(
							((LocalElectricitySoS)getElectricitySystem())
							.getCumulativeCapitalExpense(), getSociety(), this));
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnits()
	 */
	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterTimeUnits()
	 */
	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}
}