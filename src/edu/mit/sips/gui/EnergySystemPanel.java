package edu.mit.sips.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Society;
import edu.mit.sips.core.energy.CityEnergySystem;
import edu.mit.sips.core.energy.ElectricityElement;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.energy.PetroleumElement;
import edu.mit.sips.io.Icons;

/**
 * The Class EnergySystemPanel.
 */
public class EnergySystemPanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = 2218175276232419659L;
	
	private final LinearIndicatorPanel petroleumReservoirIndicatorPanel, 
	renewableEnergyIndicatorPanel, localEnergyIndicatorPanel, 
	electricityConsumptionIndicatorPanel;
	
	private final SpatialStatePanel petroleumStatePanel, electricityStatePanel;
	
	TimeSeriesCollection petroleumProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection petroleumSupplyCostData = new TimeSeriesCollection();
	TimeSeriesCollection localElectricityData = new TimeSeriesCollection();
	TimeSeriesCollection electricityProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection electricitySupplyCostData = new TimeSeriesCollection();
	TimeSeriesCollection renewableElectricityData = new TimeSeriesCollection();
	TimeSeriesCollection electricityConsumptionPerCapita = new TimeSeriesCollection();
	
	DefaultTableXYDataset petroleumReservoirDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumSourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumNetRevenue = new DefaultTableXYDataset();

	DefaultTableXYDataset electricitySourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityNetRevenue = new DefaultTableXYDataset();

	public EnergySystemPanel(EnergySystem.Local energySystem) {
		super(energySystem);

		JPanel indicatorsPanel = new JPanel();
		indicatorsPanel.setLayout(
				new BoxLayout(indicatorsPanel, BoxLayout.LINE_AXIS));
		petroleumReservoirIndicatorPanel = new LinearIndicatorPanel(
				"Oil Reservoir", 0, 
				energySystem.getPetroleumSystem().getMaxPetroleumReservoirVolume());
		renewableEnergyIndicatorPanel = new LinearIndicatorPanel(
				"Renewable Energy", 0, 1);
		localEnergyIndicatorPanel = new LinearIndicatorPanel(
				"Energy Independence", 0, 1);
		electricityConsumptionIndicatorPanel = new LinearIndicatorPanel(
				"Energy Consumption", 
				getSociety().getGlobals().getMinElectricityDemandPerCapita(), 
				getSociety().getGlobals().getMaxElectricityDemandPerCapita());
		indicatorsPanel.add(petroleumReservoirIndicatorPanel);
		indicatorsPanel.add(renewableEnergyIndicatorPanel);
		indicatorsPanel.add(localEnergyIndicatorPanel);
		indicatorsPanel.add(electricityConsumptionIndicatorPanel);
		addTab("Indicators", Icons.INDICATORS, indicatorsPanel);

		JTabbedPane petroleumPane = new JTabbedPane();
		addTab(energySystem.getPetroleumSystem().getName(), 
				Icons.PETROLEUM, petroleumPane);
		
		petroleumStatePanel = new SpatialStatePanel(getSociety(), 
				new PetroleumStateProvider());
		petroleumPane.addTab("Network Flow", Icons.NETWORK, petroleumStatePanel);

		petroleumPane.addTab("Revenue", Icons.REVENUE, 
				createStackedAreaChart("Petroleum Revenue (SAR/year)", 
				petroleumRevenue, petroleumNetRevenue));
		petroleumPane.addTab("Source", Icons.PETROLEUM_SOURCE, createStackedAreaChart(
				"Petroleum Source (bbl/year)", petroleumSourceData));
		petroleumPane.addTab("Use", Icons.PETROLEUM_USE, createStackedAreaChart(
				"Petroleum Use (bbl/year)", petroleumUseData));
		
		petroleumPane.addTab("Reservoir", Icons.PETROLEUM_RESERVOIR, createStackedAreaChart(
				"Oil Reservoir Volume (bbl)", 
				petroleumReservoirDataset), "Reservoir");
		petroleumPane.addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
				"Direct Petroleum Production Cost (SAR/bbl)", 
				petroleumProductCostData));
		petroleumPane.addTab("Supply Cost", Icons.COST_SUPPLY, createTimeSeriesChart(
				"Direct Petroleum Supply Cost (SAR/bbl)", 
				petroleumSupplyCostData));
		
		JTabbedPane electricityPane = new JTabbedPane();
		addTab(energySystem.getElectricitySystem().getName(), 
				Icons.ELECTRICITY, electricityPane);
		
		electricityStatePanel = new SpatialStatePanel(
				getSociety(), new ElectricityStateProvider());
		electricityPane.addTab("Network Flow", Icons.NETWORK, electricityStatePanel);

		electricityPane.addTab("Revenue", Icons.REVENUE, 
				createStackedAreaChart("Electricity Revenue (SAR/year)", 
				electricityRevenue, electricityNetRevenue));
		electricityPane.addTab("Source", Icons.ELECTRICITY_SOURCE, createStackedAreaChart(
				"Electricity Source (MWh/year)", electricitySourceData));
		electricityPane.addTab("Use", Icons.ELECTRICITY_USE, createStackedAreaChart(
				"Electricity Use (MWh/year)", electricityUseData));

		electricityPane.addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Electricity Use Fraction (-)", 
				localElectricityData));
		electricityPane.addTab("Renewable", Icons.RENEWABLE, createTimeSeriesChart(
				"Renewable Electricity Production Fraction (-)", 
				renewableElectricityData));
		electricityPane.addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Electricity Consumption per Capita (MWh/person)", 
				electricityConsumptionPerCapita), "Consumption");
		electricityPane.addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
				"Direct Electricity Production Cost (SAR/MWh)", 
				electricityProductCostData));
		electricityPane.addTab("Supply Cost", Icons.COST_SUPPLY, createTimeSeriesChart(
				"Direct Electricity Supply Cost (SAR/MWh)", 
				electricitySupplyCostData));
	}
	
	/**
	 * Gets the energy system.
	 *
	 * @return the energy system
	 */
	public EnergySystem.Local getEnergySystem() {
		return (EnergySystem.Local) getInfrastructureSystem();
	}

	@Override
	public void simulationInitialized(UpdateEvent event) {
		petroleumStatePanel.repaint();
		electricityStatePanel.repaint();
	}

	@Override
	public void simulationUpdated(UpdateEvent event) {
		petroleumStatePanel.repaint();
		electricityStatePanel.repaint();
	}

	@Override
	public void initialize() {
		petroleumReservoirIndicatorPanel.initialize();
		renewableEnergyIndicatorPanel.initialize();
		localEnergyIndicatorPanel.initialize();
		electricityConsumptionIndicatorPanel.initialize();
		petroleumProductCostData.removeAllSeries();
		petroleumSupplyCostData.removeAllSeries();
		localElectricityData.removeAllSeries();
		renewableElectricityData.removeAllSeries();
		electricityProductCostData.removeAllSeries();
		electricitySupplyCostData.removeAllSeries();
		electricityConsumptionPerCapita.removeAllSeries();
		petroleumReservoirDataset.removeAllSeries();
		electricityUseData.removeAllSeries();
		electricitySourceData.removeAllSeries();
		electricityRevenue.removeAllSeries();
		electricityNetRevenue.removeAllSeries();
		petroleumUseData.removeAllSeries();
		petroleumSourceData.removeAllSeries();
		petroleumRevenue.removeAllSeries();
		petroleumNetRevenue.removeAllSeries();
	}

	/**
	 * Gets the nested energy systems.
	 *
	 * @return the nested energy systems
	 */
	private List<EnergySystem.Local> getNestedEnergySystems() {
		List<EnergySystem.Local> systems = new ArrayList<EnergySystem.Local>();
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			if(nestedSociety.getWaterSystem() instanceof EnergySystem.Local) {
				systems.add((EnergySystem.Local)nestedSociety.getEnergySystem());
			}
		}
		return systems;
	}
	
	@Override
	public void update(int year) {
		updateSeriesCollection(localElectricityData, getSociety().getName(), 
				year, getEnergySystem().getElectricitySystem().getLocalElectricityFraction());
		localEnergyIndicatorPanel.setValue(
				getEnergySystem().getElectricitySystem().getLocalElectricityFraction());
		for(EnergySystem.Local nestedSystem : getNestedEnergySystems()) {
			updateSeriesCollection(localElectricityData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getElectricitySystem().getLocalElectricityFraction());
		}
		
		updateSeriesCollection(renewableElectricityData, getSociety().getName(), 
				year, getEnergySystem().getElectricitySystem().getRenewableElectricityFraction());
		renewableEnergyIndicatorPanel.setValue(getEnergySystem().getElectricitySystem().getRenewableElectricityFraction());
		for(EnergySystem.Local nestedSystem : getNestedEnergySystems()) {
			updateSeriesCollection(renewableElectricityData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getElectricitySystem().getRenewableElectricityFraction());
		}

		updateSeriesCollection(petroleumProductCostData, getSociety().getName(), 
				year, getEnergySystem().getPetroleumSystem().getProductionCost());
		for(EnergySystem.Local nestedSystem : getNestedEnergySystems()) {
			updateSeriesCollection(petroleumProductCostData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getPetroleumSystem().getProductionCost());
		}
		
		updateSeriesCollection(petroleumSupplyCostData, getSociety().getName(), 
				year, getEnergySystem().getPetroleumSystem().getSupplyCost());
		for(EnergySystem.Local nestedSystem : getNestedEnergySystems()) {
			updateSeriesCollection(petroleumSupplyCostData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getPetroleumSystem().getSupplyCost());
		}

		updateSeriesCollection(electricityProductCostData, getSociety().getName(), 
				year, getEnergySystem().getElectricitySystem().getProductionCost());
		for(EnergySystem.Local nestedSystem : getNestedEnergySystems()) {
			updateSeriesCollection(electricityProductCostData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getElectricitySystem().getProductionCost());
		}

		updateSeriesCollection(electricitySupplyCostData, getSociety().getName(), 
				year, getEnergySystem().getElectricitySystem().getSupplyCost());
		for(EnergySystem.Local nestedSystem : getNestedEnergySystems()) {
			updateSeriesCollection(electricitySupplyCostData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getElectricitySystem().getSupplyCost());
		}
		
		updateSeriesCollection(electricityConsumptionPerCapita, getSociety().getName(), 
				year, getSociety().getSocialSystem().getElectricityConsumptionPerCapita());
		electricityConsumptionIndicatorPanel.setValue(
				getSociety().getSocialSystem().getElectricityConsumptionPerCapita());
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			updateSeriesCollection(electricityConsumptionPerCapita, nestedSociety.getName(), year, 
					nestedSociety.getSocialSystem().getElectricityConsumptionPerCapita());
		}
		
		petroleumReservoirIndicatorPanel.setValue(
				getEnergySystem().getPetroleumSystem().getPetroleumReservoirVolume());
		
		if(getEnergySystem() instanceof CityEnergySystem) {
			updateSeries(petroleumReservoirDataset, getSociety().getName(), year, 
					getEnergySystem().getPetroleumSystem().getPetroleumReservoirVolume());
			
			for(ElectricityElement element : getEnergySystem().getElectricitySystem().getInternalElements()) {
				if(element.getMaxElectricityProduction() > 0) {
					updateSeries(electricitySourceData, element.getName(), year, 
							element.getElectricityProduction());
				}
				
				if(element.getMaxElectricityInput() > 0) {
					updateSeries(electricityUseData, element.getName(), year, 
							element.getElectricityInput());
				}
			}

			for(ElectricityElement element : getEnergySystem().getElectricitySystem().getExternalElements()) {
				if(element.getMaxElectricityInput() > 0) {
					updateSeries(electricitySourceData, element.getName(), year, 
							element.getElectricityOutput());
				}
			}
			
			for(PetroleumElement element : getEnergySystem().getPetroleumSystem().getInternalElements()) {
				if(element.getMaxPetroleumProduction() > 0) {
					updateSeries(petroleumSourceData, element.getName(), year, 
							element.getPetroleumProduction());
				}
				
				if(element.getMaxPetroleumInput() > 0) {
					updateSeries(petroleumUseData, element.getName(), year, 
							element.getPetroleumInput());
				}
			}
			
			for(PetroleumElement element : getEnergySystem().getPetroleumSystem().getExternalElements()) {
				if(element.getMaxPetroleumInput() > 0) {
					updateSeries(petroleumSourceData, element.getName(), year, 
							element.getPetroleumOutput());
				}
			}
		} else {
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(petroleumReservoirDataset, nestedSociety.getName(), year, 
						getEnergySystem().getPetroleumSystem().getPetroleumReservoirVolume());
			}

			updateSeries(electricitySourceData, "Production", year, 
					getEnergySystem().getElectricitySystem().getElectricityProduction());
			updateSeries(electricitySourceData, "Distribution", year, 
					getEnergySystem().getElectricitySystem().getElectricityInDistribution());
			updateSeries(electricityUseData, "Distribution", year, 
					getEnergySystem().getElectricitySystem().getElectricityOutDistribution());

			updateSeries(petroleumSourceData, "Production", year, 
					getEnergySystem().getPetroleumSystem().getPetroleumProduction());
			updateSeries(petroleumSourceData, "Distribution", year, 
					getEnergySystem().getPetroleumSystem().getPetroleumInDistribution());
			updateSeries(petroleumUseData, "Distribution", year, 
					getEnergySystem().getPetroleumSystem().getPetroleumOutDistribution());
		}
		
		updateSeries(electricityRevenue, "Capital", year, 
				-getEnergySystem().getElectricitySystem().getCapitalExpense());
		updateSeries(electricityRevenue, "Operations", year, 
				-getEnergySystem().getElectricitySystem().getOperationsExpense());
		updateSeries(electricityRevenue, "Decommission", year, 
				-getEnergySystem().getElectricitySystem().getDecommissionExpense());
		updateSeries(electricityRevenue, "Consumption", year, 
				-getEnergySystem().getElectricitySystem().getConsumptionExpense());
		updateSeries(electricityRevenue, "In-Distribution", year, 
				-getEnergySystem().getElectricitySystem().getDistributionExpense());
		updateSeries(electricityRevenue, "Out-Distribution", year, 
				getEnergySystem().getElectricitySystem().getDistributionRevenue());
		updateSeries(electricityRevenue, "Sales", year, 
				getEnergySystem().getElectricitySystem().getSalesRevenue());
		updateSeries(electricityNetRevenue, "Net Revenue", year, 
				getEnergySystem().getElectricitySystem().getCashFlow());
		updateSeries(electricityUseData, "Society", year, 
				getSociety().getSocialSystem().getElectricityConsumption());
		updateSeries(electricityUseData, "Water", year, 
				getSociety().getWaterSystem().getElectricityConsumption());
		updateSeries(electricityUseData, "Petroleum", year, 
				getEnergySystem().getPetroleumSystem().getElectricityConsumption());
		updateSeries(electricityUseData, "Wasted", year, 
				getEnergySystem().getElectricitySystem().getElectricityWasted());
		
		updateSeries(electricitySourceData, "Petroleum Burn", year, 
				getEnergySystem().getElectricitySystem().getElectricityFromBurningPetroleum());
		
		updateSeries(petroleumRevenue, "Capital", year, 
				-getEnergySystem().getPetroleumSystem().getCapitalExpense());
		updateSeries(petroleumRevenue, "Operations", year, 
				-getEnergySystem().getPetroleumSystem().getOperationsExpense());
		updateSeries(petroleumRevenue, "Decommission", year, 
				-getEnergySystem().getPetroleumSystem().getDecommissionExpense());
		updateSeries(petroleumRevenue, "Consumption", year, 
				-getEnergySystem().getPetroleumSystem().getConsumptionExpense());
		updateSeries(petroleumRevenue, "In-Distribution", year, 
				-getEnergySystem().getPetroleumSystem().getDistributionExpense());
		updateSeries(petroleumRevenue, "Import", year, 
				-getEnergySystem().getPetroleumSystem().getImportExpense());
		updateSeries(petroleumRevenue, "Out-Distribution", year, 
				getEnergySystem().getPetroleumSystem().getDistributionRevenue());
		updateSeries(petroleumRevenue, "Export", year,
				getEnergySystem().getPetroleumSystem().getExportRevenue());
		updateSeries(petroleumRevenue, "Sales", year, 
				getEnergySystem().getPetroleumSystem().getSalesRevenue());
		updateSeries(petroleumNetRevenue, "Net Revenue", year, 
				getEnergySystem().getPetroleumSystem().getCashFlow());	
		updateSeries(petroleumUseData, "Electricity", year, 
				getEnergySystem().getPetroleumConsumption() 
				- getEnergySystem().getElectricitySystem().getPetroleumBurned());
		updateSeries(petroleumUseData, "Direct Burn", year, 
				getEnergySystem().getElectricitySystem().getPetroleumBurned());
		updateSeries(petroleumSourceData, "Import", year, 
				getEnergySystem().getPetroleumSystem().getPetroleumImport());
		updateSeries(petroleumUseData, "Export", year, 
				getEnergySystem().getPetroleumSystem().getPetroleumExport());
	}
}
