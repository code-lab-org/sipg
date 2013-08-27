package edu.mit.sips.gui.agriculture;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Society;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.gui.LinearIndicatorPanel;
import edu.mit.sips.gui.SpatialStatePanel;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;

public class LocalAgricultureSystemPanel extends AgricultureSystemPanel {
	private static final long serialVersionUID = 569560127649283731L;

	private final LinearIndicatorPanel localFoodIndicatorPanel;
	
	private final SpatialStatePanel agricultureStatePanel;
	
	TimeSeriesCollection localFoodData = new TimeSeriesCollection();
	TimeSeriesCollection foodProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection foodSupplyProfitData = new TimeSeriesCollection();
	TimeSeriesCollection foodConsumptionPerCapita = new TimeSeriesCollection();

	DefaultTableXYDataset landAvailableDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset foodSourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset foodUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset agricultureRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset agricultureNetRevenue = new DefaultTableXYDataset();

	public LocalAgricultureSystemPanel(AgricultureSystem.Local agricultureSystem) {
		super(agricultureSystem);
		
		JPanel indicatorsPanel = new JPanel();
		indicatorsPanel.setLayout(
				new BoxLayout(indicatorsPanel, BoxLayout.LINE_AXIS));
		localFoodIndicatorPanel = new LinearIndicatorPanel(
				"Food Independence", 0, 1);
		indicatorsPanel.add(localFoodIndicatorPanel);
		addTab("Indicators", Icons.INDICATORS, indicatorsPanel);
		
		agricultureStatePanel = new SpatialStatePanel(
				agricultureSystem.getSociety(), new AgricultureStateProvider());
		addTab("Network Flow", Icons.NETWORK, agricultureStatePanel);
		
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Agriculture Revenue (SAR/year)", 
				agricultureRevenue, null, agricultureNetRevenue));
		addTab("Source", Icons.AGRICULTURE_SOURCE, createStackedAreaChart(
				"Food Source (GJ/year)", foodSourceData));
		addTab("Use", Icons.AGRICULTURE_USE, createStackedAreaChart(
				"Food Use (GJ/year)", foodUseData));
		addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Food Fraction (-)", localFoodData));
		addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Food Consumption Per Capita (GJ/person)", 
						foodConsumptionPerCapita));
		addTab("Arable Land", Icons.ARABLE_LAND, createStackedAreaChart(
				"Arable Land (km^2)", landAvailableDataset));
		/* TODO
		addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
				"Unit Production Cost (SAR/GJ)", 
						foodProductCostData));
		addTab("Supply Profit", Icons.COST_SUPPLY, createTimeSeriesChart(
				"Unit Supply Profit (SAR/GJ)", 
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

	@Override
	public void initialize() {
		localFoodIndicatorPanel.initialize();
		localFoodData.removeAllSeries();
		foodProductCostData.removeAllSeries();
		foodSupplyProfitData.removeAllSeries();
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
			if(nestedSociety.getAgricultureSystem() instanceof AgricultureSystem.Local) {
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
					year, getSociety().getSocialSystem().getFoodConsumption() 
					/ getSociety().getSocialSystem().getPopulation());
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				if(nestedSociety.getSocialSystem().getPopulation() > 0) {
					updateSeriesCollection(foodConsumptionPerCapita, nestedSociety.getName(), 
							year, nestedSociety.getSocialSystem().getFoodConsumption() 
							/ nestedSociety.getSocialSystem().getPopulation());
				}
			}
		}
		
		if(getAgricultureSystem() instanceof DefaultAgricultureSystem.Local) {
			updateSeries(landAvailableDataset, "Land", year, 
					getAgricultureSystem().getArableLandArea() - getAgricultureSystem().getLandAreaUsed());

		} else {
			for(AgricultureSystem.Local nestedSystem : getNestedAgricultureSystems()) {
				updateSeries(landAvailableDataset, nestedSystem.getSociety().getName(), 
						year, nestedSystem.getArableLandArea() - nestedSystem.getLandAreaUsed());
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
		if(getAgricultureSystem() instanceof DefaultAgricultureSystem.Local) {
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
