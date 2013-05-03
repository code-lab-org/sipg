package edu.mit.sips.core.social;

/**
 * The Class ExponentialGrowthModel.
 * 
 * See http://en.wikipedia.org/wiki/Malthusian_Growth_Model
 */
public class ExponentialGrowthModel implements PopulationModel {
	private final long initialTime;
	private final long initialPopulation;
	private final double growthRate;
	
	private long time;
	private transient long nextTime;
	
	/**
	 * Instantiates a new exponential growth model.
	 *
	 * @param initialTime the initial time
	 * @param initialPopulation the initial population
	 * @param growthRate the growth rate
	 */
	public ExponentialGrowthModel(long initialTime, long initialPopulation, 
			double growthRate) {
		// No need to validate datum time.
		this.initialTime = initialTime;
		
		// Validate initial population.
		if(initialPopulation < 0) {
			throw new IllegalArgumentException(
					"Initial population cannot be negative.");
		}
		this.initialPopulation = initialPopulation;
		
		// No need to validate growth rate.
		this.growthRate = growthRate;
	}
	
	/**
	 * Instantiates a new exponential growth model.
	 */
	protected ExponentialGrowthModel() {
		this.initialTime = 0;
		this.initialPopulation = 0;
		this.growthRate = 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PopulationModel#getPopulation(int)
	 */
	@Override
	public long getPopulation() {
		return Math.round(initialPopulation 
				* Math.exp(growthRate * (time - initialTime)));
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		this.time = time;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tick()
	 */
	@Override
	public void tick() {
		nextTime = time + 1;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tock()
	 */
	@Override
	public void tock() {
		time = nextTime;
	}
}
