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
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class PetroleumSystemPanel.
 */
public class LocalPetroleumSystemPanel extends PetroleumSystemPanel 
		implements OilUnitsOutput, ElectricityUnitsOutput, CurrencyUnitsOutput {
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
	DefaultTableXYDataset electricityUseData = new DefaultTableXYDataset();
	
	private final CurrencyUnits currencyUnits = CurrencyUnits.Bsim;
	private final TimeUnits currencyTimeUnits = TimeUnits.year;
	private final ElectricityUnits electricityUnits = ElectricityUnits.GWh;
	private final TimeUnits electricityTimeUnits = TimeUnits.year;
	private final OilUnits oilUnits = OilUnits.Mtoe;
	private final TimeUnits oilTimeUnits = TimeUnits.year;

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
				"Oil Reservoir", 0, OilUnits.convertStock(
						petroleumSystem.getMaxPetroleumReservoirVolume(), 
						petroleumSystem, this));
		localPetroleumIndicatorPanel = new LinearIndicatorPanel(
				"Oil Independence", 0, 1);
		indicatorsPanel.add(petroleumReservoirIndicatorPanel);
		indicatorsPanel.add(localPetroleumIndicatorPanel);
		addTab("Indicators", Icons.INDICATORS, indicatorsPanel);

		petroleumStatePanel = new SpatialStatePanel(getSociety(), 
				new PetroleumStateProvider());
		addTab("Network Flow", Icons.NETWORK, petroleumStatePanel);

		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Petroleum Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")", 
				petroleumRevenue, null, petroleumNetRevenue));
		addTab("Source", Icons.PETROLEUM_SOURCE, createStackedAreaChart(
				"Petroleum Source (" + oilUnits + "/" + oilTimeUnits + ")", petroleumSourceData));
		addTab("Use", Icons.PETROLEUM_USE, createStackedAreaChart(
				"Petroleum Use (" + oilUnits + "/" + oilTimeUnits + ")", petroleumUseData));
		addTab("Use", Icons.ELECTRICITY_USE, createStackedAreaChart(
				"Electricity Use (" + electricityUnits + "/" + electricityTimeUnits + ")",
				electricityUseData));

		
		addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Petroleum Use Fraction (-)", 
				localPetroleumData));
		addTab("Reservoir", Icons.PETROLEUM_RESERVOIR, createStackedAreaChart(
				"Oil Reservoir Volume (" + oilUnits + ")",
				petroleumReservoirDataset), "Reservoir");
		/* TODO
		addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
				"Unit Production Cost (SAR/toe)", 
				petroleumProductCostData));
		addTab("Supply Profit", Icons.COST_SUPPLY, createTimeSeriesChart(
				"Unit Supply Profit (SAR/toe)", 
				petroleumSupplyProfitData));
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
	 * Gets the nested petroleum systems.
	 *
	 * @return the nested petroleum systems
	 */
	private List<PetroleumSystem.Local> getNestedPetroleumSystems() {
		List<PetroleumSystem.Local> systems = new ArrayList<PetroleumSystem.Local>();
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			if(nestedSociety.getPetroleumSystem() instanceof PetroleumSystem.Local) {
				systems.add((PetroleumSystem.Local)nestedSociety.getPetroleumSystem());
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
	
	/**
	 * Gets the petroleum system.
	 *
	 * @return the petroleum system
	 */
	public PetroleumSystem.Local getPetroleumSystem() {
		return (PetroleumSystem.Local) getInfrastructureSystem();
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
		updateSeriesCollection(petroleumProductCostData, 
				getSociety().getName(), year, 
				getPetroleumSystem().getUnitProductionCost());
		for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
			updateSeriesCollection(petroleumProductCostData, 
					nestedSystem.getSociety().getName(), year, 
					nestedSystem.getUnitProductionCost());
		}
		
		updateSeriesCollection(petroleumSupplyProfitData, getSociety().getName(), year, 
				getPetroleumSystem().getUnitSupplyProfit());
		for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
			updateSeriesCollection(petroleumSupplyProfitData, 
					nestedSystem.getSociety().getName(), year, 
					nestedSystem.getUnitSupplyProfit());
		}
		
		petroleumReservoirIndicatorPanel.setValue(OilUnits.convertStock(
				getPetroleumSystem().getPetroleumReservoirVolume(), 
				getPetroleumSystem(), this));
		
		updateSeriesCollection(localPetroleumData, getSociety().getName(), year, 
				getPetroleumSystem().getLocalPetroleumFraction());
		localPetroleumIndicatorPanel.setValue(
				getPetroleumSystem().getLocalPetroleumFraction());
		for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
			updateSeriesCollection(localPetroleumData, 
					nestedSystem.getSociety().getName(), year, 
					nestedSystem.getLocalPetroleumFraction());
		}
		
		updateSeries(petroleumRevenue, "Capital", year, 
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getCapitalExpense(), 
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Operations", year, 
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getOperationsExpense(), 
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Decommission", year, 
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getDecommissionExpense(), 
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Consumption", year, 
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getConsumptionExpense(), 
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "In-Distribution", year, 
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getDistributionExpense(), 
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Import", year, 
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getImportExpense(), 
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Out-Distribution", year, 
				CurrencyUnits.convertFlow(
						getPetroleumSystem().getDistributionRevenue(), 
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Export", year,
				CurrencyUnits.convertFlow(
						getPetroleumSystem().getExportRevenue(), 
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Sales", year, 
				CurrencyUnits.convertFlow(
						getPetroleumSystem().getSalesRevenue(), 
						getPetroleumSystem(), this));
		updateSeries(petroleumNetRevenue, "Net Revenue", year, 
				CurrencyUnits.convertFlow(
						getPetroleumSystem().getCashFlow(), 
						getPetroleumSystem(), this));

		if(getPetroleumSystem() instanceof DefaultPetroleumSystem.Local) {
			updateSeries(petroleumReservoirDataset, "Reservoir", year, 
					OilUnits.convertStock(
							getPetroleumSystem().getPetroleumReservoirVolume(), 
							getPetroleumSystem(), this));
			
			for(PetroleumElement element : getPetroleumSystem().getInternalElements()) {
				if(element.getMaxPetroleumProduction() > 0) {
					updateSeries(petroleumSourceData, element.getName(), year, 
							OilUnits.convertFlow(element.getPetroleumProduction(), 
									element, this));
				}
				
				if(element.getMaxPetroleumInput() > 0) {
					updateSeries(petroleumUseData, element.getName(), year, 
							OilUnits.convertFlow(element.getPetroleumInput(),
									element, this));
				}
			}
			
			for(PetroleumElement element : getPetroleumSystem().getExternalElements()) {
				if(element.getMaxPetroleumInput() > 0) {
					updateSeries(petroleumSourceData, element.getName(), year, 
							OilUnits.convertFlow(element.getPetroleumOutput(),
									element, this));
				}
			}
		} else {
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(petroleumReservoirDataset, nestedSociety.getName(), year, 
						OilUnits.convertStock(((PetroleumSystem.Local)nestedSociety.
								getPetroleumSystem()).getPetroleumReservoirVolume(),
								nestedSociety.getPetroleumSystem(), this));
			}
			
			for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
				updateSeries(petroleumSourceData, nestedSystem.getSociety().getName() 
						+ " Production", year,
						OilUnits.convertFlow(nestedSystem.getPetroleumProduction(), 
								nestedSystem, this));
			}
			/*updateSeries(petroleumSourceData, "Production", year, 
					OilUnits.convertStock(
							getPetroleumSystem().getPetroleumProduction(),
							getPetroleumSystem(), this));*/
			updateSeries(petroleumSourceData, "Distribution", year, 
					OilUnits.convertStock(
							getPetroleumSystem().getPetroleumInDistribution(),
							getPetroleumSystem(), this));
			updateSeries(petroleumUseData, "Distribution", year, 
					OilUnits.convertStock(
							getPetroleumSystem().getPetroleumOutDistribution(),
							getPetroleumSystem(), this));
			updateSeries(petroleumUseData, "Distribution Losses", year, 
					OilUnits.convertStock(
							getPetroleumSystem().getPetroleumOutDistributionLosses(),
							getPetroleumSystem(), this));
		}
		
		if(getNestedPetroleumSystems().isEmpty()) {
			for(PetroleumElement element : getPetroleumSystem().getInternalElements()) {
				updateSeries(electricityUseData, element.getName(), year, 
						ElectricityUnits.convertFlow(element.getElectricityConsumption(),
								element, this));
			}
		} else {
			for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
				updateSeries(electricityUseData, nestedSystem.getName(), year, 
						ElectricityUnits.convertFlow(nestedSystem.getElectricityConsumption(),
								nestedSystem, this));
			}
		}
		updateSeries(petroleumRevenue, "Capital", year, 
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getCapitalExpense(),
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Operations", year,  
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getOperationsExpense(),
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Decommission", year,  
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getDecommissionExpense(),
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Consumption", year, 
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getConsumptionExpense(),
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "In-Distribution", year,  
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getDistributionExpense(),
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Import", year,  
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getImportExpense(),
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Out-Distribution", year, 
				CurrencyUnits.convertFlow( 
						getPetroleumSystem().getDistributionRevenue(),
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Export", year, 
				CurrencyUnits.convertFlow(
						getPetroleumSystem().getExportRevenue(),
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Sales", year,  
				CurrencyUnits.convertFlow(
						getPetroleumSystem().getSalesRevenue(),
						getPetroleumSystem(), this));
		updateSeries(petroleumNetRevenue, "Net Revenue", year,  
				CurrencyUnits.convertFlow(
						getPetroleumSystem().getCashFlow(),
						getPetroleumSystem(), this));	
		updateSeries(petroleumUseData, "Electricity", year, 
				ElectricityUnits.convertFlow(
						getSociety().getElectricitySystem().getPetroleumConsumption(),
						getSociety().getElectricitySystem(), this));
		updateSeries(petroleumSourceData, "Import", year, 
				OilUnits.convertFlow(
						getPetroleumSystem().getPetroleumImport(),
						getPetroleumSystem(), this));
		updateSeries(petroleumUseData, "Export", year, 
				OilUnits.convertFlow(
						getPetroleumSystem().getPetroleumExport(),
						getPetroleumSystem(), this));
	}
}
