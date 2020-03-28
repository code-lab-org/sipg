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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * An offline implementation of an petroleum system that replays recorded data.
 * 
 * @author Paul T. Grogan
 */
public class RecordedWaterSystem extends DefaultWaterSystem implements Serializable {
	private static final long serialVersionUID = 7733181697266453596L;
	private transient long time, nextTime;
	private final Map<Long, Double> electricityConsumptionLog = new HashMap<Long, Double>();
	private final Map<Long, Double> captialExpenseLog = new HashMap<Long, Double>();
	private final Map<Long, Double> cashFlowLog = new HashMap<Long, Double>();
	private final Map<Long, Double> waterReservoirVolumeLog = new HashMap<Long, Double>();
	private final Map<Long, Double> reservoirWithdrawalsLog = new HashMap<Long, Double>();
	private final Map<Long, Double> waterDomesticPriceLog = new HashMap<Long, Double>();
	private final Map<Long, Double> waterImportPriceLog = new HashMap<Long, Double>();

	@Override
	public double getAquiferWithdrawals() {
		if(reservoirWithdrawalsLog.containsKey(time)) {
			return reservoirWithdrawalsLog.get(time);
		} else {
			return 0;
		}
	}
	
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
	public double getWaterDomesticPrice() {
		if(waterDomesticPriceLog.containsKey(time)) {
			return waterDomesticPriceLog.get(time);
		} else {
			return 0;
		}
	}

	@Override
	public double getWaterImportPrice() {
		if(waterImportPriceLog.containsKey(time)) {
			return waterImportPriceLog.get(time);
		} else {
			return 0;
		}
	}

	@Override
	public double getWaterReservoirVolume() {
		if(waterReservoirVolumeLog.containsKey(time)) {
			return waterReservoirVolumeLog.get(time);
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
	 * Sets the recorded reservoir withdrawals.
	 *
	 * @param reservoirWithdrawalsLog the reservoir withdrawals log
	 */
	public void setRecordedReservoirWithdrawals(Map<Long, Double> reservoirWithdrawalsLog) {
		this.reservoirWithdrawalsLog.clear();
		this.reservoirWithdrawalsLog.putAll(reservoirWithdrawalsLog);
	}
	
	/**
	 * Sets the recorded water domestic price.
	 *
	 * @param waterDomesticPriceLog the water domestic price log
	 */
	public void setRecordedWaterDomesticPrice(Map<Long, Double> waterDomesticPriceLog) {
		this.waterDomesticPriceLog.clear();
		this.waterDomesticPriceLog.putAll(waterDomesticPriceLog);
	}

	/**
	 * Sets the recorded water import price.
	 *
	 * @param waterImportPriceLog the water import price log
	 */
	public void setRecordedWaterImportPrice(Map<Long, Double> waterImportPriceLog) {
		this.waterImportPriceLog.clear();
		this.waterImportPriceLog.putAll(waterImportPriceLog);
	}
	
	/**
	 * Sets the recorded water reservoir volume.
	 *
	 * @param waterReservoirVolumeLog the water reservoir volume log
	 */
	public void setRecordedWaterReservoirVolume(Map<Long, Double> waterReservoirVolumeLog) {
		this.waterReservoirVolumeLog.clear();
		this.waterReservoirVolumeLog.putAll(waterReservoirVolumeLog);
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
