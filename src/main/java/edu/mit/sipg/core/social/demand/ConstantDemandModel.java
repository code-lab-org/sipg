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
 * A demand model implementation that exhibits constant demands over time.
 * 
 * @author Paul T. Grogan
 */
public class ConstantDemandModel extends DefaultDemandModel {
	private final double baselineDemand;
	
	/**
	 * Instantiates a new constant demand model.
	 */
	protected ConstantDemandModel() {
		this(0);
	}
	
	/**
	 * Instantiates a new constant demand model.
	 *
	 * @param baselineDemand the baseline demand
	 */
	public ConstantDemandModel(double baselineDemand) {
		this.baselineDemand = baselineDemand;
	}

	@Override
	public double getDemand(Society society) {
		return baselineDemand * society.getPopulation();
	}
}
