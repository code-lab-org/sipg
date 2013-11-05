package edu.mit.sips.sim.util;

/**
 * The Class TimeUnits.
 */
public class TimeUnits extends DefaultUnits {
	public static final TimeUnits 
			day = new TimeUnits("day","day", 365.242), 
			year = new TimeUnits("year","yr", 1);
	
	/**
	 * Instantiates a new time units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	private TimeUnits(String name, String abbreviation,
			double scale) {
		super(name, abbreviation, scale);
	}
	
	/**
	 * Convert.
	 *
	 * @param value the value
	 * @param sourceUnits the source units
	 * @param targetUnits the target units
	 */
	public static double convert(double value, 
			TimeUnitsOutput source, TimeUnitsOutput target) {
		return DefaultUnits.convertStock(value, 
				source.getTimeUnits(), target.getTimeUnits());
	}
}
