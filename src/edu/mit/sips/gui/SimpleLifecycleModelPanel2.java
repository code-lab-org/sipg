package edu.mit.sips.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

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
public class SimpleLifecycleModelPanel2 extends LifecycleModelPanel implements CurrencyUnitsOutput, TimeUnitsOutput {
	private static final long serialVersionUID = 4823361209584020543L;
	
	private NumberFormat timeFormat;
	private NumberFormat decimalFormat;
	
	private final CurrencyUnits currencyUnits = CurrencyUnits.Msim;
	private final TimeUnits timeUnits = TimeUnits.year;
	
	private final JFormattedTextField timeInitializedText,
		operationsDurationText;
	private final JLabel timeOperationalLabel, levelizedCapitalCostLabel, 
		timeDecommissionedLabel;
	
	/**
	 * Instantiates a new simple lifecycle model panel.
	 *
	 * @param lifecycleModel the lifecycle model
	 */
	public SimpleLifecycleModelPanel2(final MutableSimpleLifecycleModel lifecycleModel) {
		super(lifecycleModel);
		setLayout(new GridBagLayout());
		
		timeFormat = NumberFormat.getIntegerInstance();
		timeFormat.setGroupingUsed(false);
		
		decimalFormat = NumberFormat.getNumberInstance();
		decimalFormat.setMaximumFractionDigits(2);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);
		
		final SimpleLifecycleModelPanel2 thisPanel = this;
		
		c.gridx = 0;
		timeInitializedText = new JFormattedTextField(timeFormat);
		timeInitializedText.setColumns(5);
		timeInitializedText.setHorizontalAlignment(JTextField.RIGHT);
		timeInitializedText.setValue(lifecycleModel.getTimeInitialized());
		timeInitializedText.setEnabled(lifecycleModel.getTimeInitialized() >= 1980);
		timeInitializedText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							long timeInitialized = (Long) timeInitializedText.getValue();
							// TODO temporary to only allow initialization after 1980
							if(timeInitialized < 1980) {
								throw new NumberFormatException(
										"Initialization time must be >= 1980.");
							}
							lifecycleModel.setTimeInitialized(timeInitialized);
							timeOperationalLabel.setText(timeFormat.format(
									lifecycleModel.getTimeInitialized() 
											+ lifecycleModel.getInitializationDuration()));
							timeDecommissionedLabel.setText(timeFormat.format(
									lifecycleModel.getTimeDecommissioned()));
							timeInitializedText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							timeInitializedText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Initialized", timeInitializedText, "");
		
		operationsDurationText = new JFormattedTextField(timeFormat);
		operationsDurationText.setColumns(5);
		operationsDurationText.setHorizontalAlignment(JTextField.RIGHT);
		operationsDurationText.setValue((long) TimeUnits.convert(
				lifecycleModel.getOperationsDuration(), lifecycleModel, this));
		operationsDurationText.setEnabled(lifecycleModel.getTimeDecommissioned() >= 1980);
		operationsDurationText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							long operationsDuration = (Long) operationsDurationText.getValue();
							// TODO temporary to only allow decommission after 1980
							if(lifecycleModel.getTimeInitialized() 
									+ lifecycleModel.getInitializationDuration() 
									+ operationsDuration < 1980) {
								throw new IllegalArgumentException(
										"Decommission time must be >= 1980.");
							} else if(operationsDuration > lifecycleModel.getMaxOperationsDuration()) {
								throw new IllegalArgumentException(
										"Decommission time cannot exceed maximum operations time.");
							}
							lifecycleModel.setOperationsDuration(operationsDuration);
							timeDecommissionedLabel.setText(timeFormat.format(
									lifecycleModel.getTimeDecommissioned()));
							operationsDurationText.setForeground(Color.black);
						} catch(IllegalArgumentException e) {
							operationsDurationText.setForeground(Color.red);
						}
					}
				});
		addInput(c, "Operational for", operationsDurationText, "yr");

		c.gridx = 3;
		c.gridy = 0;
		
		timeOperationalLabel = new JLabel();
		timeOperationalLabel.setText(timeFormat.format(
				lifecycleModel.getTimeInitialized() 
						+ lifecycleModel.getInitializationDuration()));
		addInput(c, "Operational in", timeOperationalLabel, "");
		
		timeDecommissionedLabel = new JLabel();
		timeDecommissionedLabel.setText(timeFormat.format(
				lifecycleModel.getTimeDecommissioned()));
		addInput(c, "Decommissioned in", timeDecommissionedLabel, "");

		c.gridx = 0;
		
		levelizedCapitalCostLabel = new JLabel();
		levelizedCapitalCostLabel.setHorizontalAlignment(JLabel.RIGHT);
		levelizedCapitalCostLabel.setText(decimalFormat.format(
				CurrencyUnits.convertFlow(
						lifecycleModel.getInitializationDuration() == 0?
								lifecycleModel.getCapitalCost() : 
									lifecycleModel.getCapitalCost() 
									/ lifecycleModel.getInitializationDuration(), 
									lifecycleModel, thisPanel)));
		addInput(c, "Capital Cost", levelizedCapitalCostLabel, 
				currencyUnits.getAbbreviation() + "/yr for " + 
						Math.max(1,lifecycleModel.getInitializationDuration()) + " yr");

	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.LifecycleModelPanel#setTemplateMode(boolean)
	 */
	@Override
	public void setTemplateMode(String templateName) {
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
