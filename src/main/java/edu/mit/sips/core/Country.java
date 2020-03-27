package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSoS;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.DefaultAgricultureSoS;
import edu.mit.sips.core.agriculture.LocalAgricultureSoS;
import edu.mit.sips.core.electricity.DefaultElectricitySoS;
import edu.mit.sips.core.electricity.ElectricitySoS;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.electricity.LocalElectricitySoS;
import edu.mit.sips.core.petroleum.DefaultPetroleumSoS;
import edu.mit.sips.core.petroleum.LocalPetroleumSoS;
import edu.mit.sips.core.petroleum.PetroleumSoS;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.social.DefaultSocialSoS;
import edu.mit.sips.core.social.SocialSoS;
import edu.mit.sips.core.water.DefaultWaterSoS;
import edu.mit.sips.core.water.LocalWaterSoS;
import edu.mit.sips.core.water.WaterSoS;
import edu.mit.sips.core.water.WaterSystem;

/**
 * The Class Country.
 */
public class Country extends DefaultSociety implements Society {
	
	/**
	 * Builds the country.
	 *
	 * @param name the name
	 * @param initialFunds the initial funds
	 * @param nestedSocieties the nested societies
	 * @return the country
	 */
	public static Country buildCountry(String name, double initialFunds, 
			List<? extends Society> nestedSocieties) {
		AgricultureSoS agricultureSystem = new DefaultAgricultureSoS();
		// agriculture system is national if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
				agricultureSystem = new LocalAgricultureSoS();
				break;
			}
		}
		
		WaterSoS waterSystem = new DefaultWaterSoS();
		// water system is national if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getWaterSystem() instanceof WaterSystem.Local) {
				waterSystem = new LocalWaterSoS();
				break;
			}
		}
		
		ElectricitySoS electricitySystem = new DefaultElectricitySoS();
		// electricity system is national if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getElectricitySystem() instanceof ElectricitySystem.Local) {
				electricitySystem = new LocalElectricitySoS();
				break;
			}
		}
		
		PetroleumSoS petroleumSystem = new DefaultPetroleumSoS();
		// petroleum system is national if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getPetroleumSystem() instanceof PetroleumSystem.Local) {
				petroleumSystem = new LocalPetroleumSoS();
				break;
			}
		}

		// social system is always national
		SocialSoS socialSystem = new DefaultSocialSoS();
		
		return new Country(name, initialFunds, nestedSocieties, agricultureSystem, 
				waterSystem, petroleumSystem, electricitySystem, socialSystem);
	}
	
	private final double initialFunds;

	/**
	 * Instantiates a new country.
	 */
	public Country() {
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
	 * @param electricitySystem the electricity system
	 * @param petroleumSystem the petroleum system
	 * @param socialSystem the social system
	 */
	private Country(String name, double initialFunds, List<? extends Society> nestedSocieties,
			AgricultureSoS agricultureSystem, WaterSoS waterSystem,
			PetroleumSoS petroleumSystem, ElectricitySoS electricitySystem, 
			SocialSoS socialSystem) {
		super(name, nestedSocieties, agricultureSystem, 
				waterSystem, petroleumSystem, electricitySystem, socialSystem);
		
		// no need to validate initial funds
		this.initialFunds = initialFunds;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.SocialSystem#getCities()
	 */
	@Override
	public List<City> getCities() {
		List<City> cities = new ArrayList<City>();
		for(Society nestedSociety : getNestedSocieties()) {
			cities.addAll(nestedSociety.getCities());
		}
		return Collections.unmodifiableList(cities);
	}
	
	/**
	 * Gets the city.
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

	/**
	 * Gets the society.
	 *
	 * @param name the name
	 * @return the society
	 */
	public Society getSociety(String name) {
		return getSocietyRecursive(name, this);
	}
	
	/**
	 * Gets the funds.
	 *
	 * @return the funds
	 */
	public double getFunds() {
		return initialFunds + getCumulativeCashFlow();
	}
	
	/**
	 * Gets the society recursive.
	 *
	 * @param name the name
	 * @param root the root
	 * @return the society recursive
	 */
	private static Society getSocietyRecursive(String name, Society root) {
		if(root.getName().equals(name)) {
			return root;
		}
		for(Society child : root.getNestedSocieties()) {
			Society sociey = getSocietyRecursive(name, child);
			if(sociey != null) {
				return sociey;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getCountry()
	 */
	@Override
	public Country getCountry() {
		return this;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#initialize(int)
	 */
	@Override
	public void initialize(long time) {
		super.initialize(time); // initializes systems
		for(InfrastructureElement e : getInternalElements()) {
			e.initialize(time);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tick()
	 */
	@Override
	public void tick() {
		super.tick(); // ticks systems
		for(InfrastructureElement e : getInternalElements()) {
			e.tick();
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tock()
	 */
	@Override
	public void tock() {
		super.tock(); // tocks systems
		for(InfrastructureElement e : getInternalElements()) {
			e.tock();
		}
	}
	
	@Override
	public AgricultureSoS getAgricultureSystem() {
		return (AgricultureSoS) super.getAgricultureSystem();
	}
	
	@Override
	public WaterSoS getWaterSystem() {
		return (WaterSoS) super.getWaterSystem();
	}
	
	@Override
	public PetroleumSoS getPetroleumSystem() {
		return (PetroleumSoS) super.getPetroleumSystem();
	}
	
	@Override
	public ElectricitySoS getElectricitySystem() {
		return (ElectricitySoS) super.getElectricitySystem();
	}
	
	@Override
	public SocialSoS getSocialSystem() {
		return (SocialSoS) super.getSocialSystem();
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
	 * Gets the aquifer security score.
	 *
	 * @return the aquifer security score
	 */
	public double getAquiferSecurityScore() {
		return getWaterSystem().getAquiferSecurityScore();
	}
	
	/**
	 * Gets the reservoir security score.
	 *
	 * @return the reservoir security score
	 */
	public double getReservoirSecurityScore() {
		return getPetroleumSystem().getReservoirSecurityScore();
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
}
