package edu.mit.sips.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Society;
import edu.mit.sips.core.water.CityWaterSystem;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.io.Icons;

/**
 * The Class WaterSystemPanel.
 */
public class WaterSystemPanel extends InfrastructureSystemPanel {	
	private static final long serialVersionUID = -3665986046863585665L;
	
	private final LinearIndicatorPanel localWaterIndicatorPanel, 
	waterReservoirIndicatorPanel, renewableWaterIndicatorPanel, 
	waterConsumptionIndicatorPanel;
	
	private final SpatialStatePanel waterStatePanel;

	DefaultTableXYDataset waterSourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset waterNetRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset waterReservoirDataset = new DefaultTableXYDataset();
	
	TimeSeriesCollection localWaterData = new TimeSeriesCollection();
	TimeSeriesCollection waterProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection waterSupplyProfitData = new TimeSeriesCollection();
	TimeSeriesCollection renewableWaterData = new TimeSeriesCollection();
	TimeSeriesCollection waterConsumptionPerCapita = new TimeSeriesCollection();

	/**
	 * Instantiates a new water system panel.
	 *
	 * @param waterSystem the water system
	 */
	public WaterSystemPanel(WaterSystem.Local waterSystem) {
		super(waterSystem);
		
		JPanel indicatorsPanel = new JPanel();
		indicatorsPanel.setLayout(
				new BoxLayout(indicatorsPanel, BoxLayout.LINE_AXIS));
		localWaterIndicatorPanel = new LinearIndicatorPanel(
				"Water Independence", 0, 1);
		indicatorsPanel.add(localWaterIndicatorPanel);
		waterReservoirIndicatorPanel = new LinearIndicatorPanel(
				"Water Reservoir", 0, 
				waterSystem.getMaxWaterReservoirVolume());
		indicatorsPanel.add(waterReservoirIndicatorPanel);
		renewableWaterIndicatorPanel = new LinearIndicatorPanel(
				"Renewable Water", 0, 1);
		indicatorsPanel.add(renewableWaterIndicatorPanel);
		waterConsumptionIndicatorPanel = new LinearIndicatorPanel(
				"Water Consumption", 
				getSociety().getGlobals().getMinWaterDemandPerCapita(), 
				getSociety().getGlobals().getMaxWaterDemandPerCapita());
		indicatorsPanel.add(waterConsumptionIndicatorPanel);
		addTab("Indicators", Icons.INDICATORS, indicatorsPanel);
		
		waterStatePanel = new SpatialStatePanel(
				waterSystem.getSociety(), new WaterStateProvider());
		addTab("Network Flow", Icons.NETWORK, waterStatePanel);
		
		addTab("Revenue", Icons.REVENUE, 
				createStackedAreaChart("Water Revenue (SAR/year)", 
				waterRevenue, null, waterNetRevenue));
		addTab("Source", Icons.WATER_SOURCE, createStackedAreaChart(
				"Water Source (m^3/year)", waterSourceData));
		addTab("Use", Icons.WATER_USE, createStackedAreaChart(
				"Water Use (m^3/year)", waterUseData));

		addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Water Use Fraction (-)", localWaterData));
		addTab("Renewable", Icons.RENEWABLE, createTimeSeriesChart(
				"Renewable Water Production Fraction (-)", 
				renewableWaterData));
		addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Water Consumption per Capita (m^3/person)", 
				waterConsumptionPerCapita));
		addTab("Reservoir", Icons.WATER_RESERVOIR, createStackedAreaChart(
				"Water Reservoir Volume (m^3)", waterReservoirDataset));
		addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
				"Unit Production Cost (SAR/m^3)", 
				waterProductCostData));
		addTab("Supply Profit", Icons.COST_SUPPLY, createTimeSeriesChart(
				"Unit Supply Profit (SAR/m^3)", 
				waterSupplyProfitData));
	}
	
	/**
	 * Gets the water system.
	 *
	 * @return the water system
	 */
	public WaterSystem.Local getWaterSystem() {
		return (WaterSystem.Local) getInfrastructureSystem();
	}

	@Override
	public void initialize() {
		localWaterData.removeAllSeries();
		renewableWaterData.removeAllSeries();
		waterProductCostData.removeAllSeries();
		waterSupplyProfitData.removeAllSeries();
		waterConsumptionPerCapita.removeAllSeries();
		waterReservoirDataset.removeAllSeries();
		
		waterUseData.removeAllSeries();
		waterSourceData.removeAllSeries();
		waterRevenue.removeAllSeries();
		waterNetRevenue.removeAllSeries();
	}
	
	private List<WaterSystem.Local> getNestedWaterSystems() {
		List<WaterSystem.Local> systems = new ArrayList<WaterSystem.Local>();
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			if(nestedSociety.getWaterSystem() instanceof WaterSystem.Local) {
				systems.add((WaterSystem.Local)nestedSociety.getWaterSystem());
			}
		}
		return systems;
	}

	@Override
	public void update(int year) {
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

		updateSeriesCollection(waterConsumptionPerCapita, getSociety().getName(), 
				year, getSociety().getSocialSystem().getWaterConsumptionPerCapita());
		waterConsumptionIndicatorPanel.setValue(
				getSociety().getSocialSystem().getWaterConsumptionPerCapita());
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			updateSeriesCollection(waterConsumptionPerCapita, nestedSociety.getName(), 
					year, nestedSociety.getSocialSystem().getWaterConsumptionPerCapita());
		}

		waterReservoirIndicatorPanel.setValue(
				getWaterSystem().getWaterReservoirVolume());

		updateSeries(waterRevenue, "Capital", year, 
				-getWaterSystem().getCapitalExpense());
		updateSeries(waterRevenue, "Operations", year, 
				-getWaterSystem().getOperationsExpense());
		updateSeries(waterRevenue, "Decommission", year, 
				-getWaterSystem().getDecommissionExpense());
		updateSeries(waterRevenue, "Consumption", year, 
				-getWaterSystem().getConsumptionExpense());
		updateSeries(waterRevenue, "In-Distribution", year, 
				-getWaterSystem().getDistributionExpense());
		updateSeries(waterRevenue, "Import", year, 
				-getWaterSystem().getImportExpense());
		updateSeries(waterRevenue, "Out-Distribution", year, 
				-getWaterSystem().getDistributionExpense());
		updateSeries(waterRevenue, "Sales", year, 
				getWaterSystem().getSalesRevenue());
		updateSeries(waterNetRevenue, "Net Revenue", year, 
				getWaterSystem().getCashFlow());	

		updateSeries(waterUseData, "Society", year, 
				getSociety().getSocialSystem().getWaterConsumption());
		updateSeries(waterUseData, "Agriculture", year, 
				getSociety().getAgricultureSystem().getWaterConsumption());
		updateSeries(waterUseData, "Energy", year, 
				getSociety().getEnergySystem().getWaterConsumption());
		updateSeries(waterUseData, "Wasted", year, 
				getWaterSystem().getWaterWasted());
		
		if(getWaterSystem() instanceof CityWaterSystem) {
			for(WaterElement element : getWaterSystem().getInternalElements()) {
				if(element.getMaxWaterProduction() > 0) {
					updateSeries(waterSourceData, element.getName(), year, 
							element.getWaterProduction());
				}
				
				if(element.getMaxWaterInput() > 0) {
					updateSeries(waterUseData, element.getName(), year, 
							element.getWaterInput());
				}
			}
			for(WaterElement element : getWaterSystem().getExternalElements()) {
				if((getSociety().getName().equals(element.getDestination())
						&& element.getMaxWaterInput() > 0)
					|| element.getMaxWaterProduction() > 0) {
					updateSeries(waterSourceData, element.getName(), year, 
							element.getWaterOutput());
				}
			}
			updateSeries(waterReservoirDataset, getSociety().getName(), 
					year, getWaterSystem().getWaterReservoirVolume());
		} else {
			updateSeries(waterSourceData, "Production", year, 
					getWaterSystem().getWaterProduction());
			updateSeries(waterSourceData, "Distribution", year, 
					getWaterSystem().getWaterInDistribution());
			updateSeries(waterUseData, "Distribution", year,
					getWaterSystem().getWaterOutDistribution());
			for(WaterSystem.Local nestedSystem : getNestedWaterSystems()) {
				updateSeries(waterReservoirDataset, nestedSystem.getSociety().getName(), 
						year, nestedSystem.getWaterReservoirVolume());
			}
		}
		updateSeries(waterSourceData, "Artesian Well", year, 
				getWaterSystem().getWaterFromArtesianWell());
		updateSeries(waterSourceData, "Import", year, 
				getWaterSystem().getWaterImport());


		
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationInitialized(UpdateEvent event) {
		waterStatePanel.repaint();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		waterStatePanel.repaint();
	}
}
