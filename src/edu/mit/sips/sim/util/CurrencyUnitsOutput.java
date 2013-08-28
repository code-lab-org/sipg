package edu.mit.sips.sim.util;

/**
 * The Interface CurrencyUnitsOutput.
 */
public interface CurrencyUnitsOutput {
	
	/**
	 * Gets the currency units numerator.
	 *
	 * @return the currency units numerator
	 */
	public CurrencyUnits.NumeratorUnits getCurrencyUnitsNumerator();
	
	/**
	 * Gets the currency units denominator.
	 *
	 * @return the currency units denominator
	 */
	public CurrencyUnits.DenominatorUnits getCurrencyUnitsDenominator();
}
