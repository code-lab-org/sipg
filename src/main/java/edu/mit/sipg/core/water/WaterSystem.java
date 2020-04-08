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
package edu.mit.sipg.core.water;

import java.util.List;

import edu.mit.sipg.core.base.InfrastructureSystem;
import edu.mit.sipg.units.ElectricityUnitsOutput;
import edu.mit.sipg.units.WaterUnitsOutput;

/**
 * An interface to water sector infrastructure systems.
 * 
 * @author Paul T. Grogan
 */
public interface WaterSystem extends InfrastructureSystem, 
		WaterUnitsOutput, ElectricityUnitsOutput {
	
	/**
	 * An interface to locally-controlled water infrastructure 
	 * systems which provide greater details.
	 * 
	 * @author Paul T. Grogan
	 */
	public static interface Local extends WaterSystem, InfrastructureSystem.Local {
		
		/**
		 * Adds a water element to this system.
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
		
		@Override
		public List<? extends WaterElement> getElements();

		@Override
		public List<? extends WaterElement> getExternalElements();

		@Override
		public List<? extends WaterElement> getInternalElements();
		
		/**
		 * Gets the fraction of water supplied from local sources.
		 *
		 * @return the local water fraction
		 */
		public double getLocalWaterFraction();
		
		/**
		 * Gets the maximum aquifer reservoir volume.
		 *
		 * @return the max aquifer reservoir volume
		 */
		public double getMaxAquiferVolume();
		
		/**
		 * Gets the fraction of water consumed from renewable sources.
		 *
		 * @return the renewable water fraction
		 */
		public double getRenewableWaterFraction();
		
		/**
		 * Gets the quantity of water produced from renewable sources.
		 *
		 * @return the renewable water production
		 */
		public double getRenewableWaterProduction();
		
		/**
		 * Gets the aquifer withdrawals from private production.
		 *
		 * @return the aquifer withdrawals from private production
		 */
		public double getAquiferWithdrawalsFromPrivateProduction();
		
		/**
		 * Gets the aquifer withdrawals from public production.
		 *
		 * @return the aquifer withdrawals from public production
		 */
		public double getAquiferWithdrawalsFromPublicProduction();
		
		/**
		 * Gets the total quantity of water supplied by this system.
		 *
		 * @return the total water supply
		 */
		public double getTotalWaterSupply();
		
		/**
		 * Gets the unit water production cost.
		 *
		 * @return the unit production cost
		 */
		public double getUnitProductionCost();
		
		/**
		 * Gets the unit water supply profit.
		 *
		 * @return the unit supply profit
		 */
		public double getUnitSupplyProfit();
		
		/**
		 * Gets the quantity of water from private production.
		 *
		 * @return the water from private production
		 */
		public double getWaterFromPrivateProduction();
		
		/**
		 * Gets the quantity of water imported by this system.
		 *
		 * @return the water import
		 */
		public double getWaterImport();
		
		/**
		 * Gets the water sent to distribution by this system.
		 *
		 * @return the water in distribution
		 */
		public double getWaterInDistribution();
		
		/**
		 * Gets the water received from distribution by this system.
		 *
		 * @return the water out distribution
		 */
		public double getWaterOutDistribution();
		
		/**
		 * Gets the quantity of water lost due to distribution inefficiency.
		 *
		 * @return the water out distribution losses
		 */
		public double getWaterOutDistributionLosses();
		
		/**
		 * Gets the quantity of water produced by this system.
		 *
		 * @return the water production
		 */
		public double getWaterProduction();
		
		/**
		 * Gets the aquifer recharge rate.
		 *
		 * @return the aquifer reservoir recharge rate
		 */
		public double getAquiferRechargeRate();
		
		/**
		 * Gets the quantity of water wasted.
		 *
		 * @return the water wasted
		 */
		public double getWaterWasted();
		
		/**
		 * Checks if this system provides coastal access.
		 *
		 * @return true, if coastal access
		 */
		public boolean isCoastalAccess();
		
		/**
		 * Removes a water element from this system.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean removeElement(WaterElement element);
	}
	
	/**
	 * Gets the aquifer lifetime.
	 *
	 * @return the aquifer lifetime
	 */
	public double getAquiferLifetime();
	
	/**
	 * Gets the quantity of energy consumed by this system.
	 *
	 * @return the energy consumption
	 */
	public double getElectricityConsumption();
	
	/**
	 * Gets the quantity of aquifer withdrawals.
	 *
	 * @return the aquifer withdrawals
	 */
	public double getAquiferWithdrawals();
	
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
	 * Gets the water reservoir volume.
	 *
	 * @return the water reservoir volume
	 */
	public double getWaterReservoirVolume();
}
