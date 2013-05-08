package edu.mit.sips.core;

/**
 * The Class Globals.
 */
public class Globals {
	private double initialFunds = 10000;								// SAR

	private double agricultureLaborParticipationRate = 0.50;			// -
	private double foodDomesticPrice = 200;								// SAR/GJ
	private double foodImportPrice = 250;								// SAR/GJ
	private double foodExportPrice = 200;								// SAR/GJ
	private double minFoodDemandPerCapita = 2;							// GJ/person
	private double maxFoodDemandPerCapita = 4.5;						// GJ/person
	private double econProductMinFoodDemand = 0;						// SAR/SAR
	private double econProductMaxFoodDemand = 100000;					// SAR/SAR
	private double privateConsumptionFromFoodProduction = 50;			// SAR/GJ
	private double privateConsumptionFromFoodConsumption = 500;			// SAR/GJ
	
	private double waterImportPrice = 40;								// SAR/m^3
	private double waterDomesticPrice = 6.;								// SAR/m^3
	private double privateConsumptionFromWaterProduction = 0;			// SAR/m^3
	private double privateConsumptionFromWaterConsumption = 0;			// SAR/m^3
	private double minWaterDemandPerCapita = 10;						// m^3
	private double maxWaterDemandPerCapita = 100;						// m^3
	
	private double petroleumDomesticPrice = 50.;						// SAR/bbl
	private double petroleumExportPrice = 300;							// SAR/bbl
	private double petroleumImportPrice = 375;							// SAR/bbl
	private double privateConsumptionFromPetroleumProduction = 0;		// SAR/bbl
	private double electricalIntensityOfBurningPetroleum = 0.300;		// MWh/bbl
	
	private double electricityDomesticPrice = 375.;						// SAR/MWh
	private double minElectricityDemandPerCapita = 1;					// MWh/person
	private double maxElectricityDemandPerCapita = 10;					// MWh/person
	private double econProductMinElectricityDemand = 0;					// SAR/person
	private double econProductMaxElectricityDemand = 100000;			// SAR/person
	private double privateConsumptionFromElectricityProduction = 0;		// SAR/MWh
	private double privateConsumptionFromElectricityConsumption = 0.5;	// SAR/MWh
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
	 * Gets the econ product max electricity demand.
	 *
	 * @return the econ product max electricity demand
	 */
	public double getEconProductMaxElectricityDemand() {
		return econProductMaxElectricityDemand;
	}

	/**
	 * Gets the econ product max food demand.
	 *
	 * @return the econ product max food demand
	 */
	public double getEconProductMaxFoodDemand() {
		return econProductMaxFoodDemand;
	}

	/**
	 * Gets the econ product min electricity demand.
	 *
	 * @return the econ product min electricity demand
	 */
	public double getEconProductMinElectricityDemand() {
		return econProductMinElectricityDemand;
	}

