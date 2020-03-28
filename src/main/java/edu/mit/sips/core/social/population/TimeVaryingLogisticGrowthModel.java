/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sips.core.social.population;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * A population model implementation that exhibits logistic 
 * growth with variable rates over time.
 * 
 * See http://en.wikipedia.org/wiki/Logistic_function
 * 
 * @author Paul T. Grogan
 */
public class TimeVaryingLogisticGrowthModel implements PopulationModel {
	private long time, nextTime;
	
	private final long initialTime;
	private final long initialPopulation;
	private final Map<Long,Double> growthRateMap;
	private final long carryingCapacity;
	private final boolean linearInterpolation;
	
	/**
	 * Instantiates a new time-varying logistic growth model.
	 */
	protected TimeVaryingLogisticGrowthModel() {
		this(0, 0, new TreeMap<Long,Double>(), 0, false);
	}
	
	/**
	 * Instantiates a new logistic growth model.
	 *
	 * @param initialTime the initial time
	 * @param initialPopulation the initial population
	 * @param growthRateMap the time growth rates
	 * @param carryingCapacity the carrying capacity
	 * @param linearInterpolation if linear interpolation should be used
	 */
	public TimeVaryingLogisticGrowthModel(long initialTime,
			long initialPopulation, Map<Long, Double> growthRateMap, 
			long carryingCapacity, boolean linearInterpolation) {
		this.initialTime = initialTime;
		
		if(initialPopulation < 0) {
			throw new IllegalArgumentException(
					"Initial population cannot be negative.");
		}
		this.initialPopulation = initialPopulation;
		
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
		
		if(carryingCapacity < 0) {
			throw new IllegalArgumentException(
					"Carrying capacity cannot be negative.");
		}
		this.carryingCapacity = carryingCapacity;
		
		this.linearInterpolation = linearInterpolation;
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
				if(linearInterpolation) {
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
	
	@Override
	public long getPopulation() {
		return Math.round(carryingCapacity * initialPopulation 
				* Math.exp(getGrowthRate() * (time - initialTime))
				/ (carryingCapacity + initialPopulation 
						* (Math.exp(getGrowthRate() * (time - initialTime)) - 1)));
	}
	
	@Override
	public void initialize(long time) {
		this.time = time;
	}

	@Override
	public void tick() {
		nextTime = time + 1;
	}
	
	@Override
	public void tock() {
		time = nextTime;
	}
}
