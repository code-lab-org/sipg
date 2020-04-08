/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sipg.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sipg.core.Country;
import edu.mit.sipg.core.agriculture.AgricultureSoS;
import edu.mit.sipg.core.electricity.ElectricitySoS;
import edu.mit.sipg.core.petroleum.PetroleumSoS;
import edu.mit.sipg.core.water.WaterSoS;
import edu.mit.sipg.gui.base.InfrastructureSystemPanel;
import edu.mit.sipg.gui.event.UpdateEvent;
import edu.mit.sipg.io.Icons;
import edu.mit.sipg.scenario.Scenario;

/**
 * Graphics panel to display key score information.
 * 
 * @author Paul T. Grogan
 */
public class ScorePanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = 355808870154994451L;
	private static Logger logger = Logger.getLogger(ScorePanel.class);

	private final Country country;
	private final Scenario scenario;

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
	private double overBudgetLimit = 0;
	private int overBudgetYear = 0;
	private int roundNumber = 0;
	
	private final JLabel scoreLabel;
	private File userOutputDir;

	/**
	 * Instantiates a new score panel.
	 *
	 * @param scenario the scenario
	 * @param scoreLabel the score label
	 */
	public ScorePanel(Scenario scenario, JLabel scoreLabel) {
		super(scenario.getCountry().getSocialSystem());
		this.scenario = scenario;
		this.country = scenario.getCountry();
		this.scoreLabel = scoreLabel;
		
		if(System.getenv().containsKey("SIPG_HOME")) {
			userOutputDir = new File(System.getenv("SIPG_HOME"));
		} else {
			userOutputDir = new File(System.getProperty("user.home"), "SIPG");
		}
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
		
		if(country.getAgricultureSystem().isLocal()) {
			agricultureScorePanel = createStackedAreaChart(null, "Score", null,
					new Color[]{PlottingUtils.YELLOW_GREEN, PlottingUtils.TOMATO, 
					PlottingUtils.GOLDENROD, PlottingUtils.BLACK}, agriculturePlayerScore);
			agricultureScoreLabel.setFont(getFont().deriveFont(20f));
			agricultureScoreLabel.setHorizontalAlignment(JLabel.CENTER);
			agricultureScorePanel.add(agricultureScoreLabel, BorderLayout.NORTH);
			addTab("Individual Score", Icons.AGRICULTURE, agricultureScorePanel);
		}
		if(country.getWaterSystem().isLocal()) {
			waterScorePanel = createStackedAreaChart(null, "Score", null,
					new Color[]{PlottingUtils.DODGER_BLUE, PlottingUtils.TOMATO, 
					PlottingUtils.GOLDENROD, PlottingUtils.BLACK}, waterPlayerScore);
			waterScoreLabel.setFont(getFont().deriveFont(20f));
			waterScoreLabel.setHorizontalAlignment(JLabel.CENTER);
			waterScorePanel.add(waterScoreLabel, BorderLayout.NORTH);
			addTab("Individual Score", Icons.WATER, waterScorePanel);
		}
		if(country.getPetroleumSystem().isLocal() 
				&& country.getElectricitySystem().isLocal()) {
			energyScorePanel = createStackedAreaChart(null, "Score", null,
					new Color[]{PlottingUtils.DIM_GRAY, PlottingUtils.TOMATO, 
					PlottingUtils.GOLDENROD, PlottingUtils.BLACK}, energyPlayerScore);
			energyScoreLabel.setFont(getFont().deriveFont(20f));
			energyScoreLabel.setHorizontalAlignment(JLabel.CENTER);
			energyScorePanel.add(energyScoreLabel, BorderLayout.NORTH);
			addTab("Individual Score", Icons.ENERGY, energyScorePanel);
		}
	}

	/**
	 * Initialize.
	 */
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
		overBudgetLimit = 0;
	}

	@Override
	public void simulationCompleted(UpdateEvent event) { 
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
								logger.error(e);
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
								logger.error(e);
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
								logger.error(e);
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
								logger.error(e);
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
					+ format.format(overBudgetLimit/1e9) + " billion.", 
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

		if(country.getAgricultureSystem() instanceof AgricultureSoS.Local) {
			double politicalScore = ((AgricultureSoS.Local) country.getAgricultureSystem()).getPoliticalPowerScore(year);
			double financialScore = ((AgricultureSoS.Local) country.getAgricultureSystem()).getFinancialSecurityScore(year);
			double aggregateScore = ((AgricultureSoS.Local) country.getAgricultureSystem()).getAggregateScore(year);

			updateSeries(agriculturePlayerScore, "Food Security", year, foodScore);
			updateSeries(agriculturePlayerScore, "Agricultural Investment", year, politicalScore);
			updateSeries(agriculturePlayerScore, "Agricultural Profit", year, financialScore);
			updateSeries(agriculturePlayerScore, "Total Score", year, aggregateScore);
			String scoreText = "Round " + roundNumber + " Agriculture Score: " 
					+ NumberFormat.getIntegerInstance().format(aggregateScore);
			agricultureScoreLabel.setText(scoreText);
			scoreLabel.setText((scoreLabel.getText().isEmpty()?"":
				scoreLabel.getText() + ", ") + scoreText);
		}
		if(country.getWaterSystem() instanceof WaterSoS.Local) {
			double politicalScore = ((WaterSoS.Local) country.getWaterSystem()).getPoliticalPowerScore(year);
			double financialScore = ((WaterSoS.Local) country.getWaterSystem()).getFinancialSecurityScore(year);
			double aggregateScore = ((WaterSoS.Local) country.getWaterSystem()).getAggregateScore(year);

			updateSeries(waterPlayerScore, "Aquifer Security", year, aquiferScore);
			updateSeries(waterPlayerScore, "Water Investment", year, politicalScore);
			updateSeries(waterPlayerScore, "Water Profit", year, financialScore);
			updateSeries(waterPlayerScore, "Total Score", year, aggregateScore);
			String scoreText = "Round " + roundNumber + " Water Score: " 
					+ NumberFormat.getIntegerInstance().format(aggregateScore);
			waterScoreLabel.setText(scoreText);
			scoreLabel.setText((scoreLabel.getText().isEmpty()?"":
				scoreLabel.getText() + ", ") + scoreText);
		}
		if(country.getPetroleumSystem() instanceof PetroleumSoS.Local 
				&& country.getElectricitySystem() instanceof ElectricitySoS.Local) {
			double politicalScore = ((PetroleumSoS.Local) country.getPetroleumSystem()).getPoliticalPowerScore(
					year, (ElectricitySoS.Local) country.getElectricitySystem());
			double financialScore = ((PetroleumSoS.Local) country.getPetroleumSystem()).getFinancialSecurityScore(
					year, (ElectricitySoS.Local) country.getElectricitySystem());
			double aggregateScore = ((PetroleumSoS.Local) country.getPetroleumSystem()).getAggregateScore(
					year, (ElectricitySoS.Local) country.getElectricitySystem());

			updateSeries(energyPlayerScore, "Oil Reservoir Security", year, reservoirScore);
			updateSeries(energyPlayerScore, "Energy Investment", year, politicalScore);
			updateSeries(energyPlayerScore, "Energy Profit", year, financialScore);

			updateSeries(energyPlayerScore, "Total Score", year, aggregateScore);
			String scoreText = "Round " + roundNumber + " Energy Score: " 
					+ NumberFormat.getIntegerInstance().format(aggregateScore);
			energyScoreLabel.setText(scoreText);
			scoreLabel.setText((scoreLabel.getText().isEmpty()?"":
				scoreLabel.getText() + ", ") + scoreText);
		}
		
		if(country.getTotalCapitalExpense() > country.getCapitalBudgetLimit()) {
			overBudgetYear = year;
			overBudgetValue = country.getTotalCapitalExpense();
			overBudgetLimit = country.getCapitalBudgetLimit();
		}

		double financialScore = country.getFinancialSecurityScore(year);

		updateSeries(teamScore, "Food Security", year, foodScore);
		updateSeries(teamScore, "Aquifer Security", year, aquiferScore);
		updateSeries(teamScore, "Oil Reservoir Security", year, reservoirScore);
		updateSeries(teamScore, "National Profit", year, financialScore);

		double aggregateScore = country.getAggregatedScore(year);
		updateSeries(teamScore, "Total Score", year, aggregateScore);
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