package edu.mit.sips.core.social;


/**
 * The Class LinearDemandModel.
 */
public class LinearBoundedDemandModel implements DemandModel {
	private final double minDemand, maxDemand;
	private final double minIndicator, maxIndicator;
	private SocialSystem socialSystem;
	private double indicator;
	private transient double nextIndicator;
	
	/**
	 * Instantiates a new linear bounded demand model.
	 */
	protected LinearBoundedDemandModel() {
		minDemand = 0;
		maxDemand = 0;
		minIndicator = 0;
		maxIndicator = 0;
	}
	
	/**
	 * Instantiates a new linear demand model.
	 *
	 * @param minIndicator the min indicator
	 * @param minDemand the min demand
	 * @param maxIndicator the max indicator
	 * @param maxDemand the max demand
	 */
	public LinearBoundedDemandModel(double minIndicator, double minDemand, 
			double maxIndicator, double maxDemand) {
		// TODO: validate arguments
		
		this.minDemand = minDemand;
		this.maxDemand = maxDemand;
		this.minIndicator = minIndicator;
		this.maxIndicator = maxIndicator;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.DemandModel#getDemand()
	 */
	@Override
	public double getDemand() {
		double demand = minDemand + (maxDemand - minDemand) 
				* (indicator - minIndicator)/(maxIndicator - minIndicator);
		return Math.max(minDemand, Math.min(maxDemand, demand));
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		if(socialSystem != null) {
			indicator = socialSystem.getDomesticProductPerCapita();
		} else {
			indicator = 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tick()
	 */
	@Override
	public void tick() {
		if(socialSystem != null) {
			nextIndicator = socialSystem.getDomesticProductPerCapita();
		} else {
			nextIndicator = 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tock()
	 */
	@Override
	public void tock() {
		indicator = nextIndicator;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.DemandModel#setSocialSystem(edu.mit.sips.core.social.SocialSystem)
	 */
	@Override
	public void setSocialSystem(SocialSystem socialSystem) {
		this.socialSystem = socialSystem;
	}
}
