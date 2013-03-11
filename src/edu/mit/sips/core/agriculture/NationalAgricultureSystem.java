package edu.mit.sips.core.agriculture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NationalAgricultureSystem extends RegionalAgricultureSystem {
	private final List<AgricultureElement> elements = 
			Collections.synchronizedList(new ArrayList<AgricultureElement>());
	
	/**
	 * Adds the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean addElement(AgricultureElement element) {
		return elements.add(element);
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
		return elements.remove(element);
	}
}
