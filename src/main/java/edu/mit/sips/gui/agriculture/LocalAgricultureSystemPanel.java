package edu.mit.sips.gui.agriculture;

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
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.LocalAgricultureSoS;
import edu.mit.sips.core.base.InfrastructureSystem;
import edu.mit.sips.gui.LinearIndicatorPanel;
import edu.mit.sips.gui.PlottingUtils;
import edu.mit.sips.gui.SpatialStatePanel;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.FoodUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Class LocalAgricultureSystemPanel.
 */
public class LocalAgricultureSystemPanel extends AgricultureSystemPanel 
implements FoodUnitsOutput, CurrencyUnitsOutput, WaterUnitsOutput {
	private static final long serialVersionUID = 569560127649283731L;

	private final LinearIndicatorPanel localFoodIndicatorPanel;
	private final List<LocalAgricultureSystemPanel> nestedPanels = 
			new ArrayList<LocalAgricultureSystemPanel>();
	private final SpatialStatePanel agricultureStatePanel;
	
	private final FoodUnits foodUnits = FoodUnits.EJ;
	private final TimeUnits foodTimeUnits = TimeUnits.year;
	private final WaterUnits waterUnits = WaterUnits.km3;
	private final TimeUnits waterTimeUnits = TimeUnits.year;
	private final CurrencyUnits currencyUnits = CurrencyUnits.Bsim;
	private final TimeUnits currencyTimeUnits = TimeUnits.year;
	
	TimeSeriesCollection localFoodData = new TimeSeriesCollection();
	TimeSeriesCollection foodProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection foodSupplyProfitData = new TimeSeriesCollection();
	TimeSeriesCollection foodConsumptionPerCapita = new TimeSeriesCollection();

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
	
	DefaultTableXYDataset capitalExpense = new DefaultTableXYDataset();
	DefaultTableXYDataset capitalExpenseTotal = new DefaultTableXYDataset();
	DefaultTableXYDataset cumulativeCapitalExpense = new DefaultTableXYDataset();
	
	/**
	 * Instantiates a new local agriculture system panel.
	 *
	 * @param agricultureSystem the agriculture system
	 */
	public LocalAgricultureSystemPanel(AgricultureSystem.Local agricultureSystem) {
		super(agricultureSystem);
		
		JPanel indicatorsPanel = new JPanel();
		indicatorsPanel.setLayout(
				new BoxLayout(indicatorsPanel, BoxLayout.LINE_AXIS));
		localFoodIndicatorPanel = new LinearIndicatorPanel(
				"Food Independence", 0, 1);
		indicatorsPanel.add(localFoodIndicatorPanel);
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
		if(agricultureSystem instanceof LocalAgricultureSoS) {
			nationalPane = new JTabbedPane();
			addTab(getSociety().getName(), Icons.COUNTRY, nationalPane);
		} else {
			nationalPane = this;
		}

		if(getAgricultureSystem() instanceof LocalAgricultureSoS) {
			nationalPane.addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
					getAgricultureSystem().getName() + " Net Revenue",
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
					getAgricultureSystem().getName() + " Net Revenue",
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
		if(!(getAgricultureSystem() instanceof LocalAgricultureSoS)) {
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
		if(getAgricultureSystem() instanceof LocalAgricultureSoS) {
			nationalPane.addTab("Source", Icons.AGRICULTURE_SOURCE, createToggleableStackedAreaChart(
					getAgricultureSystem().getName() + " Food Source",
					"Food Source (" + foodUnits + "/" + foodTimeUnits + ")", 
					foodSourceAggregatedData, PlottingUtils.getResourceColors(foodSourceAggregatedNames), 
					foodSourceDisaggregatedData, PlottingUtils.getResourceColors(foodSourceDisaggregatedNames)));
		} else {
			nationalPane.addTab("Source", Icons.AGRICULTURE_SOURCE, createStackedAreaChart(
					getAgricultureSystem().getName() + " Food Source",
					"Food Source (" + foodUnits + "/" + foodTimeUnits + ")", 
					foodSourceAggregatedData, PlottingUtils.getResourceColors(foodSourceAggregatedNames)));
		}
		
		List<String> foodUseAggregatedNames = new ArrayList<String>();
		List<String> foodUseDisaggregatedNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				foodUseDisaggregatedNames.add(society.getName());
			}
			// foodUseNames.add("Losses");
		}
		foodUseAggregatedNames.add(getSociety().getName()  + " Society");
		if(!(getAgricultureSystem() instanceof LocalAgricultureSoS)) {
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
		if(getAgricultureSystem() instanceof LocalAgricultureSoS) {
			nationalPane.addTab("Use", Icons.AGRICULTURE_USE, createToggleableStackedAreaChart(
					getAgricultureSystem().getName() + " Food Use",
					"Food Use (" + foodUnits + "/" + foodTimeUnits + ")", 
					foodUseAggregatedData, PlottingUtils.getResourceColors(foodUseAggregatedNames), 
					foodUseDisaggregatedData, PlottingUtils.getResourceColors(foodUseDisaggregatedNames)));
		} else {
			nationalPane.addTab("Use", Icons.AGRICULTURE_USE, createStackedAreaChart(
					getAgricultureSystem().getName() + " Food Use",
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
		if(getAgricultureSystem() instanceof LocalAgricultureSoS) {
			nationalPane.addTab("Use", Icons.WATER_USE, createToggleableStackedAreaChart(
					getAgricultureSystem().getName() + " Water Use",
					"Water Use (" + waterUnits + "/" + waterTimeUnits + ")",
					waterUseAggregatedData, PlottingUtils.getResourceColors(waterUseAggregatedNames),
					waterUseDisaggregatedData, PlottingUtils.getResourceColors(waterUseDisaggregatedNames)));
		} else {
			nationalPane.addTab("Use", Icons.WATER_USE, createStackedAreaChart(
					getAgricultureSystem().getName() + " Water Use",
					"Water Use (" + waterUnits + "/" + waterTimeUnits + ")",
					waterUseAggregatedData, PlottingUtils.getResourceColors(waterUseAggregatedNames)));
		}
		
		/* temporarily removed
		addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Food Fraction (-)", localFoodData));
		addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Food Consumption Per Capita (" + FoodUnits.kcal 
				+ "/" + TimeUnits.day + ")", 
						foodConsumptionPerCapita));
		 */
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
		
		if(getAgricultureSystem() instanceof LocalAgricultureSoS) {
			nationalPane.addTab("Land", Icons.ARABLE_LAND, createToggleableStackedAreaChart(
					getAgricultureSystem().getName() + " Land", "Arable Land (km^2)", 
					landAvailableAggregatedDataset, landColorsAggregated.toArray(new Color[0]),
					landAvailableDisaggregatedDataset, landColorsDisaggregated.toArray(new Color[0])));
			nationalPane.addTab("Labor", Icons.LABOR, createToggleableStackedAreaChart(
					getAgricultureSystem().getName() + " Labor", "Available Labor (people)", 
					laborAvailableAggregatedDataset, landColorsAggregated.toArray(new Color[0]),
					laborAvailableDisaggregatedDataset, landColorsDisaggregated.toArray(new Color[0])));
		} else {
			nationalPane.addTab("Land", Icons.ARABLE_LAND, createStackedAreaChart(
					getAgricultureSystem().getName() + " Land", "Arable Land (km^2)", 
					landAvailableAggregatedDataset, landColorsAggregated.toArray(new Color[0])));
			nationalPane.addTab("Labor", Icons.LABOR, createStackedAreaChart(
					getAgricultureSystem().getName() + " Labor", "Available Labor (people)", 
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
			JTabbedPane regionalData = this; // new JTabbedPane();
			for(AgricultureSystem.Local nestedSystem : 
				((LocalAgricultureSoS) agricultureSystem).getNestedSystems()) {
				LocalAgricultureSystemPanel nestedPanel = 
						new LocalAgricultureSystemPanel(nestedSystem);
				nestedPanel.addChangeListener(tabSynchronizer);
				nestedPanels.add(nestedPanel);
				regionalData.addTab(nestedSystem.getSociety().getName(), 
						Icons.CITY, nestedPanel);
			}
			// addTab("Regions", Icons.INFRASTRUCTURE, regionalData);
		}
		
		/* TODO
		addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
				"Unit Production Cost (SAR/kcal/day)", 
						foodProductCostData));
		addTab("Supply Profit", Icons.COST_SUPPLY, createTimeSeriesChart(
				"Unit Supply Profit (SAR/kcal/day)", 
						foodSupplyProfitData));
		*/
	}
	
	/**
	 * Gets the agriculture system.
	 *
	 * @return the agriculture system
	 */
	public AgricultureSystem.Local getAgricultureSystem() {
		return (AgricultureSystem.Local) getInfrastructureSystem();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodTimeUnits()
	 */
	@Override
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnits()
	 */
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
	
	private void initialize() {
		localFoodIndicatorPanel.initialize();
		localFoodData.removeAllSeries();
		foodProductCostData.removeAllSeries();
		foodSupplyProfitData.removeAllSeries();
		foodConsumptionPerCapita.removeAllSeries();
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
		for(LocalAgricultureSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationCompleted(event);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationInitialized(UpdateEvent event) {
		initialize();
		for(LocalAgricultureSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationInitialized(event);
		}
		agricultureStatePanel.repaint();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		update((int)event.getTime());
		for(LocalAgricultureSystemPanel nestedPanel : nestedPanels) {
			nestedPanel.simulationUpdated(event);
		}
		agricultureStatePanel.repaint();
	}

	private void update(int year) {
		/* temporarily removed
		updateSeriesCollection(localFoodData, getSociety().getName(), 
				year, getAgricultureSystem().getLocalFoodFraction());
		localFoodIndicatorPanel.setValue(
				getAgricultureSystem().getLocalFoodFraction());
		for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
			updateSeriesCollection(localFoodData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getLocalFoodFraction());
		}

		updateSeriesCollection(foodProductCostData, getSociety().getName(), 
				year, getAgricultureSystem().getUnitProductionCost());
		for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
			updateSeriesCollection(foodProductCostData, nestedSystem.getSociety().getName(),
					year, nestedSystem.getUnitProductionCost());
		}

		updateSeriesCollection(foodSupplyProfitData, getSociety().getName(), 
				year, getAgricultureSystem().getUnitSupplyProfit());
		for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
			updateSeriesCollection(foodSupplyProfitData, nestedSystem.getSociety().getName(),
					year, nestedSystem.getUnitSupplyProfit());
		}
		
		if(getSociety().getSocialSystem().getPopulation() > 0) {
			updateSeriesCollection(foodConsumptionPerCapita, getSociety().getName(), 
					year, DefaultUnits.convertFlow(getSociety().getSocialSystem().getFoodConsumption(), 
							getSociety().getSocialSystem().getFoodUnits(), 
							getSociety().getSocialSystem().getFoodTimeUnits(), 
							FoodUnits.kcal,
							TimeUnits.day)
							/ getSociety().getSocialSystem().getPopulation());
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				if(nestedSociety.getSocialSystem().getPopulation() > 0) {
					updateSeriesCollection(foodConsumptionPerCapita, nestedSociety.getName(), 
							year, DefaultUnits.convertFlow(nestedSociety.getSocialSystem().getFoodConsumption(), 
									nestedSociety.getSocialSystem().getFoodUnits(), 
									nestedSociety.getSocialSystem().getFoodTimeUnits(), 
									FoodUnits.kcal,
									TimeUnits.day)
									/ nestedSociety.getSocialSystem().getPopulation());
				}
			}
		}
		*/
		
		if(getAgricultureSystem() instanceof LocalAgricultureSoS) {
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
				getAgricultureSystem().getArableLandArea() - getAgricultureSystem().getLandAreaUsed());
		updateSeries(landAvailableAggregatedDataset, "Used", year, 
				getAgricultureSystem().getLandAreaUsed());
		
		updateSeries(laborAvailableAggregatedDataset, "Available", year, 
				getAgricultureSystem().getLaborParticipationRate() 
				* getSociety().getSocialSystem().getPopulation()
				- getAgricultureSystem().getLaborUsed());
		updateSeries(laborAvailableAggregatedDataset, "Used", year, 
				getAgricultureSystem().getLaborUsed());
		
		updateSeries(cashFlow, "Capital Expense", year, 
				CurrencyUnits.convertFlow(-getAgricultureSystem().getCapitalExpense(),
						getAgricultureSystem(), this));
		updateSeries(cashFlow, "Operations Expense", year, 
				CurrencyUnits.convertFlow(-getAgricultureSystem().getOperationsExpense()
						-getAgricultureSystem().getConsumptionExpense(),
						getAgricultureSystem(), this));
		updateSeries(cashFlow, "Decommission Expense", year, 
				CurrencyUnits.convertFlow(-getAgricultureSystem().getDecommissionExpense(),
						getAgricultureSystem(), this));
		/*updateSeries(agricultureRevenue, "Input Expense", year, 
				CurrencyUnits.convertFlow(-getAgricultureSystem().getConsumptionExpense(),
						getAgricultureSystem(), this));*/
		if(!(getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Expense", year, 
					CurrencyUnits.convertFlow(-getAgricultureSystem().getDistributionExpense(),
							getAgricultureSystem(), this));
		}
		updateSeries(cashFlow, "Import Expense", year, 
				CurrencyUnits.convertFlow(-getAgricultureSystem().getImportExpense(),
						getAgricultureSystem(), this));
		if(!(getSociety() instanceof Country)) {
			updateSeries(cashFlow, "Distribution Revenue", year, 
					CurrencyUnits.convertFlow(getAgricultureSystem().getDistributionRevenue(),
							getAgricultureSystem(), this));
		}
		updateSeries(cashFlow, "Export Revenue", year, 
				CurrencyUnits.convertFlow(getAgricultureSystem().getExportRevenue(),
						getAgricultureSystem(), this));
		updateSeries(cashFlow, "Domestic Revenue", year, 
				CurrencyUnits.convertFlow(getAgricultureSystem().getSalesRevenue(),
						getAgricultureSystem(), this));
		updateSeries(netCashFlow, "Net Revenue", year, 
				CurrencyUnits.convertFlow(getAgricultureSystem().getCashFlow(),
						getAgricultureSystem(), this));
		updateSeries(cumulativeBalance, "Cumulative Net Revenue", year, 
				CurrencyUnits.convertFlow(getAgricultureSystem().getCumulativeCashFlow(),
						getAgricultureSystem(), this));

		updateSeries(foodSourceAggregatedData, "Production", year, 
				FoodUnits.convertFlow(getAgricultureSystem().getFoodProduction(),
						getAgricultureSystem(), this));
		updateSeries(foodUseAggregatedData, "Society", year, 
				FoodUnits.convertFlow(getSociety().getSocialSystem().getFoodConsumption(),
						getSociety().getSocialSystem(), this));
		if(getAgricultureSystem() instanceof LocalAgricultureSoS) {
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
			/*updateSeries(foodSourceData, "Production", year, 
					FoodUnits.convert(getAgricultureSystem().getFoodProduction(),
							getAgricultureSystem(), this));*/
			/*if(!getAgricultureSystem().getExternalElements().isEmpty()) {
				updateSeries(foodSourceData, "Distribution", year, 
						FoodUnits.convertFlow(getAgricultureSystem().getFoodInDistribution(),
								getAgricultureSystem(), this));
				updateSeries(foodUseData, "Distribution", year, 
						FoodUnits.convertFlow(getAgricultureSystem().getFoodOutDistribution(),
								getAgricultureSystem(), this));
			}*/
			/*updateSeries(foodUseData, "Losses", year, 
					FoodUnits.convertFlow(getAgricultureSystem().getFoodOutDistributionLosses(),
							getAgricultureSystem(), this));*/
		} else {
			updateSeries(foodSourceAggregatedData, "Distribution", year, 
					FoodUnits.convertFlow(getAgricultureSystem().getFoodInDistribution(),
							getAgricultureSystem(), this));
			updateSeries(foodUseAggregatedData, "Distribution", year, 
					FoodUnits.convertFlow(getAgricultureSystem().getFoodOutDistribution(), 
							getAgricultureSystem(), this));
		}
		updateSeries(waterUseAggregatedData, "Operations", year, 
				WaterUnits.convertFlow(getAgricultureSystem().getWaterConsumption(),
						getAgricultureSystem(), this));
		/* temporarily removed
		for(AgricultureElement element : getAgricultureSystem().getInternalElements()) {
			if(element.getMaxLandArea() > 0) {
				updateSeries(foodSourceData, element.getName(), year, 
						FoodUnits.convertFlow(element.getFoodProduction(),
								element, this));
			}
			
			if(element.getMaxFoodInput() > 0) {
				updateSeries(foodUseData, element.getName(), year, 
						FoodUnits.convertFlow(element.getFoodInput(), element, this));
			}
		}
		for(AgricultureElement element : getAgricultureSystem().getExternalElements()) {
			if(element.getMaxFoodInput() > 0) {
				updateSeries(foodSourceData, element.getName(), year, 
						FoodUnits.convertFlow(element.getFoodOutput(),
								element, this));
			}
		}
		*/
		/* temporarily removed
		for(AgricultureElement element : getAgricultureSystem().getInternalElements()) {
			if(element.getWaterConsumption() > 0) {
				updateSeries(waterUseData, element.getName(), year, 
						WaterUnits.convertFlow(element.getWaterConsumption(),
								element, this));
			}
		}
		*/
		updateSeries(foodSourceAggregatedData, "Import", year, 
				FoodUnits.convertFlow(getAgricultureSystem().getFoodImport(),
						getAgricultureSystem(), this));
		updateSeries(foodSourceDisaggregatedData, "Import", year, 
				FoodUnits.convertFlow(getAgricultureSystem().getFoodImport(),
						getAgricultureSystem(), this));
		updateSeries(foodUseAggregatedData, "Export", year, 
				FoodUnits.convertFlow(getAgricultureSystem().getFoodExport(),
						getAgricultureSystem(), this));
		updateSeries(foodUseDisaggregatedData, "Export", year, 
				FoodUnits.convertFlow(getAgricultureSystem().getFoodExport(),
						getAgricultureSystem(), this));
		
		if(getAgricultureSystem() instanceof LocalAgricultureSoS) {
			for(InfrastructureSystem nestedSystem : getNestedAgricultureSystems()) {
				updateSeries(capitalExpense, nestedSystem.getName(), year, 
						CurrencyUnits.convertFlow(nestedSystem.getCapitalExpense(), 
								nestedSystem, this));
			}
			updateSeries(capitalExpenseTotal, "Total Investment", year, 
					CurrencyUnits.convertFlow(
							((LocalAgricultureSoS)getAgricultureSystem())
							.getCapitalExpense(), getSociety(), this));
			updateSeries(cumulativeCapitalExpense, "Cumulative Investment", year, 
					CurrencyUnits.convertFlow(
							((LocalAgricultureSoS)getAgricultureSystem())
							.getCumulativeCapitalExpense(), getSociety(), this));
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnits()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyTimeUnits()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
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
