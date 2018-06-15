package edu.mit.sips.core.petroleum;

import edu.mit.sips.core.InfrastructureSoS;
import edu.mit.sips.core.OptimizationOptions;

/**
 * The Interface PetroleumSoS.
 */
public interface PetroleumSoS extends InfrastructureSoS, PetroleumSystem {
	
	/**
	 * The Interface Local.
	 */
	public interface Local extends PetroleumSoS, PetroleumSystem.Local {
		
		/**
		 * Optimize petroleum distribution.
		 */
		public void optimizePetroleumDistribution();
		
		/**
		 * Optimize petroleum production and distribution.
		 *
		 * @param optimizationOptions the optimization options
		 */
		public void optimizePetroleumProductionAndDistribution(OptimizationOptions optimizationOptions);
	}
}
