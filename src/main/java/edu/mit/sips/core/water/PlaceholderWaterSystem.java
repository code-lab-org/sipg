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
	private final Map<Long, Double> waterReservoirVolumeMap = new HashMap<Long, Double>();
	private final Map<Long, Double> reservoirWithdrawalsMap = new HashMap<Long, Double>();
	private final Map<Long, Double> waterDomesticPriceMap = new HashMap<Long, Double>();
	private final Map<Long, Double> waterImportPriceMap = new HashMap<Long, Double>();

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
	 * @see edu.mit.sips.core.water.DefaultWaterSystem#getReservoirWithdrawals()
	 */
	@Override
	public double getReservoirWithdrawals() {
		if(reservoirWithdrawalsMap.containsKey(time)) {
			return reservoirWithdrawalsMap.get(time);
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.DefaultWaterSystem#getWaterDomesticPrice()
	 */
	@Override
	public double getWaterDomesticPrice() {
		if(waterDomesticPriceMap.containsKey(time)) {
			return waterDomesticPriceMap.get(time);
		} else {
			return 0; // return 0.05;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.DefaultWaterSystem#getWaterImportPrice()
	 */
	@Override
	public double getWaterImportPrice() {
		if(waterImportPriceMap.containsKey(time)) {
			return waterImportPriceMap.get(time);
		} else {
			return 0; // return 10;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.DefaultWaterSystem#getWaterReservoirVolume()
	 */
	@Override
	public double getWaterReservoirVolume() {
		if(waterReservoirVolumeMap.containsKey(time)) {
			return waterReservoirVolumeMap.get(time);
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
	
	/**
	 * Sets the reservoir withdrawals map.
	 *
	 * @param reservoirWithdrawalsMap the reservoir withdrawals map
	 */
	public void setReservoirWithdrawalsMap(Map<Long, Double> reservoirWithdrawalsMap) {
		this.reservoirWithdrawalsMap.clear();
		this.reservoirWithdrawalsMap.putAll(reservoirWithdrawalsMap);
	}
	
	/**
	 * Sets the water domestic price map.
	 *
	 * @param waterDomesticPriceMap the water domestic price map
	 */
	public void setWaterDomesticPriceMap(Map<Long, Double> waterDomesticPriceMap) {
		this.waterDomesticPriceMap.clear();
		this.waterDomesticPriceMap.putAll(waterDomesticPriceMap);
	}
	
	/**
	 * Sets the water import price map.
	 *
	 * @param waterImportPriceMap the water import price map
	 */
	public void setWaterImportPriceMap(Map<Long, Double> waterImportPriceMap) {
		this.waterImportPriceMap.clear();
		this.waterImportPriceMap.putAll(waterImportPriceMap);
	}
	
	/**
	 * Sets the water reservoir volume map.
	 *
	 * @param waterReservoirVolumeMap the water reservoir volume map
	 */
	public void setWaterReservoirVolumeMap(Map<Long, Double> waterReservoirVolumeMap) {
		this.waterReservoirVolumeMap.clear();
		this.waterReservoirVolumeMap.putAll(waterReservoirVolumeMap);
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
