package edu.mit.sips.gui.electricity;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.electricity.MutableElectricityElement;
import edu.mit.sips.gui.DocumentChangeListener;
import edu.mit.sips.gui.ElementPanel;

/**
 * The Class ElectricityElementPanel.
 */
public class ElectricityElementPanel extends ElementPanel {
	private static final long serialVersionUID = -9048149807650177253L;
	
	private final JFormattedTextField maxElectricityProductionText;
	private final JFormattedTextField initialElectricityProductionText;
	private final JFormattedTextField petroleumIntensityOfElectricityProductionText;
	private final JFormattedTextField waterIntensityOfElectricityProductionText;
	private final JFormattedTextField variableOperationsCostOfElectricityProductionText;
	
	private final JFormattedTextField maxElectricityInputText;
	private final JFormattedTextField initialElectricityInputText;
	private final JFormattedTextField distributionEfficiencyText;
	private final JFormattedTextField variableOperationsCostOfElectricityDistributionText;
	
	/**
	 * Instantiates a new electricity element panel.
	 *
	 * @param country the country
	 * @param element the element
	 */
	public ElectricityElementPanel(Country country, 
			final MutableElectricityElement element) {
		super(country, element);
		
		maxElectricityProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		maxElectricityProductionText.setColumns(10);
		maxElectricityProductionText.setHorizontalAlignment(JTextField.RIGHT);
		maxElectricityProductionText.setValue(
				element.getMaxElectricityProduction());
		maxElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxElectricityProduction(
									((Number) maxElectricityProductionText.getValue()).doubleValue());
							maxElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		initialElectricityProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		initialElectricityProductionText.setColumns(10);
		initialElectricityProductionText.setHorizontalAlignment(JTextField.RIGHT);
		initialElectricityProductionText.setValue(
				element.getInitialElectricityProduction());
		initialElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialElectricityProduction(
									((Number) initialElectricityProductionText.getValue()).doubleValue());
							initialElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		petroleumIntensityOfElectricityProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		petroleumIntensityOfElectricityProductionText.setColumns(10);
		petroleumIntensityOfElectricityProductionText.setHorizontalAlignment(JTextField.RIGHT);
		petroleumIntensityOfElectricityProductionText.setValue(
				element.getPetroleumIntensityOfElectricityProduction());
		petroleumIntensityOfElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setPetroleumIntensityOfElectricityProduction(
									((Number) petroleumIntensityOfElectricityProductionText.getValue()).doubleValue());
							petroleumIntensityOfElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							petroleumIntensityOfElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		waterIntensityOfElectricityProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		waterIntensityOfElectricityProductionText.setColumns(10);
		waterIntensityOfElectricityProductionText.setHorizontalAlignment(JTextField.RIGHT);
		waterIntensityOfElectricityProductionText.setValue(
				element.getPetroleumIntensityOfElectricityProduction());
		waterIntensityOfElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setPetroleumIntensityOfElectricityProduction(
									((Number) waterIntensityOfElectricityProductionText.getValue()).doubleValue());
							waterIntensityOfElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							waterIntensityOfElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfElectricityProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		variableOperationsCostOfElectricityProductionText.setColumns(10);
		variableOperationsCostOfElectricityProductionText.setHorizontalAlignment(JTextField.RIGHT);
		variableOperationsCostOfElectricityProductionText.setValue(
				element.getVariableOperationsCostOfElectricityProduction());
		variableOperationsCostOfElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfElectricityProduction(
									((Number) variableOperationsCostOfElectricityProductionText.getValue()).doubleValue());
							variableOperationsCostOfElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		maxElectricityInputText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		maxElectricityInputText.setColumns(10);
		maxElectricityInputText.setHorizontalAlignment(JTextField.RIGHT);
		maxElectricityInputText.setValue(
				element.getMaxElectricityInput());
		maxElectricityInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxElectricityInput(
									((Number) maxElectricityInputText.getValue()).doubleValue());
							maxElectricityInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxElectricityInputText.setForeground(Color.red);
						}
					}
				});
		initialElectricityInputText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		initialElectricityInputText.setColumns(10);
		initialElectricityInputText.setHorizontalAlignment(JTextField.RIGHT);
		initialElectricityInputText.setValue(
				element.getInitialElectricityInput());
		initialElectricityInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialElectricityInput(
									((Number) initialElectricityInputText.getValue()).doubleValue());
							initialElectricityInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialElectricityInputText.setForeground(Color.red);
						}
					}
				});
		distributionEfficiencyText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		distributionEfficiencyText.setColumns(10);
		distributionEfficiencyText.setHorizontalAlignment(JTextField.RIGHT);
		distributionEfficiencyText.setValue(
				element.getDistributionEfficiency());
		distributionEfficiencyText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setDistributionEfficiency(
									((Number) distributionEfficiencyText.getValue()).doubleValue());
							distributionEfficiencyText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							distributionEfficiencyText.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfElectricityDistributionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		variableOperationsCostOfElectricityDistributionText.setColumns(10);
		variableOperationsCostOfElectricityDistributionText.setHorizontalAlignment(JTextField.RIGHT);
		variableOperationsCostOfElectricityDistributionText.setValue(
				element.getVariableOperationsCostOfElectricityDistribution());
		variableOperationsCostOfElectricityDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfElectricityDistribution(
									((Number) variableOperationsCostOfElectricityDistributionText.getValue()).doubleValue());
							variableOperationsCostOfElectricityDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfElectricityDistributionText.setForeground(Color.red);
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
				addInput(elementPanel, c, "Max Electricity Production", 
						maxElectricityProductionText, "MWh/year");
				addInput(elementPanel, c, "Initial Electricity Production",
						initialElectricityProductionText, "MWh/year");
				addInput(elementPanel, c, "Petroleum Intensity of Production",
						petroleumIntensityOfElectricityProductionText, "bbl/MWh");
				addInput(elementPanel, c, "Water Intensity of Production",
						waterIntensityOfElectricityProductionText, "<html>m<sup>3</sup>/MWh</html>");
				addInput(elementPanel, c, "Variable Cost of Production",
						variableOperationsCostOfElectricityProductionText, "SAR/MWh");
		}
		if(element.getTemplate() == null 
				|| element.getTemplate().isTransport()) {
			c.gridx = 3;
			c.gridy = 0;
			addInput(elementPanel, c, "Max Electricity Input", 
					maxElectricityInputText, "MWh/year");
			addInput(elementPanel, c, "Initial Electricity Input",
					initialElectricityInputText, "MWh/year");
			addInput(elementPanel, c, "Distribution Efficiency",
					distributionEfficiencyText, "MWh out/MWh in");
			addInput(elementPanel, c, "Variable Cost of Distribution",
					variableOperationsCostOfElectricityDistributionText, " SAR/MWh");
		}

		// set input enabled state
		maxElectricityProductionText.setEnabled(element.getTemplate() == null);
		petroleumIntensityOfElectricityProductionText.setEnabled(element.getTemplate() == null);
		waterIntensityOfElectricityProductionText.setEnabled(element.getTemplate() == null);
		variableOperationsCostOfElectricityProductionText.setEnabled(element.getTemplate() == null);
		maxElectricityInputText.setEnabled(element.getTemplate() == null);
		distributionEfficiencyText.setEnabled(element.getTemplate() == null);
		variableOperationsCostOfElectricityDistributionText.setEnabled(element.getTemplate() == null);
	}
	
}
