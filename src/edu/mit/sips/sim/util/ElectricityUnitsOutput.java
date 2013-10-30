package edu.mit.sips.sim.util;

/**
 * The Interface ElectricityUnitsOutput.
 */
public interface ElectricityUnitsOutput {
	
	/**
	 * Gets the electricity units numerator.
	 *
	 * @return the electricity units numerator
	 */
	public ElectricityUnits.NumeratorUnits getElectricityUnitsNumerator();
	
	/**
	 * Gets the electricity units denominator.
	 *
	 * @return the electricity units denominator
	 */
	public ElectricityUnits.DenominatorUnits getElectricityUnitsDenominator();
}
