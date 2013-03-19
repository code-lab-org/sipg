package edu.mit.sips.core.energy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.ElementChangeEvent;
import edu.mit.sips.core.ElementChangeListener;

/**
 * The Class NationalAgricultureSystem.
 */
public class NationalElectricitySystem extends RegionalElectricitySystem implements ElementChangeListener {
	private final List<ElectricityElement> elements = 
			Collections.synchronizedList(new ArrayList<ElectricityElement>());
	
	/**
	 * Adds the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean addElement(ElectricityElement element) {
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
			fireAttributeChanges((ElectricityElement)evt.getSource());
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem.Local#getExternalElements()
	 */
	@Override
	public  List<ElectricityElement> getExternalElements() {
		return Collections.unmodifiableList(new ArrayList<ElectricityElement>());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem.Local#getInternalElements()
	 */
	@Override
	public synchronized List<ElectricityElement> getInternalElements() {
		return Collections.unmodifiableList(elements);
	}

	/**
	 * Removes the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean removeElement(ElectricityElement element) {
		boolean value = elements.remove(element);
		element.removeElementChangeListener(this);
		fireAttributeChanges(element);
		return value;
	}
}
