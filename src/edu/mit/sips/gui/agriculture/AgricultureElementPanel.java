package edu.mit.sips.gui.agriculture;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sips.core.City;
import edu.mit.sips.core.agriculture.AgricultureProduct;
import edu.mit.sips.core.agriculture.MutableAgricultureElement;
import edu.mit.sips.gui.DocumentChangeListener;
import edu.mit.sips.gui.ElementPanel;

/**
 * The Class AgricultureElementPanel.
 */
public class AgricultureElementPanel extends ElementPanel {
	private static final long serialVersionUID = -9048149807650177253L;
	
	private final JTextField maxLandAreaText, initialLandAreaText;
	private final JTextField maxFoodInput, initialFoodInput;
	private final JComboBox productCombo;
	private final JLabel productOutput, productVariableCost, productWaterUse, productLaborUse;
	private final JTextField distributionEfficiency;
	private final JTextField variableOperationsCostOfFoodDistribution;
	
	/**
	 * Instantiates a new agriculture element panel.
	 *
	 * @param city the city
	 * @param element the element
	 */
	public AgricultureElementPanel(City city, 
			final MutableAgricultureElement element) {
		super(city, element);
		
		maxLandAreaText = new JTextField(10);
		maxLandAreaText.setText(
				new Double(element.getMaxLandArea()).toString());
		maxLandAreaText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxLandArea(Double.parseDouble(
									maxLandAreaText.getText()));
							maxLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxLandAreaText.setForeground(Color.red);
						}
					}
				});
		initialLandAreaText = new JTextField(10);
		initialLandAreaText.setText(
				new Double(element.getInitialLandArea()).toString());
		initialLandAreaText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialLandArea(Double.parseDouble(
									initialLandAreaText.getText()));
							initialLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialLandAreaText.setForeground(Color.red);
						}
					}
				});
		productCombo = new JComboBox(AgricultureProduct.values());
		productCombo.setSelectedItem(element.getProduct());
		productCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(productCombo.getSelectedItem() instanceof AgricultureProduct) {
					element.setProduct((AgricultureProduct)productCombo.getSelectedItem());
				}
				productOutput.setText(new Double(
						element.getProduct().getFoodIntensityOfLandUsed()).toString());
				productVariableCost.setText(new Double(
						element.getProduct().getCostIntensityOfLandUsed()).toString());
				productWaterUse.setText(new Double(
						element.getProduct().getWaterIntensityOfLandUsed()).toString());
				productLaborUse.setText(new Double(
						element.getProduct().getLaborIntensityOfLandUsed()).toString());
			}
		});
		productOutput = new JLabel(new Double(
				element.getProduct().getFoodIntensityOfLandUsed()).toString());
		productVariableCost = new JLabel(new Double(
				element.getProduct().getCostIntensityOfLandUsed()).toString());
		productWaterUse = new JLabel(new Double(
				element.getProduct().getWaterIntensityOfLandUsed()).toString());
		productLaborUse = new JLabel(new Double(
				element.getProduct().getLaborIntensityOfLandUsed()).toString());

		maxFoodInput = new JTextField(10);
		maxFoodInput.setText(
				new Double(element.getMaxFoodInput()).toString());
		maxFoodInput.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxFoodInput(Double.parseDouble(
									maxFoodInput.getText()));
							maxFoodInput.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxFoodInput.setForeground(Color.red);
						}
					}
				});
		initialFoodInput = new JTextField(10);
		initialFoodInput.setText(
				new Double(element.getInitialFoodInput()).toString());
		initialFoodInput.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialFoodInput(Double.parseDouble(
									initialFoodInput.getText()));
							initialFoodInput.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialFoodInput.setForeground(Color.red);
						}
					}
				});
		distributionEfficiency = new JTextField(10);
		distributionEfficiency.setText(
				new Double(element.getDistributionEfficiency()).toString());
		distributionEfficiency.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setDistributionEfficiency(Double.parseDouble(
									distributionEfficiency.getText()));
							distributionEfficiency.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							distributionEfficiency.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfFoodDistribution = new JTextField(10);
		variableOperationsCostOfFoodDistribution.setText(new Double(
				element.getVariableOperationsCostOfFoodDistribution()).toString());
		variableOperationsCostOfFoodDistribution.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfFoodDistribution(Double.parseDouble(
									variableOperationsCostOfFoodDistribution.getText()));
							variableOperationsCostOfFoodDistribution.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfFoodDistribution.setForeground(Color.red);
						}
					}
				});

		JPanel elementPanel = new JPanel();
		elementPanel.setLayout(new GridBagLayout());
		add(elementPanel);

		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);


		if(element.getTemplate() == null 
				|| !element.getTemplate().isTransport()) {
			c.gridx = 0;
			addInput(elementPanel, c, 
					"<html>Max Land Area (km<sup>2</sup>)</html>", 
					maxLandAreaText);
			addInput(elementPanel, c, 
					"<html>Initial Land Area (km<sup>2</sup>)</html>",
					initialLandAreaText);
			addInput(elementPanel, c, "Product", productCombo);
			JPanel productPanel = new JPanel();
			productPanel.setLayout(new GridLayout(4,2));
			productPanel.add(new JLabel("<html>Food Output (GJ/km<sup>2</sup>)</html>"));
			productPanel.add(productOutput);
			productPanel.add(new JLabel("<html>Variable Cost (SAR/km<sup>2</sup>)</html>"));
			productPanel.add(productVariableCost);
			productPanel.add(new JLabel("<html>Water Use (m<sup>3</sup>/km<sup>2</sup>)</html>"));
			productPanel.add(productWaterUse);
			productPanel.add(new JLabel("<html>Labor Use (person/km<sup>2</sup>)</html>"));
			productPanel.add(productLaborUse);
			addInput(elementPanel, c, "", productPanel);
		}
		if(element.getTemplate() == null 
				|| element.getTemplate().isTransport()) {
			c.gridx = 2;
			c.gridy = 0;
			addInput(elementPanel, c, 
					"Max Food Input (GJ/year)", maxFoodInput);
			addInput(elementPanel, c, 
					"Initial Food Input (GJ/year)", initialFoodInput);
			addInput(elementPanel, c, 
					"Distribution Efficiency (GJ out/GJ in)", 
					distributionEfficiency);
			addInput(elementPanel, c, 
					"Operations Cost of Distribution (SAR/GJ)", 
					variableOperationsCostOfFoodDistribution);
		}

		// set input enabled state
		maxLandAreaText.setEnabled(element.getTemplate() == null);
		maxFoodInput.setEnabled(element.getTemplate() == null);
		initialFoodInput.setEnabled(element.getTemplate() == null);
		productCombo.setEnabled(element.getTemplate() == null);
		productOutput.setEnabled(element.getTemplate() == null);
		productVariableCost.setEnabled(element.getTemplate() == null);
		productWaterUse.setEnabled(element.getTemplate() == null);
		productLaborUse.setEnabled(element.getTemplate() == null);
		distributionEfficiency.setEnabled(element.getTemplate() == null);
		variableOperationsCostOfFoodDistribution.setEnabled(element.getTemplate() == null);
	}
}
