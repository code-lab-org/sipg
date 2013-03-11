package edu.mit.sips.core.social;

import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Interface SocialSystem.
 */
public interface SocialSystem extends InfrastructureSystem {
	
	/**
	 * Gets the domestic product.
	 *
	 * @return the domestic product
	 */
	public double getDomesticProduct();
	
	/**
	 * Gets the population.
	 *
	 * @return the population
	 */
	public long getPopulation();
	
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
	 * Gets the water consumption.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
	
	/**
	 * The Interface Local.
	 */
	public static interface Local extends SocialSystem, InfrastructureSystem.Local {
		
		/**
		 * Gets the food consumption per capita.
		 *
		 * @return the food consumption per capita
		 */
		public double getFoodConsumptionPerCapita();
		
		/**
		 * Gets the water consumption per capita.
		 *
		 * @return the water consumption per capita
		 */
		public double getWaterConsumptionPerCapita();
		
		/**
		 * Gets the electricity consumption per capita.
		 *
		 * @return the electricity consumption per capita
		 */
		public double getElectricityConsumptionPerCapita();
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
		 * Sets the population.
		 *
		 * @param population the new population
		 */
		public void setPopulation(long population);
		
		/**
		 * Sets the electricity consumption.
		 *
		 * @param electricityConsumption the new electricity consumption
		 */
		public void setElectricityConsumption(double electricityConsumption);
		
		/**
		 * Gets the food consumption.
		 *
		 * @param foodConsumption the food consumption
		 * @return the food consumption
		 */
		public void getFoodConsumption(double foodConsumption);
		
		/**
		 * Gets the water consumption.
		 *
		 * @param waterConsumption the water consumption
		 * @return the water consumption
		 */
		public void getWaterConsumption(double waterConsumption);
	}
}
