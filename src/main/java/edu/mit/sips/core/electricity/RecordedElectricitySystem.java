package edu.mit.sips.core.electricity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * An offline implementation of an electricity system that replays recorded data.
 * 
 * @author Paul T. Grogan
 */
public class RecordedElectricitySystem extends DefaultElectricitySystem implements Serializable {
	private static final long serialVersionUID = 7733181697266453596L;
	private transient long time, nextTime;
	private final Map<Long, Double> petroleumConsumptionLog = new HashMap<Long, Double>();
	private final Map<Long, Double> waterConsumptionLog = new HashMap<Long, Double>();
	private final Map<Long, Double> capitalExpenseLog = new HashMap<Long, Double>();
	private final Map<Long, Double> cashFlowLog = new HashMap<Long, Double>();
	private final Map<Long, Double> electricityDomesticPriceLog = new HashMap<Long, Double>();

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
	public double getElectricityDomesticPrice() {
		if(electricityDomesticPriceLog.containsKey(time)) {
			return electricityDomesticPriceLog.get(time);
		} else {
			return 0; // return 4;
		}
	}

	@Override
	public double getPetroleumConsumption() {
		if(petroleumConsumptionLog.containsKey(time)) {
			return petroleumConsumptionLog.get(time);
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
	 * @param capitalExpenseLog the capital log map
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
	 * Sets the recorded electricity domestic price.
	 *
	 * @param electricityDomesticPriceLog the electricity domestic price log
	 */
	public void setRecordedElectricityDomesticPrice(Map<Long, Double> electricityDomesticPriceLog) {
		this.electricityDomesticPriceLog.clear();
		this.electricityDomesticPriceLog.putAll(electricityDomesticPriceLog);
	}
	
	/**
	 * Sets the recorded petroleum consumption.
	 *
	 * @param petroleumConsumptionLog the petroleum consumption log
	 */
	public void setRecordedPetroleumConsumption(Map<Long, Double> petroleumConsumptionLog) {
		this.petroleumConsumptionLog.clear();
		this.petroleumConsumptionLog.putAll(petroleumConsumptionLog);
	}
	
	/**
	 * Sets the recorded water consumption.
	 *
	 * @param waterConsumptionLog the water consumption map
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
