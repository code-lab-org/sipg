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
package edu.mit.sipg.units;

/**
 * Implements a set of units for land area resources.
 * 
 * @author Paul T. Grogan
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
