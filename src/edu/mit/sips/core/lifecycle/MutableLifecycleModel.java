package edu.mit.sips.core.lifecycle;

import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.TimeUnitsOutput;

/**
 * The Interface MutableLifecycleModel.
 */
public interface MutableLifecycleModel extends TimeUnitsOutput, CurrencyUnitsOutput {
	
	/**
	 * Creates the lifecycle model.
	 *
	 * @return the lifecycle model
	 */
	public LifecycleModel createLifecycleModel();
}
