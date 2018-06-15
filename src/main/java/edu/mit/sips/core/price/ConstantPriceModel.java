package edu.mit.sips.core.price;

/**
 * The Class ConstantPriceModel.
 */
public class ConstantPriceModel implements PriceModel {
	private final double unitPrice;
	
	/**
	 * Instantiates a new constant price model.
	 */
	protected ConstantPriceModel() {
		unitPrice = 0;
	}
	
	/**
	 * Instantiates a new constant price model.
	 *
	 * @param unitPrice the unit price
	 */
	public ConstantPriceModel(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
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
	 * @see edu.mit.sips.core.price.PriceModel#getUnitPrice()
	 */
	@Override
	public double getUnitPrice() {
		return unitPrice;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.price.PriceModel#getPrice(double)
	 */
	public double getPrice(double amount) {
		return unitPrice * amount;
	}
}
