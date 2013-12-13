package edu.mit.sips.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.LocalAgricultureSoS;
import edu.mit.sips.core.electricity.LocalElectricitySoS;
import edu.mit.sips.core.petroleum.LocalPetroleumSoS;
import edu.mit.sips.core.water.LocalWaterSoS;
import edu.mit.sips.io.Icons;

public class ScorePanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = 355808870154994451L;

	private final Country country;
	
	List<Double> foodSecurityHistory = new ArrayList<Double>();
	List<Double> aquiferSecurityHistory = new ArrayList<Double>();
	List<Double> reservoirSecurityHistory = new ArrayList<Double>();

	private final JLabel agricultureScoreLabel = new JLabel("");
	DefaultTableXYDataset agriculturePlayerScore = new DefaultTableXYDataset();

	private final JLabel waterScoreLabel = new JLabel("");
	DefaultTableXYDataset waterPlayerScore = new DefaultTableXYDataset();

	private final JLabel energyScoreLabel = new JLabel("");
	DefaultTableXYDataset energyPlayerScore = new DefaultTableXYDataset();

	private final JLabel teamScoreLabel = new JLabel("");
	DefaultTableXYDataset teamScore = new DefaultTableXYDataset();
	
	public ScorePanel(Country country) {
		super(country.getSocialSystem());
		this.country = country;
		
		if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {
			JPanel scorePanel = createStackedAreaChart("Score (-)", null,
					new Color[]{PlottingUtils.YELLOW_GREEN, PlottingUtils.TOMATO, 
					PlottingUtils.GOLDENROD, PlottingUtils.PLUM, 
					PlottingUtils.BLACK}, agriculturePlayerScore);
			agricultureScoreLabel.setFont(getFont().deriveFont(20f));
			agricultureScoreLabel.setHorizontalAlignment(JLabel.CENTER);
			scorePanel.add(agricultureScoreLabel, BorderLayout.NORTH);
			addTab("Individual Score", Icons.AGRICULTURE, scorePanel);
		}
		if(country.getWaterSystem() instanceof LocalWaterSoS) {
			JPanel scorePanel = createStackedAreaChart("Score (-)", null,
					new Color[]{PlottingUtils.DODGER_BLUE, PlottingUtils.TOMATO, 
					PlottingUtils.GOLDENROD, PlottingUtils.PLUM, 
					PlottingUtils.BLACK}, waterPlayerScore);
			waterScoreLabel.setFont(getFont().deriveFont(20f));
			waterScoreLabel.setHorizontalAlignment(JLabel.CENTER);
			scorePanel.add(waterScoreLabel, BorderLayout.NORTH);
			addTab("Individual Score", Icons.WATER, scorePanel);
		}
		if(country.getPetroleumSystem() instanceof LocalPetroleumSoS
				&& country.getElectricitySystem() instanceof LocalElectricitySoS) {
			JPanel scorePanel = createStackedAreaChart("Score (-)", null,
					new Color[]{PlottingUtils.DIM_GRAY, PlottingUtils.TOMATO, 
					PlottingUtils.GOLDENROD, PlottingUtils.PLUM, 
					PlottingUtils.BLACK}, energyPlayerScore);
			energyScoreLabel.setFont(getFont().deriveFont(20f));
			energyScoreLabel.setHorizontalAlignment(JLabel.CENTER);
			scorePanel.add(energyScoreLabel, BorderLayout.NORTH);
			addTab("Individual Score", Icons.ENERGY, scorePanel);
		}
		
		JPanel scorePanel = createStackedAreaChart("Score (-)", null,
				new Color[]{PlottingUtils.YELLOW_GREEN, PlottingUtils.DODGER_BLUE, 
				PlottingUtils.DIM_GRAY, PlottingUtils.GOLDENROD, 
				PlottingUtils.BLACK}, teamScore);
		teamScoreLabel.setFont(getFont().deriveFont(20f));
		teamScoreLabel.setHorizontalAlignment(JLabel.CENTER);
		scorePanel.add(teamScoreLabel, BorderLayout.NORTH);
		addTab("Team Score", Icons.COUNTRY, scorePanel);
	}

	@Override
	public void simulationCompleted(UpdateEvent event) { }

	@Override
	public void simulationInitialized(UpdateEvent event) { }

	@Override
	public void simulationUpdated(UpdateEvent event) { }

	@Override
	public void initialize() {
		agricultureScoreLabel.setText("");
		agriculturePlayerScore.removeAllSeries();
		foodSecurityHistory.clear();
		waterScoreLabel.setText("");
		waterPlayerScore.removeAllSeries();
		aquiferSecurityHistory.clear();
		energyScoreLabel.setText("");
		energyPlayerScore.removeAllSeries();
		reservoirSecurityHistory.clear();
	}
	
	private double getFoodSecurityScore(double foodSecurity) {
		return 1000 * Math.max(Math.min(foodSecurity, 1), 0);
		// return 0.5 + Math.atan(10*(foodSecurity - 0.3))/Math.PI;
	}
	
	private double getAquiferSecurityScore(double aquiferLifetime) {
		double minLifetime = 20;
		double maxLifetime = 200;
		if(aquiferLifetime < minLifetime) {
			return 0;
		} else if(aquiferLifetime > maxLifetime) {
			return 1000;
		} else {
			return 1000 * (aquiferLifetime - minLifetime)/(maxLifetime - minLifetime);
		}
	}
	
	private static double getTotalScore(List<Double> dataset) {
		double value = 0;
		for(double item : dataset) {
			value += item;
		}
		return value / dataset.size();
	}
	
	private double getReservoirSecurityScore(double reservoirLifetime) {
		double minLifetime = 0;
		double maxLifetime = 200;
		if(reservoirLifetime < minLifetime) {
			return 0;
		} else if(reservoirLifetime > maxLifetime) {
			return 1000;
		} else {
			return 1000*(reservoirLifetime - minLifetime)/(maxLifetime - minLifetime);
		}
	}
	
	private double getTotalScoreAtYear(int year, 
			double value, double distopiaTotal, double utopiaTotal, double growthRate) {
		double growthFactor = 70*Math.log(growthRate);
		double minValue = distopiaTotal * (Math.exp(growthFactor*(year-1940)/70) - 1)/(Math.exp(growthFactor*1)-1); // 0 + Math.exp((year-1950)/60)*distopiaTotal;
		double maxValue = utopiaTotal * (Math.exp(growthFactor*(year-1940)/70) - 1)/(Math.exp(growthFactor*1)-1); // (year-1950)*(utopiaTotal-distopiaTotal)/60;
		if(value < minValue) {
			return 0;
		} else if(value > maxValue) {
			return 1000;
		} else {
			return 1000*(value - minValue)/(maxValue - minValue);
		}
	}
	
	private static double getProfitGrowthRate(String name) {
		switch(name) {
		case "Agriculture":
			return 0.03;
		case "Water":
			return 0.03;
		case "Energy":
			return 0.08;
		case "Kingdom":
			return 0.08;
		}
		return 0;
	}
	
	private static double getProfitDistopia(String name) {
		switch(name) {
		case "Agriculture":
			return 0;
		case "Water":
			return -5e9;
		case "Energy":
			return 0e9;
		case "Kingdom":
			return 0e9;
		}
		return 0;
	}
	
	private static double getProfitUtopia(String name) {
		switch(name) {
		case "Agriculture":
			return 50e9;
		case "Water":
			return 5e9;
		case "Energy":
			return 500e9;
		case "Kingdom":
			return 800e9;
		}
		return 0;
	}
	
	private static double getInvestmentDistopia(String name) {
		switch(name) {
		case "Industrial":
			return 0;
		case "Rural":
			return 0;
		case "Urban":
			return 0;
		case "Agriculture":
			return 0;
		case "Water":
			return 0;
		case "Energy":
			return 0;
		}
		return 0;
	}
	
	private static double getInvestmentUtopia(String name) {
		switch(name) {
		case "Industrial":
			return 40e9;
		case "Rural":
			return 10e9;
		case "Urban":
			return 20e9;
		case "Agriculture":
			return 10e9;
		case "Water":
			return 10e9;
		case "Energy":
			return 50e9;
		}
		return 0;
	}
	
	private static double getInvestmentGrowthRate(String name) {
		switch(name) {
		case "Industrial":
			return 0.05;
		case "Rural":
			return 0.03;
		case "Urban":
			return 0.04;
		case "Agriculture":
			return 0.03;
		case "Water":
			return 0.03;
		case "Energy":
			return 0.05;
		}
		return 0.03;
	}

	@Override
	public void update(int year) {
		if(year < 1980) {
			return;
		}
		double foodSecurity = 0;
		double aquiferSecurity = 0;
		double reservoirSecurity = 0;
		
		if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {
			foodSecurityHistory.add(getFoodSecurityScore(((LocalAgricultureSoS) 
					country.getAgricultureSystem()).getFoodSecurity()));
			
			foodSecurity = getTotalScore(foodSecurityHistory);
			double sectorInvest = getTotalScoreAtYear(year, ((LocalAgricultureSoS) 
					country.getAgricultureSystem()).getCumulativeCapitalExpense(),
					getInvestmentDistopia("Agriculture"), getInvestmentUtopia("Agriculture"), 
					getInvestmentGrowthRate("Agriculture"));
			double sectorProfit = getTotalScoreAtYear(year, ((LocalAgricultureSoS) 
					country.getAgricultureSystem()).getCumulativeCashFlow(),
					getProfitDistopia("Agriculture"), getProfitUtopia("Agriculture"), 
					getProfitGrowthRate("Agriculture"));
			City city = country.getCity("Rural");
			double regionInvest = getTotalScoreAtYear(year,
					city.getCumulativeCapitalExpense(), getInvestmentDistopia(city.getName()),
					getInvestmentUtopia(city.getName()), getInvestmentGrowthRate(city.getName()));

			updateSeries(agriculturePlayerScore, "Food Security", 
					year, foodSecurity);
			updateSeries(agriculturePlayerScore, "Agricultural Investment", 
					year, sectorInvest);
			updateSeries(agriculturePlayerScore, "Agricultural Profit", 
					year, sectorProfit);
			updateSeries(agriculturePlayerScore, city.getName() + " Investment", 
					year, regionInvest);
			
			double totalScore = 0.3*foodSecurity + 0.3*sectorProfit 
					+ 0.3*sectorInvest + 0.1*regionInvest;
			
			updateSeries(agriculturePlayerScore, "Total Score", 
					year, totalScore);
			agricultureScoreLabel.setText("Individual Score: " 
					+ NumberFormat.getIntegerInstance().format(totalScore));
		}
		if(country.getWaterSystem() instanceof LocalWaterSoS) {
			aquiferSecurityHistory.add(getAquiferSecurityScore(((LocalWaterSoS) 
					country.getWaterSystem()).getAquiferLifetime()));
			
			aquiferSecurity = getTotalScore(aquiferSecurityHistory);
			double sectorInvest = getTotalScoreAtYear(year, ((LocalWaterSoS) 
					country.getWaterSystem()).getCumulativeCapitalExpense(),
					getInvestmentDistopia("Water"), getInvestmentUtopia("Water"), 
					getInvestmentGrowthRate("Water"));
			double sectorProfit = getTotalScoreAtYear(year, ((LocalWaterSoS) 
					country.getWaterSystem()).getCumulativeCashFlow(),
					getProfitDistopia("Water"), getProfitUtopia("Water"), 
					getProfitGrowthRate("Water"));
			City city = country.getCity("Urban");
			double regionInvest = getTotalScoreAtYear(year,
					city.getCumulativeCapitalExpense(), getInvestmentDistopia(city.getName()),
					getInvestmentUtopia(city.getName()), getInvestmentGrowthRate(city.getName()));

			updateSeries(waterPlayerScore, "Aquifer Security", 
					year, aquiferSecurity);
			updateSeries(waterPlayerScore, "Water Investment", 
					year, sectorInvest);
			updateSeries(waterPlayerScore, "Water Profit", 
					year, sectorProfit);
			updateSeries(waterPlayerScore, city.getName() + " Investment", 
					year, regionInvest);
			
			double totalScore = 0.3*aquiferSecurity + 0.3*sectorProfit 
					+ 0.3*sectorInvest + 0.1*regionInvest;
			
			updateSeries(waterPlayerScore, "Total Score", 
					year, totalScore);
			waterScoreLabel.setText("Individual Score: " 
					+ NumberFormat.getIntegerInstance().format(totalScore));
		}
		if(country.getPetroleumSystem() instanceof LocalPetroleumSoS 
				&& country.getElectricitySystem() instanceof LocalElectricitySoS) {
			reservoirSecurityHistory.add(getReservoirSecurityScore(((LocalPetroleumSoS) 
					country.getPetroleumSystem()).getReservoirLifetime()));
			
			reservoirSecurity = getTotalScore(reservoirSecurityHistory);
			double sectorInvest = getTotalScoreAtYear(year, ((LocalPetroleumSoS) 
					country.getPetroleumSystem()).getCumulativeCapitalExpense() 
					+ ((LocalElectricitySoS) country.getElectricitySystem()).getCumulativeCapitalExpense() ,
					getInvestmentDistopia("Energy"), getInvestmentUtopia("Energy"), 
					getInvestmentGrowthRate("Energy"));
			double sectorProfit = getTotalScoreAtYear(year, ((LocalPetroleumSoS) 
					country.getPetroleumSystem()).getCumulativeCashFlow()
					+ ((LocalElectricitySoS) country.getElectricitySystem()).getCumulativeCashFlow(),
					getProfitDistopia("Energy"), getProfitUtopia("Energy"), 
					getProfitGrowthRate("Energy"));
			City city = country.getCity("Industrial");
			double regionInvest = getTotalScoreAtYear(year,
					city.getCumulativeCapitalExpense(), getInvestmentDistopia(city.getName()),
					getInvestmentUtopia(city.getName()), getInvestmentGrowthRate(city.getName()));

			updateSeries(energyPlayerScore, "Oil Reservoir Security", 
					year, reservoirSecurity);
			updateSeries(energyPlayerScore, "Energy Investment", 
					year, sectorInvest);
			updateSeries(energyPlayerScore, "Energy Profit", 
					year, sectorProfit);
			updateSeries(energyPlayerScore, city.getName() + " Investment", 
					year, regionInvest);
			
			double totalScore = 0.2*reservoirSecurity + 0.4*sectorProfit 
					+ 0.3*sectorInvest + 0.1*regionInvest;
			
			updateSeries(energyPlayerScore, "Total Score", 
					year, totalScore);
			energyScoreLabel.setText("Individual Score: " 
					+ NumberFormat.getIntegerInstance().format(totalScore));
		}

		double nationalProfit = getTotalScoreAtYear(year, 
				country.getCumulativeCashFlow(),
				getProfitDistopia("Kingdom"), getProfitUtopia("Kingdom"), 
				getProfitGrowthRate("Kingdom"));
		
		updateSeries(teamScore, "Food Security", 
				year, foodSecurity);
		updateSeries(teamScore, "Aquifer Security", 
				year, aquiferSecurity);
		updateSeries(teamScore, "Oil Reservoir Security", 
				year, reservoirSecurity);
		updateSeries(teamScore, "National Profit", 
				year, nationalProfit);
		
		double totalScore = 0.3*foodSecurity + 0.3*aquiferSecurity 
				+ 0.1*reservoirSecurity + 0.3*nationalProfit;
		
		updateSeries(teamScore, "Total Score", 
				year, totalScore);
		teamScoreLabel.setText("Team Score: " 
				+ NumberFormat.getIntegerInstance().format(totalScore));
	}
}