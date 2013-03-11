package edu.mit.sips.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Society;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.CityAgricultureSystem;
import edu.mit.sips.io.Icons;

public class AgricultureSystemPanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = 569560127649283731L;

	private final LinearIndicatorPanel localFoodIndicatorPanel, 
	foodConsumptionIndicatorPanel;
	
	private final SpatialStatePanel agricultureStatePanel;
	
	TimeSeriesCollection localFoodData = new TimeSeriesCollection();
	TimeSeriesCollection foodProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection foodSupplyCostData = new TimeSeriesCollection();
	TimeSeriesCollection foodConsumptionPerCapita = new TimeSeriesCollection();

	DefaultTableXYDataset landAvailableDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset foodSourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset foodUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset agricultureRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset agricultureNetRevenue = new DefaultTableXYDataset();
	/* TODO
	DefaultTableXYDataset foodProductionCost = new DefaultTableXYDataset();
	DefaultTableXYDataset foodConsumptionCost = new DefaultTableXYDataset();
	DefaultTableXYDataset foodProductionNetCost = new DefaultTableXYDataset();
	DefaultTableXYDataset foodConsumptionNetCost = new DefaultTableXYDataset();
	*/

	public AgricultureSystemPanel(AgricultureSystem.Local agricultureSystem) {
		super(agricultureSystem);
		
		JPanel indicatorsPanel = new JPanel();
		indicatorsPanel.setLayout(
				new BoxLayout(indicatorsPanel, BoxLayout.LINE_AXIS));
		localFoodIndicatorPanel = new LinearIndicatorPanel(
				"Food Independence", 0, 1);
		indicatorsPanel.add(localFoodIndicatorPanel);
		foodConsumptionIndicatorPanel = new LinearIndicatorPanel(
				"Food Consumption", 
				getSociety().getGlobals().getMinFoodDemandPerCapita(), 
				getSociety().getGlobals().getMaxFoodDemandPerCapita());
		indicatorsPanel.add(foodConsumptionIndicatorPanel);
		addTab("Indicators", Icons.INDICATORS, indicatorsPanel);
		
		agricultureStatePanel = new SpatialStatePanel(
				agricultureSystem.getSociety(), new AgricultureStateProvider());
		addTab("Network Flow", Icons.NETWORK, agricultureStatePanel);
		
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Agriculture Revenue (SAR/year)", 
				agricultureRevenue, agricultureNetRevenue));
		addTab("Source", Icons.AGRICULTURE_SOURCE, createStackedAreaChart(
				"Food Source (Mj/year)", foodSourceData));
		addTab("Use", Icons.AGRICULTURE_USE, createStackedAreaChart(
				"Food Use (Mj/year)", foodUseData));
		addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Food Fraction (-)", localFoodData));
		addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Food Consumption Per Capita (Mj/person)", 
						foodConsumptionPerCapita));
		addTab("Arable Land", Icons.ARABLE_LAND, createStackedAreaChart(
				"Arable Land Available (km^2)", landAvailableDataset));
		addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
				"Direct Food Production Cost (SAR/Mj)", 
						foodProductCostData));
		addTab("Supply Cost", Icons.COST_SUPPLY, createTimeSeriesChart(
				"Direct Food Supply Cost (SAR/Mj)", 
						foodSupplyCostData));
		/* TODO
		addTab("Production Cost", Icons.COST_PRODUCTION, createStackedAreaChart(
				"Direct Food Production Cost (SAR/Mj)", 
						foodProductionCost, foodProductionNetCost));
		addTab("Consumption Cost", Icons.COST_SUPPLY, createStackedAreaChart(
				"Direct Food Consumption Cost (SAR/Mj)", 
						foodConsumptionCost, foodConsumptionNetCost));
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

	@Override
	public void simulationInitialized(UpdateEvent event) {
		agricultureStatePanel.repaint();
	}

	@Override
	public void simulationUpdated(UpdateEvent event) {
		agricultureStatePanel.repaint();
	}

	@Override
	public void initialize() {
		localFoodIndicatorPanel.initialize();
		foodConsumptionIndicatorPanel.initialize();
		localFoodData.removeAllSeries();
		foodProductCostData.removeAllSeries();
		foodSupplyCostData.removeAllSeries();
		/* TODO
		foodProductionCost.removeAllSeries();
		foodConsumptionCost.removeAllSeries();
		foodProductionNetCost.removeAllSeries();
		foodConsumptionNetCost.removeAllSeries();
		*/
		foodConsumptionPerCapita.removeAllSeries();
		landAvailableDataset.removeAllSeries();
		foodUseData.removeAllSeries();
		foodSourceData.removeAllSeries();
		agricultureRevenue.removeAllSeries();
		agricultureNetRevenue.removeAllSeries();
	}

	/**
	 * Gets the nested agriculture systems.
	 *
	 * @return the nested agriculture systems
	 */
	private List<AgricultureSystem.Local> getNestedAgricultureSystems() {
		List<AgricultureSystem.Local> systems = new ArrayList<AgricultureSystem.Local>();
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			if(nestedSociety.getWaterSystem() instanceof AgricultureSystem.Local) {
				systems.add((AgricultureSystem.Local)nestedSociety.getAgricultureSystem());
			}
		}
		return systems;
	}

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
				year, getAgricultureSystem().getProductionCost());
		for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
			updateSeriesCollection(foodProductCostData, nestedSystem.getSociety().getName(),
					year, nestedSystem.getProductionCost());
		}

		updateSeriesCollection(foodSupplyCostData, getSociety().getName(), 
				year, getAgricultureSystem().getSupplyCost());
		for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
			updateSeriesCollection(foodSupplyCostData, nestedSystem.getSociety().getName(),
					year, nestedSystem.getSupplyCost());
		}
		/* TODO
		if(society.getAgricultureSystem().getFoodProduction() > 0) {
			updateSeries(foodProductionCost, "Capital", year, 
					society.getAgricultureSystem().getCapitalExpense()
					/ society.getAgricultureSystem().getFoodProduction());
			updateSeries(foodProductionCost, "Operations", year, 
					society.getAgricultureSystem().getOperationsExpense()
					/ society.getAgricultureSystem().getFoodProduction());
			updateSeries(foodProductionCost, "Decommission", year, 
					society.getAgricultureSystem().getDecommissionExpense()
					/ society.getAgricultureSystem().getFoodProduction());
			updateSeries(foodProductionCost, "Water", year, 
					society.getAgricultureSystem().getWaterConsumption()
					* society.getGlobals().getWaterDomesticPrice()
					/ society.getAgricultureSystem().getFoodProduction());
		}
		if(society.getTotalFoodDemand() > 0) {
			// TODO this is not quite right
			updateSeries(foodConsumptionCost, "Net Distribution", year, 
					(society.getAgricultureSystem().getDistributionExpense()
					- society.getAgricultureSystem().getDistributionRevenue())
					/ society.getTotalFoodDemand());
			updateSeries(foodConsumptionCost, "Import", year, 
					society.getAgricultureSystem().getImportExpense()
					/ society.getTotalFoodDemand());
			updateSeries(foodConsumptionCost, "Export", year, 
					-society.getAgricultureSystem().getExportRevenue()
					/ society.getTotalFoodDemand());
			updateSeries(foodConsumptionNetCost, "Net Cost", year, 
					society.getAgricultureSystem().getTotalExpense()
					/ society.getTotalFoodDemand());
		}
		*/
		
		updateSeriesCollection(foodConsumptionPerCapita, getSociety().getName(), 
				year, getSociety().getSocialSystem().getFoodConsumptionPerCapita());
		foodConsumptionIndicatorPanel.setValue(getSociety().getSocialSystem().getFoodConsumptionPerCapita());
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			updateSeriesCollection(foodConsumptionPerCapita, nestedSociety.getName(), 
					year, nestedSociety.getSocialSystem().getFoodConsumptionPerCapita());
		}
		
		if(getAgricultureSystem() instanceof CityAgricultureSystem) {
			updateSeries(landAvailableDataset, getSociety().getName(), year, 
					getAgricultureSystem().getArableLandArea()
					- getAgricultureSystem().getLandAreaUsed());

		} else {
			for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
				updateSeries(landAvailableDataset, nestedSystem.getSociety().getName(), 
						year, nestedSystem.getArableLandArea()
						- nestedSystem.getLandAreaUsed());
			}
		}
	
		updateSeries(foodUseData, "Society", year, 
				getSociety().getSocialSystem().getFoodConsumption());
		updateSeries(agricultureRevenue, "Capital", year, 
				-getAgricultureSystem().getCapitalExpense());
		updateSeries(agricultureRevenue, "Operations", year, 
				-getAgricultureSystem().getOperationsExpense());
		updateSeries(agricultureRevenue, "Decommission", year, 
				-getAgricultureSystem().getDecommissionExpense());
		updateSeries(agricultureRevenue, "Consumption", year, 
				-getAgricultureSystem().getConsumptionExpense());
		updateSeries(agricultureRevenue, "In-Distribution", year, 
				-getAgricultureSystem().getDistributionExpense());
		updateSeries(agricultureRevenue, "Import", year, 
				-getAgricultureSystem().getImportExpense());
		updateSeries(agricultureRevenue, "Out-Distribution", year, 
				getAgricultureSystem().getDistributionRevenue());
		updateSeries(agricultureRevenue, "Export", year, 
				getAgricultureSystem().getExportRevenue());
		updateSeries(agricultureRevenue, "Sales", year, 
				getAgricultureSystem().getSalesRevenue());
		updateSeries(agricultureNetRevenue, "Net Revenue", year, 
				getAgricultureSystem().getCashFlow());
		if(getAgricultureSystem() instanceof CityAgricultureSystem) {
			for(AgricultureElement element : getAgricultureSystem().getInternalElements()) {
				if(element.getMaxLandArea() > 0) {
					updateSeries(foodSourceData, element.getName(), year, 
							element.getFoodProduction());
				}
				
				if(element.getMaxFoodInput() > 0) {
					updateSeries(foodUseData, element.getName(), year, 
							element.getFoodInput());
				}
			}
			for(AgricultureElement element : getAgricultureSystem().getExternalElements()) {
				if(element.getMaxFoodInput() > 0) {
					updateSeries(foodSourceData, element.getName(), year, 
							element.getFoodOutput());
				}
			}
		} else {
			updateSeries(foodSourceData, "Production", year, 
					getAgricultureSystem().getFoodProduction());
			updateSeries(foodSourceData, "Distribution", year, 
					getAgricultureSystem().getFoodInDistribution());
			updateSeries(foodUseData, "Distribution", year, 
					getAgricultureSystem().getFoodOutDistribution());
		}
		updateSeries(foodUseData, "Export", year, 
				getAgricultureSystem().getFoodExport());
		updateSeries(foodSourceData, "Import", year, 
				getAgricultureSystem().getFoodImport());
	}

}
