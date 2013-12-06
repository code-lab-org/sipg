package edu.mit.sips.gui.electricity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.electricity.LocalElectricitySystem;
import edu.mit.sips.gui.LinearIndicatorPanel;
import edu.mit.sips.gui.PlottingUtils;
import edu.mit.sips.gui.SpatialStatePanel;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.DefaultUnits;
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
	
	private final SpatialStatePanel electricityStatePanel;
	
	TimeSeriesCollection localElectricityData = new TimeSeriesCollection();
	TimeSeriesCollection electricityProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection electricitySupplyProfitData = new TimeSeriesCollection();
	TimeSeriesCollection renewableElectricityData = new TimeSeriesCollection();
	TimeSeriesCollection electricityConsumptionPerCapita = new TimeSeriesCollection();

	DefaultTableXYDataset electricitySourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityNetRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterUseData = new DefaultTableXYDataset();
	
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
		
		electricityStatePanel = new SpatialStatePanel(
				getSociety(), new ElectricityStateProvider());
		addTab("Network Flow", Icons.NETWORK, electricityStatePanel);

		List<String> revenueNames;
		if(!(getSociety() instanceof Country)) {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", /*"Input Expense", */"Distribution Expense", 
					"Distribution Revenue", "Output Revenue");
		} else {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", /*"Input Expense", */"Output Revenue");
		}
		for(String name : revenueNames) {
			electricityRevenue.addSeries(new XYSeries(name, true, false));
		}
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Electricity Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")", 
				electricityRevenue, PlottingUtils.getCashFlowColors(revenueNames), electricityNetRevenue));
		
		List<String> electricitySourceNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				electricitySourceNames.add(society.getName() + " Production");
			}
		} else {
			electricitySourceNames.add(getSociety().getName() + " Production");
			electricitySourceNames.add("In-Distribution");
		}
		electricitySourceNames.add("Private Operations");
		for(String name : electricitySourceNames) {
			electricitySourceData.addSeries(new XYSeries(name, true, false));
		}
		addTab("Source", Icons.ELECTRICITY_SOURCE, createStackedAreaChart(
				"Electricity Source (" + electricityUnits + "/" + electricityTimeUnits + ")", 
				electricitySourceData, PlottingUtils.getResourceColors(electricitySourceNames)));
		
		List<String> electricityUseNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				electricityUseNames.add(society.getName() + " Society");
			}
			// electricityUseNames.add("Losses");
		} else {
			electricityUseNames.add(getSociety().getName()  + " Society");
			electricityUseNames.add("Out-Distribution");
		}
		electricityUseNames.add("Water Operations");
		electricityUseNames.add("Petroleum Operations");
		electricityUseNames.add("Waste");
		for(String name : electricityUseNames) {
			electricityUseData.addSeries(new XYSeries(name, true, false));
		}
		addTab("Use", Icons.ELECTRICITY_USE, createStackedAreaChart(
				"Electricity Use (" + electricityUnits + "/" + electricityTimeUnits + ")", 
				electricityUseData, PlottingUtils.getResourceColors(electricityUseNames)));
		
		List<String> oilUseNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				oilUseNames.add(society.getName() + " Operations");
			}
		} else {
			oilUseNames.add(getSociety().getName() + " Operations");
		}
		addTab("Use", Icons.PETROLEUM_USE, createStackedAreaChart(
				"Petroleum Use (" + oilUnits + "/" + oilTimeUnits + ")", 
				petroleumUseData, PlottingUtils.getResourceColors(oilUseNames)));
		
		/*
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#initialize()
	 */
	@Override
	public void initialize() {
		renewableElectricityIndicatorPanel.initialize();
		localElectricityIndicatorPanel.initialize();
		electricityRevenue.removeAllSeries();
		electricityNetRevenue.removeAllSeries();
		localElectricityData.removeAllSeries();
		renewableElectricityData.removeAllSeries();
		electricityProductCostData.removeAllSeries();
		electricitySupplyProfitData.removeAllSeries();
		electricityConsumptionPerCapita.removeAllSeries();
		electricityUseData.removeAllSeries();
		electricitySourceData.removeAllSeries();
		electricityRevenue.removeAllSeries();
		electricityNetRevenue.removeAllSeries();
		petroleumUseData.removeAllSeries();
		waterUseData.removeAllSeries();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationCompleted(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationCompleted(UpdateEvent event) {
		// nothing to do here
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationInitialized(UpdateEvent event) {
		electricityStatePanel.repaint();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		electricityStatePanel.repaint();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#update(int)
	 */
	@Override
	public void update(int year) {
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
		
		if(getNestedElectricitySystems().isEmpty()) {
			updateSeries(electricityUseData, "Society", year, 
					ElectricityUnits.convertFlow(getSociety().getSocialSystem().getElectricityConsumption(),
							getSociety().getSocialSystem(), this));
			updateSeries(petroleumUseData, "Operations", year, 
					OilUnits.convertFlow(getElectricitySystem().getPetroleumConsumptionFromPublicProduction(),
							getElectricitySystem(), this));
			updateSeries(waterUseData, "Operations", year, 
					WaterUnits.convertFlow(getElectricitySystem().getWaterConsumption(),
							getElectricitySystem(), this));
		} else {
			for(ElectricitySystem.Local nestedSystem : getNestedElectricitySystems()) {
				updateSeries(electricityUseData, nestedSystem.getSociety().getName() + " Society", year,
						ElectricityUnits.convertFlow(nestedSystem.getSociety().getSocialSystem().getElectricityConsumption(), 
								nestedSystem.getSociety().getSocialSystem(), this));
				if(nestedSystem.getPetroleumConsumption() > 0) {
					updateSeries(petroleumUseData, nestedSystem.getName(), year, 
							OilUnits.convertFlow(nestedSystem.getPetroleumConsumption(),
									nestedSystem, this));
				}
				if(nestedSystem.getWaterConsumption() > 0) {
					updateSeries(waterUseData, nestedSystem.getName(), year, 
							WaterUnits.convertFlow(nestedSystem.getWaterConsumption(),
									nestedSystem, this));
				}
			}
		}
		
		if(getElectricitySystem() instanceof LocalElectricitySystem) {
			updateSeries(electricitySourceData, "Production", year, 
					ElectricityUnits.convertFlow(
							getElectricitySystem().getElectricityProduction(), 
							getElectricitySystem(), this));
			updateSeries(electricityUseData, "Distribution", year, 
					ElectricityUnits.convertFlow(
							getElectricitySystem().getElectricityOutDistribution(), 
							getElectricitySystem(), this));
			updateSeries(electricitySourceData,  "Distribution", year, 
					ElectricityUnits.convertFlow(
							getElectricitySystem().getElectricityInDistribution(), 
							getElectricitySystem(), this));
		} else {
			for(ElectricitySystem.Local nestedSystem : getNestedElectricitySystems()) {
				updateSeries(electricitySourceData, nestedSystem.getSociety().getName() 
						+ " Production", year,
						ElectricityUnits.convertFlow(nestedSystem.getElectricityProduction(), 
								nestedSystem, this));
			}
			/*updateSeries(electricitySourceData, "Production", year, 
					ElectricityUnits.convertFlow(
							getElectricitySystem().getElectricityProduction(),
							getElectricitySystem(), this));*/

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
			/*updateSeries(electricityUseData, "Distribution Losses", year, 
					ElectricityUnits.convertFlow(
							getElectricitySystem().getElectricityOutDistributionLosses(),
							getElectricitySystem(), this));*/
		}
		
		updateSeries(electricityRevenue, "Capital Expense", year, 
				CurrencyUnits.convertFlow(
						-getElectricitySystem().getCapitalExpense(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Operations Expense", year, 
				CurrencyUnits.convertFlow(
						-getElectricitySystem().getOperationsExpense()
						-getElectricitySystem().getConsumptionExpense(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Decommission Expense", year, 
				CurrencyUnits.convertFlow(
						-getElectricitySystem().getDecommissionExpense(), 
						getElectricitySystem(), this));
		/*updateSeries(electricityRevenue, "Consumption Expense", year, 
				CurrencyUnits.convertFlow(
						-getElectricitySystem().getConsumptionExpense(), 
						getElectricitySystem(), this));*/
		if(!(getElectricitySystem().getSociety() instanceof Country)) {
			updateSeries(electricityRevenue, "Distribution Expense", year, 
					CurrencyUnits.convertFlow(
							-getElectricitySystem().getDistributionExpense(), 
							getElectricitySystem(), this));
			updateSeries(electricityRevenue, "Distribution Revenue", year, 
					CurrencyUnits.convertFlow(
							getElectricitySystem().getDistributionRevenue(), 
							getElectricitySystem(), this));
		}
		updateSeries(electricityRevenue, "Output Revenue", year, 
				CurrencyUnits.convertFlow(
						getElectricitySystem().getSalesRevenue(), 
						getElectricitySystem(), this));
		updateSeries(electricityNetRevenue, "Net Cash Flow", year, 
				CurrencyUnits.convertFlow(
						getElectricitySystem().getCashFlow(), 
						getElectricitySystem(), this));
		updateSeries(electricityUseData, "Water Operations", year,  
				ElectricityUnits.convertFlow(
						getSociety().getWaterSystem().getElectricityConsumption(),
						getSociety().getWaterSystem(), this));
		updateSeries(electricityUseData, "Petroleum Operations", year,  
				ElectricityUnits.convertFlow(
						getSociety().getPetroleumSystem().getElectricityConsumption(),
						getSociety().getPetroleumSystem(), this));
		updateSeries(electricityUseData, "Waste", year,  
				ElectricityUnits.convertFlow(
						getElectricitySystem().getElectricityWasted(),
						getElectricitySystem(), this));
		updateSeries(electricitySourceData, "Private Operations", year, 
				ElectricityUnits.convertFlow(
						getElectricitySystem().getElectricityFromPrivateProduction(),
						getElectricitySystem(), this));
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
