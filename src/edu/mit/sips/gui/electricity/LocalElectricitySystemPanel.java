package edu.mit.sips.gui.electricity;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Society;
import edu.mit.sips.core.electricity.DefaultElectricitySystem;
import edu.mit.sips.core.electricity.ElectricityElement;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.gui.LinearIndicatorPanel;
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

/**
 * The Class ElectricitySystemPanel.
 */
public class LocalElectricitySystemPanel extends ElectricitySystemPanel 
		implements CurrencyUnitsOutput, ElectricityUnitsOutput, OilUnitsOutput {
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
	
	private final CurrencyUnits currencyUnits = CurrencyUnits.Bsim;
	private final TimeUnits currencyTimeUnits = TimeUnits.year;
	private final ElectricityUnits electricityUnits = ElectricityUnits.TWh;
	private final TimeUnits electricityTimeUnits = TimeUnits.year;
	private final OilUnits oilUnits = OilUnits.Mtoe;
	private final TimeUnits oilTimeUnits = TimeUnits.year;

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
		addTab("Indicators", Icons.INDICATORS, indicatorsPanel);
		
		electricityStatePanel = new SpatialStatePanel(
				getSociety(), new ElectricityStateProvider());
		addTab("Network Flow", Icons.NETWORK, electricityStatePanel);

		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Electricity Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")", 
				electricityRevenue, null, electricityNetRevenue));
		addTab("Source", Icons.ELECTRICITY_SOURCE, createStackedAreaChart(
				"Electricity Source (" + electricityUnits + "/" + electricityTimeUnits + ")", 
				electricitySourceData));
		addTab("Use", Icons.ELECTRICITY_USE, createStackedAreaChart(
				"Electricity Use (" + electricityUnits + "/" + electricityTimeUnits + ")", 
				electricityUseData));

		addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Electricity Use Fraction (-)", 
				localElectricityData));
		addTab("Renewable", Icons.RENEWABLE, createTimeSeriesChart(
				"Renewable Electricity Fraction (-)", 
				renewableElectricityData));
		addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Electricity Consumption per Capita (" + 
						ElectricityUnits.kWh + "/" + TimeUnits.day + ")", 
				electricityConsumptionPerCapita), "Consumption");
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
			if(nestedSociety.getWaterSystem() instanceof ElectricitySystem.Local) {
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
		
		updateSeries(electricityRevenue, "Capital", year, 
				CurrencyUnits.convertFlow(-getElectricitySystem().getCapitalExpense(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Operations", year, 
				CurrencyUnits.convertFlow(-getElectricitySystem().getOperationsExpense(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Decommission", year, 
				CurrencyUnits.convertFlow(-getElectricitySystem().getDecommissionExpense(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Consumption", year, 
				CurrencyUnits.convertFlow(-getElectricitySystem().getConsumptionExpense(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "In-Distribution", year, 
				CurrencyUnits.convertFlow(-getElectricitySystem().getDistributionExpense(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Import", year, 
				CurrencyUnits.convertFlow(-getElectricitySystem().getImportExpense(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Out-Distribution", year, 
				CurrencyUnits.convertFlow(getElectricitySystem().getDistributionRevenue(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Export", year,
				CurrencyUnits.convertFlow(getElectricitySystem().getExportRevenue(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Sales", year, 
				CurrencyUnits.convertFlow(getElectricitySystem().getSalesRevenue(), 
						getElectricitySystem(), this));
		updateSeries(electricityNetRevenue, "Net Revenue", year, 
				CurrencyUnits.convertFlow(getElectricitySystem().getCashFlow(), 
						getElectricitySystem(), this));
		
		if(getElectricitySystem() instanceof DefaultElectricitySystem.Local) {
			for(ElectricityElement element : getElectricitySystem().getInternalElements()) {
				if(element.getMaxElectricityProduction() > 0) {
					updateSeries(electricitySourceData, element.getName(), year, 
							ElectricityUnits.convertFlow(
									element.getElectricityProduction(), element, this));
				}
				
				if(element.getMaxElectricityInput() > 0) {
					updateSeries(electricityUseData, element.getName(), year, 
							ElectricityUnits.convertFlow(
									element.getElectricityInput(), element, this));
				}
			}

			for(ElectricityElement element : getElectricitySystem().getExternalElements()) {
				if(element.getMaxElectricityInput() > 0) {
					updateSeries(electricitySourceData, element.getName(), year, 
							ElectricityUnits.convertFlow(
									element.getElectricityOutput(), element, this));
				}
			}
		} else {
			updateSeries(electricitySourceData, "Production", year, 
					ElectricityUnits.convertFlow(
							getElectricitySystem().getElectricityProduction(),
							getElectricitySystem(), this));
			updateSeries(electricitySourceData, "Distribution", year, 
					ElectricityUnits.convertFlow(
							getElectricitySystem().getElectricityInDistribution(),
							getElectricitySystem(), this));
			updateSeries(electricityUseData, "Distribution", year, 
					ElectricityUnits.convertFlow(
							getElectricitySystem().getElectricityOutDistribution(),
							getElectricitySystem(), this));
			updateSeries(electricityUseData, "Distribution Losses", year, 
					ElectricityUnits.convertFlow(
							getElectricitySystem().getElectricityOutDistributionLosses(),
							getElectricitySystem(), this));
		}
		
		updateSeries(electricityRevenue, "Capital", year, 
				CurrencyUnits.convertFlow(
						-getElectricitySystem().getCapitalExpense(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Operations", year, 
				CurrencyUnits.convertFlow(
						-getElectricitySystem().getOperationsExpense(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Decommission", year, 
				CurrencyUnits.convertFlow(
						-getElectricitySystem().getDecommissionExpense(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Consumption", year, 
				CurrencyUnits.convertFlow(
						-getElectricitySystem().getConsumptionExpense(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "In-Distribution", year, 
				CurrencyUnits.convertFlow(
						-getElectricitySystem().getDistributionExpense(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Out-Distribution", year, 
				CurrencyUnits.convertFlow(
						getElectricitySystem().getDistributionRevenue(), 
						getElectricitySystem(), this));
		updateSeries(electricityRevenue, "Sales", year, 
				CurrencyUnits.convertFlow(
						getElectricitySystem().getSalesRevenue(), 
						getElectricitySystem(), this));
		updateSeries(electricityNetRevenue, "Net Revenue", year, 
				CurrencyUnits.convertFlow(
						getElectricitySystem().getCashFlow(), 
						getElectricitySystem(), this));
		updateSeries(electricityUseData, "Society", year, 
				ElectricityUnits.convertFlow(
						getSociety().getSocialSystem().getElectricityConsumption(),
						getSociety().getSocialSystem(), this));
		updateSeries(electricityUseData, "Water", year,  
				ElectricityUnits.convertFlow(
						getSociety().getWaterSystem().getElectricityConsumption(),
						getSociety().getWaterSystem(), this));
		updateSeries(electricityUseData, "Petroleum", year,  
				ElectricityUnits.convertFlow(
						getSociety().getPetroleumSystem().getElectricityConsumption(),
						getSociety().getPetroleumSystem(), this));
		updateSeries(electricityUseData, "Wasted", year,  
				ElectricityUnits.convertFlow(
						getElectricitySystem().getElectricityWasted(),
						getElectricitySystem(), this));
		updateSeries(electricitySourceData, "Petroleum Burn", year, 
				ElectricityUnits.convertFlow(
						getElectricitySystem().getElectricityFromPrivateProduction(),
						getElectricitySystem(), this));
	}
}
