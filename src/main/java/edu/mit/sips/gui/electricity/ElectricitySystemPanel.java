package edu.mit.sips.gui.electricity;

import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.gui.InfrastructureSystemPanel;

/**
 * The Class ElectricitySystemPanel.
 */
public abstract class ElectricitySystemPanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = -7588345364891776159L;

	/**
	 * Instantiates a new electricity system panel.
	 *
	 * @param electricitySystem the electricity system
	 */
	public ElectricitySystemPanel(ElectricitySystem electricitySystem) {
		super(electricitySystem);
	}
}
