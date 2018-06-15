package edu.mit.sips.sim.util2;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * The Class DefaultUnits.
 */
public class DefaultUnits<t extends ResourceType> implements Units<t> {
	private final String name;
	private final String abbreviation;
	private final BigDecimal scale;
	
	/**
	 * Instantiates a new default units.
	 */
	public DefaultUnits() {
		this("Datum","-",1);
	}
	
	/**
	 * Instantiates a new denominator units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	public DefaultUnits(String name, String abbreviation, double scale) {
		this(name, abbreviation, new BigDecimal(scale));
	}
	
	/**
	 * Instantiates a new default units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	protected DefaultUnits(String name, String abbreviation, BigDecimal scale) {
		this.name = name;
		this.abbreviation = abbreviation;
		this.scale = scale;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.Units#convertTo(edu.mit.sips.sim.util.Units, java.math.BigDecimal)
	 */
	@Override
	public BigDecimal convertTo(Units<t> units, BigDecimal value) {
		if(units instanceof DefaultUnits) {
			return value.divide(this.scale, MathContext.DECIMAL128).multiply(
					((DefaultUnits<t>) units).scale);
		} else {
			throw new IllegalArgumentException("Incompatible units type " 
					+ units.getClass().getSimpleName() 
					+ " (expected " + DefaultUnits.class.getSimpleName() + ")");
		}
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
	 * @see java.lang.Enum#toString()
	 */
	public String toString() { 
		return abbreviation; 
	}
}
