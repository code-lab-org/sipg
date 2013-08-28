package edu.mit.sips.sim.util;

/**
 * The Interface FoodUnitsOutput.
 */
public interface FoodUnitsOutput {
	
	/**
	 * Gets the food units numerator.
	 *
	 * @return the food units numerator
	 */
	public FoodUnits.NumeratorUnits getFoodUnitsNumerator();
	
	/**
	 * Gets the food units denominator.
	 *
	 * @return the food units denominator
	 */
	public FoodUnits.DenominatorUnits getFoodUnitsDenominator();
}
