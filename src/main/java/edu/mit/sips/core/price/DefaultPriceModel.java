package edu.mit.sips.core.price;

/**
 * The Class DefaultPriceModel.
 */
public class DefaultPriceModel implements PriceModel {

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) { }

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tick()
	 */
	@Override
	public void tick() { }

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tock()
	 */
	@Override
	public void tock() { }

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.price.PriceModel#getPrice(double)
	 */
	@Override
	public double getPrice(double amount) {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.price.PriceModel#getUnitPrice()
	 */
	@Override
	public double getUnitPrice() {
		return 0;
	}

}
