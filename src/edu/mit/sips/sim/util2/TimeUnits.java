package edu.mit.sips.sim.util2;

import java.math.BigDecimal;

/**
 * The Class TimeUnits.
 */
public class TimeUnits {
	public static final TimeUnits 
			ms = new TimeUnits("milliseconds","ms",365.242*24*60*60*1000),
			day = new TimeUnits("day","day", 365.242), 
			year = new TimeUnits("year","yr", 1);
	private final String name;
	private final String abbreviation;
	private final BigDecimal scale;
	
	/**
	 * Instantiates a new denominator units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	protected TimeUnits(String name, String abbreviation, double scale) {
		this(name, abbreviation, new BigDecimal(scale));
	}
	
	/**
	 * Instantiates a new default units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	protected TimeUnits(String name, String abbreviation, BigDecimal scale) {
		this.name = name;
		this.abbreviation = abbreviation;
		this.scale = scale;
	}
	
	/**
	 * Convert to.
	 *
	 * @param units the units
	 * @param value the value
	 * @return the big decimal
	 */
	public BigDecimal convertTo(TimeUnits units, BigDecimal value) {
		return value.divide(this.scale).multiply(units.scale);
	}
	
	/**
	 * Gets the abbreviation.
	 *
	 * @return the abbreviation
	 */
	public String getAbbreviation() { 
		return abbreviation; 
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() { 
		return name; 
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() { 
		return abbreviation; 
	}
}
