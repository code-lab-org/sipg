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
package edu.mit.sips.gui.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.Region;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.base.DefaultInfrastructureElement;
import edu.mit.sips.core.electricity.ElectricityElement;
import edu.mit.sips.core.lifecycle.DefaultSimpleLifecycleModel;
import edu.mit.sips.core.petroleum.PetroleumElement;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.io.Icons;

/**
 * A customer renderer for element tree cells.
 * 
 * @author Paul T. Grogan
 */
public class ElementTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 5710965447037058342L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree,
			Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if(value instanceof SocietyTreeNode) {
			SocietyTreeNode node = (SocietyTreeNode) value;
			setText(node.getUserObject().getName());
			if(node.getUserObject() instanceof Country) {
				setIcon(Icons.COUNTRY);
			} else if(node.getUserObject() instanceof Region) {
				setIcon(Icons.REGION);
			} else if(node.getUserObject() instanceof City) {
				setIcon(Icons.CITY);
			}
		} else if(value instanceof ElementTreeNode) {
			ElementTreeNode node = (ElementTreeNode) value;
			if(node.getUserObject() instanceof DefaultInfrastructureElement) {
				DefaultInfrastructureElement element = (DefaultInfrastructureElement) node.getUserObject();
				if(element.getLifecycleModel() instanceof DefaultSimpleLifecycleModel) {
					DefaultSimpleLifecycleModel model = (DefaultSimpleLifecycleModel) element.getLifecycleModel();
					setText(element.getName() + " (" 
							+ (model.getTimeCommissionStart() 
									+ model.getCommissionDuration()) + "-" 
							+ model.getTimeDecommissionStart() + ")");
				} else {
					setText(element.getName());
				}
			} else {
				setText(node.getUserObject().getName());
			}
			if(node.getUserObject() instanceof AgricultureElement) {
				if(node.getUserObject().isOperational()) {
					setIcon(Icons.AGRICULTURE);
				} else {
					setIcon(Icons.AGRICULTURE_PLANNED);
				}
			} else if(node.getUserObject() instanceof WaterElement) {
				if(node.getUserObject().isOperational()) {
					setIcon(Icons.WATER);
				} else {
					setIcon(Icons.WATER_PLANNED);
				}
			} else if(node.getUserObject() instanceof ElectricityElement) {
				if(node.getUserObject().isOperational()) {
					setIcon(Icons.ELECTRICITY);
				} else {
					setIcon(Icons.ELECTRICITY_PLANNED);
				}
			} else if(node.getUserObject() instanceof PetroleumElement) {
				if(node.getUserObject().isOperational()) {
					setIcon(Icons.PETROLEUM);
				} else {
					setIcon(Icons.PETROLEUM_PLANNED);
				}
			}
		}
		return this;
	}
}
