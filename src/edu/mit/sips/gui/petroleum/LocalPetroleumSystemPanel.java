package edu.mit.sips.gui.petroleum;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Society;
import edu.mit.sips.core.petroleum.DefaultPetroleumSystem;
import edu.mit.sips.core.petroleum.PetroleumElement;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.gui.LinearIndicatorPanel;
import edu.mit.sips.gui.SpatialStatePanel;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;

/**
 * The Class PetroleumSystemPanel.
 */
public class LocalPetroleumSystemPanel extends PetroleumSystemPanel {
	private static final long serialVersionUID = 2218175276232419659L;
	
	private final LinearIndicatorPanel petroleumReservoirIndicatorPanel, 
	localPetroleumIndicatorPanel;
	
	private final SpatialStatePanel petroleumStatePanel;

	TimeSeriesCollection localPetroleumData = new TimeSeriesCollection();
	TimeSeriesCollection petroleumProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection petroleumSupplyProfitData = new TimeSeriesCollection();
	
	DefaultTableXYDataset petroleumReservoirDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumSourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumNetRevenue = new DefaultTableXYDataset();

	/**
	 * Instantiates a new local petroleum system panel.
	 *
	 * @param petroleumSystem the petroleum system
	 */
	public LocalPetroleumSystemPanel(PetroleumSystem.Local petroleumSystem) {
		super(petroleumSystem);

		JPanel indicatorsPanel = new JPanel();
		indicatorsPanel.setLayout(
				new BoxLayout(indicatorsPanel, BoxLayout.LINE_AXIS));
		petroleumReservoirIndicatorPanel = new LinearIndicatorPanel(
				"Oil Reservoir", 0, 
				petroleumSystem.getMaxPetroleumReservoirVolume());
		localPetroleumIndicatorPanel = new LinearIndicatorPanel(
				"Oil Independence", 0, 1);
		indicatorsPanel.add(petroleumReservoirIndicatorPanel);
		indicatorsPanel.add(localPetroleumIndicatorPanel);
		addTab("Indicators", Icons.INDICATORS, indicatorsPanel);

		petroleumStatePanel = new SpatialStatePanel(getSociety(), 
				new PetroleumStateProvider());
		addTab("Network Flow", Icons.NETWORK, petroleumStatePanel);

		addTab("Revenue", Icons.REVENUE, 
				createStackedAreaChart("Petroleum Revenue (SAR/year)", 
				petroleumRevenue, null, petroleumNetRevenue));
		addTab("Source", Icons.PETROLEUM_SOURCE, createStackedAreaChart(
				"Petroleum Source (bbl/year)", petroleumSourceData));
		addTab("Use", Icons.PETROLEUM_USE, createStackedAreaChart(
				"Petroleum Use (bbl/year)", petroleumUseData));
		
		addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Petroleum Use Fraction (-)", 
				localPetroleumData));
		addTab("Reservoir", Icons.PETROLEUM_RESERVOIR, createStackedAreaChart(
				"Oil Reservoir Volume (bbl)", 
				petroleumReservoirDataset), "Reservoir");
		/* TODO
		addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
				"Unit Production Cost (SAR/bbl)", 
				petroleumProductCostData));
		addTab("Supply Profit", Icons.COST_SUPPLY, createTimeSeriesChart(
				"Unit Supply Profit (SAR/bbl)", 
				petroleumSupplyProfitData));
		*/
	}
	
	/**
	 * Gets the petroleum system.
	 *
	 * @return the petroleum system
	 */
	public PetroleumSystem.Local getPetroleumSystem() {
		return (PetroleumSystem.Local) getInfrastructureSystem();
	}
	
	/**
	 * Gets the nested petroleum systems.
	 *
	 * @return the nested petroleum systems
	 */
	private List<PetroleumSystem.Local> getNestedPetroleumSystems() {
		List<PetroleumSystem.Local> systems = new ArrayList<PetroleumSystem.Local>();
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			if(nestedSociety.getWaterSystem() instanceof PetroleumSystem.Local) {
				systems.add((PetroleumSystem.Local)nestedSociety.getPetroleumSystem());
			}
		}
		return systems;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#initialize()
	 */
	@Override
	public void initialize() {
		petroleumReservoirIndicatorPanel.initialize();
		localPetroleumIndicatorPanel.initialize();
		petroleumRevenue.removeAllSeries();
		petroleumNetRevenue.removeAllSeries();
		localPetroleumData.removeAllSeries();
		petroleumProductCostData.removeAllSeries();
		petroleumSupplyProfitData.removeAllSeries();
		petroleumReservoirDataset.removeAllSeries();
		petroleumUseData.removeAllSeries();
		petroleumSourceData.removeAllSeries();
		petroleumRevenue.removeAllSeries();
		petroleumNetRevenue.removeAllSeries();
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
		petroleumStatePanel.repaint();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		petroleumStatePanel.repaint();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#update(int)
	 */
	@Override
	public void update(int year) {
		updateSeriesCollection(petroleumProductCostData, getSociety().getName(), 
				year, getPetroleumSystem().getUnitProductionCost());
		for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
			updateSeriesCollection(petroleumProductCostData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getUnitProductionCost());
		}
		
		updateSeriesCollection(petroleumSupplyProfitData, getSociety().getName(), 
				year, getPetroleumSystem().getUnitSupplyProfit());
		for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
			updateSeriesCollection(petroleumSupplyProfitData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getUnitSupplyProfit());
		}
		
		petroleumReservoirIndicatorPanel.setValue(
				getPetroleumSystem().getPetroleumReservoirVolume());
		
		updateSeriesCollection(localPetroleumData, getSociety().getName(), 
				year, getPetroleumSystem().getLocalPetroleumFraction());
		localPetroleumIndicatorPanel.setValue(
				getPetroleumSystem().getLocalPetroleumFraction());
		for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
			updateSeriesCollection(localPetroleumData, nestedSystem.getSociety().getName(), 
					year, nestedSystem.getLocalPetroleumFraction());
		}
		
		updateSeries(petroleumRevenue, "Capital", year, 
				-getPetroleumSystem().getCapitalExpense());
		updateSeries(petroleumRevenue, "Operations", year, 
				-getPetroleumSystem().getOperationsExpense());
		updateSeries(petroleumRevenue, "Decommission", year, 
				-getPetroleumSystem().getDecommissionExpense());
		updateSeries(petroleumRevenue, "Consumption", year, 
				-getPetroleumSystem().getConsumptionExpense());
		updateSeries(petroleumRevenue, "In-Distribution", year, 
				-getPetroleumSystem().getDistributionExpense());
		updateSeries(petroleumRevenue, "Import", year, 
				-getPetroleumSystem().getImportExpense());
		updateSeries(petroleumRevenue, "Out-Distribution", year, 
				getPetroleumSystem().getDistributionRevenue());
		updateSeries(petroleumRevenue, "Export", year,
				getPetroleumSystem().getExportRevenue());
		updateSeries(petroleumRevenue, "Sales", year, 
				getPetroleumSystem().getSalesRevenue());
		updateSeries(petroleumNetRevenue, "Net Revenue", year, 
				getPetroleumSystem().getCashFlow());
		
		if(getPetroleumSystem() instanceof DefaultPetroleumSystem.Local) {
			updateSeries(petroleumReservoirDataset, "Reservoir", year, 
					getPetroleumSystem().getPetroleumReservoirVolume());
			
			for(PetroleumElement element : getPetroleumSystem().getInternalElements()) {
				if(element.getMaxPetroleumProduction() > 0) {
					updateSeries(petroleumSourceData, element.getName(), year, 
							element.getPetroleumProduction());
				}
				
				if(element.getMaxPetroleumInput() > 0) {
					updateSeries(petroleumUseData, element.getName(), year, 
							element.getPetroleumInput());
				}
			}
			
			for(PetroleumElement element : getPetroleumSystem().getExternalElements()) {
				if(element.getMaxPetroleumInput() > 0) {
					updateSeries(petroleumSourceData, element.getName(), year, 
							element.getPetroleumOutput());
				}
			}
		} else {
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(petroleumReservoirDataset, nestedSociety.getName(), year, 
						getPetroleumSystem().getPetroleumReservoirVolume());
			}

			updateSeries(petroleumSourceData, "Production", year, 
					getPetroleumSystem().getPetroleumProduction());
			updateSeries(petroleumSourceData, "Distribution", year, 
					getPetroleumSystem().getPetroleumInDistribution());
			updateSeries(petroleumUseData, "Distribution", year, 
					getPetroleumSystem().getPetroleumOutDistribution());
		}
		
		updateSeries(petroleumRevenue, "Capital", year, 
				-getPetroleumSystem().getCapitalExpense());
		updateSeries(petroleumRevenue, "Operations", year, 
				-getPetroleumSystem().getOperationsExpense());
		updateSeries(petroleumRevenue, "Decommission", year, 
				-getPetroleumSystem().getDecommissionExpense());
		updateSeries(petroleumRevenue, "Consumption", year, 
				-getPetroleumSystem().getConsumptionExpense());
		updateSeries(petroleumRevenue, "In-Distribution", year, 
				-getPetroleumSystem().getDistributionExpense());
		updateSeries(petroleumRevenue, "Import", year, 
				-getPetroleumSystem().getImportExpense());
		updateSeries(petroleumRevenue, "Out-Distribution", year, 
				getPetroleumSystem().getDistributionRevenue());
		updateSeries(petroleumRevenue, "Export", year,
				getPetroleumSystem().getExportRevenue());
		updateSeries(petroleumRevenue, "Sales", year, 
				getPetroleumSystem().getSalesRevenue());
		updateSeries(petroleumNetRevenue, "Net Revenue", year, 
				getPetroleumSystem().getCashFlow());	
		updateSeries(petroleumUseData, "Electricity", year, 
				getSociety().getElectricitySystem().getPetroleumConsumption());
		updateSeries(petroleumSourceData, "Import", year, 
				getPetroleumSystem().getPetroleumImport());
		updateSeries(petroleumUseData, "Export", year, 
				getPetroleumSystem().getPetroleumExport());
	}
}
