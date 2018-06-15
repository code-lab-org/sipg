package edu.mit.sips.sim.util2;

import edu.mit.sips.sim.util2.ResourceType.Currency;

/**
 * The Class CurrencyUnits.
 */
public class CurrencyUnits extends DefaultUnits<Currency> {
	public static final CurrencyUnits 
			sim = new CurrencyUnits("Simoleon","\u00A7", 1e12), 
			ksim = new CurrencyUnits("thousand Simoleon","\u00A7k", 1e9), 
			Msim = new CurrencyUnits("million Simoleon","\u00A7M", 1e6),
			Bsim = new CurrencyUnits("billion Simoleon","\u00A7B", 1e3),
			Tsim = new CurrencyUnits("trillion Simoleon", "\u00A7T", 1);
	
	/**
	 * Instantiates a new currency units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	private CurrencyUnits(String name, String abbreviation, double scale) {
		super(name, abbreviation, scale);
	}
}
