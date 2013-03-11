package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.WaterSystem;

/**
 * The Class Country.
 */
public class Country extends DefaultSociety implements Society {
	private final Globals globals = new Globals();	
	private final double initialFunds;

	private double funds, nextFunds;

	/**
	 * Instantiates a new country.
	 */
	protected Country() {
		this.initialFunds = 0;
		
	}
	
	/**
	 * Instantiates a new country.
	 *
	 * @param name the name
	 * @param nestedSocieties the nested societies
	 */
	public Country(String name, List<Society> nestedSocieties,
			AgricultureSystem.Local agricultureSystem,
			WaterSystem.Local waterSystem,
			EnergySystem.Local energySystem,
			SocialSystem.Local socialSystem,
			double initialFunds) {
		super(name, nestedSocieties, agricultureSystem, 
				waterSystem, energySystem, socialSystem);
		
		// No validation needed for initial funds.
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
	 * @see edu.mit.sips.InfrastructureSystem#setSocialSystem(SocialSystem)
	 */
	@Override
	public void setSociety(Society society) {
		throw new IllegalArgumentException(
				"Country is the top-level society.");
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
		funds = initialFunds;
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
	
	/**
	 * Gets the internal elements.
	 *
	 * @return the internal elements
	 */
	private List<InfrastructureElement> getInternalElements() {
		List<InfrastructureElement> elements = 
				new ArrayList<InfrastructureElement>();
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			if(system instanceof InfrastructureSystem.Local) {
				// TODO, sort of an infinite loop here... 
				// need to store elements somewhere
				elements.addAll(((InfrastructureSystem.Local)system)
						.getInternalElements());
			}
		}
		return Collections.unmodifiableList(elements);
	}
}
