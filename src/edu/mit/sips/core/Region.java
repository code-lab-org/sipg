package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.RegionalAgricultureSystem;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.energy.RegionalEnergySystem;
import edu.mit.sips.core.social.RegionalSocialSystem;
import edu.mit.sips.core.water.RegionalWaterSystem;
import edu.mit.sips.core.water.WaterSystem;

/**
 * The Class Region.
 */
public class Region extends DefaultSociety implements Society {
	
	/**
	 * Builds the region.
	 *
	 * @param name the name
	 * @param nestedSocieties the nested societies
	 * @return the region
	 */
	public static Region buildRegion(String name, List<? extends Society> nestedSocieties) {
		RegionalAgricultureSystem agricultureSystem = null;
		// agriculture system is regional if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
				agricultureSystem = new RegionalAgricultureSystem();
				break;
			}
		}
		
		RegionalWaterSystem waterSystem = null;
		// water system is regional if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getWaterSystem() instanceof WaterSystem.Local) {
				waterSystem = new RegionalWaterSystem();
				break;
			}
		}
		
		RegionalEnergySystem energySystem = null;
		// energy system is regional if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getEnergySystem() instanceof EnergySystem.Local) {
				energySystem = new RegionalEnergySystem();
				break;
			}
		}

		// social system is always regional
		RegionalSocialSystem socialSystem = new RegionalSocialSystem();
		
		return new Region(name, nestedSocieties, agricultureSystem, 
				waterSystem, energySystem, socialSystem);
	}

	/**
	 * Instantiates a new region.
	 */
	protected Region() {
		
	}
	
	/**
	 * Instantiates a new region.
	 *
	 * @param name the name
	 * @param nestedSocieties the nested societies
	 */
	private Region(String name, List<? extends Society> nestedSocieties,
			RegionalAgricultureSystem agricultureSystem,
			RegionalWaterSystem waterSystem,
			RegionalEnergySystem energySystem,
			RegionalSocialSystem socialSystem) {
		super(name, nestedSocieties, agricultureSystem, 
				waterSystem, energySystem, socialSystem);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.SocialSystem#getCities()
	 */
	@Override
	public List<City> getCities() {
		List<City> cities = new ArrayList<City>();
		for(Society nestedSociety : getNestedSocieties()) {
			cities.addAll(nestedSociety.getCities());
		}
		return Collections.unmodifiableList(cities);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getCountry()
	 */
	@Override
	public Country getCountry() {
		if(getSociety() == null) {
			throw new IllegalStateException("Society cannot be null.");
		}
		return getSociety().getCountry();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getGlobals()
	 */
	@Override
	public Globals getGlobals() {
		return getCountry().getGlobals();
	}
}
