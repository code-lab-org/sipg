package edu.mit.sips.gui.petroleum;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sips.core.petroleum.MutablePetroleumElement;
import edu.mit.sips.gui.DocumentChangeListener;
import edu.mit.sips.gui.ElementPanel;
import edu.mit.sips.scenario.Scenario;

/**
 * The Class PetroleumElementPanel.
 */
public class PetroleumElementPanel extends ElementPanel {
	private static final long serialVersionUID = -9048149807650177253L;
	
	private final JFormattedTextField maxPetroleumProductionText;
	private final JFormattedTextField initialPetroleumProductionText;
	private final JFormattedTextField reservoirIntensityOfPetroleumProductionText;
	private final JFormattedTextField variableOperationsCostOfPetroleumProductionText;
	
	private final JFormattedTextField maxPetroleumInputText;
	private final JFormattedTextField initialPetroleumInputText;
	private final JFormattedTextField distributionEfficiencyText;
	private final JFormattedTextField electricalIntensityOfPetroleumDistributionText;
	private final JFormattedTextField variableOperationsCostOfPetroleumDistributionText;
	
	/**
	 * Instantiates a new petroleum element panel.
	 *
	 * @param scenario the scenario
	 * @param element the element
	 */
	public PetroleumElementPanel(Scenario scenario, 
			final MutablePetroleumElement element) {
		super(scenario, element);
		
		maxPetroleumProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		maxPetroleumProductionText.setColumns(10);
		maxPetroleumProductionText.setHorizontalAlignment(JTextField.RIGHT);
		maxPetroleumProductionText.setValue(
				element.getMaxPetroleumProduction());
		maxPetroleumProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxPetroleumProduction(
									((Number) maxPetroleumProductionText.getValue()).doubleValue());
							maxPetroleumProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxPetroleumProductionText.setForeground(Color.red);
						}
					}
				});
		initialPetroleumProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		initialPetroleumProductionText.setColumns(10);
		initialPetroleumProductionText.setHorizontalAlignment(JTextField.RIGHT);
		initialPetroleumProductionText.setValue(
				element.getInitialPetroleumProduction());
		initialPetroleumProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialPetroleumProduction(
									((Number) initialPetroleumProductionText.getValue()).doubleValue());
							initialPetroleumProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialPetroleumProductionText.setForeground(Color.red);
						}
					}
				});
		reservoirIntensityOfPetroleumProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		reservoirIntensityOfPetroleumProductionText.setColumns(10);
		reservoirIntensityOfPetroleumProductionText.setHorizontalAlignment(JTextField.RIGHT);
		reservoirIntensityOfPetroleumProductionText.setValue(
				element.getReservoirIntensityOfPetroleumProduction());
		reservoirIntensityOfPetroleumProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setReservoirIntensityOfPetroleumProduction(
									((Number) reservoirIntensityOfPetroleumProductionText.getValue()).doubleValue());
							reservoirIntensityOfPetroleumProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							reservoirIntensityOfPetroleumProductionText.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfPetroleumProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		variableOperationsCostOfPetroleumProductionText.setColumns(10);
		variableOperationsCostOfPetroleumProductionText.setHorizontalAlignment(JTextField.RIGHT);
		variableOperationsCostOfPetroleumProductionText.setValue(
				element.getVariableOperationsCostOfPetroleumProduction());
		variableOperationsCostOfPetroleumProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfPetroleumProduction(
									((Number) variableOperationsCostOfPetroleumProductionText.getValue()).doubleValue());
							variableOperationsCostOfPetroleumProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfPetroleumProductionText.setForeground(Color.red);
						}
					}
				});
		maxPetroleumInputText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		maxPetroleumInputText.setColumns(10);
		maxPetroleumInputText.setHorizontalAlignment(JTextField.RIGHT);
		maxPetroleumInputText.setValue(
				element.getMaxPetroleumInput());
		maxPetroleumInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxPetroleumInput(
									((Number) maxPetroleumInputText.getValue()).doubleValue());
							maxPetroleumInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxPetroleumInputText.setForeground(Color.red);
						}
					}
				});
		initialPetroleumInputText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		initialPetroleumInputText.setColumns(10);
		initialPetroleumInputText.setHorizontalAlignment(JTextField.RIGHT);
		initialPetroleumInputText.setValue(
				element.getInitialPetroleumInput());
		initialPetroleumInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialPetroleumInput(
									((Number) initialPetroleumInputText.getValue()).doubleValue());
							initialPetroleumInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialPetroleumInputText.setForeground(Color.red);
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
		electricalIntensityOfPetroleumDistributionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		electricalIntensityOfPetroleumDistributionText.setColumns(10);
		electricalIntensityOfPetroleumDistributionText.setHorizontalAlignment(JTextField.RIGHT);
		electricalIntensityOfPetroleumDistributionText.setValue(
				element.getElectricalIntensityOfPetroleumDistribution());
		electricalIntensityOfPetroleumDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setElectricalIntensityOfPetroleumDistribution(
									((Number) electricalIntensityOfPetroleumDistributionText.getValue()).doubleValue());
							electricalIntensityOfPetroleumDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							electricalIntensityOfPetroleumDistributionText.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfPetroleumDistributionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		variableOperationsCostOfPetroleumDistributionText.setColumns(10);
		variableOperationsCostOfPetroleumDistributionText.setHorizontalAlignment(JTextField.RIGHT);
		variableOperationsCostOfPetroleumDistributionText.setValue(
				element.getVariableOperationsCostOfPetroleumDistribution());
		variableOperationsCostOfPetroleumDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfPetroleumDistribution(
									((Number) variableOperationsCostOfPetroleumDistributionText.getValue()).doubleValue());
							variableOperationsCostOfPetroleumDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfPetroleumDistributionText.setForeground(Color.red);
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
			addInput(elementPanel, c, "Max Petroleum Production", 
					maxPetroleumProductionText, "toe/year");
			addInput(elementPanel, c, "Initial Petroleum Production",
					initialPetroleumProductionText,  " toe/year");
			addInput(elementPanel, c, "Reservoir Intensity of Production",
					reservoirIntensityOfPetroleumProductionText, "toe/toe");
			addInput(elementPanel, c, "Variable Cost of Production",
					variableOperationsCostOfPetroleumProductionText, "SAR/toe");
		}
		if(element.getTemplateName() == null 
				|| scenario.getTemplate(element.getTemplateName()) == null
				|| scenario.getTemplate(element.getTemplateName()).isTransport()) {
			c.gridx = 3;
			c.gridy = 0;
			addInput(elementPanel, c, "Max Petroleum Input", 
					maxPetroleumInputText, "toe/year");
			addInput(elementPanel, c, "Initial Petroleum Input",
					initialPetroleumInputText, "toe/year");
			addInput(elementPanel, c, "Distribution Efficiency",
					distributionEfficiencyText, "toe out/toe in");
			addInput(elementPanel, c, "Electrical Intensity of Distribution",
					electricalIntensityOfPetroleumDistributionText, "toe/toe");
			addInput(elementPanel, c, "Variable Cost of Distribution",
					variableOperationsCostOfPetroleumDistributionText, "SAR/toe");
		}
		
		// set input enabled state
		maxPetroleumProductionText.setEnabled(element.getTemplateName() == null);
		reservoirIntensityOfPetroleumProductionText.setEnabled(element.getTemplateName() == null);
		variableOperationsCostOfPetroleumProductionText.setEnabled(element.getTemplateName() == null);
		maxPetroleumInputText.setEnabled(element.getTemplateName() == null);
		distributionEfficiencyText.setEnabled(element.getTemplateName() == null);
		electricalIntensityOfPetroleumDistributionText.setEnabled(element.getTemplateName() == null);
		variableOperationsCostOfPetroleumDistributionText.setEnabled(element.getTemplateName() == null);
	}
	
}
