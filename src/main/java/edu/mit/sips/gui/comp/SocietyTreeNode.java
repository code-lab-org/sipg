package edu.mit.sips.gui.comp;

import java.util.Collection;
import java.util.Comparator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.lifecycle.SimpleLifecycleModel;

/**
 * The Class LocationTreeNode.
 */
public class SocietyTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 8920194303101671363L;
	
	private final Comparator<InfrastructureElement> elementComparator = 
			new Comparator<InfrastructureElement>() {
		@Override
		public int compare(InfrastructureElement o1,
				InfrastructureElement o2) {
			if(o1.getLifecycleModel() instanceof SimpleLifecycleModel 
					&& o2.getLifecycleModel() instanceof SimpleLifecycleModel) {
				int delta = (int) (((SimpleLifecycleModel)o1.getLifecycleModel()).getTimeInitialized() - 
					((SimpleLifecycleModel)o2.getLifecycleModel()).getTimeInitialized());
				if(delta != 0) {
					return delta;
				}
			}
			return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
		}
	};
	
	private final Comparator<Society> societyComparator = 
			new Comparator<Society>() {
		@Override
		public int compare(Society o1, Society o2) {
			return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
		}
	};

	/**
	 * Instantiates a new location tree node.
	 *
	 * @param location the location
	 * @param allElements the all elements
	 */
	public SocietyTreeNode(Society society, Collection<? extends InfrastructureElement> allElements) {
		super(society);
		for(Society nestedSociety : society.getNestedSocieties()) {
			add(new SocietyTreeNode(nestedSociety, allElements));
		}
		for(InfrastructureElement e : allElements) {
			if(e.getOrigin().equals(society.getName())) {
				add(new ElementTreeNode(e, allElements));
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#getUserObject()
	 */
	public Society getUserObject() {
		return (Society) super.getUserObject();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#add(javax.swing.tree.MutableTreeNode)
	 */
	@Override
	public void add(MutableTreeNode newChild) {
		if(newChild instanceof SocietyTreeNode) {
			Society newSociety = ((SocietyTreeNode)newChild).getUserObject();
			for(int i = 0; i < getChildCount(); i++) {
				if(this.getChildAt(i) instanceof SocietyTreeNode
						&& societyComparator.compare(newSociety, 
								((SocietyTreeNode)getChildAt(i)).getUserObject()) < 0) {
					insert(newChild, i);
					return;
				}
			}
		} else if(newChild instanceof ElementTreeNode) {
			InfrastructureElement newElement = ((ElementTreeNode)newChild).getUserObject();
			for(int i = 0; i < getChildCount(); i++) {
				if(this.getChildAt(i) instanceof ElementTreeNode
						&& elementComparator.compare(newElement, 
								((ElementTreeNode)getChildAt(i)).getUserObject()) < 0) {
					insert(newChild, i);
					return;
				}
			}
		}
		super.add(newChild);
	}
}