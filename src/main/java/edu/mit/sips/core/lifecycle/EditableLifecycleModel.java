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
package edu.mit.sips.core.lifecycle;

import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.TimeUnitsOutput;

/**
 * An interface to a mutable (editable) lifecycle model.
 * 
 * @author Paul T. Grogan
 */
public interface EditableLifecycleModel extends TimeUnitsOutput, CurrencyUnitsOutput {
	
	/**
	 * Creates the associated lifecycle model.
	 *
	 * @return the lifecycle model
	 */
	public LifecycleModel createLifecycleModel();

	/**
	 * Gets the commission duration.
	 *
	 * @return the commission duration
	 */
	long getCommissionDuration();

	/**
	 * Gets the decommission duration.
	 *
	 * @return the decommission duration
	 */
	long getDecommissionDuration();

	/**
	 * Gets the fixed operations cost.
	 *
	 * @return the fixed operations cost
	 */
	double getFixedOperationsCost();

	/**
	 * Gets the max operations duration.
	 *
	 * @return the max operations duration
	 */
	long getMaxOperationsDuration();

	/**
	 * Gets the max time decommission start.
	 *
	 * @return the max time decommission start
	 */
	long getMaxTimeDecommissionStart();

	/**
	 * Gets the min time commission start.
	 *
	 * @return the min time commission start
	 */
	long getMinTimeCommissionStart();

	/**
	 * Gets the operation duration.
	 *
	 * @return the operation duration
	 */
	long getOperationDuration();

	/**
	 * Gets the time commission start.
	 *
	 * @return the time commission start
	 */
	long getTimeCommissionStart();

	/**
	 * Gets the time decommission start.
	 *
	 * @return the time decommission start
	 */
	long getTimeDecommissionStart();

	/**
	 * Gets the total commission cost.
	 *
	 * @return the total commission cost
	 */
	double getTotalCommissionCost();

	/**
	 * Gets the total decommission cost.
	 *
	 * @return the total decommission cost
	 */
	double getTotalDecommissionCost();

	/**
	 * Checks if is spread costs.
	 *
	 * @return true, if is spread costs
	 */
	boolean isSpreadCosts();

	/**
	 * Sets the commission duration.
	 *
	 * @param commissionDuration the new commission duration
	 */
	public void setCommissionDuration(long commissionDuration);

	/**
	 * Sets the decommission duration.
	 *
	 * @param decommissionDuration the new decommission duration
	 */
	public void setDecommissionDuration(long decommissionDuration);

	/**
	 * Sets the fixed operation cost.
	 *
	 * @param fixedOperationCost the new fixed operation cost
	 */
	public void setFixedOperationCost(double fixedOperationCost);

	/**
	 * Sets the maximum operation duration.
	 *
	 * @param maxOperationDuration the new max operation duration
	 */
	public void setMaxOperationsDuration(long maxOperationDuration);

	/**
	 * Sets the min time commission can start.
	 *
	 * @param minTimeCommissionStart the new min time commission start
	 */
	public void setMinTimeCommissionStart(long minTimeCommissionStart);

	/**
	 * Sets the operation duration.
	 *
	 * @param operationDuration the new operation duration
	 */
	public void setOperationDuration(long operationDuration) ;

	/**
	 * Sets the spread costs.
	 *
	 * @param spreadCosts the new spread costs
	 */
	public void setSpreadCosts(boolean spreadCosts);

	/**
	 * Sets the time commission starts.
	 *
	 * @param timeInitialized the new time initialized
	 */
	public void setTimeCommissionStart(long timeCommissionStart);

	/**
	 * Sets the time decommission start.
	 *
	 * @param timeDecommissionStart the new time decommission start
	 */
	public void setTimeDecommissionStart(long timeDecommissionStart);

	/**
	 * Sets the total commission cost.
	 *
	 * @param totalCommissionCost the new total commission cost
	 */
	public void setTotalCommissionCost(double totalCommissionCost);

	/**
	 * Sets the total decommission cost.
	 *
	 * @param totalDecommissionCost the new total decommission cost
	 */
	public void setTotalDecommissionCost(double totalDecommissionCost);
}
