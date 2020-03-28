package edu.mit.sips.gui.petroleum;

import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.gui.base.InfrastructureSystemPanel;

/**
 * The Class PetroleumSystemPanel.
 */
public abstract class PetroleumSystemPanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = -7588345364891776159L;

	/**
	 * Instantiates a new petroleum system panel.
	 *
	 * @param petroleumSystem the petroleum system
	 */
	public PetroleumSystemPanel(PetroleumSystem petroleumSystem) {
		super(petroleumSystem);
	}
}
