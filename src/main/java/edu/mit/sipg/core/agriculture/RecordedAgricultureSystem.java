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
package edu.mit.sipg.core.agriculture;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * An offline implementation of an agriculture system that replays recorded data.
 * 
 * @author Paul T. Grogan
 */
public class RecordedAgricultureSystem extends DefaultAgricultureSystem implements Serializable {
	private static final long serialVersionUID = 7733181697266453596L;
	private transient long time, nextTime;
	private final Map<Long, Double> waterConsumptionLog = new HashMap<Long, Double>();
	private final Map<Long, Double> capitalExpenseLog = new HashMap<Long, Double>();
	private final Map<Long, Double> cashFlowLog = new HashMap<Long, Double>();
	private final Map<Long, Double> totalFoodSupplyLog = new HashMap<Long, Double>();
	private final Map<Long, Double> foodProductionLog = new HashMap<Long, Double>();
	private final Map<Long, Double> foodDomesticPriceLog = new HashMap<Long, Double>();
	private final Map<Long, Double> foodImportPriceLog = new HashMap<Long, Double>();
	private final Map<Long, Double> foodExportPriceLog = new HashMap<Long, Double>();
	
	@Override
	public double getCapitalExpense() {
		if(capitalExpenseLog.containsKey(time)) {
			return capitalExpenseLog.get(time);
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
	public double getFoodDomesticPrice() {
		if(foodDomesticPriceLog.containsKey(time)) {
			return foodDomesticPriceLog.get(time);
		} else {
			return 0;
		}
	}

	@Override
	public double getFoodExportPrice() {
		if(foodExportPriceLog.containsKey(time)) {
			return foodExportPriceLog.get(time);
		} else {
			return 0;
		}
	}

	@Override
	public double getFoodImportPrice() {
		if(foodImportPriceLog.containsKey(time)) {
			return foodImportPriceLog.get(time);
		} else {
			return 0;
		}
	}

	@Override
	public double getFoodProduction() {
		if(foodProductionLog.containsKey(time)) {
			return foodProductionLog.get(time);
		} else {
			return 0;
		}
	}
	
	@Override
	public double getTotalFoodSupply() {
		if(totalFoodSupplyLog.containsKey(time)) {
			return totalFoodSupplyLog.get(time);
		} else {
			return 0;
		}
	}

	@Override
	public double getWaterConsumption() {
		if(waterConsumptionLog.containsKey(time)) {
			return waterConsumptionLog.get(time);
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
	 * @param capitalExpenseLog the capital expense log
	 */
	public void setRecordedCapitalExpense(Map<Long, Double> capitalExpenseLog) {
		this.capitalExpenseLog.clear();
		this.capitalExpenseLog.putAll(capitalExpenseLog);
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
	 * Sets the recorded food domestic price.
	 *
	 * @param foodDomesticPriceLog the food domestic price log
	 */
	public void setRecordedFoodDomesticPrice(Map<Long, Double> foodDomesticPriceLog) {
		this.foodDomesticPriceLog.clear();
		this.foodDomesticPriceLog.putAll(foodDomesticPriceLog);
	}
	
	/**
	 * Sets the recorded food export price.
	 *
	 * @param foodExportPriceLog the food export price log
	 */
	public void setRecordedFoodExportPrice(Map<Long, Double> foodExportPriceLog) {
		this.foodExportPriceLog.clear();
		this.foodExportPriceLog.putAll(foodExportPriceLog);
	}
	
	/**
	 * Sets the recorded food import price.
	 *
	 * @param foodImportPriceLog the food import price log
	 */
	public void setRecordedFoodImportPrice(Map<Long, Double> foodImportPriceLog) {
		this.foodImportPriceLog.clear();
		this.foodImportPriceLog.putAll(foodImportPriceLog);
	}
	
	/**
	 * Sets the recorded food production.
	 *
	 * @param foodProductionLog the food production map
	 */
	public void setRecordedFoodProduction(Map<Long, Double> foodProductionLog) {
		this.foodProductionLog.clear();
		this.foodProductionLog.putAll(foodProductionLog);
	}
	
	/**
	 * Sets the recorded total food supply.
	 *
	 * @param totalFoodSupplyLog the total food supply log
	 */
	public void setRecordedTotalFoodSupply(Map<Long, Double> totalFoodSupplyLog) {
		this.totalFoodSupplyLog.clear();
		this.totalFoodSupplyLog.putAll(totalFoodSupplyLog);
	}
	
	/**
	 * Sets the recorded water consumption.
	 *
	 * @param waterConsumptionLog the water consumption log
	 */
	public void setRecordedWaterConsumption(Map<Long, Double> waterConsumptionLog) {
		this.waterConsumptionLog.clear();
		this.waterConsumptionLog.putAll(waterConsumptionLog);
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
