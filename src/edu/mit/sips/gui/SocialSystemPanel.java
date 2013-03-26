package edu.mit.sips.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.io.Icons;

public class SocialSystemPanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = -8472419089458128152L;
	
	private final LinearIndicatorPanel domesticProductIndicatorPanel;

	
	TimeSeriesCollection fundsData = new TimeSeriesCollection();
	DefaultTableXYDataset infrastructureSystemRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset infrastructureSystemNetRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset populationDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset domesticProduct = new DefaultTableXYDataset();
	DefaultTableXYDataset domesticProductPerCapita = new DefaultTableXYDataset();
	DefaultTableXYDataset societyRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset societyNetRevenue = new DefaultTableXYDataset();

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
				null, infrastructureSystemNetRevenue));
		if(!(getSocialSystem() instanceof DefaultSocialSystem.Local)) {
			addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
					"Revenue (SAR/year)", societyRevenue, null, societyNetRevenue));
		}
		addTab("Domestic Product", Icons.PRODUCT, createStackedAreaChart(
				"Domestic Product (SAR/year)", domesticProduct, 
				"Domestic Product per Capita (SAR/year)", domesticProductPerCapita));
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
		populationDataset.removeAllSeries();
		domesticProduct.removeAllSeries();
		domesticProductPerCapita.removeAllSeries();
		societyRevenue.removeAllSeries();
		societyNetRevenue.removeAllSeries();
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
		
		updateSeries(domesticProductPerCapita, getSociety().getName() + " (per capita)", 
				year, getSocialSystem().getDomesticProductPerCapita());
		domesticProductIndicatorPanel.setValue(
				getSocialSystem().getDomesticProductPerCapita());
		
		if(getSocialSystem() instanceof DefaultSocialSystem.Local) {
			updateSeries(populationDataset, getSociety().getName(), year, 
					getSocialSystem().getPopulation());
			
			updateSeries(domesticProduct, getSociety().getName(), year, 
					getSocialSystem().getDomesticProduct());
		} else {
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(populationDataset, nestedSociety.getName(), year, 
						nestedSociety.getSocialSystem().getPopulation());
			}

			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(domesticProduct, nestedSociety.getName(), year, 
						nestedSociety.getSocialSystem().getDomesticProduct());
			}

			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(societyRevenue, nestedSociety.getName(), year, 
						nestedSociety.getCashFlow());
			}
			updateSeries(societyNetRevenue, "Net Revenue", year, 
					getSociety().getCashFlow());
		}
	}

}
