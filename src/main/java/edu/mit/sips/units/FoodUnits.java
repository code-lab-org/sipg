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
 * Implements a set of units for food resources.
 * 
 * @author Paul T. Grogan
 */
public class FoodUnits extends DefaultUnits {
	public static final FoodUnits 
			kcal = new FoodUnits("kilocalories","kcal", 238902.957619),  
			J = new FoodUnits("joules","J", 1e9),
			kJ = new FoodUnits("kilojoules","kJ", 1e6),
			MJ = new FoodUnits("megajoules","MJ", 1e3),
			GJ = new FoodUnits("gigajoules","GJ", 1),
			TJ = new FoodUnits("terajoules","TJ",1e-3),
			EJ = new FoodUnits("exajoules","EJ",1e-6);

	/**
	 * Instantiates a new food units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	private FoodUnits(String name, String abbreviation, double scale) {
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
			FoodUnitsOutput source, 
			FoodUnitsOutput target) {
		return DefaultUnits.convertFlow(value, 
				source.getFoodUnits(), source.getFoodTimeUnits(), 
				target.getFoodUnits(), target.getFoodTimeUnits());
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
			FoodUnitsOutput source, 
			FoodUnitsOutput target) {
		return DefaultUnits.convertStock(value, 
				source.getFoodUnits(), 
				target.getFoodUnits());
	}
}
