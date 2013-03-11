package edu.mit.sips.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYSeries;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.energy.ElectricityElement;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.energy.PetroleumElement;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.io.Icons;

public class SocietyPane extends JTabbedPane implements UpdateListener {
	private static final long serialVersionUID = -2070963615190359867L;

	private final Society society;
	private final List<SocietyPane> nestedPaneList = 
			new ArrayList<SocietyPane>();
	private final List<SpatialStatePanel> statePanelList = 
			new ArrayList<SpatialStatePanel>();
	
	private final LinearIndicatorPanel domesticProductIndicatorPanel, 
		localWaterIndicatorPanel, waterReservoirIndicatorPanel, 
		renewableWaterIndicatorPanel, waterConsumptionIndicatorPanel,
		petroleumReservoirIndicatorPanel, renewableEnergyIndicatorPanel,
		localEnergyIndicatorPanel, electricityConsumptionIndicatorPanel, 
		localFoodIndicatorPanel, foodConsumptionIndicatorPanel;
	
	TimeSeriesCollection fundsData = new TimeSeriesCollection();
	TimeSeriesCollection domesticProductPerCapita = new TimeSeriesCollection();
	TimeSeriesCollection localFoodData = new TimeSeriesCollection();
	TimeSeriesCollection foodProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection foodSupplyCostData = new TimeSeriesCollection();
	TimeSeriesCollection localWaterData = new TimeSeriesCollection();
	TimeSeriesCollection waterProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection waterSupplyCostData = new TimeSeriesCollection();
	TimeSeriesCollection renewableWaterData = new TimeSeriesCollection();
	TimeSeriesCollection petroleumProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection petroleumSupplyCostData = new TimeSeriesCollection();
	TimeSeriesCollection localElectricityData = new TimeSeriesCollection();
	TimeSeriesCollection electricityProductCostData = new TimeSeriesCollection();
	TimeSeriesCollection electricitySupplyCostData = new TimeSeriesCollection();
	TimeSeriesCollection renewableElectricityData = new TimeSeriesCollection();
	TimeSeriesCollection foodConsumptionPerCapita = new TimeSeriesCollection();
	TimeSeriesCollection waterConsumptionPerCapita = new TimeSeriesCollection();
	TimeSeriesCollection electricityConsumptionPerCapita = new TimeSeriesCollection();
	
	DefaultTableXYDataset populationDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset domesticProduct = new DefaultTableXYDataset();
	DefaultTableXYDataset grossDomesticProduct = new DefaultTableXYDataset();
	DefaultTableXYDataset landAvailableDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset waterReservoirDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumReservoirDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset socialSystemRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset socialSystemNetRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset infrastructureSystemRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset infrastructureSystemNetRevenue = new DefaultTableXYDataset();

	DefaultTableXYDataset foodSourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset foodUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset agricultureRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset agricultureNetRevenue = new DefaultTableXYDataset();
	/* TODO
	DefaultTableXYDataset foodProductionCost = new DefaultTableXYDataset();
	DefaultTableXYDataset foodConsumptionCost = new DefaultTableXYDataset();
	DefaultTableXYDataset foodProductionNetCost = new DefaultTableXYDataset();
	DefaultTableXYDataset foodConsumptionNetCost = new DefaultTableXYDataset();
	*/

	DefaultTableXYDataset waterSourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset waterRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset waterNetRevenue = new DefaultTableXYDataset();

	DefaultTableXYDataset petroleumSourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumNetRevenue = new DefaultTableXYDataset();

	DefaultTableXYDataset electricitySourceData = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityUseData = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityNetRevenue = new DefaultTableXYDataset();
	
