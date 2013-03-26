package edu.mit.sips.core.energy;

import edu.mit.sips.core.InfrastructureSoS;

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
	 * @param deltaProductionCost the delta production cost
	 */
	public void optimizePetroleumProductionAndDistribution(double deltaProductionCost);
}
