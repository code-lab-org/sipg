package edu.mit.sips.core.electricity;

import edu.mit.sips.core.InfrastructureSoS;
import edu.mit.sips.core.OptimizationOptions;

/**
 * The Interface ElectricitySoS.
 */
public interface ElectricitySoS extends InfrastructureSoS, ElectricitySystem {
	
	/**
	 * The Interface Local.
	 */
	public interface Local extends ElectricitySoS, ElectricitySystem.Local {
		
		/**
		 * Optimize electricity distribution.
		 */
		public void optimizeElectricityDistribution();
		
		/**
		 * Optimize electricity production and distribution.
		 *
		 * @param optimizationOptions the optimization options
		 */
		public void optimizeElectricityProductionAndDistribution(OptimizationOptions optimizationOptions);
	}
}
