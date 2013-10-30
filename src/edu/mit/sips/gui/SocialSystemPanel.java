package edu.mit.sips.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.social.SocialSoS;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;

/**
 * The Class SocialSystemPanel.
 */
public class SocialSystemPanel extends InfrastructureSystemPanel implements CurrencyUnitsOutput {
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

	/**
	 * Instantiates a new social system panel.
	 *
	 * @param socialSystem the social system
	 */
	public SocialSystemPanel(SocialSystem socialSystem) {
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
					"Funds (" + getCurrencyUnitsNumerator() + ")", fundsData));
		}

		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Revenue (" + getCurrencyUnitsNumerator() 
				+ "/" + getCurrencyUnitsDenominator() + ")", 
				infrastructureSystemRevenue, null, infrastructureSystemNetRevenue));
		if(getSocialSystem() instanceof SocialSoS) {
			addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
					"Revenue (" + getCurrencyUnitsNumerator() 
					+ "/" + getCurrencyUnitsDenominator() + ")", 
					societyRevenue, null, societyNetRevenue));
		}
		addTab("Domestic Product", Icons.PRODUCT, createStackedAreaChart(
				"Domestic Product (" + getCurrencyUnitsNumerator() 
				+ "/" + getCurrencyUnitsDenominator() + ")", domesticProduct, 
				"Domestic Product per Capita (" + CurrencyUnits.NumeratorUnits.sim 
				+ "/" + CurrencyUnits.DenominatorUnits.year + ")", domesticProductPerCapita));
		addTab("Population", Icons.POPULATION, createStackedAreaChart(
				"Population", populationDataset));

	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsDenominator()
	 */
	@Override
	public CurrencyUnits.DenominatorUnits getCurrencyUnitsDenominator() {
		return CurrencyUnits.DenominatorUnits.year;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsNumerator()
	 */
	@Override
	public CurrencyUnits.NumeratorUnits getCurrencyUnitsNumerator() {
		return CurrencyUnits.NumeratorUnits.Tsim;
	}

	/**
	 * Gets the social system.
	 *
	 * @return the social system
	 */
	public SocialSystem getSocialSystem() {
		return (SocialSystem) getInfrastructureSystem(); 
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#initialize()
	 */
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
		// nothing to do here
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		// nothing to do here
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#update(int)
	 */
	@Override
	public void update(int year) {
		if(getSociety() instanceof Country) {
			updateSeriesCollection(fundsData, getSociety().getName(), year, 
					CurrencyUnits.convert(((Country)getSociety()).getFunds(), 
							getSociety(), this));
		}
		updateSeries(infrastructureSystemNetRevenue, "Net Revenue", year, 
				CurrencyUnits.convert(getSociety().getCashFlow(), 
						getSociety(), this));

		
		for(InfrastructureSystem nestedSociety : getSociety().getInfrastructureSystems()) {
			updateSeries(infrastructureSystemRevenue, nestedSociety.getName(), year, 
					CurrencyUnits.convert(nestedSociety.getCashFlow(), 
							nestedSociety, this));
		}
		
		if(getSocialSystem().getPopulation() > 0) {
			updateSeries(domesticProductPerCapita, getSociety().getName() + " (per capita)", 
					year, CurrencyUnits.convert(getSocialSystem().getDomesticProduct(), 
							getSociety().getCurrencyUnitsNumerator(), 
							getSociety().getCurrencyUnitsDenominator(), 
							CurrencyUnits.NumeratorUnits.sim,
							CurrencyUnits.DenominatorUnits.year) 
					/ getSocialSystem().getPopulation());
			domesticProductIndicatorPanel.setValue(
					CurrencyUnits.convert(getSocialSystem().getDomesticProduct(), 
							getSociety().getCurrencyUnitsNumerator(), 
							getSociety().getCurrencyUnitsDenominator(), 
							CurrencyUnits.NumeratorUnits.sim,
							CurrencyUnits.DenominatorUnits.year) 
					/ getSocialSystem().getPopulation());
		}
		
		if(getSocialSystem() instanceof SocialSoS) {
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(populationDataset, nestedSociety.getName(), year, 
						nestedSociety.getSocialSystem().getPopulation());
			}

			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(domesticProduct, nestedSociety.getName(), year, 
						CurrencyUnits.convert(nestedSociety.getSocialSystem().getDomesticProduct(), 
								nestedSociety, this));
			}

			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(societyRevenue, nestedSociety.getName(), year, 
						CurrencyUnits.convert(nestedSociety.getCashFlow(), 
								nestedSociety, this));
			}
			updateSeries(societyNetRevenue, "Net Revenue", year, 
					CurrencyUnits.convert(getSociety().getCashFlow(), 
							getSociety(), this));
		} else {
			updateSeries(populationDataset, getSociety().getName(), year, 
					getSocialSystem().getPopulation());
			
			updateSeries(domesticProduct, getSociety().getName(), year, 
					CurrencyUnits.convert(getSocialSystem().getDomesticProduct(), 
							getSocialSystem(), this));
		}
	}
}
