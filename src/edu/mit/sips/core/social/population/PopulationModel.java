package edu.mit.sips.core.social.population;

import edu.mit.sips.core.SimEntity;

/**
 * The Interface PopulationModel.
 */
public interface PopulationModel extends SimEntity {
	
	/**
	 * Gets the population.
	 *
	 * @return the population
	 */
	public long getPopulation();
}
