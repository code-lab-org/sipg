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
package edu.mit.sips.core.petroleum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * An offline implementation of an petroleum system that replays recorded data.
 * 
 * @author Paul T. Grogan
 */
public class RecordedPetroleumSystem extends DefaultPetroleumSystem implements Serializable {
	private static final long serialVersionUID = 7733181697266453596L;
	private transient long time, nextTime;
	private final Map<Long, Double> electricityConsumptionLog = new HashMap<Long, Double>();
	private final Map<Long, Double> captialExpenseLog = new HashMap<Long, Double>();
	private final Map<Long, Double> cashFlowLog = new HashMap<Long, Double>();
	private final Map<Long, Double> petroleumReservoirVolumeLog = new HashMap<Long, Double>();
	private final Map<Long, Double> petroleumWithdrawalsLog = new HashMap<Long, Double>();
	private final Map<Long, Double> petroleumDomesticPriceLog = new HashMap<Long, Double>();
	private final Map<Long, Double> petroleumImportPriceLog = new HashMap<Long, Double>();
	private final Map<Long, Double> petroleumExportPriceLog = new HashMap<Long, Double>();

	@Override
	public double getCapitalExpense() {
		if(captialExpenseLog.containsKey(time)) {
			return captialExpenseLog.get(time);
		} else {
			return 0;
		}
	}

	@Override
	public double getCashFlow() {
		if(cashFlowLog.containsKey(time)) {
			return cashFlowLog.get(time);
		} else {
			return 0;
		}
	}

	@Override
	public double getElectricityConsumption() {
		if(electricityConsumptionLog.containsKey(time)) {
			return electricityConsumptionLog.get(time);
		} else {
			return 0;
		}
	}

	@Override
	public double getPetroleumDomesticPrice() {
		if(petroleumDomesticPriceLog.containsKey(time)) {
			return petroleumDomesticPriceLog.get(time);
		} else {
			return 0;
		}
	}

	@Override
	public double getPetroleumExportPrice() {
		if(petroleumExportPriceLog.containsKey(time)) {
			return petroleumExportPriceLog.get(time);
		} else {
			return 0;
		}
	}
	
	@Override
	public double getPetroleumImportPrice() {
		if(petroleumImportPriceLog.containsKey(time)) {
			return petroleumImportPriceLog.get(time);
		} else {
			return 0;
		}
	}

	@Override
	public double getReservoirVolume() {
		if(petroleumReservoirVolumeLog.containsKey(time)) {
			return petroleumReservoirVolumeLog.get(time);
		} else {
			return 0;
		}
	}
	
	@Override
	public double getReservoirWithdrawals() {
		if(petroleumWithdrawalsLog.containsKey(time)) {
			return petroleumWithdrawalsLog.get(time);
		} else {
			return 0;
		}
	}
	
	@Override
	public void initialize(long time) {
		this.time = time;
	}

	/**
	 * Sets the recorded capital expense.
	 *
	 * @param captialExpenseLog the captial expense log
	 */
	public void setRecordedCapitalExpense(Map<Long, Double> captialExpenseLog) {
		this.captialExpenseLog.clear();
		this.captialExpenseLog.putAll(captialExpenseLog);
	}

	/**
	 * Sets the recorded cash flow.
	 *
	 * @param cashFlowLog the cash flow log
	 */
	public void setRecordedCashFlow(Map<Long, Double> cashFlowLog) {
		this.cashFlowLog.clear();
		this.cashFlowLog.putAll(cashFlowLog);
	}

	/**
	 * Sets the recorded electricity consumption.
	 *
	 * @param electricityConsumptionLog the electricity consumption log
	 */
	public void setRecordedElectricityConsumption(Map<Long, Double> electricityConsumptionLog) {
		this.electricityConsumptionLog.clear();
		this.electricityConsumptionLog.putAll(electricityConsumptionLog);
	}
	
	/**
	 * Sets the recorded petroleum domestic price.
	 *
	 * @param petroleumDomesticPriceLog the petroleum domestic price log
	 */
	public void setRecordedPetroleumDomesticPrice(Map<Long, Double> petroleumDomesticPriceLog) {
		this.petroleumDomesticPriceLog.clear();
		this.petroleumDomesticPriceLog.putAll(petroleumDomesticPriceLog);
	}
	
	/**
	 * Sets the recorded petroleum export price.
	 *
	 * @param petroleumExportPriceLog the petroleum export price log
	 */
	public void setRecordedPetroleumExportPrice(Map<Long, Double> petroleumExportPriceLog) {
		this.petroleumExportPriceLog.clear();
		this.petroleumExportPriceLog.putAll(petroleumExportPriceLog);
	}

	/**
	 * Sets the recorded petroleum import price.
	 *
	 * @param petroleumImportPriceLog the petroleum import price log
	 */
	public void setRecordedPetroleumImportPrice(Map<Long, Double> petroleumImportPriceLog) {
		this.petroleumImportPriceLog.clear();
		this.petroleumImportPriceLog.putAll(petroleumImportPriceLog);
	}

	/**
	 * Sets the recorded petroleum reservoir volume.
	 *
	 * @param petroleumReservoirVolumeLog the petroleum reservoir volume log
	 */
	public void setRecordedPetroleumReservoirVolume(Map<Long, Double> petroleumReservoirVolumeLog) {
		this.petroleumReservoirVolumeLog.clear();
		this.petroleumReservoirVolumeLog.putAll(petroleumReservoirVolumeLog);
	}

	/**
	 * Sets the recorded petroleum withdrawals.
	 *
	 * @param petroleumWithdrawalsLog the petroleum withdrawals log
	 */
	public void setRecordedPetroleumWithdrawals(Map<Long, Double> petroleumWithdrawalsLog) {
		this.petroleumWithdrawalsLog.clear();
		this.petroleumWithdrawalsLog.putAll(petroleumWithdrawalsLog);
	}

	@Override
	public void tick() {
		nextTime = time + 1;
	}

	@Override
	public void tock() {
		time = nextTime;
	}
}
