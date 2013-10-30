package edu.mit.sips.gui.water;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Society;
import edu.mit.sips.core.water.DefaultWaterSystem;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.gui.LinearIndicatorPanel;
import edu.mit.sips.gui.SpatialStatePanel;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Class WaterSystemPanel.
 */
public class LocalWaterSystemPanel extends WaterSystemPanel
implements CurrencyUnitsOutput, WaterUnitsOutput, ElectricityUnitsOutput {	
	private static final long serialVersionUID = -3665986046863585665L;
	
	private final LinearIndicatorPanel localWaterIndicatorPanel, 
	waterReservoirIndicatorPanel, renewableWaterIndicatorPanel;
	
	private final SpatialStatePanel waterStatePanel;

	DefaultTableXYDataset waterSourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset waterNetRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset waterReservoirDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityUseData = new DefaultTableXYDataset();
	
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
	public LocalWaterSystemPanel(WaterSystem.Local waterSystem) {
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
		addTab("Indicators", Icons.INDICATORS, indicatorsPanel);
		
		waterStatePanel = new SpatialStatePanel(
				waterSystem.getSociety(), new WaterStateProvider());
		addTab("Network Flow", Icons.NETWORK, waterStatePanel);
		
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Water Revenue (" + getCurrencyUnitsNumerator() 
				+ "/" + getCurrencyUnitsDenominator() + ")", 
				waterRevenue, null, waterNetRevenue));
		addTab("Source", Icons.WATER_SOURCE, createStackedAreaChart(
				"Water Source (" + getWaterUnitsNumerator() 
				+ "/" + getWaterUnitsDenominator() + ")", 
				waterSourceData));
		addTab("Use", Icons.WATER_USE, createStackedAreaChart(
				"Water Use (" + getWaterUnitsNumerator() 
				+ "/" + getWaterUnitsDenominator() + ")", 
				waterUseData));
		addTab("Use", Icons.ELECTRICITY_USE, createStackedAreaChart(
				"Electricity Use (" + getElectricityUnitsNumerator() 
				+ "/" + getElectricityUnitsDenominator() + ")",
				electricityUseData));

		addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Water Use Fraction (-)", localWaterData));
		addTab("Renewable", Icons.RENEWABLE, createTimeSeriesChart(
				"Renewable Water Fraction (-)", 
				renewableWaterData));
		addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Water Consumption per Capita (m^3/person)", 
				waterConsumptionPerCapita));
		addTab("Reservoir", Icons.WATER_RESERVOIR, createStackedAreaChart(
				"Water Reservoir Volume (m^3)", waterReservoirDataset));
		/* TODO
		addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
				"Unit Production Cost (SAR/m^3)", 
				waterProductCostData));
		addTab("Supply Profit", Icons.COST_SUPPLY, createTimeSeriesChart(
				"Unit Supply Profit (SAR/m^3)", 
				waterSupplyProfitData));
		*/
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
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#initialize()
	 */
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
		electricityUseData.removeAllSeries();
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
		waterStatePanel.repaint();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		waterStatePanel.repaint();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#update(int)
	 */
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

		if(getSociety().getSocialSystem().getPopulation() > 0) {
			updateSeriesCollection(waterConsumptionPerCapita, getSociety().getName(), 
					year, getSociety().getSocialSystem().getWaterConsumption() 
					/ getSociety().getSocialSystem().getPopulation());
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				if(nestedSociety.getSocialSystem().getPopulation() > 0) {
					updateSeriesCollection(waterConsumptionPerCapita, nestedSociety.getName(), 
							year, nestedSociety.getSocialSystem().getWaterConsumption() 
							/ nestedSociety.getSocialSystem().getPopulation());
				}
			}
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

		if(getNestedWaterSystems().isEmpty()) {
			updateSeries(waterUseData, "Society", year, 
					WaterUnits.convert(getSociety().getSocialSystem().getWaterConsumption(),
							getSociety().getSocialSystem(), this));
			updateSeries(electricityUseData, getWaterSystem().getName(), year, 
					ElectricityUnits.convert(getWaterSystem().getElectricityConsumption(),
							getWaterSystem(), this));
		} else {
			for(WaterSystem.Local nestedSystem : getNestedWaterSystems()) {
				updateSeries(waterUseData, nestedSystem.getSociety().getName() + " Society", year,
						WaterUnits.convert(nestedSystem.getSociety().getSocialSystem().getWaterConsumption(), 
								nestedSystem.getSociety().getSocialSystem(), this));
				updateSeries(electricityUseData, nestedSystem.getName(), year, 
						ElectricityUnits.convert(nestedSystem.getElectricityConsumption(),
								nestedSystem, this));
			}
		}
		updateSeries(waterUseData, "Agriculture", year, 
				getSociety().getAgricultureSystem().getWaterConsumption());
		updateSeries(waterUseData, "Electricity", year, 
				getSociety().getElectricitySystem().getWaterConsumption());
		updateSeries(waterUseData, "Wasted", year, 
				getWaterSystem().getWaterWasted());
		
		if(getWaterSystem() instanceof DefaultWaterSystem.Local) {
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
			updateSeries(waterReservoirDataset, "Reservoir", 
					year, getWaterSystem().getWaterReservoirVolume());
		} else {
			updateSeries(waterSourceData, "Production", year, 
					getWaterSystem().getWaterProduction());
			updateSeries(waterSourceData, "Distribution", year, 
					getWaterSystem().getWaterInDistribution());
			updateSeries(waterUseData, "Distribution", year,
					getWaterSystem().getWaterOutDistribution());
			updateSeries(waterUseData, "Distribution Losses", year, 
					getWaterSystem().getWaterOutDistributionLosses());
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
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsNumerator()
	 */
	@Override
	public WaterUnits.NumeratorUnits getWaterUnitsNumerator() {
		return WaterUnits.NumeratorUnits.m3;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsDenominator()
	 */
	@Override
	public WaterUnits.DenominatorUnits getWaterUnitsDenominator() {
		return WaterUnits.DenominatorUnits.year;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnitsNumerator()
	 */
	@Override
	public ElectricityUnits.NumeratorUnits getElectricityUnitsNumerator() {
		return ElectricityUnits.NumeratorUnits.MWh;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnitsDenominator()
	 */
	@Override
	public ElectricityUnits.DenominatorUnits getElectricityUnitsDenominator() {
		return ElectricityUnits.DenominatorUnits.year;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsNumerator()
	 */
	@Override
	public CurrencyUnits.NumeratorUnits getCurrencyUnitsNumerator() {
		return CurrencyUnits.NumeratorUnits.sim;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsDenominator()
	 */
	@Override
	public CurrencyUnits.DenominatorUnits getCurrencyUnitsDenominator() {
		return CurrencyUnits.DenominatorUnits.year;
	}
}
