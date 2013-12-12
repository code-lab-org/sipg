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

	DefaultTableXYDataset agriculturePlayerScore = new DefaultTableXYDataset();
	
	DefaultTableXYDataset foodSecurityScore = new DefaultTableXYDataset();
	DefaultTableXYDataset foodSecurityTotalScore = new DefaultTableXYDataset();
	DefaultTableXYDataset agricultureInvestmentTotalScore = new DefaultTableXYDataset();
	DefaultTableXYDataset agricultureCashFlowScore = new DefaultTableXYDataset();
	DefaultTableXYDataset agricultureCashFlowTotalScore = new DefaultTableXYDataset();
	
	DefaultTableXYDataset aquiferLifetimeScore = new DefaultTableXYDataset();
	DefaultTableXYDataset aquiferLifetimeTotalScore = new DefaultTableXYDataset();
	
	DefaultTableXYDataset reservoirLifetimeScore = new DefaultTableXYDataset();
	DefaultTableXYDataset reservoirLifetimeTotalScore = new DefaultTableXYDataset();
	
	
	public ScorePanel(Country country) {
		super(country.getSocialSystem());
		this.country = country;
		
		if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {
			addTab("Score", Icons.AGRICULTURE, createStackedAreaChart(
					"Score (-)", agriculturePlayerScore, 
					new Color[]{PlottingUtils.CORAL, PlottingUtils.INDIAN_RED, 
							PlottingUtils.GREEN_YELLOW, PlottingUtils.BLACK}));
			addTab("Security", Icons.AGRICULTURE, createStackedAreaChart(
					"Food Security (-)", foodSecurityScore, 
					new Color[]{PlottingUtils.getSystemColor(country.getAgricultureSystem())}, 
					foodSecurityTotalScore));
			addTab("Investment", Icons.AGRICULTURE, createSingleLineChart(
					"Investment (-)", agricultureInvestmentTotalScore));
			addTab("Cash Flow", Icons.AGRICULTURE, createStackedAreaChart(
					"Cash Flow (-)", agricultureCashFlowScore,
					new Color[]{PlottingUtils.getSystemColor(country.getAgricultureSystem())}, 
					agricultureCashFlowTotalScore));
		}
		if(country.getWaterSystem() instanceof LocalWaterSoS) {
			addTab("Security", Icons.WATER, createStackedAreaChart(
					"Aquifer Security (-)", aquiferLifetimeScore, 
					new Color[]{PlottingUtils.getSystemColor(country.getWaterSystem())},
					aquiferLifetimeTotalScore));
		}
		if(country.getPetroleumSystem() instanceof LocalPetroleumSoS) {
			addTab("Security", Icons.PETROLEUM, createStackedAreaChart(
					"Reservoir Security (-)", reservoirLifetimeScore, 
					new Color[]{PlottingUtils.getSystemColor(country.getPetroleumSystem())},
					reservoirLifetimeTotalScore));
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
		agriculturePlayerScore.removeAllSeries();
		
		foodSecurityScore.removeAllSeries();
		foodSecurityTotalScore.removeAllSeries();
		agricultureInvestmentTotalScore.removeAllSeries();
		agricultureCashFlowScore.removeAllSeries();
		agricultureCashFlowTotalScore.removeAllSeries();
		
		aquiferLifetimeScore.removeAllSeries();
		aquiferLifetimeTotalScore.removeAllSeries();
		
		reservoirLifetimeScore.removeAllSeries();
		reservoirLifetimeTotalScore.removeAllSeries();
	}
	
	private double getFoodSecurityScore(double foodSecurity) {
		return Math.max(Math.min(foodSecurity, 1), 0);
	}
	
	private double getAquiferSecurityScore(double aquiferLifetime) {
		double minLifetime = 20;
		double maxLifetime = 200;
		if(aquiferLifetime < minLifetime) {
			return 0;
		} else if(aquiferLifetime > maxLifetime) {
			return 1;
		} else {
			return (aquiferLifetime - minLifetime)/(maxLifetime - minLifetime);
		}
	}
	
	private static double getTotalScore(DefaultTableXYDataset dataset) {
		double value = 0;
		for(int i = 0; i < dataset.getSeries(0).getItemCount(); i++) {
			value += dataset.getSeries(0).getY(i).doubleValue();
		}
		return value / dataset.getSeries(0).getItemCount();
	}
	
	private double getReservoirSecurityScore(double reservoirLifetime) {
		double minLifetime = 0;
		double maxLifetime = 200;
		if(reservoirLifetime < minLifetime) {
			return 0;
		} else if(reservoirLifetime > maxLifetime) {
			return 1;
		} else {
			return (reservoirLifetime - minLifetime)/(maxLifetime - minLifetime);
		}
	}
	
	private double getAgricultureInvestmentScore(int year, double capitalExpense) {
		double minExpense = 0 + (year-1950)*1e9/60; 	// distopia: 1 billion by 2010
		double maxExpense = 1e9 + (year-1950)*9e9/60; 	// utopia: 9 billion by 2010
		if(capitalExpense < minExpense) {
			return 0;
		} else if(capitalExpense > maxExpense) {
			return 1;
		} else {
			return (capitalExpense - minExpense)/(maxExpense - minExpense);
		}
	}
	
	private double getAgricultureProfitScore(int year, double cashFlow) {
		double minCashFlow = 0 + (year-1950)*0/60; 	// distopia: no profit by 2010
		double maxCashFlow = 0 + (year-1950)*50e9/60; 	// utopia: 50 billion profit by 2010
		if(cashFlow < minCashFlow) {
			return 0;
		} else if(cashFlow > maxCashFlow) {
			return 1;
		} else {
			return (cashFlow - minCashFlow)/(maxCashFlow - minCashFlow);
		}
	}

	@Override
	public void update(int year) {
		if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {
			updateSeries(foodSecurityScore, "Annual Score", year, 
					getFoodSecurityScore(((LocalAgricultureSoS) 
							country.getAgricultureSystem()).getFoodSecurity()));
			updateSeries(foodSecurityTotalScore, "Total Score", year, 
					getTotalScore(foodSecurityScore));
			updateSeries(agricultureInvestmentTotalScore, "Investment Score", year, 
					getAgricultureInvestmentScore(year, ((LocalAgricultureSoS) 
							country.getAgricultureSystem()).getCumulativeCapitalExpense()));
			updateSeries(agricultureCashFlowTotalScore, "Cash Flow Score", year, 
					getAgricultureProfitScore(year, ((LocalAgricultureSoS) 
							country.getAgricultureSystem()).getCumulativeCashFlow()));

			updateSeries(agriculturePlayerScore, "Security", year, 
					getTotalScore(foodSecurityScore));
			updateSeries(agriculturePlayerScore, "Investment", year, 
					getAgricultureInvestmentScore(year, ((LocalAgricultureSoS) 
							country.getAgricultureSystem()).getCumulativeCapitalExpense()));
			updateSeries(agriculturePlayerScore, "Cash Flow", year, 
					getAgricultureProfitScore(year, ((LocalAgricultureSoS) 
							country.getAgricultureSystem()).getCumulativeCashFlow()));
		}
		if(country.getWaterSystem() instanceof LocalWaterSoS) {
			updateSeries(aquiferLifetimeScore, "Annual Score", year, 
					getAquiferSecurityScore(((LocalWaterSoS) 
							country.getWaterSystem()).getAquiferLifetime()));
			updateSeries(aquiferLifetimeTotalScore, "Total Score", year, 
					getTotalScore(aquiferLifetimeScore));
		}
		if(country.getPetroleumSystem() instanceof LocalPetroleumSoS) {
			updateSeries(reservoirLifetimeScore, "Annual Score", year, 
					getReservoirSecurityScore(((LocalPetroleumSoS) 
							country.getPetroleumSystem()).getReservoirLifetime()));
			updateSeries(reservoirLifetimeTotalScore, "Total Score", year, 
					getTotalScore(reservoirLifetimeScore));
		}
	}
}
