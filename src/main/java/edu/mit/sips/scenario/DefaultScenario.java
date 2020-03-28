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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.Country;

/**
 * The default implementation of the scenario interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultScenario implements Scenario {
	private final Country country;
	private final List<? extends ElementTemplate> templates;
	private final boolean displayTeamScore;
	private final boolean displayRemotePanels;
	private final long startTime;
	private final long endTime;
	
	/**
	 * Instantiates a new default scenario.
	 */
	public DefaultScenario() {
		this(new Country(), new ArrayList<ElementTemplate>(), 0, 0, true, false);
	}
	
	/**
	 * Instantiates a new default scenario.
	 *
	 * @param country the country
	 * @param templates the templates
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param displayTeamScore the display team score
	 * @param isRemotePanelsDisplayed the is remote panels displayed
	 */
	public DefaultScenario(Country country, 
			List<? extends ElementTemplate> templates, 
			long startTime, long endTime,
			boolean displayTeamScore, boolean isRemotePanelsDisplayed) {
		this.displayTeamScore = displayTeamScore;
		this.displayRemotePanels = isRemotePanelsDisplayed;
		this.country = country;
		this.templates = templates;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	@Override
	public Country getCountry() {
		return country;
	}

	@Override
	public long getEndTime() {
		return endTime;
	}

	@Override
	public long getStartTime() {
		return startTime;
	}

	@Override
	public ElementTemplate getTemplate(String name) {
		for(ElementTemplate template : templates) {
			if(template.getName().equals(name)) {
				return template;
			}
		}
		return null;
	}

	@Override
	public List<? extends ElementTemplate> getTemplates() {
		return new ArrayList<ElementTemplate>(templates);
	}

	@Override
	public List<? extends ElementTemplate> getTemplates(Collection<Sector> sectors) {
		List<ElementTemplate> filteredTemplates = new ArrayList<ElementTemplate>();
		for(ElementTemplate template : getTemplates()) {
			if(sectors.contains(template.getSector())) {
				filteredTemplates.add(template);
			}
		}
		return filteredTemplates;
	}

	@Override
	public boolean isRemotePanelsDisplayed() {
		return displayRemotePanels;
	}

	@Override
	public boolean isTeamScoreDisplayed() {
		return displayTeamScore;
	}
}
