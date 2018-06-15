package edu.mit.sips.sim.util2;

import edu.mit.sips.sim.util2.ResourceType.People;

/**
 * The Class PeopleUnits.
 */
public class PeopleUnits extends DefaultUnits<People> {
	public static final PeopleUnits 
			p = new PeopleUnits("people","-", 1),  
			kP = new PeopleUnits("thousand people","thousands", 1e-3),
			MP = new PeopleUnits("million people","millions", 1e-6);

	/**
	 * Instantiates a new people units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	private PeopleUnits(String name, String abbreviation, double scale) {
		super(name, abbreviation, scale);
	}
}
