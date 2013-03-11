
package edu.mit.sips.core.energy;

import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Interface EnergySystem.
 */
public interface EnergySystem extends InfrastructureSystem {
	
	/**
	 * The Interface Local.
	 */
	public static interface Local extends EnergySystem, InfrastructureSystem.Local {
		
		/**
		 * Gets the national energy system.
		 *
		 * @return the national energy system
		 */
		public EnergySystem.Local getNationalEnergySystem();
		
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
		
		/**
		 * Optimize energy production and distribution.
		 *
		 * @param deltaPetroleumProductionCost the delta petroleum production cost
		 * @param deltaElectricityProductionCost the delta electricity production cost
		 */
		public void optimizeEnergyProductionAndDistribution(
				double deltaPetroleumProductionCost, 
				double deltaElectricityProductionCost);
		
		/**
		 * Optimize energy distribution.
		 */
		public void optimizeEnergyDistribution();
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
	
	/**
	 * Gets the water consumption.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
}
