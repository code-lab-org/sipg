package edu.mit.sips.core.social;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Class TimeVaryingLogisticGrowthModel.
 * 
 * See http://en.wikipedia.org/wiki/Logistic_function
 */
public class TimeVaryingLogisticGrowthModel implements PopulationModel {
	private long time, nextTime;
	
	private final long initialTime;
	private final long initialPopulation;
	private final Map<Long,Double> growthRateMap;
	private final long carryingCapacity;
	private final boolean linearlyInterpolate;
	
	/**
	 * Instantiates a new logistic growth model.
	 *
	 * @param initialTime the initial time
	 * @param initialPopulation the initial population
	 * @param growthRateMap the time growth rates
	 * @param carryingCapacity the carrying capacity
	 * @param linearlyInterpolate the is linear interpolation
	 */
	public TimeVaryingLogisticGrowthModel(long initialTime,
			long initialPopulation, Map<Long, Double> growthRateMap, 
			long carryingCapacity, boolean linearlyInterpolate) {
		// No need to validate datum time.
		this.initialTime = initialTime;
		
		// Validate initial population.
		if(initialPopulation < 0) {
			throw new IllegalArgumentException(
					"Initial population cannot be negative.");
		}
		this.initialPopulation = initialPopulation;
		
		// Validate growth rate map.
		for(Long key : growthRateMap.keySet()) {
			if(key == null) {
				throw new IllegalArgumentException(
						"Growth rate keys cannot be null.");
			}
			if(growthRateMap.get(key) == null) {
				throw new IllegalArgumentException(
						"Growth rate values cannot be null.");
			}
		}
		this.growthRateMap = Collections.unmodifiableMap(
				new TreeMap<Long,Double>(growthRateMap));
		
		// Validate carrying capacity.
		if(carryingCapacity < 0) {
			throw new IllegalArgumentException(
					"Carrying capacity cannot be negative.");
		}
		this.carryingCapacity = carryingCapacity;
		
		// No need to validate interpolation.
		this.linearlyInterpolate = linearlyInterpolate;
	}
	
	/**
	 * Instantiates a new time varying logistic growth model.
	 */
	protected TimeVaryingLogisticGrowthModel() {
		initialTime = 0;
		initialPopulation = 0;
		growthRateMap = Collections.unmodifiableMap(
				new TreeMap<Long,Double>());
		carryingCapacity = 0;
		linearlyInterpolate = false;
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
				* Math.exp(getGrowthRate() * (time - initialTime))
				/ (carryingCapacity + initialPopulation 
						* (Math.exp(getGrowthRate() * (time - initialTime)) - 1)));
	}
	
	/**
	 * Gets the growth rate.
	 *
	 * @return the growth rate
	 */
	private double getGrowthRate() {
		Long lastKey = null;
		for(Long key : growthRateMap.keySet()) {
			if(lastKey == null && time <= key) {
				return growthRateMap.get(key);
			} else if(lastKey != null && lastKey <= time && key > time) {
				if(linearlyInterpolate) {
					return growthRateMap.get(lastKey) 
							+ (growthRateMap.get(key) - growthRateMap.get(lastKey)) 
							* (time - lastKey) / (key - lastKey);
				} else {
					return growthRateMap.get(lastKey);
				}
			}
			lastKey = key;
		}
		return lastKey==null ? 0 : growthRateMap.get(lastKey);
	}
}
