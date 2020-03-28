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

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.mit.sips.core.Society;
import edu.mit.sips.core.base.InfrastructureElement;

/**
 * A tree model that represents a network of societies and infrastructure systems.
 * 
 * @author Paul T. Grogan
 */
public class NetworkTreeModel extends DefaultTreeModel {
	private static final long serialVersionUID = 7544013230179337273L;

	private Society society;
	
	/**
	 * Instantiates a new network tree model.
	 */
	public NetworkTreeModel() {
		super(new DefaultMutableTreeNode());
	}
	
	/**
	 * Initialize this tree with a top-level society.
	 *
	 * @param society the new state
	 */
	public void initialize(Society society) {
		this.society = society;
		setRoot(new SocietyTreeNode(society, society.getInternalElements()));
		nodeStructureChanged(getRoot());
	}
	
	/**
	 * Gets the element.
	 *
	 * @param path the path
	 * @return the element
	 */
	public InfrastructureElement getElement(TreePath path) {
		if(path == null) return null;
		if(path.getLastPathComponent() instanceof ElementTreeNode) {
			return ((ElementTreeNode)path.getLastPathComponent()).getUserObject();
		}
		return null;
	}
	
	/**
	 * Gets the society.
	 *
	 * @param path the path
	 * @return the society
	 */
	public Society getSociety(TreePath path) {
		if(path == null) return null;
		for(int i = path.getPathCount() - 1; i >= 0; i--) {
			if(path.getPathComponent(i) instanceof SocietyTreeNode) {
				return ((SocietyTreeNode)path.getPathComponent(i)).getUserObject();
			}
		}
		return null;
	}
	
	/**
	 * Gets the root.
	 *
	 * @return the root
	 */
	public DefaultMutableTreeNode getRoot() {
		if(super.getRoot() instanceof DefaultMutableTreeNode) {
			return (DefaultMutableTreeNode) super.getRoot();
		}
		return null;
	}
	
	/**
	 * Gets the society node.
	 *
	 * @param society the society
	 * @return the location node
	 */
	private SocietyTreeNode getSocietyNode(Society society) {
		for(Enumeration<?> e = getRoot().breadthFirstEnumeration(); e.hasMoreElements();) {
			Object o = e.nextElement();
			if(o instanceof SocietyTreeNode) {
				SocietyTreeNode locationNode = (SocietyTreeNode) o;
				if(locationNode.getUserObject().equals(society)) {
					return locationNode;
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets the element node.
	 *
	 * @param element the element
	 * @return the element node
	 */
	private ElementTreeNode getElementNode(InfrastructureElement element) {
		for(Enumeration<?> e = getRoot().breadthFirstEnumeration(); e.hasMoreElements();) {
			Object o = e.nextElement();
			if(o instanceof ElementTreeNode) {
				ElementTreeNode elementNode = (ElementTreeNode) o;
				if(elementNode.getUserObject().equals(element)) {
					return elementNode;
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets the parent node.
	 *
	 * @param element the element
	 * @return the parent node
	 */
	private DefaultMutableTreeNode getParentNode(InfrastructureElement element) {
		// check all children of tree
		for(Enumeration<?> e = getRoot().breadthFirstEnumeration(); e.hasMoreElements();) {
			Object o = e.nextElement();
			if(o instanceof SocietyTreeNode) {
				SocietyTreeNode parentNode = (SocietyTreeNode) o;
				if(element.getOrigin().equals(parentNode.getUserObject().getName())) {
					return parentNode;
				}
			}
		}
		// parent node not found
		return null;
	}
	
	/**
	 * Gets the path.
	 *
	 * @param society the society
	 * @return the path
	 */
	public TreePath getPath(Society society) {
		SocietyTreeNode locationTreeNode = getSocietyNode(society);
		if(locationTreeNode != null) {
			return new TreePath(locationTreeNode.getPath());
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the path.
	 *
	 * @param element the element
	 * @return the path
	 */
	public TreePath getPath(InfrastructureElement element) {
		ElementTreeNode elementTreeNode = getElementNode(element);
		if(elementTreeNode != null) {
			return new TreePath(elementTreeNode.getPath());
		} else {
			return null;
		}
	}

	/**
	 * Element added.
	 *
	 * @param element the element
	 */
	public void elementAdded(InfrastructureElement element) {
		if(getRoot() == null) return;
		DefaultMutableTreeNode parentNode = getParentNode(element);
		if(parentNode != null) {
			ElementTreeNode elementNode = new ElementTreeNode(element, society.getInternalElements());
			parentNode.add(elementNode);
			nodesWereInserted(parentNode, 
					new int[]{parentNode.getIndex(elementNode)});
		}
	}

	/**
	 * Element modified.
	 *
	 * @param element the element
	 */
	public void elementModified(InfrastructureElement element) {
		if(getRoot() == null) return;
		ElementTreeNode elementNode = getElementNode(element);
		if(elementNode == null) {
			// element not found in tree
			DefaultMutableTreeNode parentNode = getParentNode(element);
			if(parentNode != null) {
				// element has moved to this location, add it
				elementAdded(element);
			}
		} else {
			// element found in tree
			DefaultMutableTreeNode parentNode = getParentNode(element);
			if(elementNode.getParent().equals(parentNode)) {
				// element has changed without structural update necessary
				nodeChanged(elementNode);
			} else if(parentNode != null) {
				// element has changed parents
				TreeNode oldParentNode = elementNode.getParent();
				int index = oldParentNode.getIndex(elementNode);
				elementNode.removeFromParent();
				parentNode.add(elementNode);
				nodesWereRemoved(oldParentNode, new int[]{index}, new Object[]{elementNode});
				nodesWereInserted(parentNode, new int[]{parentNode.getIndex(elementNode)});
			} else {
				// element belongs in another tree, remove it
				elementRemoved(element);
			}
		}
	}

	/**
	 * Element removed.
	 *
	 * @param element the element
	 */
	public void elementRemoved(InfrastructureElement element) {
		if(getRoot() == null) return;
		for(Enumeration<?> e = getRoot().breadthFirstEnumeration(); e.hasMoreElements();) {
			Object o = e.nextElement();
			if(o instanceof ElementTreeNode) {
				ElementTreeNode elementNode = (ElementTreeNode) o;
				if(elementNode.getUserObject().equals(element)) {
					TreeNode parentNode = elementNode.getParent();
					int index = parentNode.getIndex(elementNode);
					elementNode.removeFromParent();
					nodesWereRemoved(parentNode, 
							new int[]{index}, 
							new Object[]{elementNode});
				}
			}
		}
	}
}