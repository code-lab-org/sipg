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

import edu.mit.sipg.core.SimEntity;
import edu.mit.sipg.core.lifecycle.LifecycleModel;
import edu.mit.sipg.units.CurrencyUnitsOutput;

/**
 * An infrastructure element provides a generic interface for any infrastructure
 * participating in a discrete time simulation.
 * 
 * @author Paul T. Grogan
 */
public interface InfrastructureElement extends SimEntity, CurrencyUnitsOutput {
	
	/**
	 * Adds an element change listener that will be notified whenever 
	 * an element change event is fired.
	 *
	 * @param listener the listener
	 */
	public void addElementChangeListener(ElementChangeListener listener);
	
	/**
	 * Fires an element change event to notify any element change 
	 * listeners that a change occurred.
	 */
	public void fireElementChangeEvent();

	/**
	 * Gets the current capital expense for this infrastructure element.
	 *
	 * @return the capital expense
	 */
	public double getCapitalExpense();

	/**
	 * Gets the current decommission expense for this infrastructure element.
	 *
	 * @return the decommission expense
	 */
	public double getDecommissionExpense();
	
	/**
	 * Gets the destination of this infrastructure element.
	 *
	 * @return the destination
	 */
	public String getDestination();
	
	/**
	 * Gets the current fixed operations expense for this infrastructure element.
	 *
	 * @return the fixed operations expense
	 */
	public double getFixedOperationsExpense();
	
	/**
	 * Gets the lifecycle model of this infrastructure element.
	 *
	 * @return the lifecycle model
	 */
	public LifecycleModel getLifecycleModel();
	
	/**
	 * Gets the mutable element of this infrastructure element.
	 *
	 * @return the mutable element
	 */
	public EditableInfrastructureElement getMutableElement();
	
	/**
	 * Gets the name of this infrastructure element.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the origin of this infrastructure element.
	 *
	 * @return the origin
	 */
	public String getOrigin();
	
	/**
	 * Gets the template name of this infrastructure element.
	 *
	 * @return the template name
	 */
	public String getTemplateName();
	
	/**
	 * Gets the current total expense for this infrastructure element. 
	 * Total expense includes capital, fixed operations, variable operations, 
	 * and decommission components.
	 *
	 * @return the total expense
	 */
	public double getTotalExpense();
	
	/**
	 * Gets the current total operations expense for this infrastructure element. 
	 * Total operations expense includes fixed and variable components.
	 *
	 * @return the total operations expense
	 */
	public double getTotalOperationsExpense();
	
	/**
	 * Checks if this infrastructure element exists (i.e. has started 
	 * commissioning lifecycle phase).
	 *
	 * @return true, if is exists
	 */
	public boolean isExists();
	
	/**
	 * Checks if this infrastructure element is operational (i.e. is in 
	 * operational lifecycle phase).
	 *
	 * @return true, if is operational
	 */
	public boolean isOperational();
	
	/**
	 * Removes an element change listener from this infrastructure element.
	 *
	 * @param listener the listener
	 */
	public void removeElementChangeListener(ElementChangeListener listener);
}
