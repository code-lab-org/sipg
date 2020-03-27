package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSoS;
import edu.mit.sips.core.agriculture.DefaultAgricultureSoS;
import edu.mit.sips.core.electricity.DefaultElectricitySoS;
import edu.mit.sips.core.electricity.ElectricitySoS;
import edu.mit.sips.core.petroleum.DefaultPetroleumSoS;
import edu.mit.sips.core.petroleum.PetroleumSoS;
import edu.mit.sips.core.social.DefaultSocialSoS;
import edu.mit.sips.core.social.SocialSoS;
import edu.mit.sips.core.water.DefaultWaterSoS;
import edu.mit.sips.core.water.WaterSoS;

/**
 * The Class Country.
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
	 * @param electricitySystem the electricity system
	 * @param petroleumSystem the petroleum system
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
