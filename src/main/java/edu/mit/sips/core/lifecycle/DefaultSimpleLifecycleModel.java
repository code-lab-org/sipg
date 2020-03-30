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

import edu.mit.sips.units.CurrencyUnits;
import edu.mit.sips.units.TimeUnits;

/**
 * A simple implementation of the lifecycle model interface. 
 * Assumes lifecycle phases: empty, initializing (commissioning), operating, 
 * decommissioning, and null.
 * 
 * @author Paul T. Grogan, ptgrogan@mit.edu
 */
public class DefaultSimpleLifecycleModel implements SimpleLifecycleModel {
	private static final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	private static final TimeUnits timeUnits = TimeUnits.year;
	
	private long time;
	private transient long nextTime;
	private final long minTimeCommissionStart;
	private final long timeCommissionStart;
	private final long commissionDuration;
	private final long maxOperationDuration;
	private final long operationDuration;
	private final long decommissionDuration;
	private final double totalCommissionCost;
	private final double fixedOperationCost;
	private final double totalDecommissionCost;
	private final boolean spreadCosts;
	
	/**
	 * Instantiates a new simple lifecycle model.
	 */
	protected DefaultSimpleLifecycleModel() {
		minTimeCommissionStart = 0;
		timeCommissionStart = 0;
		commissionDuration = 0;
		maxOperationDuration = 0;
		operationDuration = 0;
		decommissionDuration = 0;
		totalCommissionCost = 0;
		fixedOperationCost = 0;
		totalDecommissionCost = 0;
		spreadCosts = false;
	}

	/**
	 * Instantiates a new simple lifecycle model.
	 *
	 * @param minTimeCommissionStart the min time commission start
	 * @param timeCommissionStart the time commission start
	 * @param commissionDuration the commission duration
	 * @param maxOperationDuration the max operation duration
	 * @param timeDecommissionStart the time decommission start
	 * @param decommissionDuration the decommission duration
	 * @param totalCommissionCost the total commission cost
	 * @param fixedOperationCost the fixed operation cost
	 * @param totalDecommissionCost the total decommission cost
	 */
	public DefaultSimpleLifecycleModel(long minTimeCommissionStart, long timeCommissionStart, 
			long commissionDuration, long maxOperationDuration,
			long timeDecommissionStart, long decommissionDuration,
			double totalCommissionCost, double fixedOperationCost, 
			double totalDecommissionCost) {
		this(minTimeCommissionStart, timeCommissionStart, commissionDuration, 
				maxOperationDuration, 
				timeDecommissionStart-timeCommissionStart-commissionDuration, 
				decommissionDuration, totalCommissionCost, 
				fixedOperationCost, totalDecommissionCost, false);
	}
	
	/**
	 * Instantiates a new simple lifecycle model.
	 *
	 * @param minTimeCommissionStart the min time commission start
	 * @param timeCommissionStart the time commission start
	 * @param commissionDuration the commission duration
	 * @param maxOperationDuration the max operation duration
	 * @param operationDuration the operation duration
	 * @param decommissionDuration the decommission duration
	 * @param totalCommissionCost the total commission cost
	 * @param fixedOperationCost the fixed operation cost
	 * @param totalDecommissionCost the total decommission cost
	 * @param spreadCosts the spread costs
	 */
	public DefaultSimpleLifecycleModel(long minTimeCommissionStart, long timeCommissionStart, 
			long commissionDuration, long maxOperationDuration,
			long operationDuration, long decommissionDuration, double totalCommissionCost, 
			double fixedOperationCost, double totalDecommissionCost, 
			boolean spreadCosts) {
		this.minTimeCommissionStart = minTimeCommissionStart;
		
		if(timeCommissionStart < minTimeCommissionStart) {
			throw new IllegalArgumentException(
					"Time commission start cannot precede minimum time available.");
		}
		this.timeCommissionStart = timeCommissionStart;
		
		if(commissionDuration < 0) {
			throw new IllegalArgumentException(
					"Commission duration cannot be negative.");
		}
		this.commissionDuration = commissionDuration;

		if(maxOperationDuration < 0) {
			throw new IllegalArgumentException(
					"Max operation duration cannot be negative.");
		}
		this.maxOperationDuration = maxOperationDuration;
		
		if(operationDuration > this.maxOperationDuration) {
			throw new IllegalArgumentException(
					"Operation duration cannot exceed maximum.");
		}
		this.operationDuration = operationDuration;
		
		if(decommissionDuration < 0) {
			throw new IllegalArgumentException(
					"Decommission duration cannot be negative.");
		}
		this.decommissionDuration = decommissionDuration;
		
		if(totalCommissionCost < 0) {
			throw new IllegalArgumentException(
					"Total commissioning cost cannot be negative.");
		}
		this.totalCommissionCost = totalCommissionCost;
		
		if(fixedOperationCost < 0) {
			throw new IllegalArgumentException(
					"Fixed operation cost cannot be negative.");
		}
		this.fixedOperationCost = fixedOperationCost;

		if(totalDecommissionCost < 0) {
			throw new IllegalArgumentException(
					"Total decommission cost cannot be negative.");
		}
		this.totalDecommissionCost = totalDecommissionCost;
		
		this.spreadCosts = spreadCosts;
	}

