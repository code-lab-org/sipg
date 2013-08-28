package edu.mit.sips.core.social;

import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.sim.util.FoodUnitsOutput;

/**
 * The Interface SocialSystem.
 */
public interface SocialSystem extends InfrastructureSystem, FoodUnitsOutput {
	/**
	 * The Interface Local.
	 */
	public static interface Local extends SocialSystem, InfrastructureSystem.Local {

	}
	
	/**
	 * The Interface Remote.
	 */
	public static interface Remote extends SocialSystem, InfrastructureSystem.Remote {
		
		/**
		 * Sets the domestic product.
		 *
		 * @param domesticProduct the new domestic product
		 */
		public void setDomesticProduct(double domesticProduct);
		
		/**
		 * Sets the electricity consumption.
		 *
		 * @param electricityConsumption the new electricity consumption
		 */
		public void setElectricityConsumption(double electricityConsumption);
		
		/**
		 * Sets the food consumption.
		 *
		 * @param foodConsumption the new food consumption
		 */
		public void setFoodConsumption(double foodConsumption);
		
		/**
		 * Sets the population.
		 *
		 * @param population the new population
		 */
		public void setPopulation(long population);
		
		/**
		 * Sets the water consumption.
		 *
		 * @param waterConsumption the new water consumption
		 */
		public void setWaterConsumption(double waterConsumption);
	}
	
	public static final String 
	ELECTRICITY_CONSUMPTION_ATTRIBUTE = "electricityConsumption",
	FOOD_CONSUMPTION_ATTRIBUTE = "foodConsumption",
	WATER_CONSUMPTION_ATTRIBUTE = "waterConsumption",
	POPULATION_ATTRIBUTE = "population",
	DOMESTIC_PRODUCT_ATTRIBUTE = "domesticProduct";
	
	/**
	 * Gets the domestic product.
	 *
	 * @return the domestic product
	 */
	public double getDomesticProduct();

	/**
	 * Gets the electricity consumption.
	 *
	 * @return the electricity consumption
	 */
	public double getElectricityConsumption();

	/**
	 * Gets the food consumption.
	 *
	 * @return the food consumption
	 */
	public double getFoodConsumption();

	/**
	 * Gets the population.
	 *
	 * @return the population
	 */
	public long getPopulation();
	
	/**
	 * Gets the water consumption.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
}
