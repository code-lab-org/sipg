package edu.mit.sips.sim.util2;

import java.math.BigDecimal;

/**
 * The Class TimeDuration.
 */
public class TimeDuration implements Comparable<TimeDuration> {
	private final TimeUnits units;
	private final BigDecimal quantity;
	
	/**
	 * Instantiates a new time duration.
	 *
	 * @param units the units
	 * @param quantity the quantity
	 */
	protected TimeDuration(TimeUnits units, BigDecimal quantity) {
		this.units = units;
		this.quantity = quantity;
	}
	
	/**
	 * Instantiates a new time duration.
	 *
	 * @param units the units
	 * @param quantity the quantity
	 */
	public TimeDuration(TimeUnits units, double quantity) {
		this(units, new BigDecimal(quantity));
	}
	
	/**
	 * Adds the.
	 *
	 * @param duration the duration
	 * @return the time duration
	 */
	public TimeDuration add(TimeDuration duration) {
		return new TimeDuration(units, quantity.add(
				duration.getQuantityBD(units)));
	}
	
	/**
	 * Gets the quantity.
	 *
	 * @param units the units
	 * @return the quantity
	 */
	public double getQuantity(TimeUnits units) {
		return getQuantityBD(units).doubleValue();
	}
	
	/**
	 * Gets the quantity bd.
	 *
	 * @param units the units
	 * @return the quantity bd
	 */
	protected BigDecimal getQuantityBD(TimeUnits units) {
		return this.units.convertTo(units, quantity);
	}
	
	/**
	 * Gets the units.
	 *
	 * @return the units
	 */
	public TimeUnits getUnits() {
		return units;
	}

	/**
	 * Multiply.
	 *
	 * @param scalar the scalar
	 * @return the resource stock
	 */
	public TimeDuration multiply(double scalar) {
		return new TimeDuration(units, quantity.multiply(new BigDecimal(scalar)));
	}
	
	/**
	 * Subtract.
	 *
	 * @param resource the resource
	 * @return the resource flow
	 */
	public TimeDuration subtract(TimeDuration duration) {
		return new TimeDuration(units, quantity.subtract(
				duration.getQuantityBD(units)));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TimeDuration duration) {
		return quantity.compareTo(duration.getQuantityBD(units));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if(object instanceof TimeDuration) {
			return quantity.equals(((TimeDuration) object).getQuantityBD(units));
		} else {
			return false;
		}
	}
}
