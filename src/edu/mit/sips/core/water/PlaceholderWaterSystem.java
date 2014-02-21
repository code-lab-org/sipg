package edu.mit.sips.core.water;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class PlaceholderWaterSystem.
 */
public class PlaceholderWaterSystem extends DefaultWaterSystem implements Serializable {
	private static final long serialVersionUID = 7733181697266453596L;
	private transient long time, nextTime;
	private final Map<Long, Double> electricityConsumptionMap = new HashMap<Long, Double>();
	private final Map<Long, Double> captialExpenseMap = new HashMap<Long, Double>();
	private final Map<Long, Double> cashFlowMap = new HashMap<Long, Double>();
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getCapitalExpense()
	 */
	@Override
	public double getCapitalExpense() {
		if(captialExpenseMap.containsKey(time)) {
			return captialExpenseMap.get(time);
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
	 * @see edu.mit.sips.core.water.DefaultWaterSystem#getElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		if(electricityConsumptionMap.containsKey(time)) {
			return electricityConsumptionMap.get(time);
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
		this.captialExpenseMap.clear();
		this.captialExpenseMap.putAll(captialExpenseMap);
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
	 * Sets the electricity consumption map.
	 *
	 * @param electricityConsumptionMap the electricity consumption map
	 */
	public void setElectricityConsumptionMap(Map<Long, Double> electricityConsumptionMap) {
		this.electricityConsumptionMap.clear();
		this.electricityConsumptionMap.putAll(electricityConsumptionMap);
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