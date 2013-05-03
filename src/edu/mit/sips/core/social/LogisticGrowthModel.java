package edu.mit.sips.core.social;

/**
 * The Class LogisticGrowthModel.
 * 
 * See http://en.wikipedia.org/wiki/Logistic_function
 */
public class LogisticGrowthModel implements PopulationModel {
	private long time;
	private transient long nextTime;
	
	private final long initialTime;
	private final long initialPopulation;
	private final double growthRate;
	private final long carryingCapacity;
	
	/**
	 * Instantiates a new logistic growth model.
	 *
	 * @param initialTime the initial time
	 * @param initialPopulation the initial population
	 * @param growthRate the growth rate
	 * @param carryingCapacity the carrying capacity
	 */
	public LogisticGrowthModel(long initialTime,
			long initialPopulation, 
			double growthRate, long carryingCapacity) {
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
		
		// Validate carrying capacity.
		if(carryingCapacity < 0) {
			throw new IllegalArgumentException(
					"Carrying capacity cannot be negative.");
		}
		this.carryingCapacity = carryingCapacity;
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.PopulationModel#getPopulation()
	 */
	@Override
	public long getPopulation() {
		return Math.round(carryingCapacity * initialPopulation 
				* Math.exp(growthRate * (time - initialTime))
				/ (carryingCapacity + initialPopulation 
						* (Math.exp(growthRate * (time - initialTime)) - 1)));
	}

}
