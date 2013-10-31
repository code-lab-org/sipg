package edu.mit.sips.sim.util;


/**
 * The Class LandAreaUnits.
 */
public class LandAreaUnits extends DefaultUnits {
	public static final LandAreaUnits 
			ha = new LandAreaUnits("hectares","ha", 1e2),  
			km2 = new LandAreaUnits("square kilometers","km^2", 1),
			kkm2 = new LandAreaUnits("thousand square kilometers","thousand km^2", 1e-3),
			Mkm2 = new LandAreaUnits("million people","million km^2", 1e-6);

	/**
	 * Instantiates a new land area units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	private LandAreaUnits(String name, String abbreviation, double scale) {
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
			LandAreaUnitsOutput source, 
			LandAreaUnitsOutput target) {
		return DefaultUnits.convertFlow(value, 
				source.getLandAreaUnits(), source.getLandAreaTimeUnits(), 
				target.getLandAreaUnits(), target.getLandAreaTimeUnits());
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
			LandAreaUnitsOutput source, 
			LandAreaUnitsOutput target) {
		return DefaultUnits.convertStock(value, 
				source.getLandAreaUnits(), 
				target.getLandAreaUnits());
	}
}
