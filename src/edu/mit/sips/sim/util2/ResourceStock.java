package edu.mit.sips.sim.util2;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * The Class ResourceStock.
 *
 * @param <t> the generic type
 */
public class ResourceStock<t extends ResourceType> implements Comparable<ResourceStock<t>> {
	private final Units<t> units;
	private final BigDecimal quantity;
	
	/**
	 * Instantiates a new resource stock.
	 */
	public ResourceStock() {
		this(new DefaultUnits<t>(), 0);
	}
	
	/**
	 * Instantiates a new resource stock.
	 *
	 * @param units the units
	 * @param quantity the quantity
	 */
	protected ResourceStock(Units<t> units, BigDecimal quantity) {
		this.units = units;
		this.quantity = quantity;
	}
	
	/**
	 * Instantiates a new resource stock.
	 *
	 * @param units the units
	 * @param quantity the quantity
	 */
	public ResourceStock(Units<t> units, double quantity) {
		this(units, new BigDecimal(quantity));
	}

	/**
	 * Divide.
	 *
	 * @param resource the resource
	 * @return the double
	 */
	public double getFraction(ResourceStock<t> resource) {
		return quantity.divide(resource.getQuantityBD(units), MathContext.DECIMAL128).doubleValue();
	}
	
	/**
	 * Adds the.
	 *
	 * @param resource the resource
	 * @return the resource flow
	 */
	public ResourceStock<t> add(ResourceStock<t> resource) {
		return new ResourceStock<t>(units, quantity.add(
				resource.getQuantityBD(units)));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ResourceStock<t> resource) {
		return quantity.compareTo(resource.getQuantityBD(units));
	}
	
	/**
	 * Divide.
	 *
	 * @param time the time
	 * @return the resource flow
	 */
	public ResourceFlow<t> divide(TimeDuration time) {
		return new ResourceFlow<t>(units, time.getUnits(), quantity);
	}
	
	/**
	 * Divide.
	 *
	 * @param resource the resource
	 * @return the specific resource flow<t,? extends resource type>
	 */
	public <s extends ResourceType> SpecificResourceStock<t,s> divide(ResourceStock<s> resource) {
		return new SpecificResourceStock<t,s>(units, resource.getUnits(),
				quantity.divide(resource.getQuantityBD(resource.getUnits()), MathContext.DECIMAL128));
	}
	
	/**
	 * Divide.
	 *
	 * @param scalar the scalar
	 * @return the resource flow
	 */
	public ResourceStock<t> divide(double scalar) {
		return new ResourceStock<t>(units, 
				quantity.divide(new BigDecimal(scalar), MathContext.DECIMAL128));
	}
	
	/**
	 * Gets the quantity.
	 *
	 * @param units the units
	 * @return the quantity
	 */
	public double getQuantity(Units<t> units) {
		return getQuantityBD(units).doubleValue();
	}
	
	/**
	 * Gets the quantity bd.
	 *
	 * @param units the units
	 * @return the quantity bd
	 */
	protected BigDecimal getQuantityBD(Units<t> units) {
		return this.units.convertTo(units, quantity);
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
	 * @return the resource stock
	 */
	public ResourceStock<t> multiply(double scalar) {
		return new ResourceStock<t>(units, quantity.multiply(new BigDecimal(scalar)));
	}

	/**
	 * Subtract.
	 *
	 * @param resource the resource
	 * @return the resource flow
	 */
	public ResourceStock<t> subtract(ResourceStock<t> resource) {
		return new ResourceStock<t>(units, quantity.subtract(
				resource.getQuantityBD(units)));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return quantity + " " + units;
	}
}
