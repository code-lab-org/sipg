package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.WaterSystem;

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
	 * @param electricitySystem the electricity system
	 * @param petroleumSystem the petroleum system
	 * @param socialSystem the social system
	 */
	public City(String name, AgricultureSystem agricultureSystem,
			WaterSystem waterSystem, ElectricitySystem electricitySystem,
			PetroleumSystem petroleumSystem, SocialSystem socialSystem) {
		super(name, new ArrayList<Society>(), agricultureSystem,
				waterSystem, electricitySystem, petroleumSystem, socialSystem);
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
