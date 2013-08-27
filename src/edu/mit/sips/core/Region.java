package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.DefaultAgricultureSoS;
import edu.mit.sips.core.electricity.DefaultElectricitySoS;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.petroleum.DefaultPetroleumSoS;
import edu.mit.sips.core.petroleum.PetroleumSystem;
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
		
		ElectricitySystem electricitySystem = new DefaultElectricitySoS();
		// electricity system is national if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getElectricitySystem() instanceof ElectricitySystem.Local) {
				electricitySystem = new DefaultElectricitySoS.Local();
				break;
			}
		}
		
		PetroleumSystem petroleumSystem = new DefaultPetroleumSoS();
		// petroleum system is national if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getPetroleumSystem() instanceof PetroleumSystem.Local) {
				petroleumSystem = new DefaultPetroleumSoS.Local();
				break;
			}
		}

		// social system is always local
		SocialSystem socialSystem = new DefaultSocialSoS();
		
		return new Region(name, nestedSocieties, agricultureSystem, 
				waterSystem, electricitySystem, petroleumSystem, socialSystem);
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
			ElectricitySystem electricitySystem, PetroleumSystem petroleumSystem, 
			SocialSystem socialSystem) {
		super(name, nestedSocieties, agricultureSystem, 
				waterSystem, electricitySystem, petroleumSystem, socialSystem);
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
}
