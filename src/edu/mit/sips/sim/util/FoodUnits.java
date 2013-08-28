package edu.mit.sips.sim.util;

/**
 * The Class AgricultureUnits.
 */
public abstract class FoodUnits {
	
	/**
	 * The Enum NumeratorUnits.
	 */
	public static enum NumeratorUnits implements Units {
		kcal("kilocalories","kcal"), 
		GJ("gigajoules","GJ");
		
		private final String name;
		private final String abbreviation;
		
		/**
		 * Instantiates a new numerator units.
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
	};
	
	/**
	 * The Enum DenominatorUnits.
	 */
	public static enum DenominatorUnits implements Units {
		day("day","day"), 
		year("year","year");
		
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
	private static final double kcal_GJ = 238902.957619;
	
	/**
	 * Convert.
	 *
	 * @param value the value
	 * @param source the source
	 * @param target the target
	 * @return the double
	 */
	public static double convert(double value, 
			FoodUnitsOutput source, 
			FoodUnitsOutput target) {
		return convert(value, 
				source.getFoodUnitsNumerator(), 
				source.getFoodUnitsDenominator(), 
				target.getFoodUnitsNumerator(), 
				target.getFoodUnitsDenominator());
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
		case GJ:
			switch(targetNumeratorUnits) {
			case GJ:
				factor *= 1;
				break;
			case kcal:
				factor *= kcal_GJ;
				break;
			}
			break;
		case kcal:
			switch(targetNumeratorUnits) {
			case GJ:
				factor /= kcal_GJ;
				break;
			case kcal:
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
