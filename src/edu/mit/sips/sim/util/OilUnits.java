package edu.mit.sips.sim.util;

/**
 * The Class OilUnits.
 */
public class OilUnits extends DefaultUnits {
	public static final OilUnits 
			toe = new OilUnits("tonne of oil equivalent","toe", 1);

	/**
	 * Instantiates a new oil units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	private OilUnits(String name, String abbreviation,
			double scale) {
		super(name, abbreviation, scale);
	}

	/**
	 * Convert flow.
	 *
	 * @param value the value
	 * @param source the source
	 * @param target the target
	 * @return the double
	 */
	public static double convertFlow(double value, 
			OilUnitsOutput source, 
			OilUnitsOutput target) {
		return DefaultUnits.convertFlow(value, 
				source.getOilUnits(), source.getOilTimeUnits(), 
				target.getOilUnits(), target.getOilTimeUnits());
	}

	/**
	 * Convert stock.
	 *
	 * @param value the value
	 * @param source the source
	 * @param target the target
	 * @return the double
	 */
	public static double convertStock(double value, 
			OilUnitsOutput source, 
			OilUnitsOutput target) {
		return DefaultUnits.convertStock(value, 
				source.getOilUnits(), 
				target.getOilUnits());
	}
}
