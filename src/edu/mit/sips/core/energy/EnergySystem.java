package edu.mit.sips.core.energy;

import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Interface EnergySystem.
 */
public interface EnergySystem extends InfrastructureSystem {
	
	/**
	 * Gets the water consumption.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
	
	/**
	 * The Interface Local.
	 */
	public static interface Local extends EnergySystem, InfrastructureSystem.Local {
		
		/**
		 * Gets the petroleum system.
		 *
		 * @return the petroleum system
		 */
		public PetroleumSystem getPetroleumSystem();
		
		/**
		 * Gets the electricity system.
		 *
		 * @return the electricity system
		 */
		public ElectricitySystem getElectricitySystem();
	}
	
	/**
	 * The Interface Remote.
	 */
	public static interface Remote extends EnergySystem, InfrastructureSystem.Remote {
		
		/**
		 * Gets the water consumption.
		 *
		 * @return the water consumption
		 */
		public void setWaterConsumption(double waterConsumption);
	}
}
