package edu.mit.sips.sim.util2;

import java.math.BigDecimal;

/**
 * The Interface Units.
 */
public interface Units<t extends ResourceType> {
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the abbreviation.
	 *
	 * @return the abbreviation
	 */
	public String getAbbreviation();
	
	/**
	 * Convert to.
	 *
	 * @param units the units
	 * @param value the value
	 * @return the double
	 */
	public BigDecimal convertTo(Units<t> units, BigDecimal value);
}
