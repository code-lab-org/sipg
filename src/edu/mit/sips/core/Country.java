package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.DefaultAgricultureSoS;
import edu.mit.sips.core.energy.DefaultEnergySoS;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.social.DefaultSocialSoS;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.DefaultWaterSoS;
import edu.mit.sips.core.water.WaterSystem;

/**
 * The Class Country.
 */
public class Country extends DefaultSociety implements Society {
	
	/**
	 * Builds the country.
	 *
	 * @param name the name
	 * @param nestedSocieties the nested societies
	 * @return the country
	 */
	public static Country buildCountry(String name, List<? extends Society> nestedSocieties) {
		AgricultureSystem agricultureSystem = new DefaultAgricultureSoS();
		// agriculture system is national if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
				agricultureSystem = new DefaultAgricultureSoS.Local();
				break;
			}
		}
		
		WaterSystem waterSystem = null;
		// water system is national if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getWaterSystem() instanceof WaterSystem.Local) {
				waterSystem = new DefaultWaterSoS.Local();
				break;
			}
		}
		
		EnergySystem energySystem = new DefaultEnergySoS();
		// energy system is national if there is a nested local system
		for(Society society : nestedSocieties) {
			if(society.getEnergySystem() instanceof EnergySystem.Local) {
				energySystem = new DefaultEnergySoS.Local();
				break;
			}
		}

		// social system is always national
		SocialSystem socialSystem = new DefaultSocialSoS();
		
		return new Country(name, nestedSocieties, agricultureSystem, 
				waterSystem, energySystem, socialSystem);
	}
	
	private final Globals globals = new Globals();	
	private double funds, nextFunds;

	/**
	 * Instantiates a new country.
	 */
	protected Country() {
		
	}
	
	/**
	 * Instantiates a new country.
	 *
	 * @param name the name
	 * @param nestedSocieties the nested societies
	 */
	private Country(String name, List<? extends Society> nestedSocieties,
			AgricultureSystem agricultureSystem, WaterSystem waterSystem,
			EnergySystem energySystem, SocialSystem socialSystem) {
		super(name, nestedSocieties, agricultureSystem, 
				waterSystem, energySystem, socialSystem);
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
	
	/**
	 * Gets the funds.
	 *
	 * @return the funds
	 */
	public double getFunds() {
		return funds;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getGlobals()
	 */
	@Override
	public Globals getGlobals() {
		return globals;
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
		funds = globals.getInitialFunds();
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
		nextFunds = funds + getCashFlow();
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
		funds = nextFunds;
	}
}
