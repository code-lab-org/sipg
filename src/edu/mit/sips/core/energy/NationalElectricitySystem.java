package edu.mit.sips.core.energy;

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
public class NationalElectricitySystem extends RegionalElectricitySystem implements ElementChangeListener {
	private final List<ElectricityElement> elements = 
			Collections.synchronizedList(new ArrayList<ElectricityElement>());
	
	/**
	 * Instantiates a new national electricity system.
	 */
	public NationalElectricitySystem() {
		super("National Electricity");
	}
	
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

	/**
	 * Element changed.
	 *
	 * @param evt the evt
	 */
	@Override
	public void elementChanged(ElementChangeEvent evt) {
		if(evt.getSource() instanceof ElectricityElement) {
			// TODO fireAttributeChanges((WaterElement) evt.getSource());
		}
	}
	
	/**
	 * Fire attribute changes.
	 *
	 * @param element the element
	 */
	private void fireAttributeChanges(ElectricityElement element) {
		Set<Society> affectedSocieties = new HashSet<Society>();
		getAffectedSocietiesRecursive(getSociety().getCountry().getSociety(element.getOrigin()), 
				affectedSocieties);
		getAffectedSocietiesRecursive(getSociety().getCountry().getSociety(element.getDestination()), 
				affectedSocieties);
		
		for(Society society : affectedSocieties) {
			if(society.getEnergySystem() instanceof EnergySystem.Local) { 
				EnergySystem.Local energySystem = (EnergySystem.Local) 
						society.getEnergySystem();
				energySystem.getElectricitySystem().fireAttributeChangeEvent(CASH_FLOW_ATTRIBUTE);
				energySystem.getElectricitySystem().fireAttributeChangeEvent(DOMESTIC_PRODUCTION_ATTRIBUTE);
				energySystem.getElectricitySystem().fireAttributeChangeEvent(WATER_CONSUMED_ATTRIBUTE);
				energySystem.getElectricitySystem().fireAttributeChangeEvent(PETROLEUM_CONSUMED_ATTRIBUTE);
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
