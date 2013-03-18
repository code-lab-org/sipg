package edu.mit.sips.core.water;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.ElementChangeEvent;
import edu.mit.sips.core.ElementChangeListener;

/**
 * The Class NationalAgricultureSystem.
 */
public class NationalWaterSystem extends RegionalWaterSystem implements ElementChangeListener {
	private final List<WaterElement> elements = 
			Collections.synchronizedList(new ArrayList<WaterElement>());
	
	/**
	 * Instantiates a new national water system.
	 */
	public NationalWaterSystem() {
		super("National Water");
	}
	
	/**
	 * Adds the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean addElement(WaterElement element) {
		boolean value = elements.add(element);
		element.addElementChangeListener(this);
		fireAttributeChanges(element);
		return value;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem.Local#getExternalElements()
	 */
	@Override
	public  List<WaterElement> getExternalElements() {
		return Collections.unmodifiableList(new ArrayList<WaterElement>());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem.Local#getInternalElements()
	 */
	@Override
	public synchronized List<WaterElement> getInternalElements() {
		return Collections.unmodifiableList(elements);
	}
	
	/**
	 * Removes the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean removeElement(WaterElement element) {
		boolean value = elements.remove(element);
		element.removeElementChangeListener(this);
		fireAttributeChanges(element);
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.ElementChangeListener#elementChanged(edu.mit.sips.core.ElementChangeEvent)
	 */
	@Override
	public void elementChanged(ElementChangeEvent evt) {
		if(elements.contains(evt.getSource())) {
			fireAttributeChanges((WaterElement)evt.getSource());
		}
	}
}
