package edu.mit.sips.core.agriculture;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class PlaceholderAgricultureSystem.
 */
public class PlaceholderAgricultureSystem extends DefaultAgricultureSystem implements Serializable {
	private static final long serialVersionUID = 7733181697266453596L;
	private transient long time, nextTime;
	private final Map<Long, Double> waterConsumptionMap = new HashMap<Long, Double>();
	private final Map<Long, Double> capitalExpenseMap = new HashMap<Long, Double>();
	private final Map<Long, Double> cashFlowMap = new HashMap<Long, Double>();
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getCapitalExpense()
	 */
	@Override
	public double getCapitalExpense() {
		if(capitalExpenseMap.containsKey(time)) {
			return capitalExpenseMap.get(time);
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.DefaultInfrastructureSystem#getCashFlow()
	 */
	@Override
	public double getCashFlow() {
		if(cashFlowMap.containsKey(time)) {
			return cashFlowMap.get(time);
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem#getWaterConsumption()
	 */
	@Override
	public double getWaterConsumption() {
		if(waterConsumptionMap.containsKey(time)) {
			return waterConsumptionMap.get(time);
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.DefaultInfrastructureSystem#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		this.time = time;
	}
	
	/**
	 * Sets the capital expense map.
	 *
	 * @param captialExpenseMap the captial expense map
	 */
	public void setCapitalExpenseMap(Map<Long, Double> captialExpenseMap) {
		this.capitalExpenseMap.clear();
		this.capitalExpenseMap.putAll(captialExpenseMap);
	}
	
	/**
	 * Sets the cash flow map.
	 *
	 * @param cashFlowMap the cash flow map
	 */
	public void setCashFlowMap(Map<Long, Double> cashFlowMap) {
		this.cashFlowMap.clear();
		this.cashFlowMap.putAll(cashFlowMap);
	}
	
	/**
	 * Sets the water consumption map.
	 *
	 * @param waterConsumptionMap the water consumption map
	 */
	public void setWaterConsumptionMap(Map<Long, Double> waterConsumptionMap) {
		this.waterConsumptionMap.clear();
		this.waterConsumptionMap.putAll(waterConsumptionMap);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.DefaultInfrastructureSystem#tick()
	 */
	@Override
	public void tick() {
		nextTime = time + 1;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.DefaultInfrastructureSystem#tock()
	 */
	@Override
	public void tock() {
		time = nextTime;
	}
}
