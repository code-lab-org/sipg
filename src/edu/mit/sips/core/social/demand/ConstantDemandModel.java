package edu.mit.sips.core.social.demand;

import edu.mit.sips.core.social.SocialSystem;

/**
 * The Class ConstantDemandModel.
 */
public class ConstantDemandModel implements DemandModel {
	private final double baselineDemand;
	
	/**
	 * Instantiates a new constant demand model.
	 */
	protected ConstantDemandModel() {
		this(0);
	}
	
	/**
	 * Instantiates a new constant demand model.
	 *
	 * @param baselineDemand the baseline demand
	 */
	public ConstantDemandModel(double baselineDemand) {
		this.baselineDemand = baselineDemand;
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.demand.DemandModel#getDemand()
	 */
	@Override
	public double getDemand() {
		return baselineDemand;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.demand.DemandModel#setSocialSystem(edu.mit.sips.core.social.SocialSystem)
	 */
	@Override
	public void setSocialSystem(SocialSystem socialSystem) {
	}
}
