package edu.mit.sips.sim.util2;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * The Class SpecificResourceFlow.
 *
 * @param <t> the generic type
 * @param <s> the generic type
 */
public class SpecificResourceFlow<t extends ResourceType, s extends ResourceType> implements Comparable<SpecificResourceFlow<t,s>> {
	private final Units<t> numeratorUnits;
	private final TimeUnits timeUnits;
	private final Units<s> denominatorUnits;
	private final BigDecimal quantity;

	/**
	 * Instantiates a new specific resource flow.
	 */
	public SpecificResourceFlow() {
		this(new DefaultUnits<t>(), TimeUnits.year, new DefaultUnits<s>(), 0);
	}
	
	/**
	 * Instantiates a new resource composite.
	 *
	 * @param numeratorUnits the numerator units
	 * @param timeUnits the time units
	 * @param denominatorUnits the denominator units
	 * @param quantity the quantity
	 */
	public SpecificResourceFlow(Units<t> numeratorUnits, TimeUnits timeUnits,
			Units<s> denominatorUnits, double quantity) {
		this(numeratorUnits, timeUnits, denominatorUnits, new BigDecimal(quantity));
	}
	
	/**
	 * Instantiates a new resource composite.
	 *
	 * @param numeratorUnits the numerator units
	 * @param timeUnits the time units
	 * @param denominatorUnits the denominator units
	 * @param quantity the quantity
	 */
	protected SpecificResourceFlow(Units<t> numeratorUnits, 
			TimeUnits timeUnits, Units<s> denominatorUnits, BigDecimal quantity) {
		this.numeratorUnits = numeratorUnits;
		this.timeUnits = timeUnits;
		this.denominatorUnits = denominatorUnits;
		this.quantity = quantity;
	}
	
	/**
	 * Gets the quantity.
	 *
	 * @param numeratorUnits the numerator units
	 * @param timeUnits the time units
	 * @param denominatorUnits the denominator units
	 * @return the quantity
	 */
	public double getQuantity(Units<t> numeratorUnits, TimeUnits timeUnits, Units<s> denominatorUnits) {
		return getQuantityBD(numeratorUnits, timeUnits, denominatorUnits).doubleValue();
	}

	/**
	 * Gets the quantity bd.
	 *
	 * @param numeratorUnits the numerator units
	 * @param timeUnits the time units
	 * @param denominatorUnits the denominator units
	 * @return the quantity bd
	 */
	protected BigDecimal getQuantityBD(Units<t> numeratorUnits, TimeUnits timeUnits, Units<s> denominatorUnits) {
		return this.numeratorUnits.convertTo(numeratorUnits, quantity)
				.divide(this.timeUnits.convertTo(timeUnits, new BigDecimal(1)), MathContext.DECIMAL128)
				.divide(this.denominatorUnits.convertTo(denominatorUnits, new BigDecimal(1)), MathContext.DECIMAL128);
	}
	
	/**
	 * Gets the numerator units.
	 *
	 * @return the numerator units
	 */
	public Units<t> getNumeratorUnits() {
		return numeratorUnits;
	}
	
	/**
	 * Gets the time units.
	 *
	 * @return the time units
	 */
	public TimeUnits getTimeUnits() {
		return timeUnits;
	}
	
	/**
	 * Multiply.
	 *
	 * @param <r> the generic type
	 * @param resource the resource
	 * @return the specific resource stock
	 */
	public <r extends ResourceType> SpecificResourceFlow<r,s> multiply(SpecificResourceStock<r,t> resource) {
		return new SpecificResourceFlow<r,s>(resource.getNumeratorUnits(), timeUnits, denominatorUnits, 
				quantity.multiply(resource.getQuantityBD(resource.getNumeratorUnits(), numeratorUnits)));
	}

	/**
	 * Multiply2.
	 *
	 * @param <r> the generic type
	 * @param resource the resource
	 * @return the specific resource stock
	 */
	public <r extends ResourceType> SpecificResourceFlow<t,r> multiply2(SpecificResourceStock<s,r> resource) {
		return new SpecificResourceFlow<t,r>(numeratorUnits, timeUnits, resource.getDenominatorUnits(), 
				quantity.multiply(resource.getQuantityBD(denominatorUnits, resource.getDenominatorUnits())));
	}

	/**
	 * Gets the denominator units.
	 *
	 * @return the denominator units
	 */
	public Units<s> getDenominatorUnits() {
		return denominatorUnits;
	}
	
	/**
	 * Adds the.
	 *
	 * @param resource the resource
	 * @return the resource flow
	 */
	public SpecificResourceFlow<t,s> add(SpecificResourceFlow<t,s> resource) {
		return new SpecificResourceFlow<t,s>(numeratorUnits, timeUnits, denominatorUnits, 
				quantity.add(resource.getQuantityBD(numeratorUnits, timeUnits, denominatorUnits)));
	}
	
	/**
	 * Subtract.
	 *
	 * @param resource the resource
	 * @return the resource flow
	 */
	public SpecificResourceFlow<t,s> subtract(SpecificResourceFlow<t,s> resource) {
		return new SpecificResourceFlow<t,s>(numeratorUnits, timeUnits, denominatorUnits, 
				quantity.subtract(resource.getQuantityBD(numeratorUnits, timeUnits, denominatorUnits)));
	}
	
	/**
	 * Multiply.
	 *
	 * @param resource the resource
	 * @return the resource stock
	 */
	public ResourceFlow<t> multiply(ResourceStock<s> resource) {
		return new ResourceFlow<t>(numeratorUnits, timeUnits,
				quantity.multiply(resource.getQuantityBD(denominatorUnits)));
	}
	
	/**
	 * Divide.
	 *
	 * @param scalar the scalar
	 * @return the specific resource flow
	 */
	public SpecificResourceFlow<t,s> divide(double scalar) {
		return new SpecificResourceFlow<t,s>(numeratorUnits, timeUnits, denominatorUnits, 
				quantity.divide(new BigDecimal(scalar), MathContext.DECIMAL128));
	}

	/**
	 * Multiply.
	 *
	 * @param scalar the scalar
	 * @return the specific resource flow
	 */
	public SpecificResourceFlow<t,s> multiply(double scalar) {
		return new SpecificResourceFlow<t,s>(numeratorUnits, timeUnits, denominatorUnits, 
				quantity.multiply(new BigDecimal(scalar), MathContext.DECIMAL128));
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(SpecificResourceFlow<t,s> resource) {
		return quantity.compareTo(resource.getQuantityBD(
				numeratorUnits, timeUnits, denominatorUnits));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return quantity + " " + numeratorUnits + "/" + denominatorUnits + "/" + timeUnits;
	}
}
