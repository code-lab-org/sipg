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
package edu.mit.sips.core.social.demand;

import edu.mit.sips.core.Society;

/**
 * A demand model implementation that exhibits linearly-variable demands over time.
 * 
 * @author Paul T. Grogan
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
	
	@Override
	public double getDemand(Society society) {
		return baselineDemand + (time - baselineTime) * demandSlope 
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
