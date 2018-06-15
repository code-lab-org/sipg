package edu.mit.sips.sim.util;


/**
 * The Class ElectricityUnits.
 */
public class ElectricityUnits extends DefaultUnits {
	public static final ElectricityUnits 
			Wh = new ElectricityUnits("watt-hours","kWh", 1),  
			kWh = new ElectricityUnits("kilowatt-hours","kWh", 1e-3),  
			MWh = new ElectricityUnits("megawatt-hours","MWh", 1e-6),  
			GWh = new ElectricityUnits("gigawatt-hours","GWh", 1e-9),  
			TWh = new ElectricityUnits("terawatt-hours","TWh", 1e-12);

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
