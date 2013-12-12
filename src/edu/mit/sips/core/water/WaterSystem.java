package edu.mit.sips.core.water;

import java.util.List;

import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Interface EnergySystem.
 */
public interface WaterSystem extends InfrastructureSystem, WaterUnitsOutput, ElectricityUnitsOutput {
	/**
	 * The Interface Local.
	 */
	public static interface Local extends WaterSystem, InfrastructureSystem.Local {
		
		/**
		 * Adds the element.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean addElement(WaterElement element);
		
		/**
		 * Gets the electricity consumption from private production.
		 *
		 * @return the electricity consumption from private production
		 */
		public double getElectricityConsumptionFromPrivateProduction();
		
		/**
		 * Gets the electricity consumption from public production.
		 *
		 * @return the electricity consumption from public production
		 */
		public double getElectricityConsumptionFromPublicProduction();
		
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
		 * Gets the aquifer lifetime.
		 *
		 * @return the aquifer lifetime
		 */
		public double getAquiferLifetime();
		
		/**
		 * Gets the local water fraction.
		 *
		 * @return the local water fraction
		 */
		public double getLocalWaterFraction();
		
		/**
		 * Gets the max water reservoir volume.
		 *
		 * @return the max water reservoir volume
		 */
		public double getMaxWaterReservoirVolume();
		
		/**
		 * Gets the renewable water fraction.
		 *
		 * @return the renewable water fraction
		 */
		public double getRenewableWaterFraction();
		
		/**
		 * Gets the renewable water production.
		 *
		 * @return the renewable water production
		 */
		public double getRenewableWaterProduction();
		
		/**
		 * Gets the reservoir withdrawals.
		 *
		 * @return the reservoir withdrawals
		 */
		public double getReservoirWithdrawals();
		
		/**
		 * Gets the reservoir withdrawals from private production.
		 *
		 * @return the reservoir withdrawals from private production
		 */
		public double getReservoirWithdrawalsFromPrivateProduction();
		
		/**
		 * Gets the reservoir withdrawals from public production.
		 *
		 * @return the reservoir withdrawals from public production
		 */
		public double getReservoirWithdrawalsFromPublicProduction();
		
		/**
		 * Gets the total water supply.
		 *
		 * @return the total water supply
		 */
		public double getTotalWaterSupply();
		
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
		
		/**
		 * Gets the water from private production.
		 *
		 * @return the water from private production
		 */
		public double getWaterFromPrivateProduction();
		
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
		 * Gets the water out distribution losses.
		 *
		 * @return the water out distribution losses
		 */
		public double getWaterOutDistributionLosses();
		
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
		 * Checks if is coastal access.
		 *
		 * @return true, if is coastal access
		 */
		public boolean isCoastalAccess();
		
		/**
		 * Removes the element.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean removeElement(WaterElement element);
	}
	
	/**
	 * Gets the energy consumption.
	 *
	 * @return the energy consumption
	 */
	public double getElectricityConsumption();
	
	/**
	 * Gets the water domestic price.
	 *
	 * @return the water domestic price
	 */
	public double getWaterDomesticPrice();
	
	/**
	 * Gets the water import price.
	 *
	 * @return the water import price
	 */
	public double getWaterImportPrice();
	
	/**
	 * Gets the water agricultural price.
	 *
	 * @return the water agricultural price
	 */
	public double getWaterAgriculturalPrice();
}
