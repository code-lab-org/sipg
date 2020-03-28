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
package edu.mit.sips.core.electricity;

import java.util.List;

import edu.mit.sips.core.base.InfrastructureSystem;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * An interface to electricity sector infrastructure systems.
 * 
 * @author Paul T. Grogan
 */
public interface ElectricitySystem extends InfrastructureSystem, 
		WaterUnitsOutput, ElectricityUnitsOutput, OilUnitsOutput {
	
	/**
	 * An interface to locally-controlled electricity infrastructure 
	 * systems which provide greater details.
	 * 
	 * @author Paul T. Grogan
	 */
	public static interface Local extends ElectricitySystem, InfrastructureSystem.Local {

		/**
		 * Adds an electricity element to this system.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean addElement(ElectricityElement element);
		
		/**
		 * Gets the quantity of electricity from private production.
		 *
		 * @return the electricity from private production
		 */
		public double getElectricityFromPrivateProduction();
		
		/**
		 * Gets the quantity of electricity sent via distribution.
		 *
		 * @return the electricity in distribution
		 */
		public double getElectricityInDistribution();
		
		/**
		 * Gets the quantity of electricity received from distribution.
		 *
		 * @return the electricity out distribution
		 */
		public double getElectricityOutDistribution();
		
		/**
		 * Gets the quantity of electricity lost from distribution losses.
		 *
		 * @return the electricity distribution losses
		 */
		public double getElectricityOutDistributionLosses();
		
		/**
		 * Gets the quantity of electricity produced.
		 *
		 * @return the electricity production
		 */
		public double getElectricityProduction();
		
		/**
		 * Gets the quantity of electricity wasted.
		 *
		 * @return the electricity wasted
		 */
		public double getElectricityWasted();
		
		@Override
		public List<? extends ElectricityElement> getElements();

		@Override
		public List<? extends ElectricityElement> getExternalElements();

		@Override
		public List<? extends ElectricityElement> getInternalElements();
		
		/**
		 * Gets the fraction of electricity consumed from local sources.
		 *
		 * @return the local electricity fraction
		 */
		public double getLocalElectricityFraction();

		/**
		 * Gets the quantity of petroleum consumed by private production.
		 *
		 * @return the petroleum consumption from private production
		 */
		public double getPetroleumConsumptionFromPrivateProduction();
		
		/**
		 * Gets the quantity of petroleum consumed by public production.
		 *
		 * @return the petroleum consumption from public production
		 */
		public double getPetroleumConsumptionFromPublicProduction();

		/**
		 * Gets the petroleum intensity of private production.
		 *
		 * @return the petroleum intensity of private production
		 */
		public double getPetroleumIntensityOfPrivateProduction();
		
		/**
		 * Gets the fraction of electricity consumed from renewable sources.
		 *
		 * @return the renewable electricity fraction
		 */
		public double getRenewableElectricityFraction();
		
		/**
		 * Gets the fraction of electricity generated from renewable sources.
		 *
		 * @return the renewable energy production
		 */
		public double getRenewableElectricityProduction();
		
		/**
		 * Gets the total quantity of electricity supply.
		 *
		 * @return the total electricity supply
		 */
		public double getTotalElectricitySupply();
		
		/**
		 * Gets the unit electricity production cost.
		 *
		 * @return the unit production cost
		 */
		public double getUnitProductionCost();
		
		/**
		 * Gets the unit electricity supply profit.
		 *
		 * @return the unit supply profit
		 */
		public double getUnitSupplyProfit();
		
		/**
		 * Removes an electricity element from this system.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean removeElement(ElectricityElement element);
	}
	
	/**
	 * Gets the electricity domestic price.
	 *
	 * @return the electricity domestic price
	 */
	public double getElectricityDomesticPrice();
	
	/**
	 * Gets the quantity of petroleum consumed by this system.
	 *
	 * @return the petroleum consumption
	 */
	public double getPetroleumConsumption();
	
	/**
	 * Gets the quantity of water consumed by this system.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
}
