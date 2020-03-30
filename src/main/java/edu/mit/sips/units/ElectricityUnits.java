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
 * Implements a set of units for electricity resources.
 * 
 * @author Paul T. Grogan
 */
public class ElectricityUnits extends DefaultUnits {
	public static final ElectricityUnits 
			Wh = new ElectricityUnits("watt-hours","kWh", 1),  
			kWh = new ElectricityUnits("kilowatt-hours","kWh", 1e-3),  
			MWh = new ElectricityUnits("megawatt-hours","MWh", 1e-6),  
			GWh = new ElectricityUnits("gigawatt-hours","GWh", 1e-9),  
			TWh = new ElectricityUnits("terawatt-hours","TWh", 1e-12);

	/**
	 * Instantiates a new electricity units.
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
