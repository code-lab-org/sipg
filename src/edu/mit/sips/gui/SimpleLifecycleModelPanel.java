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
import javax.swing.JTextField;

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
	private final JFormattedTextField maxOperationsDurationText, 
			operationsDurationText, decommissionDurationText, 
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
		timeAvailableText.setHorizontalAlignment(JTextField.RIGHT);
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
		addInput(c, "Time Available", timeAvailableText, "(year)");
		timeInitializedText = new JFormattedTextField(timeFormat);
		timeInitializedText.setColumns(10);
		timeInitializedText.setHorizontalAlignment(JTextField.RIGHT);
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
		addInput(c, "Time Initialized", timeInitializedText, "(year)");
		initializationDurationText = new JFormattedTextField(NumberFormat.getIntegerInstance());
		initializationDurationText.setColumns(10);
		initializationDurationText.setHorizontalAlignment(JTextField.RIGHT);
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
		addInput(c, "Initialization Duration", initializationDurationText, "years");
		capitalCostText = new JFormattedTextField(NumberFormat.getNumberInstance());
		capitalCostText.setColumns(10);
		capitalCostText.setHorizontalAlignment(JTextField.RIGHT);
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
		addInput(c, "Capital Cost", capitalCostText, "SAR");
		fixedOperationsCostText = new JFormattedTextField(NumberFormat.getNumberInstance());
		fixedOperationsCostText.setColumns(10);
		fixedOperationsCostText.setHorizontalAlignment(JTextField.RIGHT);
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
		addInput(c, "Fixed Operations Cost", fixedOperationsCostText, "SAR/year");

		c.gridx = 3;
		c.gridy = 0;
		
		maxOperationsDurationText = new JFormattedTextField(NumberFormat.getIntegerInstance());
		maxOperationsDurationText.setColumns(10);
		maxOperationsDurationText.setHorizontalAlignment(JTextField.RIGHT);
		maxOperationsDurationText.setValue(lifecycleModel.getMaxOperationsDuration());
		maxOperationsDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setMaxOperationsDuration(
									(Long) maxOperationsDurationText.getValue());
							maxOperationsDurationText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxOperationsDurationText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Max Operations Duration", maxOperationsDurationText, "years");
		operationsDurationText = new JFormattedTextField(NumberFormat.getIntegerInstance());
		operationsDurationText.setColumns(10);
		operationsDurationText.setHorizontalAlignment(JTextField.RIGHT);
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
		addInput(c, "Operations Duration", operationsDurationText, "years");
		decommissionDurationText = new JFormattedTextField(NumberFormat.getIntegerInstance());
		decommissionDurationText.setColumns(10);
		decommissionDurationText.setHorizontalAlignment(JTextField.RIGHT);
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
		addInput(c, "Decommission Duration", decommissionDurationText, "years");
		decommissionCostText = new JFormattedTextField(NumberFormat.getNumberInstance());
		decommissionCostText.setColumns(10);
		decommissionCostText.setHorizontalAlignment(JTextField.RIGHT);
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
		addInput(c, "Decommission Cost", decommissionCostText, "SAR");
		levelizeCostsCheck = new JCheckBox();
		levelizeCostsCheck.setSelected(lifecycleModel.isLevelizeCosts());
		levelizeCostsCheck.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				lifecycleModel.setLevelizeCosts(levelizeCostsCheck.isSelected());
			}
		});
		addInput(c, "Levelize Costs", levelizeCostsCheck, "");
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
		maxOperationsDurationText.setEnabled(template == null);
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
}
