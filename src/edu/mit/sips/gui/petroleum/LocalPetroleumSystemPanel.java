package edu.mit.sips.gui.petroleum;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.petroleum.DefaultPetroleumSystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.gui.LinearIndicatorPanel;
import edu.mit.sips.gui.PlottingUtils;
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
 * The Class PetroleumSystemPanel.
 */
public class LocalPetroleumSystemPanel extends PetroleumSystemPanel 
		implements OilUnitsOutput, ElectricityUnitsOutput, CurrencyUnitsOutput {
	private static final long serialVersionUID = 2218175276232419659L;
	private static final CurrencyUnits currencyUnits = CurrencyUnits.Msim;
	private static final TimeUnits currencyTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.Mtoe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	
	private final LinearIndicatorPanel petroleumReservoirIndicatorPanel, 
	localPetroleumIndicatorPanel;
	
	private final SpatialStatePanel petroleumStatePanel;

	TimeSeriesCollection localPetroleumData = new TimeSeriesCollection();
	TimeSeriesCollection petroleumProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection petroleumSupplyProfitData = new TimeSeriesCollection();
	TimeSeriesCollection petroleumConsumptionPerCapita = new TimeSeriesCollection();
	
	DefaultTableXYDataset petroleumReservoirDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumSourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumNetRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityUseData = new DefaultTableXYDataset();

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
		// addTab("Indicators", Icons.INDICATORS, indicatorsPanel);

		petroleumStatePanel = new SpatialStatePanel(getSociety(), 
				new PetroleumStateProvider());
		addTab("Network Flow", Icons.NETWORK, petroleumStatePanel);
		
		List<String> revenueNames;
		if(!(getSociety() instanceof Country)) {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", /*"Input Expense", */"Distribution Expense", 
					"Import Expense", "Distribution Revenue", "Export Revenue", 
					"Output Revenue");
		} else {
			revenueNames = Arrays.asList("Capital Expense", "Operations Expense", 
					"Decommission Expense", /*"Input Expense", */"Import Expense", 
					"Export Revenue", "Output Revenue");
		}
		for(String name : revenueNames) {
			petroleumRevenue.addSeries(new XYSeries(name, true, false));
		}
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Petroleum Revenue (" + currencyUnits + "/" + currencyTimeUnits + ")", 
				petroleumRevenue, PlottingUtils.getCashFlowColors(revenueNames), petroleumNetRevenue));
		
		List<String> oilSourceNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				oilSourceNames.add(society.getName() + " Production");
			}
		} else {
			oilSourceNames.add(getSociety().getName() + " Production");
			oilSourceNames.add("In-Distribution");
		}
		oilSourceNames.add("Private Operations");
		oilSourceNames.add("Import");
		for(String name : oilSourceNames) {
			petroleumSourceData.addSeries(new XYSeries(name, true, false));
		}
		addTab("Source", Icons.PETROLEUM_SOURCE, createStackedAreaChart(
				"Petroleum Source (" + oilUnits + "/" + oilTimeUnits + ")",
				petroleumSourceData, PlottingUtils.getResourceColors(oilSourceNames)));
		
		List<String> oilUseNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				oilUseNames.add(society.getName() + " Society");
			}
			// oilUseNames.add("Losses");
		} else {
			oilUseNames.add(getSociety().getName()  + " Society");
			oilUseNames.add("Out-Distribution");
		}
		oilUseNames.add("Electricity Operations");
		oilUseNames.add("Export");
		for(String name : oilUseNames) {
			petroleumUseData.addSeries(new XYSeries(name, true, false));
		}
		addTab("Use", Icons.PETROLEUM_USE, createStackedAreaChart(
				"Petroleum Use (" + oilUnits + "/" + oilTimeUnits + ")", 
				petroleumUseData, PlottingUtils.getResourceColors(oilUseNames)));
		
		List<String> electricityUseNames = new ArrayList<String>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				electricityUseNames.add(society.getName() + " Operations");
			}
		} else {
			electricityUseNames.add(getSociety().getName() + " Operations");
		}
		addTab("Use", Icons.ELECTRICITY_USE, createStackedAreaChart(
				"Electricity Use (" + electricityUnits + "/" + electricityTimeUnits + ")",
				electricityUseData, PlottingUtils.getResourceColors(electricityUseNames)));

		/* addTab("Local", Icons.LOCAL, createTimeSeriesChart(
				"Local Petroleum Use Fraction (-)", 
				localPetroleumData));
		addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
				"Oil Consumption per Capita (" + OilUnits.toe 
				+ "/" + TimeUnits.year + ")", 
				petroleumConsumptionPerCapita)); */
		List<Color> societyColors = new ArrayList<Color>();
		if(getSociety() instanceof Country) {
			for(Society society : getSociety().getNestedSocieties()) {
				societyColors.add(PlottingUtils.getSocietyColor(society));
			}
		} else {
			societyColors.add(PlottingUtils.getSocietyColor(getSociety()));
		}
		addTab("Reservoir", Icons.PETROLEUM_RESERVOIR, createStackedAreaChart(
				"Oil Reservoir Volume (" + oilUnits + ")",
				petroleumReservoirDataset, societyColors.toArray(new Color[0])), "Reservoir");
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
		petroleumConsumptionPerCapita.removeAllSeries();
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
		

		if(getSociety().getSocialSystem().getPopulation() > 0) {
			updateSeriesCollection(petroleumConsumptionPerCapita, getSociety().getName(), year, 
					DefaultUnits.convertFlow(getSociety().getSocialSystem().getPetroleumConsumption(),
							getSociety().getSocialSystem().getOilUnits(),
							getSociety().getSocialSystem().getOilTimeUnits(),
							OilUnits.toe, TimeUnits.year)
							/ getSociety().getSocialSystem().getPopulation());
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				if(nestedSociety.getSocialSystem().getPopulation() > 0) {
					updateSeriesCollection(petroleumConsumptionPerCapita, nestedSociety.getName(), year, 
							DefaultUnits.convertFlow(nestedSociety.getSocialSystem().getPetroleumConsumption(),
									nestedSociety.getSocialSystem().getOilUnits(),
									nestedSociety.getSocialSystem().getOilTimeUnits(),
									OilUnits.toe, TimeUnits.year)
									/ nestedSociety.getSocialSystem().getPopulation());
				}
			}
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

		updateSeries(petroleumRevenue, "Capital Expense", year, 
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getCapitalExpense(),
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Operations Expense", year,  
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getOperationsExpense()
						- getPetroleumSystem().getConsumptionExpense(),
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Decommission Expense", year,  
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getDecommissionExpense(),
						getPetroleumSystem(), this));
		/*updateSeries(petroleumRevenue, "Consumption", year, 
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getConsumptionExpense(),
						getPetroleumSystem(), this));*/
		if(!(getPetroleumSystem().getSociety() instanceof Country)) {
			updateSeries(petroleumRevenue, "Distribution Expense", year,  
					CurrencyUnits.convertFlow(
							-getPetroleumSystem().getDistributionExpense(),
							getPetroleumSystem(), this));
		}
		updateSeries(petroleumRevenue, "Import Expense", year,  
				CurrencyUnits.convertFlow(
						-getPetroleumSystem().getImportExpense(),
						getPetroleumSystem(), this));
		if(!(getPetroleumSystem().getSociety() instanceof Country)) {
			updateSeries(petroleumRevenue, "Distribution Revenue", year, 
					CurrencyUnits.convertFlow( 
							getPetroleumSystem().getDistributionRevenue(),
							getPetroleumSystem(), this));
		}
		updateSeries(petroleumRevenue, "Export Revenue", year, 
				CurrencyUnits.convertFlow(
						getPetroleumSystem().getExportRevenue(),
						getPetroleumSystem(), this));
		updateSeries(petroleumRevenue, "Output Revenue", year,  
				CurrencyUnits.convertFlow(
						getPetroleumSystem().getSalesRevenue(),
						getPetroleumSystem(), this));
		updateSeries(petroleumNetRevenue, "Net Cash Flow", year,  
				CurrencyUnits.convertFlow(
						getPetroleumSystem().getCashFlow(),
						getPetroleumSystem(), this));
		
		if(getNestedPetroleumSystems().isEmpty()) {
			updateSeries(petroleumUseData, "Society", year, 
					OilUnits.convertFlow(getSociety().getSocialSystem().getPetroleumConsumption(),
							getSociety().getSocialSystem(), this));
			updateSeries(electricityUseData, "Operations", year, 
					ElectricityUnits.convertFlow(getPetroleumSystem().getElectricityConsumption(),
							getPetroleumSystem(), this));
		} else {
			for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
				updateSeries(petroleumUseData, nestedSystem.getSociety().getName() + " Society", year,
						OilUnits.convertFlow(nestedSystem.getSociety().getSocialSystem().getPetroleumConsumption(), 
								nestedSystem.getSociety().getSocialSystem(), this));
				updateSeries(electricityUseData, nestedSystem.getName(), year, 
						ElectricityUnits.convertFlow(nestedSystem.getElectricityConsumption(),
								nestedSystem, this));
			}
		}

		if(getPetroleumSystem() instanceof DefaultPetroleumSystem.Local) {
			updateSeries(petroleumReservoirDataset, "Reservoir", year, 
					OilUnits.convertStock(
							getPetroleumSystem().getPetroleumReservoirVolume(), 
							getPetroleumSystem(), this));

			updateSeries(petroleumSourceData, "Production", year, 
					OilUnits.convertFlow(getPetroleumSystem().getPetroleumProduction(), 
							getPetroleumSystem(), this));
			updateSeries(petroleumUseData, "Distribution", year, 
					OilUnits.convertFlow(getPetroleumSystem().getPetroleumOutDistribution(),
							getPetroleumSystem(), this));
			updateSeries(petroleumSourceData, "Distribution", year, 
					OilUnits.convertFlow(getPetroleumSystem().getPetroleumInDistribution(),
							getPetroleumSystem(), this));
		} else {
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(petroleumReservoirDataset, nestedSociety.getName(), year, 
						OilUnits.convertStock(((PetroleumSystem.Local)nestedSociety.
								getPetroleumSystem()).getPetroleumReservoirVolume(),
								nestedSociety.getPetroleumSystem(), this));
			}
			
			for(PetroleumSystem.Local nestedSystem : getNestedPetroleumSystems()) {
				updateSeries(petroleumSourceData, nestedSystem.getSociety().getName() + " Production", year,
						OilUnits.convertFlow(nestedSystem.getPetroleumProduction(), nestedSystem, this));
			}
			/*updateSeries(petroleumSourceData, "Production", year, 
					OilUnits.convertStock(
							getPetroleumSystem().getPetroleumProduction(),
							getPetroleumSystem(), this));*/
			if(!getPetroleumSystem().getExternalElements().isEmpty()) {
				updateSeries(petroleumSourceData, "Distribution", year, 
						OilUnits.convertStock(
								getPetroleumSystem().getPetroleumInDistribution(),
								getPetroleumSystem(), this));
				updateSeries(petroleumUseData, "Distribution", year, 
						OilUnits.convertStock(
								getPetroleumSystem().getPetroleumOutDistribution(),
								getPetroleumSystem(), this));
			}
			/*updateSeries(petroleumUseData, "Distribution Losses", year, 
					OilUnits.convertStock(
							getPetroleumSystem().getPetroleumOutDistributionLosses(),
							getPetroleumSystem(), this));*/
		}
		
		updateSeries(petroleumUseData, "Electricity Operations", year, 
				OilUnits.convertFlow(
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
