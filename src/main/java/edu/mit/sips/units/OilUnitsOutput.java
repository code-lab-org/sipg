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
 * Interface specifying the units used for oil resources.
 * 
 * @author Paul T. Grogan
 */
public interface OilUnitsOutput {
	
	/**
	 * Gets the oil units.
	 *
	 * @return the oil units
	 */
	public OilUnits getOilUnits();
	
	/**
	 * Gets the oil time units.
	 *
	 * @return the oil time units
	 */
	public TimeUnits getOilTimeUnits();
}
