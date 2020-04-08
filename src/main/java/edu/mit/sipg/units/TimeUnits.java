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
 * Implements a set of units for time resources.
 * 
 * @author Paul T. Grogan
 */
public class TimeUnits extends DefaultUnits {
	public static final TimeUnits 
			day = new TimeUnits("day","day", 365.242), 
			year = new TimeUnits("year","yr", 1);
	
	/**
	 * Instantiates a new time units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	private TimeUnits(String name, String abbreviation,
			double scale) {
		super(name, abbreviation, scale);
	}
	
	/**
	 * Convert.
	 *
	 * @param value the value
	 * @param source the source
	 * @param target the target
	 * @return the double
	 */
	public static double convert(double value, 
			TimeUnitsOutput source, TimeUnitsOutput target) {
		return DefaultUnits.convertStock(value, 
				source.getTimeUnits(), target.getTimeUnits());
	}
}
