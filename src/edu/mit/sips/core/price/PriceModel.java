package edu.mit.sips.core.price;

import edu.mit.sips.core.SimEntity;

/**
 * The Interface PriceModel.
 */
public interface PriceModel extends SimEntity {
	
	/**
	 * Gets the price.
	 *
	 * @param amount the amount
	 * @return the price
	 */
	public double getPrice(double amount);
	
	/**
	 * Gets the unit price.
	 *
	 * @return the unit price
	 */
	public double getUnitPrice();
}
