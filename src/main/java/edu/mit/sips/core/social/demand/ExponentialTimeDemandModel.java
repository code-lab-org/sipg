package edu.mit.sips.core.social.demand;

import edu.mit.sips.core.Society;

/**
 * The Class ExponentialTimeDemandModel.
 */
public class ExponentialTimeDemandModel implements DemandModel {
	private final long baselineTime;
	private final double baselineDemand, growthRate;
	private long time;
	private transient long nextTime;
	
	/**
	 * Instantiates a new exponential time demand model.
	 */
	protected ExponentialTimeDemandModel() {
		this(0,0,0);
	}
	
	/**
	 * Instantiates a new exponential time demand model.
	 *
	 * @param baselineTime the baseline time
	 * @param baselineDemand the baseline demand
	 * @param growthRate the growth rate
	 */
	public ExponentialTimeDemandModel(long baselineTime, 
			double baselineDemand, double growthRate) {
		this.baselineTime = baselineTime;
		this.baselineDemand = baselineDemand;
		this.growthRate = growthRate;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		this.time = time;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tick()
	 */
	@Override
	public void tick() {
		nextTime = time + 1;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tock()
	 */
	@Override
	public void tock() {
		time = nextTime;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.demand.DemandModel#getDemand(edu.mit.sips.core.social.SocialSystem)
	 */
	@Override
	public double getDemand(Society society) {
		return baselineDemand * Math.exp(growthRate * (time - baselineTime)) 
				* society.getPopulation();
	}
}