	/**
	 * Gets the econ product min food demand.
	 *
	 * @return the econ product min food demand
	 */
	public double getEconProductMinFoodDemand() {
		return econProductMinFoodDemand;
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
	 * Gets the electricity demand.
	 *
	 * @param economicProduct the economic product
	 * @return the electricity demand
	 */
	public double getElectricityDemand(double economicProduct) {
		return new LinearBoundedDemandModel(econProductMinElectricityDemand, 
				minElectricityDemandPerCapita, econProductMaxElectricityDemand, 
				maxElectricityDemandPerCapita).getDemand(economicProduct);
	}

	/**
	 * Gets the electricity domestic price.
	 *
	 * @return the electricity domestic price
	 */
	public double getElectricityDomesticPrice() {
		return electricityDomesticPrice;
	}

	/**
	 * Gets the food demand.
	 *
	 * @param economicProduct the economic product
	 * @return the food demand
	 */
	public double getFoodDemand(double economicProduct) {
		return new LinearBoundedDemandModel(econProductMinFoodDemand, 
				minFoodDemandPerCapita, econProductMaxFoodDemand, 
				maxFoodDemandPerCapita).getDemand(economicProduct);
	}

	/**
	 * Gets the food domestic price.
	 *
	 * @return the food domestic price
	 */
	public double getFoodDomesticPrice() {
		return foodDomesticPrice;
	}

	/**
	 * Gets the food export price.
	 *
	 * @return the food export price
	 */
	public double getFoodExportPrice() {
		return foodExportPrice;
	}

	/**
	 * Gets the food import price.
	 *
	 * @return the food import price
	 */
	public double getFoodImportPrice() {
		return foodImportPrice;
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
	 * Gets the max electricity demand per capita.
	 *
	 * @return the max electricity demand per capita
	 */
	public double getMaxElectricityDemandPerCapita() {
		return maxElectricityDemandPerCapita;
	}

	/**
	 * Gets the max food demand per capita.
	 *
	 * @return the max food demand per capita
	 */
	public double getMaxFoodDemandPerCapita() {
		return maxFoodDemandPerCapita;
	}

	/**
	 * Gets the max water demand per capita.
	 *
	 * @return the max water demand per capita
	 */
	public double getMaxWaterDemandPerCapita() {
		return maxWaterDemandPerCapita;
	}

	/**
	 * Gets the min electricity demand per capita.
	 *
	 * @return the min electricity demand per capita
	 */
	public double getMinElectricityDemandPerCapita() {
		return minElectricityDemandPerCapita;
	}

	/**
	 * Gets the min food demand per capita.
	 *
	 * @return the min food demand per capita
	 */
	public double getMinFoodDemandPerCapita() {
		return minFoodDemandPerCapita;
	}

	/**
	 * Gets the min water demand per capita.
	 *
	 * @return the min water demand per capita
	 */
	public double getMinWaterDemandPerCapita() {
		return minWaterDemandPerCapita;
	}

	/**
	 * Gets the petroleum domestic price.
	 *
	 * @return the petroleum domestic price
	 */
	public double getPetroleumDomesticPrice() {
		return petroleumDomesticPrice;
	}

	/**
	 * Gets the petroleum export price.
	 *
	 * @return the petroleum export price
	 */
	public double getPetroleumExportPrice() {
		return petroleumExportPrice;
	}

	/**
	 * Gets the petroleum import price.
	 *
	 * @return the petroleum import price
	 */
	public double getPetroleumImportPrice() {
		return petroleumImportPrice;
	}

	/**
	 * Gets the water domestic price.
	 *
	 * @return the water domestic price
	 */
	public double getWaterDomesticPrice() {
		return waterDomesticPrice;
	}

	/**
	 * Gets the water import price.
	 *
	 * @return the water import price
	 */
	public double getWaterImportPrice() {
		return waterImportPrice;
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
	 * Sets the econ product max electricity demand.
	 *
	 * @param econProductMaxElectricityDemand the new econ product max electricity demand
	 */
	public void setEconProductMaxElectricityDemand(
			double econProductMaxElectricityDemand) {
		this.econProductMaxElectricityDemand = econProductMaxElectricityDemand;
	}

	/**
	 * Sets the econ product max food demand.
	 *
	 * @param econProductMaxFoodDemand the new econ product max food demand
	 */
	public void setEconProductMaxFoodDemand(double econProductMaxFoodDemand) {
		this.econProductMaxFoodDemand = econProductMaxFoodDemand;
	}

	/**
	 * Sets the econ product min electricity demand.
	 *
	 * @param econProductMinElectricityDemand the new econ product min electricity demand
	 */
	public void setEconProductMinElectricityDemand(
			double econProductMinElectricityDemand) {
		this.econProductMinElectricityDemand = econProductMinElectricityDemand;
	}
	/**
	 * Sets the econ product min food demand.
	 *
	 * @param econProductMinFoodDemand the new econ product min food demand
	 */
	public void setEconProductMinFoodDemand(double econProductMinFoodDemand) {
		this.econProductMinFoodDemand = econProductMinFoodDemand;
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
	 * Sets the electricity domestic price.
	 *
	 * @param electricityDomesticPrice the new electricity domestic price
	 */
	public void setElectricityDomesticPrice(double electricityDomesticPrice) {
		this.electricityDomesticPrice = electricityDomesticPrice;
	}

	/**
	 * Sets the food domestic price.
	 *
	 * @param foodDomesticPrice the new food domestic price
	 */
	public void setFoodDomesticPrice(double foodDomesticPrice) {
		this.foodDomesticPrice = foodDomesticPrice;
	}
	/**
	 * Sets the food export price.
	 *
	 * @param foodExportPrice the new food export price
	 */
	public void setFoodExportPrice(double foodExportPrice) {
		this.foodExportPrice = foodExportPrice;
	}
	/**
	 * Sets the food import price.
	 *
	 * @param foodImportPrice the new food import price
	 */
	public void setFoodImportPrice(double foodImportPrice) {
		this.foodImportPrice = foodImportPrice;
	}
	/**
	 * Sets the initial funds.
	 *
	 * @param initialFunds the new initial funds
	 */
	public void setInitialFunds(double initialFunds) {
		this.initialFunds = initialFunds;
	}
	/**
	 * Sets the max electricity demand per capita.
	 *
	 * @param maxElectricityDemandPerCapita the new max electricity demand per capita
	 */
	public void setMaxElectricityDemandPerCapita(
			double maxElectricityDemandPerCapita) {
		this.maxElectricityDemandPerCapita = maxElectricityDemandPerCapita;
	}
	/**
	 * Sets the max food demand per capita.
	 *
	 * @param maxFoodDemandPerCapita the new max food demand per capita
	 */
	public void setMaxFoodDemandPerCapita(double maxFoodDemandPerCapita) {
		this.maxFoodDemandPerCapita = maxFoodDemandPerCapita;
	}
	
	/**
	 * Sets the max water demand per capita.
	 *
	 * @param maxWaterDemandPerCapita the new max water demand per capita
	 */
	public void setMaxWaterDemandPerCapita(double maxWaterDemandPerCapita) {
		this.maxWaterDemandPerCapita = maxWaterDemandPerCapita;
	}

	/**
	 * Sets the min electricity demand per capita.
	 *
	 * @param minElectricityDemandPerCapita the new min electricity demand per capita
	 */
	public void setMinElectricityDemandPerCapita(
			double minElectricityDemandPerCapita) {
		this.minElectricityDemandPerCapita = minElectricityDemandPerCapita;
	}

	/**
	 * Sets the min food demand per capita.
	 *
	 * @param minFoodDemandPerCapita the new min food demand per capita
	 */
	public void setMinFoodDemandPerCapita(double minFoodDemandPerCapita) {
		this.minFoodDemandPerCapita = minFoodDemandPerCapita;
	}

	/**
	 * Sets the min water demand per capita.
	 *
	 * @param minWaterDemandPerCapita the new min water demand per capita
	 */
	public void setMinWaterDemandPerCapita(double minWaterDemandPerCapita) {
		this.minWaterDemandPerCapita = minWaterDemandPerCapita;
	}

	/**
	 * Sets the petroleum domestic price.
	 *
	 * @param petroleumDomesticPrice the new petroleum domestic price
	 */
	public void setPetroleumDomesticPrice(double petroleumDomesticPrice) {
		this.petroleumDomesticPrice = petroleumDomesticPrice;
	}

	/**
	 * Sets the petroleum export price.
	 *
	 * @param petroleumExportPrice the new petroleum export price
	 */
	public void setPetroleumExportPrice(double petroleumExportPrice) {
		this.petroleumExportPrice = petroleumExportPrice;
	}

	/**
	 * Sets the petroleum import price.
	 *
	 * @param petroleumImportPrice the new petroleum import price
	 */
	public void setPetroleumImportPrice(double petroleumImportPrice) {
		this.petroleumImportPrice = petroleumImportPrice;
	}

	/**
	 * Sets the water domestic price.
	 *
	 * @param waterDomesticPrice the new water domestic price
	 */
	public void setWaterDomesticPrice(double waterDomesticPrice) {
		this.waterDomesticPrice = waterDomesticPrice;
	}

	/**
	 * Sets the water import price.
	 *
	 * @param waterImportPrice the new water import price
	 */
	public void setWaterImportPrice(double waterImportPrice) {
		this.waterImportPrice = waterImportPrice;
	}
}
