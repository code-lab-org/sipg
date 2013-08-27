package edu.mit.sips.core.social.demand;

import edu.mit.sips.core.social.SocialSystem;

/**
 * The Class DefaultDemandModel.
 */
public class DefaultDemandModel implements DemandModel {
	
	/**
	 * Instantiates a new default demand model.
	 */
	public DefaultDemandModel() {
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.demand.DemandModel#getDemand(edu.mit.sips.core.social.SocialSystem)
	 */
	@Override
	public double getDemand(SocialSystem socialSystem) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tick()
	 */
	@Override
	public void tick() {
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tock()
	 */
	@Override
	public void tock() {
	}
}
