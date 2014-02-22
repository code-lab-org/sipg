package edu.mit.sips.gui.water;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.water.LocalWaterSoS;
import edu.mit.sips.core.water.LocalWaterSystem;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.gui.LinearIndicatorPanel;
import edu.mit.sips.gui.PlottingUtils;
import edu.mit.sips.gui.SpatialStatePanel;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Class LocalWaterSystemPanel.
 */
public class LocalWaterSystemPanel extends WaterSystemPanel
implements CurrencyUnitsOutput, WaterUnitsOutput, ElectricityUnitsOutput {	
	private static final long serialVersionUID = -3665986046863585665L;
	
	private final LinearIndicatorPanel localWaterIndicatorPanel, 
	waterAquiferIndicatorPanel, renewableWaterIndicatorPanel;
	private final List<LocalWaterSystemPanel> nestedPanels = 
			new ArrayList<LocalWaterSystemPanel>();
	
	private final SpatialStatePanel waterStatePanel;

	DefaultTableXYDataset waterSourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterAquiferDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset cashFlow = new DefaultTableXYDataset();
	DefaultTableXYDataset netCashFlow = new DefaultTableXYDataset();
	DefaultTableXYDataset cumulativeBalance = new DefaultTableXYDataset();
	
	DefaultTableXYDataset capitalExpense = new DefaultTableXYDataset();
	DefaultTableXYDataset capitalExpenseTotal = new DefaultTableXYDataset();
	DefaultTableXYDataset cumulativeCapitalExpense = new DefaultTableXYDataset();
	
	TimeSeriesCollection localWaterData = new TimeSeriesCollection();
	TimeSeriesCollection waterProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection waterSupplyProfitData = new TimeSeriesCollection();
	TimeSeriesCollection renewableWaterData = new TimeSeriesCollection();
	TimeSeriesCollection waterConsumptionPerCapita = new TimeSeriesCollection();
	
	private final CurrencyUnits currencyUnits = CurrencyUnits.Bsim;
	private final TimeUnits currencyTimeUnits = TimeUnits.year;
	private final ElectricityUnits electricityUnits = ElectricityUnits.TWh;
	private final TimeUnits electricityTimeUnits = TimeUnits.year;
	private final WaterUnits waterUnits = WaterUnits.km3;
	private final TimeUnits waterTimeUnits = TimeUnits.year;

	/**
	 * Instantiates a new local water system panel.
	 *
	 * @param waterSystem the water system
	 */
	public LocalWaterSystemPanel(WaterSystem.Local waterSystem) {
		super(waterSystem);
		
		JPanel indicatorsPanel = new JPanel();
		indicatorsPanel.setLayout(
				new BoxLayout(indicatorsPanel, BoxLayout.LINE_AXIS));
		localWaterIndicatorPanel = new LinearIndicatorPanel(
				"Water Independence", 0, 1);
		indicatorsPanel.add(localWaterIndicatorPanel);
		waterAquiferIndicatorPanel = new LinearIndicatorPanel(
				"Water Aquifer", 0, WaterUnits.convertStock(
						waterSystem.getMaxWaterReservoirVolume(), 
						waterSystem, this));
		indicatorsPanel.add(waterAquiferIndicatorPanel);
		renewableWaterIndicatorPanel = new LinearIndicatorPanel(
				"Renewable Water", 0, 1);
		indicatorsPanel.add(renewableWaterIndicatorPanel);
		// addTab("Indicators", Icons.INDICATORS, indicatorsPanel);
		
		List<String> revenueNames;
		if(!(getSociety() instanceof Country)) {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", /*"Input Expense", */"Distribution Expense", 
					"Import Expense", "Distribution Revenue", /*"Export Revenue", */
					"Domestic Revenue");
		} else {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", /*"Input Expense", */"Import Expense", 
					/*"Export Revenue",*/ "Domestic Revenue");
		}
		for(String name : revenueNames) {
			cashFlow.addSeries(new XYSeries(name, true, false));
		}
		
		JTabbedPane nationalData;
		if(waterSystem instanceof LocalWaterSoS) {
			nationalData = new JTabbedPane();
			addTab(getSociety().getName(), Icons.COUNTRY, nationalData);
		} else {
			nationalData = this;
		}
		
		if(getWaterSystem() instanceof LocalWaterSoS) {
			nationalData.addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
					getWaterSystem().getName() + " Net Revenue",
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
			nationalData.addTab("Cash Flow", Icons.REVENUE, createStackedAreaChart(
					getWaterSystem().getName() + " Cash Flow",
					"Annual Cash Flow (" + currencyUnits + "/" + currencyTimeUnits + ")", cashFlow, 
							PlottingUtils.getCashFlowColors(revenueNames), netCashFlow));
		}
		
		List<String> waterSourceNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				waterSourceNames.add(society.getName());
			}
		} else {
			waterSourceNames.add(getSociety().getName() + " Production");
			waterSourceNames.add("Private Operations");
			waterSourceNames.add("In-Distribution");
		}
		waterSourceNames.add("Import");
		for(String name : waterSourceNames) {
			waterSourceData.addSeries(new XYSeries(name, true, false));
		}
		nationalData.addTab("Source", Icons.WATER_SOURCE, createStackedAreaChart(
				getWaterSystem().getName() + " Source",
				"Water Source (" + waterUnits + "/" + waterTimeUnits + ")", 
				waterSourceData, PlottingUtils.getResourceColors(waterSourceNames)));
		
		List<String> waterUseNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				waterUseNames.add(society.getName());
			}
			// waterUseNames.add("Losses");
		} else {
			waterUseNames.add(getSociety().getName()  + " Society");
			waterUseNames.add("Agriculture Operations");
			waterUseNames.add("Out-Distribution");
		}
		//waterUseNames.add("Electricity Operations");
		for(String name : waterUseNames) {
			waterUseData.addSeries(new XYSeries(name, true, false));
		}
		nationalData.addTab("Use", Icons.WATER_USE, createStackedAreaChart(
				getWaterSystem().getName() + " Use",
				"Water Use (" + waterUnits + "/" + waterTimeUnits + ")", 
				waterUseData, PlottingUtils.getResourceColors(waterUseNames)));
		
		List<String> electricityUseNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				electricityUseNames.add(society.getName() + " Operations");
			}
		} else {
			electricityUseNames.add(getSociety().getName() + " Operations");
		}
		electricityUseNames.add("Private Operations");
		nationalData.addTab("Use", Icons.ELECTRICITY_USE, createStackedAreaChart(
				getWaterSystem().getName() + " Electricity Use",
				"Electricity Use (" + electricityUnits + "/" + electricityTimeUnits + ")",
				electricityUseData, PlottingUtils.getResourceColors(electricityUseNames)));

		/* temporarily removed
		addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Water Use Fraction (-)", localWaterData)); */
		/* temporarily removed
		addTab("Renewable", Icons.RENEWABLE, createTimeSeriesChart(
				"Renewable Water Fraction (-)", 
				renewableWaterData));
		addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Water Consumption per Capita (" + WaterUnits.L 
				+ "/" + TimeUnits.day + ")", 
				waterConsumptionPerCapita));*/
		List<Color> societyColors = new ArrayList<Color>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				societyColors.add(PlottingUtils.getSocietyColor(society));
			}
		} else {
			societyColors.add(PlottingUtils.getSocietyColor(getSociety()));
		}
		nationalData.addTab("Aquifer", Icons.WATER_RESERVOIR, createStackedAreaChart(
				getWaterSystem().getName() + " Aquifer",
				"Water Aquifer Volume (" + waterUnits + ")", 
				waterAquiferDataset, societyColors.toArray(new Color[0])));
		/* TODO
		addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
				"Unit Production Cost (SAR/m^3)", 
				waterProductCostData));
		addTab("Supply Profit", Icons.COST_SUPPLY, createTimeSeriesChart(
				"Unit Supply Profit (SAR/m^3)", 
				waterSupplyProfitData));
		*/
		
		waterStatePanel = new SpatialStatePanel(
				waterSystem.getSociety(), new WaterStateProvider());
		nationalData.addTab("Network", Icons.NETWORK, waterStatePanel);
		
		if(waterSystem instanceof LocalWaterSoS) {
			JTabbedPane regionalData = this; // new JTabbedPane();
			for(WaterSystem.Local nestedSystem : 
				((LocalWaterSoS) waterSystem).getNestedSystems()) {
				LocalWaterSystemPanel nestedPanel = 
						new LocalWaterSystemPanel(nestedSystem);
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
	
	/**
	 * Gets the water system.
	 *
	 * @return the water system
	 */
	public WaterSystem.Local getWaterSystem() {
		return (WaterSystem.Local) getInfrastructureSystem();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsDenominator()
	 */
	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnits()
	 */
	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}

	private void initialize() {
		localWaterData.removeAllSeries();
		renewableWaterData.removeAllSeries();
		waterProductCostData.removeAllSeries();
		waterSupplyProfitData.removeAllSeries();
		waterConsumptionPerCapita.removeAllSeries();
		waterAquiferDataset.removeAllSeries();
		waterUseData.removeAllSeries();
		waterSourceData.removeAllSeries();
		electricityUseData.removeAllSeries();
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
		for(LocalWaterSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationCompleted(event);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationInitialized(UpdateEvent event) {
		initialize();
		for(LocalWaterSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationInitialized(event);
		}
		waterStatePanel.repaint();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		update((int)event.getTime());
		for(LocalWaterSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationUpdated(event);
		}
		waterStatePanel.repaint();
	}

	private void update(int year) {
		/* temporarily removed
		updateSeriesCollection(localWaterData, getWaterSystem().getSociety().getName(),
				year, getWaterSystem().getLocalWaterFraction());
		localWaterIndicatorPanel.setValue(getWaterSystem().getLocalWaterFraction());
		for(WaterSystem.Local nestedSystem : getNestedWaterSystems()) {
			updateSeriesCollection(localWaterData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getLocalWaterFraction());
		}

		updateSeriesCollection(renewableWaterData, getSociety().getName(), 
				year, getWaterSystem().getRenewableWaterFraction());
		renewableWaterIndicatorPanel.setValue(getWaterSystem().getRenewableWaterFraction());
		for(WaterSystem.Local nestedSystem : getNestedWaterSystems()) {
				updateSeriesCollection(renewableWaterData, nestedSystem.getSociety().getName(), 
						year, nestedSystem.getRenewableWaterFraction());
		}

		updateSeriesCollection(waterProductCostData, getSociety().getName(), 
				year, getWaterSystem().getUnitProductionCost());
		for(WaterSystem.Local nestedSystem : getNestedWaterSystems()) {
			updateSeriesCollection(waterProductCostData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getUnitProductionCost());
		}
		
		updateSeriesCollection(waterSupplyProfitData, getSociety().getName(), 
				year, getWaterSystem().getUnitSupplyProfit());
		for(WaterSystem.Local nestedSystem : getNestedWaterSystems()) {
			updateSeriesCollection(waterSupplyProfitData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getUnitSupplyProfit());
		}

		if(getSociety().getSocialSystem().getPopulation() > 0) {
			updateSeriesCollection(waterConsumptionPerCapita, getSociety().getName(), year, 
					DefaultUnits.convertFlow(getSociety().getSocialSystem().getWaterConsumption(),
							getSociety().getSocialSystem().getWaterUnits(),
							getSociety().getSocialSystem().getWaterTimeUnits(),
							WaterUnits.L, TimeUnits.day)
							/ getSociety().getSocialSystem().getPopulation());
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				if(nestedSociety.getSocialSystem().getPopulation() > 0) {
					updateSeriesCollection(waterConsumptionPerCapita, nestedSociety.getName(), year, 
							DefaultUnits.convertFlow(nestedSociety.getSocialSystem().getWaterConsumption(),
									nestedSociety.getSocialSystem().getWaterUnits(),
									nestedSociety.getSocialSystem().getWaterTimeUnits(),
									WaterUnits.L, TimeUnits.day)
									/ nestedSociety.getSocialSystem().getPopulation());
				}
			}
		}

		waterAquiferIndicatorPanel.setValue(WaterUnits.convertStock(
				getWaterSystem().getWaterReservoirVolume(), getWaterSystem(), this));
		*/
		
		updateSeries(cashFlow, "Capital Expense", year, 
				CurrencyUnits.convertFlow(
						-getWaterSystem().getCapitalExpense(), 
						getWaterSystem(), this));
		updateSeries(cashFlow, "Operations Expense", year, 
				CurrencyUnits.convertFlow(
						-getWaterSystem().getOperationsExpense()
						-getWaterSystem().getConsumptionExpense(), 
						getWaterSystem(), this));
		updateSeries(cashFlow, "Decommission Expense", year, 
				CurrencyUnits.convertFlow(
						-getWaterSystem().getDecommissionExpense(), 
						getWaterSystem(), this));
		/*updateSeries(waterRevenue, "Consumption", year, 
				WaterUnits.convertFlow(
						-getWaterSystem().getConsumptionExpense(), 
						getWaterSystem(), this));*/
		if(!(getWaterSystem().getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Expense", year, 
					CurrencyUnits.convertFlow(
							-getWaterSystem().getDistributionExpense(), 
							getWaterSystem(), this));
		}
		updateSeries(cashFlow, "Import Expense", year, 
				CurrencyUnits.convertFlow(
						-getWaterSystem().getImportExpense(), 
						getWaterSystem(), this));
		if(!(getWaterSystem().getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Revenue", year, 
					CurrencyUnits.convertFlow(
							-getWaterSystem().getDistributionExpense(), 
							getWaterSystem(), this));
		}
		updateSeries(cashFlow, "Domestic Revenue", year, 
				CurrencyUnits.convertFlow(
						getWaterSystem().getSalesRevenue(), 
						getWaterSystem(), this));
		updateSeries(netCashFlow, "Net Cash Flow", year, 
				CurrencyUnits.convertFlow(getWaterSystem().getCashFlow(),
						getWaterSystem(), this));
		updateSeries(cumulativeBalance, "Cumulative Balance", year, 
				CurrencyUnits.convertFlow(getWaterSystem().getCumulativeCashFlow(),
						getWaterSystem(), this));
		
		if(getWaterSystem() instanceof LocalWaterSystem) {
			updateSeries(waterSourceData, "Production", year, 
					WaterUnits.convertFlow(getWaterSystem().getWaterProduction(), 
							getWaterSystem(), this));
			updateSeries(waterSourceData, "Private Operations", year, 
					WaterUnits.convertFlow(getWaterSystem().getWaterFromPrivateProduction(), 
							getWaterSystem(), this));
			updateSeries(waterSourceData, "Distribution", year, 
					WaterUnits.convertFlow(getWaterSystem().getWaterInDistribution(), 
							getWaterSystem(), this));
			updateSeries(waterUseData, "Society", year, 
					WaterUnits.convertFlow(getSociety().getSocialSystem().getWaterConsumption(),
							getSociety().getSocialSystem(), this));
			updateSeries(waterUseData, "Agriculture Operations", year, 
					WaterUnits.convertFlow(getSociety().getAgricultureSystem().getWaterConsumption(), 
							getSociety().getAgricultureSystem(), this));
			updateSeries(waterUseData, "Distribution", year,
					WaterUnits.convertFlow(getWaterSystem().getWaterOutDistribution(), 
							getWaterSystem(), this));
			updateSeries(waterAquiferDataset, "Aquifer", year, 
					WaterUnits.convertStock(getWaterSystem().getWaterReservoirVolume(), 
							getWaterSystem(), this));
			updateSeries(electricityUseData, "Operations", year, 
					ElectricityUnits.convertFlow(getWaterSystem().getElectricityConsumptionFromPublicProduction(),
							getWaterSystem(), this));
			updateSeries(electricityUseData, "Private Operations", year, 
					ElectricityUnits.convertFlow(getWaterSystem().getElectricityConsumptionFromPrivateProduction(),
							getWaterSystem(), this));
		} else {
			for(WaterSystem.Local nestedSystem : getNestedWaterSystems()) {
					updateSeries(waterSourceData, nestedSystem.getSociety().getName(), year,
							WaterUnits.convertFlow(nestedSystem.getTotalWaterSupply()
									+ nestedSystem.getWaterOutDistribution()
									- nestedSystem.getWaterInDistribution()
									+ nestedSystem.getWaterFromPrivateProduction()
									- nestedSystem.getWaterImport(), nestedSystem, this));
					updateSeries(waterUseData, nestedSystem.getSociety().getName(), year,
							WaterUnits.convertFlow(nestedSystem.getSociety().getTotalWaterDemand(), 
									nestedSystem.getSociety(), this));
					updateSeries(electricityUseData, nestedSystem.getSociety().getName(), year, 
							ElectricityUnits.convertFlow(nestedSystem.getElectricityConsumption(),
									nestedSystem, this));
			}
			/*updateSeries(waterSourceData, "Production", year, 
					WaterUnits.convertFlow(getWaterSystem().getWaterProduction(), 
							getWaterSystem(), this));*/

			/*if(!getWaterSystem().getExternalElements().isEmpty()) {
				updateSeries(waterUseData, "Distribution", year, 
						WaterUnits.convertFlow(getWaterSystem().getWaterOutDistribution(), 
								getWaterSystem(), this));
				updateSeries(waterSourceData, "Distribution", year, 
						WaterUnits.convertFlow(getWaterSystem().getWaterInDistribution(),
								getWaterSystem(), this));
			}*/
			for(WaterSystem.Local nestedSystem : getNestedWaterSystems()) {
				updateSeries(waterAquiferDataset, nestedSystem.getSociety().getName(), year, 
						WaterUnits.convertFlow(nestedSystem.getWaterReservoirVolume(), 
								getWaterSystem(), this));
			}
		}
		updateSeries(waterSourceData, "Import", year, 
				WaterUnits.convertFlow(getWaterSystem().getWaterImport(), 
						getWaterSystem(), this));

		/*updateSeries(electricityUseData, "Private Operations", year, 
				ElectricityUnits.convertFlow(getWaterSystem().getElectricityConsumptionFromPrivateProduction(),
						getWaterSystem(), this));
		updateSeries(waterUseData, "Electricity Operations", year, 
				WaterUnits.convertFlow(getSociety().getElectricitySystem().getWaterConsumption(), 
						getSociety().getElectricitySystem(), this));*/
		
		if(getWaterSystem() instanceof LocalWaterSoS) {
			for(InfrastructureSystem nestedSystem : getNestedWaterSystems()) {
				updateSeries(capitalExpense, nestedSystem.getName(), year, 
						CurrencyUnits.convertFlow(nestedSystem.getCapitalExpense(), 
								nestedSystem, this));
			}
			updateSeries(capitalExpenseTotal, "Total Investment", year, 
					CurrencyUnits.convertFlow(
							((LocalWaterSoS)getWaterSystem())
							.getCapitalExpense(), getSociety(), this));
			updateSeries(cumulativeCapitalExpense, "Cumulative Investment", year, 
					CurrencyUnits.convertFlow(
							((LocalWaterSoS)getWaterSystem())
							.getCumulativeCapitalExpense(), getSociety(), this));
		}
	}
}
