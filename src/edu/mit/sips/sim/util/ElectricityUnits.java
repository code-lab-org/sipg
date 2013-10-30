package edu.mit.sips.sim.util;


/**
 * The Class ElectricityUnits.
 */
public class ElectricityUnits extends DefaultUnits {
	public static final ElectricityUnits 
			kWh = new ElectricityUnits("kilowatt-hour","kWh", 1),  
			MWh = new ElectricityUnits("megawatt-hour","MWh", 1e6);

	/**
	 * Instantiates a new numerator units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	private ElectricityUnits(String name, String abbreviation, double scale) {
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
			ElectricityUnitsOutput source, 
			ElectricityUnitsOutput target) {
		return DefaultUnits.convertFlow(value, 
				source.getElectricityUnits(), source.getElectricityTimeUnits(), 
				target.getElectricityUnits(), target.getElectricityTimeUnits());
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
			ElectricityUnitsOutput source, 
			ElectricityUnitsOutput target) {
		return DefaultUnits.convertStock(value, 
				source.getElectricityUnits(), 
				target.getElectricityUnits());
	}
}
