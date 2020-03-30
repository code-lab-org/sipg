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
package edu.mit.sips.gui.electricity;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sips.core.electricity.EditableElectricityElement;
import edu.mit.sips.gui.base.ElementPanel;
import edu.mit.sips.gui.event.DocumentChangeListener;
import edu.mit.sips.scenario.Scenario;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * An implementation of the element panel class for the electricity sector.
 * 
 * @author Paul T. Grogan
 */
public class ElectricityElementPanel extends ElementPanel 
		implements CurrencyUnitsOutput, ElectricityUnitsOutput, OilUnitsOutput, WaterUnitsOutput {
	private static final long serialVersionUID = -9048149807650177253L;
	private static final CurrencyUnits currencyUnits = CurrencyUnits.Msim;
	private static final TimeUnits currencyTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.TWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.Mtoe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.km3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	
	private final JFormattedTextField maxElectricityProductionText;
	private final JFormattedTextField initialElectricityProductionText;
	private final JFormattedTextField petroleumIntensityOfElectricityProductionText;
	private final JFormattedTextField waterIntensityOfElectricityProductionText;
	private final JFormattedTextField variableOperationsCostOfElectricityProductionText;
	private final JLabel maxVariableCostLabel, maxPetroleumUseLabel, 
			maxWaterUseLabel;
	
	private final JFormattedTextField maxElectricityInputText;
	private final JFormattedTextField initialElectricityInputText;
	private final JFormattedTextField distributionEfficiencyText;
	private final JFormattedTextField variableOperationsCostOfElectricityDistributionText;
	private final JLabel maxOutputLabel, maxVariableCostDistLabel;
	
	/**
	 * Instantiates a new electricity element panel.
	 *
	 * @param scenario the scenario
	 * @param element the element
	 * @param detailed the detailed
	 */
	public ElectricityElementPanel(Scenario scenario, 
			final EditableElectricityElement element, 
			boolean detailed) {
		super(scenario, element, detailed);
		
		final ElectricityElementPanel thisPanel = this; 
		
		maxElectricityProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		maxElectricityProductionText.setColumns(10);
		maxElectricityProductionText.setHorizontalAlignment(JTextField.RIGHT);
		maxElectricityProductionText.setValue(ElectricityUnits.convertFlow(
				element.getMaxElectricityProduction(), element, this));
		maxElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxElectricityProduction(ElectricityUnits.convertFlow(
									(Double) maxElectricityProductionText.getValue(),
									thisPanel, element));
							maxVariableCostLabel.setText(NumberFormat.getNumberInstance().format(
									CurrencyUnits.convertFlow(element.getMaxElectricityProduction() 
											* element.getVariableOperationsCostOfElectricityProduction(), 
											element, thisPanel)));
							maxPetroleumUseLabel.setText(NumberFormat.getNumberInstance().format(
									ElectricityUnits.convertFlow(element.getMaxElectricityProduction() 
											* element.getPetroleumIntensityOfElectricityProduction(), 
											element, thisPanel)));
							maxWaterUseLabel.setText(NumberFormat.getNumberInstance().format(
									WaterUnits.convertFlow(element.getMaxElectricityProduction() 
											* element.getWaterIntensityOfElectricityProduction(), 
											element, thisPanel)));
							maxElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		initialElectricityProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		initialElectricityProductionText.setColumns(10);
		initialElectricityProductionText.setHorizontalAlignment(JTextField.RIGHT);
		initialElectricityProductionText.setValue(ElectricityUnits.convertFlow(
				element.getInitialElectricityProduction(), element, this));
		initialElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialElectricityProduction(ElectricityUnits.convertFlow(
									(Double) initialElectricityProductionText.getValue(),
									thisPanel, element));
							initialElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		petroleumIntensityOfElectricityProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		petroleumIntensityOfElectricityProductionText.setColumns(10);
		petroleumIntensityOfElectricityProductionText.setHorizontalAlignment(JTextField.RIGHT);
		petroleumIntensityOfElectricityProductionText.setValue(DefaultUnits.convert(
				element.getPetroleumIntensityOfElectricityProduction(),
				element.getOilUnits(), element.getElectricityUnits(), 
				getOilUnits(), getElectricityUnits()));
		petroleumIntensityOfElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setPetroleumIntensityOfElectricityProduction(DefaultUnits.convert(
									(Double) petroleumIntensityOfElectricityProductionText.getValue(),
									getOilUnits(), getElectricityUnits(), 
									element.getOilUnits(), element.getElectricityUnits()));
							maxPetroleumUseLabel.setText(NumberFormat.getNumberInstance().format(
									ElectricityUnits.convertFlow(element.getMaxElectricityProduction() 
											* element.getPetroleumIntensityOfElectricityProduction(), 
											element, thisPanel)));
							petroleumIntensityOfElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							petroleumIntensityOfElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		waterIntensityOfElectricityProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		waterIntensityOfElectricityProductionText.setColumns(10);
		waterIntensityOfElectricityProductionText.setHorizontalAlignment(JTextField.RIGHT);
		waterIntensityOfElectricityProductionText.setValue(DefaultUnits.convert(
				element.getPetroleumIntensityOfElectricityProduction(), 
				element.getWaterUnits(), element.getElectricityUnits(), 
				getWaterUnits(), getElectricityUnits()));
		waterIntensityOfElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setPetroleumIntensityOfElectricityProduction(DefaultUnits.convert(
									(Double) waterIntensityOfElectricityProductionText.getValue(),
									getWaterUnits(), getElectricityUnits(),
									element.getWaterUnits(), element.getElectricityUnits()));
							maxWaterUseLabel.setText(NumberFormat.getNumberInstance().format(
									WaterUnits.convertFlow(element.getMaxElectricityProduction() 
											* element.getWaterIntensityOfElectricityProduction(), 
											element, thisPanel)));
							waterIntensityOfElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							waterIntensityOfElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfElectricityProductionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		variableOperationsCostOfElectricityProductionText.setColumns(10);
		variableOperationsCostOfElectricityProductionText.setHorizontalAlignment(JTextField.RIGHT);
		variableOperationsCostOfElectricityProductionText.setValue(DefaultUnits.convert(
				element.getVariableOperationsCostOfElectricityProduction(), 
				element.getCurrencyUnits(), element.getElectricityUnits(), 
				getCurrencyUnits(), getElectricityUnits()));
		variableOperationsCostOfElectricityProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfElectricityProduction(DefaultUnits.convert(
									(Double) variableOperationsCostOfElectricityProductionText.getValue(),
									getCurrencyUnits(), getElectricityUnits(),
									element.getCurrencyUnits(), element.getElectricityUnits()));
							maxVariableCostLabel.setText(NumberFormat.getNumberInstance().format(
									CurrencyUnits.convertFlow(element.getMaxElectricityProduction() 
											* element.getVariableOperationsCostOfElectricityProduction(), 
											element, thisPanel)));
							variableOperationsCostOfElectricityProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfElectricityProductionText.setForeground(Color.red);
						}
					}
				});
		maxVariableCostLabel = new JLabel(NumberFormat.getNumberInstance().format(
				CurrencyUnits.convertFlow(element.getMaxElectricityProduction() 
						* element.getVariableOperationsCostOfElectricityProduction(), 
						element, this)), JLabel.RIGHT);
		maxPetroleumUseLabel = new JLabel(NumberFormat.getNumberInstance().format(
				ElectricityUnits.convertFlow(element.getMaxElectricityProduction() 
						* element.getPetroleumIntensityOfElectricityProduction(), 
						element, this)), JLabel.RIGHT);
		maxWaterUseLabel = new JLabel(NumberFormat.getNumberInstance().format(
				WaterUnits.convertFlow(element.getMaxElectricityProduction() 
						* element.getWaterIntensityOfElectricityProduction(), 
						element, this)), JLabel.RIGHT);
		
		maxElectricityInputText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		maxElectricityInputText.setColumns(10);
		maxElectricityInputText.setHorizontalAlignment(JTextField.RIGHT);
		maxElectricityInputText.setValue(ElectricityUnits.convertFlow(
				element.getMaxElectricityInput(), element, this));
		maxElectricityInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxElectricityInput(ElectricityUnits.convertFlow(
									(Double) maxElectricityInputText.getValue(),
									thisPanel, element));
							maxOutputLabel.setText(NumberFormat.getNumberInstance().format(
									ElectricityUnits.convertFlow(element.getMaxElectricityInput() 
											* element.getDistributionEfficiency(), 
											element, thisPanel)));
							maxVariableCostDistLabel.setText(NumberFormat.getNumberInstance().format(
									CurrencyUnits.convertFlow(element.getMaxElectricityInput() 
											* element.getVariableOperationsCostOfElectricityDistribution(), 
											element, thisPanel)));
							maxElectricityInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxElectricityInputText.setForeground(Color.red);
						}
					}
				});
		initialElectricityInputText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		initialElectricityInputText.setColumns(10);
		initialElectricityInputText.setHorizontalAlignment(JTextField.RIGHT);
		initialElectricityInputText.setValue(ElectricityUnits.convertFlow(
				element.getInitialElectricityInput(), element, this));
		initialElectricityInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialElectricityInput(ElectricityUnits.convertFlow(
									(Double) initialElectricityInputText.getValue(),
									thisPanel, element));
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
									(Double) distributionEfficiencyText.getValue());
							maxOutputLabel.setText(NumberFormat.getNumberInstance().format(
									ElectricityUnits.convertFlow(element.getMaxElectricityInput() 
											* element.getDistributionEfficiency(), 
											element, thisPanel)));
							distributionEfficiencyText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							distributionEfficiencyText.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfElectricityDistributionText = new JFormattedTextField(NumberFormat.getNumberInstance()); 
		variableOperationsCostOfElectricityDistributionText.setColumns(10);
		variableOperationsCostOfElectricityDistributionText.setHorizontalAlignment(JTextField.RIGHT);
		variableOperationsCostOfElectricityDistributionText.setValue(DefaultUnits.convert(
				element.getVariableOperationsCostOfElectricityDistribution(), 
				element.getCurrencyUnits(), element.getElectricityUnits(), 
				getCurrencyUnits(), getElectricityUnits()));
		variableOperationsCostOfElectricityDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfElectricityDistribution(DefaultUnits.convert(
									(Double) variableOperationsCostOfElectricityDistributionText.getValue(),
									getCurrencyUnits(), getElectricityUnits(),
									element.getCurrencyUnits(), element.getElectricityUnits()));
							maxVariableCostDistLabel.setText(NumberFormat.getNumberInstance().format(
									CurrencyUnits.convertFlow(element.getMaxElectricityInput() 
											* element.getVariableOperationsCostOfElectricityDistribution(), 
											element, thisPanel)));
							variableOperationsCostOfElectricityDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfElectricityDistributionText.setForeground(Color.red);
						}
					}
				});
		maxOutputLabel = new JLabel(NumberFormat.getNumberInstance().format(
				ElectricityUnits.convertFlow(element.getMaxElectricityInput() 
						* element.getDistributionEfficiency(), 
						element, this)), JLabel.RIGHT);
		maxVariableCostDistLabel = new JLabel(NumberFormat.getNumberInstance().format(
				CurrencyUnits.convertFlow(element.getMaxElectricityInput() 
						* element.getVariableOperationsCostOfElectricityDistribution(), 
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
			addInput(elementPanel, c, "Maximum Electricity Production", 
					new JLabel(NumberFormat.getNumberInstance().format(
							ElectricityUnits.convertFlow(element.getMaxElectricityProduction(), 
									element, this)), JLabel.RIGHT), 
									electricityUnits + "/" + electricityTimeUnits);
			if(detailed) {
				addInput(elementPanel, c, "Maximum Electricity Production", 
						maxElectricityProductionText, 
						electricityUnits + "/" + electricityTimeUnits);
				addInput(elementPanel, c, "Initial Electricity Production", 
						initialElectricityProductionText, 
						electricityUnits + "/" + electricityTimeUnits);
				addInput(elementPanel, c, "Specific Petrolum Consumption",
						petroleumIntensityOfElectricityProductionText,
						oilUnits + "/" + electricityUnits,
						"  max: ", maxPetroleumUseLabel,
						oilUnits + "/" + oilTimeUnits);
				addInput(elementPanel, c, "Specific Water Consumption",
						waterIntensityOfElectricityProductionText,
						waterUnits + "/" + electricityUnits,
						"  max: ", maxWaterUseLabel,
						waterUnits + "/" + waterTimeUnits);
				addInput(elementPanel, c, "Variable Operations Cost",
						variableOperationsCostOfElectricityProductionText,
						currencyUnits + "/" + electricityUnits,
						"  max: ", maxVariableCostLabel,
						currencyUnits + "/" + currencyTimeUnits);
			}
		}
		if(element.getTemplateName() == null 
				|| scenario.getTemplate(element.getTemplateName()) == null
				|| scenario.getTemplate(element.getTemplateName()).isTransport()) {
			c.gridx = 3;
			c.gridy = 0;
			addInput(elementPanel, c, "Maximum Electricity Throughput", 
					new JLabel(NumberFormat.getNumberInstance().format(
							ElectricityUnits.convertFlow(element.getMaxElectricityInput(), 
									element, this)), JLabel.RIGHT), 
									electricityUnits + "/" + electricityTimeUnits);
			if(detailed) {
				addInput(elementPanel, c, "Max Electricity Input", 
						maxElectricityInputText,
						electricityUnits + "/" + electricityTimeUnits);
				addInput(elementPanel, c, "Initial Electricity Input", 
						initialElectricityInputText, 
						electricityUnits + "/" + electricityTimeUnits);
				addInput(elementPanel, c, "Distribution Efficiency",
						distributionEfficiencyText, 
						electricityUnits + "/" + electricityUnits,
						"  max: ", maxOutputLabel,
						electricityUnits + "/" + electricityTimeUnits);
				addInput(elementPanel, c, "Variable Cost of Distribution",
						variableOperationsCostOfElectricityDistributionText,
						currencyUnits + "/" + electricityUnits,
						"  max: ", maxVariableCostDistLabel,
						currencyUnits + "/" + currencyTimeUnits);
			}
		}

		maxElectricityProductionText.setEnabled(element.getTemplateName() == null);
		petroleumIntensityOfElectricityProductionText.setEnabled(element.getTemplateName() == null);
		waterIntensityOfElectricityProductionText.setEnabled(element.getTemplateName() == null);
		variableOperationsCostOfElectricityProductionText.setEnabled(element.getTemplateName() == null);
		maxElectricityInputText.setEnabled(element.getTemplateName() == null);
		distributionEfficiencyText.setEnabled(element.getTemplateName() == null);
		variableOperationsCostOfElectricityDistributionText.setEnabled(element.getTemplateName() == null);
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

	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}
}
