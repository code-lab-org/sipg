package edu.mit.sips.core.agriculture;

import edu.mit.sips.core.InfrastructureSoS;
import edu.mit.sips.core.OptimizationOptions;

/**
 * The Interface AgricultureSoS.
 */
public interface AgricultureSoS extends InfrastructureSoS, AgricultureSystem {
	
	/**
	 * The Interface Local.
	 */
	public static interface Local extends AgricultureSoS, AgricultureSystem.Local {
		
		/**
		 * Optimize food distribution.
		 */
		public void optimizeFoodDistribution();

		/**
		 * Optimize food production and distribution.
		 *
		 * @param optimizationOptions the optimization options
		 */
		public void optimizeFoodProductionAndDistribution(OptimizationOptions optimizationOptions);
	}
}
