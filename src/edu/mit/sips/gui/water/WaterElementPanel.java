package edu.mit.sips.gui.water;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sips.core.City;
import edu.mit.sips.core.water.MutableWaterElement;
import edu.mit.sips.gui.DocumentChangeListener;
import edu.mit.sips.gui.ElementPanel;

/**
 * The Class WaterElementPanel.
 */
public class WaterElementPanel extends ElementPanel {
	private static final long serialVersionUID = -9048149807650177253L;
	
	private final JTextField maxWaterProductionText;
	private final JTextField initialWaterProductionText;
	private final JTextField reservoirIntensityOfWaterProductionText;
	private final JTextField electricalIntensityOfWaterProductionText;
	private final JTextField variableOperationsCostOfWaterProductionText;

	private final JTextField maxWaterInputText;
	private final JTextField initialWaterInputText;
	private final JTextField distributionEfficiencyText;
	private final JTextField electricalIntensityOfWaterDistributionText;
	private final JTextField variableOperationsCostOfWaterDistributionText;
	
	/**
	 * Instantiates a new water element panel.
	 *
	 * @param city the city
	 * @param element the element
	 */
	public WaterElementPanel(City city, 
			final MutableWaterElement element) {
		super(city, element);
		
		JPanel elementPanel = new JPanel();
		elementPanel.setLayout(new GridBagLayout());
		add(elementPanel);
		

		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);

		c.gridx = 0;
		maxWaterProductionText = new JTextField(10);
		maxWaterProductionText.setText(
				new Double(element.getMaxWaterProduction()).toString());
		maxWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxWaterProduction(Double.parseDouble(
									maxWaterProductionText.getText()));
							maxWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxWaterProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"<html>Max Water Production (m<sup>3</sup>/year)</html>", 
				maxWaterProductionText);
		initialWaterProductionText = new JTextField(10);
		initialWaterProductionText.setText(
				new Double(element.getInitialWaterProduction()).toString());
		initialWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialWaterProduction(Double.parseDouble(
									initialWaterProductionText.getText()));
							initialWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialWaterProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"<html>Initial Water Production (m<sup>3</sup>/year)</html>",
				initialWaterProductionText);
		reservoirIntensityOfWaterProductionText = new JTextField(10);
		reservoirIntensityOfWaterProductionText.setText(
				new Double(element.getReservoirIntensityOfWaterProduction()).toString());
		reservoirIntensityOfWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setReservoirIntensityOfWaterProduction(Double.parseDouble(
									reservoirIntensityOfWaterProductionText.getText()));
							reservoirIntensityOfWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							reservoirIntensityOfWaterProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"<html>Reservoir Intensity of Production (m<sup>3</sup>/m<sup>3</sup>)</html>",
				reservoirIntensityOfWaterProductionText);
		electricalIntensityOfWaterProductionText = new JTextField(10);
		electricalIntensityOfWaterProductionText.setText(
				new Double(element.getElectricalIntensityOfWaterProduction()).toString());
		electricalIntensityOfWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setElectricalIntensityOfWaterProduction(Double.parseDouble(
									electricalIntensityOfWaterProductionText.getText()));
							electricalIntensityOfWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							electricalIntensityOfWaterProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"<html>Electrical Intensity of Production (MWh/m<sup>3</sup>)</html>",
				electricalIntensityOfWaterProductionText);
		variableOperationsCostOfWaterProductionText = new JTextField(10);
		variableOperationsCostOfWaterProductionText.setText(
				new Double(element.getVariableOperationsCostOfWaterProduction()).toString());
		variableOperationsCostOfWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfWaterProduction(Double.parseDouble(
									variableOperationsCostOfWaterProductionText.getText()));
							variableOperationsCostOfWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfWaterProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"<html>Variable Cost of Production (SAR/m<sup>3</sup>)</html>",
				variableOperationsCostOfWaterProductionText);
		
		c.gridx = 2;
		c.gridy = 0;
		maxWaterInputText = new JTextField(10);
		maxWaterInputText.setText(
				new Double(element.getMaxWaterInput()).toString());
		maxWaterInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxWaterInput(Double.parseDouble(
									maxWaterInputText.getText()));
							maxWaterInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxWaterInputText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"<html>Max Water Input (m<sup>3</sup>/year)</html>", 
				maxWaterInputText);
		initialWaterInputText = new JTextField(10);
		initialWaterInputText.setText(
				new Double(element.getInitialWaterInput()).toString());
		initialWaterInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialWaterInput(Double.parseDouble(
									initialWaterInputText.getText()));
							initialWaterInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialWaterInputText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"<html>Initial Water Input (m<sup>3</sup>/year)</html>",
				initialWaterInputText);
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
				"<html>Distribution Efficiency (m<sup>3</sup> out/m<sup>3</sup> in)</html>",
				distributionEfficiencyText);
		electricalIntensityOfWaterDistributionText = new JTextField(10);
		electricalIntensityOfWaterDistributionText.setText(
				new Double(element.getElectricalIntensityOfWaterDistribution()).toString());
		electricalIntensityOfWaterDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setElectricalIntensityOfWaterDistribution(Double.parseDouble(
									electricalIntensityOfWaterDistributionText.getText()));
							electricalIntensityOfWaterDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							electricalIntensityOfWaterDistributionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"<html>Electrical Intensity of Distribution (MWh/m<sup>3</sup>)</html>",
				electricalIntensityOfWaterDistributionText);
		variableOperationsCostOfWaterDistributionText = new JTextField(10);
		variableOperationsCostOfWaterDistributionText.setText(
				new Double(element.getVariableOperationsCostOfWaterDistribution()).toString());
		variableOperationsCostOfWaterDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfWaterDistribution(Double.parseDouble(
									variableOperationsCostOfWaterDistributionText.getText()));
							variableOperationsCostOfWaterDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfWaterDistributionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"<html>Variable Cost of Distribution (SAR/m<sup>3</sup>)</html>",
				variableOperationsCostOfWaterDistributionText);
	}
	
}
