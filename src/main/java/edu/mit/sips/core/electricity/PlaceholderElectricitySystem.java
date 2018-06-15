package edu.mit.sips.core.electricity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class PlaceholderElectricitySystem.
 */
public class PlaceholderElectricitySystem extends DefaultElectricitySystem implements Serializable {
	private static final long serialVersionUID = 7733181697266453596L;
	private transient long time, nextTime;
	private final Map<Long, Double> petroleumConsumptionMap = new HashMap<Long, Double>();
	private final Map<Long, Double> waterConsumptionMap = new HashMap<Long, Double>();
	private final Map<Long, Double> capitalExpenseMap = new HashMap<Long, Double>();
	private final Map<Long, Double> cashFlowMap = new HashMap<Long, Double>();
	private final Map<Long, Double> electricityDomesticPriceMap = new HashMap<Long, Double>();

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
	 * @see edu.mit.sips.core.electricity.DefaultElectricitySystem#getElectricityDomesticPrice()
	 */
	@Override
	public double getElectricityDomesticPrice() {
		if(electricityDomesticPriceMap.containsKey(time)) {
			return electricityDomesticPriceMap.get(time);
		} else {
			return 0; // return 4;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.electricity.DefaultElectricitySystem#getPetroleumConsumption()
	 */
	@Override
	public double getPetroleumConsumption() {
		if(petroleumConsumptionMap.containsKey(time)) {
			return petroleumConsumptionMap.get(time);
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.electricity.DefaultElectricitySystem#getWaterConsumption()
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
	 * Sets the electricity domestic price map.
	 *
	 * @param electricityDomesticPriceMap the electricity domestic price map
	 */
	public void setElectricityDomesticPriceMap(Map<Long, Double> electricityDomesticPriceMap) {
		this.electricityDomesticPriceMap.clear();
		this.electricityDomesticPriceMap.putAll(electricityDomesticPriceMap);
	}
	
	/**
	 * Sets the petroleum consumption map.
	 *
	 * @param petroleumConsumptionMap the petroleum consumption map
	 */
	public void setPetroleumConsumptionMap(Map<Long, Double> petroleumConsumptionMap) {
		this.petroleumConsumptionMap.clear();
		this.petroleumConsumptionMap.putAll(petroleumConsumptionMap);
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
