package edu.mit.sips.gui.social;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.social.SocialSoS;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.gui.InfrastructureSystemPanel;
import edu.mit.sips.gui.LinearIndicatorPanel;
import edu.mit.sips.gui.PlottingUtils;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class SocialSystemPanel.
 */
public class SocialSystemPanel extends InfrastructureSystemPanel implements CurrencyUnitsOutput {
	private static final long serialVersionUID = -8472419089458128152L;
	
	private static final CurrencyUnits currencyUnits = CurrencyUnits.Bsim;
	private static final TimeUnits currencyTimeUnits = TimeUnits.year;
	
	private final LinearIndicatorPanel domesticProductIndicatorPanel;

	DefaultTableXYDataset fundsData = new DefaultTableXYDataset();
	DefaultTableXYDataset infrastructureSystemRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset infrastructureSystemNetRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset populationDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset populationTotalDataset = new DefaultTableXYDataset();
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

		/* temporarily removed
		 * addTab("Indicators", Icons.INDICATORS, indicatorsPanel);
		 */

		if(getSociety() instanceof Country) {
			addTab("Finances", Icons.FUNDS, createStackedAreaChart(
					"Cash Flow (" + getCurrencyUnits() + ")", infrastructureSystemRevenue, 
					PlottingUtils.getSystemColors(getSociety().getInfrastructureSystems()), 
					infrastructureSystemNetRevenue,
					"Cash Balance (" + getCurrencyUnits() + ")", fundsData));
			addTab("Population", Icons.POPULATION, createStackedAreaChart(
					"Population", populationDataset, 
					PlottingUtils.getSocietyColors(getSociety().getNestedSocieties()),
					populationTotalDataset));
		} else {
			addTab("Finances", Icons.FUNDS, createStackedAreaChart(
					"Cash Flow (" + getCurrencyUnits() + ")", infrastructureSystemRevenue, 
					PlottingUtils.getSystemColors(getSociety().getInfrastructureSystems()), 
					infrastructureSystemNetRevenue, null, null));
			addTab("Population", Icons.POPULATION, createSingleLineChart(
					"Population", populationTotalDataset));
		}

		/* temporarily removed
		addTab("System Cash Flow", Icons.REVENUE, createStackedAreaChart(
				"Cash Flow (" + getCurrencyUnits() 
				+ "/" + getCurrencyTimeUnits() + ")", 
				infrastructureSystemRevenue, null, infrastructureSystemNetRevenue));
		if(getSocialSystem() instanceof SocialSoS) {
			addTab("Regional Cash Flow", Icons.REVENUE, createStackedAreaChart(
					"Cash Flow (" + getCurrencyUnits() 
					+ "/" + getCurrencyTimeUnits() + ")", 
					societyRevenue, null, societyNetRevenue));
		}
		*/
		
		/* temporarily removed
		 * addTab("Domestic Product", Icons.PRODUCT, createStackedAreaChart(
				"Domestic Product (" + getCurrencyUnits() 
				+ "/" + getCurrencyTimeUnits() + ")", domesticProduct, 
				"Domestic Product per Capita (" + CurrencyUnits.sim 
				+ "/" + TimeUnits.year + ")", domesticProductPerCapita));
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
		populationTotalDataset.removeAllSeries();
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
			updateSeries(fundsData, getSociety().getName() + " Balance", year, 
					CurrencyUnits.convertStock(((Country)getSociety()).getFunds(), 
							getSociety(), this));
		}
		updateSeries(infrastructureSystemNetRevenue, "Net " + getSociety().getName(), year, 
				CurrencyUnits.convertFlow(getSociety().getCashFlow(), 
						getSociety(), this));

		
		for(InfrastructureSystem nestedSociety : getSociety().getInfrastructureSystems()) {
			updateSeries(infrastructureSystemRevenue, nestedSociety.getName(), year, 
					CurrencyUnits.convertFlow(nestedSociety.getCashFlow(), 
							nestedSociety, this));
		}
		
		if(getSocialSystem().getPopulation() > 0) {
			updateSeries(domesticProductPerCapita, getSociety().getName() + " (per capita)", 
					year, DefaultUnits.convertFlow(getSocialSystem().getDomesticProduct(), 
							getSociety().getCurrencyUnits(), 
							getSociety().getCurrencyTimeUnits(), 
							CurrencyUnits.sim,
							TimeUnits.year) 
					/ getSocialSystem().getPopulation());
			domesticProductIndicatorPanel.setValue(
					DefaultUnits.convertFlow(getSocialSystem().getDomesticProduct(), 
							getSociety().getCurrencyUnits(), 
							getSociety().getCurrencyTimeUnits(), 
							CurrencyUnits.sim,
							TimeUnits.year) 
					/ getSocialSystem().getPopulation());
		}
		
		if(getSocialSystem() instanceof SocialSoS) {
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(domesticProduct, nestedSociety.getName(), year, 
						CurrencyUnits.convertFlow(nestedSociety.getSocialSystem().getDomesticProduct(), 
								nestedSociety, this));
			}

			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(societyRevenue, nestedSociety.getName(), year, 
						CurrencyUnits.convertFlow(nestedSociety.getCashFlow(), 
								nestedSociety, this));
			}
			updateSeries(societyNetRevenue, "Net Revenue", year, 
					CurrencyUnits.convertFlow(getSociety().getCashFlow(), 
							getSociety(), this));
		} else {
			updateSeries(domesticProduct, getSociety().getName(), year, 
					CurrencyUnits.convertFlow(getSocialSystem().getDomesticProduct(), 
							getSocialSystem(), this));
		}

		for(Society nestedSociety : getSociety().getNestedSocieties()) {
			updateSeries(populationDataset, nestedSociety.getName(), year, 
					nestedSociety.getSocialSystem().getPopulation());
		}
		updateSeries(populationTotalDataset, getSociety().getName(), year, 
				getSocialSystem().getPopulation());
	}
}
