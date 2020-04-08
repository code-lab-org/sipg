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
package edu.mit.sipg.core.lifecycle;

import edu.mit.sipg.core.SimEntity;
import edu.mit.sipg.units.CurrencyUnitsOutput;
import edu.mit.sipg.units.TimeUnitsOutput;

/**
 * The lifecycle model interface specifies lifecycle properties for an infrastructure element.
 * 
 * @author Paul T. Grogan
 */
public interface LifecycleModel extends SimEntity, TimeUnitsOutput, CurrencyUnitsOutput {
	
	/**
	 * Gets the current capital expense.
	 *
	 * @return the capital expense
	 */
	public double getCapitalExpense();
		
	/**
	 * Gets the current decommission expense.
	 *
	 * @return the decommission expense
	 */
	public double getDecommissionExpense();
	
	/**
	 * Gets the current fixed operations expense.
	 *
	 * @return the fixed operations expense
	 */
	public double getFixedOperationsExpense();
		
	/**
	 * Gets the associated mutable lifecycle model.
	 *
	 * @return the mutable lifecycle model
	 */
	public EditableLifecycleModel getMutableLifecycleModel();
		
	/**
	 * Checks if the corresponding element exists.
	 *
	 * @return true, if is exists
	 */
	public boolean isExists();
	
	/**
	 * Checks if the corresponding element is operational (i.e. can process resources).
	 *
	 * @return true, if is operational
	 */
	public boolean isOperational();
}
