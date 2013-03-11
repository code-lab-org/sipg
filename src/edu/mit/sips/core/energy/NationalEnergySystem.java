package edu.mit.sips.core.energy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Class NationalEnergySystem.
 */
public class NationalEnergySystem extends DefaultEnergySystem.Local {
	
	/**
	 * Instantiates a new national energy system.
	 */
	public NationalEnergySystem() {
		super("National Energy", new NationalPetroleumSystem(), 
				new NationalElectricitySystem());
	}
	
	/**
	 * Adds the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean addElement(EnergyElement element) {
		if(element instanceof PetroleumElement) {
			return getPetroleumSystem().addElement((PetroleumElement)element);
		} else if(element instanceof ElectricityElement) {
			return getElectricitySystem().addElement((ElectricityElement)element);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.DefaultEnergySystem.Local#getElectricitySystem()
	 */
	@Override
	public NationalElectricitySystem getElectricitySystem() {
		return (NationalElectricitySystem) super.getElectricitySystem();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem.Local#getExternalElements()
	 */
	@Override
	public  List<EnergyElement> getExternalElements() {
		return Collections.unmodifiableList(new ArrayList<EnergyElement>());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem.Local#getInternalElements()
	 */
	@Override
	public synchronized List<EnergyElement> getInternalElements() {
		List<EnergyElement> elements = new ArrayList<EnergyElement>();
		elements.addAll(getPetroleumSystem().getInternalElements());
		elements.addAll(getElectricitySystem().getInternalElements());
		return Collections.unmodifiableList(elements);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.DefaultEnergySystem.Local#getPetroleumSystem()
	 */
	@Override
	public NationalPetroleumSystem getPetroleumSystem() {
		return (NationalPetroleumSystem) super.getPetroleumSystem();
	}

	/**
	 * Removes the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean removeElement(EnergyElement element) {
		if(element instanceof PetroleumElement) {
			return getPetroleumSystem().removeElement((PetroleumElement)element);
		} else if(element instanceof ElectricityElement) {
			return getElectricitySystem().removeElement((ElectricityElement)element);
		}
		return false;
	}
}
