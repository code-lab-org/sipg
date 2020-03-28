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
package edu.mit.sips.core.social.demand;

import edu.mit.sips.core.SimEntity;
import edu.mit.sips.core.Society;

/**
 * An interface to a demand model that quantifies resource consumption in a society.
 * 
 * @author Paul T. Grogan
 */
public interface DemandModel extends SimEntity {
	
	/**
	 * Gets the demand.
	 *
	 * @param society the society
	 * @return the demand
	 */
	public double getDemand(Society society);
}
