package edu.mit.sips.core.lifecycle;

import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class SimpleLifecycleModel.
 * 
 * @author Paul T. Grogan, ptgrogan@mit.edu
 */
public class SimpleLifecycleModel implements LifecycleModel {
	private static final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	private static final TimeUnits timeUnits = TimeUnits.year;
	
	private long time;
	private transient long nextTime;
	private final long timeAvailable, timeInitialized, initializationDuration;
	private final long maxOperationsDuration, operationsDuration, decommissionDuration;
	private final double capitalCost, fixedOperationsCost, decommissionCost;
	private final boolean levelizeCosts;
	
	/**
	 * Instantiates a new simple lifecycle model.
	 */
	protected SimpleLifecycleModel() {
		timeAvailable = 0;
		timeInitialized = 0;
		initializationDuration = 0;
		maxOperationsDuration = 0;
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
	 * @param timeAvailable the time available
	 * @param timeInitialized the time initialized
	 * @param initializationDuration the initialization duration
	 * @param timeDecommissioned the time decommissioned
	 * @param decommissionDuration the decommission duration
	 * @param capitalCost the capital cost
	 * @param fixedOperationsCost the fixed operations cost
	 * @param decommissionCost the decommission cost
	 */
	public SimpleLifecycleModel(long timeAvailable, long timeInitialized, 
			long initializationDuration, long maxOperationsDuration,
			long timeDecommissioned, long decommissionDuration,
			double capitalCost, double fixedOperationsCost, 
			double decommissionCost) {
		this(timeAvailable, timeInitialized, initializationDuration, 
				maxOperationsDuration, 
				timeDecommissioned-timeInitialized-initializationDuration, 
				decommissionDuration, capitalCost, 
				fixedOperationsCost, decommissionCost, false);
	}
	
	/**
	 * Instantiates a new simple lifecycle model.
	 *
	 * @param timeAvailable the time available
	 * @param timeInitialized the time initialized
	 * @param initializationDuration the initialization duration
	 * @param maxOperationsDuration the max operations duration
	 * @param operationsDuration the operations duration
	 * @param decommissionDuration the decommission duration
	 * @param capitalCost the capital cost
	 * @param fixedOperationsCost the fixed operations cost
	 * @param decommissionCost the decommission cost
	 * @param levelizeCosts the levelize costs
	 */
	public SimpleLifecycleModel(long timeAvailable, long timeInitialized, 
			long initializationDuration, long maxOperationsDuration,
			long operationsDuration, long decommissionDuration, double capitalCost, 
			double fixedOperationsCost, double decommissionCost, 
			boolean levelizeCosts) {
		// No validation needed for time available.
		this.timeAvailable = timeAvailable;
		
		// Validate time initialized.
		if(timeInitialized < timeAvailable) {
			throw new IllegalArgumentException(
					"Time initialized cannot precede time available.");
		}
		this.timeInitialized = timeInitialized;
		
		// Validate the implementation duration.
		if(initializationDuration < 0) {
			throw new IllegalArgumentException(
					"Initialization duration cannot be negative.");
		}
		this.initializationDuration = initializationDuration;

		// Validate the max operational duration
		if(maxOperationsDuration < 0) {
			throw new IllegalArgumentException(
					"Max operations duration cannot be negative.");
		}
		this.maxOperationsDuration = maxOperationsDuration;
		
		// Validate the operational duration
		if(operationsDuration > this.maxOperationsDuration) {
			throw new IllegalArgumentException(
					"Operations duration cannot exceed maximum.");
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#getMutableLifecycleModel()
	 */
	@Override
	public MutableSimpleLifecycleModel getMutableLifecycleModel() {
		MutableSimpleLifecycleModel model = new MutableSimpleLifecycleModel();
		model.setTimeAvailable((long) TimeUnits.convert(
				timeAvailable, this, model));
		model.setTimeInitialized((long) TimeUnits.convert(
				timeInitialized, this, model));
		model.setInitializationDuration((long) TimeUnits.convert(
				initializationDuration, this, model));
		model.setMaxOperationsDuration((long) TimeUnits.convert(
				maxOperationsDuration, this, model));
		model.setOperationsDuration((long) TimeUnits.convert(
				operationsDuration, this, model));
		model.setDecommissionDuration((long) TimeUnits.convert(
				decommissionDuration, this, model));
		model.setCapitalCost(CurrencyUnits.convertStock(
				capitalCost, this, model));
		model.setFixedOperationsCost(CurrencyUnits.convertFlow(
				fixedOperationsCost, this, model));
		model.setDecommissionCost(CurrencyUnits.convertStock(
				decommissionCost, this, model));
		model.setLevelizeCosts(levelizeCosts);
		return model;
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