	@Override
	public double getTotalCommissionCost() {
		return totalCommissionCost;
	}

	@Override
	public double getCapitalExpense() {
		if(time == timeCommissionStart 
				&& (commissionDuration == 0 || !spreadCosts)) {
			return totalCommissionCost;
		} else if(spreadCosts 
				&& commissionDuration > 0
				&& time >= timeCommissionStart 
				&& time < timeCommissionStart + commissionDuration) {
			return totalCommissionCost / commissionDuration;
		} else {
			return 0;
		}
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
	public double getTotalDecommissionCost() {
		return totalDecommissionCost;
	}

	@Override
	public long getDecommissionDuration() {
		return decommissionDuration;
	}

	@Override
	public double getDecommissionExpense() {
		if(time == getTimeDecommissionStart()
				&& (decommissionDuration == 0 || !spreadCosts)) {
			return totalDecommissionCost;
		} else if(spreadCosts 
				&& decommissionDuration > 0
				&& time >= getTimeDecommissionStart() 
				&& time < getTimeDecommissionStart() + decommissionDuration) {
			return totalDecommissionCost / decommissionDuration;
		} else {
			return 0;
		}
	}

	@Override
	public double getFixedOperationsCost() {
		return fixedOperationCost;
	}
	
	@Override
	public double getFixedOperationsExpense() {
		if(isOperational()) {
			return fixedOperationCost;
		} else {
			return 0;
		}
	}

	@Override
	public long getCommissionDuration() {
		return commissionDuration;
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
	public EditableSimpleLifecycleModel getMutableLifecycleModel() {
		EditableSimpleLifecycleModel model = new EditableSimpleLifecycleModel();
		model.setMinTimeCommissionStart((long) TimeUnits.convert(
				minTimeCommissionStart, this, model));
		model.setTimeCommissionStart((long) TimeUnits.convert(
				timeCommissionStart, this, model));
		model.setCommissionDuration((long) TimeUnits.convert(
				commissionDuration, this, model));
		model.setMaxOperationsDuration((long) TimeUnits.convert(
				maxOperationDuration, this, model));
		model.setOperationDuration((long) TimeUnits.convert(
				operationDuration, this, model));
		model.setDecommissionDuration((long) TimeUnits.convert(
				decommissionDuration, this, model));
		model.setTotalCommissionCost(CurrencyUnits.convertStock(
				totalCommissionCost, this, model));
		model.setFixedOperationCost(CurrencyUnits.convertFlow(
				fixedOperationCost, this, model));
		model.setTotalDecommissionCost(CurrencyUnits.convertStock(
				totalDecommissionCost, this, model));
		model.setSpreadCosts(spreadCosts);
		return model;
	}

	@Override
	public long getMinTimeCommissionStart() {
		return minTimeCommissionStart;
	}

	@Override
	public long getTimeDecommissionStart() {
		return timeCommissionStart + commissionDuration + operationDuration;
	}

	@Override
	public long getTimeCommissionStart() {
		return timeCommissionStart;
	}
	
	@Override
	public TimeUnits getTimeUnits() {
		return timeUnits;
	}
	
	@Override
	public void initialize(long time) {
		this.time = time;
	}

	@Override
	public boolean isExists() {
		return time >= timeCommissionStart 
				&& time < getTimeDecommissionStart() + decommissionDuration;
	}

	@Override
	public boolean isSpreadCosts() {
		return spreadCosts;
	}

	@Override
	public boolean isOperational() {
		return time >= timeCommissionStart + commissionDuration 
				&& time < getTimeDecommissionStart();
	}

	@Override
	public void tick() {
		nextTime = time + 1;
	}

	@Override
	public void tock() {
		time = nextTime;
	}

	@Override
	public long getOperationDuration() {
		return operationDuration;
	}
}
