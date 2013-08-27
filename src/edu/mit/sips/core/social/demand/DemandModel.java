package edu.mit.sips.core.social.demand;

import edu.mit.sips.core.SimEntity;
import edu.mit.sips.core.social.SocialSystem;

/**
 * The Interface DemandModel.
 */
public interface DemandModel extends SimEntity {
	
	/**
	 * Gets the demand.
	 *
	 * @param socialSystem the social system
	 * @return the demand
	 */
	public double getDemand(SocialSystem socialSystem);
}
