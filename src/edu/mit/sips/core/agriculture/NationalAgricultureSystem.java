package edu.mit.sips.core.agriculture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.ElementChangeEvent;
import edu.mit.sips.core.ElementChangeListener;

/**
 * The Class NationalAgricultureSystem.
 */
public class NationalAgricultureSystem extends RegionalAgricultureSystem implements ElementChangeListener {
	private final List<AgricultureElement> elements = 
			Collections.synchronizedList(new ArrayList<AgricultureElement>());
	
	/**
	 * Adds the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean addElement(AgricultureElement element) {
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
			fireAttributeChanges((AgricultureElement)evt.getSource());
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem.Local#getExternalElements()
	 */
	@Override
	public  List<AgricultureElement> getExternalElements() {
		return Collections.unmodifiableList(new ArrayList<AgricultureElement>());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem.Local#getInternalElements()
	 */
	@Override
	public synchronized List<AgricultureElement> getInternalElements() {
		return Collections.unmodifiableList(elements);
	}

	/**
	 * Removes the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean removeElement(AgricultureElement element) {
		boolean value = elements.remove(element);
		element.removeElementChangeListener(this);
		fireAttributeChanges(element);
		return value;
	}
}
