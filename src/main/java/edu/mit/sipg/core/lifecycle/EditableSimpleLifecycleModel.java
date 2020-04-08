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

import edu.mit.sipg.units.CurrencyUnits;
import edu.mit.sipg.units.TimeUnits;

/**
 * An implementation of a editable lifecycle model for the simple lifecycle model.
 * 
 * @author Paul T. Grogan
 */
public final class EditableSimpleLifecycleModel implements EditableLifecycleModel {
	private static final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	private static final TimeUnits timeUnits = TimeUnits.year;
	
	private long minTimeCommissionStart;
	private long timeCommissionStart;
	private long commissionDuration;
	private long maxOperationDuration;
	private long operationDuration;
	private long decommissionDuration;
	private double totalCommissionCost;
	private double fixedOperationCost;
	private double totalDecommissionCost;
	private boolean spreadCosts;

	@Override
	public DefaultSimpleLifecycleModel createLifecycleModel() {
		DefaultSimpleLifecycleModel m = new DefaultSimpleLifecycleModel(); // for units
		return new DefaultSimpleLifecycleModel(
				(long) TimeUnits.convert(minTimeCommissionStart, this, m),
				(long) TimeUnits.convert(timeCommissionStart, this, m), 
				(long) TimeUnits.convert(commissionDuration, this, m), 
				(long) TimeUnits.convert(maxOperationDuration, this, m),
				(long) TimeUnits.convert(operationDuration, this, m), 
				(long) TimeUnits.convert(decommissionDuration, this, m),
				CurrencyUnits.convertStock(totalCommissionCost, this, m),
				CurrencyUnits.convertFlow(fixedOperationCost, this, m), 
				CurrencyUnits.convertStock(totalDecommissionCost, this, m), 
				spreadCosts);
	}

	/**
	 * Gets the commission duration.
	 *
	 * @return the commission duration
	 */
	public long getCommissionDuration() {
		return commissionDuration;
	}
	
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return timeUnits;
	}

	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}
	
	/**
	 * Gets the decommission duration.
	 *
	 * @return the decommission duration
	 */
	public long getDecommissionDuration() {
		return decommissionDuration;
	}

	/**
	 * Gets the fixed operations cost.
	 *
	 * @return the fixed operations cost
	 */
	public double getFixedOperationsCost() {
		return fixedOperationCost;
	}

	/**
	 * Gets the max operations duration.
	 *
	 * @return the max operations duration
	 */
	public long getMaxOperationsDuration() {
		return maxOperationDuration;
	}

	/**
	 * Gets the max time decommission start.
	 *
	 * @return the max time decommission start
	 */
	public long getMaxTimeDecommissionStart() {
		return timeCommissionStart + commissionDuration + maxOperationDuration;
	}

	/**
	 * Gets the min time commission start.
	 *
	 * @return the min time commission start
	 */
	public long getMinTimeCommissionStart() {
		return minTimeCommissionStart;
	}

	/**
	 * Gets the operation duration.
	 *
	 * @return the operation duration
	 */
	public long getOperationDuration() {
		return operationDuration;
	}

	/**
	 * Gets the time commission start.
	 *
	 * @return the time commission start
	 */
	public long getTimeCommissionStart() {
		return timeCommissionStart;
	}

	/**
	 * Gets the time decommission start.
	 *
	 * @return the time decommission start
	 */
	public long getTimeDecommissionStart() {
		return timeCommissionStart + commissionDuration + operationDuration;
	}

	@Override
	public TimeUnits getTimeUnits() {
		return timeUnits;
	}

	/**
	 * Gets the total commission cost.
	 *
	 * @return the total commission cost
	 */
	public double getTotalCommissionCost() {
		return totalCommissionCost;
	}
	
	/**
	 * Gets the total decommission cost.
	 *
	 * @return the total decommission cost
	 */
	public double getTotalDecommissionCost() {
		return totalDecommissionCost;
	}

	/**
	 * Checks if is spread costs.
	 *
	 * @return true, if is spread costs
	 */
	public boolean isSpreadCosts() {
		return spreadCosts;
	}

	/**
	 * Sets the commission duration.
	 *
	 * @param commissionDuration the new commission duration
	 */
	public void setCommissionDuration(long commissionDuration) {
		this.commissionDuration = commissionDuration;
	}

	/**
	 * Sets the decommission duration.
	 *
	 * @param decommissionDuration the new decommission duration
	 */
	public void setDecommissionDuration(long decommissionDuration) {
		this.decommissionDuration = decommissionDuration;
	}

	/**
	 * Sets the fixed operation cost.
	 *
	 * @param fixedOperationCost the new fixed operation cost
	 */
	public void setFixedOperationCost(double fixedOperationCost) {
		this.fixedOperationCost = fixedOperationCost;
	}

	/**
	 * Sets the max operations duration.
	 *
	 * @param maxOperationDuration the new max operations duration
	 */
	public void setMaxOperationsDuration(long maxOperationDuration) {
		this.maxOperationDuration = maxOperationDuration;
	}

	/**
	 * Sets the min time commission start.
	 *
	 * @param minTimeCommissionStart the new min time commission start
	 */
	public void setMinTimeCommissionStart(long minTimeCommissionStart) {
		this.minTimeCommissionStart = minTimeCommissionStart;
	}

	/**
	 * Sets the operation duration.
	 *
	 * @param operationDuration the new operation duration
	 */
	public void setOperationDuration(long operationDuration) {
		this.operationDuration = operationDuration;
	}

	/**
	 * Sets the spread costs.
	 *
	 * @param spreadCosts the new spread costs
	 */
	public void setSpreadCosts(boolean spreadCosts) {
		this.spreadCosts = spreadCosts;
	}

	/**
	 * Sets the time commission start.
	 *
	 * @param timeCommissionStart the new time commission start
	 */
	public void setTimeCommissionStart(long timeCommissionStart) {
		this.timeCommissionStart = timeCommissionStart;
	}

	/**
	 * Sets the time decommission start.
	 *
	 * @param timeDecommissionStart the new time decommission start
	 */
	public void setTimeDecommissionStart(long timeDecommissionStart) {
		setOperationDuration(timeDecommissionStart - timeCommissionStart - commissionDuration);
	}

	/**
	 * Sets the total commission cost.
	 *
	 * @param totalCommissionCost the new total commission cost
	 */
	public void setTotalCommissionCost(double totalCommissionCost) {
		this.totalCommissionCost = totalCommissionCost;
	}

	/**
	 * Sets the total decommission cost.
	 *
	 * @param totalDecommissionCost the new total decommission cost
	 */
	public void setTotalDecommissionCost(double totalDecommissionCost) {
		this.totalDecommissionCost = totalDecommissionCost;
	}
}
