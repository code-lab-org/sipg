package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.CityAgricultureSystem;
import edu.mit.sips.core.energy.CityEnergySystem;
import edu.mit.sips.core.social.CitySocialSystem;
import edu.mit.sips.core.water.CityWaterSystem;

public class City extends DefaultSociety implements Society {
	/**
	 * Instantiates a new city.
	 */
	protected City() {
		
	}
	
	/**
	 * Instantiates a new city.
	 *
	 * @param name the name
	 * @param agricultureSystem the agriculture system
	 * @param waterSystem the water system
	 * @param energySystem the energy system
	 * @param socialSystem the social system
	 */
	public City(String name, CityAgricultureSystem agricultureSystem,
			CityWaterSystem waterSystem, CityEnergySystem energySystem,
			CitySocialSystem socialSystem) {
		super(name, new ArrayList<Society>(), agricultureSystem,
				waterSystem, energySystem, socialSystem);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.DefaultSociety#getCities()
	 */
	@Override
	public List<City> getCities() {
		return Collections.unmodifiableList(Arrays.asList(this));
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
