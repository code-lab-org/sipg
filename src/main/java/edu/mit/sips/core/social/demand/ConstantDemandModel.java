package edu.mit.sips.core.social.demand;

import edu.mit.sips.core.Society;

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
	 * @see edu.mit.sips.core.social.demand.DemandModel#getDemand(edu.mit.sips.core.social.SocialSystem)
	 */
	@Override
	public double getDemand(Society society) {
		return baselineDemand * society.getPopulation();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) { }

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tick()
	 */
	@Override
	public void tick() { }

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tock()
	 */
	@Override
	public void tock() { }
}
