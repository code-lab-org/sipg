package edu.mit.sips.gui.energy;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sips.core.City;
import edu.mit.sips.core.energy.MutableElectricityElement;
import edu.mit.sips.gui.DocumentChangeListener;
import edu.mit.sips.gui.ElementPanel;

/**
 * The Class ElectricityElementPanel.
 */
public class ElectricityElementPanel extends ElementPanel {
	private static final long serialVersionUID = -9048149807650177253L;
	
	private final JTextField maxElectricityProductionText;
	private final JTextField initialElectricityProductionText;
	private final JTextField petroleumIntensityOfElectricityProductionText;
	private final JTextField waterIntensityOfElectricityProductionText;
	private final JTextField variableOperationsCostOfElectricityProductionText;
	
	private final JTextField maxElectricityInputText;
	private final JTextField initialElectricityInputText;
	private final JTextField distributionEfficiencyText;
	private final JTextField variableOperationsCostOfElectricityDistributionText;
	
	/**
	 * Instantiates a new electricity element panel.
	 *
	 * @param city the city
	 * @param element the element
	 */
	public ElectricityElementPanel(City city, 
			final MutableElectricityElement element) {
		super(city, element);
		
		JPanel elementPanel = new JPanel();
		elementPanel.setLayout(new GridBagLayout());
		add(elementPanel);
		

		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);

		c.gridx = 0;
		maxElectricityProductionText = new JTextField(10);
		maxElectricityProductionText.setText(
				new Double(element.getMaxElectricityProduction()).toString());
		maxElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxElectricityProduction(Double.parseDouble(
									maxElectricityProductionText.getText()));
							maxElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Max Electricity Production (MWh/year)", 
				maxElectricityProductionText);
		initialElectricityProductionText = new JTextField(10);
		initialElectricityProductionText.setText(
				new Double(element.getInitialElectricityProduction()).toString());
		initialElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialElectricityProduction(Double.parseDouble(
									initialElectricityProductionText.getText()));
							initialElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Initial Electricity Production (MWh/year)",
				initialElectricityProductionText);
		petroleumIntensityOfElectricityProductionText = new JTextField(10);
		petroleumIntensityOfElectricityProductionText.setText(
				new Double(element.getPetroleumIntensityOfElectricityProduction()).toString());
		petroleumIntensityOfElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setPetroleumIntensityOfElectricityProduction(Double.parseDouble(
									petroleumIntensityOfElectricityProductionText.getText()));
							petroleumIntensityOfElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							petroleumIntensityOfElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Petroleum Intensity of Production (bbl/MWh)",
				petroleumIntensityOfElectricityProductionText);
		waterIntensityOfElectricityProductionText = new JTextField(10);
		waterIntensityOfElectricityProductionText.setText(
				new Double(element.getPetroleumIntensityOfElectricityProduction()).toString());
		waterIntensityOfElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setPetroleumIntensityOfElectricityProduction(Double.parseDouble(
									waterIntensityOfElectricityProductionText.getText()));
							waterIntensityOfElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							waterIntensityOfElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"<html>Water Intensity of Production (m<sup>3</sup>/MWh)</html>",
				waterIntensityOfElectricityProductionText);
		variableOperationsCostOfElectricityProductionText = new JTextField(10);
		variableOperationsCostOfElectricityProductionText.setText(
				new Double(element.getVariableOperationsCostOfElectricityProduction()).toString());
		variableOperationsCostOfElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfElectricityProduction(Double.parseDouble(
									variableOperationsCostOfElectricityProductionText.getText()));
							variableOperationsCostOfElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Variable Cost of Production (SAR/MWh)",
				variableOperationsCostOfElectricityProductionText);
		
		c.gridx = 2;
		c.gridy = 0;
		maxElectricityInputText = new JTextField(10);
		maxElectricityInputText.setText(
				new Double(element.getMaxElectricityInput()).toString());
		maxElectricityInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxElectricityInput(Double.parseDouble(
									maxElectricityInputText.getText()));
							maxElectricityInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxElectricityInputText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Max Electricity Input (MWh/year)", 
				maxElectricityInputText);
		initialElectricityInputText = new JTextField(10);
		initialElectricityInputText.setText(
				new Double(element.getInitialElectricityInput()).toString());
		initialElectricityInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialElectricityInput(Double.parseDouble(
									initialElectricityInputText.getText()));
							initialElectricityInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialElectricityInputText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Initial Electricity Input (MWh/year)",
				initialElectricityInputText);
		distributionEfficiencyText = new JTextField(10);
		distributionEfficiencyText.setText(
				new Double(element.getDistributionEfficiency()).toString());
		distributionEfficiencyText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setDistributionEfficiency(Double.parseDouble(
									distributionEfficiencyText.getText()));
							distributionEfficiencyText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							distributionEfficiencyText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Distribution Efficiency (MWh out/MWh in)",
				distributionEfficiencyText);
		variableOperationsCostOfElectricityDistributionText = new JTextField(10);
		variableOperationsCostOfElectricityDistributionText.setText(
				new Double(element.getVariableOperationsCostOfElectricityDistribution()).toString());
		variableOperationsCostOfElectricityDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfElectricityDistribution(Double.parseDouble(
									variableOperationsCostOfElectricityDistributionText.getText()));
							variableOperationsCostOfElectricityDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfElectricityDistributionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Variable Cost of Distribution (SAR/MWh)",
				variableOperationsCostOfElectricityDistributionText);
		
		setTemplateMode(element.getTemplate() != null);
	}
	
}
