package edu.mit.sips.core;

/**
 * The Class MutableSimpleLifecycleModel.
 */
public final class MutableSimpleLifecycleModel implements MutableLifecycleModel {
	private long timeInitialized, initializationDuration;
	private long operationsDuration, decommissionDuration;
	private double capitalCost, fixedOperationsCost, decommissionCost;
	private boolean levelizeCosts;

	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableLifecycleModel#createLifecycleModel()
	 */
	@Override
	public SimpleLifecycleModel createLifecycleModel() {
		return new SimpleLifecycleModel(timeInitialized, 
				initializationDuration, 
				operationsDuration, decommissionDuration,
				capitalCost, fixedOperationsCost, 
				decommissionCost, levelizeCosts);
	}
	
	/**
	 * Gets the capital cost.
	 *
	 * @return the capital cost
	 */
	public double getCapitalCost() {
		return capitalCost;
	}
	
	/**
	 * Gets the decommission cost.
	 *
	 * @return the decommission cost
	 */
	public double getDecommissionCost() {
		return decommissionCost;
	}
	
	/**
	 * Gets the decommission duration.
	 *
	 * @return the decommission duration
	 */
	public long getDecommissionDuration() {
		return decommissionDuration;
	}
	
	/**
	 * Gets the fixed operations cost.
	 *
	 * @return the fixed operations cost
	 */
	public double getFixedOperationsCost() {
		return fixedOperationsCost;
	}
	
	/**
	 * Gets the initialization duration.
	 *
	 * @return the initialization duration
	 */
	public long getInitializationDuration() {
		return initializationDuration;
	}
	
	/**
	 * Gets the operations duration.
	 *
	 * @return the operations duration
	 */
	public long getOperationsDuration() {
		return operationsDuration;
	}
	
	/**
	 * Gets the time initialized.
	 *
	 * @return the time initialized
	 */
	public long getTimeInitialized() {
		return timeInitialized;
	}
	
	/**
	 * Checks if is levelize costs.
	 *
	 * @return true, if is levelize costs
	 */
	public boolean isLevelizeCosts() {
		return levelizeCosts;
	}
	
	/**
	 * Sets the capital cost.
	 *
	 * @param capitalCost the new capital cost
	 */
	public void setCapitalCost(double capitalCost) {
		this.capitalCost = capitalCost;
	}
	
	/**
	 * Sets the decommission cost.
	 *
	 * @param decommissionCost the new decommission cost
	 */
	public void setDecommissionCost(double decommissionCost) {
		this.decommissionCost = decommissionCost;
	}
	
	/**
	 * Sets the decommission duration.
	 *
	 * @param decommissionDuration the new decommission duration
	 */
	public void setDecommissionDuration(long decommissionDuration) {
		this.decommissionDuration = decommissionDuration;
	}
	
	/**
	 * Sets the fixed operations cost.
	 *
	 * @param fixedOperationsCost the new fixed operations cost
	 */
	public void setFixedOperationsCost(double fixedOperationsCost) {
		this.fixedOperationsCost = fixedOperationsCost;
	}
	
	/**
	 * Sets the initialization duration.
	 *
	 * @param initializationDuration the new initialization duration
	 */
	public void setInitializationDuration(long initializationDuration) {
		this.initializationDuration = initializationDuration;
	}
	
	/**
	 * Sets the levelize costs.
	 *
	 * @param levelizeCosts the new levelize costs
	 */
	public void setLevelizeCosts(boolean levelizeCosts) {
		this.levelizeCosts = levelizeCosts;
	}
	
	/**
	 * Sets the operations duration.
	 *
	 * @param operationsDuration the new operations duration
	 */
	public void setOperationsDuration(long operationsDuration) {
		this.operationsDuration = operationsDuration;
	}

	/**
	 * Sets the time initialized.
	 *
	 * @param timeInitialized the new time initialized
	 */
	public void setTimeInitialized(long timeInitialized) {
		this.timeInitialized = timeInitialized;
	}
}
