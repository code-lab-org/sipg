package edu.mit.sips.core.water;

import edu.mit.sips.core.InfrastructureSoS;

/**
 * The Interface WaterSoS.
 */
public interface WaterSoS extends InfrastructureSoS, WaterSystem {
	
	/**
	 * The Interface Local.
	 */
	public static interface Local extends WaterSoS, WaterSystem.Local {
		
		/**
		 * Optimize water distribution.
		 */
		public void optimizeWaterDistribution();
		
		/**
		 * Optimize water production and distribution.
		 *
		 * @param deltaProductionCost the delta production cost
		 */
		public void optimizeWaterProductionAndDistribution(double deltaProductionCost);
	}
}
