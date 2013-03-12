package edu.mit.sips.core.water;

import java.util.List;

import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Interface EnergySystem.
 */
public interface WaterSystem extends InfrastructureSystem {
	public String ELECTRICITY_CONSUMPTION_ATTRIBUTE = "electricityConsumption",
			WATER_SUPPLY_PER_CAPITA_ATTRIBUTE = "waterSupplyPerCapita";
	
	/**
	 * The Interface Local.
	 */
	public static interface Local extends WaterSystem, InfrastructureSystem.Local {

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getElements()
		 */
		public List<? extends WaterElement> getElements();
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getExternalElements()
		 */
		public List<? extends WaterElement> getExternalElements();
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
		 */
		public List<? extends WaterElement> getInternalElements();
		
		/**
		 * Gets the max water reservoir volume.
		 *
		 * @return the max water reservoir volume
		 */
		public double getMaxWaterReservoirVolume();
		
		/**
		 * Gets the national water system.
		 *
		 * @return the national water system
		 */
		public WaterSystem.Local getNationalWaterSystem();
		
		/**
		 * Gets the renewable water production.
		 *
		 * @return the renewable water production
		 */
		public double getRenewableWaterProduction();
		
		/**
		 * Gets the reservoir water withdrawals.
		 *
		 * @return the reservoir water withdrawals
		 */
		public double getReservoirWaterWithdrawals();
		
		/**
		 * Gets the total water supply.
		 *
		 * @return the total water supply
		 */
		public double getTotalWaterSupply();
		
		/**
		 * Gets the water from artesian well.
		 *
		 * @return the water from artesian well
		 */
		public double getWaterFromArtesianWell();
		
		/**
		 * Gets the water import.
		 *
		 * @return the water import
		 */
		public double getWaterImport();
		
		/**
		 * Gets the water in distribution.
		 *
		 * @return the water in distribution
		 */
		public double getWaterInDistribution();
		
		/**
		 * Gets the water out distribution.
		 *
		 * @return the water out distribution
		 */
		public double getWaterOutDistribution();
		
		/**
		 * Gets the water production.
		 *
		 * @return the water production
		 */
		public double getWaterProduction();
		
		/**
		 * Gets the water reservoir recharge rate.
		 *
		 * @return the water reservoir recharge rate
		 */
		public double getWaterReservoirRechargeRate();
		
		/**
		 * Gets the water reservoir volume.
		 *
		 * @return the water reservoir volume
		 */
		public double getWaterReservoirVolume();
		
		/**
		 * Gets the water wasted.
		 *
		 * @return the water wasted
		 */
		public double getWaterWasted();
		
		/**
		 * Optimize water distribution.
		 */
		public void optimizeWaterDistribution();
		
		/**
		 * Optimize water production and distribution.
		 *
		 * @param deltaProductionCost the delta production cost
		 */
		public void optimizeWaterProductionAndDistribution(double deltaProductionCost);
		
		/**
		 * Sets the water supply per capita.
		 *
		 * @return the water supply per capita
		 */
		public void setWaterSupplyPerCapita(double waterSupplyPerCapita);
		
		/**
		 * Gets the local water fraction.
		 *
		 * @return the local water fraction
		 */
		public double getLocalWaterFraction();
		
		/**
		 * Gets the renewable water fraction.
		 *
		 * @return the renewable water fraction
		 */
		public double getRenewableWaterFraction();
		
		/**
		 * Gets the unit production cost.
		 *
		 * @return the unit production cost
		 */
		public double getUnitProductionCost();
		
		/**
		 * Gets the unit supply cost.
		 *
		 * @return the unit supply cost
		 */
		public double getUnitSupplyProfit();
	}
	
	/**
	 * The Interface Remote.
	 */
	public static interface Remote extends WaterSystem, InfrastructureSystem.Remote {
		
		/**
		 * Sets the electricity consumption.
		 *
		 * @param electricityConsumption the new electricity consumption
		 */
		public void setElectricityConsumption(double electricityConsumption);
		
		/**
		 * Sets the water supply per capita.
		 *
		 * @return the water supply per capita
		 */
		public void setWaterSupplyPerCapita(double waterSupplyPerCapita);

	}
	
	/**
	 * Gets the energy consumption.
	 *
	 * @return the energy consumption
	 */
	public double getElectricityConsumption();
	
	/**
	 * Gets the water supply per capita.
	 *
	 * @return the water supply per capita
	 */
	public double getWaterSupplyPerCapita();
}
