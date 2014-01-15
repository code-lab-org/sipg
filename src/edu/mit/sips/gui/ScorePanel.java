package edu.mit.sips.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.LocalAgricultureSoS;
import edu.mit.sips.core.electricity.LocalElectricitySoS;
import edu.mit.sips.core.petroleum.LocalPetroleumSoS;
import edu.mit.sips.core.water.LocalWaterSoS;
import edu.mit.sips.io.Icons;
import edu.mit.sips.scenario.Scenario;

public class ScorePanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = 355808870154994451L;

	private static double getInvestmentDystopia(String name) {
		switch(name) {
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
		case "Agriculture":
			return 0.06;
		case "Water":
			return 0.08;
		case "Energy":
			return 0.03;
		}
		return 0;
	}

	private static double getInvestmentUtopia(String name) {
		switch(name) {
		case "Agriculture":
			return 10e9;
		case "Water":
			return 10e9;
		case "Energy":
			return 50e9;
		}
		return 0;
	}
	private static double getProfitDystopia(String name) {
		switch(name) {
		case "Agriculture":
			return 0;
		case "Water":
			return -8e9;
		case "Energy":
			return 0e9;
		case "Country":
			return 0e9;
		}
		return 0;
	}
	private static double getProfitGrowthRate(String name) {
		switch(name) {
		case "Agriculture":
			return 0.05;
		case "Water":
			return 0.08;
		case "Energy":
			return 0.04;
		case "Country":
			return 0.04;
		}
		return 0;
	}

	private static double getProfitUtopia(String name) {
		switch(name) {
		case "Agriculture":
			return 50e9;
		case "Water":
			return 0e9;
		case "Energy":
			return 500e9;
		case "Country":
			return 600e9;
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
	private final Scenario scenario;
	
	private final File outputFile;
	private final File userOutputDir;

	List<Double> foodSecurityHistory = new ArrayList<Double>();
	List<Double> aquiferSecurityHistory = new ArrayList<Double>();
	List<Double> reservoirSecurityHistory = new ArrayList<Double>();

	private JPanel agricultureScorePanel;
	private final JLabel agricultureScoreLabel = new JLabel("");
	DefaultTableXYDataset agriculturePlayerScore = new DefaultTableXYDataset();

	private JPanel waterScorePanel;
	private final JLabel waterScoreLabel = new JLabel("");
	DefaultTableXYDataset waterPlayerScore = new DefaultTableXYDataset();

	private JPanel energyScorePanel;
	private final JLabel energyScoreLabel = new JLabel("");
	DefaultTableXYDataset energyPlayerScore = new DefaultTableXYDataset();

	private JPanel teamScorePanel;
	private final JLabel teamScoreLabel = new JLabel("");
	DefaultTableXYDataset teamScore = new DefaultTableXYDataset();
	
	private double overBudgetValue = 0;
	private int overBudgetYear = 0;
	
	private final JLabel scoreLabel;

	public ScorePanel(Scenario scenario, JLabel scoreLabel) {
		super(scenario.getCountry().getSocialSystem());
		this.scenario = scenario;
		this.country = scenario.getCountry();
		this.scoreLabel = scoreLabel;

		File logDir = new File("logs");
		if(!logDir.exists()) {
			logDir.mkdir();
		}
		outputFile = new File(logDir, System.getProperty("user.name") 
				+ "_" + new Date().getTime() + ".log");
		
		userOutputDir = new File(System.getProperty("user.home"), "sips-g");
		if(!userOutputDir.exists()) {
			userOutputDir.mkdir();
		}

		teamScorePanel = createStackedAreaChart("Score (-)", null,
				new Color[]{PlottingUtils.YELLOW_GREEN, PlottingUtils.DODGER_BLUE, 
				PlottingUtils.DIM_GRAY, PlottingUtils.GOLDENROD, 
				PlottingUtils.BLACK}, teamScore);
		teamScoreLabel.setFont(getFont().deriveFont(20f));
		teamScoreLabel.setHorizontalAlignment(JLabel.CENTER);
		teamScorePanel.add(teamScoreLabel, BorderLayout.NORTH);
		if(scenario.isTeamScoreDisplayed()) {
			addTab("Team Score", Icons.COUNTRY, teamScorePanel);
		}
		
		try {
			FileWriter fw = new FileWriter(outputFile);
			fw.write("Time, ");
			fw.write("Food Security, ");
			fw.write("Aquifer Security, ");
			fw.write("Reservoir Security, ");

			if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {
				agricultureScorePanel = createStackedAreaChart("Score (-)", null,
						new Color[]{PlottingUtils.YELLOW_GREEN, PlottingUtils.TOMATO, 
						PlottingUtils.GOLDENROD, PlottingUtils.BLACK}, agriculturePlayerScore);
				agricultureScoreLabel.setFont(getFont().deriveFont(20f));
				agricultureScoreLabel.setHorizontalAlignment(JLabel.CENTER);
				agricultureScorePanel.add(agricultureScoreLabel, BorderLayout.NORTH);
				addTab("Individual Score", Icons.AGRICULTURE, agricultureScorePanel);

				fw.write("Agriculture Profit, ");
				fw.write("Agriculture Investment, ");
				fw.write("Agriculture Score, ");
			}
			if(country.getWaterSystem() instanceof LocalWaterSoS) {
				waterScorePanel = createStackedAreaChart("Score (-)", null,
						new Color[]{PlottingUtils.DODGER_BLUE, PlottingUtils.TOMATO, 
						PlottingUtils.GOLDENROD, PlottingUtils.BLACK}, waterPlayerScore);
				waterScoreLabel.setFont(getFont().deriveFont(20f));
				waterScoreLabel.setHorizontalAlignment(JLabel.CENTER);
				waterScorePanel.add(waterScoreLabel, BorderLayout.NORTH);
				addTab("Individual Score", Icons.WATER, waterScorePanel);

				fw.write("Water Profit, ");
				fw.write("Water Investment, ");
				fw.write("Water Score, ");
			}
			if(country.getPetroleumSystem() instanceof LocalPetroleumSoS
					&& country.getElectricitySystem() instanceof LocalElectricitySoS) {
				energyScorePanel = createStackedAreaChart("Score (-)", null,
						new Color[]{PlottingUtils.DIM_GRAY, PlottingUtils.TOMATO, 
						PlottingUtils.GOLDENROD, PlottingUtils.BLACK}, energyPlayerScore);
				energyScoreLabel.setFont(getFont().deriveFont(20f));
				energyScoreLabel.setHorizontalAlignment(JLabel.CENTER);
				energyScorePanel.add(energyScoreLabel, BorderLayout.NORTH);
				addTab("Individual Score", Icons.ENERGY, energyScorePanel);

				fw.write("Energy Profit, ");
				fw.write("Energy Investment, ");
				fw.write("Energy Score, ");
			}

			fw.write("Over Budget, ");
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
		return 1000 / 0.75 * Math.max(Math.min(foodSecurity, 0.75), 0);
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

	private double getTotalScoreAtYear(int year, double value, 
			double dystopiaTotal, double utopiaTotal, double growthRate) {
		double minValue = dystopiaTotal * (Math.pow(1+growthRate, year-1940) - 1)
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
		scoreLabel.setText("");
		
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
		
		overBudgetYear = 0;
		overBudgetValue = 0;
	}
	
	private double getAgricultureScore(double foodSecurity, 
			double sectorProfit, double sectorInvestment) {
		return (foodSecurity + sectorProfit + sectorInvestment)/3d;
	}
	
	private double getWaterScore(double aquiferSecurity, 
			double sectorProfit, double sectorInvestment) {
		return (aquiferSecurity + sectorProfit + sectorInvestment)/3d;
	}
	
	private double getEnergyScore(double reservoirSecurity, 
			double sectorProfit, double sectorInvestment) {
		return (reservoirSecurity + sectorProfit + sectorInvestment)/3d;
	}
	
	private double getTeamScore(double foodSecurity, double aquiferSecurity,
			double reservoirSecurity, double nationalProfit) {
		return (foodSecurity + aquiferSecurity 
				+ reservoirSecurity + nationalProfit)/4d;
	}
	
	private double getSectorProfit(int year, LocalAgricultureSoS system) {
		return getTotalScoreAtYear(year, system.getCumulativeCashFlow(),
				getProfitDystopia("Agriculture"), getProfitUtopia("Agriculture"), 
				getProfitGrowthRate("Agriculture"));
	}
	
	private double getSectorInvestment(int year, LocalAgricultureSoS system) {
		return getTotalScoreAtYear(year, system.getCumulativeCapitalExpense(),
				getInvestmentDystopia("Agriculture"), getInvestmentUtopia("Agriculture"), 
				getInvestmentGrowthRate("Agriculture"));
	}
	
	private double getSectorProfit(int year, LocalWaterSoS system) {
		return getTotalScoreAtYear(year, system.getCumulativeCashFlow(),
				getProfitDystopia("Water"), getProfitUtopia("Water"), 
				getProfitGrowthRate("Water"));
	}
	
	private double getSectorInvestment(int year, LocalWaterSoS system) {
		return getTotalScoreAtYear(year, system.getCumulativeCapitalExpense(),
				getInvestmentDystopia("Water"), getInvestmentUtopia("Water"), 
				getInvestmentGrowthRate("Water"));
	}
	
	private double getSectorProfit(int year, LocalPetroleumSoS system1,
			LocalElectricitySoS system2) {
		return getTotalScoreAtYear(year, system1.getCumulativeCashFlow() 
				+ system2.getCumulativeCashFlow(),
				getProfitDystopia("Energy"), getProfitUtopia("Energy"), 
				getProfitGrowthRate("Energy"));
	}
	
	private double getSectorInvestment(int year, LocalPetroleumSoS system1,
			LocalElectricitySoS system2) {
		return getTotalScoreAtYear(year, system1.getCumulativeCapitalExpense()
				+ system2.getCumulativeCapitalExpense(),
				getInvestmentDystopia("Energy"), getInvestmentUtopia("Energy"), 
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
				
				fw.write(sectorProfit + ", ");
				fw.write(sectorInvest + ", ");
				fw.write(getAgricultureScore(foodSecurity, sectorProfit, 
						sectorInvest) + ", ");
			}
			if(country.getWaterSystem() instanceof LocalWaterSoS) {
				double sectorInvest = getSectorInvestment(year,
						(LocalWaterSoS) country.getWaterSystem());
				double sectorProfit = getSectorProfit(year,
						(LocalWaterSoS) country.getWaterSystem());

				fw.write(sectorProfit + ", ");
				fw.write(sectorInvest + ", ");
				fw.write(getWaterScore(aquiferSecurity, sectorProfit, 
						sectorInvest) + ", ");
			}
			if(country.getPetroleumSystem() instanceof LocalPetroleumSoS
					&& country.getElectricitySystem() instanceof LocalElectricitySoS) {
				double sectorInvest = getSectorInvestment(year,
						(LocalPetroleumSoS) country.getPetroleumSystem(),
						(LocalElectricitySoS) country.getElectricitySystem());
				double sectorProfit = getSectorProfit(year,
						(LocalPetroleumSoS) country.getPetroleumSystem(),
						(LocalElectricitySoS) country.getElectricitySystem());

				fw.write(sectorProfit + ", ");
				fw.write(sectorInvest + ", ");
				fw.write(getEnergyScore(reservoirSecurity, sectorProfit, 
						sectorInvest) + ", ");
			}

			double nationalProfit = getTotalScoreAtYear(year, 
					country.getCumulativeCashFlow(),
					getProfitDystopia("Country"), getProfitUtopia("Country"), 
					getProfitGrowthRate("Country"));

			fw.write(overBudgetYear + ", ");
			fw.write(nationalProfit + ", ");
			fw.write(getTeamScore(foodSecurity, aquiferSecurity, 
					reservoirSecurity, nationalProfit) + "\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// hack to save chart images
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				if(agricultureScorePanel != null) {
					for(Component c : agricultureScorePanel.getComponents()) {
						if(c instanceof ChartPanel) {
							ChartPanel chartPanel = (ChartPanel) c;
							BufferedImage img = chartPanel.getChart().createBufferedImage(
									chartPanel.getWidth(), chartPanel.getHeight());
							Graphics2D g2d = img.createGraphics();
							g2d.drawImage(img, 0, 0, null);
							g2d.setPaint(Color.black);
							g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
							FontMetrics fm = g2d.getFontMetrics();
							String text = agricultureScoreLabel.getText();
							g2d.drawString(text, img.getWidth()/2 - fm.stringWidth(text)/2, fm.getHeight() + 5);
							g2d.dispose();
							File outputFile = new File(userOutputDir, 
									new Date().getTime() + "-agriculture.png");
							try {
								ImageIO.write(img,  "png", outputFile);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				if(waterScorePanel != null) {
					for(Component c : waterScorePanel.getComponents()) {
						if(c instanceof ChartPanel) {
							ChartPanel chartPanel = (ChartPanel) c;
							BufferedImage img = chartPanel.getChart().createBufferedImage(
									chartPanel.getWidth(), chartPanel.getHeight());
							Graphics2D g2d = img.createGraphics();
							g2d.drawImage(img, 0, 0, null);
							g2d.setPaint(Color.black);
							g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
							FontMetrics fm = g2d.getFontMetrics();
							String text = waterScoreLabel.getText();
							g2d.drawString(text, img.getWidth()/2 - fm.stringWidth(text)/2, fm.getHeight() + 5);
							g2d.dispose();
							File outputFile = new File(userOutputDir, 
									new Date().getTime() + "-water.png");
							try {
								ImageIO.write(img,  "png", outputFile);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				if(energyScorePanel != null) {
					for(Component c : energyScorePanel.getComponents()) {
						if(c instanceof ChartPanel) {
							ChartPanel chartPanel = (ChartPanel) c;
							BufferedImage img = chartPanel.getChart().createBufferedImage(
									chartPanel.getWidth(), chartPanel.getHeight());
							Graphics2D g2d = img.createGraphics();
							g2d.drawImage(img, 0, 0, null);
							g2d.setPaint(Color.black);
							g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
							FontMetrics fm = g2d.getFontMetrics();
							String text = energyScoreLabel.getText();
							g2d.drawString(text, img.getWidth()/2 - fm.stringWidth(text)/2, fm.getHeight() + 5);
							g2d.dispose();
							File outputFile = new File(userOutputDir, 
									+ new Date().getTime() + "-energy.png");
							try {
								ImageIO.write(img,  "png", outputFile);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				if(scenario.isTeamScoreDisplayed()) {
					for(Component c : teamScorePanel.getComponents()) {
						if(c instanceof ChartPanel) {
							ChartPanel chartPanel = (ChartPanel) c;
							BufferedImage img = chartPanel.getChart().createBufferedImage(
									chartPanel.getWidth(), chartPanel.getHeight());
							Graphics2D g2d = img.createGraphics();
							g2d.drawImage(img, 0, 0, null);
							g2d.setPaint(Color.black);
							g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
							FontMetrics fm = g2d.getFontMetrics();
							String text = teamScoreLabel.getText();
							g2d.drawString(text, img.getWidth()/2 - fm.stringWidth(text)/2, fm.getHeight() + 5);
							g2d.dispose();
							File outputFile = new File(userOutputDir,  
									+ new Date().getTime() + "-team.png");
							try {
								ImageIO.write(img,  "png", outputFile);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		
		if(overBudgetYear > 0) {
			NumberFormat format = NumberFormat.getNumberInstance();
			format.setMaximumFractionDigits(3);
			JOptionPane.showMessageDialog(getTopLevelAncestor(), 
					"Total capital expenditures in " + overBudgetYear 
					+ " (\u00a7" + format.format(overBudgetValue/1e9) 
					+ " billion) was over the limit of \u00a7" 
					+ format.format(scenario.getMaxAnnualInvestment()/1e9) + " billion.", 
					"Over-Budget Warning", JOptionPane.ERROR_MESSAGE);
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
		
		scoreLabel.setText("");

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

			updateSeries(agriculturePlayerScore, "Food Security", 
					year, foodSecurity);
			updateSeries(agriculturePlayerScore, "Agricultural Investment", 
					year, sectorInvest);
			updateSeries(agriculturePlayerScore, "Agricultural Profit", 
					year, getSectorProfit((int)event.getTime(),
							((LocalAgricultureSoS) country.getAgricultureSystem())));

			double totalScore = getAgricultureScore(foodSecurity, 
					sectorProfit, sectorInvest);

			updateSeries(agriculturePlayerScore, "Total Score", 
					year, totalScore);
			String scoreText = "Agriculture Score: " 
					+ NumberFormat.getIntegerInstance().format(totalScore);
			agricultureScoreLabel.setText(scoreText);
			scoreLabel.setText((scoreLabel.getText().isEmpty()?"":
				scoreLabel.getText() + ", ") + scoreText);
		}
		if(country.getWaterSystem() instanceof LocalWaterSoS) {
			double sectorInvest = getSectorInvestment(year,
					(LocalWaterSoS) country.getWaterSystem());
			double sectorProfit = getSectorProfit(year,
					(LocalWaterSoS) country.getWaterSystem());

			updateSeries(waterPlayerScore, "Aquifer Security", 
					year, aquiferSecurity);
			updateSeries(waterPlayerScore, "Water Investment", 
					year, sectorInvest);
			updateSeries(waterPlayerScore, "Water Profit", 
					year, sectorProfit);

			double totalScore = getWaterScore(aquiferSecurity,
					sectorProfit, sectorInvest);

			updateSeries(waterPlayerScore, "Total Score", 
					year, totalScore);
			String scoreText = "Water Score: " 
					+ NumberFormat.getIntegerInstance().format(totalScore);
			waterScoreLabel.setText(scoreText);
			scoreLabel.setText((scoreLabel.getText().isEmpty()?"":
				scoreLabel.getText() + ", ") + scoreText);
		}
		if(country.getPetroleumSystem() instanceof LocalPetroleumSoS 
				&& country.getElectricitySystem() instanceof LocalElectricitySoS) {
			double sectorInvest = getSectorInvestment(year,
					(LocalPetroleumSoS) country.getPetroleumSystem(),
					(LocalElectricitySoS) country.getElectricitySystem());
			double sectorProfit = getSectorProfit(year,
					(LocalPetroleumSoS) country.getPetroleumSystem(),
					(LocalElectricitySoS) country.getElectricitySystem());

			updateSeries(energyPlayerScore, "Oil Reservoir Security", 
					year, reservoirSecurity);
			updateSeries(energyPlayerScore, "Energy Investment", 
					year, sectorInvest);
			updateSeries(energyPlayerScore, "Energy Profit", 
					year, sectorProfit);

			double totalScore = getEnergyScore(reservoirSecurity, 
					sectorProfit, sectorInvest);

			updateSeries(energyPlayerScore, "Total Score", 
					year, totalScore);
			String scoreText = "Energy Score: " 
					+ NumberFormat.getIntegerInstance().format(totalScore);
			energyScoreLabel.setText(scoreText);
			scoreLabel.setText((scoreLabel.getText().isEmpty()?"":
				scoreLabel.getText() + ", ") + scoreText);
		}
		
		if(country.getTotalCapitalExpense() > scenario.getMaxAnnualInvestment()) {
			overBudgetYear = year;
			overBudgetValue = country.getTotalCapitalExpense();
		}

		double nationalProfit = getTotalScoreAtYear(year, 
				country.getCumulativeCashFlow(),
				getProfitDystopia("Country"), getProfitUtopia("Country"), 
				getProfitGrowthRate("Country"));

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
		String scoreText = "Team Score: " 
				+ NumberFormat.getIntegerInstance().format(totalScore)
				+ (overBudgetYear>0?"* (Over budget in " + overBudgetYear + ")":"");
		teamScoreLabel.setText(scoreText);
		if(scenario.isTeamScoreDisplayed()) {
			scoreLabel.setText((scoreLabel.getText().isEmpty()?"":
				scoreLabel.getText() + ", ") + scoreText);
		}
	}
}