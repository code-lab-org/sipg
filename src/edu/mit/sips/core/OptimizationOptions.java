package edu.mit.sips.core;

/**
 * The Class OptimizationOptions.
 */
public class OptimizationOptions {
	private double deltaDomesticWaterPrice, deltaAquiferWaterPrice, 
	deltaImportWaterPrice, deltaDomesticFoodPrice, deltaImportFoodPrice, 
	deltaExportFoodPrice, deltaDomesticOilPrice, deltaReservoirOilPrice, 
	deltaImportOilPrice, deltaExportOilPrice, deltaDomesticElectricityPrice;

	/**
	 * Instantiates a new optimization options.
	 */
	public OptimizationOptions() {
		
	}

	/**
	 * Gets the delta aquifer water price.
	 *
	 * @return the delta aquifer water price
	 */
	public double getDeltaAquiferWaterPrice() {
		return deltaAquiferWaterPrice;
	}

	/**
	 * Gets the delta domestic electricity price.
	 *
	 * @return the delta domestic electricity price
	 */
	public double getDeltaDomesticElectricityPrice() {
		return deltaDomesticElectricityPrice;
	}

	/**
	 * Gets the delta domestic food price.
	 *
	 * @return the delta domestic food price
	 */
	public double getDeltaDomesticFoodPrice() {
		return deltaDomesticFoodPrice;
	}

	/**
	 * Gets the delta domestic oil price.
	 *
	 * @return the delta domestic oil price
	 */
	public double getDeltaDomesticOilPrice() {
		return deltaDomesticOilPrice;
	}

	/**
	 * Gets the delta domestic water price.
	 *
	 * @return the delta domestic water price
	 */
	public double getDeltaDomesticWaterPrice() {
		return deltaDomesticWaterPrice;
	}

	/**
	 * Gets the delta export food price.
	 *
	 * @return the delta export food price
	 */
	public double getDeltaExportFoodPrice() {
		return deltaExportFoodPrice;
	}

	/**
	 * Gets the delta export oil price.
	 *
	 * @return the delta export oil price
	 */
	public double getDeltaExportOilPrice() {
		return deltaExportOilPrice;
	}

	/**
	 * Gets the delta import food price.
	 *
	 * @return the delta import food price
	 */
	public double getDeltaImportFoodPrice() {
		return deltaImportFoodPrice;
	}

	/**
	 * Gets the delta import oil price.
	 *
	 * @return the delta import oil price
	 */
	public double getDeltaImportOilPrice() {
		return deltaImportOilPrice;
	}

	/**
	 * Gets the delta import water price.
	 *
	 * @return the delta import water price
	 */
	public double getDeltaImportWaterPrice() {
		return deltaImportWaterPrice;
	}

	/**
	 * Gets the delta reservoir oil price.
	 *
	 * @return the delta reservoir oil price
	 */
	public double getDeltaReservoirOilPrice() {
		return deltaReservoirOilPrice;
	}

	/**
	 * Sets the delta aquifer water price.
	 *
	 * @param deltaAquiferWaterPrice the new delta aquifer water price
	 */
	public void setDeltaAquiferWaterPrice(double deltaAquiferWaterPrice) {
		this.deltaAquiferWaterPrice = deltaAquiferWaterPrice;
	}

	/**
	 * Sets the delta domestic electricity price.
	 *
	 * @param deltaDomesticElectricityPrice the new delta domestic electricity price
	 */
	public void setDeltaDomesticElectricityPrice(
			double deltaDomesticElectricityPrice) {
		this.deltaDomesticElectricityPrice = deltaDomesticElectricityPrice;
	}

	/**
	 * Sets the delta domestic food price.
	 *
	 * @param deltaDomesticFoodPrice the new delta domestic food price
	 */
	public void setDeltaDomesticFoodPrice(double deltaDomesticFoodPrice) {
		this.deltaDomesticFoodPrice = deltaDomesticFoodPrice;
	}

	/**
	 * Sets the delta domestic oil price.
	 *
	 * @param deltaDomesticOilPrice the new delta domestic oil price
	 */
	public void setDeltaDomesticOilPrice(double deltaDomesticOilPrice) {
		this.deltaDomesticOilPrice = deltaDomesticOilPrice;
	}

	/**
	 * Sets the delta domestic water price.
	 *
	 * @param deltaDomesticWaterPrice the new delta domestic water price
	 */
	public void setDeltaDomesticWaterPrice(double deltaDomesticWaterPrice) {
		this.deltaDomesticWaterPrice = deltaDomesticWaterPrice;
	}

	/**
	 * Sets the delta export food price.
	 *
	 * @param deltaExportFoodPrice the new delta export food price
	 */
	public void setDeltaExportFoodPrice(double deltaExportFoodPrice) {
		this.deltaExportFoodPrice = deltaExportFoodPrice;
	}

	/**
	 * Sets the delta export oil price.
	 *
	 * @param deltaExportOilPrice the new delta export oil price
	 */
	public void setDeltaExportOilPrice(double deltaExportOilPrice) {
		this.deltaExportOilPrice = deltaExportOilPrice;
	}

	/**
	 * Sets the delta import food price.
	 *
	 * @param deltaImportFoodPrice the new delta import food price
	 */
	public void setDeltaImportFoodPrice(double deltaImportFoodPrice) {
		this.deltaImportFoodPrice = deltaImportFoodPrice;
	}

	/**
	 * Sets the delta import oil price.
	 *
	 * @param deltaImportOilPrice the new delta import oil price
	 */
	public void setDeltaImportOilPrice(double deltaImportOilPrice) {
		this.deltaImportOilPrice = deltaImportOilPrice;
	}

	/**
	 * Sets the delta import water price.
	 *
	 * @param deltaImportWaterPrice the new delta import water price
	 */
	public void setDeltaImportWaterPrice(double deltaImportWaterPrice) {
		this.deltaImportWaterPrice = deltaImportWaterPrice;
	}
	
	/**
	 * Sets the delta reservoir oil price.
	 *
	 * @param deltaReservoirOilPrice the new delta reservoir oil price
	 */
	public void setDeltaReservoirOilPrice(double deltaReservoirOilPrice) {
		this.deltaReservoirOilPrice = deltaReservoirOilPrice;
	}
}
