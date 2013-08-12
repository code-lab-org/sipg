package edu.mit.sips.core.social;

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
	 * @see edu.mit.sips.core.social.DemandModel#getDemand()
	 */
	@Override
	public double getDemand() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.DemandModel#setSocialSystem(edu.mit.sips.core.social.SocialSystem)
	 */
	@Override
	public void setSocialSystem(SocialSystem socialSystem) {
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
