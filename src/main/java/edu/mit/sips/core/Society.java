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

import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.core.base.InfrastructureSystem;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.scenario.Sector;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.FoodUnitsOutput;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * A society defines a unit of analysis with dedicated infrastructure systems.
 * 
 * @author Paul T. Grogan
 */
public interface Society extends SimEntity, CurrencyUnitsOutput, 
		FoodUnitsOutput, ElectricityUnitsOutput, WaterUnitsOutput, 
		OilUnitsOutput {
	
	/**
	 * Gets the agriculture system for this society.
	 *
	 * @return the agriculture system
	 */
	public AgricultureSystem getAgricultureSystem();
	
	/**
	 * Gets the cities contained within this society.
	 *
	 * @return the cities
	 */
	public List<City> getCities();
	
	/**
	 * Gets the country associated with this society.
	 *
	 * @return the country
	 */
	public Country getCountry();
	
	/**
	 * Gets the cumulative capital expense of infrastructure 
	 * elements in this society.
	 *
	 * @return the cumulative capital expense
	 */
	public double getCumulativeCapitalExpense();
	
	/**
	 * Gets the cumulative cash flow from infrastructure 
	 * elements in this society.
	 *
	 * @return the cumulative cash flow
	 */
	public double getCumulativeCashFlow();
	
	/**
	 * Gets the electricity system for this society.
	 *
	 * @return the electricity system
	 */
	public ElectricitySystem getElectricitySystem();

	/**
	 * Gets the list of infrastructure systems for this society.
	 *
	 * @return the infrastructure systems
	 */
	public List<? extends InfrastructureSystem> getInfrastructureSystems();
	
	/**
	 * Gets the list of infrastructure elements inside this society.
	 *
	 * @return the internal elements
	 */
	public List<? extends InfrastructureElement> getInternalElements();

	/**
	 * Gets the collection of sectors with local control in this society.
	 *
	 * @return the local sectors
	 */
	public Collection<Sector> getLocalSectors();
	
	/**
	 * Gets the name of this society.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the list of nested societies.
	 *
	 * @return the nested societies
	 */
	public List<? extends Society> getNestedSocieties();
	
	/**
	 * Gets the petroleum system for this society.
	 *
	 * @return the petroleum system
	 */
	public PetroleumSystem getPetroleumSystem();
	
	/**
	 * Gets the population of this society.
	 *
	 * @return the population
	 */
	public long getPopulation();
	
	/**
	 * Gets the social system for this society.
	 *
	 * @return the social system
	 */
	public SocialSystem getSocialSystem();
	
	/**
	 * Gets the complete list of all societies within this society 
	 * (at any level of nesting).
	 *
	 * @return the societies
	 */
	public List<? extends Society> getSocieties();
	
	/**
	 * Gets the parent society.
	 *
	 * @return the society
	 */
	public Society getSociety();
	
	/**
	 * Gets the current capital expense incurred in this society.
	 *
	 * @return the capital expense
	 */
	public double getTotalCapitalExpense();
	
	/**
	 * Gets the current total cash flow in this society.
	 *
	 * @return the total cash flow
	 */
	public double getTotalCashFlow();
	
	/**
	 * Gets the current total electricity demand of this society.
	 *
	 * @return the total electricity demand
	 */
	public double getTotalElectricityDemand();
	
	/**
	 * Gets the current total food demand in this society.
	 *
	 * @return the total food demand
	 */
	public double getTotalFoodDemand();
	
	/**
	 * Gets the current total petroleum demand in this society.
	 *
	 * @return the total petroleum demand
	 */
	public double getTotalPetroleumDemand();
	
	/**
	 * Gets the current total water demand in this society.
	 *
	 * @return the total water demand
	 */
	public double getTotalWaterDemand();
	
	/**
	 * Gets the water system for this society.
	 *
	 * @return the water system
	 */
	public WaterSystem getWaterSystem();
	
	/**
	 * Sets the parent society.
	 *
	 * @param society the new society
	 */
	public void setSociety(Society society);
}
