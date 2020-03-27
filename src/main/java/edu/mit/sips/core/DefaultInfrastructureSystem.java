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
		if(name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}
		this.name = name;
	}
	
	@Override
	public double getCapitalExpense() {
		return 0;
	}

	@Override
	public double getCashFlow() {
		return 0;
	}

	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}

	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	@Override
	public double getDomesticProduction() {
		return 0;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public final Society getSociety() {
		return society;
	}

	@Override
	public void initialize(long time) {
	}
	
	@Override
	public void setSociety(Society society) {
		this.society = society;
	}

	@Override
	public void tick() {
	}

	@Override
	public void tock() {
	}

	@Override
	public boolean isLocal() {
		return false;
	}
}
