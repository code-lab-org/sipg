package edu.mit.sips.core.social.demand;

import edu.mit.sips.core.SimEntity;
import edu.mit.sips.core.Society;

/**
 * The Interface DemandModel.
 */
public interface DemandModel extends SimEntity {
	
	/**
	 * Gets the demand.
	 *
	 * @param society the society
	 * @return the demand
	 */
	public double getDemand(Society society);
}
