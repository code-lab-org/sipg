package edu.mit.sips.gui.petroleum;

import java.awt.Color;
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
import edu.mit.sips.core.Society;
import edu.mit.sips.core.base.InfrastructureSystem;
import edu.mit.sips.core.petroleum.LocalPetroleumSoS;
import edu.mit.sips.core.petroleum.PetroleumSystem;
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

/**
 * The Class PetroleumSystemPanel.
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
	
	private final LinearIndicatorPanel petroleumReservoirIndicatorPanel, 
	localPetroleumIndicatorPanel;
	private final List<LocalPetroleumSystemPanel> nestedPanels = 
			new ArrayList<LocalPetroleumSystemPanel>();
	
	private final SpatialStatePanel petroleumStatePanel;

	TimeSeriesCollection localPetroleumData = new TimeSeriesCollection();
	TimeSeriesCollection petroleumProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection petroleumSupplyProfitData = new TimeSeriesCollection();
	TimeSeriesCollection petroleumConsumptionPerCapita = new TimeSeriesCollection();
	
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
	
	DefaultTableXYDataset capitalExpense = new DefaultTableXYDataset();
	DefaultTableXYDataset capitalExpenseTotal = new DefaultTableXYDataset();
	DefaultTableXYDataset cumulativeCapitalExpense = new DefaultTableXYDataset();

	/**
	 * Instantiates a new local petroleum system panel.
	 *
	 * @param petroleumSystem the petroleum system
	 */
	public LocalPetroleumSystemPanel(PetroleumSystem.Local petroleumSystem) {
		super(petroleumSystem);

		JPanel indicatorsPanel = new JPanel();
		indicatorsPanel.setLayout(
				new BoxLayout(indicatorsPanel, BoxLayout.LINE_AXIS));
		petroleumReservoirIndicatorPanel = new LinearIndicatorPanel(
				"Oil Reservoir", 0, OilUnits.convertStock(
						petroleumSystem.getMaxPetroleumReservoirVolume(), 
						petroleumSystem, this));
		localPetroleumIndicatorPanel = new LinearIndicatorPanel(
				"Oil Independence", 0, 1);
		indicatorsPanel.add(petroleumReservoirIndicatorPanel);
		indicatorsPanel.add(localPetroleumIndicatorPanel);
		// addTab("Indicators", Icons.INDICATORS, indicatorsPanel);
		
		List<String> revenueNames;
		if(!(getSociety() instanceof Country)) {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", /*"Input Expense", */"Distribution Expense", 
					"Import Expense", "Distribution Revenue", "Export Revenue", 
					"Domestic Revenue");
		} else {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", /*"Input Expense", */"Import Expense", 
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
		
		if(getPetroleumSystem() instanceof LocalPetroleumSoS) {
			nationalPane.addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
					getPetroleumSystem().getName() + " Net Revenue",
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
					getPetroleumSystem().getName() + " Net Revenue",
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
		if(getPetroleumSystem() instanceof LocalPetroleumSoS) {
			nationalPane.addTab("Source", Icons.PETROLEUM_SOURCE, createToggleableStackedAreaChart(
					getPetroleumSystem().getName() + " Oil Source",
					"Petroleum Source (" + oilUnits + "/" + oilTimeUnits + ")",
					petroleumSourceAggregatedData, PlottingUtils.getResourceColors(oilSourceAggregatedNames),
					petroleumSourceDisaggregatedData, PlottingUtils.getResourceColors(oilSourceDisaggregatedNames)));
		} else {
			nationalPane.addTab("Source", Icons.PETROLEUM_SOURCE, createStackedAreaChart(
					getPetroleumSystem().getName() + " Oil Source",
					"Petroleum Source (" + oilUnits + "/" + oilTimeUnits + ")",
					petroleumSourceAggregatedData, PlottingUtils.getResourceColors(oilSourceAggregatedNames)));
		}
		List<String> oilUseAggregatedNames = new ArrayList<String>();
		List<String> oilUseDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				oilUseDisaggregatedNames.add(society.getName());
			}
			// oilUseNames.add("Losses");
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
		if(getPetroleumSystem() instanceof LocalPetroleumSoS) {
			nationalPane.addTab("Use", Icons.PETROLEUM_USE, createToggleableStackedAreaChart(
					getPetroleumSystem().getName() + " Oil Use",
					"Petroleum Use (" + oilUnits + "/" + oilTimeUnits + ")", 
					petroleumUseAggregatedData, PlottingUtils.getResourceColors(oilUseAggregatedNames), 
					petroleumUseDisaggregatedData, PlottingUtils.getResourceColors(oilUseDisaggregatedNames)));
		} else {
			nationalPane.addTab("Use", Icons.PETROLEUM_USE, createStackedAreaChart(
					getPetroleumSystem().getName() + " Oil Use",
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
		/* if(getPetroleumSystem() instanceof LocalPetroleumSoS) {
			addTab("Use", Icons.ELECTRICITY_USE, createToggleableStackedAreaChart(
					getPetroleumSystem().getName() + " Electricity Use",
					"Electricity Use (" + electricityUnits + "/" + electricityTimeUnits + ")",
					electricityUseAggregatedData, PlottingUtils.getResourceColors(electricityUseAggregatedNames),
					electricityUseDisaggregatedData, PlottingUtils.getResourceColors(electricityUseDisaggregatedNames)));
		} else {
			addTab("Use", Icons.ELECTRICITY_USE, createStackedAreaChart(
					getPetroleumSystem().getName() + " Electricity Use",
					"Electricity Use (" + electricityUnits + "/" + electricityTimeUnits + ")",
					electricityUseAggregatedData, PlottingUtils.getResourceColors(electricityUseAggregatedNames)));
		}*/

		/* addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Petroleum Use Fraction (-)", 
				localPetroleumData));
		addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Oil Consumption per Capita (" + OilUnits.toe 
				+ "/" + TimeUnits.year + ")", 
				petroleumConsumptionPerCapita)); */
		List<Color> societyColors = new ArrayList<Color>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				societyColors.add(PlottingUtils.getSocietyColor(society));
			}
		} else {
			societyColors.add(PlottingUtils.getSocietyColor(getSociety()));
		}
		nationalPane.addTab("Reservoir", Icons.PETROLEUM_RESERVOIR, createStackedAreaChart(
				getPetroleumSystem().getName() + " Oil Reservoir",
				"Oil Reservoir Volume (" + oilUnits + ")",
				petroleumReservoirDataset, societyColors.toArray(new Color[0])), "Reservoir");
		/* TODO
		addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
				"Unit Production Cost (SAR/toe)", 
				petroleumProductCostData));
		addTab("Supply Profit", Icons.COST_SUPPLY, createTimeSeriesChart(
				"Unit Supply Profit (SAR/toe)", 
				petroleumSupplyProfitData));
		*/

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
	
	/**
	 * Gets the petroleum system.
	 *
	 * @return the petroleum system
	 */
	public PetroleumSystem.Local getPetroleumSystem() {
		return (PetroleumSystem.Local) getInfrastructureSystem();
	}
	
	private void initialize() {
		petroleumReservoirIndicatorPanel.initialize();
		localPetroleumIndicatorPanel.initialize();
		cashFlow.removeAllSeries();
		netCashFlow.removeAllSeries();
		localPetroleumData.removeAllSeries();
		petroleumProductCostData.removeAllSeries();
		petroleumSupplyProfitData.removeAllSeries();
		petroleumReservoirDataset.removeAllSeries();
		petroleumUseAggregatedData.removeAllSeries();
		petroleumUseDisaggregatedData.removeAllSeries();
		petroleumSourceAggregatedData.removeAllSeries();
		petroleumSourceDisaggregatedData.removeAllSeries();
		electricityUseAggregatedData.removeAllSeries();
		electricityUseDisaggregatedData.removeAllSeries();
		petroleumConsumptionPerCapita.removeAllSeries();
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
		for(LocalPetroleumSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationCompleted(event);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationInitialized(UpdateEvent event) {
		initialize();
		for(LocalPetroleumSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationInitialized(event);
		}
		petroleumStatePanel.repaint();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		update((int)event.getTime());
		for(LocalPetroleumSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationUpdated(event);
		}
		petroleumStatePanel.repaint();
	}

	private void update(int year) {
		/* temporarily removed
		updateSeriesCollection(petroleumProductCostData, 
				getSociety().getName(), year, 
				getPetroleumSystem().getUnitProductionCost());
		for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
			updateSeriesCollection(petroleumProductCostData, 
					nestedSystem.getSociety().getName(), year, 
					nestedSystem.getUnitProductionCost());
		}
		
		updateSeriesCollection(petroleumSupplyProfitData, getSociety().getName(), year, 
				getPetroleumSystem().getUnitSupplyProfit());
		for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
			updateSeriesCollection(petroleumSupplyProfitData, 
					nestedSystem.getSociety().getName(), year, 
					nestedSystem.getUnitSupplyProfit());
		}
		

		if(getSociety().getSocialSystem().getPopulation() > 0) {
			updateSeriesCollection(petroleumConsumptionPerCapita, getSociety().getName(), year, 
					DefaultUnits.convertFlow(getSociety().getSocialSystem().getPetroleumConsumption(),
							getSociety().getSocialSystem().getOilUnits(),
							getSociety().getSocialSystem().getOilTimeUnits(),
							OilUnits.toe, TimeUnits.year)
							/ getSociety().getSocialSystem().getPopulation());
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				if(nestedSociety.getSocialSystem().getPopulation() > 0) {
					updateSeriesCollection(petroleumConsumptionPerCapita, nestedSociety.getName(), year, 
							DefaultUnits.convertFlow(nestedSociety.getSocialSystem().getPetroleumConsumption(),
									nestedSociety.getSocialSystem().getOilUnits(),
									nestedSociety.getSocialSystem().getOilTimeUnits(),
									OilUnits.toe, TimeUnits.year)
									/ nestedSociety.getSocialSystem().getPopulation());
				}
			}
		}
		
		petroleumReservoirIndicatorPanel.setValue(OilUnits.convertStock(
				getPetroleumSystem().getPetroleumReservoirVolume(), 
				getPetroleumSystem(), this));
		
		updateSeriesCollection(localPetroleumData, getSociety().getName(), year, 
				getPetroleumSystem().getLocalPetroleumFraction());
		localPetroleumIndicatorPanel.setValue(
				getPetroleumSystem().getLocalPetroleumFraction());
		for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
			updateSeriesCollection(localPetroleumData, 
					nestedSystem.getSociety().getName(), year, 
					nestedSystem.getLocalPetroleumFraction());
		}
		*/

		updateSeries(cashFlow, "Capital Expense", year, 
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getCapitalExpense(),
						getPetroleumSystem(), this));
		updateSeries(cashFlow, "Operations Expense", year,  
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getOperationsExpense()
						- getPetroleumSystem().getConsumptionExpense(),
						getPetroleumSystem(), this));
		updateSeries(cashFlow, "Decommission Expense", year,  
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getDecommissionExpense(),
						getPetroleumSystem(), this));
		/*updateSeries(petroleumRevenue, "Consumption", year, 
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getConsumptionExpense(),
						getPetroleumSystem(), this));*/
		if(!(getPetroleumSystem().getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Expense", year,  
					CurrencyUnits.convertFlow(
							-getPetroleumSystem().getDistributionExpense(),
							getPetroleumSystem(), this));
		}
		updateSeries(cashFlow, "Import Expense", year,  
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getImportExpense(),
						getPetroleumSystem(), this));
		if(!(getPetroleumSystem().getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Revenue", year, 
					CurrencyUnits.convertFlow( 
							getPetroleumSystem().getDistributionRevenue(),
							getPetroleumSystem(), this));
		}
		updateSeries(cashFlow, "Export Revenue", year, 
				CurrencyUnits.convertFlow(
						getPetroleumSystem().getExportRevenue(),
						getPetroleumSystem(), this));
		updateSeries(cashFlow, "Domestic Revenue", year,  
				CurrencyUnits.convertFlow(
						getPetroleumSystem().getSalesRevenue(),
						getPetroleumSystem(), this));
		updateSeries(netCashFlow, "Net Revenue", year, 
				CurrencyUnits.convertFlow(getPetroleumSystem().getCashFlow(),
						getPetroleumSystem(), this));
		updateSeries(cumulativeBalance, "Cumulative Net Revenue", year, 
				CurrencyUnits.convertFlow(getPetroleumSystem().getCumulativeCashFlow(),
						getPetroleumSystem(), this));

		updateSeries(petroleumSourceAggregatedData, "Production", year, 
				OilUnits.convertFlow(getPetroleumSystem().getPetroleumProduction(), 
						getPetroleumSystem(), this));
		updateSeries(petroleumUseAggregatedData, "Society", year, 
				OilUnits.convertFlow(getSociety().getSocialSystem().getPetroleumConsumption(),
						getSociety().getSocialSystem(), this));
		updateSeries(petroleumUseAggregatedData, "Electricity Operations", year, 
				OilUnits.convertFlow(
						getSociety().getElectricitySystem().getPetroleumConsumption(),
						getSociety().getElectricitySystem(), this));
		updateSeries(electricityUseAggregatedData, "Operations", year, 
				ElectricityUnits.convertFlow(getPetroleumSystem().getElectricityConsumption(),
						getPetroleumSystem(), this));
		if(getPetroleumSystem() instanceof LocalPetroleumSoS) {
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
			/*updateSeries(petroleumSourceData, "Production", year, 
					OilUnits.convertStock(
							getPetroleumSystem().getPetroleumProduction(),
							getPetroleumSystem(), this));*/
			/*if(!getPetroleumSystem().getExternalElements().isEmpty()) {
				updateSeries(petroleumSourceData, "Distribution", year, 
						OilUnits.convertStock(
								getPetroleumSystem().getPetroleumInDistribution(),
								getPetroleumSystem(), this));
				updateSeries(petroleumUseData, "Distribution", year, 
						OilUnits.convertStock(
								getPetroleumSystem().getPetroleumOutDistribution(),
								getPetroleumSystem(), this));
			}*/
			/*updateSeries(petroleumUseData, "Distribution Losses", year, 
					OilUnits.convertStock(
							getPetroleumSystem().getPetroleumOutDistributionLosses(),
							getPetroleumSystem(), this));*/
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(petroleumReservoirDataset, nestedSociety.getName(), year, 
						OilUnits.convertStock(((PetroleumSystem.Local)nestedSociety.
								getPetroleumSystem()).getReservoirVolume(),
								nestedSociety.getPetroleumSystem(), this));
			}
		} else {
			updateSeries(petroleumReservoirDataset, "Reservoir", year, 
					OilUnits.convertStock(
							getPetroleumSystem().getReservoirVolume(), 
							getPetroleumSystem(), this));
			updateSeries(petroleumSourceAggregatedData, "Distribution", year, 
					OilUnits.convertFlow(getPetroleumSystem().getPetroleumInDistribution(),
							getPetroleumSystem(), this));
			updateSeries(petroleumUseAggregatedData, "Distribution", year, 
					OilUnits.convertFlow(getPetroleumSystem().getPetroleumOutDistribution(),
							getPetroleumSystem(), this));
		}
		
		updateSeries(petroleumSourceAggregatedData, "Import", year, 
				OilUnits.convertFlow(
						getPetroleumSystem().getPetroleumImport(),
						getPetroleumSystem(), this));
		updateSeries(petroleumSourceDisaggregatedData, "Import", year, 
				OilUnits.convertFlow(
						getPetroleumSystem().getPetroleumImport(),
						getPetroleumSystem(), this));
		updateSeries(petroleumUseAggregatedData, "Export", year, 
				OilUnits.convertFlow(
						getPetroleumSystem().getPetroleumExport(),
						getPetroleumSystem(), this));
		updateSeries(petroleumUseDisaggregatedData, "Export", year, 
				OilUnits.convertFlow(
						getPetroleumSystem().getPetroleumExport(),
						getPetroleumSystem(), this));

		if(getPetroleumSystem() instanceof LocalPetroleumSoS) {
			for(InfrastructureSystem nestedSystem : getNestedPetroleumSystems()) {
				updateSeries(capitalExpense, nestedSystem.getName(), year, 
						CurrencyUnits.convertFlow(nestedSystem.getCapitalExpense(), 
								nestedSystem, this));
			}
			updateSeries(capitalExpenseTotal, "Total Investment", year, 
					CurrencyUnits.convertFlow(
							((LocalPetroleumSoS)getPetroleumSystem())
							.getCapitalExpense(), getSociety(), this));
			updateSeries(cumulativeCapitalExpense, "Cumulative Investment", year, 
					CurrencyUnits.convertFlow(
							((LocalPetroleumSoS)getPetroleumSystem())
							.getCumulativeCapitalExpense(), getSociety(), this));
		}
	}
}
