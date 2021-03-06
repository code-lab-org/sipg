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
package edu.mit.sipg.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sipg.core.agriculture.AgricultureSoS;
import edu.mit.sipg.core.agriculture.DefaultAgricultureSoS;
import edu.mit.sipg.core.electricity.DefaultElectricitySoS;
import edu.mit.sipg.core.electricity.ElectricitySoS;
import edu.mit.sipg.core.petroleum.DefaultPetroleumSoS;
import edu.mit.sipg.core.petroleum.PetroleumSoS;
import edu.mit.sipg.core.social.DefaultSocialSoS;
import edu.mit.sipg.core.social.SocialSoS;
import edu.mit.sipg.core.water.DefaultWaterSoS;
import edu.mit.sipg.core.water.WaterSoS;

/**
 * A region models an intermediate regional-level unit of a society.
 * 
 * @author Paul T. Grogan
 */
public class Region extends DefaultSociety {
	private AgricultureSoS agricultureSystem;
	private WaterSoS waterSystem;
	private ElectricitySoS electricitySystem;
	private PetroleumSoS petroleumSystem;
	private SocialSoS socialSystem;
	
	/**
	 * Instantiates a new region.
	 */
	public Region() {
		super("Region", Collections.unmodifiableList(new ArrayList<Society>()));
		agricultureSystem = new DefaultAgricultureSoS();
		agricultureSystem.setSociety(this);
		waterSystem = new DefaultWaterSoS();
		waterSystem.setSociety(this);
		petroleumSystem = new DefaultPetroleumSoS();
		petroleumSystem.setSociety(this);
		electricitySystem = new DefaultElectricitySoS();
		electricitySystem.setSociety(this);
		socialSystem = new DefaultSocialSoS();
		socialSystem.setSociety(this);
	}
	
	/**
	 * Instantiates a new region.
	 *
	 * @param name the name
	 * @param initialFunds the initial funds
	 * @param nestedSocieties the nested societies
	 * @param agricultureSystem the agriculture system
	 * @param waterSystem the water system
	 * @param petroleumSystem the petroleum system
	 * @param electricitySystem the electricity system
	 * @param socialSystem the social system
	 */
	private Region(String name, double initialFunds, List<? extends Society> nestedSocieties,
			AgricultureSoS agricultureSystem, WaterSoS waterSystem,
			PetroleumSoS petroleumSystem, ElectricitySoS electricitySystem, 
			SocialSoS socialSystem) {
		super(name, nestedSocieties);
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
	public List<City> getCities() {
		List<City> cities = new ArrayList<City>();
		for(Society nestedSociety : getNestedSocieties()) {
			cities.addAll(nestedSociety.getCities());
		}
		return Collections.unmodifiableList(cities);
	}
	
	@Override
	public Country getCountry() {
		if(getSociety() == null) {
			throw new IllegalStateException("Society cannot be null.");
		}
		return getSociety().getCountry();
	}
		
	@Override
	public AgricultureSoS getAgricultureSystem() {
		return this.agricultureSystem;
	}
	
	@Override
	public WaterSoS getWaterSystem() {
		return this.waterSystem;
	}
	
	@Override
	public PetroleumSoS getPetroleumSystem() {
		return this.petroleumSystem;
	}
	
	@Override
	public ElectricitySoS getElectricitySystem() {
		return this.electricitySystem;
	}
	
	@Override
	public SocialSoS getSocialSystem() {
		return this.socialSystem;
	}
}
