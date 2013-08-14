package edu.mit.sips.core.energy;

import edu.mit.sips.core.InfrastructureSoS;
import edu.mit.sips.core.OptimizationOptions;

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
		 * @param optimizationOptions the optimization options
		 */
		public void optimizeEnergyProductionAndDistribution(OptimizationOptions optimizationOptions);
	}
}
