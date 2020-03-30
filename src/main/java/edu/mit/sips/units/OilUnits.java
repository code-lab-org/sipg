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
 * Implements a set of units for oil resources.
 * 
 * @author Paul T. Grogan
 */
public class OilUnits extends DefaultUnits {
	public static final OilUnits 
			toe = new OilUnits("tonnes of oil equivalent","toe", 1),
			ktoe = new OilUnits("thousand tonnes of oil equivalent","ktoe", 1e-3),
			Mtoe = new OilUnits("million tonnes of oil equivalent","Mtoe", 1e-6),
			Btoe = new OilUnits("billion tonnes of oil equivalent","Btoe", 1e-9),
			boe = new OilUnits("barrels of oil equivalent","boe", 7.33),
			kboe = new OilUnits("thousand barrels of oil equivalent","kboe", 7.33e-3),
			Mboe = new OilUnits("million barrels of oil equivalent","Mboe", 7.33e-6),
			Bboe = new OilUnits("billion barrels of oil equivalent","Bboe", 7.33e-9);

	/**
	 * Instantiates a new oil units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	private OilUnits(String name, String abbreviation,
			double scale) {
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
			OilUnitsOutput source, 
			OilUnitsOutput target) {
		return DefaultUnits.convertFlow(value, 
				source.getOilUnits(), source.getOilTimeUnits(), 
				target.getOilUnits(), target.getOilTimeUnits());
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
			OilUnitsOutput source, 
			OilUnitsOutput target) {
		return DefaultUnits.convertStock(value, 
				source.getOilUnits(), 
				target.getOilUnits());
	}
}
