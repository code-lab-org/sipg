package edu.mit.sips.sim.util2;

import java.math.BigDecimal;

/**
 * The Class TimePoint.
 */
public class TimePoint implements Comparable<TimePoint> {
	private final TimeUnits units;
	private final BigDecimal quantity;
	
	/**
	 * Instantiates a new time duration.
	 *
	 * @param units the units
	 * @param quantity the quantity
	 */
	protected TimePoint(TimeUnits units, BigDecimal quantity) {
		this.units = units;
		this.quantity = quantity;
	}
	
	/**
	 * Instantiates a new time duration.
	 *
	 * @param units the units
	 * @param quantity the quantity
	 */
	public TimePoint(TimeUnits units, double quantity) {
		this(units, new BigDecimal(quantity));
	}
	
	/**
	 * Adds the.
	 *
	 * @param duration the duration
	 * @return the time duration
	 */
	public TimePoint add(TimeDuration duration) {
		return new TimePoint(units, quantity.add(
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
	 * Subtract.
	 *
	 * @param duration the duration
	 * @return the resource flow
	 */
	public TimePoint subtract(TimeDuration duration) {
		return new TimePoint(units, quantity.subtract(
				duration.getQuantityBD(units)));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TimePoint time) {
		return quantity.compareTo(time.getQuantityBD(units));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if(object instanceof TimePoint) {
			return quantity.equals(((TimePoint) object).getQuantityBD(units));
		} else {
			return false;
		}
	}
}
