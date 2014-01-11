package edu.mit.sips.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
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
	private static double getInvestmentGrowthRate(String name) {
		switch(name) {
		case "Industrial":
			return 0.05;
		case "Rural":
			return 0.03;
		case "Urban":
			return 0.04;
		case "Agriculture":
			return 0.05;
		case "Water":
			return 0.03;
		case "Energy":
			return 0.05;
		}
		return 0.03;
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
	private static double getTotalScore(List<Double> dataset) {
		double value = 0;
		for(double item : dataset) {
			value += item;
		}
		return value / dataset.size();
	}

	private final Country country;
	private final File outputFile;

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

		File logDir = new File("logs");
		if(!logDir.exists()) {
			logDir.mkdir();
		}
		outputFile = new File(logDir, System.getProperty("user.name") 
				+ "_" + new Date().getTime() + ".log");
		try {
			FileWriter fw = new FileWriter(outputFile);
			fw.write("Time, ");
			fw.write("Food Security, ");
			fw.write("Aquifer Security, ");
			fw.write("Reservoir Security, ");

			if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {
				JPanel scorePanel = createStackedAreaChart("Score (-)", null,
						new Color[]{PlottingUtils.YELLOW_GREEN, PlottingUtils.TOMATO, 
						PlottingUtils.GOLDENROD, PlottingUtils.PLUM, 
						PlottingUtils.BLACK}, agriculturePlayerScore);
				agricultureScoreLabel.setFont(getFont().deriveFont(20f));
				agricultureScoreLabel.setHorizontalAlignment(JLabel.CENTER);
				scorePanel.add(agricultureScoreLabel, BorderLayout.NORTH);
				addTab("Individual Score", Icons.AGRICULTURE, scorePanel);

				fw.write("Agriculture Profit, ");
				fw.write("Agriculture Investment, ");
				fw.write("Rural Investment, ");
				fw.write("Agriculture Score, ");
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

				fw.write("Water Profit, ");
				fw.write("Water Investment, ");
				fw.write("Urban Investment, ");
				fw.write("Water Score, ");
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

				fw.write("Energy Profit, ");
				fw.write("Energy Investment, ");
				fw.write("Industrial Investment, ");
				fw.write("Energy Score, ");
			}

			JPanel scorePanel = createStackedAreaChart("Score (-)", null,
					new Color[]{PlottingUtils.YELLOW_GREEN, PlottingUtils.DODGER_BLUE, 
					PlottingUtils.DIM_GRAY, PlottingUtils.GOLDENROD, 
					PlottingUtils.BLACK}, teamScore);
			teamScoreLabel.setFont(getFont().deriveFont(20f));
			teamScoreLabel.setHorizontalAlignment(JLabel.CENTER);
			scorePanel.add(teamScoreLabel, BorderLayout.NORTH);
			addTab("Team Score", Icons.COUNTRY, scorePanel);

			fw.write("National Profit, ");
			fw.write("Team Score \n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	private double getFoodSecurityScore(double foodSecurity) {
		return 1000 * Math.max(Math.min(foodSecurity, 1), 0);
		// return 0.5 + Math.atan(10*(foodSecurity - 0.3))/Math.PI;
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
		double minValue = distopiaTotal * (Math.pow(1+growthRate, year-1940) - 1)
				/ (Math.pow(1+growthRate, 2010-1940) - 1);
		double maxValue = utopiaTotal * (Math.pow(1+growthRate, year-1940) - 1)
				/ (Math.pow(1+growthRate, 2010-1940) - 1);
		
		if(value < minValue) {
			return 0;
		} else if(value > maxValue) {
			return 1000;
		} else {
			return 1000*(value - minValue)/(maxValue - minValue);
		}
	}

	private void initialize() {
		agricultureScoreLabel.setText("");
		agriculturePlayerScore.removeAllSeries();
		foodSecurityHistory.clear();

		waterScoreLabel.setText("");
		waterPlayerScore.removeAllSeries();
		aquiferSecurityHistory.clear();

		energyScoreLabel.setText("");
		energyPlayerScore.removeAllSeries();
		reservoirSecurityHistory.clear();

		teamScoreLabel.setText("");
		teamScore.removeAllSeries();
	}
	
	private double getAgricultureScore(double foodSecurity, double sectorProfit,
			double sectorInvestment, double regionInvestment) {
		return 0.3*foodSecurity + 0.3*sectorProfit 
				+ 0.3*sectorInvestment + 0.1*regionInvestment;
	}
	
	private double getWaterScore(double aquiferSecurity, double sectorProfit,
			double sectorInvestment, double regionInvestment) {
		return 0.3*aquiferSecurity + 0.3*sectorProfit 
				+ 0.3*sectorInvestment + 0.1*regionInvestment;
	}
	
	private double getEnergyScore(double reservoirSecurity, double sectorProfit,
			double sectorInvestment, double regionInvestment) {
		return 0.2*reservoirSecurity + 0.4*sectorProfit 
				+ 0.3*sectorInvestment + 0.1*regionInvestment;
	}
	
	private double getTeamScore(double foodSecurity, double aquiferSecurity,
			double reservoirSecurity, double nationalProfit) {
		return 0.25*foodSecurity + 0.25*aquiferSecurity 
				+ 0.25*reservoirSecurity + 0.25*nationalProfit;
	}
	
	private double getRegionInvestment(int year, City city) {
		return getTotalScoreAtYear(year,
				city.getCumulativeCapitalExpense(), getInvestmentDistopia(city.getName()),
				getInvestmentUtopia(city.getName()), getInvestmentGrowthRate(city.getName()));
	}
	
	private double getSectorProfit(int year, LocalAgricultureSoS system) {
		return getTotalScoreAtYear(year, system.getCumulativeCashFlow(),
				getProfitDistopia("Agriculture"), getProfitUtopia("Agriculture"), 
				getProfitGrowthRate("Agriculture"));
	}
	
	private double getSectorInvestment(int year, LocalAgricultureSoS system) {
		return getTotalScoreAtYear(year, system.getCumulativeCapitalExpense(),
				getInvestmentDistopia("Agriculture"), getInvestmentUtopia("Agriculture"), 
				getInvestmentGrowthRate("Agriculture"));
	}
	
	private double getSectorProfit(int year, LocalWaterSoS system) {
		return getTotalScoreAtYear(year, system.getCumulativeCashFlow(),
				getProfitDistopia("Water"), getProfitUtopia("Water"), 
				getProfitGrowthRate("Water"));
	}
	
	private double getSectorInvestment(int year, LocalWaterSoS system) {
		return getTotalScoreAtYear(year, system.getCumulativeCapitalExpense(),
				getInvestmentDistopia("Water"), getInvestmentUtopia("Water"), 
				getInvestmentGrowthRate("Water"));
	}
	
	private double getSectorProfit(int year, LocalPetroleumSoS system1,
			LocalElectricitySoS system2) {
		return getTotalScoreAtYear(year, system1.getCumulativeCashFlow() 
				+ system2.getCumulativeCashFlow(),
				getProfitDistopia("Energy"), getProfitUtopia("Energy"), 
				getProfitGrowthRate("Energy"));
	}
	
	private double getSectorInvestment(int year, LocalPetroleumSoS system1,
			LocalElectricitySoS system2) {
		return getTotalScoreAtYear(year, system1.getCumulativeCapitalExpense()
				+ system2.getCumulativeCapitalExpense(),
				getInvestmentDistopia("Energy"), getInvestmentUtopia("Energy"), 
				getInvestmentGrowthRate("Energy"));
	}

	@Override
	public void simulationCompleted(UpdateEvent event) { 
		int year = (int) event.getTime();
		
		try {
			FileWriter fw = new FileWriter(outputFile, true);
			fw.write(new Date().getTime() + ", ");
			
			double foodSecurity = getTotalScore(foodSecurityHistory);
			double aquiferSecurity = getTotalScore(aquiferSecurityHistory);
			double reservoirSecurity = getTotalScore(reservoirSecurityHistory);
			
			fw.write(foodSecurity + ", ");
			fw.write(aquiferSecurity + ", ");
			fw.write(reservoirSecurity + ", ");
			
			if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {	
				double sectorInvest = getSectorInvestment(year,
						(LocalAgricultureSoS) country.getAgricultureSystem());
				double sectorProfit = getSectorProfit(year,
						(LocalAgricultureSoS) country.getAgricultureSystem());
				City city = country.getCity("Rural");
				double regionInvest = getRegionInvestment(year, city);
				
				fw.write(sectorProfit + ", ");
				fw.write(sectorInvest + ", ");
				fw.write(regionInvest + ", ");
				fw.write(getAgricultureScore(foodSecurity, sectorProfit, 
						sectorInvest, regionInvest) + ", ");
			}
			if(country.getWaterSystem() instanceof LocalWaterSoS) {
				double sectorInvest = getSectorInvestment(year,
						(LocalWaterSoS) country.getWaterSystem());
				double sectorProfit = getSectorProfit(year,
						(LocalWaterSoS) country.getWaterSystem());
				City city = country.getCity("Urban");
				double regionInvest = getRegionInvestment(year, city);

				fw.write(sectorProfit + ", ");
				fw.write(sectorInvest + ", ");
				fw.write(regionInvest + ", ");
				fw.write(getWaterScore(aquiferSecurity, sectorProfit, 
						sectorInvest, regionInvest) + ", ");
			}
			if(country.getPetroleumSystem() instanceof LocalPetroleumSoS
					&& country.getElectricitySystem() instanceof LocalElectricitySoS) {
				double sectorInvest = getSectorInvestment(year,
						(LocalPetroleumSoS) country.getPetroleumSystem(),
						(LocalElectricitySoS) country.getElectricitySystem());
				double sectorProfit = getSectorProfit(year,
						(LocalPetroleumSoS) country.getPetroleumSystem(),
						(LocalElectricitySoS) country.getElectricitySystem());
				City city = country.getCity("Industrial");
				double regionInvest = getRegionInvestment(year, city);

				fw.write(sectorProfit + ", ");
				fw.write(sectorInvest + ", ");
				fw.write(regionInvest + ", ");
				fw.write(getEnergyScore(reservoirSecurity, sectorProfit, 
						sectorInvest, regionInvest) + ", ");
			}

			
			double nationalProfit = getTotalScoreAtYear(year, 
					country.getCumulativeCashFlow(),
					getProfitDistopia("Kingdom"), getProfitUtopia("Kingdom"), 
					getProfitGrowthRate("Kingdom"));

			
			fw.write(nationalProfit + ", ");
			fw.write(getTeamScore(foodSecurity, aquiferSecurity, 
					reservoirSecurity, nationalProfit) + "\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void simulationInitialized(UpdateEvent event) {
		initialize(); 
	}

	@Override
	public void simulationUpdated(UpdateEvent event) {
		int year = (int) event.getTime();
		if(year < 1980) {
			return;
		}

		foodSecurityHistory.add(getFoodSecurityScore(
				country.getAgricultureSystem().getFoodSecurity()));
		double foodSecurity = getTotalScore(foodSecurityHistory);

		aquiferSecurityHistory.add(getAquiferSecurityScore(
				country.getWaterSystem().getAquiferLifetime()));
		double aquiferSecurity = getTotalScore(aquiferSecurityHistory);

		reservoirSecurityHistory.add(getReservoirSecurityScore(
				country.getPetroleumSystem().getReservoirLifetime()));
		double reservoirSecurity = getTotalScore(reservoirSecurityHistory);

		if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {			
			double sectorInvest = getSectorInvestment(year,
					(LocalAgricultureSoS) country.getAgricultureSystem());
			double sectorProfit = getSectorProfit(year,
					(LocalAgricultureSoS) country.getAgricultureSystem());
			City city = country.getCity("Rural");
			double regionInvest = getRegionInvestment(year, city);

			updateSeries(agriculturePlayerScore, "Food Security", 
					year, foodSecurity);
			updateSeries(agriculturePlayerScore, "Agricultural Investment", 
					year, sectorInvest);
			updateSeries(agriculturePlayerScore, "Agricultural Profit", 
					year, getSectorProfit((int)event.getTime(),
							((LocalAgricultureSoS) country.getAgricultureSystem())));
			updateSeries(agriculturePlayerScore, city.getName() + " Investment", 
					year, regionInvest);

			double totalScore = getAgricultureScore(foodSecurity, 
					sectorProfit, sectorInvest, regionInvest);

			updateSeries(agriculturePlayerScore, "Total Score", 
					year, totalScore);
			agricultureScoreLabel.setText("Individual Score: " 
					+ NumberFormat.getIntegerInstance().format(totalScore));
		}
		if(country.getWaterSystem() instanceof LocalWaterSoS) {
			double sectorInvest = getSectorInvestment(year,
					(LocalWaterSoS) country.getWaterSystem());
			double sectorProfit = getSectorProfit(year,
					(LocalWaterSoS) country.getWaterSystem());
			City city = country.getCity("Urban");
			double regionInvest = getRegionInvestment(year, city);

			updateSeries(waterPlayerScore, "Aquifer Security", 
					year, aquiferSecurity);
			updateSeries(waterPlayerScore, "Water Investment", 
					year, sectorInvest);
			updateSeries(waterPlayerScore, "Water Profit", 
					year, sectorProfit);
			updateSeries(waterPlayerScore, city.getName() + " Investment", 
					year, regionInvest);

			double totalScore = getWaterScore(aquiferSecurity,
					sectorProfit, sectorInvest, regionInvest);

			updateSeries(waterPlayerScore, "Total Score", 
					year, totalScore);
			waterScoreLabel.setText("Individual Score: " 
					+ NumberFormat.getIntegerInstance().format(totalScore));
		}
		if(country.getPetroleumSystem() instanceof LocalPetroleumSoS 
				&& country.getElectricitySystem() instanceof LocalElectricitySoS) {
			double sectorInvest = getSectorInvestment(year,
					(LocalPetroleumSoS) country.getPetroleumSystem(),
					(LocalElectricitySoS) country.getElectricitySystem());
			double sectorProfit = getSectorProfit(year,
					(LocalPetroleumSoS) country.getPetroleumSystem(),
					(LocalElectricitySoS) country.getElectricitySystem());
			City city = country.getCity("Industrial");
			double regionInvest = getRegionInvestment(year, city);

			updateSeries(energyPlayerScore, "Oil Reservoir Security", 
					year, reservoirSecurity);
			updateSeries(energyPlayerScore, "Energy Investment", 
					year, sectorInvest);
			updateSeries(energyPlayerScore, "Energy Profit", 
					year, sectorProfit);
			updateSeries(energyPlayerScore, city.getName() + " Investment", 
					year, regionInvest);

			double totalScore = getEnergyScore(reservoirSecurity, 
					sectorProfit, sectorInvest, regionInvest);

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

		double totalScore = getTeamScore(foodSecurity, aquiferSecurity, 
				reservoirSecurity, nationalProfit);

		updateSeries(teamScore, "Total Score", 
				year, totalScore);
		teamScoreLabel.setText("Team Score: " 
				+ NumberFormat.getIntegerInstance().format(totalScore));
	}
}