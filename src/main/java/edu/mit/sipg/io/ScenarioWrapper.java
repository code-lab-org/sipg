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
package edu.mit.sipg.io;

import edu.mit.sipg.scenario.Scenario;

/**
 * A wrapper for the scenario class to address a "bug" (feature)
 * of GSON not recognizing interfaces for the top-level serialized class.
 * 
 * @author Paul T. Grogan
 */
public class ScenarioWrapper {
	public final Scenario scenario;
	
	/**
	 * Instantiates a new scenario wrapper.
	 *
	 * @param scenario the scenario
	 */
	public ScenarioWrapper(Scenario scenario) {
		this.scenario = scenario;
	}
}
