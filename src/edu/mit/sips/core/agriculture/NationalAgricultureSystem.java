package edu.mit.sips.core.agriculture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.mit.sips.core.ElementChangeEvent;
import edu.mit.sips.core.ElementChangeListener;
import edu.mit.sips.core.Society;

/**
 * The Class NationalAgricultureSystem.
 */
public class NationalAgricultureSystem extends RegionalAgricultureSystem implements ElementChangeListener {
	private final List<AgricultureElement> elements = 
			Collections.synchronizedList(new ArrayList<AgricultureElement>());
	
	/**
	 * Instantiates a new national agriculture system.
	 */
	public NationalAgricultureSystem() {
		super("National Agriculture");
	}
	
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

	/**
	 * Element changed.
	 *
	 * @param evt the evt
	 */
	@Override
	public void elementChanged(ElementChangeEvent evt) {
		if(evt.getSource() instanceof AgricultureElement) {
			// TODO fireAttributeChanges((AgricultureElement) evt.getSource());
		}
	}
	
	/**
	 * Fire attribute changes.
	 *
	 * @param element the element
	 */
	private void fireAttributeChanges(AgricultureElement element) {
		Set<Society> affectedSocieties = new HashSet<Society>();
		getAffectedSocietiesRecursive(getSociety().getCountry().getSociety(element.getOrigin()), 
				affectedSocieties);
		getAffectedSocietiesRecursive(getSociety().getCountry().getSociety(element.getDestination()), 
				affectedSocieties);
		
		for(Society society : affectedSocieties) {
			if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) { 
				AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
						society.getAgricultureSystem();
				agricultureSystem.fireAttributeChangeEvent(CASH_FLOW_ATTRIBUTE);
				agricultureSystem.fireAttributeChangeEvent(DOMESTIC_PRODUCTION_ATTRIBUTE);
				agricultureSystem.fireAttributeChangeEvent(WATER_CONSUMED_ATTRIBUTE);
			}
		}
	}
	
	/**
	 * Gets the affected societies recursive.
	 *
	 * @param society the society
	 * @param affectedSocieties the affected societies
	 * @return the affected societies recursive
	 */
	private static void getAffectedSocietiesRecursive(Society society, 
			Set<Society> affectedSocieties) {
		affectedSocieties.add(society);
		if(society.getSociety() != null) {
			getAffectedSocietiesRecursive(society.getSociety(), affectedSocieties);
		}
	}
}
