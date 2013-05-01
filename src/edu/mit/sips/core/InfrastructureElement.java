package edu.mit.sips.core;

import edu.mit.sips.ElementTemplate;


/**
 * The Interface InfrastructureElement.
 */
public interface InfrastructureElement extends SimEntity {
	
	/**
	 * Adds the element change listener.
	 *
	 * @param listener the listener
	 */
	public void addElementChangeListener(ElementChangeListener listener);
	
	/**
	 * Fire element change event.
	 */
	public void fireElementChangeEvent();

	/**
	 * Gets the capital expense.
	 *
	 * @return the capital expense
	 */
	public double getCapitalExpense();
	
	/**
	 * Gets the decommission expense.
	 *
	 * @return the decommission expense
	 */
	public double getDecommissionExpense();
	
	/**
	 * Gets the destination.
	 *
	 * @return the destination
	 */
	public String getDestination();
	
	/**
	 * Gets the fixed operations expense.
	 *
	 * @return the fixed operations expense
	 */
	public double getFixedOperationsExpense();
	
	/**
	 * Gets the mutable element.
	 *
	 * @return the mutable element
	 */
	public MutableInfrastructureElement getMutableElement();
	
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
	 * Gets the template.
	 *
	 * @return the template
	 */
	public ElementTemplate getTemplate();
	
	/**
	 * Gets the total expense.
	 *
	 * @return the total expense
	 */
	public double getTotalExpense();
	
	/**
	 * Gets the total operations expense.
	 *
	 * @return the total operations expense
	 */
	public double getTotalOperationsExpense();
	
	/**
	 * Checks if is exists.
	 *
	 * @return true, if is exists
	 */
	public boolean isExists();
	
	/**
	 * Checks if is operational.
	 *
	 * @return true, if is operational
	 */
	public boolean isOperational();
	
	/**
	 * Removes the element change listener.
	 *
	 * @param listener the listener
	 */
	public void removeElementChangeListener(ElementChangeListener listener);
}