	/**
	 * Instantiates a new social system pane.
	 *
	 * @param society the society
	 */
	public SocietyPane(Society society) {
		this.society = society;

		if(society instanceof Country) {
			addTab("Funds", Icons.FUNDS, createTimeSeriesChart(
					"Funds (SAR)", fundsData));
		}
		domesticProductIndicatorPanel = new LinearIndicatorPanel(
				"Economic Production", 0, 100000);
		localWaterIndicatorPanel = new LinearIndicatorPanel(
				"Water Independence", 0, 1);
		waterReservoirIndicatorPanel = new LinearIndicatorPanel(
				"Water Reservoir", 0, 
				society.getWaterSystem().getMaxWaterReservoirVolume());
		renewableWaterIndicatorPanel = new LinearIndicatorPanel(
				"Renewable Water", 0, 1);
		waterConsumptionIndicatorPanel = new LinearIndicatorPanel(
				"Water Consumption", 
				society.getGlobals().getMinWaterDemandPerCapita(), 
				society.getGlobals().getMaxWaterDemandPerCapita());
		petroleumReservoirIndicatorPanel = new LinearIndicatorPanel(
				"Oil Reservoir", 0, 
				society.getEnergySystem().getPetroleumSystem().getMaxPetroleumReservoirVolume());
		renewableEnergyIndicatorPanel = new LinearIndicatorPanel(
				"Renewable Energy", 0, 1);
		localEnergyIndicatorPanel = new LinearIndicatorPanel(
				"Energy Independence", 0, 1);
		electricityConsumptionIndicatorPanel = new LinearIndicatorPanel(
				"Energy Consumption", 
				society.getGlobals().getMinElectricityDemandPerCapita(), 
				society.getGlobals().getMaxElectricityDemandPerCapita());
		localFoodIndicatorPanel = new LinearIndicatorPanel(
				"Food Independence", 0, 1);
		foodConsumptionIndicatorPanel = new LinearIndicatorPanel(
				"Food Consumption", 
				society.getGlobals().getMinFoodDemandPerCapita(), 
				society.getGlobals().getMaxFoodDemandPerCapita());
		JPanel indicatorsPanel = new JPanel();
		indicatorsPanel.setLayout(new GridLayout(2,6));
		indicatorsPanel.add(domesticProductIndicatorPanel);
		indicatorsPanel.add(localWaterIndicatorPanel);
		indicatorsPanel.add(waterReservoirIndicatorPanel);
		indicatorsPanel.add(renewableWaterIndicatorPanel);
		indicatorsPanel.add(waterConsumptionIndicatorPanel);
		indicatorsPanel.add(petroleumReservoirIndicatorPanel);
		indicatorsPanel.add(renewableEnergyIndicatorPanel);
		indicatorsPanel.add(localEnergyIndicatorPanel);
		indicatorsPanel.add(electricityConsumptionIndicatorPanel);
		indicatorsPanel.add(localFoodIndicatorPanel);
		indicatorsPanel.add(foodConsumptionIndicatorPanel);
		addTab("Indicators", Icons.INDICATORS, indicatorsPanel);
		
		JTabbedPane infraPane;
		if(society instanceof City) {
			// add infrastructure directly to society panel if city
			infraPane = this;
		} else {
			// otherwise create sub-tab for infrastructure
			infraPane = new JTabbedPane();
			addTab("Infrastructure", Icons.INFRASTRUCTURE, infraPane);
		}
		
		if(society instanceof Country) {
			infraPane.addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
					"Revenue (SAR/year)", infrastructureSystemRevenue, 
					infrastructureSystemNetRevenue));
		} else {
			addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
					"Revenue (SAR/year)", infrastructureSystemRevenue, 
					infrastructureSystemNetRevenue));
		}
		
		
		for(InfrastructureSystem system : society.getSystems()) {
			JTabbedPane systemPane = new JTabbedPane();
			
			 if(system.equals(society.getSocialSystem())) {
					JTabbedPane socialPane = new JTabbedPane();
					infraPane.addTab(system.getName(), getIcon(society), socialPane);

					socialPane.addTab("Revenue", Icons.REVENUE, 
							createStackedAreaChart("Revenue (SAR/year)", 
									socialSystemRevenue, socialSystemNetRevenue));
					socialPane.addTab("Domestic Product", Icons.PRODUCT, createStackedAreaChart(
							"Domestic Product (SAR/year)", domesticProduct, 
							grossDomesticProduct));
					socialPane.addTab("Domestic Product PC", Icons.PRODUCT, createTimeSeriesChart(
							"Domestic Product per capita (SAR/person)", 
							domesticProductPerCapita));
					socialPane.addTab("Population", Icons.POPULATION, createStackedAreaChart(
							"Population", populationDataset));
			} else if(system.equals(society.getAgricultureSystem())) {
				infraPane.addTab(system.getName(), Icons.AGRICULTURE, systemPane);
				
				SpatialStatePanel agricultureStatePanel = new SpatialStatePanel(
						society, new AgricultureStateProvider());
				statePanelList.add(agricultureStatePanel);
				systemPane.addTab("Network Flow", Icons.NETWORK, agricultureStatePanel);
				
				systemPane.addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
						"Agriculture Revenue (SAR/year)", 
						agricultureRevenue, agricultureNetRevenue));
				systemPane.addTab("Source", Icons.AGRICULTURE_SOURCE, createStackedAreaChart(
						"Food Source (Mj/year)", foodSourceData));
				systemPane.addTab("Use", Icons.AGRICULTURE_USE, createStackedAreaChart(
						"Food Use (Mj/year)", foodUseData));
				systemPane.addTab("Local", Icons.LOCAL, createTimeSeriesChart(
						"Local Food Fraction (-)", localFoodData));
				systemPane.addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
						"Food Consumption Per Capita (Mj/person)", 
								foodConsumptionPerCapita));
				systemPane.addTab("Arable Land", Icons.ARABLE_LAND, createStackedAreaChart(
						"Arable Land Available (km^2)", landAvailableDataset));
				systemPane.addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
						"Direct Food Production Cost (SAR/Mj)", 
								foodProductCostData));
				systemPane.addTab("Supply Cost", Icons.COST_SUPPLY, createTimeSeriesChart(
						"Direct Food Supply Cost (SAR/Mj)", 
								foodSupplyCostData));
				/* TODO
				systemPane.addTab("Production Cost", Icons.COST_PRODUCTION, createStackedAreaChart(
						"Direct Food Production Cost (SAR/Mj)", 
								foodProductionCost, foodProductionNetCost));
				systemPane.addTab("Consumption Cost", Icons.COST_SUPPLY, createStackedAreaChart(
						"Direct Food Consumption Cost (SAR/Mj)", 
								foodConsumptionCost, foodConsumptionNetCost));
				*/
			} else if(system.equals(society.getWaterSystem())) {
				infraPane.addTab(system.getName(), Icons.WATER, systemPane);
				
				SpatialStatePanel waterStatePanel = new SpatialStatePanel(
						society, new WaterStateProvider());
				statePanelList.add(waterStatePanel);
				systemPane.addTab("Network Flow", Icons.NETWORK, waterStatePanel);
				
				systemPane.addTab("Revenue", Icons.REVENUE, 
						createStackedAreaChart("Water Revenue (SAR/year)", 
						waterRevenue, waterNetRevenue));
				systemPane.addTab("Source", Icons.WATER_SOURCE, createStackedAreaChart(
						"Water Source (m^3/year)", waterSourceData));
				systemPane.addTab("Use", Icons.WATER_USE, createStackedAreaChart(
						"Water Use (m^3/year)", waterUseData));

				systemPane.addTab("Local", Icons.LOCAL, createTimeSeriesChart(
						"Local Water Use Fraction (-)", localWaterData));
				systemPane.addTab("Renewable", Icons.RENEWABLE, createTimeSeriesChart(
						"Renewable Water Production Fraction (-)", 
						renewableWaterData));
				systemPane.addTab("Consumption", Icons.CONSUMPTION, createTimeSeriesChart(
						"Water Consumption per Capita (m^3/person)", 
						waterConsumptionPerCapita));
				systemPane.addTab("Reservoir", Icons.WATER_RESERVOIR, createStackedAreaChart(
						"Water Reservoir Volume (m^3)", waterReservoirDataset));
				systemPane.addTab("Production Cost", Icons.COST_PRODUCTION, createTimeSeriesChart(
						"Direct Water Production Cost (SAR/m^3)", 
						waterProductCostData));
				systemPane.addTab("Supply Cost", Icons.COST_SUPPLY, createTimeSeriesChart(
						"Direct Water Supply Cost (SAR/m^3)", 
						waterSupplyCostData));
				
			} else if(system.equals(society.getEnergySystem())) {
				//infraPane.addTab(system.getName(), Icons.ENERGY, systemPane);
				JTabbedPane petroleumPane = new JTabbedPane();
				//systemPane.addTab(((EnergySystem)system).getPetroleumSystem().getName(),
				//		Icons.PETROLEUM, petroleumPane);
				infraPane.addTab(((EnergySystem)system).getPetroleumSystem().getName(), 
						Icons.PETROLEUM, petroleumPane);
				
				SpatialStatePanel petroleumStatePanel = new SpatialStatePanel(
						society, new PetroleumStateProvider());
				statePanelList.add(petroleumStatePanel);
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
				//systemPane.addTab(((EnergySystem)system).getElectricitySystem().getName(),
				//		Icons.ELECTRICITY, electricityPane);
				infraPane.addTab(((EnergySystem)system).getElectricitySystem().getName(), 
						Icons.ELECTRICITY, electricityPane);
				
				SpatialStatePanel electricityStatePanel = new SpatialStatePanel(
						society, new ElectricityStateProvider());
				statePanelList.add(electricityStatePanel);
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
		}

		for(Society nestedSociety : society.getNestedSocieties()) {
			SocietyPane subPane = new SocietyPane(nestedSociety);
			nestedPaneList.add(subPane);
			addTab(nestedSociety.getName(), getIcon(nestedSociety), subPane);
		}
		

		if(society instanceof Country) {
			addTab("Globals", Icons.CONFIGURATION, 
					new GlobalsPane((Country)society));
		}
	}
	
	/**
	 * Gets the icon.
	 *
	 * @param society the society
	 * @return the icon
	 */
	private Icon getIcon(Society society) {
		if(society instanceof Country) {
			return Icons.COUNTRY;
		} else if(society instanceof Region) {
			return Icons.REGION;
		} else if(society instanceof City) {
			return Icons.CITY;
		} else {
			throw new IllegalArgumentException("Unknown society.");
		}
	}
	
	/**
	 * Creates the stacked area chart.
	 *
	 * @param valueAxis the value axis
	 * @param areaDataset the dataset
	 * @return the j free chart
	 */
	private ChartPanel createStackedAreaChart(String valueAxis,
			TableXYDataset areaDataset, TableXYDataset lineDataset) {
		JFreeChart chart = ChartFactory.createStackedXYAreaChart(
						null, "Year", valueAxis, areaDataset, 
						PlotOrientation.VERTICAL, true, false, false);
		if(chart.getPlot() instanceof XYPlot) {
			XYPlot xyPlot = (XYPlot) chart.getPlot();
			xyPlot.setBackgroundPaint(Color.WHITE);
			xyPlot.setDomainGridlinePaint(Color.GRAY);
			xyPlot.setRangeGridlinePaint(Color.GRAY);
			// Change to a DateAxis to display years: maintain default styling.
			ValueAxis oldAxis = xyPlot.getDomainAxis();
			xyPlot.setDomainAxis(new DateAxis("Year"));
			xyPlot.getDomainAxis().setLabelFont(oldAxis.getLabelFont());
			xyPlot.getDomainAxis().setTickLabelFont(oldAxis.getTickLabelFont());
			
			if(lineDataset != null) {
				xyPlot.setDataset(1, lineDataset);
				xyPlot.setRenderer(1, new XYLineAndShapeRenderer());
				xyPlot.getRenderer(1).setSeriesPaint(0, Color.black);
				xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
			}
		}
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setMinimumDrawHeight(400);
		chartPanel.setMaximumDrawHeight(1050);
		chartPanel.setMinimumDrawWidth(600);
		chartPanel.setMaximumDrawWidth(1680);
		return chartPanel;
	}
	
	/**
	 * Creates the stacked area chart.
	 *
	 * @param valueAxis the value axis
	 * @param areaDataset the area dataset
	 * @return the chart panel
	 */
	private ChartPanel createStackedAreaChart(String valueAxis,
			TableXYDataset areaDataset) {
		return createStackedAreaChart(valueAxis, areaDataset, null);
	}
	
	/**
	 * Creates the chart.
	 *
	 * @param valueAxis the value axis
	 * @param seriesCollection the series collection
	 * @return the j free chart
	 */
	private ChartPanel createTimeSeriesChart(String valueAxis, 
			TimeSeriesCollection seriesCollection) {
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				null, "Year", valueAxis, seriesCollection, 
				true, false, false);
		if(chart.getPlot() instanceof XYPlot) {
			XYPlot xyPlot = (XYPlot) chart.getPlot();
			xyPlot.setRenderer(new XYLineAndShapeRenderer());
			xyPlot.setBackgroundPaint(Color.WHITE);
			xyPlot.setDomainGridlinePaint(Color.GRAY);
			xyPlot.setRangeGridlinePaint(Color.GRAY);
		}
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setMinimumDrawHeight(400);
		chartPanel.setMaximumDrawHeight(1050);
		chartPanel.setMinimumDrawWidth(600);
		chartPanel.setMaximumDrawWidth(1680);
		return chartPanel;
	}
	
	/**
	 * Initialize.
	 */
	public void initialize() {
		fundsData.removeAllSeries();
		domesticProductPerCapita.removeAllSeries();
		domesticProductIndicatorPanel.initialize();
		localWaterIndicatorPanel.initialize();
		waterReservoirIndicatorPanel.initialize();
		renewableWaterIndicatorPanel.initialize();
		waterConsumptionIndicatorPanel.initialize();
		petroleumReservoirIndicatorPanel.initialize();
		renewableEnergyIndicatorPanel.initialize();
		localEnergyIndicatorPanel.initialize();
		electricityConsumptionIndicatorPanel.initialize();
		localFoodIndicatorPanel.initialize();
		foodConsumptionIndicatorPanel.initialize();
		localFoodData.removeAllSeries();
		foodProductCostData.removeAllSeries();
		foodSupplyCostData.removeAllSeries();
		/* TODO
		foodProductionCost.removeAllSeries();
		foodConsumptionCost.removeAllSeries();
		foodProductionNetCost.removeAllSeries();
		foodConsumptionNetCost.removeAllSeries();
		*/
		
		localWaterData.removeAllSeries();
		renewableWaterData.removeAllSeries();
		waterProductCostData.removeAllSeries();
		waterSupplyCostData.removeAllSeries();
		petroleumProductCostData.removeAllSeries();
		petroleumSupplyCostData.removeAllSeries();
		localElectricityData.removeAllSeries();
		renewableElectricityData.removeAllSeries();
		electricityProductCostData.removeAllSeries();
		electricitySupplyCostData.removeAllSeries();
		foodConsumptionPerCapita.removeAllSeries();
		waterConsumptionPerCapita.removeAllSeries();
		electricityConsumptionPerCapita.removeAllSeries();
		populationDataset.removeAllSeries();
		domesticProduct.removeAllSeries();
		grossDomesticProduct.removeAllSeries();
		landAvailableDataset.removeAllSeries();
		waterReservoirDataset.removeAllSeries();
		petroleumReservoirDataset.removeAllSeries();
		socialSystemRevenue.removeAllSeries();
		infrastructureSystemRevenue.removeAllSeries();
		socialSystemNetRevenue.removeAllSeries();
		infrastructureSystemNetRevenue.removeAllSeries();
		foodUseData.removeAllSeries();
		foodSourceData.removeAllSeries();
		agricultureRevenue.removeAllSeries();
		agricultureNetRevenue.removeAllSeries();
		electricityUseData.removeAllSeries();
		electricitySourceData.removeAllSeries();
		electricityRevenue.removeAllSeries();
		electricityNetRevenue.removeAllSeries();
		petroleumUseData.removeAllSeries();
		petroleumSourceData.removeAllSeries();
		petroleumRevenue.removeAllSeries();
		petroleumNetRevenue.removeAllSeries();
		waterUseData.removeAllSeries();
		waterSourceData.removeAllSeries();
		waterRevenue.removeAllSeries();
		waterNetRevenue.removeAllSeries();
		
		for(SocietyPane subPane : nestedPaneList) {			
			subPane.initialize();
		}
	}
	
	/**
	 * Update agriculture dataset.
	 *
	 * @param year the year
	 */
	private void updateAgricultureDataset(int year) {
		updateSeries(foodUseData, "Society", year, 
				society.getSocialSystem().getFoodConsumption());
		updateSeries(agricultureRevenue, "Capital", year, 
				-society.getAgricultureSystem().getCapitalExpense());
		updateSeries(agricultureRevenue, "Operations", year, 
				-society.getAgricultureSystem().getOperationsExpense());
		updateSeries(agricultureRevenue, "Decommission", year, 
				-society.getAgricultureSystem().getDecommissionExpense());
		updateSeries(agricultureRevenue, "Consumption", year, 
				-society.getAgricultureSystem().getConsumptionExpense());
		updateSeries(agricultureRevenue, "In-Distribution", year, 
				-society.getAgricultureSystem().getDistributionExpense());
		updateSeries(agricultureRevenue, "Import", year, 
				-society.getAgricultureSystem().getImportExpense());
		updateSeries(agricultureRevenue, "Out-Distribution", year, 
				society.getAgricultureSystem().getDistributionRevenue());
		updateSeries(agricultureRevenue, "Export", year, 
				society.getAgricultureSystem().getExportRevenue());
		updateSeries(agricultureRevenue, "Sales", year, 
				society.getAgricultureSystem().getSalesRevenue());
		updateSeries(agricultureNetRevenue, "Net Revenue", year, 
				society.getAgricultureSystem().getCashFlow());
		if(society instanceof City) {
			City city = (City) society;
			for(AgricultureElement element : city.getAgricultureSystem().getInternalElements()) {
				if(element.getMaxLandArea() > 0) {
					updateSeries(foodSourceData, element.getName(), year, 
							element.getFoodProduction());
				}
				
				if(element.getMaxFoodInput() > 0) {
					updateSeries(foodUseData, element.getName(), year, 
							element.getFoodInput());
				}
			}
			for(AgricultureElement element : city.getAgricultureSystem().getExternalElements()) {
				if(element.getMaxFoodInput() > 0) {
					updateSeries(foodSourceData, element.getName(), year, 
							element.getFoodOutput());
				}
			}
		} else {
			updateSeries(foodSourceData, "Production", year, 
					society.getAgricultureSystem().getFoodProduction());
			updateSeries(foodSourceData, "Distribution", year, 
					society.getAgricultureSystem().getFoodInDistribution());
			updateSeries(foodUseData, "Distribution", year, 
					society.getAgricultureSystem().getFoodOutDistribution());
		}
		updateSeries(foodUseData, "Export", year, 
				society.getAgricultureSystem().getFoodExport());
		updateSeries(foodSourceData, "Import", year, 
				society.getAgricultureSystem().getFoodImport());
	}
	
	/**
	 * Update datasets.
	 *
	 * @param year the year
	 * @param superSystem the system
	 */
	public void updateDatasets(int year) {
		if(society instanceof Country) {
			updateSeriesCollection(fundsData, society.getName(), year, 
					((Country)society).getFunds());
		}

		double domesticProductPC = society.getSocialSystem().getDomesticProduct()
				/ society.getSocialSystem().getPopulation();
		updateSeriesCollection(domesticProductPerCapita, society.getName(), year, domesticProductPC);
		domesticProductIndicatorPanel.setValue(domesticProductPC);
		for(Society nestedSociety : society.getNestedSocieties()) {
			updateSeriesCollection(domesticProductPerCapita, nestedSociety.getName(), year, 
					nestedSociety.getSocialSystem().getDomesticProduct()/nestedSociety.getSocialSystem().getPopulation());
		}

		double localFood = Math.max(0, society.getAgricultureSystem().getFoodProduction()
				- society.getAgricultureSystem().getFoodOutDistribution()
				- society.getAgricultureSystem().getFoodExport())
				/ society.getTotalFoodDemand();
		updateSeriesCollection(localFoodData, society.getName(), year, localFood);
		localFoodIndicatorPanel.setValue(localFood);
		for(Society nestedSociety : society.getNestedSocieties()) {
			updateSeriesCollection(localFoodData, nestedSociety.getName(), year, 
					Math.max(0, nestedSociety.getAgricultureSystem().getFoodProduction()
							- nestedSociety.getAgricultureSystem().getFoodOutDistribution()
							- nestedSociety.getAgricultureSystem().getFoodExport())
							/ nestedSociety.getTotalFoodDemand());
		}

		double localWater = Math.max(0, society.getWaterSystem().getWaterProduction()
				+ society.getWaterSystem().getWaterFromArtesianWell()
				- society.getWaterSystem().getWaterOutDistribution()
				- society.getWaterSystem().getWaterWasted())
				/ society.getTotalWaterDemand();
		updateSeriesCollection(localWaterData, society.getName(), year, localWater);
		localWaterIndicatorPanel.setValue(localWater);
		for(Society nestedSociety : society.getNestedSocieties()) {
			updateSeriesCollection(localWaterData, nestedSociety.getName(), year, 
					Math.max(0, nestedSociety.getWaterSystem().getWaterProduction()
							+ nestedSociety.getWaterSystem().getWaterFromArtesianWell()
							- nestedSociety.getWaterSystem().getWaterOutDistribution()
							- nestedSociety.getWaterSystem().getWaterWasted())
							/ nestedSociety.getTotalWaterDemand());
		}
		
		if(society.getWaterSystem().getWaterProduction() > 0) {
			double renewableWater = society.getWaterSystem().getRenewableWaterProduction()
					/ (society.getWaterSystem().getWaterProduction() 
							+ society.getWaterSystem().getWaterFromArtesianWell());
			updateSeriesCollection(renewableWaterData, society.getName(), year, renewableWater);
			renewableWaterIndicatorPanel.setValue(renewableWater);
		}
		for(Society nestedSociety : society.getNestedSocieties()) {
			if(nestedSociety.getWaterSystem().getWaterProduction() > 0) {
				updateSeriesCollection(renewableWaterData, nestedSociety.getName(), year, 
						nestedSociety.getWaterSystem().getRenewableWaterProduction()
						/ (nestedSociety.getWaterSystem().getWaterProduction() 
								+ nestedSociety.getWaterSystem().getWaterFromArtesianWell()));
			}
		}

		double electricityFromBurningLocalPetroleum = 0;
		if(society.getEnergySystem().getElectricitySystem().getPetroleumBurned() > 0) {
			electricityFromBurningLocalPetroleum = 
					society.getEnergySystem().getElectricitySystem().getElectricityFromBurningPetroleum()
					* Math.min(society.getEnergySystem().getElectricitySystem().getPetroleumBurned(), 
							society.getEnergySystem().getPetroleumSystem().getPetroleumProduction()
							- society.getEnergySystem().getPetroleumSystem().getPetroleumOutDistribution()
							- society.getEnergySystem().getPetroleumSystem().getPetroleumExport())
					/ society.getEnergySystem().getElectricitySystem().getPetroleumBurned();
		}
		double localElectricity = Math.max(0, society.getEnergySystem().getElectricitySystem().getElectricityProduction()
				- society.getEnergySystem().getElectricitySystem().getElectricityOutDistribution()
				- society.getEnergySystem().getElectricitySystem().getElectricityWasted()
				+ electricityFromBurningLocalPetroleum)
				/ society.getTotalElectricityDemand();
		updateSeriesCollection(localElectricityData, society.getName(), year, localElectricity);
		localEnergyIndicatorPanel.setValue(localElectricity);
		for(Society nestedSociety : society.getNestedSocieties()) {
			electricityFromBurningLocalPetroleum = 0;
			if(nestedSociety.getEnergySystem().getElectricitySystem().getPetroleumBurned() > 0) {
				electricityFromBurningLocalPetroleum = 
						nestedSociety.getEnergySystem().getElectricitySystem().getElectricityFromBurningPetroleum()
						* Math.min(nestedSociety.getEnergySystem().getElectricitySystem().getPetroleumBurned(), 
								nestedSociety.getEnergySystem().getPetroleumSystem().getPetroleumProduction()
								- nestedSociety.getEnergySystem().getPetroleumSystem().getPetroleumOutDistribution()
								- nestedSociety.getEnergySystem().getPetroleumSystem().getPetroleumExport())
						/ nestedSociety.getEnergySystem().getElectricitySystem().getPetroleumBurned();
			}
			updateSeriesCollection(localElectricityData, nestedSociety.getName(), year, 
					Math.max(0, nestedSociety.getEnergySystem().getElectricitySystem().getElectricityProduction()
							- nestedSociety.getEnergySystem().getElectricitySystem().getElectricityOutDistribution()
							- nestedSociety.getEnergySystem().getElectricitySystem().getElectricityWasted()
							+ electricityFromBurningLocalPetroleum)
							/ nestedSociety.getTotalElectricityDemand());
		}
		
		double renewableElectricity = society.getEnergySystem().getElectricitySystem().getRenewableElectricityProduction()
				/ (society.getEnergySystem().getElectricitySystem().getElectricityProduction()
						+ society.getEnergySystem().getElectricitySystem().getElectricityFromBurningPetroleum());
		updateSeriesCollection(renewableElectricityData, society.getName(), year, renewableElectricity);
		renewableEnergyIndicatorPanel.setValue(renewableElectricity);
		for(Society nestedSociety : society.getNestedSocieties()) {
			updateSeriesCollection(renewableElectricityData, nestedSociety.getName(), year, 
					nestedSociety.getEnergySystem().getElectricitySystem().getRenewableElectricityProduction()
					/ (nestedSociety.getEnergySystem().getElectricitySystem().getElectricityProduction()
					+ society.getEnergySystem().getElectricitySystem().getElectricityFromBurningPetroleum()));
		}

		if(society.getAgricultureSystem().getFoodProduction() > 0) {
			updateSeriesCollection(foodProductCostData, society.getName(), year, 
					society.getAgricultureSystem().getLifecycleExpense()
					/ society.getAgricultureSystem().getFoodProduction());
		}
		for(Society nestedSociety : society.getNestedSocieties()) {
			if(nestedSociety.getAgricultureSystem().getFoodProduction() > 0) {
				updateSeriesCollection(foodProductCostData, nestedSociety.getName(), year, 
						nestedSociety.getAgricultureSystem().getLifecycleExpense()
						/ nestedSociety.getAgricultureSystem().getFoodProduction());
			}
		}
		
		updateSeriesCollection(foodSupplyCostData, society.getName(), year, 
				society.getAgricultureSystem().getTotalExpense()
				/ society.getAgricultureSystem().getTotalFoodSupply());
		for(Society nestedSociety : society.getNestedSocieties()) {
			updateSeriesCollection(foodSupplyCostData, nestedSociety.getName(), year, 
					nestedSociety.getAgricultureSystem().getTotalExpense()
					/ nestedSociety.getAgricultureSystem().getTotalFoodSupply());
		}
		/* TODO
		if(society.getAgricultureSystem().getFoodProduction() > 0) {
			updateSeries(foodProductionCost, "Capital", year, 
					society.getAgricultureSystem().getCapitalExpense()
					/ society.getAgricultureSystem().getFoodProduction());
			updateSeries(foodProductionCost, "Operations", year, 
					society.getAgricultureSystem().getOperationsExpense()
					/ society.getAgricultureSystem().getFoodProduction());
			updateSeries(foodProductionCost, "Decommission", year, 
					society.getAgricultureSystem().getDecommissionExpense()
					/ society.getAgricultureSystem().getFoodProduction());
			updateSeries(foodProductionCost, "Water", year, 
					society.getAgricultureSystem().getWaterConsumption()
					* society.getGlobals().getWaterDomesticPrice()
					/ society.getAgricultureSystem().getFoodProduction());
		}
		if(society.getTotalFoodDemand() > 0) {
			// TODO this is not quite right
			updateSeries(foodConsumptionCost, "Net Distribution", year, 
					(society.getAgricultureSystem().getDistributionExpense()
					- society.getAgricultureSystem().getDistributionRevenue())
					/ society.getTotalFoodDemand());
			updateSeries(foodConsumptionCost, "Import", year, 
					society.getAgricultureSystem().getImportExpense()
					/ society.getTotalFoodDemand());
			updateSeries(foodConsumptionCost, "Export", year, 
					-society.getAgricultureSystem().getExportRevenue()
					/ society.getTotalFoodDemand());
			updateSeries(foodConsumptionNetCost, "Net Cost", year, 
					society.getAgricultureSystem().getTotalExpense()
					/ society.getTotalFoodDemand());
		}
		*/
		
		if(society.getWaterSystem().getWaterProduction() > 0) {
			updateSeriesCollection(waterProductCostData, society.getName(), year, 
					society.getWaterSystem().getLifecycleExpense()
					/ society.getWaterSystem().getWaterProduction());
		}
		for(Society nestedSociety : society.getNestedSocieties()) {
			if(nestedSociety.getWaterSystem().getWaterProduction() > 0) {
				updateSeriesCollection(waterProductCostData, nestedSociety.getName(), year, 
						nestedSociety.getWaterSystem().getLifecycleExpense()
						/ nestedSociety.getWaterSystem().getWaterProduction());
			}
		}
		
		updateSeriesCollection(waterSupplyCostData, society.getName(), year, 
				society.getWaterSystem().getTotalExpense()
				/ society.getWaterSystem().getTotalWaterSupply());
		for(Society nestedSociety : society.getNestedSocieties()) {
			updateSeriesCollection(waterSupplyCostData, nestedSociety.getName(), year, 
					nestedSociety.getWaterSystem().getTotalExpense()
					/ nestedSociety.getWaterSystem().getTotalWaterSupply());
		}

		if(society.getEnergySystem().getPetroleumSystem().getPetroleumProduction() > 0) {
			updateSeriesCollection(petroleumProductCostData, society.getName(), year, 
					society.getEnergySystem().getPetroleumSystem().getLifecycleExpense()
					/ society.getEnergySystem().getPetroleumSystem().getPetroleumProduction());
		}
		for(Society nestedSociety : society.getNestedSocieties()) {
			if(nestedSociety.getEnergySystem().getPetroleumSystem().getPetroleumProduction() > 0) {
				updateSeriesCollection(petroleumProductCostData, nestedSociety.getName(), year, 
						nestedSociety.getEnergySystem().getPetroleumSystem().getLifecycleExpense()
						/ nestedSociety.getEnergySystem().getPetroleumSystem().getPetroleumProduction());
			}
		}
		
		updateSeriesCollection(petroleumSupplyCostData, society.getName(), year, 
				society.getEnergySystem().getPetroleumSystem().getTotalExpense()
				/ society.getEnergySystem().getPetroleumSystem().getTotalPetroleumSupply());
		for(Society nestedSociety : society.getNestedSocieties()) {
			updateSeriesCollection(petroleumSupplyCostData, nestedSociety.getName(), year, 
					nestedSociety.getEnergySystem().getPetroleumSystem().getTotalExpense()
					/ nestedSociety.getEnergySystem().getPetroleumSystem().getTotalPetroleumSupply());
		}

		if(society.getEnergySystem().getElectricitySystem().getElectricityProduction() > 0) {
			updateSeriesCollection(electricityProductCostData, society.getName(), year, 
					society.getEnergySystem().getElectricitySystem().getLifecycleExpense()
					/ society.getEnergySystem().getElectricitySystem().getElectricityProduction());
		}
		for(Society nestedSociety : society.getNestedSocieties()) {
			if(nestedSociety.getEnergySystem().getElectricitySystem().getElectricityProduction() > 0) {
				updateSeriesCollection(electricityProductCostData, nestedSociety.getName(), year, 
						nestedSociety.getEnergySystem().getElectricitySystem().getLifecycleExpense()
						/ nestedSociety.getEnergySystem().getElectricitySystem().getElectricityProduction());
			}

		}

		if(society.getEnergySystem().getElectricitySystem().getElectricityProduction() > 0) {
			updateSeriesCollection(electricitySupplyCostData, society.getName(), year, 
					society.getEnergySystem().getElectricitySystem().getTotalExpense()
					/ society.getEnergySystem().getElectricitySystem().getTotalElectricitySupply());
		}
		for(Society nestedSociety : society.getNestedSocieties()) {
			if(nestedSociety.getEnergySystem().getElectricitySystem().getElectricityProduction() > 0) {
				updateSeriesCollection(electricitySupplyCostData, nestedSociety.getName(), year, 
						nestedSociety.getEnergySystem().getElectricitySystem().getTotalExpense()
						/ nestedSociety.getEnergySystem().getElectricitySystem().getTotalElectricitySupply());
			}
		}

		updateSeriesCollection(foodConsumptionPerCapita, society.getName(), 
				year, society.getSocialSystem().getFoodConsumptionPerCapita());
		foodConsumptionIndicatorPanel.setValue(society.getSocialSystem().getFoodConsumptionPerCapita());
		for(Society nestedSociety : society.getNestedSocieties()) {
			updateSeriesCollection(foodConsumptionPerCapita, nestedSociety.getName(), year, 
					nestedSociety.getSocialSystem().getFoodConsumptionPerCapita());
		}

		updateSeriesCollection(waterConsumptionPerCapita, society.getName(), 
				year, society.getSocialSystem().getWaterConsumptionPerCapita());
		waterConsumptionIndicatorPanel.setValue(
				society.getSocialSystem().getWaterConsumptionPerCapita());
		for(Society nestedSociety : society.getNestedSocieties()) {
			updateSeriesCollection(waterConsumptionPerCapita, nestedSociety.getName(), year, 
					nestedSociety.getSocialSystem().getWaterConsumptionPerCapita());
		}
		
		updateSeriesCollection(electricityConsumptionPerCapita, society.getName(), 
				year, society.getSocialSystem().getElectricityConsumptionPerCapita());
		electricityConsumptionIndicatorPanel.setValue(society.getSocialSystem().getElectricityConsumptionPerCapita());
		for(Society nestedSociety : society.getNestedSocieties()) {
			updateSeriesCollection(electricityConsumptionPerCapita, nestedSociety.getName(), year, 
					nestedSociety.getSocialSystem().getElectricityConsumptionPerCapita());
		}
		
		
		updateAgricultureDataset(year);
		updateWaterDataset(year);
		updatePetroleumDataset(year);
		updateElectricityDataset(year);

		updateSeries(infrastructureSystemNetRevenue, "Net Revenue", year, 
				society.getCashFlow());

		waterReservoirIndicatorPanel.setValue(
				society.getWaterSystem().getWaterReservoirVolume());
		petroleumReservoirIndicatorPanel.setValue(
				society.getEnergySystem().getPetroleumSystem().getPetroleumReservoirVolume());
		
		if(society instanceof City) {
			updateSeries(populationDataset, society.getName(), year, 
					society.getSocialSystem().getPopulation());
			
			updateSeries(domesticProduct, society.getName(), year, 
					society.getSocialSystem().getDomesticProduct());
			updateSeries(grossDomesticProduct, society.getName(), year, 
					society.getSocialSystem().getDomesticProduct());
			updateSeries(landAvailableDataset, society.getName(), year, 
					society.getAgricultureSystem().getArableLandArea()
					- society.getAgricultureSystem().getLandAreaUsed());
			updateSeries(waterReservoirDataset, society.getName(), year, 
					society.getWaterSystem().getWaterReservoirVolume());
			updateSeries(petroleumReservoirDataset, society.getName(), year, 
					society.getEnergySystem().getPetroleumSystem().getPetroleumReservoirVolume());
			updateSeries(socialSystemRevenue, "Capital", year, 
					-society.getSocialSystem().getCapitalExpense());
			updateSeries(socialSystemRevenue, "Operations", year, 
					-society.getSocialSystem().getOperationsExpense());
			updateSeries(socialSystemRevenue, "Decommission", year, 
					-society.getSocialSystem().getDecommissionExpense());
			updateSeries(socialSystemRevenue, "Consumption", year, 
					-society.getSocialSystem().getConsumptionExpense());
			updateSeries(socialSystemRevenue, "Import", year, 
					-society.getSocialSystem().getImportExpense());
			updateSeries(socialSystemRevenue, "Export", year, 
					society.getSocialSystem().getExportRevenue());
			updateSeries(socialSystemRevenue, "Sales", year, 
					society.getSocialSystem().getSalesRevenue());
			updateSeries(socialSystemNetRevenue, "Net Revenue", year, 
					society.getSocialSystem().getCashFlow());
		} else {
			for(Society nestedSociety : society.getNestedSocieties()) {
				updateSeries(populationDataset, nestedSociety.getName(), year, 
						nestedSociety.getSocialSystem().getPopulation());
			}

			updateSeries(grossDomesticProduct, "Gross Product", year, 
					society.getSocialSystem().getDomesticProduct());
			for(Society nestedSociety : society.getNestedSocieties()) {
				updateSeries(domesticProduct, nestedSociety.getName(), year, 
						nestedSociety.getSocialSystem().getDomesticProduct());
			}

			for(Society nestedSociety : society.getNestedSocieties()) {
				updateSeries(landAvailableDataset, nestedSociety.getName(), year, 
						nestedSociety.getAgricultureSystem().getArableLandArea()
						- nestedSociety.getAgricultureSystem().getLandAreaUsed());
			}

			for(Society nestedSociety : society.getNestedSocieties()) {
				updateSeries(waterReservoirDataset, nestedSociety.getName(), year, 
						nestedSociety.getWaterSystem().getWaterReservoirVolume());
			}

			for(Society nestedSociety : society.getNestedSocieties()) {
				updateSeries(petroleumReservoirDataset, nestedSociety.getName(), year, 
						nestedSociety.getEnergySystem().getPetroleumSystem().getPetroleumReservoirVolume());
			}

			for(Society nestedSociety : society.getNestedSocieties()) {
				updateSeries(socialSystemRevenue, nestedSociety.getName(), year, 
						nestedSociety.getCashFlow());
			}
			updateSeries(socialSystemNetRevenue, "Net Revenue", year, 
					society.getCashFlow());
		}
		
		for(InfrastructureSystem nestedSociety : society.getSystems()) {
			updateSeries(infrastructureSystemRevenue, nestedSociety.getName(), year, 
					nestedSociety.getCashFlow());
		}
		
		for(SocietyPane subPane : nestedPaneList) {			
			subPane.updateDatasets(year);
		}
	}

	
	/**
	 * Update electricity dataset.
	 *
	 * @param year the year
	 */
	private void updateElectricityDataset(int year) {	
		updateSeries(electricityRevenue, "Capital", year, 
				-society.getEnergySystem().getElectricitySystem().getCapitalExpense());
		updateSeries(electricityRevenue, "Operations", year, 
				-society.getEnergySystem().getElectricitySystem().getOperationsExpense());
		updateSeries(electricityRevenue, "Decommission", year, 
				-society.getEnergySystem().getElectricitySystem().getDecommissionExpense());
		updateSeries(electricityRevenue, "Consumption", year, 
				-society.getEnergySystem().getElectricitySystem().getConsumptionExpense());
		updateSeries(electricityRevenue, "In-Distribution", year, 
				-society.getEnergySystem().getElectricitySystem().getDistributionExpense());
		updateSeries(electricityRevenue, "Out-Distribution", year, 
				society.getEnergySystem().getElectricitySystem().getDistributionRevenue());
		updateSeries(electricityRevenue, "Sales", year, 
				society.getEnergySystem().getElectricitySystem().getSalesRevenue());
		updateSeries(electricityNetRevenue, "Net Revenue", year, 
				society.getEnergySystem().getElectricitySystem().getCashFlow());
		updateSeries(electricityUseData, "Society", year, 
				society.getSocialSystem().getElectricityConsumption());
		updateSeries(electricityUseData, "Water", year, 
				society.getWaterSystem().getElectricityConsumption());
		updateSeries(electricityUseData, "Petroleum", year, 
				society.getEnergySystem().getPetroleumSystem().getElectricityConsumption());
		updateSeries(electricityUseData, "Wasted", year, 
				society.getEnergySystem().getElectricitySystem().getElectricityWasted());
		if(society instanceof City) {
			City city = (City) society;
			for(ElectricityElement element : city.getEnergySystem().getElectricitySystem().getInternalElements()) {
				if(element.getMaxElectricityProduction() > 0) {
					updateSeries(electricitySourceData, element.getName(), year, 
							element.getElectricityProduction());
				}
				
				if(element.getMaxElectricityInput() > 0) {
					updateSeries(electricityUseData, element.getName(), year, 
							element.getElectricityInput());
				}
			}

			for(ElectricityElement element : city.getEnergySystem().getElectricitySystem().getExternalElements()) {
				if(element.getMaxElectricityInput() > 0) {
					updateSeries(electricitySourceData, element.getName(), year, 
							element.getElectricityOutput());
				}
			}
		} else {
			updateSeries(electricitySourceData, "Production", year, 
					society.getEnergySystem().getElectricitySystem().getElectricityProduction());
			updateSeries(electricitySourceData, "Distribution", year, 
					society.getEnergySystem().getElectricitySystem().getElectricityInDistribution());
			updateSeries(electricityUseData, "Distribution", year, 
					society.getEnergySystem().getElectricitySystem().getElectricityOutDistribution());
		}
		updateSeries(electricitySourceData, "Petroleum Burn", year, 
				society.getEnergySystem().getElectricitySystem().getElectricityFromBurningPetroleum());
	}
	
	/**
	 * Update petroleum dataset.
	 *
	 * @param year the year
	 */
	private void updatePetroleumDataset(int year) {		
		updateSeries(petroleumRevenue, "Capital", year, 
				-society.getEnergySystem().getPetroleumSystem().getCapitalExpense());
		updateSeries(petroleumRevenue, "Operations", year, 
				-society.getEnergySystem().getPetroleumSystem().getOperationsExpense());
		updateSeries(petroleumRevenue, "Decommission", year, 
				-society.getEnergySystem().getPetroleumSystem().getDecommissionExpense());
		updateSeries(petroleumRevenue, "Consumption", year, 
				-society.getEnergySystem().getPetroleumSystem().getConsumptionExpense());
		updateSeries(petroleumRevenue, "In-Distribution", year, 
				-society.getEnergySystem().getPetroleumSystem().getDistributionExpense());
		updateSeries(petroleumRevenue, "Import", year, 
				-society.getEnergySystem().getPetroleumSystem().getImportExpense());
		updateSeries(petroleumRevenue, "Out-Distribution", year, 
				society.getEnergySystem().getPetroleumSystem().getDistributionRevenue());
		updateSeries(petroleumRevenue, "Export", year,
				society.getEnergySystem().getPetroleumSystem().getExportRevenue());
		updateSeries(petroleumRevenue, "Sales", year, 
				society.getEnergySystem().getPetroleumSystem().getSalesRevenue());
		updateSeries(petroleumNetRevenue, "Net Revenue", year, 
				society.getEnergySystem().getPetroleumSystem().getCashFlow());	
		updateSeries(petroleumUseData, "Electricity", year, 
				society.getEnergySystem().getPetroleumConsumption() 
				- society.getEnergySystem().getElectricitySystem().getPetroleumBurned());
		updateSeries(petroleumUseData, "Direct Burn", year, 
				society.getEnergySystem().getElectricitySystem().getPetroleumBurned());
		if(society instanceof City) {
			City city = (City) society;
			for(PetroleumElement element : city.getEnergySystem().getPetroleumSystem().getInternalElements()) {
				if(element.getMaxPetroleumProduction() > 0) {
					updateSeries(petroleumSourceData, element.getName(), year, 
							element.getPetroleumProduction());
				}
				
				if(element.getMaxPetroleumInput() > 0) {
					updateSeries(petroleumUseData, element.getName(), year, 
							element.getPetroleumInput());
				}
			}
			for(PetroleumElement element : city.getEnergySystem().getPetroleumSystem().getExternalElements()) {
				if(element.getMaxPetroleumInput() > 0) {
					updateSeries(petroleumSourceData, element.getName(), year, 
							element.getPetroleumOutput());
				}
			}
		} else {
			updateSeries(petroleumSourceData, "Production", year, 
					society.getEnergySystem().getPetroleumSystem().getPetroleumProduction());
			updateSeries(petroleumSourceData, "Distribution", year, 
					society.getEnergySystem().getPetroleumSystem().getPetroleumInDistribution());
			updateSeries(petroleumUseData, "Distribution", year, 
					society.getEnergySystem().getPetroleumSystem().getPetroleumOutDistribution());
		}
		updateSeries(petroleumSourceData, "Import", year, 
				society.getEnergySystem().getPetroleumSystem().getPetroleumImport());
		updateSeries(petroleumUseData, "Export", year, 
				society.getEnergySystem().getPetroleumSystem().getPetroleumExport());
	}

	
	/**
	 * Update series.
	 *
	 * @param series the series
	 * @param year the year
	 * @param value the value
	 */
	private void updateSeries(DefaultTableXYDataset dataset, Comparable<?> key, int year, double value) {
		int index = dataset.indexOf(key);
		if(index < 0) {
			dataset.addSeries(new XYSeries(key, true, false));
			index = dataset.indexOf(key);
		}
		dataset.getSeries(index).addOrUpdate(new Year(year).getLastMillisecond(), value);
	}
	
	/**
	 * Update a series collection.
	 *
	 * @param seriesCollection the series collection
	 * @param key the key
	 * @param year the year
	 * @param value the value
	 */
	private void updateSeriesCollection(TimeSeriesCollection seriesCollection, 
			Comparable<?> key, int year, double value) {
		// Note: TimeSeriesCollection.getSeries returns a null TimeSeries
		// if the key does not exist, however XYSeriesCollection.getSeries
		// actually throws an UnknownKeyException which must be handled 
		// differently. Both are included here for good programming practice.
		TimeSeries series;
		try {
			series = seriesCollection.getSeries(key);
		} catch(UnknownKeyException ex) {
			series = null;
		}
		if(series == null) {
			series = new TimeSeries(key);
			seriesCollection.addSeries(series);
		}
		series.addOrUpdate(new Year(year), value);
	}
	
	/**
	 * Update agriculture dataset.
	 *
	 * @param year the year
	 */
	private void updateWaterDataset(int year) {
		updateSeries(waterRevenue, "Capital", year, 
				-society.getWaterSystem().getCapitalExpense());
		updateSeries(waterRevenue, "Operations", year, 
				-society.getWaterSystem().getOperationsExpense());
		updateSeries(waterRevenue, "Decommission", year, 
				-society.getWaterSystem().getDecommissionExpense());
		updateSeries(waterRevenue, "Consumption", year, 
				-society.getWaterSystem().getConsumptionExpense());
		updateSeries(waterRevenue, "In-Distribution", year, 
				-society.getWaterSystem().getDistributionExpense());
		updateSeries(waterRevenue, "Import", year, 
				-society.getWaterSystem().getImportExpense());
		updateSeries(waterRevenue, "Out-Distribution", year, 
				-society.getWaterSystem().getDistributionExpense());
		updateSeries(waterRevenue, "Sales", year, 
				society.getWaterSystem().getSalesRevenue());
		updateSeries(waterNetRevenue, "Net Revenue", year, 
				society.getWaterSystem().getCashFlow());	

		updateSeries(waterUseData, "Society", year, 
				society.getSocialSystem().getWaterConsumption());
		updateSeries(waterUseData, "Agriculture", year, 
				society.getAgricultureSystem().getWaterConsumption());
		updateSeries(waterUseData, "Energy", year, 
				society.getEnergySystem().getWaterConsumption());
		updateSeries(waterUseData, "Wasted", year, 
				society.getWaterSystem().getWaterWasted());
		if(society instanceof City) {
			City city = (City) society;
			for(WaterElement element : city.getWaterSystem().getInternalElements()) {
				if(element.getMaxWaterProduction() > 0) {
					updateSeries(waterSourceData, element.getName(), year, 
							element.getWaterProduction());
				}
				
				if(element.getMaxWaterInput() > 0) {
					updateSeries(waterUseData, element.getName(), year, 
							element.getWaterInput());
				}
			}
			for(WaterElement element : city.getWaterSystem().getExternalElements()) {
				if((city.getName().equals(element.getDestination())
						&& element.getMaxWaterInput() > 0)
					|| element.getMaxWaterProduction() > 0) {
					updateSeries(waterSourceData, element.getName(), year, 
							element.getWaterOutput());
				}
			}
		} else {
			updateSeries(waterSourceData, "Production", year, 
					society.getWaterSystem().getWaterProduction());
			updateSeries(waterSourceData, "Distribution", year, 
					society.getWaterSystem().getWaterInDistribution());
			updateSeries(waterUseData, "Distribution", year,
					society.getWaterSystem().getWaterOutDistribution());
		}
		updateSeries(waterSourceData, "Artesian Well", year, 
				society.getWaterSystem().getWaterFromArtesianWell());
		updateSeries(waterSourceData, "Import", year, 
				society.getWaterSystem().getWaterImport());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationInitialized(UpdateEvent event) {
		for(SpatialStatePanel statePanel : statePanelList) {
			statePanel.repaint();
		}
		for(SocietyPane nestedPane : nestedPaneList) {
			nestedPane.repaint();
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		for(SpatialStatePanel statePanel : statePanelList) {
			statePanel.repaint();
		}
		for(SocietyPane nestedPane : nestedPaneList) {
			nestedPane.repaint();
		}
	}
}
