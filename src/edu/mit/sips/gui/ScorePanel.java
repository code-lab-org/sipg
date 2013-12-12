package edu.mit.sips.gui;

import java.awt.Color;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.LocalAgricultureSoS;
import edu.mit.sips.core.petroleum.LocalPetroleumSoS;
import edu.mit.sips.core.water.LocalWaterSoS;
import edu.mit.sips.io.Icons;

public class ScorePanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = 355808870154994451L;

	private final Country country;
	
	
	DefaultTableXYDataset foodSecurity = new DefaultTableXYDataset();
	DefaultTableXYDataset foodSecurityScore = new DefaultTableXYDataset();
	
	DefaultTableXYDataset aquiferLifetime = new DefaultTableXYDataset();
	DefaultTableXYDataset aquiferLifetimeScore = new DefaultTableXYDataset();
	
	DefaultTableXYDataset reservoirLifetime = new DefaultTableXYDataset();
	DefaultTableXYDataset reservoirLifetimeScore = new DefaultTableXYDataset();
	
	public ScorePanel(Country country) {
		super(country.getSocialSystem());
		this.country = country;
		
		if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {
			addTab("Food Security", Icons.AGRICULTURE, createStackedAreaChart(
					"Food Security (-)", foodSecurity, 
					new Color[]{PlottingUtils.getSystemColor(country.getAgricultureSystem())}, 
					foodSecurityScore));
		}
		if(country.getWaterSystem() instanceof LocalWaterSoS) {
			addTab("Aquifer Security", Icons.WATER, createStackedAreaChart(
					"Aquifer Security (-)", aquiferLifetime, 
					new Color[]{PlottingUtils.getSystemColor(country.getWaterSystem())},
					aquiferLifetimeScore));
		}
		if(country.getPetroleumSystem() instanceof LocalPetroleumSoS) {
			addTab("Reservoir Security", Icons.PETROLEUM, createStackedAreaChart(
					"Reservoir Security (-)", reservoirLifetime, 
					new Color[]{PlottingUtils.getSystemColor(country.getPetroleumSystem())},
					reservoirLifetimeScore));
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
			updateSeries(foodSecurity, "Annual Score", year, 
					((LocalAgricultureSoS) country.getAgricultureSystem())
					.getFoodSecurityScore());
			updateSeries(foodSecurityScore, "Cumulative Score", year, 
					((LocalAgricultureSoS) country.getAgricultureSystem())
					.getCumulativeFoodSecurityScore());
		}
		if(country.getWaterSystem() instanceof LocalWaterSoS) {
			updateSeries(aquiferLifetime, "Annual Score", year, 
					((LocalWaterSoS) country.getWaterSystem())
					.getAquiferSecurityScore());
			updateSeries(aquiferLifetimeScore, "Cumulative Score", year, 
					((LocalWaterSoS) country.getWaterSystem())
					.getCumulativeAquiferSecurityScore());
		}
		if(country.getPetroleumSystem() instanceof LocalPetroleumSoS) {
			updateSeries(reservoirLifetime, "Annual Score", year, 
					((LocalPetroleumSoS) country.getPetroleumSystem())
					.getReservoirSecurityScore());
			updateSeries(reservoirLifetimeScore, "Cumulative Score", year, 
					((LocalPetroleumSoS) country.getPetroleumSystem())
					.getCumulativeReservoirSecurityScore());
		}
	}
}
