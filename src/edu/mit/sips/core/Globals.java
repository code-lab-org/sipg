package edu.mit.sips.core;

/**
 * The Class Globals.
 */
public class Globals {
	private double initialFunds = 10000;								// SAR

	private double agricultureLaborParticipationRate = 0.50;			// -
	private double privateConsumptionFromFoodProduction = 0;			// SAR/GJ
	private double privateConsumptionFromFoodConsumption = 5000;		// SAR/GJ
	
	private double privateConsumptionFromWaterProduction = 0;			// SAR/m^3
	private double privateConsumptionFromWaterConsumption = 100;		// SAR/m^3
	
	private double privateConsumptionFromPetroleumProduction = 100;		// SAR/bbl
	private double electricalIntensityOfBurningPetroleum = 0.300;		// MWh/bbl
	
	private double privateConsumptionFromElectricityProduction = 0;		// SAR/MWh
	private double privateConsumptionFromElectricityConsumption = 2000;	// SAR/MWh
	/**
	 * Instantiates a new globals.
	 */
	public Globals() {
		
	}
	
	/**
	 * Gets the agriculture labor participation rate.
	 *
	 * @return the agriculture labor participation rate
	 */
	public double getAgricultureLaborParticipationRate() {
		return agricultureLaborParticipationRate;
	}

	/**
	 * Gets the private consumption from electricity consumption.
	 *
	 * @return the private consumption from electricity consumption
	 */
	public double getPrivateConsumptionFromElectricityConsumption() {
		return privateConsumptionFromElectricityConsumption;
	}

	/**
	 * Gets the private consumption from electricity production.
	 *
	 * @return the private consumption from electricity production
	 */
	public double getPrivateConsumptionFromElectricityProduction() {
		return privateConsumptionFromElectricityProduction;
	}
	
	/**
	 * Gets the private consumption from food consumption.
	 *
	 * @return the private consumption from food consumption
	 */
	public double getPrivateConsumptionFromFoodConsumption() {
		return privateConsumptionFromFoodConsumption;
	}
	
	/**
	 * Gets the private consumption from food production.
	 *
	 * @return the private consumption from food production
	 */
	public double getPrivateConsumptionFromFoodProduction() {
		return privateConsumptionFromFoodProduction;
	}
	
	/**
	 * Gets the private consumption from petroleum production.
	 *
	 * @return the private consumption from petroleum production
	 */
	public double getPrivateConsumptionFromPetroleumProduction() {
		return privateConsumptionFromPetroleumProduction;
	}

	/**
	 * Gets the private consumption from water consumption.
	 *
	 * @return the private consumption from water consumption
	 */
	public double getPrivateConsumptionFromWaterConsumption() {
		return privateConsumptionFromWaterConsumption;
	}

	/**
	 * Gets the private consumption from water production.
	 *
	 * @return the private consumption from water production
	 */
	public double getPrivateConsumptionFromWaterProduction() {
		return privateConsumptionFromWaterProduction;
	}

	/**
	 * Gets the electrical intensity of burning petroleum.
	 *
	 * @return the electrical intensity of burning petroleum
	 */
	public double getElectricalIntensityOfBurningPetroleum() {
		return electricalIntensityOfBurningPetroleum;
	}

	/**
	 * Gets the initial funds.
	 *
	 * @return the initial funds
	 */
	public double getInitialFunds() {
		return initialFunds;
	}

	/**
	 * Sets the agriculture labor participation rate.
	 *
	 * @param agricultureLaborParticipationRate the new agriculture labor participation rate
	 */
	public void setAgricultureLaborParticipationRate(
			double agricultureLaborParticipationRate) {
		this.agricultureLaborParticipationRate = agricultureLaborParticipationRate;
	}

	/**
	 * Sets the economic intensity of electricity consumption.
	 *
	 * @param economicIntensityOfElectricityConsumption the new economic intensity of electricity consumption
	 */
	public void setEconomicIntensityOfElectricityConsumption(
			double economicIntensityOfElectricityConsumption) {
		this.privateConsumptionFromElectricityConsumption = economicIntensityOfElectricityConsumption;
	}

	/**
	 * Sets the economic intensity of electricity production.
	 *
	 * @param economicIntensityOfElectricityProduction the new economic intensity of electricity production
	 */
	public void setEconomicIntensityOfElectricityProduction(
			double economicIntensityOfElectricityProduction) {
		this.privateConsumptionFromElectricityProduction = economicIntensityOfElectricityProduction;
	}

	/**
	 * Sets the economic intensity of food consumption.
	 *
	 * @param economicIntensityOfFoodConsumption the new economic intensity of food consumption
	 */
	public void setEconomicIntensityOfFoodConsumption(
			double economicIntensityOfFoodConsumption) {
		this.privateConsumptionFromFoodConsumption = economicIntensityOfFoodConsumption;
	}

	/**
	 * Sets the economic intensity of food production.
	 *
	 * @param economicIntensityOfFoodProduction the new economic intensity of food production
	 */
	public void setEconomicIntensityOfFoodProduction(
			double economicIntensityOfFoodProduction) {
		this.privateConsumptionFromFoodProduction = economicIntensityOfFoodProduction;
	}

	/**
	 * Sets the economic intensity of petroleum production.
	 *
	 * @param economicIntensityOfPetroleumProduction the new economic intensity of petroleum production
	 */
	public void setEconomicIntensityOfPetroleumProduction(
			double economicIntensityOfPetroleumProduction) {
		this.privateConsumptionFromPetroleumProduction = economicIntensityOfPetroleumProduction;
	}

	/**
	 * Sets the economic intensity of water consumption.
	 *
	 * @param economicIntensityOfWaterConsumption the new economic intensity of water consumption
	 */
	public void setEconomicIntensityOfWaterConsumption(
			double economicIntensityOfWaterConsumption) {
		this.privateConsumptionFromWaterConsumption = economicIntensityOfWaterConsumption;
	}

	/**
	 * Sets the economic intensity of water production.
	 *
	 * @param economicIntensityOfWaterProduction the new economic intensity of water production
	 */
	public void setEconomicIntensityOfWaterProduction(
			double economicIntensityOfWaterProduction) {
		this.privateConsumptionFromWaterProduction = economicIntensityOfWaterProduction;
	}

	/**
	 * Sets the electrical intensity of burning petroleum.
	 *
	 * @param electricalIntensityOfBurningPetroleum the new electrical intensity of burning petroleum
	 */
	public void setElectricalIntensityOfBurningPetroleum(
			double electricalIntensityOfBurningPetroleum) {
		this.electricalIntensityOfBurningPetroleum = electricalIntensityOfBurningPetroleum;
	}
	

	/**
	 * Sets the initial funds.
	 *
	 * @param initialFunds the new initial funds
	 */
	public void setInitialFunds(double initialFunds) {
		this.initialFunds = initialFunds;
	}
}
