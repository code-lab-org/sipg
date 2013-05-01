package edu.mit.sips.gui.comp;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.Society;

/**
 * The Class ElementsTreeModel.
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
	
	public void setState(Society society) {
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
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeModel#getRoot()
	 */
	public DefaultMutableTreeNode getRoot() {
		if(super.getRoot() instanceof DefaultMutableTreeNode) {
			return (DefaultMutableTreeNode) super.getRoot();
		}
		return null;
	}
	
	/**
	 * Gets the location node.
	 *
	 * @param location the location
	 * @return the location node
	 */
	private SocietyTreeNode getLocationNode(Society society) {
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
	 * @param location the location
	 * @return the path
	 */
	public TreePath getPath(Society society) {
		SocietyTreeNode locationTreeNode = getLocationNode(society);
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
	 * @param event the event
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
	 * @param event the event
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
	 * @param event the event
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