package edu.mit.sips;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.core.energy.DefaultEnergySystem;
import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.social.LogisticGrowthModel;
import edu.mit.sips.core.water.DefaultWaterSystem;
import edu.mit.sips.core.water.WaterElement;

/**
 * A factory for creating Society objects.
 */
public abstract class SocietyFactory {
	public static enum SectorAssignment { 
		AGRICULTURE("Agriculture"), 
		WATER("Water"),
		ENERGY("Energy");
		
		private final String name;
		
		private SectorAssignment(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public String toString() {
			return name;
		}	
	};
	public static enum CityAssignment { 
		RURAL("Sakakah"), 
		URBAN("Jeddah"), 
		INDUSTRIAL("Riyadh");
		
		private final String name;
		
		private CityAssignment(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public String toString() {
			return name;
		}
	};
	
	/**
	 * Creates a new Society object.
	 *
	 * @param cities the cities
	 * @param sectors the sectors
	 * @return the country
	 */
	public static Country createSaudiCountry(Collection<CityAssignment> cities,
			List<SectorAssignment> sectors) {
		return  Country.buildCountry("KSA", Arrays.asList(
				createIndustrialCity(cities.contains(CityAssignment.INDUSTRIAL), sectors),
				createUrbanCity(cities.contains(CityAssignment.URBAN), sectors),
				createRuralCity(cities.contains(CityAssignment.RURAL), sectors)
			));
	}
	
	/**
	 * Creates a new Society object.
	 *
	 * @param assigned the assigned
	 * @param sectors the sectors
	 * @return the city
	 */
	public static City createUrbanCity(boolean assigned, 
			Collection<SectorAssignment> sectors) {
		String name = CityAssignment.URBAN.getName();
		return new City(name, 
				sectors.contains(SectorAssignment.AGRICULTURE)?
						new DefaultAgricultureSystem.Local(4000, 
								Arrays.asList(
										ElementFactory.createDateFarm(name),
										ElementFactory.createDateFarm(name),
										ElementFactory.createDateFarm(name),
										ElementFactory.createDateFarm(name),
										ElementFactory.createDefaultFoodDistribution(
												name, CityAssignment.INDUSTRIAL.getName()),
										ElementFactory.createDefaultFoodDistribution(
												name, CityAssignment.RURAL.getName())
								)):
							new DefaultAgricultureSystem.Remote(),
				sectors.contains(SectorAssignment.WATER)?
						new DefaultWaterSystem.Local(true, 2e9, 2e9, 1e6, 0,
								Arrays.asList(
										ElementFactory.createAquiferWell(name, 1940),
										ElementFactory.createAquiferWell(name, 1965)
								)):
							new DefaultWaterSystem.Remote(),
				sectors.contains(SectorAssignment.ENERGY)?
						new DefaultEnergySystem.Local(0, 0,
								Arrays.asList(
										ElementFactory.createDefaultPetroleumDistribution(
												name, CityAssignment.INDUSTRIAL.getName()),
										ElementFactory.createDefaultPetroleumDistribution(
												name, CityAssignment.RURAL.getName())
								)):
							new DefaultEnergySystem.Remote(),
				assigned?
						new DefaultSocialSystem.Local(
								new LogisticGrowthModel(1950, 100000, 0.07, 10000000), 1000):
							new DefaultSocialSystem.Remote());
	}
	
	/**
	 * Creates a new Society object.
	 *
	 * @param assigned the assigned
	 * @param sectors the sectors
	 * @return the city
	 */
	public static City createRuralCity(boolean assigned, 
			Collection<SectorAssignment> sectors) {
		String name = CityAssignment.RURAL.getName();
		return new City(name, 
				sectors.contains(SectorAssignment.AGRICULTURE)?
						new DefaultAgricultureSystem.Local(10000, 
								Arrays.asList(
										ElementFactory.createGrazingLand(name),
										ElementFactory.createDateFarm(name),
										ElementFactory.createDateFarm(name),
										ElementFactory.createDefaultFoodDistribution(
												name, CityAssignment.INDUSTRIAL.getName()),
										ElementFactory.createDefaultFoodDistribution(
												name, CityAssignment.URBAN.getName())
								)):
							new DefaultAgricultureSystem.Remote(),
				sectors.contains(SectorAssignment.WATER)?
						new DefaultWaterSystem.Local(false, 3e9, 3e9, 3e6, 0,
								new ArrayList<WaterElement>()):
							new DefaultWaterSystem.Remote(),
				sectors.contains(SectorAssignment.ENERGY)?
						new DefaultEnergySystem.Local(0, 0,
								Arrays.asList(
										ElementFactory.createDefaultPetroleumDistribution(
												name, CityAssignment.INDUSTRIAL.getName()),
										ElementFactory.createDefaultPetroleumDistribution(
												name, CityAssignment.URBAN.getName())
								)):
							new DefaultEnergySystem.Remote(),
				assigned?
						new DefaultSocialSystem.Local(
								new LogisticGrowthModel(1950, 10000, 0.05, 750000), 1000):
							new DefaultSocialSystem.Remote());
	}
	
	/**
	 * Creates a new Society object.
	 *
	 * @param assigned the assigned
	 * @param sectors the sectors
	 * @return the city
	 */
	public static City createIndustrialCity(boolean assigned, 
			Collection<SectorAssignment> sectors) {
		String name = CityAssignment.INDUSTRIAL.getName();
		return new City(name, 
				sectors.contains(SectorAssignment.AGRICULTURE)?
						new DefaultAgricultureSystem.Local(3000, 
								Arrays.asList(
										ElementFactory.createDateFarm(name),
										ElementFactory.createDateFarm(name),
										ElementFactory.createDateFarm(name),
										ElementFactory.createDefaultFoodDistribution(
												name, CityAssignment.RURAL.getName()),
										ElementFactory.createDefaultFoodDistribution(
												name, CityAssignment.URBAN.getName())
								)):
							new DefaultAgricultureSystem.Remote(),
				sectors.contains(SectorAssignment.WATER)?
						new DefaultWaterSystem.Local(true, 3e9, 3e9, 3e6, 0,
								new ArrayList<WaterElement>()):
							new DefaultWaterSystem.Remote(),
				sectors.contains(SectorAssignment.ENERGY)?
						new DefaultEnergySystem.Local(1e10, 1e10,
								Arrays.asList(
										ElementFactory.createPetroleumWell(name, 1942),
										ElementFactory.createPetroleumWell(name, 1955),
										ElementFactory.createPetroleumWell(name, 1965),
										ElementFactory.createPetroleumWell(name, 1975),
										ElementFactory.createPetroleumWell(name, 1975),
										ElementFactory.createDefaultPetroleumDistribution(
												name, CityAssignment.RURAL.getName()),
										ElementFactory.createDefaultPetroleumDistribution(
												name, CityAssignment.URBAN.getName())
								)):
							new DefaultEnergySystem.Remote(),
				assigned?
						new DefaultSocialSystem.Local(
								new LogisticGrowthModel(1950, 50000, 0.08, 20000000), 1000):
							new DefaultSocialSystem.Remote());
	}
}
