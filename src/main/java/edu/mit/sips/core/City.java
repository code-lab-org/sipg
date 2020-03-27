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
package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.core.electricity.DefaultElectricitySystem;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.petroleum.DefaultPetroleumSystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.DefaultWaterSystem;
import edu.mit.sips.core.water.WaterSystem;

/**
 * A city models the most detailed local-level unit of society.
 * 
 * @author Paul T. Grogan
 */
public class City extends DefaultSociety {
	private AgricultureSystem agricultureSystem;
	private WaterSystem waterSystem;
	private ElectricitySystem electricitySystem;
	private PetroleumSystem petroleumSystem;
	private SocialSystem socialSystem;
	
	/**
	 * Instantiates a new city.
	 */
	protected City() { 
		super("Society", Collections.unmodifiableList(new ArrayList<Society>()));
		this.agricultureSystem = new DefaultAgricultureSystem();
		agricultureSystem.setSociety(this);
		this.waterSystem = new DefaultWaterSystem();
		waterSystem.setSociety(this);
		this.petroleumSystem = new DefaultPetroleumSystem();
		petroleumSystem.setSociety(this);
		this.electricitySystem = new DefaultElectricitySystem();
		electricitySystem.setSociety(this);
		this.socialSystem = new DefaultSocialSystem();
		socialSystem.setSociety(this);
	}
	
	/**
	 * Instantiates a new city.
	 *
	 * @param name the name
	 * @param agricultureSystem the agriculture system
	 * @param waterSystem the water system
	 * @param petroleumSystem the petroleum system
	 * @param electricitySystem the electricity system
	 * @param socialSystem the social system
	 */
	public City(String name, AgricultureSystem agricultureSystem,
			WaterSystem waterSystem, PetroleumSystem petroleumSystem,
			ElectricitySystem electricitySystem, SocialSystem socialSystem) {
		super(name, Collections.unmodifiableList(new ArrayList<Society>()));
		this.agricultureSystem = agricultureSystem;
		this.agricultureSystem.setSociety(this);
		this.waterSystem = waterSystem;
		this.waterSystem.setSociety(this);
		this.electricitySystem = electricitySystem;
		this.electricitySystem.setSociety(this);
		this.petroleumSystem = petroleumSystem;
		this.petroleumSystem.setSociety(this);
		this.socialSystem = socialSystem;
		this.socialSystem.setSociety(this);
	}
	
	@Override
	public AgricultureSystem getAgricultureSystem() {
		return agricultureSystem;
	}

	@Override
	public List<City> getCities() {
		return Collections.unmodifiableList(Arrays.asList(this));
	}

	@Override
	public Country getCountry() {
		if(getSociety() == null) {
			throw new IllegalStateException("Society cannot be null.");
		}
		return getSociety().getCountry();
	}

	@Override
	public ElectricitySystem getElectricitySystem() {
		return electricitySystem;
	}

	@Override
	public PetroleumSystem getPetroleumSystem() {
		return petroleumSystem;
	}

	@Override
	public SocialSystem getSocialSystem() {
		return socialSystem;
	}

	@Override
	public WaterSystem getWaterSystem() {
		return waterSystem;
	}

	/**
	 * Sets the agriculture system.
	 *
	 * @param agricultureSystem the new agriculture system
	 */
	public void setAgricultureSystem(AgricultureSystem agricultureSystem) {
		agricultureSystem.setSociety(this);
		this.agricultureSystem = agricultureSystem;
	}

	/**
	 * Sets the electricity system.
	 *
	 * @param electricitySystem the new electricity system
	 */
	public void setElectricitySystem(ElectricitySystem electricitySystem) {
		electricitySystem.setSociety(this);
		this.electricitySystem = electricitySystem;
	}

	/**
	 * Sets the petroleum system.
	 *
	 * @param petroleumSystem the new petroleum system
	 */
	public void setPetroleumSystem(PetroleumSystem petroleumSystem) {
		petroleumSystem.setSociety(this);
		this.petroleumSystem = petroleumSystem;
	}

	/**
	 * Sets the social system.
	 *
	 * @param socialSystem the new social system
	 */
	public void setSocialSystem(SocialSystem socialSystem) {
		socialSystem.setSociety(this);
		this.socialSystem = socialSystem;
	}

	/**
	 * Sets the water system.
	 *
	 * @param waterSystem the new water system
	 */
	public void setWaterSystem(WaterSystem waterSystem) {
		waterSystem.setSociety(this);
		this.waterSystem = waterSystem;
	}
}
