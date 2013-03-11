package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.RegionalAgricultureSystem;
import edu.mit.sips.core.energy.RegionalEnergySystem;
import edu.mit.sips.core.social.RegionalSocialSystem;
import edu.mit.sips.core.water.RegionalWaterSystem;

/**
 * The Class Region.
 */
public class Region extends DefaultSociety implements Society {

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
	public Region(String name, List<Society> nestedSocieties,
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
