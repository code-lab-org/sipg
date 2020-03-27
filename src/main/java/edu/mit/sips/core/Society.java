package edu.mit.sips.core;

import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
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
 * The Interface Society.
 */
public interface Society extends SimEntity, CurrencyUnitsOutput, 
		FoodUnitsOutput, ElectricityUnitsOutput, WaterUnitsOutput, 
		OilUnitsOutput {
	
	/**
	 * Gets the cumulative capital expense.
	 *
	 * @return the cumulative capital expense
	 */
	public double getCumulativeCapitalExpense();
	
	/**
	 * Gets the domestic product.
	 *
	 * @return the domestic product
	 */
	public double getDomesticProduct();
	
	/**
	 * Gets the agriculture system.
	 *
	 * @return the agriculture system
	 */
	public AgricultureSystem getAgricultureSystem();
	
	/**
	 * Gets the total cash flow.
	 *
	 * @return the total cash flow
	 */
	public double getTotalCashFlow();
	
	/**
	 * Gets the cumulative cash flow.
	 *
	 * @return the cumulative cash flow
	 */
	public double getCumulativeCashFlow();
	
	/**
	 * Gets the capital expense.
	 *
	 * @return the capital expense
	 */
	public double getTotalCapitalExpense();
	
	/**
	 * Gets the cities.
	 *
	 * @return the cities
	 */
	public List<City> getCities();

	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public Country getCountry();
	
	/**
	 * Gets the electricity system.
	 *
	 * @return the electricity system
	 */
	public ElectricitySystem getElectricitySystem();

	/**
	 * Gets the infrastructure systems.
	 *
	 * @return the infrastructure systems
	 */
	public List<? extends InfrastructureSystem> getInfrastructureSystems();
	
	/**
	 * Gets the internal elements.
	 *
	 * @return the internal elements
	 */
	public List<? extends InfrastructureElement> getInternalElements();
	
	/**
	 * Gets the local sectors.
	 *
	 * @return the local sectors
	 */
	public Collection<Sector> getLocalSectors();
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the nested societies.
	 *
	 * @return the nested societies
	 */
	public List<? extends Society> getNestedSocieties();
	
	/**
	 * Gets the petroleum system.
	 *
	 * @return the petroleum system
	 */
	public PetroleumSystem getPetroleumSystem();
	
	/**
	 * Gets the social system.
	 *
	 * @return the social system
	 */
	public SocialSystem getSocialSystem();
	
	/**
	 * Gets the societies.
	 *
	 * @return the societies
	 */
	public List<? extends Society> getSocieties();
	
	/**
	 * Gets the society.
	 *
	 * @return the society
	 */
	public Society getSociety();
	
	/**
	 * Gets the total electricity demand.
	 *
	 * @return the total electricity demand
	 */
	public double getTotalElectricityDemand();
	
	/**
	 * Gets the total food demand.
	 *
	 * @return the total food demand
	 */
	public double getTotalFoodDemand();
	
	/**
	 * Gets the total petroleum demand.
	 *
	 * @return the total petroleum demand
	 */
	public double getTotalPetroleumDemand();
	
	/**
	 * Gets the total water demand.
	 *
	 * @return the total water demand
	 */
	public double getTotalWaterDemand();
	
	/**
	 * Gets the water system.
	 *
	 * @return the water system
	 */
	public WaterSystem getWaterSystem();
	
	/**
	 * Sets the society.
	 *
	 * @param society the new society
	 */
	public void setSociety(Society society);
	
	/**
	 * Gets the population.
	 *
	 * @return the population
	 */
	public long getPopulation();
}
