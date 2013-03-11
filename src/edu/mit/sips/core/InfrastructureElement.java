package edu.mit.sips.core;

/**
 * The Interface InfrastructureElement.
 */
public interface InfrastructureElement extends SimEntity {

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the origin.
	 *
	 * @return the origin
	 */
	public String getOrigin();
	
	/**
	 * Gets the destination.
	 *
	 * @return the destination
	 */
	public String getDestination();
	
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
	 * Gets the total operations expense.
	 *
	 * @return the total operations expense
	 */
	public double getTotalOperationsExpense();
	
	/**
	 * Gets the decommission expense.
	 *
	 * @return the decommission expense
	 */
	public double getDecommissionExpense();
	
	/**
	 * Gets the total expense.
	 *
	 * @return the total expense
	 */
	public double getTotalExpense();
	
	/**
	 * Gets the mutable element.
	 *
	 * @return the mutable element
	 */
	public MutableInfrastructureElement getMutableElement();
}
