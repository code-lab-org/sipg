package edu.mit.sips.gui;

import java.awt.Color;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.LocalAgricultureSoS;
import edu.mit.sips.io.Icons;

public class ScorePanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = 355808870154994451L;

	private final Country country;
	
	DefaultTableXYDataset foodSecurity = new DefaultTableXYDataset();
	DefaultTableXYDataset foodSecurityScore = new DefaultTableXYDataset();
	
	public ScorePanel(Country country) {
		super(country.getSocialSystem());
		this.country = country;
		
		if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {
			addTab("Food Security", Icons.AGRICULTURE, createStackedAreaChart(
					"Food Security (-)", foodSecurity, 
					new Color[]{PlottingUtils.getSystemColor(country.getAgricultureSystem())}, 
					"Cumulative Score (-)", foodSecurityScore));
		}
	}

	@Override
	public void simulationCompleted(UpdateEvent event) { }

	@Override
	public void simulationInitialized(UpdateEvent event) { }

	@Override
	public void simulationUpdated(UpdateEvent event) { }

	@Override
	public void initialize() {
		foodSecurity.removeAllSeries();
		foodSecurityScore.removeAllSeries();
	}

	@Override
	public void update(int year) {
		if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {
			updateSeries(foodSecurity, "Food Security", year, 
					((LocalAgricultureSoS) country.getAgricultureSystem())
					.getFoodSecurity());
			updateSeries(foodSecurityScore, "Cumulative Score", year, 
					((LocalAgricultureSoS) country.getAgricultureSystem())
					.getCumulativeFoodSecurity());
		}
	}
}
