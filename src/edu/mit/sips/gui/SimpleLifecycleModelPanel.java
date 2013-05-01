package edu.mit.sips.gui;

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

import edu.mit.sips.ElementTemplate;
import edu.mit.sips.core.MutableSimpleLifecycleModel;

/**
 * The Class SimpleLifecycleModelPanel.
 */
public class SimpleLifecycleModelPanel extends LifecycleModelPanel {
	private static final long serialVersionUID = 4823361209584020543L;
	
	private NumberFormat timeFormat;
	
	private final JFormattedTextField timeAvailableText, timeInitializedText, 
			initializationDurationText, capitalCostText;
	private final JFormattedTextField fixedOperationsCostText;
	private final JFormattedTextField operationsDurationText, decommissionDurationText, 
			decommissionCostText;
	private final JCheckBox levelizeCostsCheck;
	
	/**
	 * Instantiates a new simple lifecycle model panel.
	 *
	 * @param lifecycleModel the lifecycle model
	 */
	public SimpleLifecycleModelPanel(final MutableSimpleLifecycleModel lifecycleModel) {
		super(lifecycleModel);
		setLayout(new GridBagLayout());
		
		timeFormat = NumberFormat.getIntegerInstance();
		timeFormat.setGroupingUsed(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);
		
		c.gridx = 0;
		timeAvailableText = new JFormattedTextField(timeFormat);
		timeAvailableText.setColumns(10);
		timeAvailableText.setValue(lifecycleModel.getTimeAvailable());
		timeAvailableText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setTimeAvailable(
									(Long) timeAvailableText.getValue());
							timeAvailableText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							timeAvailableText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Time Available (year)", timeAvailableText);
		timeInitializedText = new JFormattedTextField(timeFormat);
		timeInitializedText.setColumns(10);
		timeInitializedText.setValue(lifecycleModel.getTimeInitialized());
		timeInitializedText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setTimeInitialized(
									(Long) timeInitializedText.getValue());
							timeInitializedText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							timeInitializedText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Time Initialized (year)", timeInitializedText);
		initializationDurationText = new JFormattedTextField(NumberFormat.getIntegerInstance());
		initializationDurationText.setColumns(10);
		initializationDurationText.setValue(lifecycleModel.getInitializationDuration());
		initializationDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setInitializationDuration(
									(Long) initializationDurationText.getValue());
							initializationDurationText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initializationDurationText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Initialization Duration (year)", initializationDurationText);
		capitalCostText = new JFormattedTextField(NumberFormat.getNumberInstance());
		capitalCostText.setColumns(10);
		capitalCostText.setValue(lifecycleModel.getCapitalCost());
		capitalCostText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setCapitalCost(
									((Number) capitalCostText.getValue()).doubleValue());
							capitalCostText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							capitalCostText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Capital Cost (SAR)", capitalCostText);
		fixedOperationsCostText = new JFormattedTextField(NumberFormat.getNumberInstance());
		fixedOperationsCostText.setColumns(10);
		fixedOperationsCostText.setValue(lifecycleModel.getFixedOperationsCost());
		fixedOperationsCostText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setFixedOperationsCost(
									((Number) fixedOperationsCostText.getValue()).doubleValue());
							fixedOperationsCostText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							fixedOperationsCostText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Fixed Operations Cost (SAR/year)", fixedOperationsCostText);
		operationsDurationText = new JFormattedTextField(NumberFormat.getIntegerInstance());
		operationsDurationText.setColumns(10);
		operationsDurationText.setValue(lifecycleModel.getOperationsDuration());
		operationsDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setOperationsDuration(
									(Long) operationsDurationText.getValue());
							operationsDurationText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							operationsDurationText.setForeground(Color.red);
						}
					}
				});
		c.gridx = 2;
		c.gridy = 0;
		addInput(c, "Operations Duration (year)", operationsDurationText);
		decommissionDurationText = new JFormattedTextField(NumberFormat.getIntegerInstance());
		decommissionDurationText.setColumns(10);
		decommissionDurationText.setValue(lifecycleModel.getDecommissionDuration());
		decommissionDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setDecommissionDuration(
									(Long) decommissionDurationText.getValue());
							decommissionDurationText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							decommissionDurationText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Decommission Duration (year)", decommissionDurationText);
		decommissionCostText = new JFormattedTextField(NumberFormat.getNumberInstance());
		decommissionCostText.setColumns(10);
		decommissionCostText.setValue(lifecycleModel.getDecommissionCost());
		decommissionCostText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setDecommissionCost(
									((Number) decommissionCostText.getValue()).doubleValue());
							decommissionCostText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							decommissionCostText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Decommission Cost (SAR)", decommissionCostText);
		levelizeCostsCheck = new JCheckBox();
		levelizeCostsCheck.setSelected(lifecycleModel.isLevelizeCosts());
		levelizeCostsCheck.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				lifecycleModel.setLevelizeCosts(levelizeCostsCheck.isSelected());
			}
		});
		addInput(c, "Levelize Costs", levelizeCostsCheck);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.LifecycleModelPanel#setTemplateMode(boolean)
	 */
	@Override
	public void setTemplateMode(ElementTemplate template) {
		timeAvailableText.setEnabled(template == null);
		initializationDurationText.setEnabled(template == null);
		capitalCostText.setEnabled(template == null);
		fixedOperationsCostText.setEnabled(template == null);
		decommissionDurationText.setEnabled(template == null);
		decommissionCostText.setEnabled(template == null);
		levelizeCostsCheck.setEnabled(template == null);
	}
	
	/**
	 * Adds the input.
	 *
	 * @param c the c
	 * @param labelText the label text
	 * @param component the component
	 */
	private void addInput(GridBagConstraints c, String labelText, 
			JComponent component) {
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel(labelText), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(component, c);
		c.gridy++;
		c.gridx--;
	}
}
