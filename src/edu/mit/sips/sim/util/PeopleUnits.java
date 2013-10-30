package edu.mit.sips.sim.util;


/**
 * The Class PeopleUnits.
 */
public class PeopleUnits extends DefaultUnits {
	public static final PeopleUnits 
			p = new PeopleUnits("people","-", 1),  
			kP = new PeopleUnits("thousand people","thousands", 1e3),
			MP = new PeopleUnits("million people","millions", 1e6);

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
	
	/**
	 * Convert flow.
	 *
	 * @param value the value
	 * @param source the source
	 * @param target the target
	 * @return the double
	 */
	public static double convertFlow(double value, 
			PeopleUnitsOutput source, 
			PeopleUnitsOutput target) {
		return DefaultUnits.convertFlow(value, 
				source.getPeopleUnits(), source.getPeopleTimeUnits(), 
				target.getPeopleUnits(), target.getPeopleTimeUnits());
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
			PeopleUnitsOutput source, 
			PeopleUnitsOutput target) {
		return DefaultUnits.convertStock(value, 
				source.getPeopleUnits(), 
				target.getPeopleUnits());
	}
}
