package edu.mit.sips.gui.agriculture;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.gui.base.InfrastructureSystemPanel;

/**
 * The Class AgricultureSystemPanel.
 */
public abstract class AgricultureSystemPanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = -3258858158886524542L;

	public AgricultureSystemPanel(AgricultureSystem agricultureSystem) {
		super(agricultureSystem);
	}
}
