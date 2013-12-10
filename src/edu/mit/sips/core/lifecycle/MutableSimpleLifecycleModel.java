package edu.mit.sips.core.lifecycle;

import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class MutableSimpleLifecycleModel.
 */
public final class MutableSimpleLifecycleModel implements MutableLifecycleModel {
	private static final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	private static final TimeUnits timeUnits = TimeUnits.year;
	
	private long timeAvailable, timeInitialized, initializationDuration;
	private long maxOperationsDuration, operationsDuration, decommissionDuration;
	private double capitalCost, fixedOperationsCost, decommissionCost;
	private boolean levelizeCosts;

	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableLifecycleModel#createLifecycleModel()
	 */
	@Override
	public SimpleLifecycleModel createLifecycleModel() {
		SimpleLifecycleModel m = new SimpleLifecycleModel(); // for units
		return new SimpleLifecycleModel(
				(long) TimeUnits.convert(timeAvailable, this, m),
				(long) TimeUnits.convert(timeInitialized, this, m), 
				(long) TimeUnits.convert(initializationDuration, this, m), 
				(long) TimeUnits.convert(maxOperationsDuration, this, m),
				(long) TimeUnits.convert(operationsDuration, this, m), 
				(long) TimeUnits.convert(decommissionDuration, this, m),
				CurrencyUnits.convertStock(capitalCost, this, m),
				CurrencyUnits.convertFlow(fixedOperationsCost, this, m), 
				CurrencyUnits.convertStock(decommissionCost, this, m), 
				levelizeCosts);
	}
	/**
	 * Gets the capital cost.
	 *
	 * @return the capital cost
	 */
	public double getCapitalCost() {
		return capitalCost;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyTimeUnits()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return timeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnits()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
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
	 * Gets the max operations duration.
	 *
	 * @return the max operations duration
	 */
	public long getMaxOperationsDuration() {
		return maxOperationsDuration;
	}
	
	/**
	 * Gets the max time decommissioned.
	 *
	 * @return the max time decommissioned
	 */
	public long getMaxTimeDecommissioned() {
		return timeInitialized + initializationDuration + maxOperationsDuration;
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
	 * Gets the time available.
	 *
	 * @return the time available
	 */
	public long getTimeAvailable() {
		return timeAvailable;
	}
	
	/**
	 * Gets the time decommissioned.
	 *
	 * @return the time decommissioned
	 */
	public long getTimeDecommissioned() {
		return timeInitialized + initializationDuration + operationsDuration;
	}
	
	/**
	 * Gets the time initialized.
	 *
	 * @return the time initialized
	 */
	public long getTimeInitialized() {
		return timeInitialized;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.TimeUnitsOutput#getTimeUnits()
	 */
	@Override
	public TimeUnits getTimeUnits() {
		return timeUnits;
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
	 * Sets the max operations duration.
	 *
	 * @param maxOperationsDuration the new max operations duration
	 */
	public void setMaxOperationsDuration(long maxOperationsDuration) {
		this.maxOperationsDuration = maxOperationsDuration;
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
	 * Sets the time available.
	 *
	 * @param timeAvailable the new time available
	 */
	public void setTimeAvailable(long timeAvailable) {
		this.timeAvailable = timeAvailable;
	}

	/**
	 * Sets the time decommissioned.
	 *
	 * @param timeDecommissioned the new time decommissioned
	 */
	public void setTimeDecommissioned(long timeDecommissioned) {
		setOperationsDuration(timeDecommissioned - timeInitialized - initializationDuration);
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
