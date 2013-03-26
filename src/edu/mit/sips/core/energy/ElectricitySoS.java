package edu.mit.sips.core.energy;

import edu.mit.sips.core.InfrastructureSoS;

/**
 * The Interface ElectricitySoS.
 */
public interface ElectricitySoS extends InfrastructureSoS, ElectricitySystem {
	
	/**
	 * Optimize electricity distribution.
	 */
	public void optimizeElectricityDistribution();
	
	/**
	 * Optimize electricity production and distribution.
	 *
	 * @param deltaProductionCost the delta production cost
	 */
	public void optimizeElectricityProductionAndDistribution(double deltaProductionCost);
}
