/*
 * 
 */
package edu.mit.sips.core.lifecycle;

import edu.mit.sips.core.SimEntity;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.TimeUnitsOutput;

/**
 * The Interface LifecycleModel.
 * 
 * @author Paul T. Grogan
 */
public interface LifecycleModel extends SimEntity, TimeUnitsOutput, CurrencyUnitsOutput {
	
	/**
	 * Checks if is operational.
	 *
	 * @return true, if is operational
	 */
	public boolean isOperational();
	
	/**
	 * Checks if is exists.
	 *
	 * @return true, if is exists
	 */
	public boolean isExists();
	
	/**
	 * Gets the capital expense.
	 *
	 * @return the capital expense
	 */
	public double getCapitalExpense();
	
	/**
	 * Gets the fixed operations expense.
	 *
	 * @return the fixed operations expense
	 */
	public double getFixedOperationsExpense();
	
	/**
	 * Gets the decommission expense.
	 *
	 * @return the decommission expense
	 */
	public double getDecommissionExpense();
	
	/**
	 * Gets the mutable lifecycle model.
	 *
	 * @return the mutable lifecycle model
	 */
	public MutableLifecycleModel getMutableLifecycleModel();
}
