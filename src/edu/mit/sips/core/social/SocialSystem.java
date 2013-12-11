package edu.mit.sips.core.social;

import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.FoodUnitsOutput;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Interface SocialSystem.
 */
public interface SocialSystem extends InfrastructureSystem, 
		FoodUnitsOutput, WaterUnitsOutput, ElectricityUnitsOutput, OilUnitsOutput {
	/**
	 * The Interface Local.
	 */
	public static interface Local extends SocialSystem, InfrastructureSystem.Local {
		
		/**
		 * Gets the cumulative capital expense.
		 *
		 * @return the cumulative capital expense
		 */
		public double getCumulativeCapitalExpense();
	}
	
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
	
	/**
	 * Gets the petroleum consumption.
	 *
	 * @return the petroleum consumption
	 */
	public double getPetroleumConsumption();
}
