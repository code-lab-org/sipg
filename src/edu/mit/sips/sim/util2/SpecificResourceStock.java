package edu.mit.sips.sim.util2;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * The Class SpecificResourceFlow.
 *
 * @param <t> the generic type
 * @param <s> the generic type
 */
public class SpecificResourceStock<t extends ResourceType, s extends ResourceType> implements Comparable<SpecificResourceStock<t,s>> {
	private final Units<t> numeratorUnits;
	private final Units<s> denominatorUnits;
	private final BigDecimal quantity;
	
	/**
	 * Instantiates a new resource stock.
	 */
	public SpecificResourceStock() {
		this(new DefaultUnits<t>(), new DefaultUnits<s>(), 0);
	}
	
	/**
	 * Instantiates a new resource composite.
	 *
	 * @param numeratorUnits the numerator units
	 * @param denominatorUnits the denominator units
	 * @param quantity the quantity
	 */
	public SpecificResourceStock(Units<t> numeratorUnits,
			Units<s> denominatorUnits, double quantity) {
		this(numeratorUnits, denominatorUnits, new BigDecimal(quantity));
	}
	
	/**
	 * Instantiates a new resource composite.
	 *
	 * @param numeratorUnits the numerator units
	 * @param denominatorUnits the denominator units
	 * @param quantity the quantity
	 */
	protected SpecificResourceStock(Units<t> numeratorUnits, 
			Units<s> denominatorUnits, BigDecimal quantity) {
		this.numeratorUnits = numeratorUnits;
		this.denominatorUnits = denominatorUnits;
		this.quantity = quantity;
	}
	
	/**
	 * Gets the quantity.
	 *
	 * @param numeratorUnits the numerator units
	 * @param denominatorUnits the denominator units
	 * @return the quantity
	 */
	public double getQuantity(Units<t> numeratorUnits, Units<s> denominatorUnits) {
		return getQuantityBD(numeratorUnits, denominatorUnits).doubleValue();
	}

	/**
	 * Gets the quantity bd.
	 *
	 * @param numeratorUnits the numerator units
	 * @param denominatorUnits the denominator units
	 * @return the quantity bd
	 */
	protected BigDecimal getQuantityBD(Units<t> numeratorUnits, Units<s> denominatorUnits) {
		return this.numeratorUnits.convertTo(numeratorUnits, quantity)
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
	public SpecificResourceStock<t,s> add(SpecificResourceStock<t,s> resource) {
		return new SpecificResourceStock<t,s>(numeratorUnits, denominatorUnits, 
				quantity.add(resource.getQuantityBD(numeratorUnits, denominatorUnits)));
	}
	
	/**
	 * Divide.
	 *
	 * @param scalar the scalar
	 * @return the specific resource stock
	 */
	public SpecificResourceStock<t,s> divide(double scalar) {
		return new SpecificResourceStock<t,s>(numeratorUnits, denominatorUnits, 
				quantity.divide(new BigDecimal(scalar), MathContext.DECIMAL128));
	}
	
	/**
	 * Multiply.
	 *
	 * @param scalar the scalar
	 * @return the specific resource stock
	 */
	public SpecificResourceStock<t,s> multiply(double scalar) {
		return new SpecificResourceStock<t,s>(numeratorUnits, denominatorUnits, 
				quantity.multiply(new BigDecimal(scalar), MathContext.DECIMAL128));
	}
	
	/**
	 * Subtract.
	 *
	 * @param resource the resource
	 * @return the resource flow
	 */
	public SpecificResourceStock<t,s> subtract(SpecificResourceStock<t,s> resource) {
		return new SpecificResourceStock<t,s>(numeratorUnits, denominatorUnits, 
				quantity.subtract(resource.getQuantityBD(numeratorUnits, denominatorUnits)));
	}

	/**
	 * Multiply.
	 *
	 * @param <r> the generic type
	 * @param resource the resource
	 * @return the specific resource stock
	 */
	public <r extends ResourceType> SpecificResourceStock<r,s> multiply(SpecificResourceStock<r,t> resource) {
		return new SpecificResourceStock<r,s>(resource.getNumeratorUnits(), denominatorUnits, 
				quantity.multiply(resource.getQuantityBD(resource.getNumeratorUnits(), numeratorUnits)));
	}

	/**
	 * Multiply2.
	 *
	 * @param <r> the generic type
	 * @param resource the resource
	 * @return the specific resource stock
	 */
	public <r extends ResourceType> SpecificResourceStock<t,r> multiply2(SpecificResourceStock<s,r> resource) {
		return new SpecificResourceStock<t,r>(numeratorUnits, resource.getDenominatorUnits(), 
				quantity.multiply(resource.getQuantityBD(denominatorUnits, resource.getDenominatorUnits())));
	}
	
	/**
	 * Multiply.
	 *
	 * @param <r> the generic type
	 * @param resource the resource
	 * @return the specific resource stock
	 */
	public <r extends ResourceType> SpecificResourceFlow<r,s> multiply(SpecificResourceFlow<r,t> resource) {
		return new SpecificResourceFlow<r,s>(resource.getNumeratorUnits(), resource.getTimeUnits(), denominatorUnits, 
				quantity.multiply(resource.getQuantityBD(resource.getNumeratorUnits(), resource.getTimeUnits(), numeratorUnits)));
	}

	/**
	 * Multiply2.
	 *
	 * @param <r> the generic type
	 * @param resource the resource
	 * @return the specific resource stock
	 */
	public <r extends ResourceType> SpecificResourceFlow<t,r> multiply2(SpecificResourceFlow<s,r> resource) {
		return new SpecificResourceFlow<t,r>(numeratorUnits, resource.getTimeUnits(), resource.getDenominatorUnits(), 
				quantity.multiply(resource.getQuantityBD(denominatorUnits, resource.getTimeUnits(), resource.getDenominatorUnits())));
	}
	
	/**
	 * Multiply.
	 *
	 * @param resource the resource
	 * @return the resource stock
	 */
	public ResourceStock<t> multiply(ResourceStock<s> resource) {
		return new ResourceStock<t>(numeratorUnits,
				quantity.multiply(resource.getQuantityBD(denominatorUnits)));
	}
	
	/**
	 * Multiply.
	 *
	 * @param resource the resource
	 * @return the resource flow
	 */
	public ResourceFlow<t> multiply(ResourceFlow<s> resource) {
		return new ResourceFlow<t>(numeratorUnits, resource.getTimeUnits(),
				quantity.multiply(resource.getQuantityBD(denominatorUnits, resource.getTimeUnits())));
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(SpecificResourceStock<t,s> resource) {
		return quantity.compareTo(resource.getQuantityBD(
				numeratorUnits, denominatorUnits));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return quantity + " " + numeratorUnits + "/" + denominatorUnits;
	}
}
