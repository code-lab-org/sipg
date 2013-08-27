package edu.mit.sips.core.social.demand;

import edu.mit.sips.core.social.SocialSystem;

/**
 * The Class LinearTimeDemandModel.
 */
public class LinearTimeDemandModel implements DemandModel {
	private final long baselineTime;
	private final double baselineDemand, demandSlope;
	private long time;
	private transient long nextTime;
	
	/**
	 * Instantiates a new linear time demand model.
	 */
	protected LinearTimeDemandModel() {
		this(0,0,0);
	}
	
	/**
	 * Instantiates a new linear time demand model.
	 *
	 * @param baselineTime the baseline time
	 * @param baselineDemand the baseline demand
	 * @param demandSlope the demand slope
	 */
	public LinearTimeDemandModel(long baselineTime, 
			double baselineDemand, double demandSlope) {
		this.baselineTime = baselineTime;
		this.baselineDemand = baselineDemand;
		this.demandSlope = demandSlope;
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
	public double getDemand(SocialSystem socialSystem) {
		return baselineDemand + (time - baselineTime) * demandSlope;
	}
}
