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
 * Implements a set of units for currency resources.
 * 
 * @author Paul T. Grogan
 */
public class CurrencyUnits extends DefaultUnits {
	public static final CurrencyUnits 
			sim = new CurrencyUnits("Simoleon","\u00A7", 1e12), 
			ksim = new CurrencyUnits("thousand Simoleon","\u00A7k", 1e9), 
			Msim = new CurrencyUnits("million Simoleon","\u00A7M", 1e6),
			Bsim = new CurrencyUnits("billion Simoleon","\u00A7B", 1e3),
			Tsim = new CurrencyUnits("trillion Simoleon", "\u00A7T", 1);
	
	/**
	 * Instantiates a new currency units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	private CurrencyUnits(String name, String abbreviation, double scale) {
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
			CurrencyUnitsOutput source, 
			CurrencyUnitsOutput target) {
		return DefaultUnits.convertFlow(value, 
				source.getCurrencyUnits(), source.getCurrencyTimeUnits(), 
				target.getCurrencyUnits(), target.getCurrencyTimeUnits());
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
			CurrencyUnitsOutput source, 
			CurrencyUnitsOutput target) {
		return DefaultUnits.convertStock(value, 
				source.getCurrencyUnits(), 
				target.getCurrencyUnits());
	}
}
