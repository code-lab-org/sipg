package edu.mit.sips.core.water;

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
public class NationalWaterSystem extends RegionalWaterSystem implements ElementChangeListener {
	private final List<WaterElement> elements = 
			Collections.synchronizedList(new ArrayList<WaterElement>());
	
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

	/**
	 * Element changed.
	 *
	 * @param evt the evt
	 */
	@Override
	public void elementChanged(ElementChangeEvent evt) {
		if(evt.getSource() instanceof WaterElement) {
			// TODO fireAttributeChanges((WaterElement) evt.getSource());
		}
	}
	
	/**
	 * Fire attribute changes.
	 *
	 * @param element the element
	 */
	private void fireAttributeChanges(WaterElement element) {
		Set<Society> affectedSocieties = new HashSet<Society>();
		getAffectedSocietiesRecursive(getSociety().getCountry().getSociety(element.getOrigin()), 
				affectedSocieties);
		getAffectedSocietiesRecursive(getSociety().getCountry().getSociety(element.getDestination()), 
				affectedSocieties);
		
		for(Society society : affectedSocieties) {
			if(society.getWaterSystem() instanceof WaterSystem.Local) { 
				WaterSystem.Local waterSystem = (WaterSystem.Local) 
						society.getWaterSystem();
				waterSystem.fireAttributeChangeEvent(CASH_FLOW_ATTRIBUTE);
				waterSystem.fireAttributeChangeEvent(DOMESTIC_PRODUCTION_ATTRIBUTE);
				waterSystem.fireAttributeChangeEvent(ELECTRICITY_CONSUMED_ATTRIBUTE);
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
