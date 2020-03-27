package edu.mit.sips.core.agriculture;

import edu.mit.sips.core.InfrastructureSoS;
import edu.mit.sips.core.OptimizationOptions;

/**
 * The Interface AgricultureSoS.
 */
public interface AgricultureSoS extends InfrastructureSoS, AgricultureSystem {
	
	/**
	 * Gets the food security score.
	 *
	 * @return the food security score
	 */
	public double getFoodSecurityScore();
	
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
