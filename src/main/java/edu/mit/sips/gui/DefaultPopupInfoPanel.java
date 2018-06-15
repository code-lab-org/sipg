package edu.mit.sips.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

import edu.mit.sips.core.InfrastructureElement;

/**
 * The Class DefaultPopupInfoPanel.
 */
public class DefaultPopupInfoPanel extends PopupInfoPanel {
	private static final long serialVersionUID = -6344803820905475328L;

	private final JLabel nameLabel;
	private final InfrastructureElement element;
	private final GridBagConstraints c = new GridBagConstraints();
	
	/**
	 * Instantiates a new default popup info panel.
	 *
	 * @param element the element
	 */
	public DefaultPopupInfoPanel(InfrastructureElement element) {
		this.element = element;
		nameLabel = new JLabel(element.getName());
		
		setLayout(new GridBagLayout());
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		
		c.gridwidth = 3;
		add(nameLabel, c);
		c.gridwidth = 1;
		c.gridy++;
	}
	
	/**
	 * Adds the field.
	 *
	 * @param label the label
	 * @param component the component
	 * @param units the units
	 */
	protected void addField(String label, Component component, String units) {
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel(label), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(component, c);
		c.gridx++;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel(units), c);
		c.gridy++;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.PopupInfoPanel#updateFields()
	 */
	@Override
	public void updateFields(long time) {
		nameLabel.setText(element.getName() + " in " + time);
	}
}
