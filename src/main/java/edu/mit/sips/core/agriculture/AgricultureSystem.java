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
package edu.mit.sips.core.agriculture;

import java.util.List;

import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.sim.util.FoodUnitsOutput;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * An interface to agriculture sector infrastructure systems.
 * 
 * @author Paul T. Grogan
 */
public interface AgricultureSystem extends InfrastructureSystem, 
		FoodUnitsOutput, WaterUnitsOutput {
	
	/**
	 * An interface to locally-controlled agriculture infrastructure 
	 * systems which provide greater details.
	 * 
	 * @author Paul T. Grogan
	 */
	public static interface Local extends AgricultureSystem, InfrastructureSystem.Local {
		
		/**
		 * Adds an agriculture element to this system.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean addElement(AgricultureElement element);
		
		/**
		 * Gets the arable land area of this agriculture system.
		 *
		 * @return the arable land area
		 */
		public double getArableLandArea();
		
		@Override
		public List<? extends AgricultureElement> getElements();

		@Override
		public List<? extends AgricultureElement> getExternalElements();
		
		/**
		 * Gets the quantity of food for export by this system.
		 *
		 * @return the food export
		 */
		public double getFoodExport();
		
		/**
		 * Gets the quantity of food for import by this system.
		 *
		 * @return the food import
		 */
		public double getFoodImport();
		
		/**
		 * Gets the quantity of food received via distribution in this system.
		 *
		 * @return the food in distribution
		 */
		public double getFoodInDistribution();
		
		/**
		 * Gets the quantity of food sent via distribution in this system.
		 *
		 * @return the food out distribution
		 */
		public double getFoodOutDistribution();
		
		/**
		 * Gets the quantity of food sent lost in distribution.
		 *
		 * @return the food out distribution losses
		 */
		public double getFoodOutDistributionLosses();
		
		@Override
		public List<? extends AgricultureElement> getInternalElements();
		
		/**
		 * Gets the labor participation rate of this society.
		 *
		 * @return the labor participation rate
		 */
		public double getLaborParticipationRate();
		
		/**
		 * Gets the quantity of labor used by this agriculture system.
		 *
		 * @return the labor used
		 */
		public long getLaborUsed();
		
		/**
		 * Gets the land area used by this agriculture system.
		 *
		 * @return the land area used
		 */
		public double getLandAreaUsed();
		
		/**
		 * Gets the fraction of food from local sources.
		 *
		 * @return the local food fraction
		 */
		public double getLocalFoodFraction();
		
		/**
		 * Gets the quantity of food supplied from local sources.
		 *
		 * @return the local food supply
		 */
		public double getLocalFoodSupply();
		
		/**
		 * Gets the unit food production cost.
		 *
		 * @return the unit production cost
		 */
		public double getUnitProductionCost();
		
		/**
		 * Gets the unit food supply profit.
		 *
		 * @return the unit supply profit
		 */
		public double getUnitSupplyProfit();
		
		/**
		 * Removes an agriculture element from this system.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean removeElement(AgricultureElement element);
	}
	
	/**
	 * Gets the food domestic price.
	 *
	 * @return the food domestic price
	 */
	public double getFoodDomesticPrice();
	
	/**
	 * Gets the food export price.
	 *
	 * @return the food export price
	 */
	public double getFoodExportPrice();
	
	/**
	 * Gets the food import price.
	 *
	 * @return the food import price
	 */
	public double getFoodImportPrice();
	
	/**
	 * Gets the quantity of food produced.
	 *
	 * @return the food production
	 */
	public double getFoodProduction();
	
	/**
	 * Gets the food security performance metric.
	 *
	 * @return the food security
	 */
	public double getFoodSecurity();
	
	/**
	 * Gets the total quantity of food supplied.
	 *
	 * @return the total food supply
	 */
	public double getTotalFoodSupply();
	
	/**
	 * Gets the quantity of water consumed by this system.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
}
