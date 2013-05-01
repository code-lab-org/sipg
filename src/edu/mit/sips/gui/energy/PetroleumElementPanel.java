package edu.mit.sips.gui.energy;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sips.core.City;
import edu.mit.sips.core.energy.MutablePetroleumElement;
import edu.mit.sips.gui.DocumentChangeListener;
import edu.mit.sips.gui.ElementPanel;

/**
 * The Class PetroleumElementPanel.
 */
public class PetroleumElementPanel extends ElementPanel {
	private static final long serialVersionUID = -9048149807650177253L;
	
	private final JTextField maxPetroleumProductionText;
	private final JTextField initialPetroleumProductionText;
	private final JTextField reservoirIntensityOfPetroleumProductionText;
	private final JTextField variableOperationsCostOfPetroleumProductionText;
	
	private final JTextField maxPetroleumInputText;
	private final JTextField initialPetroleumInputText;
	private final JTextField distributionEfficiencyText;
	private final JTextField electricalIntensityOfPetroleumDistributionText;
	private final JTextField variableOperationsCostOfPetroleumDistributionText;
	
	/**
	 * Instantiates a new petroleum element panel.
	 *
	 * @param city the city
	 * @param element the element
	 */
	public PetroleumElementPanel(City city, 
			final MutablePetroleumElement element) {
		super(city, element);
		
		JPanel elementPanel = new JPanel();
		elementPanel.setLayout(new GridBagLayout());
		add(elementPanel);
		

		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);

		c.gridx = 0;
		maxPetroleumProductionText = new JTextField(10);
		maxPetroleumProductionText.setText(
				new Double(element.getMaxPetroleumProduction()).toString());
		maxPetroleumProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxPetroleumProduction(Double.parseDouble(
									maxPetroleumProductionText.getText()));
							maxPetroleumProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxPetroleumProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Max Petroleum Production (bbl/year)", 
				maxPetroleumProductionText);
		initialPetroleumProductionText = new JTextField(10);
		initialPetroleumProductionText.setText(
				new Double(element.getInitialPetroleumProduction()).toString());
		initialPetroleumProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialPetroleumProduction(Double.parseDouble(
									initialPetroleumProductionText.getText()));
							initialPetroleumProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialPetroleumProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Initial Petroleum Production (bbl/year)",
				initialPetroleumProductionText);
		reservoirIntensityOfPetroleumProductionText = new JTextField(10);
		reservoirIntensityOfPetroleumProductionText.setText(
				new Double(element.getReservoirIntensityOfPetroleumProduction()).toString());
		reservoirIntensityOfPetroleumProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setReservoirIntensityOfPetroleumProduction(Double.parseDouble(
									reservoirIntensityOfPetroleumProductionText.getText()));
							reservoirIntensityOfPetroleumProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							reservoirIntensityOfPetroleumProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Reservoir Intensity of Production (bbl/bbl)",
				reservoirIntensityOfPetroleumProductionText);
		variableOperationsCostOfPetroleumProductionText = new JTextField(10);
		variableOperationsCostOfPetroleumProductionText.setText(
				new Double(element.getVariableOperationsCostOfPetroleumProduction()).toString());
		variableOperationsCostOfPetroleumProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfPetroleumProduction(Double.parseDouble(
									variableOperationsCostOfPetroleumProductionText.getText()));
							variableOperationsCostOfPetroleumProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfPetroleumProductionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Variable Cost of Production (SAR/bbl)",
				variableOperationsCostOfPetroleumProductionText);
		
		c.gridx = 2;
		c.gridy = 0;
		maxPetroleumInputText = new JTextField(10);
		maxPetroleumInputText.setText(
				new Double(element.getMaxPetroleumInput()).toString());
		maxPetroleumInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxPetroleumInput(Double.parseDouble(
									maxPetroleumInputText.getText()));
							maxPetroleumInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxPetroleumInputText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Max Petroleum Input (bbl/year)", 
				maxPetroleumInputText);
		initialPetroleumInputText = new JTextField(10);
		initialPetroleumInputText.setText(
				new Double(element.getInitialPetroleumInput()).toString());
		initialPetroleumInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialPetroleumInput(Double.parseDouble(
									initialPetroleumInputText.getText()));
							initialPetroleumInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialPetroleumInputText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Initial Petroleum Input (bbl/year)",
				initialPetroleumInputText);
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
				"Distribution Efficiency (bbl out/bbl in)",
				distributionEfficiencyText);
		electricalIntensityOfPetroleumDistributionText = new JTextField(10);
		electricalIntensityOfPetroleumDistributionText.setText(
				new Double(element.getElectricalIntensityOfPetroleumDistribution()).toString());
		electricalIntensityOfPetroleumDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setElectricalIntensityOfPetroleumDistribution(Double.parseDouble(
									electricalIntensityOfPetroleumDistributionText.getText()));
							electricalIntensityOfPetroleumDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							electricalIntensityOfPetroleumDistributionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Electrical Intensity of Distribution (MWh/bbl)",
				electricalIntensityOfPetroleumDistributionText);
		variableOperationsCostOfPetroleumDistributionText = new JTextField(10);
		variableOperationsCostOfPetroleumDistributionText.setText(
				new Double(element.getVariableOperationsCostOfPetroleumDistribution()).toString());
		variableOperationsCostOfPetroleumDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfPetroleumDistribution(Double.parseDouble(
									variableOperationsCostOfPetroleumDistributionText.getText()));
							variableOperationsCostOfPetroleumDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfPetroleumDistributionText.setForeground(Color.red);
						}
					}
				});
		addInput(elementPanel, c, 
				"Variable Cost of Distribution (SAR/bbl)",
				variableOperationsCostOfPetroleumDistributionText);
		
		setTemplateMode(element.getTemplate() != null);
	}
	
}
