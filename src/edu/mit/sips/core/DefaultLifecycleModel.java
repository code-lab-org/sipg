package edu.mit.sips.core;

/**
 * The Class DefaultLifecycleModel.
 */
public class DefaultLifecycleModel implements LifecycleModel, MutableLifecycleModel {

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#isOperational()
	 */
	@Override
	public boolean isOperational() {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#isExists()
	 */
	@Override
	public boolean isExists() {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.LifecycleModel#getCapitalExpense()
	 */
	@Override
	public double getCapitalExpense() {
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
	 * @see edu.mit.sips.LifecycleModel#getDecommissionExpense()
	 */
	@Override
	public double getDecommissionExpense() {
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
	 * @see edu.mit.sips.MutableLifecycleModel#createLifecycleModel()
	 */
	@Override
	public LifecycleModel createLifecycleModel() {
		return new DefaultLifecycleModel();
	}
}
