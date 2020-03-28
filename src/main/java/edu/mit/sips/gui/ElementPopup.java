package edu.mit.sips.gui;

import java.awt.BorderLayout;

import javax.swing.JPopupMenu;

import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.core.electricity.ElectricityElement;
import edu.mit.sips.core.petroleum.PetroleumElement;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.gui.agriculture.AgriculturePopupInfoPanel;
import edu.mit.sips.gui.electricity.ElectricityPopupInfoPanel;
import edu.mit.sips.gui.petroleum.PetroleumPopupInfoPanel;
import edu.mit.sips.gui.water.WaterPopupInfoPanel;
import edu.mit.sips.sim.Simulator;

/**
 * The Class ElementPopup.
 */
public class ElementPopup extends JPopupMenu implements UpdateListener {
	private static final long serialVersionUID = -3143445528896866224L;
	
	private final Simulator simulator;
	private InfrastructureElement element;
	private PopupInfoPanel infoPanel;
		
	/**
	 * Instantiates a new element popup.
	 *
	 */
	public ElementPopup(Simulator simulator) {
		this.simulator = simulator;
		simulator.addUpdateListener(this);
		setLayout(new BorderLayout());
	}
	
	/**
	 * Gets the element.
	 *
	 * @return the element
	 */
	public InfrastructureElement getElement() {
		return element;
	}

	/**
	 * Sets the element.
	 *
	 * @param element the new element
	 */
	public void setElement(InfrastructureElement element) {
		this.element = element;
		if(infoPanel != null) {
			remove(infoPanel);
		}
		infoPanel = createPopupInfoPanel(element);
		add(infoPanel, BorderLayout.CENTER);
		updateFields(simulator.getTime());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationCompleted(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationCompleted(UpdateEvent event) {
		// nothing to do here
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationInitialized(UpdateEvent event) {
		updateFields(event.getTime());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		updateFields(event.getTime());
	}
	
	/**
	 * Update fields.
	 */
	private void updateFields(long time) {
		if(infoPanel != null) {
			infoPanel.updateFields(time);
		}
	}
	
	/**
	 * Creates the popup panel.
	 *
	 * @return the popup info panel
	 */
	private static PopupInfoPanel createPopupInfoPanel(InfrastructureElement element) {
		// TODO a more efficient implementation would cache created panels
		// rather than recreating for each element type
		if(element instanceof WaterElement) {
			return new WaterPopupInfoPanel((WaterElement)element);
		} else if(element instanceof AgricultureElement) {
			return new AgriculturePopupInfoPanel((AgricultureElement)element);
		} else if(element instanceof PetroleumElement) {
			return new PetroleumPopupInfoPanel((PetroleumElement)element);
		} else if(element instanceof ElectricityElement) {
			return new ElectricityPopupInfoPanel((ElectricityElement)element);
		}
		return new DefaultPopupInfoPanel(element);
	}
}
