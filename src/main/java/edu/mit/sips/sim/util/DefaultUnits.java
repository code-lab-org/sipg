package edu.mit.sips.sim.util;

/**
 * The Class DefaultUnits.
 */
public abstract class DefaultUnits implements Units {
	/**
	 * The Class DenominatorUnits.
	 */
	public static abstract class DenominatorUnits extends DefaultUnits {
		/**
		 * Instantiates a new denominator units.
		 *
		 * @param name the name
		 * @param abbreviation the abbreviation
		 * @param scale the scale
		 */
		protected DenominatorUnits(String name, String abbreviation, double scale) {
			super(name, abbreviation, scale);
		}
	}
	
	/**
	 * The Class NumeratorUnits.
	 */
	public static abstract class NumeratorUnits extends DefaultUnits {
		
		/**
		 * Instantiates a new numerator units.
		 *
		 * @param name the name
		 * @param abbreviation the abbreviation
		 * @param scale the scale
		 */
		protected NumeratorUnits(String name, String abbreviation, double scale) {
			super(name, abbreviation, scale);
		}
	}
	
	/**
	 * Convert flow.
	 *
	 * @param value the value
	 * @param sourceUnits the source units
	 * @param sourceTimeUnits the source time units
	 * @param targetUnits the target units
	 * @param targetTimeUnits the target time units
	 * @return the double
	 */
	public static double convertFlow(double value, 
			DefaultUnits sourceUnits, 
			TimeUnits sourceTimeUnits, 
			DefaultUnits targetUnits, 
			TimeUnits targetTimeUnits) {
		return convertStock(value, sourceUnits, targetUnits) 
				/ convertStock(1, sourceTimeUnits, targetTimeUnits);
	}
	
	/**
	 * Convert stock.
	 *
	 * @param value the value
	 * @param sourceUnits the source units
	 * @param targetUnits the target units
	 * @return the double
	 */
	public static double convertStock(double value, 
			DefaultUnits sourceUnits, 
			DefaultUnits targetUnits) {
		return value / sourceUnits.getScale() 
				* targetUnits.getScale();
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
			DefaultUnits sourceNumeratorUnits, 
			DefaultUnits sourceDenominatorUnits, 
			DefaultUnits targetNumeratorUnits, 
			DefaultUnits targetDenominatorUnits) {
		return value / sourceNumeratorUnits.getScale() 
				* sourceDenominatorUnits.getScale() 
				* targetNumeratorUnits.getScale()
				/ targetDenominatorUnits.getScale();
	}
	
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
	protected DefaultUnits(String name, String abbreviation, double scale) {
		this.name = name;
		this.abbreviation = abbreviation;
		this.scale = scale;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.Units#getAbbreviation()
	 */
	public String getAbbreviation() { 
		return abbreviation; 
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.Units#getName()
	 */
	public String getName() { 
		return name; 
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.Units#getScale()
	 */
	public double getScale() { 
		return scale; 
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() { 
		return abbreviation; 
	}
}
