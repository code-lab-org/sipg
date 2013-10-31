package edu.mit.sips.sim.util;

/**
 * The Class WaterUnits.
 */
public class WaterUnits extends DefaultUnits {
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

		/**
		 * Convert flow.
		 *
		 * @param value the value
		 * @param source the source
		 * @param target the target
		 * @return the double
		 */
		public static double convertFlow(double value, 
				WaterUnitsOutput source, 
				WaterUnitsOutput target) {
			return DefaultUnits.convertFlow(value, 
					source.getWaterUnits(), source.getWaterTimeUnits(), 
					target.getWaterUnits(), target.getWaterTimeUnits());
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
				WaterUnitsOutput source, 
				WaterUnitsOutput target) {
			return DefaultUnits.convertStock(value, 
					source.getWaterUnits(), 
					target.getWaterUnits());
		}
}
