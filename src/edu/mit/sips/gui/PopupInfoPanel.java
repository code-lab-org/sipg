package edu.mit.sips.gui;

import javax.swing.JPanel;

/**
 * The Class PopupInfoPanel.
 */
public abstract class PopupInfoPanel extends JPanel {
	private static final long serialVersionUID = 6930776734882334393L;
	
	/**
	 * Update fields.
	 */
	public abstract void updateFields(long time);
}
