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
package edu.mit.sips.gui.base;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;

import edu.mit.sips.core.lifecycle.EditableSimpleLifecycleModel;
import edu.mit.sips.gui.DocumentChangeListener;
import edu.mit.sips.scenario.Scenario;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.TimeUnitsOutput;

/**
 * A lifecycle model panel implementation for 
 * the simple lifecycle model interface.
 * 
 * @author Paul T. Grogan
 */
public class SimpleLifecycleModelPanel extends LifecycleModelPanel implements CurrencyUnitsOutput, TimeUnitsOutput {
	private static final long serialVersionUID = 4823361209584020543L;
	
	private NumberFormat timeFormat;
	
	private final CurrencyUnits currencyUnits = CurrencyUnits.Msim;
	private final TimeUnits timeUnits = TimeUnits.year;
	
	private final JFormattedTextField timeAvailableText, timeCommissionStartText, 
			commissionDurationText, capitalCostText;
	private final JFormattedTextField fixedOperationsCostText;
	private final JFormattedTextField maxOperationsDurationText, 
			operationsDurationText, timeDecommissionStartText, 
			decommissionDurationText, decommissionCostText;
	private final JCheckBox spreadCostsCheck;
	
	/**
	 * Instantiates a new simple lifecycle model panel.
	 *
	 * @param lifecycleModel the lifecycle model
	 */
	public SimpleLifecycleModelPanel(Scenario scenario, final EditableSimpleLifecycleModel lifecycleModel) {
		super(lifecycleModel);
		setLayout(new GridBagLayout());
		
		timeFormat = NumberFormat.getIntegerInstance();
		timeFormat.setGroupingUsed(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);
		
		final SimpleLifecycleModelPanel thisPanel = this;
		
		c.gridx = 0;
		timeAvailableText = new JFormattedTextField(timeFormat);
		timeAvailableText.setColumns(10);
		timeAvailableText.setHorizontalAlignment(JTextField.RIGHT);
		timeAvailableText.setValue((long) TimeUnits.convert(
				lifecycleModel.getMinTimeCommissionStart(), lifecycleModel, this));
		timeAvailableText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setMinTimeCommissionStart((long) TimeUnits.convert(
									(Long) timeAvailableText.getValue(), 
									thisPanel, lifecycleModel));
							timeAvailableText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							timeAvailableText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Year Available", timeAvailableText, "");
		timeCommissionStartText = new JFormattedTextField(timeFormat);
		timeCommissionStartText.setColumns(10);
		timeCommissionStartText.setHorizontalAlignment(JTextField.RIGHT);
		timeCommissionStartText.setValue((long) TimeUnits.convert(
				lifecycleModel.getTimeCommissionStart(), lifecycleModel, this));
		timeCommissionStartText.setEnabled(lifecycleModel.getTimeCommissionStart() >= scenario.getPresentTime());
		timeCommissionStartText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							long timeInitialized = (long) TimeUnits.convert(
									(Long) timeCommissionStartText.getValue(), 
									thisPanel, lifecycleModel);
							if(timeInitialized < scenario.getPresentTime()) {
								throw new NumberFormatException(
										"Commisstion start time must be after present time.");
							}
							lifecycleModel.setTimeCommissionStart(timeInitialized);
							timeCommissionStartText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							timeCommissionStartText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Year Commissioned", timeCommissionStartText, "");
		commissionDurationText = new JFormattedTextField(NumberFormat.getIntegerInstance());
		commissionDurationText.setColumns(10);
		commissionDurationText.setHorizontalAlignment(JTextField.RIGHT);
		commissionDurationText.setValue((long) TimeUnits.convert(
				lifecycleModel.getCommissionDuration(), lifecycleModel, this));
		commissionDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setCommissionDuration((long) TimeUnits.convert(
									(Long) commissionDurationText.getValue(), 
									thisPanel, lifecycleModel));
							commissionDurationText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							commissionDurationText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Commission Duration", commissionDurationText, timeUnits.toString());
		capitalCostText = new JFormattedTextField(NumberFormat.getNumberInstance());
		capitalCostText.setColumns(10);
		capitalCostText.setHorizontalAlignment(JTextField.RIGHT);
		capitalCostText.setValue(CurrencyUnits.convertStock(
				lifecycleModel.getTotalCommissionCost(), lifecycleModel, thisPanel));
		capitalCostText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setTotalCommissionCost(CurrencyUnits.convertStock(
									((Number) capitalCostText.getValue()).doubleValue(),
									thisPanel, lifecycleModel));
							capitalCostText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							capitalCostText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Commission Cost", capitalCostText, currencyUnits.getAbbreviation());
		fixedOperationsCostText = new JFormattedTextField(NumberFormat.getNumberInstance());
		fixedOperationsCostText.setColumns(10);
		fixedOperationsCostText.setHorizontalAlignment(JTextField.RIGHT);
		fixedOperationsCostText.setValue(CurrencyUnits.convertFlow(
				lifecycleModel.getFixedOperationsCost(), lifecycleModel, thisPanel));
		fixedOperationsCostText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setFixedOperationCost(CurrencyUnits.convertFlow(
									((Number) fixedOperationsCostText.getValue()).doubleValue(),
									thisPanel, lifecycleModel));
							fixedOperationsCostText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							fixedOperationsCostText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Fixed Operations Cost", fixedOperationsCostText, currencyUnits + "/" + timeUnits);

		c.gridx = 3;
		c.gridy = 0;
		
		maxOperationsDurationText = new JFormattedTextField(NumberFormat.getIntegerInstance());
		maxOperationsDurationText.setColumns(10);
		maxOperationsDurationText.setHorizontalAlignment(JTextField.RIGHT);
		maxOperationsDurationText.setValue((long) TimeUnits.convert(
				lifecycleModel.getMaxOperationsDuration(), lifecycleModel, this));
		maxOperationsDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setMaxOperationsDuration((long) TimeUnits.convert(
									(Long) maxOperationsDurationText.getValue(), 
									thisPanel, lifecycleModel));
							maxOperationsDurationText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxOperationsDurationText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Max Operations Duration", maxOperationsDurationText, timeUnits.toString());
		operationsDurationText = new JFormattedTextField(NumberFormat.getIntegerInstance());
		operationsDurationText.setColumns(10);
		operationsDurationText.setHorizontalAlignment(JTextField.RIGHT);
		operationsDurationText.setValue((long) TimeUnits.convert(
				lifecycleModel.getOperationDuration(), lifecycleModel, this));
		operationsDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							long operationsDuration = (long) TimeUnits.convert(
									(Long) operationsDurationText.getValue(),
									thisPanel, lifecycleModel);
							if(operationsDuration > lifecycleModel.getMaxOperationsDuration()) {
								throw new IllegalArgumentException(
										"Operations duration cannot exceed maximum.");
							}
							lifecycleModel.setOperationDuration(operationsDuration);
							operationsDurationText.setForeground(Color.black);
						} catch(IllegalArgumentException e) {
							operationsDurationText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Operations Duration", operationsDurationText, timeUnits.toString());
		timeDecommissionStartText = new JFormattedTextField(timeFormat);
		timeDecommissionStartText.setColumns(10);
		timeDecommissionStartText.setHorizontalAlignment(JTextField.RIGHT);
		timeDecommissionStartText.setValue((long) TimeUnits.convert(
				lifecycleModel.getTimeDecommissionStart(), lifecycleModel, this));
		timeDecommissionStartText.setEnabled(lifecycleModel.getTimeDecommissionStart() >= scenario.getPresentTime());
		timeDecommissionStartText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							long timeDecommissioned = (long) TimeUnits.convert(
									(Long) timeDecommissionStartText.getValue(), 
									thisPanel, lifecycleModel);
							if(timeDecommissioned < scenario.getPresentTime()) {
								throw new IllegalArgumentException(
										"Decommission time must be after present time.");
							} else if(timeDecommissioned > lifecycleModel.getMaxTimeDecommissionStart()) {
								throw new IllegalArgumentException(
										"Decommission time cannot exceed maximum operations time.");
							}
							lifecycleModel.setTimeDecommissionStart(timeDecommissioned);
							timeDecommissionStartText.setForeground(Color.black);
						} catch(IllegalArgumentException e) {
							timeDecommissionStartText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Year Decommissioned", timeDecommissionStartText, "");
		decommissionDurationText = new JFormattedTextField(NumberFormat.getIntegerInstance());
		decommissionDurationText.setColumns(10);
		decommissionDurationText.setHorizontalAlignment(JTextField.RIGHT);
		decommissionDurationText.setValue((long) TimeUnits.convert(
				lifecycleModel.getDecommissionDuration(), lifecycleModel, this));
		decommissionDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setDecommissionDuration((long) TimeUnits.convert(
									(Long) decommissionDurationText.getValue(), 
									thisPanel, lifecycleModel));
							decommissionDurationText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							decommissionDurationText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Decommission Duration", decommissionDurationText, timeUnits.toString());
		decommissionCostText = new JFormattedTextField(NumberFormat.getNumberInstance());
		decommissionCostText.setColumns(10);
		decommissionCostText.setHorizontalAlignment(JTextField.RIGHT);
		decommissionCostText.setValue(CurrencyUnits.convertStock(
				lifecycleModel.getTotalDecommissionCost(), lifecycleModel, this));
		decommissionCostText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setTotalDecommissionCost(CurrencyUnits.convertStock(
									((Number) decommissionCostText.getValue()).doubleValue(), 
									thisPanel, lifecycleModel));
							decommissionCostText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							decommissionCostText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Decommission Cost", decommissionCostText, currencyUnits.toString());
		spreadCostsCheck = new JCheckBox();
		spreadCostsCheck.setSelected(lifecycleModel.isSpreadCosts());
		spreadCostsCheck.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				lifecycleModel.setSpreadCosts(spreadCostsCheck.isSelected());
			}
		});
		addInput(c, "Spread Costs", spreadCostsCheck, "");
	}
	
	/**
	 * Adds the input.
	 *
	 * @param c the c
	 * @param labelText the label text
	 * @param component the component
	 * @param units the units
	 */
	private void addInput(GridBagConstraints c, String labelText, 
			JComponent component, String units) {
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel(labelText), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(component, c);
		c.gridx++;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel(units), c);
		c.gridy++;
		c.gridx-=2;
	}
	
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return timeUnits;
	}

	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	@Override
	public TimeUnits getTimeUnits() {
		return timeUnits;
	}

	@Override
	public void setTemplateMode(String templateName) {
		timeAvailableText.setEnabled(templateName == null);
		commissionDurationText.setEnabled(templateName == null);
		capitalCostText.setEnabled(templateName == null);
		fixedOperationsCostText.setEnabled(templateName == null);
		maxOperationsDurationText.setEnabled(templateName == null);
		decommissionDurationText.setEnabled(templateName == null);
		decommissionCostText.setEnabled(templateName == null);
		spreadCostsCheck.setEnabled(templateName == null);
	}
}
