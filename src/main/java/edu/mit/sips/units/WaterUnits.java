/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sips.units;

/**
 * Implements a set of units for water resources.
 * 
 * @author Paul T. Grogan
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
