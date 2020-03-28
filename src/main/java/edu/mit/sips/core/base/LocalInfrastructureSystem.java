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
package edu.mit.sips.core.base;

import java.util.HashMap;
import java.util.Map;

import edu.mit.sips.sim.util.CurrencyUnits;

/**
 * A locally-controlled implementation of the infrastructure system interface.
 * 
 * Includes variables to record cash flow and capital expenses over time.
 * 
 * @author Paul T. Grogan
 */
public abstract class LocalInfrastructureSystem extends DefaultInfrastructureSystem implements InfrastructureSystem.Local {
	private double cumulativeCapitalExpense;
	private transient double nextTotalCapitalExpense;
	private double cumulativeCashFlow;
	private transient double nextTotalCashFlow;

	protected transient long time;
	private transient final Map<Long, Double> cashFlowLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> capitalExpenseLog = 
			new HashMap<Long, Double>();
	
	/**
	 * Instantiates a new local infrastructure system.
	 */
	protected LocalInfrastructureSystem() {
		super("Infrastructure");
	}
	
	/**
	 * Instantiates a new local infrastructure system.
	 *
	 * @param name the name
	 */
	public LocalInfrastructureSystem(String name) {
		super(name);
	}
	
	@Override
	public double getCapitalExpense() {
		double value = 0;
		for(InfrastructureElement e : getInternalElements()) {
			value += CurrencyUnits.convertFlow(e.getCapitalExpense(), e, this);
		}
		return value;
	}

	/**
	 * Gets the capital expense log.
	 *
	 * @return the capital expense log
	 */
	public Map<Long, Double> getCapitalExpenseLog() {
		return new HashMap<Long, Double>(capitalExpenseLog);
	}

	@Override
	public double getCashFlow() {
		return getTotalRevenue() - getTotalExpense();
	}

	/**
	 * Gets the cash flow log.
	 *
	 * @return the cash flow map
	 */
	public Map<Long, Double> getCashFlowLog() {
		return new HashMap<Long, Double>(cashFlowLog);
	}

	@Override
	public double getCumulativeCapitalExpense() {
		return cumulativeCapitalExpense;
	}
	
	@Override
	public double getCumulativeCashFlow() {
		return cumulativeCashFlow;
	}

	@Override
	public double getDecommissionExpense() {
		double value = 0;
		for(InfrastructureElement e : getInternalElements()) {
			value += CurrencyUnits.convertFlow(e.getDecommissionExpense(), e, this);
		}
		return value;
	}

	@Override
	public double getLifecycleExpense() {
		 return getCapitalExpense() 
			+ getOperationsExpense() 
			+ getDecommissionExpense();
	}

	@Override
	public String getName() {
		if(getSociety() != null) {
			return getSociety().getName() + " " + super.getName();
		}
		return super.getName();
	}

	@Override
	public double getOperationsExpense() {
		double value = 0;
		for(InfrastructureElement e : getInternalElements()) {
			value += CurrencyUnits.convertFlow(e.getTotalOperationsExpense(), e, this);
		}
		return value;
	}

	@Override
	public double getTotalExpense() {
		return getLifecycleExpense()  
				+ getConsumptionExpense()
				+ getDistributionExpense()
				+ getImportExpense();
	}

	@Override
	public double getTotalRevenue() {
		return getSalesRevenue() 
				+ getDistributionRevenue()
				+ getExportRevenue();
	}

	@Override
	public void initialize(long time) {
		this.time = time;
		cumulativeCapitalExpense = 0;
		cumulativeCashFlow = 0;
		capitalExpenseLog.clear();
		cashFlowLog.clear();
	}
	
	@Override
	public boolean isLocal() {
		return true;
	}
	
	@Override
	public void tick() {
		nextTotalCapitalExpense = getCapitalExpense();
		capitalExpenseLog.put(time, getCapitalExpense());
		nextTotalCashFlow = getCashFlow();
		cashFlowLog.put(time, getCashFlow());
	}

	@Override
	public void tock() {
		time++;
		this.cumulativeCapitalExpense += nextTotalCapitalExpense;
		this.cumulativeCashFlow += nextTotalCashFlow;
	}
}