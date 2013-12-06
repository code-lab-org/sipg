package edu.mit.sips.core;

import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class DefaultInfrastructureSystem.
 */
public class DefaultInfrastructureSystem implements InfrastructureSystem {
	protected static final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	protected static final TimeUnits currencyTimeUnits = TimeUnits.year;
	
	private String name;
	private transient Society society;
	
	/**
	 * Instantiates a new default infrastructure system.
	 */
	protected DefaultInfrastructureSystem() {
		this.name = "";
	}
	
	/**
	 * Instantiates a new default infrastructure system.
	 *
	 * @param name the name
	 */
	public DefaultInfrastructureSystem(String name) {
		// Validate name.
		if(name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getCashFlow()
	 */
	@Override
	public double getCashFlow() {
		return 0;
	}


	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsDenominator()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsNumerator()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getDomesticProduction()
	 */
	@Override
	public double getDomesticProduction() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getSociety()
	 */
	@Override
	public final Society getSociety() {
		return society;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Remote#setSociety(edu.mit.sips.core.Society)
	 */
	@Override
	public void setSociety(Society society) {
		this.society = society;
	}
}
