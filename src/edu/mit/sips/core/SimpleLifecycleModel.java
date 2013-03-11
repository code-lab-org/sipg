package edu.mit.sips.core;

/**
 * The Class SimpleLifecycleModel.
 * 
 * @author Paul T. Grogan, ptgrogan@mit.edu
 */
public class SimpleLifecycleModel implements LifecycleModel {
	private long time, nextTime;
	private final long timeInitialized, initializationDuration;
	private final long operationsDuration, decommissionDuration;
	private final double capitalCost, fixedOperationsCost, decommissionCost;
	private final boolean levelizeCosts;

	/**
	 * Instantiates a new simple lifecycle model.
	 */
	protected SimpleLifecycleModel() {
		timeInitialized = 0;
		initializationDuration = 0;
		operationsDuration = 0;
		decommissionDuration = 0;
		capitalCost = 0;
		fixedOperationsCost = 0;
		decommissionCost = 0;
		levelizeCosts = false;
	}

	/**
	 * Instantiates a new simple lifecycle model.
	 *
	 * @param timeInitialized the time initialized
	 * @param initializationDuration the initialization duration
	 * @param timeDecommissioned the time decommissioned
	 * @param decommissionDuration the decommission duration
	 * @param capitalCost the capital cost
	 * @param fixedOperationsCost the fixed operations cost
	 * @param decommissionCost the decommission cost
	 */
	public SimpleLifecycleModel(long timeInitialized, long initializationDuration, 
			long timeDecommissioned, long decommissionDuration,
			double capitalCost, double fixedOperationsCost, 
			double decommissionCost) {
		this(timeInitialized, initializationDuration, timeDecommissioned, 
				decommissionDuration, capitalCost, fixedOperationsCost, 
				decommissionCost, false);
	}

	/**
	 * Instantiates a new simple lifecycle model.
	 *
	 * @param timeInitialized the time initialized
	 * @param initializationDuration the initialization duration
	 * @param operationsDuration the operations duration
	 * @param decommissionDuration the decommission duration
	 * @param capitalCost the capital cost
	 * @param fixedOperationsCost the fixed operations cost
	 * @param decommissionCost the decommission cost
	 * @param levelizeCosts the levelize costs
	 */
	public SimpleLifecycleModel(long timeInitialized, 
			long initializationDuration, long operationsDuration, 
			long decommissionDuration, double capitalCost, 
			double fixedOperationsCost, double decommissionCost, 
			boolean levelizeCosts) {
		// No validation needed for time initialized.
		this.timeInitialized = timeInitialized;
		
		// Validate the implementation duration.
		if(initializationDuration < 0) {
			throw new IllegalArgumentException(
					"Initialization duration cannot be negative.");
		}
		this.initializationDuration = initializationDuration;
		
		// Validate the operational duration
		if(operationsDuration < 0) {
			throw new IllegalArgumentException(
					"Operations duration cannot be .");
		}
		this.operationsDuration = operationsDuration;
		
		// Validate the decommission duration.
		if(decommissionDuration < 0) {
			throw new IllegalArgumentException(
					"Decommission duration cannot be negative.");
		}
		this.decommissionDuration = decommissionDuration;
		
		// Validate the capital cost.
		if(capitalCost < 0) {
			throw new IllegalArgumentException(
					"Capital cost cannot be negative.");
		}
		this.capitalCost = capitalCost;
		
		// Validate the fixed operations cost.
		if(fixedOperationsCost < 0) {
			throw new IllegalArgumentException(
					"Fixed operations cost cannot be negative.");
		}
		this.fixedOperationsCost = fixedOperationsCost;

		// Validate the decommission cost.
		if(decommissionCost < 0) {
			throw new IllegalArgumentException(
					"Decommission cost cannot be negative.");
		}
		this.decommissionCost = decommissionCost;
		
		// No need to validate levelize capital.
		this.levelizeCosts = levelizeCosts;
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
	 * @see edu.mit.sips.LifecycleModel#getCapitalExpense()
	 */
	@Override
	public double getCapitalExpense() {
		if(time == timeInitialized 
				&& (initializationDuration == 0 || !levelizeCosts)) {
			return capitalCost;
		} else if(levelizeCosts 
				&& initializationDuration > 0
				&& time >= timeInitialized 
				&& time <= timeInitialized + initializationDuration) {
			return capitalCost / initializationDuration;
		} else {
			return 0;
		}
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#getDecommissionExpense()
	 */
	@Override
	public double getDecommissionExpense() {
		if(time == getTimeDecommissioned()
				&& (decommissionDuration == 0 || !levelizeCosts)) {
			return decommissionCost;
		} else if(levelizeCosts 
				&& decommissionDuration > 0
				&& time >= getTimeDecommissioned() 
				&& time <= getTimeDecommissioned() + decommissionDuration) {
			return decommissionCost / decommissionDuration;
		} else {
			return 0;
		}
	}
	
	/**
	 * Gets the fixed operations cost.
	 *
	 * @return the fixed operations cost
	 */
	public double getFixedOperationsCost() {
		return fixedOperationsCost;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#getFixedOperationsExpense()
	 */
	@Override
	public double getFixedOperationsExpense() {
		if(isOperational()) {
			return fixedOperationsCost;
		} else {
			return 0;
		}
	}
	
	/**
	 * Gets the initialization duration.
	 *
	 * @return the initialization duration
	 */
	public long getInitializationDuration() {
		return initializationDuration;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#getMutableLifecycleModel()
	 */
	@Override
	public MutableSimpleLifecycleModel getMutableLifecycleModel() {
		MutableSimpleLifecycleModel model = new MutableSimpleLifecycleModel();
		model.setTimeInitialized(timeInitialized);
		model.setInitializationDuration(initializationDuration);
		model.setOperationsDuration(operationsDuration);
		model.setDecommissionDuration(decommissionDuration);
		model.setCapitalCost(capitalCost);
		model.setFixedOperationsCost(fixedOperationsCost);
		model.setDecommissionCost(decommissionCost);
		model.setLevelizeCosts(levelizeCosts);
		return model;
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
	 * @see edu.mit.sips.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		this.time = time;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#isExists()
	 */
	@Override
	public boolean isExists() {
		return time >= timeInitialized 
				&& time < getTimeDecommissioned() + decommissionDuration;
	}
	
	/**
	 * Checks if is levelize costs.
	 *
	 * @return true, if is levelize costs
	 */
	public boolean isLevelizeCosts() {
		return levelizeCosts;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#isOperational()
	 */
	@Override
	public boolean isOperational() {
		return time >= timeInitialized + initializationDuration 
				&& time < getTimeDecommissioned();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tick()
	 */
	@Override
	public void tick() {
		nextTime = time + 1;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tock()
	 */
	@Override
	public void tock() {
		time = nextTime;
	}
}
