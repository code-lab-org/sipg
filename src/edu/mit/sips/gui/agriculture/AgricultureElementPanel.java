package edu.mit.sips.gui.agriculture;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sips.core.agriculture.MutableAgricultureElement;
import edu.mit.sips.gui.DocumentChangeListener;
import edu.mit.sips.gui.ElementPanel;
import edu.mit.sips.scenario.Scenario;

/**
 * The Class AgricultureElementPanel.
 */
public class AgricultureElementPanel extends ElementPanel {
	private static final long serialVersionUID = -9048149807650177253L;
	
	private final JFormattedTextField maxLandAreaText;
	private final JFormattedTextField initialLandAreaText;
	private final JFormattedTextField costIntensityText;
	private final JFormattedTextField laborIntensityText;
	private final JFormattedTextField foodIntensityText;
	private final JFormattedTextField waterIntensityText;
	private final JLabel outputLabel, variableCostLabel, 
			waterUseLabel, laborUseLabel;

	private final JFormattedTextField maxFoodInput;
	private final JFormattedTextField initialFoodInput;
	private final JFormattedTextField distributionEfficiency;
	private final JFormattedTextField variableOperationsCostOfFoodDistribution;
	
	/**
	 * Instantiates a new agriculture element panel.
	 *
	 * @param scenario the scenario
	 * @param element the element
	 */
	public AgricultureElementPanel(Scenario scenario, 
			final MutableAgricultureElement element) {
		super(scenario, element);
		
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
							outputLabel.setText(NumberFormat.getNumberInstance().format(
									element.getFoodIntensityOfLandUsed()
									*element.getInitialLandArea()));
							variableCostLabel.setText(NumberFormat.getNumberInstance().format(
									element.getCostIntensityOfLandUsed()
									*element.getInitialLandArea()));
							waterUseLabel.setText(NumberFormat.getNumberInstance().format(
									element.getWaterIntensityOfLandUsed()
									*element.getInitialLandArea()));
							laborUseLabel.setText(NumberFormat.getNumberInstance().format(
									element.getLaborIntensityOfLandUsed()
									*element.getInitialLandArea()));
							initialLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialLandAreaText.setForeground(Color.red);
						}
					}
				});
		costIntensityText = new JFormattedTextField(NumberFormat.getNumberInstance());
		costIntensityText.setColumns(10);
		costIntensityText.setHorizontalAlignment(JTextField.RIGHT);
		costIntensityText.setValue(element.getInitialLandArea());
		costIntensityText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setCostIntensityOfLandUsed(
									((Number) costIntensityText.getValue()).doubleValue());
							variableCostLabel.setText(NumberFormat.getNumberInstance().format(
									element.getCostIntensityOfLandUsed()
									*element.getInitialLandArea()));
							initialLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialLandAreaText.setForeground(Color.red);
						}
					}
				});
		foodIntensityText = new JFormattedTextField(NumberFormat.getNumberInstance());
		foodIntensityText.setColumns(10);
		foodIntensityText.setHorizontalAlignment(JTextField.RIGHT);
		foodIntensityText.setValue(element.getInitialLandArea());
		foodIntensityText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setFoodIntensityOfLandUsed(
									((Number) foodIntensityText.getValue()).doubleValue());
							outputLabel.setText(NumberFormat.getNumberInstance().format(
									element.getFoodIntensityOfLandUsed()
									*element.getInitialLandArea()));
							initialLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialLandAreaText.setForeground(Color.red);
						}
					}
				});
		laborIntensityText = new JFormattedTextField(NumberFormat.getNumberInstance());
		laborIntensityText.setColumns(10);
		laborIntensityText.setHorizontalAlignment(JTextField.RIGHT);
		laborIntensityText.setValue(element.getInitialLandArea());
		laborIntensityText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setLaborIntensityOfLandUsed(
									((Number) laborIntensityText.getValue()).doubleValue());
							laborUseLabel.setText(NumberFormat.getNumberInstance().format(
									element.getLaborIntensityOfLandUsed()
									*element.getInitialLandArea()));
							initialLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialLandAreaText.setForeground(Color.red);
						}
					}
				});
		waterIntensityText = new JFormattedTextField(NumberFormat.getNumberInstance());
		waterIntensityText.setColumns(10);
		waterIntensityText.setHorizontalAlignment(JTextField.RIGHT);
		waterIntensityText.setValue(element.getInitialLandArea());
		waterIntensityText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setWaterIntensityOfLandUsed(
									((Number) waterIntensityText.getValue()).doubleValue());
							waterUseLabel.setText(NumberFormat.getNumberInstance().format(
									element.getWaterIntensityOfLandUsed()
									*element.getInitialLandArea()));
							initialLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialLandAreaText.setForeground(Color.red);
						}
					}
				});
		outputLabel = new JLabel(NumberFormat.getNumberInstance().format(
				element.getFoodIntensityOfLandUsed()
				*element.getInitialLandArea()), JLabel.RIGHT);
		variableCostLabel = new JLabel(NumberFormat.getNumberInstance().format(
				element.getCostIntensityOfLandUsed()
				*element.getInitialLandArea()), JLabel.RIGHT);
		waterUseLabel = new JLabel(NumberFormat.getNumberInstance().format(
				element.getWaterIntensityOfLandUsed()
				*element.getInitialLandArea()), JLabel.RIGHT);
		laborUseLabel = new JLabel(NumberFormat.getNumberInstance().format(
				element.getLaborIntensityOfLandUsed()
				*element.getInitialLandArea()), JLabel.RIGHT);

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

		if(element.getTemplateName() == null 
				|| scenario.getTemplate(element.getTemplateName()) == null
				|| !scenario.getTemplate(element.getTemplateName()).isTransport()) {
			c.gridx = 0;
			addInput(elementPanel, c, "Max Land Area", 
					maxLandAreaText, "<html>km<sup>2</sup></html>");
			addInput(elementPanel, c, "Initial Land Area",
					initialLandAreaText, "<html>km<sup>2</sup></html>");
			addInput(elementPanel, c, "Food Yield", foodIntensityText, 
					"<html>kcal/day/km<sup>2</sup></html>");
			addInput(elementPanel, c, "Cost Intensity", costIntensityText, 
					"<html>SAR/km<sup>2</sup></html>");
			addInput(elementPanel, c, "Water Intensity", waterIntensityText, 
					"<html>m<sup>3</sup>/km<sup>2</sup></html>");
			addInput(elementPanel, c, "Labor Intensity", laborIntensityText, 
					"<html>people/km<sup>2</sup></html>");
			JPanel productPanel = new JPanel();
			productPanel.setLayout(new GridLayout(4, 3, 2, 2));
			productPanel.add(new JLabel("Initial Food Output"));
			productPanel.add(outputLabel);
			productPanel.add(new JLabel("<html>kcal/day</html>"));
			productPanel.add(new JLabel("Initial Variable Cost"));
			productPanel.add(variableCostLabel);
			productPanel.add(new JLabel("<html>SAR</html>"));
			productPanel.add(new JLabel("Initial Water Use"));
			productPanel.add(waterUseLabel);
			productPanel.add(new JLabel("<html>m<sup>3</sup></html>"));
			productPanel.add(new JLabel("Initial Labor Use"));
			productPanel.add(laborUseLabel);
			productPanel.add(new JLabel("<html>people</html>"));
			addInput(elementPanel, c, "", productPanel, "");
		}
		if(element.getTemplateName() == null 
				|| scenario.getTemplate(element.getTemplateName()) == null
				|| scenario.getTemplate(element.getTemplateName()).isTransport()) {
			c.gridx = 3;
			c.gridy = 0;
			addInput(elementPanel, c, "Max Food Input", 
					maxFoodInput, "kcal/day/year");
			addInput(elementPanel, c, "Initial Food Input", 
					initialFoodInput, "kcal/day/year");
			addInput(elementPanel, c, "Distribution Efficiency", 
					distributionEfficiency, "kcal/day out/kcal/day in");
			addInput(elementPanel, c, "Operations Cost of Distribution", 
					variableOperationsCostOfFoodDistribution, "SAR/kcal/day");
		}

		// set input enabled state
		maxLandAreaText.setEnabled(element.getTemplateName() == null);
		maxFoodInput.setEnabled(element.getTemplateName() == null);
		costIntensityText.setEnabled(element.getTemplateName() == null);
		foodIntensityText.setEnabled(element.getTemplateName() == null);
		laborIntensityText.setEnabled(element.getTemplateName() == null);
		waterIntensityText.setEnabled(element.getTemplateName() == null);
		distributionEfficiency.setEnabled(element.getTemplateName() == null);
		variableOperationsCostOfFoodDistribution.setEnabled(element.getTemplateName() == null);
	}
}
