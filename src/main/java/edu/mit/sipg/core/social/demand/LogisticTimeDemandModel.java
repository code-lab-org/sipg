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
package edu.mit.sipg.core.social.demand;

import edu.mit.sipg.core.Society;

/**
 * A demand model implementation that exhibits logistic demands over time.
 * 
 * @author Paul T. Grogan
 */
public class LogisticTimeDemandModel implements DemandModel {
	private final long baselineTime;
	private final double baselineDemand, growthRate;
	private final double minDemand, maxDemand;
	private long time;
	private transient long nextTime;
	
	/**
	 * Instantiates a new logistic time demand model.
	 */
	protected LogisticTimeDemandModel() {
		this(0,0,0,0,0);
	}
	
	/**
	 * Instantiates a new logistic time demand model.
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
		this.minDemand = minimumDemand;
		this.maxDemand = maximumDemand;
	}
	
	@Override
	public double getDemand(Society society) {
		return (minDemand + (maxDemand-minDemand) 
				* (baselineDemand-minDemand) 
				* Math.exp(growthRate * (time - baselineTime)) 
				/ ((maxDemand-minDemand) + (baselineDemand-minDemand) 
						* (Math.exp(growthRate * (time - baselineTime)) - 1)))
				* society.getPopulation();
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
