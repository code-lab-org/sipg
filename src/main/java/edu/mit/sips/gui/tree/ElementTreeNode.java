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

import java.util.Collection;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.mit.sips.core.base.InfrastructureElement;

/**
 * A tree node that represents an infrastructure element.
 * 
 * @author Paul T. Grogan
 */
public class ElementTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 8023532879472878274L;

	/**
	 * Instantiates a new element tree node.
	 *
	 * @param element the element
	 * @param allElements the all elements
	 */
	public ElementTreeNode(InfrastructureElement element, 
			Collection<? extends InfrastructureElement> allElements) {
		super(element);
	}

	@Override
	public InfrastructureElement getUserObject() {
		return (InfrastructureElement) super.getUserObject();
	}

	@Override
	public DefaultMutableTreeNode getParent() {
		return (DefaultMutableTreeNode) super.getParent();
	}
}