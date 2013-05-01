package edu.mit.sips;

import edu.mit.sips.core.DefaultLifecycleModel;
import edu.mit.sips.core.SimpleLifecycleModel;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.agriculture.AgricultureProduct;
import edu.mit.sips.core.agriculture.DefaultAgricultureElement;
import edu.mit.sips.core.energy.DefaultPetroleumElement;
import edu.mit.sips.core.energy.PetroleumElement;
import edu.mit.sips.core.water.DefaultWaterElement;
import edu.mit.sips.core.water.WaterElement;

/**
 * A factory for creating Element objects.
 */
public abstract class ElementFactory {
	private static int grazingLandId = 0, 
			dateFarmId = 0, 
			aquiferWellId = 0,
			foodRouteId = 0,
			petroleumRouteId = 0,
			petroleumWellId = 0;
	
	/**
	 * Creates a new Element object.
	 *
	 * @param city the city
	 * @return the agriculture element
	 */
	public static AgricultureElement createGrazingLand(String city) {
		return DefaultAgricultureElement.createProductionElement(
					"Grazing Land " + ++grazingLandId, city, city,
					new DefaultLifecycleModel(), 5000, 0, 
					AgricultureProduct.GRAZING);
	}
	
	/**
	 * Creates a new Element object.
	 *
	 * @param city the city
	 * @return the agriculture element
	 */
	public static AgricultureElement createDateFarm(String city) {
		return DefaultAgricultureElement.createProductionElement(
				"Date Farm " + ++dateFarmId, city, city,
				new DefaultLifecycleModel(), 1000, 0, 
				AgricultureProduct.INDIGINOUS_CROP);
	}
	
	/**
	 * Creates a new Element object.
	 *
	 * @param origin the origin
	 * @param destination the destination
	 * @return the agriculture element
	 */
	public static AgricultureElement createDefaultFoodDistribution(
			String origin, String destination) {
		return DefaultAgricultureElement.createDistributionElement(
				"Food Route " + ++foodRouteId, origin, destination,
				new DefaultLifecycleModel(), 0.95, 1e20, 0, 0);
	}
	
	/**
	 * Creates a new Element object.
	 *
	 * @param city the city
	 * @return the water element
	 */
	public static WaterElement createAquiferWell(String city, long yearInitialized) {
		return DefaultWaterElement.createProductionElement(
				"Aquifer Well " + ++aquiferWellId, city, city, 
				new SimpleLifecycleModel(yearInitialized, 5, 60, 10, 
						10000, 1000, 5000, true), 
				1.0, 10e6, 0, 0, 0);
	}
	
	/**
	 * Creates a new Element object.
	 *
	 * @param origin the origin
	 * @param destination the destination
	 * @return the agriculture element
	 */
	public static PetroleumElement createDefaultPetroleumDistribution(
			String origin, String destination) {
		return DefaultPetroleumElement.createDistributionElement(
				"Petroleum Route " + ++petroleumRouteId, origin, destination,
				new DefaultLifecycleModel(), 0.95, 1e20, 0, 0, 10);
	}
	
	/**
	 * Creates a new Element object.
	 *
	 * @param city the city
	 * @param yearInitialized the year initialized
	 * @return the petroleum element
	 */
	public static PetroleumElement createPetroleumWell(String city, long yearInitialized) {
		return DefaultPetroleumElement.createProductionElement(
				"Petroleum Well " + ++petroleumWellId, city, city, 
				new SimpleLifecycleModel(yearInitialized, 5, 60, 10, 
						10000, 1000, 5000, true), 
				1.0, 1e6, 0, 0);
	}
}
