package edu.mit.sips.sim.util2;

import edu.mit.sips.sim.util2.ResourceType.Land;

/**
 * The Class LandAreaUnits.
 */
public class LandUnits extends DefaultUnits<Land> {
	public static final LandUnits 
			ha = new LandUnits("hectares","ha", 1e2),  
			km2 = new LandUnits("square kilometers","km^2", 1),
			kkm2 = new LandUnits("thousand square kilometers","thousand km^2", 1e-3),
			Mkm2 = new LandUnits("million people","million km^2", 1e-6);

	/**
	 * Instantiates a new land area units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	private LandUnits(String name, String abbreviation, double scale) {
		super(name, abbreviation, scale);
	}
}
