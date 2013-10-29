package edu.mit.sips.sim.util;

/**
 * The Class AgricultureUnits.
 */
public abstract class FoodUnits {
	
	/**
	 * The Enum NumeratorUnits.
	 */
	public static enum NumeratorUnits implements Units {
		kcal("kilocalories","kcal", 238902.957619),  
		J("joules","J", 1e9),
		kJ("kilojoules","kJ", 1e6),
		MJ("megajoules","MJ", 1e3),
		GJ("gigajoules","GJ", 1),
		TJ("terajoules","TJ",1e-3),
		EJ("exajoules","EJ",1e-6);
		
		private final String name;
		private final String abbreviation;
		private final double scale;
		
		/**
		 * Instantiates a new numerator units.
		 *
		 * @param name the name
		 * @param abbreviation the abbreviation
		 * @param scale the scale
		 */
		private NumeratorUnits(String name, String abbreviation, double scale) {
			this.name = name;
			this.abbreviation = abbreviation;
			this.scale = scale;
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
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.Units#getScale()
		 */
		public double getScale() { return scale; }
	};
	
	/**
	 * The Enum DenominatorUnits.
	 */
	public static enum DenominatorUnits implements Units {
		day("day","day", 365.242), 
		year("year","yr", 1);
		
		private final String name;
		private final String abbreviation;
		private final double scale;
		
		/**
		 * Instantiates a new denominator units.
		 *
		 * @param name the name
		 * @param abbreviation the abbreviation
		 * @param scale the scale
		 */
		private DenominatorUnits(String name, String abbreviation, double scale) {
			this.name = name;
			this.abbreviation = abbreviation;
			this.scale = scale;
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
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.Units#getScale()
		 */
		public double getScale() { return scale; }
	};
	
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
		return value / sourceNumeratorUnits.getScale() 
				* targetNumeratorUnits.getScale() 
				* sourceDenominatorUnits.getScale() 
				/ targetDenominatorUnits.getScale();
	}
}
