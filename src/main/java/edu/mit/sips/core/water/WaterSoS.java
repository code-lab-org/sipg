/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sips.core.water;

import edu.mit.sips.core.base.InfrastructureSoS;

/**
 * An interface to water sector infrastructure system-of-systems.
 * 
 * @author Paul T. Grogan
 */
public interface WaterSoS extends InfrastructureSoS, WaterSystem {
	
	/**
	 * An interface to locally-controlled water infrastructure 
	 * system-of-systems which provide greater details.
	 * 
	 * @author Paul T. Grogan
	 */
	public static interface Local extends WaterSoS, WaterSystem.Local {
		
		/**
		 * Gets the aggregate score.
		 *
		 * @param year the year
		 * @return the aggregate score
		 */
		public double getAggregateScore(long year);
		
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
		 * Optimize water distribution.
		 */
		public void optimizeWaterDistribution();
		
		/**
		 * Optimize water production and distribution.
		 */
		public void optimizeWaterProductionAndDistribution();
	}
	
	/**
	 * Gets the aquifer security score.
	 *
	 * @return the aquifer security score
	 */
	public double getAquiferSecurityScore();
}
