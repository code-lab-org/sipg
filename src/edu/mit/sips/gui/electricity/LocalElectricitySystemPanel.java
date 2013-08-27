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

/**
 * The Class ElectricitySystemPanel.
 */
public class LocalElectricitySystemPanel extends ElectricitySystemPanel {
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

		addTab("Revenue", Icons.REVENUE, 
				createStackedAreaChart("Electricity Revenue (SAR/year)", 
				electricityRevenue, null, electricityNetRevenue));
		addTab("Source", Icons.ELECTRICITY_SOURCE, createStackedAreaChart(
				"Electricity Source (toe/year)", electricitySourceData));
		addTab("Use", Icons.ELECTRICITY_USE, createStackedAreaChart(
				"Electricity Use (toe/year)", electricityUseData));

		addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Electricity Use Fraction (-)", 
				localElectricityData));
		addTab("Renewable", Icons.RENEWABLE, createTimeSeriesChart(
				"Renewable Electricity Fraction (-)", 
				renewableElectricityData));
		addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Electricity Consumption per Capita (toe/person)", 
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
	
	/**
	 * Gets the electricity system.
	 *
	 * @return the electricity system
	 */
	public ElectricitySystem.Local getElectricitySystem() {
		return (ElectricitySystem.Local) getInfrastructureSystem();
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
			updateSeriesCollection(localElectricityData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getLocalElectricityFraction());
		}
		
		updateSeriesCollection(renewableElectricityData, getSociety().getName(), 
				year, getElectricitySystem().getRenewableElectricityFraction());
		renewableElectricityIndicatorPanel.setValue(getElectricitySystem().getRenewableElectricityFraction());
		for(ElectricitySystem.Local nestedSystem : getNestedElectricitySystems()) {
			updateSeriesCollection(renewableElectricityData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getRenewableElectricityFraction());
		}
		updateSeriesCollection(electricityProductCostData, getSociety().getName(), 
				year, getElectricitySystem().getUnitProductionCost());
		for(ElectricitySystem.Local nestedSystem : getNestedElectricitySystems()) {
			updateSeriesCollection(electricityProductCostData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getUnitProductionCost());
		}

		updateSeriesCollection(electricitySupplyProfitData, getSociety().getName(), 
				year, getElectricitySystem().getUnitSupplyProfit());
		for(ElectricitySystem.Local nestedSystem : getNestedElectricitySystems()) {
			updateSeriesCollection(electricitySupplyProfitData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getUnitSupplyProfit());
		}
		
		updateSeriesCollection(electricityConsumptionPerCapita, getSociety().getName(), 
				year, getSociety().getSocialSystem().getElectricityConsumptionPerCapita());
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			updateSeriesCollection(electricityConsumptionPerCapita, nestedSociety.getName(), year, 
					nestedSociety.getSocialSystem().getElectricityConsumptionPerCapita());
		}
		
		updateSeries(electricityRevenue, "Capital", year, 
				-getElectricitySystem().getCapitalExpense());
		updateSeries(electricityRevenue, "Operations", year, 
				-getElectricitySystem().getOperationsExpense());
		updateSeries(electricityRevenue, "Decommission", year, 
				-getElectricitySystem().getDecommissionExpense());
		updateSeries(electricityRevenue, "Consumption", year, 
				-getElectricitySystem().getConsumptionExpense());
		updateSeries(electricityRevenue, "In-Distribution", year, 
				-getElectricitySystem().getDistributionExpense());
		updateSeries(electricityRevenue, "Import", year, 
				-getElectricitySystem().getImportExpense());
		updateSeries(electricityRevenue, "Out-Distribution", year, 
				getElectricitySystem().getDistributionRevenue());
		updateSeries(electricityRevenue, "Export", year,
				getElectricitySystem().getExportRevenue());
		updateSeries(electricityRevenue, "Sales", year, 
				getElectricitySystem().getSalesRevenue());
		updateSeries(electricityNetRevenue, "Net Revenue", year, 
				getElectricitySystem().getCashFlow());
		
		if(getElectricitySystem() instanceof DefaultElectricitySystem.Local) {
			for(ElectricityElement element : getElectricitySystem().getInternalElements()) {
				if(element.getMaxElectricityProduction() > 0) {
					updateSeries(electricitySourceData, element.getName(), year, 
							element.getElectricityProduction());
				}
				
				if(element.getMaxElectricityInput() > 0) {
					updateSeries(electricityUseData, element.getName(), year, 
							element.getElectricityInput());
				}
			}

			for(ElectricityElement element : getElectricitySystem().getExternalElements()) {
				if(element.getMaxElectricityInput() > 0) {
					updateSeries(electricitySourceData, element.getName(), year, 
							element.getElectricityOutput());
				}
			}
		} else {
			updateSeries(electricitySourceData, "Production", year, 
					getElectricitySystem().getElectricityProduction());
			updateSeries(electricitySourceData, "Distribution", year, 
					getElectricitySystem().getElectricityInDistribution());
			updateSeries(electricityUseData, "Distribution", year, 
					getElectricitySystem().getElectricityOutDistribution());
		}
		
		updateSeries(electricityRevenue, "Capital", year, 
				-getElectricitySystem().getCapitalExpense());
		updateSeries(electricityRevenue, "Operations", year, 
				-getElectricitySystem().getOperationsExpense());
		updateSeries(electricityRevenue, "Decommission", year, 
				-getElectricitySystem().getDecommissionExpense());
		updateSeries(electricityRevenue, "Consumption", year, 
				-getElectricitySystem().getConsumptionExpense());
		updateSeries(electricityRevenue, "In-Distribution", year, 
				-getElectricitySystem().getDistributionExpense());
		updateSeries(electricityRevenue, "Out-Distribution", year, 
				getElectricitySystem().getDistributionRevenue());
		updateSeries(electricityRevenue, "Sales", year, 
				getElectricitySystem().getSalesRevenue());
		updateSeries(electricityNetRevenue, "Net Revenue", year, 
				getElectricitySystem().getCashFlow());
		updateSeries(electricityUseData, "Society", year, 
				getSociety().getSocialSystem().getElectricityConsumption());
		updateSeries(electricityUseData, "Water", year, 
				getSociety().getWaterSystem().getElectricityConsumption());
		updateSeries(electricityUseData, "Petroleum", year, 
				getSociety().getPetroleumSystem().getElectricityConsumption());
		updateSeries(electricityUseData, "Wasted", year, 
				getElectricitySystem().getElectricityWasted());
		
		updateSeries(electricitySourceData, "Petroleum Burn", year, 
				getElectricitySystem().getElectricityFromBurningPetroleum());
	}
}
