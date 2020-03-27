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
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSoS;
import edu.mit.sips.core.agriculture.DefaultAgricultureSoS;
import edu.mit.sips.core.agriculture.LocalAgricultureSoS;
import edu.mit.sips.core.electricity.DefaultElectricitySoS;
import edu.mit.sips.core.electricity.ElectricitySoS;
import edu.mit.sips.core.electricity.LocalElectricitySoS;
import edu.mit.sips.core.petroleum.DefaultPetroleumSoS;
import edu.mit.sips.core.petroleum.LocalPetroleumSoS;
import edu.mit.sips.core.petroleum.PetroleumSoS;
import edu.mit.sips.core.social.DefaultSocialSoS;
import edu.mit.sips.core.social.SocialSoS;
import edu.mit.sips.core.water.DefaultWaterSoS;
import edu.mit.sips.core.water.LocalWaterSoS;
import edu.mit.sips.core.water.WaterSoS;

/**
 * A country models the national-level unit of a society.
 * 
 * @author Paul T. Grogan
 */
public class Country extends DefaultSociety {
	/**
	 * Static method to build a country from a set of nested societies.
	 *
	 * @param name the country name
	 * @param initialFunds the initial funds
	 * @param nestedSocieties the nested societies
	 * @return the country
	 */
	public static Country buildCountry(String name, double initialFunds, 
			List<? extends Society> nestedSocieties) {
		// determine if local or remote sos is required for each sector
		AgricultureSoS agricultureSystem = new DefaultAgricultureSoS();
		for(Society society : nestedSocieties) {
			if(society.getAgricultureSystem().isLocal()) {
				agricultureSystem = new LocalAgricultureSoS();
				break;
			}
		}
		WaterSoS waterSystem = new DefaultWaterSoS();
		for(Society society : nestedSocieties) {
			if(society.getWaterSystem().isLocal()) {
				waterSystem = new LocalWaterSoS();
				break;
			}
		}
		ElectricitySoS electricitySystem = new DefaultElectricitySoS();
		for(Society society : nestedSocieties) {
			if(society.getElectricitySystem().isLocal()) {
				electricitySystem = new LocalElectricitySoS();
				break;
			}
		}
		PetroleumSoS petroleumSystem = new DefaultPetroleumSoS();
		for(Society society : nestedSocieties) {
			if(society.getPetroleumSystem().isLocal()) {
				petroleumSystem = new LocalPetroleumSoS();
				break;
			}
		}
		SocialSoS socialSystem = new DefaultSocialSoS();
		
		return new Country(name, initialFunds, nestedSocieties, agricultureSystem, 
				waterSystem, petroleumSystem, electricitySystem, socialSystem);
	}
	
	private AgricultureSoS agricultureSystem;
	private WaterSoS waterSystem;
	private ElectricitySoS electricitySystem;
	private PetroleumSoS petroleumSystem;
	private SocialSoS socialSystem;
	private final double initialFunds;
	
	/**
	 * Instantiates a new country.
	 */
	public Country() {
		super("Country", Collections.unmodifiableList(new ArrayList<Society>()));
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
		initialFunds = 0;
	}
	
	/**
	 * Instantiates a new country.
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
	private Country(String name, double initialFunds, List<? extends Society> nestedSocieties,
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
		this.initialFunds = initialFunds;
	}
	
	/**
	 * Gets the aggregated score.
	 *
	 * @param year the year
	 * @return the aggregated score
	 */
	public double getAggregatedScore(long year) {
		return (getFinancialSecurityScore(year) + getFoodSecurityScore() + getAquiferSecurityScore() + getReservoirSecurityScore())/4d;
	}

	@Override
	public AgricultureSoS getAgricultureSystem() {
		return this.agricultureSystem;
	}
	
	/**
	 * Gets the aquifer security score.
	 *
	 * @return the aquifer security score
	 */
	public double getAquiferSecurityScore() {
		return getWaterSystem().getAquiferSecurityScore();
	}
	
	@Override
	public List<City> getCities() {
		List<City> cities = new ArrayList<City>();
		for(Society nestedSociety : getNestedSocieties()) {
			cities.addAll(nestedSociety.getCities());
		}
		return Collections.unmodifiableList(cities);
	}

	/**
	 * Gets a city by name. Returns null if no matching city found.
	 *
	 * @param name the name
	 * @return the city
	 */
	public City getCity(String name) {
		for(City city : getCities()) {
			if(city.getName().equals(name)) {
				return city;
			}
		}
		return null;
	}

	@Override
	public Country getCountry() {
		return this;
	}

	@Override
	public ElectricitySoS getElectricitySystem() {
		return this.electricitySystem;
	}

	/**
	 * Gets the financial security score.
	 *
	 * @param year the year
	 * @return the financial security score
	 */
	public double getFinancialSecurityScore(long year) {
		double dystopiaTotal = -10e9;
		double utopiaTotal = 550e9;
		double growthRate = 0.04;
		
		double minValue = dystopiaTotal * (Math.pow(1+growthRate, year-1940) - 1)
				/ (Math.pow(1+growthRate, 2010-1940) - 1);
		double maxValue = utopiaTotal * (Math.pow(1+growthRate, year-1940) - 1)
				/ (Math.pow(1+growthRate, 2010-1940) - 1);
		
		if(this.getCumulativeCashFlow() < minValue) {
			return 0;
		} else if(this.getCumulativeCashFlow() > maxValue) {
			return 1000;
		} else {
			return 1000*(this.getCumulativeCashFlow() - minValue)/(maxValue - minValue);
		}
	}
	
	/**
	 * Gets the food security score.
	 *
	 * @return the food security score
	 */
	public double getFoodSecurityScore() {
		return getAgricultureSystem().getFoodSecurityScore();
	}
	
	/**
	 * Gets the funds.
	 *
	 * @return the funds
	 */
	public double getFunds() {
		return initialFunds + getCumulativeCashFlow();
	}
	
	@Override
	public PetroleumSoS getPetroleumSystem() {
		return this.petroleumSystem;
	}
	
	/**
	 * Gets the reservoir security score.
	 *
	 * @return the reservoir security score
	 */
	public double getReservoirSecurityScore() {
		return getPetroleumSystem().getReservoirSecurityScore();
	}
	
	@Override
	public SocialSoS getSocialSystem() {
		return this.socialSystem;
	}
	
	@Override
	public WaterSoS getWaterSystem() {
		return this.waterSystem;
	}
	
	@Override
	public void initialize(long time) {
		super.initialize(time);
		for(InfrastructureElement e : getInternalElements()) {
			e.initialize(time);
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		for(InfrastructureElement e : getInternalElements()) {
			e.tick();
		}
	}
	
	@Override
	public void tock() {
		super.tock();
		for(InfrastructureElement e : getInternalElements()) {
			e.tock();
		}
	}
}
