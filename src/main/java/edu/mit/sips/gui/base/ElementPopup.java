/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sips.gui.base;

import java.awt.BorderLayout;

import javax.swing.JPopupMenu;

import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.core.electricity.ElectricityElement;
import edu.mit.sips.core.petroleum.PetroleumElement;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.gui.UpdateListener;
import edu.mit.sips.gui.agriculture.AgriculturePopupInfoPanel;
import edu.mit.sips.gui.electricity.ElectricityPopupInfoPanel;
import edu.mit.sips.gui.petroleum.PetroleumPopupInfoPanel;
import edu.mit.sips.gui.water.WaterPopupInfoPanel;
import edu.mit.sips.sim.Simulator;

/**
 * Generic pop-up menu to display information about infrastructure elements.
 * 
 * @author Paul T. Grogan
 */
public class ElementPopup extends JPopupMenu implements UpdateListener {
	private static final long serialVersionUID = -3143445528896866224L;
	
	/**
	 * Creates the pop-up panel.
	 *
	 * @param element the element
	 * @return the popup info panel
	 */
	private static PopupInfoPanel createPopupInfoPanel(InfrastructureElement element) {
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
	
	private final Simulator simulator;
	private InfrastructureElement element;
		
	private PopupInfoPanel infoPanel;
	
	/**
	 * Instantiates a new element pop-up.
	 *
	 * @param simulator the simulator
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

	@Override
	public void simulationCompleted(UpdateEvent event) { }

	@Override
	public void simulationInitialized(UpdateEvent event) {
		updateFields(event.getTime());
	}
	
	@Override
	public void simulationUpdated(UpdateEvent event) {
		updateFields(event.getTime());
	}
	
	/**
	 * Update fields.
	 *
	 * @param time the time
	 */
	private void updateFields(long time) {
		if(infoPanel != null) {
			infoPanel.updateFields(time);
		}
	}
}
