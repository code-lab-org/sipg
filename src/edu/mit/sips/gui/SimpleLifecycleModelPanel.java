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

import edu.mit.sips.core.lifecycle.MutableSimpleLifecycleModel;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.TimeUnitsOutput;

/**
 * The Class SimpleLifecycleModelPanel.
 */
public class SimpleLifecycleModelPanel extends LifecycleModelPanel implements CurrencyUnitsOutput, TimeUnitsOutput {
	private static final long serialVersionUID = 4823361209584020543L;
	
	private NumberFormat timeFormat;
	
	private final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	private final TimeUnits timeUnits = TimeUnits.year;
	
	private final JFormattedTextField timeAvailableText, timeInitializedText, 
			initializationDurationText, capitalCostText;
	private final JFormattedTextField fixedOperationsCostText;
	private final JFormattedTextField maxOperationsDurationText, 
			operationsDurationText, timeDecommissionedText, 
			decommissionDurationText, decommissionCostText;
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
		
		final SimpleLifecycleModelPanel thisPanel = this;
		
		c.gridx = 0;
		timeAvailableText = new JFormattedTextField(timeFormat);
		timeAvailableText.setColumns(10);
		timeAvailableText.setHorizontalAlignment(JTextField.RIGHT);
		timeAvailableText.setValue((long) TimeUnits.convert(
				lifecycleModel.getTimeAvailable(), lifecycleModel, this));
		timeAvailableText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setTimeAvailable((long) TimeUnits.convert(
									(Long) timeAvailableText.getValue(), 
									thisPanel, lifecycleModel));
							timeAvailableText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							timeAvailableText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Year Available", timeAvailableText, "");
		timeInitializedText = new JFormattedTextField(timeFormat);
		timeInitializedText.setColumns(10);
		timeInitializedText.setHorizontalAlignment(JTextField.RIGHT);
		timeInitializedText.setValue((long) TimeUnits.convert(
				lifecycleModel.getTimeInitialized(), lifecycleModel, this));
		// TODO temporary to only allow initialization editing after 1980
		timeInitializedText.setEnabled(lifecycleModel.getTimeInitialized() >= 1980);
		timeInitializedText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							long timeInitialized = (long) TimeUnits.convert(
									(Long) timeInitializedText.getValue(), 
									thisPanel, lifecycleModel);
							// TODO temporary to only allow initialization after 1980
							if(timeInitialized < 1980) {
								throw new NumberFormatException(
										"Initialization time must be >= 1980.");
							}
							lifecycleModel.setTimeInitialized(timeInitialized);
							timeInitializedText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							timeInitializedText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Year Initialized", timeInitializedText, "");
		initializationDurationText = new JFormattedTextField(NumberFormat.getIntegerInstance());
		initializationDurationText.setColumns(10);
		initializationDurationText.setHorizontalAlignment(JTextField.RIGHT);
		initializationDurationText.setValue((long) TimeUnits.convert(
				lifecycleModel.getInitializationDuration(), lifecycleModel, this));
		initializationDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setInitializationDuration((long) TimeUnits.convert(
									(Long) initializationDurationText.getValue(), 
									thisPanel, lifecycleModel));
							initializationDurationText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initializationDurationText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Initialization Duration", initializationDurationText, timeUnits.toString());
		capitalCostText = new JFormattedTextField(NumberFormat.getNumberInstance());
		capitalCostText.setColumns(10);
		capitalCostText.setHorizontalAlignment(JTextField.RIGHT);
		capitalCostText.setValue(CurrencyUnits.convertStock(
				lifecycleModel.getCapitalCost(), lifecycleModel, thisPanel));
		capitalCostText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setCapitalCost(CurrencyUnits.convertStock(
									((Number) capitalCostText.getValue()).doubleValue(),
									thisPanel, lifecycleModel));
							capitalCostText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							capitalCostText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Capital Cost", capitalCostText, currencyUnits.getAbbreviation());
		fixedOperationsCostText = new JFormattedTextField(NumberFormat.getNumberInstance());
		fixedOperationsCostText.setColumns(10);
		fixedOperationsCostText.setHorizontalAlignment(JTextField.RIGHT);
		fixedOperationsCostText.setValue(CurrencyUnits.convertFlow(
				lifecycleModel.getFixedOperationsCost(), lifecycleModel, thisPanel));
		fixedOperationsCostText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setFixedOperationsCost(CurrencyUnits.convertFlow(
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
				lifecycleModel.getOperationsDuration(), lifecycleModel, this));
		operationsDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setOperationsDuration((long) TimeUnits.convert(
									(Long) operationsDurationText.getValue(), 
									thisPanel, lifecycleModel));
							operationsDurationText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							operationsDurationText.setForeground(Color.red);
						}
					}
				});
		//TODO addInput(c, "Operations Duration", operationsDurationText, timeUnits.toString());
		timeDecommissionedText = new JFormattedTextField(timeFormat);
		timeDecommissionedText.setColumns(10);
		timeDecommissionedText.setHorizontalAlignment(JTextField.RIGHT);
		timeDecommissionedText.setValue((long) TimeUnits.convert(
				lifecycleModel.getTimeDecommissioned(), lifecycleModel, this));
		// TODO temporary to only allow initialization editing after 1980
		timeDecommissionedText.setEnabled(lifecycleModel.getTimeDecommissioned() >= 1980);
		timeDecommissionedText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							long timeDecommissioned = (long) TimeUnits.convert(
									(Long) timeDecommissionedText.getValue(), 
									thisPanel, lifecycleModel);
							// TODO temporary to only allow decommission after 1980
							if(timeDecommissioned < 1980) {
								throw new NumberFormatException(
										"Decommission time must be >= 1980.");
							}
							lifecycleModel.setTimeDecommissioned(timeDecommissioned);
							timeDecommissionedText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							timeDecommissionedText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Year Decommissioned", timeDecommissionedText, "");
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
				lifecycleModel.getDecommissionCost(), lifecycleModel, this));
		decommissionCostText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							lifecycleModel.setDecommissionCost(CurrencyUnits.convertStock(
									((Number) decommissionCostText.getValue()).doubleValue(), 
									thisPanel, lifecycleModel));
							decommissionCostText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							decommissionCostText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Decommission Cost", decommissionCostText, currencyUnits.toString());
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
	public void setTemplateMode(String templateName) {
		timeAvailableText.setEnabled(templateName == null);
		initializationDurationText.setEnabled(templateName == null);
		capitalCostText.setEnabled(templateName == null);
		fixedOperationsCostText.setEnabled(templateName == null);
		maxOperationsDurationText.setEnabled(templateName == null);
		decommissionDurationText.setEnabled(templateName == null);
		decommissionCostText.setEnabled(templateName == null);
		levelizeCostsCheck.setEnabled(templateName == null);
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnits()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyTimeUnits()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return timeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.TimeUnitsOutput#getTimeUnits()
	 */
	@Override
	public TimeUnits getTimeUnits() {
		return timeUnits;
	}
}
