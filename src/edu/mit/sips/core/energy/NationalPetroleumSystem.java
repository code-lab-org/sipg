package edu.mit.sips.core.energy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.ElementChangeEvent;
import edu.mit.sips.core.ElementChangeListener;

/**
 * The Class NationalAgricultureSystem.
 */
public class NationalPetroleumSystem extends RegionalPetroleumSystem implements ElementChangeListener {
	private final List<PetroleumElement> elements = 
			Collections.synchronizedList(new ArrayList<PetroleumElement>());

	/**
	 * Adds the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean addElement(PetroleumElement element) {
		boolean value = elements.add(element);
		element.addElementChangeListener(this);
		fireAttributeChanges(element);
		return value;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.ElementChangeListener#elementChanged(edu.mit.sips.core.ElementChangeEvent)
	 */
	@Override
	public void elementChanged(ElementChangeEvent evt) {
		if(elements.contains(evt.getSource())) {
			fireAttributeChanges((PetroleumElement)evt.getSource());
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem.Local#getExternalElements()
	 */
	@Override
	public  List<PetroleumElement> getExternalElements() {
		return Collections.unmodifiableList(new ArrayList<PetroleumElement>());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem.Local#getInternalElements()
	 */
	@Override
	public synchronized List<PetroleumElement> getInternalElements() {
		return Collections.unmodifiableList(elements);
	}

	/**
	 * Removes the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean removeElement(PetroleumElement element) {
		boolean value = elements.remove(element);
		element.removeElementChangeListener(this);
		fireAttributeChanges(element);
		return value;
	}
}
