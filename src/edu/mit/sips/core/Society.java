package edu.mit.sips.core;

import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.scenario.Sector;

/**
 * The Interface Society.
 */
public interface Society extends InfrastructureSystem, SimEntity {
	
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
	 * Sets the agriculture system.
	 *
	 * @param agricultureSystem the new agriculture system
	 */
	public void setAgricultureSystem(AgricultureSystem.Remote agricultureSystem);
	
	/**
	 * Sets the electricity system.
	 *
	 * @param electricitySystem the new electricity system
	 */
	public void setElectricitySystem(ElectricitySystem.Remote electricitySystem);
	
	/**
	 * Sets the petroleum system.
	 *
	 * @param petroluemSystem the new petroleum system
	 */
	public void setPetroleumSystem(PetroleumSystem.Remote petroluemSystem);
	
	/**
	 * Sets the social system.
	 *
	 * @param socialSystem the new social system
	 */
	public void setSocialSystem(SocialSystem.Remote socialSystem);
	
	/**
	 * Sets the water system.
	 *
	 * @param waterSystem the new water system
	 */
	public void setWaterSystem(WaterSystem.Remote waterSystem);
}
