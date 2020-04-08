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
package edu.mit.sipg.core.electricity;

import edu.mit.sipg.core.base.InfrastructureSoS;

/**
 * An interface to electricity sector infrastructure system-of-systems.
 * 
 * @author Paul T. Grogan
 */
public interface ElectricitySoS extends InfrastructureSoS, ElectricitySystem {
	
	/**
	 * An interface to locally-controlled electricity infrastructure 
	 * system-of-systems which provide greater details.
	 * 
	 * @author Paul T. Grogan
	 */
	public interface Local extends ElectricitySoS, ElectricitySystem.Local {
		
		/**
		 * Optimize electricity distribution.
		 */
		public void optimizeElectricityDistribution();
		
		/**
		 * Optimize electricity production and distribution.
		 */
		public void optimizeElectricityProductionAndDistribution();
	}
}
