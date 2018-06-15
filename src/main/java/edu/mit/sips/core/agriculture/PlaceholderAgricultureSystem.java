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
	private final Map<Long, Double> totalFoodSupplyMap = new HashMap<Long, Double>();
	private final Map<Long, Double> foodProductionMap = new HashMap<Long, Double>();
	private final Map<Long, Double> foodDomesticPriceMap = new HashMap<Long, Double>();
	private final Map<Long, Double> foodImportPriceMap = new HashMap<Long, Double>();
	private final Map<Long, Double> foodExportPriceMap = new HashMap<Long, Double>();
	
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
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem#getFoodDomesticPrice()
	 */
	@Override
	public double getFoodDomesticPrice() {
		if(foodDomesticPriceMap.containsKey(time)) {
			return foodDomesticPriceMap.get(time);
		} else {
			return 0; // return 60;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem#getFoodExportPrice()
	 */
	@Override
	public double getFoodExportPrice() {
		if(foodExportPriceMap.containsKey(time)) {
			return foodExportPriceMap.get(time);
		} else {
			return 0; // eturn 50;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem#getFoodImportPrice()
	 */
	@Override
	public double getFoodImportPrice() {
		if(foodImportPriceMap.containsKey(time)) {
			return foodImportPriceMap.get(time);
		} else {
			return 0; // return 70;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem#getFoodProduction()
	 */
	@Override
	public double getFoodProduction() {
		if(foodProductionMap.containsKey(time)) {
			return foodProductionMap.get(time);
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.DefaultAgricultureSystem#getTotalFoodSupply()
	 */
	@Override
	public double getTotalFoodSupply() {
		if(totalFoodSupplyMap.containsKey(time)) {
			return totalFoodSupplyMap.get(time);
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
	 * Sets the food domestic price map.
	 *
	 * @param foodDomesticPriceMap the food domestic price map
	 */
	public void setFoodDomesticPriceMap(Map<Long, Double> foodDomesticPriceMap) {
		this.foodDomesticPriceMap.clear();
		this.foodDomesticPriceMap.putAll(foodDomesticPriceMap);
	}
	
	/**
	 * Sets the food export price map.
	 *
	 * @param foodExportPriceMap the food export price map
	 */
	public void setFoodExportPriceMap(Map<Long, Double> foodExportPriceMap) {
		this.foodExportPriceMap.clear();
		this.foodExportPriceMap.putAll(foodExportPriceMap);
	}
	
	/**
	 * Sets the food import price map.
	 *
	 * @param foodImportPriceMap the food import price map
	 */
	public void setFoodImportPriceMap(Map<Long, Double> foodImportPriceMap) {
		this.foodImportPriceMap.clear();
		this.foodImportPriceMap.putAll(foodImportPriceMap);
	}
	
	/**
	 * Sets the food production map.
	 *
	 * @param foodProductionMap the food production map
	 */
	public void setFoodProductionMap(Map<Long, Double> foodProductionMap) {
		this.foodProductionMap.clear();
		this.foodProductionMap.putAll(foodProductionMap);
	}
	
	/**
	 * Sets the total food supply map.
	 *
	 * @param totalFoodSupplyMap the total food supply map
	 */
	public void setTotalFoodSupplyMap(Map<Long, Double> totalFoodSupplyMap) {
		this.totalFoodSupplyMap.clear();
		this.totalFoodSupplyMap.putAll(totalFoodSupplyMap);
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
