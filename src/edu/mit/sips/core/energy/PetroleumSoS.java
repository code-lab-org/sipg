package edu.mit.sips.core.energy;

import edu.mit.sips.core.InfrastructureSoS;
import edu.mit.sips.core.OptimizationOptions;

/**
 * The Interface PetroleumSoS.
 */
public interface PetroleumSoS extends InfrastructureSoS, PetroleumSystem {
	
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
