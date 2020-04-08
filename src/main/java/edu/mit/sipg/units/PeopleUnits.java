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
 * Implements a set of units for people resources.
 * 
 * @author Paul T. Grogan
 */
public class PeopleUnits extends DefaultUnits {
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
