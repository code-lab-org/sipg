package edu.mit.sips;

import java.util.ArrayList;
import java.util.Collection;

import edu.mit.sips.core.City;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.core.energy.DefaultEnergySystem;
import edu.mit.sips.core.energy.EnergyElement;
import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.social.LogisticGrowthModel;
import edu.mit.sips.core.water.DefaultWaterSystem;
import edu.mit.sips.core.water.WaterElement;


/**
 * The Enum CityTemplate.
 */
public enum CityTemplate { 
	RURAL("Sakakah"), 
	URBAN("Jeddah"), 
	INDUSTRIAL("Riyadh");
	
	private final String name;
	
	/**
	 * Instantiates a new city template.
	 *
	 * @param name the name
	 */
	private CityTemplate(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * Creates the city.
	 *
	 * @param assigned the assigned
	 * @param sectors the sectors
	 * @return the city
	 */
	public City createCity(boolean assigned, Collection<Sector> sectors) {
		switch(this) {
		case INDUSTRIAL:
			return new City(name, 
					sectors.contains(Sector.AGRICULTURE)?
							new DefaultAgricultureSystem.Local(3000, 
									new ArrayList<AgricultureElement>()):
								new DefaultAgricultureSystem.Remote(),
					sectors.contains(Sector.WATER)?
							new DefaultWaterSystem.Local(true, 3e9, 3e9, 3e6, 0,
									new ArrayList<WaterElement>()):
								new DefaultWaterSystem.Remote(),
					sectors.contains(Sector.ENERGY)?
							new DefaultEnergySystem.Local(1e10, 1e10,
									new ArrayList<EnergyElement>()):
								new DefaultEnergySystem.Remote(),
					assigned?
							new DefaultSocialSystem.Local(
									new LogisticGrowthModel(1950, 50000, 0.08, 20000000), 1000):
								new DefaultSocialSystem.Remote());
		case RURAL:
			return new City(name, 
					sectors.contains(Sector.AGRICULTURE)?
							new DefaultAgricultureSystem.Local(10000, 
									new ArrayList<AgricultureElement>()):
								new DefaultAgricultureSystem.Remote(),
					sectors.contains(Sector.WATER)?
							new DefaultWaterSystem.Local(false, 3e9, 3e9, 3e6, 0,
									new ArrayList<WaterElement>()):
								new DefaultWaterSystem.Remote(),
					sectors.contains(Sector.ENERGY)?
							new DefaultEnergySystem.Local(0, 0,
									new ArrayList<EnergyElement>()):
								new DefaultEnergySystem.Remote(),
					assigned?
							new DefaultSocialSystem.Local(
									new LogisticGrowthModel(1950, 10000, 0.05, 750000), 1000):
								new DefaultSocialSystem.Remote());
		case URBAN:
			return new City(name, 
					sectors.contains(Sector.AGRICULTURE)?
							new DefaultAgricultureSystem.Local(4000, 
									new ArrayList<AgricultureElement>()):
								new DefaultAgricultureSystem.Remote(),
					sectors.contains(Sector.WATER)?
							new DefaultWaterSystem.Local(true, 2e9, 2e9, 1e6, 0,
									new ArrayList<WaterElement>()):
								new DefaultWaterSystem.Remote(),
					sectors.contains(Sector.ENERGY)?
							new DefaultEnergySystem.Local(0, 0,
									new ArrayList<EnergyElement>()):
								new DefaultEnergySystem.Remote(),
					assigned?
							new DefaultSocialSystem.Local(
									new LogisticGrowthModel(1950, 100000, 0.07, 10000000), 1000):
								new DefaultSocialSystem.Remote());
		default:
			throw new IllegalArgumentException("Unknown city template.");
		}
	}
};