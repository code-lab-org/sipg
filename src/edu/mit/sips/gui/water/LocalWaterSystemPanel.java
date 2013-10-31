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
import edu.mit.sips.sim.util.DefaultUnits;
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
				"Water Revenue (" + getCurrencyUnits() 
				+ "/" + getCurrencyTimeUnits() + ")", 
				waterRevenue, null, waterNetRevenue));
		addTab("Source", Icons.WATER_SOURCE, createStackedAreaChart(
				"Water Source (" + getWaterUnits() 
				+ "/" + getWaterTimeUnits() + ")", 
				waterSourceData));
		addTab("Use", Icons.WATER_USE, createStackedAreaChart(
				"Water Use (" + getWaterUnits() 
				+ "/" + getWaterTimeUnits() + ")", 
				waterUseData));
		addTab("Use", Icons.ELECTRICITY_USE, createStackedAreaChart(
				"Electricity Use (" + getElectricityUnits() 
				+ "/" + getElectricityTimeUnits() + ")",
				electricityUseData));

		addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Water Use Fraction (-)", localWaterData));
		addTab("Renewable", Icons.RENEWABLE, createTimeSeriesChart(
				"Renewable Water Fraction (-)", 
				renewableWaterData));
		addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Water Consumption per Capita (" + WaterUnits.L 
				+ "/" + TimeUnits.day + ")", 
				waterConsumptionPerCapita));
		addTab("Reservoir", Icons.WATER_RESERVOIR, createStackedAreaChart(
				"Water Reservoir Volume (" + getWaterUnits() + ")", 
				waterReservoirDataset));
		/* TODO
		addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
				"Unit Production Cost (SAR/m^3)", 
				waterProductCostData));
		addTab("Supply Profit", Icons.COST_SUPPLY, createTimeSeriesChart(
				"Unit Supply Profit (SAR/m^3)", 
				waterSupplyProfitData));
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

		waterReservoirIndicatorPanel.setValue(
				getWaterSystem().getWaterReservoirVolume());

		updateSeries(waterRevenue, "Capital", year, 
				WaterUnits.convertFlow(-getWaterSystem().getCapitalExpense(), 
						getWaterSystem(), this));
		updateSeries(waterRevenue, "Operations", year, 
				WaterUnits.convertFlow(-getWaterSystem().getOperationsExpense(), 
						getWaterSystem(), this));
		updateSeries(waterRevenue, "Decommission", year, 
				WaterUnits.convertFlow(-getWaterSystem().getDecommissionExpense(), 
						getWaterSystem(), this));
		updateSeries(waterRevenue, "Consumption", year, 
				WaterUnits.convertFlow(-getWaterSystem().getConsumptionExpense(), 
						getWaterSystem(), this));
		updateSeries(waterRevenue, "In-Distribution", year, 
				WaterUnits.convertFlow(-getWaterSystem().getDistributionExpense(), 
						getWaterSystem(), this));
		updateSeries(waterRevenue, "Import", year, 
				WaterUnits.convertFlow(-getWaterSystem().getImportExpense(), 
						getWaterSystem(), this));
		updateSeries(waterRevenue, "Out-Distribution", year, 
				WaterUnits.convertFlow(-getWaterSystem().getDistributionExpense(), 
						getWaterSystem(), this));
		updateSeries(waterRevenue, "Sales", year, 
				WaterUnits.convertFlow(getWaterSystem().getSalesRevenue(), 
						getWaterSystem(), this));
		updateSeries(waterNetRevenue, "Net Revenue", year, 
				WaterUnits.convertFlow(getWaterSystem().getCashFlow(), 
						getWaterSystem(), this));

		if(getNestedWaterSystems().isEmpty()) {
			updateSeries(waterUseData, "Society", year, 
					WaterUnits.convertFlow(getSociety().getSocialSystem().getWaterConsumption(),
							getSociety().getSocialSystem(), this));
			updateSeries(electricityUseData, getWaterSystem().getName(), year, 
					ElectricityUnits.convertFlow(getWaterSystem().getElectricityConsumptionFromPublicProduction(),
							getWaterSystem(), this));
			updateSeries(electricityUseData, getWaterSystem().getName() + " (Private)", year, 
					ElectricityUnits.convertFlow(getWaterSystem().getElectricityConsumptionFromPrivateProduction(),
							getWaterSystem(), this));
		} else {
			for(WaterSystem.Local nestedSystem : getNestedWaterSystems()) {
				updateSeries(waterUseData, nestedSystem.getSociety().getName() + " Society", year,
						WaterUnits.convertFlow(nestedSystem.getSociety().getSocialSystem().getWaterConsumption(), 
								nestedSystem.getSociety().getSocialSystem(), this));
				updateSeries(electricityUseData, nestedSystem.getName(), year, 
						ElectricityUnits.convertFlow(nestedSystem.getElectricityConsumptionFromPublicProduction(),
								nestedSystem, this));
				updateSeries(electricityUseData, nestedSystem.getName() + " (Private)", year, 
						ElectricityUnits.convertFlow(nestedSystem.getElectricityConsumptionFromPrivateProduction(),
								nestedSystem, this));
			}
		}
		updateSeries(waterUseData, "Agriculture", year, 
				WaterUnits.convertFlow(getSociety().getAgricultureSystem().getWaterConsumption(), 
						getSociety().getAgricultureSystem(), this));
		updateSeries(waterUseData, "Electricity", year, 
				WaterUnits.convertFlow(getSociety().getElectricitySystem().getWaterConsumption(), 
						getSociety().getElectricitySystem(), this));
		updateSeries(waterUseData, "Wasted", year, 
				WaterUnits.convertFlow(getWaterSystem().getWaterWasted(), getWaterSystem(), this));
		
		if(getWaterSystem() instanceof DefaultWaterSystem.Local) {
			for(WaterElement element : getWaterSystem().getInternalElements()) {
				if(element.getMaxWaterProduction() > 0) {
					updateSeries(waterSourceData, element.getName(), year, 
							WaterUnits.convertFlow(element.getWaterProduction(), element, this));
				}
				
				if(element.getMaxWaterInput() > 0) {
					updateSeries(waterUseData, element.getName(), year, 
							WaterUnits.convertFlow(element.getWaterInput(), element, this));
				}
			}
			for(WaterElement element : getWaterSystem().getExternalElements()) {
				if((getSociety().getName().equals(element.getDestination())
						&& element.getMaxWaterInput() > 0)
					|| element.getMaxWaterProduction() > 0) {
					updateSeries(waterSourceData, element.getName(), year, 
							WaterUnits.convertFlow(element.getWaterOutput(), element, this));
				}
			}
			updateSeries(waterReservoirDataset, "Reservoir", year, 
					WaterUnits.convertStock(getWaterSystem().getWaterReservoirVolume(), 
							getWaterSystem(), this));
		} else {
			updateSeries(waterSourceData, "Production", year, 
					WaterUnits.convertFlow(getWaterSystem().getWaterProduction(), 
							getWaterSystem(), this));
			updateSeries(waterSourceData, "Distribution", year, 
					WaterUnits.convertFlow(getWaterSystem().getWaterInDistribution(), 
							getWaterSystem(), this));
			updateSeries(waterUseData, "Distribution", year,
					WaterUnits.convertFlow(getWaterSystem().getWaterOutDistribution(), 
							getWaterSystem(), this));
			updateSeries(waterUseData, "Distribution Losses", year, 
					WaterUnits.convertFlow(getWaterSystem().getWaterOutDistributionLosses(), 
							getWaterSystem(), this));
			for(WaterSystem.Local nestedSystem : getNestedWaterSystems()) {
				updateSeries(waterReservoirDataset, nestedSystem.getSociety().getName(), year, 
						WaterUnits.convertFlow(nestedSystem.getWaterReservoirVolume(), 
								getWaterSystem(), this));
			}
		}
		updateSeries(waterSourceData, "Artesian Well", year, 
				WaterUnits.convertFlow(getWaterSystem().getWaterFromPrivateProduction(), 
						getWaterSystem(), this));
		updateSeries(waterSourceData, "Import", year, 
				WaterUnits.convertFlow(getWaterSystem().getWaterImport(), 
						getWaterSystem(), this));
	}
}
