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

/**
 * A population model implementation that exhibits exponential growth over time.
 * 
 * See http://en.wikipedia.org/wiki/Malthusian_Growth_Model
 * 
 * @author Paul T. Grogan
 */
public class ExponentialGrowthModel implements PopulationModel {
	private final long initialTime;
	private final long initialPopulation;
	private final double growthRate;
	
	private long time;
	private transient long nextTime;
	
	/**
	 * Instantiates a new exponential growth model.
	 */
	protected ExponentialGrowthModel() {
		this(0, 0, 0);
	}
	
	/**
	 * Instantiates a new exponential growth model.
	 *
	 * @param initialTime the initial time
	 * @param initialPopulation the initial population
	 * @param growthRate the growth rate
	 */
	public ExponentialGrowthModel(long initialTime, long initialPopulation, 
			double growthRate) {
		this.initialTime = initialTime;
		
		if(initialPopulation < 0) {
			throw new IllegalArgumentException(
					"Initial population cannot be negative.");
		}
		this.initialPopulation = initialPopulation;
		
		this.growthRate = growthRate;
	}

	@Override
	public long getPopulation() {
		return Math.round(initialPopulation 
				* Math.exp(growthRate * (time - initialTime)));
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
