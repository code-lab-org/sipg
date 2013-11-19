package edu.mit.sips.sim.util;

/**
 * The Class OilUnits.
 */
public class OilUnits extends DefaultUnits {
	public static final OilUnits 
			toe = new OilUnits("tonnes of oil equivalent","toe", 1),
			ktoe = new OilUnits("thousand tonnes of oil equivalent","ktoe", 1e-3),
			Mtoe = new OilUnits("million tonnes of oil equivalent","Mtoe", 1e-6),
			Btoe = new OilUnits("billion tonnes of oil equivalent","Btoe", 1e-9),
			boe = new OilUnits("barrels of oil equivalent","boe", 7.33),
			kboe = new OilUnits("thousand barrels of oil equivalent","kboe", 7.33e-3),
			Mboe = new OilUnits("million barrels of oil equivalent","Mboe", 7.33e-6),
			Bboe = new OilUnits("billion barrels of oil equivalent","Bboe", 7.33e-9);

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