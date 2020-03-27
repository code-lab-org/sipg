package edu.mit.sips.core.petroleum;

import edu.mit.sips.core.InfrastructureSoS;
import edu.mit.sips.core.electricity.ElectricitySoS;

/**
 * The Interface PetroleumSoS.
 */
public interface PetroleumSoS extends InfrastructureSoS, PetroleumSystem {
	
	/**
	 * Gets the reservoir security score.
	 *
	 * @return the reservoir security score
	 */
	public double getReservoirSecurityScore();
	
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
		 */
		public void optimizePetroleumProductionAndDistribution();
		
		/**
		 * Gets the financial security score.
		 *
		 * @param year the year
		 * @param electricitySystem the electricity system
		 * @return the financial security score
		 */
		public double getFinancialSecurityScore(long year, ElectricitySoS.Local electricitySystem);
		
		/**
		 * Gets the political power score.
		 *
		 * @param year the year
		 * @param electricitySystem the electricity system
		 * @return the political power score
		 */
		public double getPoliticalPowerScore(long year, ElectricitySoS.Local electricitySystem);
		
		/**
		 * Gets the aggregate score.
		 *
		 * @param year the year
		 * @param electricitySystem the electricity system
		 * @return the aggregate score
		 */
		public double getAggregateScore(long year, ElectricitySoS.Local electricitySystem);
	}
}
