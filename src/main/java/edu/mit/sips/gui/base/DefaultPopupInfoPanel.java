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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

import edu.mit.sips.core.base.InfrastructureElement;

/**
 * The default implementation of the pop-up information panel.
 * 
 * @author Paul T. Grogan
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

	@Override
	public void updateFields(long time) {
		nameLabel.setText(element.getName() + " in " + time);
	}
}
