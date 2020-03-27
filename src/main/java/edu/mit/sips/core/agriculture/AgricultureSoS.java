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
package edu.mit.sips.core.agriculture;

import edu.mit.sips.core.InfrastructureSoS;

/**
 * An interface to agriculture sector infrastructure system-of-systems.
 * 
 * @author Paul T. Grogan
 */
public interface AgricultureSoS extends InfrastructureSoS, AgricultureSystem {

	/**
	 * An interface to locally-controlled agriculture infrastructure 
	 * system-of-systems which provide greater details.
	 * 
	 * @author Paul T. Grogan
	 */
	public static interface Local extends AgricultureSoS, AgricultureSystem.Local {
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
		 * Optimize food distribution.
		 */
		public void optimizeFoodDistribution();
		
		/**
		 * Optimize food production and distribution.
		 */
		public void optimizeFoodProductionAndDistribution();
	}
	
	/**
	 * Gets the food security score.
	 *
	 * @return the food security score
	 */
	public double getFoodSecurityScore();
}
