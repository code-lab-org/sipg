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
package edu.mit.sips.core.social;

import edu.mit.sips.core.base.InfrastructureSystem;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.FoodUnitsOutput;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * An interface to social infrastructure systems.
 * 
 * @author Paul T. Grogan
 */
public interface SocialSystem extends InfrastructureSystem, 
		FoodUnitsOutput, WaterUnitsOutput, ElectricityUnitsOutput, OilUnitsOutput {
	
	/**
	 * An interface to locally-controlled social infrastructure 
	 * systems which provide greater details.
	 * 
	 * @author Paul T. Grogan
	 */
	public static interface Local extends SocialSystem, InfrastructureSystem.Local { }

	/**
	 * Gets the electricity consumption.
	 *
	 * @return the electricity consumption
	 */
	public double getElectricityConsumption();

	/**
	 * Gets the food consumption.
	 *
	 * @return the food consumption
	 */
	public double getFoodConsumption();

	/**
	 * Gets the petroleum consumption.
	 *
	 * @return the petroleum consumption
	 */
	public double getPetroleumConsumption();
	
	/**
	 * Gets the population.
	 *
	 * @return the population
	 */
	public long getPopulation();
	
	/**
	 * Gets the water consumption.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
}
