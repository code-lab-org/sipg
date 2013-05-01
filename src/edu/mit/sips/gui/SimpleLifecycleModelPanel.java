package edu.mit.sips.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import edu.mit.sips.ElementTemplate;
import edu.mit.sips.core.MutableSimpleLifecycleModel;

/**
 * The Class SimpleLifecycleModelPanel.
 */
public class SimpleLifecycleModelPanel extends LifecycleModelPanel {
	private static final long serialVersionUID = 4823361209584020543L;
	
	private final JTextField timeAvailableText, timeInitializedText, 
			initializationDurationText, capitalCostText;
	private final JTextField fixedOperationsCostText;
	private final JTextField operationsDurationText, decommissionDurationText, 
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
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);
		
		c.gridx = 0;
		timeAvailableText = new JTextField(10);
		timeAvailableText.setText(
				new Long(lifecycleModel.getTimeAvailable()).toString());
		timeAvailableText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setTimeAvailable(
									Long.parseLong(timeAvailableText.getText()));
							timeAvailableText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							timeAvailableText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Time Available (year)", timeAvailableText);
		timeInitializedText = new JTextField(10);
		timeInitializedText.setText(
				new Long(lifecycleModel.getTimeInitialized()).toString());
		timeInitializedText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setTimeInitialized(
									Long.parseLong(timeInitializedText.getText()));
							timeInitializedText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							timeInitializedText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Time Initialized (year)", timeInitializedText);
		initializationDurationText = new JTextField(10);
		initializationDurationText.setText(
				new Long(lifecycleModel.getInitializationDuration()).toString());
		initializationDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setInitializationDuration(
									Long.parseLong(initializationDurationText.getText()));
							initializationDurationText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initializationDurationText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Initialization Duration (year)", initializationDurationText);
		capitalCostText = new JTextField(10);
		capitalCostText.setText(
				new Double(lifecycleModel.getCapitalCost()).toString());
		capitalCostText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setCapitalCost(
									Double.parseDouble(capitalCostText.getText()));
							capitalCostText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							capitalCostText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Capital Cost (SAR)", capitalCostText);
		fixedOperationsCostText = new JTextField(10);
		fixedOperationsCostText.setText(
				new Double(lifecycleModel.getFixedOperationsCost()).toString());
		fixedOperationsCostText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setFixedOperationsCost(
									Double.parseDouble(fixedOperationsCostText.getText()));
							fixedOperationsCostText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							fixedOperationsCostText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Fixed Operations Cost (SAR/year)", fixedOperationsCostText);
		operationsDurationText = new JTextField(10);
		operationsDurationText.setText(
				new Long(lifecycleModel.getOperationsDuration()).toString());
		operationsDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setOperationsDuration(
									Long.parseLong(operationsDurationText.getText()));
							operationsDurationText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							operationsDurationText.setForeground(Color.red);
						}
					}
				});
		c.gridx = 2;
		c.gridy = 0;
		addInput(c, "Operations Duration (year)", operationsDurationText);
		decommissionDurationText = new JTextField(10);
		decommissionDurationText.setText(
				new Long(lifecycleModel.getDecommissionDuration()).toString());
		decommissionDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setDecommissionDuration(
									Long.parseLong(decommissionDurationText.getText()));
							decommissionDurationText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							decommissionDurationText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Decommission Duration (year)", decommissionDurationText);
		decommissionCostText = new JTextField(10);
		decommissionCostText.setText(
				new Double(lifecycleModel.getDecommissionCost()).toString());
		decommissionCostText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setDecommissionCost(
									Double.parseDouble(decommissionCostText.getText()));
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
