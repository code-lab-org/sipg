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
package edu.mit.sipg.core.petroleum;

import java.util.List;

import edu.mit.sipg.core.base.InfrastructureSystem;
import edu.mit.sipg.units.ElectricityUnitsOutput;
import edu.mit.sipg.units.OilUnitsOutput;

/**
 * An interface to petroleum sector infrastructure systems.
 * 
 * @author Paul T. Grogan
 */
public interface PetroleumSystem extends InfrastructureSystem, 
		ElectricityUnitsOutput, OilUnitsOutput {
	
	/**
	 * An interface to locally-controlled petroleum infrastructure 
	 * systems which provide greater details.
	 * 
	 * @author Paul T. Grogan
	 */
	public interface Local extends PetroleumSystem, InfrastructureSystem.Local {

		/**
		 * Adds a petroleum element to this system.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean addElement(PetroleumElement element);

		@Override
		public List<? extends PetroleumElement> getElements();

		@Override
		public List<? extends PetroleumElement> getExternalElements();

		@Override
		public List<? extends PetroleumElement> getInternalElements();

		/**
		 * Gets the fraction of petroleum consumed from local sources.
		 *
		 * @return the local petroleum fraction
		 */
		public double getLocalPetroleumFraction();

		/**
		 * Gets the maximum petroleum reservoir volume.
		 *
		 * @return the max petroleum reservoir volume
		 */
		public double getMaxPetroleumReservoirVolume();

		/**
		 * Gets the quantity of petroleum exported from this system.
		 *
		 * @return the petroleum export
		 */
		public double getPetroleumExport();

		/**
		 * Gets the quantity of petroleum imported by this system.
		 *
		 * @return the petroleum import
		 */
		public double getPetroleumImport();

		/**
		 * Gets the quantity of petroleum sent for distribution.
		 *
		 * @return the petroleum in distribution
		 */
		public double getPetroleumInDistribution();

		/**
		 * Gets the quantity of petroleum received via distribution.
		 *
		 * @return the petroleum out distribution
		 */
		public double getPetroleumOutDistribution();

		/**
		 * Gets the quantity of petroleum lost due to distribution inefficiency.
		 *
		 * @return the petroleum out distribution losses
		 */
		public double getPetroleumOutDistributionLosses();
		
		/**
		 * Gets the quantity of petroleum produced.
		 *
		 * @return the petroleum production
		 */
		public double getPetroleumProduction();

		/**
		 * Gets the total quantity of petroleum supplied.
		 *
		 * @return the total petroleum supply
		 */
		public double getTotalPetroleumSupply();

		/**
		 * Gets the unit petroleum production cost.
		 *
		 * @return the unit production cost
		 */
		public double getUnitProductionCost();
		
		/**
		 * Gets the unit petroleum supply profit.
		 *
		 * @return the unit supply profit
		 */
		public double getUnitSupplyProfit();
		
		/**
		 * Removes a petroleum element from this system.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean removeElement(PetroleumElement element);
	}
	
	/**
	 * Gets the reservoir lifetime.
	 *
	 * @return the reservoir lifetime
	 */
	public double getReservoirLifetime();
	
	/**
	 * Gets the quantity of electricity consumed by this system.
	 *
	 * @return the electricity consumed
	 */
	public double getElectricityConsumption();
	
	/**
	 * Gets the petroleum domestic price.
	 *
	 * @return the petroleum domestic price
	 */
	public double getPetroleumDomesticPrice();
	
	/**
	 * Gets the petroleum export price.
	 *
	 * @return the petroleum export price
	 */
	public double getPetroleumExportPrice();
	
	/**
	 * Gets the petroleum import price.
	 *
	 * @return the petroleum import price
	 */
	public double getPetroleumImportPrice();

	/**
	 * Gets the current reservoir volume.
	 *
	 * @return the reservoir volume
	 */
	public double getReservoirVolume();

	/**
	 * Gets the quantity of reservoir withdrawals by this system.
	 *
	 * @return the reservoir withdrawals
	 */
	public double getReservoirWithdrawals();
}
