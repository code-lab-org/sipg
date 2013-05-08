package edu.mit.sips;

import java.util.ArrayList;
import java.util.Arrays;
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
									Arrays.asList(
											(AgricultureElement) ElementTemplate.DATES_2.createElement(1920, name, name),
											(AgricultureElement) ElementTemplate.LIVESTOCK_1.createElement(1945, name, name)
											)):
								new DefaultAgricultureSystem.Remote(),
					sectors.contains(Sector.WATER)?
							new DefaultWaterSystem.Local(true, 3e9, 3e9, 3e6, 0,
									new ArrayList<WaterElement>()):
								new DefaultWaterSystem.Remote(),
					sectors.contains(Sector.ENERGY)?
							new DefaultEnergySystem.Local(3e9, 3e9,
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
									Arrays.asList(
											(AgricultureElement) ElementTemplate.LIVESTOCK_1.createElement(1930, name, name),
											(AgricultureElement) ElementTemplate.LIVESTOCK_2.createElement(1940, name, name),
											(AgricultureElement) ElementTemplate.GRAINS_1.createElement(1975, name, name),
											(AgricultureElement) ElementTemplate.GRAINS_2.createElement(1985, name, name),
											(AgricultureElement) ElementTemplate.FOOD_TRANSPORT_1.createElement(1968, name, URBAN.name),
											(AgricultureElement) ElementTemplate.FOOD_TRANSPORT_1.createElement(1968, name, INDUSTRIAL.name),
											(AgricultureElement) ElementTemplate.FOOD_TRANSPORT_1.createElement(1978, name, INDUSTRIAL.name)
											)):
								new DefaultAgricultureSystem.Remote(),
					sectors.contains(Sector.WATER)?
							new DefaultWaterSystem.Local(false, 3e9, 3e9, 3e6, 0,
									new ArrayList<WaterElement>()):
								new DefaultWaterSystem.Remote(),
					sectors.contains(Sector.ENERGY)?
							new DefaultEnergySystem.Local(500e6, 500e6,
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
									Arrays.asList(
											(AgricultureElement) ElementTemplate.DATES_2.createElement(1900, name, name),
											(AgricultureElement) ElementTemplate.LIVESTOCK_1.createElement(1920, name, name)
											)):
								new DefaultAgricultureSystem.Remote(),
					sectors.contains(Sector.WATER)?
							new DefaultWaterSystem.Local(true, 2e9, 2e9, 1e6, 0,
									new ArrayList<WaterElement>()):
								new DefaultWaterSystem.Remote(),
					sectors.contains(Sector.ENERGY)?
							new DefaultEnergySystem.Local(500e6, 500e6,
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