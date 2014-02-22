package edu.mit.sips.core.petroleum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class PlaceholderPetroleumSystem.
 */
public class PlaceholderPetroleumSystem extends DefaultPetroleumSystem implements Serializable {
	private static final long serialVersionUID = 7733181697266453596L;
	private transient long time, nextTime;
	private final Map<Long, Double> electricityConsumptionMap = new HashMap<Long, Double>();
	private final Map<Long, Double> captialExpenseMap = new HashMap<Long, Double>();
	private final Map<Long, Double> cashFlowMap = new HashMap<Long, Double>();
	private final Map<Long, Double> petroleumReservoirVolumeMap = new HashMap<Long, Double>();
	private final Map<Long, Double> petroleumWithdrawalsMap = new HashMap<Long, Double>();
	private final Map<Long, Double> petroleumDomesticPriceMap = new HashMap<Long, Double>();
	private final Map<Long, Double> petroleumImportPriceMap = new HashMap<Long, Double>();
	private final Map<Long, Double> petroleumExportPriceMap = new HashMap<Long, Double>();

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
	 * @see edu.mit.sips.core.petroleum.DefaultPetroleumSystem#getElectricityConsumption()
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
	 * @see edu.mit.sips.core.petroleum.DefaultPetroleumSystem#getPetroleumDomesticPrice()
	 */
	@Override
	public double getPetroleumDomesticPrice() {
		if(petroleumDomesticPriceMap.containsKey(time)) {
			return petroleumDomesticPriceMap.get(time);
		} else {
			return 0; // return 8;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.DefaultPetroleumSystem#getPetroleumExportPrice()
	 */
	@Override
	public double getPetroleumExportPrice() {
		if(petroleumExportPriceMap.containsKey(time)) {
			return petroleumExportPriceMap.get(time);
		} else {
			return 0; // return 30;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.DefaultPetroleumSystem#getPetroleumImportPrice()
	 */
	@Override
	public double getPetroleumImportPrice() {
		if(petroleumImportPriceMap.containsKey(time)) {
			return petroleumImportPriceMap.get(time);
		} else {
			return 0; // return 35;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.DefaultPetroleumSystem#getPetroleumReservoirVolume()
	 */
	@Override
	public double getPetroleumReservoirVolume() {
		if(petroleumReservoirVolumeMap.containsKey(time)) {
			return petroleumReservoirVolumeMap.get(time);
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.DefaultPetroleumSystem#getPetroleumWithdrawals()
	 */
	@Override
	public double getPetroleumWithdrawals() {
		if(petroleumWithdrawalsMap.containsKey(time)) {
			return petroleumWithdrawalsMap.get(time);
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
	 * Sets the petroleum domestic price map.
	 *
	 * @param petroleumDomesticPriceMap the petroleum domestic price map
	 */
	public void setPetroleumDomesticPriceMap(Map<Long, Double> petroleumDomesticPriceMap) {
		this.petroleumDomesticPriceMap.clear();
		this.petroleumDomesticPriceMap.putAll(petroleumDomesticPriceMap);
	}
	
	/**
	 * Sets the petroleum export price map.
	 *
	 * @param petroleumExportPriceMap the petroleum export price map
	 */
	public void setPetroleumExportPriceMap(Map<Long, Double> petroleumExportPriceMap) {
		this.petroleumExportPriceMap.clear();
		this.petroleumExportPriceMap.putAll(petroleumExportPriceMap);
	}
	
	/**
	 * Sets the petroleum import price map.
	 *
	 * @param petroleumImportPriceMap the petroleum import price map
	 */
	public void setPetroleumImportPriceMap(Map<Long, Double> petroleumImportPriceMap) {
		this.petroleumImportPriceMap.clear();
		this.petroleumImportPriceMap.putAll(petroleumImportPriceMap);
	}
	
	/**
	 * Sets the petroleum reservoir volume map.
	 *
	 * @param petroleumReservoirVolumeMap the petroleum reservoir volume map
	 */
	public void setPetroleumReservoirVolumeMap(Map<Long, Double> petroleumReservoirVolumeMap) {
		this.petroleumReservoirVolumeMap.clear();
		this.petroleumReservoirVolumeMap.putAll(petroleumReservoirVolumeMap);
	}
	
	/**
	 * Sets the petroleum withdrawals map.
	 *
	 * @param petroleumWithdrawalsMap the petroleum withdrawals map
	 */
	public void setPetroleumWithdrawalsMap(Map<Long, Double> petroleumWithdrawalsMap) {
		this.petroleumWithdrawalsMap.clear();
		this.petroleumWithdrawalsMap.putAll(petroleumWithdrawalsMap);
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
