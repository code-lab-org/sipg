package edu.mit.sips.sim.util;


/**
 * The Class CurrencyUnits.
 */
public class CurrencyUnits {

	/**
	 * The Enum NumeratorUnits.
	 */
	public static enum NumeratorUnits implements Units {
		sim("Simoleon","\u00A7"), 
		ksim("thousand Simoleon","\u00A7k"), 
		Msim("million Simoleon","\u00A7M"),
		Bsim("billion Simoleon","\u00A7B"),
		Tsim("trillion Simoleon", "\u00A7T");
		
		private final String name;
		private final String abbreviation;
		
		/**
		 * Instantiates a new denominator units.
		 *
		 * @param name the name
		 * @param abbreviation the abbreviation
		 */
		private NumeratorUnits(String name, String abbreviation) {
			this.name = name;
			this.abbreviation = abbreviation;
		}

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		public String toString() { return abbreviation; }
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.Units#getName()
		 */
		public String getName() { return name; }
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.Units#getAbbreviation()
		 */
		public String getAbbreviation() { return abbreviation; }
	}
	
	/**
	 * The Enum DenominatorUnits.
	 */
	public static enum DenominatorUnits implements Units {
		day("day","day"), 
		year("year","yr");
		
		private final String name;
		private final String abbreviation;
		
		/**
		 * Instantiates a new denominator units.
		 *
		 * @param name the name
		 * @param abbreviation the abbreviation
		 */
		private DenominatorUnits(String name, String abbreviation) {
			this.name = name;
			this.abbreviation = abbreviation;
		}

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		public String toString() { return abbreviation; }
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.Units#getName()
		 */
		public String getName() { return name; }
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.Units#getAbbreviation()
		 */
		public String getAbbreviation() { return abbreviation; }
	};
	
	private static final double day_year = 365.242;
	private static final double sim_ksim = 1e3;
	private static final double sim_Msim = 1e6;
	private static final double sim_Bsim = 1e9;
	private static final double sim_Tsim = 1e12;
	private static final double ksim_Msim = 1e3;
	private static final double ksim_Bsim = 1e6;
	private static final double ksim_Tsim = 1e9;
	private static final double Msim_Bsim = 1e3;
	private static final double Msim_Tsim = 1e6;
	private static final double Bsim_Tsim = 1e3;
	
	/**
	 * Convert.
	 *
	 * @param value the value
	 * @param source the source
	 * @param target the target
	 * @return the double
	 */
	public static double convert(double value, 
			CurrencyUnitsOutput source, 
			CurrencyUnitsOutput target) {
		return convert(value, 
				source.getCurrencyUnitsNumerator(), 
				source.getCurrencyUnitsDenominator(), 
				target.getCurrencyUnitsNumerator(), 
				target.getCurrencyUnitsDenominator());
	}
	
	/**
	 * Convert.
	 *
	 * @param value the value
	 * @param sourceNumeratorUnits the source numerator units
	 * @param sourceDenominatorUnits the source denominator units
	 * @param targetNumeratorUnits the target numerator units
	 * @param targetDenominatorUnits the target denominator units
	 * @return the double
	 */
	public static double convert(double value, 
			NumeratorUnits sourceNumeratorUnits, 
			DenominatorUnits sourceDenominatorUnits, 
			NumeratorUnits targetNumeratorUnits, 
			DenominatorUnits targetDenominatorUnits) {
		double factor = 1;
		switch(sourceNumeratorUnits) {
		case sim:
			switch(targetNumeratorUnits) {
			case sim:
				factor *= 1;
				break;
			case ksim:
				factor /= sim_ksim;
				break;
			case Msim:
				factor /= sim_Msim;
				break;
			case Bsim:
				factor /= sim_Bsim;
				break;
			case Tsim:
				factor /= sim_Tsim;
				break;
			}
			break;
		case ksim:
			switch(targetNumeratorUnits) {
			case sim:
				factor *= sim_ksim;
				break;
			case ksim:
				factor *= 1;
				break;
			case Msim:
				factor /= ksim_Msim;
				break;
			case Bsim:
				factor /= ksim_Bsim;
				break;
			case Tsim:
				factor /= ksim_Tsim;
				break;
			}
			break;
		case Msim:
			switch(targetNumeratorUnits) {
			case sim:
				factor *= sim_Msim;
				break;
			case ksim:
				factor *= ksim_Msim;
				break;
			case Msim:
				factor *= 1;
				break;
			case Bsim:
				factor /= Msim_Bsim;
				break;
			case Tsim:
				factor /= Msim_Tsim;
				break;
			}
			break;
		case Bsim:
			switch(targetNumeratorUnits) {
			case sim:
				factor *= sim_Bsim;
				break;
			case ksim:
				factor *= ksim_Bsim;
				break;
			case Msim:
				factor *= Msim_Bsim;
				break;
			case Bsim:
				factor *= 1;
				break;
			case Tsim:
				factor /= Bsim_Tsim;
				break;
			}
			break;
		case Tsim:
			switch(targetNumeratorUnits) {
			case sim:
				factor *= sim_Tsim;
				break;
			case ksim:
				factor *= ksim_Tsim;
				break;
			case Msim:
				factor *= Msim_Tsim;
				break;
			case Bsim:
				factor *= Bsim_Tsim;
				break;
			case Tsim:
				factor *= 1;
				break;
			}
			break;
		}
		switch(sourceDenominatorUnits) {
		case day:
			switch(targetDenominatorUnits) {
			case day:
				factor *= 1;
				break;
			case year:
				factor /= day_year;
				break;
			}
			break;
		case year:
			switch(targetDenominatorUnits) {
			case day:
				factor /= day_year;
				break;
			case year:
				factor *= 1;
				break;
			}
			break;
		}
		return value * factor;
	}
}
