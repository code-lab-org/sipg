package edu.mit.sips.core.energy;

import edu.mit.sips.core.InfrastructureSoS;

/**
 * The Interface EnergySoS.
 */
public interface EnergySoS extends InfrastructureSoS, EnergySystem {
	
	/**
	 * The Interface Local.
	 */
	public static interface Local extends EnergySoS, EnergySystem.Local {
		
		/**
		 * Optimize energy distribution.
		 */
		public void optimizeEnergyDistribution();
		
		/**
		 * Optimize energy production and distribution.
		 *
		 * @param deltaPetroleumProductionCost the delta petroleum production cost
		 * @param deltaElectricityProductionCost the delta electricity production cost
		 */
		public void optimizeEnergyProductionAndDistribution(
				double deltaPetroleumProductionCost, 
				double deltaElectricityProductionCost);
	}
}
