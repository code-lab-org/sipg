package edu.mit.sips.gui;

import java.awt.Color;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.LocalAgricultureSoS;
import edu.mit.sips.core.petroleum.LocalPetroleumSoS;
import edu.mit.sips.core.water.LocalWaterSoS;
import edu.mit.sips.io.Icons;

public class ScorePanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = 355808870154994451L;

	private final Country country;

	DefaultTableXYDataset agriculturePlayerScore = new DefaultTableXYDataset();
	DefaultTableXYDataset agriculturePlayerTotalScore = new DefaultTableXYDataset();
	
	DefaultTableXYDataset foodSecurityScore = new DefaultTableXYDataset();
	DefaultTableXYDataset foodSecurityTotalScore = new DefaultTableXYDataset();
	
	DefaultTableXYDataset aquiferLifetimeScore = new DefaultTableXYDataset();
	DefaultTableXYDataset aquiferLifetimeTotalScore = new DefaultTableXYDataset();
	
	DefaultTableXYDataset reservoirLifetimeScore = new DefaultTableXYDataset();
	DefaultTableXYDataset reservoirLifetimeTotalScore = new DefaultTableXYDataset();
	
	
	public ScorePanel(Country country) {
		super(country.getSocialSystem());
		this.country = country;
		
		if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {
			addTab("Score", Icons.AGRICULTURE, createStackedAreaChart(
					"Score (-)", agriculturePlayerScore, new Color[]{}, 
					agriculturePlayerTotalScore));
			addTab("Security", Icons.AGRICULTURE, createStackedAreaChart(
					"Food Security (-)", foodSecurityScore, 
					new Color[]{PlottingUtils.getSystemColor(country.getAgricultureSystem())}, 
					foodSecurityTotalScore));
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
		agriculturePlayerTotalScore.removeAllSeries();
		
		foodSecurityScore.removeAllSeries();
		foodSecurityTotalScore.removeAllSeries();
		
		aquiferLifetimeScore.removeAllSeries();
		aquiferLifetimeTotalScore.removeAllSeries();
		
		reservoirLifetimeScore.removeAllSeries();
		reservoirLifetimeTotalScore.removeAllSeries();
	}
	
	private double getFoodSecurityScore(double foodSecurity) {
		return Math.max(Math.min(foodSecurity, 1), 0);
		// return 0.5 + Math.atan(10*(foodSecurity - 0.3))/Math.PI;
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
	
	private double agricultureInvestmentGrowthRate = 0.03;
	private double agricultureInvestmentDistopia = 0;
	private double agricultureInvestmentUtopia = 10e9; // upper bound on total investment in 2010
	private double agricultureProfitGrowthRate = 0.03;
	private double agricultureProfitDistopia = 0;
	private double agricultureProfitUtopia = 50e9; // upper bound on total profit in 2010
	
	private double getTotalScoreAtYear(int year, 
			double value, double distopiaTotal, double utopiaTotal, double growthRate) {
		double growthFactor = 70*Math.log(growthRate);
		double minValue = distopiaTotal * (Math.exp(growthFactor*(year-1940)/70) - 1)/(Math.exp(growthFactor*1)-1); // 0 + Math.exp((year-1950)/60)*distopiaTotal;
		double maxValue = utopiaTotal * (Math.exp(growthFactor*(year-1940)/70) - 1)/(Math.exp(growthFactor*1)-1); // (year-1950)*(utopiaTotal-distopiaTotal)/60;
		if(value < minValue) {
			return 0;
		} else if(value > maxValue) {
			return 1;
		} else {
			return (value - minValue)/(maxValue - minValue);
		}
	}
	
	private static double getInvestmentDistopia(City city) {
		switch(city.getName()) {
		case "Industrial":
			return 0;
		case "Rural":
			return 0;
		case "Urban":
			return 0;
		}
		return 0;
	}
	
	private static double getInvestmentUtopia(City city) {
		switch(city.getName()) {
		case "Industrial":
			return 40e9;
		case "Rural":
			return 10e9;
		case "Urban":
			return 20e9;
		}
		return 0;
	}
	
	private static double getInvestmentGrowthRate(City city) {
		switch(city.getName()) {
		case "Industrial":
			return 0.05;
		case "Rural":
			return 0.03;
		case "Urban":
			return 0.04;
		}
		return 0.03;
	}

	@Override
	public void update(int year) {
		if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {
			updateSeries(foodSecurityScore, "Annual Score", year, 
					getFoodSecurityScore(((LocalAgricultureSoS) 
							country.getAgricultureSystem()).getFoodSecurity()));
			updateSeries(foodSecurityTotalScore, "Total Score", year, 
					getTotalScore(foodSecurityScore));
			
			double security = getTotalScore(foodSecurityScore);
			double sectorInvest = getTotalScoreAtYear(year, ((LocalAgricultureSoS) 
					country.getAgricultureSystem()).getCumulativeCapitalExpense(),
					agricultureInvestmentDistopia, agricultureInvestmentUtopia, 
					agricultureInvestmentGrowthRate);
			double sectorProfit = getTotalScoreAtYear(year, ((LocalAgricultureSoS) 
					country.getAgricultureSystem()).getCumulativeCashFlow(),
					agricultureProfitDistopia, agricultureProfitUtopia, 
					agricultureProfitGrowthRate);
			City city = country.getCity("Rural");
			double regionInvest = getTotalScoreAtYear(year,
					city.getCumulativeCapitalExpense(), getInvestmentDistopia(city),
					getInvestmentUtopia(city), getInvestmentGrowthRate(city));

			updateSeries(agriculturePlayerScore, "Food Security", year, security);
			updateSeries(agriculturePlayerScore, "Agricultural Investment", 
					year, sectorInvest);
			updateSeries(agriculturePlayerScore, "Agricultural Profit", 
					year, sectorProfit);
			updateSeries(agriculturePlayerScore, city.getName() + " Investment", 
					year, regionInvest);
			
			updateSeries(agriculturePlayerTotalScore, "Total Score", year, 
					0.3*security + 0.3*sectorProfit + 0.3*sectorInvest + 0.1*regionInvest);
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
