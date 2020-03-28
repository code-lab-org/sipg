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

import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.TimeUnits;

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

	@Override
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

	@Override
	public long getDecommissionDuration() {
		return decommissionDuration;
	}

	@Override
	public double getFixedOperationsCost() {
		return fixedOperationCost;
	}

	@Override
	public long getMaxOperationsDuration() {
		return maxOperationDuration;
	}

	@Override
	public long getMaxTimeDecommissionStart() {
		return timeCommissionStart + commissionDuration + maxOperationDuration;
	}

	@Override
	public long getMinTimeCommissionStart() {
		return minTimeCommissionStart;
	}

	@Override
	public long getOperationDuration() {
		return operationDuration;
	}

	@Override
	public long getTimeCommissionStart() {
		return timeCommissionStart;
	}

	@Override
	public long getTimeDecommissionStart() {
		return timeCommissionStart + commissionDuration + operationDuration;
	}

	@Override
	public TimeUnits getTimeUnits() {
		return timeUnits;
	}

	@Override
	public double getTotalCommissionCost() {
		return totalCommissionCost;
	}
	
	@Override
	public double getTotalDecommissionCost() {
		return totalDecommissionCost;
	}

	@Override
	public boolean isSpreadCosts() {
		return spreadCosts;
	}

	@Override
	public void setCommissionDuration(long commissionDuration) {
		this.commissionDuration = commissionDuration;
	}

	@Override
	public void setDecommissionDuration(long decommissionDuration) {
		this.decommissionDuration = decommissionDuration;
	}

	@Override
	public void setFixedOperationCost(double fixedOperationCost) {
		this.fixedOperationCost = fixedOperationCost;
	}

	@Override
	public void setMaxOperationsDuration(long maxOperationDuration) {
		this.maxOperationDuration = maxOperationDuration;
	}

	@Override
	public void setMinTimeCommissionStart(long minTimeCommissionStart) {
		this.minTimeCommissionStart = minTimeCommissionStart;
	}

	@Override
	public void setOperationDuration(long operationDuration) {
		this.operationDuration = operationDuration;
	}

	@Override
	public void setSpreadCosts(boolean spreadCosts) {
		this.spreadCosts = spreadCosts;
	}

	@Override
	public void setTimeCommissionStart(long timeCommissionStart) {
		this.timeCommissionStart = timeCommissionStart;
	}

	@Override
	public void setTimeDecommissionStart(long timeDecommissionStart) {
		setOperationDuration(timeDecommissionStart - timeCommissionStart - commissionDuration);
	}

	@Override
	public void setTotalCommissionCost(double totalCommissionCost) {
		this.totalCommissionCost = totalCommissionCost;
	}

	@Override
	public void setTotalDecommissionCost(double totalDecommissionCost) {
		this.totalDecommissionCost = totalDecommissionCost;
	}
}
