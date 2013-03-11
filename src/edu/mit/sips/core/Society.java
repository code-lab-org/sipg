package edu.mit.sips.core;

import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.WaterSystem;

public interface Society extends InfrastructureSoS {
	
	/**
	 * Gets the agriculture system.
	 *
	 * @return the agriculture system
	 */
	public AgricultureSystem getAgricultureSystem();
	
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
	 * Gets the energy system.
	 *
	 * @return the energy system
	 */
	public EnergySystem getEnergySystem();
	
	/**
	 * Gets the globals.
	 *
	 * @return the globals
	 */
	public Globals getGlobals();
	
	/**
	 * Gets the nested societies.
	 *
	 * @return the nested societies
	 */
	public List<? extends Society> getNestedSocieties();
	
	/**
	 * Gets the social system.
	 *
	 * @return the social system
	 */
	public SocialSystem getSocialSystem();
	
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
}
