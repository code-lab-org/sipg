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
 * Interface specifying the units used for water resources.
 * 
 * @author Paul T. Grogan
 */
public interface WaterUnitsOutput {
	
	/**
	 * Gets the water units.
	 *
	 * @return the water units
	 */
	public WaterUnits getWaterUnits();
	
	/**
	 * Gets the water time units.
	 *
	 * @return the water time units
	 */
	public TimeUnits getWaterTimeUnits();
}
