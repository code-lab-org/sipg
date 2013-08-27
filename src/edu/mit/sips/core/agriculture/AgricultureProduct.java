package edu.mit.sips.core.agriculture;

/**
 * The Enum AgricultureProduct.
 */
public enum AgricultureProduct {
	NONE,
	LIVESTOCK("Lifestock", 				150, 	15000, 	500, 	15),
	DATES("Dates", 						400, 	42000, 	1000, 	50),
	GRAINS("Grains", 					2000, 	140000, 16666,	20),
	MODIFIED_GRAINS("Modified Grains", 	2000, 	140000, 15833,	20);
	
	private final String name;
	private final double foodIntensityOfLandUsed; 		// kcal/km^2
	private final double costIntensityOfLandUsed;		// SAR/km^2
	private final double waterIntensityOfLandUsed;		// m^3/km^2
	private final double laborIntensityOfLandUsed;		// person/km^2
	
	/**
	 * Instantiates a new agriculture product.
	 */
	private AgricultureProduct() {
		this.name = "None";
		this.foodIntensityOfLandUsed = 0;
		this.costIntensityOfLandUsed = 0;
		this.waterIntensityOfLandUsed = 0;
		this.laborIntensityOfLandUsed = 0;
	}

	/**
	 * Instantiates a new agriculture product.
	 *
	 * @param name the name
	 * @param foodIntensityOfLandUsed the food intensity of land used
	 * @param costIntensityOfLandUsed the cost intensity of land used
	 * @param waterIntensityOfLandUsed the water intensity of land used
	 * @param laborIntensityOfLandUsed the labor intensity of land used
	 */
	private AgricultureProduct(String name, double foodIntensityOfLandUsed, 
			double costIntensityOfLandUsed, double waterIntensityOfLandUsed,
			double laborIntensityOfLandUsed) {
		this.name = name;
		this.foodIntensityOfLandUsed = foodIntensityOfLandUsed;
		this.costIntensityOfLandUsed = costIntensityOfLandUsed;
		this.waterIntensityOfLandUsed = waterIntensityOfLandUsed;
		this.laborIntensityOfLandUsed = laborIntensityOfLandUsed;
	}

	/**
	 * Gets the cost intensity of land used.
	 *
	 * @return the cost intensity of land used
	 */
	public double getCostIntensityOfLandUsed() {
		return this.costIntensityOfLandUsed;
	}
	
	/**
	 * Gets the food intensity of land used.
	 *
	 * @return the food intensity of land used
	 */
	public double getFoodIntensityOfLandUsed() {
		return this.foodIntensityOfLandUsed;
	}
	
	/**
	 * Gets the labor intensity of land used.
	 *
	 * @return the labor intensity of land used
	 */
	public double getLaborIntensityOfLandUsed() {
		return laborIntensityOfLandUsed;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the water intensity of land used.
	 *
	 * @return the water intensity of land used
	 */
	public double getWaterIntensityOfLandUsed() {
		return this.waterIntensityOfLandUsed;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
