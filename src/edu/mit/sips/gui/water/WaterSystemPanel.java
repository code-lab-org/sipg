package edu.mit.sips.gui.water;

import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.gui.InfrastructureSystemPanel;

/**
 * The Class WaterSystemPanel.
 */
public abstract class WaterSystemPanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = 1082832327915006632L;

	/**
	 * Instantiates a new water system panel.
	 *
	 * @param waterSystem the water system
	 */
	public WaterSystemPanel(WaterSystem waterSystem) {
		super(waterSystem);
	}
}
