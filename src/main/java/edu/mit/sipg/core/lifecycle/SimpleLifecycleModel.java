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
package edu.mit.sipg.core.lifecycle;

/**
 * The simple lifecycle model interface assumes three phases: 
 * commissioning, operating, and decommissioning.
 * 
 * @author Paul T. Grogan
 */
public interface SimpleLifecycleModel extends LifecycleModel {
		
	/**
	 * Gets the duration of the commissioning phase.
	 *
	 * @return the commissioning duration
	 */
	public long getCommissionDuration();
	
	/**
	 * Gets the duration of the decommissioning phase.
	 *
	 * @return the decommission duration
	 */
	public long getDecommissionDuration();
	
	/**
	 * Gets the fixed operations cost.
	 *
	 * @return the fixed operations cost
	 */
	public double getFixedOperationsCost();
	
	/**
	 * Gets the maximum operations duration.
	 *
	 * @return the max operations duration
	 */
	public long getMaxOperationsDuration();
	
	/**
	 * Gets the maximum time decommissioning can start.
	 *
	 * @return the max time decommissioning can start
	 */
	public long getMaxTimeDecommissionStart();
	
	/**
	 * Gets the minimum time commissioning can start.
	 *
	 * @return the min time commissioning can start
	 */
	public long getMinTimeCommissionStart();
	
	/**
	 * Gets the duration of the operations phase.
	 *
	 * @return the operation duration
	 */
	public long getOperationDuration();
	
	/**
	 * Gets the time the commissioning phase starts.
	 *
	 * @return the time commissioning starts
	 */
	public long getTimeCommissionStart();
	
	/**
	 * Gets the time the commissioning phase starts.
	 *
	 * @return the time decommissioning starts
	 */
	public long getTimeDecommissionStart();
	
	/**
	 * Gets the total commission (capital) cost.
	 *
	 * @return the commission cost
	 */
	public double getTotalCommissionCost();
	
	/**
	 * Gets the total decommission cost.
	 *
	 * @return the total decommission cost
	 */
	public double getTotalDecommissionCost();
	
	/**
	 * Checks if costs are spread across commissioning and decommissioning phases.
	 *
	 * @return true, if spread costs
	 */
	public boolean isSpreadCosts();
}
