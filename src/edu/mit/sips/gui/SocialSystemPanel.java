package edu.mit.sips.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.social.CitySocialSystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.io.Icons;

public class SocialSystemPanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = -8472419089458128152L;
	
	private final LinearIndicatorPanel domesticProductIndicatorPanel;

	
	TimeSeriesCollection fundsData = new TimeSeriesCollection();
	TimeSeriesCollection domesticProductPerCapita = new TimeSeriesCollection();
	DefaultTableXYDataset infrastructureSystemRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset infrastructureSystemNetRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset populationDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset domesticProduct = new DefaultTableXYDataset();
	DefaultTableXYDataset grossDomesticProduct = new DefaultTableXYDataset();
	DefaultTableXYDataset socialSystemRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset socialSystemNetRevenue = new DefaultTableXYDataset();

	public SocialSystemPanel(SocialSystem.Local socialSystem) {
		super(socialSystem);

		JPanel indicatorsPanel = new JPanel();
		indicatorsPanel.setLayout(
				new BoxLayout(indicatorsPanel, BoxLayout.LINE_AXIS));
		domesticProductIndicatorPanel = new LinearIndicatorPanel(
				"Economic Production", 0, 100000);
		indicatorsPanel.add(domesticProductIndicatorPanel);
		addTab("Indicators", Icons.INDICATORS, indicatorsPanel);

		if(getSociety() instanceof Country) {
			addTab("Funds", Icons.FUNDS, createTimeSeriesChart(
					"Funds (SAR)", fundsData));
		}

		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Revenue (SAR/year)", infrastructureSystemRevenue, 
				infrastructureSystemNetRevenue));
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Revenue (SAR/year)", socialSystemRevenue, socialSystemNetRevenue));
		addTab("Domestic Product", Icons.PRODUCT, createStackedAreaChart(
				"Domestic Product (SAR/year)", domesticProduct, 
				grossDomesticProduct));
		addTab("Domestic Product PC", Icons.PRODUCT, createTimeSeriesChart(
				"Domestic Product per capita (SAR/person)", 
				domesticProductPerCapita));
		addTab("Population", Icons.POPULATION, createStackedAreaChart(
				"Population", populationDataset));

	}

	/**
	 * Gets the social system.
	 *
	 * @return the social system
	 */
	public SocialSystem.Local getSocialSystem() {
		return (SocialSystem.Local) getInfrastructureSystem(); 
	}

	@Override
	public void simulationInitialized(UpdateEvent event) {
		// nothing to do here
	}

	@Override
	public void simulationUpdated(UpdateEvent event) {
		// nothing to do here
	}

	@Override
	public void initialize() {
		fundsData.removeAllSeries();
		infrastructureSystemRevenue.removeAllSeries();
		infrastructureSystemNetRevenue.removeAllSeries();
		domesticProductPerCapita.removeAllSeries();
		populationDataset.removeAllSeries();
		domesticProduct.removeAllSeries();
		grossDomesticProduct.removeAllSeries();
		socialSystemRevenue.removeAllSeries();
		socialSystemNetRevenue.removeAllSeries();
		domesticProductIndicatorPanel.initialize();
	}

	@Override
	public void update(int year) {


		if(getSociety() instanceof Country) {
			updateSeriesCollection(fundsData, getSociety().getName(), year, 
					((Country)getSociety()).getFunds());
		}
		updateSeries(infrastructureSystemNetRevenue, "Net Revenue", year, 
				getSociety().getCashFlow());

		
		for(InfrastructureSystem nestedSociety : getSociety().getInfrastructureSystems()) {
			updateSeries(infrastructureSystemRevenue, nestedSociety.getName(), year, 
					nestedSociety.getCashFlow());
		}
		
		updateSeriesCollection(domesticProductPerCapita, getSociety().getName(), 
				year, getSocialSystem().getDomesticProductPerCapita());
		domesticProductIndicatorPanel.setValue(
				getSocialSystem().getDomesticProductPerCapita());
		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			updateSeriesCollection(domesticProductPerCapita, nestedSociety.getName(), 
					year, nestedSociety.getSocialSystem().getDomesticProductPerCapita());
		}
		
		if(getSocialSystem() instanceof CitySocialSystem) {
			updateSeries(populationDataset, getSociety().getName(), year, 
					getSocialSystem().getPopulation());
			
			updateSeries(domesticProduct, getSociety().getName(), year, 
					getSocialSystem().getDomesticProduct());
			updateSeries(grossDomesticProduct, getSociety().getName(), year, 
					getSocialSystem().getDomesticProduct());
			updateSeries(socialSystemRevenue, "Capital", year, 
					-getSocialSystem().getCapitalExpense());
			updateSeries(socialSystemRevenue, "Operations", year, 
					-getSocialSystem().getOperationsExpense());
			updateSeries(socialSystemRevenue, "Decommission", year, 
					-getSocialSystem().getDecommissionExpense());
			updateSeries(socialSystemRevenue, "Consumption", year, 
					-getSocialSystem().getConsumptionExpense());
			updateSeries(socialSystemRevenue, "Import", year, 
					-getSocialSystem().getImportExpense());
			updateSeries(socialSystemRevenue, "Export", year, 
					getSocialSystem().getExportRevenue());
			updateSeries(socialSystemRevenue, "Sales", year, 
					getSocialSystem().getSalesRevenue());
			updateSeries(socialSystemNetRevenue, "Net Revenue", year, 
					getSocialSystem().getCashFlow());
		} else {
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(populationDataset, nestedSociety.getName(), year, 
						nestedSociety.getSocialSystem().getPopulation());
			}

			updateSeries(grossDomesticProduct, "Gross Product", year, 
					getSociety().getSocialSystem().getDomesticProduct());
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(domesticProduct, nestedSociety.getName(), year, 
						nestedSociety.getSocialSystem().getDomesticProduct());
			}

			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(socialSystemRevenue, nestedSociety.getName(), year, 
						nestedSociety.getCashFlow());
			}
			updateSeries(socialSystemNetRevenue, "Net Revenue", year, 
					getSociety().getCashFlow());
		}
	}

}
