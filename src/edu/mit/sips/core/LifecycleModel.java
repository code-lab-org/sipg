/*
 * 
 */
package edu.mit.sips.core;

/**
 * The Interface LifecycleModel.
 * 
 * @author Paul T. Grogan
 */
public interface LifecycleModel extends SimEntity {
	
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
