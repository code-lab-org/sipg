package edu.mit.sips.core.social.demand;

import edu.mit.sips.core.social.SocialSystem;

/**
 * The Class LogisticTimeDemandModel.
 */
public class LogisticTimeDemandModel implements DemandModel {
	private final long baselineTime;
	private final double baselineDemand, growthRate;
	private final double minimumDemand, maximumDemand;
	private long time;
	private transient long nextTime;
	
	/**
	 * Instantiates a new logistic time demand model.
	 */
	protected LogisticTimeDemandModel() {
		this(0,0,0,0,0);
	}
	
	/**
	 * Instantiates a new exponential time demand model.
	 *
	 * @param baselineTime the baseline time
	 * @param baselineDemand the baseline demand
	 * @param growthRate the growth rate
	 * @param minimumDemand the minimum demand
	 * @param maximumDemand the maximum demand
	 */
	public LogisticTimeDemandModel(long baselineTime, 
			double baselineDemand, double growthRate, 
			double minimumDemand, double maximumDemand) {
		this.baselineTime = baselineTime;
		this.baselineDemand = baselineDemand;
		this.growthRate = growthRate;
		this.minimumDemand = minimumDemand;
		this.maximumDemand = maximumDemand;
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
	 * @see edu.mit.sips.core.social.demand.DemandModel#getDemand()
	 */
	@Override
	public double getDemand() {
		return minimumDemand + (maximumDemand-minimumDemand) * (baselineDemand-minimumDemand) * Math.exp(growthRate * (time - baselineTime)) 
				/ ((maximumDemand-minimumDemand) + (baselineDemand-minimumDemand) * (Math.exp(growthRate * (time - baselineTime)) - 1));
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.demand.DemandModel#setSocialSystem(edu.mit.sips.core.social.SocialSystem)
	 */
	@Override
	public void setSocialSystem(SocialSystem socialSystem) {
	}
}
