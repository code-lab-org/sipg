package edu.mit.sips.core.social;

import edu.mit.sips.core.SimEntity;

/**
 * The Interface DemandModel.
 */
public interface DemandModel extends SimEntity {
	
	/**
	 * Gets the demand.
	 *
	 * @return the demand
	 */
	public double getDemand();
	
	/**
	 * Sets the social system.
	 *
	 * @param socialSystem the new social system
	 */
	public void setSocialSystem(SocialSystem socialSystem);
}
