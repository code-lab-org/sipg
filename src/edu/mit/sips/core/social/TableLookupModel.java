package edu.mit.sips.core.social;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Class TableLookupModel.
 * 
 * @author Roi Guinto, guinto@mit.edu
 * @author Paul T. Grogan, ptgrogan@mit.edu
 */
public class TableLookupModel implements PopulationModel {
	private long time, nextTime;
	private final Map<Long, Long> populationMap;

	/**
	 * Instantiates a new table lookup model.
	 *
	 * @param populationMap the population map
	 */
	public TableLookupModel(Map<Long, Long> populationMap) {
		// make a copy of the passed map for safety
		this.populationMap = Collections.unmodifiableMap(
				new TreeMap<Long, Long>(populationMap));
	}
	
	/**
	 * Instantiates a new table lookup model.
	 */
	protected TableLookupModel() {
		populationMap = Collections.unmodifiableMap(
				new TreeMap<Long, Long>());
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
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.PopulationModel#getPopulation(int)
	 */
	@Override
	public long getPopulation() {
		
		Long populationValue = populationMap.get(time);
		
		if(populationValue == null) {
			
			return 0;
			
		} else {
			
			return populationValue.longValue();
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.PopulationModel#initialize(long)
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
