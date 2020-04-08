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
package edu.mit.sipg.core.base;

import java.util.List;

import edu.mit.sipg.core.SimEntity;
import edu.mit.sipg.core.Society;
import edu.mit.sipg.units.CurrencyUnitsOutput;

/**
 * An infrastructure system composes infrastructure elements within a set sector.
 * 
 * @author Paul T. Grogan
 */
public interface InfrastructureSystem extends SimEntity, CurrencyUnitsOutput {
	/**
	 * A locally-controlled infrastructure system permits greater 
	 * visibility of details.
	 * 
	 * @author Paul T. Grogan
	 */
	public static interface Local extends InfrastructureSystem {
		/**
		 * Gets the current expense for consuming operational resources.
		 *
		 * @return the consumption expense
		 */
		public double getConsumptionExpense();
		
		/**
		 * Gets the current cumulative capital expense of this infrastructure system.
		 *
		 * @return the cumulative capital expense
		 */
		public double getCumulativeCapitalExpense();
		
		/**
		 * Gets the current cumulative cash flow of this infrastructure system.
		 *
		 * @return the cumulative cash flow
		 */
		public double getCumulativeCashFlow();
		
		/**
		 * Gets the current decommission expense of this infrastructure system.
		 *
		 * @return the decommission expense
		 */
		public double getDecommissionExpense();
		
		/**
		 * Gets the current expense for receiving regionally-distributed resources.
		 *
		 * @return the distribution expense
		 */
		public double getDistributionExpense();
		
		/**
		 * Gets the current revenue for sending regionally-distributed resources.
		 *
		 * @return the distribution revenue
		 */
		public double getDistributionRevenue();
		
		/**
		 * Gets the list of infrastructure elements in this system.
		 *
		 * @return the elements
		 */
		public List<? extends InfrastructureElement> getElements();
		
		/**
		 * Gets the current revenue from exporting resources.
		 *
		 * @return the export revenue
		 */
		public double getExportRevenue();
		
		/**
		 * Gets the list of infrastructure elements that affect this system but are outside its control.
		 *
		 * @return the external elements
		 */
		public List<? extends InfrastructureElement> getExternalElements();
		
		/**
		 * Gets the current expense for importing resources.
		 *
		 * @return the import expense
		 */
		public double getImportExpense();
		
		/**
		 * Gets the list of infrastructure elements controlled by this system.
		 *
		 * @return the internal elements
		 */
		public List<? extends InfrastructureElement> getInternalElements();
		
		/**
		 * Gets the current lifecycle expense of this infrastructure system.
		 *
		 * @return the lifecycle expense
		 */
		public double getLifecycleExpense();
		
		/**
		 * Gets the current operations expense of this infrastructure system.
		 *
		 * @return the operations expense
		 */
		public double getOperationsExpense();
		
		/**
		 * Gets the current sales revenue of this infrastructure system.
		 *
		 * @return the sales revenue
		 */
		public double getSalesRevenue();
		
		/**
		 * Gets the current total expense of this infrastructure system.
		 *
		 * @return the total expense
		 */
		public double getTotalExpense();
		
		/**
		 * Gets the current total revenue of this infrastructure system.
		 *
		 * @return the total revenue
		 */
		public double getTotalRevenue();
	}
	
	/**
	 * Gets the current capital expense of this infrastructure system.
	 *
	 * @return the capital expense
	 */
	public double getCapitalExpense();
	
	/**
	 * Gets the current cash flow of this infrastructure system.
	 *
	 * @return the cash flow
	 */
	public double getCashFlow();
	
	/**
	 * Gets the name of this infrastructure system.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the society containing this infrastructure system.
	 *
	 * @return the society
	 */
	public Society getSociety();
	
	/**
	 * Checks if this infrastructure system is locally controlled.
	 *
	 * @return true, if is local
	 */
	public boolean isLocal();
	
	/**
	 * Sets the society of this infrastructure system.
	 *
	 * @param society the new society
	 */
	public void setSociety(Society society);
}
