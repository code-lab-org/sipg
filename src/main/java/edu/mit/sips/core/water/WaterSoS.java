package edu.mit.sips.core.water;

import edu.mit.sips.core.InfrastructureSoS;

/**
 * The Interface WaterSoS.
 */
public interface WaterSoS extends InfrastructureSoS, WaterSystem {
	
	/**
	 * Gets the aquifer security score.
	 *
	 * @return the aquifer security score
	 */
	public double getAquiferSecurityScore();
	
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
		 */
		public void optimizeWaterProductionAndDistribution();
		
		/**
		 * Gets the financial security score.
		 *
		 * @param year the year
		 * @return the financial security score
		 */
		public double getFinancialSecurityScore(long year);
		
		/**
		 * Gets the political power score.
		 *
		 * @param year the year
		 * @return the political power score
		 */
		public double getPoliticalPowerScore(long year);
		
		/**
		 * Gets the aggregate score.
		 *
		 * @param year the year
		 * @return the aggregate score
		 */
		public double getAggregateScore(long year);
	}
}
