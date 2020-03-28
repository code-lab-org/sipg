package edu.mit.sips.gui.comp;

import java.util.Collection;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.mit.sips.core.base.InfrastructureElement;

/**
 * The Class ElementTreeNode.
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

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#getUserObject()
	 */
	public InfrastructureElement getUserObject() {
		return (InfrastructureElement) super.getUserObject();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#getParent()
	 */
	public DefaultMutableTreeNode getParent() {
		return (DefaultMutableTreeNode) super.getParent();
	}
}