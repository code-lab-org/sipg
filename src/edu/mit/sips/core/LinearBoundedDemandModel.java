package edu.mit.sips.core;

/**
 * The Class LinearDemandModel.
 */
public class LinearBoundedDemandModel {
	private final double minDemand;
	private final double maxDemand;
	private final double minIndicator;
	private final double maxIndicator;
	
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
		this.minDemand = minDemand;
		this.maxDemand = maxDemand;
		this.minIndicator = minIndicator;
		this.maxIndicator = maxIndicator;
	}
	
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
	 * Gets the demand.
	 *
	 * @param indicator the indicator
	 * @return the demand
	 */
	public double getDemand(double indicator) {
		double demand = minDemand + (maxDemand - minDemand) 
				* (indicator - minIndicator)/(maxIndicator - minIndicator);
		return Math.max(minDemand, Math.min(maxDemand, demand));
	}
}
