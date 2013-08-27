package edu.mit.sips.core.social.demand;

import edu.mit.sips.core.social.SocialSystem;

/**
 * The Class LinearBoundedProductDemandModel.
 */
public class LinearBoundedProductDemandModel implements DemandModel {
	private final double minDemand, maxDemand;
	private final double minProduct, maxProduct;
	private SocialSystem socialSystem;
	private double indicator;
	private transient double nextIndicator;
	
	/**
	 * Instantiates a new linear bounded product demand model.
	 */
	protected LinearBoundedProductDemandModel() {
		minDemand = 0;
		maxDemand = 0;
		minProduct = 0;
		maxProduct = 0;
	}
	
	/**
	 * Instantiates a new linear bounded product demand model.
	 *
	 * @param minIndicator the min indicator
	 * @param minDemand the min demand
	 * @param maxIndicator the max indicator
	 * @param maxDemand the max demand
	 */
	public LinearBoundedProductDemandModel(double minIndicator, double minDemand, 
			double maxIndicator, double maxDemand) {
		this.minDemand = minDemand;
		this.maxDemand = maxDemand;
		this.minProduct = minIndicator;
		this.maxProduct = maxIndicator;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.DemandModel#getDemand()
	 */
	@Override
	public double getDemand() {
		double demand = minDemand + (maxDemand - minDemand) 
				* (indicator - minProduct)/(maxProduct - minProduct);
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