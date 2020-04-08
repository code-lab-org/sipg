/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sipg.gui.petroleum;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sipg.core.petroleum.EditablePetroleumElement;
import edu.mit.sipg.gui.base.ElementPanel;
import edu.mit.sipg.gui.event.DocumentChangeListener;
import edu.mit.sipg.scenario.Scenario;
import edu.mit.sipg.units.CurrencyUnits;
import edu.mit.sipg.units.CurrencyUnitsOutput;
import edu.mit.sipg.units.DefaultUnits;
import edu.mit.sipg.units.ElectricityUnits;
import edu.mit.sipg.units.ElectricityUnitsOutput;
import edu.mit.sipg.units.OilUnits;
import edu.mit.sipg.units.OilUnitsOutput;
import edu.mit.sipg.units.TimeUnits;

/**
 * An implementation of the element panel class for the petroleum sector.
 * 
 * @author Paul T. Grogan
 */
public class PetroleumElementPanel extends ElementPanel 
		implements CurrencyUnitsOutput, ElectricityUnitsOutput, OilUnitsOutput {
	private static final long serialVersionUID = -9048149807650177253L;
	private final CurrencyUnits currencyUnits = CurrencyUnits.Msim;
	private final TimeUnits currencyTimeUnits = TimeUnits.year;
	private final ElectricityUnits electricityUnits = ElectricityUnits.TWh;
	private final TimeUnits electricityTimeUnits = TimeUnits.year;
	private final OilUnits oilUnits = OilUnits.Mtoe;
	private final TimeUnits oilTimeUnits = TimeUnits.year;
	
	private final JFormattedTextField maxPetroleumProductionText;
	private final JFormattedTextField initialPetroleumProductionText;
	private final JFormattedTextField reservoirIntensityOfPetroleumProductionText;
	private final JFormattedTextField variableOperationsCostOfPetroleumProductionText;
	private final JLabel maxVariableCostLabel, maxReservoirUseLabel;
	
	private final JFormattedTextField maxPetroleumInputText;
	private final JFormattedTextField initialPetroleumInputText;
	private final JFormattedTextField distributionEfficiencyText;
	private final JFormattedTextField electricalIntensityOfPetroleumDistributionText;
	private final JFormattedTextField variableOperationsCostOfPetroleumDistributionText;
	private final JLabel maxOutputLabel, maxElectricityUseDistLabel, 
			maxVariableCostDistLabel;
	
	/**
	 * Instantiates a new petroleum element panel.
	 *
	 * @param scenario the scenario
	 * @param element the element
	 * @param detailed the detailed
	 */
	public PetroleumElementPanel(Scenario scenario, 
			final EditablePetroleumElement element, 
			boolean detailed) {
		super(scenario, element, detailed);
		
		final PetroleumElementPanel thisPanel = this; 
		
		maxPetroleumProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		maxPetroleumProductionText.setColumns(10);
		maxPetroleumProductionText.setHorizontalAlignment(JTextField.RIGHT);
		maxPetroleumProductionText.setValue(OilUnits.convertFlow(
				element.getMaxPetroleumProduction(), element, this));
		maxPetroleumProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxPetroleumProduction(OilUnits.convertFlow(
									(Double) maxPetroleumProductionText.getValue(),
									thisPanel, element));
							maxVariableCostLabel.setText(NumberFormat.getNumberInstance().format(
									CurrencyUnits.convertFlow(element.getMaxPetroleumProduction() 
											* element.getVariableOperationsCostOfPetroleumProduction(), 
											element, thisPanel)));
							maxReservoirUseLabel.setText(NumberFormat.getNumberInstance().format(
									OilUnits.convertFlow(element.getMaxPetroleumProduction() 
											* element.getReservoirIntensityOfPetroleumProduction(), 
											element, thisPanel)));
							maxPetroleumProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxPetroleumProductionText.setForeground(Color.red);
						}
					}
				});
		initialPetroleumProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		initialPetroleumProductionText.setColumns(10);
		initialPetroleumProductionText.setHorizontalAlignment(JTextField.RIGHT);
		initialPetroleumProductionText.setValue(OilUnits.convertFlow(
				element.getInitialPetroleumProduction(), element, this));
		initialPetroleumProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialPetroleumProduction(
									(Double) initialPetroleumProductionText.getValue());
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
									(Double) reservoirIntensityOfPetroleumProductionText.getValue());
							maxReservoirUseLabel.setText(NumberFormat.getNumberInstance().format(
									OilUnits.convertFlow(element.getMaxPetroleumProduction() 
											* element.getReservoirIntensityOfPetroleumProduction(), 
											element, thisPanel)));
							reservoirIntensityOfPetroleumProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							reservoirIntensityOfPetroleumProductionText.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfPetroleumProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		variableOperationsCostOfPetroleumProductionText.setColumns(10);
		variableOperationsCostOfPetroleumProductionText.setHorizontalAlignment(JTextField.RIGHT);
		variableOperationsCostOfPetroleumProductionText.setValue(DefaultUnits.convert(
				element.getVariableOperationsCostOfPetroleumProduction(),
				element.getCurrencyUnits(), element.getOilUnits(),
				getCurrencyUnits(), getOilUnits()));
		variableOperationsCostOfPetroleumProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfPetroleumProduction(DefaultUnits.convert(
									(Double) variableOperationsCostOfPetroleumProductionText.getValue(),
									getCurrencyUnits(), getOilUnits(),
									element.getCurrencyUnits(), element.getOilUnits()));
							maxVariableCostLabel.setText(NumberFormat.getNumberInstance().format(
									CurrencyUnits.convertFlow(element.getMaxPetroleumProduction() 
											* element.getVariableOperationsCostOfPetroleumProduction(), 
											element, thisPanel)));
							variableOperationsCostOfPetroleumProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfPetroleumProductionText.setForeground(Color.red);
						}
					}
				});
		maxVariableCostLabel = new JLabel(NumberFormat.getNumberInstance().format(
				CurrencyUnits.convertFlow(element.getMaxPetroleumProduction() 
						* element.getVariableOperationsCostOfPetroleumProduction(), 
						element, this)), JLabel.RIGHT);
		maxReservoirUseLabel = new JLabel(NumberFormat.getNumberInstance().format(
				OilUnits.convertFlow(element.getMaxPetroleumProduction() 
						* element.getReservoirIntensityOfPetroleumProduction(), 
						element, this)), JLabel.RIGHT);
		
		
		maxPetroleumInputText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		maxPetroleumInputText.setColumns(10);
		maxPetroleumInputText.setHorizontalAlignment(JTextField.RIGHT);
		maxPetroleumInputText.setValue(OilUnits.convertFlow(
				element.getMaxPetroleumInput(), element, this));
		maxPetroleumInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxPetroleumInput(OilUnits.convertFlow(
									(Double) maxPetroleumInputText.getValue(),
									thisPanel, element));
							maxOutputLabel.setText(NumberFormat.getNumberInstance().format(
									OilUnits.convertFlow(element.getMaxPetroleumInput() 
											* element.getDistributionEfficiency(), 
											element, thisPanel)));
							maxVariableCostDistLabel.setText(NumberFormat.getNumberInstance().format(
									CurrencyUnits.convertFlow(element.getMaxPetroleumInput() 
											* element.getVariableOperationsCostOfPetroleumDistribution(), 
											element, thisPanel)));
							maxElectricityUseDistLabel.setText(NumberFormat.getNumberInstance().format(
									ElectricityUnits.convertFlow(element.getMaxPetroleumInput() 
											* element.getElectricalIntensityOfPetroleumDistribution(), 
											element, thisPanel)));
							maxPetroleumInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxPetroleumInputText.setForeground(Color.red);
						}
					}
				});
		initialPetroleumInputText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		initialPetroleumInputText.setColumns(10);
		initialPetroleumInputText.setHorizontalAlignment(JTextField.RIGHT);
		initialPetroleumInputText.setValue(OilUnits.convertFlow(
				element.getInitialPetroleumInput(), element, this));
		initialPetroleumInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialPetroleumInput(OilUnits.convertFlow(
									(Double) initialPetroleumInputText.getValue(),
									thisPanel, element));
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
									(Double) distributionEfficiencyText.getValue());
							maxOutputLabel.setText(NumberFormat.getNumberInstance().format(
									OilUnits.convertFlow(element.getMaxPetroleumInput() 
											* element.getDistributionEfficiency(), 
											element, thisPanel)));
							distributionEfficiencyText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							distributionEfficiencyText.setForeground(Color.red);
						}
					}
				});
		electricalIntensityOfPetroleumDistributionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		electricalIntensityOfPetroleumDistributionText.setColumns(10);
		electricalIntensityOfPetroleumDistributionText.setHorizontalAlignment(JTextField.RIGHT);
		electricalIntensityOfPetroleumDistributionText.setValue(DefaultUnits.convert(
				element.getElectricalIntensityOfPetroleumDistribution(),
				element.getElectricityUnits(), element.getOilUnits(),
				getElectricityUnits(), getOilUnits()));
		electricalIntensityOfPetroleumDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setElectricalIntensityOfPetroleumDistribution(DefaultUnits.convert(
									(Double) electricalIntensityOfPetroleumDistributionText.getValue(),
									getElectricityUnits(), getOilUnits(),
									element.getElectricityUnits(), element.getOilUnits()));
							maxVariableCostDistLabel.setText(NumberFormat.getNumberInstance().format(
									CurrencyUnits.convertFlow(element.getMaxPetroleumInput() 
											* element.getVariableOperationsCostOfPetroleumDistribution(), 
											element, thisPanel)));
							electricalIntensityOfPetroleumDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							electricalIntensityOfPetroleumDistributionText.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfPetroleumDistributionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		variableOperationsCostOfPetroleumDistributionText.setColumns(10);
		variableOperationsCostOfPetroleumDistributionText.setHorizontalAlignment(JTextField.RIGHT);
		variableOperationsCostOfPetroleumDistributionText.setValue(DefaultUnits.convert(
				element.getVariableOperationsCostOfPetroleumDistribution(),
				element.getCurrencyUnits(), element.getOilUnits(),
				getCurrencyUnits(), getOilUnits()));
		variableOperationsCostOfPetroleumDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfPetroleumDistribution(DefaultUnits.convert(
									(Double) variableOperationsCostOfPetroleumDistributionText.getValue(),
									getCurrencyUnits(), getOilUnits(),
									element.getCurrencyUnits(), element.getOilUnits()));
							maxElectricityUseDistLabel.setText(NumberFormat.getNumberInstance().format(
									ElectricityUnits.convertFlow(element.getMaxPetroleumInput() 
											* element.getElectricalIntensityOfPetroleumDistribution(), 
											element, thisPanel)));
							variableOperationsCostOfPetroleumDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfPetroleumDistributionText.setForeground(Color.red);
						}
					}
				});
		maxOutputLabel = new JLabel(NumberFormat.getNumberInstance().format(
				OilUnits.convertFlow(element.getMaxPetroleumInput() 
						* element.getDistributionEfficiency(), 
						element, this)), JLabel.RIGHT);
		maxVariableCostDistLabel = new JLabel(NumberFormat.getNumberInstance().format(
				CurrencyUnits.convertFlow(element.getMaxPetroleumInput() 
						* element.getVariableOperationsCostOfPetroleumDistribution(), 
						element, this)), JLabel.RIGHT);
		maxElectricityUseDistLabel = new JLabel(NumberFormat.getNumberInstance().format(
				ElectricityUnits.convertFlow(element.getMaxPetroleumInput() 
						* element.getElectricalIntensityOfPetroleumDistribution(), 
						element, this)), JLabel.RIGHT);
		
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
			addInput(elementPanel, c, "Maximum Oil Production", 
					new JLabel(NumberFormat.getNumberInstance().format(
							OilUnits.convertFlow(element.getMaxPetroleumProduction(), 
									element, this)), JLabel.RIGHT), 
									oilUnits + "/" + oilTimeUnits);
			if(detailed) {
				addInput(elementPanel, c, "Max Petroleum Production", 
						maxPetroleumProductionText,
						oilUnits + "/" + oilTimeUnits);
				addInput(elementPanel, c, "Initial Petroleum Production", 
						initialPetroleumProductionText, 
						oilUnits + "/" + oilTimeUnits);
				addInput(elementPanel, c, "Specific Reservoir Consumption",
						reservoirIntensityOfPetroleumProductionText, 
						oilUnits + "/" + oilUnits,
						"  max: ", maxReservoirUseLabel,
						oilUnits + "/" + oilTimeUnits);
				addInput(elementPanel, c, "Variable Cost of Production",
						variableOperationsCostOfPetroleumProductionText,
						currencyUnits + "/" + oilUnits,
						"  max: ", maxVariableCostLabel,
						currencyUnits + "/" + currencyTimeUnits);
			}
		}
		if(element.getTemplateName() == null 
				|| scenario.getTemplate(element.getTemplateName()) == null
				|| scenario.getTemplate(element.getTemplateName()).isTransport()) {
			c.gridx = 3;
			c.gridy = 0;
			addInput(elementPanel, c, "Maximum Oil Throughput", 
					new JLabel(NumberFormat.getNumberInstance().format(
							OilUnits.convertFlow(element.getMaxPetroleumInput(), 
									element, this)), JLabel.RIGHT), 
									oilUnits + "/" + oilTimeUnits);
			if(detailed) {
				addInput(elementPanel, c, "Max Petroleum Input", 
						maxPetroleumInputText,
						oilUnits + "/" + oilTimeUnits);
				addInput(elementPanel, c, "Initial Petroleum Input", 
						initialPetroleumInputText, oilUnits + "/" + oilTimeUnits);
				addInput(elementPanel, c, "Distribution Efficiency",
						distributionEfficiencyText, 
						oilUnits + "/" + oilUnits,
						"  max: ", maxOutputLabel,
						oilUnits + "/" + oilTimeUnits);
				addInput(elementPanel, c, "Electrical Intensity of Distribution",
						electricalIntensityOfPetroleumDistributionText,
						electricityUnits + "/" + oilUnits,
						"  max: ", maxElectricityUseDistLabel,
						electricityUnits + "/" + electricityTimeUnits);
				addInput(elementPanel, c, "Variable Cost of Distribution",
						variableOperationsCostOfPetroleumDistributionText,
						currencyUnits + "/" + oilUnits,
						"  max: ", maxVariableCostDistLabel,
						currencyUnits + "/" + currencyTimeUnits);
			}
		}
		
		maxPetroleumProductionText.setEnabled(element.getTemplateName() == null);
		reservoirIntensityOfPetroleumProductionText.setEnabled(element.getTemplateName() == null);
		variableOperationsCostOfPetroleumProductionText.setEnabled(element.getTemplateName() == null);
		maxPetroleumInputText.setEnabled(element.getTemplateName() == null);
		distributionEfficiencyText.setEnabled(element.getTemplateName() == null);
		electricalIntensityOfPetroleumDistributionText.setEnabled(element.getTemplateName() == null);
		variableOperationsCostOfPetroleumDistributionText.setEnabled(element.getTemplateName() == null);
	}

	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}

	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}

	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
	}
}
