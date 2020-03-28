package edu.mit.sips.core.social;

import edu.mit.sips.core.InfrastructureSystem;

import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.FoodUnitsOutput;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * An interface to social infrastructure systems.
 * 
 * @author Paul T. Grogan
 */
public interface SocialSystem extends InfrastructureSystem, 
		FoodUnitsOutput, WaterUnitsOutput, ElectricityUnitsOutput, OilUnitsOutput {
	
	/**
	 * An interface to locally-controlled social infrastructure 
	 * systems which provide greater details.
	 * 
	 * @author Paul T. Grogan
	 */
	public static interface Local extends SocialSystem, InfrastructureSystem.Local { }

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
	 * Gets the petroleum consumption.
	 *
	 * @return the petroleum consumption
	 */
	public double getPetroleumConsumption();
	
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
