package edu.mit.sips.sim.util2;

import edu.mit.sips.sim.util2.ResourceType.Water;

/**
 * The Class WaterUnits.
 */
public class WaterUnits extends DefaultUnits<Water> {
	public static final WaterUnits 
	L = new WaterUnits("liters","L", 1e3),  
	m3 = new WaterUnits("cubic meters","m^3", 1),  
	MCM = new WaterUnits("million cubic meters","MCM", 1e-6),
	km3 = new WaterUnits("cubic kilometers","km^3", 1e-9);

	/**
	 * Instantiates a new water units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	private WaterUnits(String name, String abbreviation, double scale) {
		super(name, abbreviation, scale);
	}
}
