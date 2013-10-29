package edu.mit.sips.sim.util;

/**
 * The Class WaterUnits.
 */
public abstract class WaterUnits {
	
	/**
	 * The Enum NumeratorUnits.
	 */
	public static enum NumeratorUnits implements Units {
		L("liters","L", 1e-3),  
		m3("cubic meters","m^3", 1),  
		MCM("million cubic meters","MCM", 1e6),
		km3("cubic kilometers","kJ", 1e9);
		
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
			WaterUnitsOutput source, 
			WaterUnitsOutput target) {
		return convert(value, 
				source.getWaterUnitsNumerator(), 
				source.getWaterUnitsDenominator(), 
				target.getWaterUnitsNumerator(), 
				target.getWaterUnitsDenominator());
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
