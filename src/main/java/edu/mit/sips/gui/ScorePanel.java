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
import java.util.Date;

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
		case "Energy":
			return 0;
		}
		return 0;
	}
	private static double getInvestmentGrowthRate(String name) {
		switch(name) {
		case "Energy":
			return 0.03;
		}
		return 0;
	}

	private static double getInvestmentUtopia(String name) {
		switch(name) {
		case "Energy":
			return 50e9;
		}
		return 0;
	}
	private static double getProfitDystopia(String name) {
		switch(name) {
		case "Energy":
			return 0e9;
		}
		return 0;
	}
	private static double getProfitGrowthRate(String name) {
		switch(name) {
		case "Energy":
			return 0.04;
		}
		return 0;
	}

	private static double getProfitUtopia(String name) {
		switch(name) {
		case "Energy":
			return 500e9;
		}
		return 0;
	}

	private final Country country;
	private final Scenario scenario;
	
	private final File outputFile;
	private final File userOutputDir;

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
	private int roundNumber = 0;
	
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

		teamScorePanel = createStackedAreaChart(null, "Score", null,
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
			fw.write("Round, ");
			fw.write("Food Security, ");
			fw.write("Aquifer Security, ");
			fw.write("Reservoir Security, ");

			if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {
				agricultureScorePanel = createStackedAreaChart(null, "Score", null,
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
				waterScorePanel = createStackedAreaChart(null, "Score", null,
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
				energyScorePanel = createStackedAreaChart(null, "Score", null,
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

		waterScoreLabel.setText("");
		waterPlayerScore.removeAllSeries();

		energyScoreLabel.setText("");
		energyPlayerScore.removeAllSeries();

		teamScoreLabel.setText("");
		teamScore.removeAllSeries();
		
		overBudgetYear = 0;
		overBudgetValue = 0;
	}
	
	private double getEnergyScore(double reservoirSecurity, 
			double sectorProfit, double sectorInvestment) {
		return (reservoirSecurity + sectorProfit + sectorInvestment)/3d;
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
			fw.write(roundNumber + ", ");
			
			double foodScore = country.getAgricultureSystem().getFoodSecurityScore();
			double aquiferScore = country.getWaterSystem().getAquiferSecurityScore();
			double reservoirScore = country.getPetroleumSystem().getReservoirSecurityScore();
			
			fw.write(foodScore + ", ");
			fw.write(aquiferScore + ", ");
			fw.write(reservoirScore + ", ");
			
			if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {	
				double politicalScore = ((LocalAgricultureSoS) country.getAgricultureSystem()).getPoliticalPowerScore(year);
				double financialScore = ((LocalAgricultureSoS) country.getAgricultureSystem()).getFinancialSecurityScore(year);
				double aggregateScore = ((LocalAgricultureSoS) country.getAgricultureSystem()).getAggregateScore(year);

				fw.write(financialScore + ", ");
				fw.write(politicalScore + ", ");
				fw.write(aggregateScore + ", ");
			}
			if(country.getWaterSystem() instanceof LocalWaterSoS) {
				double politicalScore = ((LocalWaterSoS) country.getWaterSystem()).getPoliticalPowerScore(year);
				double financialScore = ((LocalWaterSoS) country.getWaterSystem()).getFinancialSecurityScore(year);
				double aggregateScore = ((LocalWaterSoS) country.getWaterSystem()).getAggregateScore(year);

				fw.write(financialScore + ", ");
				fw.write(politicalScore + ", ");
				fw.write(aggregateScore + ", ");
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
				fw.write(getEnergyScore(reservoirScore, sectorProfit, 
						sectorInvest) + ", ");
			}

			double nationalProfit = getTotalScoreAtYear(year, 
					country.getCumulativeCashFlow(),
					getProfitDystopia("Country"), getProfitUtopia("Country"), 
					getProfitGrowthRate("Country"));

			fw.write(overBudgetYear + ", ");
			fw.write(nationalProfit + ", ");
			fw.write(country.getAggregatedScore(year) + "\n");
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
		roundNumber++;
	}

	@Override
	public void simulationUpdated(UpdateEvent event) {
		int year = (int) event.getTime();
		if(year < 1980) {
			return;
		}
		
		scoreLabel.setText("");
		
		double foodScore = country.getAgricultureSystem().getFoodSecurityScore();
		double aquiferScore = country.getWaterSystem().getAquiferSecurityScore();
		double reservoirScore = country.getPetroleumSystem().getReservoirSecurityScore();

		if(country.getAgricultureSystem() instanceof LocalAgricultureSoS) {
			double politicalScore = ((LocalAgricultureSoS) country.getAgricultureSystem()).getPoliticalPowerScore(year);
			double financialScore = ((LocalAgricultureSoS) country.getAgricultureSystem()).getFinancialSecurityScore(year);
			double aggregateScore = ((LocalAgricultureSoS) country.getAgricultureSystem()).getAggregateScore(year);

			updateSeries(agriculturePlayerScore, "Food Security", 
					year, foodScore);
			updateSeries(agriculturePlayerScore, "Agricultural Investment", 
					year, politicalScore);
			updateSeries(agriculturePlayerScore, "Agricultural Profit", 
					year, financialScore);

			updateSeries(agriculturePlayerScore, "Total Score", 
					year, aggregateScore);
			String scoreText = "Round " + roundNumber + " Agriculture Score: " 
					+ NumberFormat.getIntegerInstance().format(aggregateScore);
			agricultureScoreLabel.setText(scoreText);
			scoreLabel.setText((scoreLabel.getText().isEmpty()?"":
				scoreLabel.getText() + ", ") + scoreText);
		}
		if(country.getWaterSystem() instanceof LocalWaterSoS) {
			double politicalScore = ((LocalWaterSoS) country.getWaterSystem()).getPoliticalPowerScore(year);
			double financialScore = ((LocalWaterSoS) country.getWaterSystem()).getFinancialSecurityScore(year);
			double aggregateScore = ((LocalWaterSoS) country.getWaterSystem()).getAggregateScore(year);

			updateSeries(waterPlayerScore, "Aquifer Security", 
					year, aquiferScore);
			updateSeries(waterPlayerScore, "Water Investment", 
					year, politicalScore);
			updateSeries(waterPlayerScore, "Water Profit", 
					year, financialScore);

			updateSeries(waterPlayerScore, "Total Score", 
					year, aggregateScore);
			String scoreText = "Round " + roundNumber + " Water Score: " 
					+ NumberFormat.getIntegerInstance().format(aggregateScore);
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
					year, reservoirScore);
			updateSeries(energyPlayerScore, "Energy Investment", 
					year, sectorInvest);
			updateSeries(energyPlayerScore, "Energy Profit", 
					year, sectorProfit);

			double totalScore = getEnergyScore(reservoirScore, 
					sectorProfit, sectorInvest);

			updateSeries(energyPlayerScore, "Total Score", 
					year, totalScore);
			String scoreText = "Round " + roundNumber + " Energy Score: " 
					+ NumberFormat.getIntegerInstance().format(totalScore);
			energyScoreLabel.setText(scoreText);
			scoreLabel.setText((scoreLabel.getText().isEmpty()?"":
				scoreLabel.getText() + ", ") + scoreText);
		}
		
		if(country.getTotalCapitalExpense() > scenario.getMaxAnnualInvestment()) {
			overBudgetYear = year;
			overBudgetValue = country.getTotalCapitalExpense();
		}

		double financialScore = country.getFinancialSecurityScore(year);

		updateSeries(teamScore, "Food Security", 
				year, foodScore);
		updateSeries(teamScore, "Aquifer Security", 
				year, aquiferScore);
		updateSeries(teamScore, "Oil Reservoir Security", 
				year, reservoirScore);
		updateSeries(teamScore, "National Profit", 
				year, financialScore);

		double aggregateScore = country.getAggregatedScore(year);

		updateSeries(teamScore, "Total Score", 
				year, aggregateScore);
		String scoreText = "Round " + roundNumber + " Team Score: " 
				+ NumberFormat.getIntegerInstance().format(aggregateScore)
				+ (overBudgetYear>0?"* (Over budget in " + overBudgetYear + ")":"");
		teamScoreLabel.setText(scoreText);
		if(scenario.isTeamScoreDisplayed()) {
			scoreLabel.setText((scoreLabel.getText().isEmpty()?"":
				scoreLabel.getText() + ", ") + scoreText);
		}
	}
}