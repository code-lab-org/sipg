package edu.mit.sips.core;

import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class DefaultLifecycleModel.
 */
public class DefaultLifecycleModel implements LifecycleModel, MutableLifecycleModel {
	private final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	private final TimeUnits timeUnits = TimeUnits.year;

	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableLifecycleModel#createLifecycleModel()
	 */
	@Override
	public LifecycleModel createLifecycleModel() {
		return new DefaultLifecycleModel();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#getCapitalExpense()
	 */
	@Override
	public double getCapitalExpense() {
		return 0;
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#getDecommissionExpense()
	 */
	@Override
	public double getDecommissionExpense() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#getFixedOperationsExpense()
	 */
	@Override
	public double getFixedOperationsExpense() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#getMutableLifecycleModel()
	 */
	@Override
	public MutableLifecycleModel getMutableLifecycleModel() {
		return this;
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
		
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#isExists()
	 */
	@Override
	public boolean isExists() {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#isOperational()
	 */
	@Override
	public boolean isOperational() {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tick()
	 */
	@Override
	public void tick() {
		
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tock()
	 */
	@Override
	public void tock() {
		
	}
}
