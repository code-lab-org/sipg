package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.DefaultAgricultureSoS;
import edu.mit.sips.core.energy.DefaultEnergySoS;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.social.DefaultSocialSoS;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.DefaultWaterSoS;
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
		AgricultureSystem agricultureSystem = new DefaultAgricultureSoS();
		// agriculture system is local if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
				agricultureSystem = new DefaultAgricultureSoS.Local();
				break;
			}
		}
		
		WaterSystem waterSystem = null;
		// water system is local if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getWaterSystem() instanceof WaterSystem.Local) {
				waterSystem = new DefaultWaterSoS.Local();
				break;
			}
		}
		
		EnergySystem energySystem = new DefaultEnergySoS();
		// energy system is local if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getEnergySystem() instanceof EnergySystem.Local) {
				energySystem = new DefaultEnergySoS.Local();
				break;
			}
		}

		// social system is always local
		SocialSystem socialSystem = new DefaultSocialSoS();
		
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
			AgricultureSystem agricultureSystem, WaterSystem waterSystem,
			EnergySystem energySystem, SocialSystem socialSystem) {
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
