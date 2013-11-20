package edu.mit.sips.gui.agriculture;

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
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.gui.LinearIndicatorPanel;
import edu.mit.sips.gui.PlottingUtils;
import edu.mit.sips.gui.SpatialStatePanel;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.DefaultUnits;
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

	DefaultTableXYDataset landAvailableDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset laborAvailableDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset foodSourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset foodUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset agricultureRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset agricultureNetRevenue = new DefaultTableXYDataset();
	
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
		/* temporarily remove
		addTab("Indicators", Icons.INDICATORS, indicatorsPanel);
		 */
		
		agricultureStatePanel = new SpatialStatePanel(
				agricultureSystem.getSociety(), new AgricultureStateProvider());
		addTab("Network Flow", Icons.NETWORK, agricultureStatePanel);

		List<String> revenueNames = Arrays.asList("Capital Exp", 
				"Operations Exp", "Decommission Exp", "Input Exp", "Distribution Exp", 
				"Import Exp", "Distribution Rev", "Export Rev", "Output Rev");
		for(String name : revenueNames) {
			agricultureRevenue.addSeries(new XYSeries(name, true, false));
		}
		addTab("Revenue", Icons.REVENUE,
				createStackedAreaChart("Agriculture Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")",
						agricultureRevenue, PlottingUtils.getCashFlowColors(revenueNames), agricultureNetRevenue));
		
		
		List<String> foodSourceNames = new ArrayList<String>();
		if(getAgricultureSystem().getSociety() instanceof Country) {
			for(Society society : getAgricultureSystem().getSociety().getNestedSocieties()) {
				foodSourceNames.add(society.getName() + " Production");
			}
		} else {
			foodSourceNames.add("Production");
			foodSourceNames.add("Distribution");
		}
		foodSourceNames.add("Import");
		for(String name : foodSourceNames) {
			foodSourceData.addSeries(new XYSeries(name, true, false));
		}
		addTab("Source", Icons.AGRICULTURE_SOURCE,
				createStackedAreaChart("Food Source (" + foodUnits + "/" + foodTimeUnits + ")", 
						foodSourceData, PlottingUtils.getResourceColors(foodSourceNames)));
		
		List<String> foodUseNames = new ArrayList<String>();
		if(getAgricultureSystem().getSociety() instanceof Country) {
			for(Society society : getAgricultureSystem().getSociety().getNestedSocieties()) {
				foodUseNames.add(society.getName() + " Society");
			}
			foodUseNames.add("Losses");
		} else {
			foodUseNames.add(getSociety().getName()  + " Society");
			foodUseNames.add("Distribution");
		}
		foodUseNames.add("Export");
		for(String name : foodUseNames) {
			foodUseData.addSeries(new XYSeries(name, true, false));
		}
		addTab("Use", Icons.AGRICULTURE_USE,
				createStackedAreaChart("Food Use (" + foodUnits + "/" + foodTimeUnits + ")", 
						foodUseData, PlottingUtils.getResourceColors(foodUseNames)));
		
		addTab("Use", Icons.WATER_USE,
				createStackedAreaChart("Water Use (" + waterUnits + "/"
						+ waterTimeUnits + ")", waterUseData));
		
		/* temporarily removed
				"Local Food Fraction (-)", localFoodData));
		addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Food Consumption Per Capita (" + FoodUnits.kcal 
				+ "/" + TimeUnits.day + ")", 
						foodConsumptionPerCapita));
		 */
		addTab("Arable Land", Icons.ARABLE_LAND, createStackedAreaChart(
				"Arable Land (km^2)", landAvailableDataset));
		addTab("Labor", Icons.LABOR, createStackedAreaChart(
				"Available Labor (people)", laborAvailableDataset));
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#initialize()
	 */
	@Override
	public void initialize() {
		localFoodIndicatorPanel.initialize();
		localFoodData.removeAllSeries();
		foodProductCostData.removeAllSeries();
		foodSupplyProfitData.removeAllSeries();
		foodConsumptionPerCapita.removeAllSeries();
		landAvailableDataset.removeAllSeries();
		laborAvailableDataset.removeAllSeries();
		foodUseData.removeAllSeries();
		waterUseData.removeAllSeries();
		foodSourceData.removeAllSeries();
		agricultureRevenue.removeAllSeries();
		agricultureNetRevenue.removeAllSeries();
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
		agricultureStatePanel.repaint();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		agricultureStatePanel.repaint();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#update(int)
	 */
	@Override
	public void update(int year) {
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
		
		if(getAgricultureSystem() instanceof DefaultAgricultureSystem.Local) {
			updateSeries(landAvailableDataset, "Available", year, 
					getAgricultureSystem().getArableLandArea() - getAgricultureSystem().getLandAreaUsed());
			updateSeries(landAvailableDataset, "Used", year, 
					getAgricultureSystem().getLandAreaUsed());
		} else {
			for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
				updateSeries(landAvailableDataset, nestedSystem.getSociety().getName() + " (Available)", 
						year, nestedSystem.getArableLandArea() - nestedSystem.getLandAreaUsed());
				updateSeries(landAvailableDataset, nestedSystem.getSociety().getName() + " (Used)", 
						year, nestedSystem.getLandAreaUsed());
			}
		}
		
		if(getAgricultureSystem() instanceof DefaultAgricultureSystem.Local) {
			updateSeries(laborAvailableDataset, "Available", year, 
					getAgricultureSystem().getLaborParticipationRate() 
					* getAgricultureSystem().getSociety().getSocialSystem().getPopulation()
					- getAgricultureSystem().getLaborUsed());
			updateSeries(laborAvailableDataset, "Used", year, 
					getAgricultureSystem().getLaborUsed());

		} else {
			for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
				updateSeries(laborAvailableDataset, nestedSystem.getSociety().getName() + " (Available)", year,
						nestedSystem.getLaborParticipationRate() 
						* nestedSystem.getSociety().getSocialSystem().getPopulation()
						- nestedSystem.getLaborUsed());
				updateSeries(laborAvailableDataset, nestedSystem.getSociety().getName() + " (Used)", year,
						nestedSystem.getLaborUsed());
			}
		}
	
		if(getNestedAgricultureSystems().isEmpty()) {
			updateSeries(foodUseData, getAgricultureSystem().getSociety().getName() + " Society", year, 
					FoodUnits.convertFlow(getSociety().getSocialSystem().getFoodConsumption(),
							getSociety().getSocialSystem(), this));
			updateSeries(waterUseData, getAgricultureSystem().getName(), year, 
					WaterUnits.convertFlow(getAgricultureSystem().getWaterConsumption(),
							getAgricultureSystem(), this));
			/* temporarily removed
			for(AgricultureElement element : getAgricultureSystem().getInternalElements()) {
				if(element.getWaterConsumption() > 0) {
					updateSeries(waterUseData, element.getName(), year, 
							WaterUnits.convertFlow(element.getWaterConsumption(),
									element, this));
				}
			}
			*/
		} else {
			for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
				updateSeries(foodUseData, nestedSystem.getSociety().getName() + " Society", year,
						FoodUnits.convertFlow(nestedSystem.getSociety().getSocialSystem().getFoodConsumption(), 
								nestedSystem.getSociety().getSocialSystem(), this));
				updateSeries(waterUseData, nestedSystem.getName(), year, 
						WaterUnits.convertFlow(nestedSystem.getWaterConsumption(),
								nestedSystem, this));
			}
		}
		updateSeries(agricultureRevenue, "Capital Exp", year, 
				CurrencyUnits.convertFlow(-getAgricultureSystem().getCapitalExpense(),
						getAgricultureSystem(), this));
		updateSeries(agricultureRevenue, "Operations Exp", year, 
				CurrencyUnits.convertFlow(-getAgricultureSystem().getOperationsExpense(),
						getAgricultureSystem(), this));
		updateSeries(agricultureRevenue, "Decommission Exp", year, 
				CurrencyUnits.convertFlow(-getAgricultureSystem().getDecommissionExpense(),
						getAgricultureSystem(), this));
		updateSeries(agricultureRevenue, "Input Exp", year, 
				CurrencyUnits.convertFlow(-getAgricultureSystem().getConsumptionExpense(),
						getAgricultureSystem(), this));
		if(!(getAgricultureSystem().getSociety() instanceof Country)) {
			updateSeries(agricultureRevenue, "Distribution Exp", year, 
					CurrencyUnits.convertFlow(-getAgricultureSystem().getDistributionExpense(),
							getAgricultureSystem(), this));
		}
		updateSeries(agricultureRevenue, "Import Exp", year, 
				CurrencyUnits.convertFlow(-getAgricultureSystem().getImportExpense(),
						getAgricultureSystem(), this));
		if(!(getAgricultureSystem().getSociety() instanceof Country)) {
			updateSeries(agricultureRevenue, "Distribution Rev", year, 
					CurrencyUnits.convertFlow(getAgricultureSystem().getDistributionRevenue(),
							getAgricultureSystem(), this));
		}
		updateSeries(agricultureRevenue, "Export Rev", year, 
				CurrencyUnits.convertFlow(getAgricultureSystem().getExportRevenue(),
						getAgricultureSystem(), this));
		updateSeries(agricultureRevenue, "Output Rev", year, 
				CurrencyUnits.convertFlow(getAgricultureSystem().getSalesRevenue(),
						getAgricultureSystem(), this));
		updateSeries(agricultureNetRevenue, "Net Cash Flow", year, 
				CurrencyUnits.convertFlow(getAgricultureSystem().getCashFlow(),
						getAgricultureSystem(), this));
		if(getAgricultureSystem() instanceof DefaultAgricultureSystem.Local) {
			updateSeries(foodSourceData, "Production", year, 
					FoodUnits.convertFlow(getAgricultureSystem().getFoodProduction(),
							getAgricultureSystem(), this));
			updateSeries(foodUseData, "Distribution", year, 
					FoodUnits.convertFlow(getAgricultureSystem().getFoodOutDistribution(), 
							getAgricultureSystem(), this));
			updateSeries(foodSourceData, "Distribution", year, 
					FoodUnits.convertFlow(getAgricultureSystem().getFoodInDistribution(),
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
		} else {
			for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
				updateSeries(foodSourceData, nestedSystem.getSociety().getName() + " Production", year,
						FoodUnits.convertFlow(nestedSystem.getFoodProduction(), nestedSystem, this));
			}
			/*updateSeries(foodSourceData, "Production", year, 
					FoodUnits.convert(getAgricultureSystem().getFoodProduction(),
							getAgricultureSystem(), this));*/
			if(!getAgricultureSystem().getExternalElements().isEmpty()) {
				updateSeries(foodSourceData, "Distribution", year, 
						FoodUnits.convertFlow(getAgricultureSystem().getFoodInDistribution(),
								getAgricultureSystem(), this));
				updateSeries(foodUseData, "Distribution", year, 
						FoodUnits.convertFlow(getAgricultureSystem().getFoodOutDistribution(),
								getAgricultureSystem(), this));
			}
			updateSeries(foodUseData, "Losses", year, 
					FoodUnits.convertFlow(getAgricultureSystem().getFoodOutDistributionLosses(),
							getAgricultureSystem(), this));
		}
		updateSeries(foodUseData, "Export", year, 
				FoodUnits.convertFlow(getAgricultureSystem().getFoodExport(),
						getAgricultureSystem(), this));
		updateSeries(foodSourceData, "Import", year, 
				FoodUnits.convertFlow(getAgricultureSystem().getFoodImport(),
						getAgricultureSystem(), this));
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
