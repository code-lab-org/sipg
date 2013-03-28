package edu.mit.sips.gui.energy;

import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.gui.InfrastructureSystemPanel;

/**
 * The Class EnergySystemPanel.
 */
public abstract class EnergySystemPanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = -7588345364891776159L;

	/**
	 * Instantiates a new energy system panel.
	 *
	 * @param energySystem the energy system
	 */
	public EnergySystemPanel(EnergySystem energySystem) {
		super(energySystem);
	}
}
