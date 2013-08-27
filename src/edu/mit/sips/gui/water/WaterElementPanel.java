package edu.mit.sips.gui.water;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.water.MutableWaterElement;
import edu.mit.sips.gui.DocumentChangeListener;
import edu.mit.sips.gui.ElementPanel;

/**
 * The Class WaterElementPanel.
 */
public class WaterElementPanel extends ElementPanel {
	private static final long serialVersionUID = -9048149807650177253L;
	
	private final JFormattedTextField maxWaterProductionText;
	private final JFormattedTextField initialWaterProductionText;
	private final JFormattedTextField reservoirIntensityOfWaterProductionText;
	private final JFormattedTextField electricalIntensityOfWaterProductionText;
	private final JFormattedTextField variableOperationsCostOfWaterProductionText;

	private final JFormattedTextField maxWaterInputText;
	private final JFormattedTextField initialWaterInputText;
	private final JFormattedTextField distributionEfficiencyText;
	private final JFormattedTextField electricalIntensityOfWaterDistributionText;
	private final JFormattedTextField variableOperationsCostOfWaterDistributionText;
	
	/**
	 * Instantiates a new water element panel.
	 *
	 * @param country the country
	 * @param element the element
	 */
	public WaterElementPanel(Country country, 
			final MutableWaterElement element) {
		super(country, element);
		
		maxWaterProductionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		maxWaterProductionText.setColumns(10);
		maxWaterProductionText.setHorizontalAlignment(JTextField.RIGHT);
		maxWaterProductionText.setValue(element.getMaxWaterProduction());
		maxWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxWaterProduction(
									((Number) maxWaterProductionText.getValue()).doubleValue());
							maxWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxWaterProductionText.setForeground(Color.red);
						}
					}
				});
		initialWaterProductionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		initialWaterProductionText.setColumns(10);
		initialWaterProductionText.setHorizontalAlignment(JTextField.RIGHT);
		initialWaterProductionText.setValue(element.getInitialWaterProduction());
		initialWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialWaterProduction(
									((Number) initialWaterProductionText.getValue()).doubleValue());
							initialWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialWaterProductionText.setForeground(Color.red);
						}
					}
				});
		reservoirIntensityOfWaterProductionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		reservoirIntensityOfWaterProductionText.setColumns(10);
		reservoirIntensityOfWaterProductionText.setHorizontalAlignment(JTextField.RIGHT);
		reservoirIntensityOfWaterProductionText.setValue(
				element.getReservoirIntensityOfWaterProduction());
		reservoirIntensityOfWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setReservoirIntensityOfWaterProduction(
									((Number) reservoirIntensityOfWaterProductionText.getValue()).doubleValue());
							reservoirIntensityOfWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							reservoirIntensityOfWaterProductionText.setForeground(Color.red);
						}
					}
				});
		electricalIntensityOfWaterProductionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		electricalIntensityOfWaterProductionText.setColumns(10);
		electricalIntensityOfWaterProductionText.setHorizontalAlignment(JTextField.RIGHT);
		electricalIntensityOfWaterProductionText.setValue(
				element.getElectricalIntensityOfWaterProduction());
		electricalIntensityOfWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setElectricalIntensityOfWaterProduction(
									((Number) electricalIntensityOfWaterProductionText.getValue()).doubleValue());
							electricalIntensityOfWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							electricalIntensityOfWaterProductionText.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfWaterProductionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		variableOperationsCostOfWaterProductionText.setColumns(10);
		variableOperationsCostOfWaterProductionText.setHorizontalAlignment(JTextField.RIGHT);
		variableOperationsCostOfWaterProductionText.setValue(
				element.getVariableOperationsCostOfWaterProduction());
		variableOperationsCostOfWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfWaterProduction(
									((Number) variableOperationsCostOfWaterProductionText.getValue()).doubleValue());
							variableOperationsCostOfWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfWaterProductionText.setForeground(Color.red);
						}
					}
				});
		maxWaterInputText = new JFormattedTextField(NumberFormat.getNumberInstance());
		maxWaterInputText.setColumns(10);
		maxWaterInputText.setHorizontalAlignment(JTextField.RIGHT);
		maxWaterInputText.setValue(element.getMaxWaterInput());
		maxWaterInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxWaterInput(
									((Number) maxWaterInputText.getValue()).doubleValue());
							maxWaterInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxWaterInputText.setForeground(Color.red);
						}
					}
				});
		initialWaterInputText = new JFormattedTextField(NumberFormat.getNumberInstance());
		initialWaterInputText.setColumns(10);
		initialWaterInputText.setHorizontalAlignment(JTextField.RIGHT);
		initialWaterInputText.setValue(element.getInitialWaterInput());
		initialWaterInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialWaterInput(
									((Number) initialWaterInputText.getValue()).doubleValue());
							initialWaterInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialWaterInputText.setForeground(Color.red);
						}
					}
				});
		distributionEfficiencyText = new JFormattedTextField(NumberFormat.getNumberInstance());
		distributionEfficiencyText.setColumns(10);
		distributionEfficiencyText.setHorizontalAlignment(JTextField.RIGHT);
		distributionEfficiencyText.setValue(element.getDistributionEfficiency());
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
		electricalIntensityOfWaterDistributionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		electricalIntensityOfWaterDistributionText.setColumns(10);
		electricalIntensityOfWaterDistributionText.setHorizontalAlignment(JTextField.RIGHT);
		electricalIntensityOfWaterDistributionText.setValue(
				element.getElectricalIntensityOfWaterDistribution());
		electricalIntensityOfWaterDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setElectricalIntensityOfWaterDistribution(
									((Number) electricalIntensityOfWaterDistributionText.getValue()).doubleValue());
							electricalIntensityOfWaterDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							electricalIntensityOfWaterDistributionText.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfWaterDistributionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		variableOperationsCostOfWaterDistributionText.setColumns(10);
		variableOperationsCostOfWaterDistributionText.setHorizontalAlignment(JTextField.RIGHT);
		variableOperationsCostOfWaterDistributionText.setValue(
				element.getVariableOperationsCostOfWaterDistribution());
		variableOperationsCostOfWaterDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfWaterDistribution(
									((Number) variableOperationsCostOfWaterDistributionText.getValue()).doubleValue());
							variableOperationsCostOfWaterDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfWaterDistributionText.setForeground(Color.red);
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
			addInput(elementPanel, c, "Max Water Production", 
					maxWaterProductionText, "<html>m<sup>3</sup>/year</html>");
			addInput(elementPanel, c, "Initial Water Production",
					initialWaterProductionText, "<html>m<sup>3</sup>/year</html>");
			addInput(elementPanel, c, "Reservoir Intensity of Production",
					reservoirIntensityOfWaterProductionText, "<html>m<sup>3</sup>/m<sup>3</sup></html>");
			addInput(elementPanel, c, "Electrical Intensity of Production",
					electricalIntensityOfWaterProductionText, "<html>toe/m<sup>3</sup></html>");
			addInput(elementPanel, c, "Variable Cost of Production",
					variableOperationsCostOfWaterProductionText, "<html>SAR/m<sup>3</sup></html>");
		}
		if(element.getTemplate() == null 
				|| element.getTemplate().isTransport()) {
			c.gridx = 3;
			c.gridy = 0;
			addInput(elementPanel, c, "Max Water Input", 
					maxWaterInputText, "<html>m<sup>3</sup>/year</html>");
			addInput(elementPanel, c, "Initial Water Input",
					initialWaterInputText, "<html>m<sup>3</sup>/year</html>");
			addInput(elementPanel, c, "Distribution Efficiency",
					distributionEfficiencyText, "<html>m<sup>3</sup> out/m<sup>3</sup> in</html>");
			addInput(elementPanel, c, "Electrical Intensity of Distribution",
					electricalIntensityOfWaterDistributionText, "<html>toe/m<sup>3</sup></html>");
			addInput(elementPanel, c, "Variable Cost of Distribution",
					variableOperationsCostOfWaterDistributionText, "<html>SAR/m<sup>3</sup></html>");
		}
		
		// set input enabled state
		maxWaterProductionText.setEnabled(element.getTemplate() == null);
		reservoirIntensityOfWaterProductionText.setEnabled(element.getTemplate() == null);
		electricalIntensityOfWaterProductionText.setEnabled(element.getTemplate() == null);
		variableOperationsCostOfWaterProductionText.setEnabled(element.getTemplate() == null);
		maxWaterInputText.setEnabled(element.getTemplate() == null);
		distributionEfficiencyText.setEnabled(element.getTemplate() == null);
		electricalIntensityOfWaterDistributionText.setEnabled(element.getTemplate() == null);
		variableOperationsCostOfWaterDistributionText.setEnabled(element.getTemplate() == null);
	}
	
}
