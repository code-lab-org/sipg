package edu.mit.sips.sim.util2;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * The Class ResourceFlow.
 *
 * @param <t> the generic type
 */
public class ResourceFlow<t extends ResourceType> implements Comparable<ResourceFlow<t>> {
	private final Units<t> units;
	private final TimeUnits timeUnits;
	private final BigDecimal quantity;
	
	/**
	 * Instantiates a new resource stock.
	 */
	public ResourceFlow() {
		this(new DefaultUnits<t>(), TimeUnits.year, 0);
	}
	
	/**
	 * Instantiates a new resource flow.
	 *
	 * @param units the units
	 * @param timeUnits the time units
	 * @param quantity the quantity
	 */
	protected ResourceFlow(Units<t> units, TimeUnits timeUnits, BigDecimal quantity) {
		this.units = units;
		this.timeUnits = timeUnits;
		this.quantity = quantity;
	}
	
	/**
	 * Instantiates a new resource flow.
	 *
	 * @param units the units
	 * @param timeUnits the time units
	 * @param quantity the quantity
	 */
	public ResourceFlow(Units<t> units, TimeUnits timeUnits, double quantity) {
		this(units, timeUnits, new BigDecimal(quantity));
	}
	
	/**
	 * Adds the.
	 *
	 * @param resource the resource
	 * @return the resource flow
	 */
	public ResourceFlow<t> add(ResourceFlow<t> resource) {
		return new ResourceFlow<t>(units, timeUnits, 
				quantity.add(resource.getQuantityBD(units, timeUnits)));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ResourceFlow<t> resource) {
		return quantity.compareTo(resource.getQuantityBD(units, timeUnits));
	}
	
	/**
	 * Divide.
	 *
	 * @param resource the resource
	 * @return the specific resource flow<t,? extends resource type>
	 */
	public <s extends ResourceType> SpecificResourceFlow<t,s> divide(ResourceStock<s> resource) {
		return new SpecificResourceFlow<t,s>(units, timeUnits, resource.getUnits(),
				quantity.divide(resource.getQuantityBD(resource.getUnits()), MathContext.DECIMAL128));
	}
	
	/**
	 * Divide.
	 *
	 * @param resource the resource
	 * @return the specific resource flow<t,? extends resource type>
	 */
	public <s extends ResourceType> SpecificResourceStock<t,s> divide(ResourceFlow<s> resource) {
		return new SpecificResourceStock<t,s>(units, resource.getUnits(),
				quantity.divide(resource.getQuantityBD(resource.getUnits(), timeUnits), MathContext.DECIMAL128));
	}

	/**
	 * Divide.
	 *
	 * @param resource the resource
	 * @return the double
	 */
	public double getFraction(ResourceFlow<t> resource) {
		return quantity.divide(resource.getQuantityBD(units, timeUnits), MathContext.DECIMAL128).doubleValue();
	}
	
	/**
	 * Divide.
	 *
	 * @param scalar the scalar
	 * @return the resource flow
	 */
	public ResourceFlow<t> divide(double scalar) {
		return new ResourceFlow<t>(units, timeUnits, 
				quantity.divide(new BigDecimal(scalar), MathContext.DECIMAL128));
	}
	
	/**
	 * Gets the quantity.
	 *
	 * @param units the units
	 * @param timeUnits the time units
	 * @return the quantity
	 */
	public double getQuantity(Units<t> units, TimeUnits timeUnits) {
		return getQuantityBD(units, timeUnits).doubleValue();
	}
	
	/**
	 * Gets the quantity bd.
	 *
	 * @param units the units
	 * @param timeUnits the time units
	 * @return the quantity bd
	 */
	protected BigDecimal getQuantityBD(Units<t> units, TimeUnits timeUnits) {
		return this.units.convertTo(units, quantity).divide(
				this.timeUnits.convertTo(timeUnits, new BigDecimal(1)), MathContext.DECIMAL128);
	}
	
	/**
	 * Gets the units.
	 *
	 * @return the units
	 */
	public TimeUnits getTimeUnits() {
		return timeUnits;
	}
	
	/**
	 * Gets the units.
	 *
	 * @return the units
	 */
	public Units<t> getUnits() {
		return units;
	}
	
	/**
	 * Multiply.
	 *
	 * @param scalar the scalar
	 * @return the resource flow
	 */
	public ResourceFlow<t> multiply(double scalar) {
		return new ResourceFlow<t>(units, timeUnits, quantity.multiply(new BigDecimal(scalar)));
	}
	
	/**
	 * Multiply.
	 *
	 * @param time the time
	 * @return the resource stock
	 */
	public ResourceStock<t> multiply(TimeDuration time) {
		return new ResourceStock<t>(units, 
				quantity.multiply(time.getQuantityBD(timeUnits)));
	}

	/**
	 * Subtract.
	 *
	 * @param resource the resource
	 * @return the resource flow
	 */
	public ResourceFlow<t> subtract(ResourceFlow<t> resource) {
		return new ResourceFlow<t>(units, timeUnits, 
				quantity.subtract(resource.getQuantityBD(units, timeUnits)));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return quantity + " " + units + "/" + timeUnits;
	}
}
