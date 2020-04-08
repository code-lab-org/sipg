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
package edu.mit.sipg.core.social.population;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * A population model implementation that looks up values from a table.
 * 
 * @author Roi Guinto
 * @author Paul T. Grogan
 */
public class TableLookupModel implements PopulationModel {
	private long time, nextTime;
	private final Map<Long, Long> populationMap;

	/**
	 * Instantiates a new table lookup model.
	 */
	protected TableLookupModel() {
		this(new TreeMap<Long, Long>());
	}
	
	/**
	 * Instantiates a new table lookup model.
	 *
	 * @param initialTime the initial time
	 * @param timeStep the time step
	 * @param populationValues the population values
	 */
	public TableLookupModel(int initialTime, int timeStep, long[] populationValues) {
		this.populationMap = new HashMap<Long, Long>();
		for(int i = initialTime; i < populationValues.length; i += timeStep) {
			populationMap.put(new Long(i), populationValues[i]);
		}
	}
	
	/**
	 * Instantiates a new table lookup model.
	 *
	 * @param populationMap the population map
	 */
	public TableLookupModel(Map<Long, Long> populationMap) {
		this.populationMap = Collections.unmodifiableMap(
				new TreeMap<Long, Long>(populationMap));
	}
	
	@Override
	public long getPopulation() {
		Long populationValue = populationMap.get(time);
		if(populationValue == null) {
			return 0;
		} else {
			return populationValue.longValue();
		}
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
