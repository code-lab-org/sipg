package edu.mit.sips.core.agriculture;

/**
 * The Enum AgricultureProduct.
 */
public enum AgricultureProduct {
	NONE,
	GRAZING("Lifestock Grazing", 		15, 	50, 	300, 	0.5),
	INDIGINOUS_CROP("Indiginous Crop", 	400, 	15000, 	5000, 	20),
	GRAINS("Grains", 					2000, 	30000, 	35000,	10),
	MODIFIED_GRAINS("Modified Grains", 	2000, 	52500, 	30000,	10);
	
	private final String name;
	private final double foodIntensityOfLandUsed; 		// GJ/km^2
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
