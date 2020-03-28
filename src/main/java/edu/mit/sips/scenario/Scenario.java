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
package edu.mit.sips.scenario;

import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.Country;

/**
 *  An interface to a design task.
 *  
 *  @author Paul T. Grogan
 */
public interface Scenario {
	
	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public Country getCountry();
	
	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public long getEndTime();
	
	/**
	 * Gets the present time.
	 *
	 * @return the present time
	 */
	public long getPresentTime();
	
	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public long getStartTime();

	/**
	 * Gets the template.
	 *
	 * @param name the name
	 * @return the template
	 */
	public ElementTemplate getTemplate(String name);
	
	/**
	 * Gets the templates.
	 *
	 * @return the templates
	 */
	public List<? extends ElementTemplate> getTemplates();
	
	/**
	 * Gets the templates.
	 *
	 * @param sectors the sectors
	 * @return the templates
	 */
	public List<? extends ElementTemplate> getTemplates(Collection<Sector> sectors);
	
	/**
	 * Checks if is remote panels displayed.
	 *
	 * @return true, if is remote panels displayed
	 */
	public boolean isRemotePanelsDisplayed();
	
	/**
	 * Checks if is team score displayed.
	 *
	 * @return true, if is team score displayed
	 */
	public boolean isTeamScoreDisplayed();
}
