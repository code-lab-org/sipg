package edu.mit.sips.core.water;

import edu.mit.sips.core.InfrastructureSoS;
import edu.mit.sips.core.OptimizationOptions;

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
		 * @param optimizationOptions the optimization options
		 */
		public void optimizeWaterProductionAndDistribution(OptimizationOptions optimizationOptions);
	}
}
