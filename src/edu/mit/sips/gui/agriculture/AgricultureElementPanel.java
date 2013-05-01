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
import javax.swing.JTextField;

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
		maxLandAreaText.setHorizontalAlignment(JTextField.RIGHT);
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
		initialLandAreaText.setHorizontalAlignment(JTextField.RIGHT);
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
				element.getProduct().getFoodIntensityOfLandUsed()), JLabel.RIGHT);
		productVariableCost = new JLabel(NumberFormat.getNumberInstance().format(
				element.getProduct().getCostIntensityOfLandUsed()), JLabel.RIGHT);
		productWaterUse = new JLabel(NumberFormat.getNumberInstance().format(
				element.getProduct().getWaterIntensityOfLandUsed()), JLabel.RIGHT);
		productLaborUse = new JLabel(NumberFormat.getNumberInstance().format(
				element.getProduct().getLaborIntensityOfLandUsed()), JLabel.RIGHT);

		maxFoodInput = new JFormattedTextField(NumberFormat.getNumberInstance());
		maxFoodInput.setColumns(10);
		maxFoodInput.setHorizontalAlignment(JTextField.RIGHT);
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
		initialFoodInput.setHorizontalAlignment(JTextField.RIGHT);
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
		distributionEfficiency.setHorizontalAlignment(JTextField.RIGHT);
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
		variableOperationsCostOfFoodDistribution.setHorizontalAlignment(JTextField.RIGHT);
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
			addInput(elementPanel, c, "Max Land Area", 
					maxLandAreaText, "<html>km<sup>2</sup></html>");
			addInput(elementPanel, c, "Initial Land Area",
					initialLandAreaText, "<html>km<sup>2</sup></html>");
			addInput(elementPanel, c, "Product", productCombo, "");
			JPanel productPanel = new JPanel();
			productPanel.setLayout(new GridLayout(4, 3, 2, 2));
			productPanel.add(new JLabel("Food Output"));
			productPanel.add(productOutput);
			productPanel.add(new JLabel("<html>GJ/km<sup>2</sup></html>"));
			productPanel.add(new JLabel("Variable Cost"));
			productPanel.add(productVariableCost);
			productPanel.add(new JLabel("<html>SAR/km<sup>2</sup></html>"));
			productPanel.add(new JLabel("Water Use"));
			productPanel.add(productWaterUse);
			productPanel.add(new JLabel("<html>m<sup>3</sup>/km<sup>2</sup></html>"));
			productPanel.add(new JLabel("Labor Use"));
			productPanel.add(productLaborUse);
			productPanel.add(new JLabel("<html>people/km<sup>2</sup></html>"));
			addInput(elementPanel, c, "", productPanel, "");
		}
		if(element.getTemplate() == null 
				|| element.getTemplate().isTransport()) {
			c.gridx = 3;
			c.gridy = 0;
			addInput(elementPanel, c, "Max Food Input", 
					maxFoodInput, "GJ/year");
			addInput(elementPanel, c, "Initial Food Input", 
					initialFoodInput, "GJ/year");
			addInput(elementPanel, c, "Distribution Efficiency", 
					distributionEfficiency, "GJ out/GJ in");
			addInput(elementPanel, c, "Operations Cost of Distribution", 
					variableOperationsCostOfFoodDistribution, "SAR/GJ");
		}

		// set input enabled state
		maxLandAreaText.setEnabled(element.getTemplate() == null);
		maxFoodInput.setEnabled(element.getTemplate() == null);
		productCombo.setEnabled(element.getTemplate() == null);
		distributionEfficiency.setEnabled(element.getTemplate() == null);
		variableOperationsCostOfFoodDistribution.setEnabled(element.getTemplate() == null);
	}
}
