package edu.mit.sips.gui.agriculture;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.AgricultureProduct;
import edu.mit.sips.core.agriculture.MutableAgricultureElement;
import edu.mit.sips.gui.DocumentChangeListener;
import edu.mit.sips.gui.ElementPanel;

/**
 * The Class AgricultureElementPanel.
 */
public class AgricultureElementPanel extends ElementPanel {
	private static final long serialVersionUID = -9048149807650177253L;
	
	private final JFormattedTextField maxLandAreaText;
	private final JFormattedTextField initialLandAreaText;
	private final JComboBox productCombo;
	private final JLabel productOutput, productVariableCost, productWaterUse, productLaborUse;

	private final JFormattedTextField maxFoodInput;
	private final JFormattedTextField initialFoodInput;
	private final JFormattedTextField distributionEfficiency;
	private final JFormattedTextField variableOperationsCostOfFoodDistribution;
	
	/**
	 * Instantiates a new agriculture element panel.
	 *
	 * @param country the country
	 * @param element the element
	 */
	public AgricultureElementPanel(Country country, 
			final MutableAgricultureElement element) {
		super(country, element);
		
		maxLandAreaText = new JFormattedTextField(NumberFormat.getNumberInstance());
		maxLandAreaText.setColumns(10);
		maxLandAreaText.setValue(element.getMaxLandArea());
		maxLandAreaText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxLandArea(
									((Number) maxLandAreaText.getValue()).doubleValue());
							maxLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxLandAreaText.setForeground(Color.red);
						}
					}
				});
		initialLandAreaText = new JFormattedTextField(NumberFormat.getNumberInstance());
		initialLandAreaText.setColumns(10);
		initialLandAreaText.setValue(element.getInitialLandArea());
		initialLandAreaText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialLandArea(
									((Number) initialLandAreaText.getValue()).doubleValue());
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
				productOutput.setText(NumberFormat.getNumberInstance().format(
						element.getProduct().getFoodIntensityOfLandUsed()));
				productVariableCost.setText(NumberFormat.getNumberInstance().format(
						element.getProduct().getCostIntensityOfLandUsed()));
				productWaterUse.setText(NumberFormat.getNumberInstance().format(
						element.getProduct().getWaterIntensityOfLandUsed()));
				productLaborUse.setText(NumberFormat.getNumberInstance().format(
						element.getProduct().getLaborIntensityOfLandUsed()));
			}
		});
		productOutput = new JLabel(NumberFormat.getNumberInstance().format(
				element.getProduct().getFoodIntensityOfLandUsed()));
		productVariableCost = new JLabel(NumberFormat.getNumberInstance().format(
				element.getProduct().getCostIntensityOfLandUsed()));
		productWaterUse = new JLabel(NumberFormat.getNumberInstance().format(
				element.getProduct().getWaterIntensityOfLandUsed()));
		productLaborUse = new JLabel(NumberFormat.getNumberInstance().format(
				element.getProduct().getLaborIntensityOfLandUsed()));

		maxFoodInput = new JFormattedTextField(NumberFormat.getNumberInstance());
		maxFoodInput.setColumns(10);
		maxFoodInput.setValue(element.getMaxFoodInput());
		maxFoodInput.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxFoodInput(
									((Number) maxFoodInput.getValue()).doubleValue());
							maxFoodInput.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxFoodInput.setForeground(Color.red);
						}
					}
				});
		initialFoodInput = new JFormattedTextField(NumberFormat.getNumberInstance());
		initialFoodInput.setColumns(10);
		initialFoodInput.setValue(element.getInitialFoodInput());
		initialFoodInput.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialFoodInput(
									((Number) initialFoodInput.getValue()).doubleValue());
							initialFoodInput.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialFoodInput.setForeground(Color.red);
						}
					}
				});
		distributionEfficiency = new JFormattedTextField(NumberFormat.getNumberInstance());
		distributionEfficiency.setColumns(10);
		distributionEfficiency.setValue(element.getDistributionEfficiency());
		distributionEfficiency.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setDistributionEfficiency(
									((Number) distributionEfficiency.getValue()).doubleValue());
							distributionEfficiency.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							distributionEfficiency.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfFoodDistribution = new JFormattedTextField(
				NumberFormat.getNumberInstance());
		variableOperationsCostOfFoodDistribution.setColumns(10);
		variableOperationsCostOfFoodDistribution.setValue(
				element.getVariableOperationsCostOfFoodDistribution());
		variableOperationsCostOfFoodDistribution.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfFoodDistribution(
									((Number) variableOperationsCostOfFoodDistribution.getValue()).doubleValue());
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
		productCombo.setEnabled(element.getTemplate() == null);
		productOutput.setEnabled(element.getTemplate() == null);
		productVariableCost.setEnabled(element.getTemplate() == null);
		productWaterUse.setEnabled(element.getTemplate() == null);
		productLaborUse.setEnabled(element.getTemplate() == null);
		distributionEfficiency.setEnabled(element.getTemplate() == null);
		variableOperationsCostOfFoodDistribution.setEnabled(element.getTemplate() == null);
	}
}
